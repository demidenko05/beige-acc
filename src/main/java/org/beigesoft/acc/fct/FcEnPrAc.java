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

package org.beigesoft.acc.fct;

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.fct.IFctPrcEnt;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.fct.FctOrId;
import org.beigesoft.fct.FctEnPrc;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.prc.PrcEntRt;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.srv.ICsvRdr;
import org.beigesoft.srv.ISrvDt;
import org.beigesoft.acc.mdlb.IDoci;
import org.beigesoft.acc.mdlb.IDcDri;
import org.beigesoft.acc.mdlp.SalInv;
import org.beigesoft.acc.mdlp.SaInGdLn;
import org.beigesoft.acc.mdlp.SaInSrLn;
import org.beigesoft.acc.mdlp.SaInSrTxLn;
import org.beigesoft.acc.mdlp.SaInTxLn;
import org.beigesoft.acc.mdlp.SaInGdTxLn;
import org.beigesoft.acc.mdlp.SalRet;
import org.beigesoft.acc.mdlp.SaRtLn;
import org.beigesoft.acc.mdlp.SaRtTxLn;
import org.beigesoft.acc.mdlp.SaRtLtl;
import org.beigesoft.acc.mdlp.PurRet;
import org.beigesoft.acc.mdlp.PuRtLn;
import org.beigesoft.acc.mdlp.PuRtTxLn;
import org.beigesoft.acc.mdlp.PuRtLtl;
import org.beigesoft.acc.mdlp.PurInv;
import org.beigesoft.acc.mdlp.PuInGdLn;
import org.beigesoft.acc.mdlp.PuInSrLn;
import org.beigesoft.acc.mdlp.PuInSrTxLn;
import org.beigesoft.acc.mdlp.PuInTxLn;
import org.beigesoft.acc.mdlp.PuInGdTxLn;
import org.beigesoft.acc.mdlp.Itm;
import org.beigesoft.acc.mdlp.ItTxDl;
import org.beigesoft.acc.mdlp.PaymFr;
import org.beigesoft.acc.mdlp.PaymTo;
import org.beigesoft.acc.mdlp.MovItm;
import org.beigesoft.acc.prc.SacntSv;
import org.beigesoft.acc.prc.SacntCr;
import org.beigesoft.acc.prc.EntrCr;
import org.beigesoft.acc.prc.EnrSrcChu;
import org.beigesoft.acc.prc.EntrChd;
import org.beigesoft.acc.prc.EntrCpr;
import org.beigesoft.acc.prc.BnStLnGfe;
import org.beigesoft.acc.prc.EntrRt;
import org.beigesoft.acc.prc.EntrSrcCr;
import org.beigesoft.acc.prc.IsacntSv;
import org.beigesoft.acc.prc.AcntDl;
import org.beigesoft.acc.prc.IsacntDl;
import org.beigesoft.acc.prc.AcntSv;
import org.beigesoft.acc.prc.TxCtLnSv;
import org.beigesoft.acc.prc.TxCtLnDl;
import org.beigesoft.acc.prc.EntrSv;
import org.beigesoft.acc.prc.InEntrSv;
import org.beigesoft.acc.prc.InEntrDl;
import org.beigesoft.acc.prc.AcStgRt;
import org.beigesoft.acc.prc.AcStgSv;
import org.beigesoft.acc.prc.InEntrRt;
import org.beigesoft.acc.prc.DocDl;
import org.beigesoft.acc.prc.DocPr;
import org.beigesoft.acc.prc.ItAdLnRv;
import org.beigesoft.acc.prc.ItAdLnSv;
import org.beigesoft.acc.prc.ItmAddSv;
import org.beigesoft.acc.prc.MnpMcsRv;
import org.beigesoft.acc.prc.MnpAcsSv;
import org.beigesoft.acc.prc.MnpMcsSv;
import org.beigesoft.acc.prc.MnfctSv;
import org.beigesoft.acc.prc.MnfPrcSv;
import org.beigesoft.acc.prc.BnkStmSv;
import org.beigesoft.acc.prc.ItUbLnRv;
import org.beigesoft.acc.prc.BnStLnSv;
import org.beigesoft.acc.prc.ItUbLnSv;
import org.beigesoft.acc.prc.ItmUlbSv;
import org.beigesoft.acc.prc.MoItLnSv;
import org.beigesoft.acc.prc.MovItmPr;
import org.beigesoft.acc.prc.DcDriPr;
import org.beigesoft.acc.prc.DocWhPr;
import org.beigesoft.acc.prc.PaymSv;
import org.beigesoft.acc.prc.PrepSv;
import org.beigesoft.acc.prc.InvLnSv;
import org.beigesoft.acc.prc.InSrLnDl;
import org.beigesoft.acc.prc.RetSv;
import org.beigesoft.acc.prc.RetLnSv;
import org.beigesoft.acc.prc.InvSv;
import org.beigesoft.acc.prc.MoItLnRv;
import org.beigesoft.acc.prc.RetLnRv;
import org.beigesoft.acc.prc.InvLnCpr;
import org.beigesoft.acc.prc.DocCpr;
import org.beigesoft.acc.prc.PrepCpr;
import org.beigesoft.acc.prc.WageSv;
import org.beigesoft.acc.prc.WgLnSv;
import org.beigesoft.acc.prc.WgLnDl;
import org.beigesoft.acc.prc.WgTxlSv;
import org.beigesoft.acc.prc.WgTxlDl;
import org.beigesoft.acc.prc.InTxLnSv;
import org.beigesoft.acc.srv.ISrAcStg;
import org.beigesoft.acc.srv.ISrBlnc;
import org.beigesoft.acc.srv.ISrToPa;
import org.beigesoft.acc.srv.ISrEntr;
import org.beigesoft.acc.srv.ISrWrhEnr;
import org.beigesoft.acc.srv.ISrDrItEnr;
import org.beigesoft.acc.srv.UtlBas;
import org.beigesoft.acc.srv.SrPaymSv;
import org.beigesoft.acc.srv.SrInvSv;
import org.beigesoft.acc.srv.SrRetSv;
import org.beigesoft.acc.srv.SrRtLnSv;
import org.beigesoft.acc.srv.SrInLnSv;
import org.beigesoft.acc.srv.SrSaGdLn;
import org.beigesoft.acc.srv.SrSaSrLn;
import org.beigesoft.acc.srv.RvSaGdLn;
import org.beigesoft.acc.srv.RvSaSrLn;
import org.beigesoft.acc.srv.SrPuGdLn;
import org.beigesoft.acc.srv.SrSaRtLn;
import org.beigesoft.acc.srv.SrPuRtLn;
import org.beigesoft.acc.srv.SrPuSrLn;
import org.beigesoft.acc.srv.RvPuGdLn;
import org.beigesoft.acc.srv.RvPuRtLn;
import org.beigesoft.acc.srv.RvSaRtLn;
import org.beigesoft.acc.srv.RvPuSrLn;
import org.beigesoft.acc.srv.UtInLnTxTo;
import org.beigesoft.acc.srv.UtInLnTxToBs;
import org.beigesoft.acc.srv.InvTxMeth;

