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

package org.beigesoft.acc.hnd;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.log.ILog;
import org.beigesoft.hnd.IHndRq;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.srv.ISrvDt;
import org.beigesoft.acc.mdl.AcUpf;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.I18Curr;
import org.beigesoft.acc.mdlp.I18Acc;
import org.beigesoft.acc.hld.HlTySac;
import org.beigesoft.acc.srv.ISrAcStg;

/**
 * <p>First handler of any accounting request.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class HndAcc<RS> implements IHndRq {

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
   */
  private IOrm orm;

  /**
   * <p>Subaccounts type-class holder.</p>
   **/
  private HlTySac hlTySac;

  /**
   * <p>Acc-ssttings service.</p>
   **/
  private ISrAcStg srAcStg;

  /**
   * <p>Date service.</p>
   **/
  private ISrvDt srvDt;

  //Cached data:
  /**
   * <p>Cached I18N currency.</p>
   */
  private List<I18Curr> i18Currs;

  /**
   * <p>Cached I18N base info.</p>
   */
  private List<I18Acc> i18Accs;

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
    List<I18Acc> i18AccsTmp = null;
    List<I18Curr> i18CurrsTmp = null;
    boolean tmpReady = false;
    if (this.srAcStg.getAcStg() == null) {
      synchronized (this) {
        if (this.srAcStg.getAcStg() == null) {
          try {
            this.rdb.setAcmt(false);
            this.rdb.setTrIsl(IRdb.TRRUC);
            this.rdb.begin();
            this.srAcStg.lazAcStg(pRvs);
            if (this.i18Currs == null) {
              i18AccsTmp = this.orm.retLst(pRvs, vs, I18Acc.class);
              i18CurrsTmp = this.orm.retLst(pRvs, vs, I18Curr.class);
              tmpReady = true;
              this.i18Currs = i18CurrsTmp;
              this.i18Accs = i18AccsTmp;
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
        }
      }
    } else { //lazAcStg and saveAcStg will put astg into pRvs!
      this.srAcStg.lazAcStg(pRvs);
    }
    if (!tmpReady) {
      if (this.i18Currs == null) {
        synchronized (this) {
          if (this.i18Currs == null) {
            try {
              this.rdb.setAcmt(false);
              this.rdb.setTrIsl(IRdb.TRRUC);
              this.rdb.begin();
              i18AccsTmp = this.orm.retLst(pRvs, vs, I18Acc.class);
              i18CurrsTmp = this.orm.retLst(pRvs, vs, I18Curr.class);
              tmpReady = true;
              this.i18Currs = i18CurrsTmp;
              this.i18Accs = i18AccsTmp;
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
        }
      }
      if (!tmpReady) {
        i18CurrsTmp = this.i18Currs;
        i18AccsTmp = this.i18Accs;
      }
    }
    AcUpf aupf = new AcUpf();
    String opDtStr = pRqDt.getParam("opDt");
    boolean ndStCo = false;
    if (opDtStr != null) {
      aupf.setOpDt(this.srvDt.from8601Date(opDtStr));
      ndStCo = true;
    } else {
      opDtStr = pRqDt.getCookVl("opDt");
      if (opDtStr != null) {
        aupf.setOpDt(new Date(Long.parseLong(opDtStr)));
      }
    }
    if (aupf.getOpDt() == null) {
      aupf.setOpDt(new Date());
      ndStCo = true;
    }
    if (ndStCo) {
      pRqDt
        .setCookVl("opDt", Long.valueOf(aupf.getOpDt().getTime()).toString());
    }
    UsPrf upf = (UsPrf) pRvs.get("upf");
    for (I18Curr ic : i18CurrsTmp) {
      if (upf.getLng().getIid().equals(ic.getLng().getIid())) {
        pRvs.put("i18Curr", ic);
        break;
      }
    }
    for (I18Acc ia : i18AccsTmp) {
      if (upf.getLng().getIid().equals(ia.getLng().getIid())) {
        pRvs.put("i18Acc", ia);
        break;
      }
    }
    pRvs.put("aupf", aupf);
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    AcStg as = this.srAcStg.lazAcStg(pRvs);
    cpf.setCostDp(as.getCsDp());
    cpf.setPriDp(as.getPrDp());
    cpf.setQuanDp(as.getQuDp());
    pRqDt.setAttr("hlTySac", this.hlTySac);
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
   * <p>Getter for orm.</p>
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
   * <p>Getter for hlTySac.</p>
   * @return HlTySac
   **/
  public final HlTySac getHlTySac() {
    return this.hlTySac;
  }

  /**
   * <p>Setter for hlTySac.</p>
   * @param pHlTySac reference
   **/
  public final void setHlTySac(final HlTySac pHlTySac) {
    this.hlTySac = pHlTySac;
  }

  /**
   * <p>Getter for srAcStg.</p>
   * @return ISrAcStg
   **/
  public final ISrAcStg getSrAcStg() {
    return this.srAcStg;
  }

  /**
   * <p>Setter for srAcStg.</p>
   * @param pSrAcStg reference
   **/
  public final void setSrAcStg(final ISrAcStg pSrAcStg) {
    this.srAcStg = pSrAcStg;
  }

  /**
   * <p>Getter for srvDt.</p>
   * @return ISrvDt
   **/
  public final ISrvDt getSrvDt() {
    return this.srvDt;
  }

  /**
   * <p>Setter for srvDt.</p>
   * @param pSrvDt reference
   **/
  public final void setSrvDt(final ISrvDt pSrvDt) {
    this.srvDt = pSrvDt;
  }
}
