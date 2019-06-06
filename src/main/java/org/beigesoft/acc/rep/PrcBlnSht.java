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

package org.beigesoft.acc.rep;

import java.util.Map;
import java.util.Date;
import java.io.OutputStream;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.prc.IPrcFl;
import org.beigesoft.srv.ISrvDt;
import org.beigesoft.acc.mdl.BlnSht;

/**
 * <p>Processor that makes balance sheet PDF report.</p>
 *
 * @author Yury Demidenko
 */
public class PrcBlnSht implements IPrcFl {

  /**
   * <p>Balance data retriever.</p>
   **/
  private ISrBlnSht srBlnSht;

  /**
   * <p>Balance reporter to PDF.</p>
   **/
  private IBlnPdf blnPdf;

  /**
   * <p>Date service.</p>
   **/
  private ISrvDt srvDt;

  /**
   * <p>Processes given request.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pSous servlet output stream
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs, final IReqDt pRqDt,
    final OutputStream pSous) throws Exception {
    Date dt = this.srvDt.from8601DateTime(pRqDt.getParam("dt"));
    BlnSht blnc = this.srBlnSht.retBlnc(pRvs, dt);
    this.blnPdf.report(pRvs, blnc, pSous);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srBlnSht.</p>
   * @return ISrBlnSht
   **/
  public final ISrBlnSht getSrBlnSht() {
    return this.srBlnSht;
  }

  /**
   * <p>Setter for srBlnSht.</p>
   * @param pSrBlnSht reference
   **/
  public final void setSrBlnSht(final ISrBlnSht pSrBlnSht) {
    this.srBlnSht = pSrBlnSht;
  }

  /**
   * <p>Getter for blnPdf.</p>
   * @return IBlnPdf
   **/
  public final IBlnPdf getBlnPdf() {
    return this.blnPdf;
  }

  /**
   * <p>Setter for blnPdf.</p>
   * @param pBlnPdf reference
   **/
  public final void setBlnPdf(final IBlnPdf pBlnPdf) {
    this.blnPdf = pBlnPdf;
  }

  /**
   * <p>Getter for srvDt.</p>
   * @return ISrvDt
   **/
  public final ISrvDt getSrvDt() {
    return this.srvDt;
  }

  /**
   * <p>Setter for srvDt.</p>
   * @param pSrvDt reference
   **/
  public final void setSrvDt(final ISrvDt pSrvDt) {
    this.srvDt = pSrvDt;
  }
}
