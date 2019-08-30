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

package org.beigesoft.ws.prc;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.log.ILog;
import org.beigesoft.prc.IPrc;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.ws.mdl.Purch;
import org.beigesoft.ws.mdlp.CuOrSeGdLn;
import org.beigesoft.ws.mdlp.CuOrSeSrLn;
import org.beigesoft.ws.mdlp.CuOrSe;
import org.beigesoft.ws.mdlp.CuOr;
import org.beigesoft.ws.mdlp.CuOrGdLn;
import org.beigesoft.ws.mdlp.CuOrSrLn;
import org.beigesoft.ws.mdlp.Buyer;
import org.beigesoft.ws.fct.FcPrWs;
import org.beigesoft.ws.srv.IBuySr;

/**
 * <p>
 * Service that shows buyer's orders from just made purchase.
 * </p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrBur<RS> implements IPrc {

  /**
   * <p>Logger.</p>
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
   * <p>Buyer service.</p>
   **/
  private IBuySr buySr;

  /**
   * <p>Processors factory.</p>
   **/
  private FcPrWs<RS> fcPrWs;

  /**
   * <p>Transaction isolation.</p>
   **/
  private Integer trIsl;

  /**
   * <p>Process request.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    Buyer buyer = null;
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      buyer = this.buySr.getAuthBuyr(pRvs, pRqDt);
      if (buyer != null) {
        Map<String, Object> vs = new HashMap<String, Object>();
        String purIdStr = pRqDt.getParam("pur");
        Long.parseLong(purIdStr);
        String whePuBr = "where PUR=" + purIdStr + " and BUYR="
          + buyer.getIid();
        String[] ndFlNm = new String[]  {"nme"};
        vs.put("PicPlcndFds", ndFlNm);
        vs.put("BuyerdpLv", 0);
        List<CuOr> ords = this.orm.retLstCnd(pRvs, vs, CuOr.class, whePuBr);
        vs.clear();
        vs.put("DbCrndFds", ndFlNm);
        vs.put("SeSelndFds", new String[] {"dbcr"});
        vs.put("SeSeldpLv", 2);
        vs.put("BuyerdpLv", 0);
        List<CuOrSe> sords = this.orm.retLstCnd(pRvs, vs, CuOrSe.class,
          whePuBr);
        vs.clear();
        retLines(pRvs, buyer, ords, sords);
        Purch pur = new Purch();
        pur.setOrds(ords);
        pur.setSords(sords);
        pRvs.put("pur",  pur);
        pRqDt.setAttr("rnd", "waor");
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
    if (buyer == null) {
  this.log.warn(pRvs, getClass(), "Trying to view purchase without buyer!");
      redir(pRvs, pRqDt);
      return;
    }
  }

  /**
   * <p>Retrieve order lines.</p>
   * @param pRvs request scoped vars
   * @param pBur buyer
   * @param pOrds orders
   * @param pSords S.E. orders
   * @throws Exception - an exception
   **/
  public final void retLines(final Map<String, Object> pRvs,
    final Buyer pBur, final List<CuOr> pOrds,
      final List<CuOrSe> pSords) throws Exception {
    StringBuffer ordIds = null;
    for (CuOr co : pOrds) {
      co.setGoods(new ArrayList<CuOrGdLn>());
      co.setServs(new ArrayList<CuOrSrLn>());
      if (ordIds == null) {
        ordIds = new StringBuffer();
        ordIds.append(co.getIid().toString());
      } else {
        ordIds.append("," + co.getIid());
      }
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    String[] ndFlNm = new String[]  {"nme"};
    String[] ndFl = new String[] {"ownr", "nme", "uom", "quan", "pri", "tot",
      "toTx"};
    Arrays.sort(ndFl);
    if (ordIds != null) {
      vs.put("CuOrGdLnndFds", ndFl);
      vs.put("CuOrdpLv", 1);
      vs.put("UomndFds", ndFlNm);
      List<CuOrGdLn> allGds = this.orm.retLstCnd(pRvs, vs, CuOrGdLn.class,
        "where OWNR in (" + ordIds + ")"); vs.clear();
      for (CuOrGdLn il : allGds) {
        for (CuOr co : pOrds) {
          if (co.getIid().equals(il.getOwnr().getIid())) {
            co.getGoods().add(il);
            break;
          }
        }
      }
      vs.put("CuOrSrLnndFds", ndFl);
      vs.put("CuOrdpLv", 1);
      vs.put("UomndFds", ndFlNm);
      List<CuOrSrLn> allSrs = this.orm.retLstCnd(pRvs, vs, CuOrSrLn.class,
        "where OWNR in (" + ordIds + ")"); vs.clear();
      for (CuOrSrLn il : allSrs) {
        for (CuOr co : pOrds) {
          if (co.getIid().equals(il.getOwnr().getIid())) {
            co.getServs().add(il);
            break;
          }
        }
      }
    }
    ordIds = null;
    for (CuOrSe co : pSords) {
      co.setGoods(new ArrayList<CuOrSeGdLn>());
      co.setServs(new ArrayList<CuOrSeSrLn>());
      if (ordIds == null) {
        ordIds = new StringBuffer();
        ordIds.append(co.getIid().toString());
      } else {
        ordIds.append("," + co.getIid());
      }
    }
    if (ordIds != null) {
      vs.put("CuOrSeGdLnndFds", ndFl);
      vs.put("CuOrSedpLv", 1);
      vs.put("UomndFds", ndFlNm);
      List<CuOrSeGdLn> allGds = this.orm.retLstCnd(pRvs, vs, CuOrSeGdLn.class,
        "where OWNR in (" + ordIds + ")"); vs.clear();
      for (CuOrSeGdLn il : allGds) {
        for (CuOrSe co : pSords) {
          if (co.getIid().equals(il.getOwnr().getIid())) {
            co.getGoods().add(il);
            break;
          }
        }
      }
      vs.put("CuOrSeSrLnndFds", ndFl);
      vs.put("CuOrSedpLv", 0);
      vs.put("UomndFds", ndFlNm);
      List<CuOrSeSrLn> allSrs = this.orm.retLstCnd(pRvs, vs, CuOrSeSrLn.class,
        "where OWNR in (" + ordIds + ")"); vs.clear();
      for (CuOrSeSrLn il : allSrs) {
        for (CuOrSe co : pSords) {
          if (co.getIid().equals(il.getOwnr().getIid())) {
            co.getServs().add(il);
            break;
          }
        }
      }
    }
  }

  /**
   * <p>Redirect.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  public final void redir(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    String procNm = pRqDt.getParam("prcRed");
    if (getClass().getSimpleName().equals(procNm)) {
      throw new ExcCode(ExcCode.SPAM, "Danger! Stupid scam!!!");
    }
    IPrc proc = this.fcPrWs.laz(pRvs, procNm);
    proc.process(pRvs, pRqDt);
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
   * <p>Getter for orm.</p>
   * @return IRdb<RS>
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
   * <p>Getter for buySr.</p>
   * @return IBuySr
   **/
  public final IBuySr getBuySr() {
    return this.buySr;
  }

  /**
   * <p>Setter for buySr.</p>
   * @param pBuySr reference
   **/
  public final void setBuySr(final IBuySr pBuySr) {
    this.buySr = pBuySr;
  }

  /**
   * <p>Getter for fcPrWs.</p>
   * @return FcPrWs<RS>
   **/
  public final FcPrWs<RS> getFcPrWs() {
    return this.fcPrWs;
  }

  /**
   * <p>Setter for fcPrWs.</p>
   * @param pFcPrWs reference
   **/
  public final void setFcPrWs(final FcPrWs<RS> pFcPrWs) {
    this.fcPrWs = pFcPrWs;
  }
}
