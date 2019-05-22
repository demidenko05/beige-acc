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

import java.util.Map;
import java.util.Date;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.log.ILog;
import org.beigesoft.hnd.IHndRq;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.srv.ISrvDt;
import org.beigesoft.acc.mdl.AcUpf;
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
   * <p>Acc-ssttings service.</p>
   **/
  private ISrAcStg srAcStg;

  /**
   * <p>Date service.</p>
   **/
  private ISrvDt srvDt;

  /**
   * <p>Handle request.</p>
   * @param pRvs Request scoped variables
   * @param pRqDt Request Data
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    if (this.srAcStg.getAcStg() == null) {
      //service puts astg into rvs by itself
      try {
        this.rdb.setAcmt(false);
        this.rdb.setTrIsl(IRdb.TRRUC);
        this.rdb.begin();
        this.srAcStg.lazAcStg(pRvs);
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
    pRvs.put("aupf", aupf);
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
