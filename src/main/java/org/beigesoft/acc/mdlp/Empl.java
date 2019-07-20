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

import java.util.Date;
import java.util.List;

import org.beigesoft.mdlp.AIdLnNm;
import org.beigesoft.acc.mdlb.ISacnt;

/**
 * <p>Model of employee.</p>
 *
 * @author Yury Demidenko
 */
public class Empl extends AIdLnNm implements ISacnt {

  /**
   * <p>Category, not null.</p>
   **/
  private EmpCt cat;

  /**
   * <p>Not Null, tax identification number e.g. SSN for US.</p>
   **/
  private String tin;


  /**
   * <p>Not Null, date of hire.</p>
   **/
  private Date daHi;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

  /**
   * <p>Year Wage Lines.</p>
   **/
  private List<EmpWg> yrWgs;

  /**
   * <p>OOP friendly Constant of code type 1010.</p>
   * @return 1010
   **/
  @Override
  public final Integer cnsTy() {
    return 1010;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for cat.</p>
   * @return EmpCt
   **/
  public final EmpCt getCat() {
    return this.cat;
  }

  /**
   * <p>Setter for cat.</p>
   * @param pCat reference
   **/
  public final void setCat(final EmpCt pCat) {
    this.cat = pCat;
  }

  /**
   * <p>Getter for tin.</p>
   * @return String
   **/
  public final String getTin() {
    return this.tin;
  }

  /**
   * <p>Setter for tin.</p>
   * @param pTin reference
   **/
  public final void setTin(final String pTin) {
    this.tin = pTin;
  }

  /**
   * <p>Getter for daHi.</p>
   * @return Date
   **/
  public final Date getDaHi() {
    return this.daHi;
  }

  /**
   * <p>Setter for daHi.</p>
   * @param pDaHi reference
   **/
  public final void setDaHi(final Date pDaHi) {
    this.daHi = pDaHi;
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
   * <p>Getter for yrWgs.</p>
   * @return List<EmpWg>
   **/
  public final List<EmpWg> getYrWgs() {
    return this.yrWgs;
  }

  /**
   * <p>Setter for yrWgs.</p>
   * @param pYrWgs reference
   **/
  public final void setYrWgs(final List<EmpWg> pYrWgs) {
    this.yrWgs = pYrWgs;
  }
}
