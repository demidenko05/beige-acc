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

import org.beigesoft.mdlp.Lng;
import org.beigesoft.ws.mdl.EItmTy;

/**
 * <p>Model of ID of I18N specifics in list.</p>
 *
 * @author Yury Demidenko
 */
public class I18SpeLiId {

  /**
   * <p>Goods/Service/SEGoods/SEService ID, not null.</p>
   **/
  private Long itId;

  /**
   * <p>Goods/Service/SEGoods/SEService, not null.</p>
   **/
  private EItmTy typ;

  /**
   * <p>The lnguage.</p>
   **/
  private Lng lng;

  //Simple getters and setters:
  /**
   * <p>Getter for itId.</p>
   * @return Long
   **/
  public final Long getItId() {
    return this.itId;
  }

  /**
   * <p>Setter for itId.</p>
   * @param pItId reference
   **/
  public final void setItId(final Long pItId) {
    this.itId = pItId;
  }

  /**
   * <p>Getter for typ.</p>
   * @return EItmTy
   **/
  public final EItmTy getTyp() {
    return this.typ;
  }

  /**
   * <p>Setter for typ.</p>
   * @param pTyp reference
   **/
  public final void setTyp(final EItmTy pTyp) {
    this.typ = pTyp;
  }

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
