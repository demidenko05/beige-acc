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

package org.beigesoft.ws.fct;

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.fct.IFcCsvDrt;
import org.beigesoft.srv.ICsvDtRet;
import org.beigesoft.ws.srv.GdPriLstRet;

/**
 * <p>CSV data retrievers factory.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FcCsvDrt<RS> implements IFcCsvDrt {

  /**
   * <p>Main factory.</p>
   **/
  private FctBlc<RS> fctBlc;

  //requested data:
  /**
   * <p>Retrievers map.</p>
   **/
  private final Map<String, ICsvDtRet> retrs = new HashMap<String, ICsvDtRet>();

  /**
   * <p>Gets retriever in lazy mode by given name.</p>
   * @param pRvs request scoped vars
   * @param pNm - retriever name
   * @return requested retriever
   * @throws Exception - an exception
   */
  @Override
  public final ICsvDtRet laz(final Map<String, Object> pRvs,
    final String pNm) throws Exception {
    ICsvDtRet rz = this.retrs.get(pNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.retrs.get(pNm);
        if (rz == null) {
          if (GdPriLstRet.class.getSimpleName().equals(pNm)) {
            GdPriLstRet rt = new GdPriLstRet();
            rt.setOrm(this.fctBlc.lazOrm(pRvs));
            rt.setI18n(this.fctBlc.lazI18n(pRvs));
            rz = rt;
            retrs.put(GdPriLstRet.class.getSimpleName(), rz);
          } else {
              throw new ExcCode(ExcCode.WRCN, "There is no ICsvDtRet: " + pNm);
          }
        }
      }
    }
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctBlc.</p>
   * @return FctBlc<RS>
   **/
  public final FctBlc<RS> getFctBlc() {
    return this.fctBlc;
  }

  /**
   * <p>Setter for fctBlc.</p>
   * @param pFctBlc reference
   **/
  public final void setFctBlc(final FctBlc<RS> pFctBlc) {
    this.fctBlc = pFctBlc;
  }
}
