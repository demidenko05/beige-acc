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

package org.beigesoft.acc.rpl;

import org.beigesoft.mdl.IOwned;
import org.beigesoft.mdlp.AOrId;
import org.beigesoft.acc.mdlp.Acnt;

/**
 * <p>Abstract model of replication method line.</p>
 *
 * @author Yury Demidenko
 */
public abstract class ARpAcLn extends AOrId implements IOwned<RplAcc, Long> {

  /**
   * <p>Owner.</p>
   **/
  private RplAcc ownr;

  /**
   * <p>Exclude account.</p>
   **/
  private Acnt acnt;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

  /**
   * <p>Getter for ownr.</p>
   * @return RplAcc
   **/
  @Override
  public final RplAcc getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final RplAcc pOwnr) {
    this.ownr = pOwnr;
  }


  //Simple getters and setters:
  /**
   * <p>Getter for acnt.</p>
   * @return Acnt
   **/
  public final Acnt getAcnt() {
    return this.acnt;
  }

  /**
   * <p>Setter for acnt.</p>
   * @param pAcnt reference
   **/
  public final void setAcnt(final Acnt pAcnt) {
    this.acnt = pAcnt;
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
}
