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

import java.util.Date;
import java.math.RoundingMode;

import org.beigesoft.doc.model.EPageSize;
import org.beigesoft.doc.model.EPageOrientation;
import org.beigesoft.mdl.EPeriod;
import org.beigesoft.mdlp.AIdLn;
import org.beigesoft.acc.mdl.ECogsMth;

/**
 * <p>Accounting settings.</p>
 *
 * @author Yury Demidenko
 */
public class AcStg extends AIdLn {

  //Organization info:
  /**
   * <p>Organization name.</p>
   **/
  private String org;

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

  //Base settings:
  /**
   * <p>Date current accounting month to restrict documents, entries.</p>
   **/
  private Date mnth;

  /**
   * <p>Cost precision.</p>
   **/
  private Integer csDp = 4;

  /**
   * <p>Price precision.</p>
   **/
  private Integer prDp = 2;

  /**
   * <p>Reports precision.</p>
   **/
  private Integer rpDp = 1;

  /**
   * <p>Quantity precision.</p>
   **/
  private Integer quDp = 2;

  /**
   * <p>Tax precision.</p>
   **/
  private Integer txDp = 3;

  /**
   * <p>Rounding mode.</p>
   **/
  private RoundingMode rndm = RoundingMode.HALF_UP;

  /**
   * <p>COGS method FIFO/LIFO/AVERAGE.</p>
   **/
  private ECogsMth cogs = ECogsMth.FIFO;

  /**
   * <p>Balance store period, not null, EPeriod.DAILY/WEEKLY/MONTHLY.</p>
   **/
  private EPeriod blPr = EPeriod.MONTHLY;

  //Sales taxes settings:
  /**
   * <p>Not Null, if extract sales taxes from sales invoice
   * into Sales Tax Payable account.</p>
   **/
  private Boolean stExs = false;

  /**
   * <p>Not Null, if extract sales taxes from purchase invoice
   * into Sales Tax From Purchase account, this is for methods where payed taxes
   * from purchase should be extracted from inventory e.g. VAT
   * or sales taxes that should be capitalized (USA producing).</p>
   **/
  private Boolean stExp = false;

  /**
   * <p>Rounding mode for sales taxes.</p>
   **/
  private RoundingMode stRm = RoundingMode.HALF_UP;

  /**
   * <p>Grouping method for sales taxes - false item basis, true - invoice.
   * This is about grouping rounding error:
   * round(2.244 + 2.244) != round(2.244) + round(2.244);
   * 4.49 != 4.48
   * </p>
   **/
  private Boolean stIb = Boolean.FALSE;

  /**
   * <p>Use aggregate tax rate or only tax.</p>
   **/
  private Boolean stAg = Boolean.FALSE;

  //Common reports settings:
  /**
   * <p>Not Null, default currency.</p>
   **/
  private Curr curr;

  /**
   * <p>Not Null, if  if uses currency sign in reports (e.g. $),
   * otherwise itsName (e.g. USD).</p>
   **/
  private Boolean curs = false;

  /**
   * <p>Not Null, if print currency on left of amount
   * e.g. "1,356.12$" or "$1,356.12".</p>
   **/
  private Boolean curl = false;

  //PDF reports settings:
  /**
   * <p>TTF file name, DejaVuSerif default.</p>
   **/
  private String ttff = "DejaVuSerif";

  /**
   * <p>TTF bold file name, DejaVuSerif-Bold default.
   * May be empty for hieroglyph's fonts.</p>
   **/
  private String ttfb = "DejaVuSerif-Bold";

  /**
   * <p>Invoice, balance reports page size, A4 default.</p>
   **/
  private EPageSize pgSz = EPageSize.A4;

  /**
   * <p>Invoice, balance reports page orientation, PORTRAIT default.</p>
   **/
  private EPageOrientation pgOr = EPageOrientation.PORTRAIT;

  /**
   * <p>Invoice, balance reports margin Left,
   * Letter - inch, otherwise millimeters, default 30mm.</p>
   **/
  private Double mrLf = 30.0;

  /**
   * <p>Invoice, balance reports Margin Right,
   * Letter - inch, otherwise millimeters, default 15mm.</p>
   **/
  private Double mrRi = 15.0;

  /**
   * <p>Invoice, balance reports Margin Top.</p>
   **/
  private Double mrTo = 20.0;

