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

package org.beigesoft.acc.mdlb;

import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.acc.mdlp.Curr;
import org.beigesoft.acc.mdlp.DbCr;

/**
 * <p>Base model of invoice.</p>
 *
 * @author Yury Demidenko
 */
public abstract class AInv extends ADoc implements IInv {

  /**
   * <p>Debtor/creditor.</p>
   **/
  private DbCr dbcr;

  /**
   * <p>Foreign currency, if used.</p>
   **/
  private Curr cuFr;

  /**
   * <p>Exchange rate for foreign currency.</p>
   **/
  private BigDecimal exRt = BigDecimal.ZERO;

  /**
   * <p>Subtotal.</p>
   **/
  private BigDecimal subt = BigDecimal.ZERO;

  /**
   * <p>Subtotal in foreign currency.</p>
   **/
  private BigDecimal suFc = BigDecimal.ZERO;

  /**
   * <p>Total taxes.</p>
   **/
  private BigDecimal toTx = BigDecimal.ZERO;

  /**
   * <p>Total taxes in foreign currency.</p>
   **/
  private BigDecimal txFc = BigDecimal.ZERO;

  /**
   * <p>If price inclusive of taxes.</p>
   **/
  private Boolean inTx;

  /**
   * <p>If omit taxes.</p>
   **/
  private Boolean omTx;

  /**
   * <p>Pay by date, if used.</p>
   **/
  private Date payb;

  /**
   * <p>Total payments.</p>
   **/
  private BigDecimal toPa = BigDecimal.ZERO;

  /**
   * <p>Total payments FC.</p>
   **/
  private BigDecimal paFc = BigDecimal.ZERO;

  /**
   * <p>Payments description.</p>
   **/
  private String pdsc;

  /**
   * <p>Getter for dbcr.</p>
   * @return DbCr
   **/
  @Override
  public final DbCr getDbcr() {
    return this.dbcr;
  }

  /**
   * <p>Setter for dbcr.</p>
   * @param pDbcr reference
   **/
  @Override
  public final void setDbcr(final DbCr pDbcr) {
    this.dbcr = pDbcr;
  }

  /**
   * <p>Getter for cuFr.</p>
   * @return Curr
   **/
  @Override
  public final Curr getCuFr() {
    return this.cuFr;
  }

  /**
   * <p>Setter for cuFr.</p>
   * @param pCuFr reference
   **/
  @Override
  public final void setCuFr(final Curr pCuFr) {
    this.cuFr = pCuFr;
  }

  /**
   * <p>Getter for exRt.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getExRt() {
    return this.exRt;
  }

  /**
   * <p>Setter for exRt.</p>
   * @param pExRt reference
   **/
  @Override
  public final void setExRt(final BigDecimal pExRt) {
    this.exRt = pExRt;
  }

  /**
   * <p>Getter for subt.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getSubt() {
    return this.subt;
  }

  /**
   * <p>Setter for subt.</p>
   * @param pSubt reference
   **/
  @Override
  public final void setSubt(final BigDecimal pSubt) {
    this.subt = pSubt;
  }

  /**
   * <p>Getter for suFc.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getSuFc() {
    return this.suFc;
  }

  /**
   * <p>Setter for suFc.</p>
   * @param pSuFc reference
   **/
  @Override
  public final void setSuFc(final BigDecimal pSuFc) {
    this.suFc = pSuFc;
  }

  /**
   * <p>Getter for toTx.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getToTx() {
    return this.toTx;
  }

  /**
   * <p>Setter for toTx.</p>
   * @param pToTx reference
   **/
  @Override
  public final void setToTx(final BigDecimal pToTx) {
    this.toTx = pToTx;
  }

  /**
   * <p>Getter for txFc.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getTxFc() {
    return this.txFc;
  }

  /**
   * <p>Setter for txFc.</p>
   * @param pTxFc reference
   **/
  @Override
  public final void setTxFc(final BigDecimal pTxFc) {
    this.txFc = pTxFc;
  }

  /**
   * <p>Getter for inTx.</p>
   * @return Boolean
   **/
  @Override
  public final Boolean getInTx() {
    return this.inTx;
  }

  /**
   * <p>Setter for inTx.</p>
   * @param pInTx reference
   **/
  @Override
  public final void setInTx(final Boolean pInTx) {
    this.inTx = pInTx;
  }

  /**
   * <p>Getter for omTx.</p>
   * @return Boolean
   **/
  @Override
  public final Boolean getOmTx() {
    return this.omTx;
  }

  /**
   * <p>Setter for omTx.</p>
   * @param pOmTx reference
   **/
  @Override
  public final void setOmTx(final Boolean pOmTx) {
    this.omTx = pOmTx;
  }

  /**
   * <p>Getter for payb.</p>
   * @return Date
   **/
  @Override
  public final Date getPayb() {
    return this.payb;
  }

  /**
   * <p>Setter for payb.</p>
   * @param pPayb reference
   **/
  @Override
  public final void setPayb(final Date pPayb) {
    this.payb = pPayb;
  }

  /**
   * <p>Getter for toPa.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getToPa() {
    return this.toPa;
  }

  /**
   * <p>Setter for toPa.</p>
   * @param pToPa reference
   **/
  @Override
  public final void setToPa(final BigDecimal pToPa) {
    this.toPa = pToPa;
  }

  /**
   * <p>Getter for paFc.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getPaFc() {
    return this.paFc;
  }

  /**
   * <p>Setter for paFc.</p>
   * @param pPaFc reference
   **/
  @Override
  public final void setPaFc(final BigDecimal pPaFc) {
    this.paFc = pPaFc;
  }

  /**
   * <p>Getter for pdsc.</p>
   * @return String
   **/
  @Override
  public final String getPdsc() {
    return this.pdsc;
  }

  /**
   * <p>Setter for pdsc.</p>
   * @param pPdsc reference
   **/
  @Override
  public final void setPdsc(final String pPdsc) {
    this.pdsc = pPdsc;
  }
}
