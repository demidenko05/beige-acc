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

import org.beigesoft.hnd.IHnTrRlBk;
import org.beigesoft.ws.mdlp.SeSel;

/**
 * <p>S.E.Seller finder service. It usually cashes S.E.Sellers.</p>
 *
 * @author Yury Demidenko
 */
public interface IFiSeSel extends IHnTrRlBk {

  /**
   * <p>Finds by name.</p>
   * @param pRvs additional request scoped parameters
   * @param pName seller's
   * @return S.E. Seller or null
   * @throws Exception - an exception
   **/
  SeSel find(Map<String, Object> pRvs, String pName) throws Exception;

  /**
   * <p>Handle S.E. seller changed.</p>
   * @param pRvs additional param
   * @param pName seller's, null means "refresh all"
   * @throws Exception - an exception
   **/
  void hndSelChg(Map<String, Object> pRvs, String pName) throws Exception;
}
