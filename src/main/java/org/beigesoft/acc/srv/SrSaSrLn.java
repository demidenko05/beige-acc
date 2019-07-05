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
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.SalInv;
import org.beigesoft.acc.mdlp.SaInSrLn;
import org.beigesoft.acc.mdlp.TxDst;

/**
 * <p>Service item oriented for sales good line.</p>
 *
 * @author Yury Demidenko
 */
public class SrSaSrLn implements ISrInItLn<SalInv, SaInSrLn> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Line reverser.</p>
   **/
  private IRvInvLn<SalInv, SaInSrLn> rvInvLn;

  /**
   * <p>For good it makes warehouse entry
   * for sales good it also makes draw item entry.</p>
   * @param pRvs Request scoped variables, not null
   * @param pVs Invoker scoped variables, not null
   * @param pEnt line, not null
   * @throws Exception - an exception
   **/
  @Override
  public final void mkEntrs(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final SaInSrLn pEnt) throws Exception {
    //nothing
  }

  /**
   * <p>Prepare line, e.g. for sales good it makes items left,
   * it may makes totals/subtotals (depends of price inclusive),
   * known cost.</p>
   * @param pRvs Request scoped variables, not null
   * @param pVs Invoker scoped variables, not null
   * @param pEnt line, not null
   * @param pTxRules maybe null
   * @throws Exception - an exception
   **/
  @Override
  public final void prepLn(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final SaInSrLn pEnt,
      final TxDst pTxRules) throws Exception {
    if (pEnt.getOwnr().getCuFr() != null) { //user passed values:
      BigDecimal exchRt = pEnt.getOwnr().getExRt();
      if (exchRt.compareTo(BigDecimal.ZERO) == -1) {
        exchRt = BigDecimal.ONE.divide(exchRt.negate(), 15,
          RoundingMode.HALF_UP);
      }
      AcStg as = (AcStg) pRvs.get("astg");
      pEnt.setPri(pEnt.getPrFc().multiply(exchRt)
        .setScale(as.getPrDp(), as.getRndm()));
      if (pTxRules == null || pEnt.getOwnr().getInTx()) {
        pEnt.setTot(pEnt.getToFc().multiply(exchRt)
          .setScale(as.getPrDp(), as.getRndm()));
      } else {
        pEnt.setSubt(pEnt.getSuFc().multiply(exchRt)
          .setScale(as.getPrDp(), as.getRndm()));
      }
    }
  }

  /**
   * <p>Retrieves and checks line for reversing, makes reversing item,
   * e.g. for sales goods lines it checks for withdrawals.</p>
   * @param pRvs Request scoped variables, not null
   * @param pVs Invoker scoped variables, not null
   * @param pEnt reversing, not null
   * @return checked line
   * @throws Exception - an exception
   **/
  @Override
  public final SaInSrLn retChkRv(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final SaInSrLn pEnt) throws Exception {
    pVs.put("SalInvdpLv", 0);
    pVs.put("SrvdpLv", 0);
    pVs.put("TxCtdpLv", 0);
    pVs.put("UomdpLv", 0);
    pVs.put("AcntdpLv", 0);
    SaInSrLn rz = new SaInSrLn();
    rz.setIid(pEnt.getRvId());
    this.orm.refrEnt(pRvs, pVs, rz); pVs.clear();
    if (rz.getIid() == null) {
      throw new ExcCode(ExcCode.SPAM, "Reversed not found! SaInSrLn id= "
        + pEnt.getRvId());
    }
    if (rz.getRvId() != null) {
      throw new ExcCode(ExcCode.SPAM, "Attempt reverse reversed SaInSrLn id= "
        + pEnt.getRvId());
    }
    pEnt.setItm(rz.getItm());
    return rz;
  }

  /**
   * <p>Reverses lines.
   * it also inserts reversing and updates reversed
   * for good it also makes warehouse reversing
   * for sales good it also makes draw item reversing.
   * It removes line tax lines.</p>
   * @param pRvs Request scoped variables, not null
   * @param pVs Invoker scoped variables, not null
   * @param pRvng reversing line, not null
   * @param pRved reversed line, not null
   * @throws Exception - an exception
   **/
  @Override
  public final void revLns(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final SaInSrLn pRvng,
      final SaInSrLn pRved) throws Exception {
    this.rvInvLn.revLns(pRvs, pVs, pRvng.getOwnr(), pRvng, pRved);
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
   * <p>Getter for rvInvLn.</p>
   * @return IRvInvLn<SalInv, SaInSrLn>
   **/
  public final IRvInvLn<SalInv, SaInSrLn> getRvInvLn() {
    return this.rvInvLn;
  }

  /**
   * <p>Setter for rvInvLn.</p>
   * @param pRvInvLn reference
   **/
  public final void setRvInvLn(final IRvInvLn<SalInv, SaInSrLn> pRvInvLn) {
    this.rvInvLn = pRvInvLn;
  }
}
