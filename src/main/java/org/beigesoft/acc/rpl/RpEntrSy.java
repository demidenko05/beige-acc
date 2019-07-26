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

package org.beigesoft.acc.rpl;

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rpl.IRpEntSync;
import org.beigesoft.acc.mdlp.Entr;
import org.beigesoft.acc.mdlp.InEntr;
import org.beigesoft.acc.srv.ISrBlnc;

/**
 * <p>Service that synchronizes Entr.</p>
 *
 * @author Yury Demidenko
 */
public class RpEntrSy implements IRpEntSync<Entr> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Balance service.</p>
   **/
  private ISrBlnc srBlnc;

  /**
   * <p>Synchronizes given Entr.</p>
   * @param pRvs request scoped vars
   * @param pEnt object
   * @throws Exception - an exception
   **/
  @Override
  public final void sync(final Map<String, Object> pRvs,
    final Entr pEnt) throws Exception {
    if (getOrm().getDbId().equals(pEnt.getDbOr())) {
      throw new ExcCode(ExcCode.WR,
    "Foreign entity born in this database! {ID, ID BIRTH, DB BIRTH}:"
  + " {" + pEnt.getIid() + ", " + pEnt.getIdOr() + "," + pEnt.getDbOr());
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    InEntr inEntr = new InEntr();
    if (pEnt.getSrTy().equals(inEntr.cnsTy())) {
      String whe = "IDOR=" + pEnt.getSrId() + " and DBOR=" + pEnt.getDbOr();
      inEntr = getOrm().retEntCnd(pRvs, vs, InEntr.class, whe);
      if (inEntr == null) {
        throw new ExcCode(ExcCode.WR,
          "There is no InEntr in this database! ID BIRTH/DB BIRTH: "
            + pEnt.getSrId() + "," + pEnt.getDbOr());
      }
      pEnt.setSrId(inEntr.getIid());
    }
    String whe = "IDOR=" + pEnt.getIid() + " and DBOR=" + pEnt.getDbOr();
    String[] ndFds = new String[] {"ver"};
    vs.put(pEnt.getClass().getSimpleName() + "ndFds", ndFds);
    Entr entDb = getOrm().retEntCnd(pRvs, vs, pEnt.getClass(), whe);
    vs.clear();
    pEnt.setIdOr(pEnt.getIid());
    if (entDb != null) {
      pEnt.setIid(entDb.getIid());
      pEnt.setIsNew(false);
    } else {
      pEnt.setIid(null);
      pEnt.setIsNew(true);
    }
    this.srBlnc.hndNewEntr(pRvs, pEnt.getDat());
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
   * <p>Getter for srBlnc.</p>
   * @return ISrBlnc
   **/
  public final ISrBlnc getSrBlnc() {
    return this.srBlnc;
  }

  /**
   * <p>Setter for srBlnc.</p>
   * @param pSrBlnc reference
   **/
  public final void setSrBlnc(final ISrBlnc pSrBlnc) {
    this.srBlnc = pSrBlnc;
  }
}
