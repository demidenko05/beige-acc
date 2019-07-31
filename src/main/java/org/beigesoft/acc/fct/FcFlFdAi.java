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

import org.beigesoft.fct.FctBlc;
import org.beigesoft.fct.IFcFlFdSt;
import org.beigesoft.hld.HldNmFilFdSt;
import org.beigesoft.prp.ISetng;
import org.beigesoft.cnv.IFilFldStr;
import org.beigesoft.cnv.FilFldHsIdStr;
import org.beigesoft.cnv.FilFldSmpStr;

/**
 * <p>Additional factory to FctNmFilFdSt of fields fillers from string
 *  for accounting import XML.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FcFlFdAi<RS> implements IFcFlFdSt {

  /**
   * <p>DB-Copy filler owned entity from string name.</p>
   **/
  public static final String FILHSIDSTDACIM = "flHsIdStAcIm";

  /**
   * <p>DB-Copy filler simple from string name.</p>
   **/
  public static final String FILSMPSTDACIM = "flSmpStAcIm";

  /**
   * <p>Main factory.</p>
   **/
  private FctBlc<RS> fctBlc;

  //requested data:
  /**
   * <p>Fillers map.</p>
   **/
  private final Map<String, IFilFldStr> fillers
    = new HashMap<String, IFilFldStr>();

  /**
   * <p>Get filler in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pFiNm - filler name
   * @return requested filler
   * @throws Exception - an exception
   */
  public final IFilFldStr laz(final Map<String, Object> pRvs,
    final String pFiNm) throws Exception {
    IFilFldStr rz = this.fillers.get(pFiNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.fillers.get(pFiNm);
        if (rz == null) {
          if (FILHSIDSTDACIM.equals(pFiNm)) {
            rz = crPuFilFldHsIdStrAcIm(pRvs);
          } else if (FILSMPSTDACIM.equals(pFiNm)) {
            rz = crPuFilFldSmpStrAcIm(pRvs);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map ACIMP FilFldHsIdStr.</p>
   * @param pRvs request scoped vars
   * @return ACIMP FilFldHsIdStr
   * @throws Exception - an exception
   */
  protected final FilFldHsIdStr crPuFilFldHsIdStrAcIm(
    final Map<String, Object> pRvs) throws Exception {
    FilFldHsIdStr rz = new FilFldHsIdStr();
    rz.setHldSets(this.fctBlc.lazHldSets(pRvs));
    rz.setHldFilFdNms((HldNmFilFdSt) this.fctBlc
      .laz(pRvs, FctAcc.HLFILFDNMACIM));
    rz.setSetng((ISetng) this.fctBlc.laz(pRvs, FctAcc.STGACIMP));
    rz.setHldFdCls(this.fctBlc.lazHldFldCls(pRvs));
    rz.setFctFilFld(this.fctBlc.lazFctNmFilFd(pRvs));
    this.fillers.put(FILHSIDSTDACIM, rz);
    this.fctBlc.lazLogStd(pRvs)
      .info(null, getClass(), FILHSIDSTDACIM + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map ACIMP FilFldSmpStr.</p>
   * @param pRvs request scoped vars
   * @return ACIMP FilFldSmpStr
   * @throws Exception - an exception
   */
  protected final FilFldSmpStr crPuFilFldSmpStrAcIm(
    final Map<String, Object> pRvs) throws Exception {
    FilFldSmpStr rz = new FilFldSmpStr();
    rz.setHldSets(this.fctBlc.lazHldSets(pRvs));
    rz.setHldNmFdCn(this.fctBlc.lazHldNmCnFrStXml(pRvs));
    rz.setFctCnvFld(this.fctBlc.lazFctNmCnFrSt(pRvs));
    this.fillers.put(FILSMPSTDACIM, rz);
    this.fctBlc.lazLogStd(pRvs)
      .info(null, getClass(), FILSMPSTDACIM + " has been created.");
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
