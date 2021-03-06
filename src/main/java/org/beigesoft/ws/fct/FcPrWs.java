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

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.fct.IFctPrc;
import org.beigesoft.fct.FctEnPrc;
import org.beigesoft.prc.IPrc;
import org.beigesoft.prc.PrcEntRt;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.ws.prc.ItmPg;
import org.beigesoft.ws.prc.WsPg;
import org.beigesoft.ws.prc.ChkOut;
import org.beigesoft.ws.prc.PrCart;
import org.beigesoft.ws.prc.ItmCart;
import org.beigesoft.ws.prc.PrBuOr;
import org.beigesoft.ws.prc.PrBur;
import org.beigesoft.ws.prc.PrPur;
import org.beigesoft.ws.prc.PrLog;
import org.beigesoft.ws.srv.ISrCart;
import org.beigesoft.ws.srv.IBuySr;
import org.beigesoft.ws.srv.IAcpOrd;

/**
 * <p>Factory of web-store public processors.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FcPrWs<RS> implements IFctPrc {

  /**
   * <p>Main factory.</p>
   **/
  private FctBlc<RS> fctBlc;

  /**
   * <p>Outside factories, e.g. PPL.</p>
   **/
  private Set<IFctPrc> fctsPrc;

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
          if (WsPg.class.getSimpleName().equals(pPrNm)) {
            rz = crPuWsPg(pRvs);
          } else if (ItmPg.class.getSimpleName().equals(pPrNm)) {
            rz = crPuItmPg(pRvs);
          } else if (ChkOut.class.getSimpleName().equals(pPrNm)) {
            rz = crPuChkOut(pRvs);
          } else if (PrCart.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrCart(pRvs);
          } else if (ItmCart.class.getSimpleName().equals(pPrNm)) {
            rz = crPuItmCart(pRvs);
          } else if (PrBuOr.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrBuOr(pRvs);
          } else if (PrBur.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrBur(pRvs);
          } else if (PrPur.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrPur(pRvs);
          } else if (PrLog.class.getSimpleName().equals(pPrNm)) {
            rz = crPuPrLog(pRvs);
          } else {
            if (this.fctsPrc != null) {
              for (IFctPrc fp : this.fctsPrc) {
                rz = fp.laz(pRvs, pPrNm);
                if (rz != null) {
                  break;
                }
              }
            }
            if (rz == null) {
              throw new ExcCode(ExcCode.WRCN, "There is no IProc: " + pPrNm);
            }
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Create and put into the Map ChkOut.</p>
   * @param pRvs request scoped vars
   * @return ChkOut
   * @throws Exception - an exception
   */
  private ChkOut<RS> crPuChkOut(
    final Map<String, Object> pRvs) throws Exception {
    ChkOut<RS> rz = new ChkOut<RS>();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setTrIsl(this.fctBlc.getFctDt().getReadTi());
    ISrCart srCart = (ISrCart) this.fctBlc
      .laz(pRvs, ISrCart.class.getSimpleName());
    rz.setSrCart(srCart);
    rz.setFcPrWs(this);
    this.procs.put(ChkOut.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      ChkOut.class.getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrCart.</p>
   * @param pRvs request scoped vars
   * @return PrCart
   * @throws Exception - an exception
   */
  private PrCart<RS> crPuPrCart(
    final Map<String, Object> pRvs) throws Exception {
    PrCart<RS> rz = new PrCart<RS>();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setTrIsl(this.fctBlc.getFctDt().getReadTi());
    ISrCart srCart = (ISrCart) this.fctBlc
      .laz(pRvs, ISrCart.class.getSimpleName());
    rz.setSrCart(srCart);
    rz.setFcPrWs(this);
    this.procs.put(PrCart.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      PrCart.class.getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map ItmCart.</p>
   * @param pRvs request scoped vars
   * @return ItmCart
   * @throws Exception - an exception
   */
  private ItmCart<RS> crPuItmCart(
    final Map<String, Object> pRvs) throws Exception {
    ItmCart<RS> rz = new ItmCart<RS>();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setTrIsl(this.fctBlc.getFctDt().getReadTi());
    ISrCart srCart = (ISrCart) this.fctBlc
      .laz(pRvs, ISrCart.class.getSimpleName());
    rz.setSrCart(srCart);
    rz.setFcPrWs(this);
    this.procs.put(ItmCart.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      ItmCart.class.getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrBuOr.</p>
   * @param pRvs request scoped vars
   * @return PrBuOr
   * @throws Exception - an exception
   */
  private PrBuOr<RS> crPuPrBuOr(
    final Map<String, Object> pRvs) throws Exception {
    PrBuOr<RS> rz = new PrBuOr<RS>();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setTrIsl(this.fctBlc.getFctDt().getReadTi());
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setSrvPg(this.fctBlc.lazSrvPg(pRvs));
    IBuySr buySr = (IBuySr) this.fctBlc.laz(pRvs, IBuySr.class.getSimpleName());
    rz.setBuySr(buySr);
    rz.setFcPrWs(this);
    @SuppressWarnings("unchecked")
    FctEnPrc<RS> fctEnPrc = (FctEnPrc<RS>) this.fctBlc
      .laz(pRvs, FctEnPrc.class.getSimpleName());
    PrcEntRt dlg = (PrcEntRt) fctEnPrc
      .lazPart(pRvs, PrcEntRt.class.getSimpleName());
    rz.setRetrv(dlg);
    this.procs.put(PrBuOr.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      PrBuOr.class.getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrBur.</p>
   * @param pRvs request scoped vars
   * @return PrBur
   * @throws Exception - an exception
   */
  private PrBur<RS> crPuPrBur(final Map<String, Object> pRvs) throws Exception {
    PrBur<RS> rz = new PrBur<RS>();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setTrIsl(this.fctBlc.getFctDt().getReadTi());
    rz.setRdb(rdb);
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    IBuySr buySr = (IBuySr) this.fctBlc.laz(pRvs, IBuySr.class.getSimpleName());
    rz.setBuySr(buySr);
    rz.setFcPrWs(this);
    this.procs.put(PrBur.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      PrBur.class.getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrPur.</p>
   * @param pRvs request scoped vars
   * @return PrPur
   * @throws Exception - an exception
   */
  private PrPur<RS> crPuPrPur(final Map<String, Object> pRvs) throws Exception {
    PrPur<RS> rz = new PrPur<RS>();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    rz.setSpamHnd(this.fctBlc.lazHndSpam(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    ISrCart srCart = (ISrCart) this.fctBlc
      .laz(pRvs, ISrCart.class.getSimpleName());
    rz.setSrCart(srCart);
    IAcpOrd acpOrd = (IAcpOrd) this.fctBlc
      .laz(pRvs, IAcpOrd.class.getSimpleName());
    rz.setAcpOrd(acpOrd);
    IBuySr buySr = (IBuySr) this.fctBlc.laz(pRvs, IBuySr.class.getSimpleName());
    rz.setBuySr(buySr);
    rz.setFcPrWs(this);
    this.procs.put(PrPur.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      PrPur.class.getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map PrLog.</p>
   * @param pRvs request scoped vars
   * @return PrLog
   * @throws Exception - an exception
   */
  private PrLog<RS> crPuPrLog(final Map<String, Object> pRvs) throws Exception {
    PrLog<RS> rz = new PrLog<RS>();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    rz.setSpamHnd(this.fctBlc.lazHndSpam(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setTrIsl(this.fctBlc.getFctDt().getReadTi());
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    ISrCart srCart = (ISrCart) this.fctBlc
      .laz(pRvs, ISrCart.class.getSimpleName());
    rz.setSrvCart(srCart);
    IBuySr buySr = (IBuySr) this.fctBlc.laz(pRvs, IBuySr.class.getSimpleName());
    rz.setBuySr(buySr);
    rz.setFcPrWs(this);
    this.procs.put(PrLog.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      PrLog.class.getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map ItmPg.</p>
   * @param pRvs request scoped vars
   * @return ItmPg
   * @throws Exception - an exception
   */
  private ItmPg<RS> crPuItmPg(final Map<String, Object> pRvs) throws Exception {
    ItmPg<RS> rz = new ItmPg<RS>();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    ISrCart srCart = (ISrCart) this.fctBlc
      .laz(pRvs, ISrCart.class.getSimpleName());
    rz.setSrCart(srCart);
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setTrIsl(this.fctBlc.getFctDt().getReadTi());
    IBuySr buySr = (IBuySr) this.fctBlc.laz(pRvs, IBuySr.class.getSimpleName());
    rz.setBuySr(buySr);
    this.procs.put(ItmPg.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      ItmPg.class.getSimpleName() + " has been created.");
    return rz;
  }

  /**
   * <p>Create and put into the Map WsPg.</p>
   * @param pRvs request scoped vars
   * @return WsPg
   * @throws Exception - an exception
   */
  private WsPg<RS> crPuWsPg(final Map<String, Object> pRvs) throws Exception {
    WsPg<RS> rz = new WsPg<RS>();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setTrIsl(this.fctBlc.getFctDt().getReadTi());
    ISrCart srCart = (ISrCart) this.fctBlc
      .laz(pRvs, ISrCart.class.getSimpleName());
    rz.setSrCart(srCart);
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setSrvPg(this.fctBlc.lazSrvPg(pRvs));
    this.procs.put(WsPg.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      WsPg.class.getSimpleName() + " has been created.");
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

  /**
   * <p>Getter for fctsPrc.</p>
   * @return Set<IFctPrc>
   **/
  public final synchronized Set<IFctPrc> getFctsPrc() {
    return this.fctsPrc;
  }

  /**
   * <p>Setter for fctsPrc.</p>
   * @param pFctsPrc reference
   **/
  public final synchronized void setFctsPrc(final Set<IFctPrc> pFctsPrc) {
    this.fctsPrc = pFctsPrc;
  }
}
