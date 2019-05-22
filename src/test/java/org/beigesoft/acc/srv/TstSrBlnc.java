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

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.ColVals;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.ReqDtTst;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.log.ILog;
import org.beigesoft.fct.IFctAsm;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.fct.FctDt;
import org.beigesoft.fct.FctEnPrc;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.prp.Setng;
import org.beigesoft.prp.ISetng;
import org.beigesoft.cnv.FilCvEnt;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.srv.ISrvDt;
import org.beigesoft.acc.mdlp.Acnt;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Expn;
import org.beigesoft.acc.mdlp.Bnka;
import org.beigesoft.acc.mdlp.DbCr;
import org.beigesoft.acc.mdlp.DcrCt;
import org.beigesoft.acc.mdlp.Srv;
import org.beigesoft.acc.mdlp.SrvCt;
import org.beigesoft.acc.mdlp.ItmCt;
import org.beigesoft.acc.mdlp.Itm;
import org.beigesoft.acc.mdlp.Entr;
import org.beigesoft.acc.mdlp.InEntr;
import org.beigesoft.acc.prc.InEntrSv;
import org.beigesoft.acc.prc.EntrSv;
import org.beigesoft.acc.prc.EntrCr;
import org.beigesoft.acc.prc.EntrSrcCr;
import org.beigesoft.acc.hnd.HndAcc;

/**
 * <p>Balance service, processors, AccHnd... test.</p>
 *
 * @author Yury Demidenko
 * @param <RS> platform dependent RDBMS recordset
 */
public class TstSrBlnc<RS> {

  private IFctAsm<RS> fctApp;

  public void tst1() throws Exception {
    Map<String, Object> rvs = new HashMap<String, Object>();
    Map<String, Object> vs = new HashMap<String, Object>();
    IOrm orm = (IOrm) this.fctApp.laz(rvs, IOrm.class.getSimpleName());
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctApp.laz(rvs, IRdb.class.getSimpleName());
    orm.init(rvs);
    Setng stgOrm = (Setng) this.fctApp.laz(rvs, FctDt.STGORMNM);
    stgOrm.release();
    ISrvDt srvDt = (ISrvDt) this.fctApp.laz(rvs, ISrvDt.class.getSimpleName());
    @SuppressWarnings("unchecked")
    FctEnPrc<RS> fctEnPrc = (FctEnPrc<RS>) this.fctApp.laz(rvs, FctEnPrc.class.getSimpleName());
    EntrSrcCr entrSrcCr = (EntrSrcCr) fctEnPrc.laz(rvs, EntrSrcCr.class.getSimpleName());
    InEntrSv inEntrSv = (InEntrSv) fctEnPrc.laz(rvs, InEntrSv.class.getSimpleName());
    @SuppressWarnings("unchecked")
    HndAcc<RS> hndAcc = (HndAcc<RS>) this.fctApp.laz(rvs, HndAcc.class.getSimpleName());
    ReqDtTst rqDt = new ReqDtTst();
    Date now = new Date();
    rqDt.getParamsMp().put("opDt", srvDt.to8601Date(now));
    hndAcc.handle(rvs, rqDt);
    Acnt acBnk = new Acnt();
    acBnk.setIid("BANK");
    Acnt acScap = new Acnt();
    acScap.setIid("SCAPITAL");
    try {
      rdb.setAcmt(false);
      rdb.setTrIsl(IRdb.TRRUC);
      rdb.begin();
      AcStg astgt = hndAcc.getSrAcStg().lazAcStg(rvs);
      astgt.setMnth(now);
      hndAcc.getSrAcStg().saveAcStg(rvs, astgt);
      Expn rent = new Expn();
      rent.setIid(1L);
      rent.setNme("Rent");
      orm.insIdLn(rvs, vs, rent);
      Bnka bnka = new Bnka();
      bnka.setIid(1L);
      bnka.setNme("#18768762 in BNK");
      orm.insIdLn(rvs, vs, bnka);
      DcrCt buyersa = new DcrCt();
      buyersa.setIid(1L);
      buyersa.setNme("Buyers A");
      orm.insIdLn(rvs, vs, buyersa);
      DbCr buyer1 = new DbCr();
      buyer1.setIid(1L);
      buyer1.setCat(buyersa);
      buyer1.setNme("Buyer 1");
      orm.insIdLn(rvs, vs, buyer1);
      SrvCt srvsa = new SrvCt();
      srvsa.setIid(1L);
      srvsa.setNme("Cleanings A");
      orm.insIdLn(rvs, vs, srvsa);
      InEntr inScap = new InEntr();
      inScap.setDbOr(orm.getDbId());
      inScap.setIsNew(true);
      rqDt = new ReqDtTst();
      UvdVar uvs = new UvdVar();
      rvs.put("uvs", uvs);
      inScap = (InEntr) entrSrcCr.process(rvs, inScap, rqDt);
      inScap = inEntrSv.process(rvs, inScap, rqDt);
      inScap.setDscr("started capital");
      inScap = inEntrSv.process(rvs, inScap, rqDt);
      rdb.commit();
      //rdb.rollBack();
    } catch (Exception e) {
      if (!rdb.getAcmt()) {
        rdb.rollBack();
      }
      throw e;
    } finally {
      rdb.release();
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctApp.</p>
   * @return FctBlc<RS>
   **/
  public final IFctAsm<RS> getFctApp() {
    return this.fctApp;
  }

  /**
   * <p>Setter for fctApp.</p>
   * @param pFctApp reference
   **/
  public final void setFctApp(final IFctAsm<RS> pFctApp) {
    this.fctApp = pFctApp;
  }
}
