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
import org.beigesoft.acc.mdlb.IItmSrc;
import org.beigesoft.acc.mdlp.PuInGdLn;

/**
 * <p>Holder item to draw sources classes by type.</p>
 *
 * @author Yury Demidenko
 */
public class HlTyItSr implements IHlIntCls {

  /**
   * <p>Data.</p>
   **/
  private final Map<Integer, Class<? extends IItmSrc>> clsMp;

  /**
   * <p>Only constructor.</p>
   **/
  public HlTyItSr() {
    this.clsMp = new HashMap<Integer, Class<? extends IItmSrc>>();
    this.clsMp.put(new PuInGdLn().cnsTy(), PuInGdLn.class); //2000
  }

  /**
   * <p>Gets source class by type.</p>
   * @param pSrTy source type
   * @return item source class
   * @throws Exception if not found or null parameter
   **/
  @Override
  public final Class<? extends IItmSrc> get(
    final Integer pSrTy) throws Exception {
    if (pSrTy == null) {
      throw new ExcCode(ExcCode.WR, "null source type!");
    }
    Class<? extends IItmSrc> rz = this.clsMp.get(pSrTy);
    if (rz == null) {
      throw new ExcCode(ExcCode.WR, "Source type not found for " + pSrTy);
    }
    return rz;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for clsMp.</p>
   * @return Map<Integer, Class<?>>
   **/
  public final Map<Integer, Class<? extends IItmSrc>> getClsMp() {
    return this.clsMp;
  }
}
