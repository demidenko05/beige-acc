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

import java.math.BigDecimal;

import org.beigesoft.mdl.IOwneda;
import org.beigesoft.ws.mdlb.ATxLn;

/**
 * <p>
 * Customer Order Tax Line model.
 * </p>
 *
 * @author Yury Demidenko
 */
public class CuOrTxLn extends ATxLn implements IOwneda<CuOr> {

  /**
   * <p>Customer Order.</p>
   **/
  private CuOr ownr;

  /**
   * <p>Txble amount for invoice basis, 0 - item basis..</p>
   **/
  private BigDecimal txb = BigDecimal.ZERO;

  /**
   * <p>Getter for ownr.</p>
   * @return CuOr
   **/
  @Override
  public final CuOr getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final CuOr pOwnr) {
    this.ownr = pOwnr;
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
