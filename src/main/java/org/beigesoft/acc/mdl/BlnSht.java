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

package org.beigesoft.acc.mdl;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

/**
 * <p>Balance Sheet.</p>
 *
 * @author Yury Demidenko
 */
public class BlnSht {

  /**
   * <p>Date of balance.</p>
   **/
  private Date dat;

  /**
   * <p>Lines.</p>
   **/
  private List<BlnLn> lns = new ArrayList<BlnLn>();

  /**
   * <p>Total assets.</p>
   **/
  private BigDecimal totAss = BigDecimal.ZERO;

  /**
   * <p>Total liabilities.</p>
   **/
  private BigDecimal totLia = BigDecimal.ZERO;

  /**
   * <p>Total owners equity.</p>
   **/
  private BigDecimal totQwe = BigDecimal.ZERO;

  /**
   * <p>Total lines assets.</p>
   **/
  private Integer toLnAs = 0;

  /**
   * <p>Total lines liabilities.</p>
   **/
  private Integer toLnLi = 0;

  /**
   * <p>Total lines owners equity.</p>
   **/
  private Integer toLnOe = 0;

  /**
   * <p>Detail Rows Count (assets vs l&oe).</p>
   **/
  private Integer detRc = 0;

  //Simple getters and setters:
  /**
   * <p>Getter for dat.</p>
   * @return Date
   **/
  public final Date getDat() {
    return this.dat;
  }

  /**
   * <p>Setter for dat.</p>
   * @param pDat reference
   **/
  public final void setDat(final Date pDat) {
    this.dat = pDat;
  }

  /**
   * <p>Getter for lns.</p>
   * @return List<BlnLn>
   **/
  public final List<BlnLn> getLns() {
    return this.lns;
  }

  /**
   * <p>Setter for lns.</p>
   * @param pLns reference
   **/
  public final void setLns(final List<BlnLn> pLns) {
    this.lns = pLns;
  }

  /**
   * <p>Getter for totAss.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTotAss() {
    return this.totAss;
  }

  /**
   * <p>Setter for totAss.</p>
   * @param pTotAss reference
   **/
  public final void setTotAss(final BigDecimal pTotAss) {
    this.totAss = pTotAss;
  }

  /**
   * <p>Getter for totLia.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTotLia() {
    return this.totLia;
  }

  /**
   * <p>Setter for totLia.</p>
   * @param pTotLia reference
   **/
  public final void setTotLia(final BigDecimal pTotLia) {
    this.totLia = pTotLia;
  }

  /**
   * <p>Getter for totQwe.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getTotQwe() {
    return this.totQwe;
  }

  /**
   * <p>Setter for totQwe.</p>
   * @param pTotQwe reference
   **/
  public final void setTotQwe(final BigDecimal pTotQwe) {
    this.totQwe = pTotQwe;
  }

  /**
   * <p>Getter for toLnAs.</p>
   * @return Integer
   **/
  public final Integer getToLnAs() {
    return this.toLnAs;
  }

  /**
   * <p>Setter for toLnAs.</p>
   * @param pToLnAs reference
   **/
  public final void setToLnAs(final Integer pToLnAs) {
    this.toLnAs = pToLnAs;
  }

  /**
   * <p>Getter for toLnLi.</p>
   * @return Integer
   **/
  public final Integer getToLnLi() {
    return this.toLnLi;
  }

  /**
   * <p>Setter for toLnLi.</p>
   * @param pToLnLi reference
   **/
  public final void setToLnLi(final Integer pToLnLi) {
    this.toLnLi = pToLnLi;
  }

  /**
   * <p>Getter for toLnOe.</p>
   * @return Integer
   **/
  public final Integer getToLnOe() {
    return this.toLnOe;
  }

  /**
   * <p>Setter for toLnOe.</p>
   * @param pToLnOe reference
   **/
  public final void setToLnOe(final Integer pToLnOe) {
    this.toLnOe = pToLnOe;
  }

  /**
   * <p>Getter for detRc.</p>
   * @return Integer
   **/
  public final Integer getDetRc() {
    return this.detRc;
  }

  /**
   * <p>Setter for detRc.</p>
   * @param pDetRc reference
   **/
  public final void setDetRc(final Integer pDetRc) {
    this.detRc = pDetRc;
  }
}
