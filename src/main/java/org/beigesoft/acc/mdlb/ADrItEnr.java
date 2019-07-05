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

package org.beigesoft.acc.mdlb;

import java.math.BigDecimal;

import org.beigesoft.mdlp.AOrId;
import org.beigesoft.acc.mdlp.Uom;
import org.beigesoft.acc.mdlp.Itm;

/**
 * <p>Base model of item withdrawal entry. It draws item for using, selling,
 * manufacturing, reporting loss/broke/stole, etc.</p>
 *
 * @author Yury Demidenko
 */
public abstract class ADrItEnr extends AOrId {

  /**
   * <p>Reversed/reversing ID.</p>
   **/
  private Long rvId;

  /**
   * <p>Integer, Not Null Source Type e.g. 2000 - PrInGdLn.
   * This is constant [document/line].cnsTy().</p>
   **/
  private Integer srTy;

  /**
   * <p>Source document/line ID, Not Null.</p>
   **/
  private Long srId;

  /**
   * <p>Integer, if exist, Source Owner Type e.g. 4 - PruInv.
   * This is constant [document/line].cnsTy().</p>
   **/
  private Integer sowTy;

  /**
   * <p>Source owner ID, if exist.</p>
   **/
  private Long sowId;

  /**
   * <p>Integer, Not Null Drawing Type e.g. 2001 - SlInGdLn.
   * This is constant [document/line].cnsTy().</p>
   **/
  private Integer drTy;

  /**
   * <p>Drawing document/line ID, Not Null.</p>
   **/
  private Long drId;

  /**
   * <p>Integer, if exist, Drawing Owner Type e.g. 5 - SalInv.
   * This is constant [document/line].cnsTy().</p>
   **/
  private Integer dowTy;

  /**
   * <p>Drawing owner ID, if exist.</p>
   **/
  private Long dowId;

  /**
   * <p>Item to draw.</p>
   **/
  private Itm itm;

  /**
   * <p>Unit of measure.</p>
   **/
  private Uom uom;

  /**
   * <p>Quantity.</p>
   **/
  private BigDecimal quan = BigDecimal.ZERO;

  /**
   * <p>Total.</p>
   **/
  private BigDecimal tot = BigDecimal.ZERO;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

  //Simple getters and setters:
  /**
   * <p>Getter for rvId.</p>
   * @return Long
   **/
  public final Long getRvId() {
    return this.rvId;
  }

  /**
   * <p>Setter for rvId.</p>
   * @param pRvId reference
   **/
  public final void setRvId(final Long pRvId) {
    this.rvId = pRvId;
  }

  /**
   * <p>Getter for srTy.</p>
   * @return Integer
   **/
  public final Integer getSrTy() {
    return this.srTy;
  }

  /**
   * <p>Setter for srTy.</p>
   * @param pSrTy reference
   **/
  public final void setSrTy(final Integer pSrTy) {
    this.srTy = pSrTy;
  }

  /**
   * <p>Getter for srId.</p>
   * @return Long
   **/
  public final Long getSrId() {
    return this.srId;
  }

  /**
   * <p>Setter for srId.</p>
   * @param pSrId reference
   **/
  public final void setSrId(final Long pSrId) {
    this.srId = pSrId;
  }

  /**
   * <p>Getter for sowTy.</p>
   * @return Integer
   **/
  public final Integer getSowTy() {
    return this.sowTy;
  }

  /**
   * <p>Setter for sowTy.</p>
   * @param pSowTy reference
   **/
  public final void setSowTy(final Integer pSowTy) {
    this.sowTy = pSowTy;
  }

  /**
   * <p>Getter for sowId.</p>
   * @return Long
   **/
  public final Long getSowId() {
    return this.sowId;
  }

  /**
   * <p>Setter for sowId.</p>
   * @param pSowId reference
   **/
  public final void setSowId(final Long pSowId) {
    this.sowId = pSowId;
  }

  /**
   * <p>Getter for drTy.</p>
   * @return Integer
   **/
  public final Integer getDrTy() {
    return this.drTy;
  }

  /**
   * <p>Setter for drTy.</p>
   * @param pDrTy reference
   **/
  public final void setDrTy(final Integer pDrTy) {
    this.drTy = pDrTy;
  }

  /**
   * <p>Getter for drId.</p>
   * @return Long
   **/
  public final Long getDrId() {
    return this.drId;
  }

  /**
   * <p>Setter for drId.</p>
   * @param pDrId reference
   **/
  public final void setDrId(final Long pDrId) {
    this.drId = pDrId;
  }

  /**
   * <p>Getter for dowTy.</p>
   * @return Integer
   **/
  public final Integer getDowTy() {
    return this.dowTy;
  }

  /**
   * <p>Setter for dowTy.</p>
   * @param pDowTy reference
   **/
  public final void setDowTy(final Integer pDowTy) {
    this.dowTy = pDowTy;
  }

  /**
   * <p>Getter for dowId.</p>
   * @return Long
   **/
  public final Long getDowId() {
    return this.dowId;
  }

  /**
   * <p>Setter for dowId.</p>
   * @param pDowId reference
   **/
  public final void setDowId(final Long pDowId) {
    this.dowId = pDowId;
  }

  /**
   * <p>Getter for itm.</p>
   * @return Itm
   **/
  public final Itm getItm() {
    return this.itm;
  }

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  public final void setItm(final Itm pItm) {
    this.itm = pItm;
  }

  /**
   * <p>Getter for uom.</p>
   * @return Uom
   **/
  public final Uom getUom() {
    return this.uom;
  }

  /**
   * <p>Setter for uom.</p>
   * @param pUom reference
   **/
  public final void setUom(final Uom pUom) {
    this.uom = pUom;
  }

  /**
   * <p>Getter for quan.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getQuan() {
    return this.quan;
  }

  /**
   * <p>Setter for quan.</p>
   * @param pQuan reference
   **/
  public final void setQuan(final BigDecimal pQuan) {
    this.quan = pQuan;
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
}
