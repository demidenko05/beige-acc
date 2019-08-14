/*
BSD 2-Clause License

Copyright (c) 2019, Beigesoft™
All rights reserved.

Redistribution and use in source and binary fsrTrStgs, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary fsrTrStg must reproduce the above copyright notice,
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

package org.beigesoft.ws.prc;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.hnd.IHnTrRlBk;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.ws.mdlp.TrdStg;
import org.beigesoft.ws.srv.ISrTrStg;

/**
 * <p>Service that saves trade settings into DB.</p>
 *
 * @author Yury Demidenko
 */
public class TrStgSv implements IPrcEnt<TrdStg, Long> {

  /**
   * <p>ORM service.</p>
   **/
  private ISrTrStg srTrStg;

  /**
   * <p>Process that saves entity.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final TrdStg process(final Map<String, Object> pRvs, final TrdStg pEnt,
    final IReqDt pRqDt) throws Exception {
    @SuppressWarnings("unchecked")
    Set<IHnTrRlBk> hnsTrRlBk = (Set<IHnTrRlBk>) pRvs.get(IHnTrRlBk.HNSTRRLBK);
    if (hnsTrRlBk == null) {
      hnsTrRlBk = new HashSet<IHnTrRlBk>();
      pRvs.put(IHnTrRlBk.HNSTRRLBK, hnsTrRlBk);
    }
    hnsTrRlBk.add(this.srTrStg);
    getSrTrStg().saveTrStg(pRvs, pEnt);
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setEnt(pEnt);
    return pEnt;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srTrStg.</p>
   * @return ISrTrStg
   **/
  public final ISrTrStg getSrTrStg() {
    return this.srTrStg;
  }

  /**
   * <p>Setter for srTrStg.</p>
   * @param pSrTrStg reference
   **/
  public final void setSrTrStg(final ISrTrStg pSrTrStg) {
    this.srTrStg = pSrTrStg;
  }
}
