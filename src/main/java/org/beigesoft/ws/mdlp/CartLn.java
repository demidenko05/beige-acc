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
import java.util.Date;

import org.beigesoft.mdl.IOwneda;
import org.beigesoft.mdlp.AIdLnNm;
import org.beigesoft.acc.mdlp.TxCt;
import org.beigesoft.acc.mdlp.Uom;
import org.beigesoft.ws.mdl.EItmTy;
import org.beigesoft.ws.mdlb.IHsSeSel;

/**
 * <p>Model of Cart Line.</p>
 *
 * @author Yury Demidenko
 */
public class CartLn extends AIdLnNm implements IOwneda<Cart>, IHsSeSel<Long> {

  /**
   * <p>Shopping Cart.</p>
   **/
  private Cart ownr;

  /**
   * <p>Do not show in cart, it's for performance,
   * old purchased cart emptied with this flag,
   * when buyer add new goods to cart then it's used any disabled
   * line (if exist) otherwise new line will be created.</p>
   **/
  private Boolean disab = Boolean.FALSE;

  /**
   * <p>Shop Item Type, not null.</p>
   **/
  private EItmTy itTyp;

  /**
   * <p>S.E.Seller which item presents in cart,
   * NULL means web-store owner's item.</p>
   **/
  private SeSel selr;

  /**
   * <p>Item ID, not null.</p>
   **/
  private Long itId;

  /**
   * <p>Pri, not null, grater than zero.</p>
   **/
  private BigDecimal pri = BigDecimal.ZERO;

  /**
   * <p>Quanity, not null.</p>
   **/
  private BigDecimal quan = BigDecimal.ZERO;

  /**
   * <p>Unit of measure, not null.</p>
   **/
  private Uom uom;

  /**
   * <p>Subtotal without taxes.</p>
   **/
  private BigDecimal subt = BigDecimal.ZERO;

  /**
   * <p>Total taxes.</p>
   **/
  private BigDecimal toTx = BigDecimal.ZERO;

  /**
   * <p>Taxes description, uneditable,
   * e.g. "tax1 10%=12, tax2 5%=6".</p>
   **/
  private String tdsc;

  /**
   * <p>Total, not null.</p>
   **/
  private BigDecimal tot = BigDecimal.ZERO;

  /**
   * <p>Available quanity, not null.</p>
   **/
  private BigDecimal avQuan = BigDecimal.ZERO;

  /**
   * <p>Tax category, NULL for non-taxable items.</p>
   **/
  private TxCt txCt;

  /**
   * <p>Quanity step, 1 default,
   * e.g. 12USD per 0.5ft, UOM ft, ST=0.5, so
   * buyer can order 0.5/1.0/1.5/2.0/etc. units of item.</p>
   **/
  private BigDecimal unSt = BigDecimal.ONE;

  /**
   * <p>forced (user can't change/delete it) item,
   * e.g. service "delivering by mail".</p>
   **/
  private Boolean forc = Boolean.FALSE;

  /**
   * <p>Nullable, booking from date1 (include) for bookable service only.</p>
   **/
  private Date dt1;

  /**
   * <p>Nullable, booking till date2 (exclude) for bookable service only.</p>
   **/
  private Date dt2;

  /**
   * <p>Dscription - item details, dynamically,
   * e.g. " at Mon.19" for booking appointment.</p>
   **/
  private String dscr;

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

  //Simple getters and setters:
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
   * <p>Getter for itTyp.</p>
   * @return EItmTy
   **/
  public final EItmTy getItTyp() {
    return this.itTyp;
  }

  /**
   * <p>Setter for itTyp.</p>
   * @param pItTyp reference
   **/
  public final void setItTyp(final EItmTy pItTyp) {
    this.itTyp = pItTyp;
  }

  /**
   * <p>Getter for itId.</p>
   * @return Long
   **/
  public final Long getItId() {
    return this.itId;
  }

  /**
   * <p>Setter for itId.</p>
   * @param pItId reference
   **/
  public final void setItId(final Long pItId) {
    this.itId = pItId;
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
   * <p>Getter for avQuan.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getAvQuan() {
    return this.avQuan;
  }

  /**
   * <p>Setter for avQuan.</p>
   * @param pAvQuan reference
   **/
  public final void setAvQuan(final BigDecimal pAvQuan) {
    this.avQuan = pAvQuan;
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

  /**
   * <p>Getter for forc.</p>
   * @return Boolean
   **/
  public final Boolean getForc() {
    return this.forc;
  }

  /**
   * <p>Setter for forc.</p>
   * @param pForc reference
   **/
  public final void setForc(final Boolean pForc) {
    this.forc = pForc;
  }

  /**
   * <p>Getter for dt1.</p>
   * @return Date
   **/
  public final Date getDt1() {
    return this.dt1;
  }

  /**
   * <p>Setter for dt1.</p>
   * @param pDt1 reference
   **/
  public final void setDt1(final Date pDt1) {
    this.dt1 = pDt1;
  }

  /**
   * <p>Getter for dt2.</p>
   * @return Date
   **/
  public final Date getDt2() {
    return this.dt2;
  }

  /**
   * <p>Setter for dt2.</p>
   * @param pDt2 reference
   **/
  public final void setDt2(final Date pDt2) {
    this.dt2 = pDt2;
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
