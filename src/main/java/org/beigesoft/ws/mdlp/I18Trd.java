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

import org.beigesoft.mdl.AHasVr;
import org.beigesoft.mdl.IHasNm;
import org.beigesoft.mdlp.Lng;

/**
 * <p>Model of I18N Web-store common.</p>
 *
 * @author Yury Demidenko
 */
public class I18Trd extends AHasVr<Lng> implements IHasNm {

  /**
   * <p>The lnguage, PK.</p>
   **/
  private Lng lng;

  /**
   * <p>Web-Store Name in the lnguage.</p>
   **/
  private String nme;

  /**
   * <p>Getter for iid.</p>
   * @return Lng
   **/
  @Override
  public final Lng getIid() {
    return this.lng;
  }

  /**
   * <p>Setter for iid.</p>
   * @param pIid reference
   **/
  @Override
  public final void setIid(final Lng pIid) {
    this.lng = pIid;
  }

  /**
   * <p>Geter for nme.</p>
   * @return String
   **/
  @Override
  public final String getNme() {
    return this.nme;
  }

  /**
   * <p>Setter for nme.</p>
   * @param pNme reference
   **/
  @Override
  public final void setNme(final String pNme) {
    this.nme = pNme;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for lng.</p>
   * @return Lng
   **/
  public final Lng getLng() {
    return this.lng;
  }

  /**
   * <p>Setter for lng.</p>
   * @param pLng reference
   **/
  public final void setLng(final Lng pLng) {
    this.lng = pLng;
  }
}
