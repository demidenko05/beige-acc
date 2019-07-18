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

import org.beigesoft.mdlp.AIdLnNm;
import org.beigesoft.acc.mdlb.ISacnt;

/**
 * <p>Model of debtor/creditor, i.e. a customer or a supplier.</p>
 *
 * @author Yury Demidenko
 */
public class DbCr extends AIdLnNm implements ISacnt {

  /**
   * <p>Category, not null.</p>
   **/
  private DcrCt cat;

  /**
   * <p>Only for overseas/overstate buyers/vendors.</p>
   **/
  private TxDst txDs;

  /**
   * <p>Registered email.</p>
   **/
  private String eml;

  /**
   * <p>Registered address1.</p>
   **/
  private String addr1;

  /**
   * <p>Registered address2.</p>
   **/
  private String addr2;

  /**
   * <p>Registered Zip.</p>
   **/
  private String zip;

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
   * <p>Registered Phone.</p>
   **/
  private String phon;

  /**
   * <p>Tax identification number e.g. SSN for US.</p>
   **/
  private String tin;

  /**
   * <p>OOP friendly Constant of code type 1002.</p>
   * @return 1002
   **/
  @Override
  public final Integer cnsTy() {
    return 1002;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for cat.</p>
   * @return DcrCt
   **/
  public final DcrCt getCat() {
    return this.cat;
  }

  /**
   * <p>Setter for cat.</p>
   * @param pCat reference
   **/
  public final void setCat(final DcrCt pCat) {
    this.cat = pCat;
  }

  /**
   * <p>Getter for txDs.</p>
   * @return TxDst
   **/
  public final TxDst getTxDs() {
    return this.txDs;
  }

  /**
   * <p>Setter for txDs.</p>
   * @param pTxDs reference
   **/
  public final void setTxDs(final TxDst pTxDs) {
    this.txDs = pTxDs;
  }

  /**
   * <p>Getter for eml.</p>
   * @return String
   **/
  public final String getEml() {
    return this.eml;
  }

  /**
   * <p>Setter for eml.</p>
   * @param pEml reference
   **/
  public final void setEml(final String pEml) {
    this.eml = pEml;
  }

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
   * <p>Getter for zip.</p>
   * @return String
   **/
  public final String getZip() {
    return this.zip;
  }

  /**
   * <p>Setter for zip.</p>
   * @param pZip reference
   **/
  public final void setZip(final String pZip) {
    this.zip = pZip;
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

  /**
   * <p>Getter for phon.</p>
   * @return String
   **/
  public final String getPhon() {
    return this.phon;
  }

  /**
   * <p>Setter for phon.</p>
   * @param pPhon reference
   **/
  public final void setPhon(final String pPhon) {
    this.phon = pPhon;
  }

  /**
   * <p>Getter for tin.</p>
   * @return String
   **/
  public final String getTin() {
    return this.tin;
  }

  /**
   * <p>Setter for tin.</p>
   * @param pTin reference
   **/
  public final void setTin(final String pTin) {
    this.tin = pTin;
  }
}
