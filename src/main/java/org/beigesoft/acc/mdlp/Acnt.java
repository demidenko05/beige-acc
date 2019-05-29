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

import org.beigesoft.mdlp.AIdStrNm;
import org.beigesoft.acc.mdl.EAccTy;
import org.beigesoft.acc.mdl.ENrBlTy;

/**
 * <p>Model of account.</p>
 *
 * @author Yury Demidenko
 */
 public class Acnt extends AIdStrNm {

  /**
   * <p>Number, not null.</p>
   **/
  private String nmbr;

  /**
   * <p>If used in current method,
   * e.g. Sales Tax Payable not used if you are not
   * Sales Tax Vendor (no selling taxable goods and services).</p>
   **/
  private Boolean used = false;

  /**
   * <p>Account Normal Balance Type - DEBIT, CREDIT.</p>
   **/
  private ENrBlTy blTy;

  /**
   * <p>EAccTy.ASSET/LIABILITY/OWNERS_EQUITY/
   * GROSS_INCOME_REVENUE/GROSS_INCOME_EXPENSE.</p>
   **/
  private EAccTy typ;

  /**
   * <p>Subacccount type, e.g. 1000 - Expn.
   * This is constant [entity].cnsTy.</p>
   **/
  private Integer saTy;

  /**
   * <p>If account created programmatically then  user can't delete
   * or change its main fields (ID, type, normal balance type).</p>
   **/
  private Boolean usCr = true;

  /**
   * <p>Dscr.</p>
   **/
  private String dscr;

  /**
   * <p>List of existed sub-accounts of type "saTy".</p>
   **/
  private List<Sacnt> sacnts;

  //Simple getters and setters:
  /**
   * <p>Geter for nmbr.</p>
   * @return String
   **/
  public final String getNmbr() {
    return this.nmbr;
  }

  /**
   * <p>Setter for nmbr.</p>
   * @param pNmbr reference
   **/
  public final void setNmbr(final String pNmbr) {
    this.nmbr = pNmbr;
  }

  /**
   * <p>Geter for used.</p>
   * @return Boolean
   **/
  public final Boolean getUsed() {
    return this.used;
  }

  /**
   * <p>Setter for used.</p>
   * @param pUsed reference
   **/
  public final void setUsed(final Boolean pUsed) {
    this.used = pUsed;
  }

  /**
   * <p>Geter for blTy.</p>
   * @return ENrBlTy
   **/
  public final ENrBlTy getBlTy() {
    return this.blTy;
  }

  /**
   * <p>Setter for blTy.</p>
   * @param pBlTy reference
   **/
  public final void setBlTy(final ENrBlTy pBlTy) {
    this.blTy = pBlTy;
  }

  /**
   * <p>Geter for typ.</p>
   * @return EAccTy
   **/
  public final EAccTy getTyp() {
    return this.typ;
  }

  /**
   * <p>Setter for typ.</p>
   * @param pTyp reference
   **/
  public final void setTyp(final EAccTy pTyp) {
    this.typ = pTyp;
  }

  /**
   * <p>Geter for saTy.</p>
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
   * <p>Getter for usCr.</p>
   * @return Boolean
   **/
  public final Boolean getUsCr() {
    return this.usCr;
  }

  /**
   * <p>Setter for usCr.</p>
   * @param pUsCr reference
   **/
  public final void setUsCr(final Boolean pUsCr) {
    this.usCr = pUsCr;
  }

  /**
   * <p>Geter for dscr.</p>
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

  /**
   * <p>Getter for sacnts.</p>
   * @return List<Sacnt>
   **/
  public final List<Sacnt> getSacnts() {
    return this.sacnts;
  }

  /**
   * <p>Setter for sacnts.</p>
   * @param pSacnts reference
   **/
  public final void setSacnts(final List<Sacnt> pSacnts) {
    this.sacnts = pSacnts;
  }
}
