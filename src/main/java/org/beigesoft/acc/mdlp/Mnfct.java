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

import org.beigesoft.acc.mdl.EItSrTy;
import org.beigesoft.acc.mdl.EDocTy;
import org.beigesoft.acc.mdl.EDocDriTy;
import org.beigesoft.acc.mdlb.ADoci;
import org.beigesoft.acc.mdlb.IDcDri;
import org.beigesoft.acc.mdlb.IItmSrc;
import org.beigesoft.acc.mdlb.IMkDriEnr;

/**
 * <p>Model of manufacturing that makes product from product in progress.</p>
 *
 * @author Yury Demidenko
 */
public class Mnfct extends ADoci implements IDcDri, IItmSrc,
  IMkDriEnr<DrItEnr> {

  /**
   * <p>Item.</p>
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
   * <p>Items left (the rest) to draw, loads by the quantity,
   * draws by sales, losses etc.</p>
   **/
  private BigDecimal itLf = BigDecimal.ZERO;

  /**
   * <p>Total left (the rest) to draw, loads by the total,
   * draws by sales, losses etc.
   * !For current implementation it is zero after loading (only itLf=quan),
   * so for the first withdrawal drawer should set it as total-withdrawal!</p>
   **/
  private BigDecimal toLf = BigDecimal.ZERO;

  /**
   * <p>Item source, not null.</p>
   **/
  private MnfPrc mnp;

  /**
   * <p>Price.</p>
   **/
  private BigDecimal pri = BigDecimal.ZERO;

  /**
   * <p>Warehouse place.</p>
   **/
  private WrhPl wrhp;

  /**
   * <p>Warehouse place from (optional).</p>
   **/
  private WrhPl whpo;

  /**
   * <p>Constant of code type 14.</p>
   * @return 14
   **/
  @Override
  public final Integer cnsTy() {
    return 14;
  }

  /**
   * <p>Getter of EDocTy.</p>
   * @return EDocTy
   **/
  @Override
  public final EDocTy getDocTy() {
    return EDocTy.ITSRDRAW;
  }

  /**
   * <p>Getter for has draw items document type.</p>
   * @return has draw items document type
   **/
  @Override
  public final EDocDriTy getDocDriTy() {
    return EDocDriTy.DRIT;
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
   * <p>Getter for itLf.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getItLf() {
    return this.itLf;
  }

  /**
   * <p>Setter for itLf.</p>
   * @param pItLf reference
   **/
  @Override
  public final void setItLf(final BigDecimal pItLf) {
    this.itLf = pItLf;
  }

  /**
   * <p>Getter for toLf.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getToLf() {
    return this.toLf;
  }

  /**
   * <p>Setter for toLf.</p>
   * @param pToLf reference
   **/
  @Override
  public final void setToLf(final BigDecimal pToLf) {
    this.toLf = pToLf;
  }

  /**
   * <p>Getter for initial total to withdraw.
   * This is because of complex tax calculation. For invoice basis subtotal
   * of a line maybe changed (adjusted) after inserting new one
   * in case of price inclusive of tax.</p>
   * @return BigDecimal
   **/
  @Override
  public final BigDecimal getIniTo() {
    return getTot();
  }

  /**
   * <p>Getter for document date (own or owner's).</p>
   * @return Date
   **/
  @Override
  public final Date getDocDt() {
    return getDat();
  }

  /**
   * <p>Setter for owner date if exist.
   * Quick and cheap solution for draw item service.</p>
   * @param pDocDt owner date from SQL query
   **/
  @Override
  public final void setDocDt(final Date pDocDt) {
    setDat(pDocDt);
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
   * <p>Setter for owner ID if exist.
   * Quick and cheap solution for draw item service.</p>
   * @param pOwnrId owner ID from SQL query
   **/
  @Override
  public final void setOwnrId(final Long pOwnrId) {
    //stub
  }

  /**
   * <p>Getter for owner type.</p>
   * @return null
   **/
  @Override
  public final Integer getOwnrTy() {
    return null;
  }

  /**
   * <p>Getter for srsTy.</p>
   * @return EItSrTy
   **/
  @Override
  public final EItSrTy getSrsTy() {
    return EItSrTy.INNER;
  }

  /**
   * <p>Getter for draw item entry class.</p>
   * @return draw item entry class
   **/
  @Override
  public final Class<DrItEnr> getEnrCls() {
    return DrItEnr.class;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for mnp.</p>
   * @return MnfPrc
   **/
  public final MnfPrc getMnp() {
    return this.mnp;
  }

  /**
   * <p>Setter for mnp.</p>
   * @param pMnp reference
   **/
  public final void setMnp(final MnfPrc pMnp) {
    this.mnp = pMnp;
  }

  /**
   * <p>Getter for pri.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getPri() {
    return this.pri;
  }

  /**
   * <p>Setter for pri.</p>
   * @param pPri reference
   **/
  public final void setPri(final BigDecimal pPri) {
    this.pri = pPri;
  }

  /**
   * <p>Getter for wrhp.</p>
   * @return WrhPl
   **/
  public final WrhPl getWrhp() {
    return this.wrhp;
  }

  /**
   * <p>Setter for wrhp.</p>
   * @param pWrhp reference
   **/
  public final void setWrhp(final WrhPl pWrhp) {
    this.wrhp = pWrhp;
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
