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
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.Date;

import org.beigesoft.log.ILog;
import org.beigesoft.mdl.ColVals;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.ws.mdl.EOrdStas;
import org.beigesoft.ws.mdlp.Buyer;
import org.beigesoft.ws.mdlp.SeItmPlc;
import org.beigesoft.ws.mdlp.ItmPlc;
import org.beigesoft.ws.mdlp.SrvPlc;
import org.beigesoft.ws.mdlp.SeSrvPlc;
import org.beigesoft.ws.mdlp.CuOr;
import org.beigesoft.ws.mdlp.CuOrGdLn;
import org.beigesoft.ws.mdlp.CuOrSrLn;
import org.beigesoft.ws.mdlp.SerBus;
import org.beigesoft.ws.mdlp.SeSerBus;
import org.beigesoft.ws.mdlp.CuOrSe;
import org.beigesoft.ws.mdlp.CuOrSeGdLn;
import org.beigesoft.ws.mdlp.CuOrSeSrLn;

/**
 * <p>It cancels all given buyer's orders.
 * E.g. buyer has not paid online after accepting (booking) orders.
 * It changes item's availability and orders status to given NEW or CANCELED.
 * It implements unbooking item available quantity from
 * several places - from the first place.
 * </p>
 *
 * @param <RS> platform dependent RDBMS recordset
 * @author Yury Demidenko
 */
public class CncOrd<RS> implements ICncOrd {

  /**
   * <p>Logger.</p>
   **/
  private ILog log;

  /**
   * <p>ORM service.</p>
   */
  private IOrm orm;

  /**
   * <p>DB service.</p>
   */
  private IRdb<RS> rdb;

  /**
   * <p>It cancels all given buyer's orders.
   * For example buyer had not paid online after accepting (booking) orders.
   * It changes item's availability and orders status to given NEW or CANCELED.
   * </p>
   * @param pRvs additional request scoped parameters
   * @param pBuyr buyer
   * @param pPurId purchase ID
   * @param pStFr usually BOOKED
   * @param pStTo usually NEW
   * @throws Exception - an exception
   **/
  @Override
  public final void cancel(final Map<String, Object> pRvs,
    final Buyer pBuyr, final Long pPurId,
      final EOrdStas pStFr, final EOrdStas pStTo) throws Exception {
    List<CuOr> ords = null;
    List<CuOrSe> sords = null;
    String tbn = CuOr.class.getSimpleName();
    String wheStBr = "where STAS=" + pStFr.ordinal() + " and BUYR="
      + pBuyr.getIid() + " and PUR=" + pPurId;
    Set<String> ndFlNm = new HashSet<String>();
    ndFlNm.add("itsId");
    ndFlNm.add("itsName");
    pRvs.put("PicPlcndFds", ndFlNm);
    pRvs.put(tbn + "buyerdpLv", 1);
    ords = this.orm.retrieveListWithConditions(pRvs,
      CuOr.class, wheStBr);
    pRvs.remove(tbn + "buyerdpLv");
    for (CuOr co : ords) {
      cancel(pRvs, co, pStTo);
    }
    tbn = CuOrSe.class.getSimpleName();
    pRvs.put("SeSelndFds", new String[] {"dbcr"});
    pRvs.put("DbCrndFds", ndFlNm);
    pRvs.put(tbn + "seldpLv", 3);
    pRvs.put(tbn + "buyerdpLv", 1);
    sords = this.orm.retrieveListWithConditions(pRvs,
      CuOrSe.class, wheStBr);
    pRvs.remove("DbCrndFds");
    for (CuOrSe co : sords) {
      cancel(pRvs, co, pStTo);
    }
  }

