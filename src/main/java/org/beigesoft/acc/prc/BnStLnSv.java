/*
BSD 2-Clause License

Copyright (c) 2019, Beigesoft™
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.beigesoft.acc.prc;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.math.BigDecimal;
import java.text.DateFormat;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.AOrId;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.srv.II18n;
import org.beigesoft.srv.ISrvDt;
import org.beigesoft.acc.mdl.EBnEnrSt;
import org.beigesoft.acc.mdl.EBnEnrRsTy;
import org.beigesoft.acc.mdl.EBnEnrRsAc;
import org.beigesoft.acc.mdlb.ADoc;
import org.beigesoft.acc.mdlp.Entr;
import org.beigesoft.acc.mdlp.BnStLn;
import org.beigesoft.acc.mdlp.SalInv;
import org.beigesoft.acc.mdlp.PurInv;
import org.beigesoft.acc.mdlp.PrepTo;
import org.beigesoft.acc.mdlp.PrepFr;
import org.beigesoft.acc.mdlp.PaymTo;
import org.beigesoft.acc.mdlp.PaymFr;
import org.beigesoft.acc.mdlp.Acnt;
import org.beigesoft.acc.mdlp.DbCr;
import org.beigesoft.acc.srv.ISrEntr;
import org.beigesoft.acc.srv.ISrToPa;
import org.beigesoft.acc.srv.IRvInvLn;

/**
 * <p>Service that saves move item line into DB.</p>
 *
 * @author Yury Demidenko
 */
