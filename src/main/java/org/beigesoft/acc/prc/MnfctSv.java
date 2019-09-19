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
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.acc.mdl.MnfFdDe;
import org.beigesoft.acc.mdl.MnfFdWe;
import org.beigesoft.acc.mdlp.Mnfct;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.srv.ISrEntr;
import org.beigesoft.acc.srv.ISrWrhEnr;
import org.beigesoft.acc.srv.ISrDrItEnr;
import org.beigesoft.acc.srv.UtlBas;

/**
 * <p>Service that saves manufacturing into DB.</p>
 *
 * @author Yury Demidenko
 */
public class MnfctSv implements IPrcEnt<Mnfct, Long> {

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
   * <p>Warehouse entries service.</p>
   **/
  private ISrWrhEnr srWrhEnr;

  /**
   * <p>Draw item entries service.</p>
   **/
  private ISrDrItEnr srDrItEnr;

  /**
   * <p>Process that saves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final Mnfct process(final Map<String, Object> pRvs, final Mnfct pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    this.orm.refrEnt(pRvs, vs, pEnt.getMnp());
    MnfFdWe mnfFdWe = new MnfFdWe(pEnt);
    MnfFdDe mnfFdDe = new MnfFdDe(pEnt);
    if (pEnt.getRvId() != null) {
      Mnfct revd = new Mnfct();
      revd.setIid(pEnt.getRvId());
      this.orm.refrEnt(pRvs, vs, revd);
      if (revd.getQuan().compareTo(revd.getItLf()) == 1) {
        throw new ExcCode(ExcCode.WRPR, "where_is_withdraw");
      }
      this.utlBas.chDtForg(pRvs, revd, revd.getDat());
      pEnt.setDbOr(this.orm.getDbId());
      pEnt.setTot(revd.getTot().negate());
      this.srEntr.revEntrs(pRvs, pEnt, revd);
      this.srDrItEnr.rvDraw(pRvs, mnfFdDe);
      this.srWrhEnr.revDraw(pRvs, mnfFdWe);
      this.srWrhEnr.revLoad(pRvs, pEnt);
      pRvs.put("msgSuc", "reverse_ok");
    } else {
      this.utlBas.chDtForg(pRvs, pEnt, pEnt.getDat());
      if (pEnt.getQuan().compareTo(BigDecimal.ZERO) <= 0) {
        throw new ExcCode(ExcCode.WRPR, "quantity_less_or_equal_zero");
      }
      if (pEnt.getQuan().compareTo(pEnt.getMnp().getItLf()) == 1) {
        throw new ExcCode(ExcCode.WRPR, "LINE_HAS_NO_GOODS");
      }
      pEnt.setItLf(pEnt.getQuan());
      AcStg as = (AcStg) pRvs.get("astg");
      if (pEnt.getMnp().getItLf().compareTo(pEnt.getQuan()) == 0) {
        pEnt.setTot(pEnt.getMnp().getToLf());
      } else {
        pEnt.setTot(pEnt.getMnp().getToLf().divide(pEnt.getMnp().getItLf(),
          as.getCsDp(), as.getRndm()).multiply(pEnt.getQuan())
            .setScale(as.getCsDp(), as.getRndm()));
      }
      pEnt.setPri(pEnt.getTot().divide(pEnt.getQuan(),
        as.getCsDp(), as.getRndm()));
      pEnt.setToLf(pEnt.getTot());
      if (pEnt.getIsNew()) {
        if ("mkEnr".equals(pRqDt.getParam("acAd"))) {
          pEnt.setMdEnr(true);
        } else {
          pRvs.put("msgSuc", "insert_ok");
        }
        this.orm.insIdLn(pRvs, vs, pEnt);
      } else {
        String[] slFds = new String[] {"tot", "mdEnr"};
        Arrays.sort(slFds);
        vs.put("MnfctndFds", slFds);
        Mnfct old = this.orm.retEnt(pRvs, vs, pEnt); vs.clear();
        pEnt.setMdEnr(old.getMdEnr());
        if (pEnt.getMdEnr()) {
          throw new ExcCode(ExcCode.SPAM, "Trying to change accounted!");
        }
        if ("mkEnr".equals(pRqDt.getParam("acAd"))) {
          if (old.getTot().compareTo(BigDecimal.ZERO) == 0) {
            throw new ExcCode(ExcCode.WRPR, "amount_eq_zero");
          }
          pEnt.setMdEnr(true);
        } else {
          pRvs.put("msgSuc", "update_ok");
        }
        getOrm().update(pRvs, vs, pEnt);
      }
      if ("mkEnr".equals(pRqDt.getParam("acAd"))) {
        this.srDrItEnr.drawFr(pRvs, mnfFdDe, pEnt.getMnp(), pEnt.getQuan());
        this.srWrhEnr.draw(pRvs, mnfFdWe, pEnt.getWhpo());
        this.srWrhEnr.load(pRvs, pEnt, pEnt.getWrhp());
        this.srEntr.mkEntrs(pRvs, pEnt);
        pRvs.put("msgSuc", "account_ok");
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
   * <p>Getter for srWrhEnr.</p>
   * @return ISrWrhEnr
   **/
  public final ISrWrhEnr getSrWrhEnr() {
    return this.srWrhEnr;
  }

  /**
   * <p>Setter for srWrhEnr.</p>
   * @param pSrWrhEnr reference
   **/
  public final void setSrWrhEnr(final ISrWrhEnr pSrWrhEnr) {
    this.srWrhEnr = pSrWrhEnr;
  }

  /**
   * <p>Getter for srDrItEnr.</p>
   * @return ISrDrItEnr
   **/
  public final ISrDrItEnr getSrDrItEnr() {
    return this.srDrItEnr;
  }

  /**
   * <p>Setter for srDrItEnr.</p>
   * @param pSrDrItEnr reference
   **/
  public final void setSrDrItEnr(final ISrDrItEnr pSrDrItEnr) {
    this.srDrItEnr = pSrDrItEnr;
  }
}
