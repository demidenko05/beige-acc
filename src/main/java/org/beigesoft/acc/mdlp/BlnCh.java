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

package org.beigesoft.acc.mdlp;

import java.util.Date;

import org.beigesoft.mdl.EPeriod;
import org.beigesoft.mdlp.AIdLn;

/**
 * <p>Model for "check dirty of Blnc for all accounts" method.
 * There is only record in database with ID=1L.</p>
 *
 * @author Yury Demidenko
 */
public class BlnCh extends AIdLn {

  /**
   * <p>Initialized date 01/01/1975.</p>
   **/
  public static final Long INITDT = 157766400000L;

  /**
   * <p>Date of start to store balance periodically, this is
   * the first month of the first accounting entry, it maintenance
   * automatically. It's also flag if there is any non-reversed entry.</p>
   **/
  private Date stDt = new Date(INITDT);

  /**
   * <p>Not Null, date of current calculated and stored balances
   * for all accounts e.g. 1 Feb, it is dirty when it's less than
   * leDt. After all recalculation:
   * leDt = cuDt.
   * Initialized 01/01/1975.</p>
   **/
  private Date cuDt = new Date(INITDT);

  /**
   * <p>Not Null, the least date of last accounting entry that is made
   * after made of cuDt e.g. 22 Jan 10:56PM.
   * For improving performance every document when it's accounted
   * its first (dirty check for all accounts) entry change
   * leDt to its date of account if it less.
   * Initialized 01/01/1975.</p>
   **/
  private Date leDt = new Date(INITDT);

  /**
   * <p>Balance store period, not null, EPeriod.DAILY/WEEKLY/MONTHLY.
   * If period has been changed (different with acc-settings one) then
   * all Blnc should be deleted.</p>
   **/
  private EPeriod stPr = EPeriod.MONTHLY;

  /**
   * <p>If period has been changed then all Blnc should be deleted.</p>
   **/
  private Boolean prCh = false;

  @Override
  public final String toString() {
    return "cuDt=" + cuDt + ", leDt=" + leDt + ", stDt=" + stDt
      + ", stPr=" + stPr + ", prCh=" + prCh;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for cuDt.</p>
   * @return Date
   **/
  public final Date getCuDt() {
    return this.cuDt;
  }

  /**
   * <p>Setter for cuDt.</p>
   * @param pCuDt reference
   **/
  public final void setCuDt(final Date pCuDt) {
    this.cuDt = pCuDt;
  }

  /**
   * <p>Getter for leDt.</p>
   * @return Date
   **/
  public final Date getLeDt() {
    return this.leDt;
  }

  /**
   * <p>Setter for leDt.</p>
   * @param pLeDt reference
   **/
  public final void setLeDt(final Date pLeDt) {
    this.leDt = pLeDt;
  }

  /**
   * <p>Getter for stDt.</p>
   * @return Date
   **/
  public final Date getStDt() {
    return this.stDt;
  }

  /**
   * <p>Setter for stDt.</p>
   * @param pStDt reference
   **/
  public final void setStDt(final Date pStDt) {
    this.stDt = pStDt;
  }

  /**
   * <p>Getter for stPr.</p>
   * @return EPeriod
   **/
  public final EPeriod getStPr() {
    return this.stPr;
  }

  /**
   * <p>Setter for stPr.</p>
   * @param pStPr reference
   **/
  public final void setStPr(final EPeriod pStPr) {
    this.stPr = pStPr;
  }

  /**
   * <p>Getter for prCh.</p>
   * @return Boolean
   **/
  public final Boolean getPrCh() {
    return this.prCh;
  }

  /**
   * <p>Setter for prCh.</p>
   * @param pPrCh reference
   **/
  public final void setPrCh(final Boolean pPrCh) {
    this.prCh = pPrCh;
  }
}
