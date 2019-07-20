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

import org.beigesoft.mdlp.AIdLnNm;

/**
 * <p>Model of wage tax table.</p>
 *
 * @author Yury Demidenko
 */
public class WagTt extends AIdLnNm {

  /**
   * <p>Tax.</p>
   **/
  private Tax tax;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

  /**
   * <p>Lines.</p>
   **/
  private List<WttLn> lns;

  /**
   * <p>Employees.</p>
   **/
  private List<WttEm> empls;

  /**
   * <p>Wages types.</p>
   **/
  private List<WttWg> wags;

  //SGS:
  /**
   * <p>Getter for tax.</p>
   * @return Tax
   **/
  public final Tax getTax() {
    return this.tax;
  }

  /**
   * <p>Setter for tax.</p>
   * @param pTax reference
   **/
  public final void setTax(final Tax pTax) {
    this.tax = pTax;
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
   * <p>Getter for lns.</p>
   * @return List<WttLn>
   **/
  public final List<WttLn> getLns() {
    return this.lns;
  }

  /**
   * <p>Setter for lns.</p>
   * @param pLns reference
   **/
  public final void setLns(final List<WttLn> pLns) {
    this.lns = pLns;
  }

  /**
   * <p>Getter for empls.</p>
   * @return List<WttEm>
   **/
  public final List<WttEm> getEmpls() {
    return this.empls;
  }

  /**
   * <p>Setter for empls.</p>
   * @param pEmpls reference
   **/
  public final void setEmpls(final List<WttEm> pEmpls) {
    this.empls = pEmpls;
  }

  /**
   * <p>Getter for wags.</p>
   * @return List<WttWg>
   **/
  public final List<WttWg> getWags() {
    return this.wags;
  }

  /**
   * <p>Setter for wags.</p>
   * @param pWags reference
   **/
  public final void setWags(final List<WttWg> pWags) {
    this.wags = pWags;
  }
}
