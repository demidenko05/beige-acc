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

package org.beigesoft.ws.srv;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.math.BigDecimal;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.ColVals;
import org.beigesoft.log.ILog;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.acc.mdlp.Uom;
import org.beigesoft.ws.mdl.EOrdStas;
import org.beigesoft.ws.mdl.EPaymentMethod;
import org.beigesoft.ws.mdl.Purch;
import org.beigesoft.ws.mdlp.SeSel;
import org.beigesoft.ws.mdlp.SeSerBus;
import org.beigesoft.ws.mdlp.SerBus;
import org.beigesoft.ws.mdlp.SeItmPlc;
import org.beigesoft.ws.mdlp.ItmPlc;
import org.beigesoft.ws.mdlp.SrvPlc;
import org.beigesoft.ws.mdlp.SeSrvPlc;
import org.beigesoft.ws.mdlp.Buyer;
import org.beigesoft.ws.mdlp.CuOrSe;
import org.beigesoft.ws.mdlp.CuOrSeSrLn;
import org.beigesoft.ws.mdlp.CuOrSeGdLn;
import org.beigesoft.ws.mdlp.CuOr;
import org.beigesoft.ws.mdlp.CuOrGdLn;
import org.beigesoft.ws.mdlp.CuOrSrLn;
import org.beigesoft.ws.mdlp.AddStg;
import org.beigesoft.ws.mdlp.Itlist;

