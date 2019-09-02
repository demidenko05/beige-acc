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

package org.beigesoft.ws;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;

import org.beigesoft.log.ILog;
import org.beigesoft.fct.FctDt;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.acc.fct.FctTstSqlt;
import org.beigesoft.prp.Setng;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;

import org.beigesoft.mdlp.Lng;
import org.beigesoft.acc.mdlp.Itm;
import org.beigesoft.acc.mdlp.I18Itm;
import org.beigesoft.acc.mdlp.ItmCt;
import org.beigesoft.acc.mdlp.Uom;
import org.beigesoft.ws.mdl.EItmSpTy;
import org.beigesoft.ws.mdlp.CatGs;
import org.beigesoft.ws.mdlp.ItmCtl;
import org.beigesoft.ws.mdlp.PicPlc;
import org.beigesoft.ws.mdlp.ItmPlc;
import org.beigesoft.ws.mdlp.PriItm;
import org.beigesoft.ws.mdlp.PriCt;
import org.beigesoft.ws.mdlp.ItmSp;
import org.beigesoft.ws.mdlp.I18ItmSp;
import org.beigesoft.ws.mdlp.ItmSpGr;
import org.beigesoft.ws.mdlp.ItmSpf;

/**
 * <p>Populating SQLite database with sample data - pizza with bacon hot#, pizza with cheese hot#, Ford #, Honda#.
 * Database must has tax category, item category, catalogs as in sample database bobs-pizza-ws2!
 * Usage with Maven example, fill database from /beige-orm/sqlite/app-settings.xml (bobs-pizza-ws2.sqlite) with 1000 sample records for each good:
 * <pre>
 * mvn exec:java -Dexec.mainClass="org.beigesoft.webstore.FillDb" -Dexec.args="100" -Dexec.classpathScope=test
 * </pre>
 * </p>
 *
 * @author Yury Demidenko
 */
public class FillDb {
  

  private FctTstSqlt fctApp;
 
  private Map<String, Object> rvs = new HashMap<String, Object>();

