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
import org.beigesoft.acc.mdlb.APaym;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.srv.IRvInvLn;
import org.beigesoft.acc.srv.SrPaymSv;

/**
 * <p>Service that saves payment into DB.</p>
 *
 * @param <T> payment type
 * @param <I> invoice type
 * @author Yury Demidenko
 */
public class PaymSv<T extends APaym<I>, I extends AInv>
  implements IPrcEnt<T, Long> {

  /**
   * <p>Save service.</p>
   **/
  private SrPaymSv srPaymSv;

  /**
   * <p>Just hold payment/prepayment class.</p>
   **/
  private IRvInvLn<I, ?> rvLn;

  /**
   * <p>Process that saves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final T process(final Map<String, Object> pRvs,
    final T pEnt, final IReqDt pRqDt) throws Exception {
    return this.srPaymSv.save(pRvs, pEnt, pRqDt, this.rvLn);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srPaymSv.</p>
   * @return SrPaymSv
   **/
  public final SrPaymSv getSrPaymSv() {
    return this.srPaymSv;
  }

  /**
   * <p>Setter for srPaymSv.</p>
   * @param pSrPaymSv reference
   **/
  public final void setSrPaymSv(final SrPaymSv pSrPaymSv) {
    this.srPaymSv = pSrPaymSv;
  }

  /**
   * <p>Getter for rvLn.</p>
   * @return IRvInvLn
   **/
  public final IRvInvLn<I, ?> getRvLn() {
    return this.rvLn;
  }

  /**
   * <p>Setter for rvLn.</p>
   * @param pRvLn reference
   **/
  public final void setRvLn(final IRvInvLn<I, ?> pRvLn) {
    this.rvLn = pRvLn;
  }
}
