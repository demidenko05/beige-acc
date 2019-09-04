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
import org.beigesoft.acc.mdlp.Uom;
import org.beigesoft.ws.mdlp.PriCt;

/**
 * <p>Model of Itm's price (Goods/Service/SeGoods/SeService).
 * Beige-ORM does not support generic fields!</p>
 *
 * @param <T> itm type
 * @param <ID> ID type
 * @author Yury Demidenko
 */
public abstract class AItmPri<T extends IIdLnNm, ID extends AItmPriId<T>>
  extends AHasVr<ID> {

  /**
   * <p>Price per unit.</p>
   **/
  private BigDecimal pri;

  /**
   * <p>It can be used to implements widely
   * used method "Price down",
   * i.e. previousPrice = 60 against itsPrice = 45, nullable.</p>
   **/
  private BigDecimal priPr = BigDecimal.ZERO;

  /**
   * <p>Unit Of Measure, optional, e.g. per night or per hour,
   * NULL means "#1 each".</p>
   **/
  private Uom uom;

  /**
   * <p>Quantity step, 1 default,
   * e.g. 12USD per 0.5ft, UOM ft, ST=0.5, so
   * buyer can order 0.5/1.0/1.5/2.0/etc. units of item.</p>
   **/
  private BigDecimal unSt = BigDecimal.ONE;

  /**
   * <p>Setter for pPriCt.</p>
   * @param pPriCt reference
   **/
  public abstract void setPriCt(PriCt pPriCt);

  /**
   * <p>Getter for priCt.</p>
   * @return PriCt
   **/
  public abstract PriCt getPriCt();

  /**
   * <p>Getter for itm.</p>
   * @return T
   **/
  public abstract T getItm();

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  public abstract void setItm(final T pItm);

  //Simple getters and setters:
  /**
   * <p>Getter for pri.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getPri() {
    return this.pri;
  }

  /**
   * <p>Setter for pri.</p>
   * @param pPri reference
   **/
  public final void setPri(final BigDecimal pPri) {
    this.pri = pPri;
  }

  /**
   * <p>Getter for priPr.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getPriPr() {
    return this.priPr;
  }

  /**
   * <p>Setter for priPr.</p>
   * @param pPriPr reference
   **/
  public final void setPriPr(final BigDecimal pPriPr) {
    this.priPr = pPriPr;
  }

  /**
   * <p>Getter for uom.</p>
   * @return Uom
   **/
  public final Uom getUom() {
    return this.uom;
  }

  /**
   * <p>Setter for uom.</p>
   * @param pUom reference
   **/
  public final void setUom(final Uom pUom) {
    this.uom = pUom;
  }

  /**
   * <p>Getter for unSt.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getUnSt() {
    return this.unSt;
  }

  /**
   * <p>Setter for unSt.</p>
   * @param pUnSt reference
   **/
  public final void setUnSt(final BigDecimal pUnSt) {
    this.unSt = pUnSt;
  }
}
