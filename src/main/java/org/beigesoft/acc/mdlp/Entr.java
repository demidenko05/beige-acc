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

import org.beigesoft.mdlp.AOrId;

/**
 * <p>Model of Accounting Record (journal entry).</p>
 *
 * @author Yury Demidenko
 */
public class Entr extends AOrId {

  /**
   * <p>Date, not null.</p>
   **/
  private Date dat;

  /**
   * <p>ID of reversed/reversing Entr.</p>
   **/
  private Long rvId;

  /**
   * <p>Integer, Not Null Source Type e.g. 1 - InEntr.
   * This is constant [document/line].cnsTy().</p>
   **/
  private Integer srTy;

  /**
   * <p>Document/line ID, Not Null.</p>
   **/
  private Long srId;

  /**
   * <p>Account debt.</p>
   **/
  private Acnt acDb;

  /**
   * <p>Subacccount debt type, e.g. 1000 - Expn.
   * This is constant [entity].cnsTy().</p>
   **/
  private Integer sadTy;

  /**
   * <p>Foreign ID of subaccount.</p>
   **/
  private Long sadId;

  /**
   * <p>Appearance of subaccount.</p>
   **/
  private String sadNm;

  /**
   * <p>Debt.</p>
   **/
  private BigDecimal debt;

  /**
   * <p>Account cred.</p>
   **/
  private Acnt acCr;

  /**
   * <p>Subacccount cred type.
   * This is constant [entity].cnsTy().</p>
   **/
  private Integer sacTy;

  /**
   * <p>Foreign ID of subaccount.</p>
   **/
  private Long sacId;

  /**
   * <p>Appearance of subaccount.</p>
   **/
  private String sacNm;

  /**
   * <p>Cred.</p>
   **/
  private BigDecimal cred;

  /**
   * <p>Dscr.</p>
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
   * <p>Getter for rvId.</p>
   * @return Long
   **/
  public final Long getRvId() {
    return this.rvId;
  }

  /**
   * <p>Setter for rvId.</p>
   * @param pRvId reference
   **/
  public final void setRvId(final Long pRvId) {
    this.rvId = pRvId;
  }

  /**
   * <p>Geter for srTy.</p>
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
   * <p>Geter for srId.</p>
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
   * <p>Geter for acDb.</p>
   * @return Acnt
   **/
  public final Acnt getAcDb() {
    return this.acDb;
  }

  /**
   * <p>Setter for acDb.</p>
   * @param pAcDb reference
   **/
  public final void setAcDb(final Acnt pAcDb) {
    this.acDb = pAcDb;
  }

  /**
   * <p>Geter for sadTy.</p>
   * @return Integer
   **/
  public final Integer getSadTy() {
    return this.sadTy;
  }

  /**
   * <p>Setter for sadTy.</p>
   * @param pSadTy reference
   **/
  public final void setSadTy(final Integer pSadTy) {
    this.sadTy = pSadTy;
  }

  /**
   * <p>Geter for sadId.</p>
   * @return Long
   **/
  public final Long getSadId() {
    return this.sadId;
  }

  /**
   * <p>Setter for sadId.</p>
   * @param pSadId reference
   **/
  public final void setSadId(final Long pSadId) {
    this.sadId = pSadId;
  }

  /**
   * <p>Geter for sadNm.</p>
   * @return String
   **/
  public final String getSadNm() {
    return this.sadNm;
  }

  /**
   * <p>Setter for sadNm.</p>
   * @param pSadNm reference
   **/
  public final void setSadNm(final String pSadNm) {
    this.sadNm = pSadNm;
  }

  /**
   * <p>Geter for debt.</p>
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
   * <p>Geter for acCr.</p>
   * @return Acnt
   **/
  public final Acnt getAcCr() {
    return this.acCr;
  }

  /**
   * <p>Setter for acCr.</p>
   * @param pAcCr reference
   **/
  public final void setAcCr(final Acnt pAcCr) {
    this.acCr = pAcCr;
  }

  /**
   * <p>Geter for sacTy.</p>
   * @return Integer
   **/
  public final Integer getSacTy() {
    return this.sacTy;
  }

  /**
   * <p>Setter for sacTy.</p>
   * @param pSacTy reference
   **/
  public final void setSacTy(final Integer pSacTy) {
    this.sacTy = pSacTy;
  }

  /**
   * <p>Geter for sacId.</p>
   * @return Long
   **/
  public final Long getSacId() {
    return this.sacId;
  }

  /**
   * <p>Setter for sacId.</p>
   * @param pSacId reference
   **/
  public final void setSacId(final Long pSacId) {
    this.sacId = pSacId;
  }

  /**
   * <p>Geter for sacNm.</p>
   * @return String
   **/
  public final String getSacNm() {
    return this.sacNm;
  }

  /**
   * <p>Setter for sacNm.</p>
   * @param pSacNm reference
   **/
  public final void setSacNm(final String pSacNm) {
    this.sacNm = pSacNm;
  }

  /**
   * <p>Geter for cred.</p>
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
}
