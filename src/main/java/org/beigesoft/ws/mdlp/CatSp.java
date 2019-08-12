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

import org.beigesoft.mdl.IOwned;
import org.beigesoft.mdlp.AIdLnNm;

/**
 * <p>
 * Filterable/orderable specifics that are used for items
 * in that catalog and its sub-catalogs.
 * It's used to make filter/order for item's list.
 * It's made either by hand - admin add FO specifics to a catalog,
 * or by service that checked items with FO specifics and added to catalog.
 * </p>
 *
 * @author Yury Demidenko
 */
public class CatSp extends AIdLnNm implements IOwned<CatGs, Long> {

  /**
   * <p>Catalog.</p>
   **/
  private CatGs ownr;

  /**
   * <p>Used filterable/orderable specifics.</p>
   **/
  private ItmSp spec;

  /**
   * <p>If used, means ID of customized filter, e.g. "231" means
   * using custom filter231.jsp for RAM size (set of size ranges)
   * instead of regular(usual/default) filter
   * integer (less, greater, from-to value1/2).</p>
   **/
  private Integer fltId;

  /**
   * <p>Getter for ownr.</p>
   * @return CatGs
   **/
  @Override
  public final CatGs getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final CatGs pOwnr) {
    this.ownr = pOwnr;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for spec.</p>
   * @return ItmSp
   **/
  public final ItmSp getSpec() {
    return this.spec;
  }

  /**
   * <p>Setter for spec.</p>
   * @param pSpec reference
   **/
  public final void setSpec(final ItmSp pSpec) {
    this.spec = pSpec;
  }

  /**
   * <p>Getter for fltId.</p>
   * @return Integer
   **/
  public final Integer getFltId() {
    return this.fltId;
  }

  /**
   * <p>Setter for fltId.</p>
   * @param pFltId reference
   **/
  public final void setFltId(final Integer pFltId) {
    this.fltId = pFltId;
  }
}
