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

package org.beigesoft.ws.srv;

import java.util.Set;
import java.util.Map;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.dlg.IEvalFr;
import org.beigesoft.ws.mdlp.SeSel;

/**
 * <p>S.E.Seller page filter.</p>
 *
 * @author Yury Demidenko
 */
public class FlSeSel implements IEvalFr<IReqDt, String> {

  /**
   * <p>S.E.Seller service.</p>
   **/
  private IFiSeSel fiSeSel;

  /**
   * <p>S.E. entities. Only <b>list</b> operation is allowed, no "modify".</p>
   **/
  private Set<Class<? extends IHasId<?>>> ents;

  /**
   * <p>Evaluate SQL filter for current user.</p>
   * @param pRvs request scoped vars
   * @param pRqDt request data
   * @return filter for has S.E. or null for foreign shared, e.g. Uom
   * @throws Exception - an exception
   **/
  @Override
  public final String eval(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    String nmEnt = pRqDt.getParam("ent");
    boolean isSe = false;
    for (Class<? extends IHasId<?>> cl : this.ents) {
      if (cl.getSimpleName().equals(nmEnt)) {
        isSe = true;
        break;
      }
    }
    if (isSe) {
      SeSel sel = this.fiSeSel.find(pRvs, pRqDt.getUsrNm());
      //simple-hummer implementation:
      String wheSe;
      if (nmEnt.startsWith("I18")) {
        wheSe = "HASNM.SELR=";
      } else if (nmEnt.startsWith("Pri") || nmEnt.endsWith("Plc")
        || nmEnt.endsWith("Spf")) {
        wheSe = "ITM.SELR=";
      } else if (nmEnt.equals("SeCuOr")) {
        wheSe = "SECUOR.SELR=";
      } else { //good/service/paymd
        wheSe = nmEnt.toUpperCase() + ".SELR=";
      }
      return wheSe + sel.getUsr().getUsr();
    } else { //picked foreign, e.g. Uom:
      return null;
    }
  }

  //SGS:
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
   * <p>Getter for ents.</p>
   * @return Set<Class<? extends IHasId<?>>>
   **/
  public final Set<Class<? extends IHasId<?>>> getEnts() {
    return this.ents;
  }

  /**
   * <p>Setter for ents.</p>
   * @param pEnts reference
   **/
  public final void setEnts(final Set<Class<? extends IHasId<?>>> pEnts) {
    this.ents = pEnts;
  }
}
