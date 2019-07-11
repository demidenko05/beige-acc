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

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.mdlp.AOrId;
import org.beigesoft.acc.mdlb.IRetLn;
import org.beigesoft.acc.mdlb.IMkDriEnr;

/**
 * <p>Model of purchase return line.</p>
 *
 * @author Yury Demidenko
 */
public class PuRtLn extends AOrId
  implements IRetLn<PurRet, PurInv, PuInGdLn>, IMkDriEnr<DrItEnr> {

  /**
   * <p>Return.</p>
   **/
  private PurRet ownr;

  /**
   * <p>Invoice line.</p>
   **/
  private PuInGdLn invl;

  /**
   * <p>Warehouse place optional.</p>
   **/
  private WrhPl whpo;

  /**
   * <p>Reversed ID.</p>
   **/
  private Long rvId;

  /**
   * <p>Quantity.</p>
   **/
  private BigDecimal quan = BigDecimal.ZERO;

  /**
   * <p>Subtotal.</p>
   **/
  private BigDecimal subt = BigDecimal.ZERO;

  /**
   * <p>Subtotal in foreign currency.</p>
   **/
  private BigDecimal suFc = BigDecimal.ZERO;

  /**
   * <p>Total taxes.</p>
   **/
  private BigDecimal toTx = BigDecimal.ZERO;

  /**
   * <p>Total taxes in foreign currency.</p>
   **/
  private BigDecimal txFc = BigDecimal.ZERO;

  /**
   * <p>Total.</p>
   **/
  private BigDecimal tot = BigDecimal.ZERO;

  /**
   * <p>Total in foreign currency.</p>
   **/
  private BigDecimal toFc = BigDecimal.ZERO;

  /**
   * <p>Tax description.</p>
   **/
  private String tdsc;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

  /**
   * <p>Item basis tax lines.</p>
   **/
  private List<PuRtLtl> txLns;

  /**
   * <p>Constant of code type.</p>
   * @return 2002
   **/
  @Override
  public final Integer cnsTy() {
    return 2002;
  }

  /**
   * <p>Getter for ownr.</p>
   * @return PurRet
   **/
  @Override
  public final PurRet getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final PurRet pOwnr) {
    this.ownr = pOwnr;
  }

  /**
   * <p>Getter for document date (own or owner's).</p>
   * @return Date
   **/
  @Override
  public final Date getDocDt() {
    return this.ownr.getDat();
  }

  /**
   * <p>Getter for owner ID if exist.</p>
   * @return ID
   **/
  @Override
  public final Long getOwnrId() {
    return this.ownr.getIid();
  }

  /**
   * <p>Getter for owner type.</p>
   * @return type code 8
   **/
  @Override
  public final Integer getOwnrTy() {
    return 8;
  }

  /**
   * <p>Getter for draw item entry class.</p>
   * @return draw item entry class
   **/
  @Override
  public final Class<DrItEnr> getEnrCls() {
    return DrItEnr.class;
  }

  /**
   * <p>Getter for itm.</p>
   * @return Itm
   **/
  @Override
  public final Itm getItm() {
    if (this.invl == null) {
      return null;
    }
    return this.invl.getItm();
  }

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  @Override
  public final void setItm(final Itm pItm) {
    throw new RuntimeException("Not allowed!");
  }

  /**
   * <p>Getter for rvId.</p>
   * @return Long
   **/
  @Override
  public final Long getRvId() {
    return this.rvId;
  }

  /**
   * <p>Setter for rvId.</p>
   * @param pRvId reference
   **/
  @Override
  public final void setRvId(final Long pRvId) {
    this.rvId = pRvId;
  }

  /**
   * <p>Getter for uom.</p>
   * @return Uom
   **/
  @Override
  public final Uom getUom() {
    if (this.invl == null) {
      return null;
    }
    return this.invl.getUom();
  }

  /**
   * <p>Setter for uom.</p>
   * @param pUom reference
   **/
  @Override
  public final void setUom(final Uom pUom) {
    throw new RuntimeException("Not allowed!");
  }

  /**
   * <p>Getter for pri.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getPri() {
    if (this.invl == null) {
      return null;
    }
    return this.invl.getPri();
  }

  /**
   * <p>Setter for pri.</p>
   * @param pPri reference
   **/
  @Override
  public final void setPri(final BigDecimal pPri) {
    throw new RuntimeException("Not allowed!");
  }

  /**
   * <p>Getter for prFc.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getPrFc() {
    if (this.invl == null) {
      return null;
    }
    return this.invl.getPrFc();
  }

  /**
   * <p>Setter for prFc.</p>
   * @param pPrFc reference
   **/
  @Override
  public final void setPrFc(final BigDecimal pPrFc) {
    throw new RuntimeException("Not allowed!");
  }

  /**
   * <p>Getter for txCt.</p>
   * @return TxCt
   **/
  public final TxCt getTxCt() {
    if (this.invl == null) {
      return null;
    }
    return this.invl.getTxCt();
  }

  /**
   * <p>Setter for txCt.</p>
   * @param pTxCt reference
   **/
  public final void setTxCt(final TxCt pTxCt) {
    throw new RuntimeException("Not allowed!");
  }

  /**
   * <p>Getter for quan.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getQuan() {
    return this.quan;
  }

  /**
   * <p>Setter for quan.</p>
   * @param pQuan reference
   **/
  @Override
  public final void setQuan(final BigDecimal pQuan) {
    this.quan = pQuan;
  }

  /**
   * <p>Getter for tdsc.</p>
   * @return String
   **/
  @Override
  public final String getTdsc() {
    return this.tdsc;
  }

  /**
   * <p>Setter for tdsc.</p>
   * @param pTdsc reference
   **/
  @Override
  public final void setTdsc(final String pTdsc) {
    this.tdsc = pTdsc;
  }

  /**
   * <p>Getter for dscr.</p>
   * @return String
   **/
  @Override
  public final String getDscr() {
    return this.dscr;
  }

  /**
   * <p>Setter for dscr.</p>
   * @param pDscr reference
   **/
  @Override
  public final void setDscr(final String pDscr) {
    this.dscr = pDscr;
  }

  /**
   * <p>Getter for subt.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getSubt() {
    return this.subt;
  }

  /**
   * <p>Setter for subt.</p>
   * @param pSubt reference
   **/
  @Override
  public final void setSubt(final BigDecimal pSubt) {
    this.subt = pSubt;
  }

  /**
   * <p>Getter for suFc.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getSuFc() {
    return this.suFc;
  }

  /**
   * <p>Setter for suFc.</p>
   * @param pSuFc reference
   **/
  @Override
  public final void setSuFc(final BigDecimal pSuFc) {
    this.suFc = pSuFc;
  }

  /**
   * <p>Getter for toTx.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getToTx() {
    return this.toTx;
  }

  /**
   * <p>Setter for toTx.</p>
   * @param pToTx reference
   **/
  @Override
  public final void setToTx(final BigDecimal pToTx) {
    this.toTx = pToTx;
  }

  /**
   * <p>Getter for txFc.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getTxFc() {
    return this.txFc;
  }

  /**
   * <p>Setter for txFc.</p>
   * @param pTxFc reference
   **/
  @Override
  public final void setTxFc(final BigDecimal pTxFc) {
    this.txFc = pTxFc;
  }

  /**
   * <p>Getter for tot.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getTot() {
    return this.tot;
  }

  /**
   * <p>Setter for tot.</p>
   * @param pTot reference
   **/
  @Override
  public final void setTot(final BigDecimal pTot) {
    this.tot = pTot;
  }

  /**
   * <p>Getter for toFc.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getToFc() {
    return this.toFc;
  }

  /**
   * <p>Setter for toFc.</p>
   * @param pToFc reference
   **/
  @Override
  public final void setToFc(final BigDecimal pToFc) {
    this.toFc = pToFc;
  }

  /**
   * <p>Getter for invl.</p>
   * @return PuInGdLn
   **/
  @Override
  public final PuInGdLn getInvl() {
    return this.invl;
  }

  /**
   * <p>Setter for invl.</p>
   * @param pInvl reference
   **/
  @Override
  public final void setInvl(final PuInGdLn pInvl) {
    this.invl = pInvl;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for txLns.</p>
   * @return List<PuRtLtl>
   **/
  public final List<PuRtLtl> getTxLns() {
    return this.txLns;
  }

  /**
   * <p>Setter for txLns.</p>
   * @param pTxLns reference
   **/
  public final void setTxLns(final List<PuRtLtl> pTxLns) {
    this.txLns = pTxLns;
  }

  /**
   * <p>Getter for whpo.</p>
   * @return WrhPl
   **/
  public final WrhPl getWhpo() {
    return this.whpo;
  }

  /**
   * <p>Setter for whpo.</p>
   * @param pWhpo reference
   **/
  public final void setWhpo(final WrhPl pWhpo) {
    this.whpo = pWhpo;
  }
}
