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

import org.beigesoft.mdl.IOwned;
import org.beigesoft.mdlp.AIdLn;

/**
 * <p>Model of sub-account line in account.
 * Version changed time algorithm.</p>
 *
 * @author Yury Demidenko
 */
public class Sacnt extends AIdLn implements IOwned<Acnt, Long> {

  /**
   * <p>Account.</p>
   **/
  private Acnt ownr;

  /**
   * <p>Subaccount type, not null, must be same as owner's one.</p>
   **/
  private Integer saTy;

  /**
   * <p>Subaccount ID, not null.</p>
   **/
  private Long saId;

  /**
   * <p>Subaccount name, not null.</p>
   **/
  private String saNm;

  /**
   * <p>Getter for ownr.</p>
   * @return Account
   **/
  @Override
  public final Acnt getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final Acnt pOwnr) {
    this.ownr = pOwnr;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for saTy.</p>
   * @return Integer
   **/
  public final Integer getSaTy() {
    return this.saTy;
  }

  /**
   * <p>Setter for saTy.</p>
   * @param pSaTy reference
   **/
  public final void setSaTy(final Integer pSaTy) {
    this.saTy = pSaTy;
  }

  /**
   * <p>Getter for saId.</p>
   * @return Integer
   **/
  public final Long getSaId() {
    return this.saId;
  }

  /**
   * <p>Setter for saId.</p>
   * @param pSaId reference
   **/
  public final void setSaId(final Long pSaId) {
    this.saId = pSaId;
  }

  /**
   * <p>Getter for saNm.</p>
   * @return String
   **/
  public final String getSaNm() {
    return this.saNm;
  }

  /**
   * <p>Setter for saNm.</p>
   * @param pSaNm reference
   **/
  public final void setSaNm(final String pSaNm) {
    this.saNm = pSaNm;
  }
}
