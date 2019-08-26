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

package org.beigesoft.ws.hnd;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.log.ILog;
import org.beigesoft.hnd.IHndRq;
import org.beigesoft.prc.IPrc;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Curr;
import org.beigesoft.ws.mdlp.Deliv;
import org.beigesoft.ws.mdlp.I18Trd;
import org.beigesoft.ws.mdlp.I18CatGs;
import org.beigesoft.ws.mdlp.CurrRt;
import org.beigesoft.ws.fct.FcPrWs;
import org.beigesoft.ws.srv.ISrTrStg;
import org.beigesoft.ws.srv.ISrAdStg;

/**
 * <p>Handler of any web-store request.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class HndTrd<RS> implements IHndRq {

  /**
   * <p>Logger.</p>
   **/
  private ILog log;

  /**
   * <p>Database service.</p>
   */
  private IRdb<RS> rdb;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Trade additional settings service.</p>
   **/
  private ISrAdStg srAdStg;

  /**
   * <p>Trade settings service.</p>
   **/
  private ISrTrStg srTrStg;

  /**
   * <p>Web-store processors factory.</p>
   **/
  private FcPrWs<RS> fcPrWs;

  //Cached data:
  /**
   * <p>Cached common trading I18N parameters.</p>
   */
  private List<I18Trd> i18TrdLs;

  /**
   * <p>Cached I18N catalogs.</p>
   */
  private List<I18CatGs> i18CatGsLs;

  /**
   * <p>Cached foreign currency rates.</p>
   */
  private List<CurrRt> currRtLs;

  /**
   * <p>Delivering methods.</p>
   */
  private List<Deliv> dlvMts;

  /**
   * <p>Handle request.</p>
   * @param pRvs Request scoped variables
   * @param pRqDt Request Data
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    List<I18Trd> i18TrdLst = null;
    List<I18CatGs> i18CatGsLst = null;
    List<CurrRt> currRtLst = null;
    List<Deliv> dlvMtst = null;
    synchronized (this) {
      if (this.srTrStg.getTrStg() == null || this.srAdStg.getAdStg() == null
        || this.i18TrdLs == null) {
        try {
          this.rdb.setAcmt(false);
          this.rdb.setTrIsl(IRdb.TRRUC);
          this.rdb.begin();
          this.srTrStg.lazTrStg(pRvs);
          this.srAdStg.lazAdStg(pRvs);
          if (this.i18TrdLs == null) {
            i18TrdLst = this.orm.retLst(pRvs, vs, I18Trd.class);
            i18CatGsLst = this.orm.retLst(pRvs, vs, I18CatGs.class);
            currRtLst = this.orm.retLst(pRvs, vs, CurrRt.class);
            dlvMtst = this.orm.retLst(pRvs, vs, Deliv.class);
            this.i18TrdLs = i18TrdLst;
            this.i18CatGsLs = i18CatGsLst;
            this.currRtLs = currRtLst;
            this.dlvMts = dlvMtst;
          }
          this.rdb.commit();
        } catch (Exception ex) {
          this.srTrStg.hndRlBk(pRvs);
          if (!this.rdb.getAcmt()) {
            this.rdb.rollBack();
          }
          throw ex;
        } finally {
          this.rdb.release();
        }
      } else { //lazTrStg and saveTrStg will put tstg into pRvs!
        this.srTrStg.lazTrStg(pRvs); // rvs.tstg
        this.srAdStg.lazAdStg(pRvs); // rvs.tastg
        i18TrdLst = this.i18TrdLs;
        i18CatGsLst = this.i18CatGsLs;
        currRtLst = this.currRtLs;
        dlvMtst = this.dlvMts;
      }
    }
    pRvs.put("i18Trds", i18TrdLst);
    pRvs.put("i18Cats", i18CatGsLst);
    pRvs.put("currRts", currRtLst);
    pRvs.put("dlvMts", dlvMtst);
    Curr wscurr = null;
    if (currRtLst.size() > 0) {
      String wscurrs = pRqDt.getParam("wscurr");
      if (wscurrs != null) {
        Long wscurrl = Long.parseLong(wscurrs);
        for (CurrRt cr : currRtLst) {
          if (cr.getCurr().getIid().equals(wscurrl)) {
            wscurr = cr.getCurr();
            pRqDt.setCookVl("wscurr", wscurr.getIid().toString());
            break;
          }
        }
      } else {
        String  wscurrsc = pRqDt.getCookVl("wscurr");
        if (wscurrsc != null) {
          Long wscurrl = Long.parseLong(wscurrsc);
          for (CurrRt cr : currRtLst) {
            if (cr.getCurr().getIid().equals(wscurrl)) {
              wscurr = cr.getCurr();
              break;
            }
          }
        }
      }
    }
    if (wscurr == null) {
      AcStg as = (AcStg) pRvs.get("astg");
      wscurr = as.getCurr();
      pRqDt.setCookVl("wscurr", wscurr.getIid().toString());
    }
    pRvs.put("wscurr", wscurr);
    Boolean shTxDet;
    String shTxDets = pRqDt.getParam("shTxDet");
    if (shTxDets == null) {
      String shTxDetsc = pRqDt.getCookVl("shTxDet");
      if (shTxDetsc == null) {
        shTxDet = Boolean.FALSE;
        pRqDt.setCookVl("shTxDet", shTxDet.toString());
      } else {
        shTxDet = Boolean.valueOf(shTxDetsc);
      }
    } else {
      shTxDet = Boolean.valueOf(shTxDets);
      pRqDt.setCookVl("shTxDet", shTxDet.toString());
    }
    pRvs.put("shTxDet", shTxDet);
    String prcNm = pRqDt.getParam("prc");
    if (prcNm != null) {
      IPrc prc = this.fcPrWs.laz(pRvs, prcNm);
      prc.process(pRvs,  pRqDt);
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for rdb.</p>
   * @return IRdb<RS>
   **/
  public final synchronized IRdb<RS> getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final synchronized void setRdb(final IRdb<RS> pRdb) {
    this.rdb = pRdb;
  }

  /**
   * <p>Geter for orm.</p>
   * @return IOrm
   **/
  public final synchronized IOrm getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final synchronized void setOrm(final IOrm pOrm) {
    this.orm = pOrm;
  }

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
   * <p>Getter for srAdStg.</p>
   * @return ISrAdStg
   **/
  public final synchronized ISrAdStg getSrAdStg() {
    return this.srAdStg;
  }

  /**
   * <p>Setter for srAdStg.</p>
   * @param pSrAdStg reference
   **/
  public final synchronized void setSrAdStg(final ISrAdStg pSrAdStg) {
    this.srAdStg = pSrAdStg;
  }

  /**
   * <p>Getter for srTrStg.</p>
   * @return ISrTrStg
   **/
  public final synchronized ISrTrStg getSrTrStg() {
    return this.srTrStg;
  }

  /**
   * <p>Setter for srTrStg.</p>
   * @param pSrTrStg reference
   **/
  public final synchronized void setSrTrStg(final ISrTrStg pSrTrStg) {
    this.srTrStg = pSrTrStg;
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
