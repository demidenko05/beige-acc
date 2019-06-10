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

import java.math.BigDecimal;

/**
 * <p>Ledger previous line.</p>
 *
 * @author Yury Demidenko
 */
public class LdgPrvLn {

  /**
   * <p>Debit subacc.</p>
   **/
  private BigDecimal debt = BigDecimal.ZERO;

  /**
   * <p>Credit subacc.</p>
   **/
  private BigDecimal cred = BigDecimal.ZERO;

  /**
   * <p>Balance subacc.</p>
   **/
  private BigDecimal blnc = BigDecimal.ZERO;

  //Simple getters and setters:

  /**
   * <p>Getter for debt.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getDebt() {
    return this.debt;
  }

  /**
   * <p>Setter for debt.</p>
   * @param pDebt reference
   **/
  public final void setDebt(final BigDecimal pDebt) {
    this.debt = pDebt;
  }

  /**
   * <p>Getter for cred.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getCred() {
    return this.cred;
  }

  /**
   * <p>Setter for cred.</p>
   * @param pCred reference
   **/
  public final void setCred(final BigDecimal pCred) {
    this.cred = pCred;
  }

  /**
   * <p>Getter for blnc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getBlnc() {
    return this.blnc;
  }

  /**
   * <p>Setter for blnc.</p>
   * @param pBlnc reference
   **/
  public final void setBlnc(final BigDecimal pBlnc) {
    this.blnc = pBlnc;
  }
}
