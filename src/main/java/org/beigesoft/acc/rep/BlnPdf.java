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

package org.beigesoft.acc.rep;

import java.text.DateFormat;
import java.util.Map;
import java.util.Locale;
import java.math.BigDecimal;
import java.io.OutputStream;

import org.beigesoft.doc.model.Document;
import org.beigesoft.doc.model.DocTable;
import org.beigesoft.doc.model.EAlignHorizontal;
import org.beigesoft.doc.model.EUnitOfMeasure;
import org.beigesoft.doc.service.IDocumentMaker;
import org.beigesoft.pdf.model.PdfDocument;
import org.beigesoft.pdf.service.IPdfFactory;
import org.beigesoft.pdf.service.IPdfMaker;

import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.acc.mdl.BlnSht;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.I18Acc;
import org.beigesoft.acc.mdlp.I18Curr;
import org.beigesoft.srv.II18n;
import org.beigesoft.srv.INumStr;
import org.beigesoft.acc.srv.ISrAcStg;

/**
 * <p>Balance sheet report into PDF.</p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @param <WI> writing instrument type PDF
 * @author Yury Demidenko
 */
public class BlnPdf<RS, WI> implements IBlnPdf {

  /**
   * <p>PDF Factory.</p>
   **/
  private IPdfFactory<WI> pdfFactory;

  /**
   * <p>Business service for accounting settings.</p>
   **/
  private ISrAcStg srvAcStg;

  /**
   * <p>I18N service.</p>
   **/
  private II18n i18n;

  /**
   * <p>Service print number.</p>
   **/
  private INumStr numStr;

