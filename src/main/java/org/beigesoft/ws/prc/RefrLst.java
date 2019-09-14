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
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE ITM OR
SRVS; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.beigesoft.ws.prc;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IIdLnNm;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdlp.DcSp;
import org.beigesoft.mdlp.Lng;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.mdlp.AI18nNm;
import org.beigesoft.cmp.CmpHasVr;
import org.beigesoft.log.ILog;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.prc.IPrc;
import org.beigesoft.srv.INumStr;
import org.beigesoft.acc.mdlp.Srv;
import org.beigesoft.acc.mdlp.Itm;
import org.beigesoft.acc.mdlp.I18Itm;
import org.beigesoft.acc.mdlp.I18Srv;
import org.beigesoft.acc.mdlp.I18Uom;
import org.beigesoft.acc.mdlp.Uom;
import org.beigesoft.ws.mdl.EItmSpTy;
import org.beigesoft.ws.mdl.EItmTy;
import org.beigesoft.ws.mdlb.AItmSpf;
import org.beigesoft.ws.mdlb.AItmPlc;
import org.beigesoft.ws.mdlb.AItmPri;
import org.beigesoft.ws.mdlp.ItmSpf;
import org.beigesoft.ws.mdlp.ItmSp;
import org.beigesoft.ws.mdlp.ChoSp;
import org.beigesoft.ws.mdlp.ItmSpGr;
import org.beigesoft.ws.mdlp.Htmlt;
import org.beigesoft.ws.mdlp.PriItm;
import org.beigesoft.ws.mdlp.ItmPlc;
import org.beigesoft.ws.mdlp.SrvPlc;
import org.beigesoft.ws.mdlp.PriSrv;
import org.beigesoft.ws.mdlp.SrvSpf;
import org.beigesoft.ws.mdlp.SeSrv;
import org.beigesoft.ws.mdlp.I18SeSrv;
import org.beigesoft.ws.mdlp.SeSrvPlc;
import org.beigesoft.ws.mdlp.SeSrvPri;
import org.beigesoft.ws.mdlp.SeSrvSpf;
import org.beigesoft.ws.mdlp.SeItm;
import org.beigesoft.ws.mdlp.I18SeItm;
import org.beigesoft.ws.mdlp.SeItmPlc;
import org.beigesoft.ws.mdlp.SeItmPri;
import org.beigesoft.ws.mdlp.SeItmSpf;
import org.beigesoft.ws.mdlp.ItlLuv;
import org.beigesoft.ws.mdlp.TrdStg;
import org.beigesoft.ws.mdlp.AddStg;
import org.beigesoft.ws.mdlp.Itlist;
import org.beigesoft.ws.mdlp.I18ChoSp;
import org.beigesoft.ws.mdlp.I18ItmSp;
import org.beigesoft.ws.mdlp.I18ItmSpGr;
import org.beigesoft.ws.mdlp.I18SpeLi;
import org.beigesoft.ws.mdlp.PriCt;