  public FillDb() throws Exception {
    this.fctApp = new FctTstSqlt();
    this.fctApp.getFctBlc().getFctDt().setLogStdNm(FillDb.class.getSimpleName());
    this.fctApp.getFctBlc().lazLogStd(this.rvs).test(this.rvs, getClass(), "Starting...");
    this.fctApp.getFctBlc().getFctDt().setStgOrmDir("sqlite");
    ISetng setng = this.fctApp.getFctBlc().lazStgOrm(this.rvs);
    String dbUrl = setng.lazCmnst().get(IOrm.DBURL);
    String currDir = System.getProperty("user.dir");
    if (dbUrl.contains(IOrm.CURDIR)) {
      dbUrl = dbUrl.replace(IOrm.CURDIR, currDir + File.separator);
    } else if (dbUrl.contains(IOrm.CURPDIR)) {
      File fcd = new File(currDir);
      dbUrl = dbUrl.replace(IOrm.CURPDIR, fcd.getParent() + File.separator);
    }
    if (dbUrl.contains("#FS#")) {
      dbUrl = dbUrl.replace("#FS#",  File.separator);
    }
    this.fctApp.getFctBlc().getFctDt().setDbUrl(dbUrl);
    this.fctApp.getFctBlc().getFctDt().setDbCls(setng.lazCmnst().get(IOrm.JDBCCLS));
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.err.println("usage: " + FillDb.class.getName() +
      "<records count>");
      System.exit(1);
    }
    String rcs = args[0];
    int rct = 0;
    try {
      rct = Integer.parseInt(rcs);
    } catch (Exception e) {
      System.err.println("usage: " + FillDb.class.getName() +
      "<records count>");
      System.exit(1);
    }
    final int rc = rct;
    Thread thread1 = new Thread() {
      public void run() {
        try {
          FillDb fdb = new FillDb();
          fdb.populate(rc);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };
    thread1.start();
  }

  public void populate(int pRc) throws Exception {
    IOrm orm = (IOrm) this.fctApp.laz(this.rvs, IOrm.class.getSimpleName());
    orm.init(this.rvs);
    Setng stgOrm = (Setng) this.fctApp.laz(this.rvs, FctDt.STGORMNM);
    stgOrm.release();
    IRdb rdb = (IRdb<ResultSet>) this.fctApp.laz(this.rvs, IRdb.class.getSimpleName());
    ILog log = this.fctApp.getFctBlc().lazLogStd(this.rvs);
    log.info(this.rvs, getClass(), "Populating records: " + pRc);
    Map<String, Object> vs = new HashMap<String, Object>();
    Lng ru = new Lng();
    ru.setIid("ru");
    ItmCt pbhc = new ItmCt();
    pbhc.setIid(3L);
    ItmCt pchc = new ItmCt();
    pchc.setIid(2L);
    Uom each = new Uom();
    each.setIid(1L);
    CatGs ctpbh = new CatGs();
    ctpbh.setIid(122L);
    CatGs ctpch = new CatGs();
    ctpch.setIid(121L);
    CatGs catCars = new CatGs();
    catCars.setIid(4L);
    PicPlc pzr = new PicPlc();
    pzr.setIid(1L);
    PriCt pct = new PriCt();
    pct.setIid(2L);
    ItmSp img = new ItmSp();
    img.setIid(1L);
    ItmSp weight = new ItmSp();
    weight.setIid(10L);
    ItmSp spYear = new ItmSp();
    spYear.setIid(105L);
    ItmSp spManuf = new ItmSp();
    spManuf.setIid(100L);
    ItmSp spClr = new ItmSp();
    spClr.setIid(101L);
    ItmSp spFuel = new ItmSp();
    spFuel.setIid(103L);
    ItmSp spBody = new ItmSp();
    spBody.setIid(102L);
    ItmSp spTrn = new ItmSp();
    spTrn.setIid(104L);
    ItmSp spHtml = new ItmSp();
    spHtml.setIid(1000L);
    ItmSpGr spImSt1 = new ItmSpGr();
    spImSt1.setIid(1L);
    spImSt1.setNme("Image set 1");
    ItmSp spIm1 = new ItmSp();
    spIm1.setIid(2000L);
    spIm1.setIdx(2000);
    spIm1.setNme("Image1 in set1");
    spIm1.setTyp(EItmSpTy.IMAGE_IN_SET);
    ItmSp spIm2 = new ItmSp();
    spIm2.setIid(2001L);
    ItmSp spIm3 = new ItmSp();
    spIm3.setIid(2002L);
    ItmCt iicCars = new ItmCt();
    iicCars.setIid(76149L);
    iicCars.setNme("Cars");
    Date now = new Date();
    try {
      rdb.setAcmt(false);
      rdb.setTrIsl(IRdb.TRRUC);
      rdb.begin();
      ItmCt iicCarsd = orm.retEnt(this.rvs, vs, iicCars);
      if (iicCarsd == null) {
        orm.insert(this.rvs, vs, iicCars);
      }
      ItmSpGr spImSt1d = orm.retEnt(this.rvs, vs, spImSt1);
      if (spImSt1d == null) {
        orm.insert(this.rvs, vs, spImSt1);
      }
      for (int i =0; i < pRc; i++) {
        Itm pbh = new Itm();
        pbh.setNme("pizza with bacon hot#" + i);
        pbh.setCat(pbhc);
        pbh.setKnCs(BigDecimal.ZERO);
        pbh.setDbOr(orm.getDbId());
        pbh.setDuom(each);
        orm.insert(this.rvs, vs, pbh);
        ItmCtl ctPbh = new ItmCtl();
        ctPbh.setItm(pbh);
        ctPbh.setCatl(ctpbh);
        orm.insert(this.rvs, vs, ctPbh);
        ItmPlc plcPbh = new ItmPlc();
        plcPbh.setSinc(now);
        plcPbh.setItm(pbh);
        plcPbh.setPipl(pzr);
        plcPbh.setQuan(BigDecimal.TEN);
        orm.insert(this.rvs, vs, plcPbh);
        PriItm priPbh = new PriItm();
        priPbh.setItm(pbh);
        priPbh.setPriCt(pct);
        priPbh.setUom(each);
        double pr = 7;
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          pr = pr + Math.round(Math.random()) + Math.random();
        } else {
          pr = pr - Math.round(Math.random()) + Math.random();
        }
        priPbh.setPri(new BigDecimal(pr).setScale(2, RoundingMode.HALF_UP));
        priPbh.setUnSt(BigDecimal.ONE);
        orm.insert(this.rvs, vs, priPbh);
        ItmSpf specWePbh = new ItmSpf();
        specWePbh.setItm(pbh);
        specWePbh.setSpec(weight);
        specWePbh.setLng1(2L);
        specWePbh.setLng2(5L);
        double wei = 0.1 + Math.random();
        specWePbh.setNum1(new BigDecimal(wei).setScale(2, RoundingMode.HALF_UP));
        orm.insert(this.rvs, vs, specWePbh);
        ItmSpf specImPbh = new ItmSpf();
        specImPbh.setItm(pbh);
        specImPbh.setSpec(img);
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          specImPbh.setStr1("upl/merchandise.png");
        } else {
          specImPbh.setStr1("upl/merchandise2.png");
        }
        orm.insert(this.rvs, vs, specImPbh);
        Itm pch = new Itm();
        pch.setNme("pizza with cheese hot#" + i);
        pch.setKnCs(BigDecimal.ZERO);
        pch.setCat(pchc);
        pch.setDbOr(orm.getDbId());
        pch.setDuom(each);
        orm.insert(this.rvs, vs, pch);
        ItmCtl ctPch = new ItmCtl();
        ctPch.setItm(pch);
        ctPch.setCatl(ctpch);
        orm.insert(this.rvs, vs, ctPch);
        ItmPlc plcPch = new ItmPlc();
        plcPch.setSinc(now);
        plcPch.setItm(pch);
        plcPch.setPipl(pzr);
        plcPch.setQuan(BigDecimal.TEN);
        orm.insert(this.rvs, vs, plcPch);
        PriItm priPch = new PriItm();
        priPch.setItm(pch);
        priPch.setPriCt(pct);
        priPch.setUom(each);
        pr = 8.0;
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          pr = pr + Math.round(Math.random()) + Math.random();
        } else {
          pr = pr - Math.round(Math.random()) + Math.random();
        }
        priPch.setPri(new BigDecimal(pr).setScale(2, RoundingMode.HALF_UP));
        priPch.setUnSt(BigDecimal.ONE);
        orm.insert(this.rvs, vs, priPch);
        ItmSpf specWePch = new ItmSpf();
        specWePch.setItm(pch);
        specWePch.setSpec(weight);
        wei = 0.1 + Math.random();
        specWePch.setNum1(new BigDecimal(wei).setScale(2, RoundingMode.HALF_UP));
        specWePch.setLng1(2L);
        specWePch.setLng2(5L);
        orm.insert(this.rvs, vs, specWePch);
        ItmSpf specImPch = new ItmSpf();
        specImPch.setItm(pch);
        specImPch.setSpec(img);
        if ((i + Math.round(Math.random())) % 2 == 0) {
          specImPch.setStr1("upl/merchandise.png");
        } else {
          specImPch.setStr1("upl/merchandise2.png");
        }
        orm.insert(this.rvs, vs, specImPch);

        Itm hond = new Itm();
        hond.setKnCs(BigDecimal.ZERO);
        hond.setNme("Honda#" + i);
        hond.setCat(iicCars);
        hond.setDbOr(orm.getDbId());
        hond.setDuom(each);
        orm.insert(this.rvs, vs, hond);
        I18Itm i18Hond = new I18Itm();
        i18Hond.setHasNm(hond);
        i18Hond.setLng(ru);
        i18Hond.setNme("Хонда#" + i);
        orm.insert(this.rvs, vs, i18Hond);
        ItmCtl ctHond = new ItmCtl();
        ctHond.setItm(hond);
        ctHond.setCatl(catCars);
        orm.insert(this.rvs, vs, ctHond);
        ItmPlc plcHond = new ItmPlc();
        plcHond.setSinc(now);
        plcHond.setItm(hond);
        plcHond.setPipl(pzr);
        plcHond.setQuan(BigDecimal.ONE);
        orm.insert(this.rvs, vs, plcHond);
        PriItm priHond = new PriItm();
        priHond.setItm(hond);
        priHond.setPriCt(pct);
        priHond.setUom(each);
        pr = 899.0 + (Math.random() * 1000);
        priHond.setPri(new BigDecimal(pr).setScale(2, RoundingMode.HALF_UP));
        priHond.setUnSt(BigDecimal.ONE);
        orm.insert(this.rvs, vs, priHond);
        ItmSpf specYearHond = new ItmSpf();
        specYearHond.setItm(hond);
        specYearHond.setSpec(spYear);
        Long year = 2005L + Double.valueOf(Math.random() * 10.0).longValue();
        specYearHond.setLng1(year);
        orm.insert(this.rvs, vs, specYearHond);
        ItmSpf spManufHond = new ItmSpf();
        spManufHond.setItm(hond);
        spManufHond.setSpec(spManuf);
        spManufHond.setLng2(1L);
        spManufHond.setStr2("Manufacturer");
        spManufHond.setLng1(2L);
        spManufHond.setStr1("Honda");
        orm.insert(this.rvs, vs, spManufHond);
        ItmSpf spClrHond = new ItmSpf();
        spClrHond.setItm(hond);
        spClrHond.setSpec(spClr);
        spClrHond.setLng2(2L);
        spClrHond.setStr2("Color");
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spClrHond.setLng1(3L);
          spClrHond.setStr1("Red");
        } else {
          spClrHond.setLng1(7L);
          spClrHond.setStr1("White");
        }
        orm.insert(this.rvs, vs, spClrHond);
        ItmSpf spFuelHond = new ItmSpf();
        spFuelHond.setItm(hond);
        spFuelHond.setSpec(spFuel);
        spFuelHond.setLng2(4L);
        spFuelHond.setStr2("Fuel");
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spFuelHond.setLng1(6L);
          spFuelHond.setStr1("Gasoline");
        } else {
          spFuelHond.setLng1(10L);
          spFuelHond.setStr1("Diesel");
        }
        orm.insert(this.rvs, vs, spFuelHond);
        ItmSpf spBodyHond = new ItmSpf();
        spBodyHond.setItm(hond);
        spBodyHond.setSpec(spBody);
        spBodyHond.setLng2(3L);
        spBodyHond.setStr2("Body type");
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spBodyHond.setLng1(5L);
          spBodyHond.setStr1("Sedan");
        } else {
          spBodyHond.setLng1(8L);
          spBodyHond.setStr1("Wagon");
        }
        orm.insert(this.rvs, vs, spBodyHond);
        ItmSpf spTrnHond = new ItmSpf();
        spTrnHond.setItm(hond);
        spTrnHond.setSpec(spTrn);
        spTrnHond.setLng2(5L);
        spTrnHond.setStr2("Transmission");
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spTrnHond.setLng1(4L);
          spTrnHond.setStr1("AT");
        } else {
          spTrnHond.setLng1(9L);
          spTrnHond.setStr1("MT");
        }
        orm.insert(this.rvs, vs, spTrnHond);
        ItmSpf specImHond = new ItmSpf();
        specImHond.setItm(hond);
        specImHond.setSpec(img);
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          specImHond.setStr1("upl/merchandise.png");
        } else {
          specImHond.setStr1("upl/merchandise2.png");
        }
        orm.insert(this.rvs, vs, specImHond);
        ItmSpf spIm1Hond = new ItmSpf();
        spIm1Hond.setItm(hond);
        spIm1Hond.setSpec(spIm1);
        spIm1Hond.setStr3("scratch on rear");
        spIm1Hond.setStr1("upl/merchandise.png");
        orm.insert(this.rvs, vs, spIm1Hond);
        ItmSpf spIm2Hond = new ItmSpf();
        spIm2Hond.setItm(hond);
        spIm2Hond.setSpec(spIm2);
        spIm2Hond.setStr3("front stain");
        spIm2Hond.setStr1("upl/merchandise2.png");
        orm.insert(this.rvs, vs, spIm2Hond);
        ItmSpf spIm3Hond = new ItmSpf();
        spIm3Hond.setItm(hond);
        spIm3Hond.setSpec(spIm3);
        spIm3Hond.setStr3("scratch on window");
        spIm3Hond.setStr1("upl/merchandise.png");
        orm.insert(this.rvs, vs, spIm3Hond);
        ItmSpf spHtmlHond = new ItmSpf();
        spHtmlHond.setItm(hond);
        spHtmlHond.setSpec(spHtml);
        spHtmlHond.setStr3("ru,fr");
        spHtmlHond.setStr1("upl/1540295496778fordred.html");
        orm.insert(this.rvs, vs, spHtmlHond);

        Itm ford = new Itm();
        ford.setNme("Ford#" + i);
        ford.setKnCs(BigDecimal.ZERO);
        ford.setCat(iicCars);
        ford.setDbOr(orm.getDbId());
        ford.setDuom(each);
        orm.insert(this.rvs, vs, ford);
        ItmCtl ctFord = new ItmCtl();
        ctFord.setItm(ford);
        ctFord.setCatl(catCars);
        orm.insert(this.rvs, vs, ctFord);
        ItmPlc plcFord = new ItmPlc();
        plcFord.setSinc(now);
        plcFord.setItm(ford);
        plcFord.setPipl(pzr);
        plcFord.setQuan(BigDecimal.ONE);
        orm.insert(this.rvs, vs, plcFord);
        PriItm priFord = new PriItm();
        priFord.setItm(ford);
        priFord.setPriCt(pct);
        priFord.setUom(each);
        pr = 899.0 + (Math.random() * 1000);
        priFord.setPri(new BigDecimal(pr).setScale(2, RoundingMode.HALF_UP));
        priFord.setUnSt(BigDecimal.ONE);
        orm.insert(this.rvs, vs, priFord);
        ItmSpf specYearFord = new ItmSpf();
        specYearFord.setItm(ford);
        specYearFord.setSpec(spYear);
        year = 2005L + Double.valueOf(Math.random() * 10.0).longValue();
        specYearFord.setLng1(year);
        orm.insert(this.rvs, vs, specYearFord);
        ItmSpf spManufFord = new ItmSpf();
        spManufFord.setItm(ford);
        spManufFord.setSpec(spManuf);
        spManufFord.setLng1(1L);
        spManufFord.setLng2(1L);
        spManufFord.setStr1("Ford");
        spManufFord.setStr2("Manufacturer");
        orm.insert(this.rvs, vs, spManufFord);
        ItmSpf spClrFord = new ItmSpf();
        spClrFord.setItm(ford);
        spClrFord.setSpec(spClr);
        spClrFord.setLng2(2L);
        spClrFord.setStr2("Color");
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spClrFord.setLng1(3L);
          spClrFord.setStr1("Red");
        } else {
          spClrFord.setLng1(7L);
          spClrFord.setStr1("White");
        }
        orm.insert(this.rvs, vs, spClrFord);
        ItmSpf spFuelFord = new ItmSpf();
        spFuelFord.setItm(ford);
        spFuelFord.setSpec(spFuel);
        spFuelFord.setLng2(4L);
        spFuelFord.setStr2("Fuel");
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spFuelFord.setLng1(6L);
          spFuelFord.setStr1("Gasoline");
        } else {
          spFuelFord.setLng1(10L);
          spFuelFord.setStr1("Diesel");
        }
        orm.insert(this.rvs, vs, spFuelFord);
        ItmSpf spBodyFord = new ItmSpf();
        spBodyFord.setItm(ford);
        spBodyFord.setSpec(spBody);
        spBodyFord.setLng2(3L);
        spBodyFord.setStr2("Body type");
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spBodyFord.setLng1(5L);
          spBodyFord.setStr1("Sedan");
        } else {
          spBodyFord.setLng1(8L);
          spBodyFord.setStr1("Wagon");
        }
        orm.insert(this.rvs, vs, spBodyFord);
        ItmSpf spTrnFord = new ItmSpf();
        spTrnFord.setItm(ford);
        spTrnFord.setSpec(spTrn);
        spTrnFord.setLng2(5L);
        spTrnFord.setStr2("Transmission");
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spTrnFord.setLng1(4L);
          spTrnFord.setStr1("AT");
        } else {
          spTrnFord.setLng1(9L);
          spTrnFord.setStr1("MT");
        }
        orm.insert(this.rvs, vs, spTrnFord);
        ItmSpf specImFord = new ItmSpf();
        specImFord.setItm(ford);
        specImFord.setSpec(img);
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          specImFord.setStr1("upl/merchandise.png");
        } else {
          specImFord.setStr1("upl/merchandise2.png");
        }
        orm.insert(this.rvs, vs, specImFord);
        ItmSpf spIm1Ford = new ItmSpf();
        spIm1Ford.setItm(ford);
        spIm1Ford.setSpec(spIm1);
        spIm1Ford.setStr3("scratch on rear");
        spIm1Ford.setStr1("upl/merchandise.png");
        orm.insert(this.rvs, vs, spIm1Ford);
        ItmSpf spIm2Ford = new ItmSpf();
        spIm2Ford.setItm(ford);
        spIm2Ford.setSpec(spIm2);
        spIm2Ford.setStr3("front stain");
        spIm2Ford.setStr1("upl/merchandise2.png");
        orm.insert(this.rvs, vs, spIm2Ford);
        ItmSpf spIm3Ford = new ItmSpf();
        spIm3Ford.setItm(ford);
        spIm3Ford.setSpec(spIm3);
        spIm3Ford.setStr3("scratch on window");
        spIm3Ford.setStr1("upl/merchandise.png");
        orm.insert(this.rvs, vs, spIm3Ford);
        ItmSpf spHtmlFord = new ItmSpf();
        spHtmlFord.setItm(ford);
        spHtmlFord.setSpec(spHtml);
        spHtmlFord.setStr3("ru,fr");
        spHtmlFord.setStr1("upl/1540295496778fordred.html");
        orm.insert(this.rvs, vs, spHtmlFord);

      }
      rdb.commit();
    } catch (Exception ex) {
      ex.printStackTrace();
      if (!rdb.getAcmt()) {
        rdb.rollBack();
      }
      throw new Exception(ex);
    } finally {
      rdb.release();
    }
    this.fctApp.release(this.rvs);
  }
}
