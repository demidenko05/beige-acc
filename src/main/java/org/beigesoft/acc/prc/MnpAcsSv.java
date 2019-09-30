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
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.srv.II18n;
import org.beigesoft.acc.mdl.EAccTy;
import org.beigesoft.acc.mdl.ENrBlTy;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.MnpAcs;

/**
 * <p>Service that saves draw material for manufacturing process into DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class MnpAcsSv<RS> implements IPrcEnt<MnpAcs, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>I18N service.</p>
   */
  private II18n i18n;

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Process that pertieves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final MnpAcs process(final Map<String, Object> pRvs, final MnpAcs pEnt,
    final IReqDt pRqDt) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    this.orm.refrEnt(pRvs, vs, pEnt.getOwnr());
    long owVrWs = Long.parseLong(pRqDt.getParam("owVr"));
    if (owVrWs != pEnt.getOwnr().getVer()) {
      throw new ExcCode(IOrm.DRTREAD, "dirty_read");
    }
    if (pEnt.getRvId() != null) {
      MnpAcs revd = new MnpAcs();
      revd.setIid(pEnt.getRvId());
      this.orm.refrEnt(pRvs, vs, revd);
      pEnt.setDbOr(this.orm.getDbId());
      pEnt.setAcc(revd.getAcc());
      pEnt.setSaId(revd.getSaId());
      pEnt.setSaNm(revd.getSaNm());
      pEnt.setSaTy(revd.getSaTy());
      pEnt.setTot(revd.getTot().negate());
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
      vs.put("ndFds", upFds);
      this.orm.update(pRvs, vs, revd); vs.clear();
      pRvs.put("msgSuc", "reverse_ok");
    } else {
      String[] ndfAc = new String[] {"saTy", "typ", "blTy"};
      Arrays.sort(ndfAc);
      vs.put("AcntndFds", ndfAc);
      this.orm.refrEnt(pRvs, vs, pEnt.getAcc()); vs.clear();
      if (pEnt.getSaId() == null || pEnt.getAcc().getSaTy() != 1000
        || pEnt.getAcc().getTyp() != EAccTy.ASSET
          || pEnt.getAcc().getBlTy() != ENrBlTy.DEBIT) {
        throw new ExcCode(ExcCode.WRPR, "account_must_be_asset_exp");
      }
      pEnt.setSaTy(1000);
      if (pEnt.getIsNew()) {
        this.orm.insIdLn(pRvs, vs, pEnt);
        pRvs.put("msgSuc", "insert_ok");
      } else {
        this.orm.update(pRvs, vs, pEnt);
        pRvs.put("msgSuc", "update_ok");
      }
    }
   String qu = "select sum(TOT) as TOT from MNPACS where RVID is null and OWNR="
      + pEnt.getOwnr().getIid() + ";";
    Double tot = this.rdb.evDouble(qu, "TOT");
    if (tot == null) {
      tot = 0.0;
    }
    AcStg as = (AcStg) pRvs.get("astg");
    pEnt.getOwnr().setAdCs(BigDecimal.valueOf(tot)
      .setScale(as.getCsDp(), as.getRndm()));
    pEnt.getOwnr().setTot(pEnt.getOwnr().getMaCs().add(pEnt.getOwnr()
      .getAdCs()).setScale(as.getCsDp(), as.getRndm()));
    pEnt.getOwnr().setToLf(pEnt.getOwnr().getTot());
    pEnt.getOwnr().setPri(pEnt.getOwnr().getTot()
      .divide(pEnt.getOwnr().getQuan(), as.getCsDp(), as.getRndm()));
    String[] upFds = new String[] {"adCs", "tot", "pri", "toLf", "ver"};
    Arrays.sort(upFds);
    vs.put("ndFds", upFds);
    getOrm().update(pRvs, vs, pEnt.getOwnr()); vs.clear();
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
