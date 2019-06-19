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

import org.beigesoft.mdlp.AOrId;
import org.beigesoft.acc.mdlp.Tax;

/**
 * <p>Abstract model of document tax line. It holds grouped total tax amount
 * for whole document for any method - invoice/item basis,
 * price included/excluded. It used for making resulting accounting tax
 * entries, e.g. sales invoice:
 * Debit AccReceivable.Customer Credit SalesTaxPayable.Tax for total amount.</p>
 *
 * @author Yury Demidenko
 */
public abstract class ADcTxLn extends AOrId {

  /**
   * <p>ID of reversed/reversing document tax line.</p>
   **/
  private Long rvId;

  /**
   * <p>Tax.</p>
   **/
  private Tax tax;

  /**
   * <p>Total.</p>
   **/
  private BigDecimal tot = BigDecimal.ZERO;

  //Simple getters and setters:
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
   * <p>Getter for tot.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTot() {
    return this.tot;
  }

  /**
   * <p>Setter for tot.</p>
   * @param pTot reference
   **/
  public final void setTot(final BigDecimal pTot) {
    this.tot = pTot;
  }
}
