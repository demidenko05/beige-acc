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

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlb.IInvb;
import org.beigesoft.acc.mdlb.AInTxLn;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.TxDst;
import org.beigesoft.acc.srv.UtInLnTxTo;

/**
 * <p>Service that saves invoice tax line (only invoice basis) into DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> invoice type
 * @param <TL> tax line type
 * @author Yury Demidenko
 */
public class InTxLnSv<RS, T extends IInvb, TL extends AInTxLn<T>>
  implements IPrcEnt<TL, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Service that makes taxes and totals for line and invoice.</p>
   **/
  private UtInLnTxTo<RS, T, ?, TL, ?> utInTxTo;

  /**
   * <p>Process that pertieves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final TL process(final Map<String, Object> pRvs, final TL pEnt,
    final IReqDt pRqDt) throws Exception {
    if (!pEnt.getIsNew()) {
      Map<String, Object> vs = new HashMap<String, Object>();
      String[] ifds = new String[] {"dat", "dbcr", "dbOr", "dscr", "iid",
        "inTx", "mdEnr", "omTx", "ver", "cuFr", "exRt"};
      Arrays.sort(ifds);
      vs.put(pEnt.getOwnr().getClass().getSimpleName() + "ndFds", ifds);
      String[] fdsdc = new String[] {"iid", "txDs"};
      Arrays.sort(fdsdc);
      vs.put("DbCrndFds", fdsdc);
      String[] fdstd = new String[] {"iid", "stRm", "stIb", "stAg"};
      Arrays.sort(fdstd);
      vs.put("TxDstndFds", fdstd);
      vs.put("DbCrdpLv", 2);
      this.orm.refrEnt(pRvs, vs, pEnt.getOwnr()); vs.clear();
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
      AcStg as = (AcStg) pRvs.get("astg");
      TxDst txRules = this.utInTxTo.revealTaxRules(pEnt.getOwnr(), as);
      if (!txRules.getStIb()) {
      throw new ExcCode(ExcCode.SPAM, "Attempt to change item basis tax line!");
      }
      TL old = getOrm().retEnt(pRvs, vs, pEnt);
      if (old.getTot().compareTo(pEnt.getTot()) == 0) {
        throw new ExcCode(ExcCode.SPAM,
          "There is no changes in item basis tax line!");
      }
      String dscr;
      if (old.getDscr() == null) {
        dscr = "!" + old.getTot() + "!";
      } else {
        dscr = old.getDscr() + " !" + old.getTot() + "!";
      }
      pEnt.setDscr(dscr);
      String[] upFds = new String[] {"tot", "dscr", "ver"};
      Arrays.sort(upFds);
      vs.put("ndFds", upFds);
      getOrm().update(pRvs, vs, pEnt); vs.clear();
      this.utInTxTo.adjInvLnsUpdTots(pRvs, vs, pEnt.getOwnr(), as, txRules);
      pRvs.put("msgSuc", "update_ok");
      UvdVar uvs = (UvdVar) pRvs.get("uvs");
      uvs.setOwnr(pEnt.getOwnr());
      return null;
    } else {
      throw new ExcCode(ExcCode.SPAM, "Attempt to insert invoice tax line!");
    }
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
   * <p>Getter for utInTxTo.</p>
   * @return UtInLnTxTo<RS, T, ?, TL, ?>
   **/
  public final UtInLnTxTo<RS, T, ?, TL, ?> getUtInTxTo() {
    return this.utInTxTo;
  }

  /**
   * <p>Setter for utInTxTo.</p>
   * @param pUtInTxTo reference
   **/
  public final void setUtInTxTo(final UtInLnTxTo<RS, T, ?, TL, ?> pUtInTxTo) {
    this.utInTxTo = pUtInTxTo;
  }
}
