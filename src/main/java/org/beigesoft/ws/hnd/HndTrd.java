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

import java.util.Map;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.log.ILog;
import org.beigesoft.hnd.IHndRq;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.ws.srv.ISrTrStg;

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
   * <p>Trade-settings service.</p>
   **/
  private ISrTrStg srTrStg;

  /**
   * <p>Handle request.</p>
   * @param pRvs Request scoped variables
   * @param pRqDt Request Data
   * @throws Exception - an exception
   */
  @Override
  public final void handle(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    if (this.srTrStg.getTrStg() == null) {
      synchronized (this) {
        if (this.srTrStg.getTrStg() == null) {
          try {
            this.rdb.setAcmt(false);
            this.rdb.setTrIsl(IRdb.TRRUC);
            this.rdb.begin();
            this.srTrStg.lazTrStg(pRvs);
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
        }
      }
    } else { //lazTrStg and saveTrStg will put tstg into pRvs!
      this.srTrStg.lazTrStg(pRvs);
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
   * <p>Getter for srTrStg.</p>
   * @return ISrTrStg
   **/
  public final ISrTrStg getSrTrStg() {
    return this.srTrStg;
  }

  /**
   * <p>Setter for srTrStg.</p>
   * @param pSrTrStg reference
   **/
  public final void setSrTrStg(final ISrTrStg pSrTrStg) {
    this.srTrStg = pSrTrStg;
  }
}
