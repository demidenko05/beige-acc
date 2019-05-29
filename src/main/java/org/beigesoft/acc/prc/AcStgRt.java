/*
BSD 2-Clause License

Copyright (c) 2019, Beigesoft™
All rights reserved.

Redistribution and use in source and binary fsrAcStgs, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary fsrAcStg must reproduce the above copyright notice,
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

package org.beigesoft.acc.prc;

import java.util.Map;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.srv.ISrAcStg;

/**
 * <p>Service that retrieves acc settings from DB.</p>
 *
 * @author Yury Demidenko
 */
public class AcStgRt implements IPrcEnt<AcStg, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private ISrAcStg srAcStg;

  /**
   * <p>Process that retrieves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final AcStg process(final Map<String, Object> pRvs, final AcStg pEnt,
    final IReqDt pRqDt) throws Exception {
    AcStg ent = getSrAcStg().lazAcStg(pRvs);
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setEnt(ent);
    return ent;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srAcStg.</p>
   * @return ISrAcStg
   **/
  public final ISrAcStg getSrAcStg() {
    return this.srAcStg;
  }

  /**
   * <p>Setter for srAcStg.</p>
   * @param pSrAcStg reference
   **/
  public final void setSrAcStg(final ISrAcStg pSrAcStg) {
    this.srAcStg = pSrAcStg;
  }
}
