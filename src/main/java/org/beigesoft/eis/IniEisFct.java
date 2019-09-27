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
import org.beigesoft.mdlp.Lng;
import org.beigesoft.mdlp.AI18nNm;
import org.beigesoft.mdlp.CsvMth;
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
import org.beigesoft.acc.mdlp.Srv;
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
import org.beigesoft.acc.mdlp.DbCr;
import org.beigesoft.acc.rpl.RplAcc;
import org.beigesoft.acc.rpl.RpExDbl;
import org.beigesoft.acc.rpl.RpExCrl;
import org.beigesoft.ws.mdlp.PriCt;
import org.beigesoft.ws.mdlp.PriItm;
import org.beigesoft.ws.mdlp.CatGs;
import org.beigesoft.ws.mdlp.CatSp;
import org.beigesoft.ws.mdlp.ChoSp;
import org.beigesoft.ws.mdlp.ChoSpTy;
import org.beigesoft.ws.mdlp.CurrRt;
import org.beigesoft.ws.mdlp.Htmlt;
import org.beigesoft.ws.mdlp.ItmSp;
import org.beigesoft.ws.mdlp.ItmSpGr;
import org.beigesoft.ws.mdlp.PicPlc;
import org.beigesoft.ws.mdlp.SubCat;
import org.beigesoft.ws.mdlp.SrvSpf;
import org.beigesoft.ws.mdlp.SrvPlc;
import org.beigesoft.ws.mdlp.SrvCtl;
import org.beigesoft.ws.mdlp.PriSrv;
import org.beigesoft.ws.mdlp.ItmSpf;
import org.beigesoft.ws.mdlp.ItmPlc;
import org.beigesoft.ws.mdlp.ItmCtl;
import org.beigesoft.ws.mdlp.AddStg;
import org.beigesoft.ws.mdlp.TrdStg;
import org.beigesoft.ws.mdlp.Buyer;
import org.beigesoft.ws.mdlp.BurPric;
import org.beigesoft.ws.mdlp.I18CatGs;
import org.beigesoft.ws.mdlp.I18ChoSp;
import org.beigesoft.ws.mdlp.I18ItmSp;
import org.beigesoft.ws.mdlp.I18ItmSpGr;
import org.beigesoft.ws.mdlp.I18SpeLi;
import org.beigesoft.ws.mdlp.I18Trd;
import org.beigesoft.ws.mdlp.SeSel;
import org.beigesoft.ws.mdlp.Deliv;
import org.beigesoft.ws.mdlp.PayMd;
import org.beigesoft.ws.mdlp.CuOr;
import org.beigesoft.ws.mdlp.CuOrGdLn;
import org.beigesoft.ws.mdlp.CuOrSrLn;
import org.beigesoft.ws.mdlp.CuOrTxLn;
import org.beigesoft.ws.mdlp.CuOrSe;
import org.beigesoft.ws.mdlp.CuOrSeGdLn;
import org.beigesoft.ws.mdlp.CuOrSeSrLn;
import org.beigesoft.ws.mdlp.CuOrSeTxLn;
import org.beigesoft.ws.mdlp.SePayMd;
import org.beigesoft.ws.mdlp.SeItmCtl;
import org.beigesoft.ws.mdlp.SeSrvCtl;
import org.beigesoft.ws.mdlp.SeItm;
import org.beigesoft.ws.mdlp.SeSrv;
import org.beigesoft.ws.mdlp.SitTxDl;
import org.beigesoft.ws.mdlp.SerTxDl;
import org.beigesoft.ws.mdlp.SeSrvPlc;
import org.beigesoft.ws.mdlp.I18SeSrv;
import org.beigesoft.ws.mdlp.SeItmPlc;
import org.beigesoft.ws.mdlp.I18SeItm;
import org.beigesoft.ws.mdlp.SeItmPri;
import org.beigesoft.ws.mdlp.SeItmSpf;
import org.beigesoft.ws.mdlp.SeSrvPri;
import org.beigesoft.ws.mdlp.SeSrvSpf;

