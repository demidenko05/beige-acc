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
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.ColVals;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.log.ILog;
import org.beigesoft.hld.IHlIntCls;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.SrvClVl;
import org.beigesoft.srv.II18n;
import org.beigesoft.acc.mdl.EDocTy;
import org.beigesoft.acc.mdlb.IDocb;
import org.beigesoft.acc.mdlb.IMkWsEnr;
import org.beigesoft.acc.mdlp.WrhEnr;
import org.beigesoft.acc.mdlp.WrhPl;
import org.beigesoft.acc.mdlp.WrhItm;

/**
 * <p>Service that makes warehouse entries, items left
 *  for given document/line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrWrhEnr<RS> implements ISrWrhEnr {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>I18N service.</p>
   */
  private II18n i18n;

  /**
   * <p>Column values service.</p>
   **/
  private SrvClVl srvClVl;

  /**
   * <p>Holder of entries sources types.</p>
   **/
  private IHlIntCls hlTyEnSr;

  /**
   * <p>Android configuration, RDB insert returns autogenerated ID,
   * updating with expression like "VER=VER+1" is not possible.</p>
   **/
  private boolean isAndr;

  /**
   * <p>Loads warehouse place with item from given source.</p>
   * @param pRvs Request scoped variables, not null
   * @param pEnt loader source, not null
   * @param pWrp warehouse place, not null
   * @throws Exception - an exception
   **/
  @Override
  public final void load(final Map<String, Object> pRvs, final IMkWsEnr pEnt,
    final WrhPl pWrp) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    DateFormat dtFr = DateFormat.getDateTimeInstance(DateFormat
      .MEDIUM, DateFormat.SHORT, new Locale(cpf.getLngDef().getIid()));
    mkEntr(pRvs, vs, pEnt, null, pWrp, dtFr);
    mkWrhItm(pRvs, vs, pEnt, pWrp, pEnt.getQuan());
  }

  /**
   * <p>Draws item from warehouse place for given source.</p>
   * @param pRvs Request scoped variables, not null
   * @param pEnt drawer source, not null
   * @param pWrp warehouse place, maybe null
   * @throws Exception - an exception
   **/
  @Override
  public final void draw(final Map<String, Object> pRvs, final IMkWsEnr pEnt,
    final WrhPl pWrp) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    DateFormat dtFr = DateFormat.getDateTimeInstance(DateFormat
      .MEDIUM, DateFormat.SHORT, new Locale(cpf.getLngDef().getIid()));
    WrhPl wrp;
    if (pWrp != null) {
      wrp = pWrp;
    } else {
      vs.put("WrhItmndFds", new String[] {"wrhp"});
      vs.put("WrhPldpLv", 0);
      List<WrhItm> lst = this.orm.retLstCnd(pRvs, vs, WrhItm.class, "where ITM="
        + pEnt.getItm().getIid() + " and UOM=" + pEnt.getUom().getIid()
          + " and ITLF>=" + pEnt.getQuan() + " limit 1"); //fastest query
      vs.clear();
      if (lst.size() == 0) {
        throw new ExcCode(ExcCode.WRPR, "THERE_IS_NO_GOODS");
      }
      wrp = lst.get(0).getWrhp();
    }
    mkEntr(pRvs, vs, pEnt, wrp, null, dtFr);
    mkWrhItm(pRvs, vs, pEnt, wrp, pEnt.getQuan().negate());
  }

  /**
   * <p>Moves item from one warehouse place to another for given source.</p>
   * @param pRvs Request scoped variables, not null
   * @param pEnt move source, not null
   * @param pWrpFr warehouse place from, not null
   * @param pWrpTo warehouse place to, not null
   * @throws Exception - an exception
   **/
  @Override
  public final void move(final Map<String, Object> pRvs, final IMkWsEnr pEnt,
    final WrhPl pWrpFr, final WrhPl pWrpTo) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    DateFormat dtFr = DateFormat.getDateTimeInstance(DateFormat
      .MEDIUM, DateFormat.SHORT, new Locale(cpf.getLngDef().getIid()));
    mkEntr(pRvs, vs, pEnt, pWrpFr, pWrpTo, dtFr);
    mkWrhItm(pRvs, vs, pEnt, pWrpFr, pEnt.getQuan().negate());
    mkWrhItm(pRvs, vs, pEnt, pWrpTo, pEnt.getQuan());
  }

  /**
   * <p>Reverse for given loading.</p>
   * @param pRvs Request scoped variables, not null
   * @param pEnt reversing loader source, not null
   * @throws Exception - an exception
   **/
  @Override
  public final void revLoad(final Map<String, Object> pRvs,
    final IMkWsEnr pEnt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    DateFormat dtFr = DateFormat.getDateTimeInstance(DateFormat
      .MEDIUM, DateFormat.SHORT, new Locale(cpf.getLngDef().getIid()));
    WrhEnr[] rz = mkRevEntr(pRvs, vs, pEnt, dtFr); //[reversing,reversed]
    mkWrhItm(pRvs, vs, pEnt, rz[1].getWpTo(), rz[1].getQuan().negate());
  }

  /**
   * <p>Reverse for given drawing.</p>
   * @param pRvs Request scoped variables, not null
   * @param pEnt reversing drawer source, not null
   * @throws Exception - an exception
   **/
  @Override
  public final void revDraw(final Map<String, Object> pRvs,
    final IMkWsEnr pEnt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    DateFormat dtFr = DateFormat.getDateTimeInstance(DateFormat
      .MEDIUM, DateFormat.SHORT, new Locale(cpf.getLngDef().getIid()));
    WrhEnr[] rz = mkRevEntr(pRvs, vs, pEnt, dtFr); //[reversing,reversed]
    mkWrhItm(pRvs, vs, pEnt, rz[1].getWpFr(), rz[1].getQuan());
  }

  /**
   * <p>Reverse for given moving.</p>
   * @param pRvs Request scoped variables, not null
   * @param pEnt reversing move source, not null
   * @throws Exception - an exception
   **/
  @Override
  public final void revMove(final Map<String, Object> pRvs,
    final IMkWsEnr pEnt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    DateFormat dtFr = DateFormat.getDateTimeInstance(DateFormat
      .MEDIUM, DateFormat.SHORT, new Locale(cpf.getLngDef().getIid()));
    WrhEnr[] rz = mkRevEntr(pRvs, vs, pEnt, dtFr); //[reversing,reversed]
    mkWrhItm(pRvs, vs, pEnt, rz[1].getWpFr(), rz[1].getQuan());
    mkWrhItm(pRvs, vs, pEnt, rz[1].getWpTo(), rz[1].getQuan().negate());
  }

  /**
   * <p>Retrieves entries for given document.</p>
   * @param pRvs Request scoped variables, not null
   * @param pDoc source document, not null
   * @return entries
   * @throws Exception - an exception
   **/
  @Override
  public final List<WrhEnr> retEntrs(final Map<String, Object> pRvs,
    final IDocb pDoc) throws Exception {
    if (pDoc.getDocTy() == EDocTy.ACC) {
    throw new ExcCode(ExcCode.WR, "Document should not has warehouse entries!");
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    List<WrhEnr> rz = null;
    if (pDoc.getDocTy() == EDocTy.DRAW || pDoc.getDocTy() == EDocTy.DRAWBTH
    || pDoc.getDocTy() == EDocTy.ITSR || pDoc.getDocTy() == EDocTy.ITSRBTH
  || pDoc.getDocTy() == EDocTy.WRH || pDoc.getDocTy() == EDocTy.WRHBTH
|| pDoc.getDocTy() == EDocTy.ITSRDRAWLN || pDoc.getDocTy() == EDocTy.ITSRDRAW) {
      rz = this.orm.retLstCnd(pRvs, vs, WrhEnr.class, "where SRTY="
        + pDoc.cnsTy() + " and SRID=" + pDoc.getIid() + ";");
    }
    if (pDoc.getDocTy() == EDocTy.DRAWLN || pDoc.getDocTy() == EDocTy.DRAWBTH
      || pDoc.getDocTy() == EDocTy.ITSRLN || pDoc.getDocTy() == EDocTy.ITSRBTH
       || pDoc.getDocTy() == EDocTy.WRHLN || pDoc.getDocTy() == EDocTy.WRHBTH
        || pDoc.getDocTy() == EDocTy.ITSRDRAWLN) {
      List<WrhEnr> rzt = this.orm.retLstCnd(pRvs, vs, WrhEnr.class,
        "where SOWTY=" + pDoc.cnsTy() + " and SOWID=" + pDoc.getIid() + ";");
      if (rz == null) {
        rz = rzt;
      } else {
        rz.addAll(rzt);
      }
    }
    return rz;
  }

  //Utils:
  /**
   * <p>Makes item left in warehouse.</p>
   * @param pRvs Request scoped variables, not null
   * @param pVs Invoker scoped variables, not null
   * @param pEnt source, not null
   * @param pWrp place, not null
   * @param pQuan positive - load, negative - draw
   * @throws Exception - an exception
   **/
  public final void mkWrhItm(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final IMkWsEnr pEnt,
      final WrhPl pWrp, final BigDecimal pQuan) throws Exception {
    WrhItm rz = new WrhItm();
    rz.setWrhp(pWrp);
    rz.setItm(pEnt.getItm());
    rz.setUom(pEnt.getUom());
    rz.setIsNew(true);
    WrhItm rzt = this.orm.retEnt(pRvs, pVs, rz);
    if (rzt != null) {
      rz = rzt;
    }
    rz.setItLf(rz.getItLf().add(pQuan));
    if (rz.getItLf().compareTo(BigDecimal.ZERO) == -1) {
      throw new ExcCode(ExcCode.WRPR, "THERE_IS_NO_GOODS");
    }
    if (rz.getIsNew()) {
      this.orm.insIdNln(pRvs, pVs, rz);
    } else {
      if (this.isAndr) {
        this.orm.update(pRvs, pVs, rz);
      } else { //use fastest locking:
        ColVals cv = new ColVals();
        if (pQuan.compareTo(BigDecimal.ZERO) == -1) {
          this.srvClVl.put(cv, "itLf", "ITLF-" + pQuan.negate());
        } else {
          this.srvClVl.put(cv, "itLf", "ITLF+" + pQuan);
        }
        this.srvClVl.putExpr(cv, "itLf");
        this.srvClVl.put(cv, "ver", "VER+1");
        this.srvClVl.putExpr(cv, "ver");
        try {
          int ur = this.rdb.update(WrhItm.class, cv, "WRHP=" + rz.getWrhp()
            .getIid() + " and ITM=" + rz.getItm().getIid() + " and UOM="
            + rz.getUom().getIid());
          if (ur == 0) {
            throw new Exception("Something wrong!");
          }
        } catch (Exception e) {
          this.log.error(pRvs,  getClass(), "THERE_IS_NO_GOODS", e);
          throw new ExcCode(ExcCode.WRPR, "THERE_IS_NO_GOODS");
        }
      }
    }
  }

  /**
   * <p>Makes entry for given source.</p>
   * @param pRvs Request scoped variables
   * @param pVs Invoker scoped variables
   * @param pEnt source
   * @param pWpFr place from
   * @param pWpTo place to
   * @param pDtFrm date format
   * @return entry
   * @throws Exception - an exception
   **/
  public final WrhEnr mkEntr(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final IMkWsEnr pEnt, final WrhPl pWpFr,
      final WrhPl pWpTo, final DateFormat pDtFrm) throws Exception {
    WrhEnr rz = new WrhEnr();
    rz.setDbOr(this.orm.getDbId());
    rz.setSrTy(pEnt.cnsTy());
    rz.setSrId(pEnt.getIid());
    rz.setSowTy(pEnt.getOwnrTy());
    rz.setSowId(pEnt.getOwnrId());
    rz.setItm(pEnt.getItm());
    rz.setUom(pEnt.getUom());
    rz.setQuan(pEnt.getQuan());
    rz.setWpFr(pWpFr);
    rz.setWpTo(pWpTo);
    StringBuffer sb = mkDscr(pRvs, pEnt, pDtFrm);
    rz.setDscr(sb.toString());
    this.orm.insIdLn(pRvs, pVs, rz);
    return rz;
  }

  /**
   * <p>Makes reversed/reversing entries for given source.</p>
   * @param pRvs Request scoped variables
   * @param pVs Invoker scoped variables
   * @param pEnt reversing
   * @param pDtFrm date format
   * @return entries reversing, reversed
   * @throws Exception - an exception
   **/
  public final WrhEnr[] mkRevEntr(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final IMkWsEnr pEnt,
      final DateFormat pDtFrm) throws Exception {
    WrhEnr revd = this.orm.retEntCnd(pRvs, pVs, WrhEnr.class, "SRTY="
      + pEnt.cnsTy() + " and SRID=" + pEnt.getRvId()
        + " and ITM=" + pEnt.getItm().getIid());
    if (revd == null) {
      throw new ExcCode(ExcCode.WR, "Can't reverse for CLS/RVID/ID/TY: "
        + pEnt.getClass() + "/" + pEnt.getRvId() + "/" + pEnt.getIid()
          + "/" + pEnt.cnsTy());
    }
    if (revd.getRvId() != null) {
      throw new ExcCode(ExcCode.WR, "Reverse reversed for CLS/RVID/ID/TY: "
        + pEnt.getClass() + "/" + pEnt.getRvId() + "/" + pEnt.getIid()
          + "/" + pEnt.cnsTy());
    }
    WrhEnr revg = new WrhEnr();
    revg.setDbOr(this.orm.getDbId());
    revg.setRvId(revd.getIid());
    revg.setSrTy(pEnt.cnsTy());
    revg.setSrId(pEnt.getIid());
    revg.setSowTy(pEnt.getOwnrTy());
    revg.setSowId(pEnt.getOwnrId());
    revg.setItm(revd.getItm());
    revg.setUom(revd.getUom());
    revg.setQuan(revd.getQuan().negate());
    revg.setWpFr(revd.getWpFr());
    revg.setWpTo(revd.getWpTo());
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    StringBuffer sb = mkDscr(pRvs, pEnt, pDtFrm);
    sb.append(" ," + getI18n().getMsg("reversed", cpf.getLngDef().getIid()));
    sb.append(" #" + revd.getDbOr() + "-" + revd.getIid());
    revg.setDscr(sb.toString() + "!");
    this.orm.insIdLn(pRvs, pVs, revg);
    revd.setRvId(revg.getIid());
    revd.setDscr(revd.getDscr() + ", !" + getI18n()
      .getMsg("reversing", cpf.getLngDef().getIid()) + " #" + revg.getDbOr()
        + "-" + revg.getIid() + "!");
    String[] ndFds = new String[] {"dscr", "rvId", "ver"};
    Arrays.sort(ndFds);
    pVs.put("ndFds", ndFds);
    this.orm.update(pRvs, pVs, revd); pVs.clear();
    return new WrhEnr[] {revg, revd};
  }

  /**
   * <p>Makes entry description.</p>
   * @param pRvs request scoped vars
   * @param pSrc source
   * @param pDtFrm date format
   * @return string buffer
   * @throws Exception - an exception
   **/
  public final StringBuffer mkDscr(final Map<String, Object> pRvs,
    final IMkWsEnr pSrc, final DateFormat pDtFrm) throws Exception {
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    StringBuffer sb = new StringBuffer();
    sb.append(getI18n().getMsg("Made_at", cpf.getLngDef().getIid()) + " "
      + pDtFrm.format(new Date()) + " ");
    sb.append(getI18n().getMsg("by_who", cpf.getLngDef().getIid()) + ": ");
    sb.append(getI18n().getMsg(pSrc.getClass().getSimpleName() + "sht",
      cpf.getLngDef().getIid()) + " #" + pSrc.getDbOr() + "-" + pSrc
        .getIid() + ", " + pDtFrm.format(pSrc.getDocDt()));
    if (pSrc.getOwnrId() != null) {
      sb.append(", " + getI18n().getMsg("in", cpf.getLngDef().getIid())
       + " " + getI18n().getMsg(this.hlTyEnSr.get(pSrc.getOwnrTy())
        .getSimpleName() + "sht", cpf.getLngDef().getIid()));
      sb.append(" #" + pSrc.getDbOr() + "-" + pSrc.getOwnrId());
    }
    return sb;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for log.</p>
   * @return ILog
   **/
  public final ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final void setLog(final ILog pLog) {
    this.log = pLog;
  }

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
   * <p>Getter for srvClVl.</p>
   * @return SrvClVl
   **/
  public final SrvClVl getSrvClVl() {
    return this.srvClVl;
  }

  /**
   * <p>Setter for srvClVl.</p>
   * @param pSrvClVl reference
   **/
  public final void setSrvClVl(final SrvClVl pSrvClVl) {
    this.srvClVl = pSrvClVl;
  }

  /**
   * <p>Getter for isAndr.</p>
   * @return boolean
   **/
  public final boolean getIsAndr() {
    return this.isAndr;
  }

  /**
   * <p>Setter for isAndr.</p>
   * @param pIsAndr reference
   **/
  public final void setIsAndr(final boolean pIsAndr) {
    this.isAndr = pIsAndr;
  }

  /**
   * <p>Getter for hlTyEnSr.</p>
   * @return IHlIntCls
   **/
  public final IHlIntCls getHlTyEnSr() {
    return this.hlTyEnSr;
  }

  /**
   * <p>Setter for hlTyEnSr.</p>
   * @param pHlTyEnSr reference
   **/
  public final void setHlTyEnSr(final IHlIntCls pHlTyEnSr) {
    this.hlTyEnSr = pHlTyEnSr;
  }
}
