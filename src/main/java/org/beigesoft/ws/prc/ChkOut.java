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
import java.util.ArrayList;
import java.util.Date;
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.log.ILog;
import org.beigesoft.prc.IPrc;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.TxDst;
import org.beigesoft.acc.mdlp.Itm;
import org.beigesoft.acc.mdlp.Srv;
import org.beigesoft.acc.mdlp.Tax;
import org.beigesoft.ws.mdl.EItmTy;
import org.beigesoft.ws.mdl.EOrdStat;
import org.beigesoft.ws.mdl.EPaymMth;
import org.beigesoft.ws.mdl.Purch;
import org.beigesoft.ws.mdlb.AItmPlc;
import org.beigesoft.ws.mdlb.ACuOrLn;
import org.beigesoft.ws.mdlb.ACuOrSeLn;
import org.beigesoft.ws.mdlp.Cart;
import org.beigesoft.ws.mdlp.CartLn;
import org.beigesoft.ws.mdlp.CartTxLn;
import org.beigesoft.ws.mdlp.CartItTxLn;
import org.beigesoft.ws.mdlp.ItmPlc;
import org.beigesoft.ws.mdlp.SeItmPlc;
import org.beigesoft.ws.mdlp.SrvPlc;
import org.beigesoft.ws.mdlp.SeSrvPlc;
import org.beigesoft.ws.mdlp.CuOrSe;
import org.beigesoft.ws.mdlp.CuOrSeTxLn;
import org.beigesoft.ws.mdlp.CuOrSeSrLn;
import org.beigesoft.ws.mdlp.CuOrSeGdLn;
import org.beigesoft.ws.mdlp.CuOrSeGdTxLn;
import org.beigesoft.ws.mdlp.CuOrSeSrTxLn;
import org.beigesoft.ws.mdlp.CuOr;
import org.beigesoft.ws.mdlp.CuOrTxLn;
import org.beigesoft.ws.mdlp.CuOrSrLn;
import org.beigesoft.ws.mdlp.CuOrGdLn;
import org.beigesoft.ws.mdlp.CuOrGdTxLn;
import org.beigesoft.ws.mdlp.CuOrSrTxLn;
import org.beigesoft.ws.mdlp.PayMd;
import org.beigesoft.ws.mdlp.SerBus;
import org.beigesoft.ws.mdlp.SeSerBus;
import org.beigesoft.ws.mdlp.TrdStg;
import org.beigesoft.ws.mdlp.Buyer;
import org.beigesoft.ws.mdlp.SeItm;
import org.beigesoft.ws.mdlp.SeSrv;
import org.beigesoft.ws.mdlp.SeSel;
import org.beigesoft.ws.fct.FcPrWs;
import org.beigesoft.ws.srv.ISrCart;

