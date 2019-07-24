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
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.srv.II18n;
import org.beigesoft.srv.ICsvRdr;
import org.beigesoft.mdlp.CsvCl;
import org.beigesoft.acc.mdl.EBnEnrSt;
import org.beigesoft.acc.mdlp.BnkStm;
import org.beigesoft.acc.mdlp.BnStLn;
import org.beigesoft.acc.srv.UtlBas;

/**
 * <p>Service that saves manufacturing process into DB.</p>
 *
 * @author Yury Demidenko
 */
public class BnkStmSv implements IPrcEnt<BnkStm, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Base service.</p>
   **/
  private UtlBas utlBas;

  /**
   * <p>I18N service.</p>
   */
  private II18n i18n;

  /**
   * <p>CSV rdr.</p>
   */
  private ICsvRdr csvRdr;

  /**
   * <p>Process that saves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final BnkStm process(final Map<String, Object> pRvs, final BnkStm pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    this.utlBas.chDtForg(pRvs, pEnt, pEnt.getDat());
    if (pEnt.getIsNew()) { //must be file!
      this.orm.refrEnt(pRvs, vs, pEnt.getMth());
      vs.put("CsvMthdpLv", 0);
      pEnt.getMth().getMth().setClns(this.orm.retLstCnd(pRvs, vs, CsvCl.class,
        "where OWNR=" + pEnt.getMth().getMth().getIid())); vs.clear();
      SimpleDateFormat sdf = null;
      try {
        sdf = new SimpleDateFormat(pEnt.getMth().getDtCl().getFrmt());
      } catch (Exception ee) {
        throw new ExcCode(ExcCode.WRPR, "!Wrong date format! Format: "
          + pEnt.getMth().getDtCl().getFrmt(), ee);
      }
      String[] seps = null;
      if (pEnt.getMth().getAmCl().getFrmt() != null) {
        try {
          seps = pEnt.getMth().getAmCl().getFrmt().split(",");
          for (int i = 0; i < 2; i++) {
            if ("SPACE".equals(seps[i])) {
              seps[i] = " ";
            } else if ("COMMA".equals(seps[i])) {
              seps[i] = ",";
            }
          }
        } catch (Exception ee) {
          throw new ExcCode(ExcCode.WRPR, "!Wrong amount format! Format: "
            + pEnt.getMth().getAmCl().getFrmt(), ee);
        }
      }
      String fileUplNm = (String) pRqDt.getAttr("fileUplNm");
      pEnt.setSrcNm(fileUplNm + "/" + pEnt.getMth().getNme());
      InputStreamReader rdr = null;
      this.orm.insIdLn(pRvs, vs, pEnt);
      try {
        InputStream ins = (InputStream) pRqDt.getAttr("fileUplIs");
        rdr = new InputStreamReader(ins, Charset
          .forName(pEnt.getMth().getMth().getChrst()).newDecoder());
        List<String> csvRow;
        int r = 0;
        while ((csvRow = this.csvRdr.readNext(pRvs, rdr,
          pEnt.getMth().getMth())) != null) {
          r++;
          if (r == 1 && pEnt.getMth().getMth().getHasHd()) {
            continue;
          }
          BnStLn bsl = new BnStLn();
          bsl.setDbOr(this.orm.getDbId());
          bsl.setOwnr(pEnt);
          String dateStr = csvRow.get(pEnt.getMth().getDtCl().getIndx() - 1);
          try {
            bsl.setDat(sdf.parse(dateStr));
          } catch (Exception ee) {
            throw new ExcCode(ExcCode.WRPR, "!Wrong date value! Value/Format: "
              + dateStr + "/" + pEnt.getMth().getDtCl().getFrmt(), ee);
          }
          String amStr = csvRow.get(pEnt.getMth().getAmCl().getIndx() - 1);
          try {
            if (seps != null) {
              if (!"NONE".equals(seps[0])) {
                amStr = amStr.replace(seps[0], ".");
              }
              if (!"NONE".equals(seps[1])) {
                amStr = amStr.replace(seps[1], "");
              }
            }
            bsl.setAmnt(new BigDecimal(amStr));
          } catch (Exception ee) {
            throw new ExcCode(ExcCode.WRPR, "Wrong amount value! Value/Format: "
              + amStr + "/" + pEnt.getMth().getAmCl().getFrmt(), ee);
          }
          String descr = null;
          if (pEnt.getMth().getDsCl() != null) {
            descr = csvRow.get(pEnt.getMth().getDsCl().getIndx() - 1);
          }
          if (pEnt.getMth().getStCl() != null) {
            String stStr = csvRow.get(pEnt.getMth().getStCl().getIndx() - 1);
            if (descr == null) {
              descr = stStr;
            } else {
              descr += "/" + stStr;
            }
            if (pEnt.getMth().getAcWds() != null
              && !pEnt.getMth().getAcWds().contains(stStr)) {
              bsl.setStas(EBnEnrSt.OTHER);
            }
            if (pEnt.getMth().getVdWds() != null
              && pEnt.getMth().getVdWds().contains(stStr)) {
              bsl.setStas(EBnEnrSt.VOIDED);
            }
          }
          bsl.setDsSt(descr);
          getOrm().insIdLn(pRvs, vs, bsl);
        }
      } finally {
        if (rdr != null) {
          rdr.close();
        }
      }
      pRvs.put("msgSuc", "insert_ok");
    } else {
      String[] upFds = new String[] {"dat", "dscr", "ver", "bnka"};
      Arrays.sort(upFds);
      vs.put("ndFds", upFds);
      getOrm().update(pRvs, vs, pEnt); vs.clear();
      pRvs.put("msgSuc", "update_ok");
    }
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setEnt(pEnt);
    return pEnt;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for orm.</p>
   * @return IOrm
   **/
  public final IOrm getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final void setOrm(final IOrm pOrm) {
    this.orm = pOrm;
  }

  /**
   * <p>Getter for utlBas.</p>
   * @return UtlBas
   **/
  public final UtlBas getUtlBas() {
    return this.utlBas;
  }

  /**
   * <p>Setter for utlBas.</p>
   * @param pUtlBas reference
   **/
  public final void setUtlBas(final UtlBas pUtlBas) {
    this.utlBas = pUtlBas;
  }

  /**
   * <p>Getter for i18n.</p>
   * @return II18n
   **/
  public final II18n getI18n() {
    return this.i18n;
  }

  /**
   * <p>Setter for i18n.</p>
   * @param pI18n reference
   **/
  public final void setI18n(final II18n pI18n) {
    this.i18n = pI18n;
  }

  /**
   * <p>Getter for csvRdr.</p>
   * @return ICsvRdr
   **/
  public final ICsvRdr getCsvRdr() {
    return this.csvRdr;
  }

  /**
   * <p>Setter for csvRdr.</p>
   * @param pCsvRdr reference
   **/
  public final void setCsvRdr(final ICsvRdr pCsvRdr) {
    this.csvRdr = pCsvRdr;
  }
}
