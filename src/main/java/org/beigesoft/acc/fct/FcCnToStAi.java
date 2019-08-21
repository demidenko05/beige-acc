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

package org.beigesoft.acc.fct;

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.fct.IFctCnToSt;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.prp.ISetng;
import org.beigesoft.cnv.ICnToSt;
import org.beigesoft.cnv.CnvHsIdStr;

/**
 * <p>Additional factory to FctNmCnToSt of fields converters to string.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FcCnToStAi<RS> implements IFctCnToSt {

  /**
   * <p>DB-Copy converter owned entity to string name.</p>
   **/
  public static final String CNHSIDSTACIM = "cnHsIdStAcIm";

  /**
   * <p>Main factory.</p>
   **/
  private FctBlc<RS> fctBlc;

  //requested data:
  /**
   * <p>Converters map.</p>
   **/
  private final Map<String, ICnToSt<?>> convrts
    = new HashMap<String, ICnToSt<?>>();

  /**
   * <p>Get converter in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pCnNm - converter name
   * @return requested converter
   * @throws Exception - an exception
   */
  @Override
  public final ICnToSt<?> laz(final Map<String, Object> pRvs, //NOPMD
    final String pCnNm) throws Exception {
    ICnToSt<?> rz = this.convrts.get(pCnNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.convrts.get(pCnNm);
        if (rz == null && CNHSIDSTACIM.equals(pCnNm)) {
          rz = crPuCnvHsIdStrAcIm(pRvs);
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map ACIMP CnvHsIdStr.</p>
   * @param pRvs request scoped vars
   * @return ACIMP CnvHsIdStr
   * @throws Exception - an exception
   */
  protected final CnvHsIdStr<IHasId<?>> crPuCnvHsIdStrAcIm(
    final Map<String, Object> pRvs) throws Exception {
    CnvHsIdStr<IHasId<?>> rz = new CnvHsIdStr<IHasId<?>>();
    rz.setHldGets(this.fctBlc.lazHldGets(pRvs));
    rz.setSetng((ISetng) this.fctBlc.laz(pRvs, FctAcc.STGACIMP));
    this.convrts.put(CNHSIDSTACIM, rz);
    this.fctBlc.lazLogStd(pRvs)
      .info(pRvs, getClass(), CNHSIDSTACIM + " has been created.");
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctBlc.</p>
   * @return FctBlc<RS>
   **/
  public final synchronized FctBlc<RS> getFctBlc() {
    return this.fctBlc;
  }

  /**
   * <p>Setter for fctBlc.</p>
   * @param pFctBlc reference
   **/
  public final synchronized void setFctBlc(final FctBlc<RS> pFctBlc) {
    this.fctBlc = pFctBlc;
  }
}
