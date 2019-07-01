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

import org.beigesoft.fct.IFctPrc;
import org.beigesoft.fct.IFctAsm;
import org.beigesoft.prc.IPrc;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.acc.rep.PrBlShTr;
import org.beigesoft.acc.rep.PrcBln;
import org.beigesoft.acc.rep.PrLdgr;
import org.beigesoft.acc.rep.PrChrAc;
import org.beigesoft.acc.rep.ISrBlnSht;
import org.beigesoft.acc.prc.RvTxCt;
import org.beigesoft.acc.srv.ISrBlnc;

/**
 * <p>Additional factory of processors for accounting,
 * secure non-transactional requests.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FcPrNtAc<RS> implements IFctPrc {

  /**
   * <p>Main factory.</p>
   **/
  private IFctAsm<RS> fctApp;

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
  public final IPrc laz(final Map<String, Object> pRvs, //NOPMD
    final String pPrNm) throws Exception {
    IPrc rz = this.procs.get(pPrNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.procs.get(pPrNm);
        if (rz == null && PrBlShTr.class.getSimpleName().equals(pPrNm)) {
          rz = crPuPrBlShTr(pRvs);
        } else if (rz == null && PrChrAc.class.getSimpleName().equals(pPrNm)) {
          rz = crPuPrChrAc(pRvs);
        } else if (rz == null && RvTxCt.class.getSimpleName().equals(pPrNm)) {
          rz = crPuRvTxCt(pRvs);
        } else if (rz == null && PrLdgr.class.getSimpleName().equals(pPrNm)) {
          rz = crPuPrLdgr(pRvs);
        } else if (rz == null && PrcBln.class.getSimpleName().equals(pPrNm)) {
          rz = crPuPrcBln(pRvs);
        }
      }
    }
    return rz;
  }

  /**
   * <p>Creates and puts into MF PrChrAc.</p>
   * @param pRvs request scoped vars
   * @return PrChrAc
   * @throws Exception - an exception
   */
  private PrChrAc<RS> crPuPrChrAc(
    final Map<String, Object> pRvs) throws Exception {
    PrChrAc<RS> rz = new PrChrAc<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(this.fctApp.getFctBlc().lazOrm(pRvs));
    rz.setTrIsl(this.fctApp.getFctBlc().getFctDt().getReadTi());
    this.procs.put(PrChrAc.class.getSimpleName(), rz);
    this.fctApp.getFctBlc().lazLogStd(pRvs).info(pRvs, getClass(),
      PrChrAc.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF RvTxCt.</p>
   * @param pRvs request scoped vars
   * @return RvTxCt
   * @throws Exception - an exception
   */
  private RvTxCt<RS> crPuRvTxCt(
    final Map<String, Object> pRvs) throws Exception {
    RvTxCt<RS> rz = new RvTxCt<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setTrIsl(this.fctApp.getFctBlc().getFctDt().getReadTi());
    this.procs.put(RvTxCt.class.getSimpleName(), rz);
    this.fctApp.getFctBlc().lazLogStd(pRvs).info(pRvs, getClass(),
      RvTxCt.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF PrLdgr.</p>
   * @param pRvs request scoped vars
   * @return PrLdgr
   * @throws Exception - an exception
   */
  private PrLdgr<RS> crPuPrLdgr(
    final Map<String, Object> pRvs) throws Exception {
    PrLdgr<RS> rz = new PrLdgr<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    ISrBlnc srBlnc = (ISrBlnc) this.fctApp
      .laz(pRvs, ISrBlnc.class.getSimpleName());
    rz.setSrBlnc(srBlnc);
    rz.setSrvDt(this.fctApp.getFctBlc().lazSrvDt(pRvs));
    rz.setTrIsl(this.fctApp.getFctBlc().getFctDt().getReadTi());
    this.procs.put(PrLdgr.class.getSimpleName(), rz);
    this.fctApp.getFctBlc().lazLogStd(pRvs).info(pRvs, getClass(),
      PrLdgr.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF PrcBln.</p>
   * @param pRvs request scoped vars
   * @return PrcBln
   * @throws Exception - an exception
   */
  private PrcBln<RS> crPuPrcBln(
    final Map<String, Object> pRvs) throws Exception {
    PrcBln<RS> rz = new PrcBln<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    ISrBlnc srBlnc = (ISrBlnc) this.fctApp
      .laz(pRvs, ISrBlnc.class.getSimpleName());
    rz.setSrBlnc(srBlnc);
    rz.setSrvDt(this.fctApp.getFctBlc().lazSrvDt(pRvs));
    rz.setTrIsl(this.fctApp.getFctBlc().getFctDt().getReadTi());
    this.procs.put(PrcBln.class.getSimpleName(), rz);
    this.fctApp.getFctBlc().lazLogStd(pRvs).info(pRvs, getClass(),
      PrcBln.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF PrBlShTr.</p>
   * @param pRvs request scoped vars
   * @return PrBlShTr
   * @throws Exception - an exception
   */
  private PrBlShTr<RS> crPuPrBlShTr(
    final Map<String, Object> pRvs) throws Exception {
    PrBlShTr<RS> rz = new PrBlShTr<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    ISrBlnc srBlnc = (ISrBlnc) this.fctApp
      .laz(pRvs, ISrBlnc.class.getSimpleName());
    rz.setSrBlnc(srBlnc);
    ISrBlnSht srBlnSht = (ISrBlnSht) this.fctApp
      .laz(pRvs, ISrBlnSht.class.getSimpleName());
    rz.setSrBlnSht(srBlnSht);
    rz.setSrvDt(this.fctApp.getFctBlc().lazSrvDt(pRvs));
    rz.setTrIsl(this.fctApp.getFctBlc().getFctDt().getReadTi());
    this.procs.put(PrBlShTr.class.getSimpleName(), rz);
    this.fctApp.getFctBlc().lazLogStd(pRvs).info(pRvs, getClass(),
      PrBlShTr.class.getSimpleName() + " has been created");
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctApp.</p>
   * @return IFctAsm<RS>
   **/
  public final synchronized IFctAsm<RS> getFctApp() {
    return this.fctApp;
  }

  /**
   * <p>Setter for fctApp.</p>
   * @param pFctApp reference
   **/
  public final synchronized void setFctApp(final IFctAsm<RS> pFctApp) {
    this.fctApp = pFctApp;
  }
}
