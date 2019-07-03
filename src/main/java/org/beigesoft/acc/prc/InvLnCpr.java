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
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.acc.mdlb.AInvLn;

/**
 * <p>Service that makes copy of invoice line to copy or reverse.</p>
 *
 * @author Yury Demidenko
 */
public class InvLnCpr implements IPrcEnt<AInvLn<?, ?>, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Process that creates copy entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final AInvLn<?, ?> process(final Map<String, Object> pRvs,
    final AInvLn<?, ?> pEnt, final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    if (!pEnt.getDbOr().equals(this.orm.getDbId())) {
      throw new ExcCode(ExcCode.SPAM, "can_not_change_foreign_src");
    }
    Long rvId = pEnt.getRvId();
    this.orm.refrEnt(pRvs, vs, pEnt);
    if (rvId == null) {
      pEnt.setQuan(BigDecimal.ZERO);
      pEnt.setPri(BigDecimal.ZERO);
      pEnt.setPrFc(BigDecimal.ZERO);
      pEnt.setSubt(BigDecimal.ZERO);
      pEnt.setSuFc(BigDecimal.ZERO);
      pEnt.setToTx(BigDecimal.ZERO);
      pEnt.setTxFc(BigDecimal.ZERO);
      pEnt.setTot(BigDecimal.ZERO);
      pEnt.setToFc(BigDecimal.ZERO);
      pEnt.setTxCt(null);
    } else {
      pEnt.setRvId(rvId);
      pEnt.setPri(pEnt.getTot().negate());
      pEnt.setPrFc(pEnt.getTot().negate());
      pEnt.setSubt(pEnt.getTot().negate());
      pEnt.setSuFc(pEnt.getTot().negate());
      pEnt.setToTx(pEnt.getTot().negate());
      pEnt.setTxFc(pEnt.getTot().negate());
      pEnt.setTot(pEnt.getTot().negate());
      pEnt.setToFc(pEnt.getToFc().negate());
    }
    pEnt.setTdsc(null);
    pEnt.setDscr(null);
    pEnt.setIid(null);
    pEnt.setIsNew(true);
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
}
