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
import java.util.HashMap;
import java.util.Date;
import java.math.BigDecimal;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.beigesoft.mdl.IRecSet;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.srv.ISrvDt;
import org.beigesoft.prc.IPrc;
import org.beigesoft.acc.mdl.LdgPrv;
import org.beigesoft.acc.mdl.LdgPrvLn;
import org.beigesoft.acc.mdl.LdgDe;
import org.beigesoft.acc.mdl.LdgDeLn;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.srv.ISrBlnc;

/**
 * <p>Transactional service that retrieves ledger and
 * puts it into request vars.</p>
 *
 * @author Yury Demidenko
 * @param <RS> platform dependent record set type
 */
public class PrLdgr<RS> implements IPrc {

  /**
   * <p>RDB service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Transaction isolation.</p>
   **/
  private Integer trIsl;

  /**
   * <p>Date service.</p>
   **/
  private ISrvDt srvDt;

  /**
   * <p>Balance data retriever.</p>
   **/
  private ISrBlnc srBlnc;

  /**
   * <p>Query detail.</p>
   **/
  private String quDet;

  /**
   * <p>Query previous.</p>
   **/
  private String quPrv;

  /**
   * <p>Process request.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void process(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    LdgPrv ldgPrv = new LdgPrv();
    LdgDe ldgDe = new LdgDe();
    Date dt1 = this.srvDt.from8601DateTime(pRqDt.getParam("dt1"));
    Date dt2 = this.srvDt.from8601DateTime(pRqDt.getParam("dt2"));
    pRvs.put("dt1", dt1);
    pRvs.put("dt2", dt2);
    pRvs.put("ldgPrv", ldgPrv);
    pRvs.put("ldgDe", ldgDe);
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      mkPrv(pRvs, pRqDt);
      mkDet(pRvs, pRqDt);
      pRqDt.setAttr("rnd", "ldgr");
      this.rdb.commit();
    } catch (Exception ex) {
      this.srBlnc.hndRlBk(pRvs);
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw ex;
    } finally {
      this.rdb.release();
    }
  }

  /**
   * <p>Makes previous data.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  public final synchronized void mkPrv(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    LdgPrv ldgPrv = (LdgPrv) pRvs.get("ldgPrv");
    String accId = pRqDt.getParam("accId");
    String saId = pRqDt.getParam("saId");
    Date dt1 = (Date) pRvs.get("dt1");
    Date dtbln = this.srBlnc.evDtPrvPerSt(pRvs, vs, dt1);
    AcStg as = (AcStg) pRvs.get("astg");
    String qu = lazQuPrv().replace(":ACCID", accId);
    qu = qu.replace(":DT1", String.valueOf(dt1.getTime()));
    qu = qu.replace(":DTBLN", String.valueOf(dtbln.getTime()));
    if (saId != null && !"".equals(saId)) {
      qu = qu.replace(":SUBACC", " and SAID=" + saId);
      qu = qu.replace(":SADNM", " and SADID=" + saId);
      qu = qu.replace(":SACNM", " and SACID=" + saId);
    } else {
      qu = qu.replace(":SUBACC", "");
      qu = qu.replace(":SADNM", "");
      qu = qu.replace(":SACNM", "");
    }
    IRecSet<RS> rs = null;
    Integer blTy = null;
    try {
      rs = getRdb().retRs(qu);
      if (rs.first()) {
        do {
          if (blTy == null) {
            blTy = rs.getInt("BLTY");
          }
          String subacc = rs.getStr("SUBACC");
          LdgPrvLn ldgPrvLn = new LdgPrvLn();
          if (subacc != null) {
            ldgPrv.getLnsMp().put(subacc, ldgPrvLn);
          }
          Double debt = rs.getDouble("DEBT");
          Double cred = rs.getDouble("CRED");
          ldgPrvLn.setDebt(BigDecimal.valueOf(debt)
            .setScale(as.getRpDp(), as.getRndm()));
          ldgPrvLn.setCred(BigDecimal.valueOf(cred)
            .setScale(as.getRpDp(), as.getRndm()));
          if (blTy == 0) {
            ldgPrvLn.setBlnc(ldgPrvLn.getDebt()
              .subtract(ldgPrvLn.getCred()));
          } else {
            ldgPrvLn.setBlnc(ldgPrvLn.getCred()
              .subtract(ldgPrvLn.getDebt()));
          }
          ldgPrv.setDebitAcc(ldgPrv.getDebitAcc()
            .add(ldgPrvLn.getDebt()));
          ldgPrv.setCreditAcc(ldgPrv.getCreditAcc()
            .add(ldgPrvLn.getCred()));
          ldgPrv.setBalanceAcc(ldgPrv.getBalanceAcc()
            .add(ldgPrvLn.getBlnc()));
        } while (rs.next());
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
  }

  /**
   * <p>Makes detail data.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  public final synchronized void mkDet(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    LdgPrv ldgPrv = (LdgPrv) pRvs.get("ldgPrv");
    LdgDe ldgDe = (LdgDe) pRvs.get("ldgDe");
    String accId = pRqDt.getParam("accId");
    String saId = pRqDt.getParam("saId");
    Date dt1 = (Date) pRvs.get("dt1");
    Date dt2 = (Date) pRvs.get("dt2");
    AcStg as = (AcStg) pRvs.get("astg");
    ldgDe.setDebitAcc(ldgPrv.getDebitAcc());
    ldgDe.setCreditAcc(ldgPrv.getCreditAcc());
    ldgDe.setBalanceAcc(ldgPrv.getBalanceAcc());
    for (Map.Entry<String, LdgPrvLn> enr : ldgPrv.getLnsMp().entrySet()) {
      ldgDe.getSaDbTo().put(enr.getKey(), enr.getValue().getDebt());
      ldgDe.getSaCrTo().put(enr.getKey(), enr.getValue().getCred());
      ldgDe.getSaBlnTo().put(enr.getKey(), enr.getValue().getBlnc());
    }
    String qu = lazQuDet().replace(":ACCID", accId);
    qu = qu.replace(":DT1", String.valueOf(dt1.getTime()));
    qu = qu.replace(":DT2", String.valueOf(dt2.getTime()));
    if (saId != null && !"".equals(saId)) {
      qu = qu.replace(":SADNM", " and SADID=" + saId);
      qu = qu.replace(":SACNM", " and SACID=" + saId);
    } else {
      qu = qu.replace(":SADNM", "");
      qu = qu.replace(":SACNM", "");
    }
    IRecSet<RS> rs = null;
    try {
      rs = getRdb().retRs(qu);
      Integer blTy = null;
      if (rs.first()) {
        do {
          if (blTy == null) {
            blTy = rs.getInt("BLTY");
          }
          LdgDeLn ldgDeLn = new LdgDeLn();
          ldgDe.getLns().add(ldgDeLn);
          String subacc = rs.getStr("SUBACC");
          if (subacc != null) {
            ldgDeLn.setSubacc(subacc);
            if (ldgDe.getSaDbTo().get(subacc) == null) {
              ldgDe.getSaDbTo().put(subacc, BigDecimal.ZERO);
            }
            if (ldgDe.getSaCrTo().get(subacc) == null) {
              ldgDe.getSaCrTo().put(subacc, BigDecimal.ZERO);
            }
            if (ldgDe.getSaBlnTo().get(subacc) == null) {
              ldgDe.getSaBlnTo().put(subacc, BigDecimal.ZERO);
            }
          }
          ldgDeLn.setDat(new Date(rs.getLong("DAT")));
          ldgDeLn.setCrAcNm(rs.getStr("CRACNM"));
          ldgDeLn.setCrAcNmb(rs.getStr("CRACNMB"));
          ldgDeLn.setCrSaNm(rs.getStr("CRSAC"));
          ldgDeLn.setDscr(rs.getStr("DSCR"));
          Double tot = rs.getDouble("TOT");
          Integer isDebt = rs.getInt("ISDEBT");
          if (isDebt == 1) {
            ldgDeLn.setDebt(BigDecimal.valueOf(tot)
              .setScale(as.getRpDp(), as.getRndm()));
            if (blTy == 0) {
              ldgDeLn.setBlnc(ldgDe.getBalanceAcc()
                .add(ldgDeLn.getDebt()));
            } else {
              ldgDeLn.setBlnc(ldgDe.getBalanceAcc()
                .subtract(ldgDeLn.getDebt()));
            }
            if (subacc != null) {
              if (blTy == 0) {
                ldgDeLn.setBlncSa(ldgDe.getSaBlnTo().get(subacc)
                  .add(ldgDeLn.getDebt()));
              } else {
                ldgDeLn.setBlncSa(ldgDe.getSaBlnTo().get(subacc)
                  .subtract(ldgDeLn.getDebt()));
              }
              ldgDe.getSaBlnTo().put(subacc, ldgDeLn.getBlncSa());
              ldgDe.getSaDbTo().put(subacc, ldgDe.getSaDbTo().get(subacc)
                .add(ldgDeLn.getDebt()));
            }
          } else {
            ldgDeLn.setCred(BigDecimal.valueOf(tot)
              .setScale(as.getRpDp(), as.getRndm()));
            if (blTy == 0) {
              ldgDeLn.setBlnc(ldgDe.getBalanceAcc()
                .subtract(ldgDeLn.getCred()));
            } else {
              ldgDeLn.setBlnc(ldgDe.getBalanceAcc()
                .add(ldgDeLn.getCred()));
            }
            if (subacc != null) {
              if (blTy == 0) {
                ldgDeLn.setBlncSa(ldgDe.getSaBlnTo().get(subacc)
                  .subtract(ldgDeLn.getCred()));
              } else {
                ldgDeLn.setBlncSa(ldgDe.getSaBlnTo().get(subacc)
                  .add(ldgDeLn.getCred()));
              }
              ldgDe.getSaBlnTo().put(subacc, ldgDeLn.getBlncSa());
              ldgDe.getSaCrTo().put(subacc, ldgDe.getSaCrTo().get(subacc)
                .add(ldgDeLn.getCred()));
            }
          }
          ldgDe.setDebitAcc(ldgDe.getDebitAcc().add(ldgDeLn.getDebt()));
          ldgDe.setCreditAcc(ldgDe.getCreditAcc().add(ldgDeLn.getCred()));
          if (blTy == 0) {
            ldgDe.setBalanceAcc(ldgDe.getBalanceAcc().add(ldgDeLn.getDebt())
              .subtract(ldgDeLn.getCred()));
          } else {
            ldgDe.setBalanceAcc(ldgDe.getBalanceAcc()
              .subtract(ldgDeLn.getDebt()).add(ldgDeLn.getCred()));
          }
        } while (rs.next());
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
  }

  /**
   * <p>Lazy gets query detail.</p>
   * @return SQL query
   * @throws IOException - IO exception
   **/
  public final synchronized String lazQuDet() throws IOException {
    if (this.quDet == null) {
      this.quDet = loadStr("/acc/ldgDet.sql");
    }
    return this.quDet;
  }

