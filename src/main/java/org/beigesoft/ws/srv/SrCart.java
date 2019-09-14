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

package org.beigesoft.ws.srv;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.IRecSet;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.log.ILog;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.srv.INumStr;
import org.beigesoft.acc.mdl.ETxTy;
import org.beigesoft.acc.mdl.CmprTxCtLnRt;
import org.beigesoft.acc.mdlb.AItm;
import org.beigesoft.acc.mdlb.ATxDsLn;
import org.beigesoft.acc.mdlp.Curr;
import org.beigesoft.acc.mdlp.TxCt;
import org.beigesoft.acc.mdlp.TxCtLn;
import org.beigesoft.acc.mdlp.Tax;
import org.beigesoft.acc.mdlp.TxDst;
import org.beigesoft.acc.mdlp.DbCr;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Itm;
import org.beigesoft.acc.mdlp.I18Itm;
import org.beigesoft.acc.mdlp.Srv;
import org.beigesoft.acc.mdlp.I18Srv;
import org.beigesoft.acc.mdlp.ItTxDl;
import org.beigesoft.acc.mdlp.SrTxDl;
import org.beigesoft.ws.mdl.EItmTy;
import org.beigesoft.ws.mdl.EPaymMth;
import org.beigesoft.ws.mdlb.IHsSeSel;
import org.beigesoft.ws.mdlb.AItmPri;
import org.beigesoft.ws.mdlp.CartItTxLn;
import org.beigesoft.ws.mdlp.BurPric;
import org.beigesoft.ws.mdlp.PriItm;
import org.beigesoft.ws.mdlp.PriSrv;
import org.beigesoft.ws.mdlp.SeSrv;
import org.beigesoft.ws.mdlp.I18SeSrv;
import org.beigesoft.ws.mdlp.SeSrvPri;
import org.beigesoft.ws.mdlp.I18SeItm;
import org.beigesoft.ws.mdlp.SeItm;
import org.beigesoft.ws.mdlp.SeItmPri;
import org.beigesoft.ws.mdlp.SeSel;
import org.beigesoft.ws.mdlp.Buyer;
import org.beigesoft.ws.mdlp.Cart;
import org.beigesoft.ws.mdlp.CartTot;
import org.beigesoft.ws.mdlp.CartLn;
import org.beigesoft.ws.mdlp.CartTxLn;
import org.beigesoft.ws.mdlp.TrdStg;
import org.beigesoft.ws.mdlp.CurrRt;
import org.beigesoft.ws.mdlp.SitTxDl;
import org.beigesoft.ws.mdlp.SerTxDl;
import org.beigesoft.ws.mdlp.Deliv;

