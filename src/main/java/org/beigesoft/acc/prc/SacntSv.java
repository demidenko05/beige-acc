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
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.acc.mdlp.Sacnt;

/**
 * <p>Service that saves subacc line.</p>
 *
 * @author Yury Demidenko
 */
public class SacntSv implements IPrcEnt<Sacnt, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

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
  public final Sacnt process(final Map<String, Object> pRvs, final Sacnt pEnt,
    final IReqDt pRqDt) throws Exception {
    if (!pEnt.getIsNew()) {
      throw new ExcCode(ExcCode.WRPR, "edit_not_allowed");
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    this.orm.refrEnt(pRvs, vs, pEnt.getOwnr());
    if (pEnt.getOwnr() == null ||  pEnt.getOwnr().getSaTy() == null) {
      throw new ExcCode(ExcCode.WRPR, "wrong_acc_for_subacc");
    }
    long owVrWs = Long.parseLong(pRqDt.getParam("owVr"));
    if (owVrWs != pEnt.getOwnr().getVer()) {
      throw new ExcCode(IOrm.DRTREAD, "dirty_read");
    }
    pEnt.setSaTy(pEnt.getOwnr().getSaTy());
    this.orm.insIdLn(pRvs, vs, pEnt);
    pRvs.put("msgSuc", "insert_ok");
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setOwnr(pEnt.getOwnr());
    return null;
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
}
