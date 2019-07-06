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

package org.beigesoft.acc.mdlb;

import org.beigesoft.mdlp.AIdLn;

/**
 * <p>Base model of records SQL queries sources.</p>
 *
 * @author Yury Demidenko
 */
public abstract class AEnrSrc extends AIdLn {

  /**
   * <p>Integer, Not Null Source Type e.g. 2 - PrepFr.
   * This is constant [document/line].cnsTy().</p>
   **/
  private Integer srTy;

  /**
   * <p>File name of SQL query, not null.</p>
   **/
  private String quFl;

  /**
   * <p>Is used in current method, not Null.</p>
   **/
  private Boolean used;

  /**
   * <p>Dscr.</p>
   **/
  private String dscr;

  //Simple getters and setters:
  /**
   * <p>Getter for srTy.</p>
   * @return Integer
   **/
  public final Integer getSrTy() {
    return this.srTy;
  }

  /**
   * <p>Setter for srTy.</p>
   * @param pSrTy reference
   **/
  public final void setSrTy(final Integer pSrTy) {
    this.srTy = pSrTy;
  }

  /**
   * <p>Getter for quFl.</p>
   * @return String
   **/
  public final String getQuFl() {
    return this.quFl;
  }

  /**
   * <p>Setter for quFl.</p>
   * @param pQuFl reference
   **/
  public final void setQuFl(final String pQuFl) {
    this.quFl = pQuFl;
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
   * <p>Getter for used.</p>
   * @return Boolean
   **/
  public final Boolean getUsed() {
    return this.used;
  }

  /**
   * <p>Setter for used.</p>
   * @param pUsed reference
   **/
  public final void setUsed(final Boolean pUsed) {
    this.used = pUsed;
  }
}
