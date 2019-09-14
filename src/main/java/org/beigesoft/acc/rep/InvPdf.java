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

import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Locale;
import java.math.BigDecimal;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.beigesoft.doc.model.Document;
import org.beigesoft.doc.model.DocTable;
import org.beigesoft.doc.model.EWraping;
import org.beigesoft.doc.model.EAlignHorizontal;
import org.beigesoft.doc.model.EUnitOfMeasure;
import org.beigesoft.doc.service.IDocumentMaker;
import org.beigesoft.pdf.model.PdfDocument;
import org.beigesoft.pdf.service.IPdfFactory;
import org.beigesoft.pdf.service.IPdfMaker;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.srv.II18n;
import org.beigesoft.srv.INumStr;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlp.SalInv;
import org.beigesoft.acc.mdlp.SaInGdLn;
import org.beigesoft.acc.mdlp.SaInTxLn;
import org.beigesoft.acc.mdlp.SaInSrLn;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.I18Acc;
import org.beigesoft.acc.mdlp.Curr;
import org.beigesoft.acc.mdlp.I18Curr;
import org.beigesoft.acc.mdlp.I18Buyr;

/**
 * <p>Invoice report into PDF.</p>
 *
 * @param <WI> writing instrument type
 * @author Yury Demidenko
 */
public class InvPdf<WI> implements IInvPdf {

  /**
   * <p>PDF Factory.</p>
   **/
  private IPdfFactory<WI> pdfFactory;

  /**
   * <p>I18N service.</p>
   **/
  private II18n i18n;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Service print number.</p>
   **/
  private INumStr numStr;

  /**
   * <p>salesInvOverseaseLines SQL.</p>
   **/
  private String saInOvrsGds;

  /**
   * <p>salesInvOverseaseServiceLines SQL.</p>
   **/
  private String slInOvrsSrs;

