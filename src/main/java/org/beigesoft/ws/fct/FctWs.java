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

import org.beigesoft.mdl.IHasId;
import org.beigesoft.fct.IFcCsvDrt;
import org.beigesoft.fct.IFctAux;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.hnd.HndEntRq;
import org.beigesoft.prp.Setng;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.eis.IniEisFct;
import org.beigesoft.ws.hnd.HndTrd;
import org.beigesoft.ws.hld.HlSeEnPr;
import org.beigesoft.ws.hld.HlPrFeSe;
import org.beigesoft.ws.srv.IFiSeSel;
import org.beigesoft.ws.srv.FiSeSel;
import org.beigesoft.ws.srv.ISrAdStg;
import org.beigesoft.ws.srv.SrAdStg;
import org.beigesoft.ws.srv.ISrTrStg;
import org.beigesoft.ws.srv.SrTrStg;
import org.beigesoft.ws.srv.IBuySr;
import org.beigesoft.ws.srv.BuySr;
import org.beigesoft.ws.srv.ISrCart;
import org.beigesoft.ws.srv.SrCart;

/**
 * <p>Auxiliary factory for web-store additional services.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctWs<RS> implements IFctAux<RS> {

  /**
   * <p>Handler S.E. entities request name.</p>
   **/
  public static final String HNSEENRQ = "hnSeEnRq";

  /**
   * <p>Processor S.E. entities page name.</p>
   **/
  public static final String PRSEENTPG = "prSeEnPg";

  /**
   * <p>Creates requested bean and put into given main factory.
   * The main factory is already synchronized when invokes this.</p>
   * @param pRvs request scoped vars
   * @param pBnNm - bean name
   * @param pFctApp main factory
   * @return Object - requested bean or NULL
   * @throws Exception - an exception
   */
  @Override
  public final Object crePut(final Map<String, Object> pRvs,
    final String pBnNm, final FctBlc<RS> pFctApp) throws Exception {
    Object rz = null;
    if (HndTrd.class.getSimpleName().equals(pBnNm)) {
      rz = crPuHndTrd(pRvs, pFctApp);
    } else if (FcPrWs.class.getSimpleName().equals(pBnNm)) {
      rz = crPuFcPrWs(pRvs, pFctApp);
    } else if (IFcCsvDrt.class.getSimpleName().equals(pBnNm)) {
      rz = crPuFcCsvDrt(pRvs, pFctApp);
    } else if (IFiSeSel.class.getSimpleName().equals(pBnNm)) {
      rz = crPuFiSeSel(pRvs, pFctApp);
    } else if (ISrAdStg.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrAdStg(pRvs, pFctApp);
    } else if (ISrCart.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrCart(pRvs, pFctApp);
    } else if (IBuySr.class.getSimpleName().equals(pBnNm)) {
      rz = crPuBuySr(pRvs, pFctApp);
    } else if (ISrTrStg.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrTrStg(pRvs, pFctApp);
    } else if (HNSEENRQ.equals(pBnNm)) {
      rz = crPuHnSeEnRq(pRvs, pFctApp);
    }
    return rz;
  }

  /**
   * <p>Releases state when main factory is releasing.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @throws Exception - an exception
   */
  @Override
  public final void release(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    //nothing
  }

  /**
   * <p>Lazy getter handler S.E. entities.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return HndEntRq
   * @throws Exception - an exception
   */
  private HndEntRq<RS> crPuHnSeEnRq(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    HndEntRq<RS> rz = new HndEntRq<RS>();
    rz.setWriteTi(pFctApp.getFctDt().getWriteTi());
    rz.setReadTi(pFctApp.getFctDt().getReadTi());
    rz.setWriteReTi(pFctApp.getFctDt().getWriteReTi());
    rz.setWrReSpTr(pFctApp.getFctDt().getWrReSpTr());
    rz.setLogStd(pFctApp.lazLogStd(pRvs));
    rz.setLogSec(pFctApp.lazLogSec(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setFilEntRq(pFctApp.lazFilEntRq(pRvs));
    rz.setFctFctEnt(pFctApp.lazFctFctEnt(pRvs));
    rz.setHldPrcFenNm(new HlPrFeSe());
    FcPrFeSe<RS> fen = new FcPrFeSe<RS>();
    fen.setFctBlc(pFctApp);
    rz.setFctPrcFen(fen);
    HlSeEnPr hlSeEnPr = new HlSeEnPr();
    hlSeEnPr.setShrEnts(pFctApp.getFctDt().evShrEnts(IniEisFct.ID_SESEL));
    rz.setHldEntPrcNm(hlSeEnPr);
    rz.setFctEntPrc(pFctApp.lazFctEnPrc(pRvs));
    rz.setEntMap(new HashMap<String, Class<? extends IHasId<?>>>());
    Setng setng = pFctApp.lazStgUvd(pRvs);
    for (Class<? extends IHasId<?>> cls  : setng.lazClss()) {
      if (pFctApp.getFctDt().isEntAlwd(cls, IniEisFct.ID_SESEL)) {
        rz.getEntMap().put(cls.getSimpleName(), cls);
      }
    }
    pFctApp.put(pRvs, HNSEENRQ, rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(), HNSEENRQ
      + " has been created.");
    return rz;
  }

  /**
   * <p>Creates and puts into MF FcPrWs.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return FcPrWs
   * @throws Exception - an exception
   */
  private FcPrWs<RS> crPuFcPrWs(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    FcPrWs<RS> rz = new FcPrWs<RS>();
    rz.setFctBlc(pFctApp);
    pFctApp.put(pRvs, FcPrWs.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      FcPrWs.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF HndTrd.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return HndTrd
   * @throws Exception - an exception
   */
  private HndTrd<RS> crPuHndTrd(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    HndTrd<RS> rz = new HndTrd<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    @SuppressWarnings("unchecked")
    FcPrWs<RS> fcPrWs = (FcPrWs<RS>) pFctApp
      .laz(pRvs, FcPrWs.class.getSimpleName());
    rz.setFcPrWs(fcPrWs);
    rz.setLog(pFctApp.lazLogStd(pRvs));
    rz.setOrm(pFctApp.lazOrm(pRvs));
    ISrAdStg srAdStg = (ISrAdStg) pFctApp
      .laz(pRvs, ISrAdStg.class.getSimpleName());
    rz.setSrAdStg(srAdStg);
    ISrTrStg srTrStg = (ISrTrStg) pFctApp
      .laz(pRvs, ISrTrStg.class.getSimpleName());
    rz.setSrTrStg(srTrStg);
    pFctApp.put(pRvs, HndTrd.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      HndTrd.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF FiSeSel.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return FiSeSel
   * @throws Exception - an exception
   */
  private FiSeSel crPuFiSeSel(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    FiSeSel rz = new FiSeSel();
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setLog(pFctApp.lazLogStd(pRvs));
    pFctApp.put(pRvs, IFiSeSel.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      FiSeSel.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SrAdStg.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrAdStg
   * @throws Exception - an exception
   */
  private SrAdStg crPuSrAdStg(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrAdStg rz = new SrAdStg();
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setLog(pFctApp.lazLogStd(pRvs));
    pFctApp.put(pRvs, ISrAdStg.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrAdStg.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SrCart.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrCart
   * @throws Exception - an exception
   */
  private SrCart<RS> crPuSrCart(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrCart<RS> rz = new SrCart<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(pFctApp.lazOrm(pRvs));
    IBuySr buySr = (IBuySr) pFctApp
      .laz(pRvs, IBuySr.class.getSimpleName());
    rz.setBuySr(buySr);
    ISrTrStg srTrStg = (ISrTrStg) pFctApp
      .laz(pRvs, ISrTrStg.class.getSimpleName());
    rz.setSrTrStg(srTrStg);
    rz.setLog(pFctApp.lazLogStd(pRvs));
    rz.setNumStr(pFctApp.lazNumStr(pRvs));
    pFctApp.put(pRvs, ISrCart.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrCart.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF BuySr.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return BuySr
   * @throws Exception - an exception
   */
  private BuySr crPuBuySr(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    BuySr rz = new BuySr();
    rz.setOrm(pFctApp.lazOrm(pRvs));
    pFctApp.put(pRvs, IBuySr.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      BuySr.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SrTrStg.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrTrStg
   * @throws Exception - an exception
   */
  private SrTrStg crPuSrTrStg(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrTrStg rz = new SrTrStg();
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setLog(pFctApp.lazLogStd(pRvs));
    pFctApp.put(pRvs, ISrTrStg.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrTrStg.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF FcCsvDrt.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return FcCsvDrt
   * @throws Exception - an exception
   */
  private FcCsvDrt crPuFcCsvDrt(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    FcCsvDrt<RS> rz = new FcCsvDrt<RS>();
    rz.setFctBlc(pFctApp);
    pFctApp.put(pRvs, IFcCsvDrt.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      IFcCsvDrt.class.getSimpleName() + " has been created.");
    return rz;
  }
}
