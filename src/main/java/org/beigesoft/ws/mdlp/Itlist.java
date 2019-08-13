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

import java.math.BigDecimal;

import org.beigesoft.mdlp.AIdLnNm;
import org.beigesoft.ws.mdl.EItmTy;
import org.beigesoft.acc.mdlp.Uom;

/**
 * <p>
 * Model of goods or service in list for improving performance.
 * This is actually cache of data gathered from several tables.
 * </p>
 *
 * @author Yury Demidenko
 */
public class Itlist extends AIdLnNm {

  /**
   * <p>Goods/Service/SEGoods/SEService ID, not null.</p>
   **/
  private Long itId;

  /**
   * <p>Goods/Service/SEGoods/SEService, not null.</p>
   **/
  private EItmTy typ;

  /**
   * <p>If exist addition to item name, max length = 500, this is actually
   * HTML string that briefly describes item.</p>
   **/
  private String specs;

  /**
   * <p>image URL if present, it usually presents in specs.</p>
   **/
  private String img;

  /**
   * <p>Null if auctioned, if TrdStg.priCus=false
   * then it should be updated with changing ItmPri,
   * otherwise it should be retrieved by additional
   * SQL query according BurPric when customer is requesting.</p>
   **/
  private BigDecimal pri;

  /**
   * <p>Null if auctioned, if TrdStg.priCus=false
   * then it should be updated with changing ItmPri,
   * otherwise it should be retrieved by additional
   * SQL query according BurPric when customer is requesting.</p>
   **/
  private BigDecimal priPr;

  /**
   * <p>It's more or equals 0, it's amount of all ItmPlc,
   * so it's updated with changing ItmPlc. If it zero then row does not
   * present in list. If customer uses filter "available since" and/or
   * "pickup place", then it's used inner join - additional SQL query
   * of ItmPlc with place filter.</p>
   **/
  private BigDecimal quan;

  /**
   * <p>Method to render details (page), NULL - no detail page.</p>
   **/
  private Integer detMt;

  /**
   * <p>Unit Of Measure, optional, e.g. per night or per hour.
   * It's used to print non-default UOM in catalog, e.g. 2USD per hour.
   * If item has no unit of measure, then default UOM "each"
   * will be used for other purposes, e.g. in the shopping cart.
   * </p>
   **/
  private Uom uom;

  /**
   * <p>Quantity step, 1 default,
   * e.g. 12USD per 0.5ft, UOM ft, ST=0.5, so
   * buyer can order 0.5/1.0/1.5/2.0/etc. units of item.</p>
   **/
  private BigDecimal unSt = BigDecimal.ONE;

  //Simple getters and setters:
  /**
   * <p>Getter for itId.</p>
   * @return Long
   **/
  public final Long getItId() {
    return this.itId;
  }

  /**
   * <p>Setter for itId.</p>
   * @param pItId reference
   **/
  public final void setItId(final Long pItId) {
    this.itId = pItId;
  }

  /**
   * <p>Getter for typ.</p>
   * @return EItmTy
   **/
  public final EItmTy getTyp() {
    return this.typ;
  }

  /**
   * <p>Setter for typ.</p>
   * @param pTyp reference
   **/
  public final void setTyp(final EItmTy pTyp) {
    this.typ = pTyp;
  }

  /**
   * <p>Getter for specs.</p>
   * @return String
   **/
  public final String getSpecs() {
    return this.specs;
  }

  /**
   * <p>Setter for specs.</p>
   * @param pSpecs reference
   **/
  public final void setSpecs(final String pSpecs) {
    this.specs = pSpecs;
  }

  /**
   * <p>Getter for img.</p>
   * @return String
   **/
  public final String getImg() {
    return this.img;
  }

  /**
   * <p>Setter for img.</p>
   * @param pImg reference
   **/
  public final void setImg(final String pImg) {
    this.img = pImg;
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
   * <p>Getter for priPr.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getPriPr() {
    return this.priPr;
  }

  /**
   * <p>Setter for priPr.</p>
   * @param pPriPr reference
   **/
  public final void setPriPr(final BigDecimal pPriPr) {
    this.priPr = pPriPr;
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
   * <p>Getter for detMt.</p>
   * @return Integer
   **/
  public final Integer getDetMt() {
    return this.detMt;
  }

  /**
   * <p>Setter for detMt.</p>
   * @param pDetMt reference
   **/
  public final void setDetMt(final Integer pDetMt) {
    this.detMt = pDetMt;
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
   * <p>Getter for unSt.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getUnSt() {
    return this.unSt;
  }

  /**
   * <p>Setter for unSt.</p>
   * @param pUnSt reference
   **/
  public final void setUnSt(final BigDecimal pUnSt) {
    this.unSt = pUnSt;
  }
}
