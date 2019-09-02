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

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdl.Page;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.log.ILog;
import org.beigesoft.prc.IPrc;
import org.beigesoft.prc.PrcEntRt;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.srv.ISrvPg;
import org.beigesoft.ws.mdlp.CuOrSe;
import org.beigesoft.ws.mdlp.CuOr;
import org.beigesoft.ws.mdlp.Buyer;
import org.beigesoft.ws.fct.FcPrWs;
import org.beigesoft.ws.srv.IBuySr;

/**
 * <p>Service that retrieve buyer's orders to print.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrBuOr<RS> implements IPrc {

  /**
   * <p>Logger.</p>
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
   * <p>Page service.</p>
   */
  private ISrvPg srvPg;

  /**
   * <p>Buyer service.</p>
   **/
  private IBuySr buySr;

  /**
   * <p>Processors factory.</p>
   **/
  private FcPrWs<RS> fcPrWs;

  /**
   * <p>Base retriever.</p>
   **/
  private PrcEntRt retrv;

  /**
   * <p>Transaction isolation.</p>
   **/
  private Integer trIsl;

  /**
   * <p>Process entity request.</p>
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
        String orIdSt = pRqDt.getParam("orId");
        String sorIdSt = pRqDt.getParam("sorId");
        UvdVar uvs = new UvdVar();
        pRvs.put("uvs", uvs);
        if (orIdSt != null || sorIdSt != null) { //print:
         if (orIdSt != null) { //order
            Long orId = Long.valueOf(orIdSt);
            CuOr or = new CuOr();
            or.setIid(orId);
            this.orm.refrEnt(pRvs, vs, or);
            this.retrv.process(pRvs, or, pRqDt);
          } else { //S.E. order:
            Long orId = Long.valueOf(sorIdSt);
            CuOrSe or = new CuOrSe();
            or.setIid(orId);
            this.orm.refrEnt(pRvs, vs, or);
            this.retrv.process(pRvs, or, pRqDt);
          }
          pRqDt.setAttr("rnd", "prn");
        } else { //page:
          page(pRvs, pRqDt, buyer);
          pRqDt.setAttr("rnd", "wors");
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
    if (buyer == null) {
  this.log.warn(pRvs, getClass(), "Trying to retrieve orders without buyer!");
      redir(pRvs, pRqDt);
    }
  }

  /**
   * <p>Retrieve page.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pBuyr buyer
   * @throws Exception - an exception
   **/
  public final void page(final Map<String, Object> pRvs,
    final IReqDt pRqDt, final Buyer pBuyr) throws Exception {
    //orders:
    int page;
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    String pgSt = pRqDt.getParam("pg");
    if (pgSt != null) {
      page = Integer.parseInt(pgSt);
    } else {
      page = 1;
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    String wheBr = "BUYR=" + pBuyr.getIid();
    Integer rowCount = this.orm.evRowCntWhe(pRvs, vs, CuOr.class, wheBr);
    int totalPages = this.srvPg.evPgCnt(rowCount, cpf.getPgSz());
    if (page > totalPages) {
      page = totalPages;
    }
    int firstResult = (page - 1) * cpf.getPgSz(); //0-20,20-40
    List<Page> pages = this.srvPg.evPgs(page, totalPages,
      cpf.getPgTl());
    pRvs.put("pgs", pages);
    String[] fnm = new String[] {"nme"};
    vs.put("PicPlcndFds", fnm);
    vs.put("BuyerdpLv", 1);
    List<CuOr> orders = getOrm().retPgCnd(pRvs, vs, CuOr.class,
      "where " + wheBr, firstResult, cpf.getPgSz()); vs.clear();
    pRvs.put("ords", orders);
    //S.E. orders:
    pgSt = pRqDt.getParam("spg");
    if (pgSt != null) {
      page = Integer.parseInt(pgSt);
    } else {
      page = 1;
    }
    rowCount = this.orm.evRowCntWhe(pRvs, vs, CuOrSe.class, wheBr);
    totalPages = this.srvPg.evPgCnt(rowCount, cpf.getPgSz());
    if (page > totalPages) {
      page = totalPages;
    }
    firstResult = (page - 1) * cpf.getPgSz(); //0-20,20-40
    pages = this.srvPg.evPgs(1, totalPages, cpf.getPgTl());
    pRvs.put("spgs", pages);
    vs.put("DbCrndFds", fnm);
    vs.put("SeSelndFds", new String[] {"dbcr"});
    vs.put("SeseldpLv", 2);
    vs.put("BuyerdpLv", 0);
    List<CuOrSe> sorders = getOrm().retPgCnd(pRvs, vs, CuOrSe.class,
      "where " + wheBr, firstResult, cpf.getPgSz()); vs.clear();
    pRvs.put("sords", sorders);
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
   * <p>Getter for srvPg.</p>
   * @return ISrvPg
   **/
  public final ISrvPg getSrvPg() {
    return this.srvPg;
  }

  /**
   * <p>Setter for srvPg.</p>
   * @param pSrvPg reference
   **/
  public final void setSrvPg(final ISrvPg pSrvPg) {
    this.srvPg = pSrvPg;
  }

  /**
   * <p>Getter for retrv.</p>
   * @return PrcEntRt
   **/
  public final PrcEntRt getRetrv() {
    return this.retrv;
  }

  /**
   * <p>Setter for retrv.</p>
   * @param pRetrv reference
   **/
  public final void setRetrv(final PrcEntRt pRetrv) {
    this.retrv = pRetrv;
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