/**
 * <p>It accepts all buyer's orders.
 * It changes item's availability and orders status to PENDING.
 * If any item is unavailable, then it throws exception.
 * And so does if there are several payees for online payment.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class AcpOrd<RS> implements IAcpOrd {

  /**
   * <p>Logger.</p>
   **/
  private ILog log;

  /**
   * <p>ORM service.</p>
   */
  private IOrm orm;

  /**
   * <p>DB service.</p>
   */
  private IRdb<RS> rdb;

  /**
   * <p>Query goods availability checkout.</p>
   **/
  private String quOrGdChk;

  /**
   * <p>Query services availability checkout.</p>
   **/
  private String quOrSrChk;

  /**
   * <p>Fastest locking (only record locking), i.e.:
   * update WAREHOUSEREST set THEREST=THEREST-1.5
   * where UNITOFMEASURE=5 and INVITEM=1 and WAREHOUSESITE=1.
   * It will fail when THEREST become less than 0.
   * Update is atomic RDBMS operation, i.e. it
   * locks affected record during invocation.</p>
   **/
  private boolean fastLoc = true;

  /**
   * <p>It accepts all buyer's orders.
   * It changes item's availability and orders status to PENDING.
   * If any item is unavailable, then it throws exception.
   * And so does if there are several payees for online payment.</p>
   * @param pRvs additional request scoped parameters
   * @param pReqDt Request Data
   * @param pBur Buyer
   * @return list of accepted orders
   * @throws Exception - an exception
   **/
  @Override
  public final Purch accept(final Map<String, Object> pRvs,
    final IReqDt pReqDt, final Buyer pBur) throws Exception {
    Purch rez = null;
    AddStg setAdd = (AddStg) pRvs.get("setAdd");
    List<CuOr> ords = null;
    List<CuOrSe> sords = null;
    String tbn = CuOr.class.getSimpleName();
    String wheStBr = "where STAS=0 and BUYR=" + pBur.getIid();
    String[] ndFlNm = new String[] {"nme"};
    pRvs.put("PicPlcndFds", ndFlNm);
    pRvs.put(tbn + "buyerdpLv", 1);
    ords = this.orm.retrieveListWithConditions(pRvs,
      CuOr.class, wheStBr); vs.clear();
    vs.put("SeSelndFds", new String[] {"dbcr"});
    vs.put("DbCrndFds", ndFlNm);
    vs.put("SeSeldpLv", 2);
    vs.put("buyerdpLv", 0);
    sords = this.orm.retrieveListWithConditions(pRvs,
      CuOrSe.class, wheStBr);
    pRvs.remove("DbCrndFds");
    if (setAdd.getOnlMd() == 0 && sords.size() > 0) {
      //checking for several online payees:
      boolean isOwnOnlPay = false;
      for (CuOr co : ords) {
        if (co.getPayMeth().equals(EPaymentMethod.ONLINE)
          || co.getPayMeth().equals(EPaymentMethod.PARTIAL_ONLINE)
            || co.getPayMeth().equals(EPaymentMethod.PAYPAL)
              || co.getPayMeth().equals(EPaymentMethod.PAYPAL_ANY)) {
          isOwnOnlPay = true;
          break;
        }
      }
      SeSel selOnl = null;
      for (CuOrSe co : sords) {
        if (co.getPayMeth().equals(EPaymentMethod.ONLINE)
          || co.getPayMeth().equals(EPaymentMethod.PARTIAL_ONLINE)
            || co.getPayMeth().equals(EPaymentMethod.PAYPAL)
              || co.getPayMeth().equals(EPaymentMethod.PAYPAL_ANY)) {
          if (isOwnOnlPay) {
            throw new Exception("Several online payee for buyer#"
              + pBur.getIid());
          } else if (selOnl == null) {
            selOnl = co.getSel();
          } else if (!selOnl.getIid().getIid()
            .equals(co.getSel().getIid().getIid())) {
            throw new Exception("Several online S.E.Payee for buyer#"
              + pBur.getIid());
          }
        }
      }
    }
    //consolidated order with bookable items for farther booking:
    if (ords.size() > 0) {
      CuOr cor = check1(pRvs, ords);
      adChekBook(pRvs, cor);
    }
    if (sords.size() > 0) {
      CuOrSe cor = checkSe1(pRvs, sords);
      adChekBookSe(pRvs, cor);
    }
    //change orders status:
    if (setAdd.getOpMd() == 0) {
      String[] ndFds = new String[] {"ver", "stas"};
      pRvs.put("ndFds", ndFds);
      for (CuOr co : ords) {
        co.setStas(EOrdStas.BOOKED);
        getOrm().update(pRvs, vs, co);
      }
      for (CuOrSe co : sords) {
        co.setStas(EOrdStas.BOOKED);
        getOrm().update(pRvs, vs, co);
      }
      pRvs.remove("ndFds");
    } else {
      ColVals cvs = new ColVals();
      cvs.put("ver", new Date().getTime());
      cvs.put("stas", EOrdStas.BOOKED.ordinal());
      this.rdb.update("CUOR", cvs, "STAS=0 and BUYR="
        + pBur.getIid());
      this.rdb.executeUpdate("CUORSE", cvs, "STAS=0 and BUYR="
        + pBur.getIid());
    }
    rez = new Purch();
    if (ords.size() > 0) {
      rez.setOrds(ords);
    }
    if (sords.size() > 0) {
      rez.setSords(sords);
    }
    return rez;
  }

  //utils:
  /**
   * <p>It half-checks items. Order can has items from several places.</p>
   * @param pRvs additional request scoped parameters
   * @param pOrds S.E. orders
   * @return consolidated order with bookable items
   * @throws Exception - an exception if checking fail
   **/
  public final CuOrSe checkSe1(final Map<String, Object> pRvs,
    final List<CuOrSe> pOrds) throws Exception {
    StringBuffer ordIds = null;
    for (CuOrSe co : pOrds) {
      co.setGoods(new ArrayList<CuOrSeGdLn>());
      co.setSrvs(new ArrayList<CuOrSeSrLn>());
      if (ordIds == null) {
        ordIds = new StringBuffer();
        ordIds.append(co.getIid().toString());
      } else {
        ordIds.append("," + co.getIid());
      }
    }
    String[] ndFlNm = new String[] {"nme"};
    Set<String> ndFl = new HashSet<String>();
    ndFl.add("itsId");
    ndFl.add("itsOwner");
    ndFl.add("itsName");
    ndFl.add("good");
    ndFl.add("uom");
    ndFl.add("quant");
    ndFl.add("price");
    ndFl.add("tot");
    ndFl.add("totTx");
    String tbnUom = Uom.class.getSimpleName();
    pRvs.put(tbn + "CuOrSeGdLnndFds", ndFl);
    pRvs.put(tbn + "gooddpLv", 1);
    pRvs.put(tbn + "itsOwnerdpLv", 1);
    pRvs.put(tbnUom + "ndFds", ndFlNm);
    List<CuOrSeGdLn> allGoods = new ArrayList<CuOrSeGdLn>();
    List<CuOrSeSrLn> allServs = new ArrayList<CuOrSeSrLn>();
    String quer = lazyGetQuOrGdChk().replace(":TORLN", "CUORSEGDLN")
      .replace(":TITPL", "SEGOODSPLACE").replace(":ORIDS", ordIds.toString());
    List<CuOrSeGdLn> allGds = this.orm.retrieveListByQuery(
      pRvs, CuOrSeGdLn.class, quer);
    for (CuOrSeGdLn gl : allGds) {
      if (gl.getQuan().compareTo(BigDecimal.ZERO) == 0) {
        throw new Exception("S.E.Good is not available #"
          + gl.getGood().getIid());
      }
    }
    for (CuOrSeGdLn gl : allGds) {
      for (CuOrSe co : pOrds) {
        if (co.getIid().equals(gl.getItsOwner().getIid())) {
          gl.setItsOwner(co);
          co.getGoods().add(gl);
        }
      }
      CuOrSeGdLn cgl = new CuOrSeGdLn();
      cgl.setIid(gl.getIid());
      cgl.setGood(gl.getGood());
      cgl.setQuan(gl.getQuan());
      allGoods.add(cgl);
    }
    pRvs.remove(tbn + "gooddpLv");
    pRvs.remove(tbn + "ndFds");
    pRvs.remove(tbn + "itsOwnerdpLv");
    ndFl.remove("good");
    ndFl.add("service");
    ndFl.add("dt1");
    ndFl.add("dt2");
    tbn = CuOrSeSrLn.class.getSimpleName();
    pRvs.put(tbn + "ndFds", ndFl);
    pRvs.put(tbn + "servicedpLv", 1);
    pRvs.put(tbn + "itsOwnerdpLv", 1);
    //non-bookable service checkout and bookable services half-checkout:
    quer = lazyGetQuOrSrChk().replace(":TORLN", "CUORSESRLN")
      .replace(":TITPL", "SESERVICEPLACE").replace(":ORIDS", ordIds.toString());
    List<CuOrSeSrLn> allSrvs = this.orm.retrieveListByQuery(
      pRvs, CuOrSeSrLn.class, quer);
    for (CuOrSeSrLn sl : allSrvs) {
      if (sl.getQuan().compareTo(BigDecimal.ZERO) == 0) {
        throw new Exception("Service is not available #"
          + sl.getSrv().getIid());
      }
    }
    for (CuOrSeSrLn sl : allSrvs) {
      for (CuOrSe co : pOrds) {
        if (co.getIid().equals(sl.getItsOwner().getIid())) {
          sl.setItsOwner(co);
          co.getSrvs().add(sl);
        }
      }
      CuOrSeSrLn csl = new CuOrSeSrLn();
      csl.setIid(sl.getIid());
      csl.setSrv(sl.getSrv());
      csl.setQuan(sl.getQuan());
      csl.setDt1(sl.getDt1());
      csl.setDt2(sl.getDt2());
      allServs.add(csl);
    }
    pRvs.remove(tbn + "servicedpLv");
    pRvs.remove(tbn + "ndFds");
    pRvs.remove(tbn + "itsOwnerdpLv");
    pRvs.remove(tbnUom + "ndFds");
    CuOrSe cor = new CuOrSe();
    cor.setGoods(allGoods);
    cor.setSrvs(allServs);
    return cor;
  }

  /**
   * <p>It half-checks items. Order can has items from several  places.</p>
   * @param pRvs additional request scoped parameters
   * @param pOrds orders
   * @return consolidated order with bookable items
   * @throws Exception - an exception if checking fail
   **/
  public final CuOr check1(final Map<String, Object> pRvs,
    final List<CuOr> pOrds) throws Exception {
    StringBuffer ordIds = null;
    for (CuOr co : pOrds) {
      co.setGoods(new ArrayList<CuOrGdLn>());
      co.setSrvs(new ArrayList<CuOrSrLn>());
      if (ordIds == null) {
        ordIds = new StringBuffer();
        ordIds.append(co.getIid().toString());
      } else {
        ordIds.append("," + co.getIid());
      }
    }
    String[] ndFlNm = new String[] {"nme"};
    Set<String> ndFl = new HashSet<String>();
    ndFl.add("itsId");
    ndFl.add("itsOwner");
    ndFl.add("itsName");
    ndFl.add("good");
    ndFl.add("uom");
    ndFl.add("quant");
    ndFl.add("price");
    ndFl.add("tot");
    ndFl.add("totTx");
    String tbn = CuOrGdLn.class.getSimpleName();
    String tbnUom = Uom.class.getSimpleName();
    pRvs.put(tbn + "ndFds", ndFl);
    pRvs.put(tbn + "gooddpLv", 1);
    pRvs.put(tbn + "itsOwnerdpLv", 1);
    pRvs.put(tbnUom + "ndFds", ndFlNm);
    List<CuOrGdLn> allGoods = new ArrayList<CuOrGdLn>();
    List<CuOrSrLn> allServs = new ArrayList<CuOrSrLn>();
    String quer = lazyGetQuOrGdChk().replace(":TORLN", "CUSTORDERGDLN")
      .replace(":TITPL", "GOODSPLACE").replace(":ORIDS", ordIds.toString());
    List<CuOrGdLn> allGds = this.orm.retrieveListByQuery(
      pRvs, CuOrGdLn.class, quer);
    for (CuOrGdLn gl : allGds) {
      if (gl.getQuan().compareTo(BigDecimal.ZERO) == 0) {
        throw new Exception("Good is not available #"
          + gl.getGood().getIid());
      }
    }
    for (CuOrGdLn gl : allGds) {
      for (CuOr co : pOrds) {
        if (co.getIid().equals(gl.getItsOwner().getIid())) {
          gl.setItsOwner(co);
          co.getGoods().add(gl);
        }
      }
      CuOrGdLn cgl = new CuOrGdLn();
      cgl.setIid(gl.getIid());
      cgl.setGood(gl.getGood());
      cgl.setQuan(gl.getQuan());
      allGoods.add(cgl);
    }
    pRvs.remove(tbn + "gooddpLv");
    pRvs.remove(tbn + "ndFds");
    pRvs.remove(tbn + "itsOwnerdpLv");
    ndFl.remove("good");
    ndFl.add("service");
    ndFl.add("dt1");
    ndFl.add("dt2");
    tbn = CuOrSrLn.class.getSimpleName();
    pRvs.put(tbn + "ndFds", ndFl);
    pRvs.put(tbn + "servicedpLv", 1);
    pRvs.put(tbn + "itsOwnerdpLv", 1);
    //non-bookable service checkout and bookable services half-checkout:
    quer = lazyGetQuOrSrChk().replace(":TORLN", "CUSTORDERSRVLN")
      .replace(":TITPL", "SERVICEPLACE").replace(":ORIDS", ordIds.toString());
    List<CuOrSrLn> allSrvs = this.orm.retrieveListByQuery(
      pRvs, CuOrSrLn.class, quer);
    for (CuOrSrLn sl : allSrvs) {
      if (sl.getQuan().compareTo(BigDecimal.ZERO) == 0) {
        throw new Exception("Service is not available #"
          + sl.getSrv().getIid());
      }
    }
    for (CuOrSrLn sl : allSrvs) {
      for (CuOr co : pOrds) {
        if (co.getIid().equals(sl.getItsOwner().getIid())) {
          sl.setItsOwner(co);
          co.getSrvs().add(sl);
        }
      }
      CuOrSrLn csl = new CuOrSrLn();
      csl.setIid(sl.getIid());
      csl.setSrv(sl.getSrv());
      csl.setQuan(sl.getQuan());
      csl.setDt1(sl.getDt1());
      csl.setDt2(sl.getDt2());
      allServs.add(csl);
    }
    pRvs.remove(tbn + "servicedpLv");
    pRvs.remove(tbn + "ndFds");
    pRvs.remove(tbn + "itsOwnerdpLv");
    pRvs.remove(tbnUom + "ndFds");
    CuOr cor = new CuOr();
    cor.setGoods(allGoods);
    cor.setSrvs(allServs);
    return cor;
  }

  /**
   * <p>It checks additionally and books S.E. items.</p>
   * @param pRvs additional request scoped parameters
   * @param pCoOr consolidate order
   * @throws Exception - an exception if incomplete
   **/
  public final void adChekBookSe(final Map<String, Object> pRvs,
    final CuOrSe pCoOr) throws Exception {
    //additional checking:
    String tbn;
    //check availability and booking for same good in different orders:
    List<CuOrSeGdLn> gljs = null;
    List<CuOrSeGdLn> glrs = null;
    for (CuOrSeGdLn gl : pCoOr.getGoods()) {
      //join lines with same item:
      for (CuOrSeGdLn gl0 : pCoOr.getGoods()) {
        if (!gl.getIid().equals(gl0.getIid())
          && gl.getGood().getIid().equals(gl0.getGood().getIid())) {
          if (gljs == null) {
            gljs = new ArrayList<CuOrSeGdLn>();
            glrs = new ArrayList<CuOrSeGdLn>();
          }
          glrs.add(gl0);
          if (!gljs.contains(gl)) {
            gljs.add(gl);
          }
          gl.setQuan(gl.getQuan().add(gl0.getQuan()));
        }
      }
    }
    if (gljs != null) {
      for (CuOrSeGdLn glr : glrs) {
        pCoOr.getGoods().remove(glr);
      }
      tbn = SeItmPlc.class.getSimpleName();
      pRvs.put(tbn + "itemdpLv", 1); //only ID
      pRvs.put(tbn + "pickUpPlacedpLv", 1);
      for (CuOrSeGdLn gl : gljs) {
        List<SeItmPlc> gps = getOrm().retrieveListWithConditions(pRvs,
          SeItmPlc.class, "where ITM=" + gl.getGood().getIid()
            + " and ITSQUANTITY>=" + gl.getQuan());
        if (gps.size() == 0) {
          throw new Exception("AC. S.E.Good is not available #"
            + gl.getGood().getIid());
        }
      }
      pRvs.remove(tbn + "itemdpLv");
      pRvs.remove(tbn + "pickUpPlacedpLv");
    }
    //bookable services final-checkout:
    String cond;
    tbn = SeSrvPlc.class.getSimpleName();
    pRvs.put(tbn + "itemdpLv", 1); //only ID
    pRvs.put(tbn + "pickUpPlacedpLv", 1);
    for (CuOrSeSrLn sl : pCoOr.getSrvs()) {
      if (sl.getDt1() != null) {
    cond = "left join (select distinct SRV from SESERBUS where FRE=0 and SRV="
    + sl.getSrv().getIid() + " and FRTM>=" + sl.getDt1().getTime()
  + " and TITM<" + sl.getDt2().getTime()
+ ") as SERBUS on SERBUS.SRV=SESERVICEPLACE.ITM where ITM=" + sl
  .getSrv() + " and ITSQUANTITY>0 and SERBUS.SRV is null";
        List<SeSrvPlc> sps = getOrm()
          .retrieveListWithConditions(pRvs, SeSrvPlc.class, cond);
        if (sps.size() == 0) {
          throw new Exception("AC. BK.Service is not available #"
            + sl.getSrv().getIid());
        }
      }
    }
    pRvs.remove(tbn + "itemdpLv");
    pRvs.remove(tbn + "pickUpPlacedpLv");
    //booking:
    //changing availability (booking):
    ColVals cvsIil = null;
    String[] fnmIil = null;
    if (this.fastLoc) {
      cvsIil = new ColVals();
      cvsIil.getFormula().add("quan");
    } else {
      fnmIil = new String[] {"itsId", "ver", "quan"};
    }
    tbn = SeItmPlc.class.getSimpleName();
    pRvs.put(tbn + "itemdpLv", 1); //only ID
    pRvs.put(tbn + "pickUpPlacedpLv", 1);
    for (CuOrSeGdLn gl : pCoOr.getGoods()) {
      List<SeItmPlc> gps = getOrm().retrieveListWithConditions(pRvs,
        SeItmPlc.class, "where ALWAY=0 and ITM=" + gl.getGood()
          .getIid());
      if (gps.size() != 0) {
        BigDecimal avQu = BigDecimal.ZERO;
        for (SeItmPlc gp : gps) {
          avQu = avQu.add(gp.getQuan());
        }
        if (avQu.compareTo(gl.getQuan()) == -1) {
          //previous test should not be passed!!!
          throw new Exception("AC. S.E.Good is not available #"
            + gl.getGood().getIid());
        }
        BigDecimal rst = gl.getQuan();
        for (SeItmPlc gp : gps) {
          if (rst.compareTo(BigDecimal.ZERO) == 0) {
            break;
          }
          if (gp.getQuan().compareTo(gl.getQuan()) == -1) {
            rst = rst.subtract(gp.getQuan());
            gp.setQuan(BigDecimal.ZERO);
          } else {
            gp.setQuan(gp.getQuan().subtract(rst));
            rst = BigDecimal.ZERO;
          }
          //TODO PERFORM fastupd
          getOrm().update(pRvs, vs, gp);
        }
        String wheTyId = "TYP=2 and ITID=" + gl.getGood().getIid();
        if (this.fastLoc) {
          //it must be RDBMS constraint: "quan>=0"!
          cvsIil.put("ver", new Date().getTime());
          cvsIil.put("quan", "QUAN-"
            + gl.getQuan());
          this.rdb.executeUpdate("ITLIST", cvsIil, wheTyId);
        } else {
          pRvs.put("ndFds", fnmIil);
          List<Itlist> iils = this.orm.retrieveListWithConditions(
            pRvs, Itlist.class, "where " + wheTyId);
          if (iils.size() == 1) {
            BigDecimal aq = iils.get(0).getQuan()
              .subtract(gl.getQuan());
            if (aq.compareTo(BigDecimal.ZERO) == -1) {
              pRvs.remove("ndFds");
    throw new Exception("Itlist NA avQuan SeGood: id/itId/avQua/quan: "
  + iils.get(0).getIid() + "/" + gl.getGood().getIid() + "/"
+ iils.get(0).getQuan() + "/" + gl.getQuan());
            } else {
              iils.get(0).setQuan(aq);
              getOrm().update(pRvs, vs, iils.get(0));
            }
          } else {
            pRvs.remove("ndFds");
            throw new Exception("Itlist WC for SeGood: itId/count: "
              + gl.getGood().getIid() + "/" + iils.size());
          }
          pRvs.remove("ndFds");
        }
      }
    }
    pRvs.remove(tbn + "itemdpLv");
    pRvs.remove(tbn + "pickUpPlacedpLv");
    tbn = SeSrvPlc.class.getSimpleName();
    pRvs.put(tbn + "itemdpLv", 1); //only ID
    pRvs.put(tbn + "pickUpPlacedpLv", 1);
    boolean tibs = false;
    for (CuOrSeSrLn sl : pCoOr.getSrvs()) {
      if (sl.getDt1() == null) {
        List<SeSrvPlc> sps = getOrm().retrieveListWithConditions(pRvs,
          SeSrvPlc.class, "where ALWAY=0 and ITM=" + sl.getSrv()
            .getIid());
        if (sps.size() != 0) {
          BigDecimal avQu = BigDecimal.ZERO;
          for (SeSrvPlc sp : sps) {
            avQu = avQu.add(sp.getQuan());
          }
          if (avQu.compareTo(sl.getQuan()) == -1) {
            //previous test should not be passed!!!
            throw new Exception("AC. S.E.Service is not available #"
              + sl.getSrv().getIid());
          }
          BigDecimal rst = sl.getQuan();
          for (SeSrvPlc sp : sps) {
            if (rst.compareTo(BigDecimal.ZERO) == 0) {
              break;
            }
            if (sp.getQuan().compareTo(sl.getQuan()) == -1) {
              rst = rst.subtract(sp.getQuan());
              sp.setQuan(BigDecimal.ZERO);
            } else {
              sp.setQuan(sp.getQuan().subtract(rst));
              rst = BigDecimal.ZERO;
            }
            //TODO PERFORM fastupd
            getOrm().update(pRvs, vs, sp);
          }
          String wheTyId = "TYP=3 and ITID=" + sl.getSrv().getIid();
          if (this.fastLoc) {
            cvsIil.put("ver", new Date().getTime());
            cvsIil.put("quan",  "QUAN-"
              + sl.getQuan());
            this.rdb.executeUpdate("ITLIST", cvsIil, wheTyId);
          } else {
            pRvs.put("ndFds", fnmIil);
            List<Itlist> iils = this.orm.retrieveListWithConditions(
              pRvs, Itlist.class, "where " + wheTyId);
            if (iils.size() == 1) {
              BigDecimal aq = iils.get(0).getQuan()
                .subtract(sl.getQuan());
              if (aq.compareTo(BigDecimal.ZERO) == -1) {
                pRvs.remove("ndFds");
      throw new Exception("Itlist NA avQuan SESERV: id/itId/avQua/quan: "
    + iils.get(0).getIid() + "/" + sl.getSrv().getIid() + "/"
  + iils.get(0).getQuan() + "/" + sl.getQuan());
              } else {
                iils.get(0).setQuan(aq);
                getOrm().update(pRvs, vs, iils.get(0));
              }
            } else {
              pRvs.remove("ndFds");
              throw new Exception("Itlist WC for SESERV: itId/count: "
                + sl.getSrv().getIid() + "/" + iils.size());
            }
            pRvs.remove("ndFds");
          }
        }
      } else {
        tibs = true;
      }
    }
    pRvs.remove(tbn + "itemdpLv");
    pRvs.remove(tbn + "pickUpPlacedpLv");
    if (tibs) {
      tbn = SeSerBus.class.getSimpleName();
      Set<String> ndFl = new HashSet<String>();
      ndFl.add("itsId");
      ndFl.add("ver");
      pRvs.put(tbn + "ndFds", ndFl);
      List<SeSerBus> sbas = this.orm.retrieveListWithConditions(pRvs,
        SeSerBus.class, "where FRE=1");
      int i = 0;
      pRvs.remove(tbn + "ndFds");
      for (CuOrSeSrLn sl : pCoOr.getSrvs()) {
        if (sl.getDt1() != null) {
          SeSerBus sb;
          if (i < sbas.size()) {
            sb = sbas.get(i);
            sb.setFre(false);
          } else {
            sb = new SeSerBus();
          }
          sb.setSrv(sl.getSrv());
          sb.setFrTm(sl.getDt1());
          sb.setTiTm(sl.getDt2());
          if (i < sbas.size()) {
            getOrm().update(pRvs, vs, sb);
            i++;
          } else {
            getOrm().insIdLn(pRvs, vs, sb);
          }
        }
      }
    }
  }

  /**
   * <p>It checks additionally and books items.</p>
   * @param pRvs additional request scoped parameters
   * @param pCoOr consolidate order
   * @throws Exception - an exception if incomplete
   **/
  public final void adChekBook(final Map<String, Object> pRvs,
    final CuOr pCoOr) throws Exception {
    //additional checking:
    String tbn;
    //check availability and booking for same good in different orders:
    List<CuOrGdLn> gljs = null;
    List<CuOrGdLn> glrs = null;
    for (CuOrGdLn gl : pCoOr.getGoods()) {
      //join lines with same item:
      for (CuOrGdLn gl0 : pCoOr.getGoods()) {
        if (!gl.getIid().equals(gl0.getIid())
          && gl.getGood().getIid().equals(gl0.getGood().getIid())) {
          if (gljs == null) {
            gljs = new ArrayList<CuOrGdLn>();
            glrs = new ArrayList<CuOrGdLn>();
          }
          glrs.add(gl0);
          if (!gljs.contains(gl)) {
            gljs.add(gl);
          }
          gl.setQuan(gl.getQuan().add(gl0.getQuan()));
        }
      }
    }
    if (gljs != null) {
      for (CuOrGdLn glr : glrs) {
        pCoOr.getGoods().remove(glr);
      }
      tbn = ItmPlc.class.getSimpleName();
      pRvs.put(tbn + "itemdpLv", 1); //only ID
      pRvs.put(tbn + "pickUpPlacedpLv", 1);
      for (CuOrGdLn gl : gljs) {
        List<ItmPlc> gps = getOrm().retrieveListWithConditions(pRvs,
          ItmPlc.class, "where ITM=" + gl.getGood().getIid()
            + " and ITSQUANTITY>=" + gl.getQuan());
        if (gps.size() == 0) {
          throw new Exception("AC. Good is not available #"
            + gl.getGood().getIid());
        }
      }
      pRvs.remove(tbn + "itemdpLv");
      pRvs.remove(tbn + "pickUpPlacedpLv");
    }
    //bookable services final-checkout:
    String cond;
    tbn = SrvPlc.class.getSimpleName();
    pRvs.put(tbn + "itemdpLv", 1); //only ID
    pRvs.put(tbn + "pickUpPlacedpLv", 1);
    for (CuOrSrLn sl : pCoOr.getSrvs()) {
      if (sl.getDt1() != null) {
      cond = "left join (select distinct SRV from SERBUS where FRE=0 and SRV="
    + sl.getSrv().getIid() + " and FRTM>=" + sl.getDt1().getTime()
  + " and TITM<" + sl.getDt2().getTime()
+ ") as SERBUS on SERBUS.SRV=SERVICEPLACE.ITM where ITM=" + sl
  .getSrv() + " and ITSQUANTITY>0 and SERBUS.SRV is null";
        List<SrvPlc> sps = getOrm()
          .retrieveListWithConditions(pRvs, SrvPlc.class, cond);
        if (sps.size() == 0) {
          throw new Exception("AC. BK.Service is not available #"
            + sl.getSrv().getIid());
        }
      }
    }
    vs.clear();
    //booking:
    //changing availability (booking):
    ColVals cvsIil = null;
    String[] fnmIil = null;
    if (this.fastLoc) {
      cvsIil = new ColVals();
      cvsIil.getFormula().add("quan");
    } else {
      fnmIil = new String[] {"ver", "quan"};
    }
    vs.put("ItmdpLv", 0); //only ID
    vs.put("PicPlcdpLv", 0);
    for (CuOrGdLn gl : pCoOr.getGoods()) {
      List<ItmPlc> gps = getOrm().retrieveListWithConditions(pRvs,
        ItmPlc.class, "where ALWAY=0 and ITM="
          + gl.getGood().getIid());
      if (gps.size() != 0) {
        BigDecimal avQu = BigDecimal.ZERO;
        for (ItmPlc gp : gps) {
          avQu = avQu.add(gp.getQuan());
        }
        if (avQu.compareTo(gl.getQuan()) == -1) {
          //previous test should not be passed!!!
          throw new Exception("S.E.Good is not available #"
            + gl.getGood().getIid());
        }
        BigDecimal rst = gl.getQuan();
        for (ItmPlc gp : gps) {
          if (rst.compareTo(BigDecimal.ZERO) == 0) {
            break;
          }
          if (gp.getQuan().compareTo(gl.getQuan()) == -1) {
            rst = rst.subtract(gp.getQuan());
            gp.setQuan(BigDecimal.ZERO);
          } else {
            gp.setQuan(gp.getQuan().subtract(rst));
            rst = BigDecimal.ZERO;
          }
          //TODO PERFORM fastupd
          getOrm().update(pRvs, vs, gp);
        }
        String wheTyId = "TYP=0 and ITID=" + gl.getGood().getIid();
        if (this.fastLoc) {
          //it must be RDBMS constraint: "quan>=0"!
          cvsIil.put("ver", new Date().getTime());
          cvsIil.put("quan", "QUAN-" + gl.getQuan());
          this.rdb.update(Itlist.class, cvsIil, wheTyId);
        } else {
          vs.put("ItlistndFds", fnmIil);
          List<Itlist> iils = this.orm.retLstCnd(pRvs, vs, Itlist.class,
            "where " + wheTyId); vs.clear();
          if (iils.size() == 1) {
            BigDecimal aq = iils.get(0).getQuan()
              .subtract(gl.getQuan());
            if (aq.compareTo(BigDecimal.ZERO) == -1) {
      throw new Exception("Itlist NA avQuan InvItem: id/itId/avQua/quan: "
    + iils.get(0).getIid() + "/" + gl.getGood().getIid() + "/"
  + iils.get(0).getQuan() + "/" + gl.getQuan());
            } else {
              iils.get(0).setQuan(aq);
              getOrm().update(pRvs, vs, iils.get(0));
            }
          } else {
            throw new Exception("Itlist WC for InvItem: itId/count: "
              + gl.getGood().getIid() + "/" + iils.size());
          }
          vs.clear();
        }
      }
    }
    vs.clear();
    vs.put("SrvdpLv", 0); //only ID
    vs.put("PicPlcdpLv", 0);
    boolean tibs = false;
    for (CuOrSrLn sl : pCoOr.getSrvs()) {
      if (sl.getDt1() == null) {
        List<SrvPlc> sps = getOrm().retLstCnd(pRvs, vs, SrvPlc.class,
          "where ALWAY=0 and ITM=" + sl.getSrv().getIid());
        if (sps.size() != 0) {
          BigDecimal avQu = BigDecimal.ZERO;
          for (SrvPlc sp : sps) {
            avQu = avQu.add(sp.getQuan());
          }
          if (avQu.compareTo(sl.getQuan()) == -1) {
            //previous test should not be passed!!!
            throw new Exception("AC. S.E.Service is not available #"
              + sl.getSrv().getIid());
          }
          BigDecimal rst = sl.getQuan();
          for (SrvPlc sp : sps) {
            if (rst.compareTo(BigDecimal.ZERO) == 0) {
              break;
            }
            if (sp.getQuan().compareTo(sl.getQuan()) == -1) {
              rst = rst.subtract(sp.getQuan());
              sp.setQuan(BigDecimal.ZERO);
            } else {
              sp.setQuan(sp.getQuan().subtract(rst));
              rst = BigDecimal.ZERO;
            }
            //TODO PERFORM fastupd
            getOrm().update(pRvs, vs, sp);
          }
          String wheTyId = "TYP=1 and ITID=" + sl.getSrv().getIid();
          if (this.fastLoc) {
            cvsIil.put("ver", new Date().getTime());
            cvsIil.put("quan",  "QUAN-" + sl.getQuan());
            this.rdb.update(Itlist.class, cvsIil, wheTyId);
          } else {
            vs.put("ItlistndFds", fnmIil);
            List<Itlist> iils = this.orm.retLstCnd(pRvs, vs, Itlist.class,
              "where " + wheTyId); vs.clear();
            if (iils.size() == 1) {
              BigDecimal aq = iils.get(0).getQuan()
                .subtract(sl.getQuan());
              if (aq.compareTo(BigDecimal.ZERO) == -1) {
      throw new Exception("Itlist NA avQuan SRV: id/itId/avQua/quan: "
    + iils.get(0).getIid() + "/" + sl.getSrv().getIid() + "/"
  + iils.get(0).getQuan() + "/" + sl.getQuan());
              } else {
                iils.get(0).setQuan(aq);
                getOrm().update(pRvs, vs, iils.get(0));
              }
            } else {
              throw new Exception("Itlist WC for SRV: itId/count: "
                + sl.getSrv().getIid() + "/" + iils.size());
            }
          }
        }
      } else {
        tibs = true;
      }
    }
    vs.clear();
    if (tibs) {
      vs.put(tbn + "SerBusndFds", new String[] {"ver"});
      List<SerBus> sbas = this.orm.retLstCnd(pRvs, vs, SerBus.class,
        "where FRE=1"); vs.clear();
      int i = 0;
      for (CuOrSrLn sl : pCoOr.getSrvs()) {
        if (sl.getDt1() != null) {
          SerBus sb;
          if (i < sbas.size()) {
            sb = sbas.get(i);
            sb.setFre(false);
          } else {
            sb = new SerBus();
          }
          sb.setSrv(sl.getSrv());
          sb.setFrTm(sl.getDt1());
          sb.setTiTm(sl.getDt2());
          if (i < sbas.size()) {
            getOrm().update(pRvs, vs, sb);
            i++;
          } else {
            getOrm().insIdLn(pRvs, vs, sb);
          }
        }
      }
    }
  }

  /**
   * <p>Lazy Getter for quOrGdChk.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  public final String lazyGetQuOrGdChk() throws IOException {
    if (this.quOrGdChk == null) {
      String flName = "/ws/ordGdChk.sql";
      this.quOrGdChk = loadString(flName);
    }
    return this.quOrGdChk;
  }

  /**
   * <p>Lazy Getter for quOrSrChk.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  public final String lazyGetQuOrSrChk() throws IOException {
    if (this.quOrSrChk == null) {
      String flName = "/ws/ordSrChk.sql";
      this.quOrSrChk = loadString(flName);
    }
    return this.quOrSrChk;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFileName file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final String loadString(final String pFileName)
        throws IOException {
    URL urlFile = AcpOrd.class
      .getResource(pFileName);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = AcpOrd.class
          .getResourceAsStream(pFileName);
        byte[] bArray = new byte[inputStream.available()];
        inputStream.read(bArray, 0, inputStream.available());
        return new String(bArray, "UTF-8");
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }
      }
    }
    return null;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for log.</p>
   * @return ILog
   **/
  public final ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLogger reference
   **/
  public final void setLog(final ILog pLogger) {
    this.log = pLogger;
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
   * <p>Getter for fastLoc.</p>
   * @return boolean
   **/
  public final boolean getFastLoc() {
    return this.fastLoc;
  }

  /**
   * <p>Setter for fastLoc.</p>
   * @param pFastLoc reference
   **/
  public final void setFastLoc(final boolean pFastLoc) {
    this.fastLoc = pFastLoc;
  }
}
