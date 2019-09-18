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

package org.beigesoft.acc.mdl;

import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.acc.mdlb.IMkWsEnr;
import org.beigesoft.acc.mdlp.Mnfct;
import org.beigesoft.acc.mdlp.Itm;
import org.beigesoft.acc.mdlp.Uom;

/**
 * <p>Wrapper of manufacturing for drawing item in progress in warehouse.</p>
 *
 * @author Yury Demidenko
 */
public class MnfFdWe implements IMkWsEnr {

  /**
   * <p>Manufacture.</p>
   **/
  private final Mnfct mnfct;

  /**
   * <p>Only constructor.</p>
   * @param pMnfct reference
   **/
  public MnfFdWe(final Mnfct pMnfct) {
    this.mnfct = pMnfct;
  }

  /**
   * <p>Geter for id.</p>
   * @return Long
   **/
  @Override
  public final Long getIid() {
    return this.mnfct.getIid();
  }

  /**
   * <p>Setter for id.</p>
   * @param pId reference/value
   **/
  @Override
  public final void setIid(final Long pId) {
    throw new RuntimeException("NA");
  }

  /**
   * <p>Geter for dbOr.</p>
   * @return Integer
   **/
  @Override
  public final Integer getDbOr() {
    return this.mnfct.getDbOr();
  }

  /**
   * <p>Setter for dbOr.</p>
   * @param pDbOr reference
   **/
  @Override
  public final void setDbOr(final Integer pDbOr) {
    throw new RuntimeException("NA");
  }

  /**
   * <p>Geter for idOr.</p>
   * @return Long
   **/
  @Override
  public final Long getIdOr() {
    return this.mnfct.getIdOr();
  }

  /**
   * <p>Setter for idOr.</p>
   * @param pIdOr reference
   **/
  @Override
  public final void setIdOr(final Long pIdOr) {
    throw new RuntimeException("NA");
  }

  /**
   * <p>Geter for ver.</p>
   * @return Long
   **/
  @Override
  public final Long getVer() {
    return this.mnfct.getVer();
  }

  /**
   * <p>Setter for ver.</p>
   * @param pVer reference
   **/
  @Override
  public final void setVer(final Long pVer) {
    throw new RuntimeException("NA");
  }

  /**
   * <p>Geter for isNew.</p>
   * @return boolean
   **/
  @Override
  public final Boolean getIsNew() {
    return this.mnfct.getIsNew();
  }

  /**
   * <p>Setter for isNew.</p>
   * @param pIsNew value
   **/
  @Override
  public final void setIsNew(final Boolean pIsNew) {
    throw new RuntimeException("NA");
  }

  /**
   * <p>Getter for rvId.</p>
   * @return Long
   **/
  @Override
  public final Long getRvId() {
    return this.mnfct.getRvId();
  }

  /**
   * <p>Setter for rvId.</p>
   * @param pRvId reference
   **/
  @Override
  public final void setRvId(final Long pRvId) {
    this.mnfct.setRvId(pRvId);
  }

  /**
   * <p>Constant of code type 14.</p>
   * @return 14
   **/
  @Override
  public final Integer cnsTy() {
    return this.mnfct.cnsTy();
  }

  /**
   * <p>Setter for description.</p>
   * @return description
   **/
  @Override
  public final String getDscr() {
    return this.mnfct.getDscr();
  }

  /**
   * <p>Setter for description.</p>
   * @param pDscr reference
   **/
  @Override
  public final void setDscr(final String pDscr) {
    this.mnfct.setDscr(pDscr);
  }

  /**
   * <p>Getter for itm.</p>
   * @return Itm
   **/
  @Override
  public final Itm getItm() {
    return this.mnfct.getMnp().getItm();
  }

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  @Override
  public final void setItm(final Itm pItm) {
    throw new RuntimeException("NA");
  }

  /**
   * <p>Getter for uom.</p>
   * @return Uom
   **/
  @Override
  public final Uom getUom() {
    return this.mnfct.getUom();
  }

  /**
   * <p>Setter for uom.</p>
   * @param pUom reference
   **/
  @Override
  public final void setUom(final Uom pUom) {
    throw new RuntimeException("NA");
  }

  /**
   * <p>Getter for quan.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getQuan() {
    return this.mnfct.getQuan();
  }

  /**
   * <p>Setter for quan.</p>
   * @param pQuan reference
   **/
  @Override
  public final void setQuan(final BigDecimal pQuan) {
    throw new RuntimeException("NA");
  }

  /**
   * <p>Getter for document date (own or owner's).</p>
   * @return Date
   **/
  @Override
  public final Date getDocDt() {
    return this.mnfct.getDat();
  }

  /**
   * <p>Getter for owner ID if exist.</p>
   * @return ID
   **/
  @Override
  public final Long getOwnrId() {
    return null;
  }

  /**
   * <p>Getter for owner type.</p>
   * @return null
   **/
  @Override
  public final Integer getOwnrTy() {
    return null;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for mnfct.</p>
   * @return Mnfct
   **/
  public final Mnfct getMnfct() {
    return this.mnfct;
  }
}
