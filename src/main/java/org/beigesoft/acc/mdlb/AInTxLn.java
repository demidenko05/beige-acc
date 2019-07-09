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

import java.math.BigDecimal;

import org.beigesoft.mdl.IOwned;

/**
 * <p>Abstract model of invoice tax line.</p>
 *
 * @param <T> invoice type
 * @author Yury Demidenko
 */
public abstract class AInTxLn<T extends IInv> extends ADcTxLn
  implements IOwned<T, Long> {

  /**
   * <p>Total FC.</p>
   **/
  private BigDecimal toFc = BigDecimal.ZERO;

  /**
   * <p>Taxable amount for invoice basis.</p>
   **/
  private BigDecimal txb = BigDecimal.ZERO;

  /**
   * <p>Taxable amount for invoice basis FC.</p>
   **/
  private BigDecimal txbFc = BigDecimal.ZERO;

  //Simple getters and setters:
  /**
   * <p>Getter for toFc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getToFc() {
    return this.toFc;
  }

  /**
   * <p>Setter for toFc.</p>
   * @param pToFc reference
   **/
  public final void setToFc(final BigDecimal pToFc) {
    this.toFc = pToFc;
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
