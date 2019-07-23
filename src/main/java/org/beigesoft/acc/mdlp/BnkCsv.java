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
import org.beigesoft.mdlp.CsvMth;
import org.beigesoft.mdlp.CsvCl;

/**
 * <p>Model of bank's CSV method.</p>
 *
 * @author Yury Demidenko
 */
public class BnkCsv extends AIdLnNm {

  /**
   * <p>CSV method, not null.</p>
   **/
  private CsvMth mth;

  /**
   * <p>Date CSV Column, not null. Example formats:
   * "dd/MM/yyyy", "MM/dd/yyyy", "MM-dd-yyyy".</p>
   **/
  private CsvCl dtCl;

  /**
   * <p>Amount CSV Column, not null. Standard value is dot separated
   * number without group separators e.g. "11245.23", otherwise accepted
   * formats: "COMMA,SPACE" European standard - "11 245,45",
   * but in that case column value must be braced with text delimiter,
   * e.g. quotes; "COMMA,NONE" - "11245,45". All other separators should be
   * original, i.e. dot is ".", e.g. ".,NONE" is default format.</p>
   **/
  private CsvCl amCl;

  /**
   * <p>If used, description CSV Column.</p>
   **/
  private CsvCl dsCl;

  /**
   * <p>If used, status CSV Column,
   * column that contains of CANCELED or NOT information.</p>
   **/
  private CsvCl stCl;


  /**
   * <p>if used, comma separated words that mean that entry was ACCEPTED,
   * e.g. "OK,ACCEPTED" or single value "true".</p>
   **/
  private String acWds;

  /**
   * <p>if used, comma separated words that mean that entry was CANCELED,
   * e.g. "VOIDED,CANCELED" or single value "false".</p>
   **/
  private String vdWds;

  //Simple getters and setters:
  /**
   * <p>Getter for mth.</p>
   * @return CsvMth
   **/
  public final CsvMth getMth() {
    return this.mth;
  }

  /**
   * <p>Setter for mth.</p>
   * @param pMth reference
   **/
  public final void setMth(final CsvMth pMth) {
    this.mth = pMth;
  }

  /**
   * <p>Getter for dtCl.</p>
   * @return CsvCl
   **/
  public final CsvCl getDtCl() {
    return this.dtCl;
  }

  /**
   * <p>Setter for dtCl.</p>
   * @param pDtCl reference
   **/
  public final void setDtCl(final CsvCl pDtCl) {
    this.dtCl = pDtCl;
  }

  /**
   * <p>Getter for amCl.</p>
   * @return CsvCl
   **/
  public final CsvCl getAmCl() {
    return this.amCl;
  }

  /**
   * <p>Setter for amCl.</p>
   * @param pAmCl reference
   **/
  public final void setAmCl(final CsvCl pAmCl) {
    this.amCl = pAmCl;
  }

  /**
   * <p>Getter for dsCl.</p>
   * @return CsvCl
   **/
  public final CsvCl getDsCl() {
    return this.dsCl;
  }

  /**
   * <p>Setter for dsCl.</p>
   * @param pDsCl reference
   **/
  public final void setDsCl(final CsvCl pDsCl) {
    this.dsCl = pDsCl;
  }

  /**
   * <p>Getter for stCl.</p>
   * @return CsvCl
   **/
  public final CsvCl getStCl() {
    return this.stCl;
  }

  /**
   * <p>Setter for stCl.</p>
   * @param pStCl reference
   **/
  public final void setStCl(final CsvCl pStCl) {
    this.stCl = pStCl;
  }

  /**
   * <p>Getter for acWds.</p>
   * @return String
   **/
  public final String getAcWds() {
    return this.acWds;
  }

  /**
   * <p>Setter for acWds.</p>
   * @param pAcWds reference
   **/
  public final void setAcWds(final String pAcWds) {
    this.acWds = pAcWds;
  }

  /**
   * <p>Getter for vdWds.</p>
   * @return String
   **/
  public final String getVdWds() {
    return this.vdWds;
  }

  /**
   * <p>Setter for vdWds.</p>
   * @param pVdWds reference
   **/
  public final void setVdWds(final String pVdWds) {
    this.vdWds = pVdWds;
  }
}
