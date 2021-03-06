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

package org.beigesoft.acc.mdlp;

import java.util.List;

import org.beigesoft.acc.mdl.EDocTy;
import org.beigesoft.acc.mdl.EDocDriTy;
import org.beigesoft.acc.mdlb.ADoci;
import org.beigesoft.acc.mdlb.IDcDri;

/**
 * <p>Model of document that adds items into inventory, e.g. for start not from
 * scratch or adjusting inventory for any reason.</p>
 *
 * @author Yury Demidenko
 */
public class ItmAdd extends ADoci implements IDcDri {

  /**
   * <p>Lines.</p>
   **/
 private List<ItAdLn> lns;

  /**
   * <p>Constant of code type 12.</p>
   * @return 12
   **/
  @Override
  public final Integer cnsTy() {
    return 12;
  }

  /**
   * <p>Getter of EDocTy.</p>
   * @return EDocTy
   **/
  @Override
  public final EDocTy getDocTy() {
    return EDocTy.ITSRLN;
  }

  /**
   * <p>Getter for has draw items document type.</p>
   * @return has draw items document type
   **/
  @Override
  public final EDocDriTy getDocDriTy() {
    return EDocDriTy.COGSDRIT;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for lns.</p>
   * @return List<ItAdLn>
   **/
  public final List<ItAdLn> getLns() {
    return this.lns;
  }

  /**
   * <p>Setter for lns.</p>
   * @param pLns reference
   **/
  public final void setLns(final List<ItAdLn> pLns) {
    this.lns = pLns;
  }
}
