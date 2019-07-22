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

package org.beigesoft.acc.srv;

import java.util.Map;
import java.util.Date;
import java.util.Calendar;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdlp.IOrId;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlp.AcStg;

/**
 * <p>Base shared code.</p>
 *
 * @author Yury Demidenko
 */
public class UtlBas {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Checks base validity - date, is foreigner before save.</p>
   * @param pRvs request scoped vars
   * @param pEnt doc/line/entry
   * @param pDat date
   * @throws Exception - an exception
   **/
  public final void chDtForg(final Map<String, Object> pRvs, final IOrId pEnt,
    final Date pDat) throws Exception {
    if (!pEnt.getDbOr().equals(this.orm.getDbId())) {
      throw new ExcCode(ExcCode.SPAM, "Can not change foreign source!");
    }
    AcStg astg = (AcStg) pRvs.get("astg");
    Calendar calCuMh = Calendar.getInstance();
    calCuMh.setTime(astg.getMnth());
    calCuMh.set(Calendar.DAY_OF_MONTH, 1);
    calCuMh.set(Calendar.HOUR_OF_DAY, 0);
    calCuMh.set(Calendar.MINUTE, 0);
    calCuMh.set(Calendar.SECOND, 0);
    calCuMh.set(Calendar.MILLISECOND, 0);
    Calendar calDoc = Calendar.getInstance();
    calDoc.setTime(pDat);
    calDoc.set(Calendar.DAY_OF_MONTH, 1);
    calDoc.set(Calendar.HOUR_OF_DAY, 0);
    calDoc.set(Calendar.MINUTE, 0);
    calDoc.set(Calendar.SECOND, 0);
    calDoc.set(Calendar.MILLISECOND, 0);
    if (calCuMh.getTime().getTime() != calDoc.getTime().getTime()) {
      throw new ExcCode(ExcCode.WRPR, "wrong_acperiod");
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
}
