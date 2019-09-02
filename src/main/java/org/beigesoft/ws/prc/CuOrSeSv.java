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
import java.util.Arrays;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.ws.mdl.EOrdStat;
import org.beigesoft.ws.mdlp.CuOrSe;
import org.beigesoft.ws.mdlp.SeSel;
import org.beigesoft.ws.srv.ICncOrd;
import org.beigesoft.ws.srv.IFiSeSel;

/**
 * <p>Service that changes customer S.E. order status. It's possible:
 * <ul>
 * <li>From BOOKED, PAYED, CLOSED to CANCELLED, action "orAct=cnc"</li>
 * <li>From BOOKED to PAYED, action "orAct=pyd"</li>
 * <li>From BOOKED, PAYED  to CLOSED, action "orAct=cls"</li>
 * </ul>. It checks derived from order invoice (if it exist).</p>
 *
 * @author Yury Demidenko
 */
public class CuOrSeSv implements IPrcEnt<CuOrSe, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Cancel order service.</p>
   **/
  private ICncOrd cncOrd;

  /**
   * <p>S.E.Seller service.</p>
   **/
  private IFiSeSel fiSeSel;

  /**
   * <p>Process entity request.</p>
   * @param pRvs additional param, e.g. return this line's
   * document in "nextEntity" for farther process
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final CuOrSe process(final Map<String, Object> pRvs,
    final CuOrSe pEnt, final IReqDt pRqDt) throws Exception {
    String orAct = pRqDt.getParam("orAct");
    if (!("cnc".equals(orAct) || "pyd".equals(orAct) || "cls".equals(orAct))) {
      throw new ExcCode(ExcCode.SPAM,
        "Wrong action CO! " + orAct);
    }
    if (pEnt.getIsNew()) {
      throw new ExcCode(ExcCode.SPAM, "Attempt creating CO!");
    } else {
      SeSel sel = this.fiSeSel.find(pRvs, pRqDt.getUsrNm());
      pEnt.setSelr(sel);
      Map<String, Object> vs = new HashMap<String, Object>();
      vs.put("SeSeldpLv", 1);
      vs.put("SeSelndFds", new String[] {"usr"});
      CuOrSe oco = this.orm.retEnt(pRvs, vs, pEnt); vs.clear();
      if (!pEnt.getSelr().getUsr().getUsr()
        .equals(oco.getSelr().getUsr().getUsr())) {
        throw new ExcCode(ExcCode.SPAM,
          "Attempt to change S.E. entity: usr/cls/id/ownr: "
            + pRqDt.getUsrNm() + "/" + pEnt.getClass() + "/" + pEnt.getIid()
              + "/" + oco.getSelr().getUsr().getUsr());
      }
      oco.setDscr(pEnt.getDscr());
      boolean isNdUp = true;
      if ("cnc".equals(orAct)) {
        if (!(oco.getStas().equals(EOrdStat.BOOKED)
          || oco.getStas().equals(EOrdStat.PAYED)
            || oco.getStas().equals(EOrdStat.CLOSED))) {
          throw new ExcCode(ExcCode.SPAM,
            "Wrong action CO for status ! " + orAct + "/" + oco.getStas());
        }
        this.cncOrd.cancel(pRvs, oco, EOrdStat.CANCELED);
        isNdUp = false;
      } else if ("pyd".equals(orAct)) {
        if (!oco.getStas().equals(EOrdStat.BOOKED)) {
          throw new ExcCode(ExcCode.SPAM,
            "Wrong action CO for status ! " + orAct + "/" + oco.getStas());
        }
        oco.setStas(EOrdStat.PAYED);
      } else if ("cls".equals(orAct)) {
        if (!(oco.getStas().equals(EOrdStat.BOOKED)
          || oco.getStas().equals(EOrdStat.PAYED))) {
          throw new ExcCode(ExcCode.SPAM,
            "Wrong action CO for status ! " + orAct + "/" + oco.getStas());
        }
        oco.setStas(EOrdStat.CLOSED);
      }
      if (isNdUp) {
        String[] ndFds = new String[] {"ver", "stas", "dscr"};
        Arrays.sort(ndFds);
        vs.put("ndFds", ndFds);
        this.orm.update(pRvs, vs, oco);
      }
      return oco;
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
   * <p>Getter for cncOrd.</p>
   * @return ICncOrd
   **/
  public final ICncOrd getCncOrd() {
    return this.cncOrd;
  }

  /**
   * <p>Setter for cncOrd.</p>
   * @param pCncOrd reference
   **/
  public final void setCncOrd(final ICncOrd pCncOrd) {
    this.cncOrd = pCncOrd;
  }
}
