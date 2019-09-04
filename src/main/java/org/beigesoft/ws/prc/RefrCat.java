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
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE ITM OR
SRVS; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.beigesoft.ws.prc;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.prc.IPrc;
import org.beigesoft.ws.srv.ILsCatlChg;

/**
 * <p>Service that refreshes web-store catalog.</p>
 *
 * @author Yury Demidenko
 */
public class RefrCat implements IPrc {

  /**
   * <p>Lstns of catalog changed.</p>
   **/
  private List<ILsCatlChg> lstns = new ArrayList<ILsCatlChg>();


  /**
   * <p>Process refresh request.</p>
   * @param pRvs additional param
   * @param pRqd Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pRvs,
    final IReqDt pRqd) throws Exception {
    for (ILsCatlChg lstn : this.lstns) {
      lstn.hndCatlChg();
    }
      pRqd.setAttr("rnd", "rfcat");
  }

  //Simple getters and setters:
  /**
   * <p>Getter for lstns.</p>
   * @return List<ILsCatlChg>
   **/
  public final List<ILsCatlChg> getLstns() {
    return this.lstns;
  }

  /**
   * <p>Setter for lstns.</p>
   * @param pLstns reference
   **/
  public final void setLstns(final List<ILsCatlChg> pLstns) {
    this.lstns = pLstns;
  }
}
