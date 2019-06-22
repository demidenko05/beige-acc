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

import java.io.IOException;

import org.beigesoft.fct.IFctRq;
import org.beigesoft.acc.mdlb.AInTxLn;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.mdlb.AInvLn;

/**
 * <p>Abstraction of tax method code/data for purchase/sales invoice.
 * It contains data dedicated to concrete invoice type.</p>
 *
 * @param <T> invoice type
 * @param <TL> invoice tax line type
 * @author Yury Demidenko
 */
public interface IInvTxMeth<T extends AInv, TL extends AInTxLn<T>> {

  /**
   * <p>Getter for invoice SQL tables names: {[GOOD LINE], [SERVICE LINE],
   * [TAX LINE], [GOOD TAX LINE], [SERVICE TAX LINE]} or
   * {[GOOD LINE], [TAX LINE], [GOOD TAX LINE]}.
   * If SQL query no needs it, then set it NULL.</p>
   * @return String[]
   **/
  String[] getTblNmsTot();

  /**
   * <p>Getter for good line class.</p>
   * @return Class<InvoiceLine<T>>
   **/
  Class<? extends AInvLn<T, ?>> getGoodLnCl();

  /**
   * <p>Getter for service line class.</p>
   * @return Class<? extends AInvLn<T>>
   **/
  Class<? extends AInvLn<T, ?>> getServiceLnCl();

  /**
   * <p>Getter for invTxLnCl.</p>
   * @return Class<TL>
   **/
  Class<TL> getInvTxLnCl();

  /**
   * <p>Getter for invTxLnCl.</p>
   * @return Class<T>
   **/
  Class<T> getInvCl();

  /**
   * <p>Getter for fctInvTxLn.</p>
   * @return IFctRq<TL>
   **/
  IFctRq<TL> getFctInvTxLn();

  /**
   * <p>Getter for isTxByUser, if line tax must be set by user.</p>
   * @return Boolean
   **/
  Boolean getIsTxByUser();

  /**
   * <p>Get where start clause for adjusting invoice goods
   * lines for invoice basis method.</p>
   * @return String
   **/
  String getStWhereAdjGdLnInvBas();

  /**
   * <p>Get where start clause for adjusting invoice service
   * lines for invoice basis method.</p>
   * @return String
   **/
  String getStWhereAdjSrLnInvBas();

  /**
   * <p>Lazy get for quTxInvBas.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  String lazyGetQuTxInvBas() throws IOException;

  /**
   * <p>Lazy get for quTxInvBasAggr.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  String lazyGetQuTxInvBasAggr() throws IOException;

  /**
   * <p>Lazy get for quTxItBasAggr.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  String lazyGetQuTxItBasAggr() throws IOException;

  /**
   * <p>Lazy get for quTxItBas.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  String lazyGetQuTxItBas() throws IOException;

  /**
   * <p>Lazy get for quTotals.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  String lazyGetQuTotals() throws IOException;

  /**
   * <p>Lazy get for quTxInvAdj.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  String lazyGetQuTxInvAdj() throws IOException;
}
