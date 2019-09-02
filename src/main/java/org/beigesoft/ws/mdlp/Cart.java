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
import java.math.BigDecimal;

import org.beigesoft.mdl.AHasVr;
import org.beigesoft.acc.mdlp.Curr;
import org.beigesoft.ws.mdl.EDeliv;
import org.beigesoft.ws.mdl.EPaymMth;

/**
 * <p>Model of buyr's cart.</p>
 *
 * @author Yury Demidenko
 */
public class Cart extends AHasVr<Buyer> {

  /**
   * <p>Buyer, PK.</p>
   **/
  private Buyer buyr;

  /**
   * <p>Curr, not null, that buyr opted.</p>
   **/
  private Curr curr;

  /**
   * <p>Exchange exRt for foreign currency, not null, default 1.</p>
   **/
  private BigDecimal exRt = BigDecimal.ONE;

  /**
   * <p>Subtotal, not null.</p>
   **/
  private BigDecimal subt = BigDecimal.ZERO;

  /**
   * <p>Total, not null.</p>
   **/
  private BigDecimal tot = BigDecimal.ZERO;

  /**
   * <p>Total taxes, not null.</p>
   **/
  private BigDecimal toTx = BigDecimal.ZERO;

  /**
   * <p>Items.</p>
   **/
  private List<CartLn> items;

  /**
   * <p>Taxes.</p>
   **/
  private List<CartTxLn> taxes;

  /**
   * <p>Totals grouped by seller.</p>
   **/
  private List<CartTot> totals;

  /**
   * <p>Delivering, not null, buyr pickup itself default.</p>
   **/
  private EDeliv delv = EDeliv.PICKUP;

  /**
   * <p>Buyer is waiting to resolve its problem, e.g. tax destination can't
   * be revealed automatically.</p>
   **/
  private Boolean err = Boolean.FALSE;

  /**
   * <p>Dscription - tax method details.</p>
   **/
  private String dscr;

  /**
   * <p>Payment Method, not null, default in trading settings.</p>
   **/
  private EPaymMth paym;

  /**
   * <p>Usually it's simple getter that return model ID.</p>
   * @return ID model ID
   **/
  @Override
  public final Buyer getIid() {
    return this.buyr;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pIid model ID
   **/
  @Override
  public final void setIid(final Buyer pIid) {
    this.buyr = pIid;
  }

  //Simple getters and setters:
  /**
   * <p>Setter for buyr.</p>
   * @param pBuyer reference
   **/
  public final void setBuyr(final Buyer pBuyer) {
    this.buyr = pBuyer;
  }

  /**
   * <p>Getter for buyr.</p>
   * @return Buyer
   **/
  public final Buyer getBuyr() {
    return this.buyr;
  }

  /**
   * <p>Getter for curr.</p>
   * @return Curr
   **/
  public final Curr getCurr() {
    return this.curr;
  }

  /**
   * <p>Setter for curr.</p>
   * @param pCurr reference
   **/
  public final void setCurr(final Curr pCurr) {
    this.curr = pCurr;
  }

  /**
   * <p>Getter for exRt.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getExRt() {
    return this.exRt;
  }

  /**
   * <p>Setter for exRt.</p>
   * @param pExRt reference
   **/
  public final void setExRt(final BigDecimal pExRt) {
    this.exRt = pExRt;
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
   * <p>Getter for items.</p>
   * @return List<CartLn>
   **/
  public final List<CartLn> getItems() {
    return this.items;
  }

  /**
   * <p>Setter for items.</p>
   * @param pItems reference
   **/
  public final void setItems(final List<CartLn> pItems) {
    this.items = pItems;
  }

  /**
   * <p>Getter for taxes.</p>
   * @return List<CartTxLn>
   **/
  public final List<CartTxLn> getTaxes() {
    return this.taxes;
  }

  /**
   * <p>Setter for taxes.</p>
   * @param pTaxes reference
   **/
  public final void setTaxes(final List<CartTxLn> pTaxes) {
    this.taxes = pTaxes;
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
   * <p>Getter for totals.</p>
   * @return List<CartTot>
   **/
  public final List<CartTot> getTotals() {
    return this.totals;
  }

  /**
   * <p>Setter for totals.</p>
   * @param pTotals reference
   **/
  public final void setTotals(final List<CartTot> pTotals) {
    this.totals = pTotals;
  }

  /**
   * <p>Getter for delv.</p>
   * @return EDeliv
   **/
  public final EDeliv getDelv() {
    return this.delv;
  }

  /**
   * <p>Setter for delv.</p>
   * @param pDeliv reference
   **/
  public final void setDelv(final EDeliv pDeliv) {
    this.delv = pDeliv;
  }

  /**
   * <p>Getter for err.</p>
   * @return Boolean
   **/
  public final Boolean getErr() {
    return this.err;
  }

  /**
   * <p>Setter for err.</p>
   * @param pErr reference
   **/
  public final void setErr(final Boolean pErr) {
    this.err = pErr;
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

  /**
   * <p>Getter for paym.</p>
   * @return EPaymMth
   **/
  public final EPaymMth getPaym() {
    return this.paym;
  }

  /**
   * <p>Setter for paym.</p>
   * @param pPaym reference
   **/
  public final void setPaym(final EPaymMth pPaym) {
    this.paym = pPaym;
  }
}