  /**
   * <p>Write PDF report for given invoice to output stream.</p>
   * @param pRvs additional param
   * @param pInv Invoice
   * @param pRqDt Request Data
   * @param pOus servlet output stream
   * @throws Exception - an exception
   **/
  public final void mkRep(final Map<String, Object> pRvs, final SalInv pInv,
    final IReqDt pRqDt, final OutputStream pOus) throws Exception {
    AcStg as = (AcStg) pRvs.get("astg");
    UsPrf upf = (UsPrf) pRvs.get("upf");
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    DateFormat dateFormat = DateFormat
      .getDateInstance(DateFormat.MEDIUM, new Locale(upf.getLng().getIid()));
    refrInv(pRvs, pInv, upf.getLng().getIid(),
      !upf.getLng().getIid().equals(cpf.getLngDef().getIid()));
    boolean stIb = false;
    for (SaInTxLn itl : pInv.getTxLns()) {
      if (itl.getTxb().compareTo(BigDecimal.ZERO) == 1) {
        stIb = true;
        break;
      }
    }
    Curr curr;
    if (pInv.getCuFr() != null) {
      curr = pInv.getCuFr();
    } else {
      curr = as.getCurr();
    }
    I18Curr i18Curr = null;
    if (!upf.getLng().getIid().equals(cpf.getLngDef().getIid())) {
      Map<String, Object> vs = new HashMap<String, Object>();
      i18Curr = new I18Curr();
      i18Curr.setHasNm(curr);
      i18Curr.setLng(upf.getLng());
      i18Curr = getOrm().retEnt(pRvs, vs, i18Curr);
    } else {
      i18Curr = (I18Curr) pRvs.get("i18Curr");
    }
    String curSign;
    boolean isPrnCurLf;
    if (i18Curr != null) {
      if (i18Curr.getCurs()) {
        curSign = i18Curr.getHasNm().getSgn();
      } else {
        curSign = " " + i18Curr.getNme() + " ";
      }
      isPrnCurLf = i18Curr.getCurl();
    } else {
      if (as.getCurs()) {
        curSign = curr.getSgn();
      } else {
        curSign = " " + curr.getNme() + " ";
      }
      isPrnCurLf = as.getCurl();
    }
    String priNm;
    if (pInv.getInTx()) {
      priNm = "inTx";
    } else {
      priNm = "pri";
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
    docPdf.getPdfInfo().setAuthor("Beigesoft (TM) EIS, "
      + as.getOrg());
    IPdfMaker<WI> pdfMaker = this.pdfFactory.lazyGetPdfMaker();
    if (as.getTtfb() != null) {
      pdfMaker.addFontTtf(docPdf, as.getTtfb());
    }
    pdfMaker.addFontTtf(docPdf, as.getTtff());
    double widthNdot = this.pdfFactory.lazyGetUomHelper()
      .fromPoints(2.0, 300.0, doc.getUnitOfMeasure()); //printer resolution
    doc.setBorder(widthNdot);
    doc.setContentPadding(0.0);
    doc.setContentPaddingBottom(mmToDocUom(1.0, doc.getUnitOfMeasure()));
    DocTable<WI> tblOwner = docMaker.addDocTableNoBorder(doc, 1, 1);
    I18Acc i18Acc = (I18Acc) pRvs.get("i18Acc");
    if (i18Acc != null) {
      tblOwner.getItsCells().get(0).setItsContent(i18Acc.getOrg());
    } else {
      tblOwner.getItsCells().get(0).setItsContent(as.getOrg());
    }
    int n = 0;
    if (as.getTin() != null) {
      docMaker.addRowToDocTable(tblOwner);
      tblOwner.getItsCells().get(++n).setItsContent(this.i18n
        .getMsg("tin", upf.getLng().getIid()) + ": "
          + as.getTin());
    }
    if (as.getZip() != null) {
      docMaker.addRowToDocTable(tblOwner);
      tblOwner.getItsCells().get(++n).setItsContent(this.i18n
        .getMsg("zip", upf.getLng().getIid()) + ": " + as.getZip());
    }
    String addr = null;
    if (i18Acc != null) {
      addr = i18Acc.getAddr1();
    } else {
      addr = as.getAddr1();
    }
    if (addr != null) {
      docMaker.addRowToDocTable(tblOwner);
      tblOwner.getItsCells().get(++n).setItsContent(this.i18n
        .getMsg("addr1", upf.getLng().getIid()) + ": " + addr);
    }
    if (i18Acc != null) {
      addr = i18Acc.getAddr2();
    } else {
      addr = as.getAddr2();
    }
    if (addr != null) {
      docMaker.addRowToDocTable(tblOwner);
      tblOwner.getItsCells().get(++n).setItsContent(this.i18n
        .getMsg("addr2", upf.getLng().getIid()) + ": " + addr);
    }
    if (i18Acc != null) {
      addr = i18Acc.getCity();
    } else {
      addr = as.getCity();
    }
    if (addr != null) {
      docMaker.addRowToDocTable(tblOwner);
      tblOwner.getItsCells().get(++n).setItsContent(this.i18n
        .getMsg("city", upf.getLng().getIid()) + ": " + addr);
    }
    if (i18Acc != null) {
      addr = i18Acc.getStat();
    } else {
      addr = as.getStat();
    }
    if (addr != null) {
      docMaker.addRowToDocTable(tblOwner);
      tblOwner.getItsCells().get(++n).setItsContent(this.i18n
        .getMsg("stat", upf.getLng().getIid()) + ": " + addr);
    }
    if (i18Acc != null) {
      addr = i18Acc.getCntr();
    } else {
      addr = as.getCntr();
    }
    if (addr != null) {
      docMaker.addRowToDocTable(tblOwner);
      tblOwner.getItsCells().get(++n).setItsContent(this.i18n
        .getMsg("cntr", upf.getLng().getIid()) + ": " + addr);
    }
    tblOwner.getItsCells().get(0).setFontNumber(1);
    docMaker.makeDocTableWrapping(tblOwner);
    tblOwner.setAlignHorizontal(EAlignHorizontal.RIGHT);
    DocTable<WI> tblTitle = docMaker.addDocTableNoBorder(doc, 1, 1);
    String invNm;
    if (pInv.getIdOr() != null) {
      invNm = pInv.getDbOr().toString() + "-" + pInv.getIdOr();
    } else {
      invNm = pInv.getDbOr().toString() + "-" + pInv.getIid();
    }
    String ttl = this.i18n.getMsg("Invoice", upf.getLng().getIid()) + " #"
      + invNm + ", " + this.i18n.getMsg("dat", upf.getLng().getIid()) + ": "
        + dateFormat.format(pInv.getDat());
    tblTitle.getItsCells().get(0).setItsContent(ttl);
    tblTitle.getItsCells().get(0).setFontNumber(1);
    tblTitle.setAlignHorizontal(EAlignHorizontal.CENTER);
    doc.setContainerMarginBottom(mmToDocUom(1.0, doc.getUnitOfMeasure()));
    docMaker.makeDocTableWrapping(tblTitle);
    DocTable<WI> tblDbcr = docMaker.addDocTableNoBorder(doc, 1, 1);
    I18Buyr i18Buyr = null;
    if (!upf.getLng().getIid().equals(cpf.getLngDef().getIid())) {
      Map<String, Object> vs = new HashMap<String, Object>();
      i18Buyr = new I18Buyr();
      i18Buyr.setHasNm(pInv.getDbcr());
      i18Buyr.setLng(upf.getLng());
      i18Buyr = getOrm().retEnt(pRvs, vs, i18Buyr);
    }
    if (i18Buyr != null) {
      tblDbcr.getItsCells().get(0).setItsContent(i18Buyr.getNme());
    } else {
      tblDbcr.getItsCells().get(0).setItsContent(pInv.getDbcr().getNme());
    }
    n = 0;
    if (pInv.getDbcr().getTin() != null) {
      docMaker.addRowToDocTable(tblDbcr);
      tblDbcr.getItsCells().get(++n).setItsContent(this.i18n.getMsg("tin",
        upf.getLng().getIid()) + ": " + pInv.getDbcr().getTin());
    }
    if (pInv.getDbcr().getZip() != null) {
      docMaker.addRowToDocTable(tblDbcr);
      tblDbcr.getItsCells().get(++n).setItsContent(this.i18n
        .getMsg("zip", upf.getLng().getIid()) + ": " + pInv.getDbcr().getZip());
    }
    if (i18Buyr != null) {
      addr = i18Buyr.getAddr1();
    } else {
      addr = pInv.getDbcr().getAddr1();
    }
    if (addr != null) {
      docMaker.addRowToDocTable(tblDbcr);
      tblDbcr.getItsCells().get(++n).setItsContent(this.i18n
        .getMsg("addr1", upf.getLng().getIid()) + ": " + addr);
    }
    if (i18Buyr != null) {
      addr = i18Buyr.getAddr2();
    } else {
      addr = pInv.getDbcr().getAddr2();
    }
    if (addr != null) {
      docMaker.addRowToDocTable(tblDbcr);
      tblDbcr.getItsCells().get(++n).setItsContent(this.i18n
        .getMsg("addr2", upf.getLng().getIid()) + ": " + addr);
    }
    if (i18Buyr != null) {
      addr = i18Buyr.getCity();
    } else {
      addr = pInv.getDbcr().getCity();
    }
    if (addr != null) {
      docMaker.addRowToDocTable(tblDbcr);
      tblDbcr.getItsCells().get(++n).setItsContent(this.i18n
        .getMsg("city", upf.getLng().getIid()) + ": " + addr);
    }
    if (i18Buyr != null) {
      addr = i18Buyr.getStat();
    } else {
      addr = pInv.getDbcr().getStat();
    }
    if (addr != null) {
      docMaker.addRowToDocTable(tblDbcr);
      tblDbcr.getItsCells().get(++n).setItsContent(this.i18n
        .getMsg("state", upf.getLng().getIid()) + ": " + addr);
    }
    if (i18Buyr != null) {
      addr = i18Buyr.getCntr();
    } else {
      addr = pInv.getDbcr().getCntr();
    }
    if (addr != null) {
      docMaker.addRowToDocTable(tblDbcr);
      tblDbcr.getItsCells().get(++n).setItsContent(this.i18n
        .getMsg("cntr", upf.getLng().getIid()) + ": " + addr);
    }
    tblDbcr.getItsCells().get(0).setFontNumber(1);
    BigDecimal pri;
    BigDecimal subt;
    BigDecimal toTx;
    BigDecimal tot;
    if (pInv.getGdLns() != null && pInv.getGdLns().size() > 0) {
      doc.setContainerMarginBottom(mmToDocUom(2.0, doc.getUnitOfMeasure()));
      DocTable<WI> tblTiGoods = docMaker.addDocTableNoBorder(doc, 1, 1);
      tblTiGoods.getItsCells().get(0).setItsContent(this.i18n
        .getMsg(SaInGdLn.class.getSimpleName() + "s", upf.getLng().getIid()));
      tblTiGoods.getItsCells().get(0).setFontNumber(1);
      tblTiGoods.setAlignHorizontal(EAlignHorizontal.CENTER);
      docMaker.makeDocTableWrapping(tblTiGoods);
      doc.setContentPadding(mmToDocUom(1.0, doc.getUnitOfMeasure()));
      doc.setContentPaddingBottom(mmToDocUom(1.5, doc.getUnitOfMeasure()));
      int rowc;
      if (stIb) {
        rowc = 6;
      } else if (pInv.getInTx()) {
        rowc = 7;
      } else {
        rowc = 8;
      }
      DocTable<WI> tbGds = docMaker
        .addDocTable(doc, rowc, pInv.getGdLns().size() + 1);
      tbGds.setIsRepeatHead(true);
      tbGds.getItsRows().get(0).setIsHead(true);
      tbGds.getItsCells().get(0).setItsContent(this.i18n
        .getMsg("Itm", upf.getLng().getIid()).replace(" ", "\n"));
      tbGds.getItsColumns().get(0).setIsWidthFixed(true);
      tbGds.getItsColumns().get(0).setWidthInPercentage(60.0);
      tbGds.getItsCells().get(1).setItsContent(this.i18n
        .getMsg("uom", upf.getLng().getIid()).replace(" ", "\n"));
      tbGds.getItsColumns().get(1).setWraping(EWraping.WRAP_CONTENT);
      tbGds.getItsCells().get(2).setItsContent(this.i18n
        .getMsg(priNm, upf.getLng().getIid()).replace(" ", "\n"));
      tbGds.getItsColumns().get(2).setWraping(EWraping.WRAP_CONTENT);
      tbGds.getItsCells().get(3).setItsContent(this.i18n
        .getMsg("quan", upf.getLng().getIid()).replace(" ", "\n"));
      tbGds.getItsColumns().get(3).setWraping(EWraping.WRAP_CONTENT);
      int pos = 3;
      if (!pInv.getInTx()) {
        pos++;
        tbGds.getItsCells().get(pos).setItsContent(this.i18n
        .getMsg("subt", upf.getLng().getIid()).replace(" ", "\n"));
        tbGds.getItsColumns().get(pos).setWraping(EWraping.WRAP_CONTENT);
      } else if (stIb && pInv.getInTx()) {
        pos++;
        tbGds.getItsCells().get(pos).setItsContent(this.i18n
        .getMsg("tot", upf.getLng().getIid()).replace(" ", "\n"));
        tbGds.getItsColumns().get(pos).setWraping(EWraping.WRAP_CONTENT);
      }
      pos++;
      tbGds.getItsCells().get(pos)
        .setItsContent(this.i18n.getMsg("tdsc", upf.getLng().getIid()));
      if (!stIb) {
        pos++;
        tbGds.getItsCells().get(pos).setItsContent(this.i18n
          .getMsg("toTx", upf.getLng().getIid()).replace(" ", "\n"));
        tbGds.getItsColumns().get(pos).setWraping(EWraping.WRAP_CONTENT);
        pos++;
        tbGds.getItsCells().get(pos).setItsContent(this.i18n
          .getMsg("tot", upf.getLng().getIid()).replace(" ", "\n"));
        tbGds.getItsColumns().get(pos).setWraping(EWraping.WRAP_CONTENT);
      }
      for (int i = 0; i < rowc; i++) {
        tbGds.getItsCells().get(i).setFontNumber(1);
        tbGds.getItsCells().get(i)
          .setAlignHorizontal(EAlignHorizontal.CENTER);
      }
      int j = 1;
      for (SaInGdLn ln : pInv.getGdLns()) {
        if (pInv.getCuFr() != null) {
          pri = ln.getPrFc();
          subt = ln.getSuFc();
          toTx = ln.getTxFc();
          tot = ln.getToFc();
        } else {
          pri = ln.getPri();
          subt = ln.getSubt();
          toTx = ln.getToTx();
          tot = ln.getTot();
        }
        int i = 0;
        int k = j * rowc + i++;
        tbGds.getItsCells().get(k).setItsContent(ln.getItm().getNme());
        k = j * rowc + i++;
        tbGds.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.CENTER);
        tbGds.getItsCells().get(k).setItsContent(ln.getUom().getNme());
        k = j * rowc + i++;
        tbGds.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
        tbGds.getItsCells().get(k).setItsContent(prn(as, cpf, upf, pri));
        k = j * rowc + i++;
        tbGds.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
        tbGds.getItsCells().get(k)
          .setItsContent(prn(as, cpf, upf, ln.getQuan()));
        if (!pInv.getInTx()) {
          k = j * rowc + i++;
          tbGds.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
          tbGds.getItsCells().get(k).setItsContent(prn(as, cpf, upf, subt));
        } else if (stIb && pInv.getInTx()) {
          k = j * rowc + i++;
          tbGds.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
          tbGds.getItsCells().get(k).setItsContent(prn(as, cpf, upf, tot));
        }
        k = j * rowc + i++;
        tbGds.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.CENTER);
        tbGds.getItsCells().get(k).setItsContent(ln.getTdsc());
        if (!stIb) {
          k = j * rowc + i++;
          tbGds.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
          tbGds.getItsCells().get(k).setItsContent(prn(as, cpf, upf, toTx));
          k = j * rowc + i++;
          tbGds.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
          tbGds.getItsCells().get(k).setItsContent(prn(as, cpf, upf, tot));
        }
        j++;
      }
    }
    if (pInv.getSrLns() != null && pInv.getSrLns().size() > 0) {
      doc.setContainerMarginBottom(mmToDocUom(2.0, doc.getUnitOfMeasure()));
      DocTable<WI> tblTiServices = docMaker.addDocTableNoBorder(doc, 1, 1);
      tblTiServices.getItsCells().get(0).setItsContent(this.i18n
        .getMsg(SaInSrLn.class.getSimpleName() + "s", upf.getLng().getIid()));
      tblTiServices.getItsCells().get(0).setFontNumber(1);
      tblTiServices.setAlignHorizontal(EAlignHorizontal.CENTER);
      docMaker.makeDocTableWrapping(tblTiServices);
      doc.setContentPadding(mmToDocUom(1.0, doc.getUnitOfMeasure()));
      doc.setContentPaddingBottom(mmToDocUom(1.5, doc.getUnitOfMeasure()));
      int rowc;
      double wd15;
      if (stIb) {
        rowc = 6;
        wd15 = 30.0d;
      } else if (pInv.getInTx()) {
        rowc = 7;
        wd15 = 15.0d;
      } else {
        rowc = 8;
        wd15 = 15.0d;
      }
      DocTable<WI> tbSrs = docMaker
        .addDocTable(doc, rowc, pInv.getSrLns().size() + 1);
      tbSrs.setIsRepeatHead(true);
      tbSrs.getItsRows().get(0).setIsHead(true);
      tbSrs.getItsCells().get(0)
        .setItsContent(this.i18n.getMsg("Srv", upf.getLng().getIid()));
      tbSrs.getItsColumns().get(0).setIsWidthFixed(true);
      tbSrs.getItsColumns().get(0).setWidthInPercentage(35.0);
      tbSrs.getItsCells().get(1).setItsContent(this.i18n
        .getMsg("uom", upf.getLng().getIid()).replace(" ", "\n"));
      tbSrs.getItsColumns().get(1).setWraping(EWraping.WRAP_CONTENT);
      tbSrs.getItsCells().get(2).setItsContent(this.i18n.getMsg(priNm,
        upf.getLng().getIid()).replace(" ", "\n"));
      tbSrs.getItsColumns().get(2).setIsWidthFixed(true);
      tbSrs.getItsColumns().get(2).setWidthInPercentage(wd15);
      tbSrs.getItsCells().get(3).setItsContent(this.i18n
        .getMsg("quan", upf.getLng().getIid()).replace(" ", "\n"));
      tbSrs.getItsColumns().get(3).setWraping(EWraping.WRAP_CONTENT);
      int pos = 3;
      if (!pInv.getInTx()) {
        pos++;
        tbSrs.getItsCells().get(pos).setItsContent(this.i18n
          .getMsg("subt", upf.getLng().getIid()).replace(" ", "\n"));
        tbSrs.getItsColumns().get(pos).setWraping(EWraping.WRAP_CONTENT);
      } else if (stIb && pInv.getInTx()) {
        pos++;
        tbSrs.getItsCells().get(pos).setItsContent(this.i18n
          .getMsg("tot", upf.getLng().getIid()).replace(" ", "\n"));
        tbSrs.getItsColumns().get(pos).setWraping(EWraping.WRAP_CONTENT);
      }
      pos++;
      tbSrs.getItsCells().get(pos)
        .setItsContent(this.i18n.getMsg("tdsc", upf.getLng().getIid()));
      if (!stIb) {
        tbSrs.getItsColumns().get(pos).setIsWidthFixed(true);
        tbSrs.getItsColumns().get(pos).setWidthInPercentage(20.0);
        pos++;
        tbSrs.getItsCells().get(pos)
          .setItsContent(this.i18n.getMsg("toTx", upf.getLng().getIid()));
        tbSrs.getItsColumns().get(pos).setIsWidthFixed(true);
        tbSrs.getItsColumns().get(pos).setWidthInPercentage(wd15);
        pos++;
        tbSrs.getItsCells().get(pos)
          .setItsContent(this.i18n.getMsg("tot", upf.getLng().getIid()));
        tbSrs.getItsColumns().get(pos).setIsWidthFixed(true);
        tbSrs.getItsColumns().get(pos).setWidthInPercentage(wd15);
      }
      for (int i = 0; i < rowc; i++) {
        tbSrs.getItsCells().get(i).setFontNumber(1);
        tbSrs.getItsCells().get(i).setAlignHorizontal(EAlignHorizontal.CENTER);
      }
      int j = 1;
      for (SaInSrLn ln : pInv.getSrLns()) {
        if (pInv.getCuFr() != null) {
          pri = ln.getPrFc();
          subt = ln.getSuFc();
          toTx = ln.getTxFc();
          tot = ln.getToFc();
        } else {
          pri = ln.getPri();
          subt = ln.getSubt();
          toTx = ln.getToTx();
          tot = ln.getTot();
        }
        int i = 0;
        int k = j * rowc + i++;
        tbSrs.getItsCells().get(k).setItsContent(ln.getItm().getNme());
        k = j * rowc + i++;
        tbSrs.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.CENTER);
        tbSrs.getItsCells().get(k).setItsContent(ln.getUom().getNme());
        k = j * rowc + i++;
        tbSrs.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
        tbSrs.getItsCells().get(k).setItsContent(prn(as, cpf, upf, pri));
        k = j * rowc + i++;
        tbSrs.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
        tbSrs.getItsCells().get(k)
          .setItsContent(prn(as, cpf, upf, ln.getQuan()));
        if (!pInv.getInTx()) {
          k = j * rowc + i++;
          tbSrs.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
          tbSrs.getItsCells().get(k).setItsContent(prn(as, cpf, upf, subt));
        } else if (stIb && pInv.getInTx()) {
          k = j * rowc + i++;
          tbSrs.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
          tbSrs.getItsCells().get(k).setItsContent(prn(as, cpf, upf, tot));
        }
        k = j * rowc + i++;
        tbSrs.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.CENTER);
        tbSrs.getItsCells().get(k).setItsContent(ln.getTdsc());
        if (!stIb) {
          k = j * rowc + i++;
          tbSrs.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
          tbSrs.getItsCells().get(k).setItsContent(prn(as, cpf, upf, toTx));
          k = j * rowc + i++;
          tbSrs.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
          tbSrs.getItsCells().get(k).setItsContent(prn(as, cpf, upf, tot));
        }
        j++;
      }
    }
    if (pInv.getTxLns() != null && pInv.getTxLns().size() > 0) {
      doc.setContainerMarginBottom(mmToDocUom(2.0, doc.getUnitOfMeasure()));
      DocTable<WI> tblTiTaxes = docMaker.addDocTableNoBorder(doc, 1, 1);
      tblTiTaxes.setIsRepeatHead(true);
      tblTiTaxes.getItsRows().get(0).setIsHead(true);
      tblTiTaxes.getItsCells().get(0).setItsContent(this.i18n
        .getMsg(SaInTxLn.class.getSimpleName() + "s", upf.getLng().getIid()));
      tblTiTaxes.getItsCells().get(0).setFontNumber(1);
      tblTiTaxes.setAlignHorizontal(EAlignHorizontal.CENTER);
      docMaker.makeDocTableWrapping(tblTiTaxes);
      doc.setContentPadding(mmToDocUom(1.0, doc.getUnitOfMeasure()));
      doc.setContentPaddingBottom(mmToDocUom(1.5, doc.getUnitOfMeasure()));
      int rowc;
      double rowtw;
      if (stIb) {
        rowc = 3;
        rowtw = 40.0d;
      } else {
        rowc = 2;
        rowtw = 70.0d;
      }
      DocTable<WI> tbTxs = docMaker
        .addDocTable(doc, rowc, pInv.getTxLns().size() + 1);
      tbTxs.getItsCells().get(0)
        .setItsContent(this.i18n.getMsg("tax", upf.getLng().getIid()));
      tbTxs.getItsColumns().get(0).setIsWidthFixed(true);
      tbTxs.getItsColumns().get(0).setWidthInPercentage(rowtw);
      if (stIb) {
        tbTxs.getItsCells().get(1)
          .setItsContent(this.i18n.getMsg("txb", upf.getLng().getIid()));
      }
      tbTxs.getItsCells().get(rowc - 1)
        .setItsContent(this.i18n.getMsg("tot", upf.getLng().getIid()));
      for (int i = 0; i < rowc; i++) {
        tbTxs.getItsCells().get(i).setFontNumber(1);
        tbTxs.getItsCells().get(i).setAlignHorizontal(EAlignHorizontal.CENTER);
      }
      BigDecimal txb;
      int j = 1;
      for (SaInTxLn ln : pInv.getTxLns()) {
        if (pInv.getCuFr() != null) {
          txb = ln.getTxbFc();
          tot = ln.getToFc();
        } else {
          txb = ln.getTxb();
          tot = ln.getTot();
        }
        int i = 0;
        tbTxs.getItsCells().get(j * rowc + i++)
          .setItsContent(ln.getTax().getNme());
        int k = j * rowc + i++;
        if (stIb) {
          tbTxs.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
          tbTxs.getItsCells().get(k).setItsContent(prn(as, cpf, upf, txb));
          k = j * rowc + i++;
        }
        tbTxs.getItsCells().get(k).setAlignHorizontal(EAlignHorizontal.RIGHT);
        tbTxs.getItsCells().get(k).setItsContent(prn(as, cpf, upf, tot));
        j++;
      }
    }
    doc.setAlignHoriCont(EAlignHorizontal.RIGHT);
    DocTable<WI> tblRez = docMaker.addDocTableNoBorder(doc, 2, 3);
    tblRez.getItsCells().get(0).setFontNumber(1);
    tblRez.getItsCells().get(0).setItsContent(this.i18n.getMsg("subt",
      upf.getLng().getIid()) + ": ");
    tblRez.getItsCells().get(1).setFontNumber(1);
    String cnt;
    if (pInv.getCuFr() != null) {
      subt = pInv.getSuFc();
      toTx = pInv.getTxFc();
      tot = pInv.getToFc();
    } else {
      subt = pInv.getSubt();
      toTx = pInv.getToTx();
      tot = pInv.getTot();
    }
    if (isPrnCurLf) {
      cnt = curSign + prn(as, cpf, upf, subt);
    } else {
      cnt = prn(as, cpf, upf, subt) + curSign;
    }
    tblRez.getItsCells().get(1).setItsContent(cnt);
    tblRez.getItsCells().get(2).setFontNumber(1);
    tblRez.getItsCells().get(2).setItsContent(this.i18n.getMsg("toTx",
      upf.getLng().getIid()) + ": ");
    tblRez.getItsCells().get(3).setFontNumber(1);
    if (isPrnCurLf) {
      cnt = curSign + prn(as, cpf, upf, toTx);
    } else {
      cnt = prn(as, cpf, upf, toTx) + curSign;
    }
    tblRez.getItsCells().get(3).setItsContent(cnt);
    tblRez.getItsCells().get(4).setFontNumber(1);
    tblRez.getItsCells().get(4).setItsContent(this.i18n.getMsg("tot",
      upf.getLng().getIid()) + ": ");
    tblRez.getItsCells().get(5).setFontNumber(1);
    if (isPrnCurLf) {
      cnt = curSign + prn(as, cpf, upf, tot);
    } else {
      cnt = prn(as, cpf, upf, tot) + curSign;
    }
    tblRez.getItsCells().get(5).setItsContent(cnt);
    tblRez.setAlignHorizontal(EAlignHorizontal.RIGHT);
    docMaker.makeDocTableWrapping(tblRez);
    docMaker.addPagination(doc);
    docMaker.deriveElements(doc);
    pdfMaker.prepareBeforeWrite(docPdf);
    this.pdfFactory.lazyGetPdfWriter().write(null, docPdf, pOus);
  }

  /**
   * <p>Refreshes sales invoice from DB.</p>
   * @param pRvs additional param
   * @param pInv Invoice
   * @param pLng Lang
   * @param pIsOverseas Is Overseas
   * @throws Exception an Exception
   **/
  public final void refrInv(final Map<String, Object> pRvs,
    final SalInv pInv, final String pLng,
      final boolean pIsOverseas) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    this.orm.refrEnt(pRvs, vs, pInv);
    String[] ndFlSil = new String[] {"subt", "toTx", "tdsc", "itm",
      "uom", "quan", "pri", "tot", "prFr", "suFr", "txFc", "toFc"};
    Arrays.sort(ndFlSil);
    vs.put("SaInGdLnndFds", ndFlSil);
    String[] ndFlItUm = new String[] {"nme"};
    vs.put("ItmndFds", ndFlItUm);
    vs.put("UomndFds", ndFlItUm);
    if (pIsOverseas) {
      pInv.setGdLns(getOrm().retLstQu(pRvs, vs, SaInGdLn.class,
        evalSaInOvrsGds(pInv.getIid().toString(), pLng))); vs.clear();
    } else {
      pInv.setGdLns(getOrm().retLstCnd(pRvs, vs, SaInGdLn.class,
          "where RVID is null and OWNR=" + pInv.getIid())); vs.clear();
    }
    vs.put("SaInSrLnndFds", ndFlSil);
    vs.put("SrvndFds", ndFlItUm);
    vs.put("UomndFds", ndFlItUm);
    if (pIsOverseas) {
      pInv.setSrLns(getOrm().retLstQu(pRvs, vs, SaInSrLn.class,
        evalSaInOvrsSrs(pInv.getIid().toString(), pLng))); vs.clear();
    } else {
      pInv.setSrLns(getOrm().retLstCnd(pRvs, vs, SaInSrLn.class,
        "where RVID is null and OWNR=" + pInv.getIid())); vs.clear();
    }
    //overseas sales usually free from sales taxes
    vs.put("SalInvdpLv", 0); //only ID
    pInv.setTxLns(getOrm().retLstCnd(pRvs, vs, SaInTxLn.class,
      "where OWNR=" + pInv.getIid()));
  }

  /**
   * <p>Evaluate I18N overseas sales invoice service lines query.</p>
   * @param pInvId ID of sales invoice
   * @param pLng upf.getLng().getIid()
   * @return query
   * @throws Exception - an exception
   **/
  public final String evalSaInOvrsSrs(final String pInvId,
    final String pLng) throws Exception {
    if (this.slInOvrsSrs == null) {
      synchronized (this) {
        if (this.slInOvrsSrs == null) {
          String flName = "/acc/rep/saInOvrsSrs.sql";
          this.slInOvrsSrs = loadStr(flName);
        }
      }
    }
    return this.slInOvrsSrs.replace(":OWNR",
      pInvId).replace(":LNG", pLng);
  }

  /**
   * <p>Evaluate I18N overseas sales invoice lines query.</p>
   * @param pInvId ID of sales invoice
   * @param pLng upf.getLng().getIid()
   * @return query
   * @throws Exception - an exception
   **/
  public final String evalSaInOvrsGds(final String pInvId,
    final String pLng) throws Exception {
    if (this.saInOvrsGds == null) {
      synchronized (this) {
        if (this.saInOvrsGds == null) {
          String flName = "/acc/rep/saInOvrsGds.sql";
          this.saInOvrsGds = loadStr(flName);
        }
      }
    }
    return this.saInOvrsGds.replace(":OWNR", pInvId).replace(":LNG", pLng);
  }

  /**
   * <p>Load string file (usually SQL query).</p>
   * @param pFlNm file name
   * @return SQL query, not null
   * @throws IOException - IO exception
   **/
  public final String loadStr(final String pFlNm) throws IOException {
    URL urlFile = InvPdf.class
      .getResource(pFlNm);
    if (urlFile != null) {
      InputStream inputStream = null;
      try {
        inputStream = InvPdf.class.getResourceAsStream(pFlNm);
        byte[] bArray = new byte[inputStream.available()];
        inputStream.read(bArray, 0, inputStream.available());
        return new String(bArray, "UTF-8");
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }
      }
    }
    throw new RuntimeException("File not found: " + pFlNm);
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
   * <p>Getter for orm.</p>
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