/**
 * <p>Service that retrieve/create buyer's shopping cart, make cart totals
 * after any line action, etc.
 * This is shared non-transactional service.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrCart<RS> implements ISrCart {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Business service for trading settings.</p>
   **/
  private ISrTrStg srTrStg;

  /**
   * <p>Service print number.</p>
   **/
  private INumStr numStr;

  /**
   * <p>PPL ready class-flag.</p>
   **/
  private Class<?> pplCl;

  /**
   * <p>Buyer service.</p>
   **/
  private IBuySr buySr;

  //Cached queries:
  /**
   * <p>Query taxes invoice basis non-aggregate.</p>
   **/
  private String quTxInvBas;

  /**
   * <p>Query taxes invoice basis aggregate.</p>
   **/
  private String quTxInvBasAggr;

  /**
   * <p>Query taxes item basis non-aggregate.</p>
   **/
  private String quTxItBas;

  /**
   * <p>Query taxes item basis aggregate.</p>
   **/
  private String quTxItBasAggr;

  /**
   * <p>Query item price.</p>
   **/
  private String quItemPrice;

  /**
   * <p>Query S.E. item price.</p>
   **/
  private String quItemSePrice;

  /**
   * <p>Query item price for category.</p>
   **/
  private String quItemPriceCat;

  /**
   * <p>Query S.E. item price for category.</p>
   **/
  private String quItemSePriceCat;

  /**
   * <p>Get/Create Cart.</p>
   * @param pRvs additional param
   * @param pRqDt Request Data
   * @param pIsNeedToCreate  if need to create, e.g. "NO" for deleting item from
   *  cart, "YES" for adding one.
   * @param pIsBuAuth buyer must be authorized
   * @return shopping cart or null
   * @throws Exception - an exception
   **/
  @Override
  public final Cart getCart(final Map<String, Object> pRvs, final IReqDt pRqDt,
    final boolean pIsNeedToCreate, final boolean pIsBuAuth) throws Exception {
    Buyer buyer = null;
    boolean burNew = false;
    Map<String, Object> vs = new HashMap<String, Object>();
    if (pIsBuAuth) {
      buyer = this.buySr.getAuthBuyr(pRvs, pRqDt);
    } else {
      buyer = (Buyer) pRvs.get("buyr");
      if (buyer == null) {
        buyer = this.buySr.getBuyr(pRvs, pRqDt);
      }
      if (buyer == null && pIsNeedToCreate) {
        buyer = this.buySr.createBuyr(pRvs, pRqDt);
        if (buyer.getIsNew()) {
          getOrm().insIdLn(pRvs, vs, buyer);
        } else {
          getOrm().update(pRvs, vs, buyer);
        }
        pRqDt.setCookVl("cBuyerId", buyer.getIid().toString());
        burNew = true;
      }
    }
    TrdStg ts = srTrStg.lazTrStg(pRvs);
    pRvs.put("buyr", buyer);
    Cart cart = (Cart) pRvs.get("cart");
    if (cart == null && buyer != null && !buyer.getIsNew()) {
      cart = retrCart(pRvs, buyer, false);
      if (cart == null && pIsNeedToCreate) {
        cart = new Cart();
        Curr curr = (Curr) pRvs.get("wscurr");
        cart.setPaym(ts.getPaym());
        cart.setCurr(curr);
        cart.setItems(new ArrayList<CartLn>());
        cart.setTaxes(new ArrayList<CartTxLn>());
        cart.setTotals(new ArrayList<CartTot>());
        cart.setIid(buyer);
        getOrm().insIdNln(pRvs, vs, cart);
      } else if (cart != null && burNew) {
        emptyCart(pRvs, cart);
      }
      if (cart != null) {
        pRvs.put("cart", cart);
      }
    }
    if (cart != null) {
      if (EPaymMth.ANY.equals(cart.getPaym())
        || EPaymMth.PARTIAL_ONLINE.equals(cart.getPaym())
          || EPaymMth.ONLINE.equals(cart.getPaym())) {
        cart.setPaym(EPaymMth.PAY_CASH);
      }
      List<EPaymMth> payMts = new ArrayList<EPaymMth>();
      pRvs.put("payMts", payMts);
      payMts.add(EPaymMth.PAY_CASH);
      payMts.add(EPaymMth.BANK_TRANSFER);
      payMts.add(EPaymMth.BANK_CHEQUE);
      if (pplCl != null) {
        payMts.add(EPaymMth.PAYPAL);
      }
    }
    return cart;
  }

  /**
   * <p>Refresh cart totals by seller cause line inserted/changed/deleted.</p>
   * @param pRvs request scoped vars
   * @param pTs TrdStg
   * @param pCaLn affected cart line
   * @param pAs Accounting Settings
   * @param pTxRls NULL if not taxable
   * @throws Exception - an exception.
   **/
  @Override
  public final void mkCartTots(final Map<String, Object> pRvs, final TrdStg pTs,
    final CartLn pCaLn, final AcStg pAs, final TxDst pTxRls) throws Exception {
    BigDecimal toTx = BigDecimal.ZERO;
    BigDecimal toTxSe = BigDecimal.ZERO;
    String descr = null;
    Map<String, Object> vs = new HashMap<String, Object>();
    if (pTxRls != null) {
      descr = "Tax rules: aggregate/invoice basis/zip/RM = " + pTxRls.getStAg()
    + "/" + pTxRls.getStIb() + "/" + pTxRls.getZip() + "/" + pTxRls.getStRm();
      boolean dbgSh = getLog().getDbgSh(this.getClass(), 13000);
      if (dbgSh) {
        String txCat;
        if (pCaLn.getTxCt() != null) {
          txCat = pCaLn.getTxCt().getNme();
        } else {
          txCat = "-";
        }
        getLog().debug(pRvs, SrCart.class, "Item: name/tax category/disabled = "
          + pCaLn.getNme() + "/" + txCat + "/" + pCaLn.getDisab());
      }
      for (CartTxLn ctl : pCaLn.getOwnr().getTaxes()) {
        if (!ctl.getDisab() && (ctl.getSelr() == null && pCaLn.getSelr() == null
          || ctl.getSelr() != null && pCaLn.getSelr() != null
&& pCaLn.getSelr().getIid().getIid().equals(ctl.getSelr().getIid().getIid()))) {
          ctl.setDisab(true);
        }
      }
      //data storage for aggregate rate
      //and non-aggregate invoice basis taxes included:
      List<CartLn> txdLns = null;
      //data storages for non-aggregate rate
      //except invoice basis with included taxes:
      List<Tax> txs = null; //taxes
      List<Double> toTxTaxb = null; //tax's totals/taxables
      List<Double> txPerc = null; //tax's percents for invoice basis
      String query;
      if (!pTxRls.getStAg() && !(pTxRls.getStIb() && !pTs.getTxExcl())) {
        //non-aggregate except invoice basis with included taxes:
        txs = new ArrayList<Tax>();
        toTxTaxb = new ArrayList<Double>();
        if (!pTxRls.getStIb()) {
          //item basis:
          query = lazQuTxItBas();
        } else {
          //invoice basis, taxes excluded:
          txPerc = new ArrayList<Double>();
          query = lazQuTxInvBas();
        }
      } else { //non-aggregate invoice basis with included taxes
        //and aggregate for others:
        txdLns = new ArrayList<CartLn>();
        if (!pTxRls.getStIb()) { //item basis
          query = lazQuTxItBasAggr();
        } else { //invoice basis:
          query = lazQuTxInvBasAggr();
        }
      }
      String condSel;
      if (pCaLn.getSelr() == null) {
        condSel = " is null";
      } else {
        condSel = "=" + pCaLn.getSelr().getIid().getIid();
      }
      query = query.replace(":CARTID", pCaLn.getOwnr().getBuyr()
        .getIid().toString()).replace(":CONDSEL", condSel);
      IRecSet<RS> rs = null;
      try {
        rs = getRdb().retRs(query);
        if (rs.first()) {
          do {
            Long txId = rs.getLong("TAXID");
            String txNm = rs.getStr("TAXNAME");
            Tax tax = new Tax();
            tax.setIid(txId);
            tax.setNme(txNm);
            if (!pTxRls.getStAg() && !(pTxRls.getStIb() && !pTs.getTxExcl())) {
              //non-aggregate except invoice basis with included taxes:
              txs.add(tax);
              if (!pTxRls.getStIb()) {
                //item basis, taxes excluded/included:
                toTxTaxb.add(rs.getDouble("TOTALTAX"));
              } else {
                //invoice basis, taxes excluded:
                txPerc.add(rs.getDouble("RATE"));
                toTxTaxb.add(rs.getDouble("SUBT"));
              }
            } else { //non-aggregate invoice basis with included taxes
              //and aggregate for others:
              Double percent = rs.getDouble("RATE");
              Long tcId = rs.getLong("TAXCATID");
              if (!pTxRls.getStIb()) { //item basis:
                Long clId = rs.getLong("CLID");
                CartLn txdLn = mkTxdLn(txdLns, clId, tcId, tax, percent, pAs);
                txdLn.setToTx(BigDecimal.valueOf(rs.getDouble("TOTX"))
                  .setScale(pAs.getPrDp(), RoundingMode.HALF_UP));
              } else { //invoice basis:
                CartLn txdLn = mkTxdLn(txdLns, tcId, tcId, tax, percent,
                  pAs);
                txdLn.setTot(BigDecimal.valueOf(rs.getDouble("TOT"))
                  .setScale(pAs.getPrDp(), RoundingMode.HALF_UP));
                txdLn.setSubt(BigDecimal.valueOf(rs.getDouble("SUBT"))
                  .setScale(pAs.getPrDp(), RoundingMode.HALF_UP));
              }
            }
          } while (rs.next());
        }
      } finally {
        if (rs != null) {
          rs.close();
        }
      }
      if (!pTxRls.getStAg() && !(pTxRls.getStIb() && !pTs.getTxExcl())) {
        //non-aggregate except invoice basis with included taxes:
        for (int i = 0; i < txs.size(); i++) {
          CartTxLn ctl = fndCreTxLn(pRvs, pCaLn.getOwnr(), txs.get(i),
            pCaLn.getSelr(), false);
          Double toTxd;
          if (!pTxRls.getStIb()) {
            //item basis, taxes excluded/included:
            toTxd = toTxTaxb.get(i);
          } else {
            //invoice basis, taxes excluded:
            toTxd = toTxTaxb.get(i) * txPerc.get(i) / 100.0;
            ctl.setTxb(BigDecimal.valueOf(toTxTaxb.get(i)));
          }
          ctl.setTot(BigDecimal.valueOf(toTxd)
            .setScale(pAs.getPrDp(), pTxRls.getStRm()));
          if (ctl.getIsNew()) {
            getOrm().insIdLn(pRvs, vs, ctl);
          } else {
            getOrm().update(pRvs, vs, ctl);
          }
        }
      } else { //non-aggregate invoice basis with included taxes
        //and aggregate for others:
        BigDecimal bd100 = new BigDecimal("100.00");
        Comparator<TxCtLn> cmpr = Collections.reverseOrder(new CmprTxCtLnRt());
        for (CartLn txdLn : txdLns) {
          int ti = 0;
          //aggregate rate line scoped storages:
          BigDecimal txAgr = null;
          BigDecimal txAgrAcm = BigDecimal.ZERO;
          Collections.sort(txdLn.getTxCt().getTxs(), cmpr);
          for (TxCtLn itcl : txdLn.getTxCt().getTxs()) {
            ti++;
            if (txAgr == null) {
              if (pTxRls.getStIb()) { //invoice basis:
                if (!pTs.getTxExcl()) {
                  txAgr = txdLn.getTot().subtract(txdLn.getTot().divide(
                BigDecimal.ONE.add(txdLn.getTxCt().getAgRt()
              .divide(bd100, pAs.getPrDp(), pTxRls.getStRm()))));
                } else {
                  txAgr = txdLn.getSubt().multiply(txdLn.getTxCt()
                .getAgRt()).divide(bd100, pAs.getPrDp(), pTxRls.getStRm());
                }
              } else {
                //item basis, taxes included/excluded
                txAgr = txdLn.getToTx();
              }
            }
            if (ti < txdLn.getTxCt().getTxs().size()) {
              txdLn.setToTx(txAgr.multiply(itcl.getRate()).divide(txdLn
                .getTxCt().getAgRt(), pAs.getPrDp(), pTxRls.getStRm()));
              txAgrAcm = txAgrAcm.add(txdLn.getToTx());
            } else { //the rest or only tax:
              txdLn.setToTx(txAgr.subtract(txAgrAcm));
            }
            CartTxLn ctl = fndCreTxLn(pRvs, pCaLn.getOwnr(),
              itcl.getTax(), pCaLn.getSelr(), true);
            ctl.setTot(ctl.getTot().add(txdLn.getToTx()));
            if (pTxRls.getStIb()) {
              if (pTs.getTxExcl()) {
                ctl.setTxb(ctl.getTxb().add(txdLn.getSubt()));
              } else {
                ctl.setTxb(ctl.getTxb().add(txdLn.getTot()));
              }
            }
            if (ctl.getIsNew()) {
              getOrm().insIdLn(pRvs, vs, ctl);
            } else {
              getOrm().update(pRvs, vs, ctl);
            }
          }
        }
      }
      for (CartTxLn ctl : pCaLn.getOwnr().getTaxes()) {
        if (!ctl.getDisab()) {
          if (ctl.getSelr() == null && pCaLn.getSelr() == null
            || ctl.getSelr() != null && pCaLn.getSelr() != null
&& pCaLn.getSelr().getIid().getIid().equals(ctl.getSelr().getIid().getIid())) {
            toTxSe = toTxSe.add(ctl.getTot());
          }
          toTx = toTx.add(ctl.getTot());
        } else if (ctl.getSelr() == null && pCaLn.getSelr() == null
          || ctl.getSelr() != null && pCaLn.getSelr() != null
&& pCaLn.getSelr().getIid().getIid().equals(ctl.getSelr().getIid().getIid())) {
          getOrm().update(pRvs, vs, ctl);
        }
      }
    }
    BigDecimal tot = BigDecimal.ZERO;
    BigDecimal totSe = BigDecimal.ZERO;
    for (CartLn cl : pCaLn.getOwnr().getItems()) {
      if (!cl.getDisab()) {
        if (cl.getSelr() == null && pCaLn.getSelr() == null
          || cl.getSelr() != null && pCaLn.getSelr() != null
&& pCaLn.getSelr().getIid().getIid().equals(cl.getSelr().getIid().getIid())) {
          totSe = totSe.add(cl.getTot());
        }
        tot = tot.add(cl.getTot());
      }
    }
    pCaLn.getOwnr().setToTx(toTx);
    pCaLn.getOwnr().setSubt(tot.subtract(toTx));
    pCaLn.getOwnr().setTot(tot);
    pCaLn.getOwnr().setDscr(descr);
    getOrm().update(pRvs, vs, pCaLn.getOwnr());
    CartTot cartTot = null;
    for (CartTot ct : pCaLn.getOwnr().getTotals()) {
      if (!ct.getDisab() && (ct.getSelr() == null && pCaLn.getSelr() == null
        || ct.getSelr() != null && pCaLn.getSelr() != null
&& pCaLn.getSelr().getIid().getIid().equals(ct.getSelr().getIid().getIid()))) {
        cartTot = ct;
        break;
      }
    }
    if (totSe.compareTo(BigDecimal.ZERO) == 0 && cartTot != null) {
      //last seller's line has been deleted, disable enabled:
      cartTot.setDisab(true);
      getOrm().update(pRvs, vs, cartTot);
    } else if (totSe.compareTo(BigDecimal.ZERO) == 1) {
      if (cartTot == null) {
        for (CartTot ct : pCaLn.getOwnr().getTotals()) {
          if (ct.getDisab()) {
            cartTot = ct;
            cartTot.setDisab(false);
            break;
          }
        }
      }
      if (cartTot == null) {
        cartTot = new CartTot();
        cartTot.setOwnr(pCaLn.getOwnr());
        cartTot.setIsNew(true);
      }
      cartTot.setSelr(pCaLn.getSelr());
      cartTot.setToTx(toTxSe);
      cartTot.setSubt(totSe.subtract(toTxSe));
      cartTot.setTot(totSe);
      if (cartTot.getIsNew()) {
        getOrm().insIdLn(pRvs, vs, cartTot);
      } else {
        getOrm().update(pRvs, vs, cartTot);
      }
    }
  }

  /**
   * <p>Reveal shared tax rules for cart. It also makes buyer-regCustomer.</p>
   * @param pRvs request scoped vars
   * @param pCart cart
   * @param pAs Accounting Settings
   * @return tax rules, NULL if not taxable
   * @throws Exception - an exception.
   **/
  @Override
  public final TxDst revTxRules(final Map<String, Object> pRvs,
    final Cart pCart, final AcStg pAs) throws Exception {
    if (pCart.getBuyr().getCust() == null) {
      //copy buyer info into non-persistable customer.
      pCart.getBuyr().setCust(new DbCr());
      pCart.getBuyr().getCust().setZip(pCart.getBuyr().getZip());
      pCart.getBuyr().getCust().setTxDs(pCart.getBuyr().getTxDs());
    }
    TxDst txRules = null;
    if (pAs.getStExs() && !pCart.getBuyr().getFrgn()) {
      if (pCart.getBuyr().getCust().getTxDs() != null) {
        //override tax method:
        txRules = pCart.getBuyr().getCust().getTxDs();
      } else {
        txRules = new TxDst();
        txRules.setStIb(pAs.getStIb());
        txRules.setStAg(pAs.getStAg());
        txRules.setStRm(pAs.getStRm());
      }
    }
    return txRules;
  }

  /**
   * <p>Handle event cart currency changed.</p>
   * @param pRvs request scoped vars
   * @param pCart cart
   * @param pAs Accounting Settings
   * @param pTs TrdStg
   * @throws Exception - an exception.
   **/
  @Override
  public final void hndCurrChg(final Map<String, Object> pRvs, final Cart pCart,
    final AcStg pAs, final TrdStg pTs) throws Exception {
    TxDst txRules = revTxRules(pRvs, pCart, pAs);
    CartLn clf = null;
    for (CartLn cl : pCart.getItems()) {
      if (!cl.getDisab()) {
        if (cl.getForc()) {
          clf = cl;
          delLine(pRvs, cl, txRules);
        } else {
          mkLine(pRvs, cl, pAs, pTs, txRules, true, true);
          mkCartTots(pRvs, pTs, cl, pAs, txRules);
        }
      }
    }
    if (clf != null) {
      hndCartChg(pRvs, pCart, txRules);
    }
  }

  /**
   * <p>Deletes cart line.</p>
   * @param pRvs request scoped vars
   * @param pCaLn cart line
   * @param pTxRls Tax Rules
   * @throws Exception - an exception.
   **/
  @Override
  public final void delLine(final Map<String, Object> pRvs,
    final CartLn pCaLn, final TxDst pTxRls) throws Exception {
    String[] ndFds = new String[] {"ver", "disab"};
    Arrays.sort(ndFds);
    Map<String, Object> vs = new HashMap<String, Object>();
    vs.put("ndFds", ndFds);
    pCaLn.setDisab(true);
    getOrm().update(pRvs, vs, pCaLn);
    if (pTxRls != null && pCaLn.getTxCt() != null && !pTxRls.getStIb()
      && !pTxRls.getStAg()) {
      vs.put("CartLndpLv", 0);
      List<CartItTxLn> itls = getOrm().retLstCnd(pRvs, vs, CartItTxLn.class,
        "where DISAB=0 and OWNR=" + pCaLn.getIid());
      for (CartItTxLn itl : itls) {
        if (!itl.getDisab() && itl.getOwnr().getIid().equals(pCaLn.getIid())) {
          itl.setDisab(true);
          getOrm().update(pRvs, vs, itl);
        }
      }
    }
    vs.clear();
    AcStg as = (AcStg) pRvs.get("astg");
    TrdStg ts = (TrdStg) pRvs.get("tstg");
    mkCartTots(pRvs, ts, pCaLn, as, pTxRls);
  }

  /**
   * <p>Handle event cart delivering or line changed
   * and redone forced service if need.</p>
   * @param pRvs request scoped vars
   * @param pCart cart
   * @param pTxRls Tax Rules
   * @throws Exception - an exception.
   **/
  @Override
  public final void hndCartChg(final Map<String, Object> pRvs,
    final Cart pCart, final TxDst pTxRls) throws Exception {
    @SuppressWarnings("unchecked")
    List<Deliv> dlvMts = (List<Deliv>) pRvs.get("dlvMts");
    Deliv cdl = null;
    for (Deliv dl : dlvMts) {
      if (dl.getIid().equals(pCart.getDelv())) {
        cdl = dl;
        break;
      }
    }
    if (cdl == null) {
      throw new Exception("wrong delivering!");
    }
    //it must be at least one item to add forced service:
    boolean crtEmpty = true;
    CartLn clFrc = null;
    CartLn clEm = null;
    for (CartLn cl : pCart.getItems()) {
      if (cl.getDisab()) {
        clEm = cl;
      } else if (!cl.getDisab() && cl.getForc()) {
        clFrc = cl;
      } else if (!cl.getDisab() && !cl.getForc()) {
        crtEmpty = false;
      }
    }
    if (clFrc == null && cdl.getFrcSr() == null || cdl.getApMt() == null) {
      return;
    }
    if (crtEmpty) {
      if (clFrc != null) {
        delLine(pRvs, clFrc, pTxRls);
      }
      return;
    }
    int cartTot;
    AcStg as = (AcStg) pRvs.get("astg");
    TrdStg ts = (TrdStg) pRvs.get("tstg");
    BigDecimal ct = pCart.getTot();
    if (clFrc != null && clFrc.getTot().compareTo(BigDecimal.ZERO) == 1) {
      ct = ct.subtract(clFrc.getTot());
    }
    if (pCart.getExRt().compareTo(BigDecimal.ONE) == 0) {
      cartTot = ct.intValue();
    } else {
      cartTot = ct.divide(pCart.getExRt(), as.getPrDp(),
        as.getRndm()).intValue();
    }
    if (cartTot >= cdl.getApMt()) {
      if (clFrc != null && clFrc.getTot().compareTo(BigDecimal.ZERO) == 1) {
        clFrc.setPri(BigDecimal.ZERO);
        clFrc.setTot(BigDecimal.ZERO);
        clFrc.setToTx(BigDecimal.ZERO);
        clFrc.setSubt(BigDecimal.ZERO);
        clFrc.setTdsc(null);
        clFrc.setTxCt(null);
        Map<String, Object> vs = new HashMap<String, Object>();
        this.orm.update(pRvs, vs, clFrc);
        mkCartTots(pRvs, ts, clFrc, as, pTxRls);
      }
    } else {
      if (clFrc == null) {
        if (clEm == null) {
          clFrc = new CartLn();
          clFrc.setIsNew(true);
          clFrc.setOwnr(pCart);
          pCart.getItems().add(clFrc);
        } else {
          clFrc = clEm;
        }
        clFrc.setSelr(null);
        clFrc.setForc(true);
        clFrc.setDisab(false);
        clFrc.setItTyp(EItmTy.SERVICE);
        clFrc.setItId(cdl.getFrcSr().getIid());
        clFrc.setNme(cdl.getFrcSr().getNme());
        clFrc.setUom(cdl.getFrcSr().getDuom());
        clFrc.setAvQuan(BigDecimal.ONE);
        clFrc.setQuan(BigDecimal.ONE);
        clFrc.setUnSt(BigDecimal.ONE);
        mkLine(pRvs, clFrc, as, ts, pTxRls, true, true);
        mkCartTots(pRvs, ts, clFrc, as, pTxRls);
      } else if (clFrc.getTot().compareTo(BigDecimal.ZERO) == 0) {
        mkLine(pRvs, clFrc, as, ts, pTxRls, true, true);
        mkCartTots(pRvs, ts, clFrc, as, pTxRls);
      }
    }
  }

  /**
   * <p>Makes cart line.</p>
   * @param pRvs request scoped vars
   * @param pCaLn cart line
   * @param pAs Accounting Settings
   * @param pTs TrdStg
   * @param pTxRls NULL if not taxable
   * @param pRedoPr redo price
   * @param pRedoTxc redo tax category
   * @throws Exception - an exception.
   **/
  @Override
  public final void mkLine(final Map<String, Object> pRvs,
    final CartLn pCaLn, final AcStg pAs, final TrdStg pTs,
      final TxDst pTxRls, final boolean pRedoPr,
        final boolean pRedoTxc) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    AItmPri<?, ?> itPri = null;
    if (pRedoPr || pRedoTxc) {
      itPri = revItmPri(pRvs, pTs, pCaLn.getOwnr().getBuyr(),
        pCaLn.getItTyp(), pCaLn.getItId());
      if (pCaLn.getItTyp().equals(EItmTy.SESERVICE)
        || pCaLn.getItTyp().equals(EItmTy.SEGOODS)) {
        IHsSeSel seItPr = (IHsSeSel) itPri;
        pCaLn.setSelr(seItPr.getSelr());
      }
      pCaLn.setPri(itPri.getPri());
      pCaLn.setNme(itPri.getItm().getNme());
      BigDecimal qosr = pCaLn.getQuan().remainder(itPri.getUnSt());
      if (qosr.compareTo(BigDecimal.ZERO) != 0) {
        pCaLn.setQuan(pCaLn.getQuan().subtract(qosr));
      }
      @SuppressWarnings("unchecked")
      List<CurrRt> currRts = (List<CurrRt>) pRvs.get("currRts");
      for (CurrRt cr: currRts) {
        if (cr.getCurr().getIid().equals(pCaLn.getOwnr()
          .getCurr().getIid())) {
          BigDecimal exchRate = cr.getExRt();
          if (exchRate.compareTo(BigDecimal.ZERO) == -1) {
            exchRate = BigDecimal.ONE.divide(exchRate.negate(), 15,
              RoundingMode.HALF_UP);
          }
          pCaLn.getOwnr().setExRt(exchRate);
          pCaLn.setPri(pCaLn.getPri().multiply(exchRate)
            .setScale(pAs.getPrDp(), pAs.getRndm()));
          break;
        }
      }
      BigDecimal amount = pCaLn.getPri().multiply(pCaLn.getQuan()).
        setScale(pAs.getPrDp(), pAs.getRndm());
      if (pTs.getTxExcl()) {
        pCaLn.setSubt(amount);
      } else {
        pCaLn.setTot(amount);
      }
    }
    if (pRedoTxc && pTxRls != null) {
      AItm<?, ?> item = (AItm<?, ?>) itPri.getItm();
      pCaLn.setTxCt(item.getTxCt());
      if (pTs.getUtxds() && pCaLn.getOwnr().getBuyr()
        .getCust().getTxDs() != null) {
        Class<? extends ATxDsLn<?>> dstTxItLnCl;
        if (pCaLn.getItTyp().equals(EItmTy.GOODS)) {
          dstTxItLnCl = ItTxDl.class;
        } else if (pCaLn.getItTyp().equals(EItmTy.SERVICE)) {
          dstTxItLnCl = SrTxDl.class;
        } else if (pCaLn.getItTyp().equals(EItmTy.SESERVICE)) {
          dstTxItLnCl = SerTxDl.class;
        } else {
          dstTxItLnCl = SitTxDl.class;
        }
        //override tax method:
        vs.put(item.getClass().getSimpleName() + "dpLv", 1);
        @SuppressWarnings("unchecked")
        List<ATxDsLn<?>> dtls = (List<ATxDsLn<?>>) getOrm()
          .retLstCnd(pRvs, vs, dstTxItLnCl, "where OWNR=" + pCaLn.getItId());
        vs.clear();
        for (ATxDsLn<?> dtl : dtls) {
          if (dtl.getTxDs().getIid().equals(pCaLn.getOwnr()
            .getBuyr().getCust().getTxDs().getIid())) {
            pCaLn.setTxCt(dtl.getTxCt()); //it may be null
            break;
          }
        }
      }
    }
    BigDecimal toTxs = BigDecimal.ZERO;
    List<CartItTxLn> itls = null;
    UsPrf upf = (UsPrf) pRvs.get("upf");
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    if (pTxRls != null && pCaLn.getTxCt() != null) {
      if (!pTxRls.getStIb()) {
        BigDecimal bd100 = new BigDecimal("100.00");
        if (!pTxRls.getStAg()) {
          itls = new ArrayList<CartItTxLn>();
          vs.put("TxCtdpLv", 1);
          List<TxCtLn> itcls = getOrm().retLstCnd(pRvs, vs, TxCtLn.class,
            "where OWNR=" + pCaLn.getTxCt().getIid() + " order by TXCTLN.RATE");
          vs.clear();
          BigDecimal taxTot = null;
          BigDecimal taxRest = null;
          if (!pTs.getTxExcl()) {
            taxTot = pCaLn.getTot().subtract(pCaLn.getTot().divide(BigDecimal
              .ONE.add(pCaLn.getTxCt().getAgRt().divide(bd100)),
                pAs.getPrDp(), pTxRls.getStRm()));
            taxRest = taxTot;
          }
          StringBuffer sb = new StringBuffer();
          int i = 0;
          for (TxCtLn itcl : itcls) {
           if (ETxTy.TSALES.equals(itcl.getTax().getTyp())) {
              if (i++ > 0) {
                sb.append(", ");
              }
              CartItTxLn itl = new CartItTxLn();
              itl.setIsNew(true);
              itl.setTax(itcl.getTax());
              itls.add(itl);
              BigDecimal addTx;
              if (!pTs.getTxExcl()) {
                if (i < itcls.size()) {
                  addTx = taxTot.multiply(itcl.getRate()).divide(pCaLn.getTxCt()
                    .getAgRt(), pAs.getPrDp(), pTxRls.getStRm());
                  taxRest = taxRest.subtract(addTx);
                } else {
                  addTx = taxRest;
                }
              } else {
                addTx = pCaLn.getSubt().multiply(itcl.getRate())
                  .divide(bd100, pAs.getPrDp(), pTxRls.getStRm());
              }
              toTxs = toTxs.add(addTx);
              itl.setTot(addTx);
             sb.append(itl.getTax().getNme() + " " + prn(pAs, cpf, upf, addTx));
            }
          }
          pCaLn.setTdsc(sb.toString());
        } else {
          if (!pTs.getTxExcl()) {
            toTxs = pCaLn.getTot().subtract(pCaLn.getTot().divide(BigDecimal.ONE
              .add(pCaLn.getTxCt().getAgRt().divide(bd100)),
                pAs.getPrDp(), pTxRls.getStRm()));
          } else {
            toTxs = pCaLn.getSubt().multiply(pCaLn.getTxCt()
              .getAgRt()).divide(bd100, pAs.getPrDp(), pTxRls.getStRm());
          }
          pCaLn.setTdsc(pCaLn.getTxCt().getNme());
        }
      } else {
        pCaLn.setTdsc(pCaLn.getTxCt().getNme());
      }
    }
    pCaLn.setToTx(toTxs);
    if (pTs.getTxExcl()) {
      pCaLn.setTot(pCaLn.getSubt().add(pCaLn.getToTx()));
    } else {
      pCaLn.setSubt(pCaLn.getTot().subtract(pCaLn.getToTx()));
    }
    if (pCaLn.getIsNew()) {
      getOrm().insIdLn(pRvs, vs, pCaLn);
    } else {
      getOrm().update(pRvs, vs, pCaLn);
    }
    if (itls != null) {
      vs.put("CartLndpLv", 0);
      List<CartItTxLn> itlsr = getOrm().retLstCnd(pRvs, vs, CartItTxLn.class,
        "where CARTID=" + pCaLn.getOwnr().getBuyr().getIid()); vs.clear();
      for (CartItTxLn itlrt : itlsr) {
        if (!itlrt.getDisab() && itlrt.getOwnr().getIid()
          .equals(pCaLn.getIid())) {
          itlrt.setDisab(true);
        }
      }
      for (CartItTxLn itl : itls) {
        CartItTxLn itlr = null;
        for (CartItTxLn itlrt : itlsr) {
          if (itlrt.getDisab()) {
            itlr = itlrt;
            itlr.setDisab(false);
            break;
          }
        }
        if (itlr == null) {
          itl.setOwnr(pCaLn);
          if (pCaLn.getSelr() != null) {
            itl.setSelId(pCaLn.getSelr().getIid().getIid());
          }
          itl.setCartId(pCaLn.getOwnr().getBuyr().getIid());
          itl.setOwnr(pCaLn);
          getOrm().insIdLn(pRvs, vs, itl);
        } else {
          itlr.setTax(itl.getTax());
          itlr.setTot(itl.getTot());
          itlr.setOwnr(pCaLn);
          if (pCaLn.getSelr() == null) {
            itlr.setSelId(null);
          } else {
            itlr.setSelId(pCaLn.getSelr().getIid().getIid());
          }
          itlr.setCartId(pCaLn.getOwnr().getBuyr().getIid());
          getOrm().update(pRvs, vs, itlr);
        }
      }
      String[] ndFds = new String[] {"ver", "disab"};
      Arrays.sort(ndFds);
      vs.put("ndFds", ndFds);
      for (CartItTxLn itlrt : itlsr) {
        if (itlrt.getDisab() && itlrt.getOwnr().getIid()
          .equals(pCaLn.getIid())) {
          getOrm().update(pRvs, vs, itlrt);
        }
      }
    }
  }

  /**
   * <p>Reveals item's price descriptor.</p>
   * @param pRvs request scoped vars
   * @param pTs TrdStg
   * @param pBuyr Buyer
   * @param pItType Item Type
   * @param pItId Item ID
   * @return item's price descriptor or exception
   * @throws Exception - an exception
   **/
  @Override
  public final AItmPri<?, ?> revItmPri(final Map<String, Object> pRvs,
    final TrdStg pTs, final Buyer pBuyr, final EItmTy pItType,
      final Long pItId) throws Exception {
    UsPrf upf = (UsPrf) pRvs.get("upf");
    String lang = upf.getLng().getIid();
    AItmPri<?, ?> itPri = null;
    String query;
    Class<?> itemI18Cl;
    Class<?> itemCl;
    Class<? extends AItmPri<?, ?>> itemPriceCl;
    if (pItType.equals(EItmTy.GOODS)) {
      itemCl = Itm.class;
      itemI18Cl = I18Itm.class;
      itemPriceCl = PriItm.class;
    } else if (pItType.equals(EItmTy.SERVICE)) {
      itemCl = Srv.class;
      itemI18Cl = I18Srv.class;
      itemPriceCl = PriSrv.class;
    } else if (pItType.equals(EItmTy.SESERVICE)) {
      itemCl = SeSrv.class;
      itemI18Cl = I18SeSrv.class;
      itemPriceCl = SeSrvPri.class;
    } else {
      itemCl = SeItm.class;
      itemI18Cl = I18SeItm.class;
      itemPriceCl = SeItmPri.class;
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    String[] ndFlItPr = new String[] {"pri", "unSt"};
    Arrays.sort(ndFlItPr);
    vs.put(itemPriceCl.getSimpleName() + "ndFds", ndFlItPr);
    vs.put("PriCtdpLv", 0);
    String[] ndFlIt;
    if (pItType.equals(EItmTy.SEGOODS) || pItType.equals(EItmTy.SESERVICE)) {
      ndFlIt = new String[] {"nme", "txCt", "selr"};
      vs.put("SeSelndFds", new String[] {"dbcr"});
      vs.put("DbCrndFds", new String[] {"nme"});
      vs.put("SeSeldpLv", 3);
    } else {
      ndFlIt = new String[] {"nme", "txCt"};
    }
    Arrays.sort(ndFlIt);
    vs.put(itemCl.getSimpleName() + "ndFds", ndFlIt);
    String[] ndFlTc = new String[] {"nme", "agRt"};
    Arrays.sort(ndFlTc);
    vs.put("TxCtndFds", ndFlTc);
    vs.put(itemCl.getSimpleName() + "dpLv", 3);
    if (pTs.getPriCus() && pBuyr != null && !pBuyr.getIsNew()) {
      //try to reveal price dedicated to customer:
      List<BurPric> buyerPrCats = getOrm()
        .retLstCnd(pRvs, vs, BurPric.class, "where BUYR=" + pBuyr.getIid());
      if (buyerPrCats.size() > 1) {
        this.log.error(pRvs, SrCart.class,
          "Several price category for same buyer! buyer ID=" + pBuyr.getIid());
        throw new ExcCode(ExcCode.WRCN,
          "several_price_category_for_same_buyer");
      }
      if (buyerPrCats.size() == 1) {
        if (pItType.equals(EItmTy.GOODS) || pItType.equals(EItmTy.SERVICE)) {
          query = lazQuItemPriceCat();
        } else {
          query = lazQuItemSePriceCat();
        }
        query = query.replace(":ITMID", pItId.toString());
        query = query.replace(":LNG", lang);
        query = query.replace(":TITMPRI", itemPriceCl.getSimpleName()
          .toUpperCase());
        query = query.replace(":TITM", itemCl.getSimpleName().toUpperCase());
        query = query.replace(":TI18ITM", itemI18Cl.getSimpleName()
          .toUpperCase());
        StringBuffer pccnd = new StringBuffer("");
        pccnd.append("=" + buyerPrCats.get(0).getPriCt().getIid());
        query = query.replace(":PRCATIDCOND", pccnd);
        itPri = (AItmPri<?, ?>) getOrm().retEntQu(pRvs, vs, itemPriceCl, query);
      }
    }
    vs.clear();
    if (itPri == null) {
      //retrieve price for all:
      if (pItType.equals(EItmTy.GOODS) || pItType.equals(EItmTy.SERVICE)) {
        query = lazQuItemPrice();
      } else {
        query = lazQuItemSePrice();
      }
      query = query.replace(":ITMID", pItId.toString());
      query = query.replace(":LNG", lang);
      query = query.replace(":TITMPRI", itemPriceCl.getSimpleName()
        .toUpperCase());
      query = query.replace(":TITM", itemCl.getSimpleName().toUpperCase());
      query = query.replace(":TI18ITM", itemI18Cl.getSimpleName()
        .toUpperCase());
      @SuppressWarnings("unchecked")
      List<AItmPri<?, ?>> itPris = (List<AItmPri<?, ?>>)
        getOrm().retLstQu(pRvs, vs, itemPriceCl, query);
      if (itPris.size() == 0) {
        throw new ExcCode(ExcCode.WR, "requested item has no price");
      }
      if (itPris.size() > 1) {
        throw new ExcCode(ExcCode.WR, "requested item has several prices");
      }
      itPri = itPris.get(0);
    }
    return itPri;
  }

  /**
   * <p>Empties Cart.</p>
   * @param pRvs request scoped vars
   * @param pBuyr buyer
   * @throws Exception - an exception
   **/
  @Override
  public final void emptyCart(final Map<String, Object> pRvs,
    final Buyer pBuyr) throws Exception {
    Cart cart = retrCart(pRvs, pBuyr, true);
    if (cart != null) {
      emptyCart(pRvs, cart);
    }
  }

  /**
   * <p>Empties Cart.</p>
   * @param pRvs request scoped vars
   * @param pCart cart
   * @throws Exception - an exception
   **/
  public final void emptyCart(final Map<String, Object> pRvs,
    final Cart pCart) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    String[] ndFds = new String[] {"ver", "disab"};
    Arrays.sort(ndFds);
    vs.put("ndFds", ndFds);
    for (CartLn l : pCart.getItems()) {
      if (!l.getDisab()) {
        l.setDisab(true);
        getOrm().update(pRvs, vs, l);
      }
    }
    for (CartTxLn l : pCart.getTaxes()) {
      if (!l.getDisab()) {
        l.setDisab(true);
        getOrm().update(pRvs, vs, l);
      }
    }
    for (CartTot l : pCart.getTotals()) {
      if (!l.getDisab()) {
        l.setDisab(true);
        getOrm().update(pRvs, vs, l);
      }
    }
    if (pCart.getTot().compareTo(BigDecimal.ZERO) == 1) {
      vs.clear();
      ndFds = new String[] {"ver", "tot"};
      Arrays.sort(ndFds);
      vs.put("ndFds", ndFds);
      pCart.setTot(BigDecimal.ZERO);
      getOrm().update(pRvs, vs, pCart);
    }
  }

  /**
   * <p>Retrieves Cart from DB.</p>
   * @param pRvs additional param
   * @param pBuyr buyer
   * @param pForEmpty for emptying
   * @return shopping cart or null
   * @throws Exception - an exception
   **/
  public final Cart retrCart(final Map<String, Object> pRvs,
    final Buyer pBuyr, final boolean pForEmpty) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    Cart cart = new Cart();
    cart.setIid(pBuyr);
    getOrm().refrEnt(pRvs, vs, cart);
    if (cart.getIid() != null) {
      String[] ndFlDe = new String[] {"ver"};
      String[] ndFlNm = new String[] {"nme"};
      String[] ndFlDc = new String[] {"selr"};
      if (!pForEmpty) {
        vs.put("UomndFds", ndFlNm);
        vs.put("DbCrndFds", ndFlNm);
        vs.put("SeSelndFds", ndFlDc);
        vs.put("TxCtdpLv", 1);
        vs.put("SeSeldpLv", 3);
        vs.put("CartdpLv", 1);
      } else {
        vs.put("CartLnndFds", ndFlDe);
      }
      cart.setItems(getOrm().retLstCnd(pRvs, vs, CartLn.class,
        "where OWNR=" + cart.getBuyr().getIid())); vs.clear();
      for (CartLn clt : cart.getItems()) {
        clt.setOwnr(cart);
      }
      if (!pForEmpty) {
        vs.put("TaxndFds", ndFlNm);
        vs.put("SeSeldpLv", 4);
        vs.put("CartdpLv", 1);
      } else {
        vs.put("CartTxLnndFds", ndFlDe);
      }
      cart.setTaxes(getOrm().retLstCnd(pRvs, vs, CartTxLn.class,
        "where OWNR=" + cart.getBuyr().getIid())); vs.clear();
      for (CartTxLn ctl : cart.getTaxes()) {
        ctl.setOwnr(cart);
      }
      if (!pForEmpty) {
        vs.put("SeSeldpLv", 4);
        vs.put("CartdpLv", 1);
      } else {
        vs.put("CartTotndFds", ndFlDe);
      }
      cart.setTotals(getOrm().retLstCnd(pRvs, vs, CartTot.class,
        "where OWNR=" + cart.getBuyr().getIid())); vs.clear();
      for (CartTot cttl : cart.getTotals()) {
        cttl.setOwnr(cart);
      }
      cart.setBuyr(pBuyr);
    } else {
      cart = null;
    }
    return cart;
  }

  /**
   * <p>Make cart line that stores taxes data in lines set
   * for invoice basis or item basis aggregate rate.</p>
   * @param pTxdLns TD lines
   * @param pTdlId line ID
   * @param pCatId tax category ID
   * @param pTax tax
   * @param pRate tax rate
   * @param pAs AS
   * @return line
   **/
  public final CartLn mkTxdLn(final List<CartLn> pTxdLns, final Long pTdlId,
    final Long pCatId,  final Tax pTax, final Double pRate, final AcStg pAs) {
    CartLn txdLn = null;
    for (CartLn tdl : pTxdLns) {
      if (tdl.getIid().equals(pTdlId)) {
        txdLn = tdl;
      }
    }
    if (txdLn == null) {
      txdLn = new CartLn();
      txdLn.setIid(pTdlId);
      TxCt tc = new TxCt();
      tc.setIid(pCatId);
      tc.setTxs(new ArrayList<TxCtLn>());
      txdLn.setTxCt(tc);
      pTxdLns.add(txdLn);
    }
    TxCtLn itcl = new TxCtLn();
    itcl.setTax(pTax);
    itcl.setRate(BigDecimal.valueOf(pRate)
      .setScale(pAs.getTxDp(), RoundingMode.HALF_UP));
    txdLn.getTxCt().getTxs().add(itcl);
    txdLn.getTxCt().setAgRt(txdLn.getTxCt().getAgRt()
      .add(itcl.getRate()));
    return txdLn;
  }

  /**
   * <p>Finds (if need) enabled line with same tax and seller or any
   * disabled tax line or creates one.</p>
   * @param pRvs additional param
   * @param pCart cart
   * @param pTax tax
   * @param pSeller seller
   * @param pNeedFind if need to find enabled
   * @return line
   * @throws Exception if no need to find but line is found
   **/
  public final CartTxLn fndCreTxLn(final Map<String, Object> pRvs,
    final Cart pCart, final Tax pTax, final SeSel pSeller,
      final boolean pNeedFind) throws Exception {
    CartTxLn ctl = null;
    //find enabled line to add amount
    for (CartTxLn tl : pCart.getTaxes()) {
      if (!tl.getDisab() && tl.getTax().getIid().equals(pTax.getIid())
        && (pSeller == null && tl.getSelr() == null
          || pSeller != null && tl.getSelr() != null && pSeller.getIid()
            .getIid().equals(tl.getSelr().getIid().getIid()))) {
        if (!pNeedFind) {
          throw new Exception("Algorithm error!!!");
        }
        ctl = tl;
        break;
      }
    }
    if (ctl == null) {
      //find disabled line to initialize new tax
      for (CartTxLn tl : pCart.getTaxes()) {
        if (tl.getDisab()) {
          ctl = tl;
          ctl.setDisab(false);
          ctl.setTot(BigDecimal.ZERO);
          ctl.setTxb(BigDecimal.ZERO);
          ctl.setTax(pTax);
          ctl.setSelr(pSeller);
          break;
        }
      }
    }
    if (ctl == null) {
      ctl = new CartTxLn();
      ctl.setOwnr(pCart);
      ctl.setIsNew(true);
      ctl.setTax(pTax);
      ctl.setSelr(pSeller);
      pCart.getTaxes().add(ctl);
    }
    return ctl;
  }

  /**
   * <p>Create Buyer.</p>
   * @param pRvs additional param
   * @param pRqDt Request Data
   * @return buyer
   * @throws Exception - an exception
   **/
  public final Buyer createBuyer(
    final Map<String, Object> pRvs,
      final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    Buyer buyer = null;
    List<Buyer> brs = getOrm().retLstCnd(pRvs, vs, Buyer.class,
      "where FRE=1 and PWD is null");
    if (brs.size() > 0) {
      double rd = Math.random();
      if (rd > 0.5) {
        buyer = brs.get(brs.size() - 1);
      } else {
        buyer = brs.get(0);
      }
      buyer.setFre(false);
      getOrm().update(pRvs, vs, buyer);
    }
    if (buyer == null) {
      buyer = new Buyer();
      buyer.setIsNew(true);
      buyer.setNme("newbe" + new Date().getTime());
      getOrm().insIdLn(pRvs, vs, buyer);
    }
    return buyer;
  }

  /**
   * <p>Lazy Getter for quTxInvBas.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  public final String lazQuTxInvBas() throws IOException {
    if (this.quTxInvBas == null) {
      this.quTxInvBas = loadString("/ws/cartTxInvBas.sql");
    }
    return this.quTxInvBas;
  }

  /**
   * <p>Lazy Getter for quTxInvBasAggr.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  public final String lazQuTxInvBasAggr() throws IOException {
    if (this.quTxInvBasAggr == null) {
      this.quTxInvBasAggr = loadString("/ws/cartTxInvBasAggr.sql");
    }
    return this.quTxInvBasAggr;
  }

  /**
   * <p>Lazy Getter for quTxItBas.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  public final String lazQuTxItBas() throws IOException {
    if (this.quTxItBas == null) {
      this.quTxItBas = loadString("/ws/cartTxItBas.sql");
    }
    return this.quTxItBas;
  }

  /**
   * <p>Lazy Getter for quTxItBasAggr.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  public final String lazQuTxItBasAggr() throws IOException {
    if (this.quTxItBasAggr == null) {
      this.quTxItBasAggr = loadString("/ws/cartTxItBasAggr.sql");
    }
    return this.quTxItBasAggr;
  }

  /**
   * <p>Lazy Getter for quItemPrice.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  public final String lazQuItemPrice() throws IOException {
    if (this.quItemPrice == null) {
      this.quItemPrice = loadString("/ws/itemPrice.sql");
    }
    return this.quItemPrice;
  }

  /**
   * <p>Lazy Getter for quItemSePrice.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  public final String lazQuItemSePrice() throws IOException {
    if (this.quItemSePrice == null) {
      this.quItemSePrice = loadString("/ws/itemSePrice.sql");
    }
    return this.quItemSePrice;
  }

  /**
   * <p>Lazy Getter for quItemPriceCat.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  public final String lazQuItemPriceCat() throws IOException {
    if (this.quItemPriceCat == null) {
      this.quItemPriceCat = loadString("/ws/itemPriceCat.sql");
    }
    return this.quItemPriceCat;
  }

  /**
   * <p>Lazy Getter for quItemSePriceCat.</p>
   * @return String
   * @throws IOException - IO exception
   **/
  public final String lazQuItemSePriceCat() throws IOException {
    if (this.quItemSePriceCat == null) {
      this.quItemSePriceCat = loadString("/ws/itemSePriceCat.sql");
    }
    return this.quItemSePriceCat;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFiNm file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final String loadString(final String pFiNm) throws IOException {
    URL urlFile = SrCart.class.getResource(pFiNm);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = SrCart.class.getResourceAsStream(pFiNm);
        byte[] bArray = new byte[inputStream.available()];
        inputStream.read(bArray, 0, inputStream.available());
        return new String(bArray, "UTF-8");
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }
      }
    }
    return null;
  }

  /**
   * <p>Simple delegator to print number.</p>
   * @param pAs ACC stg
   * @param pCpf common prefs
   * @param pUpf user prefs
   * @param pVal value
   * @return String
   **/
  public final String prn(final AcStg pAs, final CmnPrf pCpf, final UsPrf pUpf,
    final BigDecimal pVal) {
    return this.numStr.frmt(pVal.toString(), pCpf.getDcSpv(),
      pCpf.getDcGrSpv(), pAs.getPrDp(), pUpf.getDgInGr());
  }

  //Simple getters and setters:
  /**
   * <p>Getter for numStr.</p>
   * @return INumStr
   **/
  public final INumStr getNumStr() {
    return this.numStr;
  }

  /**
   * <p>Setter for numStr.</p>
   * @param pNumStr reference
   **/
  public final void setNumStr(final INumStr pNumStr) {
    this.numStr = pNumStr;
  }

  /**
   * <p>Geter for log.</p>
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
   * <p>Getter for rdb.</p>
   * @return IRdb<RS>
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
   * <p>Getter for srTrStg.</p>
   * @return ISrTrStg
   **/
  public final ISrTrStg getSrTrStg() {
    return this.srTrStg;
  }

  /**
   * <p>Setter for srTrStg.</p>
   * @param pSrTrStg reference
   **/
  public final void setSrTrStg(final ISrTrStg pSrTrStg) {
    this.srTrStg = pSrTrStg;
  }

  /**
   * <p>Getter for pplCl.</p>
   * @return Class<?>
   **/
  public final Class<?> getPplCl() {
    return this.pplCl;
  }

  /**
   * <p>Setter for pplCl.</p>
   * @param pPplCl reference
   **/
  public final void setPplCl(final Class<?> pPplCl) {
    this.pplCl = pPplCl;
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
}
