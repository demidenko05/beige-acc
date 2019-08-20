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
import org.beigesoft.ws.mdlb.AItmPlc;

/**
 * <p>Model of service available at pickup place.</p>
 *
 * @author Yury Demidenko
 */
public class SrvPlc extends AItmPlc<Srv, SrvPlcId> {

  /**
   * <p>Complex ID.</p>
   **/
  private SrvPlcId iid;

  /**
   * <p>Pick up (storage) place, not null.</p>
   **/
  private PicPlc pipl;

  /**
   * <p>Service, not null.</p>
   **/
  private Srv itm;

  /**
   * <p>Distance if present in hundred meters, i.e. 1 means 100meters.</p>
   **/
  private Long dist;

  /**
   * <p>Usually it's simple getter that return model ID.</p>
   * @return ID model ID
   **/
  @Override
  public final SrvPlcId getIid() {
    return this.iid;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pIid model ID
   **/
  @Override
  public final void setIid(final SrvPlcId pIid) {
    this.iid = pIid;
    if (this.iid != null) {
      this.pipl = this.iid.getPipl();
      this.itm = this.iid.getItm();
    } else {
      this.pipl = null;
      this.itm = null;
    }
  }

  /**
   * <p>Getter for pipl.</p>
   * @return PicPlc
   **/
  @Override
  public final PicPlc getPipl() {
    return this.pipl;
  }

  /**
   * <p>Setter for pipl.</p>
   * @param pPicPlc reference
   **/
  @Override
  public final void setPipl(final PicPlc pPicPlc) {
    this.pipl = pPicPlc;
    if (this.iid == null) {
      this.iid = new SrvPlcId();
    }
    this.iid.setPipl(this.pipl);
  }

  /**
   * <p>Getter for item.</p>
   * @return Srv
   **/
  @Override
  public final Srv getItm() {
    return this.itm;
  }

  /**
   * <p>Setter for item.</p>
   * @param pSrv reference
   **/
  @Override
  public final void setItm(final Srv pSrv) {
    this.itm = pSrv;
    if (this.iid == null) {
      this.iid = new SrvPlcId();
    }
    this.iid.setItm(this.itm);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for dist.</p>
   * @return Long
   **/
  public final Long getDist() {
    return this.dist;
  }

  /**
   * <p>Setter for dist.</p>
   * @param pDist reference
   **/
  public final void setDist(final Long pDist) {
    this.dist = pDist;
  }
}
