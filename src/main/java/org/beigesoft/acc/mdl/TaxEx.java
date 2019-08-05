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

import org.beigesoft.acc.mdlp.Tax;

/**
 * <p>Wrapper of tax with intermediate variables.</p>
 *
 * @author Yury Demidenko
 */
public class TaxEx extends Tax {

  /**
   * <p>Total tax.</p>
   **/
  private BigDecimal toTx;

  /**
   * <p>Total tax FC.</p>
   **/
  private BigDecimal txFc;

  /**
   * <p>Total taxable.</p>
   **/
  private BigDecimal txb;

  /**
   * <p>Total taxable FC.</p>
   **/
  private BigDecimal txbFc;

  /**
   * <p>Explanation.</p>
   **/
  @Override
  public final String toString() {
    return this.getIid() + "/" + this.getNme() + "/" + this.txb + "/"
      + this.txbFc + "/" + this.toTx + "/" + this.txFc;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for toTx.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getToTx() {
    return this.toTx;
  }

  /**
   * <p>Setter for toTx.</p>
   * @param pToTx reference
   **/
  public final void setToTx(final BigDecimal pToTx) {
    this.toTx = pToTx;
  }

  /**
   * <p>Getter for txFc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTxFc() {
    return this.txFc;
  }

  /**
   * <p>Setter for txFc.</p>
   * @param pTxFc reference
   **/
  public final void setTxFc(final BigDecimal pTxFc) {
    this.txFc = pTxFc;
  }

  /**
   * <p>Getter for txb.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTxb() {
    return this.txb;
  }

  /**
   * <p>Setter for txb.</p>
   * @param pTxb reference
   **/
  public final void setTxb(final BigDecimal pTxb) {
    this.txb = pTxb;
  }

  /**
   * <p>Getter for txbFc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTxbFc() {
    return this.txbFc;
  }

  /**
   * <p>Setter for txbFc.</p>
   * @param pTxbFc reference
   **/
  public final void setTxbFc(final BigDecimal pTxbFc) {
    this.txbFc = pTxbFc;
  }
}