/**
 * <p>Service that refresh webstore itm in Itlist according current
 * GoodsAvailiable, ItmSpf, PriItm, GoodsRating, etc.
 * This is non-public processor.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class RefrLst<RS> implements IPrc {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Service print number.</p>
   **/
  private INumStr numStr;

  /**
   * <p>Transaction isolation.</p>
   **/
  private Integer trIsl;

  /**
   * <p>Process entity request.</p>
   * @param pRvs additional param
   * @param pRqd Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs,
    final IReqDt pRqd) throws Exception {
    retStData(pRvs);
    Map<String, Object> vs = new HashMap<String, Object>();
    String prCtId = pRqd.getParam("priCt");
    PriCt defPc = this.orm.retEntCnd(pRvs, vs, PriCt.class, "IID=" + prCtId);
    if (defPc == null) {
      throw new ExcCode(ExcCode.SPAM, "Can't find price category #" + prCtId);
    }
    AddStg tastg = (AddStg) pRvs.get("tastg");
    TrdStg tstg = (TrdStg) pRvs.get("tstg");
    ItlLuv itlLuv = (ItlLuv) pRvs.get("itlLuv");
    pRvs.remove("itlLuv");
    boolean refrAll = "true".equals(pRqd.getParam("refrAll"));
    List<ItmSpf> itmSpecLst;
    if (refrAll) {
      itmSpecLst = retItmSpfLs(pRvs, null, ItmSpf.class);
    } else {
      itmSpecLst = retItmSpfLs(pRvs, itlLuv.getGdSpv(), ItmSpf.class);
    }
    updLstItSpfs(pRvs, itmSpecLst, tastg, itlLuv, tstg, I18Itm.class,
      EItmTy.GOODS);
    pRvs.put("totUpdGdSp", itmSpecLst.size());
    itmSpecLst = null;
    List<PriItm> itmPriLst;
    if (refrAll) {
      itmPriLst = retItmPriLs(pRvs, null, PriItm.class, defPc.getIid());
    } else {
      itmPriLst = retItmPriLs(pRvs, itlLuv.getGdPrv(), PriItm.class,
        defPc.getIid());
    }
    updFoItPriLs(pRvs, itmPriLst, tastg, itlLuv, EItmTy.GOODS);
    pRvs.put("totUpdGdPr", itmPriLst.size());
    itmPriLst = null;
    List<ItmPlc> itmPlaceLst;
    if (refrAll) {
      itmPlaceLst = retItmPlcLst(pRvs, null, ItmPlc.class);
    } else {
      itmPlaceLst = retItmPlcLst(pRvs, itlLuv.getGdPlv(), ItmPlc.class);
    }
    updFoItmPlcLs(pRvs, itmPlaceLst, tastg, itlLuv, EItmTy.GOODS);
    pRvs.put("totUpdGdAv", itmPlaceLst.size());
    itmPlaceLst = null;
    List<SrvSpf> srvSpecLst;
    if (refrAll) {
      srvSpecLst = retItmSpfLs(pRvs, null, SrvSpf.class);
    } else {
      srvSpecLst = retItmSpfLs(pRvs, itlLuv.getSrSpv(), SrvSpf.class);
    }
    updLstItSpfs(pRvs, srvSpecLst, tastg, itlLuv, tstg, I18Srv.class,
      EItmTy.SERVICE);
    pRvs.put("totUpdServSp", srvSpecLst.size());
    srvSpecLst = null;
    List<PriSrv> srvPriLst;
    if (refrAll) {
      srvPriLst = retItmPriLs(pRvs, null, PriSrv.class, defPc.getIid());
    } else {
      srvPriLst = retItmPriLs(pRvs, itlLuv.getSrPrv(), PriSrv.class,
        defPc.getIid());
    }
    updFoItPriLs(pRvs, srvPriLst, tastg, itlLuv, EItmTy.SERVICE);
    pRvs.put("totUpdServPr", srvPriLst.size());
    srvPriLst = null;
    List<SrvPlc> srvPlaceLst;
    if (refrAll) {
      srvPlaceLst = retItmPlcLst(pRvs, null, SrvPlc.class);
    } else {
      srvPlaceLst = retItmPlcLst(pRvs, itlLuv.getSrPlv(), SrvPlc.class);
    }
    updFoItmPlcLs(pRvs, srvPlaceLst, tastg, itlLuv, EItmTy.SERVICE);
    pRvs.put("totUpdServAv", srvPlaceLst.size());
    srvPlaceLst = null;
    List<SeItmSpf> seGoodSpecLst;
    if (refrAll) {
      seGoodSpecLst = retItmSpfLs(pRvs, null, SeItmSpf.class);
    } else {
      seGoodSpecLst = retItmSpfLs(pRvs, itlLuv.getSgdSpv(), SeItmSpf.class);
    }
    updLstItSpfs(pRvs, seGoodSpecLst, tastg, itlLuv, tstg,
      I18SeItm.class, EItmTy.SEGOODS);
    pRvs.put("totUpdSeGoodSp", seGoodSpecLst.size());
    seGoodSpecLst = null;
    List<SeItmPri> seGoodPriLst;
    if (refrAll) {
      seGoodPriLst = retItmPriLs(pRvs, null, SeItmPri.class,
        defPc.getIid());
    } else {
      seGoodPriLst = retItmPriLs(pRvs, itlLuv.getSgdPrv(), SeItmPri.class,
        defPc.getIid());
    }
    updFoItPriLs(pRvs, seGoodPriLst, tastg, itlLuv, EItmTy.SEGOODS);
    pRvs.put("totUpdSeGoodPr", seGoodPriLst.size());
    seGoodPriLst = null;
    List<SeItmPlc> seGoodPlaceLst;
    if (refrAll) {
      seGoodPlaceLst = retItmPlcLst(pRvs, null, SeItmPlc.class);
    } else {
      seGoodPlaceLst = retItmPlcLst(pRvs, itlLuv.getSgdPlv(), SeItmPlc.class);
    }
    updFoItmPlcLs(pRvs, seGoodPlaceLst, tastg, itlLuv, EItmTy.SEGOODS);
    pRvs.put("totUpdSeGoodAv", seGoodPlaceLst.size());
    seGoodPlaceLst = null;
    List<SeSrvSpf> seSrvSpfLst;
    if (refrAll) {
      seSrvSpfLst = retItmSpfLs(pRvs, null, SeSrvSpf.class);
    } else {
      seSrvSpfLst = retItmSpfLs(pRvs, itlLuv.getSsrSpv(), SeSrvSpf.class);
    }
    updLstItSpfs(pRvs, seSrvSpfLst, tastg, itlLuv, tstg,
      I18SeSrv.class, EItmTy.SESERVICE);
    pRvs.put("totUpdSeSrvSp", seSrvSpfLst.size());
    seSrvSpfLst = null;
    List<SeSrvPri> sePriSrvLst;
    if (refrAll) {
      sePriSrvLst = retItmPriLs(pRvs, null, SeSrvPri.class,
        defPc.getIid());
    } else {
      sePriSrvLst = retItmPriLs(pRvs, itlLuv.getSsrPrv(), SeSrvPri.class,
        defPc.getIid());
    }
    updFoItPriLs(pRvs, sePriSrvLst, tastg, itlLuv, EItmTy.SESERVICE);
    pRvs.put("totUpdSeSrvPr", sePriSrvLst.size());
    sePriSrvLst = null;
    List<SeSrvPlc> seSrvPlcLst;
    if (refrAll) {
      seSrvPlcLst = retItmPlcLst(pRvs, null, SeSrvPlc.class);
    } else {
      seSrvPlcLst = retItmPlcLst(pRvs, itlLuv.getSsrPlv(), SeSrvPlc.class);
    }
    updFoItmPlcLs(pRvs, seSrvPlcLst, tastg, itlLuv, EItmTy.SESERVICE);
    pRvs.put("totUpdSeSrvAv", seSrvPlcLst.size());
    seSrvPlcLst = null;
    pRqd.setAttr("rnd", "rflst");
  }

  /**
   * <p>Retrieve start data.</p>
   * @param pRvs additional param
   * @throws Exception - an exception
   **/
  public final void retStData(final Map<String, Object> pRvs) throws Exception {
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      Map<String, Object> vs = new HashMap<String, Object>();
      ItlLuv itlLuv = new ItlLuv();
      itlLuv.setIid(1L);
      getOrm().refrEnt(pRvs, vs, itlLuv);
      if (itlLuv.getIid() == null) {
        itlLuv = new ItlLuv();
        itlLuv.setIid(1L);
        getOrm().insIdLn(pRvs, vs, itlLuv);
      }
      pRvs.put("itlLuv", itlLuv);
      this.rdb.commit();
    } catch (Exception ex) {
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw ex;
    } finally {
      this.rdb.release();
    }
  }

  /**
   * <p>Retrieve Item Places (outdated or all).</p>
   * @param <T> itm place type
   * @param pRvs additional param
   * @param pLuv last updated version or null for all
   * @param pItmPlcCl Item Place Class
   * @return Outdated Item Place list
   * @throws Exception - an exception
   **/
  public final <T extends AItmPlc<?, ?>> List<T> retItmPlcLst(
    final Map<String, Object> pRvs, final Long pLuv,
      final Class<T> pItmPlcCl) throws Exception {
    List<T> result = null;
    Map<String, Object> vs = new HashMap<String, Object>();
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      String tblNm = pItmPlcCl.getSimpleName().toUpperCase();
      String verCond;
      if (pLuv != null) {
        verCond = " where " + tblNm + ".VER>" + pLuv.toString();
      } else {
        verCond = "";
      }
      result = getOrm().retLstCnd(pRvs, vs, pItmPlcCl,
        verCond + " order by " + tblNm + ".VER asc");
      this.rdb.commit();
    } catch (Exception ex) {
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw ex;
    } finally {
      this.rdb.release();
    }
    return result;
  }

  /**
   * <p>Update Itlist with Item Place.</p>
   * @param <T> itm place type
   * @param <I> itm type
   * @param pRvs additional param
   * @param pItmPlc Item Place
   * @param pItTy EItmTy
   * @throws Exception - an exception
   **/
  public final <T extends AItmPlc<I, ?>, I extends IIdLnNm> void updFoItmPlc(
    final Map<String, Object> pRvs, final T pItmPlc,
      final EItmTy pItTy) throws Exception {
    String whereStr = "TYP=" + pItTy.ordinal() + " and ITID="
      + pItmPlc.getItm().getIid();
    Map<String, Object> vs = new HashMap<String, Object>();
    Itlist itlst = getOrm().retEntCnd(pRvs, vs, Itlist.class, whereStr);
    if (itlst == null) {
      itlst = creItlst(pRvs, pItmPlc.getItm());
    }
    itlst.setQuan(pItmPlc.getQuan());
    if (itlst.getIsNew()) {
      getOrm().insIdLn(pRvs, vs, itlst);
    } else {
      getOrm().update(pRvs, vs, itlst);
    }
  }

  /**
   * <p>Update Itlist withitem place list.
   * It does it with [N]-records per transaction method.</p>
   * @param <T> itm place type
   * @param <I> itm type
   * @param pRvs additional param
   * @param pItmPlcLst itm place list
   * @param pAddStg settings Add
   * @param pItlLuv itlLuv
   * @param pItTy EItmTy
   * @throws Exception - an exception
   **/
  public final <T extends AItmPlc<I, ?>, I extends IIdLnNm> void updFoItmPlcLs(
    final Map<String, Object> pRvs, final List<T> pItmPlcLst,
      final AddStg pAddStg, final ItlLuv pItlLuv,
        final EItmTy pItTy) throws Exception {
    if (pItmPlcLst.size() == 0) {
      //Beige ORM may return empty list
      return;
    }
    int steps = pItmPlcLst.size() / pAddStg.getRcsTr();
    int curStp = 1;
    Long lstVer = null;
    Map<String, Object> vs = new HashMap<String, Object>();
    do {
      try {
        this.rdb.setAcmt(false);
        this.rdb.setTrIsl(this.trIsl);
        this.rdb.begin();
        int stepLen = Math.min(pItmPlcLst.size(), curStp * pAddStg.getRcsTr());
        for (int i = (curStp - 1) * pAddStg.getRcsTr(); i < stepLen; i++) {
          T itmPlc = pItmPlcLst.get(i);
          updFoItmPlc(pRvs, itmPlc,  pItTy);
          lstVer = itmPlc.getVer();
        }
        if (pItTy == EItmTy.GOODS) {
          pItlLuv.setGdPlv(lstVer);
        } else if (pItTy == EItmTy.SERVICE) {
          pItlLuv.setSrPlv(lstVer);
        } else if (pItTy == EItmTy.SEGOODS) {
          pItlLuv.setSgdPlv(lstVer);
        } else if (pItTy == EItmTy.SESERVICE) {
          pItlLuv.setSsrPlv(lstVer);
        } else {
          throw new Exception("NEI for " + pItTy);
        }
        getOrm().update(pRvs, vs, pItlLuv);
        this.rdb.commit();
      } catch (Exception ex) {
        if (!this.rdb.getAcmt()) {
          this.rdb.rollBack();
        }
        throw ex;
      } finally {
        this.rdb.release();
      }
    } while (curStp++ <= steps);
  }

  /**
   * <p>Retrieve itm price list (outdated or all).</p>
   * @param <T> itm price type
   * @param pRvs additional param
   * @param pLuv last updated version or null for all
   * @param pItmPriCl Item Pri Class
   * @param pPrCatId default online price category ID
   * @return Outdated itm price list
   * @throws Exception - an exception
   **/
  public final <T extends AItmPri<?, ?>> List<T> retItmPriLs(
    final Map<String, Object> pRvs, final Long pLuv, final Class<T> pItmPriCl,
      final Long pPrCatId) throws Exception {
    List<T> result = null;
    Map<String, Object> vs = new HashMap<String, Object>();
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      String tblNm = pItmPriCl.getSimpleName().toUpperCase();
      String verCond = "where PRICT=" + pPrCatId;
      if (pLuv != null) {
        verCond += " and " + tblNm + ".VER>" + pLuv.toString();
      }
      result = getOrm().retLstCnd(pRvs, vs, pItmPriCl,
        verCond + " order by " + tblNm + ".VER asc");
      this.rdb.commit();
    } catch (Exception ex) {
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw ex;
    } finally {
      this.rdb.release();
    }
    return result;
  }

  /**
   * <p>Update Itlist for Item Pri.</p>
   * @param <T> itm price type
   * @param <I> itm type
   * @param pRvs additional param
   * @param pItmPri Item Pri
   * @param pItTy EItmTy
   * @throws Exception - an exception
   **/
  public final <T extends AItmPri<I, ?>, I extends IIdLnNm> void updFoItmPri(
    final Map<String, Object> pRvs, final T pItmPri,
      final EItmTy pItTy) throws Exception {
    String whereStr = "TYP=" + pItTy.ordinal() + " and ITID="
      + pItmPri.getItm().getIid();
    Map<String, Object> vs = new HashMap<String, Object>();
    Itlist itlst = getOrm().retEntCnd(pRvs, vs, Itlist.class, whereStr);
    if (itlst == null) {
      itlst = creItlst(pRvs, pItmPri.getItm());
    }
    itlst.setPri(pItmPri.getPri());
    itlst.setUnSt(pItmPri.getUnSt());
    itlst.setPriPr(pItmPri.getPriPr());
    itlst.setUom(pItmPri.getUom());
    if (itlst.getIsNew()) {
      getOrm().insIdLn(pRvs, vs, itlst);
    } else {
      getOrm().update(pRvs, vs, itlst);
    }
  }

  /**
   * <p>Update Itlist with itm price list.
   * It does it with [N]-records per transaction method.</p>
   * @param <T> itm price type
   * @param <I> itm type
   * @param pRvs additional param
   * @param pItPriLs itm price list
   * @param pAddStg settings Add
   * @param pItlLuv itlLuv
   * @param pItTy EItmTy
   * @throws Exception - an exception
   **/
  public final <T extends AItmPri<I, ?>, I extends IIdLnNm> void updFoItPriLs(
   final Map<String, Object> pRvs, final List<T> pItPriLs, final AddStg pAddStg,
      final ItlLuv pItlLuv, final EItmTy pItTy) throws Exception {
    if (pItPriLs.size() == 0) {
      //Beige ORM may return empty list
      return;
    }
    int steps = pItPriLs.size() / pAddStg.getRcsTr();
    int curStp = 1;
    Long lstVer = null;
    Map<String, Object> vs = new HashMap<String, Object>();
    do {
      try {
        this.rdb.setAcmt(false);
        this.rdb.setTrIsl(this.trIsl);
        this.rdb.begin();
        int stepLen = Math.min(pItPriLs.size(), curStp * pAddStg.getRcsTr());
        for (int i = (curStp - 1) * pAddStg.getRcsTr(); i < stepLen; i++) {
          T itemPri = pItPriLs.get(i);
          updFoItmPri(pRvs, itemPri, pItTy);
          lstVer = itemPri.getVer();
        }
        if (pItTy == EItmTy.GOODS) {
          pItlLuv.setGdPrv(lstVer);
        } else if (pItTy == EItmTy.SERVICE) {
          pItlLuv.setSrPrv(lstVer);
        } else if (pItTy == EItmTy.SEGOODS) {
          pItlLuv.setSgdPrv(lstVer);
        } else if (pItTy == EItmTy.SESERVICE) {
          pItlLuv.setSsrPrv(lstVer);
        } else {
          throw new Exception("NEI for " + pItTy);
        }
        getOrm().update(pRvs, vs, pItlLuv);
        this.rdb.commit();
      } catch (Exception ex) {
        if (!this.rdb.getAcmt()) {
          this.rdb.rollBack();
        }
        throw ex;
      } finally {
        this.rdb.release();
      }
    } while (curStp++ <= steps);
  }

  /**
   * <p>Retrieve Item Spec list outdated or all.</p>
   * @param <T> Item Spec type
   * @param <I> Item type
   * @param pRvs additional param
   * @param pLuv last updated version - null all
   * @param pItSpCls ItemSpec Class
   * @return Item Spec list
   * @throws Exception - an exception
   **/
  public final <T extends AItmSpf<I, ?>, I extends IIdLnNm> List<T> retItmSpfLs(
    final Map<String, Object> pRvs, final Long pLuv,
      final Class<T> pItSpCls) throws Exception {
    List<T> result = null;
    String verCond;
    if (pLuv != null) {
      String tblNm = pItSpCls.getSimpleName().toUpperCase();
      verCond = " where " + tblNm + ".ITM in " + " (select distinct  ITM from "
        + tblNm + " join ITMSP on " + tblNm + ".SPEC=ITMSP.IID where "
          + tblNm + ".VER>" + pLuv.toString() + ")";
    } else {
      verCond = "";
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      vs.put("ItmSpdpLv", 1); //HTML templates only ID
      vs.put("ItmndFds", new String[] {"nme"});
      String[] soiFds = new String[] {"nme", "inLst", "typ", "grp", "htmt"};
      Arrays.sort(soiFds);
      vs.put("ItmSpndFds", soiFds);
      String[] soigFds = new String[] {"nme", "tmpls", "tmple", "tmpld"};
      Arrays.sort(soigFds);
      vs.put("ItmSpGrndFds", soigFds);
      result = getOrm().retLstCnd(pRvs, vs, pItSpCls, verCond
        + " order by ITM10.IID, SPEC11.IDX asc"); vs.clear();
      this.rdb.commit();
    } catch (Exception ex) {
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw ex;
    } finally {
      this.rdb.release();
    }
    //make list of goods ordered by max(last updated specific) for that goods
    //goods itsVersion holds max(last updated specific):
    List<I> itmsFoSpf = new ArrayList<I>();
    //also list of all used HTML templates ID to retrieve their full-filled list
    Set<Long> htmltsIds = new HashSet<Long>();
    I currItem = null;
    for (AItmSpf<I, ?> gs : result) {
      if (gs.getSpec().getHtmt() != null) {
        htmltsIds.add(gs.getSpec().getHtmt().getIid());
      }
      if (gs.getSpec().getGrp() != null) {
        if (gs.getSpec().getGrp().getTmpls() != null) {
          htmltsIds.add(gs.getSpec().getGrp().getTmpls().getIid());
        }
        if (gs.getSpec().getGrp().getTmple() != null) {
          htmltsIds.add(gs.getSpec().getGrp().getTmple().getIid());
        }
        if (gs.getSpec().getGrp().getTmpls() != null) {
          htmltsIds.add(gs.getSpec().getGrp().getTmpld().getIid());
        }
      }
      if (currItem == null || !gs.getItm().getIid().equals(currItem.getIid())) {
        currItem = gs.getItm();
        currItem.setVer(gs.getVer());
        itmsFoSpf.add(currItem);
      } else { //2-nd, 3-d... specifics of this goods
        if (currItem.getVer() < gs.getVer()) {
          currItem.setVer(gs.getVer());
        }
      }
    }
    CmpHasVr<I> cmp = new CmpHasVr<I>();
    Collections.sort(itmsFoSpf, cmp);
    pRvs.put("itmsFoSpf", itmsFoSpf);
    pRvs.put("htmltsIds", htmltsIds);
    return result;
  }

  /**
   * <p>Update Itlist.SpecificInList with outdated ItmSpf.</p>
   * @param <T> itm specifics type
   * @param pRvs additional param
   * @param pAddStg AddStg
   * @param pOutdGdSp outdated ItmSpf
   * @param pItlist Itlist
   * @param pItmSpGrsWas ItmSpGr previous
   * @param pI18SpeLis I18SpeLi list
   * @param pI18SpecLst I18ItmSp list
   * @param pI18ChSpecLst I18ChoSp list
   * @param pI18UomLst I18Uom list
   * @throws Exception - an exception
   **/
  public final <T extends AItmSpf<?, ?>> void updLstItSpf(
    final Map<String, Object> pRvs, final AddStg pAddStg, final T pOutdGdSp,
      final Itlist pItlist, final ItmSpGr pItmSpGrsWas,
        final List<I18SpeLi> pI18SpeLis, final List<I18ItmSp> pI18SpecLst,
          final List<I18ChoSp> pI18ChSpecLst,
            final List<I18Uom> pI18UomLst) throws Exception {
    String val1 = "";
    String val2 = "";
    @SuppressWarnings("unchecked")
    List<UsPrf> usPrfs = (List<UsPrf>) pRvs.get("usPrfs");
    UsPrf curLp = usPrfs.get(0);
    for (UsPrf lp : usPrfs) {
      if (lp.getDef()) {
        curLp = lp;
        break;
      }
    }
    if (pOutdGdSp.getSpec().getTyp().equals(EItmSpTy.TEXT)) {
      //i18 not implemented, use chooseable specifics instead
      val1 = pOutdGdSp.getStr1();
    } else if (pOutdGdSp.getSpec().getTyp().equals(EItmSpTy.BIGDECIMAL)) {
      String dcSpv;
      String dcGrSpv;
      if (curLp.getDcSp().getIid().equals(DcSp.SPACEID)) {
        dcSpv = DcSp.SPACEVL;
      } else if (curLp.getDcSp().getIid().equals(DcSp.EMPTYID)) {
        dcSpv = DcSp.EMPTYVL;
      } else {
        dcSpv = curLp.getDcSp().getIid();
      }
      if (curLp.getDcGrSp().getIid().equals(DcSp.SPACEID)) {
        dcGrSpv = DcSp.SPACEVL;
      } else if (curLp.getDcGrSp().getIid().equals(DcSp.EMPTYID)) {
        dcGrSpv = DcSp.EMPTYVL;
      } else {
        dcGrSpv = curLp.getDcGrSp().getIid();
      }
      val1 = numStr.frmt(pOutdGdSp.getNum1().toString(), dcSpv, dcGrSpv,
        Integer.valueOf(pOutdGdSp.getLng1().intValue()), curLp.getDgInGr());
      if (pOutdGdSp.getStr1() != null) {
        val2 = pOutdGdSp.getStr1();
      }
    } else if (pOutdGdSp.getSpec().getTyp().equals(EItmSpTy.INTEGER)) {
      val1 = pOutdGdSp.getLng1().toString();
      if (pOutdGdSp.getStr1() != null) {
        val2 = pOutdGdSp.getStr1();
      }
    } else if (pOutdGdSp.getSpec().getTyp()
      .equals(EItmSpTy.CHOOSEABLE_SPECIFICS)) {
      val1 =  pOutdGdSp.getStr1();
    } else {
      return;
    }
    String tmpld;
    if (pOutdGdSp.getSpec().getHtmt() != null) {
      tmpld = pOutdGdSp.getSpec().getHtmt().getVal();
    } else if (pOutdGdSp.getSpec().getGrp() != null
      && pOutdGdSp.getSpec().getGrp().getTmpld() != null) {
      tmpld = pOutdGdSp.getSpec().getGrp().getTmpld().getVal();
    } else {
      tmpld = " <b>:SPECNM:</b> :VAL1:VAL2";
    }
    String spdet = tmpld.replace(":SPECNM",
      pOutdGdSp.getSpec().getNme());
    spdet = spdet.replace(":VAL1", val1);
    spdet = spdet.replace(":VAL2", val2);
    if (pOutdGdSp.getSpec().getGrp() != null && pItmSpGrsWas != null
      && pOutdGdSp.getSpec().getGrp().getIid().equals(pItmSpGrsWas.getIid())) {
      pItlist.setSpecs(pItlist.getSpecs() + pAddStg.getSpeSp() + spdet);
    } else {
      pItlist.setSpecs(pItlist.getSpecs() + spdet);
    }
    if (pI18SpeLis != null) {
      for (I18SpeLi i18spl : pI18SpeLis) {
        if (i18spl.getItId().equals(pItlist.getItId())
          && i18spl.getTyp().equals(pItlist.getTyp())) {
          for (UsPrf lp : usPrfs) {
            if (lp.getLng().getIid().equals(i18spl.getLng().getIid())) {
              curLp = lp;
              break;
            }
          }
          String dcSpv;
          String dcGrSpv;
          if (curLp.getDcSp().getIid().equals(DcSp.SPACEID)) {
            dcSpv = DcSp.SPACEVL;
          } else if (curLp.getDcSp().getIid().equals(DcSp.EMPTYID)) {
            dcSpv = DcSp.EMPTYVL;
          } else {
            dcSpv = curLp.getDcSp().getIid();
          }
          if (curLp.getDcGrSp().getIid().equals(DcSp.SPACEID)) {
            dcGrSpv = DcSp.SPACEVL;
          } else if (curLp.getDcGrSp().getIid().equals(DcSp.EMPTYID)) {
            dcGrSpv = DcSp.EMPTYVL;
          } else {
            dcGrSpv = curLp.getDcGrSp().getIid();
          }
          if (pOutdGdSp.getSpec().getTyp().equals(EItmSpTy.BIGDECIMAL)) {
            val1 = numStr.frmt(pOutdGdSp.getNum1().toString(), dcSpv, dcGrSpv,
           Integer.valueOf(pOutdGdSp.getLng1().intValue()), curLp.getDgInGr());
          }
          if (pOutdGdSp.getSpec().getTyp().equals(EItmSpTy.BIGDECIMAL)
            || pOutdGdSp.getSpec().getTyp().equals(EItmSpTy.INTEGER)
              && pOutdGdSp.getLng2() != null) {
            Uom uom = new Uom();
            uom.setNme(pOutdGdSp.getStr1());
            uom.setIid(pOutdGdSp.getLng2());
            val2 = fndUomNm(pI18UomLst, uom, i18spl.getLng());
          } else if (pOutdGdSp.getSpec().getTyp()
            .equals(EItmSpTy.CHOOSEABLE_SPECIFICS)) {
            ChoSp sp = new ChoSp();
            sp.setNme(pOutdGdSp.getStr1());
            sp.setIid(pOutdGdSp.getLng1());
            val1 =  fndChoSpNm(pI18ChSpecLst, sp, i18spl.getLng());
          }
          spdet = tmpld.replace(":SPECNM", fndSpecNm(pI18SpecLst,
            pOutdGdSp.getSpec(), i18spl.getLng()));
          spdet = spdet.replace(":VAL1", val1);
          if (val2 == null) { //UOM optional
            val2 = "";
          }
          spdet = spdet.replace(":VAL2", val2);
          if (pOutdGdSp.getSpec().getGrp() != null && pItmSpGrsWas != null
      && pOutdGdSp.getSpec().getGrp().getIid().equals(pItmSpGrsWas.getIid())) {
            i18spl.setVal(i18spl.getVal() + pAddStg.getSpeSp() + spdet);
          } else {
            i18spl.setVal(i18spl.getVal() + spdet);
          }
        }
      }
    }
  }

  /**
   * <p>Update Itlist with outdated itm specifics list.
   * It does it with [N]-records per transaction method.</p>
   * @param <I> itm type
   * @param <T> itm specifics type
   * @param pRvs additional param
   * @param pOudItSpfs outdated ItmSpf list
   * @param pAddStg settings Add
   * @param pItlLuv itlLuv
   * @param pTrdStg trading settings
   * @param pI18ItCls I18Item Class
   * @param pItTy EItmTy
   * @throws Exception - an exception
   **/
  public final <I extends AI18nNm<?, ?>, T extends AItmSpf<?, ?>> void
    updLstItSpfs(final Map<String, Object> pRvs, final List<T> pOudItSpfs,
      final AddStg pAddStg, final ItlLuv pItlLuv, final TrdStg pTrdStg,
        final Class<I> pI18ItCls, final EItmTy pItTy) throws Exception {
    if (pOudItSpfs.size() == 0) {
      //Beige ORM may return empty list
      return;
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    @SuppressWarnings("unchecked")
    List<IIdLnNm> itmsFoSpf = (List<IIdLnNm>) pRvs.get("itmsFoSpf");
    pRvs.remove("itmsFoSpf");
    @SuppressWarnings("unchecked")
    Set<Long> htmltsIds = (Set<Long>) pRvs.get("htmltsIds");
    pRvs.remove("htmltsIds");
    List<Htmlt> htmlts = null;
    List<Itlist> itmsInList = null;
    List<I18ChoSp> i18ChoSpLst = null;
    List<I18ItmSpGr> i18I18ItmSpGrs = null;
    List<I18ItmSp> i18ItmSpLst = null;
    List<I> i18ItemLst = null;
    List<I18SpeLi> i18SpeLis = null;
    List<I18Uom> i18UomLst = null;
    boolean dbgSh = getLog().getDbgSh(getClass(), 13200);
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      StringBuffer itmsIdsIn = new StringBuffer("(");
      boolean isFirst = true;
      for (IIdLnNm it : itmsFoSpf) {
        if (isFirst) {
          isFirst = false;
        } else {
          itmsIdsIn.append(", ");
        }
        itmsIdsIn.append(it.getIid().toString());
      }
      itmsIdsIn.append(")");
      if (dbgSh) {
        getLog().debug(pRvs, getClass(), "itmsIdsIn: " + itmsIdsIn);
      }
      itmsInList = getOrm().retLstCnd(pRvs, vs, Itlist.class, "where TYP="
        + pItTy.ordinal() + " and ITID in " + itmsIdsIn.toString());
      if (htmltsIds.size() > 0) {
        StringBuffer whereStr = new StringBuffer("where IID in (");
        isFirst = true;
        for (Long id : htmltsIds) {
          if (isFirst) {
            isFirst = false;
          } else {
            whereStr.append(", ");
          }
          whereStr.append(id.toString());
        }
        whereStr.append(")");
        htmlts = getOrm().retLstCnd(pRvs, vs, Htmlt.class, whereStr.toString());
      }
      if (pTrdStg.getAi18n()) {
        vs.put("LngdpLv", 0);
        i18ItemLst = getOrm().retLstCnd(pRvs, vs, pI18ItCls,
          "where HASNM in " + itmsIdsIn.toString()); vs.clear();
        if (i18ItemLst.size() > 0) {
          i18SpeLis = getOrm().retLstCnd(pRvs, vs, I18SpeLi.class, "where TYP="
            + pItTy.ordinal() + " and ITID in " + itmsIdsIn.toString());
          StringBuffer specGrIdIn = null;
          StringBuffer specChIdIn = null;
          StringBuffer specIdIn = new StringBuffer("(");
          isFirst = true;
          boolean isFirstGr = true;
          boolean isFirstCh = true;
          for (AItmSpf<?, ?> gs : pOudItSpfs) {
            if (isFirst) {
              isFirst = false;
            } else {
              specIdIn.append(", ");
            }
            specIdIn.append(gs.getSpec().getIid().toString());
            if (gs.getSpec().getGrp() != null) {
              if (isFirstGr) {
                specGrIdIn = new StringBuffer("(");
                isFirstGr = false;
              } else {
                specGrIdIn.append(", ");
              }
              specGrIdIn.append(gs.getSpec().getGrp().getIid().toString());
            }
            if (gs.getSpec().getTyp().equals(EItmSpTy.CHOOSEABLE_SPECIFICS)) {
              if (isFirstCh) {
                specChIdIn = new StringBuffer("(");
                isFirstCh = false;
              } else {
                specChIdIn.append(", ");
              }
              specChIdIn.append(gs.getLng1().toString());
            }
          }
          specIdIn.append(")");
          if (specGrIdIn != null) {
            specGrIdIn.append(")");
          }
          if (specChIdIn != null) {
            specChIdIn.append(")");
          }
          vs.put("I18ItmSpdpLv", 1);
          i18ItmSpLst = getOrm().retLstCnd(pRvs, vs, I18ItmSp.class,
            "where HASNM in " + specIdIn.toString()); vs.clear();
          vs.put("I18UomdpLv", 1);
          i18UomLst = getOrm().retLst(pRvs, vs, I18Uom.class); vs.clear();
          if (specGrIdIn != null) {
            vs.put("I18ItmSpGrdpLv", 1);
            i18I18ItmSpGrs = getOrm().retLstCnd(pRvs, vs, I18ItmSpGr.class,
              "where HASNM in " + specGrIdIn.toString()); vs.clear();
          }
          if (specChIdIn != null) {
            vs.put("I18ChodpLv", 1);
            i18ChoSpLst = getOrm().retLstCnd(pRvs, vs, I18ChoSp.class,
              "where HASNM in " + specChIdIn.toString()); vs.clear();
          }
        }
      }
      this.rdb.commit();
    } catch (Exception ex) {
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw ex;
    } finally {
      this.rdb.release();
    }
    if (i18SpeLis != null) {
      for (I18SpeLi i18spInLs : i18SpeLis) {
        if (pAddStg.getShtms() !=  null) {
          i18spInLs.setVal(pAddStg.getShtms());
        } else {
          i18spInLs.setVal("");
        }
      }
    }
    if (htmlts != null && htmlts.size() > 0) {
      for (AItmSpf<?, ?> gs : pOudItSpfs) {
        if (gs.getSpec().getHtmt() != null) {
          gs.getSpec().setHtmt(
          fndHtmlt(htmlts, gs.getSpec().getHtmt().getIid()));
        }
        if (gs.getSpec().getGrp() != null) {
          if (gs.getSpec().getGrp().getTmpls() != null) {
            gs.getSpec().getGrp().setTmpls(fndHtmlt(htmlts,
              gs.getSpec().getGrp().getTmpls().getIid()));
          }
          if (gs.getSpec().getGrp().getTmple() != null) {
            gs.getSpec().getGrp().setTmple(fndHtmlt(htmlts,
              gs.getSpec().getGrp().getTmple().getIid()));
          }
          if (gs.getSpec().getGrp().getTmpls() != null) {
            gs.getSpec().getGrp().setTmpld(fndHtmlt(htmlts,
              gs.getSpec().getGrp().getTmpld().getIid()));
          }
        }
      }
    }
    for (Itlist itlst : itmsInList) {
      itlst.setDetMt(null);
      itlst.setImg(null);
    }
    int steps = itmsFoSpf.size() / pAddStg.getRcsTr();
    int curStp = 1;
    Long lstVer = null;
    do {
      try {
        this.rdb.setAcmt(false);
        this.rdb.setTrIsl(this.trIsl);
        this.rdb.begin();
        int stepLen = Math.min(itmsFoSpf.size(), curStp * pAddStg.getRcsTr());
        for (int i = (curStp - 1) * pAddStg.getRcsTr(); i < stepLen; i++) {
          IIdLnNm itm = itmsFoSpf.get(i);
          Itlist itlst = fndItlst(itmsInList, itm.getIid(), pItTy);
          if (itlst == null) {
            itlst = creItlst(pRvs, itm);
          }
          int j = fnd1Idx(pOudItSpfs, itm);
          ItmSpGr itSpGrWas = null;
          //reset any way:
          itlst.setNme(itm.getNme());
          //i18:
          if (i18ItemLst != null && i18ItemLst.size() >  0) {
            for (I i18Item : i18ItemLst) {
              if (i18Item.getHasNm().getIid().equals(itm.getIid())) {
                I18SpeLi i18spInLs = fndI18SpeLi(i18SpeLis, itm, pItTy,
                  i18Item.getLng());
                if (i18spInLs == null) {
                  i18spInLs = new I18SpeLi();
                  i18spInLs.setIsNew(true);
                  i18spInLs.setTyp(pItTy);
                  i18spInLs.setItId(itm.getIid());
                  i18spInLs.setLng(i18Item.getLng());
                  i18SpeLis.add(i18spInLs);
                  if (pAddStg.getShtms() !=  null) {
                    i18spInLs.setVal(pAddStg.getShtms());
                  } else {
                    i18spInLs.setVal("");
                  }
                }
                i18spInLs.setNme(i18Item.getNme());
                break;
              }
            }
          }
          if (pAddStg.getShtms() !=  null) {
            itlst.setSpecs(pAddStg.getShtms());
          } else {
            itlst.setSpecs("");
          }
          boolean wasGrStart = false;
          do {
            if (pOudItSpfs.get(j).getSpec().getInLst()) {
              if (pOudItSpfs.get(j).getSpec().getTyp().equals(EItmSpTy.IMAGE)) {
                itlst.setImg(pOudItSpfs.get(j).getStr1());
              } else { // build Itlist.specs:
                if (pOudItSpfs.get(j).getSpec().getGrp() == null
                  || itSpGrWas == null || !pOudItSpfs.get(j).getSpec()
                    .getGrp().getIid().equals(itSpGrWas.getIid())) {
                  if (wasGrStart) {
                    if (pAddStg.getSpGrSp() != null
                      && pAddStg.getSghtme() != null) {
                      itlst.setSpecs(itlst.getSpecs() + pAddStg.getSghtme()
                        + pAddStg.getSpGrSp());
                      if (i18SpeLis != null) {
                        for (I18SpeLi i18spInLs : i18SpeLis) {
                          if (i18spInLs.getItId().equals(itm.getIid())
                            && i18spInLs.getTyp().equals(pItTy)) {
                            i18spInLs.setVal(i18spInLs.getVal()
                              + pAddStg.getSghtme() + pAddStg.getSpGrSp());
                          }
                        }
                      }
                    } else if (pAddStg.getSghtme() != null) {
                      itlst.setSpecs(itlst.getSpecs() + pAddStg.getSghtme());
                      if (i18SpeLis != null) {
                        for (I18SpeLi i18spInLs : i18SpeLis) {
                          if (i18spInLs.getItId().equals(itm.getIid())
                            && i18spInLs.getTyp().equals(pItTy)) {
                            i18spInLs.setVal(i18spInLs.getVal()
                              + pAddStg.getSghtme());
                          }
                        }
                      }
                    } else if (pAddStg.getSpGrSp() != null) {
                      itlst.setSpecs(itlst.getSpecs() + pAddStg.getSpGrSp());
                    }
                  }
                  wasGrStart = true;
                  if (pAddStg.getSghtms() != null) {
                    itlst.setSpecs(itlst.getSpecs() + pAddStg.getSghtms());
                    if (i18SpeLis != null) {
                      for (I18SpeLi i18spInLs : i18SpeLis) {
                        if (i18spInLs.getItId().equals(itm.getIid())
                          && i18spInLs.getTyp().equals(pItTy)) {
                          i18spInLs.setVal(i18spInLs.getVal()
                            + pAddStg.getSghtms());
                        }
                      }
                    }
                  }
                  if (pOudItSpfs.get(j).getSpec().getGrp() != null
                   && pOudItSpfs.get(j).getSpec().getGrp().getTmpls() != null) {
                    String grst = pOudItSpfs.get(j).getSpec().getGrp()
                      .getTmpls().getVal().replace(":SPECGRNM",
                        pOudItSpfs.get(j).getSpec().getGrp().getNme());
                    itlst.setSpecs(itlst.getSpecs() + grst);
                    if (i18SpeLis != null) {
                      for (I18SpeLi i18spInLs : i18SpeLis) {
                        if (i18spInLs.getItId().equals(itm.getIid())
                          && i18spInLs.getTyp().equals(pItTy)) {
                          String gn = fndItmSpGrNm(i18I18ItmSpGrs, pOudItSpfs
                            .get(j).getSpec().getGrp(), i18spInLs.getLng());
                          grst = pOudItSpfs.get(j).getSpec().getGrp().getTmpls()
                            .getVal().replace(":SPECGRNM", gn);
                          i18spInLs.setVal(i18spInLs.getVal() + grst);
                        }
                      }
                    }
                  }
                }
                updLstItSpf(pRvs, pAddStg, pOudItSpfs.get(j), itlst, itSpGrWas,
                  i18SpeLis, i18ItmSpLst, i18ChoSpLst, i18UomLst);
                itSpGrWas = pOudItSpfs.get(j).getSpec().getGrp();
              }
            } else {
              itlst.setDetMt(1);
            }
            j++;
          } while (j < pOudItSpfs.size()
            && pOudItSpfs.get(j).getItm().getIid().equals(itm.getIid()));
          if (pAddStg.getSghtme() != null) {
            itlst.setSpecs(itlst.getSpecs() + pAddStg.getSghtme());
            if (i18SpeLis != null) {
              for (I18SpeLi i18spInLs : i18SpeLis) {
                if (i18spInLs.getItId().equals(itm.getIid())
                  && i18spInLs.getTyp().equals(pItTy)) {
                  i18spInLs.setVal(i18spInLs.getVal() + pAddStg.getSghtme());
                }
              }
            }
          }
          if (pAddStg.getShtme() != null) {
            itlst.setSpecs(itlst.getSpecs() + pAddStg.getShtme());
            if (i18SpeLis != null) {
              for (I18SpeLi i18spInLs : i18SpeLis) {
                if (i18spInLs.getItId().equals(itm.getIid())
                  && i18spInLs.getTyp().equals(pItTy)) {
                  i18spInLs.setVal(i18spInLs.getVal() + pAddStg.getShtme());
                }
              }
            }
          }
          if (itlst.getIsNew()) {
            getOrm().insIdLn(pRvs, vs, itlst);
          } else {
            getOrm().update(pRvs, vs, itlst);
          }
          if (i18SpeLis != null) {
            for (I18SpeLi i18spInLs : i18SpeLis) {
              if (i18spInLs.getItId().equals(itm.getIid())
                && i18spInLs.getTyp().equals(pItTy)) {
                if (i18spInLs.getIsNew()) {
                  getOrm().insIdNln(pRvs, vs, i18spInLs);
                } else {
                  getOrm().update(pRvs, vs, i18spInLs);
                }
              }
            }
          }
          //itm holds itm-specifics version
          lstVer = itm.getVer();
        }
        if (pItTy.equals(EItmTy.GOODS)) {
          pItlLuv.setGdSpv(lstVer);
        } else if (pItTy.equals(EItmTy.SERVICE)) {
          pItlLuv.setSrSpv(lstVer);
        } else if (pItTy.equals(EItmTy.SEGOODS)) {
          pItlLuv.setSgdSpv(lstVer);
        } else if (pItTy.equals(EItmTy.SESERVICE)) {
          pItlLuv.setSsrSpv(lstVer);
        } else {
          throw new Exception("NYI for " + pItTy);
        }
        getOrm().update(pRvs, vs, pItlLuv);
        this.rdb.commit();
      } catch (Exception ex) {
        if (!this.rdb.getAcmt()) {
          this.rdb.rollBack();
        }
        throw ex;
      } finally {
        this.rdb.release();
      }
    } while (curStp++ <= steps);
  }

  /**
   * <p>Find find I18 Specific Group Name
   * with given specifics group and lang.</p>
   * @param pI18ItmSpGrs list
   * @param pSpecGr Specific Group
   * @param pLng lang
   * @return group name
   **/
  protected final String fndItmSpGrNm(final List<I18ItmSpGr> pI18ItmSpGrs,
    final ItmSpGr pSpecGr, final Lng pLng) {
    if (pI18ItmSpGrs != null) {
      for (I18ItmSpGr i18spg : pI18ItmSpGrs) {
        if (i18spg.getHasNm().getIid().equals(pSpecGr.getIid())
          && i18spg.getLng().getIid().equals(pLng.getIid())) {
          return i18spg.getNme();
        }
      }
    }
    return pSpecGr.getNme();
  }

  /**
   * <p>Find find I18 UOM Name with given UOM and lang.</p>
   * @param pI18UomLst list
   * @param pUom UOM
   * @param pLng lang
   * @return UOM name
   **/
  protected final String fndUomNm(final List<I18Uom> pI18UomLst,
    final Uom pUom, final Lng pLng) {
    for (I18Uom i18uom : pI18UomLst) {
      if (i18uom.getHasNm().getIid().equals(pUom.getIid())
        && i18uom.getLng().getIid().equals(pLng.getIid())) {
        return i18uom.getNme();
      }
    }
    return pUom.getNme();
  }

  /**
   * <p>Find find I18 Chooseable Specific Name with
   * given specifics and lang.</p>
   * @param pI18ChoSps list
   * @param pSpec Specific Group
   * @param pLng lang
   * @return specifics name
   **/
  protected final String fndChoSpNm(final List<I18ChoSp> pI18ChoSps,
    final ChoSp pSpec, final Lng pLng) {
    if (pI18ChoSps != null) {
      for (I18ChoSp i18sp : pI18ChoSps) {
        if (i18sp.getHasNm().getIid().equals(pSpec.getIid())
          && i18sp.getLng().getIid().equals(pLng.getIid())) {
          return i18sp.getNme();
        }
      }
    }
    return pSpec.getNme();
  }

  /**
   * <p>Find find I18 Specific Name with given specifics and lang.</p>
   * @param pI18ItmSps list
   * @param pSpec Specific Group
   * @param pLng lang
   * @return specifics name
   **/
  protected final String fndSpecNm(final List<I18ItmSp> pI18ItmSps,
    final ItmSp pSpec, final Lng pLng) {
    for (I18ItmSp i18sp : pI18ItmSps) {
      if (i18sp.getHasNm().getIid().equals(pSpec.getIid())
        && i18sp.getLng().getIid().equals(pLng.getIid())) {
        return i18sp.getNme();
      }
    }
    return pSpec.getNme();
  }

  /**
   * <p>Find I18SpeLi with given itm, type and lang.</p>
   * @param pI18SpeLis list
   * @param pItm itm
   * @param pItTy itm type
   * @param pLng lang
   * @return I18SpeLi with given goods and lang if exist or null
   **/
  protected final I18SpeLi fndI18SpeLi(final List<I18SpeLi> pI18SpeLis,
    final IIdLnNm pItm, final EItmTy pItTy, final Lng pLng) {
    int j = 0;
    while (j < pI18SpeLis.size()) {
      if (pI18SpeLis.get(j).getItId().equals(pItm.getIid())
        && pI18SpeLis.get(j).getTyp().equals(pItTy)
          && pI18SpeLis.get(j).getLng().getIid().equals(pLng.getIid())) {
        return pI18SpeLis.get(j);
      }
      j++;
    }
    return null;
  }

  /**
   * <p>Find Itlist with given itm ID and type.</p>
   * @param pItlsts items list
   * @param pItId Item ID
   * @param pItTy Item type
   * @return Itlist with given itm and type if exist or null
   **/
  protected final Itlist fndItlst(final List<Itlist> pItlsts, final Long pItId,
    final EItmTy pItTy) {
    int j = 0;
    while (j < pItlsts.size()) {
      if (pItlsts.get(j).getTyp().equals(pItTy)
        &&  pItlsts.get(j).getItId().equals(pItId)) {
        return pItlsts.get(j);
      }
      j++;
    }
    return null;
  }

  /**
   * <p>Find the first index of specific with given itm.</p>
   * @param <T> itm specifics type
   * @param pOudItSpfs GS list
   * @param pItm Goods
   * @return the first index of specific with given itm
   * it throws Exception - if out of bounds
   **/
  protected final <T extends AItmSpf<?, ?>> int fnd1Idx(
    final List<T> pOudItSpfs, final IIdLnNm pItm) {
    int j = 0;
    while (!pOudItSpfs.get(j).getItm().getIid().equals(pItm.getIid())) {
      j++;
    }
    return j;
  }

  /**
   * <p>Find Html Template with given ID.</p>
   * @param pHtmlts Html Templates
   * @param pId ID
   * @return template with given ID
   * @throws Exception - if not found
   **/
  protected final Htmlt fndHtmlt(final List<Htmlt> pHtmlts,
    final Long pId) throws Exception {
    for (Htmlt tmpl : pHtmlts) {
      if (tmpl.getIid().equals(pId)) {
        return tmpl;
      }
    }
    throw new Exception("Template not found for ID: " + pId);
  }

  /**
   * <p>Create Itlist for itm.</p>
   * @param <I> itm type
   * @param pRvs additional param
   * @param pItm Item
   * @return Itlist
   * @throws Exception - an exception
   **/
  protected final <I extends IIdLnNm> Itlist creItlst(
    final Map<String, Object> pRvs, final I pItm) throws Exception {
    Itlist itlst = new Itlist();
    itlst.setIsNew(true);
    EItmTy itmTy;
    if (pItm.getClass() == Itm.class) {
      itmTy = EItmTy.GOODS;
    } else if (pItm.getClass() == Srv.class) {
      itmTy = EItmTy.SERVICE;
    } else if (pItm.getClass() == SeItm.class) {
      itmTy = EItmTy.SEGOODS;
    } else if (pItm.getClass() == SeSrv.class) {
      itmTy = EItmTy.SESERVICE;
    } else {
      throw new Exception("NYI for " + pItm.getClass());
    }
    itlst.setTyp(itmTy);
    itlst.setItId(pItm.getIid());
    itlst.setNme(pItm.getNme());
    itlst.setQuan(BigDecimal.ZERO);
    return itlst;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for rdb.</p>
   * @return IRdb
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
   * <p>Getter for trIsl.</p>
   * @return Integer
   **/
  public final Integer getTrIsl() {
    return this.trIsl;
  }

  /**
   * <p>Setter for trIsl.</p>
   * @param pTrIsl reference
   **/
  public final void setTrIsl(final Integer pTrIsl) {
    this.trIsl = pTrIsl;
  }

  /**
   * <p>Geter for orm.</p>
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
   * <p>Getter for numStr.</p>
   * @return INumStr
   **/
  public final INumStr getNumStr() {
    return this.numStr;
  }

  /**
   * <p>Setter for numStr.</p>
   * @param pNumStr reference
   **/
  public final void setNumStr(final INumStr pNumStr) {
    this.numStr = pNumStr;
  }

  /**
   * <p>Geter for log.</p>
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
}
