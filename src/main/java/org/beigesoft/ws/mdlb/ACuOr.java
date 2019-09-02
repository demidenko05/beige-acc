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

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

import org.beigesoft.mdlp.AIdLna;
import org.beigesoft.acc.mdlp.Curr;
import org.beigesoft.ws.mdl.EDeliv;
import org.beigesoft.ws.mdl.EPaymMth;
import org.beigesoft.ws.mdl.EOrdStat;
import org.beigesoft.ws.mdlp.Buyer;
import org.beigesoft.ws.mdlp.PicPlc;

/**
 * <p>Abstract model of webstore owner's/S.E.seller customer order.</p>
 *
 * @param <GL> good line type
 * @param <SL> service line type
 * @author Yury Demidenko
 */
public abstract class ACuOr<GL extends AOrdLn, SL extends AOrdLn>
  extends AIdLna {

  /**
   * <p>Its date, not null.</p>
   **/
  private Date dat;

  /**
   * <p>Purchase ID is cart version, not null.</p>
   **/
  private Long pur;

  /**
   * <p>Buyr, not null.</p>
   **/
  private Buyer buyr;

  /**
   * <p>Pipl where goods is stored or service is performed,
   * null if method "pickup by buyr from several pipls"
   * is not implemented/used.</p>
   **/
  private PicPlc pipl;

  /**
   * <p>Payment Method, not null.</p>
   **/
  private EPaymMth paym;

  /**
   * <p>Order stasus, not null, NEW default.</p>
   **/
  private EOrdStat stas = EOrdStat.NEW;

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
   * <p>Dscription.</p>
   **/
  private String dscr;

  /**
   * <p>Delivering, not null.</p>
   **/
  private EDeliv delv;

  /**
   * <p>Getter for goods.</p>
   * @return List<CuOrGdLn>
   **/
  public abstract List<GL> getGoods();

  /**
   * <p>Setter for goods.</p>
   * @param pGoods reference
   **/
  public abstract void setGoods(final List<GL> pGoods);

  /**
   * <p>Getter for servs.</p>
   * @return List<CuOrSrvLn>
   **/
  public abstract List<SL> getServs();

  /**
   * <p>Setter for servs.</p>
   * @param pServs reference
   **/
  public abstract void setServs(final List<SL> pServs);

  //Simple getters and setters:
  /**
   * <p>Getter for dat.</p>
   * @return Date
   **/
  public final Date getDat() {
    return this.dat;
  }

  /**
   * <p>Setter for dat.</p>
   * @param pDat reference
   **/
  public final void setDat(final Date pDat) {
    this.dat = pDat;
  }

  /**
   * <p>Getter for pur.</p>
   * @return Long
   **/
  public final Long getPur() {
    return this.pur;
  }

  /**
   * <p>Setter for pur.</p>
   * @param pPur reference
   **/
  public final void setPur(final Long pPur) {
    this.pur = pPur;
  }

  /**
   * <p>Getter for buyr.</p>
   * @return Buyer
   **/
  public final Buyer getBuyr() {
    return this.buyr;
  }

  /**
   * <p>Setter for buyr.</p>
   * @param pBuyr reference
   **/
  public final void setBuyr(final Buyer pBuyr) {
    this.buyr = pBuyr;
  }

  /**
   * <p>Getter for pipl.</p>
   * @return PicPlc
   **/
  public final PicPlc getPipl() {
    return this.pipl;
  }

  /**
   * <p>Setter for pipl.</p>
   * @param pPipl reference
   **/
  public final void setPipl(final PicPlc pPipl) {
    this.pipl = pPipl;
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

  /**
   * <p>Getter for stas.</p>
   * @return EOrdStat
   **/
  public final EOrdStat getStas() {
    return this.stas;
  }

  /**
   * <p>Setter for stas.</p>
   * @param pStat reference
   **/
  public final void setStas(final EOrdStat pStat) {
    this.stas = pStat;
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
}
