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
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IRecSet;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.srv.ISrvDt;
import org.beigesoft.prc.IPrc;
import org.beigesoft.acc.mdl.LdgDe;
import org.beigesoft.acc.mdl.LdgDeLn;
import org.beigesoft.acc.mdlp.AcStg;

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
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      LdgDe ldgDe = new LdgDe();
      Date dt1 = this.srvDt.from8601DateTime(pRqDt.getParam("dt1"));
      Date dt2 = this.srvDt.from8601DateTime(pRqDt.getParam("dt2"));
      String qd = lazQuDet().replace(":ACCID", pRqDt.getParam("accid"));
      qd = qd.replace(":DT1", String.valueOf(dt1.getTime()));
      qd = qd.replace(":DT2", String.valueOf(dt2.getTime()));
      String saId = pRqDt.getParam("saId");
      if (saId != null) {
        qd = qd.replace(":SADNM", " and SADID=" + saId);
        qd = qd.replace(":SACNM", " and SACID=" + saId);
      }
      IRecSet<RS> rs = null;
      AcStg as = (AcStg) pRvs.get("astg");
      try {
        rs = getRdb().retRs(qd);
        String accWas = null;
        if (rs.first()) {
          do {
            LdgDeLn ldgDeLn = new LdgDeLn();
            ldgDe.getLns().add(ldgDeLn);
            String acc = rs.getStr("ACC");
            if (acc.equals(accWas)) {
              accWas = acc;
            }
            Double tot = rs.getDouble("TOT");
            Integer isDebt = rs.getInt("ISDEBT");
            if (isDebt == 1) {
              ldgDeLn.setDebt(BigDecimal.valueOf(tot)
                .setScale(as.getRpDp(), as.getRndm()));
            } else {
              ldgDeLn.setCred(BigDecimal.valueOf(tot)
                .setScale(as.getRpDp(), as.getRndm()));
            }
          } while (rs.next());
        }
      } finally {
        if (rs != null) {
          rs.close();
        }
      }
      pRvs.put("ldgDe", ldgDe);
      pRqDt.setAttr("rnd", "ldgr");
      this.rdb.commit();
    } catch (Exception ex) {
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw ex;
    } finally {
      this.rdb.release();
    }
  }

  /**
   * <p>Lazy gets query detail.</p>
   * @return SQL query
   * @throws IOException - IO exception
   **/
  public final synchronized String lazQuDet() throws IOException {
    if (this.quDet == null) {
      this.quDet = loadStr("acc/ldgr/det.sql");
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
      this.quPrv = loadStr("acc/ldgr/prev.sql");
    }
    return this.quPrv;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFlNm file name
   * @return String usually SQL query
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
    return null;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for rdb.</p>
   * @return IRdb
   **/
  public final IRdb<RS> getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final void setRdb(final IRdb<RS> pRdb) {
    this.rdb = pRdb;
  }

  /**
   * <p>Getter for trIsl.</p>
   * @return Integer
   **/
  public final Integer getTrIsl() {
    return this.trIsl;
  }

  /**
   * <p>Setter for trIsl.</p>
   * @param pTrIsl reference
   **/
  public final void setTrIsl(final Integer pTrIsl) {
    this.trIsl = pTrIsl;
  }

  /**
   * <p>Getter for srvDt.</p>
   * @return ISrvDt
   **/
  public final ISrvDt getSrvDt() {
    return this.srvDt;
  }

  /**
   * <p>Setter for srvDt.</p>
   * @param pSrvDt reference
   **/
  public final void setSrvDt(final ISrvDt pSrvDt) {
    this.srvDt = pSrvDt;
  }
}
