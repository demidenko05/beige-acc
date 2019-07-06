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
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Locale;
import java.text.DateFormat;
import java.math.BigDecimal;

import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.srv.II18n;
import org.beigesoft.srv.INumStr;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.mdlb.APaym;
import org.beigesoft.acc.mdlp.AcStg;

/**
 * <p>Service that makes total payment for given
 * purchase/sales invoice.</p>
 *
 * @author Yury Demidenko
 */
public class SrToPa implements ISrToPa {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>I18N service.</p>
   */
  private II18n i18n;

  /**
   * <p>Service print number.</p>
   **/
  private INumStr numStr;

  /**
   * <p>Makes total payment for given invoice.</p>
   * @param <T> invoice type
   * @param pRvs Request scoped variables, not null
   * @param pEnt invoice, not null
   * @throws Exception - an exception
   **/
  @Override
  public final <T extends AInv> void mkToPa(final Map<String, Object> pRvs,
    final T pEnt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    AcStg as = (AcStg) pRvs.get("astg");
    UsPrf upf = (UsPrf) pRvs.get("upf");
    DateFormat dtFr = DateFormat.getDateTimeInstance(DateFormat
      .MEDIUM, DateFormat.SHORT, new Locale(cpf.getLngDef().getIid()));
    StringBuffer sb = new StringBuffer();
    BigDecimal toPa = BigDecimal.ZERO;
    BigDecimal paFc = BigDecimal.ZERO;
    boolean isFst = true;
    BigDecimal to;
    String[] fds =  new String[] {"iid", "dat", "dbOr", "tot", "toFc"};
    Arrays.sort(fds);
    vs.put(pEnt.getPrepCls().getSimpleName() + "ndFds", fds);
    if (pEnt.getPrep() != null) {
      this.orm.refrEnt(pRvs, vs, pEnt.getPrep()); vs.clear();
      if (pEnt.getCuFr() == null) {
        to = pEnt.getPrep().getTot();
      } else {
        to = pEnt.getPrep().getToFc();
      }
      sb.append(getI18n().getMsg(pEnt.getPrep().getClass().getSimpleName()
    + "sht", cpf.getLngDef().getIid()) + " #" + pEnt.getPrep().getDbOr() + "-"
  + pEnt.getPrep().getIid() + ", " + dtFr.format(pEnt.getPrep().getDat())
+ ", " + prn(as, cpf,  upf, to));
      isFst = false;
      toPa = toPa.add(pEnt.getPrep().getTot());
      paFc = paFc.add(pEnt.getPrep().getToFc());
    }
    if (!pEnt.getIsNew()) {
      vs.put(pEnt.getPaymCls().getSimpleName() + "ndFds", fds);
      List<? extends APaym<?>> payms = this.orm.retLstCnd(pRvs, vs, pEnt
.getPaymCls(), "where rvId is null and mdEnr=1 and inv="      + pEnt.getIid());
      vs.clear();
      for (APaym<?> paym : payms) {
        if (isFst) {
          isFst = false;
        } else {
          sb.append("; ");
        }
        if (pEnt.getCuFr() == null) {
          to = paym.getTot();
        } else {
          to = paym.getToFc();
        }
        sb.append(getI18n().getMsg(paym.getClass().getSimpleName() + "sht",
      cpf.getLngDef().getIid()) + " #" + paym.getDbOr() + "-" + paym.getIid()
    + ", " + dtFr.format(paym.getDat()) + ", " + prn(as, cpf,  upf, to));
        toPa = toPa.add(paym.getTot());
        paFc = paFc.add(paym.getToFc());
      }
    }
    if (!isFst) {
      if (pEnt.getCuFr() == null) {
        sb.append(" (" + as.getCurr().getNme() + ")");
      } else {
        sb.append(" (" + pEnt.getCuFr().getNme() + ")");
      }
    }
    pEnt.setPdsc(sb.toString());
    pEnt.setToPa(toPa);
    pEnt.setPaFc(paFc);
  }

  /**
   * <p>Simple delegator to print number.</p>
   * @param pAs ACC stg
   * @param pCpf common prefs
   * @param pUpf user prefs
   * @param pVal value
   * @return String
   **/
  public final String prn(final AcStg pAs, final CmnPrf pCpf, final UsPrf pUpf,
    final BigDecimal pVal) {
    return this.numStr.frmt(pVal.toString(), pCpf.getDcSpv(),
      pCpf.getDcGrSpv(), pAs.getPrDp(), pUpf.getDgInGr());
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
   * <p>Getter for numStr.</p>
   * @return INumStr
   **/
  public final INumStr getNumStr() {
    return this.numStr;
  }

  /**
   * <p>Setter for numStr.</p>
   * @param pNumStr reference
   **/
  public final void setNumStr(final INumStr pNumStr) {
    this.numStr = pNumStr;
  }
}
