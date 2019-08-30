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
import java.util.Arrays;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.log.ILog;
import org.beigesoft.prc.IPrc;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.TxDst;
import org.beigesoft.ws.mdl.EPaymMth;
import org.beigesoft.ws.mdl.EDeliv;
import org.beigesoft.ws.mdlp.Cart;
import org.beigesoft.ws.fct.FcPrWs;
import org.beigesoft.ws.srv.ISrCart;

/**
 * <p>Processor that changes cart's delivering and payment methods.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrCart<RS> implements IPrc {

  /**
   * <p>Logger.</p>
   **/
  private ILog log;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Shopping Cart service.</p>
   **/
  private ISrCart srCart;

  /**
   * <p>Processors factory.</p>
   **/
  private FcPrWs<RS> fcPrWs;

  /**
   * <p>RDB service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Transaction isolation.</p>
   **/
  private Integer trIsl;

  /**
   * <p>Process request.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      Cart cart = this.srCart.getCart(pRvs, pRqDt, false, false);
      if (cart != null) {
        Map<String, Object> vs = new HashMap<String, Object>();
        String dlvStr = pRqDt.getParam("delv");
        String paymStr = pRqDt.getParam("paym");
        EDeliv dlv = EDeliv.class.getEnumConstants()[Integer.parseInt(dlvStr)];
        EPaymMth paym = EPaymMth.class.
          getEnumConstants()[Integer.parseInt(paymStr)];
        if (dlv != cart.getDelv() || paym != cart.getPaym()) {
          EDeliv dlvOld = cart.getDelv();
          cart.setDelv(dlv);
          cart.setPaym(paym);
          String[] ndFds = new String[] {"ver", "paym", "delv"};
          Arrays.sort(ndFds);
          vs.put("ndFds", ndFds);
          this.orm.update(pRvs, vs, cart);
          if (dlv != dlvOld) {
            AcStg as = (AcStg) pRvs.get("astg");
            TxDst txRules = this.srCart.revTxRules(pRvs, cart, as);
            if (txRules != null) {
              pRvs.put("txRules", txRules);
            }
            this.srCart.hndCartChg(pRvs, cart, txRules);
          }
        }
      } else {
        throw new ExcCode(ExcCode.SPAM, "There is no cart!");
      }
      this.rdb.commit();
    } catch (Exception ex) {
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw ex;
    } finally {
      this.rdb.release();
    }
    String procNm = pRqDt.getParam("prcRed");
    if (getClass().getSimpleName().equals(procNm)) {
      throw new ExcCode(ExcCode.SPAM, "Danger! stupid scam!!!");
    }
    IPrc proc = this.fcPrWs.laz(pRvs, procNm);
    proc.process(pRvs, pRqDt);
  }

  //Simple getters and setters:
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
   * <p>Getter for trIsl.</p>
   * @return Integer
   **/
  public final Integer getTrIsl() {
    return this.trIsl;
  }

  /**
   * <p>Setter for trIsl.</p>
   * @param pTrIsl reference
   **/
  public final void setTrIsl(final Integer pTrIsl) {
    this.trIsl = pTrIsl;
  }

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
   * <p>Geter for orm.</p>
   * @return IOrm
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
