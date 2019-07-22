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
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdl.ETxTy;
import org.beigesoft.acc.mdlp.WgTxl;

/**
 * <p>Service that deletes wage tax line from DB.</p>
 *
 * @author Yury Demidenko
 */
public class WgTxlDl implements IPrcEnt<WgTxl, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Process that pertieves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final WgTxl process(final Map<String, Object> pRvs, final WgTxl pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    this.orm.refrEnt(pRvs, vs, pEnt.getOwnr());
    long owVrWs = Long.parseLong(pRqDt.getParam("owVr"));
    if (owVrWs != pEnt.getOwnr().getVer()) {
      throw new ExcCode(IOrm.DRTREAD, "dirty_read");
    }
    if (!pEnt.getOwnr().getDbOr().equals(this.orm.getDbId())) {
      throw new ExcCode(ExcCode.SPAM, "can_not_change_foreign_src");
    }
    if (!pEnt.getDbOr().equals(this.orm.getDbId())) {
      throw new ExcCode(ExcCode.SPAM, "can_not_change_foreign_src");
    }
    if (pEnt.getOwnr().getMdEnr()) {
      throw new ExcCode(ExcCode.SPAM, "Attempt to change accounted document!");
    }
    if (pEnt.getRvId() != null) {
      throw new ExcCode(ExcCode.SPAM, "Attempt to delete reversed line!");
    }
    this.orm.del(pRvs, vs, pEnt);
    pRvs.put("msgSuc", "delete_ok");
    BigDecimal txEe = BigDecimal.ZERO;
    BigDecimal txEr = BigDecimal.ZERO;
    vs.put("WagedpLv", 0);
    List<WgTxl> lns = this.orm.retLstCnd(pRvs, vs, WgTxl.class, "where OWNR="
      + pEnt.getOwnr().getIid()); vs.clear();
    for (WgTxl tl : lns) {
      if (tl.getTax().getTyp() == ETxTy.TEMPLOYEE) {
        txEe = txEe.add(tl.getTot());
      } else if (tl.getTax().getTyp() == ETxTy.TEMPLOYER) {
        txEr = txEr.add(tl.getTot());
      } else {
        throw new ExcCode(ExcCode.WR, "Wage has non-wage tax! nme/id/typ: "
          + tl.getTax().getNme() + "/" + tl.getTax().getIid() + "/"
            + tl.getTax().getTyp());
      }
    }
    pEnt.getOwnr().setTxEe(txEe);
    pEnt.getOwnr().setTxEr(txEr);
    pEnt.getOwnr().setNtWg(pEnt.getOwnr().getTot().subtract(txEe));
    String[] upFds = new String[] {"ntWg", "txEe", "txEr", "ver"};
    Arrays.sort(upFds);
    vs.put("ndFds", upFds);
    getOrm().update(pRvs, vs, pEnt.getOwnr()); vs.clear();
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setOwnr(pEnt.getOwnr());
    return null;
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
