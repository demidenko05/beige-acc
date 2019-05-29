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

package org.beigesoft.acc.hld;

import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.hld.IHlIntCls;
import org.beigesoft.acc.mdlp.Expn;
import org.beigesoft.acc.mdlp.Bnka;
import org.beigesoft.acc.mdlp.DbCr;
import org.beigesoft.acc.mdlp.DcrCt;
import org.beigesoft.acc.mdlp.Srv;
import org.beigesoft.acc.mdlp.SrvCt;
import org.beigesoft.acc.mdlp.ItmCt;
import org.beigesoft.acc.mdlp.Itm;

/**
 * <p>Holder subacc classes by type.</p>
 *
 * @author Yury Demidenko
 */
public class HlTySac implements IHlIntCls {

  /**
   * <p>Data.</p>
   **/
  private final Map<Integer, Class<?>> clsMp;

  /**
   * <p>Only constructor.</p>
   **/
  public HlTySac() {
    this.clsMp = new HashMap<Integer, Class<?>>();
    this.clsMp.put(new Expn().cnsTy(), Expn.class);
    this.clsMp.put(new Bnka().cnsTy(), Bnka.class);
    this.clsMp.put(new DbCr().cnsTy(), DbCr.class);
    this.clsMp.put(new DcrCt().cnsTy(), DcrCt.class);
    this.clsMp.put(new Expn().cnsTy(), Expn.class);
    this.clsMp.put(new Srv().cnsTy(), Srv.class);
    this.clsMp.put(new SrvCt().cnsTy(), SrvCt.class);
    this.clsMp.put(new Itm().cnsTy(), Itm.class);
    this.clsMp.put(new ItmCt().cnsTy(), ItmCt.class);
  }

  /**
   * <p>Gets subacc class by type.</p>
   * @param pSaTy subacc type
   * @return subacc class
   * @throws Exception if not found or null parameter
   **/
  @Override
  public final Class<?> get(final Integer pSaTy) throws Exception {
    if (pSaTy == null) {
      throw new ExcCode(ExcCode.WRPR, "null_subacc_type");
    }
    Class<?> rz = this.clsMp.get(pSaTy);
    if (rz == null) {
      throw new ExcCode(ExcCode.WRPR, "Subacc type not found for " + pSaTy);
    }
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for clsMp.</p>
   * @return Map<Integer, Class<?>>
   **/
  public final Map<Integer, Class<?>> getClsMp() {
    return this.clsMp;
  }
}
