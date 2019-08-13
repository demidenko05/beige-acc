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

import org.beigesoft.mdlp.AIdLnNm;
import org.beigesoft.ws.mdl.EPaymMth;

/**
 * <p>Trading settings, field nme is used as web-store name.</p>
 *
 * @author Yury Demidenko
 */
public class TrdStg extends AIdLnNm {

  /**
   * <p>Not Null, default false,
   * if use <b>Price for customer</b> method.</p>
   **/
  private Boolean priCus;

  /**
   * <p>not null, default 5, maximum quantity
   * of top level catalogs shown in menu,
   * others will be in drop-down menu "others".</p>
   **/
  private Integer mqtc = 5;

  /**
   * <p>Catalog Of Goods/Services, nullable, In case of little catalog to list
   * all goods on start without clicking on "menu-[catalog]",
   * or it's catalog that offers different goods/services for all on start.</p>
   **/
  private CatGs catl;

  /**
   * <p>not null, default 2, items list columns count.</p>
   **/
  private Integer colCnt = 2;

  /**
   * <p>Not null, false default, Use advanced internalization.</p>
   **/
  private Boolean ai18n = false;

  /**
   * <p>Default payment method, not null, PAY_CASH default.</p>
   **/
  private EPaymMth paym = EPaymMth.PAY_CASH;

  /**
   * <p>If taxes excluded, default FALSE (included).</p>
   **/
  private Boolean txExcl = Boolean.FALSE;

  /**
   * <p>Not null, false default, Use "in country" tax destinations.</p>
   **/
  private Boolean utxds = Boolean.FALSE;

  //Simple getters and setters:
  /**
   * <p>Getter for priCus.</p>
   * @return Boolean
   **/
  public final Boolean getPriCus() {
    return this.priCus;
  }

  /**
   * <p>Setter for priCus.</p>
   * @param pPriCus reference
   **/
  public final void setPriCus(final Boolean pPriCus) {
    this.priCus = pPriCus;
  }

  /**
   * <p>Getter for mqtc.</p>
   * @return Integer
   **/
  public final Integer getMqtc() {
    return this.mqtc;
  }

  /**
   * <p>Setter for mqtc.</p>
   * @param pMqtc reference
   **/
  public final void setMqtc(final Integer pMqtc) {
    this.mqtc = pMqtc;
  }

  /**
   * <p>Getter for catl.</p>
   * @return CatGs
   **/
  public final CatGs getCatl() {
    return this.catl;
  }

  /**
   * <p>Setter for catl.</p>
   * @param pCatl reference
   **/
  public final void setCatl(final CatGs pCatl) {
    this.catl = pCatl;
  }

  /**
   * <p>Getter for colCnt.</p>
   * @return Integer
   **/
  public final Integer getColCnt() {
    return this.colCnt;
  }

  /**
   * <p>Setter for colCnt.</p>
   * @param pColCnt reference
   **/
  public final void setColCnt(final Integer pColCnt) {
    this.colCnt = pColCnt;
  }

  /**
   * <p>Getter for ai18n.</p>
   * @return Boolean
   **/
  public final Boolean getAi18n() {
    return this.ai18n;
  }

  /**
   * <p>Setter for ai18n.</p>
   * @param pAi18n reference
   **/
  public final void setAi18n(final Boolean pAi18n) {
    this.ai18n = pAi18n;
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
   * <p>Getter for txExcl.</p>
   * @return Boolean
   **/
  public final Boolean getTxExcl() {
    return this.txExcl;
  }

  /**
   * <p>Setter for txExcl.</p>
   * @param pTxExcl reference
   **/
  public final void setTxExcl(final Boolean pTxExcl) {
    this.txExcl = pTxExcl;
  }

  /**
   * <p>Getter for utxds.</p>
   * @return Boolean
   **/
  public final Boolean getUtxds() {
    return this.utxds;
  }

  /**
   * <p>Setter for utxds.</p>
   * @param pUtxds reference
   **/
  public final void setUtxds(final Boolean pUtxds) {
    this.utxds = pUtxds;
  }
}
