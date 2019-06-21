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

package org.beigesoft.acc.mdlp;

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.acc.mdlb.AInvLn;
import org.beigesoft.acc.mdlb.IItmSrc;

/**
 * <p>Model of purchase invoice goods line.</p>
 *
 * @author Yury Demidenko
 */
public class PuInGdLn extends AInvLn<PurInv, Itm> implements IItmSrc {

  /**
   * <p>Invoice.</p>
   **/
  private PurInv ownr;

  /**
   * <p>Warehouse place.</p>
   **/
  private WrhPl wrhp;

  /**
   * <p>Item.</p>
   **/
  private Itm itm;

  /**
   * <p>Items left (the rest) to draw, loads by the quantity,
   * draws by sales, losses etc.</p>
   **/
  private BigDecimal itLf = BigDecimal.ZERO;

  /**
   * <p>Total left (the rest) to draw, loads by the total,
   * draws by sales, losses etc.</p>
   **/
  private BigDecimal toLf = BigDecimal.ZERO;

  /**
   * <p>Item basis tax lines.</p>
   **/
  private List<PuInGdTxLn> txLns;

  /**
   * <p>Constant of code type.</p>
   * @return 2000
   **/
  @Override
  public final Integer cnsTy() {
    return 2000;
  }

  /**
   * <p>Getter for ownr.</p>
   * @return PurInv
   **/
  @Override
  public final PurInv getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final PurInv pOwnr) {
    this.ownr = pOwnr;
  }

  /**
   * <p>Getter for itm.</p>
   * @return Itm
   **/
  @Override
  public final Itm getItm() {
    return this.itm;
  }

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  @Override
  public final void setItm(final Itm pItm) {
    this.itm = pItm;
  }

  /**
   * <p>Getter for itLf.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getItLf() {
    return this.itLf;
  }

  /**
   * <p>Setter for itLf.</p>
   * @param pItLf reference
   **/
  @Override
  public final void setItLf(final BigDecimal pItLf) {
    this.itLf = pItLf;
  }

  /**
   * <p>Getter for toLf.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getToLf() {
    return this.toLf;
  }

  /**
   * <p>Setter for toLf.</p>
   * @param pToLf reference
   **/
  @Override
  public final void setToLf(final BigDecimal pToLf) {
    this.toLf = pToLf;
  }

  /**
   * <p>Getter for document date (own or owner's).</p>
   * @return Date
   **/
  @Override
  public final Date getDocDt() {
    return this.ownr.getDat();
  }

  /**
   * <p>Getter for owner ID if exist.</p>
   * @return ID
   **/
  @Override
  public final Long getOwnrId() {
    return this.ownr.getIid();
  }

  /**
   * <p>Getter for owner type code if exist.</p>
   * @return type code
   **/
  @Override
  public final Integer getOwnrTy() {
    return this.ownr.cnsTy();
  }

  //Simple getters and setters:
  /**
   * <p>Getter for txLns.</p>
   * @return List<PuInGdTxLn>
   **/
  public final List<PuInGdTxLn> getTxLns() {
    return this.txLns;
  }

  /**
   * <p>Setter for txLns.</p>
   * @param pTxLns reference
   **/
  public final void setTxLns(final List<PuInGdTxLn> pTxLns) {
    this.txLns = pTxLns;
  }

  /**
   * <p>Getter for wrhp.</p>
   * @return WrhPl
   **/
  public final WrhPl getWrhp() {
    return this.wrhp;
  }

  /**
   * <p>Setter for wrhp.</p>
   * @param pWrhp reference
   **/
  public final void setWrhp(final WrhPl pWrhp) {
    this.wrhp = pWrhp;
  }
}
