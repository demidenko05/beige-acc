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
import org.beigesoft.fct.FctEnPrc;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.prc.PrcEnfSv;
import org.beigesoft.prc.PrcEntRt;
import org.beigesoft.ws.mdlb.AItmSpf;
import org.beigesoft.ws.prc.AdStgRt;
import org.beigesoft.ws.prc.AdStgSv;
import org.beigesoft.ws.prc.TrStgRt;
import org.beigesoft.ws.prc.TrStgSv;
import org.beigesoft.ws.prc.HsSeSelSv;
import org.beigesoft.ws.prc.HsSeSelDl;
import org.beigesoft.ws.prc.HsSeSelRt;
import org.beigesoft.ws.prc.SeSelSv;
import org.beigesoft.ws.prc.SeSelDl;
import org.beigesoft.ws.prc.ItmSpSv;
import org.beigesoft.ws.prc.ItmSpDl;
import org.beigesoft.ws.prc.CuOrSv;
import org.beigesoft.ws.prc.CuOrSeSv;
import org.beigesoft.ws.srv.ISrTrStg;
import org.beigesoft.ws.srv.IFiSeSel;
import org.beigesoft.ws.srv.ISrAdStg;
import org.beigesoft.ws.srv.ICncOrd;

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
          } else if (AdStgRt.class.getSimpleName().equals(pPrNm)) {
            rz = crPuAdStgRt(pRvs);
          } else if (HsSeSelRt.class.getSimpleName().equals(pPrNm)) {
            rz = crPuHsSeSelRt(pRvs);
          } else if (HsSeSelDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuHsSeSelDl(pRvs);
          } else if (CuOrSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuCuOrSv(pRvs);
          } else if (CuOrSeSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuCuOrSeSv(pRvs);
          } else if (HsSeSelSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuHsSeSelSv(pRvs);
          } else if (SeSelDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuSeSelDl(pRvs);
          } else if (SeSelSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuSeSelSv(pRvs);
          } else if (ItmSpDl.class.getSimpleName().equals(pPrNm)) {
            rz = crPuItmSpDl(pRvs);
          } else if (ItmSpSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuItmSpSv(pRvs);
          } else if (AdStgSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuAdStgSv(pRvs);
          } else if (TrStgSv.class.getSimpleName().equals(pPrNm)) {
            rz = crPuTrStgSv(pRvs);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map HsSeSelRt.</p>
   * @param pRvs request scoped vars
   * @return HsSeSelRt
   * @throws Exception - an exception
   */
  private HsSeSelRt crPuHsSeSelRt(
    final Map<String, Object> pRvs) throws Exception {
    HsSeSelRt rz = new HsSeSelRt();
    IFiSeSel fiHsSeSel = (IFiSeSel) this.fctBlc
      .laz(pRvs, IFiSeSel.class.getSimpleName());
    rz.setFiSeSel(fiHsSeSel);
    @SuppressWarnings("unchecked")
    FctEnPrc<RS> fctEnPrc = (FctEnPrc<RS>) this.fctBlc
      .laz(pRvs, FctEnPrc.class.getSimpleName());
    PrcEntRt dlg = (PrcEntRt) fctEnPrc
      .lazPart(pRvs, PrcEntRt.class.getSimpleName());
    rz.setRetrv(dlg);
    this.procs.put(HsSeSelRt.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), HsSeSelRt.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map HsSeSelDl.</p>
   * @param pRvs request scoped vars
   * @return HsSeSelDl
   * @throws Exception - an exception
   */
  private HsSeSelDl crPuHsSeSelDl(
    final Map<String, Object> pRvs) throws Exception {
    HsSeSelDl rz = new HsSeSelDl();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    IFiSeSel fiHsSeSel = (IFiSeSel) this.fctBlc
      .laz(pRvs, IFiSeSel.class.getSimpleName());
    rz.setFiSeSel(fiHsSeSel);
    @SuppressWarnings("unchecked")
    ItmSpDl<AItmSpf<?, ?>, ?> dlg = (ItmSpDl<AItmSpf<?, ?>, ?>)
      this.laz(pRvs, ItmSpDl.class.getSimpleName());
    rz.setItmSpDl(dlg);
    this.procs.put(HsSeSelDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), HsSeSelDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CuOrSv.</p>
   * @param pRvs request scoped vars
   * @return CuOrSv
   * @throws Exception - an exception
   */
  private CuOrSv crPuCuOrSv(
    final Map<String, Object> pRvs) throws Exception {
    CuOrSv rz = new CuOrSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    ICncOrd cncOrd = (ICncOrd) this.fctBlc
      .laz(pRvs, ICncOrd.class.getSimpleName());
    rz.setCncOrd(cncOrd);
    this.procs.put(CuOrSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), CuOrSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map CuOrSeSv.</p>
   * @param pRvs request scoped vars
   * @return CuOrSeSv
   * @throws Exception - an exception
   */
  private CuOrSeSv crPuCuOrSeSv(
    final Map<String, Object> pRvs) throws Exception {
    CuOrSeSv rz = new CuOrSeSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    ICncOrd cncOrd = (ICncOrd) this.fctBlc
      .laz(pRvs, ICncOrd.class.getSimpleName());
    rz.setCncOrd(cncOrd);
    IFiSeSel fiHsSeSel = (IFiSeSel) this.fctBlc
      .laz(pRvs, IFiSeSel.class.getSimpleName());
    rz.setFiSeSel(fiHsSeSel);
    this.procs.put(CuOrSeSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), CuOrSeSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map HsSeSelSv.</p>
   * @param pRvs request scoped vars
   * @return HsSeSelSv
   * @throws Exception - an exception
   */
  private HsSeSelSv crPuHsSeSelSv(
    final Map<String, Object> pRvs) throws Exception {
    HsSeSelSv rz = new HsSeSelSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    IFiSeSel fiHsSeSel = (IFiSeSel) this.fctBlc
      .laz(pRvs, IFiSeSel.class.getSimpleName());
    rz.setFiSeSel(fiHsSeSel);
    @SuppressWarnings("unchecked")
    ItmSpSv<AItmSpf<?, ?>, ?> dlg = (ItmSpSv<AItmSpf<?, ?>, ?>)
      this.laz(pRvs, ItmSpSv.class.getSimpleName());
    rz.setItmSpSv(dlg);
    this.procs.put(HsSeSelSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), HsSeSelSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map SeSelDl.</p>
   * @param pRvs request scoped vars
   * @return SeSelDl
   * @throws Exception - an exception
   */
  private SeSelDl crPuSeSelDl(final Map<String, Object> pRvs) throws Exception {
    SeSelDl rz = new SeSelDl();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    IFiSeSel fiSeSel = (IFiSeSel) this.fctBlc
      .laz(pRvs, IFiSeSel.class.getSimpleName());
    rz.setFiSeSel(fiSeSel);
    this.procs.put(SeSelDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), SeSelDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map SeSelSv.</p>
   * @param pRvs request scoped vars
   * @return SeSelSv
   * @throws Exception - an exception
   */
  private SeSelSv crPuSeSelSv(final Map<String, Object> pRvs) throws Exception {
    SeSelSv rz = new SeSelSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    IFiSeSel fiSeSel = (IFiSeSel) this.fctBlc
      .laz(pRvs, IFiSeSel.class.getSimpleName());
    rz.setFiSeSel(fiSeSel);
    this.procs.put(SeSelSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), SeSelSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map ItmSpDl.</p>
   * @param pRvs request scoped vars
   * @return ItmSpDl
   * @throws Exception - an exception
   */
  private ItmSpDl crPuItmSpDl(final Map<String, Object> pRvs) throws Exception {
    ItmSpDl rz = new ItmSpDl();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setAppPth(this.fctBlc.getFctDt().getAppPth());
    this.procs.put(ItmSpDl.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), ItmSpDl.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map ItmSpSv.</p>
   * @param pRvs request scoped vars
   * @return ItmSpSv
   * @throws Exception - an exception
   */
  private ItmSpSv crPuItmSpSv(final Map<String, Object> pRvs) throws Exception {
    ItmSpSv rz = new ItmSpSv();
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    FctEnPrc<RS> fctEnPrc = (FctEnPrc<RS>) this.fctBlc
      .laz(pRvs, FctEnPrc.class.getSimpleName());
    PrcEnfSv dlg = (PrcEnfSv) fctEnPrc
      .lazPart(pRvs, PrcEnfSv.class.getSimpleName());
    rz.setPrcEnfSv(dlg);
    rz.setAppPth(this.fctBlc.getFctDt().getAppPth());
    rz.setUplDir(this.fctBlc.getFctDt().getUplDir());
    this.procs.put(ItmSpSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), ItmSpSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map AdStgSv.</p>
   * @param pRvs request scoped vars
   * @return AdStgSv
   * @throws Exception - an exception
   */
  private AdStgSv crPuAdStgSv(final Map<String, Object> pRvs) throws Exception {
    AdStgSv rz = new AdStgSv();
    ISrAdStg srAdStg = (ISrAdStg) this.fctBlc
      .laz(pRvs, ISrAdStg.class.getSimpleName());
    rz.setSrAdStg(srAdStg);
    this.procs.put(AdStgSv.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), AdStgSv.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map TrStgSv.</p>
   * @param pRvs request scoped vars
   * @return TrStgSv
   * @throws Exception - an exception
   */
  private TrStgSv crPuTrStgSv(final Map<String, Object> pRvs) throws Exception {
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
   * <p>Create and put into the Map AdStgRt.</p>
   * @param pRvs request scoped vars
   * @return AdStgRt
   * @throws Exception - an exception
   */
  private AdStgRt crPuAdStgRt(final Map<String, Object> pRvs) throws Exception {
    AdStgRt rz = new AdStgRt();
    ISrAdStg srAdStg = (ISrAdStg) this.fctBlc
      .laz(pRvs, ISrAdStg.class.getSimpleName());
    rz.setSrAdStg(srAdStg);
    this.procs.put(AdStgRt.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(), AdStgRt.class
      .getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map TrStgRt.</p>
   * @param pRvs request scoped vars
   * @return TrStgRt
   * @throws Exception - an exception
   */
  private TrStgRt crPuTrStgRt(final Map<String, Object> pRvs) throws Exception {
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
