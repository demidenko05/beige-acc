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

import java.math.BigDecimal;

import org.beigesoft.mdl.AHasVr;
import org.beigesoft.acc.mdlp.Curr;

/**
 * <p>
 * Holds accepted foreign currency exRts.
 * </p>
 *
 * @author Yury Demidenko
 */
public class CurrRt extends AHasVr<Curr> {

  /**
   * <p>Curr, PK.</p>
   **/
  private Curr curr;

  /**
   * <p>ExRt, not null.</p>
   **/
  private BigDecimal exRt;

  /**
   * <p>Usually it's simple getter that return model ID.</p>
   * @return ID model ID
   **/
  @Override
  public final Curr getIid() {
    return this.curr;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pIid model ID
   **/
  @Override
  public final void setIid(final Curr pIid) {
    this.curr = pIid;
  }

  //Simple getters and setters:
  /**
   * <p>Setter for curr.</p>
   * @param pCurr reference
   **/
  public final void setCurr(final Curr pCurr) {
    this.curr = pCurr;
  }

  /**
   * <p>Getter for curr.</p>
   * @return Curr
   **/
  public final Curr getCurr() {
    return this.curr;
  }

  /**
   * <p>Getter for exRt.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getExRt() {
    return this.exRt;
  }

  /**
   * <p>Setter for exRt.</p>
   * @param pExRt reference
   **/
  public final void setExRt(final BigDecimal pExRt) {
    this.exRt = pExRt;
  }
}
