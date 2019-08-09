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
 * <p>Tax wrapper with additional information.</p>
 *
 * @author Yury Demidenko
 */
public class TaxWr {

  /**
   * <p>Tax.</p>
   **/
  private Tax tax;

  /**
   * <p>Rate=tax.itsPercentage/100.</p>
   **/
  private BigDecimal rate = BigDecimal.ZERO;

  /**
   * <p>If used.</p>
   **/
  private Boolean used = Boolean.FALSE;

  //Simple getters and setters:
  /**
   * <p>Getter for tax.</p>
   * @return Tax
   **/
  public final Tax getTax() {
    return this.tax;
  }

  /**
   * <p>Setter for tax.</p>
   * @param pTax reference
   **/
  public final void setTax(final Tax pTax) {
    this.tax = pTax;
  }

  /**
   * <p>Getter for rate.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getRate() {
    return this.rate;
  }

  /**
   * <p>Setter for rate.</p>
   * @param pRate reference
   **/
  public final void setRate(final BigDecimal pRate) {
    this.rate = pRate;
  }

  /**
   * <p>Getter for used.</p>
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
}
