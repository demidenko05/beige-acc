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

import org.beigesoft.mdl.IIdLna;
import org.beigesoft.mdlp.AIdLnNm;
import org.beigesoft.acc.mdlp.DbCr;
import org.beigesoft.acc.mdlp.TxDst;

/**
 * <p>Model of debtor/creditor, i.e. a customer or a supplier.</p>
 *
 * @author Yury Demidenko
 */
public class Buyer extends AIdLnNm implements IIdLna {

  /**
   * <p>Customer, null for unregistered buyer.</p>
   **/
  private DbCr cust;

  /**
   * <p>Password, null for unregistered buyer.</p>
   **/
  private String pwd;

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
   * <p>Last time login or logged change cart, logout means 0.
   * Buyer is logged then Now - lsTm not exceed 30 minutes.
   * </p>
   **/
  private Long lsTm = 0L;

  /**
   * <p>Not null, false default.
   * If pwd=null and fre=true, then use it for new
   * buyer.</p>
   **/
  private Boolean fre = Boolean.FALSE;

  /**
   * <p>Buyer's last/current session ID.</p>
   **/
  private String buSeId;

  //For S.E. sellers without DebtorCreditor:
  /**
   * <p>TIN.</p>
   **/
  private String tin;

  /**
   * <p>Only for overseas/overstate buyers/vendors.</p>
   **/
  private TxDst txDs;

  //Simple getters and setters:
  /**
   * <p>Getter for cust.</p>
   * @return DbCr
   **/
  public final DbCr getCust() {
    return this.cust;
  }

  /**
   * <p>Setter for cust.</p>
   * @param pCust reference
   **/
  public final void setCust(final DbCr pCust) {
    this.cust = pCust;
  }

  /**
   * <p>Getter for pwd.</p>
   * @return String
   **/
  public final String getPwd() {
    return this.pwd;
  }

  /**
   * <p>Setter for pwd.</p>
   * @param pPwd reference
   **/
  public final void setPwd(final String pPwd) {
    this.pwd = pPwd;
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
   * <p>Getter for lsTm.</p>
   * @return Long
   **/
  public final Long getLsTm() {
    return this.lsTm;
  }

  /**
   * <p>Setter for lsTm.</p>
   * @param pLsTm reference
   **/
  public final void setLsTm(final Long pLsTm) {
    this.lsTm = pLsTm;
  }

  /**
   * <p>Getter for fre.</p>
   * @return Boolean
   **/
  public final Boolean getFre() {
    return this.fre;
  }

  /**
   * <p>Setter for fre.</p>
   * @param pFre reference
   **/
  public final void setFre(final Boolean pFre) {
    this.fre = pFre;
  }

  /**
   * <p>Getter for buSeId.</p>
   * @return String
   **/
  public final String getBuSeId() {
    return this.buSeId;
  }

  /**
   * <p>Setter for buSeId.</p>
   * @param pBuSeId reference
   **/
  public final void setBuSeId(final String pBuSeId) {
    this.buSeId = pBuSeId;
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
}
