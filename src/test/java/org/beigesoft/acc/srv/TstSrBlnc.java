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
import java.util.Calendar;
import java.util.Locale;

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
import org.beigesoft.hnd.HndI18nRq;
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
import org.beigesoft.acc.srv.ISrBlnc;

/**
 * <p>Balance service, processors, AccHnd... test.</p>
 *
 * @author Yury Demidenko
 * @param <RS> platform dependent RDBMS recordset
 */
public class TstSrBlnc<RS> {
  //external:
  private IFctAsm<RS> fctApp;

  //inner:
    //vars:
  private Map<String, Object> rvs = new HashMap<String, Object>();
  private Map<String, Object> vs = new HashMap<String, Object>();
  private ReqDtTst rqDt = new ReqDtTst();
  private UvdVar uvs = new UvdVar();
    //services:
  private ILog log;
  private IOrm orm;
  private IRdb<RS> rdb;
  private ISrvDt srvDt;
  private FctEnPrc<RS> fctEnPrc;
  private EntrSrcCr entrSrcCr;
  private InEntrSv inEntrSv;
  private EntrCr entrCr;
  private EntrSv entrSv;
  private HndAcc<RS> hndAcc;
  private HndI18nRq<RS> hndBase;
  private Setng stgOrm;
  private ISrBlnc srBlnc;
  private Calendar cal = Calendar.getInstance(new Locale("en", "US"));
    //Accounts:
  private Acnt acBnk;
  private Acnt acScap;
  private Acnt acExpn;
    //Subaccounts:
  private Expn rent;
  private Bnka bnka;
  private DcrCt buyersa;
  private DbCr buyer1;
  private SrvCt srvsa;

