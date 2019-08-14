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

import org.beigesoft.fct.IFctPrcEnt;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.ws.prc.TrStgRt;
import org.beigesoft.ws.prc.TrStgSv;
import org.beigesoft.ws.srv.ISrTrStg;

/**
 * <p>Trade additional factory of entity processors.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FcEnPrTr<RS> implements IFctPrcEnt {

  /**
   * <p>Main factory.</p>
   **/
  private FctBlc<RS> fctBlc;

  //requested data:
  /**
   * <p>Processors map.</p>
   **/
  private final Map<String, IPrcEnt<?, ?>> procs =
    new HashMap<String, IPrcEnt<?, ?>>();

  /**
   * <p>Get processor in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pPrNm - filler name
   * @return requested processor or NULL
   * @throws Exception - an exception
   */
  public final IPrcEnt<?, ?> laz(final Map<String, Object> pRvs,
    final String pPrNm) throws Exception {
    IPrcEnt<?, ?> rz = this.procs.get(pPrNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.procs.get(pPrNm);
        if (rz == null) {
          if (TrStgRt.class.getSimpleName().equals(pPrNm)) {
            rz = crPuTrStgRt(pRvs);
          } else if (TrStgSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuTrStgSv(pRvs);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map TrStgSv.</p>
   * @param pRvs request scoped vars
   * @return TrStgSv
   * @throws Exception - an exception
   */
  private TrStgSv crPuTrStgSv(
    final Map<String, Object> pRvs) throws Exception {
    TrStgSv rz = new TrStgSv();
    ISrTrStg srTrStg = (ISrTrStg) this.fctBlc
      .laz(pRvs, ISrTrStg.class.getSimpleName());
    rz.setSrTrStg(srTrStg);
    this.procs.put(TrStgSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), TrStgSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map TrStgRt.</p>
   * @param pRvs request scoped vars
   * @return TrStgRt
   * @throws Exception - an exception
   */
  private TrStgRt crPuTrStgRt(
    final Map<String, Object> pRvs) throws Exception {
    TrStgRt rz = new TrStgRt();
    ISrTrStg srTrStg = (ISrTrStg) this.fctBlc
      .laz(pRvs, ISrTrStg.class.getSimpleName());
    rz.setSrTrStg(srTrStg);
    this.procs.put(TrStgRt.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), TrStgRt.class
      .getSimpleName() + " has been created.");
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
