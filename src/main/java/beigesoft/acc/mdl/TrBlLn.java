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

package org.beigesoft.acc.mdl;

import java.math.BigDecimal;

/**
 * <p>Trial Balance Line.</p>
 *
 * @author Yury Demidenko
 */
public class TrBlLn {

  /**
   * <p>Account ID.</p>
   **/
  private String acId;

  /**
   * <p>Account number.</p>
   **/
  private String acNb;

  /**
   * <p>Account name.</p>
   **/
  private String acNm;

  /**
   * <p>Subaccount ID.</p>
   **/
  private Long saId;

  /**
   * <p>Subacccount type, e.g. 1000 - Expn.</p>
   **/
  private Integer saTy;

  /**
   * <p>Subaccount name, e.g. "invItem line1".</p>
   **/
  private String saNm;

  /**
   * <p>Debit sub-account.</p>
   **/
  private BigDecimal debt;

  /**
   * <p>Credit sub-account.</p>
   **/
  private BigDecimal cred;

  //Summary not from query:
  /**
   * <p>Debit account.</p>
   **/
  private BigDecimal debtAcc;

  /**
   * <p>Credit account.</p>
   **/
  private BigDecimal credAcc;

  //Simple getters and setters:
  /**
   * <p>Getter for acNb.</p>
   * @return String
   **/
  public final String getAcNb() {
    return this.acNb;
  }

  /**
   * <p>Setter for acNb.</p>
   * @param pAcNb reference
   **/
  public final void setAcNb(final String pAcNb) {
    this.acNb = pAcNb;
  }

  /**
   * <p>Getter for acNm.</p>
   * @return String
   **/
  public final String getAcNm() {
    return this.acNm;
  }

  /**
   * <p>Setter for acNm.</p>
   * @param pAcNm reference
   **/
  public final void setAcNm(final String pAcNm) {
    this.acNm = pAcNm;
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
   * <p>Getter for debt.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getDebt() {
    return this.debt;
  }

  /**
   * <p>Setter for debt.</p>
   * @param pDebt reference
   **/
  public final void setDebt(final BigDecimal pDebt) {
    this.debt = pDebt;
  }

  /**
   * <p>Getter for cred.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getCred() {
    return this.cred;
  }

  /**
   * <p>Setter for cred.</p>
   * @param pCred reference
   **/
  public final void setCred(final BigDecimal pCred) {
    this.cred = pCred;
  }

  /**
   * <p>Getter for acId.</p>
   * @return String
   **/
  public final String getAcId() {
    return this.acId;
  }

  /**
   * <p>Setter for acId.</p>
   * @param pAcId reference
   **/
  public final void setAcId(final String pAcId) {
    this.acId = pAcId;
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
   * <p>Getter for debtAcc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getDebtAcc() {
    return this.debtAcc;
  }

  /**
   * <p>Setter for debtAcc.</p>
   * @param pDebtAcc reference
   **/
  public final void setDebtAcc(final BigDecimal pDebtAcc) {
    this.debtAcc = pDebtAcc;
  }

  /**
   * <p>Getter for credAcc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getCredAcc() {
    return this.credAcc;
  }

  /**
   * <p>Setter for credAcc.</p>
   * @param pCredAcc reference
   **/
  public final void setCredAcc(final BigDecimal pCredAcc) {
    this.credAcc = pCredAcc;
  }
}
