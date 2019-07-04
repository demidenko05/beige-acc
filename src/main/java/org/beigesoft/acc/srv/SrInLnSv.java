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

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.mdlb.AInvLn;
import org.beigesoft.acc.mdlb.AInTxLn;
import org.beigesoft.acc.mdlb.ALnTxLn;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.TxDst;

/**
 * <p>Service that saves invoice line into DB.
 * It's type-safe part (shared code) that is used inside type safe
 * processor-assembly.</p>
 *
 * @author Yury Demidenko
 */
public class SrInLnSv {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Saves entity generic method.</p>
   * @param <RS> platform dependent record set type
   * @param <T> invoice type
   * @param <L> invoice line type
   * @param <TL> tax line type
   * @param <LTL> item tax line type
   * @param pRvs request scoped vars
   * @param pEnt Entity to process
   * @param pRqDt Request Data
   * @param pUtTxTo taxes and totals maker
   * @param pSrInItLn line's item oriented service
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  public final <RS, T extends AInv, L extends AInvLn<T, ?>,
    TL extends AInTxLn<T>, LTL extends ALnTxLn<T, L>> L save(
      final Map<String, Object> pRvs, final L pEnt, final IReqDt pRqDt,
        final UtInLnTxTo<RS, T, L, TL, LTL> pUtTxTo,
          final ISrInItLn<T, L> pSrInItLn) throws Exception {
    if (!pEnt.getIsNew() && !pUtTxTo.getIsMutable()) {
      throw new ExcCode(ExcCode.SPAM, "Attempt to update immutable line");
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    String[] ifds = new String[] {"dat", "dbcr", "dbOr", "dscr", "iid", "inTx",
      "mdEnr", "omTx", "ver"};
    Arrays.sort(ifds); //without it dbOr is omitted
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
    TxDst txRules = pUtTxTo.revealTaxRules(pEnt.getOwnr(), as);
    if (pEnt.getRvId() != null) {
      //retrieves reversed, make reversing item,
      //for purchase goods lines it also checks for withdrawals:
      L revd = pSrInItLn.retChkRv(pRvs, vs, pEnt);
      revd.setOwnr(pEnt.getOwnr());
      pEnt.setDbOr(this.orm.getDbId());
      pEnt.setUom(revd.getUom());
      pEnt.setTxCt(revd.getTxCt());
      pEnt.setTdsc(revd.getTdsc());
      pEnt.setPri(revd.getPri());
      pEnt.setPrFc(revd.getPrFc());
      pEnt.setQuan(revd.getQuan().negate());
      pEnt.setSubt(revd.getSubt().negate());
      pEnt.setSuFc(revd.getSuFc().negate());
      pEnt.setToTx(revd.getToTx().negate());
      pEnt.setTxFc(revd.getTxFc().negate());
      pEnt.setTot(revd.getTot().negate());
      pEnt.setToFc(revd.getToFc().negate());
      //it also inserts reversing and updates reversed
      //for good it also makes warehouse reversing
      //for sales good it also makes draw item reversing
      //It removes line tax lines:
      pSrInItLn.revLns(pRvs, vs, pEnt, revd);
      pRvs.put("msgSuc", "reverse_ok");
    } else {
      if (pEnt.getQuan().compareTo(BigDecimal.ZERO) != 1) {
        throw new ExcCode(ExcCode.WRPR, "quantity_less_or_equal_zero");
      }
      if (!(pEnt.getPri().compareTo(BigDecimal.ZERO) == 1
        || pEnt.getPrFc().compareTo(BigDecimal.ZERO) == 1)) {
        throw new ExcCode(ExcCode.WRPR, "price_less_eq_0");
      }
      //prepare line, e.g. for purchase good it makes items left,
      //it may makes totals/subtotals (depends of price inclusive),
      //known cost:
      pSrInItLn.prepLn(pRvs, vs, pEnt, txRules);
      //it make taxes for line and update it
      //it also inserts or updates line and put success message
      pUtTxTo.mkLnTxTo(pRvs, vs, pEnt, as, txRules);
      //for good it makes warehouse entry
      //for sales good it also makes draw item entry:
      pSrInItLn.mkEntrs(pRvs, vs, pEnt);
    }
    //it updates invoice
    pUtTxTo.makeTotals(pRvs, vs, pEnt, as, txRules);
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
