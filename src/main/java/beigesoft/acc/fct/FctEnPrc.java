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

import org.beigesoft.fct.IFctNm;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.acc.prc.EntrCr;
import org.beigesoft.acc.prc.EntrSrcCr;
import org.beigesoft.acc.prc.EntrSv;
import org.beigesoft.acc.prc.InEntrSv;
import org.beigesoft.acc.srv.ISrBlnc;

/**
 * <p>Accounting additional factory of entity processors.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctEnPrc<RS> implements IFctNm<IPrcEnt<?, ?>> {

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
          if (EntrCr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrCr(pRvs);
          } else if (EntrSrcCr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrSrcCr(pRvs);
          } else if (InEntrSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuInEntrSv(pRvs);
          } else if (EntrSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrSv(pRvs);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map InEntrSv.</p>
   * @param pRvs request scoped vars
   * @return InEntrSv
   * @throws Exception - an exception
   */
  private InEntrSv crPuInEntrSv(
    final Map<String, Object> pRvs) throws Exception {
    InEntrSv rz = new InEntrSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(InEntrSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), InEntrSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map EntrSv.</p>
   * @param pRvs request scoped vars
   * @return EntrSv
   * @throws Exception - an exception
   */
  private EntrSv<RS> crPuEntrSv(
    final Map<String, Object> pRvs) throws Exception {
    EntrSv<RS> rz = new EntrSv<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    ISrBlnc srBlnc = (ISrBlnc) this.fctBlc
      .laz(pRvs, ISrBlnc.class.getSimpleName());
    rz.setSrBlnc(srBlnc);
    this.procs.put(EntrSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), EntrSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map EntrSrcCr.</p>
   * @param pRvs request scoped vars
   * @return EntrSrcCr
   * @throws Exception - an exception
   */
  private EntrSrcCr crPuEntrSrcCr(
    final Map<String, Object> pRvs) throws Exception {
    EntrSrcCr rz = new EntrSrcCr();
    this.procs.put(EntrSrcCr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), EntrSrcCr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map EntrCr.</p>
   * @param pRvs request scoped vars
   * @return EntrCr
   * @throws Exception - an exception
   */
  private EntrCr crPuEntrCr(
    final Map<String, Object> pRvs) throws Exception {
    EntrCr rz = new EntrCr();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setI18n(this.fctBlc.lazI18n(pRvs));
    this.procs.put(EntrCr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), EntrCr.class
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