  /**
   * <p>It cancels given buyer's order.</p>
   * @param pRvs additional request scoped parameters
   * @param pCuOr order
   * @param pStas NEW or CANCELED
   * @throws Exception - an exception
   **/
  @Override
  public final void cancel(final Map<String, Object> pRvs,
    final CuOr pCuOr, final EOrdStas pStas) throws Exception {
    Set<String> ndFl = new HashSet<String>();
    String tbn = CuOrGdLn.class.getSimpleName();
    ndFl.add("itsId");
    ndFl.add("quant");
    ndFl.add("good");
    pRvs.put(tbn + "ndFds", ndFl);
    pRvs.put(tbn + "gooddpLv", 1);
    List<CuOrGdLn> gds = this.orm.retrieveListWithConditions(
      pRvs, CuOrGdLn.class, "where OWNR=" + pCuOr.getIid());
    pRvs.remove(tbn + "ndFds");
    pRvs.remove(tbn + "gooddpLv");
    ColVals cvsIil = new ColVals();
    cvsIil.getFormula().add("quan");
    for (CuOrGdLn gl : gds) {
      List<ItmPlc> gps = getOrm().retrieveListWithConditions(pRvs,
        ItmPlc.class, "where ALWAY=0 and ITM="
          + gl.getGood().getIid());
      if (gps.size() > 0) {
        gps.get(0)
          .setQuan(gps.get(0).getQuan().add(gl.getQuan()));
        getOrm().update(pRvs, vs, gps.get(0));
        cvsIil.put("ver", new Date().getTime());
        cvsIil.put("quan", "QUAN+" + gl.getQuan());
        this.rdb.executeUpdate("ITLIST", cvsIil,
          "TYP=0 and ITID=" + gps.get(0).getItm().getIid());
      }
    }
    tbn = CuOrSrLn.class.getSimpleName();
    ndFl.remove("good");
    ndFl.add("service");
    ndFl.add("dt1");
    ndFl.add("dt2");
    pRvs.put(tbn + "ndFds", ndFl);
    pRvs.put(tbn + "gooddpLv", 1);
    List<CuOrSrLn> sls = this.orm.retrieveListWithConditions(
      pRvs, CuOrSrLn.class, "where OWNR=" + pCuOr.getIid());
    pRvs.remove(tbn + "ndFds");
    pRvs.remove(tbn + "gooddpLv");
    for (CuOrSrLn sl : sls) {
      if (sl.getDt1() == null) { //non-bookable:
        List<SrvPlc> sps = getOrm().retrieveListWithConditions(pRvs,
          SrvPlc.class, "where ALWAY=0 and ITM=" + sl.getSrv()
            .getIid());
        if (sps.size() > 0) {
          sps.get(0)
            .setQuan(sps.get(0).getQuan().add(sl.getQuan()));
          getOrm().update(pRvs, vs, sps.get(0));
          cvsIil.put("ver", new Date().getTime());
          cvsIil.put("quan", "QUAN+" + sl.getQuan());
          this.rdb.executeUpdate("ITLIST", cvsIil,
            "TYP=1 and ITID=" + sps.get(0).getItm().getIid());
        }
      } else { //bookable:
        List<SerBus> sebs = getOrm().retrieveListWithConditions(pRvs,
      SerBus.class, "where FRE=0 and SRV=" + sl.getSrv().getIid()
+ " and FRTM=" + sl.getDt1().getTime() + " and TITM=" + sl.getDt1().getTime());
        if (sebs.size() == 1) {
          sebs.get(0).setFre(true);
          getOrm().update(pRvs, vs, sebs.get(0));
        } else if (sebs.size() > 1) {
          this.log.error(pRvs, CncOrd.class,
        "Several SERBUS for booked service: " + sl.getSrv().getIid()
      + "/"  + sl.getDt1().getTime() + "/" + sl.getDt1().getTime());
          for (SerBus seb : sebs) {
            seb.setFre(true);
            getOrm().update(pRvs, vs, seb);
          }
        } else {
          this.log.error(pRvs, CncOrd.class,
        "There is no SERBUS for booked service: " + sl.getSrv().getIid()
      + "/"  + sl.getDt1().getTime() + "/" + sl.getDt1().getTime());
        }
      }
    }
    String[] ndFds = new String[] {"itsId", "ver", "stat"};
    pRvs.put("ndFds", ndFds);
    pCuOr.setStas(pStas);
    getOrm().update(pRvs, vs, pCuOr);
    pRvs.remove("ndFds");
  }

