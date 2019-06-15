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

/**
 * <p>Abstract model of document that makes accounting entries,
 * e.g. Purchase Invoice.</p>
 *
 * @author Yury Demidenko
 */
public abstract class ADoc extends AEntrSrc implements IDoc {

  /**
   * <p>If has made entries.</p>
   **/
  private Boolean mdEnr;

  /**
   * <p>ID of reversed/reversing doc.</p>
   **/
  private Long rvId;

  /**
   * <p>ID database birth of reversed/reversing doc.</p>
   **/
  private Integer rvDbOr;

  /**
   * <p>Total.</p>
   **/
  private BigDecimal tot = BigDecimal.ZERO;

  /**
   * <p>Total in foreign currency.</p>
   **/
  private BigDecimal toFc = BigDecimal.ZERO;

  /**
   * <p>Getter for mdEnr.</p>
   * @return Boolean
   **/
  @Override
  public final Boolean getMdEnr() {
    return this.mdEnr;
  }

  /**
   * <p>Setter for mdEnr.</p>
   * @param pMdEnr reference
   **/
  @Override
  public final void setMdEnr(final Boolean pMdEnr) {
    this.mdEnr = pMdEnr;
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
   * <p>Getter for rvDbOr.</p>
   * @return Integer
   **/
  @Override
  public final Integer getRvDbOr() {
    return this.rvDbOr;
  }

  /**
   * <p>Setter for rvDbOr.</p>
   * @param pRvDbOr reference
   **/
  @Override
  public final void setRvDbOr(final Integer pRvDbOr) {
    this.rvDbOr = pRvDbOr;
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
}
