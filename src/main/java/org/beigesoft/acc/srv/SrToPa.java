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
import org.beigesoft.rdb.IOrm;
import org.beigesoft.srv.II18n;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.mdlb.APaym;
import org.beigesoft.acc.mdlb.APrep;

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
    DateFormat dtFr = DateFormat.getDateTimeInstance(DateFormat
      .MEDIUM, DateFormat.SHORT, new Locale(cpf.getLngDef().getIid()));
    StringBuffer sb = new StringBuffer();
    BigDecimal toPa = BigDecimal.ZERO;
    String[] fds =  new String[] {"iid", "dat", "dbOr", "tot"};
    Arrays.sort(fds);
    vs.put(pEnt.getPrepCls().getSimpleName() + "ndFds", fds);
    APrep prep;
    if (!pEnt.getIsNew()) {
      prep = this.orm.retEntCnd(pRvs, vs, pEnt.getPrepCls(),
      "rvId is null and mdEnr=1 and invId=" + pEnt.getIid());
    } else {
      prep = this.orm.retEntCnd(pRvs, vs, pEnt.getPrepCls(),
      "rvId is null and mdEnr=1 and iid=" + pEnt.getPrep().getIid());
    }
    vs.clear();
    boolean isFst = true;
    if (prep != null) {
      sb.append(getI18n().getMsg(prep.getClass().getSimpleName() + "sht",
    cpf.getLngDef().getIid()) + " #" + prep.getDbOr() + "-" + prep
  .getIid() + ", " + dtFr.format(prep.getDat()) + ", " + prep.getTot());
      isFst = false;
      toPa = toPa.add(prep.getTot());
    }
    if (!pEnt.getIsNew()) {
      vs.put(pEnt.getPaymCls().getSimpleName() + "ndFds", fds);
      List<? extends APaym<?>> payms = this.orm.retLstCnd(pRvs, vs, pEnt
.getPaymCls(), "where rvId is null and mdEnr=1 and inv="      + pEnt.getIid());
      vs.clear();
      for (APaym<?> paym : payms) {
        if (isFst) {
          sb.append("; ");
          isFst = false;
        }
        sb.append(getI18n().getMsg(paym.getClass().getSimpleName() + "sht",
      cpf.getLngDef().getIid()) + " #" + paym.getDbOr() + "-" + paym
    .getIid() + ", " + dtFr.format(paym.getDat()) + ", " + paym.getTot());
        toPa = toPa.add(paym.getTot());
      }
    }
    pEnt.setPdsc(sb.toString());
    pEnt.setToPa(toPa);
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
}
