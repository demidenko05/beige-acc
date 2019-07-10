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
import org.beigesoft.acc.mdlb.IRet;
import org.beigesoft.acc.mdlb.IRetLn;
import org.beigesoft.acc.mdlb.AInTxLn;
import org.beigesoft.acc.mdlb.ALnTxLn;
import org.beigesoft.acc.srv.SrRtLnSv;
import org.beigesoft.acc.srv.ISrInItLn;
import org.beigesoft.acc.srv.UtInLnTxTo;

/**
 * <p>Processor that saves return line into DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @param <T> return type
 * @param <L> return line type
 * @param <TL> tax line type
 * @param <LTL> item tax line type
 * @author Yury Demidenko
 */
public class RetLnSv<RS, T extends IRet<?>, L extends IRetLn<T, ?, ?>,
  TL extends AInTxLn<T>, LTL extends ALnTxLn<T, L>>
    implements IPrcEnt<L, Long> {

  /**
   * <p>Saving service.</p>
   **/
  private SrRtLnSv srRtLnSv;

  /**
   * <p>Service that makes taxes and totals for line and invoice.</p>
   **/
  private UtInLnTxTo<RS, T, L, TL, LTL> utInTxTo;

  /**
   * <p>Item line oriented service.</p>
   **/
  private ISrInItLn<T, L> srInItLn;

  /**
   * <p>Process that saves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final L process(final Map<String, Object> pRvs, final L pEnt,
    final IReqDt pRqDt) throws Exception {
    return this.srRtLnSv.save(pRvs, pEnt, pRqDt, this.utInTxTo, this.srInItLn);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srRtLnSv.</p>
   * @return SrRtLnSv
   **/
  public final SrRtLnSv getSrRtLnSv() {
    return this.srRtLnSv;
  }

  /**
   * <p>Setter for srRtLnSv.</p>
   * @param pSrRtLnSv reference
   **/
  public final void setSrRtLnSv(final SrRtLnSv pSrRtLnSv) {
    this.srRtLnSv = pSrRtLnSv;
  }

  /**
   * <p>Getter for utInTxTo.</p>
   * @return UtInLnTxTo<RS, T, L, TL, LTL>
   **/
  public final UtInLnTxTo<RS, T, L, TL, LTL> getUtInTxTo() {
    return this.utInTxTo;
  }

  /**
   * <p>Setter for utInTxTo.</p>
   * @param pUtInTxTo reference
   **/
  public final void setUtInTxTo(final UtInLnTxTo<RS, T, L, TL, LTL> pUtInTxTo) {
    this.utInTxTo = pUtInTxTo;
  }

  /**
   * <p>Getter for srInItLn.</p>
   * @return ISrInItLn<T, L>
   **/
  public final ISrInItLn<T, L> getSrInItLn() {
    return this.srInItLn;
  }

  /**
   * <p>Setter for srInItLn.</p>
   * @param pSrInItLn reference
   **/
  public final void setSrInItLn(final ISrInItLn<T, L> pSrInItLn) {
    this.srInItLn = pSrInItLn;
  }
}
