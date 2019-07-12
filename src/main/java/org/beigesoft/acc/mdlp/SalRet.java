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
import org.beigesoft.acc.mdlb.ADoc;
import org.beigesoft.acc.mdlb.IRet;
import org.beigesoft.acc.mdlb.IDcDri;

/**
 * <p>Model of sales return.</p>
 *
 * @author Yury Demidenko
 */
public class SalRet extends ADoc implements IRet<SalInv>, IDcDri<DrItEnr> {

  /**
   * <p>Invoice.</p>
   **/
  private SalInv inv;

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
   * <p>Tax lines.</p>
   **/
  private List<SaRtTxLn> txLns;

  /**
   * <p>Goods lines.</p>
   **/
  private List<SaRtLn> gdLns;

  /**
   * <p>Constant of code type.</p>
   * @return 9
   **/
  @Override
  public final Integer cnsTy() {
    return 9;
  }

  /**
   * <p>Getter of EDocTy.</p>
   * @return EDocTy
   **/
  @Override
  public final EDocTy getDocTy() {
    return EDocTy.ITSRLN;
  }

  /**
   * <p>Getter for draw item entry class.</p>
   * @return draw item entry class
   **/
  @Override
  public final Class<DrItEnr> getEnrCls() {
    return DrItEnr.class;
  }

  /**
   * <p>Getter for dbcr.</p>
   * @return DbCr
   **/
  @Override
  public final DbCr getDbcr() {
    if (this.inv == null) {
      return null;
    }
    return this.inv.getDbcr();
  }

  /**
   * <p>Setter for dbcr.</p>
   * @param pDbcr reference
   **/
  @Override
  public final void setDbcr(final DbCr pDbcr) {
    throw new RuntimeException("Not allowed!");
  }

  /**
   * <p>Getter for cuFr.</p>
   * @return Curr
   **/
  @Override
  public final Curr getCuFr() {
    if (this.inv == null) {
      return null;
    }
    return this.inv.getCuFr();
  }

  /**
   * <p>Setter for cuFr.</p>
   * @param pCuFr reference
   **/
  @Override
  public final void setCuFr(final Curr pCuFr) {
    throw new RuntimeException("Not allowed!");
  }

  /**
   * <p>Getter for exRt.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getExRt() {
    if (this.inv == null) {
      return null;
    }
    return this.inv.getExRt();
  }

  /**
   * <p>Setter for exRt.</p>
   * @param pExRt reference
   **/
  @Override
  public final void setExRt(final BigDecimal pExRt) {
    throw new RuntimeException("Not allowed!");
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
    if (this.inv == null) {
      return null;
    }
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
    if (this.inv == null) {
      return null;
    }
    return this.inv.getInTx();
  }

  /**
   * <p>Setter for inTx.</p>
   * @param pInTx reference
   **/
  @Override
  public final void setInTx(final Boolean pInTx) {
    throw new RuntimeException("Not allowed!");
  }

  /**
   * <p>Getter for omTx.</p>
   * @return Boolean
   **/
  @Override
  public final Boolean getOmTx() {
    if (this.inv == null) {
      return null;
    }
    return this.inv.getOmTx();
  }

  /**
   * <p>Setter for omTx.</p>
   * @param pOmTx reference
   **/
  @Override
  public final void setOmTx(final Boolean pOmTx) {
    throw new RuntimeException("Not allowed!");
  }

  /**
   * <p>Getter for inv.</p>
   * @return SalInv
   **/
  @Override
  public final SalInv getInv() {
    return this.inv;
  }

  /**
   * <p>Setter for inv.</p>
   * @param pInv reference
   **/
  @Override
  public final void setInv(final SalInv pInv) {
    this.inv = pInv;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for txLns.</p>
   * @return List<SaRtTxLn>
   **/
  public final List<SaRtTxLn> getTxLns() {
    return this.txLns;
  }

  /**
   * <p>Setter for txLns.</p>
   * @param pTxLns reference
   **/
  public final void setTxLns(final List<SaRtTxLn> pTxLns) {
    this.txLns = pTxLns;
  }

  /**
   * <p>Getter for gdLns.</p>
   * @return List<SaRtLn>
   **/
  public final List<SaRtLn> getGdLns() {
    return this.gdLns;
  }

  /**
   * <p>Setter for gdLns.</p>
   * @param pGdLns reference
   **/
  public final void setGdLns(final List<SaRtLn> pGdLns) {
    this.gdLns = pGdLns;
  }
}
