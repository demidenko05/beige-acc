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
import java.util.Map;

import org.beigesoft.acc.mdlb.IInv;
import org.beigesoft.acc.mdlb.IInvLn;

/**
 * <p>Abstraction of reverser for purchase/sales invoice line.</p>
 *
 * @param <T> invoice type
 * @param <L> invoice line type
 * @author Yury Demidenko
 */
public interface IRvInvLn<T extends IInv, L extends IInvLn<T, ?>> {

  /**
   * <p>Retrieves and checks lines for reversing,
   * e.g. for purchase goods lines it checks for withdrawals.</p>
   * @param pRvs Request scoped variables, not null
   * @param pVs Invoker scoped variables, not null
   * @param pEnt invoice, not null
   * @return checked lines
   * @throws Exception - an exception
   **/
  List<L> retChkLns(Map<String, Object> pRvs, Map<String, Object> pVs,
    T pEnt) throws Exception;

  /**
   * <p>Reverses lines.
   * it also inserts reversing and updates reversed
   * for good it also makes warehouse reversing
   * for sales good it also makes draw item reversing.
   * It removes line tax lines.</p>
   * @param pRvs Request scoped variables, not null
   * @param pVs Invoker scoped variables, not null
   * @param pEnt invoice, not null
   * @param pRvng reversing line, not null
   * @param pRved reversed line, not null
   * @throws Exception - an exception
   **/
  void revLns(Map<String, Object> pRvs, Map<String, Object> pVs, T pEnt,
    L pRvng, L pRved) throws Exception;
}