  public void tst1() throws Exception {
    init();
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(IRdb.TRRUC);
      this.rdb.begin();
      mkAccStg("2017-01");
      mkSubacc();
      inEn1Scap10Jan17();
      inEn2Wrong11Jan16();
      //this.rdb.commit();
      this.rdb.rollBack();
    } catch (Exception e) {
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw e;
    } finally {
      this.rdb.release();
    }
  }
  
  public void init() throws Exception {
    this.acBnk = new Acnt();
    this.acBnk.setIid("BANK");
    this.acScap = new Acnt();
    this.acScap.setIid("SCAPITAL");
    this.acExpn = new Acnt();
    this.acExpn.setIid("EXPENSES");
    this.orm = (IOrm) this.fctApp.laz(this.rvs, IOrm.class.getSimpleName());
    this.orm.init(this.rvs);
    this.stgOrm = (Setng) this.fctApp.laz(this.rvs, FctDt.STGORMNM);
    this.stgOrm.release();
    this.rdb = (IRdb<RS>) this.fctApp.laz(this.rvs, IRdb.class.getSimpleName());
    this.log = this.fctApp.getFctBlc().lazLogStd(this.rvs);
    this.srBlnc = (ISrBlnc) this.fctApp.laz(this.rvs, ISrBlnc.class.getSimpleName());
    this.srvDt = (ISrvDt) this.fctApp.laz(this.rvs, ISrvDt.class.getSimpleName());
    this.fctEnPrc = (FctEnPrc<RS>) this.fctApp.laz(this.rvs, FctEnPrc.class.getSimpleName());
    this.entrSrcCr = (EntrSrcCr) this.fctEnPrc.laz(this.rvs, EntrSrcCr.class.getSimpleName());
    this.inEntrSv = (InEntrSv) this.fctEnPrc.laz(this.rvs, InEntrSv.class.getSimpleName());
    this.entrCr = (EntrCr) this.fctEnPrc.laz(this.rvs, EntrCr.class.getSimpleName());
    this.entrSv = (EntrSv) this.fctEnPrc.laz(this.rvs, EntrSv.class.getSimpleName());
    this.hndAcc = (HndAcc<RS>) this.fctApp.laz(this.rvs, HndAcc.class.getSimpleName());
    this.hndBase = (HndI18nRq<RS>) this.fctApp.laz(this.rvs, HndI18nRq.class.getSimpleName());
    this.hndBase.handle(this.rvs, this.rqDt);
    this.hndAcc.handle(this.rvs, this.rqDt);
  }

  public void mkSubacc() throws Exception {
    this.rent = new Expn();
    this.rent.setIid(1L);
    this.rent.setNme("Rent");
    this.orm.insIdLn(this.rvs, this.vs, this.rent); this.vs.clear();
    this.bnka = new Bnka();
    this.bnka.setIid(1L);
    this.bnka.setNme("#18768762 in BNK");
    this.orm.insIdLn(this.rvs, this.vs, this.bnka); this.vs.clear();
    this.buyersa = new DcrCt();
    this.buyersa.setIid(1L);
    this.buyersa.setNme("Buyers A");
    this.orm.insIdLn(this.rvs, this.vs, this.buyersa); this.vs.clear();
    this.buyer1 = new DbCr();
    this.buyer1.setIid(1L);
    this.buyer1.setCat(this.buyersa);
    this.buyer1.setNme("Buyer 1");
    this.orm.insIdLn(this.rvs, this.vs, this.buyer1); this.vs.clear();
    this.srvsa = new SrvCt();
    this.srvsa.setIid(1L);
    this.srvsa.setNme("Cleanings A");
    this.orm.insIdLn(this.rvs, this.vs, this.srvsa); this.vs.clear();
  }

  // #1 - started capital 0n 10jan17, blnc on 21jan17=0r, blnc on 21feb17=2r
  public void inEn1Scap10Jan17() throws Exception {
    iniNewReq();
    cal.setTime(this.hndAcc.getSrAcStg().lazAcStg(this.rvs).getMnth());
    cal.set(Calendar.DAY_OF_MONTH, 10);
    this.rqDt.getParamsMp().put("opDt", this.srvDt.to8601Date(cal.getTime()));
    this.hndAcc.handle(this.rvs, this.rqDt);
    InEntr inScap = new InEntr();
    inScap.setDbOr(this.orm.getDbId());
    inScap.setIsNew(true);
    inScap = (InEntr) this.entrSrcCr.process(this.rvs, inScap, this.rqDt);
    iniNewReq(); inScap = this.inEntrSv.process(this.rvs, inScap, this.rqDt);
    iniNewReq(); inScap.setDscr("started capital");
    inScap = this.inEntrSv.process(this.rvs, inScap, this.rqDt);
    iniNewReq(); Entr enScap = new Entr();
    enScap.setDbOr(this.orm.getDbId());
    enScap.setIsNew(true);
    enScap.setSrId(inScap.getIid());
    enScap = this.entrCr.process(this.rvs, enScap, this.rqDt);
    iniNewReq(); enScap.setDebt(new BigDecimal("40000"));
    enScap.setAcDb(this.acBnk);
    enScap.setSadId(this.bnka.getIid());
    enScap.setSadNm(this.bnka.getNme());
    enScap.setSadTy(this.bnka.cnsTy());
    enScap.setAcCr(this.acScap);
    this.rqDt.getParamsMp().put("owVr", inScap.getVer().toString());
    assertNotNull(this.rvs.get("astg"));
    enScap = this.entrSv.process(this.rvs, enScap, this.rqDt);
    cal.set(Calendar.DAY_OF_MONTH, 21);
    this.srBlnc.recalcIfNd(this.rvs, cal.getTime());
    Integer trows = this.rdb.evInt("select count(*) as TROWS from BLNC;","TROWS");
    assertEquals(Integer.valueOf(0), trows);
    cal.set(Calendar.MONTH, 1); //jan=0
    this.srBlnc.recalcIfNd(this.rvs, cal.getTime());
    trows = this.rdb.evInt("select count(*) as TROWS from BLNC;","TROWS");
    assertEquals(Integer.valueOf(2), trows);
  }

  // #2 - wrong Bnk-Exp.Rent 11jan16, blnc on 21jan17=26r, blnc on 21feb17=27r
  //reverse wrong Bnk-Exp.Rent 11jan16, blnc on 21jan17=27r, blnc on 21feb17=27r
  public void inEn2Wrong11Jan16() throws Exception {
    iniNewReq();
    mkAccStg("2016-01"); //wrong year
    cal.setTime(this.hndAcc.getSrAcStg().lazAcStg(this.rvs).getMnth());
    cal.set(Calendar.DAY_OF_MONTH, 11);
    this.rqDt.getParamsMp().put("opDt", this.srvDt.to8601Date(cal.getTime()));
    this.hndAcc.handle(this.rvs, this.rqDt);
    InEntr inPayRent = new InEntr();
    inPayRent.setDbOr(this.orm.getDbId());
    inPayRent.setIsNew(true);
    inPayRent.setDscr("payed rent");
    inPayRent = (InEntr) this.entrSrcCr.process(this.rvs, inPayRent, this.rqDt);
    iniNewReq(); inPayRent = this.inEntrSv.process(this.rvs, inPayRent, this.rqDt);
    iniNewReq(); Entr enPayRent = new Entr();
    enPayRent.setDbOr(this.orm.getDbId());
    enPayRent.setIsNew(true);
    enPayRent.setSrId(inPayRent.getIid());
    enPayRent = this.entrCr.process(this.rvs, enPayRent, this.rqDt);
    iniNewReq(); enPayRent.setDebt(new BigDecimal("1000"));
    enPayRent.setAcDb(this.acExpn);
    enPayRent.setSadId(this.rent.getIid());
    enPayRent.setSadNm(this.rent.getNme());
    enPayRent.setSadTy(this.rent.cnsTy());
    enPayRent.setAcCr(this.acBnk);
    enPayRent.setSacId(this.bnka.getIid());
    enPayRent.setSacNm(this.bnka.getNme());
    enPayRent.setSacTy(this.bnka.cnsTy());
    this.rqDt.getParamsMp().put("owVr", inPayRent.getVer().toString());
    this.entrSv.process(this.rvs, enPayRent, this.rqDt);
    this.srBlnc.recalcIfNd(this.rvs, this.srvDt.from8601Date("2017-01-21"));
    Integer trows = this.rdb.evInt("select count(*) as TROWS from BLNC;","TROWS");
    assertEquals(Integer.valueOf(26), trows);
    this.srBlnc.recalcIfNd(this.rvs, this.srvDt.from8601Date("2017-02-21"));
    trows = this.rdb.evInt("select count(*) as TROWS from BLNC;","TROWS");
    assertEquals(Integer.valueOf(27), trows); //bank/scap refreshed expn.rent added
    //reversing
    iniNewReq(); Entr enPrRev = new Entr();
    enPrRev.setDbOr(this.orm.getDbId());
    enPrRev.setIsNew(true);
    enPrRev.setSrId(inPayRent.getIid());
    enPrRev.setRvId(enPayRent.getIid());
    enPrRev = this.entrCr.process(this.rvs, enPrRev, this.rqDt);
    this.orm.refrEnt(this.rvs, this.vs, inPayRent);
    iniNewReq(); this.rqDt.getParamsMp().put("owVr", inPayRent.getVer().toString());
    this.entrSv.process(this.rvs, enPrRev, this.rqDt);
    this.srBlnc.recalcIfNd(this.rvs, this.srvDt.from8601Date("2017-01-21"));
    trows = this.rdb.evInt("select count(*) as TROWS from BLNC;","TROWS");
    assertEquals(Integer.valueOf(27), trows);
    this.srBlnc.recalcIfNd(this.rvs, this.srvDt.from8601Date("2017-02-21"));
    trows = this.rdb.evInt("select count(*) as TROWS from BLNC;","TROWS");
    assertEquals(Integer.valueOf(27), trows);
    mkAccStg("2017-01"); //right year
  }

  public void mkAccStg(final String pMnthStr) throws Exception {
    AcStg astgt = this.hndAcc.getSrAcStg().lazAcStg(this.rvs);
    astgt.setMnth(this.srvDt.fromYearMonth(pMnthStr));
    this.hndAcc.getSrAcStg().saveAcStg(this.rvs, astgt);
    assertEquals(astgt, this.hndAcc.getSrAcStg().lazAcStg(this.rvs));
  }

  public void iniNewReq() throws Exception {
    this.uvs = new UvdVar();
    this.rvs.clear();
    this.vs.clear();
    this.rqDt.iniNewRq();
    this.rvs.put("uvs", this.uvs);
    this.hndBase.handle(this.rvs, this.rqDt);
    this.hndAcc.handle(this.rvs, this.rqDt);
    assertNotNull(this.rvs.get("astg"));
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
