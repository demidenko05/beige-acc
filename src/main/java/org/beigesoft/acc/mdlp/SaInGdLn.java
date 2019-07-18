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
import java.util.Date;

import org.beigesoft.acc.mdlb.AInvLn;
import org.beigesoft.acc.mdlb.IMkDriEnr;

/**
 * <p>Model of sales invoice goods line.</p>
 *
 * @author Yury Demidenko
 */
public class SaInGdLn extends AInvLn<SalInv, Itm>
  implements IMkDriEnr<CogsEnr> {

  /**
   * <p>Invoice.</p>
   **/
  private SalInv ownr;

  /**
   * <p>Warehouse place optional.</p>
   **/
  private WrhPl whpo;

  /**
   * <p>Item.</p>
   **/
  private Itm itm;

  /**
   * <p>Item basis tax lines.</p>
   **/
  private List<SaInGdTxLn> txLns;

  /**
   * <p>Constant of code type 2001.</p>
   * @return 2001
   **/
  @Override
  public final Integer cnsTy() {
    return 2001;
  }

  /**
   * <p>Getter for ownr.</p>
   * @return SalInv
   **/
  @Override
  public final SalInv getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final SalInv pOwnr) {
    this.ownr = pOwnr;
  }

  /**
   * <p>Getter for itm.</p>
   * @return Itm
   **/
  @Override
  public final Itm getItm() {
    return this.itm;
  }

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  @Override
  public final void setItm(final Itm pItm) {
    this.itm = pItm;
  }

  /**
   * <p>Getter for document date (own or owner's).</p>
   * @return Date
   **/
  @Override
  public final Date getDocDt() {
    return this.ownr.getDat();
  }

  /**
   * <p>Getter for owner ID if exist.</p>
   * @return ID
   **/
  @Override
  public final Long getOwnrId() {
    return this.ownr.getIid();
  }

  /**
   * <p>Getter for owner type 6.</p>
   * @return type code 6
   **/
  @Override
  public final Integer getOwnrTy() {
    return 6;
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
   * <p>Getter for txLns.</p>
   * @return List<SaInGdTxLn>
   **/
  public final List<SaInGdTxLn> getTxLns() {
    return this.txLns;
  }

  /**
   * <p>Setter for txLns.</p>
   * @param pTxLns reference
   **/
  public final void setTxLns(final List<SaInGdTxLn> pTxLns) {
    this.txLns = pTxLns;
  }

  /**
   * <p>Getter for whpo.</p>
   * @return WrhPl
   **/
  public final WrhPl getWhpo() {
    return this.whpo;
  }

  /**
   * <p>Setter for whpo.</p>
   * @param pWhpo reference
   **/
  public final void setWhpo(final WrhPl pWhpo) {
    this.whpo = pWhpo;
  }
}
