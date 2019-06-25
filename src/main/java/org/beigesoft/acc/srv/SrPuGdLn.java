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
import org.beigesoft.acc.mdlp.PurInv;
import org.beigesoft.acc.mdlp.PuInGdLn;
import org.beigesoft.acc.mdlp.TxDst;

/**
 * <p>Service item oriented for purchase good line.</p>
 *
 * @author Yury Demidenko
 */
public class SrPuGdLn implements ISrInItLn<PurInv, PuInGdLn> {

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
  private IRvInvLn<PurInv, PuInGdLn> rvInvLn;

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
    final Map<String, Object> pVs, final PuInGdLn pEnt) throws Exception {
    this.srWrhEnr.load(pRvs, pEnt, pEnt.getWrhp());
  }

  /**
   * <p>Prepare line, e.g. for purchase good it makes items left,
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
    final Map<String, Object> pVs, final PuInGdLn pEnt,
      final TxDst pTxRules) throws Exception {
    pEnt.setItLf(pEnt.getQuan());
    //For current implementation toLf is zero after loading (only itLf=quan),
    //so for the first withdrawal drawer should set it as total-withdrawal!
    BigDecimal exchRt = pEnt.getOwnr().getExRt();
    if (exchRt.compareTo(BigDecimal.ZERO) == -1) {
      exchRt = BigDecimal.ONE.divide(exchRt.negate(), 15, RoundingMode.HALF_UP);
    }
    AcStg as = (AcStg) pRvs.get("astg");
    pVs.put("ItmNdFds", new String[] {"iid", "knCs"});
    this.orm.refrEnt(pRvs, pVs, pEnt.getItm()); pVs.clear();
    if (pEnt.getItm().getKnCs().compareTo(BigDecimal.ZERO) == 1) {
      //known cost
      if (pEnt.getOwnr().getCuFr() != null) {
        pEnt.setPrFc(pEnt.getItm().getKnCs());
        BigDecimal toSu = pEnt.getPrFc().multiply(pEnt.getQuan())
          .setScale(as.getPrDp(), as.getRndm());
        if (pTxRules == null || pEnt.getOwnr().getInTx()) {
          pEnt.setToFc(toSu);
        } else {
          pEnt.setSuFc(toSu);
        }
        pEnt.setPri(pEnt.getPrFc().multiply(exchRt)
          .setScale(as.getPrDp(), as.getRndm()));
      } else {
        pEnt.setPri(pEnt.getItm().getKnCs());
      }
      BigDecimal toSu = pEnt.getPri().multiply(pEnt.getQuan())
        .setScale(as.getPrDp(), as.getRndm());
      if (pTxRules == null || pEnt.getOwnr().getInTx()) {
        pEnt.setTot(toSu);
      } else {
        pEnt.setSubt(toSu);
      }
    } else if (pEnt.getOwnr().getCuFr() != null) { //user passed values:
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
   * e.g. for purchase goods lines it checks for withdrawals.</p>
   * @param pRvs Request scoped variables, not null
   * @param pVs Invoker scoped variables, not null
   * @param pEnt reversing, not null
   * @return checked line
   * @throws Exception - an exception
   **/
  @Override
  public final PuInGdLn retChkRv(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final PuInGdLn pEnt) throws Exception {
    pVs.put("PurInvdpLv", 0);
    pVs.put("ItmdpLv", 0);
    pVs.put("TxCtdpLv", 0);
    pVs.put("WrhPldpLv", 0);
    pVs.put("UomdpLv", 0);
    PuInGdLn rz = new PuInGdLn();
    rz.setIid(pEnt.getRvId());
    this.orm.refrEnt(pRvs, pVs, rz); pVs.clear();
    if (rz.getIid() == null) {
      throw new ExcCode(ExcCode.SPAM, "Reversed not found! PuInGdLn id= "
        + pEnt.getRvId());
    }
    if (rz.getRvId() != null) {
      throw new ExcCode(ExcCode.SPAM, "Attempt reverse reversed PuInGdLn id= "
        + pEnt.getRvId());
    }
    if (rz.getQuan().compareTo(rz.getItLf()) == 1) {
      throw new ExcCode(ExcCode.WRPR, "where_is_withdraw");
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
    final Map<String, Object> pVs, final PuInGdLn pRvng,
      final PuInGdLn pRved) throws Exception {
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
   * @return IRvInvLn<PurInv, PuInGdLn>
   **/
  public final IRvInvLn<PurInv, PuInGdLn> getRvInvLn() {
    return this.rvInvLn;
  }

  /**
   * <p>Setter for rvInvLn.</p>
   * @param pRvInvLn reference
   **/
  public final void setRvInvLn(final IRvInvLn<PurInv, PuInGdLn> pRvInvLn) {
    this.rvInvLn = pRvInvLn;
  }
}
