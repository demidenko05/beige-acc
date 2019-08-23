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

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.ws.mdlp.Buyer;

/**
 * <p>Buyer's service.</p>
 *
 * @author Yury Demidenko
 */
public interface IBuySr {

  /**
   * <p>Get authorized buyer.</p>
   * @param pRqVs request scoped vars
   * @param pRqDt Request Data
   * @return authorized buyer or null
   * @throws Exception - an exception
   **/
  Buyer getAuthBuyr(Map<String, Object> pRqVs,
    IReqDt pRqDt) throws Exception;

  /**
   * <p>Get authorized or not buyer by cookie.</p>
   * @param pRqVs request scoped vars
   * @param pRqDt Request Data
   * @return buyer or null
   * @throws Exception - an exception
   **/
  Buyer getBuyr(Map<String, Object> pRqVs,
    IReqDt pRqDt) throws Exception;

  /**
   * <p>Creates buyer.</p>
   * @param pRqVs request scoped vars
   * @param pRqDt Request Data
   * @return created buyer will be unsaved into DB!
   * @throws Exception - an exception
   **/
  Buyer createBuyr(Map<String, Object> pRqVs,
    IReqDt pRqDt) throws Exception;
}
