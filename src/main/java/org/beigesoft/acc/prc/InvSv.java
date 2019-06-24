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

package org.beigesoft.acc.prc;

import java.util.Map;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.mdlb.AInvLn;
import org.beigesoft.acc.mdlp.Itm;
import org.beigesoft.acc.mdlp.Srv;
import org.beigesoft.acc.srv.IRvInvLn;
import org.beigesoft.acc.srv.SrInvSv;

/**
 * <p>Service that saves invoice account in head into DB.</p>
 *
 * @param <T> invoice type
 * @param <G> invoice goods line type
 * @param <S> invoice service line type
 * @author Yury Demidenko
 */
public class InvSv<T extends AInv, G extends AInvLn<T, Itm>,
  S extends AInvLn<T, Srv>> implements IPrcEnt<T, Long> {

  /**
   * <p>Saving service.</p>
   **/
  private SrInvSv srInvSv;

  /**
   * <p>Reverser service for good line.</p>
   **/
  private IRvInvLn<T, G> rvInGdLn;

  /**
   * <p>Reverser service for service line.</p>
   **/
  private IRvInvLn<T, S> rvInSrLn;

  /**
   * <p>Process that saves entity.</p>
   * @param pRvs request scoped vars, e.g. return this line's
   * owner(document) in "nextEntity" for farther processing
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final T process(final Map<String, Object> pRvs, final T pEnt,
    final IReqDt pRqDt) throws Exception {
    return this.srInvSv.save(pRvs, pEnt, pRqDt, this.rvInGdLn, this.rvInSrLn);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srInvSv.</p>
   * @return SrInvSv
   **/
  public final SrInvSv getSrInvSv() {
    return this.srInvSv;
  }

  /**
   * <p>Setter for srInvSv.</p>
   * @param pSrInvSv reference
   **/
  public final void setSrInvSv(final SrInvSv pSrInvSv) {
    this.srInvSv = pSrInvSv;
  }

  /**
   * <p>Getter for rvInGdLn.</p>
   * @return IRvInvLn<T, G>
   **/
  public final IRvInvLn<T, G> getRvInGdLn() {
    return this.rvInGdLn;
  }

  /**
   * <p>Setter for rvInGdLn.</p>
   * @param pRvInGdLn reference
   **/
  public final void setRvInGdLn(final IRvInvLn<T, G> pRvInGdLn) {
    this.rvInGdLn = pRvInGdLn;
  }

  /**
   * <p>Getter for rvInSrLn.</p>
   * @return IRvInvLn<T, S>
   **/
  public final IRvInvLn<T, S> getRvInSrLn() {
    return this.rvInSrLn;
  }

  /**
   * <p>Setter for rvInSrLn.</p>
   * @param pRvInSrLn reference
   **/
  public final void setRvInSrLn(final IRvInvLn<T, S> pRvInSrLn) {
    this.rvInSrLn = pRvInSrLn;
  }
}
