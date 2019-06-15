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
import java.math.BigDecimal;

import org.beigesoft.mdlp.AOrIdNm;
import org.beigesoft.acc.mdlb.ISacnt;
import org.beigesoft.acc.mdlb.IHsTxCt;

/**
 * <p>Model of material(tangible) inventory item.</p>
 *
 * @author Yury Demidenko
 */
public class Itm extends AOrIdNm implements ISacnt, IHsTxCt {

  /**
   * <p>Category, not null.</p>
   **/
  private ItmCt cat;

  /**
   * <p>Tax category, nullable.</p>
   **/
  private TxCt txCt;

  /**
   * <p>Known cost, 0 means not used.</p>
   **/
  private BigDecimal knCs = BigDecimal.ZERO;

  /**
   * <p>Tax destination lines.</p>
   **/
  private List<ItTxDl> tdls;

  /**
   * <p>OOP friendly Constant of code type.</p>
   * @return 1005
   **/
  @Override
  public final Integer cnsTy() {
    return 1005;
  }

  /**
   * <p>Getter for txCt.</p>
   * @return TxCt
   **/
  @Override
  public final TxCt getTxCt() {
    return this.txCt;
  }

  /**
   * <p>Setter for txCt.</p>
   * @param pTxCt reference
   **/
  @Override
  public final void setTxCt(final TxCt pTxCt) {
    this.txCt = pTxCt;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for cat.</p>
   * @return ItmCt
   **/
  public final ItmCt getCat() {
    return this.cat;
  }

  /**
   * <p>Setter for cat.</p>
   * @param pCat reference
   **/
  public final void setCat(final ItmCt pCat) {
    this.cat = pCat;
  }

  /**
   * <p>Getter for knCs.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getKnCs() {
    return this.knCs;
  }

  /**
   * <p>Setter for knCs.</p>
   * @param pKnCs reference
   **/
  public final void setKnCs(final BigDecimal pKnCs) {
    this.knCs = pKnCs;
  }

  /**
   * <p>Getter for tdls.</p>
   * @return List<ItTxDl>
   **/
  public final List<ItTxDl> getTdls() {
    return this.tdls;
  }

  /**
   * <p>Setter for tdls.</p>
   * @param pTdls reference
   **/
  public final void setTdls(final List<ItTxDl> pTdls) {
    this.tdls = pTdls;
  }
}