  /**
   * <p>Invoice, balance reports Margin Bottom,
   * Letter - inch, otherwise millimeters, default 20mm.</p>
   **/
  private Double mrBo = 20.0;

  /**
   * <p>Invoice, balance reports Font size,
   * Letter - inch, otherwise millimeters, default 3.5mm (0.1378INCH).</p>
   **/
  private Double fnSz = 3.5;

  //Simple getters and setters:
  /**
   * <p>Getter for org.</p>
   * @return String
   **/
  public final String getOrg() {
    return this.org;
  }

  /**
   * <p>Setter for org.</p>
   * @param pOrg reference
   **/
  public final void setOrg(final String pOrg) {
    this.org = pOrg;
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

  /**
   * <p>Getter for mnth.</p>
   * @return Date
   **/
  public final Date getMnth() {
    return this.mnth;
  }

  /**
   * <p>Setter for mnth.</p>
   * @param pMnth reference
   **/
  public final void setMnth(final Date pMnth) {
    this.mnth = pMnth;
  }

  /**
   * <p>Getter for csDp.</p>
   * @return Integer
   **/
  public final Integer getCsDp() {
    return this.csDp;
  }

  /**
   * <p>Setter for csDp.</p>
   * @param pCsDp reference
   **/
  public final void setCsDp(final Integer pCsDp) {
    this.csDp = pCsDp;
  }

  /**
   * <p>Getter for prDp.</p>
   * @return Integer
   **/
  public final Integer getPrDp() {
    return this.prDp;
  }

  /**
   * <p>Setter for prDp.</p>
   * @param pPrDp reference
   **/
  public final void setPrDp(final Integer pPrDp) {
    this.prDp = pPrDp;
  }

  /**
   * <p>Getter for rpDp.</p>
   * @return Integer
   **/
  public final Integer getRpDp() {
    return this.rpDp;
  }

  /**
   * <p>Setter for rpDp.</p>
   * @param pRpDp reference
   **/
  public final void setRpDp(final Integer pRpDp) {
    this.rpDp = pRpDp;
  }

  /**
   * <p>Getter for quDp.</p>
   * @return Integer
   **/
  public final Integer getQuDp() {
    return this.quDp;
  }

  /**
   * <p>Setter for quDp.</p>
   * @param pQuDp reference
   **/
  public final void setQuDp(final Integer pQuDp) {
    this.quDp = pQuDp;
  }

  /**
   * <p>Getter for txDp.</p>
   * @return Integer
   **/
  public final Integer getTxDp() {
    return this.txDp;
  }

  /**
   * <p>Setter for txDp.</p>
   * @param pTxDp reference
   **/
  public final void setTxDp(final Integer pTxDp) {
    this.txDp = pTxDp;
  }

  /**
   * <p>Getter for rndm.</p>
   * @return RoundingMode
   **/
  public final RoundingMode getRndm() {
    return this.rndm;
  }

  /**
   * <p>Setter for rndm.</p>
   * @param pRndm reference
   **/
  public final void setRndm(final RoundingMode pRndm) {
    this.rndm = pRndm;
  }

  /**
   * <p>Getter for cogs.</p>
   * @return ECogsMth
   **/
  public final ECogsMth getCogs() {
    return this.cogs;
  }

  /**
   * <p>Setter for cogs.</p>
   * @param pCogs reference
   **/
  public final void setCogs(final ECogsMth pCogs) {
    this.cogs = pCogs;
  }

  /**
   * <p>Getter for blPr.</p>
   * @return EPeriod
   **/
  public final EPeriod getBlPr() {
    return this.blPr;
  }

  /**
   * <p>Setter for blPr.</p>
   * @param pBlPr reference
   **/
  public final void setBlPr(final EPeriod pBlPr) {
    this.blPr = pBlPr;
  }

  /**
   * <p>Getter for stExs.</p>
   * @return Boolean
   **/
  public final Boolean getStExs() {
    return this.stExs;
  }

  /**
   * <p>Setter for stExs.</p>
   * @param pStExs reference
   **/
  public final void setStExs(final Boolean pStExs) {
    this.stExs = pStExs;
  }

  /**
   * <p>Getter for stExp.</p>
   * @return Boolean
   **/
  public final Boolean getStExp() {
    return this.stExp;
  }

  /**
   * <p>Setter for stExp.</p>
   * @param pStExp reference
   **/
  public final void setStExp(final Boolean pStExp) {
    this.stExp = pStExp;
  }

  /**
   * <p>Getter for stRm.</p>
   * @return RoundingMode
   **/
  public final RoundingMode getStRm() {
    return this.stRm;
  }

  /**
   * <p>Setter for stRm.</p>
   * @param pStRm reference
   **/
  public final void setStRm(final RoundingMode pStRm) {
    this.stRm = pStRm;
  }

  /**
   * <p>Getter for stIb.</p>
   * @return Boolean
   **/
  public final Boolean getStIb() {
    return this.stIb;
  }

  /**
   * <p>Setter for stIb.</p>
   * @param pStIb reference
   **/
  public final void setStIb(final Boolean pStIb) {
    this.stIb = pStIb;
  }

  /**
   * <p>Getter for stAg.</p>
   * @return Boolean
   **/
  public final Boolean getStAg() {
    return this.stAg;
  }

  /**
   * <p>Setter for stAg.</p>
   * @param pStAg reference
   **/
  public final void setStAg(final Boolean pStAg) {
    this.stAg = pStAg;
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

  /**
   * <p>Getter for ttff.</p>
   * @return String
   **/
  public final String getTtff() {
    return this.ttff;
  }

  /**
   * <p>Setter for ttff.</p>
   * @param pTtff reference
   **/
  public final void setTtff(final String pTtff) {
    this.ttff = pTtff;
  }

  /**
   * <p>Getter for ttfb.</p>
   * @return String
   **/
  public final String getTtfb() {
    return this.ttfb;
  }

  /**
   * <p>Setter for ttfb.</p>
   * @param pTtfb reference
   **/
  public final void setTtfb(final String pTtfb) {
    this.ttfb = pTtfb;
  }

  /**
   * <p>Getter for pgSz.</p>
   * @return EPageSize
   **/
  public final EPageSize getPgSz() {
    return this.pgSz;
  }

  /**
   * <p>Setter for pgSz.</p>
   * @param pPgSz reference
   **/
  public final void setPgSz(final EPageSize pPgSz) {
    this.pgSz = pPgSz;
  }

  /**
   * <p>Getter for pgOr.</p>
   * @return EPageOrientation
   **/
  public final EPageOrientation getPgOr() {
    return this.pgOr;
  }

  /**
   * <p>Setter for pgOr.</p>
   * @param pPgOr reference
   **/
  public final void setPgOr(final EPageOrientation pPgOr) {
    this.pgOr = pPgOr;
  }

  /**
   * <p>Getter for mrLf.</p>
   * @return Double
   **/
  public final Double getMrLf() {
    return this.mrLf;
  }

  /**
   * <p>Setter for mrLf.</p>
   * @param pMrLf reference
   **/
  public final void setMrLf(final Double pMrLf) {
    this.mrLf = pMrLf;
  }

  /**
   * <p>Getter for mrRi.</p>
   * @return Double
   **/
  public final Double getMrRi() {
    return this.mrRi;
  }

  /**
   * <p>Setter for mrRi.</p>
   * @param pMrRi reference
   **/
  public final void setMrRi(final Double pMrRi) {
    this.mrRi = pMrRi;
  }

  /**
   * <p>Getter for mrTo.</p>
   * @return Double
   **/
  public final Double getMrTo() {
    return this.mrTo;
  }

  /**
   * <p>Setter for mrTo.</p>
   * @param pMrTo reference
   **/
  public final void setMrTo(final Double pMrTo) {
    this.mrTo = pMrTo;
  }

  /**
   * <p>Getter for mrBo.</p>
   * @return Double
   **/
  public final Double getMrBo() {
    return this.mrBo;
  }

  /**
   * <p>Setter for mrBo.</p>
   * @param pMrBo reference
   **/
  public final void setMrBo(final Double pMrBo) {
    this.mrBo = pMrBo;
  }

  /**
   * <p>Getter for fnSz.</p>
   * @return Double
   **/
  public final Double getFnSz() {
    return this.fnSz;
  }

  /**
   * <p>Setter for fnSz.</p>
   * @param pFnSz reference
   **/
  public final void setFnSz(final Double pFnSz) {
    this.fnSz = pFnSz;
  }
}
