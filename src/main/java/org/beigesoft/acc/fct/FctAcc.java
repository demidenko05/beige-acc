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

package org.beigesoft.acc.fct;

import java.util.Map;

import org.beigesoft.pdf.model.HasPdfContent;
import org.beigesoft.pdf.service.PdfFactory;
import org.beigesoft.pdf.service.IPdfFactory;

import org.beigesoft.fct.IFctAux;
import org.beigesoft.fct.FctBlc;
import org.beigesoft.fct.FctOrId;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlp.PurInv;
import org.beigesoft.acc.mdlp.PuInTxLn;
import org.beigesoft.acc.mdlp.PuInSrLn;
import org.beigesoft.acc.mdlp.PuInGdLn;
import org.beigesoft.acc.hnd.HndAcc;
import org.beigesoft.acc.hld.HlTySac;
import org.beigesoft.acc.hld.HlTyEnSr;
import org.beigesoft.acc.srv.ISrAcStg;
import org.beigesoft.acc.srv.SrAcStg;
import org.beigesoft.acc.srv.UtInLnTxToBs;
import org.beigesoft.acc.srv.ISrWrhEnr;
import org.beigesoft.acc.srv.RvPuGdLn;
import org.beigesoft.acc.srv.RvPuSrLn;
import org.beigesoft.acc.srv.SrWrhEnr;
import org.beigesoft.acc.srv.ISrEntr;
import org.beigesoft.acc.srv.SrEntr;
import org.beigesoft.acc.srv.ISrBlnc;
import org.beigesoft.acc.srv.SrBlnc;
import org.beigesoft.acc.srv.UtlBas;
import org.beigesoft.acc.srv.SrInLnSv;
import org.beigesoft.acc.srv.SrInvSv;
import org.beigesoft.acc.srv.InvTxMeth;
import org.beigesoft.acc.rep.ISrBlnSht;
import org.beigesoft.acc.rep.SrBlnSht;
import org.beigesoft.acc.rep.IBlnPdf;
import org.beigesoft.acc.rep.BlnPdf;

