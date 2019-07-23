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
import org.beigesoft.acc.mdlb.IDocb;
import org.beigesoft.acc.mdlp.InEntr;
import org.beigesoft.acc.mdlp.PrepFr;
import org.beigesoft.acc.mdlp.PrepTo;
import org.beigesoft.acc.mdlp.PurInv;
import org.beigesoft.acc.mdlp.PaymTo;
import org.beigesoft.acc.mdlp.SalInv;
import org.beigesoft.acc.mdlp.PaymFr;
import org.beigesoft.acc.mdlp.PurRet;
import org.beigesoft.acc.mdlp.SalRet;
import org.beigesoft.acc.mdlp.MovItm;
import org.beigesoft.acc.mdlp.ItmUlb;
import org.beigesoft.acc.mdlp.ItmAdd;
import org.beigesoft.acc.mdlp.MnfPrc;
import org.beigesoft.acc.mdlp.Mnfct;
import org.beigesoft.acc.mdlp.Wage;
import org.beigesoft.acc.mdlp.BnStLn;

/**
 * <p>Holder head entries sources classes by type.</p>
 *
 * @author Yury Demidenko
 */
public class HlTyEnSr implements IHlIntCls {

  /**
   * <p>Data.</p>
   **/
  private final Map<Integer, Class<? extends IDocb>> clsMp;

  /**
   * <p>Only constructor.</p>
   **/
  public HlTyEnSr() {
    this.clsMp = new HashMap<Integer, Class<? extends IDocb>>();
    this.clsMp.put(new InEntr().cnsTy(), InEntr.class); //1
    this.clsMp.put(new PrepFr().cnsTy(), PrepFr.class); //2
    this.clsMp.put(new PrepTo().cnsTy(), PrepTo.class); //3
    this.clsMp.put(new PurInv().cnsTy(), PurInv.class); //4
    this.clsMp.put(new PaymTo().cnsTy(), PaymTo.class); //5
    this.clsMp.put(new SalInv().cnsTy(), SalInv.class); //6
    this.clsMp.put(new PaymFr().cnsTy(), PaymFr.class); //7
    this.clsMp.put(new PurRet().cnsTy(), PurRet.class); //8
    this.clsMp.put(new SalRet().cnsTy(), SalRet.class); //9
    this.clsMp.put(new MovItm().cnsTy(), MovItm.class); //10
    this.clsMp.put(new ItmUlb().cnsTy(), ItmUlb.class); //11
    this.clsMp.put(new ItmAdd().cnsTy(), ItmAdd.class); //12
    this.clsMp.put(new MnfPrc().cnsTy(), MnfPrc.class); //13
    this.clsMp.put(new Mnfct().cnsTy(), Mnfct.class); //14
    this.clsMp.put(new Wage().cnsTy(), Wage.class); //15
    this.clsMp.put(new BnStLn().cnsTy(), BnStLn.class); //2008
  }

  /**
   * <p>Gets subacc class by type.</p>
   * @param pSaTy subacc type
   * @return subacc class
   * @throws Exception if not found or null parameter
   **/
  @Override
  public final Class<? extends IDocb> get(
    final Integer pSaTy) throws Exception {
    if (pSaTy == null) {
      throw new ExcCode(ExcCode.WR, "null_subacc_type");
    }
    Class<? extends IDocb> rz = this.clsMp.get(pSaTy);
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
  public final Map<Integer, Class<? extends IDocb>> getClsMp() {
    return this.clsMp;
  }
}
