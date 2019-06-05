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

import org.beigesoft.mdlp.AIdLnNm;

/**
 * <p>Model of Currency.</p>
 *
 * @author Yury Demidenko
 */
public class Curr extends AIdLnNm {

  /**
   * <p>Sign e.g. $</p>
   **/
  private String sgn;

  /**
   * <p>String code, e.g. "USD" for integrating with systems
   * that use such currency identifiers instead of "840".</p>
   **/
  private String stCo;

  //Simple getters and setters:

  /**
   * <p>Getter for sgn.</p>
   * @return String
   **/
  public final String getSgn() {
    return this.sgn;
  }

  /**
   * <p>Setter for sgn.</p>
   * @param pSgn reference
   **/
  public final void setSgn(final String pSgn) {
    this.sgn = pSgn;
  }

  /**
   * <p>Getter for stCo.</p>
   * @return String
   **/
  public final String getStCo() {
    return this.stCo;
  }

  /**
   * <p>Setter for stCo.</p>
   * @param pStCo reference
   **/
  public final void setStCo(final String pStCo) {
    this.stCo = pStCo;
  }
}
