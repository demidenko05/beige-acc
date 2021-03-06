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
 * it's inner document without foreign currency  e.g. manufacturing,
 * using supplies.</p>
 *
 * @author Yury Demidenko
 */
public abstract class ADoci extends AEntrSrc implements IDoci {

  /**
   * <p>If has made entries.</p>
   **/
  private Boolean mdEnr = Boolean.FALSE;

  /**
   * <p>ID of reversed/reversing doc.</p>
   **/
  private Long rvId;

  /**
   * <p>Total.</p>
   **/
  private BigDecimal tot = BigDecimal.ZERO;

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
}
