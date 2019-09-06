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

package org.beigesoft.ws.mdlb;

import org.beigesoft.mdl.IIdLnNm;
import org.beigesoft.ws.mdlp.ItmSp;

/**
 * <p>
 * Model of ID of Specifics values for a Item (Goods/Service/SeGoods/SeService).
 * Beige-ORM does not support generic fields!
 * </p>
 *
 * @param <T> item type
 * @author Yury Demidenko
 */
public abstract class AItmSpfId<T extends IIdLnNm> {

  /**
   * <p>Specifics.</p>
   **/
  private ItmSp spec;

  /**
   * <p>Getter for item.</p>
   * @return T
   **/
  public abstract T getItm();

  /**
   * <p>Setter for item.</p>
   * @param pItm reference
   **/
  public abstract void setItm(T pItm);

  //Simple getters and setters:
  /**
   * <p>Getter for spec.</p>
   * @return ItmSp
   **/
  public final ItmSp getSpec() {
    return this.spec;
  }

  /**
   * <p>Setter for spec.</p>
   * @param pSpec reference
   **/
  public final void setSpec(final ItmSp pSpec) {
    this.spec = pSpec;
  }
}
