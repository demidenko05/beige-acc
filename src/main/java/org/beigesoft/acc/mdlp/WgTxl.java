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

import java.math.BigDecimal;

import org.beigesoft.mdl.IOwned;
import org.beigesoft.acc.mdlb.ADcTxLn;

/**
 * <p>Model of wage tax line.</p>
 *
 * @author Yury Demidenko
 */
public class WgTxl extends ADcTxLn implements IOwned<Wage, Long> {

  /**
   * <p>Owner.</p>
   **/
  private Wage ownr;

  /**
   * <p>Allowance.</p>
   **/
  private BigDecimal alw = BigDecimal.ZERO;

  /**
   * <p>Rate.</p>
   **/
  private BigDecimal rate = BigDecimal.ZERO;

  /**
   * <p>Plus amount.</p>
   **/
  private BigDecimal plAm = BigDecimal.ZERO;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

  /**
   * <p>Getter for ownr.</p>
   * @return Wage
   **/
  @Override
  public final Wage getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final Wage pOwnr) {
    this.ownr = pOwnr;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for alw.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getAlw() {
    return this.alw;
  }

  /**
   * <p>Setter for alw.</p>
   * @param pAlw reference
   **/
  public final void setAlw(final BigDecimal pAlw) {
    this.alw = pAlw;
  }

  /**
   * <p>Getter for rate.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getRate() {
    return this.rate;
  }

  /**
   * <p>Setter for rate.</p>
   * @param pRate reference
   **/
  public final void setRate(final BigDecimal pRate) {
    this.rate = pRate;
  }

  /**
   * <p>Getter for plAm.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getPlAm() {
    return this.plAm;
  }

  /**
   * <p>Setter for plAm.</p>
   * @param pPlAm reference
   **/
  public final void setPlAm(final BigDecimal pPlAm) {
    this.plAm = pPlAm;
  }

  /**
   * <p>Getter for dscr.</p>
   * @return String
   **/
  public final String getDscr() {
    return this.dscr;
  }

  /**
   * <p>Setter for dscr.</p>
   * @param pDscr reference
   **/
  public final void setDscr(final String pDscr) {
    this.dscr = pDscr;
  }
}
