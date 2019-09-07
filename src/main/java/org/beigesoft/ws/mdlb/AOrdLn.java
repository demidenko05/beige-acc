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

import org.beigesoft.mdlp.AIdLnaNm;
import org.beigesoft.acc.mdlp.Uom;
import org.beigesoft.acc.mdlp.TxCt;

/**
 * <p>
 * Model of Customer Order Item line.
 * Item can be goods/service/S.E. goods/S.E.service.
 * Item has already I18N name (in nme field) same as in the cart.
 * </p>
 *
 * @author Yury Demidenko
 */
public class AOrdLn extends AIdLnaNm {

  /**
   * <p>Unit of measure, not null.</p>
   **/
  private Uom uom;

  /**
   * <p>Pri, not null, grater than zero.</p>
   **/
  private BigDecimal pri;

  /**
   * <p>Quanity, not null.</p>
   **/
  private BigDecimal quan;

  /**
   * <p>Subtotal without taxes.</p>
   **/
  private BigDecimal subt;

  /**
   * <p>Total taxes.</p>
   **/
  private BigDecimal toTx;

  /**
   * <p>Total, not null.</p>
   **/
  private BigDecimal tot;

  /**
   * <p>Tax category, NULL for non-taxable items.</p>
   **/
  private TxCt txCt;

  /**
   * <p>Taxes dscription, uneditable,
   * e.g. "tax1 10%=12, tax2 5%=6".</p>
   **/
  private String tdsc;

  /**
   * <p>Dscription - item details, dynamically,
   * e.g. " at Mon.19" for booking appointment.</p>
   **/
  private String dscr;

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
   * <p>Getter for subt.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getSubt() {
    return this.subt;
  }

  /**
   * <p>Setter for subt.</p>
   * @param pSubt reference
   **/
  public final void setSubt(final BigDecimal pSubt) {
    this.subt = pSubt;
  }

  /**
   * <p>Getter for toTx.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getToTx() {
    return this.toTx;
  }

  /**
   * <p>Setter for toTx.</p>
   * @param pToTx reference
   **/
  public final void setToTx(final BigDecimal pToTx) {
    this.toTx = pToTx;
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

  /**
   * <p>Getter for txCt.</p>
   * @return TxCt
   **/
  public final TxCt getTxCt() {
    return this.txCt;
  }

  /**
   * <p>Setter for txCt.</p>
   * @param pTxCt reference
   **/
  public final void setTxCt(final TxCt pTxCt) {
    this.txCt = pTxCt;
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
