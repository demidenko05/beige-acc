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
import org.beigesoft.acc.mdlb.IMkWsEnr;

/**
 * <p>Model of move item line.</p>
 *
 * @author Yury Demidenko
 */
public class MoItLn extends AOrId implements IOwned<MovItm, Long>, IMkWsEnr {

  /**
   * <p>Owner.</p>
   **/
  private MovItm ownr;

  /**
   * <p>Reversed ID.</p>
   **/
  private Long rvId;

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

  /**
   * <p>Constant of code type.</p>
   * @return 2004
   **/
  @Override
  public final Integer cnsTy() {
    return 2004;
  }

  /**
   * <p>Getter for ownr.</p>
   * @return MovItm
   **/
  @Override
  public final MovItm getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final MovItm pOwnr) {
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
   * @return type code 10
   **/
  @Override
  public final Integer getOwnrTy() {
    return 10;
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
   * <p>Getter for itm.</p>
   * @return Itm
   **/
  @Override
  public final Itm getItm() {
    return this.itm;
  }

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  @Override
  public final void setItm(final Itm pItm) {
    this.itm = pItm;
  }

  /**
   * <p>Getter for uom.</p>
   * @return Uom
   **/
  @Override
  public final Uom getUom() {
    return this.uom;
  }

  /**
   * <p>Setter for uom.</p>
   * @param pUom reference
   **/
  @Override
  public final void setUom(final Uom pUom) {
    this.uom = pUom;
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

  //SGS:
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
}
