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
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.log.ILog;
import org.beigesoft.prc.IPrc;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Uom;
import org.beigesoft.acc.mdlp.TxDst;
import org.beigesoft.ws.fct.FcPrWs;
import org.beigesoft.ws.mdl.EItmTy;
import org.beigesoft.ws.mdlp.Cart;
import org.beigesoft.ws.mdlp.CartLn;
import org.beigesoft.ws.mdlp.TrdStg;
import org.beigesoft.ws.srv.ISrCart;

/**
 * <p>Processor that adds/deletes item to/from cart or changes its quantity
 * (from modal dialog for single item).</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class ItmCart<RS> implements IPrc {

  /**
   * <p>Logger.</p>
   **/
  private ILog log;

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
   * <p>Process entity request.</p>
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
      TrdStg ts = (TrdStg) pRvs.get("tstg");
      AcStg as = (AcStg) pRvs.get("astg");
      TxDst txRules = null;
      String act = pRqDt.getParam("act");
      if ("del".equals(act)) {
        Cart cart = this.srCart.getCart(pRvs, pRqDt, false, false);
        if (cart != null) {
          txRules = this.srCart.revTxRules(pRvs, cart, as);
          String lnIdStr = pRqDt.getParam("lnId");
          if (lnIdStr != null) {
            Long lnId = Long.valueOf(lnIdStr);
            CartLn cartLn = null;
            for (CartLn ci : cart.getItems()) {
              if (ci.getIid().equals(lnId)) {
                if (ci.getDisab()) {
                  throw new ExcCode(ExcCode.SPAM, "requested item disabled!");
                }
                cartLn = ci;
                break;
              }
            }
            if (cartLn == null) {
              throw new ExcCode(ExcCode.SPAM, "Requested item not found!");
            }
            if (cartLn.getForc()) {
              throw new ExcCode(ExcCode.SPAM, "Requested item forced");
            }
            this.srCart.delLine(pRvs, cartLn, txRules);
            this.srCart.hndCartChg(pRvs, cart, txRules);
          }
        }
      } else {
        Cart cart = this.srCart.getCart(pRvs, pRqDt, true, false);
        txRules = this.srCart.revTxRules(pRvs, cart, as);
        CartLn cartLn = null;
        String lnIdStr = pRqDt.getParam("lnId");
        String quanStr = pRqDt.getParam("quan");
        String avQuanStr = pRqDt.getParam("avQuan");
        String unStStr = pRqDt.getParam("unSt");
        BigDecimal quan = new BigDecimal(quanStr);
        BigDecimal avQuan = new BigDecimal(avQuanStr);
        if (quan.compareTo(avQuan) == 1) {
          quan = avQuan;
        }
        BigDecimal unSt = new BigDecimal(unStStr);
        String itIdStr = pRqDt.getParam("itId");
        String itTypStr = pRqDt.getParam("itTyp");
        Long itId = Long.valueOf(itIdStr);
        EItmTy itTyp = EItmTy.class.
          getEnumConstants()[Integer.parseInt(itTypStr)];
        boolean redoPr = false;
        if (lnIdStr != null) { //change quanity
          Long lnId = Long.valueOf(lnIdStr);
          cartLn = fndCartItmById(cart, lnId);
        } else { //add
          redoPr = true;
          String uomIdStr = pRqDt.getParam("uomId");
          Long uomId = Long.valueOf(uomIdStr);
          for (CartLn ci : cart.getItems()) {
            //check for duplicate cause "weird" but accepted request
            if (!ci.getDisab() && ci.getItTyp().equals(itTyp)
              && ci.getItId().equals(itId)) {
              cartLn = ci;
              break;
            }
          }
          if (cartLn == null) {
            for (CartLn ci : cart.getItems()) {
              if (ci.getDisab()) {
                cartLn = ci;
                cartLn.setDisab(false);
                cartLn.setForc(false);
                cartLn.setSelr(null);
                cartLn.setTxCt(null);
                cartLn.setTdsc(null);
                break;
              }
            }
          }
          if (cartLn == null) {
            cartLn = creCartItm(cart);
          }
          Uom uom = new Uom();
          uom.setIid(uomId);
          cartLn.setUom(uom);
          cartLn.setItId(itId);
          cartLn.setItTyp(itTyp);
        }
        if (!cartLn.getForc()) {
          cartLn.setAvQuan(avQuan);
          cartLn.setQuan(quan);
          cartLn.setUnSt(unSt);
          BigDecimal amount = cartLn.getPri().multiply(cartLn.getQuan()).
            setScale(as.getPrDp(), as.getRndm());
          if (ts.getTxExcl()) {
            cartLn.setSubt(amount);
          } else {
            cartLn.setTot(amount);
          }
          this.srCart.mkLine(pRvs, cartLn, as, ts, txRules, redoPr, true);
          this.srCart.hndCartChg(pRvs, cart, txRules);
        }
      }
      if (txRules != null) {
        pRvs.put("txRules", txRules);
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
    redir(pRvs, pRqDt);
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

  /**
   * <p>Find cart item by ID.</p>
   * @param pCart cart
   * @param pCrItId cart item ID
   * @return cart item
   * @throws Exception - an exception
   **/
  public final CartLn fndCartItmById(final Cart pCart,
    final Long pCrItId) throws Exception {
    CartLn cartLn = null;
    for (CartLn ci : pCart.getItems()) {
      if (ci.getIid().equals(pCrItId)) {
        if (ci.getDisab()) {
          throw new ExcCode(ExcCode.WR, "requested_item_disabled");
        }
        cartLn = ci;
        break;
      }
    }
    if (cartLn == null) {
      throw new ExcCode(ExcCode.WR, "requested_item_not_found");
    }
    return cartLn;
  }

  /**
   * <p>Create cart item.</p>
   * @param pCart cart
   * @return cart item
   **/
  public final CartLn creCartItm(final Cart pCart) {
    CartLn cartLn = new CartLn();
    cartLn.setIsNew(true);
    cartLn.setDisab(false);
    cartLn.setForc(false);
    cartLn.setOwnr(pCart);
    pCart.getItems().add(cartLn);
    return cartLn;
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
