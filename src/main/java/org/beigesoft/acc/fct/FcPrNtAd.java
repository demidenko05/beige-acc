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

package org.beigesoft.acc.fct;

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.fct.FctBlc;
import org.beigesoft.fct.IFctPrc;
import org.beigesoft.hld.HldNmFilFdSt;
import org.beigesoft.hld.HldNmCnToStXml;
import org.beigesoft.prc.IPrc;
import org.beigesoft.prp.ISetng;
import org.beigesoft.rpl.RpRtrvDbXml;
import org.beigesoft.rpl.RpStorDbXmlSy;
import org.beigesoft.rpl.RplXmlHttps;
import org.beigesoft.rpl.RpEntWriXml;
import org.beigesoft.rpl.RpEntReadXml;
import org.beigesoft.rpl.PsgAft;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.acc.rpl.AccImp;
import org.beigesoft.acc.rpl.AccExp;
import org.beigesoft.acc.rpl.FcRpEnSy;
import org.beigesoft.acc.rpl.FctFltEnt;
import org.beigesoft.ws.prc.RefrLst;
import org.beigesoft.ws.prc.RefrCat;

/**
 * <p>Additional factory to FctPrcNtrAd of processors for admin,
 * secure non-transactional requests.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class FcPrNtAd<RS> implements IFctPrc {

  /**
   * <p>Main factory.</p>
   **/
  private FctBlc<RS> fctBlc;

  //requested data:
  /**
   * <p>Processors map.</p>
   **/
  private final Map<String, IPrc> procs = new HashMap<String, IPrc>();

  /**
   * <p>Get processor in lazy mode (if bean is null then initialize it).</p>
   * @param pRvs request scoped vars
   * @param pPrNm - filler name
   * @return requested processor
   * @throws Exception - an exception
   */
  public final IPrc laz(final Map<String, Object> pRvs,
    final String pPrNm) throws Exception {
    IPrc rz = this.procs.get(pPrNm);
    if (rz == null) {
      synchronized (this) {
        rz = this.procs.get(pPrNm);
        if (rz == null) {
          if (AccImp.class.getSimpleName().equals(pPrNm)) {
            rz = crPuAccImp(pRvs);
          } else if (AccExp.class.getSimpleName().equals(pPrNm)) {
            rz = crPuAccExp(pRvs);
          } else if (RefrCat.class.getSimpleName().equals(pPrNm)) {
            rz = crPuRefrCat(pRvs);
          } else if (RefrLst.class.getSimpleName().equals(pPrNm)) {
            rz = crPuRefrLst(pRvs);
          }
        }
      }
    }
    return rz;
  }

  /**
   * <p>Creates and puts into MF RefrCat.</p>
   * @param pRvs request scoped vars
   * @return RefrCat
   * @throws Exception - an exception
   */
  private RefrCat crPuRefrCat(final Map<String, Object> pRvs) throws Exception {
    RefrCat rz = new RefrCat();
    this.procs.put(RefrCat.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      RefrCat.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF RefrLst.</p>
   * @param pRvs request scoped vars
   * @return RefrLst
   * @throws Exception - an exception
   */
  private RefrLst<RS> crPuRefrLst(
    final Map<String, Object> pRvs) throws Exception {
    RefrLst<RS> rz = new RefrLst<RS>();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    rz.setRdb(rdb);
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    rz.setNumStr(this.fctBlc.lazNumStr(pRvs));
    this.procs.put(RefrLst.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      RefrLst.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF AccExp.</p>
   * @param pRvs request scoped vars
   * @return AccExp
   * @throws Exception - an exception
   */
  private AccExp crPuAccExp(final Map<String, Object> pRvs) throws Exception {
    AccExp rz = new AccExp();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    RpRtrvDbXml<RS> retr = new RpRtrvDbXml<RS>();
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    retr.setRdb(rdb);
    retr.setOrm(this.fctBlc.lazOrm(pRvs));
    retr.setLog(this.fctBlc.lazLogStd(pRvs));
    retr.setReadTi(this.fctBlc.getFctDt().getReadTi());
    RpEntWriXml entWr = new RpEntWriXml();
    entWr.setLog(this.fctBlc.lazLogStd(pRvs));
    entWr.setSetng((ISetng) this.fctBlc.laz(pRvs, FctAcc.STGACIMP));
    entWr.setHldGets(this.fctBlc.lazHldGets(pRvs));
    HldNmCnToStXml hlCnToSt = new HldNmCnToStXml();
    hlCnToSt.setHldFdCls(this.fctBlc.lazHldFldCls(pRvs));
    hlCnToSt.setCnHsIdToStNm(FcCnToStAi.CNHSIDSTACIM);
    entWr.setHldNmFdCn(hlCnToSt);
    entWr.setFctCnvFld(this.fctBlc.lazFctNmCnToSt(pRvs));
    retr.setRpEntWri(entWr);
    rz.setRetr(retr);
    this.procs.put(AccExp.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      AccExp.class.getSimpleName() + " has been created");
    return rz;
  }

  /**
   * <p>Creates and puts into MF AccImp.</p>
   * @param pRvs request scoped vars
   * @return AccImp
   * @throws Exception - an exception
   */
  private AccImp crPuAccImp(final Map<String, Object> pRvs) throws Exception {
    AccImp rz = new AccImp();
    rz.setLog(this.fctBlc.lazLogStd(pRvs));
    rz.setOrm(this.fctBlc.lazOrm(pRvs));
    RplXmlHttps<RS> repl = new RplXmlHttps<RS>();
    repl.setSetng((ISetng) this.fctBlc.laz(pRvs, FctAcc.STGACIMP));
    @SuppressWarnings("unchecked")
    IRdb<RS> rdb = (IRdb<RS>) this.fctBlc.laz(pRvs, IRdb.class.getSimpleName());
    repl.setRdb(rdb);
    repl.setLog(this.fctBlc.lazLogStd(pRvs));
    repl.setUtlXml(this.fctBlc.lazUtlXml(pRvs));
    FctFltEnt<RS> ffle = new FctFltEnt<RS>();
    ffle.setFctBlc(this.fctBlc);
    repl.setFctFltEnts(ffle);
    ISetng setng = (ISetng) this.fctBlc.laz(pRvs, FctAcc.STGACIMP);
    RpStorDbXmlSy<RS> rpStor = new RpStorDbXmlSy<RS>();
    rpStor.setLog(this.fctBlc.lazLogStd(pRvs));
    rpStor.setOrm(this.fctBlc.lazOrm(pRvs));
    rpStor.setUtlXml(this.fctBlc.lazUtlXml(pRvs));
    rpStor.setRdb(rdb);
    rpStor.setSetng(setng);
    rpStor.setWriteTi(this.fctBlc.getFctDt().getWriteTi());
    FcRpEnSy<RS> fesy = new FcRpEnSy<RS>();
    fesy.setFctBlc(this.fctBlc);
    rpStor.setFctEntSy(fesy);
    RpEntReadXml erd = new RpEntReadXml();
    erd.setLog(this.fctBlc.lazLogStd(pRvs));
    erd.setSetng(setng);
    HldNmFilFdSt hldFilFdSt = new HldNmFilFdSt();
    hldFilFdSt.setHldFdCls(this.fctBlc.lazHldFldCls(pRvs));
    hldFilFdSt.setFilHasIdNm(FcFlFdAi.FILHSIDSTDACIM);
    hldFilFdSt.setFilSmpNm(FcFlFdAi.FILSMPSTDACIM);
    hldFilFdSt.setSetng((ISetng) this.fctBlc.laz(pRvs, FctAcc.STGACIMP));
    erd.setHldFilFdNms(hldFilFdSt);
    erd.setUtlXml(this.fctBlc.lazUtlXml(pRvs));
    erd.setFctFilFld(this.fctBlc.lazFctNmFilFd(pRvs));
    rpStor.setRpEntRead(erd);
    repl.setRpStor(rpStor);
    if (this.fctBlc.getFctDt().getIsPstg()) {
      PsgAft<RS> dbAf = new PsgAft<RS>();
      dbAf.setRdb(rdb);
      dbAf.setLog(repl.getLog());
      dbAf.setSetng(repl.getSetng());
      repl.setDbAfter(dbAf);
    }
    rz.setRepl(repl);
    this.procs.put(AccImp.class.getSimpleName(), rz);
    this.fctBlc.lazLogStd(pRvs).info(pRvs, getClass(),
      AccImp.class.getSimpleName() + " has been created");
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
