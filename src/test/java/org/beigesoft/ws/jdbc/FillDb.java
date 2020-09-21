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

package org.beigesoft.ws.jdbc;

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
import org.beigesoft.acc.jdbc.FctTstSqlt;
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
import org.beigesoft.ws.mdlp.I18CatGs;
import org.beigesoft.ws.mdlp.CatGs;
import org.beigesoft.ws.mdlp.ItmCtl;
import org.beigesoft.ws.mdlp.PicPlc;
import org.beigesoft.ws.mdlp.ItmPlc;
import org.beigesoft.ws.mdlp.PriItm;
import org.beigesoft.ws.mdlp.PriCt;
import org.beigesoft.ws.mdlp.ItmSp;
import org.beigesoft.ws.mdlp.I18ItmSp;
import org.beigesoft.ws.mdlp.ItmSpf;
import org.beigesoft.ws.mdlp.ItmSpGr;
import org.beigesoft.ws.mdlp.I18ChoSp;
import org.beigesoft.ws.mdlp.ChoSp;
import org.beigesoft.ws.mdlp.ChoSpTy;
import org.beigesoft.ws.mdlp.Htmlt;

/**
 * <p>Populating SQLite database with sample data - pizza with bacon hot#, pizza with cheese hot#, Ford #, Honda#.
 * Database must has tax category, item category, catalogs as in sample database bobs-pizza-ws2!
 * Usage with Maven example, to fill database bsws.sqlite with 1000 sample records for each good:
 * <pre>
 * mvn exec:exec -Dexec.executable="java" -Dexec.args="-classpath %classpath org.beigesoft.ws.FillDb 100" -Dexec.classpathScope=test
 * 
 * or with debugging:
 * mvn exec:exec -Dexec.executable="java" -Dexec.args="-classpath %classpath -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000 org.beigesoft.ws.FillDb 100" -Dexec.classpathScope=test
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
    String dbUrl = "jdbc:sqlite:#currentDir#bsws.sqlite";
    String currDir = System.getProperty("user.dir");
    dbUrl = dbUrl.replace(IOrm.CURDIR, currDir + File.separator);
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
    try {
      FillDb fdb = new FillDb();
      fdb.populate(rct);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void populate(int pRc) throws Exception {
    IOrm orm = (IOrm) this.fctApp.laz(this.rvs, IOrm.class.getSimpleName());
    orm.init(this.rvs);
    Setng stgOrm = (Setng) this.fctApp.laz(this.rvs, FctDt.STGORMNM);
    stgOrm.release();
    IRdb rdb = (IRdb<ResultSet>) this.fctApp.laz(this.rvs, IRdb.class.getSimpleName());
    ILog log = this.fctApp.getFctBlc().lazLogStd(this.rvs);
    log.info(this.rvs, getClass(), "Populating records count: " + pRc);
    Map<String, Object> vs = new HashMap<String, Object>();
    Lng ru = new Lng();
    ru.setIid("ru");
    Uom each = new Uom();
    each.setIid(1L);
    ItmSp spHtml = new ItmSp();
    spHtml.setIid(50L);
    spHtml.setIdx(50);
    spHtml.setNme("Embed HTML");
    spHtml.setTyp(EItmSpTy.FILE_EMBEDDED);
    Htmlt htmlt = new Htmlt();
    htmlt.setIid(1L);
    htmlt.setNme("spec name bold: spec value1 value2");
    htmlt.setVal("<b>:SPECNM:</b> :VAL1 :VAL2");
    ItmSp weight = new ItmSp();
    weight.setIid(1L);
    weight.setIdx(10);
    weight.setNme("Weight");
    weight.setHtmt(htmlt);
    weight.setTyp(EItmSpTy.BIGDECIMAL);
    weight.setInFlt(true);
    weight.setInLst(true);
    weight.setFoOrd(true);
    I18ItmSp i18weight = new I18ItmSp();
    i18weight.setHasNm(weight);
    i18weight.setLng(ru);
    i18weight.setNme("Вес");
    ItmSp img = new ItmSp();
    img.setIid(2L);
    img.setIdx(1);
    img.setNme("Image");
    img.setTyp(EItmSpTy.IMAGE);
    ItmSp spYear = new ItmSp();
    spYear.setIid(105L);
    spYear.setIdx(105);
    spYear.setNme("Year");
    spYear.setTyp(EItmSpTy.INTEGER);
    spYear.setInFlt(true);
    spYear.setInLst(true);
    spYear.setFoOrd(true);
    I18ItmSp i18spYear = new I18ItmSp();
    i18spYear.setHasNm(spYear);
    i18spYear.setLng(ru);
    i18spYear.setNme("Год");
    ItmSpGr spgImSt = new ItmSpGr();
    spgImSt.setIid(1L);
    spgImSt.setNme("Image set");
    ItmSp spIm1 = new ItmSp();
    spIm1.setIid(2000L);
    spIm1.setIdx(2000);
    spIm1.setNme("Image1 in set");
    spIm1.setTyp(EItmSpTy.IMAGE_IN_SET);
    spIm1.setGrp(spgImSt);
    ItmSp spIm2 = new ItmSp();
    spIm2.setIid(2001L);
    spIm2.setIdx(2001);
    spIm2.setNme("Image2 in set");
    spIm2.setTyp(EItmSpTy.IMAGE_IN_SET);
    spIm2.setGrp(spgImSt);
    ItmSp spIm3 = new ItmSp();
    spIm3.setIid(2002L);
    spIm3.setIdx(2002);
    spIm3.setNme("Image3 in set");
    spIm3.setTyp(EItmSpTy.IMAGE_IN_SET);
    spIm3.setGrp(spgImSt);
    ItmCt iicCars = new ItmCt();
    iicCars.setIid(76149L);
    iicCars.setNme("Cars");
    ChoSpTy body = new ChoSpTy();
    body.setIid(3L);
    body.setNme("Body type");
    ChoSpTy color = new ChoSpTy();
    color.setIid(2L);
    color.setNme("Color");
    ChoSpTy fuel = new ChoSpTy();
    fuel.setIid(4L);
    fuel.setNme("Fuel");
    ChoSpTy trans = new ChoSpTy();
    trans.setIid(5L);
    trans.setNme("Transmission");
    ChoSpTy manuf = new ChoSpTy();
    manuf.setIid(1L);
    manuf.setNme("Manufacturer");
    ChoSp chsFord = new ChoSp();
    chsFord.setIid(1L);
    chsFord.setNme("Ford");
    chsFord.setChoTy(manuf);
    ChoSp chsHonda = new ChoSp();
    chsHonda.setIid(2L);
    chsHonda.setNme("Honda");
    chsHonda.setChoTy(manuf);
    ChoSp chsRed = new ChoSp();
    chsRed.setIid(3L);
    chsRed.setNme("Red");
    chsRed.setChoTy(color);
    ChoSp chsWhite = new ChoSp();
    chsWhite.setIid(4L);
    chsWhite.setNme("White");
    chsWhite.setChoTy(color);
    ChoSp chsAT = new ChoSp();
    chsAT.setIid(5L);
    chsAT.setNme("AT");
    chsAT.setChoTy(trans);
    ChoSp chsMT = new ChoSp();
    chsMT.setIid(6L);
    chsMT.setNme("MT");
    chsMT.setChoTy(trans);
    ChoSp chsGasoline = new ChoSp();
    chsGasoline.setIid(7L);
    chsGasoline.setNme("Gasoline");
    chsGasoline.setChoTy(fuel);
    ChoSp chsDiesel = new ChoSp();
    chsDiesel.setIid(8L);
    chsDiesel.setNme("Diesel");
    chsDiesel.setChoTy(fuel);
    ChoSp chsSedan = new ChoSp();
    chsSedan.setIid(9L);
    chsSedan.setNme("Sedan");
    chsSedan.setChoTy(body);
    ChoSp chsWagon = new ChoSp();
    chsWagon.setIid(10L);
    chsWagon.setNme("Wagon");
    chsWagon.setChoTy(body);
    I18ChoSp i18chsHonda = new I18ChoSp();
    i18chsHonda.setHasNm(chsHonda);
    i18chsHonda.setLng(ru);
    i18chsHonda.setNme("Хонда");
    I18ChoSp i18chsRed = new I18ChoSp();
    i18chsRed.setHasNm(chsRed);
    i18chsRed.setLng(ru);
    i18chsRed.setNme("Красный");
    I18ChoSp i18chsWhite = new I18ChoSp();
    i18chsWhite.setHasNm(chsWhite);
    i18chsWhite.setLng(ru);
    i18chsWhite.setNme("Белый");
    I18ChoSp i18chsAT = new I18ChoSp();
    i18chsAT.setHasNm(chsAT);
    i18chsAT.setLng(ru);
    i18chsAT.setNme("Автомат");
    I18ChoSp i18chsMT = new I18ChoSp();
    i18chsMT.setHasNm(chsMT);
    i18chsMT.setLng(ru);
    i18chsMT.setNme("Механика");
    I18ChoSp i18chsGasoline = new I18ChoSp();
    i18chsGasoline.setHasNm(chsGasoline);
    i18chsGasoline.setLng(ru);
    i18chsGasoline.setNme("Бензин");
    I18ChoSp i18chsDiesel = new I18ChoSp();
    i18chsDiesel.setHasNm(chsDiesel);
    i18chsDiesel.setLng(ru);
    i18chsDiesel.setNme("Дизель");
    I18ChoSp i18chsSedan = new I18ChoSp();
    i18chsSedan.setHasNm(chsSedan);
    i18chsSedan.setLng(ru);
    i18chsSedan.setNme("Седан");
    I18ChoSp i18chsWagon = new I18ChoSp();
    i18chsWagon.setHasNm(chsWagon);
    i18chsWagon.setLng(ru);
    i18chsWagon.setNme("Универсал");
    I18ChoSp i18chsFord = new I18ChoSp();
    i18chsFord.setHasNm(chsFord);
    i18chsFord.setLng(ru);
    i18chsFord.setNme("Форд");
    ItmSp spManuf = new ItmSp();
    spManuf.setIid(100L);
    spManuf.setIdx(100);
    spManuf.setNme("Manufacturer");
    spManuf.setTyp(EItmSpTy.CHOOSEABLE_SPECIFICS);
    spManuf.setChSpTy(manuf);
    spManuf.setInFlt(true);
    spManuf.setInLst(true);
    I18ItmSp i18spManuf = new I18ItmSp();
    i18spManuf.setHasNm(spManuf);
    i18spManuf.setLng(ru);
    i18spManuf.setNme("Производитель");
    ItmSp spClr = new ItmSp();
    spClr.setIid(101L);
    spClr.setIdx(101);
    spClr.setNme(color.getNme());
    spClr.setTyp(EItmSpTy.CHOOSEABLE_SPECIFICS);
    spClr.setChSpTy(color);
    spClr.setInFlt(true);
    spClr.setInLst(true);
    I18ItmSp i18spClr = new I18ItmSp();
    i18spClr.setHasNm(spClr);
    i18spClr.setLng(ru);
    i18spClr.setNme("Цвет");
    ItmSp spFuel = new ItmSp();
    spFuel.setIid(103L);
    spFuel.setIdx(103);
    spFuel.setNme(fuel.getNme());
    spFuel.setTyp(EItmSpTy.CHOOSEABLE_SPECIFICS);
    spFuel.setChSpTy(fuel);
    spFuel.setInFlt(true);
    spFuel.setInLst(true);
    I18ItmSp i18spFuel = new I18ItmSp();
    i18spFuel.setHasNm(spFuel);
    i18spFuel.setLng(ru);
    i18spFuel.setNme("Топливо");
    ItmSp spBody = new ItmSp();
    spBody.setIid(102L);
    spBody.setIdx(102);
    spBody.setNme(body.getNme());
    spBody.setTyp(EItmSpTy.CHOOSEABLE_SPECIFICS);
    spBody.setChSpTy(body);
    spBody.setInFlt(true);
    spBody.setInLst(true);
    I18ItmSp i18spBody = new I18ItmSp();
    i18spBody.setHasNm(spBody);
    i18spBody.setLng(ru);
    i18spBody.setNme("Кузов");
    ItmSp spTrn = new ItmSp();
    spTrn.setIid(104L);
    spTrn.setIdx(104);
    spTrn.setNme(trans.getNme());
    spTrn.setTyp(EItmSpTy.CHOOSEABLE_SPECIFICS);
    spTrn.setChSpTy(trans);
    spTrn.setInFlt(true);
    spTrn.setInLst(true);
    I18ItmSp i18spTrn = new I18ItmSp();
    i18spTrn.setHasNm(spTrn);
    i18spTrn.setLng(ru);
    i18spTrn.setNme("Трансмиссия");
    ItmCt pbhc = new ItmCt();
    pbhc.setIid(3333L);
    pbhc.setNme("Pizza bekon hot");
    ItmCt pchc = new ItmCt();
    pchc.setIid(3332L);
    pchc.setNme("Pizza cheese hot");
    CatGs ctpall = new CatGs();
    ctpall.setIid(2L);
    ctpall.setIdx(2);
    ctpall.setNme("Pizza");
    ctpall.setHsSub(true);
    ctpall.setHsGds(true);
    CatGs ctpbh = new CatGs();
    ctpbh.setIid(4L);
    ctpbh.setIdx(4);
    ctpbh.setNme("Pizza with meat");
    ctpbh.setHsGds(true);
    CatGs ctpch = new CatGs();
    ctpch.setIid(3L);
    ctpch.setIdx(3);
    ctpch.setNme("Pizza vegeterian");
    ctpch.setHsGds(true);
    CatGs catCars = new CatGs();
    catCars.setIid(120L);
    catCars.setNme("Cars");
    catCars.setIdx(6);
    catCars.setFlSpe(true);
    catCars.setHsGds(true);
    catCars.setHsSgo(true);
    I18CatGs i18catCars = new I18CatGs();
    i18catCars.setHasNm(catCars);
    i18catCars.setLng(ru);
    i18catCars.setNme("Авто");
    PicPlc pzr = new PicPlc();
    pzr.setIid(1L);
    pzr.setNme("Pizzeria");
    PriCt pct = new PriCt();
    pct.setIid(1L);
    pct.setNme("All");
    try {
      rdb.setAcmt(false);
      rdb.setTrIsl(IRdb.TRRUC);
      rdb.begin();
      PicPlc pzrd = orm.retEnt(this.rvs, vs, pzr);
      if (pzrd == null) {
        orm.insert(this.rvs, vs, pzr);
      }
      CatGs catCarsd = orm.retEnt(this.rvs, vs, catCars);
      if (catCarsd == null) {
        orm.insert(this.rvs, vs, catCars);
      }
      I18CatGs i18catCarsd = orm.retEnt(this.rvs, vs, i18catCars);
      if (i18catCarsd == null) {
        orm.insert(this.rvs, vs, i18catCars);
      }
      CatGs ctpalld = orm.retEnt(this.rvs, vs, ctpall);
      if (ctpalld == null) {
        orm.insert(this.rvs, vs, ctpall);
      }
      CatGs ctpchd = orm.retEnt(this.rvs, vs, ctpch);
      if (ctpchd == null) {
        orm.insert(this.rvs, vs, ctpch);
      }
      CatGs ctpbhd = orm.retEnt(this.rvs, vs, ctpbh);
      if (ctpbhd == null) {
        orm.insert(this.rvs, vs, ctpbh);
      }
      PriCt pctd = orm.retEnt(this.rvs, vs, pct);
      if (pctd == null) {
        orm.insert(this.rvs, vs, pct);
      }
      ItmCt pbhcd = orm.retEnt(this.rvs, vs, pbhc);
      if (pbhcd == null) {
        orm.insert(this.rvs, vs, pbhc);
      }
      ItmCt pchcd = orm.retEnt(this.rvs, vs, pchc);
      if (pchcd == null) {
        orm.insert(this.rvs, vs, pchc);
      }
      ItmCt iicCarsd = orm.retEnt(this.rvs, vs, iicCars);
      if (iicCarsd == null) {
        orm.insert(this.rvs, vs, iicCars);
      }
      ItmSpGr spgImStd = orm.retEnt(this.rvs, vs, spgImSt);
      if (spgImStd == null) {
        orm.insert(this.rvs, vs, spgImSt);
      }
      ChoSpTy manufd = orm.retEnt(this.rvs, vs, manuf);
      if (manufd == null) {
        orm.insert(this.rvs, vs, manuf);
      }
      ChoSpTy transd = orm.retEnt(this.rvs, vs, trans);
      if (transd == null) {
        orm.insert(this.rvs, vs, trans);
      }
      ChoSpTy fueld = orm.retEnt(this.rvs, vs, fuel);
      if (fueld == null) {
        orm.insert(this.rvs, vs, fuel);
      }
      ChoSpTy bodyd = orm.retEnt(this.rvs, vs, body);
      if (bodyd == null) {
        orm.insert(this.rvs, vs, body);
      }
      ChoSpTy colord = orm.retEnt(this.rvs, vs, color);
      if (colord == null) {
        orm.insert(this.rvs, vs, color);
      }
      ChoSp chsWagond = orm.retEnt(this.rvs, vs, chsWagon);
      if (chsWagond == null) {
        orm.insert(this.rvs, vs, chsWagon);
      }
      ChoSp chsDieseld = orm.retEnt(this.rvs, vs, chsDiesel);
      if (chsDieseld == null) {
        orm.insert(this.rvs, vs, chsDiesel);
      }
      ChoSp chsGasolined = orm.retEnt(this.rvs, vs, chsGasoline);
      if (chsGasolined == null) {
        orm.insert(this.rvs, vs, chsGasoline);
      }
      ChoSp chsMTd = orm.retEnt(this.rvs, vs, chsMT);
      if (chsMTd == null) {
        orm.insert(this.rvs, vs, chsMT);
      }
      ChoSp chsATd = orm.retEnt(this.rvs, vs, chsAT);
      if (chsATd == null) {
        orm.insert(this.rvs, vs, chsAT);
      }
      ChoSp chsWhited = orm.retEnt(this.rvs, vs, chsWhite);
      if (chsWhited == null) {
        orm.insert(this.rvs, vs, chsWhite);
      }
      ChoSp chsRedd = orm.retEnt(this.rvs, vs, chsRed);
      if (chsRedd == null) {
        orm.insert(this.rvs, vs, chsRed);
      }
      ChoSp chsHondad = orm.retEnt(this.rvs, vs, chsHonda);
      if (chsHondad == null) {
        orm.insert(this.rvs, vs, chsHonda);
      }
      ChoSp chsFordd = orm.retEnt(this.rvs, vs, chsFord);
      if (chsFordd == null) {
        orm.insert(this.rvs, vs, chsFord);
      }
      ChoSp chsSedand = orm.retEnt(this.rvs, vs, chsSedan);
      if (chsSedand == null) {
        orm.insert(this.rvs, vs, chsSedan);
      }
      I18ChoSp i18chsSedand = orm.retEnt(this.rvs, vs, i18chsSedan);
      if (i18chsSedand == null) {
        orm.insert(this.rvs, vs, i18chsSedan);
      }
      I18ChoSp i18chsWagond = orm.retEnt(this.rvs, vs, i18chsWagon);
      if (i18chsWagond == null) {
        orm.insert(this.rvs, vs, i18chsWagon);
      }
      I18ChoSp i18chsDieseld = orm.retEnt(this.rvs, vs, i18chsDiesel);
      if (i18chsDieseld == null) {
        orm.insert(this.rvs, vs, i18chsDiesel);
      }
      I18ChoSp i18chsGasolined = orm.retEnt(this.rvs, vs, i18chsGasoline);
      if (i18chsGasolined == null) {
        orm.insert(this.rvs, vs, i18chsGasoline);
      }
      I18ChoSp i18chsMTd = orm.retEnt(this.rvs, vs, i18chsMT);
      if (i18chsMTd == null) {
        orm.insert(this.rvs, vs, i18chsMT);
      }
      I18ChoSp i18chsATd = orm.retEnt(this.rvs, vs, i18chsAT);
      if (i18chsATd == null) {
        orm.insert(this.rvs, vs, i18chsAT);
      }
      I18ChoSp i18chsWhited = orm.retEnt(this.rvs, vs, i18chsWhite);
      if (i18chsWhited == null) {
        orm.insert(this.rvs, vs, i18chsWhite);
      }
      I18ChoSp i18chsRedd = orm.retEnt(this.rvs, vs, i18chsRed);
      if (i18chsRedd == null) {
        orm.insert(this.rvs, vs, i18chsRed);
      }
      I18ChoSp i18chsHondad = orm.retEnt(this.rvs, vs, i18chsHonda);
      if (i18chsHondad == null) {
        orm.insert(this.rvs, vs, i18chsHonda);
      }
      I18ChoSp i18chsFordd = orm.retEnt(this.rvs, vs, i18chsFord);
      if (i18chsFordd == null) {
        orm.insert(this.rvs, vs, i18chsFord);
      }
      Htmlt htmltd = orm.retEnt(this.rvs, vs, htmlt);
      if (htmltd == null) {
        orm.insert(this.rvs, vs, htmlt);
      }
      ItmSp imgd = orm.retEnt(this.rvs, vs, img);
      if (imgd == null) {
        orm.insert(this.rvs, vs, img);
      }
      ItmSp spHtmld = orm.retEnt(this.rvs, vs, spHtml);
      if (spHtmld == null) {
        orm.insert(this.rvs, vs, spHtml);
      }
      ItmSp spIm1d = orm.retEnt(this.rvs, vs, spIm1);
      if (spIm1d == null) {
        orm.insert(this.rvs, vs, spIm1);
      }
      ItmSp spIm2d = orm.retEnt(this.rvs, vs, spIm2);
      if (spIm2d == null) {
        orm.insert(this.rvs, vs, spIm2);
      }
      ItmSp spIm3d = orm.retEnt(this.rvs, vs, spIm3);
      if (spIm3d == null) {
        orm.insert(this.rvs, vs, spIm3);
      }
      ItmSp spYeard = orm.retEnt(this.rvs, vs, spYear);
      if (spYeard == null) {
        orm.insert(this.rvs, vs, spYear);
      }
      ItmSp weightd = orm.retEnt(this.rvs, vs, weight);
      if (weightd == null) {
        orm.insert(this.rvs, vs, weight);
      }
      ItmSp spBodyd = orm.retEnt(this.rvs, vs, spBody);
      if (spBodyd == null) {
        orm.insert(this.rvs, vs, spBody);
      }
      ItmSp spFueld = orm.retEnt(this.rvs, vs, spFuel);
      if (spFueld == null) {
        orm.insert(this.rvs, vs, spFuel);
      }
      ItmSp spTrnd = orm.retEnt(this.rvs, vs, spTrn);
      if (spTrnd == null) {
        orm.insert(this.rvs, vs, spTrn);
      }
      ItmSp spManufd = orm.retEnt(this.rvs, vs, spManuf);
      if (spManufd == null) {
        orm.insert(this.rvs, vs, spManuf);
      }
      ItmSp spClrd = orm.retEnt(this.rvs, vs, spClr);
      if (spClrd == null) {
        orm.insert(this.rvs, vs, spClr);
      }
      I18ItmSp i18spYeard = orm.retEnt(this.rvs, vs, i18spYear);
      if (i18spYeard == null) {
        orm.insert(this.rvs, vs, i18spYear);
      }
      I18ItmSp i18weightd = orm.retEnt(this.rvs, vs, i18weight);
      if (i18weightd == null) {
        orm.insert(this.rvs, vs, i18weight);
      }
      I18ItmSp i18spBodyd = orm.retEnt(this.rvs, vs, i18spBody);
      if (i18spBodyd == null) {
        orm.insert(this.rvs, vs, i18spBody);
      }
      I18ItmSp i18spFueld = orm.retEnt(this.rvs, vs, i18spFuel);
      if (i18spFueld == null) {
        orm.insert(this.rvs, vs, i18spFuel);
      }
      I18ItmSp i18spTrnd = orm.retEnt(this.rvs, vs, i18spTrn);
      if (i18spTrnd == null) {
        orm.insert(this.rvs, vs, i18spTrn);
      }
      I18ItmSp i18spManufd = orm.retEnt(this.rvs, vs, i18spManuf);
      if (i18spManufd == null) {
        orm.insert(this.rvs, vs, i18spManuf);
      }
      I18ItmSp i18spClrd = orm.retEnt(this.rvs, vs, i18spClr);
      if (i18spClrd == null) {
        orm.insert(this.rvs, vs, i18spClr);
      }
      rdb.commit();
      log.info(this.rvs, getClass(), "Base models OK");
    } catch (Exception ex) {
      ex.printStackTrace();
      if (!rdb.getAcmt()) {
        rdb.rollBack();
      }
      this.fctApp.release(this.rvs);
      throw new Exception(ex);
    } finally {
      log.info(this.rvs, getClass(), "Base models, RDB close");
      rdb.release();
    }
    try {
      log.info(this.rvs, getClass(), "Generating data...");
      rdb.setAcmt(false);
      rdb.setTrIsl(IRdb.TRRUC);
      rdb.begin();
      Date now = new Date();
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
        spManufHond.setLng2(manuf.getIid());
        spManufHond.setStr2(manuf.getNme());
        spManufHond.setLng1(chsHonda.getIid());
        spManufHond.setStr1(chsHonda.getNme());
        orm.insert(this.rvs, vs, spManufHond);
        ItmSpf spClrHond = new ItmSpf();
        spClrHond.setItm(hond);
        spClrHond.setSpec(spClr);
        spClrHond.setLng2(color.getIid());
        spClrHond.setStr2(color.getNme());
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spClrHond.setLng1(chsRed.getIid());
          spClrHond.setStr1(chsRed.getNme());
        } else {
          spClrHond.setLng1(chsWhite.getIid());
          spClrHond.setStr1(chsWhite.getNme());
        }
        orm.insert(this.rvs, vs, spClrHond);
        ItmSpf spFuelHond = new ItmSpf();
        spFuelHond.setItm(hond);
        spFuelHond.setSpec(spFuel);
        spFuelHond.setLng2(fuel.getIid());
        spFuelHond.setStr2(fuel.getNme());
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spFuelHond.setLng1(chsGasoline.getIid());
          spFuelHond.setStr1(chsGasoline.getNme());
        } else {
          spFuelHond.setLng1(chsDiesel.getIid());
          spFuelHond.setStr1(chsDiesel.getNme());
        }
        orm.insert(this.rvs, vs, spFuelHond);
        ItmSpf spBodyHond = new ItmSpf();
        spBodyHond.setItm(hond);
        spBodyHond.setSpec(spBody);
        spBodyHond.setLng2(body.getIid());
        spBodyHond.setStr2(body.getNme());
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spBodyHond.setLng1(chsSedan.getIid());
          spBodyHond.setStr1(chsSedan.getNme());
        } else {
          spBodyHond.setLng1(chsWagon.getIid());
          spBodyHond.setStr1(chsWagon.getNme());
        }
        orm.insert(this.rvs, vs, spBodyHond);
        ItmSpf spTrnHond = new ItmSpf();
        spTrnHond.setItm(hond);
        spTrnHond.setSpec(spTrn);
        spTrnHond.setLng2(trans.getIid());
        spTrnHond.setStr2(trans.getNme());
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spTrnHond.setLng1(chsAT.getIid());
          spTrnHond.setStr1(chsAT.getNme());
        } else {
          spTrnHond.setLng1(chsMT.getIid());
          spTrnHond.setStr1(chsMT.getNme());
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
        I18Itm i18Ford = new I18Itm();
        i18Ford.setHasNm(ford);
        i18Ford.setLng(ru);
        i18Ford.setNme("Форд#" + i);
        orm.insert(this.rvs, vs, i18Ford);
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
        spManufFord.setLng1(chsFord.getIid());
        spManufFord.setStr1(chsFord.getNme());
        spManufFord.setLng2(manuf.getIid());
        spManufFord.setStr2(manuf.getNme());
        orm.insert(this.rvs, vs, spManufFord);
        ItmSpf spClrFord = new ItmSpf();
        spClrFord.setItm(ford);
        spClrFord.setSpec(spClr);
        spClrFord.setLng2(color.getIid());
        spClrFord.setStr2(color.getNme());
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spClrFord.setLng1(chsRed.getIid());
          spClrFord.setStr1(chsRed.getNme());
        } else {
          spClrFord.setLng1(chsWhite.getIid());
          spClrFord.setStr1(chsWhite.getNme());
        }
        orm.insert(this.rvs, vs, spClrFord);
        ItmSpf spFuelFord = new ItmSpf();
        spFuelFord.setItm(ford);
        spFuelFord.setSpec(spFuel);
        spFuelFord.setLng2(fuel.getIid());
        spFuelFord.setStr2(fuel.getNme());
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spFuelFord.setLng1(chsGasoline.getIid());
          spFuelFord.setStr1(chsGasoline.getNme());
        } else {
          spFuelFord.setLng1(chsDiesel.getIid());
          spFuelFord.setStr1(chsDiesel.getNme());
        }
        orm.insert(this.rvs, vs, spFuelFord);
        ItmSpf spBodyFord = new ItmSpf();
        spBodyFord.setItm(ford);
        spBodyFord.setSpec(spBody);
        spBodyFord.setLng2(body.getIid());
        spBodyFord.setStr2(body.getNme());
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spBodyFord.setLng1(chsSedan.getIid());
          spBodyFord.setStr1(chsSedan.getNme());
        } else {
          spBodyFord.setLng1(chsWagon.getIid());
          spBodyFord.setStr1(chsWagon.getNme());
        }
        orm.insert(this.rvs, vs, spBodyFord);
        ItmSpf spTrnFord = new ItmSpf();
        spTrnFord.setItm(ford);
        spTrnFord.setSpec(spTrn);
        spTrnFord.setLng2(trans.getIid());
        spTrnFord.setStr2(trans.getNme());
        if ((i + Math.round(Math.random() * 2.0)) % 2 == 0) {
          spTrnFord.setLng1(chsAT.getIid());
          spTrnFord.setStr1(chsAT.getNme());
        } else {
          spTrnFord.setLng1(chsMT.getIid());
          spTrnFord.setStr1(chsMT.getNme());
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
      log.info(this.rvs, getClass(), "Populated records count: " + pRc);
    } catch (Exception ex) {
      ex.printStackTrace();
      if (!rdb.getAcmt()) {
        rdb.rollBack();
      }
      this.fctApp.release(this.rvs);
      throw new Exception(ex);
    } finally {
      log.info(this.rvs, getClass(), "Generating data. Close RDB...");
      rdb.release();
    }
    log.info(this.rvs, getClass(), "Releasing factory ...");
    this.fctApp.release(this.rvs);
    log.info(this.rvs, getClass(), "The end");
  }
}
