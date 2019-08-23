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

package org.beigesoft.ws.mdl;

import org.beigesoft.flt.AFlt;
import org.beigesoft.ws.mdlp.CatSp;

/**
 * <p>Bundle of specifics and flt.</p>
 *
 * @author Yury Demidenko
 */
public class FltSpf  {

  /**
   * <p>Catalog-Specifics.</p>
   **/
  private CatSp catSpf;

  /**
   * <p>Filter.</p>
   **/
  private AFlt flt;

  //Simple getters and setters:
  /**
   * <p>Getter for catSpf.</p>
   * @return CatSp
   **/
  public final CatSp getCatSpf() {
    return this.catSpf;
  }

  /**
   * <p>Setter for catSpf.</p>
   * @param pCatSpf reference
   **/
  public final void setCatSpf(final CatSp pCatSpf) {
    this.catSpf = pCatSpf;
  }

  /**
   * <p>Getter for flt.</p>
   * @return AFlt
   **/
  public final AFlt getFlt() {
    return this.flt;
  }

  /**
   * <p>Setter for flt.</p>
   * @param pFilter reference
   **/
  public final void setFlt(final AFlt pFilter) {
    this.flt = pFilter;
  }
}