/**
 * <p>Accounting additional factory of entity processors.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FcEnPrAc<RS> implements IFctPrcEnt {

  /**
   * <p>Sales invoice good line service name.</p>
   **/
  public static final String SALINVGDLNSV = "SalInvGdLnSv";

  /**
   * <p>Sales invoice service line service name.</p>
   **/
  public static final String SALINVSRLNSV = "SalInvSrLnSv";

  /**
   * <p>Sales invoice line service name.</p>
   **/
  public static final String SALINVSV = "SalInvSv";

  /**
   * <p>Purchase invoice good line service name.</p>
   **/
  public static final String PURINVGDLNSV = "PurInvGdLnSv";

  /**
   * <p>Purchase invoice service line delete name.</p>
   **/
  public static final String PUINSRLNDL = "PuInSrLnDl";

  /**
   * <p>Sales invoice service line delete name.</p>
   **/
  public static final String SAINSRLNDL = "SaInSrLnDl";

  /**
   * <p>Purchase invoice service line service name.</p>
   **/
  public static final String PURINVSRLNSV = "PurInvSrLnSv";

  /**
   * <p>Purchase invoice line service name.</p>
   **/
  public static final String PURINVSV = "PurInvSv";

  /**
   * <p>Payment from save service name.</p>
   **/
  public static final String PAYFRSV = "PayFrSv";

  /**
   * <p>Payment to save service name.</p>
   **/
  public static final String PAYTOSV = "PayToSv";

  /**
   * <p>Purchase return saving service name.</p>
   **/
  public static final String PURETSV = "PrRtSv";

  /**
   * <p>Purchase return line saving service name.</p>
   **/
  public static final String PURTLNSV = "PrRtLnSv";

  /**
   * <p>Sales return saving service name.</p>
   **/
  public static final String SARETSV = "SaRtSv";

  /**
   * <p>Sales return line saving service name.</p>
   **/
  public static final String SARTLNSV = "SaRtLnSv";

  /**
   * <p>Sales invoice tax line saving service name.</p>
   **/
  public static final String SAINTXLNSV = "SaInTxLnSv";

  /**
   * <p>Main factory.</p>
   **/
  private FctBlc<RS> fctBlc;

  //requested data:
  /**
   * <p>Processors map.</p>
   **/
  private final Map<String, IPrcEnt<?, ?>> procs =
    new HashMap<String, IPrcEnt<?, ?>>();

  /**
   * <p>Get processor in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pPrNm - filler name
   * @return requested processor or NULL
   * @throws Exception - an exception
   */
  public final IPrcEnt<?, ?> laz(final Map<String, Object> pRvs,
    final String pPrNm) throws Exception {
    IPrcEnt<?, ?> rz = this.procs.get(pPrNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.procs.get(pPrNm);
        if (rz == null) {
          if (EntrCr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrCr(pRvs);
          } else if (BnStLnGfe.class.getSimpleName().equals(pPrNm)) {
            rz = crPuBnStLnGfe(pRvs);
          } else if (EntrRt.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrRt(pRvs);
          } else if (EnrSrcChu.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEnrSrcChu(pRvs);
          } else if (EntrChd.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrChd(pRvs);
          } else if (EntrCpr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrCpr(pRvs);
          } else if (SacntSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuSacntSv(pRvs);
          } else if (SacntCr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuSacntCr(pRvs);
          } else if (EntrSrcCr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrSrcCr(pRvs);
          } else if (WageSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuWageSv(pRvs);
          } else if (WgLnDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuWgLnDl(pRvs);
          } else if (WgLnSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuWgLnSv(pRvs);
          } else if (WgTxlDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuWgTxlDl(pRvs);
          } else if (WgTxlSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuWgTxlSv(pRvs);
          } else if (InEntrSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuInEntrSv(pRvs);
          } else if (AcStgRt.class.getSimpleName().equals(pPrNm)) {
            rz = crPuAcStgRt(pRvs);
          } else if (AcStgSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuAcStgSv(pRvs);
          } else if (InEntrRt.class.getSimpleName().equals(pPrNm)) {
            rz = crPuInEntrRt(pRvs);
          } else if (InEntrDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuInEntrDl(pRvs);
          } else if (AcntDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuAcntDl(pRvs);
          } else if (IsacntDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuIsacntDl(pRvs);
          } else if (IsacntSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuIsacntSv(pRvs);
          } else if (AcntSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuAcntSv(pRvs);
          } else if (TxCtLnDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuTxCtLnDl(pRvs);
          } else if (TxCtLnSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuTxCtLnSv(pRvs);
          } else if (ItAdLnRv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuItAdLnRv(pRvs);
          } else if (MnpMcsRv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuMnpMcsRv(pRvs);
          } else if (ItUbLnRv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuItUbLnRv(pRvs);
          } else if (MoItLnRv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuMoItLnRv(pRvs);
          } else if (RetLnRv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuRetLnRv(pRvs);
          } else if (InvLnCpr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuInvLnCpr(pRvs);
          } else if (DocCpr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuDocCpr(pRvs);
          } else if (PrepCpr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrepCpr(pRvs);
          } else if (MovItmPr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuMovItmPr(pRvs);
          } else if (ItmAddSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuItmAddSv(pRvs);
          } else if (ItAdLnSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuItAdLnSv(pRvs);
          } else if (MnfctSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuMnfctSv(pRvs);
          } else if (BnkStmSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuBnkStmSv(pRvs);
          } else if (MnfPrcSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuMnfPrcSv(pRvs);
          } else if (ItmUlbSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuItmUlbSv(pRvs);
          } else if (MnpAcsSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuMnpAcsSv(pRvs);
          } else if (MnpMcsSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuMnpMcsSv(pRvs);
          } else if (BnStLnSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuBnStLnSv(pRvs);
          } else if (ItUbLnSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuItUbLnSv(pRvs);
          } else if (MoItLnSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuMoItLnSv(pRvs);
          } else if (DcDriPr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuDcDriPr(pRvs);
          } else if (DocWhPr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuDocWhPr(pRvs);
          } else if (DocDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuDocDl(pRvs);
          } else if (DocPr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuDocPr(pRvs);
          } else if (SAINTXLNSV.equals(pPrNm)) {
            rz = crPuSaInTxLnSv(pRvs);
          } else if (SALINVSRLNSV.equals(pPrNm)) {
            rz = crPuSalInvSrLnSv(pRvs);
          } else if (SARTLNSV.equals(pPrNm)) {
            rz = crPuSaRtLnSv(pRvs);
          } else if (PURTLNSV.equals(pPrNm)) {
            rz = crPuPuRtLnSv(pRvs);
          } else if (SALINVGDLNSV.equals(pPrNm)) {
            rz = crPuSalInvGdLnSv(pRvs);
          } else if (SALINVSV.equals(pPrNm)) {
            rz = crPuSalInvSv(pRvs);
          } else if (SAINSRLNDL.equals(pPrNm)) {
            rz = crPuSaInSrLnDl(pRvs);
          } else if (PUINSRLNDL.equals(pPrNm)) {
            rz = crPuPuInSrLnDl(pRvs);
          } else if (PURINVSRLNSV.equals(pPrNm)) {
            rz = crPuPurInvSrLnSv(pRvs);
          } else if (PURINVGDLNSV.equals(pPrNm)) {
            rz = crPuPurInvGdLnSv(pRvs);
          } else if (PURINVSV.equals(pPrNm)) {
            rz = crPuPurInvSv(pRvs);
          } else if (SARETSV.equals(pPrNm)) {
            rz = crPuSalRetSv(pRvs);
          } else if (PURETSV.equals(pPrNm)) {
            rz = crPuPurRetSv(pRvs);
          } else if (PAYFRSV.equals(pPrNm)) {
            rz = crPuPaymFrSv(pRvs);
          } else if (PAYTOSV.equals(pPrNm)) {
            rz = crPuPaymToSv(pRvs);
          } else if (PrepSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrepSv(pRvs);
          } else if (EntrSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrSv(pRvs);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map AcStgSv.</p>
   * @param pRvs request scoped vars
   * @return AcStgSv
   * @throws Exception - an exception
   */
  private AcStgSv crPuAcStgSv(
    final Map<String, Object> pRvs) throws Exception {
    AcStgSv rz = new AcStgSv();
    ISrAcStg srAcStg = (ISrAcStg) this.fctBlc
      .laz(pRvs, ISrAcStg.class.getSimpleName());
    rz.setSrAcStg(srAcStg);
    this.procs.put(AcStgSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), AcStgSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map AcStgRt.</p>
   * @param pRvs request scoped vars
   * @return AcStgRt
   * @throws Exception - an exception
   */
  private AcStgRt crPuAcStgRt(
    final Map<String, Object> pRvs) throws Exception {
    AcStgRt rz = new AcStgRt();
    ISrAcStg srAcStg = (ISrAcStg) this.fctBlc
      .laz(pRvs, ISrAcStg.class.getSimpleName());
    rz.setSrAcStg(srAcStg);
    this.procs.put(AcStgRt.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), AcStgRt.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map InEntrRt.</p>
   * @param pRvs request scoped vars
   * @return InEntrRt
   * @throws Exception - an exception
   */
  private InEntrRt crPuInEntrRt(
    final Map<String, Object> pRvs) throws Exception {
    InEntrRt rz = new InEntrRt();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(InEntrRt.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), InEntrRt.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map WageSv.</p>
   * @param pRvs request scoped vars
   * @return WageSv
   * @throws Exception - an exception
   */
  private WageSv<RS> crPuWageSv(
    final Map<String, Object> pRvs) throws Exception {
    WageSv<RS> rz = new WageSv<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    ISrEntr srEntr = (ISrEntr) this.fctBlc
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    UtlBas utlBas = (UtlBas) this.fctBlc
      .laz(pRvs, UtlBas.class.getSimpleName());
    rz.setUtlBas(utlBas);    this.procs.put(WageSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), WageSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map WgLnDl.</p>
   * @param pRvs request scoped vars
   * @return WgLnDl
   * @throws Exception - an exception
   */
  private WgLnDl<RS> crPuWgLnDl(
    final Map<String, Object> pRvs) throws Exception {
    WgLnDl<RS> rz = new WgLnDl<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    this.procs.put(WgLnDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), WgLnDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map WgLnSv.</p>
   * @param pRvs request scoped vars
   * @return WgLnSv
   * @throws Exception - an exception
   */
  private WgLnSv<RS> crPuWgLnSv(
    final Map<String, Object> pRvs) throws Exception {
    WgLnSv<RS> rz = new WgLnSv<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    this.procs.put(WgLnSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), WgLnSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map WgTxlDl.</p>
   * @param pRvs request scoped vars
   * @return WgTxlDl
   * @throws Exception - an exception
   */
  private WgTxlDl crPuWgTxlDl(
    final Map<String, Object> pRvs) throws Exception {
    WgTxlDl rz = new WgTxlDl();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(WgTxlDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), WgTxlDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map WgTxlSv.</p>
   * @param pRvs request scoped vars
   * @return WgTxlSv
   * @throws Exception - an exception
   */
  private WgTxlSv crPuWgTxlSv(
    final Map<String, Object> pRvs) throws Exception {
    WgTxlSv rz = new WgTxlSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(WgTxlSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), WgTxlSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map InEntrSv.</p>
   * @param pRvs request scoped vars
   * @return InEntrSv
   * @throws Exception - an exception
   */
  private InEntrSv crPuInEntrSv(
    final Map<String, Object> pRvs) throws Exception {
    InEntrSv rz = new InEntrSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(InEntrSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), InEntrSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map InEntrDl.</p>
   * @param pRvs request scoped vars
   * @return InEntrDl
   * @throws Exception - an exception
   */
  private InEntrDl<RS> crPuInEntrDl(
    final Map<String, Object> pRvs) throws Exception {
    InEntrDl<RS> rz = new InEntrDl<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    this.procs.put(InEntrDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), InEntrDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map AcntDl.</p>
   * @param pRvs request scoped vars
   * @return AcntDl
   * @throws Exception - an exception
   */
  private AcntDl<RS> crPuAcntDl(
    final Map<String, Object> pRvs) throws Exception {
    AcntDl<RS> rz = new AcntDl<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    this.procs.put(AcntDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), AcntDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map IsacntDl.</p>
   * @param pRvs request scoped vars
   * @return IsacntDl
   * @throws Exception - an exception
   */
  private IsacntDl<RS> crPuIsacntDl(
    final Map<String, Object> pRvs) throws Exception {
    IsacntDl<RS> rz = new IsacntDl<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    this.procs.put(IsacntDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), IsacntDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map IsacntSv.</p>
   * @param pRvs request scoped vars
   * @return IsacntSv
   * @throws Exception - an exception
   */
  private IsacntSv crPuIsacntSv(
    final Map<String, Object> pRvs) throws Exception {
    IsacntSv rz = new IsacntSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    ISrBlnc srBlnc = (ISrBlnc) this.fctBlc
      .laz(pRvs, ISrBlnc.class.getSimpleName());
    rz.setSrBlnc(srBlnc);
    this.procs.put(IsacntSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), IsacntSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map AcntSv.</p>
   * @param pRvs request scoped vars
   * @return AcntSv
   * @throws Exception - an exception
   */
  private AcntSv<RS> crPuAcntSv(
    final Map<String, Object> pRvs) throws Exception {
    AcntSv<RS> rz = new AcntSv<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    this.procs.put(AcntSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), AcntSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map TxCtLnDl.</p>
   * @param pRvs request scoped vars
   * @return TxCtLnDl
   * @throws Exception - an exception
   */
  private TxCtLnDl<RS> crPuTxCtLnDl(
    final Map<String, Object> pRvs) throws Exception {
    TxCtLnDl<RS> rz = new TxCtLnDl<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    this.procs.put(TxCtLnDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), TxCtLnDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map TxCtLnSv.</p>
   * @param pRvs request scoped vars
   * @return TxCtLnSv
   * @throws Exception - an exception
   */
  private TxCtLnSv<RS> crPuTxCtLnSv(
    final Map<String, Object> pRvs) throws Exception {
    TxCtLnSv<RS> rz = new TxCtLnSv<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    this.procs.put(TxCtLnSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), TxCtLnSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map ItAdLnRv.</p>
   * @param pRvs request scoped vars
   * @return ItAdLnRv
   * @throws Exception - an exception
   */
  private ItAdLnRv crPuItAdLnRv(
    final Map<String, Object> pRvs) throws Exception {
    ItAdLnRv rz = new ItAdLnRv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(ItAdLnRv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), ItAdLnRv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map MnpMcsRv.</p>
   * @param pRvs request scoped vars
   * @return MnpMcsRv
   * @throws Exception - an exception
   */
  private MnpMcsRv crPuMnpMcsRv(
    final Map<String, Object> pRvs) throws Exception {
    MnpMcsRv rz = new MnpMcsRv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(MnpMcsRv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), MnpMcsRv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map ItUbLnRv.</p>
   * @param pRvs request scoped vars
   * @return ItUbLnRv
   * @throws Exception - an exception
   */
  private ItUbLnRv crPuItUbLnRv(
    final Map<String, Object> pRvs) throws Exception {
    ItUbLnRv rz = new ItUbLnRv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(ItUbLnRv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), ItUbLnRv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map MoItLnRv.</p>
   * @param pRvs request scoped vars
   * @return MoItLnRv
   * @throws Exception - an exception
   */
  private MoItLnRv crPuMoItLnRv(
    final Map<String, Object> pRvs) throws Exception {
    MoItLnRv rz = new MoItLnRv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(MoItLnRv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), MoItLnRv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map RetLnRv.</p>
   * @param pRvs request scoped vars
   * @return RetLnRv
   * @throws Exception - an exception
   */
  private RetLnRv crPuRetLnRv(
    final Map<String, Object> pRvs) throws Exception {
    RetLnRv rz = new RetLnRv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(RetLnRv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), RetLnRv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map InvLnCpr.</p>
   * @param pRvs request scoped vars
   * @return InvLnCpr
   * @throws Exception - an exception
   */
  private InvLnCpr crPuInvLnCpr(
    final Map<String, Object> pRvs) throws Exception {
    InvLnCpr rz = new InvLnCpr();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(InvLnCpr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), InvLnCpr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map DocCpr.</p>
   * @param pRvs request scoped vars
   * @return DocCpr
   * @throws Exception - an exception
   */
  private DocCpr crPuDocCpr(
    final Map<String, Object> pRvs) throws Exception {
    DocCpr rz = new DocCpr();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(DocCpr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), DocCpr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrepCpr.</p>
   * @param pRvs request scoped vars
   * @return PrepCpr
   * @throws Exception - an exception
   */
  private PrepCpr crPuPrepCpr(
    final Map<String, Object> pRvs) throws Exception {
    PrepCpr rz = new PrepCpr();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(PrepCpr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PrepCpr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map ItmAddSv.</p>
   * @param pRvs request scoped vars
   * @return ItmAddSv
   * @throws Exception - an exception
   */
  private ItmAddSv crPuItmAddSv(
    final Map<String, Object> pRvs) throws Exception {
    ItmAddSv rz = new ItmAddSv();
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    ISrEntr srEntr = (ISrEntr) this.fctBlc
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    UtlBas utlBas = (UtlBas) this.fctBlc
      .laz(pRvs, UtlBas.class.getSimpleName());
    rz.setUtlBas(utlBas);
    this.procs.put(ItmAddSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), ItmAddSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map ItAdLnSv.</p>
   * @param pRvs request scoped vars
   * @return ItAdLnSv
   * @throws Exception - an exception
   */
  private ItAdLnSv<RS> crPuItAdLnSv(
    final Map<String, Object> pRvs) throws Exception {
    ItAdLnSv<RS> rz = new ItAdLnSv<RS>();
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    this.procs.put(ItAdLnSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), ItAdLnSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map MnfctSv.</p>
   * @param pRvs request scoped vars
   * @return MnfctSv
   * @throws Exception - an exception
   */
  private MnfctSv crPuMnfctSv(
    final Map<String, Object> pRvs) throws Exception {
    MnfctSv rz = new MnfctSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    ISrDrItEnr srDrItEnr = (ISrDrItEnr) this.fctBlc
      .laz(pRvs, ISrDrItEnr.class.getSimpleName());
    rz.setSrDrItEnr(srDrItEnr);
    ISrEntr srEntr = (ISrEntr) this.fctBlc
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    UtlBas utlBas = (UtlBas) this.fctBlc
      .laz(pRvs, UtlBas.class.getSimpleName());
    rz.setUtlBas(utlBas);
    this.procs.put(MnfctSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), MnfctSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map BnkStmSv.</p>
   * @param pRvs request scoped vars
   * @return BnkStmSv
   * @throws Exception - an exception
   */
  private BnkStmSv crPuBnkStmSv(
    final Map<String, Object> pRvs) throws Exception {
    BnkStmSv rz = new BnkStmSv();
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    ICsvRdr csvRdr = (ICsvRdr) this.fctBlc
      .laz(pRvs, ICsvRdr.class.getSimpleName());
    rz.setCsvRdr(csvRdr);
    UtlBas utlBas = (UtlBas) this.fctBlc
      .laz(pRvs, UtlBas.class.getSimpleName());
    rz.setUtlBas(utlBas);
    this.procs.put(BnkStmSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), BnkStmSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map MnfPrcSv.</p>
   * @param pRvs request scoped vars
   * @return MnfPrcSv
   * @throws Exception - an exception
   */
  private MnfPrcSv crPuMnfPrcSv(
    final Map<String, Object> pRvs) throws Exception {
    MnfPrcSv rz = new MnfPrcSv();
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    ISrDrItEnr srDrItEnr = (ISrDrItEnr) this.fctBlc
      .laz(pRvs, ISrDrItEnr.class.getSimpleName());
    rz.setSrDrItEnr(srDrItEnr);
    ISrEntr srEntr = (ISrEntr) this.fctBlc
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    UtlBas utlBas = (UtlBas) this.fctBlc
      .laz(pRvs, UtlBas.class.getSimpleName());
    rz.setUtlBas(utlBas);
    this.procs.put(MnfPrcSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), MnfPrcSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map ItmUlbSv.</p>
   * @param pRvs request scoped vars
   * @return ItmUlbSv
   * @throws Exception - an exception
   */
  private ItmUlbSv crPuItmUlbSv(
    final Map<String, Object> pRvs) throws Exception {
    ItmUlbSv rz = new ItmUlbSv();
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    ISrDrItEnr srDrItEnr = (ISrDrItEnr) this.fctBlc
      .laz(pRvs, ISrDrItEnr.class.getSimpleName());
    rz.setSrDrItEnr(srDrItEnr);
    ISrEntr srEntr = (ISrEntr) this.fctBlc
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    UtlBas utlBas = (UtlBas) this.fctBlc
      .laz(pRvs, UtlBas.class.getSimpleName());
    rz.setUtlBas(utlBas);
    this.procs.put(ItmUlbSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), ItmUlbSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map MnpAcsSv.</p>
   * @param pRvs request scoped vars
   * @return MnpAcsSv
   * @throws Exception - an exception
   */
  private MnpAcsSv<RS> crPuMnpAcsSv(
    final Map<String, Object> pRvs) throws Exception {
    MnpAcsSv<RS> rz = new MnpAcsSv<RS>();
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    this.procs.put(MnpAcsSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), MnpAcsSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map MnpMcsSv.</p>
   * @param pRvs request scoped vars
   * @return MnpMcsSv
   * @throws Exception - an exception
   */
  private MnpMcsSv<RS> crPuMnpMcsSv(
    final Map<String, Object> pRvs) throws Exception {
    MnpMcsSv<RS> rz = new MnpMcsSv<RS>();
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    ISrDrItEnr srDrItEnr = (ISrDrItEnr) this.fctBlc
      .laz(pRvs, ISrDrItEnr.class.getSimpleName());
    rz.setSrDrItEnr(srDrItEnr);
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    this.procs.put(MnpMcsSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), MnpMcsSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map BnStLnSv.</p>
   * @param pRvs request scoped vars
   * @return BnStLnSv
   * @throws Exception - an exception
   */
  private BnStLnSv crPuBnStLnSv(
    final Map<String, Object> pRvs) throws Exception {
    BnStLnSv rz = new BnStLnSv();
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    ISrToPa srToPa = (ISrToPa) this.fctBlc
      .laz(pRvs, ISrToPa.class.getSimpleName());
    rz.setSrToPa(srToPa);
    ISrvDt srvDt = (ISrvDt) this.fctBlc
      .laz(pRvs, ISrvDt.class.getSimpleName());
    rz.setSrvDt(srvDt);
    ISrEntr srEntr = (ISrEntr) this.fctBlc
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    @SuppressWarnings("unchecked")
    RvPuGdLn<RS> rvpl = (RvPuGdLn<RS>)
      this.fctBlc.laz(pRvs, RvPuGdLn.class.getSimpleName());
    rz.setRvPuLn(rvpl);
    @SuppressWarnings("unchecked")
    RvSaGdLn<RS> rvsl = (RvSaGdLn<RS>)
      this.fctBlc.laz(pRvs, RvSaGdLn.class.getSimpleName());
    rz.setRvSaLn(rvsl);
    this.procs.put(BnStLnSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), BnStLnSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map ItUbLnSv.</p>
   * @param pRvs request scoped vars
   * @return ItUbLnSv
   * @throws Exception - an exception
   */
  private ItUbLnSv<RS> crPuItUbLnSv(
    final Map<String, Object> pRvs) throws Exception {
    ItUbLnSv<RS> rz = new ItUbLnSv<RS>();
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    ISrDrItEnr srDrItEnr = (ISrDrItEnr) this.fctBlc
      .laz(pRvs, ISrDrItEnr.class.getSimpleName());
    rz.setSrDrItEnr(srDrItEnr);
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    this.procs.put(ItUbLnSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), ItUbLnSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map MoItLnSv.</p>
   * @param pRvs request scoped vars
   * @return MoItLnSv
   * @throws Exception - an exception
   */
  private MoItLnSv crPuMoItLnSv(
    final Map<String, Object> pRvs) throws Exception {
    MoItLnSv rz = new MoItLnSv();
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    this.procs.put(MoItLnSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), MoItLnSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map MovItmPr.</p>
   * @param pRvs request scoped vars
   * @return MovItmPr
   * @throws Exception - an exception
   */
  private MovItmPr crPuMovItmPr(
    final Map<String, Object> pRvs) throws Exception {
    MovItmPr rz = new MovItmPr();
    @SuppressWarnings("unchecked")
    FctEnPrc<RS> fctEnPrc = (FctEnPrc<RS>) this.fctBlc
      .laz(pRvs, FctEnPrc.class.getSimpleName());
    @SuppressWarnings("unchecked")
    PrcEntRt<MovItm, Long> rtr = (PrcEntRt<MovItm, Long>) fctEnPrc
      .lazPart(pRvs, PrcEntRt.class.getSimpleName());
    rz.setRetrv(rtr);
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    this.procs.put(MovItmPr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), MovItmPr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map DcDriPr.</p>
   * @param pRvs request scoped vars
   * @return DcDriPr
   * @throws Exception - an exception
   */
  private DcDriPr crPuDcDriPr(final Map<String, Object> pRvs) throws Exception {
    DcDriPr rz = new DcDriPr();
    @SuppressWarnings("unchecked")
    FctEnPrc<RS> fctEnPrc = (FctEnPrc<RS>) this.fctBlc
      .laz(pRvs, FctEnPrc.class.getSimpleName());
    @SuppressWarnings("unchecked")
    PrcEntRt<IDcDri, Long> rtr = (PrcEntRt<IDcDri, Long>) fctEnPrc
      .lazPart(pRvs, PrcEntRt.class.getSimpleName());
    rz.setRetrv(rtr);
    ISrEntr srEntr = (ISrEntr) this.fctBlc
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    ISrDrItEnr srDrItEnr = (ISrDrItEnr) this.fctBlc
      .laz(pRvs, ISrDrItEnr.class.getSimpleName());
    rz.setSrDrItEnr(srDrItEnr);
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    this.procs.put(DcDriPr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), DcDriPr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map DocWhPr.</p>
   * @param pRvs request scoped vars
   * @return DocWhPr
   * @throws Exception - an exception
   */
  private DocWhPr crPuDocWhPr(final Map<String, Object> pRvs) throws Exception {
    DocWhPr rz = new DocWhPr();
    @SuppressWarnings("unchecked")
    FctEnPrc<RS> fctEnPrc = (FctEnPrc<RS>) this.fctBlc
      .laz(pRvs, FctEnPrc.class.getSimpleName());
    @SuppressWarnings("unchecked")
    PrcEntRt<IDoci, Long> rtr = (PrcEntRt<IDoci, Long>) fctEnPrc
      .lazPart(pRvs, PrcEntRt.class.getSimpleName());
    rz.setRetrv(rtr);
    ISrEntr srEntr = (ISrEntr) this.fctBlc
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    this.procs.put(DocWhPr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), DocWhPr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map DocDl.</p>
   * @param pRvs request scoped vars
   * @return DocDl
   * @throws Exception - an exception
   */
  private DocDl crPuDocDl(final Map<String, Object> pRvs) throws Exception {
    DocDl rz = new DocDl();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(DocDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), DocDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map DocPr.</p>
   * @param pRvs request scoped vars
   * @return DocPr
   * @throws Exception - an exception
   */
  private DocPr crPuDocPr(final Map<String, Object> pRvs) throws Exception {
    DocPr rz = new DocPr();
    @SuppressWarnings("unchecked")
    FctEnPrc<RS> fctEnPrc = (FctEnPrc<RS>) this.fctBlc
      .laz(pRvs, FctEnPrc.class.getSimpleName());
    @SuppressWarnings("unchecked")
    PrcEntRt<IDoci, Long> rtr = (PrcEntRt<IDoci, Long>) fctEnPrc
      .lazPart(pRvs, PrcEntRt.class.getSimpleName());
    rz.setRetrv(rtr);
    ISrEntr srEntr = (ISrEntr) this.fctBlc
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    this.procs.put(DocPr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), DocPr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map SalGdRetLnSv.</p>
   * @param pRvs request scoped vars
   * @return RetLnSv purchase good
   * @throws Exception - an exception
   */
  private RetLnSv<RS, SalRet, SaRtLn, SaRtTxLn, SaRtLtl> crPuSaRtLnSv(
    final Map<String, Object> pRvs) throws Exception {
    RetLnSv<RS, SalRet, SaRtLn, SaRtTxLn, SaRtLtl> rz =
      new RetLnSv<RS, SalRet, SaRtLn, SaRtTxLn, SaRtLtl>();
    SrRtLnSv srRtLnSv = (SrRtLnSv) this.fctBlc
      .laz(pRvs, SrRtLnSv.class.getSimpleName());
    rz.setSrRtLnSv(srRtLnSv);
    SrSaRtLn sritln = new SrSaRtLn();
    rz.setSrInItLn(sritln);
    sritln.setOrm(this.fctBlc.lazOrm(pRvs));
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    sritln.setSrWrhEnr(srWrhEnr);
    @SuppressWarnings("unchecked")
    RvSaRtLn<RS> rvgl = (RvSaRtLn<RS>)
      this.fctBlc.laz(pRvs, RvSaRtLn.class.getSimpleName());
    sritln.setRvInvLn(rvgl);
    UtInLnTxTo<RS, SalRet, SaRtLn, SaRtTxLn, SaRtLtl> utInTxTo =
      new UtInLnTxTo<RS, SalRet, SaRtLn, SaRtTxLn, SaRtLtl>();
    rz.setUtInTxTo(utInTxTo);
    @SuppressWarnings("unchecked")
    UtInLnTxToBs<RS> utInTxToBs = (UtInLnTxToBs<RS>) this.fctBlc
      .laz(pRvs, UtInLnTxToBs.class.getSimpleName());
    utInTxTo.setUtlInv(utInTxToBs);
    @SuppressWarnings("unchecked")
    InvTxMeth<SalRet, SaRtTxLn> invTxMeth = (InvTxMeth<SalRet, SaRtTxLn>)
      this.fctBlc.laz(pRvs, FctAcc.SARTTXMT);
    utInTxTo.setInvTxMeth(invTxMeth);
    FctOrId<SaRtLtl> fcpigtl = new FctOrId<SaRtLtl>();
    utInTxTo.setFctLineTxLn(fcpigtl);
    fcpigtl.setCls(SaRtLtl.class);
    fcpigtl.setDbOr(sritln.getOrm().getDbId());
    utInTxTo.setLtlCl(SaRtLtl.class);
    utInTxTo.setDstTxItLnCl(ItTxDl.class);
    utInTxTo.setInvLnCl(SaRtLn.class);
    utInTxTo.setItmCl(Itm.class);
    utInTxTo.setIsMutable(false);
    utInTxTo.setNeedMkTxCat(false);
    utInTxTo.setIsPurch(false);
    this.procs.put(SARTLNSV, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      SARTLNSV + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map SalGdRetLnSv.</p>
   * @param pRvs request scoped vars
   * @return RetLnSv purchase good
   * @throws Exception - an exception
   */
  private RetLnSv<RS, PurRet, PuRtLn, PuRtTxLn, PuRtLtl> crPuPuRtLnSv(
    final Map<String, Object> pRvs) throws Exception {
    RetLnSv<RS, PurRet, PuRtLn, PuRtTxLn, PuRtLtl> rz =
      new RetLnSv<RS, PurRet, PuRtLn, PuRtTxLn, PuRtLtl>();
    SrRtLnSv srRtLnSv = (SrRtLnSv) this.fctBlc
      .laz(pRvs, SrRtLnSv.class.getSimpleName());
    rz.setSrRtLnSv(srRtLnSv);
    SrPuRtLn sritln = new SrPuRtLn();
    rz.setSrInItLn(sritln);
    sritln.setOrm(this.fctBlc.lazOrm(pRvs));
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    sritln.setSrWrhEnr(srWrhEnr);
    ISrDrItEnr srDrItEnr = (ISrDrItEnr) this.fctBlc
      .laz(pRvs, ISrDrItEnr.class.getSimpleName());
    sritln.setSrDrItEnr(srDrItEnr);
    @SuppressWarnings("unchecked")
    RvPuRtLn<RS> rvgl = (RvPuRtLn<RS>)
      this.fctBlc.laz(pRvs, RvPuRtLn.class.getSimpleName());
    sritln.setRvInvLn(rvgl);
    UtInLnTxTo<RS, PurRet, PuRtLn, PuRtTxLn, PuRtLtl> utInTxTo =
      new UtInLnTxTo<RS, PurRet, PuRtLn, PuRtTxLn, PuRtLtl>();
    rz.setUtInTxTo(utInTxTo);
    @SuppressWarnings("unchecked")
    UtInLnTxToBs<RS> utInTxToBs = (UtInLnTxToBs<RS>) this.fctBlc
      .laz(pRvs, UtInLnTxToBs.class.getSimpleName());
    utInTxTo.setUtlInv(utInTxToBs);
    @SuppressWarnings("unchecked")
    InvTxMeth<PurRet, PuRtTxLn> invTxMeth = (InvTxMeth<PurRet, PuRtTxLn>)
      this.fctBlc.laz(pRvs, FctAcc.PURTTXMT);
    utInTxTo.setInvTxMeth(invTxMeth);
    FctOrId<PuRtLtl> fcpigtl = new FctOrId<PuRtLtl>();
    utInTxTo.setFctLineTxLn(fcpigtl);
    fcpigtl.setCls(PuRtLtl.class);
    fcpigtl.setDbOr(sritln.getOrm().getDbId());
    utInTxTo.setLtlCl(PuRtLtl.class);
    utInTxTo.setDstTxItLnCl(ItTxDl.class);
    utInTxTo.setInvLnCl(PuRtLn.class);
    utInTxTo.setItmCl(Itm.class);
    utInTxTo.setIsMutable(false);
    utInTxTo.setNeedMkTxCat(false);
    utInTxTo.setIsPurch(true);
    this.procs.put(PURTLNSV, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      PURTLNSV + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map SalSrInTxLnSv.</p>
   * @param pRvs request scoped vars
   * @return InTxLnSv purchase service
   * @throws Exception - an exception
   */
  private InTxLnSv<RS, SalInv, SaInTxLn> crPuSaInTxLnSv(
    final Map<String, Object> pRvs) throws Exception {
    InTxLnSv<RS, SalInv, SaInTxLn> rz =
      new InTxLnSv<RS, SalInv, SaInTxLn>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    UtInLnTxTo<RS, SalInv, ?, SaInTxLn, ?> utInTxTo =
      (UtInLnTxTo<RS, SalInv, ?, SaInTxLn, ?>) this.fctBlc
        .laz(pRvs, FctAcc.UTSAINSRTX);
    rz.setUtInTxTo(utInTxTo);
    this.procs.put(SAINTXLNSV, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      SAINTXLNSV + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map SalSrInvLnSv.</p>
   * @param pRvs request scoped vars
   * @return InvLnSv purchase service
   * @throws Exception - an exception
   */
  private InvLnSv<RS, SalInv, SaInSrLn, SaInTxLn, SaInSrTxLn> crPuSalInvSrLnSv(
    final Map<String, Object> pRvs) throws Exception {
    InvLnSv<RS, SalInv, SaInSrLn, SaInTxLn, SaInSrTxLn> rz =
      new InvLnSv<RS, SalInv, SaInSrLn, SaInTxLn, SaInSrTxLn>();
    SrInLnSv srInLnSv = (SrInLnSv) this.fctBlc
      .laz(pRvs, SrInLnSv.class.getSimpleName());
    rz.setSrInLnSv(srInLnSv);
    SrSaSrLn sritln = new SrSaSrLn();
    rz.setSrInItLn(sritln);
    sritln.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    RvSaSrLn<RS> rvgl = (RvSaSrLn<RS>)
      this.fctBlc.laz(pRvs, RvSaSrLn.class.getSimpleName());
    sritln.setRvInvLn(rvgl);
    @SuppressWarnings("unchecked")
    UtInLnTxTo<RS, SalInv, SaInSrLn, SaInTxLn, SaInSrTxLn> utInTxTo =
      (UtInLnTxTo<RS, SalInv, SaInSrLn, SaInTxLn, SaInSrTxLn>) this.fctBlc
        .laz(pRvs, FctAcc.UTSAINSRTX);
    rz.setUtInTxTo(utInTxTo);
    this.procs.put(SALINVSRLNSV, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      SALINVSRLNSV + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map SalGdInvLnSv.</p>
   * @param pRvs request scoped vars
   * @return InvLnSv purchase good
   * @throws Exception - an exception
   */
  private InvLnSv<RS, SalInv, SaInGdLn, SaInTxLn, SaInGdTxLn> crPuSalInvGdLnSv(
    final Map<String, Object> pRvs) throws Exception {
    InvLnSv<RS, SalInv, SaInGdLn, SaInTxLn, SaInGdTxLn> rz =
      new InvLnSv<RS, SalInv, SaInGdLn, SaInTxLn, SaInGdTxLn>();
    SrInLnSv srInLnSv = (SrInLnSv) this.fctBlc
      .laz(pRvs, SrInLnSv.class.getSimpleName());
    rz.setSrInLnSv(srInLnSv);
    SrSaGdLn sritln = new SrSaGdLn();
    rz.setSrInItLn(sritln);
    sritln.setOrm(this.fctBlc.lazOrm(pRvs));
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    sritln.setSrWrhEnr(srWrhEnr);
    ISrDrItEnr srDrItEnr = (ISrDrItEnr) this.fctBlc
      .laz(pRvs, ISrDrItEnr.class.getSimpleName());
    sritln.setSrDrItEnr(srDrItEnr);
    @SuppressWarnings("unchecked")
    RvSaGdLn<RS> rvgl = (RvSaGdLn<RS>)
      this.fctBlc.laz(pRvs, RvSaGdLn.class.getSimpleName());
    sritln.setRvInvLn(rvgl);
    UtInLnTxTo<RS, SalInv, SaInGdLn, SaInTxLn, SaInGdTxLn> utInTxTo =
      new UtInLnTxTo<RS, SalInv, SaInGdLn, SaInTxLn, SaInGdTxLn>();
    rz.setUtInTxTo(utInTxTo);
    @SuppressWarnings("unchecked")
    UtInLnTxToBs<RS> utInTxToBs = (UtInLnTxToBs<RS>) this.fctBlc
      .laz(pRvs, UtInLnTxToBs.class.getSimpleName());
    utInTxTo.setUtlInv(utInTxToBs);
    @SuppressWarnings("unchecked")
    InvTxMeth<SalInv, SaInTxLn> invTxMeth = (InvTxMeth<SalInv, SaInTxLn>)
      this.fctBlc.laz(pRvs, FctAcc.SALINVTXMETH);
    utInTxTo.setInvTxMeth(invTxMeth);
    FctOrId<SaInGdTxLn> fcpigtl = new FctOrId<SaInGdTxLn>();
    utInTxTo.setFctLineTxLn(fcpigtl);
    fcpigtl.setCls(SaInGdTxLn.class);
    fcpigtl.setDbOr(sritln.getOrm().getDbId());
    utInTxTo.setLtlCl(SaInGdTxLn.class);
    utInTxTo.setDstTxItLnCl(ItTxDl.class);
    utInTxTo.setInvLnCl(SaInGdLn.class);
    utInTxTo.setItmCl(Itm.class);
    utInTxTo.setIsMutable(false);
    utInTxTo.setNeedMkTxCat(true);
    utInTxTo.setIsPurch(false);
    this.procs.put(SALINVGDLNSV, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      SALINVGDLNSV + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map InvSv.</p>
   * @param pRvs request scoped vars
   * @return InvSv
   * @throws Exception - an exception
   */
  private InvSv<SalInv, SaInGdLn, SaInSrLn> crPuSalInvSv(
    final Map<String, Object> pRvs) throws Exception {
    InvSv<SalInv, SaInGdLn, SaInSrLn> rz =
      new InvSv<SalInv, SaInGdLn, SaInSrLn>();
    SrInvSv srInvSv = (SrInvSv) this.fctBlc
      .laz(pRvs, SrInvSv.class.getSimpleName());
    rz.setSrInvSv(srInvSv);
    @SuppressWarnings("unchecked")
    RvSaSrLn<RS> rvsl = (RvSaSrLn<RS>)
      this.fctBlc.laz(pRvs, RvSaSrLn.class.getSimpleName());
    rz.setRvInSrLn(rvsl);
    @SuppressWarnings("unchecked")
    RvSaGdLn<RS> rvgl = (RvSaGdLn<RS>)
      this.fctBlc.laz(pRvs, RvSaGdLn.class.getSimpleName());
    rz.setRvInGdLn(rvgl);
    this.procs.put(SALINVSV, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      SALINVSV + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PurSrInSrLnDl.</p>
   * @param pRvs request scoped vars
   * @return InSrLnDl purchase service
   * @throws Exception - an exception
   */
  private InSrLnDl<RS, SalInv, SaInSrLn, SaInSrTxLn> crPuSaInSrLnDl(
    final Map<String, Object> pRvs) throws Exception {
    InSrLnDl<RS, SalInv, SaInSrLn, SaInSrTxLn> rz =
      new InSrLnDl<RS, SalInv, SaInSrLn, SaInSrTxLn>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    UtInLnTxTo<RS, SalInv, SaInSrLn, SaInTxLn, SaInSrTxLn> utInTxTo =
      (UtInLnTxTo<RS, SalInv, SaInSrLn, SaInTxLn, SaInSrTxLn>) this.fctBlc
        .laz(pRvs, FctAcc.UTSAINSRTX);
    rz.setUtInTxTo(utInTxTo);
    this.procs.put(SAINSRLNDL, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      SAINSRLNDL + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PurSrInSrLnDl.</p>
   * @param pRvs request scoped vars
   * @return InSrLnDl purchase service
   * @throws Exception - an exception
   */
  private InSrLnDl<RS, PurInv, PuInSrLn, PuInSrTxLn> crPuPuInSrLnDl(
    final Map<String, Object> pRvs) throws Exception {
    InSrLnDl<RS, PurInv, PuInSrLn, PuInSrTxLn> rz =
      new InSrLnDl<RS, PurInv, PuInSrLn, PuInSrTxLn>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    UtInLnTxTo<RS, PurInv, PuInSrLn, PuInTxLn, PuInSrTxLn> utInTxTo =
      (UtInLnTxTo<RS, PurInv, PuInSrLn, PuInTxLn, PuInSrTxLn>) this.fctBlc
        .laz(pRvs, FctAcc.UTPUINSRTX);
    rz.setUtInTxTo(utInTxTo);
    this.procs.put(PUINSRLNDL, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      PUINSRLNDL + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PurSrInvLnSv.</p>
   * @param pRvs request scoped vars
   * @return InvLnSv purchase service
   * @throws Exception - an exception
   */
  private InvLnSv<RS, PurInv, PuInSrLn, PuInTxLn, PuInSrTxLn> crPuPurInvSrLnSv(
    final Map<String, Object> pRvs) throws Exception {
    InvLnSv<RS, PurInv, PuInSrLn, PuInTxLn, PuInSrTxLn> rz =
      new InvLnSv<RS, PurInv, PuInSrLn, PuInTxLn, PuInSrTxLn>();
    SrInLnSv srInLnSv = (SrInLnSv) this.fctBlc
      .laz(pRvs, SrInLnSv.class.getSimpleName());
    rz.setSrInLnSv(srInLnSv);
    SrPuSrLn sritln = new SrPuSrLn();
    rz.setSrInItLn(sritln);
    sritln.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    RvPuSrLn<RS> rvgl = (RvPuSrLn<RS>)
      this.fctBlc.laz(pRvs, RvPuSrLn.class.getSimpleName());
    sritln.setRvInvLn(rvgl);
    @SuppressWarnings("unchecked")
    UtInLnTxTo<RS, PurInv, PuInSrLn, PuInTxLn, PuInSrTxLn> utInTxTo =
      (UtInLnTxTo<RS, PurInv, PuInSrLn, PuInTxLn, PuInSrTxLn>) this.fctBlc
        .laz(pRvs, FctAcc.UTPUINSRTX);
    rz.setUtInTxTo(utInTxTo);
    this.procs.put(PURINVSRLNSV, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      PURINVSRLNSV + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PurGdInvLnSv.</p>
   * @param pRvs request scoped vars
   * @return InvLnSv purchase good
   * @throws Exception - an exception
   */
  private InvLnSv<RS, PurInv, PuInGdLn, PuInTxLn, PuInGdTxLn> crPuPurInvGdLnSv(
    final Map<String, Object> pRvs) throws Exception {
    InvLnSv<RS, PurInv, PuInGdLn, PuInTxLn, PuInGdTxLn> rz =
      new InvLnSv<RS, PurInv, PuInGdLn, PuInTxLn, PuInGdTxLn>();
    SrInLnSv srInLnSv = (SrInLnSv) this.fctBlc
      .laz(pRvs, SrInLnSv.class.getSimpleName());
    rz.setSrInLnSv(srInLnSv);
    SrPuGdLn sritln = new SrPuGdLn();
    rz.setSrInItLn(sritln);
    sritln.setOrm(this.fctBlc.lazOrm(pRvs));
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) this.fctBlc
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    sritln.setSrWrhEnr(srWrhEnr);
    @SuppressWarnings("unchecked")
    RvPuGdLn<RS> rvgl = (RvPuGdLn<RS>)
      this.fctBlc.laz(pRvs, RvPuGdLn.class.getSimpleName());
    sritln.setRvInvLn(rvgl);
    UtInLnTxTo<RS, PurInv, PuInGdLn, PuInTxLn, PuInGdTxLn> utInTxTo =
      new UtInLnTxTo<RS, PurInv, PuInGdLn, PuInTxLn, PuInGdTxLn>();
    rz.setUtInTxTo(utInTxTo);
    @SuppressWarnings("unchecked")
    UtInLnTxToBs<RS> utInTxToBs = (UtInLnTxToBs<RS>) this.fctBlc
      .laz(pRvs, UtInLnTxToBs.class.getSimpleName());
    utInTxTo.setUtlInv(utInTxToBs);
    @SuppressWarnings("unchecked")
    InvTxMeth<PurInv, PuInTxLn> invTxMeth = (InvTxMeth<PurInv, PuInTxLn>)
      this.fctBlc.laz(pRvs, FctAcc.PURINVTXMETH);
    utInTxTo.setInvTxMeth(invTxMeth);
    FctOrId<PuInGdTxLn> fcpigtl = new FctOrId<PuInGdTxLn>();
    utInTxTo.setFctLineTxLn(fcpigtl);
    fcpigtl.setCls(PuInGdTxLn.class);
    fcpigtl.setDbOr(sritln.getOrm().getDbId());
    utInTxTo.setLtlCl(PuInGdTxLn.class);
    utInTxTo.setDstTxItLnCl(ItTxDl.class);
    utInTxTo.setInvLnCl(PuInGdLn.class);
    utInTxTo.setItmCl(Itm.class);
    utInTxTo.setIsMutable(false);
    utInTxTo.setNeedMkTxCat(true);
    utInTxTo.setIsPurch(true);
    this.procs.put(PURINVGDLNSV, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      PURINVGDLNSV + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map InvSv.</p>
   * @param pRvs request scoped vars
   * @return InvSv
   * @throws Exception - an exception
   */
  private InvSv<PurInv, PuInGdLn, PuInSrLn> crPuPurInvSv(
    final Map<String, Object> pRvs) throws Exception {
    InvSv<PurInv, PuInGdLn, PuInSrLn> rz =
      new InvSv<PurInv, PuInGdLn, PuInSrLn>();
    SrInvSv srInvSv = (SrInvSv) this.fctBlc
      .laz(pRvs, SrInvSv.class.getSimpleName());
    rz.setSrInvSv(srInvSv);
    @SuppressWarnings("unchecked")
    RvPuSrLn<RS> rvsl = (RvPuSrLn<RS>)
      this.fctBlc.laz(pRvs, RvPuSrLn.class.getSimpleName());
    rz.setRvInSrLn(rvsl);
    @SuppressWarnings("unchecked")
    RvPuGdLn<RS> rvgl = (RvPuGdLn<RS>)
      this.fctBlc.laz(pRvs, RvPuGdLn.class.getSimpleName());
    rz.setRvInGdLn(rvgl);
    this.procs.put(PURINVSV, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      PURINVSV + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map RetSv.</p>
   * @param pRvs request scoped vars
   * @return RetSv
   * @throws Exception - an exception
   */
  private RetSv<SalRet, SalInv, SaRtLn> crPuSalRetSv(
    final Map<String, Object> pRvs) throws Exception {
    RetSv<SalRet, SalInv, SaRtLn> rz =
      new RetSv<SalRet, SalInv, SaRtLn>();
    SrRetSv srRetSv = (SrRetSv) this.fctBlc
      .laz(pRvs, SrRetSv.class.getSimpleName());
    rz.setSrRetSv(srRetSv);
    @SuppressWarnings("unchecked")
    RvSaRtLn<RS> rvgl = (RvSaRtLn<RS>)
      this.fctBlc.laz(pRvs, RvSaRtLn.class.getSimpleName());
    rz.setRvLn(rvgl);
    this.procs.put(SARETSV, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      SARETSV + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map RetSv.</p>
   * @param pRvs request scoped vars
   * @return RetSv
   * @throws Exception - an exception
   */
  private RetSv<PurRet, PurInv, PuRtLn> crPuPurRetSv(
    final Map<String, Object> pRvs) throws Exception {
    RetSv<PurRet, PurInv, PuRtLn> rz =
      new RetSv<PurRet, PurInv, PuRtLn>();
    SrRetSv srRetSv = (SrRetSv) this.fctBlc
      .laz(pRvs, SrRetSv.class.getSimpleName());
    rz.setSrRetSv(srRetSv);
    @SuppressWarnings("unchecked")
    RvPuRtLn<RS> rvgl = (RvPuRtLn<RS>)
      this.fctBlc.laz(pRvs, RvPuRtLn.class.getSimpleName());
    rz.setRvLn(rvgl);
    this.procs.put(PURETSV, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      PURETSV + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PaymSv.</p>
   * @param pRvs request scoped vars
   * @return PaymSv
   * @throws Exception - an exception
   */
  private PaymSv<PaymTo, PurInv> crPuPaymToSv(
    final Map<String, Object> pRvs) throws Exception {
    PaymSv<PaymTo, PurInv> rz = new PaymSv<PaymTo, PurInv>();
    SrPaymSv srPaymSv = (SrPaymSv) this.fctBlc
      .laz(pRvs, SrPaymSv.class.getSimpleName());
    rz.setSrPaymSv(srPaymSv);
    @SuppressWarnings("unchecked")
    RvPuGdLn<RS> rvgl = (RvPuGdLn<RS>)
      this.fctBlc.laz(pRvs, RvPuGdLn.class.getSimpleName());
    rz.setRvLn(rvgl);
    this.procs.put(PAYTOSV, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PAYTOSV
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PaymSv.</p>
   * @param pRvs request scoped vars
   * @return PaymSv
   * @throws Exception - an exception
   */
  private PaymSv<PaymFr, SalInv> crPuPaymFrSv(
    final Map<String, Object> pRvs) throws Exception {
    PaymSv<PaymFr, SalInv> rz = new PaymSv<PaymFr, SalInv>();
    SrPaymSv srPaymSv = (SrPaymSv) this.fctBlc
      .laz(pRvs, SrPaymSv.class.getSimpleName());
    rz.setSrPaymSv(srPaymSv);
    @SuppressWarnings("unchecked")
    RvSaGdLn<RS> rvgl = (RvSaGdLn<RS>)
      this.fctBlc.laz(pRvs, RvSaGdLn.class.getSimpleName());
    rz.setRvLn(rvgl);
    this.procs.put(PAYFRSV, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PAYFRSV
      + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrepSv.</p>
   * @param pRvs request scoped vars
   * @return PrepSv
   * @throws Exception - an exception
   */
  private PrepSv crPuPrepSv(
    final Map<String, Object> pRvs) throws Exception {
    PrepSv rz = new PrepSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    UtlBas utlBas = (UtlBas) this.fctBlc
      .laz(pRvs, UtlBas.class.getSimpleName());
    rz.setUtlBas(utlBas);
    ISrEntr srEntr = (ISrEntr) this.fctBlc
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    this.procs.put(PrepSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PrepSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map EntrSv.</p>
   * @param pRvs request scoped vars
   * @return EntrSv
   * @throws Exception - an exception
   */
  private EntrSv<RS> crPuEntrSv(
    final Map<String, Object> pRvs) throws Exception {
    EntrSv<RS> rz = new EntrSv<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    ISrBlnc srBlnc = (ISrBlnc) this.fctBlc
      .laz(pRvs, ISrBlnc.class.getSimpleName());
    rz.setSrBlnc(srBlnc);
    this.procs.put(EntrSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), EntrSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map EntrSrcCr.</p>
   * @param pRvs request scoped vars
   * @return EntrSrcCr
   * @throws Exception - an exception
   */
  private EntrSrcCr crPuEntrSrcCr(
    final Map<String, Object> pRvs) throws Exception {
    EntrSrcCr rz = new EntrSrcCr();
    this.procs.put(EntrSrcCr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), EntrSrcCr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map SacntSv.</p>
   * @param pRvs request scoped vars
   * @return SacntSv
   * @throws Exception - an exception
   */
  private SacntSv crPuSacntSv(
    final Map<String, Object> pRvs) throws Exception {
    SacntSv rz = new SacntSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(SacntSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), SacntSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map SacntCr.</p>
   * @param pRvs request scoped vars
   * @return SacntCr
   * @throws Exception - an exception
   */
  private SacntCr crPuSacntCr(
    final Map<String, Object> pRvs) throws Exception {
    SacntCr rz = new SacntCr();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(SacntCr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), SacntCr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map BnStLnGfe.</p>
   * @param pRvs request scoped vars
   * @return BnStLnGfe
   * @throws Exception - an exception
   */
  private BnStLnGfe crPuBnStLnGfe(
    final Map<String, Object> pRvs) throws Exception {
    BnStLnGfe rz = new BnStLnGfe();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(BnStLnGfe.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), BnStLnGfe.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map EntrRt.</p>
   * @param pRvs request scoped vars
   * @return EntrRt
   * @throws Exception - an exception
   */
  private EntrRt crPuEntrRt(
    final Map<String, Object> pRvs) throws Exception {
    EntrRt rz = new EntrRt();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(EntrRt.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), EntrRt.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map EnrSrcChu.</p>
   * @param pRvs request scoped vars
   * @return EnrSrcChu
   * @throws Exception - an exception
   */
  private EnrSrcChu crPuEnrSrcChu(
    final Map<String, Object> pRvs) throws Exception {
    EnrSrcChu rz = new EnrSrcChu();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    ISrEntr srEntr = (ISrEntr) this.fctBlc
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    ISrDrItEnr srDrItEnr = (ISrDrItEnr) this.fctBlc
      .laz(pRvs, ISrDrItEnr.class.getSimpleName());
    rz.setSrDrItEnr(srDrItEnr);
    this.procs.put(EnrSrcChu.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), EnrSrcChu.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map EntrChd.</p>
   * @param pRvs request scoped vars
   * @return EntrChd
   * @throws Exception - an exception
   */
  private EntrChd crPuEntrChd(
    final Map<String, Object> pRvs) throws Exception {
    EntrChd rz = new EntrChd();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(EntrChd.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), EntrChd.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map EntrCpr.</p>
   * @param pRvs request scoped vars
   * @return EntrCpr
   * @throws Exception - an exception
   */
  private EntrCpr crPuEntrCpr(
    final Map<String, Object> pRvs) throws Exception {
    EntrCpr rz = new EntrCpr();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(EntrCpr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), EntrCpr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map EntrCr.</p>
   * @param pRvs request scoped vars
   * @return EntrCr
   * @throws Exception - an exception
   */
  private EntrCr crPuEntrCr(
    final Map<String, Object> pRvs) throws Exception {
    EntrCr rz = new EntrCr();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    this.procs.put(EntrCr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), EntrCr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctBlc.</p>
   * @return FctBlc<RS>
   **/
  public final FctBlc<RS> getFctBlc() {
    return this.fctBlc;
  }

  /**
   * <p>Setter for fctBlc.</p>
   * @param pFctBlc reference
   **/
  public final void setFctBlc(final FctBlc<RS> pFctBlc) {
    this.fctBlc = pFctBlc;
  }
}
