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
import java.util.HashSet;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.fct.IFctPrc;
import org.beigesoft.prc.IPrc;
import org.beigesoft.prc.PrcEntPg;
import org.beigesoft.srv.EntPg;
import org.beigesoft.eis.IniEisFct;
import org.beigesoft.ws.srv.FlSeSel;

/**
 * <p>Factory of processors for S.E. entity request like list.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FcPrFeSe<RS> implements IFctPrc {

  /**
   * <p>Main factory.</p>
   **/
  private FctBlc<RS> fctBlc;

  //requested data:
  /**
   * <p>Processors map.</p>
   **/
  private final Map<String, IPrc> procs = new HashMap<String, IPrc>();

  /**
   * <p>Get processor in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pPrNm - filler name
   * @return requested processor
   * @throws Exception - an exception
   */
  public final IPrc laz(final Map<String, Object> pRvs,
    final String pPrNm) throws Exception {
    IPrc rz = this.procs.get(pPrNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.procs.get(pPrNm);
        if (rz == null) {
          if (FctWs.PRSEENTPG.equals(pPrNm)) {
            rz = crPuPrSeEnPg(pRvs);
          } else {
            throw new ExcCode(ExcCode.WRCN, "There is no IProc: " + pPrNm);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcEntPg.</p>
   * @param pRvs request scoped vars
   * @return PrcEntPg
   * @throws Exception - an exception
   */
  private PrcEntPg crPuPrSeEnPg(
    final Map<String, Object> pRvs) throws Exception {
    PrcEntPg rz = new PrcEntPg();
    EntPg<RS> entPg = new EntPg<RS>();
    entPg.setLog(this.fctBlc.lazLogStd(pRvs));
    entPg.setHlpEntPg(this.fctBlc.lazHlpEntPg(pRvs));
    entPg.setEntMp(new HashMap<String, Class<? extends IHasId<?>>>());
    FlSeSel flSeSel = new FlSeSel();
    entPg.setMkrFlt(flSeSel);
    flSeSel.setEnts(new HashSet<Class<? extends IHasId<?>>>());
    for (Class<? extends IHasId<?>> cls : this.fctBlc
      .lazStgUvd(pRvs).lazClss()) {
      if (this.fctBlc.getFctDt().isEntAlwd(cls, IniEisFct.ID_SESEL)) {
        entPg.getEntMp().put(cls.getSimpleName(), cls);
        flSeSel.getEnts().add(cls);
      }
    }
    rz.setEntPg(entPg);
    this.procs.put(FctWs.PRSEENTPG, rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), FctWs.PRSEENTPG
      + " has been created.");
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
