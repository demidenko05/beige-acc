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

package org.beigesoft.acc.mdlp;

import org.beigesoft.mdlp.AI18nNm;
import org.beigesoft.mdlp.Lng;

/**
 * <p>Model of I18N name of currency.</p>
 *
 * @author Yury Demidenko
 */
public class I18Curr extends AI18nNm<Curr, I18CurrId> {

  /**
   * <p>ID.</p>
   **/
  private I18CurrId iid;

  /**
   * <p>Internationalized thing.</p>
   **/
  private Curr hasNm;

  /**
   * <p>The language.</p>
   **/
  private Lng lng;

  /**
   * <p>Not Null, if  if use currency sign in reports (e.g. $),
   * otherwise itsName (e.g. USD).</p>
   **/
  private Boolean curs = false;

  /**
   * <p>Not Null, if print currency on left of amount
   * e.g. "1,356.12$" or "$1,356.12".</p>
   **/
  private Boolean curl = false;

  /**
   * <p>Getter for iid.</p>
   * @return InCurrId
   **/
  @Override
  public final I18CurrId getIid() {
    return this.iid;
  }

  /**
   * <p>Setter for iid.</p>
   * @param pIid reference
   **/
  @Override
  public final void setIid(final I18CurrId pIid) {
    this.iid = pIid;
    if (this.iid == null) {
      this.lng = null;
      this.hasNm = null;
    } else {
      this.lng = this.iid.getLng();
      this.hasNm = this.iid.getHasNm();
    }
  }

  /**
   * <p>Setter for lng.</p>
   * @param pLng reference
   **/
  @Override
  public final void setLng(final Lng pLng) {
    this.lng = pLng;
    if (this.iid == null) {
      this.iid = new I18CurrId();
    }
    this.iid.setLng(this.lng);
  }

  /**
   * <p>Setter for hasNm.</p>
   * @param pHasNm reference
   **/
  @Override
  public final void setHasNm(final Curr pHasNm) {
    this.hasNm = pHasNm;
    if (this.iid == null) {
      this.iid = new I18CurrId();
    }
    this.iid.setHasNm(this.hasNm);
  }

  /**
   * <p>Getter for hasNm.</p>
   * @return Curr
   **/
  @Override
  public final Curr getHasNm() {
    return this.hasNm;
  }

  /**
   * <p>Getter for lng.</p>
   * @return Lng
   **/
  @Override
  public final Lng getLng() {
    return this.lng;
  }

  //SGS:
  /**
   * <p>Getter for curs.</p>
   * @return Boolean
   **/
  public final Boolean getCurs() {
    return this.curs;
  }

  /**
   * <p>Setter for curs.</p>
   * @param pCurs reference
   **/
  public final void setCurs(final Boolean pCurs) {
    this.curs = pCurs;
  }

  /**
   * <p>Getter for curl.</p>
   * @return Boolean
   **/
  public final Boolean getCurl() {
    return this.curl;
  }

  /**
   * <p>Setter for curl.</p>
   * @param pCurl reference
   **/
  public final void setCurl(final Boolean pCurl) {
    this.curl = pCurl;
  }
}
