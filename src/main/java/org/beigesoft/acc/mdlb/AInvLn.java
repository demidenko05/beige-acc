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
import org.beigesoft.acc.mdlp.Uom;

/**
 * <p>Base model of invoice.</p>
 *
 * @param <T> invoice type
 * @param <I> item type
 * @author Yury Demidenko
 */
public abstract class AInvLn<T extends AInv, I extends AItm<?, ?>>
  extends TxDtLn implements IOwned<T, Long> {

  /**
   * <p>Reversed ID.</p>
   **/
  private Long rvId;

  /**
   * <p>Unit of measure.</p>
   **/
  private Uom uom;

  /**
   * <p>Price.</p>
   **/
  private BigDecimal pri = BigDecimal.ZERO;

  /**
   * <p>Price in foreign currency.</p>
   **/
  private BigDecimal prFc = BigDecimal.ZERO;

  /**
   * <p>Quantity.</p>
   **/
  private BigDecimal quan = BigDecimal.ZERO;

  /**
   * <p>Tax description.</p>
   **/
  private String tdsc;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

  /**
   * <p>Getter for itm.</p>
   * @return AItm<?, ?>
   **/
  public abstract I getItm();

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  public abstract void setItm(I pItm);

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
   * <p>Getter for prFc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getPrFc() {
    return this.prFc;
  }

  /**
   * <p>Setter for prFc.</p>
   * @param pPrFc reference
   **/
  public final void setPrFc(final BigDecimal pPrFc) {
    this.prFc = pPrFc;
  }

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
   * <p>Getter for tdsc.</p>
   * @return String
   **/
  public final String getTdsc() {
    return this.tdsc;
  }

  /**
   * <p>Setter for tdsc.</p>
   * @param pTdsc reference
   **/
  public final void setTdsc(final String pTdsc) {
    this.tdsc = pTdsc;
  }

  /**
   * <p>Getter for dscr.</p>
   * @return String
   **/
  public final String getDscr() {
    return this.dscr;
  }

  /**
   * <p>Setter for dscr.</p>
   * @param pDscr reference
   **/
  public final void setDscr(final String pDscr) {
    this.dscr = pDscr;
  }
}
