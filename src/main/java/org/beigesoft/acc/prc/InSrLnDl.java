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
import org.beigesoft.rdb.IRdb;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.mdlb.AInvLn;
import org.beigesoft.acc.mdlb.ALnTxLn;
import org.beigesoft.acc.mdlp.Srv;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.TxDst;
import org.beigesoft.acc.srv.UtInLnTxTo;

/**
 * <p>Processor that deletes invoice service line from DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> invoice type
 * @param <L> invoice line type
 * @param <LTL> item tax line type
 * @author Yury Demidenko
 */
public class InSrLnDl<RS, T extends AInv, L extends AInvLn<T, Srv>,
  LTL extends ALnTxLn<T, L>> implements IPrcEnt<L, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Service that makes taxes and totals for line and invoice.</p>
   **/
  private UtInLnTxTo<RS, T, L, ?, LTL> utInTxTo;

  /**
   * <p>Process that saves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final L process(final Map<String, Object> pRvs, final L pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    String[] ifds = new String[] {"dat", "dbcr", "dbOr", "dscr", "iid", "inTx",
      "mdEnr", "omTx", "ver", "cuFr", "exRt"};
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
    if (pEnt.getRvId() != null) {
      throw new ExcCode(ExcCode.SPAM, "Attempt to delete reversed line!");
    }
    this.orm.del(pRvs, vs, pEnt);
    this.rdb.delete(this.utInTxTo.getLtlCl().getSimpleName().toUpperCase(),
      "OWNR=" + pEnt.getIid());
    AcStg as = (AcStg) pRvs.get("astg");
    TxDst txRules = this.utInTxTo.revealTaxRules(pEnt.getOwnr(), as);
    this.utInTxTo.makeTotals(pRvs, vs, pEnt, as, txRules);
    pRvs.put("msgSuc", "delete_ok");
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setOwnr(pEnt.getOwnr());
    return null;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for utInTxTo.</p>
   * @return UtInLnTxTo<RS, T, L, ?, LTL>
   **/
  public final UtInLnTxTo<RS, T, L, ?, LTL> getUtInTxTo() {
    return this.utInTxTo;
  }

  /**
   * <p>Setter for utInTxTo.</p>
   * @param pUtInTxTo reference
   **/
  public final void setUtInTxTo(final UtInLnTxTo<RS, T, L, ?, LTL> pUtInTxTo) {
    this.utInTxTo = pUtInTxTo;
  }

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
   * <p>Geter for rdb.</p>
   * @return IRdb
   **/
  public final IRdb<RS> getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final void setRdb(
    final IRdb<RS> pRdb) {
    this.rdb = pRdb;
  }
}
