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

import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.mdlp.AIdLn;

/**
 * <p>Model that store balance of a account and
 * subaccount at the start of each month (or week etc) to improve performance.
 * Version changed time algorithm.</p>
 *
 * @author Yury Demidenko
 */
public class Blnc extends AIdLn {

  /**
   * <p>Date, Not Null, usually start of month.</p>
   **/
  private Date dat;

  /**
   * <p>Account e.g Inventory.</p>
   **/
  private Acnt acc;

  /**
   * <p>Subacccount type, e.g. 1000 - Expn.</p>
   **/
  private Integer saTy;

  /**
   * <p>Foreign ID of subaccount.</p>
   **/
  private Long saId;

  /**
   * <p>Appearance of subaccount.</p>
   **/
  private String saNm;

  /**
   * <p>Balance.</p>
   **/
  private BigDecimal bln;

  //Simple getters and setters:
  /**
   * <p>Getter for dat.</p>
   * @return Date
   **/
  public final Date getDat() {
    return this.dat;
  }

  /**
   * <p>Setter for dat.</p>
   * @param pDat reference
   **/
  public final void setDat(final Date pDat) {
    this.dat = pDat;
  }

  /**
   * <p>Getter for acc.</p>
   * @return Acnt
   **/
  public final Acnt getAcc() {
    return this.acc;
  }

  /**
   * <p>Setter for acc.</p>
   * @param pAcc reference
   **/
  public final void setAcc(final Acnt pAcc) {
    this.acc = pAcc;
  }

  /**
   * <p>Getter for saTy.</p>
   * @return Integer
   **/
  public final Integer getSaTy() {
    return this.saTy;
  }

  /**
   * <p>Setter for saTy.</p>
   * @param pSaTy reference
   **/
  public final void setSaTy(final Integer pSaTy) {
    this.saTy = pSaTy;
  }

  /**
   * <p>Getter for saId.</p>
   * @return Long
   **/
  public final Long getSaId() {
    return this.saId;
  }

  /**
   * <p>Setter for saId.</p>
   * @param pSaId reference
   **/
  public final void setSaId(final Long pSaId) {
    this.saId = pSaId;
  }

  /**
   * <p>Getter for saNm.</p>
   * @return String
   **/
  public final String getSaNm() {
    return this.saNm;
  }

  /**
   * <p>Setter for saNm.</p>
   * @param pSaNm reference
   **/
  public final void setSaNm(final String pSaNm) {
    this.saNm = pSaNm;
  }

  /**
   * <p>Getter for bln.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getBln() {
    return this.bln;
  }

  /**
   * <p>Setter for bln.</p>
   * @param pBln reference
   **/
  public final void setBln(final BigDecimal pBln) {
    this.bln = pBln;
  }
}
