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

package org.beigesoft.ws.prc;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.log.ILog;
import org.beigesoft.prc.IPrc;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.ws.mdl.EPaymMth;
import org.beigesoft.ws.mdl.Purch;
import org.beigesoft.ws.mdlp.CuOrSeTxLn;
import org.beigesoft.ws.mdlp.CuOrSe;
import org.beigesoft.ws.mdlp.CuOr;
import org.beigesoft.ws.mdlp.CuOrTxLn;
import org.beigesoft.ws.mdlp.AddStg;
import org.beigesoft.ws.mdlp.Buyer;
import org.beigesoft.ws.fct.FcPrWs;
import org.beigesoft.ws.srv.ISrCart;
import org.beigesoft.ws.srv.IAcpOrd;
import org.beigesoft.ws.srv.IBuySr;

/**
 * <p>
 * Service that accepts purchase (books orders) that will be payed offline.
 * </p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrPur<RS> implements IPrc {

  /**
   * <p>Logger.</p>
   **/
  private ILog log;

  /**
   * <p>Logger security.</p>
   **/
  private ILog secLog;

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Shopping Cart service.</p>
   **/
  private ISrCart srCart;

  /**
   * <p>Accept buyer's new orders service.</p>
   **/
  private IAcpOrd acpOrd;

  /**
   * <p>Buyer service.</p>
   **/
  private IBuySr buySr;

  /**
   * <p>Processors factory.</p>
   **/
  private FcPrWs<RS> fcPrWs;

  /**
   * <p>Process entity request.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    if (!pRqDt.getReqUrl().toString().toLowerCase().startsWith("https")) {
      throw new Exception("HTTP not supported!!!");
    }
    Buyer buyer = this.buySr.getAuthBuyr(pRvs, pRqDt);
    if (buyer == null) {
  this.log.warn(pRvs, getClass(), "Trying to accept purchase without buyer!");
      redir(pRvs, pRqDt);
      return;
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    AddStg setAdd = (AddStg) pRvs.get("tastg");
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(setAdd.getBkTr());
      this.rdb.begin();
      Purch pur = this.acpOrd.accept(pRvs, pRqDt, buyer);
      this.srCart.emptyCart(pRvs, buyer);
      if (pur.getOrds() != null && pur.getOrds().size() > 0) {
        //checking orders with online payment:
        vs.put("CuOrdpLv", 0);
        for (CuOr or : pur.getOrds()) { //TODO PERFORM to IAcpOrd
          if (or.getPaym().equals(EPaymMth.PAYPAL)
            || or.getPaym().equals(EPaymMth.PAYPAL_ANY)
              || or.getPaym().equals(EPaymMth.PARTIAL_ONLINE)
                || or.getPaym().equals(EPaymMth.ONLINE)) {
            throw new Exception("It must be offline payment!!");
          }
          if (or.getToTx().compareTo(BigDecimal.ZERO) == 1) {
            List<CuOrTxLn> tls = getOrm().retLstCnd(pRvs, vs, CuOrTxLn.class,
              "where OWNR=" + or.getIid());
            or.setTaxes(tls);
          }
        }
        vs.clear();
      }
      if (pur.getSords() != null && pur.getSords().size() > 0) {
        //checking S.E. orders with online payment:
        vs.put("CuOrSedpLv", 0);
        for (CuOrSe or : pur.getSords()) {
          if (or.getPaym().equals(EPaymMth.PAYPAL)
            || or.getPaym().equals(EPaymMth.PAYPAL_ANY)
              || or.getPaym().equals(EPaymMth.PARTIAL_ONLINE)
                || or.getPaym().equals(EPaymMth.ONLINE)) {
            throw new Exception("It must be offline payment!!");
          }
          if (or.getToTx().compareTo(BigDecimal.ZERO) == 1) {
            List<CuOrSeTxLn> tls = getOrm().retLstCnd(pRvs, vs,
              CuOrSeTxLn.class, "where OWNR=" + or.getIid());
            or.setTaxes(tls);
          }
          vs.clear();
        }
      }
      pRvs.put("pur",  pur);
      pRqDt.setAttr("rnd", "waor");
      this.rdb.commit();
    } catch (Exception ex) {
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw ex;
    } finally {
      this.rdb.release();
    }
  }

  /**
   * <p>Redirect.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  public final void redir(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    String procNm = pRqDt.getParam("prcRed");
    if (getClass().getSimpleName().equals(procNm)) {
      throw new ExcCode(ExcCode.SPAM, "Danger! Stupid scam!!!");
    }
    IPrc proc = this.fcPrWs.laz(pRvs, procNm);
    proc.process(pRvs, pRqDt);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for log.</p>
   * @return ILog
   **/
  public final ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final void setLog(final ILog pLog) {
    this.log = pLog;
  }

  /**
   * <p>Getter for secLog.</p>
   * @return ILog
   **/
  public final ILog getSecLog() {
    return this.secLog;
  }

  /**
   * <p>Setter for secLog.</p>
   * @param pSecLog reference
   **/
  public final void setSecLog(final ILog pSecLog) {
    this.secLog = pSecLog;
  }

  /**
   * <p>Getter for rdb.</p>
   * @return IRdb
   **/
  public final IRdb<RS> getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final void setRdb(final IRdb<RS> pRdb) {
    this.rdb = pRdb;
  }

  /**
   * <p>Getter for orm.</p>
   * @return IRdb<RS>
   **/
  public final IOrm getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final void setOrm(final IOrm pOrm) {
    this.orm = pOrm;
  }

  /**
   * <p>Getter for srCart.</p>
   * @return ISrCart
   **/
  public final ISrCart getSrCart() {
    return this.srCart;
  }

  /**
   * <p>Setter for srCart.</p>
   * @param pSrCart reference
   **/
  public final void setSrCart(final ISrCart pSrCart) {
    this.srCart = pSrCart;
  }

  /**
   * <p>Getter for acpOrd.</p>
   * @return IAcpOrd
   **/
  public final IAcpOrd getAcpOrd() {
    return this.acpOrd;
  }

  /**
   * <p>Setter for acpOrd.</p>
   * @param pAcpOrd reference
   **/
  public final void setAcpOrd(final IAcpOrd pAcpOrd) {
    this.acpOrd = pAcpOrd;
  }

  /**
   * <p>Getter for buySr.</p>
   * @return IBuySr
   **/
  public final IBuySr getBuySr() {
    return this.buySr;
  }

  /**
   * <p>Setter for buySr.</p>
   * @param pBuySr reference
   **/
  public final void setBuySr(final IBuySr pBuySr) {
    this.buySr = pBuySr;
  }

  /**
   * <p>Getter for fcPrWs.</p>
   * @return FcPrWs<RS>
   **/
  public final FcPrWs<RS> getFcPrWs() {
    return this.fcPrWs;
  }

  /**
   * <p>Setter for fcPrWs.</p>
   * @param pFcPrWs reference
   **/
  public final void setFcPrWs(final FcPrWs<RS> pFcPrWs) {
    this.fcPrWs = pFcPrWs;
  }
}
