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

package org.beigesoft.ws.mdlb;

import java.math.BigDecimal;

import org.beigesoft.mdl.AHasVr;
import org.beigesoft.mdl.IIdLnNm;
import org.beigesoft.ws.mdlp.PicPlc;

/**
 * <p>
 * Model of Item availability at place (Goods/Service/SeGoods/SeService).
 * Beige-ORM does not support generic fields!
 * </p>
 *
 * @param <T> item type
 * @param <ID> ID type
 * @author Yury Demidenko
 */
public abstract class AItmPlc<T extends IIdLnNm, ID extends AItmPlcId<T>>
  extends AHasVr<ID> {

  /**
   * <p>It's more or equals zero, if available then must be more than zero
   * cause performance optimization (filter only "quantity>0").
   * Set it to zero to get any item (goods/service) out of list.</p>
   **/
  private BigDecimal quan;

  /**
   * <p>To switch method <b>Always available</b>.</p>
   **/
  private Boolean alway;

  /**
   * <p>Setter for Place.</p>
   * @param pPlace reference
   **/
  public abstract void setPipl(PicPlc pPlace);

  /**
   * <p>Getter for Place.</p>
   * @return PicPlc
   **/
  public abstract PicPlc getPipl();

  /**
   * <p>Getter for item.</p>
   * @return T
   **/
  public abstract T getItm();

  /**
   * <p>Setter for item.</p>
   * @param pItm reference
   **/
  public abstract void setItm(T pItm);

  //Simple getters and setters:
  /**
   * <p>Getter for quan.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getQuan() {
    return this.quan;
  }

  /**
   * <p>Setter for quan.</p>
   * @param pQuan reference
   **/
  public final void setQuan(final BigDecimal pQuan) {
    this.quan = pQuan;
  }

  /**
   * <p>Getter for alway.</p>
   * @return Boolean
   **/
  public final Boolean getAlway() {
    return this.alway;
  }

  /**
   * <p>Setter for alway.</p>
   * @param pAlway reference
   **/
  public final void setAlway(final Boolean pAlway) {
    this.alway = pAlway;
  }
}