  /**
   * <p>Write PDF report for given balance to output stream.</p>
   * @param pRvs additional param
   * @param pBalance Balance
   * @param pOus servlet output stream
   * @throws Exception - an exception
   **/
  @Override
  public final void report(final Map<String, Object> pRvs,
    final BlnSht pBalance, final OutputStream pOus) throws Exception {
    AcStg as = this.srvAcStg.lazAcStg(pRvs);
    UsPrf upf = (UsPrf) pRvs.get("upf");
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    DateFormat dateFormat = DateFormat
      .getDateInstance(DateFormat.MEDIUM, new Locale(upf.getLng().getIid()));
    String curSign;
    I18Curr i18Curr = (I18Curr) pRvs.get("i18Curr");
    boolean isPrnCurLf;
    if (i18Curr != null) {
      isPrnCurLf = i18Curr.getCurl();
      if (i18Curr.getCurs()) {
        curSign = i18Curr.getHasNm().getSgn();
      } else {
        curSign = " " + i18Curr.getNme() + " ";
      }
    } else {
      if (as.getCurs()) {
        curSign = as.getCurr().getSgn();
      } else {
        curSign = " " + as.getCurr().getNme() + " ";
      }
      isPrnCurLf = as.getCurl();
    }
    Document<WI> doc = this.pdfFactory.lazyGetFctDocument()
      .createDoc(as.getPgSz(), as.getPgOr());
    doc.setFontSize(as.getFnSz());
    doc.getPages().get(0).setMarginBottom(as.getMrBo());
    doc.getPages().get(0).setMarginTop(as.getMrTo());
    doc.getPages().get(0).setMarginLeft(as.getMrLf());
    doc.getPages().get(0).setMarginRight(as.getMrRi());
    PdfDocument<WI> docPdf = this.pdfFactory.createPdfDoc(doc);
    IDocumentMaker<WI> docMaker = this.pdfFactory.lazyGetDocumentMaker();
    docPdf.getPdfInfo().setAuthor("Beigesoft(TM) EIS, "
      + as.getOrg());
    IPdfMaker<WI> pdfMaker = this.pdfFactory.lazyGetPdfMaker();
    pdfMaker.addFontTtf(docPdf, as.getTtff());
    if (as.getTtfb() != null) {
      pdfMaker.addFontTtf(docPdf, as.getTtfb());
    }
    double widthNdot = this.pdfFactory.lazyGetUomHelper()
      .fromPoints(2.0, 300.0, doc.getUnitOfMeasure()); //printer resolution
    doc.setBorder(widthNdot);
    doc.setContentPadding(0.0);
    doc.setContainerMarginBottom(mmToDocUom(3.5, doc.getUnitOfMeasure()));
    doc.setContentPaddingBottom(mmToDocUom(0.5, doc.getUnitOfMeasure()));
    doc.setAlignHoriCont(EAlignHorizontal.CENTER);
    DocTable<WI> tblTitle = docMaker.addDocTableNoBorder(doc, 1, 3);
    I18Acc i18Acc = (I18Acc) pRvs.get("i18Acc");
    if (i18Acc != null) {
      tblTitle.getItsCells().get(0).setItsContent(i18Acc.getOrg());
    } else {
      tblTitle.getItsCells().get(0).setItsContent(as.getOrg());
    }
    tblTitle.getItsCells().get(1)
      .setItsContent(this.i18n.getMsg("balance_sheet", upf.getLng().getIid()));
    tblTitle.getItsCells().get(2)
      .setItsContent(dateFormat.format(pBalance.getDat()));
    tblTitle.setAlignHorizontal(EAlignHorizontal.CENTER);
    docMaker.makeDocTableWrapping(tblTitle);
    doc.setContentPadding(mmToDocUom(1.0, doc.getUnitOfMeasure()));
    doc.setAlignHoriCont(EAlignHorizontal.LEFT);
    DocTable<WI> tblBal = docMaker
      .addDocTable(doc, 4, pBalance.getDetRc() + 2);
    tblBal.getItsColumns().get(0).setIsWidthFixed(true);
    tblBal.getItsColumns().get(0).setWidthInPercentage(30.0);
    tblBal.getItsColumns().get(1).setIsWidthFixed(true);
    tblBal.getItsColumns().get(1).setWidthInPercentage(20.0);
    tblBal.getItsColumns().get(2).setIsWidthFixed(true);
    tblBal.getItsColumns().get(2).setWidthInPercentage(30.0);
    tblBal.getItsColumns().get(3).setIsWidthFixed(true);
    tblBal.getItsColumns().get(3).setWidthInPercentage(20.0);
    tblBal.getItsCells().get(0).setItsContent(this.i18n
      .getMsg("AssetsTitle", upf.getLng().getIid()));
    tblBal.getItsCells().get(0).setMergedCell(tblBal.getItsCells().get(1));
    tblBal.getItsCells().get(2).setItsContent(this.i18n
      .getMsg("LiabilitiesTitle", upf.getLng().getIid()));
    tblBal.getItsCells().get(2).setMergedCell(tblBal.getItsCells().get(3));
    int row = 1;
    for (int i = 0; i < pBalance.getToLnAs(); i++) {
      String cnt;
      if (pBalance.getLns().get(i).getDebit()
        .compareTo(BigDecimal.ZERO) != 0) {
        cnt = prn(as, cpf, upf, pBalance.getLns().get(i).getDebit());
      } else {
        cnt = "(" + prn(as, cpf, upf, pBalance.getLns().get(i).getCredit())
          + ")";
      }
      tblBal.getItsCells().get(row * 4)
        .setItsContent(pBalance.getLns().get(i).getAccName());
      tblBal.getItsCells().get(row * 4).setFontNumber(1);
      tblBal.getItsCells().get(row * 4 + 1).setItsContent(cnt);
      tblBal.getItsCells().get(row * 4 + 1)
        .setAlignHorizontal(EAlignHorizontal.RIGHT);
      tblBal.getItsCells().get(row * 4 + 1).setFontNumber(1);
      row++;
    }
    int totLeabOwnEq = pBalance.getToLnLi()
      + pBalance.getToLnOe();
    int lastRowIdx = Math.max(pBalance.getToLnAs() + 1,
      totLeabOwnEq + 4);
    tblBal.getItsCells().get(lastRowIdx * 4)
      .setItsContent(this.i18n.getMsg("total_assets", upf.getLng().getIid()));
    tblBal.getItsCells().get(lastRowIdx * 4 + 1)
      .setAlignHorizontal(EAlignHorizontal.RIGHT);
    String cntc;
    if (isPrnCurLf) {
      cntc = curSign + prn(as, cpf, upf, pBalance.getTotAss());
    } else {
      cntc = prn(as, cpf, upf, pBalance.getTotAss()) + curSign;
    }
    tblBal.getItsCells().get(lastRowIdx * 4 + 1).setItsContent(cntc);
    row = 1;
    int totAssLeab = pBalance.getToLnAs()
        + pBalance.getToLnLi();
    for (int i = pBalance.getToLnAs(); i < totAssLeab; i++) {
      String cnt;
      if (pBalance.getLns().get(i).getCredit()
        .compareTo(BigDecimal.ZERO) != 0) {
        cnt = prn(as, cpf, upf, pBalance.getLns().get(i).getCredit());
      } else {
        cnt = "(" + prn(as, cpf, upf, pBalance.getLns().get(i).getDebit())
          + ")";
      }
      tblBal.getItsCells().get(row * 4 + 2)
        .setItsContent(pBalance.getLns().get(i).getAccName());
      tblBal.getItsCells().get(row * 4 + 2).setFontNumber(1);
      tblBal.getItsCells().get(row * 4 + 3).setItsContent(cnt);
      tblBal.getItsCells().get(row * 4 + 3)
        .setAlignHorizontal(EAlignHorizontal.RIGHT);
      tblBal.getItsCells().get(row * 4 + 3).setFontNumber(1);
      row++;
    }
    tblBal.getItsCells().get(pBalance.getToLnLi() * 4 + 6)
      .setItsContent(this.i18n.getMsg("total_l", upf.getLng().getIid()));
    tblBal.getItsCells().get(pBalance.getToLnLi() * 4 + 7)
      .setAlignHorizontal(EAlignHorizontal.RIGHT);
    tblBal.getItsCells().get(pBalance.getToLnLi() * 4 + 7)
      .setItsContent(prn(as, cpf, upf, pBalance.getTotLia()));
    int oetIdx = pBalance.getToLnLi() * 4 + 10;
    tblBal.getItsCells().get(oetIdx).setItsContent(this.i18n
      .getMsg("OwnersEquityTitle", upf.getLng().getIid()));
    tblBal.getItsCells().get(oetIdx)
      .setMergedCell(tblBal.getItsCells().get(oetIdx + 1));
    row = 1 + pBalance.getToLnLi() + 2;
    for (int i = totAssLeab;
      i < totAssLeab + pBalance.getToLnOe(); i++) {
      String cnt;
      if (pBalance.getLns().get(i).getCredit()
        .compareTo(BigDecimal.ZERO) != 0) {
        cnt = prn(as, cpf, upf, pBalance.getLns().get(i).getCredit());
      } else {
        cnt = "(" + prn(as, cpf, upf, pBalance.getLns().get(i).getDebit())
          + ")";
      }
      tblBal.getItsCells().get(row * 4 + 2)
        .setItsContent(pBalance.getLns().get(i).getAccName());
      tblBal.getItsCells().get(row * 4 + 2).setFontNumber(1);
      tblBal.getItsCells().get(row * 4 + 3).setItsContent(cnt);
      tblBal.getItsCells().get(row * 4 + 3)
        .setAlignHorizontal(EAlignHorizontal.RIGHT);
      tblBal.getItsCells().get(row * 4 + 3).setFontNumber(1);
      row++;
    }
    tblBal.getItsCells().get((totLeabOwnEq + 3) * 4 + 2)
      .setItsContent(this.i18n.getMsg("total_oe", upf.getLng().getIid()));
    tblBal.getItsCells().get((totLeabOwnEq + 3) * 4 + 3)
      .setAlignHorizontal(EAlignHorizontal.RIGHT);
    tblBal.getItsCells().get((totLeabOwnEq + 3) * 4 + 3)
      .setItsContent(prn(as, cpf, upf, pBalance.getTotOwe()));
    tblBal.getItsCells().get(lastRowIdx * 4 + 2)
      .setItsContent(this.i18n.getMsg("total_l_oe", upf.getLng().getIid()));
    tblBal.getItsCells().get(lastRowIdx * 4 + 3)
      .setAlignHorizontal(EAlignHorizontal.RIGHT);
    if (isPrnCurLf) {
      cntc = curSign + prn(as, cpf, upf, pBalance.getTotOwe()
        .add(pBalance.getTotLia()));
    } else {
      cntc = prn(as, cpf, upf, pBalance.getTotOwe()
        .add(pBalance.getTotLia())) + curSign;
    }
    tblBal.getItsCells().get(lastRowIdx * 4 + 3).setItsContent(cntc);
    docMaker.deriveElements(doc);
    pdfMaker.prepareBeforeWrite(docPdf);
    try {
      this.pdfFactory.lazyGetPdfWriter().write(null, docPdf, pOus);
    } finally {
      pOus.close();
    }
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
      pCpf.getDcGrSpv(), pAs.getRpDp(), pUpf.getDgInGr());
  }

  /**
   * <p>Convert value from millimeters to document UOM.</p>
   * @param pValue in millimeters
   * @param pUom doc UOM
   * @return in document UOM value
   **/
  public final double mmToDocUom(final double pValue,
    final EUnitOfMeasure pUom) {
    if (pUom.equals(EUnitOfMeasure.INCH)) {
      return pValue / 25.4;
    }
    return pValue;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for pdfFactory.</p>
   * @return IPdfFactory
   **/
  public final IPdfFactory<WI> getPdfFactory() {
    return this.pdfFactory;
  }

  /**
   * <p>Setter for pdfFactory.</p>
   * @param pFactory reference
   **/
  public final void setPdfFactory(final IPdfFactory<WI> pFactory) {
    this.pdfFactory = pFactory;
  }

  /**
   * <p>Getter for srvAcStg.</p>
   * @return ISrAcStg
   **/
  public final ISrAcStg getSrAcStg() {
    return this.srvAcStg;
  }

  /**
   * <p>Setter for srvAcStg.</p>
   * @param pSrAcStg reference
   **/
  public final void setSrAcStg(final ISrAcStg pSrAcStg) {
    this.srvAcStg = pSrAcStg;
  }

  /**
   * <p>Getter for i18n.</p>
   * @return II18n
   **/
  public final II18n getI18n() {
    return this.i18n;
  }

  /**
   * <p>Setter for i18n.</p>
   * @param pI18n reference
   **/
  public final void setI18n(final II18n pI18n) {
    this.i18n = pI18n;
  }

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
}
