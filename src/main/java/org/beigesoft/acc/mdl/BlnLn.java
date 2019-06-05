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
 * <p>Balance sheet line.</p>
 *
 * @author Yury Demidenko
 */
public class BlnLn {

  /**
   * <p>Account number.</p>
   **/
  private String accNumber;

  /**
   * <p>Account name.</p>
   **/
  private String accName;

  /**
   * <p>Debit subacc.</p>
   **/
  private BigDecimal debit;

  /**
   * <p>Credit subacc.</p>
   **/
  private BigDecimal credit;

  /**
   * <p>Account ID.</p>
   **/
  private String accId;

  /**
   * <p>Account type, 0-asset, 1-liabilities, 2-owner's equity.</p>
   **/
  private Integer accType;

  //Simple getters and setters:
  /**
   * <p>Geter for accNumber.</p>
   * @return String
   **/
  public final String getAccNumber() {
    return this.accNumber;
  }

  /**
   * <p>Setter for accNumber.</p>
   * @param pAccNumber reference
   **/
  public final void setAccNumber(final String pAccNumber) {
    this.accNumber = pAccNumber;
  }

  /**
   * <p>Geter for accName.</p>
   * @return String
   **/
  public final String getAccName() {
    return this.accName;
  }

  /**
   * <p>Setter for accName.</p>
   * @param pAccName reference
   **/
  public final void setAccName(final String pAccName) {
    this.accName = pAccName;
  }

  /**
   * <p>Geter for debit.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getDebit() {
    return this.debit;
  }

  /**
   * <p>Setter for debit.</p>
   * @param pDebit reference
   **/
  public final void setDebit(final BigDecimal pDebit) {
    this.debit = pDebit;
  }

  /**
   * <p>Geter for credit.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getCredit() {
    return this.credit;
  }

  /**
   * <p>Setter for credit.</p>
   * @param pCredit reference
   **/
  public final void setCredit(final BigDecimal pCredit) {
    this.credit = pCredit;
  }

  /**
   * <p>Getter for accId.</p>
   * @return String
   **/
  public final String getAccId() {
    return this.accId;
  }

  /**
   * <p>Setter for accId.</p>
   * @param pAccId reference
   **/
  public final void setAccId(final String pAccId) {
    this.accId = pAccId;
  }

  /**
   * <p>Getter for accType.</p>
   * @return Integer
   **/
  public final Integer getAccType() {
    return this.accType;
  }

  /**
   * <p>Setter for accType.</p>
   * @param pAccType reference
   **/
  public final void setAccType(final Integer pAccType) {
    this.accType = pAccType;
  }
}
