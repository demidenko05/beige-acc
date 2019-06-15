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

package org.beigesoft.acc.srv;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.ResultSet;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.beigesoft.log.ILog;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.acc.fct.FctTstPsg;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rdb.IOrm;

/**
 * <p>Postgres tests.</p>
 *
 * @author Yury Demidenko
 */
public class PsgTest {

  private FctTstPsg fctApp;
 
  private Map<String, Object> rqVs = new HashMap<String, Object>();

  public PsgTest() throws Exception {
    this.fctApp = new FctTstPsg();
    this.fctApp.getFctBlc().getFctDt().setLogStdNm(PsgTest.class.getSimpleName());
    this.fctApp.getFctBlc().lazLogStd(rqVs).test(rqVs, getClass(), "Starting...");
    this.fctApp.getFctBlc().getFctDt().setStgOrmDir("postgres");
    ISetng setng = this.fctApp.getFctBlc().lazStgOrm(rqVs);
    this.fctApp.getFctBlc().getFctDt().setDbCls(setng.lazCmnst().get(IOrm.DSCLS));
    this.fctApp.getFctBlc().getFctDt().setDbUsr(setng.lazCmnst().get(IOrm.DBUSR));
    this.fctApp.getFctBlc().getFctDt().setDbPwd(setng.lazCmnst().get(IOrm.DBPSW));
    this.fctApp.getFctBlc().getFctDt().setDbUrl(setng.lazCmnst().get(IOrm.DBURL));
  }

  //@Test
  public void tst1() throws Exception {
    TstSrBlnc tst1 = new TstSrBlnc();
    tst1.setFctApp(this.fctApp);
    tst1.tst1();
    this.fctApp.release(this.rqVs);
  }
}
