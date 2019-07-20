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
import org.beigesoft.mdlp.AOrId;

/**
 * <p>Model of wage tax table line.</p>
 *
 * @author Yury Demidenko
 */
public class WttLn extends AOrId implements IOwned<WagTt, Long> {

  /**
   * <p>Owner.</p>
   **/
  private WagTt ownr;

  /**
   * <p>Wage from.</p>
   **/
  private BigDecimal wgFr = BigDecimal.ZERO;

  /**
   * <p>Wage to.</p>
   **/
  private BigDecimal wgTo = BigDecimal.ZERO;

  /**
   * <p>Year wage from.</p>
   **/
  private BigDecimal ywgFr = BigDecimal.ZERO;

  /**
   * <p>Year wage to.</p>
   **/
  private BigDecimal ywgTo = BigDecimal.ZERO;

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
   * <p>Getter for ownr.</p>
   * @return WagTt
   **/
  @Override
  public final WagTt getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final WagTt pOwnr) {
    this.ownr = pOwnr;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for wgFr.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getWgFr() {
    return this.wgFr;
  }

  /**
   * <p>Setter for wgFr.</p>
   * @param pWgFr reference
   **/
  public final void setWgFr(final BigDecimal pWgFr) {
    this.wgFr = pWgFr;
  }

  /**
   * <p>Getter for wgTo.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getWgTo() {
    return this.wgTo;
  }

  /**
   * <p>Setter for wgTo.</p>
   * @param pWgTo reference
   **/
  public final void setWgTo(final BigDecimal pWgTo) {
    this.wgTo = pWgTo;
  }

  /**
   * <p>Getter for ywgFr.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getYwgFr() {
    return this.ywgFr;
  }

  /**
   * <p>Setter for ywgFr.</p>
   * @param pYwgFr reference
   **/
  public final void setYwgFr(final BigDecimal pYwgFr) {
    this.ywgFr = pYwgFr;
  }

  /**
   * <p>Getter for ywgTo.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getYwgTo() {
    return this.ywgTo;
  }

  /**
   * <p>Setter for ywgTo.</p>
   * @param pYwgTo reference
   **/
  public final void setYwgTo(final BigDecimal pYwgTo) {
    this.ywgTo = pYwgTo;
  }

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
}
