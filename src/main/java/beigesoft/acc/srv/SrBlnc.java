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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import java.math.BigDecimal;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.EPeriod;
import org.beigesoft.mdl.IRecSet;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Acnt;
import org.beigesoft.acc.mdlp.Blnc;
import org.beigesoft.acc.mdlp.BlnCh;
import org.beigesoft.acc.mdl.TrBlLn;
import org.beigesoft.log.ILog;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;


/**
 * <p>Service that maintenance Blnc
 * and implements dirty check for all account.
 * If balance for account at given date is NULL then
 * it will be no record Blnc, this is cheap approach.
 * All work include recalculation all balances is executed
 * in single transaction. It works if there is any entry.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrBlnc<RS> implements ISrBlnc {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Acc-ssttings service.</p>
   **/
  private ISrAcStg srAcStg;

  /**
   * <p>Query balance for all accounts.</p>
   **/
  private String quBlnc;

  /**
   * <p>Initialized date constant.</p>
   **/
  private final Date iniDt = new Date(BlnCh.INITDT);

  /**
   * <p>Balance info for mantain.</p>
   **/
  private BlnCh blnCh;

  /**
   * <p>Retrieve Trial Balance report, it should be invoked recalIfNd
   * before it.</p>
   * @param pRvs Request scoped variables
   * @param pDt date
   * @return balance lines
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized List<TrBlLn> retTrBlnc(
    final Map<String, Object> pRvs, final Date pDt) throws Exception {
    List<TrBlLn> rz = new ArrayList<TrBlLn>();
    Map<String, Object> vs = new HashMap<String, Object>();
    lazBlnCh(pRvs, vs);
    if (this.blnCh.getStDt().getTime() != BlnCh.INITDT) {
      String query = evQuBlnc(pRvs, pDt);
      IRecSet<RS> rs = null;
      try {
        rs = getRdb().retRs(query);
        AcStg as = getSrAcStg().lazAcStg(pRvs);
        if (rs.first()) {
          do {
            String acNm = rs.getStr("ACNM");
            String acNb = rs.getStr("ACNB");
            String saNm = rs.getStr("SANM");
            Double debt = rs.getDouble("DEBT");
            Double cred = rs.getDouble("CRED");
            if (debt != 0 || cred != 0) {
              TrBlLn tbl = new TrBlLn();
              tbl.setAcNm(acNm);
              tbl.setAcNb(acNb);
              tbl.setSaNm(saNm);
              tbl.setDebt(BigDecimal.valueOf(debt)
                .setScale(as.getRpDp(), as.getRndm()));
              tbl.setCred(BigDecimal.valueOf(cred)
                .setScale(as.getRpDp(), as.getRndm()));
              if (tbl.getDebt().compareTo(BigDecimal.ZERO) != 0
                || tbl.getCred().compareTo(BigDecimal.ZERO) != 0) {
                rz.add(tbl);
              }
            }
          } while (rs.next());
        }
      } finally {
        if (rs != null) {
          rs.close();
        }
      }
      //account totals:
      BigDecimal debitAcc = BigDecimal.ZERO;
      BigDecimal creditAcc = BigDecimal.ZERO;
      String accCurr = null;
      int lineCurr = 0;
      int lineStartAcc = 0;
      for (TrBlLn tbl : rz) {
        if (!tbl.getAcNb().equals(accCurr)) {
          //save to old
          if (accCurr != null) {
            for (int j = lineStartAcc; j < lineCurr; j++) {
              rz.get(j).setDebtAcc(debitAcc);
              rz.get(j).setCredAcc(creditAcc);
            }
          }
          //init new acc:
          lineStartAcc = lineCurr;
          accCurr = tbl.getAcNb();
        }
        debitAcc = debitAcc.add(tbl.getDebt());
        creditAcc = creditAcc.add(tbl.getCred());
        lineCurr++;
      }
    }
    return rz;
  }

  /**
   * <p>Recalculate if need for all balances for all dates less
   * or equals pDtFor, this method is always invoked by reports.</p>
   * @param pRvs Request scoped variables
   * @param pDtFor date for
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void recalcIfNd(final Map<String, Object> pRvs,
    final Date pDtFor) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    //lazy get start data, order is essential:
    lazBlnCh(pRvs, vs);
    evBlStPer(pRvs, vs);
    if (evBlStDt(pRvs, vs).getTime() == BlnCh.INITDT) {
      return;
    }
    Date dtSt = evDtStPer(pRvs, pDtFor);
    Calendar calStBl = Calendar.getInstance(new Locale("en", "US"));
    calStBl.setTime(this.blnCh.getStDt());
    calStBl.set(Calendar.MONTH, 0);
    calStBl.set(Calendar.DAY_OF_MONTH, 1);
    calStBl.set(Calendar.HOUR_OF_DAY, 0);
    calStBl.set(Calendar.MINUTE, 0);
    calStBl.set(Calendar.SECOND, 0);
    calStBl.set(Calendar.MILLISECOND, 0);
    if (this.blnCh.getLeDt().getTime() < this.blnCh.getStDt().getTime()) {
      //there is new entry with date less than balance start
      this.blnCh.setStDt(iniDt);
      this.blnCh.setLeDt(iniDt);
      if (evBlStDt(pRvs, vs).getTime() == BlnCh.INITDT) {
        return;
      }
      recalc(pRvs, vs, pDtFor);
    } else if (this.blnCh.getPrCh() //period changed
      || dtSt.getTime() > this.blnCh.getCuDt().getTime() //new period
        //old period dirty:
        || this.blnCh.getLeDt().getTime() < this.blnCh.getCuDt().getTime()) {
      recalc(pRvs, vs, pDtFor);
    }
  }

  /**
   * <p>Handle new accounting entry to check dirty of stored balances.</p>
   * @param pRvs Request scoped variables
   * @param pDtAt date at
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void hndNewEntr(final Map<String, Object> pRvs,
    final Date pDtAt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    if (lazBlnCh(pRvs, vs).getLeDt().getTime() > pDtAt.getTime()) {
      boolean isDbgSh = getLog().getDbgSh(this.getClass())
        && getLog().getDbgFl() < 14001 && getLog().getDbgCl() > 13999;
      if (isDbgSh) {
        getLog().debug(pRvs, SrBlnc.class, "change least last entry date from "
          + this.blnCh.getLeDt() + " to " + pDtAt);
      }
      this.blnCh.setLeDt(pDtAt);
      getOrm().update(pRvs, vs, this.blnCh);
    }
  }

  /**
   * <p>Evaluate start of period nearest to pDtFor.
   * Tested in blc org.beigesoft.test.CalendarTest.</p>
   * @param pRvs Request scoped variables
   * @param pDtFor date for
   * @return Start of period nearest to pDtFor
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized Date evDtStPer(final Map<String, Object> pRvs,
    final Date pDtFor) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    lazBlnCh(pRvs, vs);
    EPeriod per = evBlStPer(pRvs, vs);
    if (!(per.equals(EPeriod.MONTHLY) || per.equals(EPeriod.WEEKLY)
      || per.equals(EPeriod.DAILY))) {
      throw new ExcCode(ExcCode.WRPR, "stored_balance_period_must_be_dwm");
    }
    Calendar cal = Calendar.getInstance(new Locale("en", "US"));
    cal.setTime(pDtFor);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0); //Daily is ready
    if (per.equals(EPeriod.MONTHLY)) {
      cal.set(Calendar.DAY_OF_MONTH, 1);
    } else if (per.equals(EPeriod.WEEKLY)) {
      cal.set(Calendar.DAY_OF_WEEK, 1);
    }
    return cal.getTime();
  }

  //Utils:
  /**
   * <p>Forced recalculation all balances for all dates less
   * or equals pDtFor. If balance for account at given date is NULL then
   * it will be no recorded into Blnc, this is cheap approach.</p>
   * @param pRvs Request scoped variables
   * @param pVs Invoker scoped variables
   * @param pDtFor date for
   * @throws Exception - an exception
   **/
  public final synchronized void recalc(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final Date pDtFor) throws Exception {
    getLog().info(pRvs, SrBlnc.class, "recalculation start BlnCh was "
      + this.blnCh);
    if (this.blnCh.getPrCh()) {
      getLog().info(pRvs, SrBlnc.class,
        "deleting all stored balances cause period has changed");
      getRdb().delete(Blnc.class.getSimpleName().toUpperCase(), null);
      this.blnCh.setPrCh(false);
      this.blnCh.setCuDt(this.iniDt);
    }
    Date curDt;
    if (this.blnCh.getPrCh()) {
      //recalculate from start;
      curDt = evDtNxtPerSt(pRvs, pVs, this.blnCh.getStDt());
      getLog().info(pRvs, SrBlnc.class, "recalculating balances from start "
        + curDt + " <- " + this.blnCh.getStDt());
   } else if (this.blnCh.getLeDt().getTime() < this.blnCh.getCuDt().getTime()) {
      //recalculate from previous to changes period;
      curDt = evDtPrvPerSt(pRvs, pVs, this.blnCh.getLeDt());
      if (curDt.getTime() <= this.blnCh.getStDt().getTime()) {
        //recalculate from start;
        curDt = evDtNxtPerSt(pRvs, pVs, this.blnCh.getStDt());
        getLog().info(pRvs, SrBlnc.class, "recalculating balances from start "
          + curDt + " <- " + this.blnCh.getStDt());
      } else {
        getLog().info(pRvs, SrBlnc.class,
          "recalculating balances from previous " + curDt);
      }
    } else {
      //recalculate from current end;
      curDt = evDtNxtPerSt(pRvs, pVs, this.blnCh.getCuDt());
      getLog().info(pRvs, SrBlnc.class, "recalc balances from current end "
        + curDt + " <- " + this.blnCh.getCuDt());
    }
    Date lstBlStDt;
    do {
      lstBlStDt = curDt;
      String query = evQuBlnc(pRvs, new Date(lstBlStDt.getTime() - 1));
      List<TrBlLn> tbls = retBlnLnsToSv(query);
      for (TrBlLn tbl : tbls) {
        String saWhe;
        if (tbl.getSaId() == null) {
          saWhe = " and SAID is null and BLNC.SATY is null";
        } else {
          saWhe = " and SAID=" + tbl.getSaId() + " and BLNC.SATY="
            + tbl.getSaTy();
        }
        Blnc blnc = getOrm().retEntCnd(pRvs, pVs, Blnc.class, "where ACC='"
          + tbl.getAcId() + "' and DAT=" + lstBlStDt.getTime() + saWhe);
        if (blnc == null) {
          blnc = new Blnc();
          blnc.setIsNew(true);
        }
        blnc.setDat(lstBlStDt);
        Acnt acc = new Acnt();
        acc.setIid(tbl.getAcId());
        blnc.setAcc(acc);
        if (tbl.getDebt().compareTo(BigDecimal.ZERO) != 0) {
          blnc.setBln(tbl.getDebt());
        } else {
          blnc.setBln(tbl.getCred());
        }
        blnc.setSaTy(tbl.getSaTy());
        blnc.setSaId(tbl.getSaId());
        blnc.setSaNm(tbl.getSaNm());
        if (blnc.getIsNew()) {
          getOrm().insIdLn(pRvs, pVs, blnc);
          blnc.setIsNew(false);
        } else {
          getOrm().update(pRvs, pVs, blnc);
        }
      }
      curDt = evDtNxtPerSt(pRvs, pVs, curDt);
    } while (curDt.getTime() <= pDtFor.getTime());
      getLog().info(pRvs, SrBlnc.class, "last stored balance curDt "
        + lstBlStDt + ", curDt for " + pDtFor);
    this.blnCh.setCuDt(lstBlStDt);
    this.blnCh.setLeDt(this.blnCh.getCuDt());
    getOrm().update(pRvs, pVs, this.blnCh);
    getLog().info(pRvs, SrBlnc.class, "recalculation end BlnCh is "
      + this.blnCh);
  }

  /**
   * <p>Evaluate period of stored balances according settings,
   * if it's changed then it switch on "current balances are dirty".</p>
   * @param pRvs Request scoped variables
   * @param pVs Invoker scoped variables
   * @return pPer EPeriod e.g. MONTHLY
   * @throws Exception - an exception
   **/
  public final synchronized EPeriod evBlStPer(final Map<String, Object> pRvs,
    final Map<String, Object> pVs) throws Exception {
    AcStg as = getSrAcStg().lazAcStg(pRvs);
    if (!this.blnCh.getStPr().equals(as.getBlPr())) {
      getLog().info(pRvs, SrBlnc.class, "changing period from " + this.blnCh
        .getStPr() + " to " + as.getBlPr());
      this.blnCh.setStPr(as.getBlPr());
      this.blnCh.setPrCh(true);
      this.blnCh.setLeDt(iniDt);
      getOrm().update(pRvs, pVs, this.blnCh);
    }
    return this.blnCh.getStPr();
  }

  /**
   * <p>Evaluate date start of stored balances according settings,
   * this is the first month of the first accounting entry.
   * It also may change date start in BlnCh.</p>
   * @param pRvs Request scoped variables
   * @param pVs Invoker scoped variables
   * @return Date
   * @throws Exception - an exception
   **/
  public final synchronized Date evBlStDt(final Map<String, Object> pRvs,
    final Map<String, Object> pVs) throws Exception {
    if (this.blnCh.getStDt().getTime() == BlnCh.INITDT) {
      //any situation:
      //1.the first invocation
      //2.there is new entry with date less then current start was
      Long dtFsEnrLn = this.rdb.evLong(
        "select min(DAT) as MINDT from ENTR where RVID is null;", "MINDT");
      if (dtFsEnrLn != null) {
        this.blnCh.setStDt(evDtStPer(pRvs, new Date(dtFsEnrLn)));
        getOrm().update(pRvs, pVs, this.blnCh);
      } else if (this.blnCh.getCuDt().getTime() != BlnCh.INITDT) {
        //there is Blnc without any non-reversed entry
        getLog().error(pRvs, getClass(), "Dirty balances!");
        getLog().info(pRvs, SrBlnc.class,
          "deleting all stored balances cause where is no entries");
        getRdb().delete(Blnc.class.getSimpleName().toUpperCase(), null);
        this.blnCh.setPrCh(false);
        this.blnCh.setStDt(this.iniDt);
        this.blnCh.setLeDt(this.iniDt);
        this.blnCh.setCuDt(this.iniDt);
        getOrm().update(pRvs, pVs, this.blnCh);
      }
    }
    return this.blnCh.getStDt();
  }

  /**
   * <p>Evaluate Trial Balance query.</p>
   * @param pRvs Request scoped variables
   * @param pDt date of balance
   * @return query of balance
   * @throws Exception - an exception
   **/
  public final synchronized String evQuBlnc(final Map<String, Object> pRvs,
    final Date pDt) throws Exception {
    if (this.quBlnc == null) {
      String flName = "/acc/blnc.sql";
      this.quBlnc = loadStr(flName);
    }
    String query = quBlnc.replace(":DT1",
      String.valueOf(evDtStPer(pRvs, pDt).getTime()));
    query = query.replace(":DT2", String.valueOf(pDt.getTime()));
    return query;
  }

  /**
   * <p>Retrieve Trial Balance lines with given query to save into DB.</p>
   * @param pQu date
   * @return balance lines
   * @throws Exception - an exception
   **/
  public final synchronized List<TrBlLn> retBlnLnsToSv(
    final String pQu) throws Exception {
    List<TrBlLn> rz = new ArrayList<TrBlLn>();
    IRecSet<RS> rs = null;
    try {
      rs = getRdb().retRs(pQu);
      if (rs.first()) {
        do {
          String acId = rs.getStr("ACID");
          String acNb = rs.getStr("ACNB");
          String acNm = rs.getStr("ACNM");
          Long saId = rs.getLong("SAID");
          Integer saTy = rs.getInt("SATY");
          String saNm = rs.getStr("SANM");
          Double debt = rs.getDouble("DEBT");
          Double cred = rs.getDouble("CRED");
          TrBlLn tbl = new TrBlLn();
          tbl.setAcId(acId);
          tbl.setAcNm(acNm);
          tbl.setAcNb(acNb);
          tbl.setSaId(saId);
          tbl.setSaTy(saTy);
          tbl.setSaNm(saNm);
          tbl.setDebt(BigDecimal.valueOf(debt));
          tbl.setCred(BigDecimal.valueOf(cred));
          rz.add(tbl);
        } while (rs.next());
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    return rz;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFlNm file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final synchronized String loadStr(
    final String pFlNm) throws IOException {
    URL urlFile = SrBlnc.class.getResource(pFlNm);
    if (urlFile != null) {
      InputStream is = null;
      try {
        is = SrBlnc.class.getResourceAsStream(pFlNm);
        byte[] bArray = new byte[is.available()];
        is.read(bArray, 0, is.available());
        return new String(bArray, "UTF-8");
      } finally {
        if (is != null) {
          is.close();
        }
      }
    }
    return null;
  }

  /**
   * <p>Lazy getter for blnCh.</p>
   * @param pRvs Request scoped variables
   * @param pVs Invoker scoped variables
   * @return BlnCh
   * @throws Exception - an exception
   **/
  public final synchronized BlnCh lazBlnCh(final Map<String, Object> pRvs,
    final Map<String, Object> pVs) throws Exception {
    if (this.blnCh == null) {
      BlnCh balLoc = new BlnCh();
      balLoc.setIid(1L);
      getOrm().refrEnt(pRvs, pVs, balLoc);
      if (balLoc.getIid() == null) {
        balLoc.setIid(1L);
        getOrm().insIdLn(pRvs, pVs, balLoc);
        balLoc.setIsNew(false);
      }
      this.blnCh = balLoc;
    }
    return this.blnCh;
  }

  /**
   * <p>Evaluate date start of next balance store period.
   * Tested in blc org.beigesoft.test.CalendarTest.</p>
   * @param pRvs Request scoped variables
   * @param pVs Invoker scoped variables
   * @param pDtFor date for
   * @return Start of next period nearest to pDtFor
   * @throws Exception - an exception
   **/
  public final synchronized Date evDtNxtPerSt(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final Date pDtFor) throws Exception {
    EPeriod per = evBlStPer(pRvs, pVs);
    if (!(per.equals(EPeriod.MONTHLY) || per.equals(EPeriod.WEEKLY)
      || per.equals(EPeriod.DAILY))) {
      throw new ExcCode(ExcCode.WRPR, "stored_balance_period_must_be_dwm");
    }
    Calendar cal = Calendar.getInstance(new Locale("en", "US"));
    cal.setTime(pDtFor);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    if (per.equals(EPeriod.DAILY)) {
      cal.add(Calendar.DATE, 1);
    } else if (per.equals(EPeriod.MONTHLY)) {
      cal.add(Calendar.MONTH, 1);
      cal.set(Calendar.DAY_OF_MONTH, 1);
    } else if (per.equals(EPeriod.WEEKLY)) {
      cal.add(Calendar.DAY_OF_YEAR, 7);
      cal.set(Calendar.DAY_OF_WEEK, 1);
    }
    return cal.getTime();
  }

  /**
   * <p>Evaluate date start of previous balance store period.
   * Tested in blc org.beigesoft.test.CalendarTest.</p>
   * @param pRvs Request scoped variables
   * @param pVs Invoker scoped variables
   * @param pDtFor date for
   * @return Start of next period nearest to pDtFor
   * @throws Exception - an exception
   **/
  public final synchronized Date evDtPrvPerSt(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final Date pDtFor) throws Exception {
    EPeriod per = evBlStPer(pRvs, pVs);
    if (!(per.equals(EPeriod.MONTHLY) || per.equals(EPeriod.WEEKLY)
      || per.equals(EPeriod.DAILY))) {
      throw new ExcCode(ExcCode.WRPR, "stored_balance_period_must_be_dwm");
    }
    Calendar cal = Calendar.getInstance(new Locale("en", "US"));
    cal.setTime(pDtFor);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    if (per.equals(EPeriod.DAILY)) {
      cal.add(Calendar.DATE, -1);
    } else if (per.equals(EPeriod.MONTHLY)) {
      cal.add(Calendar.MONTH, -1);
      cal.set(Calendar.DAY_OF_MONTH, 1);
    } else if (per.equals(EPeriod.WEEKLY)) {
      cal.add(Calendar.DAY_OF_YEAR, -7);
      cal.set(Calendar.DAY_OF_WEEK, 1);
    }
    return cal.getTime();
  }


  //Simple getters and setters:
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

  /**
   * <p>Geter for rdb.</p>
   * @return IRdb
   **/
  public final synchronized IRdb<RS> getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final synchronized void setRdb(
    final IRdb<RS> pRdb) {
    this.rdb = pRdb;
  }

  /**
   * <p>Getter for quBlnc.</p>
   * @return String
   **/
  public final synchronized String getQuBlnc() {
    return this.quBlnc;
  }

  /**
   * <p>Setter for quBlnc.</p>
   * @param pQuBlnc reference
   **/
  public final synchronized void setQuBlnc(final String pQuBlnc) {
    this.quBlnc = pQuBlnc;
  }

  /**
   * <p>Geter for log.</p>
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
   * <p>Getter for srAcStg.</p>
   * @return ISrAcStg
   **/
  public final synchronized ISrAcStg getSrAcStg() {
    return this.srAcStg;
  }

  /**
   * <p>Setter for srAcStg.</p>
   * @param pSrAcStg reference
   **/
  public final synchronized void setSrAcStg(final ISrAcStg pSrAcStg) {
    this.srAcStg = pSrAcStg;
  }

  /**
   * <p>Getter for blnCh.</p>
   * @return BlnCh
   **/
  public final synchronized BlnCh getBlnCh() {
    return this.blnCh;
  }

  /**
   * <p>Setter for blnCh.</p>
   * @param pBlnCh reference
   **/
  public final synchronized void setBlnCh(final BlnCh pBlnCh) {
    this.blnCh = pBlnCh;
  }
}
