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

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.beigesoft.acc.mdl.ISacnt;
import org.beigesoft.acc.mdl.TrBlLn;

/**
 * <p>Service that maintenances Blnc and BlncCh, retrieves trial balance,
 * also that maintenances subaccount dirting - SacCh, and changes
 * if need entries Entr.sacNm, Entr.sadNm, Blnc.SaNm.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrBlnc {

  /**
   * <p>Retrieves Trial Balance report.</p>
   * @param pRvs Request scoped variables
   * @param pDt date
   * @return Trial Balance Lines
   * @throws Exception - an exception
   **/
  List<TrBlLn> retTrBlnc(Map<String, Object> pRvs, Date pDt) throws Exception;

  /**
   * <p>Recalculates if need for all balances for all dates less
   * or equals pDtFor, this method is always invoked by report ledger.</p>
   * @param pRvs Request scoped variables
   * @param pDtFor date for
   * @return if has been recalculation
   * @throws Exception - an exception
   **/
  boolean recalcIfNd(Map<String, Object> pRvs, Date pDtFor) throws Exception;

  /**
   * <p>Handles new accounting entry to check
   * dirty of stored balances.</p>
   * @param pRvs Request scoped variables
   * @param pDtAt date at
   * @throws Exception - an exception
   **/
  void hndNewEntr(Map<String, Object> pRvs, Date pDtAt) throws Exception;

  /**
   * <p>Evaluates start of period nearest to pDtFor.
   * Tested in blc org.beigesoft.test.CalendarTest.</p>
   * @param pRvs Request scoped variables
   * @param pDtFor date for
   * @return Start of period nearest to pDtFor
   * @throws Exception - an exception
   **/
  Date evDtStPer(Map<String, Object> pRvs, Date pDtFor) throws Exception;

  /**
   * <p>Changes if need entries Entr.sacNm, Entr.sadNm, Blnc.SaNm.
   * It should be invoked together with recalcIfNd,
   * and service must be locked (synchronized).</p>
   * @param pRvs Request scoped variables
   * @return updated rows count
   * @throws Exception - an exception
   **/
  int chngSacsIfNd(Map<String, Object> pRvs) throws Exception;

  /**
   * <p>Handle subaccount has been changed.</p>
   * @param pRvs Request scoped variables
   * @param pSacnt subaccount
   * @throws Exception - an exception
   **/
  void hndSacntCh(Map<String, Object> pRvs, ISacnt pSacnt) throws Exception;
}