public class BnStLnSv implements IPrcEnt<BnStLn, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>I18N service.</p>
   */
  private II18n i18n;

  /**
   * <p>Entries service.</p>
   **/
  private ISrEntr srEntr;

  /**
   * <p>Date service.</p>
   **/
  private ISrvDt srvDt;

  /**
   * <p>Total paym service.</p>
   **/
  private ISrToPa srToPa;

  /**
   * <p>Purchase line reverser, just holds paym/prep class.</p>
   **/
  private IRvInvLn<PurInv, ?> rvPuLn;

  /**
   * <p>Sales line reverser, just holds paym/prep class.</p>
   **/
  private IRvInvLn<SalInv, ?> rvSaLn;

  /**
   * <p>Process that pertieves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final BnStLn process(final Map<String, Object> pRvs, final BnStLn pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    vs.put("BnkStmdpLv", 3);
    getOrm().refrEnt(pRvs, vs, pEnt);
    long owVrWs = Long.parseLong(pRqDt.getParam("owVr"));
    if (owVrWs != pEnt.getOwnr().getVer()) {
      throw new ExcCode(IOrm.DRTREAD, "dirty_read");
    }
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    if (!pEnt.getDbOr().equals(getOrm().getDbId())) {
      throw new ExcCode(ExcCode.SPAM, "can_not_change_foreign_src");
    }
    if (pEnt.getRsAc() != null) {
      throw new ExcCode(ExcCode.SPAM,
        "Attempt to edit completed bank statement line!");
    }
    if (pEnt.getAmnt().compareTo(BigDecimal.ZERO) == 0) {
      throw new ExcCode(ExcCode.WRPR, "amount_is_zero");
    }
    DateFormat dtFrm = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
      DateFormat.SHORT, new Locale(cpf.getLngDef().getIid()));
    String prepId = pRqDt.getParam("prepId");
    if (prepId != null && !"".equals(prepId)) {
      makePrepMatchingReversed(pRvs, vs, prepId, pEnt, dtFrm, cpf);
    } else {
      String paymId = pRqDt.getParam("paymId");
      if (paymId != null && !"".equals(paymId)) {
        makePaymMatchingReversed(pRvs, vs, paymId, pEnt, dtFrm, cpf);
      } else {
        String entrId = pRqDt.getParam("entrId");
        if (entrId != null && !"".equals(entrId)) {
          makeAccentryMatchingReversed(pRvs, vs, entrId, pEnt, dtFrm, cpf);
        } else {
          String adjDocType = pRqDt.getParam("adjDocType");
          if (adjDocType != null && !"".equals(adjDocType)
            && !"-".equals(adjDocType)) {
            if (EBnEnrSt.VOIDED.equals(pEnt.getStas())) {
              throw new ExcCode(ExcCode.SPAM, "can_not_create_for_voided");
            }
            if ("1".equals(adjDocType)) {
              createPrep(pRvs, vs, pEnt, dtFrm, cpf, pRqDt);
            } else if ("2".equals(adjDocType)) {
              createPaym(pRvs, vs, pEnt, dtFrm, cpf, pRqDt);
            } else {
              createAccentry(pRvs, vs, pEnt, dtFrm, cpf, pRqDt);
            }
          } else {
            throw new ExcCode(ExcCode.WR, "Wrong parameters!");
          }
        }
      }
    }
    String[] upFds = new String[] {"dsSt", "rsAc", "rsDs", "rsRcId",
      "rsRcTy", "ver"};
    Arrays.sort(upFds);
    vs.put("ndFds", upFds);
    this.orm.update(pRvs, vs, pEnt); vs.clear();
    pRvs.put("msgSuc", "update_ok");
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setOwnr(pEnt.getOwnr());
    return null;
  }


  /**
   * <p>Creates paym.</p>
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars
   * @param pBsl BSL
   * @param pDtFrm Date Formatter
   * @param pCpf language
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  public final void createPaym(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final BnStLn pBsl, final DateFormat pDtFrm,
      final CmnPrf pCpf, final IReqDt pRqDt) throws Exception {
    BigDecimal toFc;
    Acnt accCash;
    Date dat;
    String forTotStr = pRqDt.getParam("toFc");
    if (forTotStr == null || "".equals(forTotStr)) {
      toFc = BigDecimal.ZERO;
    } else {
      if (pCpf.getDcSpv() != null) {
        forTotStr = forTotStr.replace(pCpf.getDcGrSpv(), "")
          .replace(pCpf.getDcSpv(), ".");
      }
      toFc = new BigDecimal(forTotStr);
    }
    String accCashStr = pRqDt.getParam("accCash");
    accCash = new Acnt();
    accCash.setIid(accCashStr);
    getOrm().refrEnt(pRvs, pVs, accCash);
    if (accCash.getIid() == null) {
      throw new Exception("cant_find_account");
    }
    dat = this.srvDt.from8601DateTime(pRqDt.getParam("dat"));
    EBnEnrRsTy rsRcTy = null;
    EBnEnrRsAc rsAc = EBnEnrRsAc.CREATE;
    String rsDs = null;
    Long rsRcId = null;
    if (pBsl.getAmnt().compareTo(BigDecimal.ZERO) > 0) {
      SalInv inv;
      String invStr = pRqDt.getParam("invoice");
      inv = new SalInv();
      inv.setIid(Long.parseLong(invStr));
      getOrm().refrEnt(pRvs, pVs, inv);
      if (inv.getIid() == null) {
        throw new Exception("Can't find debtor invoice# " + invStr);
      }
      rsRcTy = EBnEnrRsTy.PREPFR;
      PaymFr pay = new PaymFr();
      pay.setDbOr(getOrm().getDbId());
      pay.setDat(dat);
      pay.setInv(inv);
      pay.setAcc(accCash);
      pay.setSaNm(pBsl.getOwnr().getBnka().getNme());
      pay.setSaId(pBsl.getOwnr().getBnka().getIid());
      pay.setSaTy(1003);
      pay.setTot(pBsl.getAmnt().abs());
      pay.setToFc(toFc);
      pay.setDscr(makeDescrForCreated(pBsl, pDtFrm, pCpf));
      getOrm().insIdLn(pRvs, pVs, pay);
      this.srEntr.mkEntrs(pRvs, pay);
      this.srToPa.mkToPa(pRvs, pay.getInv(), this.rvSaLn);
      String[] fdsIa = new String[] {"pdsc", "toPa", "paFc", "ver"};
      Arrays.sort(fdsIa);
      pVs.put("ndFds", fdsIa);
      this.orm.update(pRvs, pVs, pay.getInv()); pVs.clear();
      rsRcId = pay.getIid();
      rsDs = makeBslResDescr(rsAc, pDtFrm, pay, pay.getDat(), pCpf);
    } else {
      PurInv inv;
      String invStr = pRqDt.getParam("invoice");
      inv = new PurInv();
      inv.setIid(Long.parseLong(invStr));
      getOrm().refrEnt(pRvs, pVs, inv);
      if (inv.getIid() == null) {
        throw new Exception("Can't find debtor invoice# " + invStr);
      }
      rsRcTy = EBnEnrRsTy.PREPTO;
      PaymTo pay = new PaymTo();
      pay.setDbOr(getOrm().getDbId());
      pay.setDat(dat);
      pay.setInv(inv);
      pay.setAcc(accCash);
      pay.setSaNm(pBsl.getOwnr().getBnka().getNme());
      pay.setSaId(pBsl.getOwnr().getBnka().getIid());
      pay.setSaTy(1003);
      pay.setTot(pBsl.getAmnt().abs());
      pay.setToFc(toFc);
      pay.setDscr(makeDescrForCreated(pBsl, pDtFrm, pCpf));
      getOrm().insIdLn(pRvs, pVs, pay);
      this.srEntr.mkEntrs(pRvs, pay);
      this.srToPa.mkToPa(pRvs, pay.getInv(), this.rvPuLn);
      String[] fdsIa = new String[] {"pdsc", "toPa", "paFc", "ver"};
      Arrays.sort(fdsIa);
      pVs.put("ndFds", fdsIa);
      this.orm.update(pRvs, pVs, pay.getInv()); pVs.clear();
      rsRcId = pay.getIid();
      rsDs = makeBslResDescr(rsAc, pDtFrm, pay,
        pay.getDat(), pCpf);
    }
    pBsl.setRsAc(rsAc);
    pBsl.setRsRcTy(rsRcTy);
    pBsl.setRsRcId(rsRcId);
    pBsl.setRsDs(rsDs);
  }

  /**
   * <p>Creates prep.</p>
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars
   * @param pBsl BSL
   * @param pDtFrm Date Formatter
   * @param pCpf language
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  public final void createPrep(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final BnStLn pBsl, final DateFormat pDtFrm,
      final CmnPrf pCpf, final IReqDt pRqDt) throws Exception {
    BigDecimal toFc;
    DbCr dc;
    Acnt accCash;
    Date dat;
    String forTotStr = pRqDt.getParam("toFc");
    if (forTotStr == null || "".equals(forTotStr)) {
      toFc = BigDecimal.ZERO;
    } else {
      if (pCpf.getDcSpv() != null) {
        forTotStr = forTotStr.replace(pCpf.getDcGrSpv(), "")
          .replace(pCpf.getDcSpv(), ".");
      }
      toFc = new BigDecimal(forTotStr);
    }
    String dcIdStr = pRqDt.getParam("dbcr");
    dc = new DbCr();
    dc.setIid(Long.parseLong(dcIdStr));
    getOrm().refrEnt(pRvs, pVs, dc);
    if (dc.getIid() == null) {
      throw new Exception("cant_find_debtor_creditor");
    }
    String accCashStr = pRqDt.getParam("accCash");
    accCash = new Acnt();
    accCash.setIid(accCashStr);
    getOrm().refrEnt(pRvs, pVs, accCash);
    if (accCash.getIid() == null) {
      throw new Exception("cant_find_account");
    }
    dat = this.srvDt.from8601DateTime(pRqDt.getParam("dat"));
    EBnEnrRsTy rsRcTy = null;
    EBnEnrRsAc rsAc = EBnEnrRsAc.CREATE;
    String rsDs = null;
    Long rsRcId = null;
    if (pBsl.getAmnt().compareTo(BigDecimal.ZERO) > 0) {
      rsRcTy = EBnEnrRsTy.PREPFR;
      PrepFr prep = new PrepFr();
      prep.setDbOr(getOrm().getDbId());
      prep.setDat(dat);
      prep.setDbcr(dc);
      prep.setAcc(accCash);
      prep.setSaNm(pBsl.getOwnr().getBnka().getNme());
      prep.setSaId(pBsl.getOwnr().getBnka().getIid());
      prep.setSaTy(1003);
      prep.setTot(pBsl.getAmnt().abs());
      prep.setToFc(toFc);
      prep.setDscr(makeDescrForCreated(pBsl, pDtFrm, pCpf));
      getOrm().insIdLn(pRvs, pVs, prep);
      this.srEntr.mkEntrs(pRvs, prep);
      rsRcId = prep.getIid();
      rsDs = makeBslResDescr(rsAc, pDtFrm, prep, prep.getDat(), pCpf);
    } else {
      rsRcTy = EBnEnrRsTy.PREPTO;
      PrepTo prep = new PrepTo();
      prep.setDbOr(getOrm().getDbId());
      prep.setDbcr(dc);
      prep.setDat(dat);
      prep.setAcc(accCash);
      prep.setSaNm(pBsl.getOwnr().getBnka().getNme());
      prep.setSaId(pBsl.getOwnr().getBnka().getIid());
      prep.setSaTy(1003);
      prep.setTot(pBsl.getAmnt().abs());
      prep.setToFc(toFc);
      prep.setDscr(makeDescrForCreated(pBsl, pDtFrm, pCpf));
      getOrm().insIdLn(pRvs, pVs, prep);
      this.srEntr.mkEntrs(pRvs, prep);
      rsRcId = prep.getIid();
      rsDs = makeBslResDescr(rsAc, pDtFrm, prep, prep.getDat(), pCpf);
    }
    pBsl.setRsAc(rsAc);
    pBsl.setRsRcTy(rsRcTy);
    pBsl.setRsRcId(rsRcId);
    pBsl.setRsDs(rsDs);
  }

  /**
   * <p>Create accentry.</p>
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars
   * @param pBsl BSL
   * @param pDtFrm Date Formatter
   * @param pCpf language
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  public final void createAccentry(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final BnStLn pBsl, final DateFormat pDtFrm,
      final CmnPrf pCpf, final IReqDt pRqDt) throws Exception {
    Acnt accCash;
    Acnt corAcc;
    String subcorAccNm = null;
    Long subcorAccId = null;
    Date dat;
    String accCashStr = pRqDt.getParam("accCash");
    accCash = new Acnt();
    accCash.setIid(accCashStr);
    getOrm().refrEnt(pRvs, pVs, accCash);
    if (accCash.getIid() == null) {
      throw new Exception("cant_find_account");
    }
    String corAccStr = pRqDt.getParam("corAcc");
    corAcc = new Acnt();
    corAcc.setIid(corAccStr);
    getOrm().refrEnt(pRvs, pVs, corAcc);
    if (corAcc == null) {
      throw new Exception("cant_find_account");
    }
    if (corAcc.getSaTy() != null) {
      subcorAccNm = pRqDt.getParam("subcorAccNm");
      String subcorAccIdStr = pRqDt.getParam("subcorAccId");
      subcorAccId = Long.parseLong(subcorAccIdStr);
    }
    dat = this.srvDt.from8601DateTime(pRqDt.getParam("dat"));
    EBnEnrRsTy rsRcTy = EBnEnrRsTy.ACC_ENTRY;
    EBnEnrRsAc rsAc = EBnEnrRsAc.CREATE;
    String rsDs = null;
    Long rsRcId = null;
    Entr entr = new Entr();
    if (pBsl.getAmnt().compareTo(BigDecimal.ZERO) > 0) {
      entr.setAcDb(accCash);
      entr.setSadNm(pBsl.getOwnr().getBnka().getNme());
      entr.setSadId(pBsl.getOwnr().getBnka().getIid());
      entr.setSadTy(1003);
      entr.setAcCr(corAcc);
      entr.setSacNm(subcorAccNm);
      entr.setSacId(subcorAccId);
      entr.setSacTy(corAcc.getSaTy());
    } else {
      entr.setAcDb(corAcc);
      entr.setSadNm(subcorAccNm);
      entr.setSadId(subcorAccId);
      entr.setSadTy(corAcc.getSaTy());
      entr.setAcCr(accCash);
      entr.setSacNm(pBsl.getOwnr().getBnka().getNme());
      entr.setSacId(pBsl.getOwnr().getBnka().getIid());
      entr.setSacTy(1003);
    }
    entr.setSrTy(pBsl.cnsTy());
    entr.setSrId(pBsl.getIid());
    entr.setDbOr(pBsl.getDbOr());
    entr.setDat(dat);
    entr.setDebt(pBsl.getAmnt().abs());
    entr.setCred(entr.getDebt());
    entr.setDscr(makeDescrForCreated(pBsl, pDtFrm, pCpf));
    getOrm().insIdLn(pRvs, pVs, entr);
    rsRcId = entr.getIid();
    rsDs = makeBslResDescr(rsAc, pDtFrm, entr, entr.getDat(), pCpf);
    pBsl.setRsAc(rsAc);
    pBsl.setRsRcTy(rsRcTy);
    pBsl.setRsRcId(rsRcId);
    pBsl.setRsDs(rsDs);
  }

  /**
   * <p>Makes accentry matching or revd.</p>
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars
   * @param pEntryId Accentry Id
   * @param pBsl BSL
   * @param pDtFrm Date Formatter
   * @param pCpf language
   * @throws Exception - an exception
   **/
  public final void makeAccentryMatchingReversed(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final String pEntryId,
      final BnStLn pBsl, final DateFormat pDtFrm,
        final CmnPrf pCpf) throws Exception {
    EBnEnrRsTy rsRcTy = EBnEnrRsTy.ACC_ENTRY;
    EBnEnrRsAc rsAc = null;
    String rsDs = null;
    Long rsRcId = null;
    Entr entr = new Entr();
    entr.setIid(Long.parseLong(pEntryId));
    getOrm().refrEnt(pRvs, pVs, entr);
    if (entr.getIid() == null) {
      throw new ExcCode(ExcCode.WRPR,
        "cant_found_accentry");
    }
    if (entr.getSrTy() == 1010) {
      throw new ExcCode(ExcCode.WRPR, "AlreadyDone");
    }
    if (pBsl.getAmnt().compareTo(BigDecimal.ZERO) > 0 && pBsl.getAmnt()
    .compareTo(entr.getDebt()) != 0 && entr.getSadTy() != 1003
  && !pBsl.getOwnr().getBnka().getIid().equals(entr.getSadId())) {
      throw new ExcCode(ExcCode.WRPR, "record_is_not_matching");
    } else if (pBsl.getAmnt().compareTo(BigDecimal.ZERO) < 0 && pBsl.getAmnt()
    .abs().compareTo(entr.getCred()) != 0 && entr.getSacTy() != 1003
  && !pBsl.getOwnr().getBnka().getIid().equals(entr.getSacId())) {
      throw new ExcCode(ExcCode.WRPR, "record_is_not_matching");
    }
    if (EBnEnrSt.VOIDED.equals(pBsl.getStas())) {
      if (!entr.getDbOr().equals(getOrm().getDbId())) {
        throw new ExcCode(ExcCode.SPAM, "can_not_change_foreign_src");
      }
      rsAc = EBnEnrRsAc.CREATE;
      Entr revd = entr;
      entr = new Entr();
      entr.setSrTy(pBsl.cnsTy());
      entr.setSrId(pBsl.getIid());
      entr.setDbOr(revd.getDbOr());
      entr.setRvId(revd.getIid());
      entr.setDat(new Date(revd.getDat().getTime() + 1));
      entr.setAcDb(revd.getAcDb());
      entr.setSadNm(revd.getSadNm());
      entr.setSadId(revd.getSadId());
      entr.setSadTy(revd.getSadTy());
      entr.setDebt(revd.getDebt().negate());
      entr.setAcCr(revd.getAcCr());
      entr.setSacNm(revd.getSacNm());
      entr.setSacId(revd.getSacId());
      entr.setSacTy(revd.getSacTy());
      entr.setCred(revd.getCred().negate());
      entr.setDscr(makeDescrForCreated(pBsl, pDtFrm, pCpf)
        + " " + getI18n().getMsg("revd_n", pCpf.getLngDef().getIid())
          + entr.getDbOr() + "-" + entr.getRvId());
      getOrm().insIdLn(pRvs, pVs, entr);
      String oldDesr = "";
      if (revd.getDscr() != null) {
        oldDesr = revd.getDscr();
      }
      revd.setDscr(oldDesr + " " + getI18n()
        .getMsg("reversing_n", pCpf.getLngDef().getIid()) + entr.getDbOr() + "-"
          + entr.getIid());
      revd.setRvId(entr.getIid());
      getOrm().update(pRvs, pVs, revd);
    } else {
      rsAc = EBnEnrRsAc.MATCH;
    }
    rsRcId = entr.getIid();
    rsDs = makeBslResDescr(rsAc, pDtFrm, entr, entr.getDat(), pCpf);
    pBsl.setRsAc(rsAc);
    pBsl.setRsRcTy(rsRcTy);
    pBsl.setRsRcId(rsRcId);
    pBsl.setRsDs(rsDs);
  }

  /**
   * <p>Makes paym matching or revd.</p>
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars
   * @param pPayId Paym Id
   * @param pBsl BSL
   * @param pDtFrm Date Formatter
   * @param pCpf language
   * @throws Exception - an exception
   **/
  public final void makePaymMatchingReversed(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final String pPayId,
      final BnStLn pBsl, final DateFormat pDtFrm,
        final CmnPrf pCpf) throws Exception {
    EBnEnrRsTy rsRcTy = null;
    EBnEnrRsAc rsAc = null;
    String rsDs = null;
    Long rsRcId = null;
    if (pBsl.getAmnt().compareTo(BigDecimal.ZERO) > 0) {
      PaymFr pay = new PaymFr();
      pay.setIid(Long.parseLong(pPayId));
      getOrm().refrEnt(pRvs, pVs, pay);
      if (!pay.getMdEnr() || pBsl.getAmnt().abs().compareTo(pay.getTot()) != 0
        || pay.getSaTy() != 1003
          || !pBsl.getOwnr().getBnka().getIid().equals(pay.getSaId())) {
        throw new ExcCode(ExcCode.WRPR, "record_is_not_matching");
      }
      rsRcTy = EBnEnrRsTy.PAYMFR;
      if (EBnEnrSt.VOIDED.equals(pBsl.getStas()) && pay.getRvId() == null) {
        rsAc = EBnEnrRsAc.CREATE;
        PaymFr revd = pay;
        pay = new PaymFr();
        pay.setAcc(revd.getAcc());
        pay.setSaNm(revd.getSaNm());
        pay.setSaId(revd.getSaId());
        pay.setSaTy(1003);
        pay.setInv(revd.getInv());
        pay.setToFc(revd.getToFc().negate());
        pay.setDscr(makeDescrForCreated(pBsl, pDtFrm, pCpf)
          + " " + getI18n().getMsg("revd_n", pCpf.getLngDef().getIid()) + revd
            .getDbOr() + "-" + revd.getIid());
        makeDocReversed(pRvs, pVs, pay, revd, pCpf);
      } else {
        rsAc = EBnEnrRsAc.MATCH;
      }
      rsRcId = pay.getIid();
      rsDs = makeBslResDescr(rsAc, pDtFrm, pay, pay.getDat(), pCpf);
    } else {
      PaymTo pay = new PaymTo();
      pay.setIid(Long.parseLong(pPayId));
      getOrm().refrEnt(pRvs, pVs, pay);
      if (!pay.getMdEnr() || pBsl.getAmnt().abs().compareTo(pay.getTot()) != 0
          || pay.getSaTy() != 1003
            || !pBsl.getOwnr().getBnka().getIid().equals(pay.getSaId())) {
        throw new ExcCode(ExcCode.WRPR, "record_is_not_matching");
      }
      rsRcTy = EBnEnrRsTy.PAYMTO;
      if (EBnEnrSt.VOIDED.equals(pBsl.getStas()) && pay.getRvId() == null) {
        rsAc = EBnEnrRsAc.CREATE;
        PaymTo revd = pay;
        pay = new PaymTo();
        pay.setAcc(revd.getAcc());
        pay.setSaNm(revd.getSaNm());
        pay.setSaId(revd.getSaId());
        pay.setSaTy(1003);
        pay.setInv(revd.getInv());
        pay.setToFc(revd.getToFc().negate());
        pay.setDscr(makeDescrForCreated(pBsl, pDtFrm, pCpf)
          + " " + getI18n().getMsg("revd_n", pCpf.getLngDef().getIid()) + revd
            .getDbOr() + "-" + revd.getIid());
        makeDocReversed(pRvs, pVs, pay, revd, pCpf);
      } else {
        rsAc = EBnEnrRsAc.MATCH;
      }
      rsRcId = pay.getIid();
      rsDs = makeBslResDescr(rsAc, pDtFrm, pay, pay.getDat(), pCpf);
    }
    pBsl.setRsAc(rsAc);
    pBsl.setRsRcTy(rsRcTy);
    pBsl.setRsRcId(rsRcId);
    pBsl.setRsDs(rsDs);
  }

  /**
   * <p>Makes prep matching or revd.</p>
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars
   * @param pPrepayId Prep Id
   * @param pBsl BSL
   * @param pDtFrm Date Formatter
   * @param pCpf language
   * @throws Exception - an exception
   **/
  public final void makePrepMatchingReversed(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final String pPrepayId,
      final BnStLn pBsl, final DateFormat pDtFrm,
        final CmnPrf pCpf) throws Exception {
    EBnEnrRsTy rsRcTy = null;
    EBnEnrRsAc rsAc = null;
    String rsDs = null;
    Long rsRcId = null;
    if (pBsl.getAmnt().compareTo(BigDecimal.ZERO) > 0) {
      PrepFr prep = new PrepFr();
      prep.setIid(Long.parseLong(pPrepayId));
      getOrm().refrEnt(pRvs, pVs, prep);
      if (!prep.getMdEnr() || pBsl.getAmnt().abs().compareTo(prep.getTot()) != 0
          || prep.getSaTy() != 1003
            || !pBsl.getOwnr().getBnka().getIid().equals(prep.getSaId())) {
        throw new ExcCode(ExcCode.WRPR, "record_is_not_matching");
      }
      rsRcTy = EBnEnrRsTy.PREPFR;
      if (EBnEnrSt.VOIDED.equals(pBsl.getStas()) && prep.getRvId() == null) {
        rsAc = EBnEnrRsAc.CREATE;
        PrepFr revd = prep;
        prep = new PrepFr();
        prep.setAcc(revd.getAcc());
        prep.setSaNm(revd.getSaNm());
        prep.setSaId(revd.getSaId());
        prep.setSaTy(1003);
        prep.setDbcr(revd.getDbcr());
        prep.setToFc(revd.getToFc().negate());
        prep.setDscr(makeDescrForCreated(pBsl, pDtFrm, pCpf)
          + " " + getI18n().getMsg("revd_n", pCpf.getLngDef().getIid()) + revd
            .getDbOr() + "-" + revd.getIid());
        makeDocReversed(pRvs, pVs, prep, revd, pCpf);
      } else {
        rsAc = EBnEnrRsAc.MATCH;
      }
      rsRcId = prep.getIid();
      rsDs = makeBslResDescr(rsAc, pDtFrm, prep, prep.getDat(), pCpf);
    } else {
      PrepTo prep = new PrepTo();
      prep.setIid(Long.parseLong(pPrepayId));
      getOrm().refrEnt(pRvs, pVs, prep);
      if (!prep.getMdEnr() || pBsl.getAmnt().abs().compareTo(prep.getTot()) != 0
        || prep.getSaTy() != 1003
          || !pBsl.getOwnr().getBnka().getIid().equals(prep.getSaId())) {
        throw new ExcCode(ExcCode.WRPR, "record_is_not_matching");
      }
      rsRcTy = EBnEnrRsTy.PREPTO;
      if (EBnEnrSt.VOIDED.equals(pBsl.getStas()) && prep.getRvId() == null) {
        rsAc = EBnEnrRsAc.CREATE;
        PrepTo revd = prep;
        prep = new PrepTo();
        prep.setAcc(revd.getAcc());
        prep.setSaNm(revd.getSaNm());
        prep.setSaId(revd.getSaId());
        prep.setSaTy(1003);
        prep.setDbcr(revd.getDbcr());
        prep.setToFc(revd.getToFc().negate());
        prep.setDscr(makeDescrForCreated(pBsl, pDtFrm, pCpf)
          + " " + getI18n().getMsg("revd_n", pCpf.getLngDef().getIid()) + revd
            .getDbOr() + "-" + revd.getIid());
        makeDocReversed(pRvs, pVs, prep, revd, pCpf);
      } else {
        rsAc = EBnEnrRsAc.MATCH;
      }
      rsRcId = prep.getIid();
      rsDs = makeBslResDescr(rsAc, pDtFrm, prep, prep.getDat(), pCpf);
    }
    pBsl.setRsAc(rsAc);
    pBsl.setRsRcTy(rsRcTy);
    pBsl.setRsRcId(rsRcId);
    pBsl.setRsDs(rsDs);
  }

  /**
   * <p>Makes document reversed.</p>
   * @param pRvs request scoped vars
   * @param pVs invoker scoped vars
   * @param pRvng Reversing
   * @param pRved Reversed
   * @param pCpf language
   * @throws Exception - an exception
   **/
  public final void makeDocReversed(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final ADoc pRvng, final ADoc pRved,
      final CmnPrf pCpf) throws Exception {
    pRvng.setDbOr(pRved.getDbOr());
    pRvng.setRvId(pRved.getIid());
    pRvng.setDat(new Date(pRved.getDat().getTime() + 1));
    pRvng.setTot(pRved.getTot().negate());
    pRvng.setMdEnr(false);
    getOrm().insIdLn(pRvs, pVs, pRvng);
    String oldDesr = "";
    if (pRved.getDscr() != null) {
      oldDesr = pRved.getDscr();
    }
    pRved.setDscr(oldDesr + " " + getI18n()
      .getMsg("reversing_n", pCpf.getLngDef().getIid()) + pRvng.getDbOr() + "-"
        + pRvng.getIid());
    pRved.setRvId(pRvng.getIid());
    getOrm().update(pRvs, pVs, pRved);
    this.srEntr.revEntrs(pRvs, pRvng, pRved);
  }

  /**
   * <p>Makes BSL result description.</p>
   * @param pResAct action
   * @param pDtFrm Date Formatter
   * @param pRecord Record
   * @param pDate Date
   * @param pCpf language
   * @return description
   **/
  public final String makeBslResDescr(final EBnEnrRsAc pResAct,
    final DateFormat pDtFrm, final AOrId pRecord,
      final Date pDate, final CmnPrf pCpf) {
    StringBuffer sb = new StringBuffer();
    if (EBnEnrRsAc.MATCH.equals(pResAct)) {
      sb.append(getI18n().getMsg("Found", pCpf.getLngDef().getIid()));
    } else {
      sb.append(getI18n().getMsg("Created", pCpf.getLngDef().getIid()));
    }
    sb.append(" " + getI18n().getMsg(pRecord.getClass().getSimpleName()
      + "short", pCpf.getLngDef().getIid()));
    sb.append("#" + pRecord.getDbOr() + "-" + pRecord.getIid()
      + ", " + pDtFrm.format(pDate));
    return sb.toString();
  }

  /**
   * <p>Makes description for created record.</p>
   * @param pBsl BSL
   * @param pDtFrm Date Formatter
   * @param pCpf language
   * @return description
   **/
  public final String makeDescrForCreated(final BnStLn pBsl,
    final DateFormat pDtFrm, final CmnPrf pCpf) {
    StringBuffer sb = new StringBuffer();
    sb.append(getI18n().getMsg("Created", pCpf.getLngDef().getIid())
      + " " + getI18n().getMsg("by", pCpf.getLngDef().getIid()));
    sb.append(" " + getI18n().getMsg(pBsl.getClass().getSimpleName()
      + "short", pCpf.getLngDef().getIid()));
    sb.append("#" + pBsl.getDbOr() + "-" + pBsl.getIid()
      + ", " + pDtFrm.format(pBsl.getDat()));
    sb.append(" (" + pBsl.getDsSt() + ")");
    return sb.toString();
  }

  //Simple getters and setters:
  /**
   * <p>Getter for orm.</p>
   * @return IOrm
   **/
  public final IOrm getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final void setOrm(final IOrm pOrm) {
    this.orm = pOrm;
  }

  /**
   * <p>Getter for i18n.</p>
   * @return II18n
   **/
  public final II18n getI18n() {
    return this.i18n;
  }

  /**
   * <p>Setter for i18n.</p>
   * @param pI18n reference
   **/
  public final void setI18n(final II18n pI18n) {
    this.i18n = pI18n;
  }

  /**
   * <p>Getter for srEntr.</p>
   * @return ISrEntr
   **/
  public final ISrEntr getSrEntr() {
    return this.srEntr;
  }

  /**
   * <p>Setter for srEntr.</p>
   * @param pSrEntr reference
   **/
  public final void setSrEntr(final ISrEntr pSrEntr) {
    this.srEntr = pSrEntr;
  }

  /**
   * <p>Getter for srvDt.</p>
   * @return ISrvDt
   **/
  public final ISrvDt getSrvDt() {
    return this.srvDt;
  }

  /**
   * <p>Setter for srvDt.</p>
   * @param pSrvDt reference
   **/
  public final void setSrvDt(final ISrvDt pSrvDt) {
    this.srvDt = pSrvDt;
  }

  /**
   * <p>Getter for srToPa.</p>
   * @return ISrToPa
   **/
  public final ISrToPa getSrToPa() {
    return this.srToPa;
  }

  /**
   * <p>Setter for srToPa.</p>
   * @param pSrToPa reference
   **/
  public final void setSrToPa(final ISrToPa pSrToPa) {
    this.srToPa = pSrToPa;
  }

  /**
   * <p>Getter for rvPuLn.</p>
   * @return IRvInvLn<PurInv, ?>
   **/
  public final IRvInvLn<PurInv, ?> getRvPuLn() {
    return this.rvPuLn;
  }

  /**
   * <p>Setter for rvPuLn.</p>
   * @param pRvPuLn reference
   **/
  public final void setRvPuLn(final IRvInvLn<PurInv, ?> pRvPuLn) {
    this.rvPuLn = pRvPuLn;
  }

  /**
   * <p>Getter for rvSaLn.</p>
   * @return IRvInvLn<SalInv, ?>
   **/
  public final IRvInvLn<SalInv, ?> getRvSaLn() {
    return this.rvSaLn;
  }

  /**
   * <p>Setter for rvSaLn.</p>
   * @param pRvSaLn reference
   **/
  public final void setRvSaLn(final IRvInvLn<SalInv, ?> pRvSaLn) {
    this.rvSaLn = pRvSaLn;
  }
}
