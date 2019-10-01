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

import org.beigesoft.mdl.IOwnedOr;
import org.beigesoft.mdlp.AOrId;
import org.beigesoft.acc.mdlb.IRvId;

/**
 * <p>Model of wage line.</p>
 *
 * @author Yury Demidenko
 */
public class WgLn extends AOrId implements IOwnedOr<Wage>, IRvId {

  /**
   * <p>Owner.</p>
   **/
  private Wage ownr;

  /**
   * <p>Wage type.</p>
   **/
  private WagTy wgTy;

  /**
   * <p>Account wage expenses.</p>
   **/
  private Acnt acWge;

  /**
   * <p>Reversed ID.</p>
   **/
  private Long rvId;

  /**
   * <p>Taxes due to employee.</p>
   **/
  private BigDecimal txEe = BigDecimal.ZERO;

  /**
   * <p>Gross wage.</p>
   **/
  private BigDecimal grWg = BigDecimal.ZERO;

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

  /**
   * <p>Getter for rvId.</p>
   * @return Long
   **/
  @Override
  public final Long getRvId() {
    return this.rvId;
  }

  /**
   * <p>Setter for rvId.</p>
   * @param pRvId reference
   **/
  @Override
  public final void setRvId(final Long pRvId) {
    this.rvId = pRvId;
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
   * <p>Getter for acWge.</p>
   * @return Acnt
   **/
  public final Acnt getAcWge() {
    return this.acWge;
  }

  /**
   * <p>Setter for acWge.</p>
   * @param pAcWge reference
   **/
  public final void setAcWge(final Acnt pAcWge) {
    this.acWge = pAcWge;
  }

  /**
   * <p>Getter for txEe.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTxEe() {
    return this.txEe;
  }

  /**
   * <p>Setter for txEe.</p>
   * @param pTxEe reference
   **/
  public final void setTxEe(final BigDecimal pTxEe) {
    this.txEe = pTxEe;
  }

  /**
   * <p>Getter for grWg.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getGrWg() {
    return this.grWg;
  }

  /**
   * <p>Setter for grWg.</p>
   * @param pGrWg reference
   **/
  public final void setGrWg(final BigDecimal pGrWg) {
    this.grWg = pGrWg;
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
