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

import org.beigesoft.mdlp.AIdLnNm;

/**
 * <p>
 * Model of Group of Specifics e.g. "Monitor" for its size, web-cam,
 * LED-type in a notebook.
 * </p>
 *
 * @author Yury Demidenko
 */
public class ItmSpGr extends AIdLnNm {

  /**
   * <p>HTML template start to render it, if assigned.</p>
   **/
  private Htmlt tmpls;

  /**
   * <p>HTML template end to render it, if assigned.</p>
   **/
  private Htmlt tmple;

  /**
   * <p>HTML template start to render it, if assigned.</p>
   **/
  private Htmlt tmpld;

  //Simple getters and setters:
  /**
   * <p>Getter for tmpls.</p>
   * @return Htmlt
   **/
  public final Htmlt getTmpls() {
    return this.tmpls;
  }

  /**
   * <p>Setter for tmpls.</p>
   * @param pTmpls reference
   **/
  public final void setTmpls(final Htmlt pTmpls) {
    this.tmpls = pTmpls;
  }

  /**
   * <p>Getter for tmple.</p>
   * @return Htmlt
   **/
  public final Htmlt getTmple() {
    return this.tmple;
  }

  /**
   * <p>Setter for tmple.</p>
   * @param pTmple reference
   **/
  public final void setTmple(final Htmlt pTmple) {
    this.tmple = pTmple;
  }

  /**
   * <p>Getter for tmpld.</p>
   * @return Htmlt
   **/
  public final Htmlt getTmpld() {
    return this.tmpld;
  }

  /**
   * <p>Setter for tmpld.</p>
   * @param pTmpld reference
   **/
  public final void setTmpld(final Htmlt pTmpld) {
    this.tmpld = pTmpld;
  }
}
