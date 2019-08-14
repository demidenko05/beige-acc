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

import java.util.List;

import org.beigesoft.mdlp.AIdLnNm;

/**
 * <p>Model of catalog of goods/services.</p>
 *
 * @author Yury Demidenko
 */
public class CatGs extends AIdLnNm {

  /**
   * <p>If has subcatalogs, not null, false default.</p>
   **/
  private Boolean hsSub = false;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

  /**
   * <p>Ordering, not null.</p>
   **/
  private Integer idx = 1;

  /**
   * <p>Is it in the menu, default true, to quick switch on/off from menu
   * or for catalog that shows only on start.</p>
   **/
  private Boolean used = true;

  /**
   * <p>Use filter specifics for this catalog/sub-catalogs.</p>
   **/
  private Boolean flSpe = false;

  /**
   * <p>Use filter sub-catalogs for this catalog/sub-catalogs.</p>
   **/
  private Boolean flSub = false;

  /**
   * <p>Use pickup place filter for this catalog/sub-catalogs.</p>
   **/
  private Boolean flPi = false;

  /**
   * <p>Use availability filter for this catalog/sub-catalogs.</p>
   **/
  private Boolean flAvl = false;

  /**
   * <p>List of filterable/orderable specifics that are used for items
   * in that catalog and its sub-catalogs.
   * It's used to make filter/order for item's list.</p>
   **/
  private List<CatSp> usedSpecs;

  /**
   * <p>If used, means ID of customized filter, e.g. "231" means
   * using custom filterPrice231.jsp for used car (set of price ranges)
   * instead of regular(usual/default) filter
   * integer (less, greater, from-to value1/2).</p>
   **/
  private Integer flPrId;

  /**
   * <p>Contains of goods.</p>
   **/
  private Boolean hsGds = true;

  /**
   * <p>Contains of services.</p>
   **/
  private Boolean hsSrv = false;

  /**
   * <p>Contains of S.E. goods.</p>
   **/
  private Boolean hsSgo = false;

  /**
   * <p>Contains of S.E. services.</p>
   **/
  private Boolean hsSse = false;

  //Simple getters and setters:
  /**
   * <p>Getter for hsSub.</p>
   * @return Boolean
   **/
  public final Boolean getHsSub() {
    return this.hsSub;
  }

  /**
   * <p>Setter for hsSub.</p>
   * @param pHsSub reference
   **/
  public final void setHsSub(final Boolean pHsSub) {
    this.hsSub = pHsSub;
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

  /**
   * <p>Getter for idx.</p>
   * @return Integer
   **/
  public final Integer getIdx() {
    return this.idx;
  }

  /**
   * <p>Setter for idx.</p>
   * @param pIdx reference
   **/
  public final void setIdx(final Integer pIdx) {
    this.idx = pIdx;
  }

  /**
   * <p>Getter for used.</p>
   * @return Boolean
   **/
  public final Boolean getUsed() {
    return this.used;
  }

  /**
   * <p>Setter for used.</p>
   * @param pUsed reference
   **/
  public final void setUsed(final Boolean pUsed) {
    this.used = pUsed;
  }

  /**
   * <p>Getter for flSpe.</p>
   * @return Boolean
   **/
  public final Boolean getFlSpe() {
    return this.flSpe;
  }

  /**
   * <p>Setter for flSpe.</p>
   * @param pFlSpe reference
   **/
  public final void setFlSpe(final Boolean pFlSpe) {
    this.flSpe = pFlSpe;
  }

  /**
   * <p>Getter for flSub.</p>
   * @return Boolean
   **/
  public final Boolean getFlSub() {
    return this.flSub;
  }

  /**
   * <p>Setter for flSub.</p>
   * @param pFlSub reference
   **/
  public final void setFlSub(final Boolean pFlSub) {
    this.flSub = pFlSub;
  }

  /**
   * <p>Getter for flPi.</p>
   * @return Boolean
   **/
  public final Boolean getFlPi() {
    return this.flPi;
  }

  /**
   * <p>Setter for flPi.</p>
   * @param pFlPi reference
   **/
  public final void setFlPi(final Boolean pFlPi) {
    this.flPi = pFlPi;
  }

  /**
   * <p>Getter for flAvl.</p>
   * @return Boolean
   **/
  public final Boolean getFlAvl() {
    return this.flAvl;
  }

  /**
   * <p>Setter for flAvl.</p>
   * @param pFlAvl reference
   **/
  public final void setFlAvl(final Boolean pFlAvl) {
    this.flAvl = pFlAvl;
  }

  /**
   * <p>Getter for usedSpecs.</p>
   * @return List<CatSp>
   **/
  public final List<CatSp> getUsedSpecs() {
    return this.usedSpecs;
  }

  /**
   * <p>Setter for usedSpecs.</p>
   * @param pUsedSpecs reference
   **/
  public final void setUsedSpecs(final List<CatSp> pUsedSpecs) {
    this.usedSpecs = pUsedSpecs;
  }

  /**
   * <p>Getter for flPrId.</p>
   * @return Integer
   **/
  public final Integer getFlPrId() {
    return this.flPrId;
  }

  /**
   * <p>Setter for flPrId.</p>
   * @param pFlPrId reference
   **/
  public final void setFlPrId(final Integer pFlPrId) {
    this.flPrId = pFlPrId;
  }

  /**
   * <p>Getter for hsGds.</p>
   * @return Boolean
   **/
  public final Boolean getHsGds() {
    return this.hsGds;
  }

  /**
   * <p>Setter for hsGds.</p>
   * @param pHsGds reference
   **/
  public final void setHsGds(final Boolean pHsGds) {
    this.hsGds = pHsGds;
  }

  /**
   * <p>Getter for hsSrv.</p>
   * @return Boolean
   **/
  public final Boolean getHsSrv() {
    return this.hsSrv;
  }

  /**
   * <p>Setter for hsSrv.</p>
   * @param pHsSrv reference
   **/
  public final void setHsSrv(final Boolean pHsSrv) {
    this.hsSrv = pHsSrv;
  }

  /**
   * <p>Getter for hsSgo.</p>
   * @return Boolean
   **/
  public final Boolean getHsSgo() {
    return this.hsSgo;
  }

  /**
   * <p>Setter for hsSgo.</p>
   * @param pHsSgo reference
   **/
  public final void setHsSgo(final Boolean pHsSgo) {
    this.hsSgo = pHsSgo;
  }

  /**
   * <p>Getter for hsSse.</p>
   * @return Boolean
   **/
  public final Boolean getHsSse() {
    return this.hsSse;
  }

  /**
   * <p>Setter for hsSse.</p>
   * @param pHsSse reference
   **/
  public final void setHsSse(final Boolean pHsSse) {
    this.hsSse = pHsSse;
  }
}
