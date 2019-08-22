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

import java.util.List;

import org.beigesoft.ws.mdlb.ACuOr;
import org.beigesoft.ws.mdlb.IHsSeSel;

/**
 * <p>
 * Model of Customer Order for S.E. seller's items.
 * It's used to create Sales Invoice.
 * Customer order does neither accounting nor warehouse entries,
 * but it reduces "Goods Available in Place". Canceled order increases
 * back "Goods Available in Place".
 * </p>
 *
 * @author Yury Demidenko
 */
public class CuOrSe extends ACuOr<CuOrSeGdLn, CuOrSeSrLn>
  implements IHsSeSel<Long> {

  /**
   * <p>S.E. seller, not null.</p>
   **/
  private SeSel selr;

  /**
   * <p>Ordered goods.</p>
   **/
  private List<CuOrSeGdLn> goods;

  /**
   * <p>Ordered services.</p>
   **/
  private List<CuOrSeSrLn> servs;

  /**
   * <p>Order's taxes summary.</p>
   **/
  private List<CuOrSeTxLn> taxes;

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

  /**
   * <p>Getter for goods.</p>
   * @return List<CuOrSeGdLn>
   **/
  @Override
  public final List<CuOrSeGdLn> getGoods() {
    return this.goods;
  }

  /**
   * <p>Setter for goods.</p>
   * @param pGoods reference
   **/
  @Override
  public final void setGoods(final List<CuOrSeGdLn> pGoods) {
    this.goods = pGoods;
  }

  /**
   * <p>Getter for servs.</p>
   * @return List<CuOrSeSrLn>
   **/
  @Override
  public final List<CuOrSeSrLn> getServs() {
    return this.servs;
  }

  /**
   * <p>Setter for servs.</p>
   * @param pServs reference
   **/
  @Override
  public final void setServs(final List<CuOrSeSrLn> pServs) {
    this.servs = pServs;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for taxes.</p>
   * @return List<CuOrSeTxLn>
   **/
  public final List<CuOrSeTxLn> getTaxes() {
    return this.taxes;
  }

  /**
   * <p>Setter for taxes.</p>
   * @param pTaxes reference
   **/
  public final void setTaxes(final List<CuOrSeTxLn> pTaxes) {
    this.taxes = pTaxes;
  }
}
