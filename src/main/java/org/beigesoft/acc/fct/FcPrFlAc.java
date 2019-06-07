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

import org.beigesoft.fct.IFctPrcFl;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.prc.IPrcFl;
import org.beigesoft.acc.rep.PrcBlnSht;
import org.beigesoft.acc.rep.ISrBlnSht;
import org.beigesoft.acc.rep.IBlnPdf;

/**
 * <p>Accounting additional factory of file reporter processors.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FcPrFlAc<RS> implements IFctPrcFl {

  /**
   * <p>Main factory.</p>
   **/
  private FctBlc<RS> fctBlc;

  //requested data:
  /**
   * <p>Processors map.</p>
   **/
  private final Map<String, IPrcFl> procs = new HashMap<String, IPrcFl>();

  /**
   * <p>Get processor in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pPrNm - filler name
   * @return requested processor or NULL
   * @throws Exception - an exception
   */
  public final IPrcFl laz(final Map<String, Object> pRvs, //NOPMD
    final String pPrNm) throws Exception {
    IPrcFl rz = this.procs.get(pPrNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.procs.get(pPrNm);
        if (rz == null && PrcBlnSht.class.getSimpleName().equals(pPrNm)) {
          rz = crPuPrcBlnSht(pRvs);
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map PrcBlnSht.</p>
   * @param pRvs request scoped vars
   * @return PrcBlnSht
   * @throws Exception - an exception
   */
  private PrcBlnSht crPuPrcBlnSht(
    final Map<String, Object> pRvs) throws Exception {
    PrcBlnSht rz = new PrcBlnSht();
    ISrBlnSht srBlnSht = (ISrBlnSht) this.fctBlc
      .laz(pRvs, ISrBlnSht.class.getSimpleName());
    rz.setSrBlnSht(srBlnSht);
    IBlnPdf blnPdf = (IBlnPdf) this.fctBlc
      .laz(pRvs, IBlnPdf.class.getSimpleName());
    rz.setBlnPdf(blnPdf);
    rz.setSrvDt(this.fctBlc.lazSrvDt(pRvs));
    this.procs.put(PrcBlnSht.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), PrcBlnSht.class
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
