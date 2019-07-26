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
import org.beigesoft.prp.Setng;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlp.SalRet;
import org.beigesoft.acc.mdlp.SaRtLn;
import org.beigesoft.acc.mdlp.SaRtTxLn;
import org.beigesoft.acc.mdlp.PurRet;
import org.beigesoft.acc.mdlp.PuRtLn;
import org.beigesoft.acc.mdlp.PuRtTxLn;
import org.beigesoft.acc.mdlp.SalInv;
import org.beigesoft.acc.mdlp.SaInTxLn;
import org.beigesoft.acc.mdlp.SaInSrLn;
import org.beigesoft.acc.mdlp.SaInSrTxLn;
import org.beigesoft.acc.mdlp.SaInGdLn;
import org.beigesoft.acc.mdlp.PurInv;
import org.beigesoft.acc.mdlp.PuInTxLn;
import org.beigesoft.acc.mdlp.PuInSrTxLn;
import org.beigesoft.acc.mdlp.PuInSrLn;
import org.beigesoft.acc.mdlp.PuInGdLn;
import org.beigesoft.acc.mdlp.SrTxDl;
import org.beigesoft.acc.mdlp.Srv;
import org.beigesoft.acc.hnd.HndAcc;
import org.beigesoft.acc.hld.HlTySac;
import org.beigesoft.acc.hld.HlTyEnSr;
import org.beigesoft.acc.hld.HlTyItSr;
import org.beigesoft.acc.srv.ISrAcStg;
import org.beigesoft.acc.srv.SrAcStg;
import org.beigesoft.acc.srv.UtInLnTxToBs;
import org.beigesoft.acc.srv.UtInLnTxTo;
import org.beigesoft.acc.srv.ISrWrhEnr;
import org.beigesoft.acc.srv.RvSaRtLn;
import org.beigesoft.acc.srv.RvPuRtLn;
import org.beigesoft.acc.srv.RvSaGdLn;
import org.beigesoft.acc.srv.RvSaSrLn;
import org.beigesoft.acc.srv.RvPuGdLn;
import org.beigesoft.acc.srv.RvPuSrLn;
import org.beigesoft.acc.srv.SrToPa;
import org.beigesoft.acc.srv.ISrToPa;
import org.beigesoft.acc.srv.SrWrhEnr;
import org.beigesoft.acc.srv.SrDrItEnr;
import org.beigesoft.acc.srv.ISrDrItEnr;
import org.beigesoft.acc.srv.ISrEntr;
import org.beigesoft.acc.srv.SrEntr;
import org.beigesoft.acc.srv.ISrBlnc;
import org.beigesoft.acc.srv.SrBlnc;
import org.beigesoft.acc.srv.UtlBas;
import org.beigesoft.acc.srv.SrRtLnSv;
import org.beigesoft.acc.srv.SrInLnSv;
import org.beigesoft.acc.srv.SrPaymSv;
import org.beigesoft.acc.srv.SrRetSv;
import org.beigesoft.acc.srv.SrInvSv;
import org.beigesoft.acc.srv.InvTxMeth;
import org.beigesoft.acc.rep.ISrBlnSht;
import org.beigesoft.acc.rep.SrBlnSht;
import org.beigesoft.acc.rep.IInvPdf;
import org.beigesoft.acc.rep.InvPdf;
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
   * <p>Sales invoice tax method name.</p>
   **/
  public static final String SALINVTXMETH = "SalInvTxMeth";

  /**
   * <p>Purchase return tax method name.</p>
   **/
  public static final String PURTTXMT = "PuRtTxMt";

  /**
   * <p>Sales return tax method name.</p>
   **/
  public static final String SARTTXMT = "SaRtTxMt";

  /**
   * <p>Purchase invoice service total tax utility name.</p>
   **/
  public static final String UTPUINSRTX = "utPuInSrTx";

  /**
   * <p>Sales invoice service total tax utility name.</p>
   **/
  public static final String UTSAINSRTX = "utSaInSrTx";

  /**
   * <p>Import accounting data name.</p>
   **/
  public static final String STGACIMP = "stgAcImp";

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
    } else if (HlTyItSr.class.getSimpleName().equals(pBnNm)) {
      rz = crPuHlTyItSr(pRvs, pFctApp);
    } else if (HlTyEnSr.class.getSimpleName().equals(pBnNm)) {
      rz = crPuHlTyEnSr(pRvs, pFctApp);
    } else if (HlTySac.class.getSimpleName().equals(pBnNm)) {
      rz = crPuHlTySac(pRvs, pFctApp);
    } else if (IInvPdf.class.getSimpleName().equals(pBnNm)) {
      rz = crPuInvPdf(pRvs, pFctApp);
    } else if (IBlnPdf.class.getSimpleName().equals(pBnNm)) {
      rz = crPuBlnPdf(pRvs, pFctApp);
    } else if (IPdfFactory.class.getSimpleName().equals(pBnNm)) {
      rz = crPuPdfFactory(pRvs, pFctApp);
    } else if (ISrBlnSht.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrBlnSht(pRvs, pFctApp);
    } else if (SrRtLnSv.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrRtLnSv(pRvs, pFctApp);
    } else if (SrInLnSv.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrInLnSv(pRvs, pFctApp);
    } else if (SrPaymSv.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrPaymSv(pRvs, pFctApp);
    } else if (SrRetSv.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrRetSv(pRvs, pFctApp);
    } else if (SrInvSv.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrInvSv(pRvs, pFctApp);
    } else if (UtlBas.class.getSimpleName().equals(pBnNm)) {
      rz = crPuUtlBas(pRvs, pFctApp);
    } else if (RvSaRtLn.class.getSimpleName().equals(pBnNm)) {
      rz = crSaRvSaRtLn(pRvs, pFctApp);
    } else if (RvPuRtLn.class.getSimpleName().equals(pBnNm)) {
      rz = crSaRvPuRtLn(pRvs, pFctApp);
    } else if (RvSaGdLn.class.getSimpleName().equals(pBnNm)) {
      rz = crSaRvSaGdLn(pRvs, pFctApp);
    } else if (RvSaSrLn.class.getSimpleName().equals(pBnNm)) {
      rz = crSaRvSaSrLn(pRvs, pFctApp);
    } else if (RvPuGdLn.class.getSimpleName().equals(pBnNm)) {
      rz = crPuRvPuGdLn(pRvs, pFctApp);
    } else if (RvPuSrLn.class.getSimpleName().equals(pBnNm)) {
      rz = crPuRvPuSrLn(pRvs, pFctApp);
    } else if (UtInLnTxToBs.class.getSimpleName().equals(pBnNm)) {
      rz = crPuUtInLnTxToBs(pRvs, pFctApp);
    } else if (SALINVTXMETH.equals(pBnNm)) {
      rz = crPuSalInvTxMeth(pRvs, pFctApp);
    } else if (SARTTXMT.equals(pBnNm)) {
      rz = crPuSalRetTxMeth(pRvs, pFctApp);
    } else if (PURTTXMT.equals(pBnNm)) {
      rz = crPuPurRetTxMeth(pRvs, pFctApp);
    } else if (UTSAINSRTX.equals(pBnNm)) {
      rz = crPuUtSaInSrTx(pRvs, pFctApp);
    } else if (UTPUINSRTX.equals(pBnNm)) {
      rz = crPuUtPuInSrTx(pRvs, pFctApp);
    } else if (PURINVTXMETH.equals(pBnNm)) {
      rz = crPuPurInvTxMeth(pRvs, pFctApp);
    } else if (ISrToPa.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrToPa(pRvs, pFctApp);
    } else if (ISrDrItEnr.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrDrItEnr(pRvs, pFctApp);
    } else if (ISrWrhEnr.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrWrhEnr(pRvs, pFctApp);
    } else if (ISrEntr.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrEntr(pRvs, pFctApp);
    } else if (ISrBlnc.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrBlnc(pRvs, pFctApp);
    } else if (ISrAcStg.class.getSimpleName().equals(pBnNm)) {
      rz = crPuSrAcStg(pRvs, pFctApp);
    } else if (STGACIMP.equals(pBnNm)) {
      rz = crPuStgAcIm(pRvs, pFctApp);
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
   * <p>Creates and puts into MF ACIMP Setting.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return ACIMP Setting
   * @throws Exception - an exception
   */
  private Setng crPuStgAcIm(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    Setng rz = new Setng();
    rz.setDir("acimp");
    rz.setReflect(pFctApp.lazReflect(pRvs));
    rz.setUtlPrp(pFctApp.lazUtlPrp(pRvs));
    rz.setHldFdCls(pFctApp.lazHldFldCls(pRvs));
    rz.setLog(pFctApp.lazLogStd(pRvs));
    pFctApp.put(pRvs, STGACIMP, rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(), STGACIMP
      + " has been created.");
    return rz;
  }

  /**
   * <p>Creates and puts into MF HlTyItSr.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return HlTyItSr
   * @throws Exception - an exception
   */
  private HlTyItSr crPuHlTyItSr(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    HlTyItSr rz = new HlTyItSr();
    pFctApp.put(pRvs, HlTyItSr.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      HlTyItSr.class.getSimpleName() + " has been created");
    return rz;
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
   * <p>Creates and puts into MF InvPdf.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return InvPdf
   * @throws Exception - an exception
   */
  private InvPdf<HasPdfContent> crPuInvPdf(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    InvPdf<HasPdfContent> rz = new InvPdf<HasPdfContent>();
    @SuppressWarnings("unchecked")
    IPdfFactory<HasPdfContent> pdfFactory = (IPdfFactory<HasPdfContent>) pFctApp
      .laz(pRvs, IPdfFactory.class.getSimpleName());
    rz.setPdfFactory(pdfFactory);
    rz.setI18n(pFctApp.lazI18n(pRvs));
    rz.setNumStr(pFctApp.lazNumStr(pRvs));
    rz.setOrm(pFctApp.lazOrm(pRvs));
    pFctApp.put(pRvs, IInvPdf.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      InvPdf.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF BlnPdf.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return BlnPdf
   * @throws Exception - an exception
   */
  private BlnPdf<HasPdfContent> crPuBlnPdf(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    BlnPdf<HasPdfContent> rz = new BlnPdf<HasPdfContent>();
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
   * <p>Creates and puts into MF SrRtLnSv.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrRtLnSv
   * @throws Exception - an exception
   */
  private SrRtLnSv crPuSrRtLnSv(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrRtLnSv rz = new SrRtLnSv();
    rz.setOrm(pFctApp.lazOrm(pRvs));
    pFctApp.put(pRvs, SrRtLnSv.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrRtLnSv.class.getSimpleName() + " has been created");
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
   * <p>Creates and puts into MF SrPaymSv.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrPaymSv
   * @throws Exception - an exception
   */
  private SrPaymSv crPuSrPaymSv(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrPaymSv rz = new SrPaymSv();
    rz.setOrm(pFctApp.lazOrm(pRvs));
    ISrToPa srToPa = (ISrToPa) pFctApp
      .laz(pRvs, ISrToPa.class.getSimpleName());
    rz.setSrToPa(srToPa);
    ISrEntr srEntr = (ISrEntr) pFctApp
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    UtlBas utlBas = (UtlBas) pFctApp
      .laz(pRvs, UtlBas.class.getSimpleName());
    rz.setUtlBas(utlBas);
    pFctApp.put(pRvs, SrPaymSv.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrPaymSv.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SrRetSv.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrRetSv
   * @throws Exception - an exception
   */
  private SrRetSv crPuSrRetSv(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrRetSv rz = new SrRetSv();
    rz.setOrm(pFctApp.lazOrm(pRvs));
    ISrEntr srEntr = (ISrEntr) pFctApp
      .laz(pRvs, ISrEntr.class.getSimpleName());
    rz.setSrEntr(srEntr);
    UtlBas utlBas = (UtlBas) pFctApp
      .laz(pRvs, UtlBas.class.getSimpleName());
    rz.setUtlBas(utlBas);
    pFctApp.put(pRvs, SrRetSv.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrRetSv.class.getSimpleName() + " has been created");
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
    ISrToPa srToPa = (ISrToPa) pFctApp
      .laz(pRvs, ISrToPa.class.getSimpleName());
    rz.setSrToPa(srToPa);
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
   * <p>Creates and puts into MF RvSaRtLn.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return RvSaRtLn
   * @throws Exception - an exception
   */
  private RvSaRtLn<RS> crSaRvSaRtLn(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    RvSaRtLn<RS> rz = new RvSaRtLn<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setI18n(pFctApp.lazI18n(pRvs));
    rz.setHldUvd(pFctApp.lazHldUvd(pRvs));
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) pFctApp
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    pFctApp.put(pRvs, RvSaRtLn.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      RvSaRtLn.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF RvPuRtLn.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return RvPuRtLn
   * @throws Exception - an exception
   */
  private RvPuRtLn<RS> crSaRvPuRtLn(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    RvPuRtLn<RS> rz = new RvPuRtLn<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setI18n(pFctApp.lazI18n(pRvs));
    rz.setHldUvd(pFctApp.lazHldUvd(pRvs));
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) pFctApp
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    ISrDrItEnr srDrItEnr = (ISrDrItEnr) pFctApp
      .laz(pRvs, ISrDrItEnr.class.getSimpleName());
    rz.setSrDrItEnr(srDrItEnr);
    pFctApp.put(pRvs, RvPuRtLn.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      RvPuRtLn.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF RvSaGdLn.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return RvSaGdLn
   * @throws Exception - an exception
   */
  private RvSaGdLn<RS> crSaRvSaGdLn(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    RvSaGdLn<RS> rz = new RvSaGdLn<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setI18n(pFctApp.lazI18n(pRvs));
    rz.setHldUvd(pFctApp.lazHldUvd(pRvs));
    ISrWrhEnr srWrhEnr = (ISrWrhEnr) pFctApp
      .laz(pRvs, ISrWrhEnr.class.getSimpleName());
    rz.setSrWrhEnr(srWrhEnr);
    ISrDrItEnr srDrItEnr = (ISrDrItEnr) pFctApp
      .laz(pRvs, ISrDrItEnr.class.getSimpleName());
    rz.setSrDrItEnr(srDrItEnr);
    pFctApp.put(pRvs, RvSaGdLn.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      RvSaGdLn.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF RvSaSrLn.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return RvSaSrLn
   * @throws Exception - an exception
   */
  private RvSaSrLn<RS> crSaRvSaSrLn(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    RvSaSrLn<RS> rz = new RvSaSrLn<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setI18n(pFctApp.lazI18n(pRvs));
    rz.setHldUvd(pFctApp.lazHldUvd(pRvs));
    pFctApp.put(pRvs, RvSaSrLn.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      RvSaSrLn.class.getSimpleName() + " has been created");
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
   * <p>Creates and puts into MF PurRetTxMeth.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return PurRetTxMeth
   * @throws Exception - an exception
   */
  private InvTxMeth<SalRet, SaRtTxLn> crPuSalRetTxMeth(
    final Map<String, Object> pRvs, final FctBlc<RS> pFctApp) throws Exception {
    InvTxMeth<SalRet, SaRtTxLn> rz = new InvTxMeth<SalRet, SaRtTxLn>();
    FctOrId<SaRtTxLn> fpitl = new FctOrId<SaRtTxLn>();
    rz.setFctInvTxLn(fpitl);
    fpitl.setCls(SaRtTxLn.class);
    fpitl.setDbOr(pFctApp.lazOrm(pRvs).getDbId());
    rz.setFlTotals("invGdTot");
    rz.setFlTxInvAdj("saRtTxInAdj");
    rz.setFlTxInvBas("saRtTxInBs");
    rz.setFlTxInvBasAggr("saRtTxInBsAg");
    rz.setFlTxItBas("invGdTxItBs");
    rz.setFlTxItBasAggr("saRtTxItBsAg");
    rz.setTblNmsTot(new String[] {"SARTLN", "SARTTXLN", "SARTLTL"});
    rz.setIsTxByUser(true);
    rz.setGoodLnCl(SaRtLn.class);
    rz.setInvTxLnCl(SaRtTxLn.class);
    rz.setInvCl(SalRet.class);
    pFctApp.put(pRvs, SARTTXMT, rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SARTTXMT + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF PurRetTxMeth.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return PurRetTxMeth
   * @throws Exception - an exception
   */
  private InvTxMeth<PurRet, PuRtTxLn> crPuPurRetTxMeth(
    final Map<String, Object> pRvs, final FctBlc<RS> pFctApp) throws Exception {
    InvTxMeth<PurRet, PuRtTxLn> rz = new InvTxMeth<PurRet, PuRtTxLn>();
    FctOrId<PuRtTxLn> fpitl = new FctOrId<PuRtTxLn>();
    rz.setFctInvTxLn(fpitl);
    fpitl.setCls(PuRtTxLn.class);
    fpitl.setDbOr(pFctApp.lazOrm(pRvs).getDbId());
    rz.setFlTotals("invGdTot");
    rz.setFlTxInvAdj("purRtTxInAdj");
    rz.setFlTxInvBas("purRtTxInBs");
    rz.setFlTxInvBasAggr("purRtTxInBsAg");
    rz.setFlTxItBas("invGdTxItBs");
    rz.setFlTxItBasAggr("purRtTxItBsAg");
    rz.setTblNmsTot(new String[] {"PURTLN", "PURTTXLN", "PURTLTL"});
    rz.setIsTxByUser(true);
    rz.setGoodLnCl(PuRtLn.class);
    rz.setInvTxLnCl(PuRtTxLn.class);
    rz.setInvCl(PurRet.class);
    pFctApp.put(pRvs, PURTTXMT, rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      PURTTXMT + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SalInvTxMeth.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SalInvTxMeth
   * @throws Exception - an exception
   */
  private InvTxMeth<SalInv, SaInTxLn> crPuSalInvTxMeth(
    final Map<String, Object> pRvs, final FctBlc<RS> pFctApp) throws Exception {
    InvTxMeth<SalInv, SaInTxLn> rz = new InvTxMeth<SalInv, SaInTxLn>();
    FctOrId<SaInTxLn> fpitl = new FctOrId<SaInTxLn>();
    rz.setFctInvTxLn(fpitl);
    fpitl.setCls(SaInTxLn.class);
    fpitl.setDbOr(pFctApp.lazOrm(pRvs).getDbId());
    rz.setFlTotals("invTot");
    rz.setFlTxInvAdj("invTxInAdj");
    rz.setFlTxInvBas("invTxInBs");
    rz.setFlTxInvBasAggr("invTxInBsAg");
    rz.setFlTxItBas("invTxItBs");
    rz.setFlTxItBasAggr("invTxItBsAg");
    rz.setTblNmsTot(new String[] {"SAINGDLN", "SAINSRLN", "SAINTXLN",
      "SAINGDTXLN", "SAINSRTXLN"});
    rz.setIsTxByUser(true);
    rz.setGoodLnCl(SaInGdLn.class);
    rz.setServiceLnCl(SaInSrLn.class);
    rz.setInvTxLnCl(SaInTxLn.class);
    rz.setInvCl(SalInv.class);
    pFctApp.put(pRvs, SALINVTXMETH, rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SALINVTXMETH + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SalInv UtInLnTxTo.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SalInv UtInLnTxTo
   * @throws Exception - an exception
   */
  private UtInLnTxTo<RS, SalInv, SaInSrLn, SaInTxLn, SaInSrTxLn> crPuUtSaInSrTx(
    final Map<String, Object> pRvs, final FctBlc<RS> pFctApp) throws Exception {
    UtInLnTxTo<RS, SalInv, SaInSrLn, SaInTxLn, SaInSrTxLn> rz =
      new UtInLnTxTo<RS, SalInv, SaInSrLn, SaInTxLn, SaInSrTxLn>();
    @SuppressWarnings("unchecked")
    UtInLnTxToBs<RS> utInTxToBs = (UtInLnTxToBs<RS>) pFctApp
      .laz(pRvs, UtInLnTxToBs.class.getSimpleName());
    rz.setUtlInv(utInTxToBs);
    @SuppressWarnings("unchecked")
    InvTxMeth<SalInv, SaInTxLn> invTxMeth = (InvTxMeth<SalInv, SaInTxLn>)
      pFctApp.laz(pRvs, FctAcc.SALINVTXMETH);
    rz.setInvTxMeth(invTxMeth);
    FctOrId<SaInSrTxLn> fcpigtl = new FctOrId<SaInSrTxLn>();
    rz.setFctLineTxLn(fcpigtl);
    fcpigtl.setCls(SaInSrTxLn.class);
    fcpigtl.setDbOr(pFctApp.lazOrm(pRvs).getDbId());
    rz.setLtlCl(SaInSrTxLn.class);
    rz.setDstTxItLnCl(SrTxDl.class);
    rz.setInvLnCl(SaInSrLn.class);
    rz.setItmCl(Srv.class);
    rz.setIsMutable(true);
    rz.setNeedMkTxCat(true);
    rz.setIsPurch(true);
    pFctApp.put(pRvs, UTSAINSRTX, rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      UTSAINSRTX + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF PurInv UtInLnTxTo.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return PurInv UtInLnTxTo
   * @throws Exception - an exception
   */
  private UtInLnTxTo<RS, PurInv, PuInSrLn, PuInTxLn, PuInSrTxLn> crPuUtPuInSrTx(
    final Map<String, Object> pRvs, final FctBlc<RS> pFctApp) throws Exception {
    UtInLnTxTo<RS, PurInv, PuInSrLn, PuInTxLn, PuInSrTxLn> rz =
      new UtInLnTxTo<RS, PurInv, PuInSrLn, PuInTxLn, PuInSrTxLn>();
    @SuppressWarnings("unchecked")
    UtInLnTxToBs<RS> utInTxToBs = (UtInLnTxToBs<RS>) pFctApp
      .laz(pRvs, UtInLnTxToBs.class.getSimpleName());
    rz.setUtlInv(utInTxToBs);
    @SuppressWarnings("unchecked")
    InvTxMeth<PurInv, PuInTxLn> invTxMeth = (InvTxMeth<PurInv, PuInTxLn>)
      pFctApp.laz(pRvs, PURINVTXMETH);
    rz.setInvTxMeth(invTxMeth);
    FctOrId<PuInSrTxLn> fcpigtl = new FctOrId<PuInSrTxLn>();
    rz.setFctLineTxLn(fcpigtl);
    fcpigtl.setCls(PuInSrTxLn.class);
    fcpigtl.setDbOr(pFctApp.lazOrm(pRvs).getDbId());
    rz.setLtlCl(PuInSrTxLn.class);
    rz.setDstTxItLnCl(SrTxDl.class);
    rz.setInvLnCl(PuInSrLn.class);
    rz.setItmCl(Srv.class);
    rz.setIsMutable(true);
    rz.setNeedMkTxCat(true);
    rz.setIsPurch(true);
    pFctApp.put(pRvs, UTPUINSRTX, rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      UTPUINSRTX + " has been created");
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
   * <p>Creates and puts into MF SrToPa.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrToPa
   * @throws Exception - an exception
   */
  private SrToPa crPuSrToPa(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrToPa rz = new SrToPa();
    rz.setNumStr(pFctApp.lazNumStr(pRvs));
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setI18n(pFctApp.lazI18n(pRvs));
    pFctApp.put(pRvs, ISrToPa.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrToPa.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF SrDrItEnr.</p>
   * @param pRvs request scoped vars
   * @param pFctApp main factory
   * @return SrDrItEnr
   * @throws Exception - an exception
   */
  private SrDrItEnr<RS> crPuSrDrItEnr(final Map<String, Object> pRvs,
    final FctBlc<RS> pFctApp) throws Exception {
    SrDrItEnr<RS> rz = new SrDrItEnr<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) pFctApp.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(pFctApp.lazOrm(pRvs));
    rz.setI18n(pFctApp.lazI18n(pRvs));
    rz.setLog(pFctApp.lazLogStd(pRvs));
    rz.setIsAndr(pFctApp.getFctDt().getIsAndr());
    rz.setSrvClVl(pFctApp.lazSrvClVl(pRvs));
    HlTyItSr hlTyItSr = (HlTyItSr) pFctApp
      .laz(pRvs, HlTyItSr.class.getSimpleName());
    rz.setHlTyItSr(hlTyItSr);
    HlTyEnSr hlTyEnSr = (HlTyEnSr) pFctApp
      .laz(pRvs, HlTyEnSr.class.getSimpleName());
    rz.setHlTyEnSr(hlTyEnSr);
    pFctApp.put(pRvs, ISrDrItEnr.class.getSimpleName(), rz);
    pFctApp.lazLogStd(pRvs).info(pRvs, getClass(),
      SrDrItEnr.class.getSimpleName() + " has been created");
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
    HlTyItSr hlTyItSr = (HlTyItSr) pFctApp
      .laz(pRvs, HlTyItSr.class.getSimpleName());
    rz.setHlTyItSr(hlTyItSr);
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
