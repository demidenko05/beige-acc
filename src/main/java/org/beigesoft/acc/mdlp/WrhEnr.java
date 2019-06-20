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

import org.beigesoft.mdlp.AOrId;

/**
 * <p>Model of warehouse entry. It loads/draws/transfer item in warehouse.</p>
 *
 * @author Yury Demidenko
 */
public class WrhEnr extends AOrId {

  /**
   * <p>Date.</p>
   **/
  private Date dat;

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
   * <p>Place from, maybe null if from outside.</p>
   **/
  private WrhPl wpFr;

  /**
   * <p>Place to, maybe null if to outside.</p>
   **/
  private WrhPl wpTo;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

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
   * <p>Getter for wpFr.</p>
   * @return WrhPl
   **/
  public final WrhPl getWpFr() {
    return this.wpFr;
  }

  /**
   * <p>Setter for wpFr.</p>
   * @param pWpFr reference
   **/
  public final void setWpFr(final WrhPl pWpFr) {
    this.wpFr = pWpFr;
  }

  /**
   * <p>Getter for wpTo.</p>
   * @return WrhPl
   **/
  public final WrhPl getWpTo() {
    return this.wpTo;
  }

  /**
   * <p>Setter for wpTo.</p>
   * @param pWpTo reference
   **/
  public final void setWpTo(final WrhPl pWpTo) {
    this.wpTo = pWpTo;
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
