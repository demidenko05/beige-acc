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
import java.util.Arrays;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.log.ILog;
import org.beigesoft.prc.IPrc;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.acc.mdlp.Itm;
import org.beigesoft.acc.mdlp.Srv;
import org.beigesoft.acc.mdlp.Curr;
import org.beigesoft.acc.mdlp.TxDst;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.ws.mdl.EItmTy;
import org.beigesoft.ws.mdl.EItmSpTy;
import org.beigesoft.ws.mdlb.AItmSpf;
import org.beigesoft.ws.mdlb.AItmPri;
import org.beigesoft.ws.mdlp.ItmSpf;
import org.beigesoft.ws.mdlp.SrvSpf;
import org.beigesoft.ws.mdlp.SeSrv;
import org.beigesoft.ws.mdlp.SeSrvPlc;
import org.beigesoft.ws.mdlp.SeSrvSpf;
import org.beigesoft.ws.mdlp.SeItm;
import org.beigesoft.ws.mdlp.SeItmPlc;
import org.beigesoft.ws.mdlp.SeItmSpf;
import org.beigesoft.ws.mdlp.SrvPlc;
import org.beigesoft.ws.mdlp.ItmPlc;
import org.beigesoft.ws.mdlp.CartLn;
import org.beigesoft.ws.mdlp.Cart;
import org.beigesoft.ws.mdlp.Buyer;
import org.beigesoft.ws.mdlp.TrdStg;
import org.beigesoft.ws.mdlp.CurrRt;
import org.beigesoft.ws.srv.ISrCart;
import org.beigesoft.ws.srv.IBuySr;

