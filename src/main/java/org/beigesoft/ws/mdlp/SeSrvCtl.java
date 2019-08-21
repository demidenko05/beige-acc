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
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE serviceS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.beigesoft.ws.mdlp;

import org.beigesoft.ws.mdlb.AItmCtl;

/**
 * <p>Model of S.E.service in catalog.</p>
 *
 * @author Yury Demidenko
 */
public class SeSrvCtl extends AItmCtl<SeSrv, SeSrvCtlId> {

  /**
   * <p>ID.</p>
   **/
  private SeSrvCtlId iid;

  /**
   * <p>service.</p>
   **/
  private SeSrv itm;

  /**
   * <p>Catalog, not null, its hsSub=false.</p>
   **/
  private CatGs catl;

  /**
   * <p>Usually it's simple getter that return model ID.</p>
   * @return SeSrvCtlId model ID
   **/
  @Override
  public final SeSrvCtlId getIid() {
    return this.iid;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pIid model ID
   **/
  @Override
  public final void setIid(final SeSrvCtlId pIid) {
    this.iid = pIid;
    if (this.iid != null) {
      this.catl = this.iid.getCatl();
      setItm(this.iid.getItm());
    } else {
      this.catl = null;
      setItm(null);
    }
  }

  /**
   * <p>Setter for pCatl.</p>
   * @param pCatl reference
   **/
  @Override
  public final void setCatl(final CatGs pCatl) {
    this.catl = pCatl;
    if (this.iid == null) {
      this.iid = new SeSrvCtlId();
    }
    this.iid.setCatl(this.catl);
  }

  /**
   * <p>Getter for pCatl.</p>
   * @return pCatl reference
   **/
  @Override
  public final CatGs getCatl() {
    return this.catl;
  }

  /**
   * <p>Getter for itm.</p>
   * @return SeSrv
   **/
  @Override
  public final SeSrv getItm() {
    return this.itm;
  }

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  @Override
  public final void setItm(final SeSrv pItm) {
    this.itm = pItm;
    if (this.iid == null) {
      this.iid = new SeSrvCtlId();
    }
    this.iid.setItm(this.itm);
  }
}
