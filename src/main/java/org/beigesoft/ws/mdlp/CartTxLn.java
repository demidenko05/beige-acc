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

package org.beigesoft.ws.mdlp;

import org.beigesoft.mdl.IOwneda;
import org.beigesoft.ws.mdlb.ATxLn;
import org.beigesoft.ws.mdlb.IHsSeSel;

import java.math.BigDecimal;

/**
 * <p>Shoping Cart Tax Line model.
 * It holds total taxes grouped by invoice (seller).</p>
 *
 * @author Yury Demidenko
 */
public class CartTxLn extends ATxLn implements IOwneda<Cart>, IHsSeSel<Long> {

  /**
   * <p>Shopping Cart.</p>
   **/
  private Cart ownr;

  /**
   * <p>SeSel which items presents in cart,
   * NULL means web-store owner's items.</p>
   **/
  private SeSel selr;

  /**
   * <p>Txble amount for invoice basis, 0 - item basis..</p>
   **/
  private BigDecimal txb = BigDecimal.ZERO;

  /**
   * <p>Do not show in cart, it's for performance,
   * old purchased cart emptied with this flag,
   * when buyer add new goods to cart then it's used any disabled
   * line (if exist) otherwise new line will be created.</p>
   **/
  private Boolean disab = Boolean.FALSE;

  /**
   * <p>Getter for ownr.</p>
   * @return Cart
   **/
  @Override
  public final Cart getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final Cart pOwnr) {
    this.ownr = pOwnr;
  }

  /**
   * <p>Getter for seller.</p>
   * @return SeSel
   **/
  @Override
  public final SeSel getSelr() {
    return this.selr;
  }

  /**
   * <p>Setter for seller.</p>
   * @param pSeller reference
   **/
  @Override
  public final void setSelr(final SeSel pSeller) {
    this.selr = pSeller;
  }

  //SGS:
  /**
   * <p>Getter for disab.</p>
   * @return Boolean
   **/
  public final Boolean getDisab() {
    return this.disab;
  }

  /**
   * <p>Setter for disab.</p>
   * @param pDisab reference
   **/
  public final void setDisab(final Boolean pDisab) {
    this.disab = pDisab;
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
}
