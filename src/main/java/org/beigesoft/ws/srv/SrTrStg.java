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

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.log.ILog;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.ws.mdlp.TrdStg;

/**
 * <p>Trade settings service.</p>
 *
 * @author Yury Demidenko
 */
public class SrTrStg implements ISrTrStg {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Cached settings.</p>
   **/
  private TrdStg trStg;

  /**
   * <p>Retrieves/gets Trade settings.</p>
   * @param pRvs Request scoped variables
   * @return Trade settings
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized TrdStg lazTrStg(
    final Map<String, Object> pRvs) throws Exception {
    if (this.trStg == null) {
      Map<String, Object> vs = new HashMap<String, Object>();
      TrdStg tstg = new TrdStg();
      tstg.setIid(1L);
      this.orm.refrEnt(pRvs, vs, tstg);
      if (tstg.getIid() == null) {
        tstg.setIid(1L);
        this.orm.insIdLn(pRvs, vs, tstg);
      }
      this.trStg = tstg;
      pRvs.put("tstg", this.trStg);
    } else if (pRvs.get("tstg") == null) {
      pRvs.put("tstg", this.trStg);
    }
    return this.trStg;
  }

  /**
   * <p>Saves trade-settings into DB.</p>
   * @param pRvs Request scoped variables
   * @param pTrStg entity
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void saveTrStg(final Map<String, Object> pRvs,
    final TrdStg pTrStg) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    this.orm.update(pRvs, vs, pTrStg);
    this.orm.refrEnt(pRvs, vs, pTrStg);
    this.trStg = pTrStg;
    pRvs.put("tstg", this.trStg);
  }

  /**
   * <p>Getter for trdStg for avoiding starting new transaction.</p>
   * @return TrdStg
   **/
  @Override
  public final synchronized TrdStg getTrStg() {
    return this.trStg;
  }

  /**
   * <p>Handle rollback, e.g. clears cached data.</p>
   * @param pRvs Request scoped variables
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void hndRlBk(
    final Map<String, Object> pRvs) throws Exception {
    getLog().warn(pRvs, getClass(), "Clear cache cause transaction rollback!");
    this.trStg = null;
    pRvs.remove("tstg");
  }

  //Simple getters and setters:
  /**
   * <p>Getter for log.</p>
   * @return ILog
   **/
  public final synchronized ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final synchronized void setLog(final ILog pLog) {
    this.log = pLog;
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
}
