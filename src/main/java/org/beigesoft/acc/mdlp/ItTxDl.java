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

import org.beigesoft.mdlp.IOrId;
import org.beigesoft.acc.mdlb.ATxDsLn;

/**
 * <p>Model of item tax destination line.</p>
 *
 * @author Yury Demidenko
 */
public class ItTxDl extends ATxDsLn<Itm> implements IOrId {

  /**
   * <p>Item tax category.</p>
   **/
  private Itm ownr;

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
   * <p>Getter for ownr.</p>
   * @return Itm
   **/
  @Override
  public final Itm getOwnr() {
    return this.ownr;
  }

  /**
   * <p>Setter for ownr.</p>
   * @param pOwnr reference
   **/
  @Override
  public final void setOwnr(final Itm pOwnr) {
    this.ownr = pOwnr;
  }
}
