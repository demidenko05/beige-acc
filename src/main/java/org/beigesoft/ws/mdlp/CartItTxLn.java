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

/**
 * <p>Cart item's tax line for item basis multi-taxes non-aggregate rate.</p>
 *
 * @author Yury Demidenko
 */
public class CartItTxLn extends ATxLn implements IOwneda<CartLn> {

  /**
   * <p>Shopping CartLn.</p>
   **/
  private CartLn ownr;

  /**
   * <p>SeSeller ID which items presents in cart,
   * NULL means web-store owner's items.
   * It duplicates owner's seller for performance purposes.</p>
   **/
  private Long selId;

  /**
   * <p>Cart ID (to improve performance).</p>
   **/
  private Long cartId;

  /**
   * <p>Do not show in cart, it's for performance,
   * old purchased cart emptied with this flag,
   * when buyer add new goods to cart then it's used any disabled
   * line (if exist) otherwise new line will be created.</p>
   **/
  private Boolean disab = Boolean.FALSE;

  /**
   * <p>Getter for ownr.</p>
   * @return CartLn
   **/
  @Override
  public final CartLn getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final CartLn pOwnr) {
    this.ownr = pOwnr;
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
   * <p>Getter for cartId.</p>
   * @return Long
   **/
  public final Long getCartId() {
    return this.cartId;
  }

  /**
   * <p>Setter for cartId.</p>
   * @param pCartId reference
   **/
  public final void setCartId(final Long pCartId) {
    this.cartId = pCartId;
  }

  /**
   * <p>Getter for selId.</p>
   * @return Long
   **/
  public final Long getSelId() {
    return this.selId;
  }

  /**
   * <p>Setter for selId.</p>
   * @param pSelId reference
   **/
  public final void setSelId(final Long pSelId) {
    this.selId = pSelId;
  }
}
