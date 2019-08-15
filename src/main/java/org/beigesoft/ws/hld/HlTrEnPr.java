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

package org.beigesoft.ws.hld;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.ws.mdlb.AItmSpf;
import org.beigesoft.ws.mdlp.TrdStg;
import org.beigesoft.ws.prc.ItmSpSv;
import org.beigesoft.ws.prc.TrStgSv;
import org.beigesoft.ws.prc.TrStgRt;

/**
 * <p>Additional holder of names of admin/trade entities processors.</p>
 *
 * @author Yury Demidenko
 */
public class HlTrEnPr implements IHlNmClSt {

  /**
   * <p>Get processor name for given class and action name.</p>
   * @param <T> entity type
   * @param pCls a Class
   * @param pAct action name
   * @return processor name
   * @throws Exception an Exception
   **/
  @Override
  public final <T extends IHasId<?>> String get(final Class<T> pCls,
    final String pAct) throws Exception {
    if (AItmSpf.class.isAssignableFrom(pCls) && "entSv".equals(pAct)) {
      return ItmSpSv.class.getSimpleName();
    } else if (TrdStg.class == pCls) {
      if ("entEd".equals(pAct) || "entPr".equals(pAct)) {
        return TrStgRt.class.getSimpleName();
      } else if ("entSv".equals(pAct)) {
        return TrStgSv.class.getSimpleName();
      }
      return NULL; //forbidden
    }
    //other - default:
    return null;
  }
}
