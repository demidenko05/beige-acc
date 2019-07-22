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
import java.util.Calendar;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.srv.II18n;
import org.beigesoft.acc.mdl.ETxTy;
import org.beigesoft.acc.mdlp.EmpWg;
import org.beigesoft.acc.mdlp.Wage;
import org.beigesoft.acc.mdlp.WgLn;
import org.beigesoft.acc.mdlp.WgTxl;
import org.beigesoft.acc.mdlp.WagTt;
import org.beigesoft.acc.mdlp.WagTy;
import org.beigesoft.acc.mdlp.WttEm;
import org.beigesoft.acc.mdlp.WttLn;
import org.beigesoft.acc.mdlp.WttWg;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.srv.ISrEntr;
import org.beigesoft.acc.srv.UtlBas;

/**
 * <p>Service that saves wage into DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class WageSv<RS> implements IPrcEnt<Wage, Long> {

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
   * <p>I18N service.</p>
   */
  private II18n i18n;

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Process that saves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final Wage process(final Map<String, Object> pRvs, final Wage pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    if (pEnt.getRvId() != null) {
      Wage revd = new Wage();
      revd.setIid(pEnt.getRvId());
      this.orm.refrEnt(pRvs, vs, revd);
      this.utlBas.chDtForg(pRvs, revd, revd.getDat());
      pEnt.setDbOr(this.orm.getDbId());
      pEnt.setTot(revd.getTot().negate());
      this.srEntr.revEntrs(pRvs, pEnt, revd);
      vs.put("WgLndpLv", 1);
      List<WgLn> rdls = this.orm.retLstCnd(pRvs, vs, WgLn.class,
        "where OWNR=" + revd.getIid()); vs.clear();
      CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
      for (WgLn rdl : rdls) {
        WgLn rgl = new WgLn();
        rgl.setDbOr(this.orm.getDbId());
        rgl.setOwnr(pEnt);
        rgl.setRvId(rdl.getIid());
        rgl.setWgTy(rdl.getWgTy());
        rgl.setAcWge(rdl.getAcWge());
        rgl.setTxEe(rdl.getTxEe().negate());
        rgl.setGrWg(rdl.getGrWg().negate());
        StringBuffer sb = new StringBuffer();
        if (rgl.getDscr() != null) {
          sb.append(rgl.getDscr() + " !");
        }
        sb.append(getI18n().getMsg("reversed", cpf.getLngDef().getIid()));
        sb.append(" #" + rdl.getDbOr() + "-" + rdl.getIid());
        rgl.setDscr(sb.toString());
        this.orm.insIdLn(pRvs, vs, rgl);
        sb.delete(0, sb.length());
        if (rdl.getDscr() != null) {
          sb.append(rdl.getDscr() + " !");
        }
        sb.append(getI18n().getMsg("reversing", cpf.getLngDef().getIid()));
        sb.append(" #" + rgl.getDbOr() + "-" + rgl.getIid());
        rdl.setDscr(sb.toString());
        rdl.setRvId(rgl.getIid());
        String[] upFds = new String[] {"rvId", "dscr", "ver"};
        Arrays.sort(upFds);
        vs.put("ndFds", upFds);
        this.orm.update(pRvs, vs, rdl); vs.clear();
      }
      Calendar cal = Calendar.getInstance();
      int year = cal.get(Calendar.YEAR);
      vs.put("WgLndpLv", 1);
      revd.setWags(getOrm().retLstCnd(pRvs, vs, WgLn.class, "where OWNR="
        + revd.getIid())); vs.clear();
      for (WgLn wl : revd.getWags()) {
        EmpWg empWg = this.orm.retEntCnd(pRvs, vs, EmpWg.class, "OWNR="
          + revd.getEmpl().getIid() + " and YER=" + year
            + " and WGTY=" + wl.getWgTy().getIid());
        empWg.setTot(empWg.getTot().subtract(wl.getGrWg())
          .add(wl.getTxEe()));
        getOrm().update(pRvs, vs, empWg);
      }
      pRvs.put("msgSuc", "reverse_ok");
    } else {
      this.utlBas.chDtForg(pRvs, pEnt, pEnt.getDat());
      if (pEnt.getIsNew()) {
        this.orm.insIdLn(pRvs, vs, pEnt);
        pRvs.put("msgSuc", "insert_ok");
      } else {
        if ("mkTxs".equals(pRqDt.getParam("acAd"))) {
          mkTxs(pRvs, pEnt);
        } else {
          String[] slFds = new String[] {"tot", "mdEnr"};
          Arrays.sort(slFds);
          vs.put("WagendFds", slFds);
          Wage old = this.orm.retEnt(pRvs, vs, pEnt); vs.clear();
          pEnt.setMdEnr(old.getMdEnr());
          if (pEnt.getMdEnr()) {
            throw new ExcCode(ExcCode.SPAM, "Trying to change accounted!");
          }
          if ("mkEnr".equals(pRqDt.getParam("acAd"))) {
           if (old.getTot().compareTo(BigDecimal.ZERO) == 0) {
              throw new ExcCode(ExcCode.WRPR, "amount_eq_zero");
            }
            String[] upFds = new String[] {"dat", "dscr", "ver", "mdEnr",
              "acTx", "empl"};
            Arrays.sort(upFds);
            pRvs.put(ISrEntr.DOCFDSUPD, upFds);
            this.srEntr.mkEntrs(pRvs, pEnt);
            pRvs.remove(ISrEntr.DOCFDSUPD);
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            vs.put("WgLndpLv", 1);
            pEnt.setWags(getOrm().retLstCnd(pRvs, vs, WgLn.class, "where OWNR="
              + pEnt.getIid())); vs.clear();
            for (WgLn wl : pEnt.getWags()) {
              EmpWg empWg = this.orm.retEntCnd(pRvs, vs, EmpWg.class, "OWNR="
                + pEnt.getEmpl().getIid() + " and YER=" + year
                  + " and WGTY=" + wl.getWgTy().getIid());
              if (empWg == null) {
                empWg = new EmpWg();
                empWg.setIsNew(true);
                empWg.setDbOr(this.orm.getDbId());
                empWg.setOwnr(pEnt.getEmpl());
                empWg.setWgTy(wl.getWgTy());
                empWg.setYer(year);
              }
              empWg.setTot(empWg.getTot().add(wl.getGrWg())
                .subtract(wl.getTxEe()));
              if (empWg.getIsNew()) {
                getOrm().insIdLn(pRvs, vs, empWg);
              } else {
                getOrm().update(pRvs, vs, empWg);
              }
            }
            pRvs.put("msgSuc", "account_ok");
          } else {
            String[] uFds = new String[] {"dat", "dscr", "ver", "acTx", "empl"};
            Arrays.sort(uFds);
            vs.put("ndFds", uFds);
            getOrm().update(pRvs, vs, pEnt); vs.clear();
            pRvs.put("msgSuc", "update_ok");
          }
        }
      }
    }
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setEnt(pEnt);
    return pEnt;
  }

  //Utils:
  /**
   * <p>It fills wage tax lines according Table Percentage method,
   * recalculates and updates whole document and its wages lines.</p>
   * @param pRvs additional params
   * @param pWage Wage document
   * @throws Exception - an exception
   **/
  public final void mkTxs(final Map<String, Object> pRvs,
    final Wage pWage) throws Exception {
    AcStg as = (AcStg) pRvs.get("astg");
    Map<String, Object> vs = new HashMap<String, Object>();
    vs.put("WttEmdpLv", 1);
    List<WttEm> wttEms = getOrm().retLstCnd(pRvs, vs, WttEm.class, "where EMPL="
      + pWage.getEmpl().getIid()); vs.clear();
    if (wttEms.size() > 0) {
      CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
      String quYerWg = "select sum(TOT) as TOT from EMPWG where OWNR=" + pWage
        .getEmpl().getIid();
      Double yearWgDbl = getRdb().evDouble(quYerWg, "TOT");
      if (yearWgDbl == null) {
        yearWgDbl = 0d;
      }
      BigDecimal yearWg = BigDecimal.valueOf(yearWgDbl);
      getRdb().delete("WGTXL", "OWNR=" + pWage.getIid());
      BigDecimal bgd100 = new BigDecimal("100.00");
      BigDecimal txEe = BigDecimal.ZERO;
      BigDecimal txEr = BigDecimal.ZERO;
      vs.put("WgLndpLv", 1);
      pWage.setWags(getOrm().retLstCnd(pRvs, vs, WgLn.class, "where OWNR="
        + pWage.getIid())); vs.clear();
      Map<WagTy, BigDecimal> empTotTxMp = new HashMap<WagTy, BigDecimal>();
      for (WgLn wl : pWage.getWags()) {
        empTotTxMp.put(wl.getWgTy(), BigDecimal.ZERO);
      }
      for (WttEm wttEm : wttEms) {
        if (wttEm.getOwnr().getTax() == null) {
          getOrm().refrEnt(pRvs, vs, wttEm.getOwnr());
          vs.put("WttLndpLv", 1);
          wttEm.getOwnr().setLns(getOrm().retLstCnd(pRvs, vs, WttLn.class,
            "where OWNR=" + wttEm.getOwnr().getIid())); vs.clear();
          vs.put("WttWgdpLv", 1);
          wttEm.getOwnr().setWags(getOrm().retLstCnd(pRvs, vs, WttWg.class,
            "where OWNR=" + wttEm.getOwnr().getIid())); vs.clear();
        }
        BigDecimal totTxb = BigDecimal.ZERO;
        for (WgLn wl : pWage.getWags()) {
          if (isWageApplied(wl.getWgTy(), wttEm.getOwnr())) {
            totTxb = totTxb.add(wl.getGrWg());
          }
        }
        if (totTxb.doubleValue() > 0) {
          BigDecimal wgMnsAlw = totTxb.subtract(wttEm.getAlw());
          boolean isFilled = false;
          for (WttLn wttLn : wttEm.getOwnr().getLns()) {
            if (wgMnsAlw.compareTo(wttLn.getWgFr()) >= 0 && wgMnsAlw.compareTo(
          wttLn.getWgTo()) < 0 && yearWg.compareTo(wttLn.getYwgFr()) >= 0
        && yearWg.compareTo(wttLn.getYwgTo()) < 0) {
              WgTxl wtl = new WgTxl();
              wtl.setIsNew(true);
              wtl.setDbOr(getOrm().getDbId());
              wtl.setOwnr(pWage);
              wtl.setAlw(wttEm.getAlw());
              wtl.setPlAm(wttLn.getPlAm());
              wtl.setTax(wttEm.getOwnr().getTax());
              wtl.setRate(wttLn.getRate());
              wtl.setTot(wgMnsAlw.subtract(wttLn.getAlw()).multiply(wttLn
  .getRate()).divide(bgd100, as.getPrDp(), as.getRndm()).add(wttLn.getPlAm()));
              wtl.setDscr(getI18n().getMsg("TIDNametaxb", cpf.getLngDef()
                .getIid()) + ": " + wttEm.getOwnr().getIid() + "/"
                  + wttEm.getOwnr().getNme() + "/" + totTxb);
              getOrm().insIdLn(pRvs, vs, wtl);
              if (wtl.getTax().getTyp().equals(ETxTy.TEMPLOYEE)) {
                txEe = txEe.add(wtl.getTot());
                for (WgLn wl : pWage.getWags()) {
                  BigDecimal newTxEeLn = empTotTxMp.get(wl.getWgTy()).add(wl
                    .getGrWg().multiply(wtl.getTot())
                      .divide(totTxb, as.getPrDp(), as.getRndm()));
                  empTotTxMp.put(wl.getWgTy(), newTxEeLn);
                }
              } else if (wtl.getTax().getTyp().equals(ETxTy.TEMPLOYER)) {
                txEr = txEr.add(wtl.getTot());
              }
              isFilled = true;
              break;
            }
          }
          if (!isFilled) {
            throw new ExcCode(ExcCode.WRPR,
              "there_is_no_suitable_tax_percent_entry");
          }
        }
      }
      String[] upFds = new String[] {"txEe", "ver"};
      Arrays.sort(upFds);
      vs.put("ndFds", upFds);
      for (WgLn wl : pWage.getWags()) {
        wl.setTxEe(empTotTxMp.get(wl.getWgTy()));
        getOrm().update(pRvs, vs, wl);
      }
      vs.clear();
      pWage.setTxEe(txEe);
      pWage.setTxEr(txEr);
      pWage.setNtWg(pWage.getTot().subtract(pWage.getTxEe()));
      upFds = new String[] {"ntWg", "txEe", "txEr", "ver"};
      Arrays.sort(upFds);
      vs.put("ndFds", upFds);
      getOrm().update(pRvs, vs, pWage);
    }
  }

  /**
   * <p>Check if wage type applied for tax.</p>
   * @param pWgTy Wage Type
   * @param pWagTt Wage Tax Table
   * @return is applied
   **/
  public final boolean isWageApplied(final WagTy pWgTy,
    final WagTt pWagTt) {
    for (WttWg wttWg : pWagTt.getWags()) {
      if (wttWg.getWgTy().getIid().equals(pWgTy.getIid())) {
        return true;
      }
    }
    return false;
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
   * <p>Getter for rdb.</p>
   * @return IRdb<RS>
   **/
  public final IRdb<RS> getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final void setRdb(final IRdb<RS> pRdb) {
    this.rdb = pRdb;
  }
}
