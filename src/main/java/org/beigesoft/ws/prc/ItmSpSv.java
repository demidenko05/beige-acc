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
import java.util.HashMap;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.prc.PrcEnfSv;
import org.beigesoft.ws.mdlb.AItmSpf;
import org.beigesoft.ws.mdlb.AItmSpfId;

/**
 * <p>Service that saves item specifics into DB.</p>
 *
 * @param <T> entity type
 * @param <ID> entity ID type
 * @author Yury Demidenko
 */
public class ItmSpSv<T extends AItmSpf<?, ID>, ID extends AItmSpfId<?>>
  implements IPrcEnt<T, ID> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Delegate.</p>
   **/
  private PrcEnfSv<T, ID> prcEnfSv;

  /**
   * <p>Process that saves entity.</p>
   * @param pRvs request scoped vars, e.g. return this line's
   * owner(document) in "nextEntity" for farther processing
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final T process(final Map<String, Object> pRvs, final T pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    if (pRqDt.getParam("parFile") != null) { //file upload need:
      return this.prcEnfSv.process(pRvs, pEnt, pRqDt);
    } else {
      this.orm.refrEnt(pRvs, vs, pEnt.getSpec());
      if (pEnt.getSpec().getChSpTy() != null) {
        pEnt.setLng2(pEnt.getSpec().getChSpTy().getIid());
        pEnt.setStr2(pEnt.getSpec().getChSpTy().getNme());
      }
      if (pEnt.getIsNew()) {
        this.orm.insert(pRvs, vs, pEnt);
        pRvs.put("msgSuc", "insert_ok");
      } else {
        this.orm.update(pRvs, vs, pEnt);
        pRvs.put("msgSuc", "update_ok");
      }
      return pEnt;
    }
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
   * <p>Getter for prcEnfSv.</p>
   * @return PrcEnfSv<T, ID>
   **/
  public final PrcEnfSv<T, ID> getPrcEnfSv() {
    return this.prcEnfSv;
  }

  /**
   * <p>Setter for prcEnfSv.</p>
   * @param pPrcEnfSv reference
   **/
  public final void setPrcEnfSv(final PrcEnfSv<T, ID> pPrcEnfSv) {
    this.prcEnfSv = pPrcEnfSv;
  }
}
