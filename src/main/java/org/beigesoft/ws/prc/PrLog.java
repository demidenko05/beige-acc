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

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.UUID;
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.ColVals;
import org.beigesoft.log.ILog;
import org.beigesoft.fct.IFctPrc;
import org.beigesoft.prc.IPrc;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.TxDst;
import org.beigesoft.ws.mdlp.Buyer;
import org.beigesoft.ws.mdlp.Cart;
import org.beigesoft.ws.mdlp.CartLn;
import org.beigesoft.ws.mdlp.CartTxLn;
import org.beigesoft.ws.mdlp.CartTot;
import org.beigesoft.ws.mdlp.TrdStg;
import org.beigesoft.ws.srv.ISrCart;
import org.beigesoft.ws.srv.IBuySr;

/**
 * <p>Processor that registers, logins, logouts buyer.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrLog<RS> implements IPrc {

  /**
   * <p>Logger.</p>
   **/
  private ILog log;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>DB service.</p>
   */
  private IRdb<RS> rdb;

  /**
   * <p>Processors factory.</p>
   **/
  private IFctPrc procFac;

  /**
   * <p>Shopping Cart service.</p>
   **/
  private ISrCart srvCart;

  /**
   * <p>Buyer service.</p>
   **/
  private IBuySr buySr;

  /**
   * <p>Process request.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    Buyer buyer = this.buySr.getBuyr(pRvs, pRqDt);
    if (buyer == null) {
      buyer = this.buySr.createBuyr(pRvs, pRqDt);
    }
    String nme = pRqDt.getParam("nme");
    String eml = pRqDt.getParam("eml");
    String pwd = pRqDt.getParam("pwd");
    String pwdc = pRqDt.getParam("pwdc");
    long now = new Date().getTime();
    pRvs.put("buyr", buyer);
    Map<String, Object> vs = new HashMap<String, Object>();
    if (buyer.getEml() == null) { //unregistered:
      if (nme != null && pwd != null && pwdc != null && eml != null) {
        //creating:
        if (nme.length() > 2 && pwd.length() > 7 && pwd.equals(pwdc)
          && eml.length() > 5) {
          vs.put("BuyerdpLv", 0);
          List<Buyer> brs = getOrm().retLstCnd(pRvs, vs, Buyer.class,
            "where BUYER.EML='" + eml + "'"); vs.clear();
          if (brs.size() == 0) {
            buyer.setNme(nme);
            buyer.setPwd(pwd);
            buyer.setEml(eml);
            buyer.setLsTm(now);
            UUID buseid = UUID.randomUUID();
            buyer.setBuSeId(buseid.toString());
            if (buyer.getIsNew()) {
              this.orm.insIdLn(pRvs, vs, buyer);
            } else {
              this.orm.update(pRvs, vs, buyer);
            }
            pRqDt.setCookVl("cBuyerId", buyer.getIid().toString());
            pRqDt.setCookVl("buSeId", buyer.getBuSeId());
            getLog().info(pRvs, PrLog.class, "Buyer registered: " + nme + "/"
              + eml);
          } else if (brs.size() == 1) {
            pRvs.put("errMsg", "emBusy");
          } else {
            getLog().error(pRvs, PrLog.class,
              "Several users with same email!: " + eml);
          }
        } else {
          pRvs.put("errMsg", "buyCrRul");
        }
      } else if (pwd != null && eml != null) {
        //login from new browser
        vs.put("BuyerdpLv", 1);
        List<Buyer> brs = getOrm().retLstCnd(pRvs, vs, Buyer.class,
          "where PWD='" + pwd + "' and EML='" + eml + "'"); vs.clear();
        if (brs.size() == 1) {
          //free buyer and moving its cart by fast updates:
          mkFreBuyr(pRvs,  pRqDt, buyer, brs.get(0));
        } else if (brs.size() == 0) {
          pRvs.put("errMsg", "wrong_em_password");
        } else {
          getLog().error(pRvs, PrLog.class,
            "Several users with same password and email!: " + eml);
        }
      } else {
        spam(pRvs, pRqDt);
      }
    } else { //registered:
      if (now - buyer.getLsTm() < 1800000L && buyer.getBuSeId() != null) {
        //there is opened session:
        String buSeId = pRqDt.getCookVl("buSeId");
        if (buyer.getBuSeId().equals(buSeId)) {
          //authorized requests:
          String zip = pRqDt.getParam("zip");
          String addr1 = pRqDt.getParam("addr1");
          if (nme != null && zip != null  && addr1 != null) {
            //change name, shipping address:
            String cntr = pRqDt.getParam("cntr");
            String city = pRqDt.getParam("city");
            String addr2 = pRqDt.getParam("addr2");
            String phon = pRqDt.getParam("phon");
            if (nme.length() > 2 && zip.length() > 2 && addr1.length() > 2) {
              buyer.setNme(nme);
              buyer.setZip(zip);
              buyer.setAddr1(addr1);
              buyer.setAddr2(addr2);
              buyer.setCntr(cntr);
              buyer.setCity(city);
              buyer.setPhon(phon);
              buyer.setLsTm(now);
              this.orm.update(pRvs, vs, buyer);
              getLog().info(pRvs, PrLog.class, "Buyer's info changed : " + nme
                + "/" + eml + "/" + zip + "/" + city + "/" + cntr + "/"
                  + phon + "/" + addr1 + "/" + addr2);
            } else {
              pRvs.put("errMsg", "buyEmRul");
            }
          } else if (pwd != null && pwdc != null) {
            //change password:
            if (pwd.length() > 7 && pwd.equals(pwdc)) {
              buyer.setPwd(pwd);
              buyer.setLsTm(now);
              this.orm.update(pRvs, vs, buyer);
              getLog().info(pRvs, PrLog.class, "Buyer's password changed : "
                + nme + "/" + eml);
            } else {
              pRvs.put("errMsg", "buyPwdRul");
            }
          } else {
            //logout action:
            buyer.setLsTm(0L);
            this.orm.update(pRvs, vs, buyer);
          }
        } else { //either spam or buyer login from other browser without logout
          spam(pRvs, pRqDt);
        }
      } else {
        //unauthorized requests:
        if (pwd != null) {
          //login action:
          if (pwd.equals(buyer.getPwd())) {
            buyer.setLsTm(now);
            UUID buseid = UUID.randomUUID();
            buyer.setBuSeId(buseid.toString());
            pRqDt.setCookVl("buSeId", buyer.getBuSeId());
            this.orm.update(pRvs, vs, buyer);
          } else {
            pRvs.put("errMsg", "wrong_password");
          }
        } else {
          spam(pRvs, pRqDt);
        }
      }
    }
    String procNm = pRqDt.getParam("prcRed");
    if (getClass().getSimpleName().equals(procNm)) {
      throw new ExcCode(ExcCode.SPAM, "Danger stupid scam!!!!");
    }
    IPrc proc = this.procFac.laz(pRvs, procNm);
    proc.process(pRvs, pRqDt);
  }

  /**
   * <p>Handles spam.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  public final void spam(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    //TODO
  }

  /**
   * <p>Makes free buyer and moving its cart by fast updates.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pBuTmp buyer unregistered
   * @param pBuyr buyer registered
   * @throws Exception - an exception
   **/
  public final void mkFreBuyr(final Map<String, Object> pRvs,
    final IReqDt pRqDt, final Buyer pBuTmp,
      final Buyer pBuyr) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    long now = new Date().getTime();
    if (!pBuTmp.getIsNew()) {
      pBuTmp.setFre(true);
      pBuTmp.setEml(null);
      pBuTmp.setPwd(null);
      pBuTmp.setLsTm(0L);
      this.orm.update(pRvs, vs, pBuTmp);
    }
    pBuyr.setLsTm(now);
    UUID buseid = UUID.randomUUID();
    pBuyr.setBuSeId(buseid.toString());
    this.orm.update(pRvs, vs, pBuyr);
    pRqDt.setCookVl("buSeId", pBuyr.getBuSeId());
    pRqDt.setCookVl("cBuyerId", pBuyr.getIid().toString());
    pRvs.put("buyr", pBuyr);
    Cart oldCrt = new Cart();
    oldCrt.setIid(pBuTmp);
    oldCrt = this.orm.retEnt(pRvs, vs, oldCrt);
    if (oldCrt != null && oldCrt.getTot().compareTo(BigDecimal.ZERO) == 1) {
      Long obid = pBuTmp.getIid();
      ColVals cvs = new ColVals();
      cvs.setLongs(new HashMap<String, Long>());
      cvs.getLongs().put("ownr", pBuyr.getIid());
      this.rdb.update(CartLn.class, cvs, "OWNR=" + obid);
      this.rdb.update(CartTxLn.class, cvs, "OWNR=" + obid);
      this.rdb.update(CartTot.class, cvs, "OWNR=" + obid);
      Cart cart = this.srvCart.getCart(pRvs, pRqDt, true, false);
      TrdStg ts = (TrdStg) pRvs.get("tstg");
      AcStg as = (AcStg) pRvs.get("astg");
      TxDst txRules = this.srvCart.revTxRules(pRvs, cart, as);
      if (txRules != null) {
        pRvs.put("txRules", txRules);
      }
      cart.setDelv(oldCrt.getDelv());
      cart.setPaym(oldCrt.getPaym());
      //redo prices and taxes:
      CartLn frCl = null;
      for (CartLn cl : cart.getItems()) {
        if (!cl.getDisab()) {
          if (cl.getForc()) {
            frCl = cl;
            this.srvCart.delLine(pRvs, cl, txRules);
          } else {
            this.srvCart.mkLine(pRvs, cl, as, ts, txRules, true, true);
            this.srvCart.mkCartTots(pRvs, ts, cl, as, txRules);
          }
        }
      }
      if (frCl != null) {
         this.srvCart.hndCartChg(pRvs, cart, txRules);
      }
    }
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
   * <p>Getter for orm.</p>
   * @return IRdb<RS>
   **/
  public final IOrm getOrm() {
    return this.orm;
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
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final void setOrm(final IOrm pOrm) {
    this.orm = pOrm;
  }

  /**
   * <p>Getter for procFac.</p>
   * @return IFctPrc
   **/
  public final IFctPrc getProcFac() {
    return this.procFac;
  }

  /**
   * <p>Setter for procFac.</p>
   * @param pProcFac reference
   **/
  public final void setProcFac(final IFctPrc pProcFac) {
    this.procFac = pProcFac;
  }

  /**
   * <p>Getter for srvCart.</p>
   * @return ISrCart
   **/
  public final ISrCart getSrvCart() {
    return this.srvCart;
  }

  /**
   * <p>Setter for srvCart.</p>
   * @param pSrvCart reference
   **/

  public final void setSrvCart(final ISrCart pSrvCart) {
    this.srvCart = pSrvCart;
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
