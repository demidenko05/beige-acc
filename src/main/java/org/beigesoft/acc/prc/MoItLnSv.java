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
import java.util.Arrays;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.srv.II18n;
import org.beigesoft.acc.mdlp.MoItLn;
import org.beigesoft.acc.srv.ISrWrhEnr;

/**
 * <p>Service that saves move item line into DB.</p>
 *
 * @author Yury Demidenko
 */
public class MoItLnSv implements IPrcEnt<MoItLn, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Warehouse entries service.</p>
   **/
  private ISrWrhEnr srWrhEnr;

  /**
   * <p>I18N service.</p>
   */
  private II18n i18n;

  /**
   * <p>Process that pertieves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final MoItLn process(final Map<String, Object> pRvs, final MoItLn pEnt,
    final IReqDt pRqDt) throws Exception {
    if (pEnt.getIsNew()) {
      Map<String, Object> vs = new HashMap<String, Object>();
      this.orm.refrEnt(pRvs, vs, pEnt.getOwnr());
      long owVrWs = Long.parseLong(pRqDt.getParam("owVr"));
      if (owVrWs != pEnt.getOwnr().getVer()) {
        throw new ExcCode(IOrm.DRTREAD, "dirty_read");
      }
      if (pEnt.getRvId() != null) {
        MoItLn revd = new MoItLn();
        revd.setIid(pEnt.getRvId());
        this.orm.refrEnt(pRvs, vs, revd);
        pEnt.setDbOr(this.orm.getDbId());
        pEnt.setItm(revd.getItm());
        pEnt.setUom(revd.getUom());
        pEnt.setWpFr(revd.getWpFr());
        pEnt.setWpTo(revd.getWpTo());
        pEnt.setQuan(revd.getQuan().negate());
        CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
        StringBuffer sb = new StringBuffer();
        if (pEnt.getDscr() != null) {
          sb.append(pEnt.getDscr() + " !");
        }
        sb.append(getI18n().getMsg("reversed", cpf.getLngDef().getIid()));
        sb.append(" #" + revd.getDbOr() + "-" + revd.getIid());
        pEnt.setDscr(sb.toString());
        this.orm.insIdLn(pRvs, vs, pEnt);
        sb.delete(0, sb.length());
        if (revd.getDscr() != null) {
          sb.append(revd.getDscr() + " !");
        }
        sb.append(getI18n().getMsg("reversing", cpf.getLngDef().getIid()));
        sb.append(" #" + pEnt.getDbOr() + "-" + pEnt.getIid());
        revd.setDscr(sb.toString());
        revd.setRvId(pEnt.getIid());
        String[] upFds = new String[] {"rvId", "dscr", "ver"};
        Arrays.sort(upFds);
        vs.put("upFds", upFds);
        this.orm.update(pRvs, vs, revd); vs.clear();
        this.srWrhEnr.revMove(pRvs, pEnt);
        pRvs.put("msgSuc", "reverse_ok");
      } else {
        this.orm.insIdLn(pRvs, vs, pEnt);
        this.srWrhEnr.move(pRvs, pEnt, pEnt.getWpFr(), pEnt.getWpTo());
        pRvs.put("msgSuc", "insert_ok");
      }
      UvdVar uvs = (UvdVar) pRvs.get("uvs");
      uvs.setOwnr(pEnt.getOwnr());
      return null;
    } else {
      throw new ExcCode(ExcCode.SPAM, "Attempt to update move item line!");
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
   * <p>Getter for srWrhEnr.</p>
   * @return ISrWrhEnr
   **/
  public final ISrWrhEnr getSrWrhEnr() {
    return this.srWrhEnr;
  }

  /**
   * <p>Setter for srWrhEnr.</p>
   * @param pSrWrhEnr reference
   **/
  public final void setSrWrhEnr(final ISrWrhEnr pSrWrhEnr) {
    this.srWrhEnr = pSrWrhEnr;
  }

  /**
   * <p>Getter for i18n.</p>
   * @return II18n
   **/
  public final II18n getI18n() {
    return this.i18n;
  }

  /**
   * <p>Setter for i18n.</p>
   * @param pI18n reference
   **/
  public final void setI18n(final II18n pI18n) {
    this.i18n = pI18n;
  }
}
