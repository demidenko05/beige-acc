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
 * <p>Model of Price category for Buyer.</p>
 *
 * @author Yury Demidenko
 */
public class BurPric extends AHasVr<BurPricId> {

  /**
   * <p>Complex ID.</p>
   **/
  private BurPricId iid;

  /**
   * <p>Price Category.</p>
   **/
  private PriCt priCt;

  /**
   * <p>Buyer, not null.</p>
   **/
  private Buyer buyr;

  /**
   * <p>Usually it's simple getter that return model ID.</p>
   * @return ID model ID
   **/
  @Override
  public final BurPricId getIid() {
    return this.iid;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pIid model ID
   **/
  @Override
  public final void setIid(final BurPricId pIid) {
    this.iid = pIid;
    if (this.iid != null) {
      this.priCt = this.iid.getPriCt();
      this.buyr = this.iid.getBuyr();
    } else {
      this.priCt = null;
      this.buyr = null;
    }
  }

  /**
   * <p>Setter for pPriCt.</p>
   * @param pPriCt reference
   **/
  public final void setPriCt(final PriCt pPriCt) {
    this.priCt = pPriCt;
    if (this.iid == null) {
      this.iid = new BurPricId();
    }
    this.iid.setPriCt(this.priCt);
  }

  /**
   * <p>Setter for buyer.</p>
   * @param pBuyer reference
   **/
  public final void setBuyr(final Buyer pBuyer) {
    this.buyr = pBuyer;
    if (this.iid == null) {
      this.iid = new BurPricId();
    }
    this.iid.setBuyr(this.buyr);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for priCt.</p>
   * @return PriCt
   **/
  public final PriCt getPriCt() {
    return this.priCt;
  }

  /**
   * <p>Getter for buyr.</p>
   * @return Buyer
   **/
  public final Buyer getBuyr() {
    return this.buyr;
  }
}
