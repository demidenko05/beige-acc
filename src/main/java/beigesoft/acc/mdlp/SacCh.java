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

import org.beigesoft.mdl.AHasVr;
import org.beigesoft.mdl.IHasNm;

/**
 * <p>Model of sub-account name changed, so entries (sacNm, sadNm)
 * and balances(saNm) may be dirty.</p>
 *
 * @author Yury Demidenko
 */
public class SacCh extends AHasVr<SacChId> implements IHasNm {

  /**
   * <p>Subaccount id,  type, not null, PK.</p>
   **/
  private SacChId iid;

  /**
   * <p>Subaccount ID, not null, PK.</p>
   **/
  private Long saId;

  /**
   * <p>Subaccount type, not null, PK.</p>
   **/
  private Integer saTy;

  /**
   * <p>Subaccount last name.</p>
   **/
  private String nme;

  /**
   * <p>If dirty (name changed, entries may have old names), not null.</p>
   **/
  private Boolean drt;

  /**
   * <p>Geter for nme.</p>
   * @return String
   **/
  @Override
  public final String getNme() {
    return this.nme;
  }

  /**
   * <p>Setter for nme.</p>
   * @param pNme reference
   **/
  @Override
  public final void setNme(final String pNme) {
    this.nme = pNme;
  }

  /**
   * <p>Getter for iid.</p>
   * @return SacChId
   **/
  @Override
  public final SacChId getIid() {
    return this.iid;
  }

  /**
   * <p>Setter for iid.</p>
   * @param pIid reference
   **/
  @Override
  public final void setIid(final SacChId pIid) {
    this.iid = pIid;
    if (this.iid == null) {
      this.saId = null;
      this.saTy = null;
    } else {
      this.saId = this.iid.getSaId();
      this.saTy = this.iid.getSaTy();
    }
  }

  /**
   * <p>Setter for saId.</p>
   * @param pSaId reference
   **/
  public final void setSaId(final Long pSaId) {
    this.saId = pSaId;
    if (this.iid == null) {
      this.iid = new SacChId();
    }
    this.iid.setSaId(this.saId);
  }

  /**
   * <p>Setter for saTy.</p>
   * @param pSaTy reference
   **/
  public final void setSaTy(final Integer pSaTy) {
    this.saTy = pSaTy;
    if (this.iid == null) {
      this.iid = new SacChId();
    }
    this.iid.setSaTy(this.saTy);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for saId.</p>
   * @return Long
   **/
  public final Long getSaId() {
    return this.saId;
  }

  /**
   * <p>Getter for saTy.</p>
   * @return Integer
   **/
  public final Integer getSaTy() {
    return this.saTy;
  }

  /**
   * <p>Getter for drt.</p>
   * @return Boolean
   **/
  public final Boolean getDrt() {
    return this.drt;
  }

  /**
   * <p>Setter for drt.</p>
   * @param pDrt reference
   **/
  public final void setDrt(final Boolean pDrt) {
    this.drt = pDrt;
  }
}
