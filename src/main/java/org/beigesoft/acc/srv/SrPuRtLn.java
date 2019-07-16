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
import java.util.Arrays;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.PurInv;
import org.beigesoft.acc.mdlp.PurRet;
import org.beigesoft.acc.mdlp.PuRtLn;
import org.beigesoft.acc.mdlp.TxDst;

/**
 * <p>Service line oriented for purchase return line.</p>
 *
 * @author Yury Demidenko
 */
public class SrPuRtLn implements ISrInItLn<PurRet, PuRtLn> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Warehouse entries service.</p>
   **/
  private ISrWrhEnr srWrhEnr;

  /**
   * <p>Line reverser.</p>
   **/
  private IRvInvLn<PurRet, PuRtLn> rvInvLn;

  /**
   * <p>Draw item service.</p>
   **/
  private ISrDrItEnr srDrItEnr;

  /**
   * <p>It makes warehouse entry and draw item entry.</p>
   * @param pRvs Request scoped variables, not null
   * @param pVs Invoker scoped variables, not null
   * @param pEnt line, not null
   * @throws Exception - an exception
   **/
  @Override
  public final void mkEntrs(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final PuRtLn pEnt) throws Exception {
    this.srDrItEnr.drawFr(pRvs, pEnt, pEnt.getInvl(), pEnt.getQuan());
    this.srWrhEnr.draw(pRvs, pEnt, pEnt.getWhpo());
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
    final Map<String, Object> pVs, final PuRtLn pEnt,
      final TxDst pTxRules) throws Exception {
    BigDecimal exchRt = pEnt.getOwnr().getExRt();
    if (exchRt.compareTo(BigDecimal.ZERO) == -1) {
      exchRt = BigDecimal.ONE.divide(exchRt.negate(), 15, RoundingMode.HALF_UP);
    }
    AcStg as = (AcStg) pRvs.get("astg");
    if (pEnt.getOwnr().getCuFr() != null) { //user passed values:
      if (pTxRules == null || pEnt.getOwnr().getInTx()) {
        pEnt.setTot(pEnt.getToFc().multiply(exchRt)
          .setScale(as.getPrDp(), as.getRndm()));
      } else {
        pEnt.setSubt(pEnt.getSuFc().multiply(exchRt)
          .setScale(as.getPrDp(), as.getRndm()));
      }
    }
    String[] fds = new String[] {"itm", "uom", "pri", "prFc", "txCt", "itLf"};
    Arrays.sort(fds);
    pVs.put(pEnt.getInvl().getClass().getSimpleName() + "ndFds", fds);
    pVs.put(pEnt.getInvl().getClass().getSimpleName() + "dpLv", 1);
    String[] fdstc = new String[] {"nme", "agRt"};
    Arrays.sort(fdstc);
    pVs.put("TxCtndFds", fdstc);
    pVs.put("TxCtdpLv", 1);
    this.orm.refrEnt(pRvs, pVs, pEnt.getInvl()); pVs.clear();
    pEnt.getInvl().setOwnr(pEnt.getOwnr().getInv());
    if (pEnt.getInvl().getItLf().compareTo(pEnt.getQuan()) == -1) {
      throw new ExcCode(ExcCode.WRPR, "LINE_HAS_NO_GOODS");
    }
    String src = "*" + pEnt.getInvl().getPri() + ", "
      + pEnt.getInvl().getItLf() + "*";
    if (pEnt.getDscr() == null) {
      pEnt.setDscr(src);
    } else {
      pEnt.setDscr(pEnt.getDscr() + " " + src);
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
  public final PuRtLn retChkRv(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final PuRtLn pEnt) throws Exception {
    PuRtLn rz = new PuRtLn();
    rz.setIid(pEnt.getRvId());
    this.orm.refrEnt(pRvs, pVs, rz); pVs.clear();
    if (rz.getIid() == null) {
      throw new ExcCode(ExcCode.SPAM, "Reversed not found! PuRtLn id= "
        + pEnt.getRvId());
    }
    if (rz.getRvId() != null) {
      throw new ExcCode(ExcCode.SPAM, "Attempt reverse reversed PuRtLn id= "
        + pEnt.getRvId());
    }
    pEnt.setInvl(rz.getInvl());
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
    final Map<String, Object> pVs, final PuRtLn pRvng,
      final PuRtLn pRved) throws Exception {
    this.rvInvLn.revLns(pRvs, pVs, pRvng.getOwnr(), pRvng, pRved);
  }

  //Only for returns, only in reverser good lines! Cheapest method.
  /**
   * <p>Getter for invoice class.</p>
   * @return invoice class
   **/
  @Override
  public final Class<? extends AInv> getBinvCls() {
    return PurInv.class;
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
   * <p>Getter for srWrhEnr.</p>
   * @return ISrWrhEnr
   **/
  public final ISrWrhEnr getSrWrhEnr() {
    return this.srWrhEnr;
  }

  /**
   * <p>Setter for srWrhEnr.</p>
   * @param pSrWrhEnr reference
   **/
  public final void setSrWrhEnr(final ISrWrhEnr pSrWrhEnr) {
    this.srWrhEnr = pSrWrhEnr;
  }

  /**
   * <p>Getter for rvInvLn.</p>
   * @return IRvInvLn<PurRet, PuRtLn>
   **/
  public final IRvInvLn<PurRet, PuRtLn> getRvInvLn() {
    return this.rvInvLn;
  }

  /**
   * <p>Setter for rvInvLn.</p>
   * @param pRvInvLn reference
   **/
  public final void setRvInvLn(final IRvInvLn<PurRet, PuRtLn> pRvInvLn) {
    this.rvInvLn = pRvInvLn;
  }

  /**
   * <p>Getter for srDrItEnr.</p>
   * @return ISrDrItEnr
   **/
  public final ISrDrItEnr getSrDrItEnr() {
    return this.srDrItEnr;
  }

  /**
   * <p>Setter for srDrItEnr.</p>
   * @param pSrDrItEnr reference
   **/
  public final void setSrDrItEnr(final ISrDrItEnr pSrDrItEnr) {
    this.srDrItEnr = pSrDrItEnr;
  }
}
