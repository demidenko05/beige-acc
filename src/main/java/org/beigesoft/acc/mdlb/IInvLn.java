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

import java.math.BigDecimal;

import org.beigesoft.mdl.IOwned;
import org.beigesoft.acc.mdlp.Uom;
import org.beigesoft.acc.mdlp.TxCt;

/**
 * <p>Base model of invoice line.</p>
 *
 * @param <T> invoice type
 * @param <I> item type
 * @author Yury Demidenko
 */
public interface IInvLn<T extends IInv, I extends AItm<?, ?>>
  extends IOwned<T, Long> {

  /**
   * <p>Getter for itm.</p>
   * @return AItm<?, ?>
   **/
  I getItm();

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  void setItm(I pItm);

  /**
   * <p>Getter for txCt.</p>
   * @return TxCt
   **/
  TxCt getTxCt();

  /**
   * <p>Setter for txCt.</p>
   * @param pTxCt reference
   **/
  void setTxCt(TxCt pTxCt);

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
   * <p>Getter for tot.</p>
   * @return BigDecimal
   **/
  BigDecimal getTot();

  /**
   * <p>Setter for tot.</p>
   * @param pTot reference
   **/
  void setTot(BigDecimal pTot);

  /**
   * <p>Getter for toFc.</p>
   * @return BigDecimal
   **/
  BigDecimal getToFc();

  /**
   * <p>Setter for toFc.</p>
   * @param pToFc reference
   **/
  void setToFc(BigDecimal pToFc);

  /**
   * <p>Getter for rvId.</p>
   * @return Long
   **/
  Long getRvId();

  /**
   * <p>Setter for rvId.</p>
   * @param pRvId reference
   **/
  void setRvId(Long pRvId);

  /**
   * <p>Getter for uom.</p>
   * @return Uom
   **/
  Uom getUom();

  /**
   * <p>Setter for uom.</p>
   * @param pUom reference
   **/
  void setUom(Uom pUom);

  /**
   * <p>Getter for pri.</p>
   * @return BigDecimal
   **/
  BigDecimal getPri();

  /**
   * <p>Setter for pri.</p>
   * @param pPri reference
   **/
  void setPri(BigDecimal pPri);

  /**
   * <p>Getter for prFc.</p>
   * @return BigDecimal
   **/
  BigDecimal getPrFc();

  /**
   * <p>Setter for prFc.</p>
   * @param pPrFc reference
   **/
  void setPrFc(BigDecimal pPrFc);

  /**
   * <p>Getter for quan.</p>
   * @return BigDecimal
   **/
  BigDecimal getQuan();

  /**
   * <p>Setter for quan.</p>
   * @param pQuan reference
   **/
  void setQuan(BigDecimal pQuan);

  /**
   * <p>Getter for tdsc.</p>
   * @return String
   **/
  String getTdsc();

  /**
   * <p>Setter for tdsc.</p>
   * @param pTdsc reference
   **/
  void setTdsc(String pTdsc);

  /**
   * <p>Getter for dscr.</p>
   * @return String
   **/
  String getDscr();

  /**
   * <p>Setter for dscr.</p>
   * @param pDscr reference
   **/
  void setDscr(String pDscr);
}
