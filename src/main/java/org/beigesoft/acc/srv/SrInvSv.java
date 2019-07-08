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

package org.beigesoft.acc.srv;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.mdlb.AInvLn;
import org.beigesoft.acc.mdlp.Itm;
import org.beigesoft.acc.mdlp.Srv;

/**
 * <p>Service that saves invoice into DB.
 * It's type-safe part (shared code) that is used inside type safe
 * processor-assembly.</p>
 *
 * @author Yury Demidenko
 */
public class SrInvSv {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Entries service.</p>
   **/
  private ISrEntr srEntr;

  /**
   * <p>Base service.</p>
   **/
  private UtlBas utlBas;

  /**
   * <p>Total payment service.</p>
   **/
  private ISrToPa srToPa;

  /**
   * <p>Saves entity generic method.</p>
   * @param <T> invoice type
   * @param <G> invoice goods line type
   * @param <S> invoice service line type
   * @param pRvs request scoped vars
   * @param pEnt Entity to process
   * @param pRqDt Request Data
   * @param pRvGdLn reverser good line
   * @param pRvSrLn reverser service line
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  public final <T extends AInv, G extends AInvLn<T, Itm>,
    S extends AInvLn<T, Srv>> T save(final Map<String, Object> pRvs,
      final T pEnt, final IReqDt pRqDt, final IRvInvLn<T, G> pRvGdLn,
        final IRvInvLn<T, S> pRvSrLn) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    if (pEnt.getRvId() != null) {
      @SuppressWarnings("unchecked")
      T revd = (T) pEnt.getClass().newInstance();
      revd.setIid(pEnt.getRvId());
      this.orm.refrEnt(pRvs, vs, revd);
      this.utlBas.chDtForg(pRvs, revd, revd.getDat());
      if (revd.getPrep() != null) {
        vs.put(revd.getPrep().getClass().getSimpleName() + "ndFds",
          new String[] {"iid", "tot", "ver"});
        this.orm.refrEnt(pRvs, vs, revd.getPrep()); vs.clear();
      }
      if (revd.getPrep() == null && revd.getToPa().compareTo(BigDecimal
        .ZERO) == 1 || revd.getToPa().compareTo(revd.getPrep().getTot()) == 1) {
        throw new ExcCode(ExcCode.WRPR, "reverse_pay_first");
      }
      if (revd.getPrep() != null) {
        revd.getPrep().setInvId(null);
        vs.put("ndFds", new String[] {"invId"});
        this.orm.update(pRvs, vs, revd.getPrep()); vs.clear();
      }
      pEnt.setDbOr(this.orm.getDbId());
      pEnt.setDbcr(revd.getDbcr());
      pEnt.setSubt(revd.getSubt().negate());
      pEnt.setSuFc(revd.getSuFc().negate());
      pEnt.setToTx(revd.getToTx().negate());
      pEnt.setTxFc(revd.getTxFc().negate());
      pEnt.setTot(revd.getTot().negate());
      pEnt.setToFc(revd.getToFc().negate());
      this.srEntr.revEntrs(pRvs, pEnt, revd);
      pRvs.put("msgSuc", "reverse_ok");
      //for purchase goods lines it also checks for withdrawals:
      List<G> gdLns = pRvGdLn.retChkLns(pRvs, vs, revd);
      for (G rvdLn : gdLns) {
        @SuppressWarnings("unchecked")
        G rvgLn = (G) rvdLn.getClass().newInstance();
        rvgLn.setOwnr(pEnt);
        rvgLn.setDbOr(this.orm.getDbId());
        rvgLn.setRvId(rvdLn.getIid());
        rvgLn.setItm(rvdLn.getItm());
        rvgLn.setUom(rvdLn.getUom());
        rvgLn.setTxCt(rvdLn.getTxCt());
        rvgLn.setPri(rvdLn.getPri());
        rvgLn.setPrFc(rvdLn.getPrFc());
        rvgLn.setQuan(rvdLn.getQuan().negate());
        rvgLn.setSubt(rvdLn.getSubt().negate());
        rvgLn.setSuFc(rvdLn.getSuFc().negate());
        rvgLn.setToTx(rvdLn.getToTx().negate());
        rvgLn.setTxFc(rvdLn.getTxFc().negate());
        rvgLn.setTot(rvdLn.getTot().negate());
        rvgLn.setToFc(rvdLn.getToFc().negate());
        rvgLn.setTdsc(rvdLn.getTdsc());
        //it also inserts reversing and updates reversed
        //for good it also makes warehouse reversing
        //for sales good it also makes draw item reversing
        //It removes line tax lines:
        pRvGdLn.revLns(pRvs, vs, pEnt, rvgLn, rvdLn);
      }
      List<S> srLns = pRvSrLn.retChkLns(pRvs, vs, revd);
      for (S rvdLn : srLns) {
        @SuppressWarnings("unchecked")
        S rvgLn = (S) rvdLn.getClass().newInstance();
        rvgLn.setOwnr(pEnt);
        rvgLn.setDbOr(this.orm.getDbId());
        rvgLn.setRvId(rvdLn.getIid());
        rvgLn.setItm(rvdLn.getItm());
        rvgLn.setUom(rvdLn.getUom());
        rvgLn.setTxCt(rvdLn.getTxCt());
        rvgLn.setPri(rvdLn.getPri());
        rvgLn.setPrFc(rvdLn.getPrFc());
        rvgLn.setQuan(rvdLn.getQuan().negate());
        rvgLn.setSubt(rvdLn.getSubt().negate());
        rvgLn.setSuFc(rvdLn.getSuFc().negate());
        rvgLn.setToTx(rvdLn.getToTx().negate());
        rvgLn.setTxFc(rvdLn.getTxFc().negate());
        rvgLn.setTot(rvdLn.getTot().negate());
        rvgLn.setToFc(rvdLn.getToFc().negate());
        rvgLn.setTdsc(rvdLn.getTdsc());
        //it also sets service line specific fields - acc...
        pRvSrLn.revLns(pRvs, vs, pEnt, rvgLn, rvdLn);
      }
    } else {
      this.utlBas.chDtForg(pRvs, pEnt, pEnt.getDat());
      String[] prUpFds = new String[] {"invId", "ver"};
      Arrays.sort(prUpFds);
      if (!pEnt.getIsNew()) {
        String[] ndf = new String[] {"dbcr", "iid", "inTx", "omTx",
          "prep", "tot", "mdEnr", "exRt", "cuFr"};
        Arrays.sort(ndf);
        vs.put(pEnt.getClass().getSimpleName() + "ndFds", ndf);
        vs.put("DbCrndFds", new String[] {"iid", "txDs"});
        String[] fdIid = new String[] {"iid"};
        vs.put("CurrndFds", fdIid);
        vs.put(pEnt.getPrepCls().getSimpleName() + "ndFds", fdIid);
        T old = this.orm.retEnt(pRvs, vs, pEnt); vs.clear();
        pEnt.setMdEnr(old.getMdEnr());
        if (pEnt.getMdEnr()) {
          throw new ExcCode(ExcCode.SPAM, "Trying to change accounted!");
        }
        if (pEnt.getPrep() != null) {
          vs.put(pEnt.getPrepCls().getSimpleName() + "ndFds",
            new String[] {"dbcr", "iid"});
          vs.put("DbCrndFds", fdIid);
          this.orm.refrEnt(pRvs, vs, pEnt.getPrep()); vs.clear();
          if (!pEnt.getDbcr().getIid()
            .equals(pEnt.getPrep().getDbcr().getIid())) {
        throw new ExcCode(ExcCode.WRPR, "wrong_debtor_creditor_for_prepayment");
          }
        }
        String[] fdDcUpd;
        if (old.getTot().compareTo(BigDecimal.ZERO) == 1) {
          if (!old.getExRt().equals(pEnt.getExRt())) {
            throw new ExcCode(ExcCode.SPAM, "Attempt to change exchange rate!");
          }
          if (old.getCuFr() == null && pEnt.getCuFr() != null
            || old.getCuFr() != null && pEnt.getCuFr() == null
              || old.getCuFr() != null && pEnt.getCuFr() != null
                && !old.getCuFr().getIid().equals(pEnt.getCuFr().getIid())) {
            throw new ExcCode(ExcCode.SPAM, "Attempt to change currency!");
          }
          if (!old.getOmTx().equals(pEnt.getOmTx())
            || !old.getInTx().equals(pEnt.getInTx())) {
            throw new ExcCode(ExcCode.SPAM, "Attempt to change tax method!");
          }
          vs.put("DbCrndFds", new String[] {"iid", "txDs"});
          this.orm.refrEnt(pRvs, vs, pEnt.getDbcr()); vs.clear();
          if (!old.getDbcr().getIid().equals(pEnt.getDbcr().getIid())
        && (pEnt.getDbcr().getTxDs() == null && old.getDbcr().getTxDs() != null
      || pEnt.getDbcr().getTxDs() != null && old.getDbcr().getTxDs() == null
    || pEnt.getDbcr().getTxDs().getIid().equals(old.getDbcr()
  .getTxDs().getIid()))) {
            throw new ExcCode(ExcCode.WRPR,
              "can_not_cange_customer_with_another_tax_destination");
          }
          if (pEnt.getDbcr().getTxDs() == null) {
            fdDcUpd = new String[] {"dat", "dscr", "dbcr", "mdEnr", "payb",
              "pdsc", "prep", "toPa", "paFc", "ver"};
          } else {
            fdDcUpd = new String[] {"dat", "dscr", "mdEnr", "payb", "pdsc",
              "prep", "toPa", "paFc", "ver"};
          }
        } else {
          fdDcUpd = new String[] {"cuFr", "exRt", "dat", "dbcr", "dscr", "inTx",
            "mdEnr", "omTx", "payb", "pdsc", "prep", "toPa", "paFc", "ver"};
        }
        boolean ndUpToPa = false;
        if (old.getPrep() != null && (pEnt.getPrep() == null
            || !old.getPrep().getIid().equals(pEnt.getPrep().getIid()))) {
          old.getPrep().setInvId(null);
          vs.put("ndFds", prUpFds);
          this.orm.update(pRvs, vs, old.getPrep()); vs.clear();
          ndUpToPa = true;
        }
        if (pEnt.getPrep() != null && (old.getPrep() == null
          || !old.getPrep().getIid().equals(pEnt.getPrep().getIid()))) {
          pEnt.getPrep().setInvId(pEnt.getIid());
          vs.put("ndFds", prUpFds);
          this.orm.update(pRvs, vs, pEnt.getPrep()); vs.clear();
          ndUpToPa = true;
        }
        if (ndUpToPa) {
          this.srToPa.mkToPa(pRvs, pEnt);
        }
        Arrays.sort(fdDcUpd);
        if ("mkEnr".equals(pRqDt.getParam("acAd"))) {
          if (old.getTot().compareTo(BigDecimal.ZERO) == 0) {
            throw new ExcCode(ExcCode.WRPR, "amount_eq_zero");
          }
          pRvs.put(ISrEntr.DOCFDSUPD, fdDcUpd);
          this.srEntr.mkEntrs(pRvs, pEnt);
          pRvs.remove(ISrEntr.DOCFDSUPD);
          pRvs.put("msgSuc", "account_ok");
        } else {
          vs.put("ndFds", fdDcUpd);
          this.orm.update(pRvs, vs, pEnt); vs.clear();
          pRvs.put("msgSuc", "update_ok");
        }
      } else {
        if (pEnt.getPrep() != null) {
          this.srToPa.mkToPa(pRvs, pEnt);
        }
        this.orm.insIdLn(pRvs, vs, pEnt);
        if (pEnt.getPrep() != null) {
          pEnt.getPrep().setInvId(pEnt.getIid());
          vs.put("ndFds", prUpFds);
          this.orm.update(pRvs, vs, pEnt.getPrep()); vs.clear();
        }
      }
    }
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setEnt(pEnt);
    return pEnt;
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
   * <p>Getter for utlBas.</p>
   * @return UtlBas
   **/
  public final UtlBas getUtlBas() {
    return this.utlBas;
  }

  /**
   * <p>Setter for utlBas.</p>
   * @param pUtlBas reference
   **/
  public final void setUtlBas(final UtlBas pUtlBas) {
    this.utlBas = pUtlBas;
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
}
