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

package org.beigesoft.eis;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.math.BigDecimal;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IHasNm;
import org.beigesoft.mdl.IOwned;
import org.beigesoft.mdlp.AI18nNm;
import org.beigesoft.fct.IFctAsm;
import org.beigesoft.fct.IIniBdFct;
import org.beigesoft.fct.IniBdFct;
import org.beigesoft.hld.HldFldStg;
import org.beigesoft.hld.HldClsStg;
import org.beigesoft.hld.HldEnts;
import org.beigesoft.hld.EntShr;
import org.beigesoft.hld.ICtx;
import org.beigesoft.acc.mdlb.IDoci;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.mdlb.IInvb;
import org.beigesoft.acc.mdlb.ADcTxLn;
import org.beigesoft.acc.mdlp.Entr;
import org.beigesoft.acc.mdlp.I18Acc;
import org.beigesoft.acc.mdlp.I18Curr;
import org.beigesoft.acc.mdlp.I18Itm;
import org.beigesoft.acc.mdlp.I18Buyr;
import org.beigesoft.acc.mdlp.I18Srv;
import org.beigesoft.acc.mdlp.I18Uom;
import org.beigesoft.acc.mdlp.InEntr;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Acnt;
import org.beigesoft.acc.mdlp.Sacnt;
import org.beigesoft.acc.mdlp.EnrSrc;
import org.beigesoft.acc.mdlp.DriEnrSr;
import org.beigesoft.acc.mdlp.Curr;
import org.beigesoft.acc.mdlp.SrvCt;
import org.beigesoft.acc.mdlp.ItmCt;
import org.beigesoft.acc.mdlp.DcrCt;
import org.beigesoft.acc.mdlp.Tax;
import org.beigesoft.acc.mdlp.TxCt;
import org.beigesoft.acc.mdlp.Uom;
import org.beigesoft.acc.mdlp.Wrh;
import org.beigesoft.acc.mdlp.WrhPl;
import org.beigesoft.acc.mdlp.TxDst;
import org.beigesoft.acc.mdlp.Expn;
import org.beigesoft.acc.mdlp.Bnka;
import org.beigesoft.acc.mdlp.PuInGdLn;
import org.beigesoft.acc.mdlp.SaInGdLn;
import org.beigesoft.acc.mdlp.SaRtLn;
import org.beigesoft.acc.mdlp.PuRtLn;
import org.beigesoft.acc.mdlp.SalInv;
import org.beigesoft.acc.mdlp.MoItLn;
import org.beigesoft.acc.mdlp.MovItm;
import org.beigesoft.acc.mdlp.ItmAdd;
import org.beigesoft.acc.mdlp.ItAdLn;
import org.beigesoft.acc.mdlp.MnfPrc;
import org.beigesoft.acc.mdlp.MnpMcs;
import org.beigesoft.acc.mdlp.Itm;
import org.beigesoft.acc.mdlp.ItmUlb;
import org.beigesoft.acc.mdlp.ItUbLn;
import org.beigesoft.acc.mdlp.EmpWg;
import org.beigesoft.acc.mdlp.WagTy;
import org.beigesoft.acc.mdlp.EmpCt;
import org.beigesoft.acc.mdlp.WgTxl;
import org.beigesoft.acc.mdlp.Wage;
import org.beigesoft.acc.mdlp.BnkStm;
import org.beigesoft.acc.mdlp.BnStLn;
import org.beigesoft.acc.rpl.RplAcc;
import org.beigesoft.acc.rpl.RpExDbl;
import org.beigesoft.acc.rpl.RpExCrl;

