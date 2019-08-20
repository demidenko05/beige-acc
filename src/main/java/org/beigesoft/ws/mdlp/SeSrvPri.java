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

import org.beigesoft.ws.mdlb.AItmPri;
import org.beigesoft.ws.mdlb.IHsSeSel;

/**
 * <p>Model of service price.</p>
 *
 * @author Yury Demidenko
 */
public class SeSrvPri extends AItmPri<SeSrv, SeSrvPriId>
  implements IHsSeSel<SeSrvPriId> {

  /**
   * <p>Complex ID.</p>
   **/
  private SeSrvPriId iid;

  /**
   * <p>Service, not null.</p>
   **/
  private SeSrv itm;

  /**
   * <p>Price Category.</p>
   **/
  private PriCt priCt;

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
  public final SeSrvPriId getIid() {
    return this.iid;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pIid model ID
   **/
  @Override
  public final void setIid(final SeSrvPriId pIid) {
    this.iid = pIid;
    if (this.iid != null) {
      this.priCt = this.iid.getPriCt();
      this.itm = this.iid.getItm();
    } else {
      this.priCt = null;
      this.itm = null;
    }
  }

  /**
   * <p>Getter for priCt.</p>
   * @return PriCt
   **/
  @Override
  public final PriCt getPriCt() {
    return this.priCt;
  }

  /**
   * <p>Setter for priCt.</p>
   * @param pPriCt reference
   **/
  @Override
  public final void setPriCt(final PriCt pPriCt) {
    this.priCt = pPriCt;
    if (this.iid == null) {
      this.iid = new SeSrvPriId();
    }
    this.iid.setPriCt(this.priCt);
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
      this.iid = new SeSrvPriId();
    }
    this.iid.setItm(this.itm);
  }
}
