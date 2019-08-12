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

import org.beigesoft.mdlp.AIdLnNm;
import org.beigesoft.ws.mdl.EItmSpTy;

/**
 * <p>Model of item specifics e.g. size, form notebook - web-cam,
 * LED-type.</p>
 *
 * @author Yury Demidenko
 */
public class ItmSp extends AIdLnNm {

  /**
   * <p>Specifics Group, if exist
   * e.g. "Monitor" for its size, web-cam, LED-type.</p>
   **/
  private ItmSpGr grp;

  /**
   * <p>Specifics Type described how to treat (edit/print/filter)
   * specifics, not null, default ESpecificsItemType.STRING.</p>
   **/
  private EItmSpTy typ;

  /**
   * <p>If used in filter, default false.</p>
   **/
  private Boolean inFlt;

  /**
   * <p>If show in list, default false - show only in goods page.</p>
   **/
  private Boolean inLst;

  /**
   * <p>Index, not null, used for ordering when printing.</p>
   **/
  private Integer idx;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

  /**
   * <p>ChooseableSpecificsType if it is chooseable.</p>
   **/
  private ChoSpTy chSpTy;

  /**
   * <p>If used for ordering list, default false.</p>
   **/
  private Boolean foOrd;

  /**
   * <p>If assigned, e.g. ":SPECNM :SPECVAL",
   * this overrides SpecificsOfItemGroup.templateDetail.</p>
   **/
  private Htmlt htmt;

  //Simple getters and setters:
  /**
   * <p>Getter for grp.</p>
   * @return ItmSpGr
   **/
  public final ItmSpGr getGrp() {
    return this.grp;
  }

  /**
   * <p>Setter for grp.</p>
   * @param pGrp reference
   **/
  public final void setGrp(final ItmSpGr pGrp) {
    this.grp = pGrp;
  }

  /**
   * <p>Getter for typ.</p>
   * @return EItmSpTy
   **/
  public final EItmSpTy getTyp() {
    return this.typ;
  }

  /**
   * <p>Setter for typ.</p>
   * @param pTyp reference
   **/
  public final void setTyp(final EItmSpTy pTyp) {
    this.typ = pTyp;
  }

  /**
   * <p>Getter for inFlt.</p>
   * @return Boolean
   **/
  public final Boolean getInFlt() {
    return this.inFlt;
  }

  /**
   * <p>Setter for inFlt.</p>
   * @param pInFlt reference
   **/
  public final void setInFlt(final Boolean pInFlt) {
    this.inFlt = pInFlt;
  }

  /**
   * <p>Getter for inLst.</p>
   * @return Boolean
   **/
  public final Boolean getInLst() {
    return this.inLst;
  }

  /**
   * <p>Setter for inLst.</p>
   * @param pInLst reference
   **/
  public final void setInLst(final Boolean pInLst) {
    this.inLst = pInLst;
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
   * <p>Getter for chSpTy.</p>
   * @return ChoSpTy
   **/
  public final ChoSpTy getChSpTy() {
    return this.chSpTy;
  }

  /**
   * <p>Setter for chSpTy.</p>
   * @param pChSpTy reference
   **/
  public final void setChSpTy(final ChoSpTy pChSpTy) {
    this.chSpTy = pChSpTy;
  }

  /**
   * <p>Getter for foOrd.</p>
   * @return Boolean
   **/
  public final Boolean getFoOrd() {
    return this.foOrd;
  }

  /**
   * <p>Setter for foOrd.</p>
   * @param pFoOrd reference
   **/
  public final void setFoOrd(final Boolean pFoOrd) {
    this.foOrd = pFoOrd;
  }

  /**
   * <p>Getter for htmt.</p>
   * @return Htmlt
   **/
  public final Htmlt getHtmt() {
    return this.htmt;
  }

  /**
   * <p>Setter for htmt.</p>
   * @param pHtmt reference
   **/
  public final void setHtmt(final Htmlt pHtmt) {
    this.htmt = pHtmt;
  }
}
