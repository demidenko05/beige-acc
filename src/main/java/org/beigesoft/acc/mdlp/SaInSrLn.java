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

import java.util.List;

import org.beigesoft.acc.mdlb.AInvLn;

/**
 * <p>Model of sales invoice service line.</p>
 *
 * @author Yury Demidenko
 */
public class SaInSrLn extends AInvLn<SalInv, Srv> {

  /**
   * <p>Invoice.</p>
   **/
  private SalInv ownr;

  /**
   * <p>Item.</p>
   **/
  private Srv itm;

  /**
   * <p>Item basis tax lines.</p>
   **/
  private List<SaInSrTxLn> txLns;

  /**
   * <p>Getter for ownr.</p>
   * @return SalInv
   **/
  @Override
  public final SalInv getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final SalInv pOwnr) {
    this.ownr = pOwnr;
  }

  /**
   * <p>Getter for itm.</p>
   * @return Srv
   **/
  @Override
  public final Srv getItm() {
    return this.itm;
  }

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  @Override
  public final void setItm(final Srv pItm) {
    this.itm = pItm;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for txLns.</p>
   * @return List<SaInSrTxLn>
   **/
  public final List<SaInSrTxLn> getTxLns() {
    return this.txLns;
  }

  /**
   * <p>Setter for txLns.</p>
   * @param pTxLns reference
   **/
  public final void setTxLns(final List<SaInSrTxLn> pTxLns) {
    this.txLns = pTxLns;
  }
}
