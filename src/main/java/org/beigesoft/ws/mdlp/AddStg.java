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
 * <p>Additional settings.</p>
 *
 * @author Yury Demidenko
 */
public class AddStg extends AIdLn {

  /**
   * <p>null default, HTML template for whole specifics start,
   * e.g. "&lt;ul&gt;".</p>
   **/
  private String shtms = "<ul>";

  /**
   * <p>null default, HTML template for whole specifics end,
   * e.g. "&lt;/ul&gt;".</p>
   **/
  private String shtme = "</ul>";

  /**
   * <p>null default, HTML template for group specifics start,
   * e.g. "&lt;li&gt;".</p>
   **/
  private String sghtms = "<li>";

  /**
   * <p>null default, HTML template for group specifics end,
   * e.g. "&lt;/li&gt;".</p>
   **/
  private String sghtme = "</li>";

  /**
   * <p>not null, specifics separator, default ",".</p>
   **/
  private String speSp = ",";

  /**
   * <p>null default, specifics groups separator, e.g ";".</p>
   **/
  private String spGrSp;

  /**
   * <p>Records per transaction, 100 default.</p>
   **/
  private Integer rcsTr = 100;

  /**
   * <p>Booking transaction isolation mode ISrvDatabase:
   * <ul>
   * <li>1 read uncommited.</li>
   * <li>2 read commited, default.</li>
   * <li>3 repeatable read.</li>
   * <li>4 serializable.</li>
   * </ul>
   * </p>
   **/
  private Integer bkTr = 2;

  /**
   * <p>Online payment mode:
   * <ul>
   * <li>0 any seller is payee.</li>
   * <li>1 only owner is payee,
   *   i.e. itself sent part of payment to S.E.Seller(s).</li>
   * </ul>
   * </p>
   **/
  private Integer onlMd = 0;

  //Simple getters and setters:
  /**
   * <p>Getter for shtms.</p>
   * @return String
   **/
  public final String getShtms() {
    return this.shtms;
  }

  /**
   * <p>Setter for shtms.</p>
   * @param pShtms reference
   **/
  public final void setShtms(final String pShtms) {
    this.shtms = pShtms;
  }

  /**
   * <p>Getter for shtme.</p>
   * @return String
   **/
  public final String getShtme() {
    return this.shtme;
  }

  /**
   * <p>Setter for shtme.</p>
   * @param pShtme reference
   **/
  public final void setShtme(final String pShtme) {
    this.shtme = pShtme;
  }

  /**
   * <p>Getter for sghtms.</p>
   * @return String
   **/
  public final String getSghtms() {
    return this.sghtms;
  }

  /**
   * <p>Setter for sghtms.</p>
   * @param pSghtms reference
   **/
  public final void setSghtms(final String pSghtms) {
    this.sghtms = pSghtms;
  }

  /**
   * <p>Getter for sghtme.</p>
   * @return String
   **/
  public final String getSghtme() {
    return this.sghtme;
  }

  /**
   * <p>Setter for sghtme.</p>
   * @param pSghtme reference
   **/
  public final void setSghtme(final String pSghtme) {
    this.sghtme = pSghtme;
  }

  /**
   * <p>Getter for speSp.</p>
   * @return String
   **/
  public final String getSpeSp() {
    return this.speSp;
  }

  /**
   * <p>Setter for speSp.</p>
   * @param pSpeSp reference
   **/
  public final void setSpeSp(final String pSpeSp) {
    this.speSp = pSpeSp;
  }

  /**
   * <p>Getter for spGrSp.</p>
   * @return String
   **/
  public final String getSpGrSp() {
    return this.spGrSp;
  }

  /**
   * <p>Setter for spGrSp.</p>
   * @param pSpGrSp reference
   **/
  public final void setSpGrSp(final String pSpGrSp) {
    this.spGrSp = pSpGrSp;
  }

  /**
   * <p>Getter for rcsTr.</p>
   * @return Integer
   **/
  public final Integer getRcsTr() {
    return this.rcsTr;
  }

  /**
   * <p>Setter for rcsTr.</p>
   * @param pRcsTr reference
   **/
  public final void setRcsTr(final Integer pRcsTr) {
    this.rcsTr = pRcsTr;
  }

  /**
   * <p>Getter for bkTr.</p>
   * @return Integer
   **/
  public final Integer getBkTr() {
    return this.bkTr;
  }

  /**
   * <p>Setter for bkTr.</p>
   * @param pBkTr reference
   **/
  public final void setBkTr(final Integer pBkTr) {
    this.bkTr = pBkTr;
  }

  /**
   * <p>Getter for onlMd.</p>
   * @return Integer
   **/
  public final Integer getOnlMd() {
    return this.onlMd;
  }

  /**
   * <p>Setter for onlMd.</p>
   * @param pOnlMd reference
   **/
  public final void setOnlMd(final Integer pOnlMd) {
    this.onlMd = pOnlMd;
  }
}