/**
 * <p>Business-logic dependent sub-initializer main
 * factory during startup.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class IniEisFct<RS> implements IIniBdFct<RS> {

  /**
   * <p>S.E.Seller entities ID.</p>
   **/
  public static final Integer ID_SESEL = 3;

  /**
   * <p>Base initializer.</p>
   **/
  private final IniBdFct<RS> iniBdFct = new IniBdFct<RS>();

  /**
   * <p>S.E.Seller entities.</p>
   **/
  private HldEnts seEnts;

  /**
   * <p>Initializes factory.</p>
   * @param pRvs request scoped vars
   * @param pFct factory
   * @param pCtx Context attributes
   * @throws Exception - an exception
   **/
  @Override
  public final void iniBd(final Map<String, Object> pRvs,
    final IFctAsm<RS> pFct, final ICtx pCtx) throws Exception {
    this.iniBdFct.lazAdmEnts().getEnts().add(RplAcc.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(RpExDbl.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(RpExCrl.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(PriItm.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(PriCt.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(CatGs.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(CatSp.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(ChoSp.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(ChoSpTy.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(CurrRt.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(Htmlt.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(ItmSp.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(ItmSpGr.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(PicPlc.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(SubCat.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(BurPric.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(Buyer.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(AddStg.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(TrdStg.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(ItmCtl.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(ItmPlc.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(ItmSpf.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(PriSrv.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(SrvCtl.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(SrvPlc.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(SrvSpf.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(I18CatGs.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(I18ChoSp.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(I18ItmSp.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(I18ItmSpGr.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(I18Trd.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(SeSel.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(SeItmCtl.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(SeSrvCtl.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(Deliv.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(PayMd.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(CuOr.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(CuOrGdLn.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(CuOrSrLn.class);
    this.iniBdFct.lazAdmEnts().getEnts().add(CuOrTxLn.class);
    this.iniBdFct.iniBd(pRvs, pFct, pCtx);
    Set<Integer> rdre = new HashSet<Integer>();
    rdre.add(ID_SESEL);
    this.iniBdFct.lazAdmEnts().getShrEnts().add(new EntShr(PicPlc.class, rdre));
    this.iniBdFct.lazAdmEnts().getShrEnts().add(new EntShr(ChoSp.class, rdre));
    this.iniBdFct.lazAdmEnts().getShrEnts().add(new EntShr(ItmSp.class, rdre));
    this.iniBdFct.lazAdmEnts().getShrEnts().add(new EntShr(PriCt.class, rdre));
    makeUvdCls(pRvs, pFct);
    makeUvdFds(pRvs, pFct);
    HldEnts acEnts = new HldEnts();
    acEnts.setIid(HldEnts.ID_BASE);
    acEnts.setShrEnts(new HashSet<EntShr>());
    Set<Integer> rdrs = new HashSet<Integer>();
    rdrs.add(HldEnts.ID_ADMIN);
    Set<Integer> rdrse = new HashSet<Integer>();
    rdrse.add(HldEnts.ID_ADMIN);
    rdrse.add(ID_SESEL);
    acEnts.getShrEnts().add(new EntShr(Curr.class, rdrs));
    acEnts.getShrEnts().add(new EntShr(DbCr.class, rdrs));
    acEnts.getShrEnts().add(new EntShr(Lng.class, rdrse));
    acEnts.getShrEnts().add(new EntShr(Acnt.class, rdrs));
    acEnts.getShrEnts().add(new EntShr(Tax.class, rdrse));
    acEnts.getShrEnts().add(new EntShr(TxDst.class, rdrse));
    acEnts.getShrEnts().add(new EntShr(TxCt.class, rdrse));
    acEnts.getShrEnts().add(new EntShr(Uom.class, rdrse));
    acEnts.getShrEnts().add(new EntShr(Itm.class, rdrs));
    acEnts.getShrEnts().add(new EntShr(Srv.class, rdrs));
    acEnts.getShrEnts().add(new EntShr(CsvMth.class, rdrs));
    pFct.getFctBlc().getFctDt().getHldsEnts().add(acEnts);
    pFct.getFctBlc().getFctDt().getMaFrClss().add(ItmCt.class);
    pFct.getFctBlc().getFctDt().getHldsEnts().add(lazSeEnts());
  }

  /**
   * <p>Getter for S.E.Seller's entities.</p>
   * @return HldEnts
   **/
  public final HldEnts lazSeEnts() {
    if (this.seEnts == null) {
      this.seEnts = new HldEnts();
      this.seEnts.setIid(ID_SESEL);
      this.seEnts.setShrEnts(new HashSet<EntShr>());
      Set<Integer> rdrs = new HashSet<Integer>();
      rdrs.add(HldEnts.ID_ADMIN);
      this.seEnts.getShrEnts().add(new EntShr(SeSrv.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(SeItm.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(SitTxDl.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(SerTxDl.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(SeSrvPlc.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(I18SeSrv.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(SeItmPlc.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(I18SeItm.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(SeItmSpf.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(SeSrvSpf.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(SeItmPri.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(SeSrvPri.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(CuOrSe.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(CuOrSeGdLn.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(CuOrSeSrLn.class, rdrs));
      this.seEnts.getShrEnts().add(new EntShr(CuOrSeTxLn.class, rdrs));
      this.seEnts.setEnts(new HashSet<Class<? extends IHasId<?>>>());
      this.seEnts.getEnts().add(SeItm.class);
      this.seEnts.getEnts().add(SitTxDl.class);
      this.seEnts.getEnts().add(SeSrv.class);
      this.seEnts.getEnts().add(SerTxDl.class);
      this.seEnts.getEnts().add(SeSrvPlc.class);
      this.seEnts.getEnts().add(I18SeSrv.class);
      this.seEnts.getEnts().add(SeItmPlc.class);
      this.seEnts.getEnts().add(I18SeItm.class);
      this.seEnts.getEnts().add(SeItmSpf.class);
      this.seEnts.getEnts().add(SeItmPri.class);
      this.seEnts.getEnts().add(SeSrvSpf.class);
      this.seEnts.getEnts().add(SeSrvPri.class);
      this.seEnts.getEnts().add(SePayMd.class);
      this.seEnts.getEnts().add(CuOrSe.class);
      this.seEnts.getEnts().add(CuOrSeGdLn.class);
      this.seEnts.getEnts().add(CuOrSeSrLn.class);
      this.seEnts.getEnts().add(CuOrSeTxLn.class);
    }
    return this.seEnts;
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
    pFct.getFctBlc().getFctDt().getCustIdClss().add(PriItm.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SubCat.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(CurrRt.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(PriSrv.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(BurPric.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(ItmCtl.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SeSrvPlc.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SeItmPlc.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SeItmPri.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SeItmSpf.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SeSrvPri.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SeSrvSpf.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(ItmPlc.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(ItmSpf.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SrvCtl.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SrvPlc.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SrvSpf.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SrvSpf.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18CatGs.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18ChoSp.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18SeSrv.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18SeItm.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18ItmSp.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18ItmSpGr.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18SpeLi.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(I18Trd.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SeSel.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SeSrvCtl.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(SeItmCtl.class);
    pFct.getFctBlc().getFctDt().getCustIdClss().add(Deliv.class);
    String stgNm = "flOr"; //list filter order
    HldClsStg hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getNulClss().add(AcStg.class);
    hlClSt.getNulClss().add(AddStg.class);
    hlClSt.getNulClss().add(TrdStg.class);
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
    hlClSt.getStgClss().put(BurPric.class, "buyr");
    hlClSt.getStgClss().put(I18CatGs.class, "lng");
    hlClSt.getStgClss().put(I18ChoSp.class, "lng");
    hlClSt.getStgClss().put(SeSel.class, "dbcr");
    hlClSt.getStgClss().put(I18SeSrv.class, "lng");
    hlClSt.getStgClss().put(I18SeItm.class, "lng");
    hlClSt.getStgClss().put(I18ItmSp.class, "lng");
    hlClSt.getStgClss().put(I18ItmSpGr.class, "lng");
    hlClSt.getStgClss().put(I18Trd.class, "lng");
    hlClSt.getStgClss().put(SrvSpf.class, "itm");
    hlClSt.getStgClss().put(SrvPlc.class, "itm");
    hlClSt.getStgClss().put(SrvCtl.class, "itm");
    hlClSt.getStgClss().put(SeItmPri.class, "itm");
    hlClSt.getStgClss().put(SeItmSpf.class, "itm");
    hlClSt.getStgClss().put(SeSrvPri.class, "itm");
    hlClSt.getStgClss().put(SeSrvSpf.class, "itm");
    hlClSt.getStgClss().put(SeSrvPlc.class, "itm");
    hlClSt.getStgClss().put(SeItmPlc.class, "itm");
    hlClSt.getStgClss().put(ItmPlc.class, "itm");
    hlClSt.getStgClss().put(SeSrvCtl.class, "itm");
    hlClSt.getStgClss().put(SeItmCtl.class, "itm");
    hlClSt.getStgClss().put(ItmCtl.class, "itm");
    hlClSt.getStgClss().put(ItmSpf.class, "itm");
    hlClSt.getStgClss().put(PriSrv.class, "itm");
    hlClSt.getStgClss().put(PriItm.class, "itm");
    hlClSt.getStgClss().put(CurrRt.class, "curr");
    hlClSt.getStgClss().put(SubCat.class, "catl");
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
    hlClSt.getNulClss().add(AddStg.class);
    hlClSt.getNulClss().add(TrdStg.class);
    hlClSt.getNulClss().add(EnrSrc.class);
    hlClSt.getNulClss().add(DriEnrSr.class);
    hlClSt.getNulClss().add(EmpWg.class);
    hlClSt.getNulClss().add(CuOr.class);
    hlClSt.getNulClss().add(CuOrSe.class);
    hlClSt.getNulClss().add(CuOrTxLn.class);
    hlClSt.getNulClss().add(CuOrSeTxLn.class);
    hlClSt.getNulClss().add(CuOrSrLn.class);
    hlClSt.getNulClss().add(CuOrSeSrLn.class);
    hlClSt.getNulClss().add(CuOrGdLn.class);
    hlClSt.getNulClss().add(CuOrSeGdLn.class);
    stgNm = "liAc"; //list item actions
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getStgClss().put(Acnt.class, "acac");
    hlClSt.getStgClss().put(BnStLn.class, "absl");
    hlClSt.getStgClss().put(Entr.class, "acae");
    hlClSt.getStgClss().put(InEntr.class, "acia");
    hlClSt.getStgClss().put(AcStg.class, "ace");
    hlClSt.getStgClss().put(AddStg.class, "ace");
    hlClSt.getStgClss().put(TrdStg.class, "ace");
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
    hlClSt.getStgClss().put(SeItmSpf.class, "speAc");
    hlClSt.getStgClss().put(SeSrvSpf.class, "speAc");
    hlClSt.getStgClss().put(ItmSpf.class, "speAc");
    hlClSt.getStgClss().put(SrvSpf.class, "speAc");
    hlClSt.getStgClss().put(CuOr.class, "acCuOr");
    hlClSt.getStgClss().put(CuOrSe.class, "acCuOr");
    hlClSt.getStgClss().put(EmpWg.class, null);
    hlClSt.getStgClss().put(CuOrTxLn.class, null);
    hlClSt.getStgClss().put(CuOrSeTxLn.class, null);
    hlClSt.getStgClss().put(CuOrSrLn.class, null);
    hlClSt.getStgClss().put(CuOrSeSrLn.class, null);
    hlClSt.getStgClss().put(CuOrGdLn.class, null);
    hlClSt.getStgClss().put(CuOrSeGdLn.class, null);
    hlClSt.getStgSclss().put(IDoci.class, "adoc");
    stgNm = "fmAc"; //form actions
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getStgClss().put(ItmAdd.class, "adcl");
    hlClSt.getStgClss().put(MnfPrc.class, "adcl");
    hlClSt.getStgClss().put(ItmUlb.class, "adcl");
    hlClSt.getStgClss().put(Wage.class, "awg");
    hlClSt.getStgClss().put(SeItmSpf.class, "speAc");
    hlClSt.getStgClss().put(SeSrvSpf.class, "speAc");
    hlClSt.getStgClss().put(ItmSpf.class, "speAc");
    hlClSt.getStgClss().put(SrvSpf.class, "speAc");
    hlClSt.getStgClss().put(CuOr.class, "acCfl");
    hlClSt.getStgClss().put(CuOrSe.class, "acCfl");
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
    hlFdSt.getStgFdNm().put("sec1", "pwd");
    hlFdSt.getStgFdNm().put("sec2", "pwd");
    hlFdSt.getCustClss().add(BigDecimal.class); //inv.payment total vs others
    hlFdSt.getStgClss().put(PuInGdLn.class, "pinl");
    hlFdSt.getStgClss().put(SaInGdLn.class, "inl");
    hlFdSt.getStgClss().put(MnfPrc.class, "dits");
    hlFdSt.getStgClss().put(SeSel.class, "selr");
    //Acnt.saTy
    hlFdSt.getCustClss().add(Integer.class);
    hlFdSt.getStgSclss().put(AInv.class, "inv");
    hlFdSt.getStgSclss().put(IDoci.class, "doc");
    stgNm = "ord"; //order
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getStgFdNm().put("knCs", "ord");
    hlFdSt.getStgFdNm().put("tot", "ord");
    hlFdSt.getStgFdNm().put("itm", "ord");
    hlFdSt.getStgFdNm().put("spec", "ord");
    stgNm = "flt"; //filter
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getStgFdNm().put("knCs", "pri");
    hlFdSt.getStgFdNm().put("tot", "pri");
    hlFdSt.getStgFdNm().put("toPa", "explToPa");
    hlFdSt.getStgFdNm().put("dbcr", "ent");
    hlFdSt.getStgFdNm().put("acc", "ent");
    hlFdSt.getStgFdNm().put("lng", "ent");
    hlFdSt.getStgFdNm().put("itm", "ent");
    hlFdSt.getStgFdNm().put("spec", "ent");
    hlFdSt.getStgFdNm().put("payb", "dt");
    hlFdSt.getStgFdNm().put("rvId", null);
    stgNm = "flth"; //filter hidden
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.setStgFdNm(new HashMap<String, String>());
    hlFdSt.getStgFdNm().put("choTy", "ent");
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
    hlFdSt.getStgFdNm().put("paFc", null);
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
    hlFdSt.getStgFdNm().put("paFc", null);
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
    hlFdSt.getStgFdNm().put("str4", "ispec");
  }
}
