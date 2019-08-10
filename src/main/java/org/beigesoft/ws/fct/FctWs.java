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

import org.beigesoft.fct.IFcCsvDrt;
import org.beigesoft.fct.IFctAux;
import org.beigesoft.fct.FctBlc;

/**
 * <p>Auxiliary factory for web-store additional services.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctWs<RS> implements IFctAux<RS> {

  /**
   * <p>Creates requested bean and put into given main factory.
   * The main factory is already synchronized when invokes this.</p>
   * @param pRvs request scoped vars
   * @param pBnNm - bean name
   * @param pFctApp main factory
   * @return Object - requested bean or NULL
   * @throws Exception - an exception
   */
  @Override
  public final Object crePut(final Map<String, Object> pRvs,
    final String pBnNm, final FctBlc<RS> pFctApp) throws Exception {
    Object rz = null;
    if (IFcCsvDrt.class.getSimpleName().equals(pBnNm)) {
      rz = crPuFcCsvDrt(pRvs, pFctApp);
    }
    return rz;
  }

  /**
   * <p>Releases state when main factory is releasing.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @throws Exception - an exception
   */
  @Override
  public final void release(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    //nothing
  }

  /**
   * <p>Creates and puts into MF FcCsvDrt.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return FcCsvDrt
   * @throws Exception - an exception
   */
  private FcCsvDrt crPuFcCsvDrt(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    FcCsvDrt<RS> rz = new FcCsvDrt<RS>();
    rz.setFctBlc(pFctApp);
    pFctApp.put(pRvs, IFcCsvDrt.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      IFcCsvDrt.class.getSimpleName() + " has been created.");
    return rz;
  }
}
