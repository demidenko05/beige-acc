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

import org.beigesoft.acc.mdlb.ISacnt;
import org.beigesoft.acc.mdlb.AItm;

/**
 * <p>Model of service (to sell or to purchase).</p>
 *
 * @author Yury Demidenko
 */
public class Srv extends AItm<Srv, SrTxDl> implements ISacnt {

  /**
   * <p>Category, not null.</p>
   **/
  private SrvCt cat;

  /**
   * <p>Tax destination lines.</p>
   **/
  private List<SrTxDl> tdls;

  /**
   * <p>OOP friendly Constant of code type 1007.</p>
   * @return 1007
   **/
  @Override
  public final Integer cnsTy() {
    return 1007;
  }

  /**
   * <p>Getter for tdls.</p>
   * @return List<SrTxDl>
   **/
  @Override
  public final List<SrTxDl> getTdls() {
    return this.tdls;
  }

  /**
   * <p>Setter for tdls.</p>
   * @param pTdls reference
   **/
  @Override
  public final void setTdls(final List<SrTxDl> pTdls) {
    this.tdls = pTdls;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for cat.</p>
   * @return SrvCt
   **/
  public final SrvCt getCat() {
    return this.cat;
  }

  /**
   * <p>Setter for cat.</p>
   * @param pCat reference
   **/
  public final void setCat(final SrvCt pCat) {
    this.cat = pCat;
  }
}
