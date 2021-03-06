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
import org.beigesoft.mdl.IHasId;
import org.beigesoft.hld.IHlIntCls;
import org.beigesoft.acc.mdlp.Expn;
import org.beigesoft.acc.mdlp.Bnka;
import org.beigesoft.acc.mdlp.DbCr;
import org.beigesoft.acc.mdlp.DcrCt;
import org.beigesoft.acc.mdlp.Srv;
import org.beigesoft.acc.mdlp.SrvCt;
import org.beigesoft.acc.mdlp.ItmCt;
import org.beigesoft.acc.mdlp.Itm;
import org.beigesoft.acc.mdlp.Tax;
import org.beigesoft.acc.mdlp.EmpCt;
import org.beigesoft.acc.mdlp.Empl;
import org.beigesoft.acc.mdlp.WagTy;
import org.beigesoft.acc.mdlp.Prop;

/**
 * <p>Holder subacc classes by type.</p>
 *
 * @author Yury Demidenko
 */
public class HlTySac implements IHlIntCls {

  /**
   * <p>Data.</p>
   **/
  private final Map<Integer, Class<? extends IHasId<?>>> clsMp;

  /**
   * <p>Only constructor.</p>
   **/
  public HlTySac() {
    this.clsMp = new HashMap<Integer, Class<? extends IHasId<?>>>();
    this.clsMp.put(new Expn().cnsTy(), Expn.class); //1000
    this.clsMp.put(new DcrCt().cnsTy(), DcrCt.class); //1001
    this.clsMp.put(new DbCr().cnsTy(), DbCr.class); //1002
    this.clsMp.put(new Bnka().cnsTy(), Bnka.class); //1003
    this.clsMp.put(new ItmCt().cnsTy(), ItmCt.class); //1004
    this.clsMp.put(new Itm().cnsTy(), Itm.class); //1005
    this.clsMp.put(new SrvCt().cnsTy(), SrvCt.class); //1006
    this.clsMp.put(new Srv().cnsTy(), Srv.class); //1007
    this.clsMp.put(new Tax().cnsTy(), Tax.class); //1008
    this.clsMp.put(new EmpCt().cnsTy(), EmpCt.class); //1009
    this.clsMp.put(new Empl().cnsTy(), Empl.class); //1010
    this.clsMp.put(new WagTy().cnsTy(), WagTy.class); //1011
    this.clsMp.put(new Prop().cnsTy(), Prop.class); //1012
  }

  /**
   * <p>Gets subacc class by type.</p>
   * @param pSaTy subacc type
   * @return subacc class
   * @throws Exception if not found or null parameter
   **/
  @Override
  public final Class<? extends IHasId<?>> get(
    final Integer pSaTy) throws Exception {
    if (pSaTy == null) {
      throw new ExcCode(ExcCode.WR, "null_subacc_type");
    }
    Class<? extends IHasId<?>> rz = this.clsMp.get(pSaTy);
    if (rz == null) {
      throw new ExcCode(ExcCode.WR, "Subacc type not found for " + pSaTy);
    }
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for clsMp.</p>
   * @return Map<Integer, Class<?>>
   **/
  public final Map<Integer, Class<? extends IHasId<?>>> getClsMp() {
    return this.clsMp;
  }
}
