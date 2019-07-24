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

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.acc.mdlp.BnStLn;
import org.beigesoft.acc.mdlp.Entr;
import org.beigesoft.acc.mdlp.PrepFr;
import org.beigesoft.acc.mdlp.PrepTo;
import org.beigesoft.acc.mdlp.PaymFr;
import org.beigesoft.acc.mdlp.PaymTo;

/**
 * <p>Service that retrieves bank statement line for processing.</p>
 *
 * @author Yury Demidenko
 */
public class BnStLnGfe implements IPrcEnt<BnStLn, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Process that retrieves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final BnStLn process(final Map<String, Object> pRvs, final BnStLn pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    this.orm.refrEnt(pRvs, vs, pEnt);
    if (pEnt.getRsAc() != null) {
      throw new ExcCode(ExcCode.SPAM,
        "Attempt to edit completed bank statement line!");
    }
    String amStr;
    if (pEnt.getAmnt().compareTo(BigDecimal.ZERO) > 0) {
      amStr = pEnt.getAmnt().toString();
    } else if (pEnt.getAmnt().compareTo(BigDecimal.ZERO) < 0) {
      amStr = pEnt.getAmnt().negate().toString();
    } else {
      throw new ExcCode(ExcCode.WRPR, "amount_eq_zero");
    }
    long[] startEnd = evalDayStartEndFor(pEnt.getDat());
    String dWhere = "where MDENR=1 and RVID is null and TOT=" + amStr
      + " and DAT>=" + startEnd[0] + " and DAT<=" + startEnd[1];
    String[] ndFlDoc = new String[] {"dbOr", "idOr", "tot", "dat", "dscr"};
    Arrays.sort(ndFlDoc);
    if (pEnt.getAmnt().compareTo(BigDecimal.ZERO) > 0) {
      //bank account debit
      vs.put("PrepFrndFds", ndFlDoc);
      List<PrepFr> prepsFr = getOrm().retLstCnd(pRvs, vs, PrepFr.class, dWhere);
      vs.clear();
      if (prepsFr.size() > 0) {
        pRqDt.setAttr("preps", prepsFr);
      }
      vs.put("PaymFrndFds", ndFlDoc);
      List<PaymFr> paymsFr = getOrm().retLstCnd(pRvs, vs, PaymFr.class, dWhere);
      vs.clear();
      if (paymsFr.size() > 0) {
        pRqDt.setAttr("payms", paymsFr);
      }
      String eWhereD = "where RVID is null and SRTY in (1,2008) and SADTY=1003"
    + " and SADID=" + pEnt.getOwnr().getBnka().getIid() + " and DEBT=" + amStr
  + " and DAT>=" + startEnd[0] + " and DAT<=" + startEnd[1];
      List<Entr> entriesFr = getOrm().retLstCnd(pRvs, vs, Entr.class, eWhereD);
      if (entriesFr.size() > 0) {
        pRqDt.setAttr("entrs", entriesFr);
      }
    } else {
      //bank account credit
      vs.put("PrepTondFds", ndFlDoc);
      List<PrepTo> prepsTo = getOrm().retLstCnd(pRvs, vs, PrepTo.class, dWhere);
      vs.clear();
      if (prepsTo.size() > 0) {
        pRqDt.setAttr("preps", prepsTo);
      }
      vs.put("PaymTondFds", ndFlDoc);
      List<PaymTo> paymsTo = getOrm().retLstCnd(pRvs, vs, PaymTo.class, dWhere);
      vs.clear();
      if (paymsTo.size() > 0) {
        pRqDt.setAttr("payms", paymsTo);
      }
      String eWhereC = "where RVID is null and SRTY in (1,2008) and SACTY=1003"
    + " and SACID=" + pEnt.getOwnr().getBnka().getIid() + " and CRED=" + amStr
  + " and DAT>=" + startEnd[0] + " and DAT<= " + startEnd[1];
      List<Entr> entriesTo = getOrm().retLstCnd(pRvs, vs, Entr.class, eWhereC);
      if (entriesTo.size() > 0) {
        pRqDt.setAttr("entrs", entriesTo);
      }
    }
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setEnt(pEnt);
    return pEnt;
  }

  /**
   * <p>Evaluate start and end of day for given pDateFor.</p>
   * @param pDateFor date for
   * @return Start and end of day for pDateFor
   **/
  public final long[] evalDayStartEndFor(final Date pDateFor) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(pDateFor);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    long[] result = new long[2];
    result[0] = cal.getTimeInMillis();
    cal.set(Calendar.HOUR_OF_DAY, 23);
    cal.set(Calendar.MINUTE, 59);
    cal.set(Calendar.SECOND, 59);
    cal.set(Calendar.MILLISECOND, 999);
    result[1] = cal.getTimeInMillis();
    return result;
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
}
