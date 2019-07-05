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

package org.beigesoft.acc.mdlp;

import java.util.List;

import org.beigesoft.acc.mdl.EDocTy;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.mdlb.APrep;
import org.beigesoft.acc.mdlb.APaym;

/**
 * <p>Model of purchase invoice.</p>
 *
 * @author Yury Demidenko
 */
public class PurInv extends AInv {

  /**
   * <p>Prepayments.</p>
   **/
  private PrepTo prep;

  /**
   * <p>Tax lines.</p>
   **/
  private List<PuInTxLn> txLns;

  /**
   * <p>Goods lines.</p>
   **/
  private List<PuInGdLn> gdLns;

  /**
   * <p>Service lines.</p>
   **/
  private List<PuInSrLn> srLns;

  /**
   * <p>Constant of code type.</p>
   * @return 4
   **/
  @Override
  public final Integer cnsTy() {
    return 4;
  }

  /**
   * <p>Getter of EDocTy.</p>
   * @return EDocTy
   **/
  @Override
  public final EDocTy getDocTy() {
    return EDocTy.ITSRLN;
  }

  /**
   * <p>Getter for prep.</p>
   * @return PrepTo
   **/
  @Override
  public final PrepTo getPrep() {
    return this.prep;
  }

  /**
   * <p>Getter for prepayment class.</p>
   * @return Prepayment class
   **/
  @Override
  public final Class<? extends APrep> getPrepCls() {
    return PrepTo.class;
  }

  /**
   * <p>Getter for payment class.</p>
   * @return payment class
   **/
  @Override
  public final Class<? extends APaym<?>> getPaymCls() {
    return PaymTo.class;
  }

  //Simple getters and setters:
  /**
   * <p>Setter for prep.</p>
   * @param pPrep reference
   **/
  public final void setPrep(final PrepTo pPrep) {
    this.prep = pPrep;
  }

  /**
   * <p>Getter for txLns.</p>
   * @return List<PuInTxLn>
   **/
  public final List<PuInTxLn> getTxLns() {
    return this.txLns;
  }

  /**
   * <p>Setter for txLns.</p>
   * @param pTxLns reference
   **/
  public final void setTxLns(final List<PuInTxLn> pTxLns) {
    this.txLns = pTxLns;
  }

  /**
   * <p>Getter for gdLns.</p>
   * @return List<PuInGdLn>
   **/
  public final List<PuInGdLn> getGdLns() {
    return this.gdLns;
  }

  /**
   * <p>Setter for gdLns.</p>
   * @param pGdLns reference
   **/
  public final void setGdLns(final List<PuInGdLn> pGdLns) {
    this.gdLns = pGdLns;
  }

  /**
   * <p>Getter for srLns.</p>
   * @return List<PuInSrLn>
   **/
  public final List<PuInSrLn> getSrLns() {
    return this.srLns;
  }

  /**
   * <p>Setter for srLns.</p>
   * @param pSrLns reference
   **/
  public final void setSrLns(final List<PuInSrLn> pSrLns) {
    this.srLns = pSrLns;
  }
}