/**
 * <p>Business-logic dependent sub-initializer main
 * factory during startup.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class IniEisFct<RS> implements IIniBdFct<RS> {

  /**
   * <p>Base initializer.</p>
   **/
  private final IniBdFct<RS> iniBdFct = new IniBdFct<RS>();
  /**
   * <p>Initializes factory.</p>
   * @param pRvs request scoped vars
   * @param pFct factory
   * @param pCtxAttr Context attributes
   * @throws Exception - an exception
   **/
  @Override
  public final void iniBd(final Map<String, Object> pRvs,
    final IFctAsm<RS> pFct, final ICtx pCtx) throws Exception {
    this.iniBdFct.lazAdmEnts().getEnts().add(RplAcc.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(RpExDbl.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(RpExCrl.class);
    this.iniBdFct.iniBd(pRvs, pFct, pCtx);
    makeUvdCls(pRvs, pFct);
    makeUvdFds(pRvs, pFct);
    HldEnts acEnts = new HldEnts();
    acEnts.setIid(HldEnts.ID_BASE);
    acEnts.setShrEnts(new HashSet<EntShr>());
    Set<Integer> rdrs = new HashSet<Integer>();
    rdrs.add(HldEnts.ID_ADMIN);
    acEnts.getShrEnts().add(new EntShr(Acnt.class, rdrs));
    pFct.getFctBlc().getFctDt().getHldsEnts().add(acEnts);
  }

  /**
   * <p>Makes UVD class settings.</p>
   * @param pRvs request scoped vars
   * @param pFct factory app
   * @throws Exception - an exception
   **/
  public final void makeUvdCls(final Map<String, Object> pRvs,
    final IFctAsm<RS> pFct) throws Exception {
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18Acc.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18Curr.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18Srv.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18Uom.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18Itm.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18Buyr.class);
    String stgNm = "flOr"; //list filter order
    HldClsStg hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getNulClss().add(AcStg.class);
    hlClSt.getNulClss().add(Acnt.class);
    hlClSt.getNulClss().add(Sacnt.class);
    hlClSt.getNulClss().add(EnrSrc.class);
    hlClSt.getNulClss().add(DriEnrSr.class);
    hlClSt.getNulClss().add(Curr.class);
    hlClSt.getNulClss().add(SrvCt.class);
    hlClSt.getNulClss().add(ItmCt.class);
    hlClSt.getNulClss().add(DcrCt.class);
    hlClSt.getNulClss().add(Tax.class);
    hlClSt.getNulClss().add(TxCt.class);
    hlClSt.getNulClss().add(Uom.class);
    hlClSt.getNulClss().add(Wrh.class);
    hlClSt.getNulClss().add(WrhPl.class);
    hlClSt.getNulClss().add(TxDst.class);
    hlClSt.getNulClss().add(I18Acc.class);
    hlClSt.getNulClss().add(I18Curr.class);
    hlClSt.getNulClss().add(Expn.class);
    hlClSt.getNulClss().add(Bnka.class);
    hlClSt.getNulClss().add(EmpWg.class);
    hlClSt.getNulClss().add(WagTy.class);
    hlClSt.getNulClss().add(EmpCt.class);
    stgNm = "ordDf"; //list order by field default
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getStgSclss().remove(IHasId.class);
    hlClSt.getStgSclss().remove(IHasNm.class);
    hlClSt.getStgClss().put(Acnt.class, "nmbr");
    hlClSt.getStgClss().put(I18Acc.class, "lng");
    hlClSt.getStgSclss().put(AI18nNm.class, "lng");
    hlClSt.getStgSclss().put(IHasNm.class, "nme");
    hlClSt.getStgSclss().put(IHasId.class, "iid");
    stgNm = "owl"; //owned list
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.setStgClss(new HashMap<Class<? extends IHasId<?>>, String>());
    hlClSt.getStgClss().put(Sacnt.class, "owla");
    hlClSt.getStgClss().put(WgTxl.class, "owl");
    hlClSt.setStgSclss(new LinkedHashMap<Class<?>, String>());
    hlClSt.getStgSclss().put(ADcTxLn.class, "intxs");
    stgNm = "liFo"; //list footer
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.setStgClss(new HashMap<Class<? extends IHasId<?>>, String>());
    hlClSt.getStgClss().put(BnkStm.class, "lffl");
    hlClSt.getStgClss().put(Acnt.class, "lfac");
    hlClSt.getStgClss().put(Entr.class, "lfna");
    hlClSt.getStgClss().put(InEntr.class, "lfia");
    hlClSt.getStgClss().put(WgTxl.class, "olf");
    hlClSt.getStgClss().put(PuInGdLn.class, "pglf");
    hlClSt.getStgSclss().remove(IOwned.class);
    hlClSt.getStgSclss().put(ADcTxLn.class, null);
    hlClSt.getStgSclss().put(IOwned.class, "olf");
    hlClSt.setNulClss(new HashSet<Class<? extends IHasId<?>>>());
    hlClSt.getNulClss().add(AcStg.class);
    hlClSt.getNulClss().add(EnrSrc.class);
    hlClSt.getNulClss().add(DriEnrSr.class);
    hlClSt.getNulClss().add(EmpWg.class);
    stgNm = "liAc"; //list item actions
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getStgClss().put(Acnt.class, "acac");
    hlClSt.getStgClss().put(BnStLn.class, "absl");
    hlClSt.getStgClss().put(Entr.class, "acae");
    hlClSt.getStgClss().put(InEntr.class, "acia");
    hlClSt.getStgClss().put(AcStg.class, "ace");
    hlClSt.getStgClss().put(EnrSrc.class, "ace");
    hlClSt.getStgClss().put(DriEnrSr.class, "ace");
    hlClSt.getStgClss().put(PuInGdLn.class, "acrv");
    hlClSt.getStgClss().put(SaInGdLn.class, "acrv");
    hlClSt.getStgClss().put(Sacnt.class, "acd");
    hlClSt.getStgClss().put(SaRtLn.class, "acrv");
    hlClSt.getStgClss().put(PuRtLn.class, "acrv");
    hlClSt.getStgClss().put(MoItLn.class, "acrv");
    hlClSt.getStgClss().put(ItAdLn.class, "acrv");
    hlClSt.getStgClss().put(MnpMcs.class, "acrv");
    hlClSt.getStgClss().put(ItUbLn.class, "acrv");
    hlClSt.getStgClss().put(SalInv.class, "asiv");
    hlClSt.getStgClss().put(EmpWg.class, null);
    hlClSt.getStgSclss().put(IDoci.class, "adoc");
    stgNm = "fmAc"; //form actions
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getStgClss().put(ItmAdd.class, "adcl");
    hlClSt.getStgClss().put(MnfPrc.class, "adcl");
    hlClSt.getStgClss().put(ItmUlb.class, "adcl");
    hlClSt.getStgClss().put(Wage.class, "awg");
    hlClSt.getStgSclss().put(IInvb.class, "adcl");
    hlClSt.getStgSclss().put(IDoci.class, "adoc");
    stgNm = "prn"; //print
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.setStgClss(new HashMap<Class<? extends IHasId<?>>, String>());
    hlClSt.getStgClss().put(InEntr.class, "pria");
    hlClSt.getStgClss().put(MovItm.class, "prmi");
    hlClSt.setStgSclss(new LinkedHashMap<Class<?>, String>());
    hlClSt.getStgSclss().put(IDoci.class, "prdc");
    stgNm = "de"; //delete
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.setStgClss(new HashMap<Class<? extends IHasId<?>>, String>());
    hlClSt.getStgClss().put(InEntr.class, "deia");
    stgNm = "pic"; //picker
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getStgSclss().put(AInv.class, "inv");
    hlClSt.getStgSclss().put(IDoci.class, "doc");
    hlClSt.getStgClss().put(Acnt.class, "acc");
    hlClSt.getStgClss().put(Sacnt.class, "sac");
    hlClSt.getStgClss().put(PuInGdLn.class, "pinl");
    hlClSt.getStgClss().put(SaInGdLn.class, "inl");
    hlClSt.getStgClss().put(Itm.class, "iuom");
  }

  /**
   * <p>Makes UVD fields settings.</p>
   * @param pRvs request scoped vars
   * @param pFct factory app
   * @throws Exception - an exception
   **/
  public final void makeUvdFds(final Map<String, Object> pRvs,
    final IFctAsm<RS> pFct) throws Exception {
    //fields settings:
    String stgNm = "inp"; //input
    HldFldStg hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.setEnumVal(null); //reconfiguration!
    hlFdSt.getCustSclss().add(Enum.class);
    //saTy, srTy
    hlFdSt.getStgClss().remove(Integer.class); //reconfiguration!
    hlFdSt.getCustClss().add(Integer.class);
    stgNm = "str"; //to string
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getCustClss().add(BigDecimal.class); //inv.payment total vs others
    hlFdSt.getStgClss().put(PuInGdLn.class, "pinl");
    hlFdSt.getStgClss().put(SaInGdLn.class, "inl");
    hlFdSt.getStgClss().put(MnfPrc.class, "dits");
    //Acnt.saTy
    hlFdSt.getCustClss().add(Integer.class);
    hlFdSt.getStgSclss().put(AInv.class, "inv");
    hlFdSt.getStgSclss().put(IDoci.class, "doc");
    stgNm = "ord"; //order
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getStgFdNm().put("knCs", "ord");
    hlFdSt.getStgFdNm().put("tot", "ord");
    stgNm = "flt"; //filter
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getStgFdNm().put("knCs", "pri");
    hlFdSt.getStgFdNm().put("tot", "pri");
    hlFdSt.getStgFdNm().put("toPa", "explToPa");
    hlFdSt.getStgFdNm().put("dbcr", "ent");
    hlFdSt.getStgFdNm().put("acc", "ent");
    hlFdSt.getStgFdNm().put("lng", "ent");
    stgNm = "flth"; //filter hidden
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.setStgFdNm(new HashMap<String, String>());
    hlFdSt.getStgFdNm().put("inv", "ent");
    hlFdSt.getStgFdNm().put("invl", "ent");
    hlFdSt.getStgFdNm().put("ownr", "ent");
    hlFdSt.getStgFdNm().put("invId", "int");
    hlFdSt.getStgFdNm().put("rvId", "int");
    hlFdSt.getStgFdNm().put("saTy", "int");
    hlFdSt.getStgFdNm().put("used", "bln");
    hlFdSt.getStgFdNm().put("inTx", "bln");
    hlFdSt.getStgFdNm().put("mdEnr", "bln");
    stgNm = "ceDe"; //to cell detail
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getStgFdNm().put("rsRcId", null);
    hlFdSt.getStgFdNm().put("saTy", null);
    hlFdSt.getStgFdNm().put("saId", null);
    hlFdSt.getStgFdNm().put("ownr", null);
    hlFdSt.getStgFdNm().put("rvId", "empt");
    hlFdSt.getStgFdNm().put("toLf", "empt");
    hlFdSt.getStgFdNm().put("txCt", "cdNil");
    hlFdSt.getStgFdNm().put("tot", "cdNil");
    hlFdSt.getStgFdNm().put("toFc", "cdNil");
    hlFdSt.getStgFdNm().put("subt", "cdNil");
    hlFdSt.getStgFdNm().put("suFc", "cdNil");
    hlFdSt.getStgFdNm().put("prFc", "cdNil");
    hlFdSt.getStgFdNm().put("toTx", "cdNil");
    hlFdSt.getStgFdNm().put("txFc", "cdNil");
    hlFdSt.getStgFdNm().put("pri", "cdIl");
    hlFdSt.getStgFdNm().put("tdsc", "cdTd");
    hlFdSt.getStgFdNm().put("invl", "cdinl");
    stgNm = "ceHe"; //to cell header
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getStgFdNm().put("rsRcId", null);
    hlFdSt.getStgFdNm().put("saTy", null);
    hlFdSt.getStgFdNm().put("saId", null);
    hlFdSt.getStgFdNm().put("ownr", null);
    hlFdSt.getStgFdNm().put("rvId", "empt");
    hlFdSt.getStgFdNm().put("toLf", "empt");
    hlFdSt.getStgFdNm().put("txCt", "chNil");
    hlFdSt.getStgFdNm().put("tot", "chNil");
    hlFdSt.getStgFdNm().put("toFc", "chNil");
    hlFdSt.getStgFdNm().put("subt", "chNil");
    hlFdSt.getStgFdNm().put("suFc", "chNil");
    hlFdSt.getStgFdNm().put("prFc", "chNil");
    hlFdSt.getStgFdNm().put("toTx", "chNil");
    hlFdSt.getStgFdNm().put("txFc", "chNil");
    hlFdSt.getStgFdNm().put("pri", "chIl");
    hlFdSt.getStgFdNm().put("tdsc", "chTd");
    hlFdSt.getStgFdNm().put("invl", "chinl");
    stgNm = "inWr"; //input wrapper
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getStgFdNm().put("rvId", null);
    hlFdSt.getStgFdNm().put("saId", null);
    hlFdSt.getStgFdNm().put("ownr", null);
    hlFdSt.getStgFdNm().put("mdEnr", null);
    //unique filed name in PuInGdLn,SaRtLn,ItAdLn,MnfPrc!:
    hlFdSt.getStgFdNm().put("itLf", "iwis");
  }
}
