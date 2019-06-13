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

package org.beigesoft.acc.mdl;

/**
 * <p>Model of account to print in chart.</p>
 *
 * @author Yury Demidenko
 */
 public class ChrtAcc {

  /**
   * <p>ID.</p>
   **/
  private String iid;

  /**
   * <p>Number.</p>
   **/
  private String nmbr;

  /**
   * <p>Name.</p>
   **/
  private String nme;

  /**
   * <p>Account Normal Balance Type - DEBIT, CREDIT.</p>
   **/
  private ENrBlTy blTy;

  /**
   * <p>EAccTy.ASSET/LIABILITY/OWN_EQUITY/
   * INC_REVENUE/INC_EXPENSE.</p>
   **/
  private EAccTy typ;

  /**
   * <p>Sub-account, e.g. "Mini-market A" for Account Payable.</p>
   **/
  private String subacc;

  /**
   * <p>Sub-account ID (Sacnt.iid) to manage IDs.</p>
   **/
  private Long sacntId;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

  //Simple getters and setters:
  /**
   * <p>Getter for iid.</p>
   * @return String
   **/
  public final String getIid() {
    return this.iid;
  }

  /**
   * <p>Setter for iid.</p>
   * @param pIid reference
   **/
  public final void setIid(final String pIid) {
    this.iid = pIid;
  }

  /**
   * <p>Getter for nmbr.</p>
   * @return String
   **/
  public final String getNmbr() {
    return this.nmbr;
  }

  /**
   * <p>Setter for nmbr.</p>
   * @param pNmbr reference
   **/
  public final void setNmbr(final String pNmbr) {
    this.nmbr = pNmbr;
  }

  /**
   * <p>Getter for nme.</p>
   * @return String
   **/
  public final String getNme() {
    return this.nme;
  }

  /**
   * <p>Setter for nme.</p>
   * @param pNme reference
   **/
  public final void setNme(final String pNme) {
    this.nme = pNme;
  }

  /**
   * <p>Getter for blTy.</p>
   * @return ENrBlTy
   **/
  public final ENrBlTy getBlTy() {
    return this.blTy;
  }

  /**
   * <p>Setter for blTy.</p>
   * @param pBlTy reference
   **/
  public final void setBlTy(final ENrBlTy pBlTy) {
    this.blTy = pBlTy;
  }

  /**
   * <p>Getter for typ.</p>
   * @return EAccTy
   **/
  public final EAccTy getTyp() {
    return this.typ;
  }

  /**
   * <p>Setter for typ.</p>
   * @param pTyp reference
   **/
  public final void setTyp(final EAccTy pTyp) {
    this.typ = pTyp;
  }

  /**
   * <p>Getter for subacc.</p>
   * @return String
   **/
  public final String getSubacc() {
    return this.subacc;
  }

  /**
   * <p>Setter for subacc.</p>
   * @param pSubacc reference
   **/
  public final void setSubacc(final String pSubacc) {
    this.subacc = pSubacc;
  }

  /**
   * <p>Getter for sacntId.</p>
   * @return String
   **/
  public final Long getSacntId() {
    return this.sacntId;
  }

  /**
   * <p>Setter for sacntId.</p>
   * @param pSacntId reference
   **/
  public final void setSacntId(final Long pSacntId) {
    this.sacntId = pSacntId;
  }

  /**
   * <p>Getter for dscr.</p>
   * @return String
   **/
  public final String getDscr() {
    return this.dscr;
  }

  /**
   * <p>Setter for dscr.</p>
   * @param pDscr reference
   **/
  public final void setDscr(final String pDscr) {
    this.dscr = pDscr;
  }
}
