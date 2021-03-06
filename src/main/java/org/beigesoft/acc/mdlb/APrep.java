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

package org.beigesoft.acc.mdlb;

import org.beigesoft.acc.mdl.EDocTy;
import org.beigesoft.acc.mdlp.Acnt;
import org.beigesoft.acc.mdlp.DbCr;

/**
 * <p>Model of prepayment.</p>
 *
 * @author Yury Demidenko
 */
public abstract class APrep extends ADoc {

  /**
   * <p>Invoice ID. Invoice sets it during its accounting,
   * and clears during reversing.</p>
   **/
  private Long invId;

  /**
   * <p>Debtor/creditor.</p>
   **/
  private DbCr dbcr;

  /**
   * <p>Account cash.</p>
   **/
  private Acnt acc;

  /**
   * <p>Sub-account cash name if exist.</p>
   **/
  private String saNm;

  /**
   * <p>Sub-account cash ID if exist.</p>
   **/
  private Long saId;

  /**
   * <p>Sub-account cash type if exist.</p>
   **/
  private Integer saTy;

  /**
   * <p>Getter of EDocTy.</p>
   * @return EDocTy
   **/
  @Override
  public final EDocTy getDocTy() {
    return EDocTy.ACC;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for invId.</p>
   * @return Long
   **/
  public final Long getInvId() {
    return this.invId;
  }

  /**
   * <p>Setter for invId.</p>
   * @param pInvId reference
   **/
  public final void setInvId(final Long pInvId) {
    this.invId = pInvId;
  }
  /**
   * <p>Getter for dbcr.</p>
   * @return DbCr
   **/
  public final DbCr getDbcr() {
    return this.dbcr;
  }

  /**
   * <p>Setter for dbcr.</p>
   * @param pDbcr reference
   **/
  public final void setDbcr(final DbCr pDbcr) {
    this.dbcr = pDbcr;
  }

  /**
   * <p>Getter for acc.</p>
   * @return Acnt
   **/
  public final Acnt getAcc() {
    return this.acc;
  }

  /**
   * <p>Setter for acc.</p>
   * @param pAcc reference
   **/
  public final void setAcc(final Acnt pAcc) {
    this.acc = pAcc;
  }

  /**
   * <p>Getter for saNm.</p>
   * @return String
   **/
  public final String getSaNm() {
    return this.saNm;
  }

  /**
   * <p>Setter for saNm.</p>
   * @param pSaNm reference
   **/
  public final void setSaNm(final String pSaNm) {
    this.saNm = pSaNm;
  }

  /**
   * <p>Getter for saId.</p>
   * @return Long
   **/
  public final Long getSaId() {
    return this.saId;
  }

  /**
   * <p>Setter for saId.</p>
   * @param pSaId reference
   **/
  public final void setSaId(final Long pSaId) {
    this.saId = pSaId;
  }

  /**
   * <p>Getter for saTy.</p>
   * @return Integer
   **/
  public final Integer getSaTy() {
    return this.saTy;
  }

  /**
   * <p>Setter for saTy.</p>
   * @param pSaTy reference
   **/
  public final void setSaTy(final Integer pSaTy) {
    this.saTy = pSaTy;
  }
}
