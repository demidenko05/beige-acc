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
import java.util.List;

import org.beigesoft.log.ILog;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.ws.mdlp.SeSel;

/**
 * <p>S.E.Seller finder service. Standard implementation
 * for small number of S.E. Sellers. It requires opened transaction.
 * Methods are synchronized.</p>
 *
 * @author Yury Demidenko
 */
public class FiSeSel implements IFiSeSel {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Cache of all S.E.Sellers.</p>
   **/
  private List<SeSel> sellers;

  /**
   * <p>Finds by name.</p>
   * @param pRvs additional request scoped parameters
   * @param pName seller's
   * @return S.E. Seller or null
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized SeSel find(final Map<String, Object> pRvs,
    final String pName) throws Exception {
    if (this.sellers == null) {
      Map<String, Object> vs = new HashMap<String, Object>();
      this.sellers = this.orm.retLst(pRvs, vs, SeSel.class);
    }
    for (SeSel ses : this.sellers) {
      if (ses.getUsr().getUsr().equals(pName)) {
        return ses;
      }
    }
    return null;
  }

  /**
   * <p>Handle S.E. seller changed.
   * Any change leads to refreshing whole list.</p>
   * @param pRvs additional param
   * @param pName seller's, null means "refresh all"
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void hndSelChg(final Map<String, Object> pRvs,
    final String pName) throws Exception {
    this.sellers = null;
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
    this.sellers = null;
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
