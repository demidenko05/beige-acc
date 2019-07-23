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
import java.math.BigDecimal;

import org.beigesoft.mdl.IOwned;
import org.beigesoft.mdlp.AOrId;
import org.beigesoft.acc.mdl.EDocTy;
import org.beigesoft.acc.mdl.EBnEnrSt;
import org.beigesoft.acc.mdl.EBnEnrRsTy;
import org.beigesoft.acc.mdl.EBnEnrRsAc;
import org.beigesoft.acc.mdlb.IDocb;

/**
 * <p>Model of move item line.</p>
 *
 * @author Yury Demidenko
 */
public class BnStLn extends AOrId implements IOwned<BnkStm, Long>, IDocb {

  /**
   * <p>Owner.</p>
   **/
  private BnkStm ownr;

  /**
   * <p>Date.</p>
   **/
  private Date dat;

  /**
   * <p>Amount.</p>
   **/
  private BigDecimal amnt = BigDecimal.ZERO;

  /**
   * <p>Description status.</p>
   **/
  private String dsSt;

  /**
   * <p>Status ACCEPTED default or from CSV according settings,
   * read only field.</p>
   **/
  private EBnEnrSt stas = EBnEnrSt.ACCEPTED;

  /**
   * <p>if action was made, read only field,
   * CREATE or MATCH.</p>
   **/
  private EBnEnrRsAc rsAc;

  /**
   * <p>if action was made, read only field,
   * e.g. "was made PaymentTo#12665".</p>
   **/
  private String rsDs;

  /**
   * <p>if made, read only field.</p>
   **/
  private Long rsRcId;

  /**
   * <p>if made, read only field.</p>
   **/
  private EBnEnrRsTy rsRcTy;

  /**
   * <p>Constant of code type 2008.</p>
   * @return 2008
   **/
  @Override
  public final Integer cnsTy() {
    return 2008;
  }

  /**
   * <p>Getter of EDocTy.</p>
   * @return EDocTy
   **/
  @Override
  public final EDocTy getDocTy() {
    return EDocTy.ACC;
  }

  /**
   * <p>Getter for ownr.</p>
   * @return BnkStm
   **/
  @Override
  public final BnkStm getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final BnkStm pOwnr) {
    this.ownr = pOwnr;
  }

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
   * <p>Getter for amnt.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getAmnt() {
    return this.amnt;
  }

  /**
   * <p>Setter for amnt.</p>
   * @param pAmnt reference
   **/
  public final void setAmnt(final BigDecimal pAmnt) {
    this.amnt = pAmnt;
  }

  /**
   * <p>Getter for dsSt.</p>
   * @return String
   **/
  public final String getDsSt() {
    return this.dsSt;
  }

  /**
   * <p>Setter for dsSt.</p>
   * @param pDsSt reference
   **/
  public final void setDsSt(final String pDsSt) {
    this.dsSt = pDsSt;
  }

  /**
   * <p>Getter for stas.</p>
   * @return EBnEnrSt
   **/
  public final EBnEnrSt getStas() {
    return this.stas;
  }

  /**
   * <p>Setter for stas.</p>
   * @param pStas reference
   **/
  public final void setStas(final EBnEnrSt pStas) {
    this.stas = pStas;
  }

  /**
   * <p>Getter for rsAc.</p>
   * @return EBnEnrRsAc
   **/
  public final EBnEnrRsAc getRsAc() {
    return this.rsAc;
  }

  /**
   * <p>Setter for rsAc.</p>
   * @param pRsAc reference
   **/
  public final void setRsAc(final EBnEnrRsAc pRsAc) {
    this.rsAc = pRsAc;
  }

  /**
   * <p>Getter for rsDs.</p>
   * @return String
   **/
  public final String getRsDs() {
    return this.rsDs;
  }

  /**
   * <p>Setter for rsDs.</p>
   * @param pRsDs reference
   **/
  public final void setRsDs(final String pRsDs) {
    this.rsDs = pRsDs;
  }

  /**
   * <p>Getter for rsRcId.</p>
   * @return Long
   **/
  public final Long getRsRcId() {
    return this.rsRcId;
  }

  /**
   * <p>Setter for rsRcId.</p>
   * @param pRsRcId reference
   **/
  public final void setRsRcId(final Long pRsRcId) {
    this.rsRcId = pRsRcId;
  }

  /**
   * <p>Getter for rsRcTy.</p>
   * @return EBnEnrRsTy
   **/
  public final EBnEnrRsTy getRsRcTy() {
    return this.rsRcTy;
  }

  /**
   * <p>Setter for rsRcTy.</p>
   * @param pRsRcTy reference
   **/
  public final void setRsRcTy(final EBnEnrRsTy pRsRcTy) {
    this.rsRcTy = pRsRcTy;
  }
}
