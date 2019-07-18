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

import org.beigesoft.mdlp.IOrId;
import org.beigesoft.acc.mdlb.ISacnt;
import org.beigesoft.acc.mdlb.AItm;

/**
 * <p>Model of material(tangible) inventory item.</p>
 *
 * @author Yury Demidenko
 */
public class Itm extends AItm<Itm, ItTxDl> implements ISacnt, IOrId {

  /**
   * <p>Category, not null.</p>
   **/
  private ItmCt cat;

  /**
   * <p>Known cost, 0 means not used.</p>
   **/
  private BigDecimal knCs = BigDecimal.ZERO;

  /**
   * <p>Tax destination lines.</p>
   **/
  private List<ItTxDl> tdls;

  /**
   * <p>Implicit(there is no database constraints for it)
   * ID database where Entity was born.
   * For replication purpose. Not NULL.</p>
   **/
  private Integer dbOr;

  /**
   * <p>Implicit(there is no database constraints for it)
   * ID of this Entity from database where it was born.
   * For replication purpose. NULL if it was born in current database.</p>
   **/
  private Long idOr;

  /**
   * <p>Geter for dbOr.</p>
   * @return Integer
   **/
  @Override
  public final Integer getDbOr() {
    return this.dbOr;
  }

  /**
   * <p>Setter for dbOr.</p>
   * @param pDbOr reference
   **/
  @Override
  public final void setDbOr(final Integer pDbOr) {
    this.dbOr = pDbOr;
  }

  /**
   * <p>Geter for idOr.</p>
   * @return Long
   **/
  @Override
  public final Long getIdOr() {
    return this.idOr;
  }

  /**
   * <p>Setter for idOr.</p>
   * @param pIdOr reference
   **/
  @Override
  public final void setIdOr(final Long pIdOr) {
    this.idOr = pIdOr;
  }

  /**
   * <p>OOP friendly Constant of code type 1005.</p>
   * @return 1005
   **/
  @Override
  public final Integer cnsTy() {
    return 1005;
  }

  /**
   * <p>Getter for tdls.</p>
   * @return List<ItTxDl>
   **/
  @Override
  public final List<ItTxDl> getTdls() {
    return this.tdls;
  }

  /**
   * <p>Setter for tdls.</p>
   * @param pTdls reference
   **/
  @Override
  public final void setTdls(final List<ItTxDl> pTdls) {
    this.tdls = pTdls;
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
}
