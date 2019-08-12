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

/**
 * <p>
 * Model of Catalog that contains of Subcatalogs of Goods/Services.
 * </p>
 *
 * @author Yury Demidenko
 */
public class SubCat extends AHasVr<SubCatId> {

  /**
   * <p>Complex ID.</p>
   **/
  private SubCatId iid;

  /**
   * <p>Subcatalog's Catalog, not null, its hsSub=true.</p>
   **/
  private CatGs catl;

  /**
   * <p>Subcatalog, not null.</p>
   **/
  private CatGs sucat;

  /**
   * <p>Usually it's simple getter that return model ID.</p>
   * @return ID model ID
   **/
  @Override
  public final SubCatId getIid() {
    return this.iid;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pIid model ID
   **/
  @Override
  public final void setIid(final SubCatId pIid) {
    this.iid = pIid;
    if (this.iid != null) {
      this.catl = this.iid.getCatl();
      this.sucat = this.iid.getSucat();
    } else {
      this.catl = null;
      this.sucat = null;
    }
  }

  /**
   * <p>Setter for catl.</p>
   * @param pCatalog reference
   **/
  public final void setCatl(final CatGs pCatalog) {
    this.catl = pCatalog;
    if (this.iid == null) {
      this.iid = new SubCatId();
    }
    this.iid.setCatl(this.catl);
  }

  /**
   * <p>Setter for sucat.</p>
   * @param pSucat reference
   **/
  public final void setSucat(final CatGs pSucat) {
    this.sucat = pSucat;
    if (this.iid == null) {
      this.iid = new SubCatId();
    }
    this.iid.setSucat(this.sucat);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for catl.</p>
   * @return CatGs
   **/
  public final CatGs getCatl() {
    return this.catl;
  }

  /**
   * <p>Getter for sucat.</p>
   * @return CatGs
   **/
  public final CatGs getSucat() {
    return this.sucat;
  }
}
