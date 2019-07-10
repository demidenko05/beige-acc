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
import java.util.List;

import org.beigesoft.fct.IFctRq;
import org.beigesoft.mdl.IIdLn;
import org.beigesoft.acc.mdlb.AInTxLn;
import org.beigesoft.acc.mdlb.ALnTxLn;
import org.beigesoft.acc.mdlb.ATxDsLn;
import org.beigesoft.acc.mdlb.IInvb;
import org.beigesoft.acc.mdlb.IInvLn;
import org.beigesoft.acc.mdlb.TxDtLn;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.TxDst;

/**
 * <p>Utility for purchase/sales invoice line.
 * It makes taxes and totals for line and invoice.
 * It's final assembly dedicated to concrete invoice line type.
 * Code in base utility is shared (there is only instance in memory).</p>
 *
 * @param <T> invoice type
 * @param <L> invoice line type
 * @param <TL> invoice tax line type
 * @param <LTL> invoice line's tax line type
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class UtInLnTxTo<RS, T extends IInvb, L extends IInvLn<T, ?>,
  TL extends AInTxLn<T>, LTL extends ALnTxLn<T, L>>
    implements IInvLnTxMeth<T, L, LTL> {

  /**
   * <p>Shared code-bunch.</p>
   **/
  private UtInLnTxToBs<RS> utlInv;

  /**
   * <p>Invoice level shared options.</p>
   **/
  private InvTxMeth<T, TL> invTxMeth;

  /**
   * <p>Invoice line's tax line factory.</p>
   **/
  private IFctRq<LTL> fctLineTxLn;

  /**
   * <p>Line's tax line class.</p>
   **/
  private Class<LTL> ltlCl;

  /**
   * <p>Item's destination tax line class.</p>
   **/
  private Class<? extends ATxDsLn<?>> dstTxItLnCl;

  /**
   * <p>Item's class.</p>
   **/
  private Class<L> invLnCl;

  /**
   * <p>Item's class.</p>
   **/
  private Class<? extends IIdLn> itmCl;

  /**
   * <p>If line editable, e.g. any good doesn't.</p>
   **/
  private Boolean isMutable;

  /**
   * <p>If need make line tax category (purchase return not).</p>
   **/
  private Boolean needMkTxCat;

  /**
   * <p>For extract sales taxes main setting from AS -sales or purchase.</p>
   **/
  private Boolean isPurch;

  /**
   * <p>Getter for need make line tax category (purchase return not).</p>
   * @return Boolean
   **/
  @Override
  public final Boolean getNeedMkTxCat() {
    return needMkTxCat;
  }

  /**
   * <p>Getter for itmCl.</p>
   * @return Class<?>
   **/
  @Override
  public final Class<? extends IIdLn> getItmCl() {
    return this.itmCl;
  }

  /**
   * <p>Getter for invLnCl.</p>
   * @return Class<?>
   **/
  @Override
  public final Class<L> getInvLnCl() {
    return this.invLnCl;
  }

  /**
   * <p>Getter for dstTxItLnCl.</p>
   * @return Class<?>
   **/
  @Override
  public final Class<? extends ATxDsLn<?>> getDstTxItLnCl() {
    return this.dstTxItLnCl;
  }

  /**
   * <p>Getter for isMutable, if line editable, e.g. any good doesn't.</p>
   * @return Boolean
   **/
  @Override
  public final Boolean getIsMutable() {
    return this.isMutable;
  }

  /**
   * <p>Getter for ltlCl.</p>
   * @return Class<LTL>
   **/
  @Override
  public final Class<LTL> getLtlCl() {
    return this.ltlCl;
  }

  /**
   * <p>Getter for fctLineTxLn.</p>
   * @return IFctRq<LTL>
   **/
  @Override
  public final IFctRq<LTL> getFctLineTxLn() {
    return this.fctLineTxLn;
  }

  /**
   * <p>Reveal shared tax rules for invoice..</p>
   * @param pInv invoice
   * @param pAs Accounting Settings
   * @return tax rules, NULL if not taxable
   * @throws Exception - an exception.
   **/
  public final TxDst revealTaxRules(final IInvb pInv,
    final AcStg pAs) throws Exception {
    Boolean extSt;
    if (this.isPurch) {
      extSt = pAs.getStExp();
    } else {
      extSt = pAs.getStExs();
    }
    return this.utlInv.revealTaxRules(pInv, pAs, extSt);
  }

  /**
   * <p>Makes invoice line's taxes, totals.</p>
   * @param pRvs request scoped vars
   * @param pVs Invoker scoped variables, not null
   * @param pLine invoice line
   * @param pAs Accounting Settings
   * @param pTxRules NULL if not taxable
   * @throws Exception - an exception.
   **/
  public final void mkLnTxTo(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final L pLine, final AcStg pAs,
      final TxDst pTxRules) throws Exception {
    this.utlInv.mkLnTxTo(pRvs, pVs, pLine, pAs, pTxRules, this.invTxMeth, this);
  }

  /**
   * <p>Makes invoice totals include taxes lines
   * cause line inserted/changed/deleted.</p>
   * @param pRvs request scoped vars
   * @param pVs Invoker scoped variables, not null
   * @param pLine affected line
   * @param pAs Accounting Settings
   * @param pTxRules NULL if not taxable
   * @throws Exception - an exception.
   **/
  public final void makeTotals(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final L pLine, final AcStg pAs,
      final TxDst pTxRules) throws Exception {
    this.utlInv.makeTotals(pRvs, pVs, pLine, pAs, pTxRules, this.invTxMeth);
  }

  /**
   * <p>adjust invoice lines and Update its totals after tax line has
   * been changed (Invoice basis).</p>
   * @param pRvs additional param
   * @param pVs Invoker scoped variables, not null
   * @param pInv Invoice
   * @param pAs accounting settings
   * @param pTxRules not NULL
   * @throws Exception - an exception
   **/
  public final void adjInvLnsUpdTots(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final T pInv, final AcStg pAs,
      final TxDst pTxRules) throws Exception {
    List<TxDtLn> txdLns = this.utlInv.retrTxdLnsAdjInv(pRvs, pInv,
      pAs, pTxRules, this.invTxMeth);
    this.utlInv.adjustInvoiceLns(pRvs, pVs, pInv, txdLns, pAs, this.invTxMeth);
    this.utlInv.updInvTots(pRvs, pVs, pInv, pAs, this.invTxMeth);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for utlInv.</p>
   * @return UtInLnTxTo<RS>
   **/
  public final UtInLnTxToBs<RS> getUtlInv() {
    return this.utlInv;
  }

  /**
   * <p>Setter for utlInv.</p>
   * @param pUtlInv reference
   **/
  public final void setUtlInv(final UtInLnTxToBs<RS> pUtlInv) {
    this.utlInv = pUtlInv;
  }

  /**
   * <p>Setter for fctLineTxLn.</p>
   * @param pFctLineTxLn reference
   **/
  public final void setFctLineTxLn(final IFctRq<LTL> pFctLineTxLn) {
    this.fctLineTxLn = pFctLineTxLn;
  }

  /**
   * <p>Getter for invTxMeth.</p>
   * @return InvTxMeth<T, TL>
   **/
  public final InvTxMeth<T, TL> getInvTxMeth() {
    return this.invTxMeth;
  }

  /**
   * <p>Setter for invTxMeth.</p>
   * @param pInvTxMeth reference
   **/
  public final void setInvTxMeth(final InvTxMeth<T, TL> pInvTxMeth) {
    this.invTxMeth = pInvTxMeth;
  }

  /**
   * <p>Setter for ltlCl.</p>
   * @param pLtlCl reference
   **/
  public final void setLtlCl(final Class<LTL> pLtlCl) {
    this.ltlCl = pLtlCl;
  }

  /**
   * <p>Setter for itmCl.</p>
   * @param pItmCl reference
   **/
  public final void setItmCl(final Class<? extends IIdLn> pItmCl) {
    this.itmCl = pItmCl;
  }

  /**
   * <p>Setter for invLnCl.</p>
   * @param pInvLnCl reference
   **/
  public final void setInvLnCl(final Class<L> pInvLnCl) {
    this.invLnCl = pInvLnCl;
  }

  /**
   * <p>Setter for dstTxItLnCl.</p>
   * @param pDstTxItLnCl reference
   **/
  public final void setDstTxItLnCl(
    final Class<? extends ATxDsLn<?>> pDstTxItLnCl) {
    this.dstTxItLnCl = pDstTxItLnCl;
  }

  /**
   * <p>Setter for isMutable.</p>
   * @param pIsMutable reference
   **/
  public final void setIsMutable(final Boolean pIsMutable) {
    this.isMutable = pIsMutable;
  }

  /**
   * <p>Setter for needMkTxCat.</p>
   * @param pNeedMkTxCat reference
   **/
  public final void setNeedMkTxCat(final Boolean pNeedMkTxCat) {
    this.needMkTxCat = pNeedMkTxCat;
  }

  /**
   * <p>Getter for isPurch.</p>
   * @return Boolean
   **/
  public final Boolean getIsPurch() {
    return this.isPurch;
  }

  /**
   * <p>Setter for isPurch.</p>
   * @param pIsPurch reference
   **/
  public final void setIsPurch(final Boolean pIsPurch) {
    this.isPurch = pIsPurch;
  }
}
