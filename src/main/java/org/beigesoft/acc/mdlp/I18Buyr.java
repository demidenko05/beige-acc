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
 * <p>Model of I18N name of buyer.</p>
 *
 * @author Yury Demidenko
 */
public class I18Buyr extends AI18nNm<DbCr, I18BuyrId> {

  /**
   * <p>ID.</p>
   **/
  private I18BuyrId iid;

  /**
   * <p>Internationalized thing.</p>
   **/
  private DbCr hasNm;

  /**
   * <p>The language.</p>
   **/
  private Lng lng;

  /**
   * <p>Registered address1.</p>
   **/
  private String addr1;

  /**
   * <p>Registered address2.</p>
   **/
  private String addr2;

  /**
   * <p>Registered Country.</p>
   **/
  private String cntr;

  /**
   * <p>Registered State.</p>
   **/
  private String stat;

  /**
   * <p>Registered City.</p>
   **/
  private String city;

  /**
   * <p>Getter for iid.</p>
   * @return InCurrId
   **/
  @Override
  public final I18BuyrId getIid() {
    return this.iid;
  }

  /**
   * <p>Setter for iid.</p>
   * @param pIid reference
   **/
  @Override
  public final void setIid(final I18BuyrId pIid) {
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
      this.iid = new I18BuyrId();
    }
    this.iid.setLng(this.lng);
  }

  /**
   * <p>Setter for hasNm.</p>
   * @param pHasNm reference
   **/
  @Override
  public final void setHasNm(final DbCr pHasNm) {
    this.hasNm = pHasNm;
    if (this.iid == null) {
      this.iid = new I18BuyrId();
    }
    this.iid.setHasNm(this.hasNm);
  }

  /**
   * <p>Getter for hasNm.</p>
   * @return DbCr
   **/
  @Override
  public final DbCr getHasNm() {
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
   * <p>Getter for addr1.</p>
   * @return String
   **/
  public final String getAddr1() {
    return this.addr1;
  }

  /**
   * <p>Setter for addr1.</p>
   * @param pAddr1 reference
   **/
  public final void setAddr1(final String pAddr1) {
    this.addr1 = pAddr1;
  }

  /**
   * <p>Getter for addr2.</p>
   * @return String
   **/
  public final String getAddr2() {
    return this.addr2;
  }

  /**
   * <p>Setter for addr2.</p>
   * @param pAddr2 reference
   **/
  public final void setAddr2(final String pAddr2) {
    this.addr2 = pAddr2;
  }

  /**
   * <p>Getter for cntr.</p>
   * @return String
   **/
  public final String getCntr() {
    return this.cntr;
  }

  /**
   * <p>Setter for cntr.</p>
   * @param pCntr reference
   **/
  public final void setCntr(final String pCntr) {
    this.cntr = pCntr;
  }

  /**
   * <p>Getter for stat.</p>
   * @return String
   **/
  public final String getStat() {
    return this.stat;
  }

  /**
   * <p>Setter for stat.</p>
   * @param pStat reference
   **/
  public final void setStat(final String pStat) {
    this.stat = pStat;
  }

  /**
   * <p>Getter for city.</p>
   * @return String
   **/
  public final String getCity() {
    return this.city;
  }

  /**
   * <p>Setter for city.</p>
   * @param pCity reference
   **/
  public final void setCity(final String pCity) {
    this.city = pCity;
  }
}