/**
 * <p>Auxiliary factory for accounting additional services.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FctAcc<RS> implements IFctAux<RS> {

  /**
   * <p>Purchase invoice tax method name.</p>
   **/
  public static final String PURINVTXMETH = "PurInvTxMeth";

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
    if (HndAcc.class.getSimpleName().equals(pBnNm)) {
      rz = crPuHndAcc(pRvs, pFctApp);
    } else if (HlTyEnSr.class.getSimpleName().equals(pBnNm)) {
      rz = crPuHlTyEnSr(pRvs, pFctApp);
    } else if (HlTySac.class.getSimpleName().equals(pBnNm)) {
      rz = crPuHlTySac(pRvs, pFctApp);
    } else if (IBlnPdf.class.getSimpleName().equals(pBnNm)) {
      rz = crPuBlnPdf(pRvs, pFctApp);
    } else if (IPdfFactory.class.getSimpleName().equals(pBnNm)) {
      rz = crPuPdfFactory(pRvs, pFctApp);
    } else if (ISrBlnSht.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrBlnSht(pRvs, pFctApp);
    } else if (SrInLnSv.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrInLnSv(pRvs, pFctApp);
    } else if (SrInvSv.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrInvSv(pRvs, pFctApp);
    } else if (UtlBas.class.getSimpleName().equals(pBnNm)) {
      rz = crPuUtlBas(pRvs, pFctApp);
    } else if (RvPuGdLn.class.getSimpleName().equals(pBnNm)) {
      rz = crPuRvPuGdLn(pRvs, pFctApp);
    } else if (RvPuSrLn.class.getSimpleName().equals(pBnNm)) {
      rz = crPuRvPuSrLn(pRvs, pFctApp);
    } else if (UtInLnTxToBs.class.getSimpleName().equals(pBnNm)) {
      rz = crPuUtInLnTxToBs(pRvs, pFctApp);
    } else if (PURINVTXMETH.equals(pBnNm)) {
      rz = crPuPurInvTxMeth(pRvs, pFctApp);
    } else if (ISrWrhEnr.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrWrhEnr(pRvs, pFctApp);
    } else if (ISrEntr.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrEntr(pRvs, pFctApp);
    } else if (ISrBlnc.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrBlnc(pRvs, pFctApp);
    } else if (ISrAcStg.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrAcStg(pRvs, pFctApp);
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
   * <p>Creates and puts into MF HlTyEnSr.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return HlTyEnSr
   * @throws Exception - an exception
   */
  private HlTyEnSr crPuHlTyEnSr(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    HlTyEnSr rz = new HlTyEnSr();
    pFctApp.put(pRvs, HlTyEnSr.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      HlTyEnSr.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF HlTySac.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return HlTySac
   * @throws Exception - an exception
   */
  private HlTySac crPuHlTySac(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    HlTySac rz = new HlTySac();
    pFctApp.put(pRvs, HlTySac.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      HlTySac.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF BlnPdf.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return BlnPdf
   * @throws Exception - an exception
   */
  private BlnPdf<RS, HasPdfContent> crPuBlnPdf(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    BlnPdf<RS, HasPdfContent> rz = new BlnPdf<RS, HasPdfContent>();
    @SuppressWarnings("unchecked")
    IPdfFactory<HasPdfContent> pdfFactory = (IPdfFactory<HasPdfContent>) pFctApp
      .laz(pRvs, IPdfFactory.class.getSimpleName());
    rz.setPdfFactory(pdfFactory);
    rz.setI18n(pFctApp.lazI18n(pRvs));
    rz.setNumStr(pFctApp.lazNumStr(pRvs));
    ISrAcStg srAcStg = (ISrAcStg) pFctApp
      .laz(pRvs, ISrAcStg.class.getSimpleName());
    rz.setSrAcStg(srAcStg);
    pFctApp.put(pRvs, IBlnPdf.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      BlnPdf.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF PdfFactory.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return PdfFactory
   * @throws Exception - an exception
   */
  private PdfFactory crPuPdfFactory(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    PdfFactory rz = new PdfFactory();
    rz.setLog(pFctApp.lazLogStd(pRvs));
    rz.init();
    pFctApp.put(pRvs, IPdfFactory.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      PdfFactory.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SrBlnSht.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrBlnSht
   * @throws Exception - an exception
   */
  private SrBlnSht<RS> crPuSrBlnSht(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrBlnSht<RS> rz = new SrBlnSht<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    ISrAcStg srAcStg = (ISrAcStg) pFctApp
      .laz(pRvs, ISrAcStg.class.getSimpleName());
    rz.setSrAcStg(srAcStg);
    ISrBlnc srBlnc = (ISrBlnc) pFctApp
      .laz(pRvs, ISrBlnc.class.getSimpleName());
    rz.setSrBlnc(srBlnc);
    pFctApp.put(pRvs, ISrBlnSht.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrBlnSht.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SrInLnSv.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrInLnSv
   * @throws Exception - an exception
   */
  private SrInLnSv crPuSrInLnSv(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrInLnSv rz = new SrInLnSv();
    rz.setOrm(pFctApp.lazOrm(pRvs));
    pFctApp.put(pRvs, SrInLnSv.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrInLnSv.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SrInvSv.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrInvSv
   * @throws Exception - an exception
   */
  private SrInvSv crPuSrInvSv(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrInvSv rz = new SrInvSv();
    rz.setOrm(pFctApp.lazOrm(pRvs));
    ISrEntr srEntr = (ISrEntr) pFctApp
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    UtlBas utlBas = (UtlBas) pFctApp
      .laz(pRvs, UtlBas.class.getSimpleName());
    rz.setUtlBas(utlBas);
    pFctApp.put(pRvs, SrInvSv.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrInvSv.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF UtlBas.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return UtlBas
   * @throws Exception - an exception
   */
  private UtlBas crPuUtlBas(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    UtlBas rz = new UtlBas();
    rz.setOrm(pFctApp.lazOrm(pRvs));
    pFctApp.put(pRvs, UtlBas.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      UtlBas.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF RvPuGdLn.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return RvPuGdLn
   * @throws Exception - an exception
   */
  private RvPuGdLn<RS> crPuRvPuGdLn(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    RvPuGdLn<RS> rz = new RvPuGdLn<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setI18n(pFctApp.lazI18n(pRvs));
    rz.setHldUvd(pFctApp.lazHldUvd(pRvs));
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) pFctApp
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    pFctApp.put(pRvs, RvPuGdLn.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      RvPuGdLn.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF RvPuSrLn.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return RvPuSrLn
   * @throws Exception - an exception
   */
  private RvPuSrLn<RS> crPuRvPuSrLn(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    RvPuSrLn<RS> rz = new RvPuSrLn<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setI18n(pFctApp.lazI18n(pRvs));
    rz.setHldUvd(pFctApp.lazHldUvd(pRvs));
    pFctApp.put(pRvs, RvPuSrLn.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      RvPuSrLn.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF PurInvTxMeth.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return PurInvTxMeth
   * @throws Exception - an exception
   */
  private InvTxMeth<PurInv, PuInTxLn> crPuPurInvTxMeth(
    final Map<String, Object> pRvs, final FctBlc<RS> pFctApp) throws Exception {
    InvTxMeth<PurInv, PuInTxLn> rz = new InvTxMeth<PurInv, PuInTxLn>();
    FctOrId<PuInTxLn> fpitl = new FctOrId<PuInTxLn>();
    rz.setFctInvTxLn(fpitl);
    fpitl.setCls(PuInTxLn.class);
    fpitl.setDbOr(pFctApp.lazOrm(pRvs).getDbId());
    rz.setFlTotals("invTot");
    rz.setFlTxInvAdj("invTxInAdj");
    rz.setFlTxInvBas("invTxInBs");
    rz.setFlTxInvBasAggr("invTxInBsAg");
    rz.setFlTxItBas("invTxItBs");
    rz.setFlTxItBasAggr("invTxItBsAg");
    rz.setTblNmsTot(new String[] {"PUINGDLN", "PUINSRLN", "PUINTXLN",
      "PUINGDTXLN", "PUINSRTXLN"});
    rz.setIsTxByUser(true);
    rz.setGoodLnCl(PuInGdLn.class);
    rz.setServiceLnCl(PuInSrLn.class);
    rz.setInvTxLnCl(PuInTxLn.class);
    rz.setInvCl(PurInv.class);
    pFctApp.put(pRvs, PURINVTXMETH, rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      PURINVTXMETH + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF UtInLnTxToBs.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return UtInLnTxToBs
   * @throws Exception - an exception
   */
  private UtInLnTxToBs<RS> crPuUtInLnTxToBs(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    UtInLnTxToBs<RS> rz = new UtInLnTxToBs<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setNumStr(pFctApp.lazNumStr(pRvs));
    rz.setLog(pFctApp.lazLogStd(pRvs));
    pFctApp.put(pRvs, UtInLnTxToBs.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      UtInLnTxToBs.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SrWrhEnr.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrWrhEnr
   * @throws Exception - an exception
   */
  private SrWrhEnr<RS> crPuSrWrhEnr(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrWrhEnr<RS> rz = new SrWrhEnr<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setI18n(pFctApp.lazI18n(pRvs));
    rz.setLog(pFctApp.lazLogStd(pRvs));
    rz.setIsAndr(pFctApp.getFctDt().getIsAndr());
    HlTyEnSr hlTyEnSr = (HlTyEnSr) pFctApp
      .laz(pRvs, HlTyEnSr.class.getSimpleName());
    rz.setHlTyEnSr(hlTyEnSr);
    rz.setSrvClVl(pFctApp.lazSrvClVl(pRvs));
    pFctApp.put(pRvs, ISrWrhEnr.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrWrhEnr.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SrEntr.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrEntr
   * @throws Exception - an exception
   */
  private SrEntr<RS> crPuSrEntr(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrEntr<RS> rz = new SrEntr<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setI18n(pFctApp.lazI18n(pRvs));
    ISrBlnc srBlnc = (ISrBlnc) pFctApp
      .laz(pRvs, ISrBlnc.class.getSimpleName());
    rz.setSrBlnc(srBlnc);
    pFctApp.put(pRvs, ISrEntr.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrEntr.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SrBlnc.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrBlnc
   * @throws Exception - an exception
   */
  private SrBlnc<RS> crPuSrBlnc(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrBlnc<RS> rz = new SrBlnc<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setIsAndr(pFctApp.getFctDt().getIsAndr());
    rz.setSrvClVl(pFctApp.lazSrvClVl(pRvs));
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setLog(pFctApp.lazLogStd(pRvs));
    ISrAcStg srAcStg = (ISrAcStg) pFctApp
      .laz(pRvs, ISrAcStg.class.getSimpleName());
    rz.setSrAcStg(srAcStg);
    pFctApp.put(pRvs, ISrBlnc.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrBlnc.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF HndAcc.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return HndAcc
   * @throws Exception - an exception
   */
  private HndAcc<RS> crPuHndAcc(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    HndAcc<RS> rz = new HndAcc<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setLog(pFctApp.lazLogStd(pRvs));
    rz.setSrvDt(pFctApp.lazSrvDt(pRvs));
    ISrAcStg srAcStg = (ISrAcStg) pFctApp
      .laz(pRvs, ISrAcStg.class.getSimpleName());
    rz.setSrAcStg(srAcStg);
    HlTyEnSr hlTyEnSr = (HlTyEnSr) pFctApp
      .laz(pRvs, HlTyEnSr.class.getSimpleName());
    rz.setHlTyEnSr(hlTyEnSr);
    HlTySac hlTySac = (HlTySac) pFctApp
      .laz(pRvs, HlTySac.class.getSimpleName());
    rz.setHlTySac(hlTySac);
    pFctApp.put(pRvs, HndAcc.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      HndAcc.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SrAcStg.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrAcStg
   * @throws Exception - an exception
   */
  private SrAcStg crPuSrAcStg(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrAcStg rz = new SrAcStg();
    IOrm orm = (IOrm) pFctApp.laz(pRvs, IOrm.class.getSimpleName());
    rz.setOrm(orm);
    rz.setLog(pFctApp.lazLogStd(pRvs));
    pFctApp.put(pRvs, ISrAcStg.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrAcStg.class.getSimpleName() + " has been created");
    return rz;
  }
}
