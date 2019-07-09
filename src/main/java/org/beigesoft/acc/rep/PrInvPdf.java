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
import java.io.OutputStream;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.rdb.IRdb;
import org.beigesoft.prc.IPrcFl;
import org.beigesoft.acc.mdlp.SalInv;

/**
 * <p>Processor that makes invoice PDF report.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrInvPdf<RS> implements IPrcFl {

  /**
   * <p>RDB service.</p>
   **/
  private IRdb<RS> rdb;

  /**
   * <p>Invoice reporter to PDF.</p>
   **/
  private IInvPdf invPdf;

  /**
   * <p>Transaction isolation.</p>
   **/
  private Integer trIsl;

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
    Long inId = Long.valueOf(pRqDt.getParam("inId"));
    SalInv si = new SalInv();
    si.setIid(inId);
    try {
      this.rdb.setAcmt(false);
      this.rdb.setTrIsl(this.trIsl);
      this.rdb.begin();
      this.invPdf.mkRep(pRvs, si, pRqDt, pSous);
      this.rdb.commit();
    } catch (Exception ex) {
      if (!this.rdb.getAcmt()) {
        this.rdb.rollBack();
      }
      throw ex;
    } finally {
      this.rdb.release();
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for rdb.</p>
   * @return IRdb
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
   * <p>Getter for srBlnSht.</p>
   * @return IInvPdf
   **/
  public final IInvPdf getInvPdf() {
    return this.invPdf;
  }

  /**
   * <p>Setter for srBlnSht.</p>
   * @param pInvPdf reference
   **/
  public final void setInvPdf(final IInvPdf pInvPdf) {
    this.invPdf = pInvPdf;
  }

  /**
   * <p>Getter for trIsl.</p>
   * @return Integer
   **/
  public final Integer getTrIsl() {
    return this.trIsl;
  }

  /**
   * <p>Setter for trIsl.</p>
   * @param pTrIsl reference
   **/
  public final void setTrIsl(final Integer pTrIsl) {
    this.trIsl = pTrIsl;
  }
}
