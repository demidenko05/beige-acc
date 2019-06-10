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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;

/**
 * <p>Ledger detail.</p>
 *
 * @author Yury Demidenko
 */
public class LdgDe {

  /**
   * <p>Lines.</p>
   **/
  private List<LdgDeLn> lns = new ArrayList<LdgDeLn>();

  /**
   * <p>Subaccount debit total.</p>
   **/
  private Map<String, BigDecimal> saDbTo =
    new HashMap<String, BigDecimal>();

  /**
   * <p>Subaccount credit total.</p>
   **/
  private Map<String, BigDecimal> saCrTo =
    new HashMap<String, BigDecimal>();

  /**
   * <p>Subaccount debit total.</p>
   **/
  private Map<String, BigDecimal> saBlnTo =
    new HashMap<String, BigDecimal>();

  /**
   * <p>Debit account total.</p>
   **/
  private BigDecimal debitAcc = BigDecimal.ZERO;

  /**
   * <p>Credit account total.</p>
   **/
  private BigDecimal creditAcc = BigDecimal.ZERO;

  /**
   * <p>Balance account total from zero.</p>
   **/
  private BigDecimal balanceAcc = BigDecimal.ZERO;

  //Simple getters and setters:
  /**
   * <p>Getter for lns.</p>
   * @return List<LdgDeLn>
   **/
  public final List<LdgDeLn> getLns() {
    return this.lns;
  }

  /**
   * <p>Getter for saDbTo.</p>
   * @return Map<String, BigDecimal>
   **/
  public final Map<String, BigDecimal> getSaDbTo() {
    return this.saDbTo;
  }

  /**
   * <p>Getter for saCrTo.</p>
   * @return Map<String, BigDecimal>
   **/
  public final Map<String, BigDecimal> getSaCrTo() {
    return this.saCrTo;
  }

  /**
   * <p>Getter for saBlnTo.</p>
   * @return Map<String, BigDecimal>
   **/
  public final Map<String, BigDecimal> getSaBlnTo() {
    return this.saBlnTo;
  }

  /**
   * <p>Getter for debitAcc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getDebitAcc() {
    return this.debitAcc;
  }

  /**
   * <p>Getter for creditAcc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getCreditAcc() {
    return this.creditAcc;
  }

  /**
   * <p>Getter for balanceAcc.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getBalanceAcc() {
    return this.balanceAcc;
  }
}
