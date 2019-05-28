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
import org.beigesoft.log.ILog;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.acc.mdl.ISacnt;

/**
 * <p>Service that deletes subacc from DB.</p>
 *
 * @author Yury Demidenko
 * @param <RS> platform dependent record set type
 */
public class IsacntDl<RS> implements IPrcEnt<ISacnt, Long> {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>RDB service.</p>
   **/
  private IRdb<RS> rdb;

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
  public final ISacnt process(final Map<String, Object> pRvs, final ISacnt pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    Integer usedRc = this.rdb.evInt(//cheks if used even in reversed entries:
      "select count(*) as USEDRC from ENTR where (SADID=" + pEnt.getIid()
        + " and SADTY=" + pEnt.cnsTy() +  ") or (SACID=" + pEnt.getIid()
          + " and SACTY=" + pEnt.cnsTy() + ");", "USEDRC");
    if (usedRc == null || usedRc == 0) {
      this.orm.del(pRvs, vs, pEnt);
      pRvs.put("msgSuc", "delete_ok");
    } else {
      StringBuffer sb = new StringBuffer();
      sb.append("Attemp to delete used subacc!!! usr/cls/type/id/nm: ");
      sb.append(pRqDt.getUsrNm() + "/" + pEnt.getClass().getSimpleName());
      sb.append("/" + pEnt.cnsTy() + "/" + pEnt.getIid() + "/" + pEnt.getNme());
      this.log.error(pRvs, getClass(), sb.toString());
      throw new ExcCode(ExcCode.WRPR, "subacc_used");
    }
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

  /**
   * <p>Geter for log.</p>
   * @return ILog
   **/
  public final ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final void setLog(final ILog pLog) {
    this.log = pLog;
  }

  /**
   * <p>Getter for rdb.</p>
   * @return IRdb<RS>
   **/
  public final IRdb<RS> getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final void setRdb(final IRdb<RS> pRdb) {
    this.rdb = pRdb;
  }
}
