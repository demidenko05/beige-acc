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

import java.util.Date;

import org.beigesoft.mdl.AHasVr;

/**
 * <p>Model of online payment data.</p>
 *
 * @author Yury Demidenko
 */
public class OnlPay extends AHasVr<Buyer> {

  /**
   * <p>Buyer, PK.</p>
   **/
  private Buyer buyr;

  /**
   * <p>Payment ID (from payment gateway), not null.</p>
   **/
  private String payId;

  /**
   * <p>Purchase ID (from order), not null.</p>
   **/
  private Long pur;

  /**
   * <p>Seller, if applied.</p>
   **/
  private SeSel selr;

  /**
   * <p>Date.</p>
   **/
  private Date dat;

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
   * <p>Getter for payId.</p>
   * @return String
   **/
  public final String getPayId() {
    return this.payId;
  }

  /**
   * <p>Setter for payId.</p>
   * @param pPayId reference
   **/
  public final void setPayId(final String pPayId) {
    this.payId = pPayId;
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
   * <p>Getter for selr.</p>
   * @return SeSel
   **/
  public final SeSel getSelr() {
    return this.selr;
  }

  /**
   * <p>Setter for selr.</p>
   * @param pSelr reference
   **/
  public final void setSelr(final SeSel pSelr) {
    this.selr = pSelr;
  }

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
}