/**
 * <p>Service that checkouts cart phase#1, i.e. creates orders with status NEW
 * from cart. Next phase#2 is accepting these orders (booking bookable items)
 * and making payments (if there is order with any online payment method).</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class ChkOut<RS> implements IPrc {

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
   * <p>Process entity request.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    boolean isCompl = true;
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      Cart cart = this.srCart.getCart(pRvs, pRqDt, false, true);
      if (cart == null) {
        isCompl = false;
        this.log.warn(pRvs, getClass(), "Trying to checkout without cart!");
      } else {
        Map<String, Object> vs = new HashMap<String, Object>();
        if (EPaymMth.PAYPAL.equals(cart.getPaym())) {
          //TODO partially online, SE
          List<PayMd> payMds = this.orm.retLstCnd(pRvs, vs, PayMd.class,
            "where NME='PAYPAL'");
          if (payMds.size() != 1) {
            throw new Exception("There is no properly PPL PayMd");
          } else {
            pRvs.put("pmde", payMds.get(0).getMde());
          }
        }
        TrdStg ts = (TrdStg) pRvs.get("tstg");
        AcStg as = (AcStg) pRvs.get("astg");
        TxDst txRules = this.srCart.revTxRules(pRvs, cart, as);
        //redo prices and taxes:
        for (CartLn cl : cart.getItems()) {
          if (!cl.getDisab() && !cl.getForc()) {
            this.srCart.mkLine(pRvs, cl, as, ts, txRules, true, true);
            this.srCart.mkCartTots(pRvs, ts, cl, as, txRules);
          }
        }
        Purch pur = retNewOrds(pRvs, cart.getBuyr());
        String cond;
        for (CartLn cl : cart.getItems()) {
          if (cl.getDisab()) {
            continue;
          }
          Class<?> itCl;
          Class<? extends AItmPlc<?, ?>> itPlCl;
          String serBus = null;
          if (cl.getItTyp().equals(EItmTy.GOODS)) {
            itCl = Itm.class;
            itPlCl = ItmPlc.class;
          } else if (cl.getItTyp().equals(EItmTy.SERVICE)) {
            itCl = Srv.class;
            itPlCl = SrvPlc.class;
            serBus = SerBus.class.getSimpleName();
          } else if (cl.getItTyp().equals(EItmTy.SESERVICE)) {
            itCl = SeSrv.class;
            itPlCl = SeSrvPlc.class;
            serBus = SeSerBus.class.getSimpleName();
          } else {
            itCl = SeItm.class;
            itPlCl = SeItmPlc.class;
          }
          if (serBus == null || cl.getDt1() == null) {
            // good or non-bookable service
            cond = "where QUAN>0 and ITM=" + cl.getItId();
          } else {
          cond = "left join (select distinct SRV from " + serBus + " where SRV="
          + cl.getItId() + " and FRTM>=" + cl.getDt1().getTime() + " and TITM<"
        + cl.getDt2().getTime() + ") as " + serBus + " on " + serBus + ".SRV="
      + itPlCl.getSimpleName() + ".ITM where ITM=" + cl.getItId()
    + " and QUAN>0 and " + serBus + ".SRV is null";
          }
          vs.put("PicPlcndFds", new String[] {"nme"});
          vs.put(itCl.getSimpleName() + "dpLv", 0); //only ID
          List<? extends AItmPlc<?, ?>> places = getOrm()
            .retLstCnd(pRvs, vs, itPlCl, cond); vs.clear();
          if (places.size() > 1 && serBus != null && cl.getDt1() != null) {
            //for bookable service it's ambiguous - same service (e.g. appointme
            //to DR.Jonson) is available at same time in two different places)
            //for non-bookable service, e.g. for delivering place means
            //starting-point, but it should be selected automatically TODO
            //for goods: TODO
            //1. buyer chooses place (in filter), so use this place
            //2. buyer will pickups by yourself from different places,
            // but it must chooses them (in filter) in this case.
            // As a result places should ordered by itsQuanity and removed items
            // that are out of filter.
            isCompl = false;
            String errs = "!Wrong places for item name/ID/type: " + cl.getNme()
              + "/" + cl.getItId() + "/" + cl.getItTyp();
            if (cart.getDscr() == null) {
              cart.setDscr(errs);
            } else {
              cart.setDscr(cart.getDscr() + errs);
            }
            cart.setErr(true);
            getOrm().update(pRvs, vs, cart);
            this.log.warn(pRvs, ChkOut.class, errs);
            break;
          } else { //only/multiply place/s with non-zero availability
            BigDecimal avQu = BigDecimal.ZERO;
            for (AItmPlc<?, ?> pl : places) {
              avQu = avQu.add(pl.getQuan());
            }
            if (avQu.compareTo(cl.getQuan()) == -1) {
              isCompl = false;
              cl.setAvQuan(avQu);
              String errs = "!Not available item name/ID/type/quant/avail: "
                + cl.getNme() + "/" + cl.getItId() + "/" + cl.getItTyp()
                  + "/" + cl.getQuan() + "/" + avQu;
              this.log.warn(pRvs, ChkOut.class, errs);
            }
          }
          if (isCompl && cl.getPri().compareTo(BigDecimal.ZERO) == 1) {
            //without free delivering:
            if (cl.getSelr() == null) {
              makeOrdLn(pRvs, pur.getOrds(), null, cl, ts);
            } else {
              makeSeOrdLn(pRvs, pur.getSords(), cl.getSelr(), null, cl, ts);
            }
          } else {
            getOrm().update(pRvs, vs, cl);
          }
        }
        pRvs.put("cart", cart);
        pRqDt.setAttr("rnd", "wchou");
        if (txRules != null) {
          pRvs.put("txRules", txRules);
        }
        if (isCompl) {
          saveOrds(pRvs, pur, cart);
          saveSords(pRvs, pur, cart);
          pRvs.put("orders", pur.getOrds());
          pRvs.put("sorders", pur.getSords());
        }
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
    if (!isCompl) {
      redir(pRvs, pRqDt);
    }
  }

  /**
   * <p>Saves S.E. orders.</p>
   * @param pRvs request scoped vars
   * @param pPur purchase
   * @param pCart cart
   * @throws Exception - an exception
   **/
  public final void saveSords(final Map<String, Object> pRvs,
    final Purch pPur, final Cart pCart) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    List<CuOrSe> dels = null;
    for (CuOrSe co : pPur.getSords()) {
      if (co.getCurr() == null) { //stored unused order
        //remove it and all its lines:
        for (CuOrSeGdLn gl : co.getGoods()) {
          for (CuOrSeGdTxLn gtl : gl.getItTxs()) {
            getOrm().del(pRvs, vs, gtl);
          }
          getOrm().del(pRvs, vs, gl);
        }
        for (CuOrSeSrLn sl : co.getServs()) {
          for (CuOrSeSrTxLn stl : sl.getItTxs()) {
            getOrm().del(pRvs, vs, stl);
          }
          getOrm().del(pRvs, vs, sl);
        }
        for (CuOrSeTxLn otlt : co.getTaxes()) {
          getOrm().del(pRvs, vs, otlt);
        }
        getOrm().del(pRvs, vs, co);
        if (dels == null) {
          dels = new ArrayList<CuOrSe>();
        }
        dels.add(co);
        continue;
      }
      if (co.getIsNew()) {
        getOrm().insIdLn(pRvs, vs, co);
      }
      BigDecimal tot = BigDecimal.ZERO;
      BigDecimal subt = BigDecimal.ZERO;
      List<CuOrSeGdLn> delsGd = null;
      for (CuOrSeGdLn gl : co.getGoods()) {
        gl.setOwnr(co);
        if (gl.getIsNew()) {
          getOrm().insIdLn(pRvs, vs, gl);
        }
        if (gl.getItTxs() != null && gl.getItTxs().size() > 0) {
          for (CuOrSeGdTxLn gtl : gl.getItTxs()) {
            gtl.setOwnr(gl);
            if (gtl.getIsNew()) {
              getOrm().insIdLn(pRvs, vs, gtl);
            } else if (gl.getGood() == null || gtl.getTax() == null) {
              getOrm().del(pRvs, vs, gtl);
            } else {
              getOrm().update(pRvs, vs, gtl);
            }
          }
        }
        if (!gl.getIsNew() && gl.getGood() == null) {
          getOrm().del(pRvs, vs, gl);
          if (delsGd == null) {
            delsGd = new ArrayList<CuOrSeGdLn>();
          }
          delsGd.add(gl);
        } else {
          tot = tot.add(gl.getTot());
          subt = subt.add(gl.getSubt());
          if (!gl.getIsNew()) {
            getOrm().update(pRvs, vs, gl);
          }
        }
      }
      if (delsGd != null) {
        for (CuOrSeGdLn gl : delsGd) {
          co.getGoods().remove(gl);
        }
      }
      List<CuOrSeSrLn> delsSr = null;
      for (CuOrSeSrLn sl : co.getServs()) {
        sl.setOwnr(co);
        if (sl.getIsNew()) {
          getOrm().insIdLn(pRvs, vs, sl);
        }
        if (sl.getItTxs() != null && sl.getItTxs().size() > 0) {
          for (CuOrSeSrTxLn stl : sl.getItTxs()) {
            stl.setOwnr(sl);
            if (stl.getIsNew()) {
              getOrm().insIdLn(pRvs, vs, stl);
            } else if (sl.getSrv() == null || stl.getTax() == null) {
              getOrm().del(pRvs, vs, stl);
            } else {
              getOrm().update(pRvs, vs, stl);
            }
          }
        }
        if (!sl.getIsNew() && sl.getSrv() == null) {
          getOrm().del(pRvs, vs, sl);
          if (delsSr == null) {
            delsSr = new ArrayList<CuOrSeSrLn>();
          }
          delsSr.add(sl);
        } else {
          tot = tot.add(sl.getTot());
          subt = subt.add(sl.getSubt());
          if (!sl.getIsNew()) {
            getOrm().update(pRvs, vs, sl);
          }
        }
      }
      if (delsSr != null) {
        for (CuOrSeSrLn sl : delsSr) {
          co.getServs().remove(sl);
        }
      }
      BigDecimal totTx = BigDecimal.ZERO;
      for (CartTxLn ctl : pCart.getTaxes()) {
        if (ctl.getDisab() || ctl.getSelr() == null
          || !ctl.getSelr().getDbcr().getIid()
            .equals(co.getSelr().getDbcr().getIid())) {
          continue;
        }
        CuOrSeTxLn otl = null;
        if (!co.getIsNew()) {
          for (CuOrSeTxLn otlt : co.getTaxes()) {
            if (otlt.getTax() == null) {
              otl = otlt;
              break;
            }
          }
        }
        if (otl == null) {
          otl = new CuOrSeTxLn();
          otl.setIsNew(true);
          co.getTaxes().add(otl);
        }
        otl.setOwnr(co);
        Tax tx = new Tax();
        tx.setIid(ctl.getTax().getIid());
        tx.setNme(ctl.getTax().getNme());
        otl.setTax(tx);
        otl.setTot(ctl.getTot());
        otl.setTxb(ctl.getTxb());
        totTx = totTx.add(otl.getTot());
        if (otl.getIsNew()) {
          getOrm().insIdLn(pRvs, vs, otl);
        } else {
          getOrm().update(pRvs, vs, otl);
        }
      }
      if (!co.getIsNew()) {
        List<CuOrSeTxLn> delsTx = null;
        for (CuOrSeTxLn otlt : co.getTaxes()) {
          if (otlt.getTax() == null) {
            getOrm().del(pRvs, vs, otlt);
            if (delsTx == null) {
              delsTx = new ArrayList<CuOrSeTxLn>();
            }
            delsTx.add(otlt);
          }
        }
        if (delsTx != null) {
          for (CuOrSeTxLn tl : delsTx) {
            co.getTaxes().remove(tl);
          }
        }
      }
      co.setTot(tot);
      co.setSubt(subt);
      co.setToTx(totTx);
      getOrm().update(pRvs, vs, co);
    }
    if (dels != null) {
      for (CuOrSe co : dels) {
        pPur.getSords().remove(co);
      }
    }
  }

  /**
   * <p>Saves orders.</p>
   * @param pRvs request scoped vars
   * @param pPur purchase
   * @param pCart cart
   * @throws Exception - an exception
   **/
  public final void saveOrds(final Map<String, Object> pRvs,
    final Purch pPur, final Cart pCart) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    List<CuOr> dels = null;
    for (CuOr co : pPur.getOrds()) {
      if (co.getCurr() == null) { //stored unused order
        //remove it and all its lines:
        for (CuOrGdLn gl : co.getGoods()) {
          for (CuOrGdTxLn gtl : gl.getItTxs()) {
            getOrm().del(pRvs, vs, gtl);
          }
          getOrm().del(pRvs, vs, gl);
        }
        for (CuOrSrLn sl : co.getServs()) {
          for (CuOrSrTxLn stl : sl.getItTxs()) {
            getOrm().del(pRvs, vs, stl);
          }
          getOrm().del(pRvs, vs, sl);
        }
        for (CuOrTxLn otlt : co.getTaxes()) {
          getOrm().del(pRvs, vs, otlt);
        }
        getOrm().del(pRvs, vs, co);
        if (dels == null) {
          dels = new ArrayList<CuOr>();
        }
        dels.add(co);
        continue;
      }
      if (co.getIsNew()) {
        getOrm().insIdLn(pRvs, vs, co);
      }
      BigDecimal tot = BigDecimal.ZERO;
      BigDecimal subt = BigDecimal.ZERO;
      List<CuOrGdLn> delsGd = null;
      for (CuOrGdLn gl : co.getGoods()) {
        gl.setOwnr(co);
        if (gl.getIsNew()) {
          getOrm().insIdLn(pRvs, vs, gl);
        }
        if (gl.getItTxs() != null && gl.getItTxs().size() > 0) {
          for (CuOrGdTxLn gtl : gl.getItTxs()) {
            gtl.setOwnr(gl);
            if (gtl.getIsNew()) {
              getOrm().insIdLn(pRvs, vs, gtl);
            } else if (gl.getGood() == null || gtl.getTax() == null) {
              getOrm().del(pRvs, vs, gtl);
            } else {
              getOrm().update(pRvs, vs, gtl);
            }
          }
        }
        if (!gl.getIsNew() && gl.getGood() == null) {
          getOrm().del(pRvs, vs, gl);
          if (delsGd == null) {
            delsGd = new ArrayList<CuOrGdLn>();
          }
          delsGd.add(gl);
        } else {
          tot = tot.add(gl.getTot());
          subt = subt.add(gl.getSubt());
          if (!gl.getIsNew()) {
            getOrm().update(pRvs, vs, gl);
          }
        }
      }
      if (delsGd != null) {
        for (CuOrGdLn gl : delsGd) {
          co.getGoods().remove(gl);
        }
      }
      List<CuOrSrLn> delsSr = null;
      for (CuOrSrLn sl : co.getServs()) {
        sl.setOwnr(co);
        if (sl.getIsNew()) {
          getOrm().insIdLn(pRvs, vs, sl);
        }
        if (sl.getItTxs() != null && sl.getItTxs().size() > 0) {
          for (CuOrSrTxLn stl : sl.getItTxs()) {
            stl.setOwnr(sl);
            if (stl.getIsNew()) {
              getOrm().insIdLn(pRvs, vs, stl);
            } else if (sl.getSrv() == null || stl.getTax() == null) {
              getOrm().del(pRvs, vs, stl);
            } else {
              getOrm().update(pRvs, vs, stl);
            }
          }
        }
        if (!sl.getIsNew() && sl.getSrv() == null) {
          getOrm().del(pRvs, vs, sl);
          if (delsSr == null) {
            delsSr = new ArrayList<CuOrSrLn>();
          }
          delsSr.add(sl);
        } else {
          tot = tot.add(sl.getTot());
          subt = subt.add(sl.getSubt());
          if (!sl.getIsNew()) {
            getOrm().update(pRvs, vs, sl);
          }
        }
      }
      if (delsSr != null) {
        for (CuOrSrLn sl : delsSr) {
          co.getServs().remove(sl);
        }
      }
      List<CuOrTxLn> delsTx = null;
      BigDecimal totTx = BigDecimal.ZERO;
      for (CartTxLn ctl : pCart.getTaxes()) {
        if (ctl.getDisab() || ctl.getSelr() != null) {
          continue;
        }
        CuOrTxLn otl = null;
        if (!co.getIsNew()) {
          for (CuOrTxLn otlt : co.getTaxes()) {
            if (otlt.getTax() == null) {
              otl = otlt;
              break;
            }
          }
        }
        if (otl == null) {
          otl = new CuOrTxLn();
          otl.setIsNew(true);
          co.getTaxes().add(otl);
        }
        otl.setOwnr(co);
        Tax tx = new Tax();
        tx.setIid(ctl.getTax().getIid());
        tx.setNme(ctl.getTax().getNme());
        otl.setTax(tx);
        otl.setTot(ctl.getTot());
        otl.setTxb(ctl.getTxb());
        totTx = totTx.add(otl.getTot());
        if (otl.getIsNew()) {
          getOrm().insIdLn(pRvs, vs, otl);
        } else {
          getOrm().update(pRvs, vs, otl);
        }
      }
      if (!co.getIsNew()) {
        for (CuOrTxLn otlt : co.getTaxes()) {
          if (otlt.getTax() == null) {
            getOrm().del(pRvs, vs, otlt);
            if (delsTx == null) {
              delsTx = new ArrayList<CuOrTxLn>();
            }
            delsTx.add(otlt);
          }
        }
      }
      if (delsTx != null) {
        for (CuOrTxLn tl : delsTx) {
          co.getTaxes().remove(tl);
        }
      }
      co.setTot(tot);
      co.setSubt(subt);
      co.setToTx(totTx);
      getOrm().update(pRvs, vs, co);
    }
    if (dels != null) {
      for (CuOr co : dels) {
        pPur.getOrds().remove(co);
      }
    }
  }

  /**
   * <p>Retrieve new orders to redone.</p>
   * @param pRvs request scoped vars
   * @param pBur buyer
   * @return purchase with new orders to redone
   * @throws Exception - an exception
   **/
  public final Purch retNewOrds(final Map<String, Object> pRvs,
    final Buyer pBur) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    String[] ndFl = new String[] {"ver"};
    vs.put("CuOrndFds", ndFl);
    List<CuOr> orders = getOrm().retLstCnd(pRvs, vs, CuOr.class,
      "where STAS=0 and BUYR=" + pBur.getIid()); vs.clear();
    for (CuOr cuOr : orders) {
      //redo all lines:
      //itsOwner and other data will be set farther only for used lines!!!
      //unused lines will be removed from DB
      vs.put("CuOrTxLnndFds", ndFl);
      cuOr.setTaxes(getOrm().retLstCnd(pRvs, vs, CuOrTxLn.class,
        "where OWNR=" + cuOr.getIid())); vs.clear();
      vs.put("CuOrGdLnndFds", ndFl);
      cuOr.setGoods(getOrm().retLstCnd(pRvs, vs, CuOrGdLn.class,
        "where OWNR=" + cuOr.getIid())); vs.clear();
      vs.put("CuOrSrLnndFds", ndFl);
      cuOr.setServs(getOrm().retLstCnd(pRvs, vs, CuOrSrLn.class,
        "where OWNR=" + cuOr.getIid())); vs.clear();
      vs.put("CuOrGdTxLnndFds", ndFl);
      for (CuOrGdLn gl : cuOr.getGoods()) {
        gl.setItTxs(getOrm().retLstCnd(pRvs, vs, CuOrGdTxLn.class,
          "where OWNR=" + gl.getIid()));
      }
      vs.clear();
      vs.put("CuOrSrTxLnndFds", ndFl);
      for (CuOrSrLn sl : cuOr.getServs()) {
        sl.setItTxs(getOrm().retLstCnd(pRvs, vs, CuOrSrTxLn.class,
          "where OWNR=" + sl.getIid()));
      }
      vs.clear();
    }
    vs.put("CuOrSendFds", ndFl);
    List<CuOrSe> sorders = getOrm().retLstCnd(pRvs, vs, CuOrSe.class,
      "where STAS=0 and BUYR=" + pBur.getIid()); vs.clear();
    for (CuOrSe cuOr : sorders) {
      vs.put("CuOrSeTxLnndFds", ndFl);
      cuOr.setTaxes(getOrm().retLstCnd(pRvs, vs, CuOrSeTxLn.class,
        "where OWNR=" + cuOr.getIid())); vs.clear();
      vs.put("CuOrSeGdLnndFds", ndFl);
      cuOr.setGoods(getOrm().retLstCnd(pRvs, vs, CuOrSeGdLn.class,
        "where OWNR=" + cuOr.getIid())); vs.clear();
      vs.put("CuOrSeSrLnndFds", ndFl);
      cuOr.setServs(getOrm().retLstCnd(pRvs, vs, CuOrSeSrLn.class,
        "where OWNR=" + cuOr.getIid())); vs.clear();
      vs.put("CuOrSeGdTxLnndFds", ndFl);
      for (CuOrSeGdLn gl : cuOr.getGoods()) {
        gl.setItTxs(getOrm().retLstCnd(pRvs, vs, CuOrSeGdTxLn.class,
          "where OWNR=" + gl.getIid()));
      }
      vs.clear();
      vs.put("CuOrSeSrTxLnndFds", ndFl);
      for (CuOrSeSrLn sl : cuOr.getServs()) {
        sl.setItTxs(getOrm().retLstCnd(pRvs, vs, CuOrSeSrTxLn.class,
          "where OWNR=" + sl.getIid()));
      }
      vs.clear();
    }
    Purch pur = new Purch();
    pur.setOrds(orders);
    pur.setSords(sorders);
    return pur;
  }

  /**
   * <p>It makes S.E. order line.</p>
   * @param pRvs Request scoped Vars
   * @param pOrders Orders
   * @param pSel seller
   * @param pItPl item place
   * @param pCartLn Cart Line
   * @param pTs trading settings
   * @throws Exception an Exception
   **/
  public final void makeSeOrdLn(final Map<String, Object> pRvs,
    final List<CuOrSe> pOrders, final SeSel pSel,
      final AItmPlc<?, ?> pItPl, final CartLn pCartLn,
        final TrdStg pTs) throws Exception {
    CuOrSe cuOr = null;
    boolean isNdOrInit = true;
    for (CuOrSe co : pOrders) {
      if (co.getCurr() != null && co.getSelr() != null
  && co.getSelr().getDbcr().getIid().equals(pSel.getDbcr().getIid())) {
        cuOr = co;
        isNdOrInit = false;
        break;
      }
    }
    if (cuOr == null) {
      for (CuOrSe co : pOrders) {
        if (co.getCurr() == null) {
          cuOr = co;
          break;
        }
      }
    }
    if (cuOr == null) {
      cuOr = new CuOrSe();
      cuOr.setIsNew(true);
      cuOr.setTaxes(new ArrayList<CuOrSeTxLn>());
      cuOr.setGoods(new ArrayList<CuOrSeGdLn>());
      cuOr.setServs(new ArrayList<CuOrSeSrLn>());
      pOrders.add(cuOr);
    }
    if (isNdOrInit) {
      cuOr.setDat(new Date());
      cuOr.setSelr(pSel);
      cuOr.setStas(EOrdStat.NEW);
      cuOr.setDelv(pCartLn.getOwnr().getDelv());
      cuOr.setPaym(pCartLn.getOwnr().getPaym());
      cuOr.setBuyr(pCartLn.getOwnr().getBuyr());
      //TODO  method "pickup by buyer from several places"
      //cuOr.setPlace(pItPl.getPipl());
      cuOr.setPur(pCartLn.getOwnr().getVer());
      cuOr.setCurr(pCartLn.getOwnr().getCurr());
      cuOr.setExRt(pCartLn.getOwnr().getExRt());
      cuOr.setDscr(pCartLn.getOwnr().getDscr());
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    vs.put("CartLndpLv", 0);
    vs.put("TaxdpLv", 0);
    List<CartItTxLn> citls = getOrm().retLstCnd(pRvs, vs, CartItTxLn.class,
      "where DISAB=0 and OWNR=" + pCartLn.getIid()); vs.clear();
    ACuOrSeLn ol;
    if (pCartLn.getItTyp().equals(EItmTy.SEGOODS)) {
      CuOrSeGdLn ogl = null;
      if (!cuOr.getIsNew()) {
        for (CuOrSeGdLn gl : cuOr.getGoods()) {
          if (gl.getGood() == null) {
            ogl = gl;
            break;
          }
        }
      }
      if (ogl == null) {
        ogl = new CuOrSeGdLn();
        ogl.setIsNew(true);
        cuOr.getGoods().add(ogl);
      }
      SeItm gd = new SeItm();
      gd.setSelr(pSel);
      gd.setIid(pCartLn.getItId());
      gd.setNme(pCartLn.getNme());
      ogl.setGood(gd);
      if (citls.size() > 0) {
        if (ogl.getIsNew()) {
          ogl.setItTxs(new ArrayList<CuOrSeGdTxLn>());
        }
        for (CartItTxLn citl : citls) {
          CuOrSeGdTxLn oitl = null;
          if (!cuOr.getIsNew()) {
            for (CuOrSeGdTxLn itl : ogl.getItTxs()) {
              if (itl.getTax() == null) {
                oitl = itl;
                break;
              }
            }
          }
          if (oitl == null) {
            oitl = new CuOrSeGdTxLn();
            oitl.setIsNew(true);
            ogl.getItTxs().add(oitl);
          }
          oitl.setOwnr(ogl);
          Tax tx = new Tax();
          tx.setIid(citl.getTax().getIid());
          tx.setNme(citl.getTax().getNme());
          oitl.setTax(tx);
          oitl.setTot(citl.getTot());
        }
      }
      ol = ogl;
    } else {
      CuOrSeSrLn osl = null;
      if (!cuOr.getIsNew()) {
        for (CuOrSeSrLn sl : cuOr.getServs()) {
          if (sl.getSrv() == null) {
            osl = sl;
            break;
          }
        }
      }
      if (osl == null) {
        osl = new CuOrSeSrLn();
        osl.setIsNew(true);
        cuOr.getServs().add(osl);
      }
      SeSrv sr = new SeSrv();
      sr.setSelr(pSel);
      sr.setIid(pCartLn.getItId());
      sr.setNme(pCartLn.getNme());
      osl.setSrv(sr);
      osl.setDt1(pCartLn.getDt1());
      osl.setDt2(pCartLn.getDt2());
      if (citls.size() > 0) {
        if (osl.getIsNew()) {
          osl.setItTxs(new ArrayList<CuOrSeSrTxLn>());
        }
        for (CartItTxLn citl : citls) {
          CuOrSeSrTxLn oitl = null;
          if (!cuOr.getIsNew()) {
            for (CuOrSeSrTxLn itl : osl.getItTxs()) {
              if (itl.getTax() == null) {
                oitl = itl;
                break;
              }
            }
          }
          if (oitl == null) {
            oitl = new CuOrSeSrTxLn();
            oitl.setIsNew(true);
            osl.getItTxs().add(oitl);
          }
          oitl.setOwnr(osl);
          Tax tx = new Tax();
          tx.setIid(citl.getTax().getIid());
          tx.setNme(citl.getTax().getNme());
          oitl.setTax(tx);
          oitl.setTot(citl.getTot());
        }
      }
      ol = osl;
    }
    if (pCartLn.getDscr() != null) {
      ol.setDscr(pCartLn.getNme() + ". " + pCartLn.getDscr());
    } else {
      ol.setDscr(pCartLn.getNme());
    }
    ol.setUom(pCartLn.getUom());
    ol.setPri(pCartLn.getPri());
    ol.setQuan(pCartLn.getQuan());
    ol.setSubt(pCartLn.getSubt());
    ol.setTot(pCartLn.getTot());
    ol.setToTx(pCartLn.getToTx());
    ol.setTxCt(pCartLn.getTxCt());
    ol.setTdsc(pCartLn.getTdsc());
  }

  /**
   * <p>It makes order line.</p>
   * @param pRvs Request scoped Vars
   * @param pOrders Orders
   * @param pItPl item place
   * @param pCartLn Cart Line
   * @param pTs trading settings
   * @throws Exception an Exception
   **/
  public final void makeOrdLn(final Map<String, Object> pRvs,
    final List<CuOr> pOrders, final AItmPlc<?, ?> pItPl,
      final CartLn pCartLn, final TrdStg pTs) throws Exception {
    CuOr cuOr = null;
    boolean isNdOrInit = true;
    for (CuOr co : pOrders) {
      if (co.getCurr() != null) {
        cuOr = co;
        isNdOrInit = false;
        break;
      }
    }
    if (cuOr == null) {
      cuOr = new CuOr();
      cuOr.setIsNew(true);
      cuOr.setTaxes(new ArrayList<CuOrTxLn>());
      cuOr.setGoods(new ArrayList<CuOrGdLn>());
      cuOr.setServs(new ArrayList<CuOrSrLn>());
      pOrders.add(cuOr);
    }
    if (isNdOrInit) {
      cuOr.setDat(new Date());
      cuOr.setStas(EOrdStat.NEW);
      cuOr.setDelv(pCartLn.getOwnr().getDelv());
      cuOr.setPaym(pCartLn.getOwnr().getPaym());
      cuOr.setBuyr(pCartLn.getOwnr().getBuyr());
      //TODO method "pickup by buyer from several places"
      //cuOr.setPlace(pItPl.getPipl());
      cuOr.setPur(pCartLn.getOwnr().getVer());
      cuOr.setCurr(pCartLn.getOwnr().getCurr());
      cuOr.setExRt(pCartLn.getOwnr().getExRt());
      cuOr.setDscr(pCartLn.getOwnr().getDscr());
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    vs.put("CartLndpLv", 0);
    vs.put("TaxdpLv", 0);
    List<CartItTxLn> citls = getOrm().retLstCnd(pRvs, vs, CartItTxLn.class,
      "where DISAB=0 and OWNR=" + pCartLn.getIid()); vs.clear();
    ACuOrLn ol;
    if (pCartLn.getItTyp().equals(EItmTy.GOODS)) {
      CuOrGdLn ogl = null;
      if (!cuOr.getIsNew()) {
        for (CuOrGdLn gl : cuOr.getGoods()) {
          if (gl.getGood() == null) {
            ogl = gl;
            break;
          }
        }
      }
      if (ogl == null) {
        ogl = new CuOrGdLn();
        ogl.setIsNew(true);
        cuOr.getGoods().add(ogl);
      }
      Itm gd = new Itm();
      gd.setIid(pCartLn.getItId());
      gd.setNme(pCartLn.getNme());
      ogl.setGood(gd);
      if (citls.size() > 0) {
        if (ogl.getIsNew()) {
          ogl.setItTxs(new ArrayList<CuOrGdTxLn>());
        }
        for (CartItTxLn citl : citls) {
          CuOrGdTxLn oitl = null;
          if (!cuOr.getIsNew()) {
            for (CuOrGdTxLn itl : ogl.getItTxs()) {
              if (itl.getTax() == null) {
                oitl = itl;
                break;
              }
            }
          }
          if (oitl == null) {
            oitl = new CuOrGdTxLn();
            oitl.setIsNew(true);
            ogl.getItTxs().add(oitl);
          }
          oitl.setOwnr(ogl);
          Tax tx = new Tax();
          tx.setIid(citl.getTax().getIid());
          tx.setNme(citl.getTax().getNme());
          oitl.setTax(tx);
          oitl.setTot(citl.getTot());
        }
      }
      ol = ogl;
    } else {
      CuOrSrLn osl = null;
      if (!cuOr.getIsNew()) {
        for (CuOrSrLn sl : cuOr.getServs()) {
          if (sl.getSrv() == null) {
            osl = sl;
            break;
          }
        }
      }
      if (osl == null) {
        osl = new CuOrSrLn();
        osl.setIsNew(true);
        cuOr.getServs().add(osl);
      }
      Srv sr = new Srv();
      sr.setIid(pCartLn.getItId());
      sr.setNme(pCartLn.getNme());
      osl.setSrv(sr);
      osl.setDt1(pCartLn.getDt1());
      osl.setDt2(pCartLn.getDt2());
      if (citls.size() > 0) {
        if (osl.getIsNew()) {
          osl.setItTxs(new ArrayList<CuOrSrTxLn>());
        }
        for (CartItTxLn citl : citls) {
          CuOrSrTxLn oitl = null;
          if (!cuOr.getIsNew()) {
            for (CuOrSrTxLn itl : osl.getItTxs()) {
              if (itl.getTax() == null) {
                oitl = itl;
                break;
              }
            }
          }
          if (oitl == null) {
            oitl = new CuOrSrTxLn();
            oitl.setIsNew(true);
            osl.getItTxs().add(oitl);
          }
          oitl.setOwnr(osl);
          Tax tx = new Tax();
          tx.setIid(citl.getTax().getIid());
          tx.setNme(citl.getTax().getNme());
          oitl.setTax(tx);
          oitl.setTot(citl.getTot());
        }
      }
      ol = osl;
    }
    if (pCartLn.getDscr() != null) {
      ol.setDscr(pCartLn.getNme() + ". " + pCartLn.getDscr());
    } else {
      ol.setDscr(pCartLn.getNme());
    }
    ol.setUom(pCartLn.getUom());
    ol.setPri(pCartLn.getPri());
    ol.setQuan(pCartLn.getQuan());
    ol.setSubt(pCartLn.getSubt());
    ol.setTot(pCartLn.getTot());
    ol.setToTx(pCartLn.getToTx());
    ol.setTxCt(pCartLn.getTxCt());
    ol.setTdsc(pCartLn.getTdsc());
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
