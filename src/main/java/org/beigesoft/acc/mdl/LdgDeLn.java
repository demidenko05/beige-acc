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

import java.util.Date;
import java.math.BigDecimal;

/**
 * <p>Ledger detail line.</p>
 *
 * @author Yury Demidenko
 */
public class LdgDeLn {

  /**
   * <p>Date.</p>
   **/
  private Date dat;

  /**
   * <p>Integer, Not Null Source Type e.g. 1 - InEntr.</p>
   **/
  private Integer srTy;

  /**
   * <p>Document/line ID.</p>
   **/
  private Long srId;

  /**
   * <p>Correspondent account number.</p>
   **/
  private String crAcNmb;

  /**
   * <p>Correspondent account name.</p>
   **/
  private String crAcNm;

  /**
   * <p>Correspondent subaccount name, e.g. "invItem line1".</p>
   **/
  private String crSaNm;

  /**
   * <p>Subaccount name, e.g. "invItem line1".</p>
   **/
  private String subacc;

  /**
   * <p>Debit.</p>
   **/
  private BigDecimal debt = BigDecimal.ZERO;

  /**
   * <p>Credit.</p>
   **/
  private BigDecimal cred = BigDecimal.ZERO;

  /**
   * <p>Balance.</p>
   **/
  private BigDecimal blnc = BigDecimal.ZERO;

  /**
   * <p>Balance subaccount.</p>
   **/
  private BigDecimal blncSa = BigDecimal.ZERO;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

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
   * <p>Getter for srTy.</p>
   * @return Integer
   **/
  public final Integer getSrTy() {
    return this.srTy;
  }

  /**
   * <p>Setter for srTy.</p>
   * @param pSrTy reference
   **/
  public final void setSrTy(final Integer pSrTy) {
    this.srTy = pSrTy;
  }

  /**
   * <p>Getter for srId.</p>
   * @return Long
   **/
  public final Long getSrId() {
    return this.srId;
  }

  /**
   * <p>Setter for srId.</p>
   * @param pSrId reference
   **/
  public final void setSrId(final Long pSrId) {
    this.srId = pSrId;
  }

  /**
   * <p>Getter for crAcNmb.</p>
   * @return String
   **/
  public final String getCrAcNmb() {
    return this.crAcNmb;
  }

  /**
   * <p>Setter for crAcNmb.</p>
   * @param pCrAcNmb reference
   **/
  public final void setCrAcNmb(final String pCrAcNmb) {
    this.crAcNmb = pCrAcNmb;
  }

  /**
   * <p>Getter for crAcNm.</p>
   * @return String
   **/
  public final String getCrAcNm() {
    return this.crAcNm;
  }

  /**
   * <p>Setter for crAcNm.</p>
   * @param pCrAcNm reference
   **/
  public final void setCrAcNm(final String pCrAcNm) {
    this.crAcNm = pCrAcNm;
  }

  /**
   * <p>Getter for crSaNm.</p>
   * @return String
   **/
  public final String getCrSaNm() {
    return this.crSaNm;
  }

  /**
   * <p>Setter for crSaNm.</p>
   * @param pCrSaNm reference
   **/
  public final void setCrSaNm(final String pCrSaNm) {
    this.crSaNm = pCrSaNm;
  }

  /**
   * <p>Getter for subacc.</p>
   * @return String
   **/
  public final String getSubacc() {
    return this.subacc;
  }

  /**
   * <p>Setter for subacc.</p>
   * @param pSubacc reference
   **/
  public final void setSubacc(final String pSubacc) {
    this.subacc = pSubacc;
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
   * <p>Getter for blnc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getBlnc() {
    return this.blnc;
  }

  /**
   * <p>Setter for blnc.</p>
   * @param pBlnc reference
   **/
  public final void setBlnc(final BigDecimal pBlnc) {
    this.blnc = pBlnc;
  }

  /**
   * <p>Getter for blncSa.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getBlncSa() {
    return this.blncSa;
  }

  /**
   * <p>Setter for blncSa.</p>
   * @param pBlncSa reference
   **/
  public final void setBlncSa(final BigDecimal pBlncSa) {
    this.blncSa = pBlncSa;
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
