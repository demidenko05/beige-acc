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

package org.beigesoft.ws.mdlp;

import java.util.List;
import java.util.Date;

import org.beigesoft.acc.mdlp.Srv;
import org.beigesoft.ws.mdlb.ACuOrLn;

/**
 * <p>
 * Model of Customer Order Srv line.
 * </p>
 *
 * @author Yury Demidenko
 */
public class CuOrSrLn extends ACuOrLn {

  /**
   * <p>Srv, not null.</p>
   **/
  private Srv srv;

  /**
   * <p>Item taxes for item basis non-aggregate method.</p>
   **/
  private List<CuOrSrTxLn> itTxs;

  /**
   * <p>Nullable, booking from date1 (include) for bookable srv only.</p>
   **/
  private Date dt1;

  /**
   * <p>Nullable, booking till date2 (exclude) for bookable srv only.</p>
   **/
  private Date dt2;

  //Simple getters and setters:
  /**
   * <p>Getter for srv.</p>
   * @return Srv
   **/
  public final Srv getSrv() {
    return this.srv;
  }

  /**
   * <p>Setter for srv.</p>
   * @param pSrv reference
   **/
  public final void setSrv(final Srv pSrv) {
    this.srv = pSrv;
  }

  /**
   * <p>Getter for itTxs.</p>
   * @return List<CuOrSrTxLn>
   **/
  public final List<CuOrSrTxLn> getItTxs() {
    return this.itTxs;
  }

  /**
   * <p>Setter for itTxs.</p>
   * @param pItTxs reference
   **/
  public final void setItTxs(final List<CuOrSrTxLn> pItTxs) {
    this.itTxs = pItTxs;
  }

  /**
   * <p>Getter for dt1.</p>
   * @return Date
   **/
  public final Date getDt1() {
    return this.dt1;
  }

  /**
   * <p>Setter for dt1.</p>
   * @param pDt1 reference
   **/
  public final void setDt1(final Date pDt1) {
    this.dt1 = pDt1;
  }

  /**
   * <p>Getter for dt2.</p>
   * @return Date
   **/
  public final Date getDt2() {
    return this.dt2;
  }

  /**
   * <p>Setter for dt2.</p>
   * @param pDt2 reference
   **/
  public final void setDt2(final Date pDt2) {
    this.dt2 = pDt2;
  }
}
