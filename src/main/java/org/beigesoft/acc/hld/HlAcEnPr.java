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

package org.beigesoft.acc.hld;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IOwned;
import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.prc.PrcEnoCr;
import org.beigesoft.prc.PrcEntRt;
import org.beigesoft.prc.PrcEntSv;
import org.beigesoft.prc.PrcEntCr;
import org.beigesoft.prc.PrcEnoDl;
import org.beigesoft.acc.mdlb.ISacnt;
import org.beigesoft.acc.mdlb.IEntrSrc;
import org.beigesoft.acc.mdlb.IDoc;
import org.beigesoft.acc.mdlb.IDcDri;
import org.beigesoft.acc.mdlb.APrep;
import org.beigesoft.acc.mdlb.ITyp;
import org.beigesoft.acc.mdlb.IRvId;
import org.beigesoft.acc.mdlb.IRetLn;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.mdlb.IInvLn;
import org.beigesoft.acc.mdlb.AInvLn;
import org.beigesoft.acc.mdlb.ADcTxLn;
import org.beigesoft.acc.mdlb.AEnrSrc;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Entr;
import org.beigesoft.acc.mdlp.InEntr;
import org.beigesoft.acc.mdlp.Acnt;
import org.beigesoft.acc.mdlp.Sacnt;
import org.beigesoft.acc.mdlp.TxCtLn;
import org.beigesoft.acc.mdlp.Blnc;
import org.beigesoft.acc.mdlp.BlnCh;
import org.beigesoft.acc.mdlp.SalRet;
import org.beigesoft.acc.mdlp.SaRtLn;
import org.beigesoft.acc.mdlp.PurRet;
import org.beigesoft.acc.mdlp.PuRtLn;
import org.beigesoft.acc.mdlp.SalInv;
import org.beigesoft.acc.mdlp.SaInGdLn;
import org.beigesoft.acc.mdlp.SaInSrLn;
import org.beigesoft.acc.mdlp.PaymFr;
import org.beigesoft.acc.mdlp.PaymTo;
import org.beigesoft.acc.mdlp.PurInv;
import org.beigesoft.acc.mdlp.PuInGdLn;
import org.beigesoft.acc.mdlp.PuInSrLn;
import org.beigesoft.acc.mdlp.MovItm;
import org.beigesoft.acc.mdlp.MoItLn;
import org.beigesoft.acc.mdlp.ItAdLn;
import org.beigesoft.acc.mdlp.ItmAdd;
import org.beigesoft.acc.mdlp.ItUbLn;
import org.beigesoft.acc.mdlp.ItmUlb;
import org.beigesoft.acc.fct.FcEnPrAc;
import org.beigesoft.acc.prc.SacntCr;
import org.beigesoft.acc.prc.SacntSv;
import org.beigesoft.acc.prc.AcntSv;
import org.beigesoft.acc.prc.EntrCr;
import org.beigesoft.acc.prc.EntrChd;
import org.beigesoft.acc.prc.EntrRt;
import org.beigesoft.acc.prc.EntrCpr;
import org.beigesoft.acc.prc.EntrSrcCr;
import org.beigesoft.acc.prc.IsacntSv;
import org.beigesoft.acc.prc.IsacntDl;
import org.beigesoft.acc.prc.AcntDl;
import org.beigesoft.acc.prc.EntrSv;
import org.beigesoft.acc.prc.InEntrSv;
import org.beigesoft.acc.prc.InEntrDl;
import org.beigesoft.acc.prc.InEntrRt;
import org.beigesoft.acc.prc.AcStgSv;
import org.beigesoft.acc.prc.AcStgRt;
import org.beigesoft.acc.prc.TxCtLnSv;
import org.beigesoft.acc.prc.TxCtLnDl;
import org.beigesoft.acc.prc.PrepSv;
import org.beigesoft.acc.prc.PrepCpr;
import org.beigesoft.acc.prc.DocPr;
import org.beigesoft.acc.prc.DcDriPr;
import org.beigesoft.acc.prc.DocWhPr;
import org.beigesoft.acc.prc.DocCpr;
import org.beigesoft.acc.prc.EnrSrcChu;
import org.beigesoft.acc.prc.RetLnRv;
import org.beigesoft.acc.prc.InvLnCpr;
import org.beigesoft.acc.prc.MovItmPr;
import org.beigesoft.acc.prc.MoItLnSv;
import org.beigesoft.acc.prc.MoItLnRv;
import org.beigesoft.acc.prc.ItmAddSv;
import org.beigesoft.acc.prc.ItAdLnSv;
import org.beigesoft.acc.prc.ItAdLnRv;
import org.beigesoft.acc.prc.ItmUlbSv;
import org.beigesoft.acc.prc.ItUbLnSv;
import org.beigesoft.acc.prc.ItUbLnRv;

