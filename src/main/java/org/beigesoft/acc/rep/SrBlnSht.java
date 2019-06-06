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

package org.beigesoft.acc.rep;

import java.util.Map;
import java.util.Date;
import java.math.BigDecimal;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.beigesoft.mdl.IRecSet;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.acc.mdl.BlnSht;
import org.beigesoft.acc.mdl.BlnLn;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.srv.ISrAcStg;
import org.beigesoft.acc.srv.ISrBlnc;


/**
 * <p>Service that retrieves data for balance sheet.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrBlnSht<RS> implements ISrBlnSht {

  /**
   * <p>ORM service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrAcStg srAcStg;

  /**
   * <p>Balance service.</p>
   **/
  private ISrBlnc srBlnc;

  /**
   * <p>Query balance for all accounts.</p>
   **/
  private String quBlnc;

  /**
   * <p>Retrieves balance sheet.</p>
   * @param pRvs additional param
   * @param pDt date
   * @return balance sheet
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized BlnSht retBlnc(final Map<String, Object> pRvs,
    final Date pDt) throws Exception {
    synchronized (this.srBlnc) {
      this.srBlnc.chngSacsIfNd(pRvs);
      this.srBlnc.recalcIfNd(pRvs, pDt);
    }
    AcStg as = this.srAcStg.lazAcStg(pRvs);
    BlnSht rz = new BlnSht();
    rz.setDat(pDt);
    String query = evQuBlnc(pRvs, pDt);
    IRecSet<RS> rs = null;
    try {
      rs = getRdb().retRs(query);
      if (rs.first()) {
        do {
          String accName = rs.getStr("NME");
          Integer accType = rs.getInt("TYP");
          String accNumber = rs.getStr("NMBR");
          Double debit = rs.getDouble("DEBT");
          Double credit = rs.getDouble("CRED");
          if (debit != 0 || credit != 0) {
            BlnLn bl = new BlnLn();
            bl.setAccName(accName);
            bl.setAccNumber(accNumber);
            bl.setAccType(accType);
            bl.setDebit(BigDecimal.valueOf(debit).setScale(
              as.getRpDp(), as.getRndm()));
            bl.setCredit(BigDecimal.valueOf(credit).setScale(
              as.getRpDp(), as.getRndm()));
            if (bl.getDebit().doubleValue() != 0
              || bl.getCredit().doubleValue() != 0) {
              rz.getLns().add(bl);
              if (accType == 0) {
                rz.setToLnAs(rz.getToLnAs() + 1);
                rz.setTotAss(rz.getTotAss().add(bl.getDebit()
                  .subtract(bl.getCredit())));
              } else if (accType == 1) {
                rz.setToLnLi(rz.getToLnLi() + 1);
                rz.setTotLia(rz.getTotLia()
                  .add(bl.getCredit().subtract(bl.getDebit())));
              } else if (accType == 2) {
                rz.setToLnOe(rz.getToLnOe() + 1);
                rz.setTotOwe(rz.getTotOwe()
                  .add(bl.getCredit().subtract(bl.getDebit())));
              }
            }
          }
        } while (rs.next());
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    rz.setDetRc(Math.max(rz.getToLnAs(),
      rz.getToLnLi() + rz.getToLnOe() + 3)); //3 -total l, title e ,total e
    return rz;
  }

  /**
   * <p>Evaluate Balance query.</p>
   * @param pRvs additional param
   * @param pDt date of balance
   * @return query of balance
   * @throws Exception - an exception
   **/
  public final synchronized String evQuBlnc(final Map<String, Object> pRvs,
    final Date pDt) throws Exception {
    if (this.quBlnc == null) {
      String flName = "/acc/blnSht.sql";
      this.quBlnc = loadStr(flName);
    }
    String query = quBlnc.replace(":DT1", String
      .valueOf(getSrBlnc().evDtStPer(pRvs, pDt).getTime()));
    query = query.replace(":DT2", String.valueOf(pDt.getTime()));
    return query;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFlNm file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final synchronized String loadStr(
    final String pFlNm) throws IOException {
    URL urlFile = SrBlnSht.class.getResource(pFlNm);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = SrBlnSht.class.getResourceAsStream(pFlNm);
        byte[] bArray = new byte[inputStream.available()];
        inputStream.read(bArray, 0, inputStream.available());
        return new String(bArray, "UTF-8");
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }
      }
    }
    return null;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for rdb.</p>
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
   * <p>Geter for srAcStg.</p>
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
   * <p>Getter for srBlnc.</p>
   * @return ISrBlnc
   **/
  public final synchronized ISrBlnc getSrBlnc() {
    return this.srBlnc;
  }

  /**
   * <p>Setter for srBlnc.</p>
   * @param pSrBlnc reference
   **/
  public final synchronized void setSrBlnc(final ISrBlnc pSrBlnc) {
    this.srBlnc = pSrBlnc;
  }
}