  /**
   * <p>Lazy gets query previous.</p>
   * @return SQL query
   * @throws IOException - IO exception
   **/
  public final synchronized String lazQuPrv() throws IOException {
    if (this.quPrv == null) {
      this.quPrv = loadStr("/acc/ldgPrv.sql");
    }
    return this.quPrv;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFlNm file name
   * @return SQL query, not null
   * @throws IOException - IO exception
   **/
  public final synchronized String loadStr(
    final String pFlNm) throws IOException {
    URL urlFile = PrLdgr.class.getResource(pFlNm);
    if (urlFile != null) {
      InputStream is = null;
      try {
        is = PrLdgr.class.getResourceAsStream(pFlNm);
        byte[] bArray = new byte[is.available()];
        is.read(bArray, 0, is.available());
        return new String(bArray, "UTF-8");
      } finally {
        if (is != null) {
          is.close();
        }
      }
    }
    throw new RuntimeException("File not found: " + pFlNm);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for rdb.</p>
   * @return IRdb
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
   * <p>Getter for trIsl.</p>
   * @return Integer
   **/
  public final synchronized Integer getTrIsl() {
    return this.trIsl;
  }

  /**
   * <p>Setter for trIsl.</p>
   * @param pTrIsl reference
   **/
  public final synchronized void setTrIsl(final Integer pTrIsl) {
    this.trIsl = pTrIsl;
  }

  /**
   * <p>Getter for srvDt.</p>
   * @return ISrvDt
   **/
  public final synchronized ISrvDt getSrvDt() {
    return this.srvDt;
  }

  /**
   * <p>Setter for srvDt.</p>
   * @param pSrvDt reference
   **/
  public final synchronized void setSrvDt(final ISrvDt pSrvDt) {
    this.srvDt = pSrvDt;
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
