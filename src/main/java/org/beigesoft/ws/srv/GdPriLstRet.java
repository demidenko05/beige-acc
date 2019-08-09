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

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.beigesoft.cmp.CmpHasIdLn;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.Node;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.srv.ICsvDtRet;
import org.beigesoft.srv.I18n;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdl.TaxWr;
import org.beigesoft.acc.mdl.TxCtWr;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Tax;
import org.beigesoft.acc.mdlp.TxCt;
import org.beigesoft.acc.mdlp.TxCtLn;
import org.beigesoft.acc.mdlp.WrhPl;
import org.beigesoft.acc.mdlp.WrhItm;
import org.beigesoft.ws.mdlp.PriItm;

/**
 * <p>Goods Price List Retriever.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class GdPriLstRet<RS> implements ICsvDtRet {

  /**
   * <p>I18N service.</p>
   **/
  private I18n i18n;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Retrieves CSV data.
   * Request data must has:
   * <pre>
   *  priCtId - price category ID
   * </pre>
   * also might has:
   * <pre>
   *  optmQuan - optimistic quantity
   *  unAvPri - unavailable price
   * </pre>
   * </p>
   * @param pRvs request parameters,
   * @return data table
   * @throws Exception an Exception
   **/
  @Override
  public final List<List<Object>> retData(
    final Map<String, Object> pRvs) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    AcStg as = (AcStg) pRvs.get("astg");
    List<List<Object>> result = new ArrayList<List<Object>>();
    IReqDt rqDt = (IReqDt) pRvs.get("rqDt");
    Long priCtId = Long.parseLong(rqDt.getParam("priCtId"));
    BigDecimal unAvPri = null;
    String unAvPris = rqDt.getParam("unAvPri");
    if (unAvPris != null) {
      unAvPri = new BigDecimal(unAvPris);
    }
    BigDecimal optmQuan = null;
    String optmQuans = rqDt.getParam("optmQuan");
    if (optmQuans != null) {
      optmQuan = new BigDecimal(optmQuans);
    }
    String[] ndFlPg = new String[] {"itm", "pri"};
    Arrays.sort(ndFlPg);
    String[] ndFdNm = new String[] {"nme"};
    vs.put("PriItmdpLv", 3);
    vs.put("PriItmndFds", ndFlPg);
    vs.put("ItmCtndFds", ndFdNm);
    vs.put("UomndFds", ndFdNm);
    List<PriItm> gpl = getOrm().retLstCnd(pRvs, vs, PriItm.class,
      "where PRICT=" + priCtId); vs.clear();
    String[] ndFdr = new String[] {"rate"};
    pRvs.put("TxCtLnndFds", ndFdr);
    pRvs.put("TaxndFds", ndFdNm);
    List<TxCtLn> allTaxCatsLns = getOrm().retLst(pRvs, vs, TxCtLn.class);
    vs.clear();
    List<Tax> usedTaxes = new ArrayList<Tax>();
    List<TxCt> usedTaxCats = new ArrayList<TxCt>();
    for (TxCtLn tcl : allTaxCatsLns) {
      boolean txListed = false;
      for (Tax tx : usedTaxes) {
        if (tx.getIid().equals(tcl.getTax().getIid())) {
          txListed = true;
          break;
        }
      }
      if (!txListed) {
        usedTaxes.add(tcl.getTax());
      }
      int tci = -1;
      for (TxCt tc : usedTaxCats) {
        if (tc.getIid().equals(tcl.getOwnr().getIid())) {
          tci = usedTaxCats.indexOf(tc);
          break;
        }
      }
      if (tci == -1) {
        usedTaxCats.add(tcl.getOwnr());
        tcl.getOwnr().setTxs(new ArrayList<TxCtLn>());
        tcl.getOwnr().getTxs().add(tcl);
      } else {
        usedTaxCats.get(tci).getTxs().add(tcl);
      }
    }
    Collections.sort(usedTaxes, new CmpHasIdLn<Tax>());
    Collections.sort(usedTaxCats, new CmpHasIdLn<TxCt>());
    boolean isOnlyTax = true;
    for (TxCt txc : usedTaxCats) {
      if (txc.getTxs().size() > 1) {
        isOnlyTax = false;
        break;
      }
    }
    String[] fdsItLf = new String[] {"itm", "itLf", "wrhp"};
    Arrays.sort(fdsItLf);
    vs.put("WrhItmndFds", fdsItLf);
    vs.put("ItmndFds", ndFdNm);
    vs.put("WrhPlndFds", ndFdNm);
    String qur =
    "select ITM, sum(ITLF) as ITLF, min(WRHP) as WRHP from WRHITM group by ITM";
    List<WrhItm> whRests = getOrm().retLstQu(pRvs, vs, WrhItm.class, qur);
    vs.clear();
    BigDecimal bd1d2 = new BigDecimal("1.2");
    BigDecimal bd100 = new BigDecimal("100");
    for (PriItm pg : gpl) {
      List<Object> row = new ArrayList<Object>();
      result.add(row);
      row.add(pg.getItm());
      row.add(pg.getPri());
      row.add(pg.getPri().divide(bd1d2, 2, RoundingMode.HALF_UP));
      BigDecimal quantity;
      Boolean avlb;
      WrhPl ws = null;
      if (unAvPri != null
        && pg.getPri().compareTo(unAvPri) == 0) {
        quantity = BigDecimal.ZERO;
        avlb = Boolean.FALSE;
      } else {
        WrhItm wr = findRest(pg.getItm().getIid(), whRests);
        if (wr != null) {
          quantity = wr.getItLf();
          avlb = Boolean.TRUE;
          ws = wr.getWrhp();
        } else {
          if (optmQuan == null) {
            quantity = BigDecimal.ZERO;
            avlb = Boolean.FALSE;
          } else {
            quantity = optmQuan;
            avlb = Boolean.TRUE;
          }
        }
      }
      row.add(quantity);
      row.add(avlb);
      row.add(ws);
      if (pg.getItm().getTxCt() != null) {
        for (TxCt txc : usedTaxCats) {
          if (txc.getIid().equals(pg.getItm()
            .getTxCt().getIid())) {
            //tax category with tax lines:
            pg.getItm().setTxCt(txc);
            break;
          }
        }
      }
      if (isOnlyTax) {
        TaxWr onlyTax = new TaxWr();
        if (pg.getItm().getTxCt() != null) {
          onlyTax.setTax(pg.getItm().getTxCt().getTxs()
            .get(0).getTax());
          onlyTax.setUsed(true);
          onlyTax.setRate(pg.getItm().getTxCt().getTxs().get(0).getRate()
            .divide(bd100, as.getTxDp() + 2, RoundingMode.HALF_UP));
        }
        row.add(onlyTax);
      } else { //multiply taxes case:
        TxCtWr taxCat = new TxCtWr();
        if (pg.getItm().getTxCt() != null) {
          taxCat.setTxCt(pg.getItm().getTxCt());
          taxCat.setUsed(true);
          for (TxCtLn tl : taxCat.getTxCt().getTxs()) {
            taxCat.setAggrPercent(taxCat.getAggrPercent()
              .add(tl.getRate()));
          }
          taxCat.setAggrRate(taxCat.getAggrPercent()
            .divide(bd100, as.getTxDp() + 2, RoundingMode.HALF_UP));
        }
        row.add(taxCat);
        for (Tax tx : usedTaxes) {
          TaxWr txWr = new TaxWr();
          if (pg.getItm().getTxCt() != null) {
            for (TxCtLn tl : pg.getItm().getTxCt().getTxs()) {
              if (tl.getTax().getIid().equals(tx.getIid())) {
                txWr.setTax(tl.getTax());
                txWr.setUsed(true);
                txWr.setRate(tl.getRate().divide(bd100,
                  as.getTxDp() + 2, RoundingMode.HALF_UP));
                break;
              }
            }
          }
          row.add(txWr);
        }
        for (TxCt txc : usedTaxCats) {
          TxCtWr txCtWr = new TxCtWr();
          if (pg.getItm().getTxCt() != null && txc.getIid()
            .equals(pg.getItm().getTxCt().getIid())) {
            txCtWr.setTxCt(txc);
            txCtWr.setUsed(true);
            for (TxCtLn tl : txCtWr.getTxCt().getTxs()) {
              txCtWr.setAggrPercent(txCtWr.getAggrPercent()
                .add(tl.getRate()));
            }
            txCtWr.setAggrRate(txCtWr.getAggrPercent().divide(bd100,
              as.getTxDp() + 2, RoundingMode.HALF_UP));
          }
          row.add(txCtWr);
        }
      }
    }
    return result;
  }

  /**
   * <p>Retrieves sample data row (tree) to make CSV column.</p>
   * @param pRvs additional param
   * @return sample data row
   * @throws Exception an Exception
   **/
  @Override
  public final List<Node<String>> getSmpDtRow(
    final Map<String, Object> pRvs) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    String lang = cpf.getLngDef().getIid();
    List<Node<String>> result = new ArrayList<Node<String>>();
    Integer idx = 1;
    Node<String> nodeGoods = new Node<String>();
    result.add(nodeGoods);
    nodeGoods.setNme(getI18n().getMsg("itm", lang));
    nodeGoods.setNodes(new ArrayList<Node<String>>());
    Node<String> nodeGoodsName = new Node<String>();
    nodeGoods.getNodes().add(nodeGoodsName);
    nodeGoodsName.setNme(getI18n().getMsg("nme", lang));
    nodeGoodsName.setVal(idx.toString() + ";nme");
    Node<String> nodeGoodsId = new Node<String>();
    nodeGoods.getNodes().add(nodeGoodsId);
    nodeGoodsId.setNme(getI18n().getMsg("iid", lang));
    nodeGoodsId.setVal(idx.toString() + ";iid");
    Node<String> nodeGoodsItsCategory = new Node<String>();
    nodeGoods.getNodes().add(nodeGoodsItsCategory);
    nodeGoodsItsCategory.setNme(getI18n().getMsg("cat", lang));
    nodeGoodsItsCategory.setNodes(new ArrayList<Node<String>>());
    Node<String> nodeGoodsItsCategoryName = new Node<String>();
    nodeGoodsItsCategory.getNodes().add(nodeGoodsItsCategoryName);
    nodeGoodsItsCategoryName.setNme(getI18n().getMsg("nme", lang));
    nodeGoodsItsCategoryName
      .setVal(idx.toString() + ";cat,nme");
    Node<String> nodeGoodsItsCategoryId = new Node<String>();
    nodeGoodsItsCategory.getNodes().add(nodeGoodsItsCategoryId);
    nodeGoodsItsCategoryId.setNme(getI18n().getMsg("iid", lang));
    nodeGoodsItsCategoryId.setVal(idx.toString() + ";cat,iid");
    Node<String> nodeGoodsDefUom = new Node<String>();
    nodeGoods.getNodes().add(nodeGoodsDefUom);
    nodeGoodsDefUom.setNme(getI18n()
      .getMsg("duom", lang));
    nodeGoodsDefUom.setNodes(new ArrayList<Node<String>>());
    Node<String> nodeGoodsDefUomName = new Node<String>();
    nodeGoodsDefUom.getNodes().add(nodeGoodsDefUomName);
    nodeGoodsDefUomName
      .setNme(getI18n().getMsg("nme", lang));
    nodeGoodsDefUomName
      .setVal(idx.toString() + ";duom,nme");
    Node<String> nodeGoodsDefUomId = new Node<String>();
    nodeGoodsDefUom.getNodes().add(nodeGoodsDefUomId);
    nodeGoodsDefUomId.setNme(getI18n().getMsg("iid", lang));
    nodeGoodsDefUomId
      .setVal(idx.toString() + ";duom,iid");
    idx++;
    Node<String> nodePrice = new Node<String>();
    result.add(nodePrice);
    nodePrice.setNme(getI18n().getMsg("pri", lang));
    nodePrice.setVal(idx.toString());
    idx++;
    Node<String> nodeCost = new Node<String>();
    result.add(nodeCost);
    nodeCost.setNme(getI18n().getMsg("cost", lang));
    nodeCost.setVal(idx.toString());
    idx++;
    Node<String> nodeQuantity = new Node<String>();
    result.add(nodeQuantity);
    nodeQuantity.setNme(getI18n().getMsg("quan", lang));
    nodeQuantity.setVal(idx.toString());
    idx++;
    Node<String> nodeIsAvailable = new Node<String>();
    result.add(nodeIsAvailable);
    nodeIsAvailable.setNme(getI18n().getMsg("avlb", lang));
    nodeIsAvailable.setVal(idx.toString());
    idx++;
    Node<String> nodeWrhPl = new Node<String>();
    result.add(nodeWrhPl);
    nodeWrhPl.setNme(getI18n().getMsg("WrhPl", lang));
    nodeWrhPl.setNodes(new ArrayList<Node<String>>());
    Node<String> nodeWrhPlName = new Node<String>();
    nodeWrhPl.getNodes().add(nodeWrhPlName);
    nodeWrhPlName.setNme(getI18n().getMsg("nme", lang));
    nodeWrhPlName.setVal(idx.toString() + ";nme");
    Node<String> nodeWrhPlId = new Node<String>();
    nodeWrhPl.getNodes().add(nodeWrhPlId);
    nodeWrhPlId.setNme(getI18n().getMsg("iid", lang));
    nodeWrhPlId.setVal(idx.toString() + ";iid");
    Node<String> nodeWrhPlWrh = new Node<String>();
    nodeWrhPl.getNodes().add(nodeWrhPlWrh);
    nodeWrhPlWrh
      .setNme(getI18n().getMsg("wrh", lang));
    nodeWrhPlWrh.setNodes(new ArrayList<Node<String>>());
    Node<String> nodeWrhPlWrhName = new Node<String>();
    nodeWrhPlWrh.getNodes()
      .add(nodeWrhPlWrhName);
    nodeWrhPlWrhName
      .setNme(getI18n().getMsg("nme", lang));
    nodeWrhPlWrhName
      .setVal(idx.toString() + ";wrh,nme");
    Node<String> nodeWrhPlWrhId = new Node<String>();
    nodeWrhPlWrh.getNodes().add(nodeWrhPlWrhId);
    nodeWrhPlWrhId.setNme(getI18n().getMsg("iid", lang));
    nodeWrhPlWrhId
      .setVal(idx.toString() + ";wrh,iid");
    Set<String> ndFdNm = new HashSet<String>();
    ndFdNm.add("iid");
    ndFdNm.add("nme");
    pRvs.put("TxCtndFds", ndFdNm);
    pRvs.put("TaxndFds", ndFdNm);
    List<TxCtLn> allTaxCatsLns = getOrm().retLst(pRvs, vs, TxCtLn.class);
    vs.clear();
    List<Tax> usedTaxes = new ArrayList<Tax>();
    List<TxCt> usedTaxCats = new ArrayList<TxCt>();
    for (TxCtLn tcl : allTaxCatsLns) {
      boolean txListed = false;
      for (Tax tx : usedTaxes) {
        if (tx.getIid().equals(tcl.getTax().getIid())) {
          txListed = true;
          break;
        }
      }
      if (!txListed) {
        usedTaxes.add(tcl.getTax());
      }
      int tci = -1;
      for (TxCt tc : usedTaxCats) {
        if (tc.getIid().equals(tcl.getOwnr().getIid())) {
          tci = usedTaxCats.indexOf(tc);
          break;
        }
      }
      if (tci == -1) {
        usedTaxCats.add(tcl.getOwnr());
        tcl.getOwnr().setTxs(new ArrayList<TxCtLn>());
        tcl.getOwnr().getTxs().add(tcl);
      } else {
        usedTaxCats.get(tci).getTxs().add(tcl);
      }
    }
    boolean isOnlyTax = true;
    for (TxCt txc : usedTaxCats) {
      if (txc.getTxs().size() > 1) {
        isOnlyTax = false;
        break;
      }
    }
    if (isOnlyTax) {
      idx++;
      addTaxWr(result, idx.toString(),
        getI18n().getMsg("OnlyTax", lang), lang);
    } else {
      idx++;
      addTaxCatWr(result, idx.toString(),
        getI18n().getMsg("txCt", lang), lang);
      Collections.sort(usedTaxes, new CmpHasIdLn<Tax>());
      for (Tax tx : usedTaxes) {
        idx++;
        addTaxWr(result, idx.toString(), tx.getNme(), lang);
      }
      Collections.sort(usedTaxCats, new CmpHasIdLn<TxCt>());
      for (TxCt txc : usedTaxCats) {
        idx++;
        addTaxCatWr(result, idx.toString(), txc.getNme(), lang);
      }
    }
    return result;
  }

  //Utils:
  /**
   * <p>Add tax wrapper 1-st level node.</p>
   * @param pTree tree nodes
   * @param pIndex Index
   * @param pName Name
   * @param pLang Language
   **/
  public final void addTaxWr(final List<Node<String>> pTree,
    final String pIndex, final String pName, final String pLang) {
    Node<String> nodeTaxWr = new Node<String>();
    pTree.add(nodeTaxWr);
    nodeTaxWr.setNme(pName);
    nodeTaxWr.setNodes(new ArrayList<Node<String>>());
    Node<String> nodeTaxWrUsed = new Node<String>();
    nodeTaxWr.getNodes().add(nodeTaxWrUsed);
    nodeTaxWrUsed.setNme(getI18n().getMsg("used", pLang));
    nodeTaxWrUsed.setVal(pIndex + ";used");
    Node<String> nodeTaxWrRate = new Node<String>();
    nodeTaxWr.getNodes().add(nodeTaxWrRate);
    nodeTaxWrRate.setNme(getI18n().getMsg("rate", pLang));
    nodeTaxWrRate.setVal(pIndex + ";rate");
    Node<String> nodeTaxWrTax = new Node<String>();
    nodeTaxWr.getNodes().add(nodeTaxWrTax);
    nodeTaxWrTax.setNme(getI18n().getMsg("tax", pLang));
    nodeTaxWrTax.setNodes(new ArrayList<Node<String>>());
    Node<String> nodeTaxWrTaxName = new Node<String>();
    nodeTaxWrTax.getNodes().add(nodeTaxWrTaxName);
    nodeTaxWrTaxName.setNme(getI18n().getMsg("nme", pLang));
    nodeTaxWrTaxName.setVal(pIndex + ";tax,nme");
    Node<String> nodeTaxWrTaxId = new Node<String>();
    nodeTaxWrTax.getNodes().add(nodeTaxWrTaxId);
    nodeTaxWrTaxId.setNme(getI18n().getMsg("iid", pLang));
    nodeTaxWrTaxId.setVal(pIndex + ";tax,iid");
    Node<String> nodeTaxWrTaxPercentage = new Node<String>();
    nodeTaxWrTax.getNodes().add(nodeTaxWrTaxPercentage);
    nodeTaxWrTaxPercentage
      .setNme(getI18n().getMsg("rate", pLang));
    nodeTaxWrTaxPercentage.setVal(pIndex + ";tax,rate");
  }

  /**
   * <p>Add tax category wrapper 1-st level node.</p>
   * @param pTree tree nodes
   * @param pIndex Index
   * @param pName Name
   * @param pLang Language
   **/
  public final void addTaxCatWr(final List<Node<String>> pTree,
    final String pIndex, final String pName, final String pLang) {
    Node<String> nodeTaxCatWr = new Node<String>();
    pTree.add(nodeTaxCatWr);
    nodeTaxCatWr.setNme(pName);
    nodeTaxCatWr.setNodes(new ArrayList<Node<String>>());
    Node<String> nodeTaxCatWrUsed = new Node<String>();
    nodeTaxCatWr.getNodes().add(nodeTaxCatWrUsed);
    nodeTaxCatWrUsed.setNme(getI18n().getMsg("used", pLang));
    nodeTaxCatWrUsed.setVal(pIndex + ";used");
    Node<String> nodeTaxCatWrPercent = new Node<String>();
    nodeTaxCatWr.getNodes().add(nodeTaxCatWrPercent);
    nodeTaxCatWrPercent.setNme(getI18n().getMsg("aggrPercent", pLang));
    nodeTaxCatWrPercent.setVal(pIndex + ";aggrPercent");
    Node<String> nodeTaxCatWrRate = new Node<String>();
    nodeTaxCatWr.getNodes().add(nodeTaxCatWrRate);
    nodeTaxCatWrRate.setNme(getI18n().getMsg("aggrRate", pLang));
    nodeTaxCatWrRate.setVal(pIndex + ";aggrRate");
    Node<String> nodeTaxCatWrTaxCat = new Node<String>();
    nodeTaxCatWr.getNodes().add(nodeTaxCatWrTaxCat);
    nodeTaxCatWrTaxCat.setNme(getI18n().getMsg("txCt", pLang));
    nodeTaxCatWrTaxCat.setNodes(new ArrayList<Node<String>>());
    Node<String> nodeTaxCatWrTaxCatName = new Node<String>();
    nodeTaxCatWrTaxCat.getNodes().add(nodeTaxCatWrTaxCatName);
    nodeTaxCatWrTaxCatName.setNme(getI18n().getMsg("nme", pLang));
    nodeTaxCatWrTaxCatName.setVal(pIndex + ";txCt,nme");
    Node<String> nodeTaxCatWrTaxCatId = new Node<String>();
    nodeTaxCatWrTaxCat.getNodes().add(nodeTaxCatWrTaxCatId);
    nodeTaxCatWrTaxCatId.setNme(getI18n().getMsg("iid", pLang));
    nodeTaxCatWrTaxCatId.setVal(pIndex + ";txCt,iid");
  }

  /**
   * <p>Finds wrh rest line by item ID.</p>
   * @param pItmId Itm ID
   * @param pRestList Rest List
   * @return Wrh rest line or null if not found
   **/
  public final WrhItm findRest(final Long pItmId,
    final List<WrhItm> pRestList) {
    for (WrhItm wr : pRestList) {
      if (wr.getItm().getIid().equals(pItmId)) {
        return wr;
      }
    }
    return null;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for orm.</p>
   * @return IOrm<RS>
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
   * <p>Getter for i18n.</p>
   * @return I18n
   **/
  public final I18n getI18n() {
    return this.i18n;
  }

  /**
   * <p>Setter for i18n.</p>
   * @param pI18n reference
   **/
  public final void setI18n(final I18n pI18n) {
    this.i18n = pI18n;
  }
}
