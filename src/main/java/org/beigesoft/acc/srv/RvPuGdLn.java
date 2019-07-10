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
import java.util.Arrays;
import java.math.BigDecimal;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.hld.HldUvd;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.srv.II18n;
import org.beigesoft.acc.mdlb.APrep;
import org.beigesoft.acc.mdlb.APaym;
import org.beigesoft.acc.mdlp.PaymTo;
import org.beigesoft.acc.mdlp.PrepTo;
import org.beigesoft.acc.mdlp.PurInv;
import org.beigesoft.acc.mdlp.PuInGdLn;

/**
 * <p>Reverser for purchase good invoice line.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class RvPuGdLn<RS> implements IRvInvLn<PurInv, PuInGdLn> {

  /**
   * <p>Database service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Warehouse entries service.</p>
   **/
  private ISrWrhEnr srWrhEnr;

  /**
   * <p>Holder UVD settings, vars.</p>
   */
  private HldUvd hldUvd;

  /**
   * <p>I18N service.</p>
   */
  private II18n i18n;

  /**
   * <p>Retrieves and checks lines for reversing,
   * e.g. for purchase goods lines it checks for withdrawals.</p>
   * @param pRvs Request scoped variables, not null
   * @param pVs Invoker scoped variables, not null
   * @param pEnt reversed invoice, not null
   * @return checked lines
   * @throws Exception - an exception
   **/
  @Override
  public final List<PuInGdLn> retChkLns(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final PurInv pEnt) throws Exception {
    String[] lstFds = this.hldUvd.lazLstFds(PuInGdLn.class);
    String[] ndFds = Arrays.copyOf(lstFds, lstFds.length);
    Arrays.sort(ndFds);
    pVs.put("PuInGdLnndFds", ndFds);
    pVs.put("ItmdpLv", 0);
    pVs.put("TxCtdpLv", 0);
    pVs.put("WrhPldpLv", 0);
    pVs.put("UomdpLv", 0);
    List<PuInGdLn> lst = this.orm.retLstCnd(pRvs, pVs, PuInGdLn.class,
      "where PUINGDLN.RVID is null and OWNR=" + pEnt.getIid()); pVs.clear();
    for (PuInGdLn gl : lst) {
      if (gl.getQuan().compareTo(gl.getItLf()) == 1) {
        throw new ExcCode(ExcCode.WRPR, "where_is_withdraw");
      }
    }
    return lst;
  }

  /**
   * <p>Reverses lines.
   * it also inserts reversing and updates reversed
   * for good it also makes warehouse reversing.
   * It removes line tax lines.</p>
   * @param pRvs Request scoped variables, not null
   * @param pVs Invoker scoped variables, not null
   * @param pEnt reversed invoice, not null
   * @param pRvng reversing line, not null
   * @param pRved reversed line, not null
   * @throws Exception - an exception
   **/
  @Override
  public final void revLns(final Map<String, Object> pRvs,
    final Map<String, Object> pVs, final PurInv pEnt,
      final PuInGdLn pRvng, final PuInGdLn pRved) throws Exception {
    this.rdb.delete("PUINGDTXLN", "OWNR=" + pRved.getIid());
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    pRvng.setWrhp(pRved.getWrhp());
    StringBuffer sb = new StringBuffer();
    if (pRvng.getDscr() != null) {
      sb.append(pRvng.getDscr() + " !");
    }
    sb.append(getI18n().getMsg("reversed", cpf.getLngDef().getIid()));
    sb.append(" #" + pRved.getDbOr() + "-" + pRved.getIid());
    pRvng.setDscr(sb.toString());
    this.orm.insIdLn(pRvs, pVs, pRvng);
    this.srWrhEnr.revLoad(pRvs, pRvng);
    sb.delete(0, sb.length());
    if (pRved.getDscr() != null) {
      sb.append(pRved.getDscr() + " !");
    }
    sb.append(getI18n().getMsg("reversing", cpf.getLngDef().getIid()));
    sb.append(" #" + pRvng.getDbOr() + "-" + pRvng.getIid());
    pRved.setDscr(sb.toString());
    pRved.setRvId(pRvng.getIid());
    pRved.setItLf(BigDecimal.ZERO);
    pRved.setToLf(BigDecimal.ZERO);
    String[] ndFds = new String[] {"dscr", "itLf", "rvId", "toLf", "ver"};
    Arrays.sort(ndFds);
    pVs.put("ndFds", ndFds);
    this.orm.update(pRvs, pVs, pRved); pVs.clear();
  }

  /**
   * <p>Getter for prepayment class.</p>
   * @return Prepayment class
   **/
  @Override
  public final Class<? extends APrep> getPrepCls() {
    return PrepTo.class;
  }

  /**
   * <p>Getter for payment class.</p>
   * @return payment class
   **/
  @Override
  public final Class<? extends APaym<?>> getPaymCls() {
    return PaymTo.class;
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
   * <p>Getter for hldUvd.</p>
   * @return HldUvd
   **/
  public final HldUvd getHldUvd() {
    return this.hldUvd;
  }

  /**
   * <p>Setter for hldUvd.</p>
   * @param pHldUvd reference
   **/
  public final void setHldUvd(final HldUvd pHldUvd) {
    this.hldUvd = pHldUvd;
  }

  /**
   * <p>Geter for rdb.</p>
   * @return IRdb
   **/
  public final IRdb<RS> getRdb() {
    return this.rdb;
  }

  /**
   * <p>Setter for rdb.</p>
   * @param pRdb reference
   **/
  public final void setRdb(
    final IRdb<RS> pRdb) {
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
}
