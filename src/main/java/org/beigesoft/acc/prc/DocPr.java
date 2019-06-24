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

package org.beigesoft.acc.prc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.IOwned;
import org.beigesoft.hld.HldUvd;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.acc.mdlb.IDoc;
import org.beigesoft.acc.mdlp.Entr;
import org.beigesoft.acc.srv.ISrEntr;

/**
 * <p>Service that retrieves document with entries for printing.</p>
 *
 * @author Yury Demidenko
 */
public class DocPr implements IPrcEnt<IDoc, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Holder UVD settings, vars.</p>
   */
  private HldUvd hldUvd;

  /**
   * <p>Entries service.</p>
   **/
  private ISrEntr srEntr;

  /**
   * <p>Process that pertieves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final IDoc process(final Map<String, Object> pRvs, final IDoc pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    this.orm.refrEnt(pRvs, vs, pEnt);
    pEnt.setIsNew(false);
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setEnt(pEnt);
    List<Class<? extends IOwned<?, ?>>> oeLst = this.hldUvd
      .lazOwnd(pEnt.getClass());
    if (oeLst != null) {
      Map<Class<? extends IOwned<?, ?>>, List<? extends IOwned<?, ?>>>
        owdEntsMp = new LinkedHashMap<Class<? extends IOwned<?, ?>>,
          List<? extends IOwned<?, ?>>>();
      String idOwnr = this.hldUvd.idSql(pRvs, pEnt);
      for (Class<? extends IOwned<?, ?>> oecg : oeLst) {
        Class<IOwned<IDoc, Long>> oec = (Class<IOwned<IDoc, Long>>) oecg;
        String[] lstFds = this.hldUvd.lazLstFds(oec);
        String[] ndFds = Arrays.copyOf(lstFds, lstFds.length);
        Arrays.sort(ndFds);
        vs.put(oec.getSimpleName() + "ndFds", ndFds);
        List<IOwned<IDoc, Long>> lst = this.orm.retLstCnd(pRvs, vs,
          oec, "where OWNR=" + idOwnr); vs.clear();
        owdEntsMp.put(oecg, (List) lst);
        for (IOwned<IDoc, Long> owd : lst) {
          owd.setOwnr(pEnt);
        }
      }
      uvs.setOwdEntsMp(owdEntsMp);
    }
    if (pEnt.getMdEnr()) {
      pRvs.put("entrCls", Entr.class);
      pRvs.put("entrs", this.srEntr.retEntrs(pRvs, pEnt));
    }
    return pEnt;
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
   * <p>Getter for srEntr.</p>
   * @return ISrEntr
   **/
  public final ISrEntr getSrEntr() {
    return this.srEntr;
  }

  /**
   * <p>Setter for srEntr.</p>
   * @param pSrEntr reference
   **/
  public final void setSrEntr(final ISrEntr pSrEntr) {
    this.srEntr = pSrEntr;
  }

  /**
   * <p>Getter for hldUvd.</p>
   * @return HldUvd
   **/
  public final HldUvd getHldUvd() {
    return this.hldUvd;
  }

  /**
   * <p>Setter for hldUvd.</p>
   * @param pHldUvd reference
   **/
  public final void setHldUvd(final HldUvd pHldUvd) {
    this.hldUvd = pHldUvd;
  }
}
