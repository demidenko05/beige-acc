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

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;
import java.math.BigDecimal;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdl.IRecSet;
import org.beigesoft.hnd.IHnTrRlBk;
import org.beigesoft.srv.II18n;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.acc.mdlb.IEntrSrc;
import org.beigesoft.acc.mdlb.IDoci;
import org.beigesoft.acc.mdlp.Entr;
import org.beigesoft.acc.mdlp.EnrSrc;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.Acnt;

/**
 * <p>Service that makes, reverses, retrieves entries for given document.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class SrEntr<RS> implements ISrEntr {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>RDB service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>I18N service.</p>
   */
  private II18n i18n;

  /**
   * <p>Balance service.</p>
   **/
  private ISrBlnc srBlnc;

  //Cached data:

  /**
   * <p>Entries queries.</p>
   */
  private Map<String, String> entrQus = new HashMap<String, String>();

  /**
   * <p>Entries sources.</p>
   */
  private List<EnrSrc> entrSrcs;

  /**
   * <p>Handles settings changed, i.e. clears cached data.</p>
   * @param pRvs Request scoped variables
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void hndStgCng(
    final Map<String, Object> pRvs) throws Exception {
    this.entrSrcs = null;
  }

  /**
   * <p>Makes entries for given document and updates its status (mdEnr).
   * Document must be inserted.</p>
   * @param pRvs Request scoped variables, may has DOCFDSUPD - list document
   *  fields to update
   * @param pDoc source document
   * @throws Exception - an exception
   **/
  @Override
  public final void mkEntrs(final Map<String, Object> pRvs,
    final IDoci pDoc) throws Exception {
    if (pDoc.getMdEnr()) {
      throw new ExcCode(ExcCode.SPAM, "Trying to account accounted!");
    }
    AcStg as = (AcStg) pRvs.get("astg");
    Map<String, Object> vs = new HashMap<String, Object>();
    if (this.entrSrcs == null) {
      synchronized (this) {
        if (this.entrSrcs == null) {
          this.entrSrcs = this.orm.retLst(pRvs, vs, EnrSrc.class);
        }
      }
    }
    StringBuffer sb = new StringBuffer();
    boolean isFst = true;
    for (EnrSrc ensr : this.entrSrcs) {
      if (ensr.getUsed() && ensr.getSrTy().equals(pDoc.cnsTy())) {
        String qu = lazEntrQu(ensr.getQuFl());
        String wheDocId = ensr.getSrIdNm() + "=" + pDoc.getIid();
        if (qu.contains(":WHEAD")) {
          qu = qu.replace(":WHEAD", "and " + wheDocId);
        } else if (qu.contains(":WHE")) {
          qu = qu.replace(":WHE", "where " + wheDocId);
        }
        if (isFst) {
          isFst = false;
        } else {
          sb.append("\nunion all\n");
        }
        sb.append(qu);
      }
    }
    if (isFst) {
      throw new ExcCode(ExcCode.WRPR, "doc_entr_src_no_set");
    }
    String qu = sb.toString().trim() + ";";
    @SuppressWarnings("unchecked")
    Set<IHnTrRlBk> hnsTrRlBk = (Set<IHnTrRlBk>) pRvs.get(IHnTrRlBk.HNSTRRLBK);
    if (hnsTrRlBk == null) {
      hnsTrRlBk = new HashSet<IHnTrRlBk>();
      pRvs.put(IHnTrRlBk.HNSTRRLBK, hnsTrRlBk);
    }
    hnsTrRlBk.add(this.srBlnc);
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat
      .MEDIUM, DateFormat.SHORT, new Locale(cpf.getLngDef().getIid()));
    String dcDscr;
    if (pDoc.getDscr() != null) {
      dcDscr = pDoc.getDscr();
    } else {
      dcDscr = "";
    }
    IRecSet<RS> rs = null;
    try {
      rs = getRdb().retRs(qu);
      long dt = pDoc.getDat().getTime();
      if (rs.first()) {
        do {
          Entr entr = new Entr();
          entr.setDbOr(this.orm.getDbId());
          entr.setSrId(pDoc.getIid());
          entr.setSrTy(pDoc.cnsTy());
          entr.setDat(new Date(dt++));
          String acDbs = rs.getStr("ACDB");
          if (acDbs !=  null) {
            Acnt acDb = new Acnt();
            acDb.setIid(acDbs);
            entr.setAcDb(acDb);
            entr.setSadNm(rs.getStr("SADNM"));
            entr.setSadId(rs.getLong("SADID"));
            entr.setSadTy(rs.getInt("SADTY"));
            Double debt = rs.getDouble("DEBT");
            entr.setDebt(BigDecimal.valueOf(debt)
              .setScale(as.getCsDp(), as.getRndm()));
          }
          String acCrs = rs.getStr("ACCR");
          if (acCrs !=  null) {
            Acnt acCr = new Acnt();
            acCr.setIid(acCrs);
            entr.setAcCr(acCr);
            entr.setSacNm(rs.getStr("SACNM"));
            entr.setSacId(rs.getLong("SACID"));
            entr.setSacTy(rs.getInt("SACTY"));
            Double cred = rs.getDouble("CRED");
            entr.setCred(BigDecimal.valueOf(cred)
              .setScale(as.getCsDp(), as.getRndm()));
          }
          entr.setDscr(getI18n().getMsg(pDoc.getClass().getSimpleName() + "sht",
        cpf.getLngDef().getIid()) + " #" + pDoc.getDbOr() + "-" + pDoc.getIid()
      + ", " + dateFormat.format(pDoc.getDat()) + ". " + dcDscr);
          getOrm().insIdLn(pRvs, vs, entr);
          getSrBlnc().hndNewEntr(pRvs, entr.getDat());
        } while (rs.next());
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
    }
    pDoc.setMdEnr(true);
    if (pRvs.get(DOCFDSUPD) != null) {
      vs.put("ndFds", pRvs.get(DOCFDSUPD));
    }
    getOrm().update(pRvs, vs, pDoc);
  }

  /**
   * <p>Reverses entries for given document.</p>
   * @param pRvs Request scoped variables
   * @param pRvng reversing document
   * @param pRved reversed document
   * @throws Exception - an exception
   **/
  @Override
  public final void revEntrs(final Map<String, Object> pRvs, final IDoci pRvng,
    final IDoci pRved) throws Exception {
    if (pRved.getRvId() != null) {
      throw new ExcCode(ExcCode.SPAM, "Attempt to reverse reversed!");
    }
    if (!pRved.getMdEnr()) {
      throw new ExcCode(ExcCode.SPAM, "Attempt to reverse non-accounted!");
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    List<Entr> entrs = this.orm.retLstCnd(pRvs, vs, Entr.class, "where SRID="
      + pRved.getIid() + " and SRTY=" + pRved.cnsTy());
    @SuppressWarnings("unchecked")
    Set<IHnTrRlBk> hnsTrRlBk = (Set<IHnTrRlBk>) pRvs.get(IHnTrRlBk.HNSTRRLBK);
    if (hnsTrRlBk == null) {
      hnsTrRlBk = new HashSet<IHnTrRlBk>();
      pRvs.put(IHnTrRlBk.HNSTRRLBK, hnsTrRlBk);
    }
    hnsTrRlBk.add(this.srBlnc);
    pRvng.setDat(pRved.getDat());
    pRvng.setTot(pRved.getTot().negate());
    pRvng.setMdEnr(true);
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat
      .MEDIUM, DateFormat.SHORT, new Locale(cpf.getLngDef().getIid()));
    StringBuffer sb = new StringBuffer();
    if (pRvng.getDscr() != null) {
      sb.append(pRvng.getDscr() + " !");
    }
    sb.append(getI18n().getMsg("reversed", cpf.getLngDef().getIid()));
    sb.append(" #" + pRved.getDbOr() + "-" + pRved.getIid());
    pRvng.setDscr(sb.toString());
    if (pRvng.getIsNew()) {
      getOrm().insIdLn(pRvs, vs, pRvng);
    } else {
      getOrm().update(pRvs, vs, pRvng);
    }
    for (Entr revd : entrs) {
      Entr revg = new Entr();
      revg.setDbOr(this.orm.getDbId());
      revg.setSrId(pRvng.getIid());
      revg.setSrTy(pRvng.cnsTy());
      mkReving(pRvs, revg, revd, pRvng, dateFormat);
      this.orm.insIdLn(pRvs, vs, revg);
      getSrBlnc().hndNewEntr(pRvs, revg.getDat());
      mkReved(pRvs, revg, revd);
      this.orm.update(pRvs, vs, revd);
      getSrBlnc().hndNewEntr(pRvs, revd.getDat());
    }
    sb.delete(0, sb.length());
    if (pRved.getDscr() != null) {
      sb.append(pRved.getDscr() + " !");
    }
    sb.append(getI18n().getMsg("reversing", cpf.getLngDef().getIid()));
    sb.append(" #" + pRvng.getDbOr() + "-" + pRvng.getIid());
    pRved.setDscr(sb.toString());
    pRved.setRvId(pRved.getIid());
    getOrm().update(pRvs, vs, pRved);
  }

  /**
   * <p>Retrieves entries for given document.</p>
   * @param pRvs Request scoped variables
   * @param pSrc source document
   * @return entries
   * @throws Exception - an exception
   **/
  @Override
  public final List<Entr> retEntrs(final Map<String, Object> pRvs,
    final IEntrSrc pSrc) throws Exception {
    Long iid;
    if (pSrc.getIdOr() != null) {
      iid = pSrc.getIdOr();
    } else {
      iid = pSrc.getIid();
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    return this.orm.retLstCnd(pRvs, vs, Entr.class, "where SRID=" + iid
      + " and SRTY=" + pSrc.cnsTy());
  }

  //Utils:
  /**
   * <p>Makes reversing entry.</p>
   * @param pRvs request scoped vars
   * @param pRving reversing entry
   * @param pRved reversed entry
   * @param pSrc document
   * @param pDtFrm date format
   **/
  public final void mkReving(final Map<String, Object> pRvs, final Entr pRving,
    final Entr pRved, final IEntrSrc pSrc, final DateFormat pDtFrm) {
    pRving.setRvId(pRved.getIid());
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
    StringBuffer sb = new StringBuffer();
    sb.append(getI18n().getMsg(pSrc.getClass().getSimpleName() + "sht",
      cpf.getLngDef().getIid()) + " #" + pSrc.getDbOr() + "-" + pSrc
        .getIid() + ", " + pDtFrm.format(pSrc.getDat()));
    if (pSrc.getDscr() != null) {
      sb.append(", " + pSrc.getDscr());
    }
    sb.append(" ," + getI18n().getMsg("reversed", cpf.getLngDef().getIid()));
    sb.append(" #" + pRved.getDbOr() + "-" + pRved.getIid());
    pRving.setDscr(sb.toString() + "!");
  }

  /**
   * <p>Makes reversed entry.</p>
   * @param pRvs request scoped vars
   * @param pRving reversing entry
   * @param pRved reversed entry
   **/
  public final void mkReved(final Map<String, Object> pRvs,
    final Entr pRving, final Entr pRved) {
    pRved.setRvId(pRving.getIid());
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    StringBuffer sb = new StringBuffer();
    if (pRved.getDscr() != null) {
      sb.append(pRved.getDscr() + " !");
    }
    sb.append(getI18n().getMsg("reversing", cpf.getLngDef().getIid()));
    sb.append(" #" + pRving.getDbOr() + "-" + pRving.getIid());
    pRved.setDscr(sb.toString());
  }

  /**
   * <p>Lazy gets SQL query.</p>
   * @param pFlNm file name
   * @return SQL query
   * @throws IOException - IO exception
   **/
  public final String lazEntrQu(final String pFlNm) throws IOException {
    String rz = this.entrQus.get(pFlNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.entrQus.get(pFlNm);
        if (rz == null) {
          rz = loadStr("/acc/entr/" + pFlNm + ".sql");
          this.entrQus.put(pFlNm, rz);
        }
      }
    }
    return rz;
  }

  /**
   * <p>Loads SQL query.</p>
   * @param pFlNm file name
   * @return SQL query, not null
   * @throws IOException - IO exception
   **/
  public final String loadStr(final String pFlNm) throws IOException {
    URL urlFile = SrEntr.class.getResource(pFlNm);
    if (urlFile != null) {
      InputStream is = null;
      try {
        is = SrEntr.class.getResourceAsStream(pFlNm);
        byte[] bArray = new byte[is.available()];
        is.read(bArray, 0, is.available());
        return new String(bArray, "UTF-8");
      } finally {
        if (is != null) {
          is.close();
        }
      }
    }
    throw new RuntimeException("File not found: " + pFlNm);
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
