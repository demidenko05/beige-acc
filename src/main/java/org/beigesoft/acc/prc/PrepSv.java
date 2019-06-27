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
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.acc.mdlb.APrep;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Sacnt;
import org.beigesoft.acc.srv.UtlBas;
import org.beigesoft.acc.srv.ISrEntr;

/**
 * <p>Service that saves prepayment into DB.</p>
 *
 * @author Yury Demidenko
 */
public class PrepSv implements IPrcEnt<APrep, Long> {

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
   * <p>Process that saves entity.</p>
   * @param pRvs request scoped vars, e.g. return this line's
   * owner(document) in "nextEntity" for farther processing
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final APrep process(final Map<String, Object> pRvs, final APrep pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    if (pEnt.getRvId() != null) {
      APrep revd = pEnt.getClass().newInstance();
      revd.setIid(pEnt.getRvId());
      this.orm.refrEnt(pRvs, vs, revd);
      this.utlBas.chDtForg(pRvs, revd, revd.getDat());
      if (revd.getInvId() != null) {
        throw new ExcCode(ExcCode.WRPR, "reverse_inv_first");
      }
      pEnt.setDbOr(this.orm.getDbId());
      pEnt.setDbcr(revd.getDbcr());
      pEnt.setAcc(revd.getAcc());
      pEnt.setSaId(revd.getSaId());
      pEnt.setSaNm(revd.getSaNm());
      this.srEntr.revEntrs(pRvs, pEnt, revd);
      pRvs.put("msgSuc", "reverse_ok");
    } else {
      if (pEnt.getTot().compareTo(BigDecimal.ZERO) == 0) {
        throw new ExcCode(ExcCode.WRPR, "amount_eq_zero");
      }
      if (pEnt.getAcc() == null) {
        throw new ExcCode(ExcCode.WRPR, "account_is_null");
      }
      this.utlBas.chDtForg(pRvs, pEnt, pEnt.getDat());
      if (pEnt.getAcc().getSaTy() != null) {
        if (pEnt.getSaId() == null) {
          throw new ExcCode(ExcCode.WRPR, "select_subaccount");
        } else {
          Sacnt sa = getOrm().retEntCnd(pRvs, vs, Sacnt.class, "OWNR='"
            + pEnt.getAcc().getIid() + "' and SAID=" + pEnt.getSaId());
          if (sa == null) {
            throw new ExcCode(ExcCode.WRPR, "wrong_subaccount");
          }
          pEnt.setSaNm(sa.getSaNm());
        }
      }
      AcStg astg = (AcStg) pRvs.get("astg");
      pEnt.setTot(pEnt.getTot().setScale(astg.getPrDp(), astg.getRndm()));
      pEnt.setToFc(pEnt.getToFc().setScale(astg.getPrDp(), astg.getRndm()));
      if ("mkEnr".equals(pRqDt.getParam("acAd"))) {
        if (!pEnt.getIsNew()) {
          APrep old = this.orm.retEnt(pRvs, vs, pEnt);
          if (old.getMdEnr()) {
            throw new ExcCode(ExcCode.SPAM, "Attempt account accounted!");
          }
        } else {
          this.orm.insIdLn(pRvs, vs, pEnt);
          pRvs.put("docFdsUpd", new String[] {"mdEnr", "ver"});
        }
        this.srEntr.mkEntrs(pRvs, pEnt);
        pRvs.remove("docFdsUpd");
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
