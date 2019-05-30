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
import java.util.HashSet;

import org.beigesoft.fct.IFctAsm;
import org.beigesoft.fct.IIniBdFct;
import org.beigesoft.fct.IniBdFct;
import org.beigesoft.hld.HldFldStg;
import org.beigesoft.hld.HldClsStg;
import org.beigesoft.hld.ICtx;
import org.beigesoft.acc.mdlp.InEntr;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Acnt;
import org.beigesoft.acc.mdlp.Sacnt;

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
    String stgNm = "flOr"; //list filter order
    HldClsStg hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getNulClss().add(AcStg.class);
    hlClSt.getNulClss().add(Acnt.class);
    hlClSt.getNulClss().add(Sacnt.class);
    stgNm = "liFo"; //list footer
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getStgSclss().put(InEntr.class, "lfia");
    hlClSt.setNulClss(new HashSet<Class<?>>());
    hlClSt.getNulClss().add(AcStg.class);
    stgNm = "liAc"; //list item actions
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.getStgClss().put(InEntr.class, "acia");
    hlClSt.getStgClss().put(AcStg.class, "ace");
    stgNm = "pic"; //picker IHasNm default
    hlClSt = pFct.getFctBlc().getFctDt().getHlClStgMp().get(stgNm);
    hlClSt.setStgClss(new HashMap<Class<?>, String>());
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
    String stgNm = "str"; //to string
    HldFldStg hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    //Acnt.saTy
    hlFdSt.getCustClss().add(Integer.class);
    stgNm = "ceDe"; //to cell detail
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getStgFdNm().put("saId", null);
    stgNm = "ceHe"; //to cell header
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getStgFdNm().put("saId", null);
    stgNm = "inWr"; //input wrapper
    hlFdSt = pFct.getFctBlc().getFctDt().getHlFdStgMp().get(stgNm);
    hlFdSt.getStgFdNm().put("saId", null);
  }
}
