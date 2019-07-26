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

import org.beigesoft.fct.FctBlc;
import org.beigesoft.rpl.IFcRpEnSy;
import org.beigesoft.rpl.IRpEntSync;
import org.beigesoft.rpl.RpEntSyDb;
import org.beigesoft.rpl.RpEntSyOrId;
import org.beigesoft.acc.srv.ISrBlnc;

/**
 * <p>Synchronizers entity factory.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FcRpEnSy<RS> implements IFcRpEnSy {

  /**
   * <p>Main factory.</p>
   **/
  private FctBlc<RS> fctBlc;

  //requested data:
  /**
   * <p>Processors map.</p>
   **/
  private final Map<String, IRpEntSync<?>> syncs =
    new HashMap<String, IRpEntSync<?>>();

  /**
   * <p>Gets synchronizer in lazy mode by given name.</p>
   * @param pRvs request scoped vars
   * @param pNm - synchronizer name
   * @return requested synchronizer
   * @throws Exception - an exception
   */
  public final IRpEntSync<?> laz(final Map<String, Object> pRvs,
    final String pNm) throws Exception {
    IRpEntSync rz = this.syncs.get(pNm);
    if (rz == null) {
      synchronized (this) {
       rz = this.syncs.get(pNm);
        if (rz == null) {
          if (RpEntSyDb.class.getSimpleName().equals(pNm)) {
            RpEntSyDb sync = new RpEntSyDb();
            sync.setOrm(this.fctBlc.lazOrm(pRvs));
            rz = sync;
            this.syncs.put(pNm, rz);
          } else if (RpEntSyOrId.class.getSimpleName().equals(pNm)) {
            RpEntSyOrId sync = new RpEntSyOrId();
            sync.setOrm(this.fctBlc.lazOrm(pRvs));
            rz = sync;
            this.syncs.put(pNm, rz);
          } else if (RpEntrSy.class.getSimpleName().equals(pNm)) {
            RpEntrSy sync = new RpEntrSy();
            sync.setOrm(this.fctBlc.lazOrm(pRvs));
            sync.setSrBlnc((ISrBlnc) this.fctBlc
              .laz(pRvs, ISrBlnc.class.getSimpleName()));
            rz = sync;
            this.syncs.put(pNm, rz);
          }
        }
      }
    }
    if (rz == null) {
      throw new Exception("There is no filter with name " + pNm);
    }
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for fctBlc.</p>
   * @return FctBlc<RS>
   **/
  public final synchronized FctBlc<RS> getFctBlc() {
    return this.fctBlc;
  }

  /**
   * <p>Setter for fctBlc.</p>
   * @param pFctBlc reference
   **/
  public final synchronized void setFctBlc(final FctBlc<RS> pFctBlc) {
    this.fctBlc = pFctBlc;
  }
}
