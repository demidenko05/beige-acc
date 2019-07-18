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
import org.beigesoft.acc.mdlb.IDcDri;

/**
 * <p>Model of sales invoice.</p>
 *
 * @author Yury Demidenko
 */
public class SalInv extends AInv implements IDcDri<CogsEnr> {

  /**
   * <p>Prepayments.</p>
   **/
  private PrepFr prep;

  /**
   * <p>Tax lines.</p>
   **/
  private List<SaInTxLn> txLns;

  /**
   * <p>Goods lines.</p>
   **/
  private List<SaInGdLn> gdLns;

  /**
   * <p>Services lines.</p>
   **/
  private List<SaInSrLn> srLns;

  /**
   * <p>Constant of code type 6.</p>
   * @return 6
   **/
  @Override
  public final Integer cnsTy() {
    return 6;
  }

  /**
   * <p>Getter of EDocTy.</p>
   * @return EDocTy
   **/
  @Override
  public final EDocTy getDocTy() {
    return EDocTy.DRAWLN;
  }

  /**
   * <p>Getter for prep.</p>
   * @return PrepFr
   **/
  @Override
  public final PrepFr getPrep() {
    return this.prep;
  }

  /**
   * <p>Getter for draw item entry class.</p>
   * @return draw item entry class
   **/
  @Override
  public final Class<CogsEnr> getEnrCls() {
    return CogsEnr.class;
  }

  //Simple getters and setters:
  /**
   * <p>Setter for prep.</p>
   * @param pPrep reference
   **/
  public final void setPrep(final PrepFr pPrep) {
    this.prep = pPrep;
  }

  /**
   * <p>Getter for txLns.</p>
   * @return List<SaInTxLn>
   **/
  public final List<SaInTxLn> getTxLns() {
    return this.txLns;
  }

  /**
   * <p>Setter for txLns.</p>
   * @param pTxLns reference
   **/
  public final void setTxLns(final List<SaInTxLn> pTxLns) {
    this.txLns = pTxLns;
  }

  /**
   * <p>Getter for gdLns.</p>
   * @return List<SaInGdLn>
   **/
  public final List<SaInGdLn> getGdLns() {
    return this.gdLns;
  }

  /**
   * <p>Setter for gdLns.</p>
   * @param pGdLns reference
   **/
  public final void setGdLns(final List<SaInGdLn> pGdLns) {
    this.gdLns = pGdLns;
  }

  /**
   * <p>Getter for srLns.</p>
   * @return List<SaInSrLn>
   **/
  public final List<SaInSrLn> getSrLns() {
    return this.srLns;
  }

  /**
   * <p>Setter for srLns.</p>
   * @param pSrLns reference
   **/
  public final void setSrLns(final List<SaInSrLn> pSrLns) {
    this.srLns = pSrLns;
  }
}
