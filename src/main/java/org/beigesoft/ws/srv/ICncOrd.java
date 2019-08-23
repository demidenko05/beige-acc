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

package org.beigesoft.ws.srv;

import java.util.Map;

import org.beigesoft.ws.mdl.EOrdStat;
import org.beigesoft.ws.mdlp.Buyer;
import org.beigesoft.ws.mdlp.CuOr;
import org.beigesoft.ws.mdlp.CuOrSe;

/**
 * <p>It cancels all given buyer's orders.
 * E.g. buyer has not paid online after accepting (booking) orders.
 * It changes item's availability and orders status to given NEW or CANCELED.
 * </p>
 *
 * @author Yury Demidenko
 */
public interface ICncOrd {

  /**
   * <p>It cancels all given buyer's orders.
   * For example buyer had not paid online after accepting (booking) orders.
   * It changes item's availability and orders status to given NEW or CANCELED.
   * </p>
   * @param pRvs additional request scoped parameters
   * @param pBuyr buyer
   * @param pPurId purchase ID
   * @param pStFr usually BOOKED
   * @param pStTo usually NEW
   * @throws Exception - an exception
   **/
  void cancel(Map<String, Object> pRvs, Buyer pBuyr, Long pPurId,
    EOrdStat pStFr, EOrdStat pStTo) throws Exception;

  /**
   * <p>It cancels given buyer's order.</p>
   * @param pRvs additional request scoped parameters
   * @param pCuOr order
   * @param pStat NEW or CANCELED
   * @throws Exception - an exception
   **/
  void cancel(Map<String, Object> pRvs, CuOr pCuOr,
    EOrdStat pStat) throws Exception;

  /**
   * <p>It cancels given buyer's S.E.order.</p>
   * @param pRvs additional request scoped parameters
   * @param pCuOr order
   * @param pStat NEW or CANCELED
   * @throws Exception - an exception
   **/
  void cancel(Map<String, Object> pRvs, CuOrSe pCuOr,
    EOrdStat pStat) throws Exception;
}
