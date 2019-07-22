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
import java.math.BigDecimal;

import org.beigesoft.acc.mdl.EDocTy;
import org.beigesoft.acc.mdlb.ADoci;

/**
 * <p>Model of wage document.</p>
 *
 * @author Yury Demidenko
 */
public class Wage extends ADoci {

  /**
   * <p>Employee.</p>
   **/
  private Empl empl;

  /**
   * <p>Account tax expenses.</p>
   **/
  private Acnt acTx;

  /**
   * <p>Sub-account, not null.</p>
   **/
  private String saNm;

  /**
   * <p>Sub-account, not null.</p>
   **/
  private Long saId;

  /**
   * <p>Sub-account type, not null,  always 1000 - expense.</p>
   **/
  private Integer saTy = 1000;

  /**
   * <p>Taxes due to employer.</p>
   **/
  private BigDecimal txEr = BigDecimal.ZERO;

  /**
   * <p>Taxes due to employee.</p>
   **/
  private BigDecimal txEe = BigDecimal.ZERO;

  /**
   * <p>Net wage = gross wage - taxes due to employee.</p>
   **/
  private BigDecimal ntWg = BigDecimal.ZERO;

  /**
   * <p>Wages lines.</p>
   **/
  private List<WgLn> wags;

  /**
   * <p>Taxes lines.</p>
   **/
  private List<WgTxl> txs;

  /**
   * <p>Constant of code type 15.</p>
   * @return 15
   **/
  @Override
  public final Integer cnsTy() {
    return 15;
  }

  /**
   * <p>Getter of EDocTy.</p>
   * @return EDocTy
   **/
  @Override
  public final EDocTy getDocTy() {
    return EDocTy.ACC;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for empl.</p>
   * @return Empl
   **/
  public final Empl getEmpl() {
    return this.empl;
  }

  /**
   * <p>Setter for empl.</p>
   * @param pEmpl reference
   **/
  public final void setEmpl(final Empl pEmpl) {
    this.empl = pEmpl;
  }

  /**
   * <p>Getter for acTx.</p>
   * @return Acnt
   **/
  public final Acnt getAcTx() {
    return this.acTx;
  }

  /**
   * <p>Setter for acTx.</p>
   * @param pAcTx reference
   **/
  public final void setAcTx(final Acnt pAcTx) {
    this.acTx = pAcTx;
  }

  /**
   * <p>Getter for txEr.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTxEr() {
    return this.txEr;
  }

  /**
   * <p>Setter for txEr.</p>
   * @param pTxEr reference
   **/
  public final void setTxEr(final BigDecimal pTxEr) {
    this.txEr = pTxEr;
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
   * <p>Getter for ntWg.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getNtWg() {
    return this.ntWg;
  }

  /**
   * <p>Setter for ntWg.</p>
   * @param pNtWg reference
   **/
  public final void setNtWg(final BigDecimal pNtWg) {
    this.ntWg = pNtWg;
  }

  /**
   * <p>Getter for wags.</p>
   * @return List<WgLn>
   **/
  public final List<WgLn> getWags() {
    return this.wags;
  }

  /**
   * <p>Setter for wags.</p>
   * @param pWags reference
   **/
  public final void setWags(final List<WgLn> pWags) {
    this.wags = pWags;
  }

  /**
   * <p>Getter for txs.</p>
   * @return List<WgTxl>
   **/
  public final List<WgTxl> getTxs() {
    return this.txs;
  }

  /**
   * <p>Setter for txs.</p>
   * @param pTxs reference
   **/
  public final void setTxs(final List<WgTxl> pTxs) {
    this.txs = pTxs;
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
}
