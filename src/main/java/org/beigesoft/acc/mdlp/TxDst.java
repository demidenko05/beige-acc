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

import java.math.RoundingMode;

import org.beigesoft.mdlp.AIdLnNm;

/**
 * <p>Tax destination.</p>
 *
 * @author Yury Demidenko
 */
public class TxDst extends AIdLnNm {

  /**
   * <p>Destination Zip.</p>
   **/
  private String zip;

  /**
   * <p>Rounding mode for sales taxes.</p>
   **/
  private RoundingMode stRm = RoundingMode.HALF_UP;

  /**
   * <p>Grouping method for sales taxes - false item basis, true - invoice.
   * This is about grouping rounding error:
   * round(2.244 + 2.244) != round(2.244) + round(2.244);
   * 4.49 != 4.48
   * </p>
   **/
  private Boolean stIb = Boolean.FALSE;

  /**
   * <p>Use aggregate tax rate or only tax.</p>
   **/
  private Boolean stAg = Boolean.FALSE;

  //Simple getters and setters:

  /**
   * <p>Getter for zip.</p>
   * @return String
   **/
  public final String getZip() {
    return this.zip;
  }

  /**
   * <p>Setter for zip.</p>
   * @param pZip reference
   **/
  public final void setZip(final String pZip) {
    this.zip = pZip;
  }

  /**
   * <p>Getter for stRm.</p>
   * @return RoundingMode
   **/
  public final RoundingMode getStRm() {
    return this.stRm;
  }

  /**
   * <p>Setter for stRm.</p>
   * @param pStRm reference
   **/
  public final void setStRm(final RoundingMode pStRm) {
    this.stRm = pStRm;
  }

  /**
   * <p>Getter for stIb.</p>
   * @return Boolean
   **/
  public final Boolean getStIb() {
    return this.stIb;
  }

  /**
   * <p>Setter for stIb.</p>
   * @param pStIb reference
   **/
  public final void setStIb(final Boolean pStIb) {
    this.stIb = pStIb;
  }

  /**
   * <p>Getter for stAg.</p>
   * @return Boolean
   **/
  public final Boolean getStAg() {
    return this.stAg;
  }

  /**
   * <p>Setter for stAg.</p>
   * @param pStAg reference
   **/
  public final void setStAg(final Boolean pStAg) {
    this.stAg = pStAg;
  }
}
