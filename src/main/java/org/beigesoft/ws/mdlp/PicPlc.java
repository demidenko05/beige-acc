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
 * Model of Pick-Up Place for goods means where it is located,
 * e.g. for small store there is only place e.g. "shop".
 * For a service it means either where is service performed
 * (e.g. haircut saloon) or service maker/s location
 * (for services that performed in the buyer territory
 * e.g. fix faucet by plumber). It's used for goods/service availability
 * and can be used for buyer that prefer pick up goods or
 * get service at chosen place.
 * </p>
 *
 * @author Yury Demidenko
 */
public class PicPlc extends AIdLnNm {

  /**
   * <p>Latitude if used.</p>
   **/
  private String latid;

  /**
   * <p>Longitude if used.</p>
   **/
  private String lonud;

  /**
   * <p>Time zone GMT+ minutes if used.</p>
   **/
  private Integer tmZn;

  //Simple getters and setters:

  /**
   * <p>Getter for latid.</p>
   * @return String
   **/
  public final String getLatid() {
    return this.latid;
  }

  /**
   * <p>Setter for latid.</p>
   * @param pLatid reference
   **/
  public final void setLatid(final String pLatid) {
    this.latid = pLatid;
  }

  /**
   * <p>Getter for lonud.</p>
   * @return String
   **/
  public final String getLonud() {
    return this.lonud;
  }

  /**
   * <p>Setter for lonud.</p>
   * @param pLonud reference
   **/
  public final void setLonud(final String pLonud) {
    this.lonud = pLonud;
  }

  /**
   * <p>Getter for tmZn.</p>
   * @return Integer
   **/
  public final Integer getTmZn() {
    return this.tmZn;
  }

  /**
   * <p>Setter for tmZn.</p>
   * @param pTmZn reference
   **/
  public final void setTmZn(final Integer pTmZn) {
    this.tmZn = pTmZn;
  }
}
