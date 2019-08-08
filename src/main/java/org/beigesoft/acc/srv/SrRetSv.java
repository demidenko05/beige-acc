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

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.mdlb.IRet;
import org.beigesoft.acc.mdlb.IInvLn;
import org.beigesoft.acc.mdlp.Itm;

/**
 * <p>Service that saves good return into DB.
 * It's type-safe part (shared code) that is used inside type safe
 * processor-assembly.</p>
 *
 * @author Yury Demidenko
 */
public class SrRetSv {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Entries service.</p>
   **/
  private ISrEntr srEntr;

  /**
   * <p>Base service.</p>
   **/
  private UtlBas utlBas;

  /**
   * <p>Saves entity generic method.</p>
   * @param <T> return type
   * @param <I> invoice type
   * @param <G> return goods line type
   * @param pRvs request scoped vars
   * @param pEnt Entity to process
   * @param pRqDt Request Data
   * @param pRvGdLn reverser good line
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  public final <T extends IRet<I>, I extends AInv, G extends IInvLn<T, Itm>>
    T save(final Map<String, Object> pRvs, final T pEnt, final IReqDt pRqDt,
      final IRvInvLn<T, G> pRvGdLn) throws Exception {
    Map<String, Object> vs = new HashMap<String, Object>();
    if (pEnt.getRvId() != null) {
      @SuppressWarnings("unchecked")
      T revd = (T) pEnt.getClass().newInstance();
      revd.setIid(pEnt.getRvId());
      this.orm.refrEnt(pRvs, vs, revd);
      this.utlBas.chDtForg(pRvs, revd, revd.getDat());
      pEnt.setDbOr(this.orm.getDbId());
      pEnt.setInv(revd.getInv());
      pEnt.setSubt(revd.getSubt().negate());
      pEnt.setSuFc(revd.getSuFc().negate());
      pEnt.setToTx(revd.getToTx().negate());
      pEnt.setTxFc(revd.getTxFc().negate());
      pEnt.setTot(revd.getTot().negate());
      pEnt.setToFc(revd.getToFc().negate());
      this.srEntr.revEntrs(pRvs, pEnt, revd);
      pRvs.put("msgSuc", "reverse_ok");
      //for purchase goods lines it also checks for withdrawals:
      List<G> gdLns = pRvGdLn.retChkLns(pRvs, vs, revd);
      for (G rvdLn : gdLns) {
        @SuppressWarnings("unchecked")
        G rvgLn = (G) rvdLn.getClass().newInstance();
        rvgLn.setOwnr(pEnt);
        rvgLn.setDbOr(this.orm.getDbId());
        rvgLn.setRvId(rvdLn.getIid());
        rvgLn.setQuan(rvdLn.getQuan().negate());
        rvgLn.setSubt(rvdLn.getSubt().negate());
        rvgLn.setSuFc(rvdLn.getSuFc().negate());
        rvgLn.setToTx(rvdLn.getToTx().negate());
        rvgLn.setTxFc(rvdLn.getTxFc().negate());
        rvgLn.setTot(rvdLn.getTot().negate());
        rvgLn.setToFc(rvdLn.getToFc().negate());
        rvgLn.setTdsc(rvdLn.getTdsc());
        //it also sets invoice line,
        //inserts reversing and updates reversed,
        //makes warehouse reversing
        //and draw item reversing
        //It removes line tax lines:
        pRvGdLn.revLns(pRvs, vs, pEnt, rvgLn, rvdLn);
      }
    } else {
      this.utlBas.chDtForg(pRvs, pEnt, pEnt.getDat());
      if (!pEnt.getIsNew()) {
        String[] ndf = new String[] {"inv", "rvId", "mdEnr", "tot"};
        Arrays.sort(ndf);
        vs.put(pEnt.getClass().getSimpleName() + "ndFds", ndf);
        String[] ndfi = new String[] {"rvId", "mdEnr", "tot"};
        Arrays.sort(ndfi);
        vs.put(pEnt.getInv().getClass().getSimpleName() + "ndFds", ndfi);
        T old = this.orm.retEnt(pRvs, vs, pEnt); vs.clear();
        pEnt.setMdEnr(old.getMdEnr());
        if (pEnt.getMdEnr()) {
          throw new ExcCode(ExcCode.SPAM, "Trying to change accounted!");
        }
        if (old.getRvId() != null) {
          throw new ExcCode(ExcCode.SPAM, "Trying to change reversed!");
        }
        if (old.getTot().compareTo(BigDecimal.ZERO) == 1
          && !old.getInv().getIid().equals(pEnt.getInv().getIid())) {
          throw new ExcCode(ExcCode.SPAM, "Trying to change involved invoice!");
        }
        if (old.getInv().getIid().equals(pEnt.getInv().getIid())) {
          pEnt.setInv(old.getInv());
        } else {
          vs.put(pEnt.getInv().getClass().getSimpleName() + "ndFds", ndfi);
          this.orm.refrEnt(pRvs, vs, pEnt.getInv()); vs.clear();
        }
        if (!pEnt.getInv().getMdEnr()) {
          throw new ExcCode(ExcCode.WRPR, "inv_not_accounted");
        }
        if (pEnt.getInv().getRvId() != null) {
          throw new ExcCode(ExcCode.WRPR, "inv_reversed");
        }
        String[] fdDcUpd = new String[] {"dat", "dscr", "mdEnr", "ver", "inv"};
        Arrays.sort(fdDcUpd);
        if ("mkEnr".equals(pRqDt.getParam("acAd"))) {
          if (old.getTot().compareTo(BigDecimal.ZERO) == 0) {
            throw new ExcCode(ExcCode.SPAM, "Attempt to account zero doc!");
          }
          pEnt.setMdEnr(true);
          vs.put("ndFds", fdDcUpd);
          this.orm.update(pRvs, vs, pEnt); vs.clear();
          this.srEntr.mkEntrs(pRvs, pEnt);
          pRvs.put("msgSuc", "account_ok");
        } else {
          vs.put("ndFds", fdDcUpd);
          this.orm.update(pRvs, vs, pEnt); vs.clear();
          pRvs.put("msgSuc", "update_ok");
        }
      } else {
        pRvs.put("msgSuc", "insert_ok");
        this.orm.insIdLn(pRvs, vs, pEnt);
      }
    }
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setEnt(pEnt);
    return pEnt;
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
   * <p>Getter for srEntr.</p>
   * @return ISrEntr
   **/
  public final ISrEntr getSrEntr() {
    return this.srEntr;
  }

  /**
   * <p>Setter for srEntr.</p>
   * @param pSrEntr reference
   **/
  public final void setSrEntr(final ISrEntr pSrEntr) {
    this.srEntr = pSrEntr;
  }

  /**
   * <p>Getter for utlBas.</p>
   * @return UtlBas
   **/
  public final UtlBas getUtlBas() {
    return this.utlBas;
  }

  /**
   * <p>Setter for utlBas.</p>
   * @param pUtlBas reference
   **/
  public final void setUtlBas(final UtlBas pUtlBas) {
    this.utlBas = pUtlBas;
  }
}