/**
 * <p>Additional holder of names of ACC entities processors.</p>
 *
 * @author Yury Demidenko
 */
public class HlAcEnPr implements IHlNmClSt {

  /**
   * <p>Get processor name for given class and action name.</p>
   * @param <T> entity type
   * @param pCls a Class
   * @param pAct action name
   * @return processor name
   * @throws Exception an Exception
   **/
  @Override
  public final <T extends IHasId<?>> String get(final Class<T> pCls,
    final String pAct) throws Exception {
    if (Blnc.class == pCls || BlnCh.class == pCls) {
      return NULL;
    }
    if (Acnt.class == pCls || Sacnt.class == pCls
          || AcStg.class == pCls || TxCtLn.class == pCls || AEnrSrc.class
            .isAssignableFrom(pCls) || ITyp.class.isAssignableFrom(pCls)
              || IRvId.class.isAssignableFrom(pCls)) {
      if ("entCr".equals(pAct)) { //Create
        if (Entr.class == pCls) {
          return EntrCr.class.getSimpleName();
        } else if (Sacnt.class == pCls) {
          return SacntCr.class.getSimpleName();
        } else if (IEntrSrc.class.isAssignableFrom(pCls)) {
          return EntrSrcCr.class.getSimpleName();
        } else if (Acnt.class == pCls || MovItm.class == pCls
          || ISacnt.class.isAssignableFrom(pCls)) {
          return PrcEntCr.class.getSimpleName();
        } else if (IOwned.class.isAssignableFrom(pCls)) {
          return PrcEnoCr.class.getSimpleName();
        }
      } else if ("entCp".equals(pAct)) { //Copy
        if (Entr.class == pCls) {
          return EntrCpr.class.getSimpleName();
        } else if (APrep.class.isAssignableFrom(pCls)) {
          return PrepCpr.class.getSimpleName();
        } else if (IDoc.class.isAssignableFrom(pCls)) {
          return DocCpr.class.getSimpleName();
        }
      } else if ("entRv".equals(pAct)) { //Create copy for reversing
        if (Entr.class == pCls) {
          return EntrCpr.class.getSimpleName();
        } else if (ItAdLn.class == pCls) {
          return ItAdLnRv.class.getSimpleName();
        } else if (ItUbLn.class == pCls) {
          return ItUbLnRv.class.getSimpleName();
        } else if (MoItLn.class == pCls) {
          return MoItLnRv.class.getSimpleName();
        } else if (APrep.class.isAssignableFrom(pCls)) {
          return PrepCpr.class.getSimpleName();
        } else if (IDoc.class.isAssignableFrom(pCls)) {
          return DocCpr.class.getSimpleName();
        } else if (AInvLn.class.isAssignableFrom(pCls)) {
          return InvLnCpr.class.getSimpleName();
        } else if (IRetLn.class.isAssignableFrom(pCls)) {
          return RetLnRv.class.getSimpleName();
        }
      } else if ("entEd".equals(pAct) || "entPr".equals(pAct)
        || "entCd".equals(pAct)) { //Retrieve for any action
        if (InEntr.class == pCls) {
          return InEntrRt.class.getSimpleName();
        } else if (Entr.class == pCls) {
          if ("entEd".equals(pAct) || "entPr".equals(pAct)) {
            return EntrRt.class.getSimpleName();
          }
          return NULL;
        } else if (AcStg.class == pCls) {
          if ("entEd".equals(pAct) || "entPr".equals(pAct)) {
            return AcStgRt.class.getSimpleName();
          }
          return NULL;
       } else if ("entPr".equals(pAct) && MovItm.class == pCls) {
         return MovItmPr.class.getSimpleName();
       } else if ("entPr".equals(pAct) && IDcDri.class.isAssignableFrom(pCls)) {
          return DcDriPr.class.getSimpleName();
        } else if ("entPr".equals(pAct) && IDoc.class.isAssignableFrom(pCls)) {
          if (AInv.class.isAssignableFrom(pCls)) {
            return DocWhPr.class.getSimpleName();
          }
          return DocPr.class.getSimpleName();
        } else if (AEnrSrc.class.isAssignableFrom(pCls)) {
          if ("entEd".equals(pAct) || "entPr".equals(pAct)) {
            return PrcEntRt.class.getSimpleName();
          }
          return NULL;
        }
        return PrcEntRt.class.getSimpleName();
      } else if ("entSv".equals(pAct)) { //Save
        if (Acnt.class == pCls) {
          return AcntSv.class.getSimpleName();
        } else if (SalRet.class == pCls) {
          return FcEnPrAc.SARETSV;
        } else if (SaRtLn.class == pCls) {
          return FcEnPrAc.SARTLNSV;
        } else if (PurRet.class == pCls) {
          return FcEnPrAc.PURETSV;
        } else if (PuRtLn.class == pCls) {
          return FcEnPrAc.PURTLNSV;
        } else if (PurInv.class == pCls) {
          return FcEnPrAc.PURINVSV;
        } else if (SalInv.class == pCls) {
          return FcEnPrAc.SALINVSV;
        } else if (SaInGdLn.class == pCls) {
          return FcEnPrAc.SALINVGDLNSV;
        } else if (SaInSrLn.class == pCls) {
          return FcEnPrAc.SALINVSRLNSV;
        } else if (PuInGdLn.class == pCls) {
          return FcEnPrAc.PURINVGDLNSV;
        } else if (PuInSrLn.class == pCls) {
          return FcEnPrAc.PURINVSRLNSV;
        } else if (AcStg.class == pCls) {
          return AcStgSv.class.getSimpleName();
        } else if (TxCtLn.class == pCls) {
          return TxCtLnSv.class.getSimpleName();
        } else if (Entr.class == pCls) {
          return EntrSv.class.getSimpleName();
        } else if (InEntr.class == pCls) {
          return InEntrSv.class.getSimpleName();
        } else if (Sacnt.class == pCls) {
          return SacntSv.class.getSimpleName();
        } else if (MovItm.class == pCls) {
          return PrcEntSv.class.getSimpleName();
        } else if (ItmAdd.class == pCls) {
          return ItmAddSv.class.getSimpleName();
        } else if (ItAdLn.class == pCls) {
          return ItAdLnSv.class.getSimpleName();
        } else if (ItmUlb.class == pCls) {
          return ItmUlbSv.class.getSimpleName();
        } else if (ItUbLn.class == pCls) {
          return ItUbLnSv.class.getSimpleName();
        } else if (MoItLn.class == pCls) {
          return MoItLnSv.class.getSimpleName();
        } else if (PaymFr.class.isAssignableFrom(pCls)) {
          return FcEnPrAc.PAYFRSV;
        } else if (PaymTo.class.isAssignableFrom(pCls)) {
          return FcEnPrAc.PAYTOSV;
        } else if (AEnrSrc.class.isAssignableFrom(pCls)) {
          return EnrSrcChu.class.getSimpleName();
        } else if (ISacnt.class.isAssignableFrom(pCls)) {
          return IsacntSv.class.getSimpleName();
        } else if (APrep.class.isAssignableFrom(pCls)) {
          return PrepSv.class.getSimpleName();
        }
      } else if ("entChd".equals(pAct) && Entr.class == pCls) {
        //Entr save only changed description
        return EntrChd.class.getSimpleName();
      } else if ("entDl".equals(pAct)) { //Delete
        if (Acnt.class == pCls) {
          return AcntDl.class.getSimpleName();
        } else if (TxCtLn.class == pCls) {
          return TxCtLnDl.class.getSimpleName();
        } else if (InEntr.class == pCls) {
          return InEntrDl.class.getSimpleName();
        } else if (ISacnt.class.isAssignableFrom(pCls)) {
          return IsacntDl.class.getSimpleName();
        } else if (IInvLn.class.isAssignableFrom(pCls)) {
          return NULL;
        } else if (ADcTxLn.class.isAssignableFrom(pCls)) {
          return NULL;
        } else if (IOwned.class.isAssignableFrom(pCls)) {
          return PrcEnoDl.class.getSimpleName();
        }
      }
      //Forbidden:
      return NULL;
    }
    //TxCt, SrTxDl, Uom, non-acc entities:
    return null;
  }
}
