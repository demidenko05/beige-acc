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
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE ITM OR
SRVS; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.beigesoft.ws.prc;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.Page;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.flt.EFltOpr;
import org.beigesoft.flt.FltInt;
import org.beigesoft.flt.FltBgd;
import org.beigesoft.flt.FltItms;
import org.beigesoft.log.ILog;
import org.beigesoft.prc.IPrc;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.srv.ISrvPg;
import org.beigesoft.acc.mdlp.TxDst;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Curr;
import org.beigesoft.ws.mdl.TrCatl;
import org.beigesoft.ws.mdl.CmpTrCatl;
import org.beigesoft.ws.mdl.EItmTy;
import org.beigesoft.ws.mdl.EItmSpTy;
import org.beigesoft.ws.mdl.FltSpf;
import org.beigesoft.ws.mdl.FltsSpfWhe;
import org.beigesoft.ws.mdlp.ChoSp;
import org.beigesoft.ws.mdlp.ChoSpTy;
import org.beigesoft.ws.mdlp.CatSp;
import org.beigesoft.ws.mdlp.CatGs;
import org.beigesoft.ws.mdlp.SubCat;
import org.beigesoft.ws.mdlp.Cart;
import org.beigesoft.ws.mdlp.CartLn;
import org.beigesoft.ws.mdlp.Itlist;
import org.beigesoft.ws.mdlp.TrdStg;
import org.beigesoft.ws.mdlp.BurPric;
import org.beigesoft.ws.mdlp.PriItm;
import org.beigesoft.ws.mdlp.CurrRt;
import org.beigesoft.ws.srv.ISrCart;
import org.beigesoft.ws.srv.ILsCatlChg;

