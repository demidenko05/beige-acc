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

import org.beigesoft.mdlp.AIdLn;

/**
 * <p>Holds payment method data.</p>
 *
 * @author Yury Demidenko
 */
public class PayMd extends AIdLn {

  /**
   * <p>Mode, e.g. PayPal "sandbox".</p>
   **/
  private String mde;

  /**
   * <p>Secret or not phrase 1, e.g. PayPal client ID.</p>
   **/
  private String sec1;

  /**
   * <p>Secret or not phrase 2, e.g. PayPal client secret.</p>
   **/
  private String sec2;

  //Simple getters and setters:
  /**
   * <p>Getter for mde.</p>
   * @return String
   **/
  public final String getMde() {
    return this.mde;
  }

  /**
   * <p>Setter for mde.</p>
   * @param pMde reference
   **/
  public final void setMde(final String pMde) {
    this.mde = pMde;
  }

  /**
   * <p>Getter for sec1.</p>
   * @return String
   **/
  public final String getSec1() {
    return this.sec1;
  }

  /**
   * <p>Setter for sec1.</p>
   * @param pSec1 reference
   **/
  public final void setSec1(final String pSec1) {
    this.sec1 = pSec1;
  }

  /**
   * <p>Getter for sec2.</p>
   * @return String
   **/
  public final String getSec2() {
    return this.sec2;
  }

  /**
   * <p>Setter for sec2.</p>
   * @param pSec2 reference
   **/
  public final void setSec2(final String pSec2) {
    this.sec2 = pSec2;
  }
}