/**
 * <p>Service that retrieves goods/service details. It passes itmPri=null
 * in case of outdated or inconsistent price data.
 * JSP should handle wrong price or availability data.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class ItmPg<RS> implements IPrc {

  /**
   * <p>Log.</p>
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
   * <p>Buyer service.</p>
   **/
  private IBuySr buySr;

  /**
   * <p>RDB service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Transaction isolation.</p>
   **/
  private Integer trIsl;

  //Cached queries:
  /**
   * <p>I18 query item specifics.</p>
   **/
  private String quItSpDeIn;

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
      String itTyStr = pRqDt.getParam("itTy");
      Long itId = Long.valueOf(pRqDt.getParam("itId"));
      Cart cart = this.srCart.getCart(pRvs, pRqDt, false, false);
      Buyer buyr;
      if (cart != null) {
        buyr = cart.getBuyr();
        if (cart.getTot().compareTo(BigDecimal.ZERO) == 0) {
          pRvs.put("cart", null);
        } else {
          AcStg as = (AcStg) pRvs.get("astg");
          Curr curr = (Curr) pRvs.get("wscurr");
          if (!cart.getCurr().getIid().equals(curr.getIid())) {
            cart.setCurr(curr);
            List<CurrRt> currRts = (List<CurrRt>) pRvs.get("currRts");
            for (CurrRt cr: currRts) {
              if (cr.getCurr().getIid().equals(cart.getCurr().getIid())) {
                cart.setExRt(cr.getExRt());
                break;
              }
            }
            this.srCart.hndCurrChg(pRvs, cart, as, ts);
          }
          if (pRvs.get("txRules") == null) {
            TxDst txRules = this.srCart.revTxRules(pRvs, cart, as);
            pRvs.put("txRules", txRules);
          }
          for (CartLn ci : cart.getItems()) {
            if (!ci.getDisab() && ci.getItId().equals(itId)
              && ci.getItTyp().toString().equals(itTyStr)) {
              pRvs.put("cartItm", ci);
              break;
            }
          }
        }
      } else {
        buyr = this.buySr.getBuyr(pRvs, pRqDt);
        if (buyr == null) {
          buyr = this.buySr.createBuyr(pRvs, pRqDt);
        }
      }
      if (EItmTy.GOODS.toString().equals(itTyStr)) {
        processGoods(pRvs, pRqDt, ts, buyr, itId);
      } else if (EItmTy.SERVICE.toString().equals(itTyStr)) {
        processService(pRvs, pRqDt, ts, buyr, itId);
      } else if (EItmTy.SEGOODS.toString().equals(itTyStr)) {
        processSeItm(pRvs, pRqDt, ts, buyr, itId);
      } else if (EItmTy.SEGOODS.toString().equals(itTyStr)) {
        processSeItm(pRvs, pRqDt, ts, buyr, itId);
      } else if (EItmTy.SESERVICE.toString().equals(itTyStr)) {
        procSeSrv(pRvs, pRqDt, ts, buyr, itId);
      } else {
        throw new Exception(
          "Detail page not yet implemented for item type: " + itTyStr);
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
  }

  /**
   * <p>Process a goods from our warehouse.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pTs TrdStg
   * @param pBuyer Buyer
   * @param pItId Item ID
   * @throws Exception - an exception
   **/
  public final void processGoods(final Map<String, Object> pRvs,
    final IReqDt pRqDt, final TrdStg pTs,
      final Buyer pBuyer, final Long pItId) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    List<ItmSpf> itmSpecLs;
    List<ItmPlc> itmPlcLs;
    itmSpecLs = retItmSpfLs(pRvs, pTs, pItId, ItmSpf.class,
      Itm.class.getSimpleName());
    //extract main image if exist:
    int miIdx = -1;
    for (int i = 0; i < itmSpecLs.size(); i++) {
      if (itmSpecLs.get(i).getSpec().getTyp().equals(EItmSpTy.IMAGE)) {
        pRvs.put("itmImg", itmSpecLs.get(i));
        miIdx = i;
        break;
      }
    }
    if (miIdx != -1) {
      itmSpecLs.remove(miIdx);
    }
    AItmPri<?, ?> itmPri = getSrCart().revItmPri(pRvs, pTs, pBuyer,
      EItmTy.GOODS, pItId);
    itmPlcLs = getOrm().retLstCnd(pRvs, vs, ItmPlc.class, "where ITM="
      + pItId);
    pRvs.put("itmSpecLs", itmSpecLs);
    pRvs.put("itmPlcLs", itmPlcLs);
    pRvs.put("itmPri", itmPri);
  }

  /**
   * <p>Process a seGood.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pTs TrdStg
   * @param pBuyer Buyer
   * @param pItId Item ID
   * @throws Exception - an exception
   **/
  public final void procSeSrv(final Map<String, Object> pRvs,
    final IReqDt pRqDt, final TrdStg pTs,
      final Buyer pBuyer, final Long pItId) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    List<SeSrvSpf> itmSpecLs;
    List<SeSrvPlc> itmPlcLs;
    itmSpecLs = retItmSpfLs(pRvs, pTs, pItId,
      SeSrvSpf.class, SeSrv.class.getSimpleName());
    //extract main image if exist:
    int miIdx = -1;
    for (int i = 0; i < itmSpecLs.size(); i++) {
      if (itmSpecLs.get(i).getSpec().getTyp().equals(EItmSpTy.IMAGE)) {
        pRvs.put("itmImg", itmSpecLs.get(i));
        miIdx = i;
        break;
      }
    }
    if (miIdx != -1) {
      itmSpecLs.remove(miIdx);
    }
    AItmPri<?, ?> itmPri = getSrCart().revItmPri(pRvs, pTs, pBuyer,
      EItmTy.SESERVICE, pItId);
    itmPlcLs = getOrm().retLstCnd(pRvs, vs, SeSrvPlc.class, "where ITM="
      + pItId);
    pRvs.put("itmSpecLs", itmSpecLs);
    pRvs.put("itmPlcLs", itmPlcLs);
    pRvs.put("itmPri", itmPri);
  }

  /**
   * <p>Process a seGood.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pTs TrdStg
   * @param pBuyer Buyer
   * @param pItId Item ID
   * @throws Exception - an exception
   **/
  public final void processSeItm(final Map<String, Object> pRvs,
    final IReqDt pRqDt, final TrdStg pTs,
      final Buyer pBuyer, final Long pItId) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    List<SeItmSpf> itmSpecLs;
    List<SeItmPlc> itmPlcLs;
    itmSpecLs = retItmSpfLs(pRvs, pTs, pItId, SeItmSpf.class,
      SeItm.class.getSimpleName());
    //extract main image if exist:
    int miIdx = -1;
    for (int i = 0; i < itmSpecLs.size(); i++) {
      if (itmSpecLs.get(i).getSpec().getTyp().equals(EItmSpTy.IMAGE)) {
        pRvs.put("itmImg", itmSpecLs.get(i));
        miIdx = i;
        break;
      }
    }
    if (miIdx != -1) {
      itmSpecLs.remove(miIdx);
    }
    AItmPri<?, ?> itmPri = getSrCart().revItmPri(pRvs, pTs, pBuyer,
      EItmTy.SEGOODS, pItId);
    itmPlcLs = getOrm().retLstCnd(pRvs, vs, SeItmPlc.class, "where ITM="
      + pItId);
    pRvs.put("itmSpecLs", itmSpecLs);
    pRvs.put("itmPlcLs", itmPlcLs);
    pRvs.put("itmPri", itmPri);
  }

  /**
   * <p>Process a service.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pTs TrdStg
   * @param pBuyer Buyer
   * @param pItId Item ID
   * @throws Exception - an exception
   **/
  public final void processService(final Map<String, Object> pRvs,
    final IReqDt pRqDt, final TrdStg pTs,
      final Buyer pBuyer, final Long pItId) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    List<SrvSpf> itmSpecLs;
    List<SrvPlc> itmPlcLs;
    itmSpecLs = retItmSpfLs(pRvs, pTs, pItId, SrvSpf.class,
      Srv.class.getSimpleName());
    //extract main image if exist:
    int miIdx = -1;
    for (int i = 0; i < itmSpecLs.size(); i++) {
      if (itmSpecLs.get(i).getSpec().getTyp().equals(EItmSpTy.IMAGE)) {
        pRvs.put("itmImg", itmSpecLs.get(i));
        miIdx = i;
        break;
      }
    }
    if (miIdx != -1) {
      itmSpecLs.remove(miIdx);
    }
    AItmPri<?, ?> itmPri = getSrCart().revItmPri(pRvs, pTs, pBuyer,
      EItmTy.SERVICE, pItId);
    itmPlcLs = getOrm().retLstCnd(pRvs, vs, SrvPlc.class, "where ITM=" + pItId);
    pRvs.put("itmSpecLs", itmSpecLs);
    pRvs.put("itmPlcLs", itmPlcLs);
    pRvs.put("itmPri", itmPri);
  }

  /**
   * <p>Retrieve Item Specifics list for item.</p>
   * @param <T> item type
   * @param pRvs request scoped vars
   * @param pTs TrdStg
   * @param pItId item ID
   * @param pItmSpfCl item specifics class
   * @param pItemSn item simple name
   * @return Item Specifics list
   * @throws Exception - an exception
   **/
  public final <T extends AItmSpf<?, ?>> List<T> retItmSpfLs(
    final Map<String, Object> pRvs, final TrdStg pTs, final Long pItId,
      final Class<T> pItmSpfCl, final String pItemSn) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    //HTML templates full
    vs.put("ItmSpdpLv", 3);
    vs.put(pItemSn + "ndFds", new String[] {"nme"});
    String[] soiFldNms = new String[] {"nme", "inLst", "typ", "grp", "htmt"};
    Arrays.sort(soiFldNms);
    vs.put("ItmSpndFds", soiFldNms);
    String[] soigFldNms = new String[] {"nme", "tmpls", "tmple", "tmpld"};
    Arrays.sort(soigFldNms);
    vs.put("ItmSpGrndFds", soigFldNms);
    vs.put("HtmltndFds", new String[] {"val"});
    List<T> result = null;
    if (pTs.getAi18n()) {
      UsPrf upf = (UsPrf) pRvs.get("upf");
      CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
      String lang = upf.getLng().getIid();
      String langDef = cpf.getLngDef().getIid();
      if (!lang.equals(langDef)) {
        String qd;
        if (pItmSpfCl == ItmSpf.class) {
          qd = lazyGetQuItSpDeIn().replace(":TITSPEC", "ITMSPF")
            .replace(":TITM", "ITM").replace(":T18NIT", "I18ITM")
              .replace(":ITMID", pItId.toString()).replace(":LNG", lang);
        } else if (pItmSpfCl == SrvSpf.class) {
          qd = lazyGetQuItSpDeIn().replace(":TITSPEC", "SRVSPF")
            .replace(":TITM", "SRV").replace(":T18NIT", "I18SRV")
              .replace(":ITMID", pItId.toString()).replace(":LNG", lang);
        } else if (pItmSpfCl == SeItmSpf.class) {
          qd = lazyGetQuItSpDeIn().replace(":TITSPEC", "SEITMSPF")
            .replace(":TITM", "SEITM").replace(":T18NIT", "I18SEITM")
              .replace(":ITMID", pItId.toString()).replace(":LNG", lang);
        } else if (pItmSpfCl == SeSrvSpf.class) {
          qd = lazyGetQuItSpDeIn().replace(":TITSPEC", "SESRVSPF")
            .replace(":TITM", "SESRV").replace(":T18NIT", "I18SESRV")
              .replace(":ITMID", pItId.toString()).replace(":LNG", lang);
        } else {
          throw new Exception("NYI for " +  pItmSpfCl);
        }
        result = getOrm().retLstQu(pRvs, vs, pItmSpfCl, qd);
      }
    }
    if (result == null) {
      result = getOrm().retLstCnd(pRvs, vs, pItmSpfCl, "where " + pItmSpfCl
     .getSimpleName().toUpperCase() + ".ITM=" + pItId + " order by SPEC11.IDX");
    }
    return result;
  }

  /**
   * <p>Lazy Get quItSpDeIn.</p>
   * @return String
   * @throws Exception - an exception
   **/
  public final String lazyGetQuItSpDeIn() throws Exception {
    if (this.quItSpDeIn == null) {
      this.quItSpDeIn = loadStr("/ws/itSpDeIn.sql");
    }
    return this.quItSpDeIn;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFiNm file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final String loadStr(final String pFiNm) throws IOException {
    URL urlFile = ItmPg.class.getResource(pFiNm);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = ItmPg.class.getResourceAsStream(pFiNm);
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

  //Simple getters and setters:
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
}