/**
 * <p>Service that retrieve webstore page.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class WsPg<RS> implements IPrc, ILsCatlChg {

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Page service.</p>
   */
  private ISrvPg srvPg;

  /**
   * <p>Shopping Cart service.</p>
   **/
  private ISrCart srCart;

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Comparator of catls by index.</p>
   **/
  private CmpTrCatl cmpCatls = new CmpTrCatl();

  /**
   * <p>Transaction isolation.</p>
   **/
  private Integer trIsl;

  //Cached data:
  /**
   * <p>Cached catls.</p>
   **/
  private List<TrCatl> catls;

  /**
   * <p>Query for specifics of item flt.</p>
   **/
  private String quItSpFlt;

  /**
   * <p>Query item in list for catl not auctioning
   * same price for all customers.</p>
   **/
  private String quItInLstCa;

  /**
   * <p>Query total items in list for catl not auctioning
   * same price for all customers.</p>
   **/
  private String quItInLstCaTo;

  /**
   * <p>Query I18N items in list for catl not auctioning
   * same price for all customers.</p>
   **/
  private String quItInLstCaIn;

  /**
   * <p>Handle catl changed event.</p>
   * @throws Exception an Exception
   **/
  @Override
  public final synchronized void hndCatlChg() throws Exception {
    this.catls = null;
  }

  /**
   * <p>Process entity request.</p>
   * @param pRvs additional param
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
      Map<String, Object> vs = new HashMap<String, Object>();
      pRvs.put("catls", lazCatls(pRvs));
      TrdStg ts = (TrdStg) pRvs.get("tstg");
      String catlIdStr = pRqDt.getParam("catlId");
      Long catId = null;
      if (catlIdStr != null) {
        catId = Long.valueOf(catlIdStr);
      }
      if (catId == null && ts.getCatl() != null) {
        catId = ts.getCatl().getIid();
      }
      Cart cart = (Cart) pRvs.get("cart");
      if (cart == null) {
        cart = this.srCart
          .getCart(pRvs, pRqDt, false, false);
      }
      AcStg as = (AcStg) pRvs.get("astg");
      Curr curr = (Curr) pRvs.get("wscurr");
      List<CurrRt> currRts = (List<CurrRt>) pRvs.get("currRts");
      BigDecimal cuRt = BigDecimal.ONE;
      for (CurrRt cr: currRts) {
        if (cr.getCurr().getIid().equals(curr.getIid())) {
          cuRt = cr.getExRt();
          break;
        }
      }
      if (cart != null) {
        if (cart.getTot().compareTo(BigDecimal.ZERO) == 0) {
          pRvs.put("cart", null);
        } else {
          if (!cart.getCurr().getIid().equals(curr.getIid())) {
            cart.setCurr(curr);
            cart.setExRt(cuRt);
            this.srCart.hndCurrChg(pRvs, cart, as, ts);
          }
          if (pRvs.get("txRules") == null) {
            TxDst txRules = this.srCart.revTxRules(pRvs, cart, as);
            pRvs.put("txRules", txRules);
          }
          Map<EItmTy, Map<Long, CartLn>> cartMap =
            new HashMap<EItmTy, Map<Long, CartLn>>();
          for (CartLn ci : cart.getItems()) {
            if (!ci.getDisab()) {
              Map<Long, CartLn> typedMap = cartMap.get(ci.getItTyp());
              if (typedMap == null) {
                typedMap = new HashMap<Long, CartLn>();
                cartMap.put(ci.getItTyp(), typedMap);
              }
              typedMap.put(ci.getItId(), ci);
            }
          }
          pRvs.put("cartMap", cartMap);
        }
      }
      if (catId != null) {
        // either selected by user catl or "on start" must be
        // if user additionally selected filters (include set of subcatls)
        // then the main (root) catl ID still present in request
        TrCatl tcat = findTrCatlById(this.catls, catId);
        if (tcat == null) {
          this.log.warn(pRvs, WsPg.class,
            "Can't find catl #" + catId);
        } else {
          String ordb = pRqDt.getParam("ordb");
          pRvs.put("ordb", ordb);
          String orderBy = null;
          if (ordb != null) {
            if (ordb.equals("pa")) {
              orderBy = " order by PRI asc";
            } else if (ordb.equals("pd")) {
              orderBy = " order by PRI desc";
            }
          }
          FltInt fltPri = revFltPri(tcat, pRvs, pRqDt);
          FltItms<CatGs> fltCatl = revFltCatl(tcat, pRvs, pRqDt);
          List<FltSpf> fltSpfs = revFltsSpec(tcat, pRvs, pRqDt);
          String whereAdd = revWhePri(fltPri, cuRt);
          String whereCatl = revealWhereCatl(tcat, fltCatl);
          //TODO StringBuffer
          String queryg = null;
          String querys = null;
          String queryseg = null;
          String queryses = null;
          if (ts.getAi18n()) {
            UsPrf upf = (UsPrf) pRvs.get("upf");
            CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
            String lang = upf.getLng().getIid();
            String langDef = cpf.getLngDef().getIid();
            if (!lang.equals(langDef)) {
              if (tcat.getCatl().getHsGds()) {
                queryg = lazyGetQuItInLstCaIn().replace(":ITTYP", "0")
      .replace(":TITCAT", "ITMCTL").replace(":FLTCAT", whereCatl)
    .replace(":WHEREADD", whereAdd).replace(":LNG", lang);
              }
              if (tcat.getCatl().getHsSrv()) {
                querys = lazyGetQuItInLstCaIn().replace(":ITTYP", "1")
    .replace(":TITCAT", "SRVCTL").replace(":FLTCAT", whereCatl)
  .replace(":WHEREADD", whereAdd).replace(":LNG", lang);
              }
              if (tcat.getCatl().getHsSgo()) {
                queryseg = lazyGetQuItInLstCaIn().replace(":ITTYP", "2")
     .replace(":TITCAT", "SEITMCTL").replace(":FLTCAT", whereCatl)
   .replace(":WHEREADD", whereAdd).replace(":LNG", lang);
              }
              if (tcat.getCatl().getHsSse()) {
                queryses = lazyGetQuItInLstCaIn().replace(":ITTYP", "3")
            .replace(":TITCAT", "SESRVCTL").replace(":FLTCAT", whereCatl)
          .replace(":WHEREADD", whereAdd).replace(":LNG", lang);
              }
            }
          }
          if (tcat.getCatl().getHsGds() && queryg == null) {
            queryg = lazyGetQuItInLstCa().replace(":ITTYP", "0")
      .replace(":TITCAT", "ITMCTL").replace(":FLTCAT", whereCatl)
    .replace(":WHEREADD", whereAdd);
          }
          if (tcat.getCatl().getHsSrv() && querys == null) {
            querys = lazyGetQuItInLstCa().replace(":ITTYP", "1")
    .replace(":TITCAT", "SRVCTL").replace(":FLTCAT", whereCatl)
  .replace(":WHEREADD", whereAdd);
          }
          if (tcat.getCatl().getHsSgo() && queryseg == null) {
            queryseg = lazyGetQuItInLstCa().replace(":ITTYP", "2")
     .replace(":TITCAT", "SEITMCTL").replace(":FLTCAT", whereCatl)
   .replace(":WHEREADD", whereAdd);
          }
          if (tcat.getCatl().getHsSse() && queryses == null) {
            queryses = lazyGetQuItInLstCa().replace(":ITTYP", "3")
         .replace(":TITCAT", "SESRVCTL").replace(":FLTCAT", whereCatl)
       .replace(":WHEREADD", whereAdd);
          }
          String querygRc = null;
          if (tcat.getCatl().getHsGds()) {
            querygRc = lazyGetQuItInLstCaTo().replace(":ITTYP", "0")
      .replace(":TITCAT", "ITMCTL").replace(":FLTCAT", whereCatl)
    .replace(":WHEREADD", whereAdd);
          }
          String querysRc = null;
          if (tcat.getCatl().getHsSrv()) {
            querysRc = lazyGetQuItInLstCaTo().replace(":ITTYP", "1")
    .replace(":TITCAT", "SRVCTL").replace(":FLTCAT", whereCatl)
  .replace(":WHEREADD", whereAdd);
          }
          String querysegRc = null;
          if (tcat.getCatl().getHsSgo()) {
            querysegRc = lazyGetQuItInLstCaTo().replace(":ITTYP", "2")
     .replace(":TITCAT", "SEITMCTL").replace(":FLTCAT", whereCatl)
    .replace(":WHEREADD", whereAdd);
          }
          String querysesRc = null;
          if (tcat.getCatl().getHsSse()) {
            querysesRc = lazyGetQuItInLstCaTo().replace(":ITTYP", "3")
             .replace(":TITCAT", "SESRVCTL").replace(":FLTCAT", whereCatl)
              .replace(":WHEREADD", whereAdd);
          }
          boolean dbgSh = getLog().getDbgSh(this.getClass(), 13100);
          if (fltSpfs != null) {
            if (dbgSh) {
              getLog().debug(pRvs, WsPg.class,
                "filters apecifics: size: " + fltSpfs.size());
            }
            FltsSpfWhe whereSpec = revWheSpec(fltSpfs);
            if (whereSpec != null) {
              if (queryg != null) {
                String querySpec = lazyGetQuItSpFlt()
      .replace(":TITSPEC", "ITMSPF").replace(":WHESPITFLR", whereSpec
    .getWhere()).replace(":SPITFLTCO", whereSpec.getWhereCount().toString());
                queryg += querySpec;
                querygRc += querySpec;
              }
              if (querys != null) {
                String querySpec = lazyGetQuItSpFlt()
      .replace(":TITSPEC", "SRVSPF").replace(":WHESPITFLR", whereSpec
    .getWhere()).replace(":SPITFLTCO", whereSpec.getWhereCount().toString());
                querys += querySpec;
                querysRc += querySpec;
              }
              if (queryseg != null) {
                String querySpec = lazyGetQuItSpFlt()
      .replace(":TITSPEC", "SEITMSPF").replace(":WHESPITFLR", whereSpec
    .getWhere()).replace(":SPITFLTCO", whereSpec.getWhereCount().toString());
                queryseg += querySpec;
                querysegRc += querySpec;
              }
              if (queryses != null) {
                String querySpec = lazyGetQuItSpFlt()
      .replace(":TITSPEC", "SESRVSPF").replace(":WHESPITFLR", whereSpec
    .getWhere()).replace(":SPITFLTCO", whereSpec.getWhereCount().toString());
                queryses += querySpec;
                querysesRc += querySpec;
              }
            }
          }
          if (queryg != null || querys != null || queryseg != null
            || queryses != null) {
            String query = null;
            String queryRc = null;
            if (queryg != null) {
              query = queryg;
              queryRc = querygRc;
            }
            if (querys != null) {
              if (query == null) {
                query = querys;
                queryRc = querysRc;
              } else {
                query += "\n union all \n" + querys;
                queryRc += "\n union all \n" + querysRc;
              }
            }
            if (queryseg != null) {
              if (query == null) {
                query = queryseg;
                queryRc = querysegRc;
              } else {
                query += "\n union all \n" + queryseg;
                queryRc += "\n union all \n" + querysegRc;
              }
            }
            if (queryses != null) {
              if (query == null) {
                query = queryses;
                queryRc = querysesRc;
              } else {
                query += "\n union all \n" + queryses;
                queryRc += "\n union all \n" + querysesRc;
              }
            }
            queryRc = "select count(*) as TROWS from (" + queryRc
              + ") as ALLTOT";
            if (orderBy != null) {
              query += orderBy;
            }
            queryRc += ";";
            Integer rowCount = this.rdb.evInt(queryRc, "TROWS");
            String[] ndFds = new String[] {"typ", "itId", "nme", "img", "specs",
              "pri", "priPr", "quan", "detMt"};
            Arrays.sort(ndFds);
            vs.put("ItlistndFds", ndFds);
            String pageStr = pRqDt.getParam("page");
            Integer page;
            if (pageStr != null) {
              page = Integer.valueOf(pageStr);
            } else {
              page = 1;
            }
            CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
            int totalPages = this.srvPg.evPgCnt(rowCount, cpf.getPgSz());
            if (page > totalPages) {
              page = totalPages;
            }
            int firstResult = (page - 1) * cpf.getPgSz(); //0-20,20-40
            List<Itlist> itList = getOrm().retPgQu(pRvs, vs, Itlist.class,
              query, firstResult, cpf.getPgSz()); vs.clear();
            if (ts.getPriCus() && cart != null
              && cart.getBuyr().getEml() != null) {
              List<BurPric> burPrics = getOrm().retLstCnd(pRvs, vs,
                BurPric.class, "where BUYR=" + cart.getBuyr().getIid());
              if (burPrics.size() > 1) {
                this.log.error(pRvs, WsPg.class,
                  "Several price category for same buyer! buyer ID="
                    + cart.getBuyr().getIid());
                throw new ExcCode(ExcCode.WRCN,
                  "several_price_category_for_same_buyer");
              }
              if (burPrics.size() == 1) {
                StringBuffer sbg = null;
                StringBuffer sbs = null;
                StringBuffer sbsg = null;
                StringBuffer sbss = null;
                for (Itlist iil : itList) {
                  if (iil.getTyp().equals(EItmTy.GOODS)) {
                    if (sbg == null) {
                      sbg = new StringBuffer();
                      sbg.append("(" + iil.getItId());
                    } else {
                      sbg.append("," + iil.getItId());
                    }
                  } else if (iil.getTyp().equals(EItmTy.SERVICE)) {
                    if (sbs == null) {
                      sbs = new StringBuffer();
                      sbs.append("(" + iil.getItId());
                    } else {
                      sbs.append("," + iil.getItId());
                    }
                  } else if (iil.getTyp().equals(EItmTy.SEGOODS)) {
                    if (sbsg == null) {
                      sbsg = new StringBuffer();
                      sbsg.append("(" + iil.getItId());
                    } else {
                      sbsg.append("," + iil.getItId());
                    }
                  } else if (iil.getTyp().equals(EItmTy.SESERVICE)) {
                    if (sbss == null) {
                      sbss = new StringBuffer();
                      sbss.append("(" + iil.getItId());
                    } else {
                      sbss.append("," + iil.getItId());
                    }
                  }
                }
                StringBuffer sbq = null;
                if (sbg != null) {
                  sbq = new StringBuffer();
                  sbq.append(
            "select 0 as VER, ITM, PRI from PRIITM where ITM in " + sbg + ")");
                }
                if (sbs != null) {
                  if (sbq == null) {
                    sbq = new StringBuffer();
                  } else {
                    sbq.append("\n union all \n");
                  }
                  sbq.append(
            "select 1 as VER, ITM, PRI from PRISRV where ITM in " + sbs + ")");
                }
                if (sbsg != null) {
                  if (sbq == null) {
                    sbq = new StringBuffer();
                  } else {
                    sbq.append("\n union all \n");
                  }
                  sbq.append(
          "select 2 as VER, ITM, PRI from SEITMPRI where ITM in " + sbsg + ")");
                }
                if (sbss != null) {
                  if (sbq == null) {
                    sbq = new StringBuffer();
                  } else {
                    sbq.append("\n union all \n");
                  }
                  sbq.append(
          "select 3 as VER, ITM, PRI from SEPRISRV where ITM in " + sbss + ")");
                }
                if (sbq != null) {
                  sbq.append(";");
                  String[] ndFlPr = new String[] {"itm", "pri",
                    "ver"}; //actually item type!
                  Arrays.sort(ndFlPr);
                  vs.put("PriItmndFds", ndFlPr);
                  vs.put("ItmdpLv", 0);
                  List<PriItm> prcs = this.orm.retLstQu(pRvs, vs, PriItm.class,
                    sbq.toString());
                  vs.clear();
                  for (PriItm pri : prcs) {
                    for (Itlist iil : itList) {
                      long itTyp = iil.getTyp().ordinal();
                      if (iil.getItId().equals(pri.getItm().getIid())
                        && itTyp == pri.getVer()) {
                        iil.setPri(pri.getPri());
                        break;
                      }
                    }
                  }
                }
              }
            }
            List<Page> pages = this.srvPg.evPgs(page, totalPages,
              cpf.getPgTl());
            pRvs.put("pages", pages);
            pRvs.put("itList", itList);
            pRvs.put("totalItems", rowCount);
          }
          if (fltPri != null) {
            pRvs.put("fltPri", fltPri);
          }
          if (fltCatl != null && fltCatl.getAll().size() > 0) {
            pRvs.put("fltCatl", fltCatl);
          }
          if (fltSpfs != null) {
            pRvs.put("fltSpfs", fltSpfs);
          }
          pRvs.put("catl", tcat.getCatl());
        }
      }
      pRqDt.setAttr("rnd", "webstore");
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
   * <p>Build catls in lazy mode.</p>
   * @param pRvs params
   * @return trading catls
   * @throws Exception - an exception
   **/
  public final List<TrCatl> lazCatls(
    final Map<String, Object> pRvs) throws Exception {
    if (this.catls == null) {
      synchronized (this) {
        if (this.catls == null) {
          Map<String, Object> vs = new HashMap<String, Object>();
          List<CatGs> catlsGs = getOrm().retLstCnd(pRvs, vs, CatGs.class,
            "order by IDX");
          vs.put("CatGsdpLv", 0); //only ID
          for (CatGs cat : catlsGs) {
            cat.setUsedSpecs(getOrm().retLstCnd(pRvs, vs, CatSp.class,
              "where OWNR=" + cat.getIid() + " order by IDX"));
          }
          vs.clear();
          //only ID:
          vs.put("SubCatdpLv", 1);
          List<SubCat> scList = getOrm().retLst(pRvs, vs, SubCat.class);
          vs.clear();
          List<TrCatl> result = new ArrayList<TrCatl>();
          Set<Long> firstLevel = new HashSet<Long>();
          Set<Long> allLevels = new HashSet<Long>();
          for (SubCat catSubc : scList) {
            firstLevel.add(catSubc.getCatl().getIid());
            allLevels.add(catSubc.getCatl().getIid());
            allLevels.add(catSubc.getSucat().getIid());
          }
          for (SubCat catSubc : scList) {
            firstLevel.remove(catSubc.getSucat().getIid());
          }
          //first level is from tree and not (that has no sub-catlsGs)
          for (Long id : firstLevel) {
            TrCatl tc = new TrCatl();
            tc.setCatl(findCatGsById(catlsGs, id));
            result.add(tc);
          }
          for (CatGs cat : catlsGs) {
            boolean inTree = false;
            for (Long id : allLevels) {
              if (cat.getIid().equals(id)) {
                inTree = true;
                break;
              }
            }
            if (!inTree) {
              TrCatl tc = new TrCatl();
              tc.setCatl(findCatGsById(catlsGs, cat.getIid()));
              result.add(tc);
            }
          }
          //2-nd .. levels:
          retSubcatls(result, catlsGs, scList);
          //Sorting all levels recursively:
          sortCatls(result);
          refrCatlsFlts(pRvs,  result);
          this.catls = result;
        }
      }
    }
    return this.catls;
  }

  /**
   * <p>Refresh catl filters, the first catl in which filter is enabled
   * is propagated into sub-catls.</p>
   * @param pRvs params
   * @param pCurLst Catl List current
   * @throws Exception an Exception
   **/
  public final void refrCatlsFlts(final Map<String, Object> pRvs,
    final List<TrCatl> pCurLst) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    vs.put("CatGsdpLv", 0); //only ID
    for (TrCatl tc : pCurLst) {
      if (tc.getCatl().getFlAvl() || tc.getCatl().getFlSpe()
        || tc.getCatl().getFlSub() || tc.getCatl().getFlPi()) {
        if (tc.getCatl().getFlSpe()) {
          tc.getCatl().setUsedSpecs(getOrm().retLstCnd(pRvs, vs, CatSp.class,
            "where OWNR=" + tc.getCatl().getIid() + " order by IDX"));
        }
        prpgCatlStg(tc);
      } else if (tc.getSubcatls().size() > 0) {
        tc.getCatl().setUsedSpecs(null); //reset if not null
        //recursion:
        refrCatlsFlts(pRvs, tc.getSubcatls());
      } else {
        tc.getCatl().setUsedSpecs(null); //reset if not null
      }
    }
  }

  /**
   * <p>Sort recursively catls in tree.</p>
   * @param pCurLst Catl List current
   **/
  public final void sortCatls(final List<TrCatl> pCurLst) {
    Collections.sort(pCurLst, this.cmpCatls);
    for (TrCatl tc : pCurLst) {
      if (tc.getSubcatls().size() > 0) {
        sortCatls(tc.getSubcatls());
      }
    }
  }

  /**
   * <p>Reveal filter price for catl and fill it from request data.</p>
   * @param pTcat Catl
   * @param pRvs params
   * @param pRqDt Request Data
   * @return filter price or null if catl hasn't any filter
   * @throws Exception an Exception
   **/
  public final FltInt revFltPri(final TrCatl pTcat,
    final Map<String, Object> pRvs, final IReqDt pRqDt) throws Exception {
    if (pTcat.getCatl().getFlSpe() || pTcat.getCatl().getFlSub()
      || pTcat.getCatl().getFlAvl() || pTcat.getCatl().getFlPi()) {
      FltInt res = new FltInt();
      String operStr = pRqDt.getParam("fltPriOp");
      String val1Str = pRqDt.getParam("fltPriVal1");
      CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
      String dgsep = cpf.getDcGrSpv();
      if (operStr != null && !"".equals(operStr)
        && val1Str != null && !"".equals(val1Str)) {
        res.setOpr(Enum.valueOf(EFltOpr.class, operStr));
        if (dgsep != null) {
          val1Str = val1Str.replace(dgsep, "");
        }
        res.setVal1(Integer.valueOf(val1Str));
        String val2Str = pRqDt.getParam("fltPriVal2");
        if (val2Str != null && !"".equals(val2Str)) {
          if (dgsep != null) {
            val2Str = val2Str.replace(dgsep, "");
          }
          res.setVal2(Integer.valueOf(val2Str));
        }
      }
      return res;
    } else {
      return null;
    }
  }

  /**
   * <p>Reveal filters specifics for given catl
   * and fill it from request data.</p>
   * @param pTcat Catl
   * @param pRvs params
   * @param pRqDt Request Data
   * @return filter specifics or null
   * @throws Exception an Exception
   **/
  public final List<FltSpf> revFltsSpec(final TrCatl pTcat,
    final Map<String, Object> pRvs, final IReqDt pRqDt) throws Exception {
    if (pTcat.getCatl().getUsedSpecs() != null
      && pTcat.getCatl().getUsedSpecs().size() > 0) {
      List<FltSpf> res = new ArrayList<FltSpf>();
      CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
      String dsep = cpf.getDcSpv();
      String dgsep = cpf.getDcGrSpv();
      for (CatSp cs : pTcat.getCatl().getUsedSpecs()) {
        String operStr = pRqDt.getParam("fltSp" + cs.getSpec().getIid() + "Op");
        if (cs.getSpec().getChSpTy() != null
        && cs.getSpec().getChSpTy().getIid() != null) {
          FltItms<ChoSp> fltItms = new FltItms<ChoSp>();
          FltSpf spf = new FltSpf();
          spf.setFlt(fltItms);
          spf.setCatSpf(cs);
          res.add(spf);
          fltItms.setAll(retAllChSpec(pRvs, cs.getSpec().getChSpTy()));
          String[] valStrs = pRqDt.getParamVls("fltSp"
            + cs.getSpec().getIid() + "Val");
          if (operStr != null && !"".equals(operStr)
            && valStrs != null && valStrs.length > 0) {
            fltItms.setOpr(Enum.valueOf(EFltOpr.class, operStr));
            for (String idStr : valStrs) {
              Long id = Long.valueOf(idStr);
              ChoSp chs = fndChoSpById(fltItms
                .getAll(), id);
              if (chs == null) {
                this.log.warn(pRvs, WsPg.class,
                  "Can't find chspecifics #: " + id);
              } else {
                fltItms.getItms().add(chs);
              }
            }
          }
        } else if (EItmSpTy.INTEGER.equals(cs.getSpec().getTyp())) {
          FltInt flt = new FltInt();
          FltSpf spf = new FltSpf();
          spf.setFlt(flt);
          spf.setCatSpf(cs);
          res.add(spf);
          String val1Str = pRqDt.getParam("fltSp"
            + cs.getSpec().getIid() + "Val1");
          if (operStr != null && !"".equals(operStr) && val1Str != null
            && val1Str.length() > 0) {
            flt.setOpr(Enum.valueOf(EFltOpr.class, operStr));
            if (dgsep != null) {
              val1Str = val1Str.replace(dgsep, "");
            }
            flt.setVal1(Integer.valueOf(val1Str));
            String val2Str = pRqDt.getParam("fltSp"
              + cs.getSpec().getIid() + "Val2");
            if (val2Str != null && val2Str.length() > 0) {
              if (dgsep != null) {
                val2Str = val2Str.replace(dgsep, "");
              }
              flt.setVal2(Integer.valueOf(val2Str));
            }
          }
        } else if (EItmSpTy.BIGDECIMAL.equals(cs.getSpec().getTyp())) {
          FltBgd flt = new FltBgd();
          FltSpf spf = new FltSpf();
          spf.setFlt(flt);
          spf.setCatSpf(cs);
          res.add(spf);
          String val1Str = pRqDt.getParam("fltSp"
            + cs.getSpec().getIid() + "Val1");
          if (operStr != null && !"".equals(operStr)
            && val1Str != null && val1Str.length() > 0) {
            flt.setOpr(Enum.valueOf(EFltOpr.class, operStr));
            if (dgsep != null) {
              val1Str = val1Str.replace(dgsep, "");
            }
            if (dsep != null) {
              val1Str = val1Str.replace(dsep, ".");
            }
            flt.setVal1(new BigDecimal(val1Str));
            String val2Str = pRqDt.getParam("fltSp"
              + cs.getSpec().getIid() + "Val2");
            if (val2Str != null && val2Str.length() > 0) {
              if (dgsep != null) {
                val2Str = val2Str.replace(dgsep, "");
              }
              if (dsep != null) {
                val2Str = val2Str.replace(dsep, ".");
              }
              flt.setVal2(new BigDecimal(val2Str));
            }
          }
        } else {
          this.log.error(pRvs, WsPg.class,
            "Flt specifics not implemented yet, for - "
              + cs.getSpec().getNme());
        }
      }
      return res;
    } else {
      return null;
    }
  }
  /**
   * <p>Retrieve all choseeable specifics.</p>
   * @param pRvs params
   * @param pChoSpTy Ch-Spec Type
   * @return List Ch-Spec
   * @throws Exception an Exception
   **/
  public final List<ChoSp> retAllChSpec(final Map<String, Object> pRvs,
    final ChoSpTy pChoSpTy) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    vs.put("ChoSpdpLv", 1);
    return getOrm().retLstCnd(pRvs, vs, ChoSp.class,
      "where CHOTY=" + pChoSpTy.getIid());
  }

  /**
   * <p>Find chooseable specifics in given list by ID.</p>
   * @param pChoSps List Ch-Spec
   * @param pId Id
   * @return chooseable specifics or null
   **/
  public final ChoSp fndChoSpById(final List<ChoSp> pChoSps, final Long pId) {
    for (ChoSp chs : pChoSps) {
      if (chs.getIid().equals(pId)) {
        return chs;
      }
    }
    return null;
  }


  /**
   * <p>Reveal filter catl for catl and fill it from request data.</p>
   * @param pTcat Catl
   * @param pRvs params
   * @param pRqDt Request Data
   * @return filter catl or null if catl hasn't any filter
   * @throws Exception an Exception
   **/
  public final FltItms<CatGs> revFltCatl(final TrCatl pTcat,
    final Map<String, Object> pRvs, final IReqDt pRqDt) throws Exception {
    if (pTcat.getCatl().getFlSub()) {
      FltItms<CatGs> res = new FltItms<CatGs>();
      copSubcats(pTcat, res.getAll());
      String operStr = pRqDt.getParam("fltCtOp");
      String[] valStrs = pRqDt.getParamVls("fltCtVal");
      if (operStr != null && !"".equals(operStr)
        && valStrs != null && valStrs.length > 0) {
        res.setOpr(Enum.valueOf(EFltOpr.class, operStr));
        for (String idStr : valStrs) {
          Long id = Long.valueOf(idStr);
          CatGs cgs = findSubcatByIdInTc(pTcat, id);
          if (cgs == null) {
            throw new Exception("Algorithm error! Can't find subcatl #/in: "
              + id + "/" + pTcat.getCatl().getNme());
          }
          res.getItms().add(cgs);
        }
      }
      return res;
    } else {
      return null;
    }
  }

  /**
   * <p>Reveal part of where clause e.g. " and PRI<21" or empty string.</p>
   * @param pFltPri Flt Price
   * @param pCuRt currency rate
   * @return part of where clause e.g. " and PRI<21" or empty string
   * @throws Exception an Exception
   **/
  public final String revWhePri(final FltInt pFltPri,
    final BigDecimal pCuRt) throws Exception {
    if (pFltPri == null || pFltPri.getOpr() == null
      || pFltPri.getVal1() == null) {
      return "";
    }
    BigDecimal exchRate = pCuRt;
    if (exchRate.compareTo(BigDecimal.ZERO) == -1) {
      exchRate = BigDecimal.ONE.divide(exchRate.negate(), 15,
        RoundingMode.HALF_UP);
    }
    int val1 = pFltPri.getVal1();
    if (exchRate.compareTo(BigDecimal.ONE) != 0) {
      BigDecimal bd = new BigDecimal(val1);
      bd = bd.divide(exchRate, 0, RoundingMode.HALF_UP);
      val1 = bd.intValue();
    }
    if (EFltOpr.LESS_THAN.equals(pFltPri.getOpr())
      || EFltOpr.LESS_THAN_EQUAL.equals(pFltPri.getOpr())
        || EFltOpr.GREATER_THAN.equals(pFltPri.getOpr())
          || EFltOpr.GREATER_THAN_EQUAL.equals(pFltPri.getOpr())) {
      return " and PRI" + toSqlOpr(pFltPri.getOpr()) + val1;
    }
    if (pFltPri.getVal2() != null) {
      int val2 = pFltPri.getVal2();
      if (exchRate.compareTo(BigDecimal.ONE) != 0) {
        BigDecimal bd = new BigDecimal(val2);
        bd = bd.divide(exchRate, 0, RoundingMode.HALF_UP);
        val2 = bd.intValue();
      }
      if (EFltOpr.BETWEEN.equals(pFltPri.getOpr())) {
        return " and PRI>" + val1 + " and PRI<" + val2;
      } else if (EFltOpr.BETWEEN_INCLUDE.equals(pFltPri.getOpr())) {
        return " and PRI>=" + val1 + " and PRI<=" + val2;
      }
    }
    return "";
  }

  /**
   * <p>Reveal part of where specifics clause e.g.:
   * "(SPEC=3 and LNG1 in (3, 14))
   * or (SPEC=4 and NUM1&lt;2.33)",
   * and count of conditions, or null.</p>
   * @param pFltsSpf Flts Spec
   * @return FltsSpfWhe bundle or null
   * @throws Exception an Exception
   **/
  public final FltsSpfWhe revWheSpec(
    final List<FltSpf> pFltsSpf) throws Exception {
    FltsSpfWhe result = null;
    StringBuffer sb = new StringBuffer();
    boolean isFst = true;
    for (FltSpf sf : pFltsSpf) {
      if (sf.getFlt().getOpr() != null) {
        if (isFst) {
          sb.append("(SPEC=" + sf.getCatSpf().getSpec().getIid() + " and ");
          isFst = false;
        } else {
          sb.append(" or (SPEC=" + sf.getCatSpf().getSpec().getIid() + " and ");
        }
        if (sf.getFlt().getClass() == FltItms.class) {
          @SuppressWarnings("unchecked")
          FltItms<ChoSp> fltItms = (FltItms<ChoSp>) sf.getFlt();
          sb.append("LNG1");
          if (EFltOpr.IN.equals(fltItms.getOpr())
            && fltItms.getItms().size() == 1) {
            sb.append("=" + fltItms.getItms().get(0).getIid());
          } else if (EFltOpr.NOT_IN.equals(fltItms.getOpr())
            && fltItms.getItms().size() == 1) {
            sb.append("!=" + fltItms.getItms().get(0).getIid());
          } else {
            if (EFltOpr.IN.equals(fltItms.getOpr())) {
              sb.append(" in (");
            } else {
              sb.append(" not in (");
            }
            boolean isFstItm = true;
            for (ChoSp chs : fltItms.getItms()) {
              if (isFstItm) {
                isFstItm = false;
              } else {
                sb.append(",");
              }
              sb.append(chs.getIid());
            }
            sb.append(")");
          }
        } else if (sf.getFlt().getClass() == FltInt.class) {
          FltInt flt = (FltInt) sf.getFlt();
          sb.append("LNG1");
          if (EFltOpr.LESS_THAN.equals(flt.getOpr())
            || EFltOpr.LESS_THAN_EQUAL.equals(flt.getOpr())
              || EFltOpr.GREATER_THAN.equals(flt.getOpr())
                || EFltOpr.GREATER_THAN_EQUAL.equals(flt.getOpr())) {
            sb.append(toSqlOpr(flt.getOpr()) + flt.getVal1());
          } else if (EFltOpr.BETWEEN.equals(flt.getOpr())) {
            sb.append(">" + flt.getVal1() + " and LNG1<" + flt.getVal2());
          } else if (EFltOpr.BETWEEN_INCLUDE.equals(flt.getOpr())) {
            sb.append(">=" + flt.getVal1() + " and LNG1<=" + flt.getVal2());
          } else {
            throw new Exception(
              "Algorithm error for where integer specifics/operator: "
                + sf.getCatSpf().getSpec().getNme() + "/" + flt.getOpr());
          }
        } else if (sf.getFlt().getClass() == FltBgd.class) {
          FltBgd flt = (FltBgd) sf.getFlt();
          sb.append("NUM1");
          if (EFltOpr.LESS_THAN.equals(flt.getOpr())
            || EFltOpr.LESS_THAN_EQUAL.equals(flt.getOpr())
              || EFltOpr.GREATER_THAN.equals(flt.getOpr())
                || EFltOpr.GREATER_THAN_EQUAL.equals(flt.getOpr())) {
            sb.append(toSqlOpr(flt.getOpr()) + flt.getVal1());
          } else if (EFltOpr.BETWEEN.equals(flt.getOpr())) {
            sb.append(">" + flt.getVal1() + " and NUM1<" + flt.getVal2());
          } else if (EFltOpr.BETWEEN_INCLUDE.equals(flt.getOpr())) {
            sb.append(">=" + flt.getVal1() + " and NUM1<=" + flt.getVal2());
          } else {
            throw new Exception(
              "Algorithm error for where integer specifics/operator: "
                + sf.getCatSpf().getSpec().getNme() + "/" + flt.getOpr());
          }
        } else {
          throw new Exception(
            "Making WHERE not implemented for specifics/filter: "
          + sf.getCatSpf().getSpec().getNme() + "/" + sf.getFlt().getClass());
        }
        sb.append(")");
        if (result == null) {
          result = new FltsSpfWhe();
        }
        result.setWhereCount(result.getWhereCount() + 1);
      }
    }
    if (result != null) {
      result.setWhere(sb.toString());
    }
    return result;
  }

  /**
   * <p>Reveal part of where catl clause e.g. "=12",
   * " in (12,14)".</p>
   * @param pTcat Catl
   * @param pFltCatl Flt Catl
   * @return part of where clause e.g. "=12", " in (12,14)"
   * @throws Exception an Exception
   **/
  public final String revealWhereCatl(final TrCatl pTcat,
    final FltItms<CatGs> pFltCatl) throws Exception {
    List<CatGs> subcgsAll = new ArrayList<CatGs>();
    subcgsAll.add(pTcat.getCatl());
    if (pFltCatl != null && pFltCatl.getOpr() != null
      && pFltCatl.getItms().size() > 0) {
      for (CatGs cgs : pFltCatl.getItms()) {
        TrCatl tcat = findTrCatlById(this.catls, cgs.getIid());
        subcgsAll.add(cgs);
        copSubcats(tcat, subcgsAll);
      }
    } else {
      copSubcats(pTcat, subcgsAll);
    }
    Set<CatGs> subcgs = new HashSet<CatGs>();
    for (CatGs cgs : subcgsAll) {
      if (!cgs.getHsSub()) {
        subcgs.add(cgs);
      }
    }
    if (subcgs.size() > 1) {
      StringBuffer sb = new StringBuffer(" in (" + pTcat.getCatl().getIid());
      for (CatGs cgs : subcgs) {
        sb.append("," + cgs.getIid());
      }
      sb.append(")");
      return sb.toString();
    } else {
      return "=" + subcgs.iterator().next().getIid();
    }
  }

  /**
   * <p>Convert from EFltOpr to SQL one.</p>
   * @param pFltOpr EFltOpr
   * @return SQL operator
   * @throws Exception if not found
   **/
  public final String toSqlOpr(final EFltOpr pFltOpr) throws Exception {
    if (EFltOpr.LESS_THAN.equals(pFltOpr)) {
      return "<";
    }
    if (EFltOpr.LESS_THAN_EQUAL.equals(pFltOpr)) {
      return "<=";
    }
    if (EFltOpr.GREATER_THAN.equals(pFltOpr)) {
      return ">";
    }
    if (EFltOpr.GREATER_THAN_EQUAL.equals(pFltOpr)) {
      return ">=";
    }
    throw new ExcCode(ExcCode.WR,
      "Algorithm error! Cant match SQL operator to EFltOpr: " + pFltOpr);
  }

  /**
   * <p>Set filters/orders/has goods/services...
   * for all sub-catls same as main-catl.</p>
   * @param pMainCatl main catl
   * @throws Exception an Exception
   **/
  public final void prpgCatlStg(final TrCatl pMainCatl) throws Exception {
    for (TrCatl tc : pMainCatl.getSubcatls()) {
      //copy filters/specifics:
      tc.getCatl().setFlAvl(pMainCatl.getCatl().getFlAvl());
      tc.getCatl().setFlSpe(pMainCatl.getCatl().getFlSpe());
      tc.getCatl().setFlSub(pMainCatl.getCatl().getFlSub());
      tc.getCatl().setFlPi(pMainCatl.getCatl().getFlPi());
      tc.getCatl().setUsedSpecs(pMainCatl.getCatl().getUsedSpecs());
      tc.getCatl().setHsGds(pMainCatl.getCatl().getHsGds());
      tc.getCatl().setHsSrv(pMainCatl.getCatl().getHsSrv());
      tc.getCatl().setHsSgo(pMainCatl.getCatl().getHsSgo());
      tc.getCatl().setHsSse(pMainCatl.getCatl().getHsSse());
      if (tc.getSubcatls().size() > 0) {
        //recursion:
        prpgCatlStg(tc);
      }
    }
  }

  /**
   * <p>Retrieve recursively sub-catls for current level catls.</p>
   * @param pCurLst Catl List current
   * @param pCatls CatGs List
   * @param pSubCats Catls-Subcatls
   * @throws Exception an Exception
   **/
  public final void retSubcatls(final List<TrCatl> pCurLst,
    final List<CatGs> pCatls, final List<SubCat> pSubCats) throws Exception {
    for (TrCatl tc : pCurLst) {
      for (SubCat catSubc : pSubCats) {
        if (tc.getCatl().getIid().equals(catSubc.getCatl()
          .getIid())) {
          TrCatl tci = new TrCatl();
          tci.setCatl(findCatGsById(pCatls, catSubc.getSucat().getIid()));
          tc.getSubcatls().add(tci);
        }
      }
      if (tc.getSubcatls().size() > 0) {
        //recursion:
        retSubcatls(tc.getSubcatls(), pCatls, pSubCats);
      }
    }
  }

  /**
   * <p>Find catl GS by ID.</p>
   * @param pCatls Catl List
   * @param pId Catl ID
   * @return CatGs Catl
   * @throws Exception if not found
   **/
  public final CatGs findCatGsById(final List<CatGs> pCatls,
    final Long pId) throws Exception {
    for (CatGs cat : pCatls) {
      if (cat.getIid().equals(pId)) {
        return cat;
      }
    }
    throw new Exception("Algorithm error! Can't find catl #" + pId);
  }

  /**
   * <p>Find trading catl by ID.</p>
   * @param pCatls trading catls
   * @param pId Catl ID
   * @return Trading Catl
   **/
  public final TrCatl findTrCatlById(final List<TrCatl> pCatls,
    final Long pId) {
    for (TrCatl cat : pCatls) {
      if (cat.getCatl().getIid().equals(pId)) {
        return cat;
      }
      if (cat.getSubcatls().size() > 0) {
        //recursion:
        TrCatl tc = findTrCatlById(cat.getSubcatls(), pId);
        if (tc != null) {
          return tc;
        }
      }
    }
    return null;
  }

  /**
   * <p>Copy sub-catl-GS from given catl-T to given set-CGS.</p>
   * @param pCatl trading catl
   * @param pCatlsGs given set-CGS
   **/
  public final void copSubcats(final TrCatl pCatl, final List<CatGs> pCatlsGs) {
    for (TrCatl cat : pCatl.getSubcatls()) {
      pCatlsGs.add(cat.getCatl());
      for (TrCatl cati : cat.getSubcatls()) {
        pCatlsGs.add(cati.getCatl());
        //recursion:
        copSubcats(cati, pCatlsGs);
      }
    }
  }

  /**
   * <p>Find sub-catl-GS by ID in root catl-T.</p>
   * @param pCatl trading catl
   * @param pId Catl ID
   * @return Catl GS
   **/
  public final CatGs findSubcatByIdInTc(final TrCatl pCatl, final Long pId) {
    for (TrCatl cat : pCatl.getSubcatls()) {
      if (cat.getCatl().getIid().equals(pId)) {
        return cat.getCatl();
      }
      for (TrCatl cati : cat.getSubcatls()) {
        if (cati.getCatl().getIid().equals(pId)) {
          return cati.getCatl();
        }
        //recursion:
        CatGs cgs = findSubcatByIdInTc(cati, pId);
        if (cgs != null) {
          return cgs;
        }
      }
    }
    return null;
  }

  /**
   * <p>Lazy Get quItSpFlt.</p>
   * @return String
   * @throws Exception - an exception
   **/
  public final String lazyGetQuItSpFlt() throws Exception {
    if (this.quItSpFlt == null) {
      this.quItSpFlt = loadStr("/ws/itSpFlt.sql");
    }
    return this.quItSpFlt;
  }

  /**
   * <p>Lazy Get quItInLstCaIn.</p>
   * @return String
   * @throws Exception - an exception
   **/
  public final String lazyGetQuItInLstCaIn() throws Exception {
    if (this.quItInLstCaIn == null) {
      this.quItInLstCaIn = loadStr("/ws/itInLstCaIn.sql");
    }
    return this.quItInLstCaIn;
  }

  /**
   * <p>Lazy Get quItInLstCaTo.</p>
   * @return String
   * @throws Exception - an exception
   **/
  public final String lazyGetQuItInLstCaTo() throws Exception {
    if (this.quItInLstCaTo == null) {
      this.quItInLstCaTo = loadStr("/ws/itInLstCaTo.sql");
    }
    return this.quItInLstCaTo;
  }

  /**
   * <p>Lazy Get quItInLstCa.</p>
   * @return String
   * @throws Exception - an exception
   **/
  public final String lazyGetQuItInLstCa() throws Exception {
    if (this.quItInLstCa == null) {
      this.quItInLstCa = loadStr("/ws/itInLstCa.sql");
    }
    return this.quItInLstCa;
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFiNm file name
   * @return String usually SQL query
   * @throws IOException - IO exception
   **/
  public final String loadStr(final String pFiNm) throws IOException {
    URL urlFile = WsPg.class.getResource(pFiNm);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = WsPg.class.getResourceAsStream(pFiNm);
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
   * <p>Getter for srvPg.</p>
   * @return ISrvPg
   **/
  public final ISrvPg getSrvPg() {
    return this.srvPg;
  }

  /**
   * <p>Setter for srvPg.</p>
   * @param pSrvPg reference
   **/
  public final void setSrvPg(final ISrvPg pSrvPg) {
    this.srvPg = pSrvPg;
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
   * <p>Getter for cmpCatls.</p>
   * @return CmpTrCatl
   **/
  public final CmpTrCatl getCmpCatls() {
    return this.cmpCatls;
  }

  /**
   * <p>Setter for cmpCatls.</p>
   * @param pCmpCatls reference
   **/
  public final void setCmpCatls(final CmpTrCatl pCmpCatls) {
    this.cmpCatls = pCmpCatls;
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
}
