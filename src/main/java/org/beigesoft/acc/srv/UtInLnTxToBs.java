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

package org.beigesoft.acc.srv;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.beigesoft.log.ILog;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdl.IRecSet;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.fct.IFctRq;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.srv.INumStr;
import org.beigesoft.acc.mdl.ETxTy;
import org.beigesoft.acc.mdl.TaxEx;
import org.beigesoft.acc.mdl.CmprTxCtLnRt;
import org.beigesoft.acc.mdl.CmprTaxTot;
import org.beigesoft.acc.mdl.CmprInvLnTot;
import org.beigesoft.acc.mdlb.AInTxLn;
import org.beigesoft.acc.mdlb.ALnTxLn;
import org.beigesoft.acc.mdlb.ATxDsLn;
import org.beigesoft.acc.mdlb.IInvb;
import org.beigesoft.acc.mdlb.IInvLn;
import org.beigesoft.acc.mdlb.TxDtLn;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Tax;
import org.beigesoft.acc.mdlp.TxDst;
import org.beigesoft.acc.mdlp.TxCt;
import org.beigesoft.acc.mdlp.TxCtLn;

/**
 * <p>Utility for purchase/sales invoice.
 * It makes taxes and totals for line and invoice.
 * Base shared code-bunch.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class UtInLnTxToBs<RS> {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Service print number.</p>
   **/
  private INumStr numStr;

  //Invoice's level code:
  /**
   * <p>Reveal shared tax rules for invoice..</p>
   * @param pInv invoice
   * @param pAs Accounting Settings
   * @param pIsExtrTx if extract taxes
   * @return tax rules, NULL if not taxable
   * @throws Exception - an exception.
   **/
  public final TxDst revealTaxRules(final IInvb pInv, final AcStg pAs,
    final Boolean pIsExtrTx) throws Exception {
    TxDst txRules = null;
    if (pIsExtrTx && !pInv.getOmTx()) {
      if (pInv.getDbcr().getTxDs() != null) {
        //override tax method:
        txRules = pInv.getDbcr().getTxDs();
      } else {
        txRules = new TxDst();
        txRules.setStIb(pAs.getStIb());
        txRules.setStAg(pAs.getStAg());
        txRules.setStRm(pAs.getStRm());
      }
    }
    return txRules;
  }

  /**
   * <p>Makes invoice totals include taxes lines
   * cause line inserted/changed/deleted.</p>
   * @param <T> invoice type
   * @param <L> invoice line type
   * @param <TL> invoice tax line type
   * @param pRvs request scoped vars
   * @param pVs Invoker scoped variables, not null
   * @param pLine affected line
   * @param pAs Accounting Settings
   * @param pTxRules NULL if not taxable
   * @param pInvTxMeth tax method code/data for purchase/sales invoice
   * @throws Exception - an exception.
   **/
  public final <T extends IInvb, L extends IInvLn<T, ?>,
    TL extends AInTxLn<T>> void makeTotals(final Map<String, Object> pRvs,
      final Map<String, Object> pVs, final L pLine, final AcStg pAs,
        final TxDst pTxRules,
          final IInvTxMeth<T, TL> pInvTxMeth) throws Exception {
    //all tax lines will be redone:
    pVs.put(pInvTxMeth.getInvCl().getSimpleName() + "dpLv", 0);
    List<TL> invTxLns = getOrm().retLstCnd(pRvs, pVs, pInvTxMeth.getInvTxLnCl(),
      "where OWNR=" + pLine.getOwnr().getIid());
    pVs.clear();
    for (TL tl : invTxLns) {
      tl.setDbOr(this.orm.getDbId());
      tl.setTax(null);
      tl.setTxb(BigDecimal.ZERO);
      tl.setTxbFc(BigDecimal.ZERO);
      tl.setTot(BigDecimal.ZERO);
      tl.setToFc(BigDecimal.ZERO);
    }
    if (pTxRules != null) {
      DataTx dtTx = retrieveDataTx(pRvs, pLine, pAs, pTxRules, pInvTxMeth);
      if (!pTxRules.getStAg() && !(pTxRules.getStIb()
        && pLine.getOwnr().getInTx())) {
        //non-aggregate except invoice basis with included taxes:
        for (int i = 0; i < dtTx.getTxs().size(); i++) {
          TL tl = findCreateTaxLine(pRvs, pLine.getOwnr(),
            invTxLns, dtTx.getTxs().get(i), false, pInvTxMeth.getFctInvTxLn());
          Double txTotd;
          Double txTotdFc = 0.0;
          if (!pTxRules.getStIb()) {
            //item basis, taxes excluded/included:
            txTotd = dtTx.getTxTotTaxb().get(i);
            txTotdFc = dtTx.getTxTotTaxbFc().get(i);
          } else {
            //invoice basis, taxes excluded:
            txTotd = dtTx.getTxTotTaxb().get(i)
              * dtTx.getTxPerc().get(i) / 100.0;
            tl.setTxb(BigDecimal.valueOf(dtTx.getTxTotTaxb().get(i)));
            if (pLine.getOwnr().getCuFr() != null) {
              txTotdFc = dtTx.getTxTotTaxbFc().get(i)
                * dtTx.getTxPerc().get(i) / 100.0;
              tl.setTxbFc(BigDecimal
                .valueOf(dtTx.getTxTotTaxbFc().get(i)));
            }
          }
          tl.setTot(BigDecimal.valueOf(txTotd).setScale(pAs.
            getPrDp(), pTxRules.getStRm()));
          tl.setToFc(BigDecimal.valueOf(txTotdFc).setScale(pAs.
            getPrDp(), pTxRules.getStRm()));
          if (tl.getIsNew()) {
            getOrm().insIdLn(pRvs, pVs, tl);
          } else {
            getOrm().update(pRvs, pVs, tl);
          }
        }
      } else { //non-aggregate invoice basis with included taxes
        //and aggregate for others:
        BigDecimal bd100 = new BigDecimal("100.00");
        Comparator<TxCtLn> cmpr = Collections
          .reverseOrder(new CmprTxCtLnRt());
        for (TxDtLn txdLn : dtTx.getTxdLns()) {
          int ti = 0;
          //aggregate rate line scoped storages:
          BigDecimal taxAggegated = null;
          BigDecimal taxAggrAccum = BigDecimal.ZERO;
          BigDecimal taxAggegatedFc = BigDecimal.ZERO;
          BigDecimal taxAggrAccumFc = BigDecimal.ZERO;
          Collections.sort(txdLn.getTxCt().getTxs(), cmpr);
          for (TxCtLn itcl : txdLn.getTxCt().getTxs()) {
            ti++;
            if (taxAggegated == null) {
              if (pTxRules.getStIb()) { //invoice basis:
                if (pLine.getOwnr().getInTx()) {
                  taxAggegated = txdLn.getTot().subtract(txdLn.getTot()
              .divide(BigDecimal.ONE.add(txdLn.getTxCt().getAgRt().divide(bd100,
            15, RoundingMode.HALF_UP)), pAs.getPrDp(), pTxRules.getStRm()));
                  if (pLine.getOwnr().getCuFr() != null) {
                    taxAggegatedFc = txdLn.getToFc().subtract(txdLn.getToFc()
              .divide(BigDecimal.ONE.add(txdLn.getTxCt().getAgRt().divide(bd100,
            15, RoundingMode.HALF_UP)), pAs.getPrDp(), pTxRules.getStRm()));
                  }
                } else {
                  taxAggegated = txdLn.getSubt().multiply(txdLn
                    .getTxCt().getAgRt()).divide(bd100, pAs
                      .getPrDp(), pTxRules.getStRm());
                  if (pLine.getOwnr().getCuFr() != null) {
                    taxAggegatedFc = txdLn.getSuFc().multiply(txdLn
                      .getTxCt().getAgRt()).divide(bd100, pAs
                        .getPrDp(), pTxRules.getStRm());
                  }
                }
              } else {
                //item basis, taxes included/excluded
                taxAggegated = txdLn.getToTx();
                taxAggegatedFc = txdLn.getTxFc();
              }
            }
            //here total taxes means total for current tax:
            if (ti < txdLn.getTxCt().getTxs().size()) {
              txdLn.setToTx(taxAggegated.multiply(itcl.getRate())
                .divide(txdLn.getTxCt().getAgRt(),
                  pAs.getPrDp(), pTxRules.getStRm()));
              taxAggrAccum = taxAggrAccum.add(txdLn.getToTx());
              if (pLine.getOwnr().getCuFr() != null) {
                txdLn.setTxFc(taxAggegatedFc.multiply(itcl
                  .getRate()).divide(txdLn.getTxCt().getAgRt(),
                    pAs.getPrDp(), pTxRules.getStRm()));
                taxAggrAccumFc = taxAggrAccumFc.add(txdLn.getTxFc());
              }
            } else { //the rest or only tax:
              txdLn.setToTx(taxAggegated.subtract(taxAggrAccum));
              if (pLine.getOwnr().getCuFr() != null) {
                txdLn.setTxFc(taxAggegatedFc.subtract(taxAggrAccumFc));
              }
            }
            TL tl = findCreateTaxLine(pRvs, pLine.getOwnr(),
              invTxLns, itcl.getTax(), true, pInvTxMeth.getFctInvTxLn());
            tl.setTot(tl.getTot().add(txdLn.getToTx()));
            if (pLine.getOwnr().getCuFr() != null) {
              tl.setToFc(tl.getToFc().add(txdLn.getTxFc()));
            }
            if (pTxRules.getStIb()) {
              if (ti == txdLn.getTxCt().getTxs().size()) {
                //total line taxes for farther invoice adjusting:
                txdLn.setToTx(taxAggegated);
                txdLn.setTxFc(taxAggegatedFc);
              }
              if (!pLine.getOwnr().getInTx()) {
                tl.setTxb(tl.getTxb().add(txdLn.getSubt()));
                if (pLine.getOwnr().getCuFr() != null) {
                  tl.setTxbFc(tl.getTxbFc().add(txdLn.getSuFc()));
                }
              } else {
                tl.setTxb(tl.getTxb().add(txdLn.getTot()));
                if (pLine.getOwnr().getCuFr() != null) {
                  tl.setTxbFc(tl.getTxbFc().add(txdLn.getToFc()));
                }
              }
            }
            if (tl.getIsNew()) {
              getOrm().insIdLn(pRvs, pVs, tl);
            } else {
              tl.setDscr(null); //clears hands made changing history Inv.Basis
              getOrm().update(pRvs, pVs, tl);
            }
          }
        }
      }
      if (pTxRules.getStIb()) {
        adjustInvoiceLns(pRvs, pVs, pLine.getOwnr(),
          dtTx.getTxdLns(), pAs, pInvTxMeth);
      }
    }
    //delete tax lines with zero tax:
    for (TL tl : invTxLns) {
      if (tl.getTax() == null) {
        getOrm().del(pRvs, pVs, tl);
      }
    }
    if (pTxRules != null && !pTxRules.getStAg() && pLine.getOwnr().getInTx()) {
      String watr = "TTR without aggregate! ";
      if (pLine.getOwnr().getDscr() == null) {
        pLine.getOwnr().setDscr(watr);
      } else if (!pLine.getOwnr().getDscr().contains(watr)) {
        pLine.getOwnr().setDscr(watr + pLine.getOwnr().getDscr());
      }
    }
    updInvTots(pRvs, pVs, pLine.getOwnr(), pAs, pInvTxMeth);
  }

  /**
   * <p>Update invoice totals after its line has been changed/deleted
   * and taxes lines has been made
   * or after tax line has been changed (Invoice basis).</p>
   * @param <T> invoice type
   * @param pRvs additional param
   * @param pVs Invoker scoped variables, not null
   * @param pInv Invoice
   * @param pAs accounting settings
   * @param pInvTxMeth tax method code/data for purchase/sales invoice
   * @throws Exception - an exception
   **/
  public final <T extends IInvb> void updInvTots(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final T pInv, final AcStg pAs,
      final IInvTxMeth<T, ?> pInvTxMeth) throws Exception {
    String query = pInvTxMeth.lazyGetQuTotals();
    query = query.replace(":OWNR", pInv.getIid().toString());
    if (pInvTxMeth.getTblNmsTot().length == 5) { //sales/purchase:
      query = query.replace(":TGDLN", pInvTxMeth.getTblNmsTot()[0]);
      query = query.replace(":TSRVLN", pInvTxMeth.getTblNmsTot()[1]);
      query = query.replace(":TTAXLN", pInvTxMeth.getTblNmsTot()[2]);
    } else { //returns:
      query = query.replace(":TGDLN", pInvTxMeth.getTblNmsTot()[0]);
      query = query.replace(":TTAXLN", pInvTxMeth.getTblNmsTot()[1]);
    }
    String[] cls = new String[] {"TOT", "TOTX", "TOFC", "TXFC"};
    Double[] totals = getRdb().evDoubles(query, cls);
    if (totals[0] == null) {
      totals[0] = 0d;
    }
    if (totals[1] == null) {
      totals[1] = 0d;
    }
    if (totals[2] == null) {
      totals[2] = 0d;
    }
    if (totals[3] == null) {
      totals[3] = 0d;
    }
    pInv.setTot(BigDecimal.valueOf(totals[0]).setScale(
      pAs.getPrDp(), pAs.getRndm()));
    pInv.setToTx(BigDecimal.valueOf(totals[1]).setScale(
      pAs.getPrDp(), pAs.getStRm()));
    pInv.setSubt(pInv.getTot().subtract(pInv.getToTx()));
    pInv.setToFc(BigDecimal.valueOf(totals[2]).setScale(
      pAs.getPrDp(), pAs.getRndm()));
    pInv.setTxFc(BigDecimal.valueOf(totals[3]).setScale(
      pAs.getPrDp(), pAs.getStRm()));
    pInv.setSuFc(pInv.getToFc().subtract(pInv.getTxFc()));
    String[] fdsUpd =  new String[] {"subt", "suFc", "toFc", "tot",
      "toTx", "txFc", "ver"};
    Arrays.sort(fdsUpd);
    pVs.put("ndFds", fdsUpd);
    getOrm().update(pRvs, pVs, pInv); pVs.clear();
  }

  /**
   * <p>Adjust invoice lines totals/subtotals for invoice basis method.</p>
   * @param <T> invoice type
   * @param <L> invoice line type
   * @param <TL> invoice tax line type
   * @param pRvs additional param
   * @param pVs Invoker scoped variables, not null
   * @param pInv invoice
   * @param pTxdLns Tax Data lines
   * @param pAs AS
   * @param pInvTxMeth tax method code/data for purchase/sales invoice
   * @throws Exception an Exception
   **/
  public final <T extends IInvb, L extends IInvLn<T, ?>,
    TL extends AInTxLn<T>> void adjustInvoiceLns(final Map<String, Object> pRvs,
      final Map<String, Object> pVs, final T pInv, final List<TxDtLn> pTxdLns,
        final AcStg pAs, final IInvTxMeth<T, TL> pInvTxMeth) throws Exception {
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 11000);
    if (dbgSh) {
      getLog().debug(pRvs, UtInLnTxToBs.class,
        "Starting adjusting invoice basis lines...");
      getLog().debug(pRvs, UtInLnTxToBs.class,
      "txdLns [tax cat.ID/subt/suFc/tot/toFc/toTx/txFc]: " + pTxdLns);
    }
    String[] fdsSel =
      new String[] {"iid", "subt", "suFc", "toFc", "tot", "txCt", "ver"};
    Arrays.sort(fdsSel);
    pVs.put("TxCtdpLv", 0);
    pVs.put(pInvTxMeth.getGoodLnCl().getSimpleName() + "ndFds", fdsSel);
    List<? extends IInvLn<T, ?>> gls = getOrm().retLstCnd(pRvs, pVs, pInvTxMeth
      .getGoodLnCl(), "where TXCT is not null and RVID is null and OWNR="
        + pInv.getIid());
    pVs.clear();
    List<? extends IInvLn<T, ?>> sls = null;
    if (pInvTxMeth.getServiceLnCl() != null) {
      pVs.put("TxCtdpLv", 0);
      pVs.put(pInvTxMeth.getServiceLnCl().getSimpleName() + "ndFds", fdsSel);
      sls = getOrm().retLstCnd(pRvs, pVs, pInvTxMeth.getServiceLnCl(),
        "where TXCT is not null and RVID is null and OWNR=" + pInv.getIid());
      pVs.clear();
    }
    //matched to current tax category (affected) invoice lines:
    List<IInvLn<T, ?>> ilsm = new ArrayList<IInvLn<T, ?>>();
    Comparator<IInvLn<?, ?>> cmpr = Collections
      .reverseOrder(new CmprInvLnTot());
    String[] fdsUpd =  new String[] {"subt", "suFc", "toFc", "tot", "toTx",
      "txFc", "ver"};
    Arrays.sort(fdsUpd);
    pVs.put("ndFds", fdsUpd);
    for (TxDtLn txdLn : pTxdLns) {
      for (IInvLn<T, ?> gl : gls) {
        if (gl.getTxCt().getIid().equals(txdLn.getTxCt().getIid())) {
          ilsm.add(gl);
        }
      }
      if (sls != null) {
        for (IInvLn<T, ?> sl : sls) {
          if (sl.getTxCt().getIid().equals(txdLn.getTxCt().getIid())) {
            ilsm.add(sl);
          }
        }
      }
      Collections.sort(ilsm, cmpr);
      BigDecimal txRest = txdLn.getToTx();
      BigDecimal txRestFc = txdLn.getTxFc();
      for (int i = 0; i < ilsm.size(); i++) {
        if (i + 1 == ilsm.size()) {
          if (pInv.getInTx()) {
            ilsm.get(i).setSubt(ilsm.get(i).getTot().subtract(txRest));
          } else {
            ilsm.get(i).setTot(ilsm.get(i).getSubt().add(txRest));
          }
          ilsm.get(i).setToTx(txRest);
          if (pInv.getCuFr() != null) {
            if (pInv.getInTx()) {
              ilsm.get(i).setSuFc(ilsm.get(i).getToFc().subtract(txRestFc));
            } else {
              ilsm.get(i).setToFc(ilsm.get(i).getSuFc().add(txRestFc));
            }
            ilsm.get(i).setTxFc(txRestFc);
          }
        } else {
          BigDecimal taxTot;
          if (pInv.getInTx()) {
            taxTot = txdLn.getToTx().multiply(ilsm.get(i).getTot())
              .divide(txdLn.getTot(), pAs.getPrDp(), RoundingMode.HALF_UP);
            ilsm.get(i).setSubt(ilsm.get(i).getTot().subtract(taxTot));
          } else {
            taxTot = txdLn.getToTx().multiply(ilsm.get(i).getSubt())
              .divide(txdLn.getSubt(), pAs.getPrDp(), RoundingMode.HALF_UP);
            ilsm.get(i).setTot(ilsm.get(i).getSubt().add(taxTot));
          }
          ilsm.get(i).setToTx(taxTot);
          txRest = txRest.subtract(taxTot);
          if (pInv.getCuFr() != null) {
            BigDecimal taxTotFc;
            if (pInv.getInTx()) {
              taxTotFc = txdLn.getTxFc().multiply(ilsm.get(i).getToFc())
                .divide(txdLn.getToFc(), pAs.getPrDp(), RoundingMode.HALF_UP);
              ilsm.get(i).setSuFc(ilsm.get(i).getToFc().subtract(taxTotFc));
            } else {
              taxTotFc = txdLn.getTxFc().multiply(ilsm.get(i).getSuFc())
                .divide(txdLn.getSuFc(), pAs.getPrDp(), RoundingMode.HALF_UP);
              ilsm.get(i).setToFc(ilsm.get(i).getSuFc().add(taxTotFc));
            }
            ilsm.get(i).setTxFc(taxTotFc);
            txRestFc = txRestFc.subtract(taxTotFc);
          }
        }
        getOrm().update(pRvs, pVs, ilsm.get(i));
      }
      ilsm.clear();
    }
    pVs.clear();
  }

  /**
   * <p>Retrieve from database tax data for adjusting invoice lines after
   * invoice tax line has been changed by user.</p>
   * @param <T> invoice type
   * @param <L> invoice line type
   * @param <TL> invoice tax line type
   * @param pRvs request scoped vars
   * @param pInv affected invoice
   * @param pAs Accounting Settings
   * @param pTxRules taxable rules
   * @param pInvTxMeth tax method code/data for purchase/sales invoice
   * @return taxes data
   * @throws Exception - an exception.
   **/
  public final <T extends IInvb, L extends IInvLn<T, ?>,
    TL extends AInTxLn<T>> ArrayList<TxDtLn> retrTxdLnsAdjInv(
      final Map<String, Object> pRvs, final T pInv, final AcStg pAs,
        final TxDst pTxRules,
          final IInvTxMeth<T, TL> pInvTxMeth) throws Exception {
    //totals by tax category for farther adjusting invoice:
    ArrayList<TxDtLn> txdLns = new ArrayList<TxDtLn>();
    //taxable/tax totals by "tax category, tax", map key is ID of tax category
    //from all  invoice lines
    //TaxEx holds total taxable/tax and total taxable/tax FC by tax
    //e.g. item A "tax18%, tax3%", item B "tax18%, tax1%"
    //there tax18% is used in two tax categories
    Map<Long, List<TaxEx>> tcTxs = new HashMap<Long,  List<TaxEx>>();
    //tax totals by tax from invoice tax lines for farther adjusting
    //by "tax category, tax", TaxEx holds total tax and total tax FC by tax
    ArrayList<TaxEx> txs = new ArrayList<TaxEx>();
    String query = pInvTxMeth.lazyGetQuTxInvAdj();
    if (pInvTxMeth.getTblNmsTot().length == 5) { //sales/purchase:
      query = query.replace(":TGDLN", pInvTxMeth.getTblNmsTot()[0]);
      query = query.replace(":TSRVLN", pInvTxMeth.getTblNmsTot()[1]);
      query = query.replace(":TTAXLN", pInvTxMeth.getTblNmsTot()[2]);
    } else { //returns:
      query = query.replace(":TGDLN", pInvTxMeth.getTblNmsTot()[0]);
      query = query.replace(":TTAXLN", pInvTxMeth.getTblNmsTot()[1]);
    }
    query = query.replace(":INVID", pInv.getIid().toString());
    IRecSet<RS> rs = null;
    BigDecimal bd100 = new BigDecimal("100.00");
    try {
      rs = getRdb().retRs(query);
      if (rs.first()) {
        do {
          Long txId = rs.getLong("TAXID");
          String txNm = rs.getStr("TAXNME");
          int li = txs.size() - 1;
          if (!(li >= 0 && txs.get(li).getIid().equals(txId))) {
            TaxEx tax = new TaxEx();
            tax.setIid(txId);
            tax.setNme(txNm);
            Double totx = rs.getDouble("TOTX");
            Double txfc = rs.getDouble("TXFC");
            tax.setToTx(BigDecimal.valueOf(totx));
            tax.setTxFc(BigDecimal.valueOf(txfc));
            txs.add(tax);
          }
          Double pr = rs.getDouble("RATE");
          Long tcId = rs.getLong("TXCTID");
          List<TaxEx> tctxs = null;
          for (Map.Entry<Long, List<TaxEx>> ent : tcTxs.entrySet()) {
            if (ent.getKey().equals(tcId)) {
              tctxs = ent.getValue();
              break;
            }
          }
          if (tctxs == null) {
            tctxs = new ArrayList<TaxEx>();
            tcTxs.put(tcId, tctxs);
          }
          TaxEx tctx = new TaxEx();
          tctx.setIid(txId);
          tctx.setNme(txNm);
          tctxs.add(tctx);
          BigDecimal prbd = BigDecimal.valueOf(pr);
          BigDecimal txv;
          BigDecimal txvf;
          if (!pInv.getInTx()) {
            Double su = rs.getDouble("SUBT");
            Double suf = rs.getDouble("SUFC");
            BigDecimal subd = BigDecimal.valueOf(su);
            BigDecimal subdf = BigDecimal.valueOf(suf);
            txv = subd.multiply(prbd).divide(bd100,
              pAs.getPrDp(), pTxRules.getStRm());
            txvf = subdf.multiply(prbd).divide(bd100,
              pAs.getPrDp(), pTxRules.getStRm());
            tctx.setTxb(subd);
            tctx.setTxbFc(subdf);
          } else {
            Double tot = rs.getDouble("TOT");
            Double totf = rs.getDouble("TOFC");
            BigDecimal totbd = BigDecimal.valueOf(tot);
            BigDecimal totbdf = BigDecimal.valueOf(totf);
            txv = totbd.subtract(totbd.divide(BigDecimal.ONE.add(prbd
              .divide(bd100, 15, RoundingMode.HALF_UP)),
                pAs.getPrDp(), pTxRules.getStRm()));
            txvf = totbdf.subtract(totbdf.divide(BigDecimal.ONE.add(prbd
              .divide(bd100, 15, RoundingMode.HALF_UP)),
                pAs.getPrDp(), pTxRules.getStRm()));
            tctx.setTxb(totbd);
            tctx.setTxbFc(totbdf);
          }
          tctx.setTxFc(txvf);
          tctx.setToTx(txv);
        } while (rs.next());
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 11001);
    if (dbgSh) {
      getLog().debug(pRvs, UtInLnTxToBs.class,
      "Tax totals by tax category: Tax category ID -"
        + "[ Tax ID/Name/taxable/taxable FC/total tax/total tax FC]: "
          + tcTxs.toString());
      getLog().debug(pRvs, UtInLnTxToBs.class,
      "Tax totals by tax [ Tax ID/Name/-/-/total tax/total tax FC]: "
          + txs.toString());
    }
    //adjusting "total tax by [tax] -> total tax by [tax category, tax]":
    Comparator<TaxEx> cmpr = new CmprTaxTot();
    for (TaxEx tx : txs) {
      List<TaxEx> tlns = new ArrayList<TaxEx>();
      for (Map.Entry<Long, List<TaxEx>> ent : tcTxs.entrySet()) {
        for (TaxEx tctx : ent.getValue()) {
          if (tctx.getIid().equals(tx.getIid())) {
            tlns.add(tctx);
          }
        }
      }
      Collections.sort(tlns, cmpr);
      BigDecimal txRest = tx.getToTx();
      BigDecimal txRestFc = tx.getTxFc();
      for (int i = 0; i < tlns.size(); i++) {
        if (i + 1 == tlns.size()) { //the biggest last gives the rest:
          tlns.get(i).setToTx(txRest);
          tlns.get(i).setTxFc(txRestFc);
        } else { //the first lines are kept unchanged:
          txRest = txRest.subtract(tlns.get(i).getToTx());
          txRestFc = txRestFc.subtract(tlns.get(i).getTxFc());
        }
      }
    }
    if (dbgSh) {
      getLog().debug(pRvs, UtInLnTxToBs.class,
      "After adjusting tax totals by tax category: Tax category ID -"
        + "[Tax ID/Name/taxable/taxable FC/total tax/total tax FC]: "
          + tcTxs.toString());
    }
    for (Map.Entry<Long, List<TaxEx>> ent : tcTxs.entrySet()) {
      TxDtLn txdLn = new TxDtLn();
      txdLn.setIid(ent.getKey());
      TxCt tc = new TxCt();
      tc.setIid(ent.getKey());
      txdLn.setTxCt(tc);
      txdLns.add(txdLn);
      for (TaxEx tx : ent.getValue()) {
        txdLn.setToTx(txdLn.getToTx().add(tx.getToTx()));
        txdLn.setTxFc(txdLn.getTxFc().add(tx.getTxFc()));
        if (!pInv.getInTx()) {
          txdLn.setSubt(txdLn.getSubt().add(tx.getTxb()));
          txdLn.setSuFc(txdLn.getSuFc().add(tx.getTxbFc()));
        } else {
          txdLn.setTot(txdLn.getTot().add(tx.getTxb()));
          txdLn.setToFc(txdLn.getToFc().add(tx.getTxbFc()));
        }
      }
    }
    return txdLns;
  }

  //Line's level code:
  /**
   * <p>Makes invoice line's taxes, totals.</p>
   * @param <T> invoice type
   * @param <L> invoice line type
   * @param <TL> invoice tax line type
   * @param <LTL> invoice line's tax line type
   * @param pRvs request scoped vars
   * @param pVs Invoker scoped variables, not null
   * @param pLine invoice line
   * @param pAs Accounting Settings
   * @param pTxRules NULL if not taxable
   * @param pInvTxMeth tax method code/data for purchase/sales invoice
   * @param pInvLnTxMeth tax method code/data for purchase/sales invoice line
   * @throws Exception - an exception.
   **/
  public final <T extends IInvb, L extends IInvLn<T, ?>,
    TL extends AInTxLn<T>, LTL extends ALnTxLn<T, L>> void mkLnTxTo(
      final Map<String, Object> pRvs, final Map<String, Object> pVs,
        final L pLine, final AcStg pAs, final TxDst pTxRules,
          final IInvTxMeth<T, TL> pInvTxMeth,
            final IInvLnTxMeth<T, L, LTL> pInvLnTxMeth) throws Exception {
    if (pInvLnTxMeth.getNeedMkTxCat()) {
      if (pTxRules != null) {
        boolean isSetOver = false;
        if (pLine.getOwnr().getDbcr().getTxDs() != null) {
          //override tax method:
          pVs.put(pInvLnTxMeth.getItmCl().getSimpleName() + "dpLv", 0);
          List<ATxDsLn<?>> dtls = (List<ATxDsLn<?>>) getOrm()
            .retLstCnd(pRvs, pVs, pInvLnTxMeth.getDstTxItLnCl(),
              "where OWNR=" + pLine.getItm().getIid());
          pVs.clear();
          for (ATxDsLn<?> dtl : dtls) {
            if (dtl.getTxDs().getIid().equals(pLine.getOwnr()
              .getDbcr().getTxDs().getIid())) {
              isSetOver = true;
              pLine.setTxCt(dtl.getTxCt()); //it may be null
              break;
            }
          }
        }
        if (!isSetOver) { //there is no overriding destination - set origin:
          String[] fdit = new String[] {"nme", "txCt"};
          Arrays.sort(fdit);
          String[] fdtc = new String[] {"nme", "agRt"};
          Arrays.sort(fdtc);
          pVs.put(pLine.getItm().getClass().getSimpleName() + "ndFds", fdit);
          pVs.put("TxCtndFds", fdtc);
          this.orm.refrEnt(pRvs, pVs, pLine.getItm()); pVs.clear();
          pLine.setTxCt(pLine.getItm().getTxCt());
        }
      } else {
        pLine.setTxCt(null);
      }
    }
    List<LTL> itls = null;
    if (pLine.getTxCt() != null) {
      if (!pTxRules.getStIb()) {
        if (!pTxRules.getStAg()) {
          itls = mkLnTxItBasNonAggr(pRvs, pVs, pLine, pAs, pTxRules,
            pInvTxMeth, pInvLnTxMeth);
        } else {
          BigDecimal totTxs = BigDecimal.ZERO;
          BigDecimal totTxsFc = BigDecimal.ZERO;
          BigDecimal bd100 = new BigDecimal("100.00");
          if (pLine.getOwnr().getInTx()) {
            totTxs = pLine.getTot().subtract(pLine.getTot().divide(BigDecimal
              .ONE.add(pLine.getTxCt().getAgRt().divide(bd100, 15,
                RoundingMode.HALF_UP)), pAs.getPrDp(), pTxRules.getStRm()));
            if (pLine.getOwnr().getCuFr() != null) {
            totTxsFc = pLine.getToFc().subtract(pLine.getToFc()
              .divide(BigDecimal.ONE.add(pLine.getTxCt().getAgRt().divide(bd100,
                15, RoundingMode.HALF_UP)), pAs.getPrDp(), pTxRules.getStRm()));
            }
          } else {
            totTxs = pLine.getSubt().multiply(pLine.getTxCt()
              .getAgRt()).divide(bd100, pAs.getPrDp(), pTxRules.getStRm());
            if (pLine.getOwnr().getCuFr() != null) {
              totTxsFc = pLine.getSuFc().multiply(pLine.getTxCt().getAgRt())
                .divide(bd100, pAs.getPrDp(), pTxRules.getStRm());
            }
          }
          pLine.setTdsc(pLine.getTxCt().getNme());
          mkLnFinal(pLine, totTxs, totTxsFc, pInvTxMeth.getIsTxByUser());
        }
      } else {
        pLine.setTdsc(pLine.getTxCt().getNme());
        pLine.setToTx(BigDecimal.ZERO);
        pLine.setTxFc(BigDecimal.ZERO);
      }
    } else {
      pLine.setTdsc(null);
      pLine.setToTx(BigDecimal.ZERO);
      pLine.setTxFc(BigDecimal.ZERO);
    }
    if (pLine.getOwnr().getCuFr() == null) {
      pLine.setTxFc(BigDecimal.ZERO);
      pLine.setToFc(BigDecimal.ZERO);
      pLine.setSuFc(BigDecimal.ZERO);
    }
    if (pLine.getTxCt() == null || pTxRules == null || !pTxRules.getStIb()) {
      //non-taxable or taxable item basis:
      if (pTxRules == null || pLine.getOwnr().getInTx()) {
        pLine.setSubt(pLine.getTot().subtract(pLine.getToTx()));
        if (pLine.getOwnr().getCuFr() != null) {
          pLine.setSuFc(pLine.getToFc().subtract(pLine.getTxFc()));
        }
      } else {
        pLine.setTot(pLine.getSubt().add(pLine.getToTx()));
        if (pLine.getOwnr().getCuFr() != null) {
          pLine.setToFc(pLine.getSuFc().add(pLine.getTxFc()));
        }
      }
    } //invoice basis - taxable lines tax, subt, tot will be adjusted later!
    if (pLine.getIsNew()) {
      getOrm().insIdLn(pRvs, pVs, pLine);
    } else {
      getOrm().update(pRvs, pVs, pLine);
    }
    if (itls != null) {
      List<LTL> itlsr = null;
      if (pInvLnTxMeth.getIsMutable() && !pLine.getIsNew()) {
        pVs.put(pInvLnTxMeth.getInvLnCl().getSimpleName() + "dpLv", 0);
        pVs.put(pInvLnTxMeth.getLtlCl().getSimpleName() + "dpLv", 1);
        String[] ndfitl = new String[] {"iid", "dbOr", "ver", "tax"};
        Arrays.sort(ndfitl);
        pVs.put(pInvLnTxMeth.getLtlCl().getSimpleName() + "ndFds", ndfitl);
        itlsr = getOrm().retLstCnd(pRvs, pVs, pInvLnTxMeth.getLtlCl(),
          "where OWNR=" + pLine.getIid());
        pVs.clear();
      }
      String[] taxTotOwnr = new String[] {"ownr", "tax", "tot", "ver"};
      Arrays.sort(taxTotOwnr);
      for (int j = 0; j < itls.size(); j++) {
        if (itlsr != null && j < itlsr.size()) {
          itlsr.get(j).setTax(itls.get(j).getTax());
          itlsr.get(j).setTot(itls.get(j).getTot());
          itlsr.get(j).setOwnr(pLine);
          pVs.put("ndFds", taxTotOwnr);
          getOrm().update(pRvs, pVs, itlsr.get(j)); pVs.clear();
        } else {
          itls.get(j).setOwnr(pLine);
          itls.get(j).setInvId(pLine.getOwnr().getIid());
          getOrm().insIdLn(pRvs, pVs, itls.get(j));
        }
      }
      if (itlsr != null) {
        for (int j = itls.size(); j < itlsr.size(); j++) {
          getOrm().del(pRvs, pVs, itlsr.get(j));
        }
      }
    }
  }


  /**
   * <p>Retrieve from database bundle of tax data.</p>
   * @param <T> invoice type
   * @param <L> invoice line type
   * @param <TL> invoice tax line type
   * @param pRvs request scoped vars
   * @param pLine affected line
   * @param pAs Accounting Settings
   * @param pTxRules taxable rules
   * @param pInvTxMeth tax method code/data for purchase/sales invoice
   * @return taxes data
   * @throws Exception - an exception.
   **/
  public final <T extends IInvb, L extends IInvLn<T, ?>,
    TL extends AInTxLn<T>> DataTx retrieveDataTx(
      final Map<String, Object> pRvs, final L pLine, final AcStg pAs,
        final TxDst pTxRules,
          final IInvTxMeth<T, TL> pInvTxMeth) throws Exception {
    DataTx dtTx = new DataTx();
    String query;
    if (!pTxRules.getStAg() && !(pTxRules.getStIb()
      && pLine.getOwnr().getInTx())) {
      //non-aggregate except invoice basis with included taxes:
      dtTx.setTxs(new ArrayList<TaxEx>());
      dtTx.setTxTotTaxb(new ArrayList<Double>());
      dtTx.setTxTotTaxbFc(new ArrayList<Double>());
      if (!pTxRules.getStIb()) {
        //item basis:
        query = pInvTxMeth.lazyGetQuTxItBas();
      } else {
        //invoice basis, taxes excluded:
        dtTx.setTxPerc(new ArrayList<Double>());
        query = pInvTxMeth.lazyGetQuTxInvBas();
        //totals by tax category for farther adjusting invoice:
        dtTx.setTxdLns(new ArrayList<TxDtLn>());
      }
    } else { //non-aggregate invoice basis with included taxes
      //and aggregate for others:
      dtTx.setTxdLns(new ArrayList<TxDtLn>());
      if (!pTxRules.getStIb()) { //item basis
        query = pInvTxMeth.lazyGetQuTxItBasAggr();
      } else { //invoice basis:
        query = pInvTxMeth.lazyGetQuTxInvBasAggr();
      }
    }
    boolean dbgSh = getLog().getDbgSh(this.getClass(), 11002);
    if (dbgSh) {
      getLog().debug(pRvs, UtInLnTxToBs.class,
    "Tax rules: aggregate/invoice basis/zip/RM = " + pTxRules.getStAg() + "/"
  + pTxRules.getStIb() + "/" + pTxRules.getZip() + "/" + pTxRules.getStRm());
      String txCat;
      if (pLine.getTxCt() != null) {
        txCat = pLine.getTxCt().getNme();
      } else {
        txCat = "-";
      }
      getLog().debug(pRvs, UtInLnTxToBs.class, "Item: name/tax category = "
        + pLine.getItm().getNme() + "/" + txCat);
      getLog().debug(pRvs, UtInLnTxToBs.class, "Tax query: " + query);
    }
    if (!pTxRules.getStAg() && !pTxRules.getStIb()) {
      if (pInvTxMeth.getTblNmsTot().length == 5) { //sales/purchase:
        query = query.replace(":TGDTXLN", pInvTxMeth.getTblNmsTot()[3]);
        query = query.replace(":TSRVTXLN", pInvTxMeth.getTblNmsTot()[4]);
      } else { //returns:
        query = query.replace(":TITTXLN", pInvTxMeth.getTblNmsTot()[2]);
      }
    } else {
      if (pInvTxMeth.getTblNmsTot().length == 5) { //sales/purchase:
        query = query.replace(":TGDLN", pInvTxMeth.getTblNmsTot()[0]);
        query = query.replace(":TSRVLN", pInvTxMeth.getTblNmsTot()[1]);
      } else { //returns:
        query = query.replace(":TGDLN", pInvTxMeth.getTblNmsTot()[0]);
      }
    }
    query = query.replace(":INVID", pLine.getOwnr().getIid().toString());
    IRecSet<RS> rs = null;
    try {
      rs = getRdb().retRs(query);
      if (rs.first()) {
        do {
          Long txId = rs.getLong("TAXID");
          String txNm = rs.getStr("TAXNME");
          TaxEx tax = new TaxEx();
          tax.setIid(txId);
          tax.setNme(txNm);
          if (!pTxRules.getStAg() && !(pTxRules.getStIb()
            && pLine.getOwnr().getInTx())) {
            //non-aggregate except invoice basis with included taxes:
            if (!pTxRules.getStIb()) {
              //item basis, taxes excluded/included:
              dtTx.getTxs().add(tax);
              dtTx.getTxTotTaxb().add(rs.getDouble("TOTX"));
              dtTx.getTxTotTaxbFc().add(rs.getDouble("TXFC"));
            } else {
              //invoice basis, taxes excluded:
              boolean isNew = true;
              int li = dtTx.getTxTotTaxb().size() - 1;
              if (li >= 0 && dtTx.getTxs().get(li).getIid()
                .equals(tax.getIid())) {
                isNew = false;
              }
              Double su = rs.getDouble("SUBT");
              Double suf = rs.getDouble("SUFC");
              Double pr = rs.getDouble("RATE");
              if (isNew) {
                dtTx.getTxs().add(tax);
                dtTx.getTxTotTaxb().add(su);
                dtTx.getTxTotTaxbFc().add(suf);
                dtTx.getTxPerc().add(pr);
              } else {
                Double sut = su + dtTx.getTxTotTaxb().get(li);
                dtTx.getTxTotTaxb().set(li, sut);
                Double sutf = suf + dtTx.getTxTotTaxbFc().get(li);
                dtTx.getTxTotTaxbFc().set(li, sutf);
              }
              Long tcId = rs.getLong("TXCTID");
              addInvBsTxExTxc(dtTx.getTxdLns(), tcId, su, suf, pr, pAs,
                pTxRules);
            }
          } else { //non-aggregate invoice basis with included taxes
            //and aggregate for others:
            Double percent = rs.getDouble("RATE");
            Long tcId = rs.getLong("TXCTID");
            if (!pTxRules.getStIb()) { //item basis:
              Long clId = rs.getLong("ILID");
              TxDtLn txdLn = makeTxdLine(dtTx.getTxdLns(),
                clId, tcId, tax, percent, pAs);
              txdLn.setToTx(BigDecimal.valueOf(rs.getDouble("TOTX"))
                .setScale(pAs.getPrDp(), RoundingMode.HALF_UP));
              txdLn.setTxFc(BigDecimal.valueOf(rs.getDouble("TXFC"))
                .setScale(pAs.getPrDp(), RoundingMode.HALF_UP));
            } else { //invoice basis:
              TxDtLn txdLn = makeTxdLine(dtTx.getTxdLns(),
                tcId, tcId, tax, percent, pAs);
              txdLn.setTot(BigDecimal.valueOf(rs.getDouble("TOT"))
                .setScale(pAs.getPrDp(), RoundingMode.HALF_UP));
              txdLn.setSubt(BigDecimal.valueOf(rs.getDouble("SUBT"))
                .setScale(pAs.getPrDp(), RoundingMode.HALF_UP));
              txdLn.setToFc(BigDecimal.valueOf(rs.getDouble("TOFC"))
                .setScale(pAs.getPrDp(), RoundingMode.HALF_UP));
              txdLn.setSuFc(BigDecimal.valueOf(rs.getDouble("SUFC"))
                .setScale(pAs.getPrDp(), RoundingMode.HALF_UP));
            }
          }
        } while (rs.next());
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    if (dbgSh) {
      getLog().debug(pRvs, UtInLnTxToBs.class,
        "Data tax: " + dtTx);
    }
    return dtTx;
  }

  /**
   * <p>Adds total tax by category for farther invoice adjusting.</p>
   * @param pTxdLns Tax Data lines
   * @param pCatId tax category ID
   * @param pSubt subtotal without taxes
   * @param pSubtFc subtotal FC without taxes
   * @param pPercent tax rate
   * @param pAs AS
   * @param pTxRules tax rules
   **/
  public final void addInvBsTxExTxc(final List<TxDtLn> pTxdLns,
    final Long pCatId, final Double pSubt, final Double pSubtFc,
      final Double pPercent, final AcStg pAs, final TxDst pTxRules) {
    TxDtLn txdLn = null;
    for (TxDtLn tdl : pTxdLns) {
      if (tdl.getIid().equals(pCatId)) {
        txdLn = tdl;
      }
    }
    if (txdLn == null) {
      txdLn = new TxDtLn();
      txdLn.setIid(pCatId);
      TxCt tc = new TxCt();
      tc.setIid(pCatId);
      txdLn.setTxCt(tc);
      pTxdLns.add(txdLn);
    }
    BigDecimal bd100 = new BigDecimal("100.00");
    BigDecimal txv = BigDecimal.valueOf(pSubt).multiply(BigDecimal
      .valueOf(pPercent)).divide(bd100, pAs.getPrDp(), pTxRules.getStRm());
    txdLn.setToTx(txdLn.getToTx().add(txv));
    BigDecimal txvf = BigDecimal.valueOf(pSubtFc).multiply(BigDecimal
      .valueOf(pPercent)).divide(bd100, pAs.getPrDp(), pTxRules.getStRm());
    txdLn.setTxFc(txdLn.getTxFc().add(txvf));
  }

  /**
   * <p>Make invoice line that stores taxes data in lines set
   * for invoice basis or item basis aggregate rate.</p>
   * @param pTxdLns Tax Data lines
   * @param pTdlId line ID
   * @param pCatId tax category ID
   * @param pTax tax
   * @param pPercent tax rate
   * @param pAs AS
   * @return line
   **/
  public final TxDtLn makeTxdLine(final List<TxDtLn> pTxdLns,
    final Long pTdlId, final Long pCatId,  final TaxEx pTax,
      final Double pPercent, final AcStg pAs) {
    TxDtLn txdLn = null;
    for (TxDtLn tdl : pTxdLns) {
      if (tdl.getIid().equals(pTdlId)) {
        txdLn = tdl;
      }
    }
    if (txdLn == null) {
      txdLn = new TxDtLn();
      txdLn.setIid(pTdlId);
      TxCt tc = new TxCt();
      tc.setIid(pCatId);
      tc.setTxs(new ArrayList<TxCtLn>());
      txdLn.setTxCt(tc);
      pTxdLns.add(txdLn);
    }
    TxCtLn itcl = new TxCtLn();
    itcl.setTax(pTax);
    itcl.setRate(BigDecimal.valueOf(pPercent)
      .setScale(pAs.getTxDp(), RoundingMode.HALF_UP));
    txdLn.getTxCt().getTxs().add(itcl);
    txdLn.getTxCt().setAgRt(txdLn.getTxCt().getAgRt().add(itcl.getRate()));
    return txdLn;
  }

  /**
   * <p>Makes invoice line taxes item basis basis non-aggregate.</p>
   * @param <T> invoice type
   * @param <L> invoice line type
   * @param <LTL> invoice line's tax line type
   * @param pRvs request scoped vars
   * @param pVs Invoker scoped variables, not null
   * @param pLine invoice line
   * @param pAs Accounting Settings
   * @param pTxRules taxable rules
   * @param pInvTxMeth tax method code/data for purchase/sales invoice
   * @param pInvLnTxMeth tax method code/data for purchase/sales invoice line
   * @return created invoice line taxes for farther proceed
   * @throws Exception - an exception.
   **/
  public final <T extends IInvb, L extends IInvLn<T, ?>,
    LTL extends ALnTxLn<T, L>> List<LTL> mkLnTxItBasNonAggr(
      final Map<String, Object> pRvs, final Map<String, Object> pVs,
        final L pLine, final AcStg pAs, final TxDst pTxRules,
          final IInvTxMeth<T, ?> pInvTxMeth,
            final IInvLnTxMeth<T, L, LTL> pInvLnTxMeth) throws Exception {
    List<LTL> itls = new ArrayList<LTL>();
    BigDecimal totTxs = BigDecimal.ZERO;
    BigDecimal totTxsFc = BigDecimal.ZERO;
    BigDecimal bd100 = new BigDecimal("100.00");
    pVs.put("TxCtdpLv", 0);
    List<TxCtLn> itcls = getOrm().retLstCnd(pRvs, pVs, TxCtLn.class,
      "where OWNR=" + pLine.getTxCt().getIid() + " order by RATE");
    pVs.clear();
    BigDecimal taxTot = null;
    BigDecimal taxRest = null;
    BigDecimal taxTotFc = null;
    BigDecimal taxRestFc = null;
    if (pLine.getOwnr().getInTx()) {
      taxTot = pLine.getTot().subtract(pLine.getTot().divide(BigDecimal.ONE
        .add(pLine.getTxCt().getAgRt().divide(bd100, 15, RoundingMode.HALF_UP)),
          pAs.getPrDp(), pTxRules.getStRm()));
      taxRest = taxTot;
      if (pLine.getOwnr().getCuFr() != null) {
        taxTotFc = pLine.getToFc().subtract(pLine.getToFc()
          .divide(BigDecimal.ONE.add(pLine.getTxCt().getAgRt().divide(bd100,
            15, RoundingMode.HALF_UP)), pAs.getPrDp(), pTxRules.getStRm()));
        taxRestFc = taxTotFc;
      }
    }
    StringBuffer sb = new StringBuffer();
    int i = 0;
    UsPrf upf = (UsPrf) pRvs.get("upf");
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    for (TxCtLn itcl : itcls) {
     if (ETxTy.TSALES.equals(itcl.getTax().getTyp())) {
        if (i++ > 0) {
          sb.append(", ");
        }
        LTL itl = pInvLnTxMeth.getFctLineTxLn().create(pRvs);
        itl.setIsNew(true);
        itl.setTax(itcl.getTax());
        itls.add(itl);
        BigDecimal addTx;
        BigDecimal addTxFc = null;
        if (pLine.getOwnr().getInTx()) {
          //tax set by user:
          if (i < itcls.size()) {
            addTx = taxTot.multiply(itcl.getRate()).divide(pLine.getTxCt()
              .getAgRt(), pAs.getPrDp(), pTxRules.getStRm());
            taxRest = taxRest.subtract(addTx);
          } else {
            addTx = taxRest;
          }
          if (pLine.getOwnr().getCuFr() != null) {
            if (i < itcls.size()) {
              addTxFc = taxTotFc.multiply(itcl.getRate()).divide(pLine.getTxCt()
                .getAgRt(), pAs.getPrDp(), pTxRules.getStRm());
              taxRestFc = taxRestFc.subtract(addTxFc);
            } else {
              addTxFc = taxRestFc;
            }
          }
        } else {
          //tax always calculated:
          addTx = pLine.getSubt().multiply(itcl.getRate())
            .divide(bd100, pAs.getPrDp(), pTxRules.getStRm());
          if (pLine.getOwnr().getCuFr() != null) {
            addTxFc = pLine.getSuFc().multiply(itcl.getRate())
              .divide(bd100, pAs.getPrDp(), pTxRules.getStRm());
          }
        }
        totTxs = totTxs.add(addTx);
        itl.setTot(addTx);
        if (pLine.getOwnr().getCuFr() != null) {
          totTxsFc = totTxsFc.add(addTxFc);
          itl.setToFc(addTxFc);
        }
        sb.append(itl.getTax().getNme() + " " + prn(pAs, cpf, upf, addTx));
      }
    }
    pLine.setTdsc(sb.toString());
    mkLnFinal(pLine, totTxs, totTxsFc, pLine.getOwnr().getInTx());
    return itls;
  }

  /**
   * <p>Makes invoice line final results.</p>
   * @param <T> invoice type
   * @param <L> invoice line type
   * @param pLine invoice line
   * @param pTotTxs total line taxes
   * @param pTotTxsFc total line taxes FC
   * @param pIsTxByUser if tax set by user
   **/
  public final <T extends IInvb, L extends IInvLn<T,  ?>> void mkLnFinal(
    final L pLine, final BigDecimal pTotTxs, final BigDecimal pTotTxsFc,
      final Boolean pIsTxByUser) {
    if (pIsTxByUser) {
      if (pLine.getOwnr().getCuFr() == null) {
        if (pLine.getToTx().compareTo(pTotTxs) != 0) {
          if (pLine.getDscr() == null) {
            pLine.setDscr(pLine.getToTx().toString() + "!="
              + pTotTxs + "!");
          } else {
            pLine.setDscr(pLine.getDscr() + " " + pLine
              .getToTx().toString() + "!=" + pTotTxs + "!");
          }
        }
      } else {
        pLine.setToTx(pTotTxs);
        if (pLine.getTxFc().compareTo(pTotTxsFc) != 0) {
          if (pLine.getDscr() == null) {
            pLine.setDscr(pLine.getTxFc().toString()
              + "!=" + pTotTxsFc + "!");
          } else {
            pLine.setDscr(pLine.getDscr() + " " + pLine
              .getTxFc().toString() + "!=" + pTotTxsFc + "!");
          }
        }
      }
    } else {
      pLine.setToTx(pTotTxs);
      pLine.setTxFc(pTotTxsFc);
    }
  }

  /**
   * <p>Finds (if need) line with same tax or creates one.</p>
   * @param <T> invoice type
   * @param <TL> invoice tax line type
   * @param pRvs additional param
   * @param pInv invoice
   * @param pInvTxLns invoice tax lines
   * @param pTax tax
   * @param pNeedFind if need to find enabled
   * @param pFctInvTxLn invoice tax line factory
   * @return line
   * @throws Exception if no need to find but line is found
   **/
  public final <T extends IInvb, TL extends AInTxLn<T>>
    TL findCreateTaxLine(final Map<String, Object> pRvs, final T pInv,
      final List<TL> pInvTxLns, final Tax pTax, final boolean pNeedFind,
        final IFctRq<TL> pFctInvTxLn) throws Exception {
    TL itl = null;
    //find same line to add amount:
    for (TL tl : pInvTxLns) {
      if (tl.getTax() != null
        && tl.getTax().getIid().equals(pTax.getIid())) {
        if (!pNeedFind) {
          throw new Exception("Algorithm error!!!");
        }
        itl = tl;
        break;
      }
    }
    if (itl == null) {
      //find and enable disabled line:
      for (TL tl : pInvTxLns) {
        if (tl.getTax() == null) {
          itl = tl;
          itl.setTax(pTax);
          break;
        }
      }
    }
    if (itl == null) {
      itl = pFctInvTxLn.create(pRvs);
      itl.setOwnr(pInv);
      itl.setIsNew(true);
      itl.setTax(pTax);
      pInvTxLns.add(itl);
    }
    return itl;
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
      pCpf.getDcGrSpv(), pAs.getPrDp(), pUpf.getDgInGr());
  }

  //Simple getters and setters:
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
