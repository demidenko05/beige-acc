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

import java.util.List;
import java.util.ArrayList;

import org.beigesoft.ws.mdlp.CatGs;

/**
 * <p>Model of Catalog Of Goods/services to print on page.</p>
 *
 * @author Yury Demidenko
 */
public class TrCatl  {

  /**
   * <p>Persistable catalog.</p>
   **/
  private CatGs catl;

  /**
   * <p>Subcatalogs.</p>
   **/
  private List<TrCatl> subcatls = new ArrayList<TrCatl>();

  //Simple getters and setters:
  /**
   * <p>Getter for catalog.</p>
   * @return CatGs
   **/
  public final CatGs getCatl() {
    return this.catl;
  }

  /**
   * <p>Setter for catalog.</p>
   * @param pCatalog reference
   **/
  public final void setCatl(final CatGs pCatalog) {
    this.catl = pCatalog;
  }

  /**
   * <p>Getter for subcatalogs.</p>
   * @return List<TrCatl>
   **/
  public final List<TrCatl> getSubcatls() {
    return this.subcatls;
  }

  /**
   * <p>Setter for subcatalogs.</p>
   * @param pSubcatls reference
   **/
  public final void setSubcatls(final List<TrCatl> pSubcatls) {
    this.subcatls = pSubcatls;
  }
}
