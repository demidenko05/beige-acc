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

package org.beigesoft.acc.rpl;

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.rpl.IFltEnts;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlp.Entr;

/**
 * <p>Filter that avoids debits/credits for accounting entries.</p>
 *
 * @author Yury Demidenko
 */
public class FlAvDbCr implements IFltEnts {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Makes SQL WHERE filter for given entity.</p>
   * @param pCls Entity Class
   * @param pRvs request scoped vars mast has rqDbId - requested database ID
   * and rpAcMtId - replication accounting method ID (both strings)
   * @return filter, e.g. "DBOR=1 and (ACDB is null or ACDB in list ('PAYB'))"
   * @throws Exception - an exception
   **/
  @Override
  public final String makeWhe(final Map<String, Object> pRvs,
    final Class<? extends IHasId<?>> pCls) throws Exception {
    if (Entr.class != pCls) {
  throw new Exception("Wrong configuration! This filter for accounting entry!");
    }
    Integer rqDbId = Integer.parseInt((String) pRvs.get("rqDbId"));
    if (!this.orm.getDbId().equals(rqDbId)) {
      throw new Exception("Wrong DB ID! this DB ID/requested: "
        + this.orm.getDbId() + "/" + rqDbId);
    }
    Long rpAcMtId = Long.parseLong((String) pRvs.get("rpAcMtId"));
    RplAcc rplAcc = new RplAcc();
    rplAcc.setIid(rpAcMtId);
    Map<String, Object> vs = new HashMap<String, Object>();
    this.orm.refrEnt(pRvs, vs, rplAcc);
    if (rplAcc.getIid() == null) {
      throw new Exception("There is no replication method with ID " + rpAcMtId);
    }
    String[] ndfAc = new String[] {"acnt"};
    vs.put("RpExDblndFds", ndfAc);
    vs.put("AcntdpLv", 0);
    rplAcc.setExDbls(this.orm.retLstCnd(pRvs, vs, RpExDbl.class,
      "where OWNR=" + rpAcMtId)); vs.clear();
    vs.put("RpExCrlndFds", ndfAc);
    vs.put("AcntdpLv", 0);
    rplAcc.setExCrds(this.orm.retLstCnd(pRvs, vs, RpExCrl.class,
      "where OWNR=" + rpAcMtId)); vs.clear();
    StringBuffer sb = new StringBuffer("DBOR=" + rqDbId);
    if (rplAcc.getExDbls().size() > 0) {
      sb.append(" and (ACDB is null or ACDB not in (");
      boolean isFst = true;
      for (RpExDbl ln : rplAcc.getExDbls()) {
        if (isFst) {
          isFst = false;
        } else {
          sb.append(",");
        }
        sb.append("'" + ln.getAcnt().getIid() + "'");
      }
      sb.append("))");
    }
    if (rplAcc.getExCrds().size() > 0) {
      sb.append(" and (ACCR is null or ACCR not in (");
      boolean isFst = true;
      for (RpExCrl ln : rplAcc.getExCrds()) {
        if (isFst) {
          isFst = false;
        } else {
          sb.append(",");
        }
        sb.append("'" + ln.getAcnt().getIid() + "'");
      }
      sb.append("))");
    }
    return sb.toString();
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
