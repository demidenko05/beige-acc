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

import java.util.Map;
import java.util.LinkedHashMap;
import java.math.BigDecimal;

/**
 * <p>Ledger previous line.</p>
 *
 * @author Yury Demidenko
 */
public class LdgPrv {

  /**
   * <p>Lines of subaccounts or single line
   * with empty name if they not exist.</p>
   **/
  private Map<String, LdgPrvLn> lnsMp =
    new LinkedHashMap<String, LdgPrvLn>();

  /**
   * <p>Debit account total.</p>
   **/
  private BigDecimal debitAcc = BigDecimal.ZERO;

  /**
   * <p>Credit account total.</p>
   **/
  private BigDecimal creditAcc = BigDecimal.ZERO;

  /**
   * <p>Balance account total.</p>
   **/
  private BigDecimal balanceAcc = BigDecimal.ZERO;

  //Simple getters and setters:
  /**
   * <p>Getter for lnsMp.</p>
   * @return Map<String, LdgPrvLn>
   **/
  public final Map<String, LdgPrvLn> getLnsMp() {
    return this.lnsMp;
  }

  /**
   * <p>Getter for debitAcc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getDebitAcc() {
    return this.debitAcc;
  }

  /**
   * <p>Setter for debitAcc.</p>
   * @param pDebitAcc reference
   **/
  public final void setDebitAcc(final BigDecimal pDebitAcc) {
    this.debitAcc = pDebitAcc;
  }

  /**
   * <p>Getter for creditAcc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getCreditAcc() {
    return this.creditAcc;
  }

  /**
   * <p>Setter for creditAcc.</p>
   * @param pCreditAcc reference
   **/
  public final void setCreditAcc(final BigDecimal pCreditAcc) {
    this.creditAcc = pCreditAcc;
  }

  /**
   * <p>Getter for balanceAcc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getBalanceAcc() {
    return this.balanceAcc;
  }

  /**
   * <p>Setter for balanceAcc.</p>
   * @param pBalanceAcc reference
   **/
  public final void setBalanceAcc(final BigDecimal pBalanceAcc) {
    this.balanceAcc = pBalanceAcc;
  }
}
