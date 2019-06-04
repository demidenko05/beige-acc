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

import org.beigesoft.fct.IFctPrcEnt;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.acc.prc.SacntSv;
import org.beigesoft.acc.prc.SacntCr;
import org.beigesoft.acc.prc.EntrCr;
import org.beigesoft.acc.prc.EntrChd;
import org.beigesoft.acc.prc.EntrCpr;
import org.beigesoft.acc.prc.EntrRt;
import org.beigesoft.acc.prc.EntrSrcCr;
import org.beigesoft.acc.prc.IsacntSv;
import org.beigesoft.acc.prc.IsacntDl;
import org.beigesoft.acc.prc.AcntSv;
import org.beigesoft.acc.prc.EntrSv;
import org.beigesoft.acc.prc.InEntrSv;
import org.beigesoft.acc.prc.InEntrDl;
import org.beigesoft.acc.prc.AcStgRt;
import org.beigesoft.acc.prc.AcStgSv;
import org.beigesoft.acc.prc.InEntrRt;
import org.beigesoft.acc.srv.ISrAcStg;
import org.beigesoft.acc.srv.ISrBlnc;

/**
 * <p>Accounting additional factory of entity processors.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctEnPrc<RS> implements IFctPrcEnt {

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
          } else if (EntrRt.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrRt(pRvs);
          } else if (EntrChd.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrChd(pRvs);
          } else if (EntrCpr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrCpr(pRvs);
          } else if (SacntSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuSacntSv(pRvs);
          } else if (SacntCr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuSacntCr(pRvs);
          } else if (EntrSrcCr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrSrcCr(pRvs);
          } else if (InEntrSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuInEntrSv(pRvs);
          } else if (AcStgRt.class.getSimpleName().equals(pPrNm)) {
            rz = crPuAcStgRt(pRvs);
          } else if (AcStgSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuAcStgSv(pRvs);
          } else if (InEntrRt.class.getSimpleName().equals(pPrNm)) {
            rz = crPuInEntrRt(pRvs);
          } else if (InEntrDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuInEntrDl(pRvs);
          } else if (IsacntDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuIsacntDl(pRvs);
          } else if (IsacntSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuIsacntSv(pRvs);
          } else if (AcntSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuAcntSv(pRvs);
          } else if (EntrSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuEntrSv(pRvs);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map AcStgSv.</p>
   * @param pRvs request scoped vars
   * @return AcStgSv
   * @throws Exception - an exception
   */
  private AcStgSv crPuAcStgSv(
    final Map<String, Object> pRvs) throws Exception {
    AcStgSv rz = new AcStgSv();
    ISrAcStg srAcStg = (ISrAcStg) this.fctBlc
      .laz(pRvs, ISrAcStg.class.getSimpleName());
    rz.setSrAcStg(srAcStg);
    this.procs.put(AcStgSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), AcStgSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map AcStgRt.</p>
   * @param pRvs request scoped vars
   * @return AcStgRt
   * @throws Exception - an exception
   */
  private AcStgRt crPuAcStgRt(
    final Map<String, Object> pRvs) throws Exception {
    AcStgRt rz = new AcStgRt();
    ISrAcStg srAcStg = (ISrAcStg) this.fctBlc
      .laz(pRvs, ISrAcStg.class.getSimpleName());
    rz.setSrAcStg(srAcStg);
    this.procs.put(AcStgRt.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), AcStgRt.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map InEntrRt.</p>
   * @param pRvs request scoped vars
   * @return InEntrRt
   * @throws Exception - an exception
   */
  private InEntrRt crPuInEntrRt(
    final Map<String, Object> pRvs) throws Exception {
    InEntrRt rz = new InEntrRt();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(InEntrRt.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), InEntrRt.class
      .getSimpleName() + " has been created.");
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
   * <p>Create and put into the Map InEntrDl.</p>
   * @param pRvs request scoped vars
   * @return InEntrDl
   * @throws Exception - an exception
   */
  private InEntrDl<RS> crPuInEntrDl(
    final Map<String, Object> pRvs) throws Exception {
    InEntrDl<RS> rz = new InEntrDl<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    this.procs.put(InEntrDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), InEntrDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map IsacntDl.</p>
   * @param pRvs request scoped vars
   * @return IsacntDl
   * @throws Exception - an exception
   */
  private IsacntDl<RS> crPuIsacntDl(
    final Map<String, Object> pRvs) throws Exception {
    IsacntDl<RS> rz = new IsacntDl<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    this.procs.put(IsacntDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), IsacntDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map IsacntSv.</p>
   * @param pRvs request scoped vars
   * @return IsacntSv
   * @throws Exception - an exception
   */
  private IsacntSv crPuIsacntSv(
    final Map<String, Object> pRvs) throws Exception {
    IsacntSv rz = new IsacntSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    ISrBlnc srBlnc = (ISrBlnc) this.fctBlc
      .laz(pRvs, ISrBlnc.class.getSimpleName());
    rz.setSrBlnc(srBlnc);
    this.procs.put(IsacntSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), IsacntSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map AcntSv.</p>
   * @param pRvs request scoped vars
   * @return AcntSv
   * @throws Exception - an exception
   */
  private AcntSv<RS> crPuAcntSv(
    final Map<String, Object> pRvs) throws Exception {
    AcntSv<RS> rz = new AcntSv<RS>();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    this.procs.put(AcntSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), AcntSv.class
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
   * <p>Create and put into the Map SacntSv.</p>
   * @param pRvs request scoped vars
   * @return SacntSv
   * @throws Exception - an exception
   */
  private SacntSv crPuSacntSv(
    final Map<String, Object> pRvs) throws Exception {
    SacntSv rz = new SacntSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(SacntSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), SacntSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map SacntCr.</p>
   * @param pRvs request scoped vars
   * @return SacntCr
   * @throws Exception - an exception
   */
  private SacntCr crPuSacntCr(
    final Map<String, Object> pRvs) throws Exception {
    SacntCr rz = new SacntCr();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(SacntCr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), SacntCr.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map EntrRt.</p>
   * @param pRvs request scoped vars
   * @return EntrRt
   * @throws Exception - an exception
   */
  private EntrRt crPuEntrRt(
    final Map<String, Object> pRvs) throws Exception {
    EntrRt rz = new EntrRt();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(EntrRt.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), EntrRt.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map EntrChd.</p>
   * @param pRvs request scoped vars
   * @return EntrChd
   * @throws Exception - an exception
   */
  private EntrChd crPuEntrChd(
    final Map<String, Object> pRvs) throws Exception {
    EntrChd rz = new EntrChd();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(EntrChd.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), EntrChd.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map EntrCpr.</p>
   * @param pRvs request scoped vars
   * @return EntrCpr
   * @throws Exception - an exception
   */
  private EntrCpr crPuEntrCpr(
    final Map<String, Object> pRvs) throws Exception {
    EntrCpr rz = new EntrCpr();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    this.procs.put(EntrCpr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), EntrCpr.class
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
