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

package org.beigesoft.acc.prc;

import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.beigesoft.mdl.IRecSet;
import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.prc.IPrc;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.TxCt;
import org.beigesoft.acc.mdlp.TxDst;

/**
 * <p>Transactional service that retrieves destination tax category
 * for item.</p>
 *
 * @author Yury Demidenko
 * @param <RS> platform dependent record set type
 */
public class RvTxCt<RS> implements IPrc {

  /**
   * <p>RDB service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Transaction isolation.</p>
   **/
  private Integer trIsl;

  //Cashed:
  /**
   * <p>Query.</p>
   **/
  private String quRvTxCt;

  /**
   * <p>Process request.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    String ent = pRqDt.getParam("ent");
    String itm;
    String itdl;
    if ("PuInGdLn".equals(ent) || "SaInGdLn".equals(ent)) {
      itm = "ITM";
      itdl = "ITTXDL";
    } else if ("PuInSrLn".equals(ent) || "SaInSrLn".equals(ent)) {
      itm = "SRV";
      itdl = "SRTXDL";
    } else {
      throw new ExcCode(ExcCode.SPAM, "Wrong request revealing tax category!");
    }
    String itmId = pRqDt.getParam("itmId");
    String txDsId = pRqDt.getParam("txDsId");
    if (itmId == null || txDsId == null) {
      throw new ExcCode(ExcCode.SPAM, "Wrong request revealing tax category!");
    }
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      reveal(pRvs, itm, itdl, itmId, txDsId);
      pRqDt.setAttr("rnd", "rvtcj");
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
   * <p>Reveal data.</p>
   * @param pRvs request scoped vars
   * @param pItm table item
   * @param pItdl table destination tax
   * @param pItmId item ID
   * @param pTxDsId DS ID
   * @throws Exception - an exception
   **/
  public final void reveal(final Map<String, Object> pRvs, final String pItm,
    final String pItdl, final String pItmId,
      final String pTxDsId) throws Exception {
    String qu = lazQuRvTxCt().replace(":TITM", pItm);
    qu = qu.replace(":TTDL", pItdl);
    qu = qu.replace(":ITM", pItmId);
    qu = qu.replace(":TXDS", pTxDsId);
    IRecSet<RS> rs = null;
    try {
      rs = getRdb().retRs(qu);
      if (rs.first()) {
        Double agRt;
        Integer stRm = rs.getInt("STRM");
        String tcNm = rs.getStr("TCDNME");
        if (tcNm  == null) {
          tcNm = rs.getStr("TCONME");
          agRt = rs.getDouble("TCOAGRT");
        } else {
          agRt = rs.getDouble("TCDAGRT");
        }
        if (rs.next()) {
          throw new ExcCode(ExcCode.WRPR, "multi_tax_dest");
        }
        if (agRt  == null) { //non-taxable:
          agRt = 0.0;
        }
        RoundingMode rm;
        if (stRm  == null) {
          AcStg as = (AcStg) pRvs.get("astg");
          rm = as.getStRm();
        } else {
          rm = RoundingMode.class.getEnumConstants()[stRm];
        }
        TxCt txCt = new TxCt();
        txCt.setNme(tcNm);
        txCt.setAgRt(BigDecimal.valueOf(agRt));
        pRvs.put("txCt", txCt);
        TxDst txDs = new TxDst();
        txDs.setStRm(rm);
        pRvs.put("txDs", txDs);
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
  }

  /**
   * <p>Lazy gets query revealing tax category.</p>
   * @return SQL query
   * @throws IOException - IO exception
   **/
  public final synchronized String lazQuRvTxCt() throws IOException {
    if (this.quRvTxCt == null) {
      this.quRvTxCt = loadStr("/acc/rvTxCt.sql");
    }
    return this.quRvTxCt;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFlNm file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final synchronized String loadStr(
    final String pFlNm) throws IOException {
    URL urlFile = RvTxCt.class.getResource(pFlNm);
    if (urlFile != null) {
      InputStream is = null;
      try {
        is = RvTxCt.class.getResourceAsStream(pFlNm);
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
}
