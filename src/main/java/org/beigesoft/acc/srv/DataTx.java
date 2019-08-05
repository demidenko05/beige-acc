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

package org.beigesoft.acc.srv;

import java.util.List;

import org.beigesoft.acc.mdl.TaxEx;
import org.beigesoft.acc.mdlb.TxDtLn;

/**
 * <p>Bundle of retrieved from database tax data.</p>
 *
 * @author Yury Demidenko
 */
public class DataTx {

  /**
   * <p>Data storage for aggregate rate
   * and non-aggregate invoice basis taxes included.</p>
   **/
  private List<TxDtLn> txdLns;

  /**
   * <p>Taxes data storage for non-aggregate rate
   * except invoice basis with included taxes.
   * </p>
   **/
  private List<TaxEx> txs;

  /**
   * <p>TaxEx's totals/taxables data storage for non-aggregate rate
   * except invoice basis with included taxes.
   * </p>
   **/
  private List<Double> txTotTaxb;

  /**
   * <p>TaxEx's totals/taxables FC data storage for non-aggregate rate
   * except invoice basis with included taxes.
   * </p>
   **/
  private List<Double> txTotTaxbFc;

  /**
   * <p>TaxEx's percents for invoice basis data storage for non-aggregate rate
   * except invoice basis with included taxes.
   * </p>
   **/
  private List<Double> txPerc;

  /**
   * <p>Explanation.</p>
   **/
  @Override
  public final String toString() {
    return "List<TxDtLn> txdLns [tax cat.ID/subt/suFc/tot/toFc/toTx/txFc]: "
      + this.txdLns
+ "\nList<TaxEx> txs [Tax ID/Name/taxable/taxable FC/total tax/total tax FC]: "
        + this.txs + "\nList<Double> txTotTaxb: " + this.txTotTaxb
          + "\nList<Double> txTotTaxbFc: " + this.txTotTaxbFc
            + "\nList<Double> txPerc: " + this.txPerc;
  }
  //Simple getters and setters:

  /**
   * <p>Getter for txdLns.</p>
   * @return List<TxDtLn>
   **/
  public final List<TxDtLn> getTxdLns() {
    return this.txdLns;
  }

  /**
   * <p>Setter for txdLns.</p>
   * @param pTxdLns reference
   **/
  public final void setTxdLns(final List<TxDtLn> pTxdLns) {
    this.txdLns = pTxdLns;
  }

  /**
   * <p>Getter for txs.</p>
   * @return List<TaxEx>
   **/
  public final List<TaxEx> getTxs() {
    return this.txs;
  }

  /**
   * <p>Setter for txs.</p>
   * @param pTxs reference
   **/
  public final void setTxs(final List<TaxEx> pTxs) {
    this.txs = pTxs;
  }

  /**
   * <p>Getter for txTotTaxbFc.</p>
   * @return List<Double>
   **/
  public final List<Double> getTxTotTaxbFc() {
    return this.txTotTaxbFc;
  }

  /**
   * <p>Setter for txTotTaxbFc.</p>
   * @param pTxTotTaxbFc reference
   **/
  public final void setTxTotTaxbFc(final List<Double> pTxTotTaxbFc) {
    this.txTotTaxbFc = pTxTotTaxbFc;
  }

  /**
   * <p>Getter for txTotTaxb.</p>
   * @return List<Double>
   **/
  public final List<Double> getTxTotTaxb() {
    return this.txTotTaxb;
  }

  /**
   * <p>Setter for txTotTaxb.</p>
   * @param pTxTotTaxb reference
   **/
  public final void setTxTotTaxb(final List<Double> pTxTotTaxb) {
    this.txTotTaxb = pTxTotTaxb;
  }

  /**
   * <p>Getter for txPerc.</p>
   * @return List<Double>
   **/
  public final List<Double> getTxPerc() {
    return this.txPerc;
  }

  /**
   * <p>Setter for txPerc.</p>
   * @param pTxPerc reference
   **/
  public final void setTxPerc(final List<Double> pTxPerc) {
    this.txPerc = pTxPerc;
  }
}
