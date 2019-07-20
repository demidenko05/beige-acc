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
 * <p>Model of employee year wage line.</p>
 *
 * @author Yury Demidenko
 */
public class EmpWg extends AOrId implements IOwned<Empl, Long> {

  /**
   * <p>Owner.</p>
   **/
  private Empl ownr;

  /**
   * <p>Year, not null.</p>
   **/
  private Integer yer;

  /**
   * <p>Wage type.</p>
   **/
  private WagTy wgTy;

  /**
   * <p>Total.</p>
   **/
  private BigDecimal tot = BigDecimal.ZERO;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

  /**
   * <p>Getter for ownr.</p>
   * @return Empl
   **/
  @Override
  public final Empl getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final Empl pOwnr) {
    this.ownr = pOwnr;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for wgTy.</p>
   * @return WagTy
   **/
  public final WagTy getWgTy() {
    return this.wgTy;
  }

  /**
   * <p>Setter for wgTy.</p>
   * @param pWgTy reference
   **/
  public final void setWgTy(final WagTy pWgTy) {
    this.wgTy = pWgTy;
  }

  /**
   * <p>Getter for yer.</p>
   * @return Integer
   **/
  public final Integer getYer() {
    return this.yer;
  }

  /**
   * <p>Setter for yer.</p>
   * @param pYer reference
   **/
  public final void setYer(final Integer pYer) {
    this.yer = pYer;
  }

  /**
   * <p>Getter for tot.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTot() {
    return this.tot;
  }

  /**
   * <p>Setter for tot.</p>
   * @param pTot reference
   **/
  public final void setTot(final BigDecimal pTot) {
    this.tot = pTot;
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
