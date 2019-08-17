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

package org.beigesoft.ws.prc;

import java.util.Map;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.prc.PrcEntRt;
import org.beigesoft.ws.mdlb.IHsSeSel;
import org.beigesoft.ws.mdlp.SeSel;
import org.beigesoft.ws.srv.IFiSeSel;

/**
 * <p>Service that retrieves has S.E.seller from DB.</p>
 *
 * @param <T> entity type
 * @param <ID> entity ID type
 * @author Yury Demidenko
 */
public class HsSeSelRt<T extends IHsSeSel<ID>, ID> implements IPrcEnt<T, ID> {

  /**
   * <p>S.E.Seller service.</p>
   **/
  private IFiSeSel fiSeSel;

  /**
   * <p>Base retriever.</p>
   **/
  private PrcEntRt<T, ID> retrv;

  /**
   * <p>Process that retrieves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final T process(final Map<String, Object> pRvs, final T pEnt,
    final IReqDt pRqDt) throws Exception {
    SeSel sel = this.fiSeSel.find(pRvs, pRqDt.getUsrNm());
    this.retrv.process(pRvs, pEnt, pRqDt);
    if (!pEnt.getSelr().getUsr().getUsr()
      .equals(sel.getUsr().getUsr())) {
      throw new ExcCode(ExcCode.SPAM,
        "Attempt to view S.E. entity: usr/cls/id/ownr: "
          + pRqDt.getUsrNm() + "/" + pEnt.getClass() + "/" + pEnt.getIid()
            + "/" + pEnt.getSelr().getUsr().getUsr());
    }
    return pEnt;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fiSeSel.</p>
   * @return IFiSeSel
   **/
  public final IFiSeSel getFiSeSel() {
    return this.fiSeSel;
  }

  /**
   * <p>Setter for fiSeSel.</p>
   * @param pFiSeSel reference
   **/
  public final void setFiSeSel(final IFiSeSel pFiSeSel) {
    this.fiSeSel = pFiSeSel;
  }

  /**
   * <p>Getter for retrv.</p>
   * @return PrcEntRt<T, ID>
   **/
  public final PrcEntRt<T, ID> getRetrv() {
    return this.retrv;
  }

  /**
   * <p>Setter for retrv.</p>
   * @param pRetrv reference
   **/
  public final void setRetrv(final PrcEntRt<T, ID> pRetrv) {
    this.retrv = pRetrv;
  }
}
