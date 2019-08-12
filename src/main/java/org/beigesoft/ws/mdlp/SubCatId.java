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

/**
 * <p>
 * Model of ID of Catalog that contains of Sucats of Goods/Services.
 * </p>
 *
 * @author Yury Demidenko
 */
public class SubCatId {

  /**
   * <p>Sucats Catalog, not null, its hasSucats=true.</p>
   **/
  private CatGs catl;

  /**
   * <p>Subatalog, not null.</p>
   **/
  private CatGs sucat;

  //Simple getters and setters:
  /**
   * <p>Getter for catl.</p>
   * @return CatGs
   **/
  public final CatGs getCatl() {
    return this.catl;
  }

  /**
   * <p>Setter for catl.</p>
   * @param pCatl reference
   **/
  public final void setCatl(final CatGs pCatl) {
    this.catl = pCatl;
  }

  /**
   * <p>Getter for sucat.</p>
   * @return CatGs
   **/
  public final CatGs getSucat() {
    return this.sucat;
  }

  /**
   * <p>Setter for sucat.</p>
   * @param pSucat reference
   **/
  public final void setSucat(final CatGs pSucat) {
    this.sucat = pSucat;
  }
}