  /**
   * <p>It cancels given buyer's S.E.order.</p>
   * @param pRvs additional request scoped parameters
   * @param pCuOr order
   * @param pStas NEW or CANCELED
   * @throws Exception - an exception
   **/
  @Override
  public final void cancel(final Map<String, Object> pRvs,
    final CuOrSe pCuOr, final EOrdStas pStas) throws Exception {
    Set<String> ndFl = new HashSet<String>();
    String tbn = CuOrSeGdLn.class.getSimpleName();
    ndFl.add("itsId");
    ndFl.add("quant");
    ndFl.add("good");
    pRvs.put(tbn + "ndFds", ndFl);
    pRvs.put(tbn + "gooddpLv", 1);
    List<CuOrSeGdLn> gds = this.orm.retrieveListWithConditions(
      pRvs, CuOrSeGdLn.class, "where OWNR=" + pCuOr.getIid());
    pRvs.remove(tbn + "ndFds");
    pRvs.remove(tbn + "gooddpLv");
    ColVals cvsIil = new ColVals();
    cvsIil.getFormula().add("quan");
    for (CuOrSeGdLn gl : gds) {
      List<SeItmPlc> gps = getOrm().retrieveListWithConditions(pRvs,
        SeItmPlc.class, "where ALWAY=0 and ITM=" + gl.getGood()
          .getIid());
      if (gps.size() > 0) {
        gps.get(0)
          .setQuan(gps.get(0).getQuan().add(gl.getQuan()));
        getOrm().update(pRvs, vs, gps.get(0));
        cvsIil.put("ver", new Date().getTime());
        cvsIil.put("quan", "QUAN+" + gl.getQuan());
        this.rdb.executeUpdate("ITLIST", cvsIil,
          "TYP=2 and ITID=" + gps.get(0).getItm().getIid());
      }
    }
    tbn = CuOrSeSrLn.class.getSimpleName();
    ndFl.remove("good");
    ndFl.add("service");
    ndFl.add("dt1");
    ndFl.add("dt2");
    pRvs.put(tbn + "ndFds", ndFl);
    pRvs.put(tbn + "gooddpLv", 1);
    List<CuOrSeSrLn> sls = this.orm.retrieveListWithConditions(
      pRvs, CuOrSeSrLn.class, "where OWNR=" + pCuOr.getIid());
    pRvs.remove(tbn + "ndFds");
    pRvs.remove(tbn + "gooddpLv");
    for (CuOrSeSrLn sl : sls) {
      if (sl.getDt1() == null) { //non-bookable:
        List<SeSrvPlc> sps = getOrm().retrieveListWithConditions(pRvs,
          SeSrvPlc.class, "where ALWAY=0 and ITM=" + sl.getSrv()
            .getIid());
        if (sps.size() > 0) {
          sps.get(0)
            .setQuan(sps.get(0).getQuan().add(sl.getQuan()));
          getOrm().update(pRvs, vs, sps.get(0));
          cvsIil.put("ver", new Date().getTime());
          cvsIil.put("quan", "QUAN+" + sl.getQuan());
          this.rdb.executeUpdate("ITLIST", cvsIil,
            "TYP=3 and ITID=" + sps.get(0).getItm().getIid());
        }
      } else { //bookable:
        List<SeSerBus> sebs = getOrm().retrieveListWithConditions(pRvs,
      SeSerBus.class, "where FRE=0 and SRV=" + sl.getSrv().getIid()
+ " and FRTM=" + sl.getDt1().getTime() + " and TITM=" + sl.getDt1().getTime());
        if (sebs.size() == 1) {
          sebs.get(0).setFre(true);
          getOrm().update(pRvs, vs, sebs.get(0));
        } else if (sebs.size() > 1) {
          this.log.error(pRvs, CncOrd.class,
        "Several SESERBUS for booked SeService: " + sl.getSrv().getIid()
      + "/"  + sl.getDt1().getTime() + "/" + sl.getDt1().getTime());
          for (SeSerBus seb : sebs) {
            seb.setFre(true);
            getOrm().update(pRvs, vs, seb);
          }
        } else {
          this.log.error(pRvs, CncOrd.class,
      "There is no SESERBUS for booked SeService: " + sl.getSrv().getIid()
    + "/"  + sl.getDt1().getTime() + "/" + sl.getDt1().getTime());
        }
      }
    }
    pCuOr.setStas(pStas);
    String[] ndFds = new String[] {"ver", "stat"};
    Arrays.sort(ndFds);
    vs.put("ndFds", ndFds);
    getOrm().update(pRvs, vs, pCuOr);
  }

  //Simple getters and setters:
  /**
   * <p>Geter for log.</p>
   * @return ILog
   **/
  public final ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLogger reference
   **/
  public final void setLog(final ILog pLogger) {
    this.log = pLogger;
  }

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
}
