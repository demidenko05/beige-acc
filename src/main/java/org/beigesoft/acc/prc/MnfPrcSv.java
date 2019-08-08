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
import java.util.List;
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.srv.II18n;
import org.beigesoft.acc.mdlp.MnfPrc;
import org.beigesoft.acc.mdlp.MnpMcs;
import org.beigesoft.acc.mdlp.MnpAcs;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.srv.ISrEntr;
import org.beigesoft.acc.srv.ISrWrhEnr;
import org.beigesoft.acc.srv.ISrDrItEnr;
import org.beigesoft.acc.srv.UtlBas;

/**
 * <p>Service that saves manufacturing process into DB.</p>
 *
 * @author Yury Demidenko
 */
public class MnfPrcSv implements IPrcEnt<MnfPrc, Long> {

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
   * <p>I18N service.</p>
   */
  private II18n i18n;

  /**
   * <p>Process that saves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final MnfPrc process(final Map<String, Object> pRvs, final MnfPrc pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    if (pEnt.getRvId() != null) {
      MnfPrc revd = new MnfPrc();
      revd.setIid(pEnt.getRvId());
      this.orm.refrEnt(pRvs, vs, revd);
      this.utlBas.chDtForg(pRvs, revd, revd.getDat());
      pEnt.setDbOr(this.orm.getDbId());
      pEnt.setTot(revd.getTot().negate());
      this.srEntr.revEntrs(pRvs, pEnt, revd);
      vs.put("MnpMcsdpLv", 1);
      List<MnpMcs> rdmls = this.orm.retLstCnd(pRvs, vs, MnpMcs.class,
        "where OWNR=" + revd.getIid()); vs.clear();
      CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
      for (MnpMcs rdml : rdmls) {
        MnpMcs rgl = new MnpMcs();
        rgl.setDbOr(this.orm.getDbId());
        rgl.setOwnr(pEnt);
        rgl.setRvId(rdml.getIid());
        rgl.setItm(rdml.getItm());
        rgl.setUom(rdml.getUom());
        rgl.setWhpo(rdml.getWhpo());
        rgl.setQuan(rdml.getQuan().negate());
        StringBuffer sb = new StringBuffer();
        if (rgl.getDscr() != null) {
          sb.append(rgl.getDscr() + " !");
        }
        sb.append(getI18n().getMsg("reversed", cpf.getLngDef().getIid()));
        sb.append(" #" + rdml.getDbOr() + "-" + rdml.getIid());
        rgl.setDscr(sb.toString());
        this.orm.insIdLn(pRvs, vs, rgl);
        sb.delete(0, sb.length());
        if (rdml.getDscr() != null) {
          sb.append(rdml.getDscr() + " !");
        }
        sb.append(getI18n().getMsg("reversing", cpf.getLngDef().getIid()));
        sb.append(" #" + rgl.getDbOr() + "-" + rgl.getIid());
        rdml.setDscr(sb.toString());
        rdml.setRvId(rgl.getIid());
        String[] upFds = new String[] {"rvId", "dscr", "ver"};
        Arrays.sort(upFds);
        vs.put("ndFds", upFds);
        this.orm.update(pRvs, vs, rdml); vs.clear();
        this.srDrItEnr.rvDraw(pRvs, rgl);
        this.srWrhEnr.revDraw(pRvs, rgl);
      }
      vs.put("MnpAcsdpLv", 1);
      List<MnpAcs> rdals = this.orm.retLstCnd(pRvs, vs, MnpAcs.class,
        "where OWNR=" + revd.getIid()); vs.clear();
      for (MnpAcs rdal : rdals) {
        MnpAcs rgl = new MnpAcs();
        rgl.setDbOr(this.orm.getDbId());
        rgl.setOwnr(pEnt);
        rgl.setRvId(rdal.getIid());
        rgl.setAcc(rdal.getAcc());
        rgl.setSaId(rdal.getSaId());
        rgl.setSaNm(rdal.getSaNm());
        rgl.setSaTy(rdal.getSaTy());
        rgl.setTot(rdal.getTot().negate());
        StringBuffer sb = new StringBuffer();
        if (rgl.getDscr() != null) {
          sb.append(rgl.getDscr() + " !");
        }
        sb.append(getI18n().getMsg("reversed", cpf.getLngDef().getIid()));
        sb.append(" #" + rdal.getDbOr() + "-" + rdal.getIid());
        rgl.setDscr(sb.toString());
        this.orm.insIdLn(pRvs, vs, rgl);
        sb.delete(0, sb.length());
        if (rdal.getDscr() != null) {
          sb.append(rdal.getDscr() + " !");
        }
        sb.append(getI18n().getMsg("reversing", cpf.getLngDef().getIid()));
        sb.append(" #" + rgl.getDbOr() + "-" + rgl.getIid());
        rdal.setDscr(sb.toString());
        rdal.setRvId(rgl.getIid());
        String[] upFds = new String[] {"rvId", "dscr", "ver"};
        Arrays.sort(upFds);
        vs.put("ndFds", upFds);
        this.orm.update(pRvs, vs, rdal); vs.clear();
      }
      pRvs.put("msgSuc", "reverse_ok");
    } else {
      this.utlBas.chDtForg(pRvs, pEnt, pEnt.getDat());
      if (pEnt.getQuan().compareTo(BigDecimal.ZERO) <= 0) {
        throw new ExcCode(ExcCode.WRPR, "quantity_less_or_equal_zero");
      }
      pEnt.setItLf(pEnt.getQuan());
      if (pEnt.getIsNew()) {
        this.orm.insIdLn(pRvs, vs, pEnt);
        pRvs.put("msgSuc", "insert_ok");
      } else {
        String[] slFds = new String[] {"tot", "mdEnr"};
        Arrays.sort(slFds);
        vs.put("MnfPrcndFds", slFds);
        MnfPrc old = this.orm.retEnt(pRvs, vs, pEnt); vs.clear();
        pEnt.setMdEnr(old.getMdEnr());
        if (pEnt.getMdEnr()) {
          throw new ExcCode(ExcCode.SPAM, "Trying to change accounted!");
        }
        AcStg as = (AcStg) pRvs.get("astg");
        pEnt.setPri(pEnt.getTot().divide(pEnt.getQuan(),
          as.getCsDp(), as.getRndm()));
        if ("mkEnr".equals(pRqDt.getParam("acAd"))) {
          if (old.getTot().compareTo(BigDecimal.ZERO) == 0) {
            throw new ExcCode(ExcCode.WRPR, "amount_eq_zero");
          }
          if (!pEnt.getCmpl()) {
            throw new ExcCode(ExcCode.WRPR, "complite_first");
          }
          this.srWrhEnr.load(pRvs, pEnt, pEnt.getWrhp());
          pEnt.setMdEnr(true);
          String[] upFds = new String[] {"dat", "dscr", "ver", "cmpl", "quan",
            "pri", "itm", "uom", "wrhp", "mdEnr", "itLf"};
          Arrays.sort(upFds);
          vs.put("ndFds", upFds);
          getOrm().update(pRvs, vs, pEnt); vs.clear();
          this.srEntr.mkEntrs(pRvs, pEnt);
          pRvs.put("msgSuc", "account_ok");
        } else {
          String[] upFds = new String[] {"dat", "dscr", "ver", "cmpl", "quan",
            "pri", "itm", "uom", "wrhp", "itLf"};
          Arrays.sort(upFds);
          vs.put("ndFds", upFds);
          getOrm().update(pRvs, vs, pEnt); vs.clear();
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
