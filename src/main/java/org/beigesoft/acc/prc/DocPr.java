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

import java.util.Map;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.prc.PrcEntRt;
import org.beigesoft.acc.mdlb.IDoci;
import org.beigesoft.acc.mdlp.Entr;
import org.beigesoft.acc.srv.ISrEntr;

/**
 * <p>Service that retrieves document with entries for printing.</p>
 *
 * @author Yury Demidenko
 */
public class DocPr implements IPrcEnt<IDoci, Long> {

  /**
   * <p>Base retriever.</p>
   **/
  private PrcEntRt<IDoci, Long> retrv;

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
  public final IDoci process(final Map<String, Object> pRvs, final IDoci pEnt,
    final IReqDt pRqDt) throws Exception {
    this.retrv.process(pRvs, pEnt, pRqDt);
    if (pEnt.getMdEnr()) {
      pRvs.put("entrCls", Entr.class);
      pRvs.put("entrs", this.srEntr.retEntrs(pRvs, pEnt));
    }
    return pEnt;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for retrv.</p>
   * @return PrcEntRt<IDoci, Long>
   **/
  public final PrcEntRt<IDoci, Long> getRetrv() {
    return this.retrv;
  }

  /**
   * <p>Setter for retrv.</p>
   * @param pRetrv reference
   **/
  public final void setRetrv(final PrcEntRt<IDoci, Long> pRetrv) {
    this.retrv = pRetrv;
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
}
