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

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.HashSet;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IHasNm;
import org.beigesoft.mdlp.AI18nNm;
import org.beigesoft.fct.IFctAsm;
import org.beigesoft.fct.IIniBdFct;
import org.beigesoft.fct.IniBdFct;
import org.beigesoft.hld.HldFldStg;
import org.beigesoft.hld.HldClsStg;
import org.beigesoft.hld.ICtx;
import org.beigesoft.acc.mdlb.IDoc;
import org.beigesoft.acc.mdlp.Entr;
import org.beigesoft.acc.mdlp.I18Acc;
import org.beigesoft.acc.mdlp.I18Curr;
import org.beigesoft.acc.mdlp.InEntr;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Acnt;
import org.beigesoft.acc.mdlp.Sacnt;
import org.beigesoft.acc.mdlp.EnrSrc;

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
    this.iniBdFct.iniBd(pRvs, pFct, pCtx);
    makeUvdCls(pRvs, pFct);
    makeUvdFds(pRvs, pFct);
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
    String stgNm = "flOr"; //list filter order
    HldClsStg hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getNulClss().add(AcStg.class);
    hlClSt.getNulClss().add(Acnt.class);
    hlClSt.getNulClss().add(Sacnt.class);
    hlClSt.getNulClss().add(EnrSrc.class);
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
    stgNm = "liFo"; //list footer
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.setStgClss(new HashMap<Class<? extends IHasId<?>>, String>());
    hlClSt.getStgClss().put(Acnt.class, "lfac");
    hlClSt.getStgClss().put(Entr.class, "lfna");
    hlClSt.getStgClss().put(InEntr.class, "lfia");
    hlClSt.setNulClss(new HashSet<Class<? extends IHasId<?>>>());
    hlClSt.getNulClss().add(AcStg.class);
    hlClSt.getNulClss().add(EnrSrc.class);
    stgNm = "liAc"; //list item actions
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getStgClss().put(Acnt.class, "acac");
    hlClSt.getStgClss().put(Entr.class, "acae");
    hlClSt.getStgClss().put(InEntr.class, "acia");
    hlClSt.getStgClss().put(AcStg.class, "ace");
    hlClSt.getStgClss().put(EnrSrc.class, "ace");
    hlClSt.getStgSclss().put(IDoc.class, "adoc");
    stgNm = "fmAc"; //form actions
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getStgSclss().put(IDoc.class, "adoc");
    stgNm = "prn"; //print
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.setStgClss(new HashMap<Class<? extends IHasId<?>>, String>());
    hlClSt.getStgClss().put(InEntr.class, "pria");
    hlClSt.setStgSclss(new LinkedHashMap<Class<?>, String>());
    hlClSt.getStgSclss().put(IDoc.class, "prdc");
    stgNm = "de"; //delete
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.setStgClss(new HashMap<Class<? extends IHasId<?>>, String>());
    hlClSt.getStgClss().put(InEntr.class, "deia");
    stgNm = "pic"; //picker IHasNm default
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.setStgClss(new HashMap<Class<? extends IHasId<?>>, String>());
    hlClSt.getStgClss().put(Acnt.class, "acc");
    hlClSt.getStgClss().put(Sacnt.class, "sac");
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
    //saTy, srTy
    hlFdSt.getCustClss().add(Integer.class);
    stgNm = "str"; //to string
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    //Acnt.saTy
    hlFdSt.getCustClss().add(Integer.class);
    stgNm = "ord"; //order
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.setStgFdNm(new HashMap<String, String>());
    hlFdSt.getStgFdNm().put("saId", null);
    stgNm = "flt"; //filter
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.setStgFdNm(new HashMap<String, String>());
    hlFdSt.getStgFdNm().put("saId", null);
    stgNm = "ceDe"; //to cell detail
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getStgFdNm().put("saId", null);
    hlFdSt.getStgFdNm().put("ownr", null);
    stgNm = "ceHe"; //to cell header
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getStgFdNm().put("saId", null);
    hlFdSt.getStgFdNm().put("ownr", null);
    stgNm = "inWr"; //input wrapper
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getStgFdNm().put("saId", null);
    hlFdSt.getStgFdNm().put("ownr", null);
  }
}
