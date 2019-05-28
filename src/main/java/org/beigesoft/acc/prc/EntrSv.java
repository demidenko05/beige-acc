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
import java.util.Calendar;
import java.util.Locale;
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.srv.II18n;
import org.beigesoft.acc.mdlp.Entr;
import org.beigesoft.acc.mdlp.InEntr;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Sacnt;
import org.beigesoft.acc.srv.ISrBlnc;

/**
 * <p>Service that saves accounting entry into DB.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class EntrSv<RS> implements IPrcEnt<Entr, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>RDB service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Balance service.</p>
   **/
  private ISrBlnc srBlnc;

  /**
   * <p>I18N service.</p>
   */
  private II18n i18n;

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
  public final Entr process(final Map<String, Object> pRvs, final Entr pEnt,
    final IReqDt pRqDt) throws Exception {
    if (!pEnt.getIsNew()) {
      throw new ExcCode(ExcCode.WRPR, "edit_not_allowed");
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    InEntr doc = new InEntr();
    doc.setIid(pEnt.getSrId());
    this.orm.refrEnt(pRvs, vs, doc);
    if (!doc.getDbOr().equals(this.orm.getDbId())) {
      throw new ExcCode(ExcCode.WRPR, "can_not_change_foreign_src");
    }
    long owVrWs = Long.parseLong(pRqDt.getParam("owVr"));
    if (owVrWs != doc.getVer()) {
      throw new ExcCode(IOrm.DRTREAD, "dirty_read");
    }
    if (!pEnt.getDbOr().equals(this.orm.getDbId())) {
      throw new ExcCode(ExcCode.WRPR, "can_not_change_foreign_src");
    }
    AcStg astg = (AcStg) pRvs.get("astg");
    pEnt.setSrTy(doc.cnsTy());
    pEnt.setSrDbOr(doc.getDbOr());
    if (pEnt.getRvId() != null) {
      Entr revd = new Entr();
      revd.setIid(pEnt.getRvId());
      this.orm.refrEnt(pRvs, vs, revd);
      if (!revd.getSrId().equals(pEnt.getSrId())) {
        throw new ExcCode(ExcCode.WRPR, "different_source");
      }
      if (revd.getRvId() != null) {
        throw new ExcCode(ExcCode.WRPR, "can_not_reverse_reversed");
      }
      mkRevers(pRvs, pEnt, revd);
      this.orm.insIdLn(pRvs, vs, pEnt);
      revd.setRvId(pEnt.getIid());
      this.orm.update(pRvs, vs, revd);
      pRvs.put("msgSuc", "reverse_ok");
    } else {
      if (pEnt.getDebt().compareTo(BigDecimal.ZERO) == 0) {
        throw new ExcCode(ExcCode.WRPR, "amount_eq_zero");
      }
      Calendar calCuMh = Calendar.getInstance(new Locale("en", "US"));
      calCuMh.setTime(astg.getMnth());
      calCuMh.set(Calendar.DAY_OF_MONTH, 1);
      calCuMh.set(Calendar.HOUR_OF_DAY, 0);
      calCuMh.set(Calendar.MINUTE, 0);
      calCuMh.set(Calendar.SECOND, 0);
      calCuMh.set(Calendar.MILLISECOND, 0);
      Calendar calDoc = Calendar.getInstance(new Locale("en", "US"));
      calDoc.setTime(pEnt.getDat());
      calDoc.set(Calendar.DAY_OF_MONTH, 1);
      calDoc.set(Calendar.HOUR_OF_DAY, 0);
      calDoc.set(Calendar.MINUTE, 0);
      calDoc.set(Calendar.SECOND, 0);
      calDoc.set(Calendar.MILLISECOND, 0);
      if (calCuMh.getTime().getTime() != calDoc.getTime().getTime()) {
        throw new ExcCode(ExcCode.WRPR, "wrong_acperiod");
      }
      if (pEnt.getAcDb() == null && pEnt.getAcCr() == null) {
        throw new ExcCode(ExcCode.WRPR, "account_is_null");
      }
      if (pEnt.getAcDb() == null) {
        pEnt.setDebt(BigDecimal.ZERO);
      } else {
        getOrm().refrEnt(pRvs, vs, pEnt.getAcDb());
        if (pEnt.getAcDb().getStyp() != null) {
          if (pEnt.getSadId() == null) {
            throw new ExcCode(ExcCode.WRPR, "select_subaccount");
          } else {
            Sacnt sa = getOrm().retEntCnd(pRvs, vs, Sacnt.class, "OWNR='"
              + pEnt.getAcDb().getIid() + "' and SAID=" + pEnt.getSadId());
            if (sa == null) {
              throw new ExcCode(ExcCode.WRPR, "wrong_subaccount");
            }
            pEnt.setSadNm(sa.getSaNm());
            pEnt.setSadTy(pEnt.getAcDb().getStyp());
          }
        }
      }
      if (pEnt.getAcCr() != null) {
        pEnt.setCred(pEnt.getDebt());
        getOrm().refrEnt(pRvs, vs, pEnt.getAcCr());
        if (pEnt.getAcCr().getStyp() != null) {
          if (pEnt.getSacId() == null) {
            throw new ExcCode(ExcCode.WRPR, "select_subaccount");
          } else {
            Sacnt sa = getOrm().retEntCnd(pRvs, vs, Sacnt.class, "OWNR='"
              + pEnt.getAcCr().getIid() + "' and SAID=" + pEnt.getSacId());
            if (sa == null) {
              throw new ExcCode(ExcCode.WRPR, "wrong_subaccount");
            }
            pEnt.setSacNm(sa.getSaNm());
            pEnt.setSacTy(pEnt.getAcCr().getStyp());
          }
        }
      }
      this.orm.insIdLn(pRvs, vs, pEnt);
      pRvs.put("msgSuc", "insert_ok");
    }
    String qu = "select sum(DEBT) as DEBT, sum(CRED) as CRED from ENTR where"
      + " RVID is null and SRTY=" + pEnt.getSrTy() + " and SRID="
        + doc.getIid() + ";";
    String[] cols = new String[]{"DEBT", "CRED"};
    Double[] tots = getRdb().evDoubles(qu, cols);
    if (tots[0] == null) {
      tots[0] = 0.0;
    }
    if (tots[1] == null) {
      tots[1] = 0.0;
    }
    doc.setDebt(BigDecimal.valueOf(tots[0])
      .setScale(astg.getCsDp(), astg.getRndm()));
    doc.setCred(BigDecimal.valueOf(tots[1])
      .setScale(astg.getCsDp(), astg.getRndm()));
    getOrm().update(pRvs, vs, doc);
    getSrBlnc().hndNewEntr(pRvs, pEnt.getDat());
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setOwnr(doc);
    return null;
  }

  //Utils:
  /**
   * <p>Makes reversing/reversed entry.</p>
   * @param pRvs request scoped vars
   * @param pRving reversing entry
   * @param pRved reversed entry
   **/
  public final void mkRevers(final Map<String, Object> pRvs,
    final Entr pRving, final Entr pRved) {
    pRving.setDat(pRved.getDat());
    pRving.setDebt(pRved.getDebt().negate());
    pRving.setCred(pRved.getCred().negate());
    pRving.setAcDb(pRved.getAcDb());
    pRving.setSadId(pRved.getSadId());
    pRving.setSadTy(pRved.getSadTy());
    pRving.setSadNm(pRved.getSadNm());
    pRving.setAcCr(pRved.getAcCr());
    pRving.setSacId(pRved.getSacId());
    pRving.setSacTy(pRved.getSacTy());
    pRving.setSacNm(pRved.getSacNm());
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    StringBuffer dscing = new StringBuffer();
    if (pRving.getDscr() != null) {
      dscing.append(pRving.getDscr());
    }
    dscing.append(getI18n().getMsg("reversed", cpf.getLngDef().getIid()));
    dscing.append(" #" + pRved.getDbOr() + "-" + pRved.getIid());
    pRving.setDscr(dscing.toString() + " !");
    StringBuffer dsced = new StringBuffer();
    if (pRved.getDscr() != null) {
      dsced.append(pRved.getDscr() + " !");
    }
    dsced.append(getI18n().getMsg("reversing", cpf.getLngDef().getIid()));
    dsced.append(" #" + pRving.getDbOr() + "-" + pRving.getIid());
    pRved.setDscr(dsced.toString());
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
