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
      if (revd.getToPa().compareTo(BigDecimal.ZERO) == 1) {
        if (revd.getPrep() != null) {
          vs.put(revd.getPrep().getClass().getSimpleName() + "ndFds",
            new String[] {"iid", "tot"});
          this.orm.refrEnt(pRvs, vs, revd.getPrep()); vs.clear();
        }
        if (revd.getPrep() == null
          || revd.getToPa().compareTo(revd.getPrep().getTot()) == 1) {
          throw new ExcCode(ExcCode.WRPR, "reverse_pay_first");
        }
        if (revd.getPrep() != null) {
          revd.getPrep().setInvId(null);
          vs.put("ndFds", new String[] {"invId"});
          this.orm.update(pRvs, vs, revd.getPrep()); vs.clear();
        }
        pEnt.setToPa(revd.getToPa().negate());
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
      List<G> gdLns = pRvGdLn.retChkLns(pRvs, vs, pEnt);
      for (G rvdLn : gdLns) {
        @SuppressWarnings("unchecked")
        G rvgLn = (G) rvdLn.getClass().newInstance();
        rvgLn.setOwnr(pEnt);
        rvgLn.setDbOr(this.orm.getDbId());
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
      List<S> srLns = pRvSrLn.retChkLns(pRvs, vs, pEnt);
      for (S rvdLn : srLns) {
        @SuppressWarnings("unchecked")
        S rvgLn = (S) rvdLn.getClass().newInstance();
        rvgLn.setOwnr(pEnt);
        rvgLn.setDbOr(this.orm.getDbId());
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
        pRvSrLn.revLns(pRvs, vs, pEnt, rvgLn, rvdLn);
      }
    } else {
      this.utlBas.chDtForg(pRvs, pEnt, pEnt.getDat());
      if (!pEnt.getIsNew()) {
        vs.put(pEnt.getClass().getSimpleName() + "ndFds",
          new String[] {"dbcr", "iid", "inTx", "omTx", "prep"});
        vs.put("DbCrndFds", new String[] {"iid", "txDs"});
        vs.put(pEnt.getPrepCls().getSimpleName() + "ndFds",
          new String[] {"iid"});
        T old = this.orm.retEnt(pRvs, vs, pEnt); vs.clear();
        if (pEnt.getTot().compareTo(BigDecimal.ZERO) == 1) {
          if (!old.getOmTx().equals(pEnt.getOmTx())
            || !old.getInTx().equals(pEnt.getInTx())) {
            throw new ExcCode(ExcCode.WRPR, "can_not_change_tax_method");
          }
          vs.put("DbCrndFds", new String[] {"iid", "txDs"});
          this.orm.refrEnt(pRvs, vs, pEnt.getDbcr()); vs.clear();
          if (!old.getDbcr().getIid().equals(pEnt.getDbcr().getIid())
        && (pEnt.getDbcr().getTxDs() == null && old.getDbcr().getTxDs() != null)
      || pEnt.getDbcr().getTxDs() != null && old.getDbcr().getTxDs() == null
    || pEnt.getDbcr().getTxDs().getIid().equals(old.getDbcr()
  .getTxDs().getIid())) {
            throw new ExcCode(ExcCode.WRPR,
              "can_not_cange_customer_with_another_tax_destination");
          }
        }
        if (old.getPrep() != null && pEnt.getPrep() == null
          || old.getPrep() == null && pEnt.getPrep() != null
           || !old.getPrep().getIid().equals(pEnt.getPrep().getIid())) {
          old.getPrep().setInvId(null);
          vs.put("ndFds", new String[] {"invId"});
          this.orm.update(pRvs, vs, old.getPrep()); vs.clear();
        }
      }
      if ("mkEnr".equals(pRqDt.getParam("acAd"))) {
        if (pEnt.getTot().compareTo(BigDecimal.ZERO) == 0) {
          throw new ExcCode(ExcCode.WRPR, "amount_eq_zero");
        }
        if (pEnt.getIsNew()) {
          this.orm.insIdLn(pRvs, vs, pEnt);
        }
        this.srEntr.mkEntrs(pRvs, pEnt);
        pRvs.put("msgSuc", "account_ok");
      } else {
        if (pEnt.getIsNew()) {
          this.orm.insIdLn(pRvs, vs, pEnt);
          pRvs.put("msgSuc", "insert_ok");
        } else {
          this.orm.update(pRvs, vs, pEnt);
          pRvs.put("msgSuc", "update_ok");
        }
      }
      if (pEnt.getPrep() != null) {
        pEnt.getPrep().setInvId(pEnt.getIid());
        vs.put("ndFds", new String[] {"invId"});
        this.orm.update(pRvs, vs, pEnt.getPrep()); vs.clear();
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
}
