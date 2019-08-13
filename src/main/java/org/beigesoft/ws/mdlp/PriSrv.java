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

import org.beigesoft.acc.mdlp.Srv;
import org.beigesoft.ws.mdlb.AItmPri;

/**
 * <p>Model of service price.</p>
 *
 * @author Yury Demidenko
 */
public class PriSrv extends AItmPri<Srv, PriSrvId> {

  /**
   * <p>Complex ID.</p>
   **/
  private PriSrvId iid;

  /**
   * <p>Service, not null.</p>
   **/
  private Srv itm;

  /**
   * <p>Price Category.</p>
   **/
  private PriCt priCt;

  /**
   * <p>Usually it's simple getter that return model ID.</p>
   * @return ID model ID
   **/
  @Override
  public final PriSrvId getIid() {
    return this.iid;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pIid model ID
   **/
  @Override
  public final void setIid(final PriSrvId pIid) {
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
      this.iid = new PriSrvId();
    }
    this.iid.setPriCt(this.priCt);
  }

  /**
   * <p>Getter for itm.</p>
   * @return Srv
   **/
  @Override
  public final Srv getItm() {
    return this.itm;
  }

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  @Override
  public final void setItm(final Srv pItm) {
    this.itm = pItm;
    if (this.iid == null) {
      this.iid = new PriSrvId();
    }
    this.iid.setItm(this.itm);
  }
}
