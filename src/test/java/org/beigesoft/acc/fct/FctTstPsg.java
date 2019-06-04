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

package org.beigesoft.acc.fct;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.sql.ResultSet;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.fct.IFctPrcEnt;
import org.beigesoft.fct.IFctAsm;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.hld.IAttrs;
import org.beigesoft.log.ILog;
import org.beigesoft.log.LogFile;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.SrvClVl;
import org.beigesoft.prp.ISetng;
import org.beigesoft.jdbc.Rdb;
import org.beigesoft.jdbc.FctPostgr;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.acc.fct.FctAcc;
import org.beigesoft.acc.fct.FctEnPrc;

/**
 * <p>Tests final configuration factory for Postgres JDBC.</p>
 *
 * @author Yury Demidenko
 */
public class FctTstPsg implements IFctAsm<ResultSet> {


  /**
   * <p>Main only factory.</p>
   **/
  private FctBlc<ResultSet> fctBlc;

  /**
   * <p>Only constructor.</p>
   * @throws Exception - an exception
   */
  public FctTstPsg() throws Exception {
    Map<String, Object> rqVs = new HashMap<String, Object>();
    this.fctBlc = new FctBlc<ResultSet>();
    String tstDir = System.getProperty("user.dir") + File.separator
        + "target";
    this.fctBlc.getFctDt().setLngCntr("en,US,ru,RU");
    this.fctBlc.getFctDt().setLogPth(tstDir);
    this.fctBlc.getFctDt().setDbgSh(false);
    this.fctBlc.getFctDt().setDbgFl(8002);
    this.fctBlc.getFctDt().setDbgCl(8002);
    this.fctBlc.getFctsAux().add(new FctPostgr());
    this.fctBlc.getFctsAux().add(new FctAcc<ResultSet>());
    Set<IFctPrcEnt> fctsPrcEnt = new HashSet<IFctPrcEnt>();
    FctEnPrc<ResultSet> fcep = new FctEnPrc<ResultSet>();
    fcep.setFctBlc(this.fctBlc);
    fctsPrcEnt.add(fcep);
    this.fctBlc.getFctDt().setFctsPrcEnt(fctsPrcEnt);
  }

  /**
   * <p>Get bean in lazy mode (if bean is null then initialize it).</p>
   * @param pRqVs request scoped vars
   * @param pBnNm - bean name
   * @return Object - requested bean or exception if not found
   * @throws Exception - an exception
   */
  @Override
  public final Object laz(final Map<String, Object> pRqVs,
    final String pBnNm) throws Exception {
    return this.fctBlc.laz(pRqVs, pBnNm);
  }

  /**
   * <p>Releases memory.</p>
   * @param pRqVs request scoped vars
   * @throws Exception - an exception
   */
  @Override
  public final void release(final Map<String, Object> pRqVs) throws Exception {
    this.fctBlc.release(pRqVs);
  }


  /**
   * <p>Puts beans by external AUX factory.</p>
   * @param pRqVs request scoped vars
   * @param pBnNm - bean name
   * @param pBean - bean
   * @throws Exception - an exception, e.g. if bean exists
   **/
  @Override
  public final void put(final Map<String, Object> pRqVs,
    final String pBnNm, final Object pBean) throws Exception {
    this.fctBlc.put(pRqVs, pBnNm, pBean);
  }

  /**
   * <p>Gets main factory for setting configuration parameters.</p>
   * @return Object - requested bean
   */
  @Override
  public final FctBlc<ResultSet> getFctBlc() {
    return this.fctBlc;
  }

  /**
   * <p>Initializes factory.</p>
   * @param pRvs request scoped vars
   * @param pCtxAttrs context attributes
   * @throws Exception - an exception, e.g. if bean exists
   */
  @Override
  public final void init(final Map<String, Object> pRvs,
    final IAttrs pCtxAttrs) throws Exception {
    //nothing;
  }
}
