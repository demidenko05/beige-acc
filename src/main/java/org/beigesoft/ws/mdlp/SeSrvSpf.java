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

import org.beigesoft.ws.mdlb.AItmSpf;
import org.beigesoft.ws.mdlb.IHsSeSel;

/**
 * <p>Model of Specifics values for a service.</p>
 *
 * @author Yury Demidenko
 */
public class SeSrvSpf extends AItmSpf<SeSrv, SeSrvSpfId>
  implements IHsSeSel<SeSrvSpfId> {

  /**
   * <p>Complex ID.</p>
   **/
  private SeSrvSpfId iid;

  /**
   * <p>Specifics.</p>
   **/
  private ItmSp spec;

  /**
   * <p>Good.</p>
   **/
  private SeSrv itm;

  /**
   * <p>Getter for selr.</p>
   * @return SeSel
   **/
  @Override
  public final SeSel getSelr() {
    return this.itm.getSelr();
  }

  /**
   * <p>Setter for selr.</p>
   * @param pSelr reference
   **/
  @Override
  public final void setSelr(final SeSel pSelr) {
    this.itm.setSelr(pSelr);
  }

  /**
   * <p>Usually it's simple getter that return model ID.</p>
   * @return ID model ID
   **/
  @Override
  public final SeSrvSpfId getIid() {
    return this.iid;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pIid model ID
   **/
  @Override
  public final void setIid(final SeSrvSpfId pIid) {
    this.iid = pIid;
    if (this.iid != null) {
      setSpec(this.iid.getSpec());
      setItm(this.iid.getItm());
    } else {
      setSpec(null);
      setItm(null);
    }
  }

  /**
   * <p>Setter for spec.</p>
   * @param pSpec reference
   **/
  @Override
  public final void setSpec(final ItmSp pSpec) {
    this.spec = pSpec;
    if (this.iid == null) {
      this.iid = new SeSrvSpfId();
    }
    this.iid.setSpec(this.spec);
  }

  /**
   * <p>Getter for spec.</p>
   * @return ItmSp
   **/
  @Override
  public final ItmSp getSpec() {
    return this.spec;
  }

  /**
   * <p>Getter for itm.</p>
   * @return T
   **/
  @Override
  public final SeSrv getItm() {
    return this.itm;
  }

  /**
   * <p>Setter for itm.</p>
   * @param pItem reference
   **/
  @Override
  public final void setItm(final SeSrv pItem) {
    this.itm = pItem;
    if (this.iid == null) {
      this.iid = new SeSrvSpfId();
    }
    this.iid.setItm(this.itm);
  }
}
