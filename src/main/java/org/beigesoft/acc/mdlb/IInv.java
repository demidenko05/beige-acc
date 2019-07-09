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

import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.acc.mdlp.Curr;
import org.beigesoft.acc.mdlp.DbCr;

/**
 * <p>Base model of invoice.</p>
 *
 * @author Yury Demidenko
 */
public interface IInv extends IDoc {

  /**
   * <p>Getter for prep.</p>
   * @return PrepTo
   **/
  APrep getPrep();

  /**
   * <p>Getter for prepayment class.</p>
   * @return Prepayment class
   **/
  Class<? extends APrep> getPrepCls();

  /**
   * <p>Getter for payment class.</p>
   * @return Payment class
   **/
  Class<? extends APaym<?>> getPaymCls();

  /**
   * <p>Getter for dbcr.</p>
   * @return DbCr
   **/
  DbCr getDbcr();

  /**
   * <p>Setter for dbcr.</p>
   * @param pDbcr reference
   **/
  void setDbcr(DbCr pDbcr);

  /**
   * <p>Getter for cuFr.</p>
   * @return Curr
   **/
  Curr getCuFr();

  /**
   * <p>Setter for cuFr.</p>
   * @param pCuFr reference
   **/
  void setCuFr(Curr pCuFr);

  /**
   * <p>Getter for exRt.</p>
   * @return BigDecimal
   **/
  BigDecimal getExRt();

  /**
   * <p>Setter for exRt.</p>
   * @param pExRt reference
   **/
  void setExRt(BigDecimal pExRt);

  /**
   * <p>Getter for subt.</p>
   * @return BigDecimal
   **/
  BigDecimal getSubt();

  /**
   * <p>Setter for subt.</p>
   * @param pSubt reference
   **/
  void setSubt(BigDecimal pSubt);

  /**
   * <p>Getter for suFc.</p>
   * @return BigDecimal
   **/
  BigDecimal getSuFc();

  /**
   * <p>Setter for suFc.</p>
   * @param pSuFc reference
   **/
  void setSuFc(BigDecimal pSuFc);

  /**
   * <p>Getter for toTx.</p>
   * @return BigDecimal
   **/
  BigDecimal getToTx();

  /**
   * <p>Setter for toTx.</p>
   * @param pToTx reference
   **/
  void setToTx(BigDecimal pToTx);

  /**
   * <p>Getter for txFc.</p>
   * @return BigDecimal
   **/
  BigDecimal getTxFc();

  /**
   * <p>Setter for txFc.</p>
   * @param pTxFc reference
   **/
  void setTxFc(BigDecimal pTxFc);

  /**
   * <p>Getter for inTx.</p>
   * @return Boolean
   **/
  Boolean getInTx();

  /**
   * <p>Setter for inTx.</p>
   * @param pInTx reference
   **/
  void setInTx(Boolean pInTx);

  /**
   * <p>Getter for omTx.</p>
   * @return Boolean
   **/
  Boolean getOmTx();

  /**
   * <p>Setter for omTx.</p>
   * @param pOmTx reference
   **/
  void setOmTx(Boolean pOmTx);

  /**
   * <p>Getter for payb.</p>
   * @return Date
   **/
  Date getPayb();

  /**
   * <p>Setter for payb.</p>
   * @param pPayb reference
   **/
  void setPayb(Date pPayb);

  /**
   * <p>Getter for toPa.</p>
   * @return BigDecimal
   **/
  BigDecimal getToPa();

  /**
   * <p>Setter for toPa.</p>
   * @param pToPa reference
   **/
  void setToPa(BigDecimal pToPa);

  /**
   * <p>Getter for paFc.</p>
   * @return BigDecimal
   **/
  BigDecimal getPaFc();

  /**
   * <p>Setter for paFc.</p>
   * @param pPaFc reference
   **/
  void setPaFc(BigDecimal pPaFc);

  /**
   * <p>Getter for pdsc.</p>
   * @return String
   **/
  String getPdsc();

  /**
   * <p>Setter for pdsc.</p>
   * @param pPdsc reference
   **/
  void setPdsc(String pPdsc);
}
