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
  private String shtms;

  /**
   * <p>null default, HTML template for whole specifics start,
   * e.g. "&lt;/ul&gt;".</p>
   **/
  private String shtme;

  /**
   * <p>null default, HTML template for group specifics start,
   * e.g. "&lt;li&gt;".</p>
   **/
  private String sghtms;

  /**
   * <p>null default, HTML template for group specifics start,
   * e.g. "&lt;/li&gt;".</p>
   **/
  private String sghtme;

  /**
   * <p>not null, specifics separator, default ",".</p>
   **/
  private String speSp = ",";

  /**
   * <p>null default, specifics groups separator, e.g ";".</p>
   **/
  private String spGrSp;

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
}
