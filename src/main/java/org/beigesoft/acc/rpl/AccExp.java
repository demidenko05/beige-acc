/*
BSD 2-Clause License

Copyright (c) 2019, Beigesoft™
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following cond are met:

* Redistributions of source code must retain the above copyright notice, this
  list of cond and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of cond and the following disclaimer in the documentation
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

package org.beigesoft.acc.rpl;

import java.util.Map;
import java.io.PrintWriter;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.mdl.IHasId;
import org.beigesoft.log.ILog;
import org.beigesoft.prc.IPrc;
import org.beigesoft.rpl.IRpRtrv;

/**
 * <p>Exports accounting data with WEB-service.</p>
 *
 * @author Yury Demidenko
 */
public class AccExp implements IPrc {

  /**
   * <p>Logger.</p>
   **/
  private ILog log;

  /**
   * <p>Retriever.</p>
   **/
  private IRpRtrv retr;

  /**
   * <p>Process request.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    PrintWriter htmWri = (PrintWriter) pRvs.get("htmWri");
    @SuppressWarnings("unchecked")
    Class<? extends IHasId<?>> cls = (Class<? extends IHasId<?>>) Class
      .forName(pRqDt.getParam("ent"));
    pRvs.put("srDbId", pRqDt.getParam("srDbId"));
    pRvs.put("dsDbVr", pRqDt.getParam("dsDbVr"));
    pRvs.put("cond", pRqDt.getParam("cond"));
    this.retr.rtrvTo(pRvs, cls, htmWri);
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
   * <p>Getter for retr.</p>
   * @return IRpRtrv
   **/
  public final IRpRtrv getRetr() {
    return this.retr;
  }

  /**
   * <p>Setter for retr.</p>
   * @param pRetr reference
   **/
  public final void setRetr(final IRpRtrv pRetr) {
    this.retr = pRetr;
  }
}
