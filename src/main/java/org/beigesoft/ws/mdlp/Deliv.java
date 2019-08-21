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
import org.beigesoft.acc.mdlp.Srv;
import org.beigesoft.ws.mdl.EDeliv;

/**
 * <p>Model of delivering methods.</p>
 *
 * @author Yury Demidenko
 */
public class Deliv extends AHasVr<EDeliv> {

  /**
   * <p>Delivering, PK.</p>
   **/
  private EDeliv iid;

  /**
   * <p>Forced service, if applied.</p>
   **/
  private Srv frcSr;

  /**
   * <p>Forced service applying method,
   * e.g. minimum cart total to omit delivering fee, 0 default.</p>
   **/
  private Integer apMt = 0;

  /**
   * <p>Usually it's simple getter that return model ID.</p>
   * @return ID model ID
   **/
  @Override
  public final EDeliv getIid() {
    return this.iid;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pIid model ID
   **/
  @Override
  public final void setIid(final EDeliv pIid) {
    this.iid = pIid;
  }

  /**
   * <p>Getter for frcSr.</p>
   * @return Srv
   **/
  public final Srv getFrcSr() {
    return this.frcSr;
  }

  /**
   * <p>Setter for frcSr.</p>
   * @param pFrcSr reference
   **/
  public final void setFrcSr(final Srv pFrcSr) {
    this.frcSr = pFrcSr;
  }

  /**
   * <p>Getter for apMt.</p>
   * @return Integer
   **/
  public final Integer getApMt() {
    return this.apMt;
  }

  /**
   * <p>Setter for apMt.</p>
   * @param pApMt reference
   **/
  public final void setApMt(final Integer pApMt) {
    this.apMt = pApMt;
  }
}
