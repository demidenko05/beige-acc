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

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.log.ILog;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.acc.mdlp.AcStg;

/**
 * <p>Accounting settings service.</p>
 *
 * @author Yury Demidenko
 */
public class SrAcStg implements ISrAcStg {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Cached settings.</p>
   **/
  private AcStg acStg;

  /**
   * <p>Retrieves/gets Accounting settings.</p>
   * @param pRvs Request scoped variables
   * @return Accounting settings
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized AcStg lazAcStg(
    final Map<String, Object> pRvs) throws Exception {
    if (this.acStg == null) {
      Map<String, Object> vs = new HashMap<String, Object>();
      AcStg astg = new AcStg();
      astg.setIid(1L);
      this.orm.refrEnt(pRvs, vs, astg);
      if (astg.getIid() == null) {
        throw new ExcCode(ExcCode.WRCN, "There_is_no_accounting_settings");
      }
      this.acStg = astg;
      pRvs.put("astg", this.acStg);
    } else if (pRvs.get("astg") == null) {
      pRvs.put("astg", this.acStg);
    }
    return this.acStg;
  }

  /**
   * <p>Saves acc-settings into DB.</p>
   * @param pRvs Request scoped variables
   * @param pAcStg entity
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void saveAcStg(final Map<String, Object> pRvs,
    final AcStg pAcStg) throws Exception {
    if (pAcStg.getCsDp() < 0 || pAcStg.getCsDp() > 4) {
      throw new ExcCode(ExcCode.WRPR, "precision_must_be_from_0_to_4");
    }
    if (pAcStg.getPrDp() < 0 || pAcStg.getPrDp() > 4) {
      throw new ExcCode(ExcCode.WRPR, "precision_must_be_from_0_to_4");
    }
    if (pAcStg.getQuDp() < 0 || pAcStg.getQuDp() > 4) {
      throw new ExcCode(ExcCode.WRPR, "precision_must_be_from_0_to_4");
    }
    if (pAcStg.getRpDp() < 0 || pAcStg.getRpDp() > 4) {
      throw new ExcCode(ExcCode.WRPR, "precision_must_be_from_0_to_4");
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    this.orm.update(pRvs, vs, pAcStg);
    this.acStg = pAcStg;
    pRvs.put("astg", this.acStg);
  }

  /**
   * <p>Getter for acStg for avoiding starting new transaction.</p>
   * @return AcStg
   **/
  @Override
  public final synchronized AcStg getAcStg() {
    return this.acStg;
  }

  /**
   * <p>Handle rollback, e.g. clears cached data.</p>
   * @param pRvs Request scoped variables
   * @throws Exception - an exception
   **/
  @Override
  public final synchronized void hndRlBk(
    final Map<String, Object> pRvs) throws Exception {
    this.acStg = null;
    pRvs.remove("astg");
  }

  //Simple getters and setters:
  /**
   * <p>Getter for log.</p>
   * @return ILog
   **/
  public final ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final void setLog(final ILog pLog) {
    this.log = pLog;
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
}
