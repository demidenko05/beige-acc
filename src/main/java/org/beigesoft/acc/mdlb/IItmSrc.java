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

/**
 * <p>Model of entity that hold inventory item to draw.
 * It loads(puts) an item into warehouse. E.g. purchase invoice line,
 * beginning inventory line.</p>
 *
 * @author Yury Demidenko
 */
public interface IItmSrc extends IMkWsEnr {

  /**
   * <p>Getter for itLf.</p>
   * @return BigDecimal
   **/
  BigDecimal getItLf();

  /**
   * <p>Setter for itLf.</p>
   * @param pItLf reference
   **/
  void setItLf(BigDecimal pItLf);

  /**
   * <p>Getter for toLf.</p>
   * @return BigDecimal
   **/
  BigDecimal getToLf();

  /**
   * <p>Setter for toLf.</p>
   * @param pToLf reference
   **/
  void setToLf(BigDecimal pToLf);

  /**
   * <p>Getter for initial total - it's subtotal for invoice line.
   * This is because of complex tax calculation. For invoice basis subtotal
   * of a line maybe changed (adjusted) after inserting new one
   * in case of price inclusive of tax.</p>
   * @return BigDecimal
   **/
  BigDecimal getIniTo();

  /**
   * <p>Setter for owner ID if exist.
   * Quick and cheap solution for draw item service.</p>
   * @param pOwnrId owner ID from SQL query
   **/
  void setOwnrId(Long pOwnrId);
}
