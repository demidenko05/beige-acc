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

package org.beigesoft.acc.mdlb;

import java.util.List;

import org.beigesoft.mdlp.AIdLnNm;
import org.beigesoft.acc.mdlp.Uom;
import org.beigesoft.acc.mdlp.TxCt;

/**
 * <p>Model of item.</p>
 *
 * @param <T> item type
 * @param <L> item tax destination line type
 * @author Yury Demidenko
 */
public abstract class AItm<T extends AItm<?, ?>, L extends ATxDsLn<T>>
  extends AIdLnNm {

  /**
   * <p>Tax category, nullable.</p>
   **/
  private TxCt txCt;

  /**
   * <p>Default unit of measure.</p>
   **/
  private Uom duom;

  /**
   * <p>Getter for tdls.</p>
   * @return List<ItTxDl>
   **/
  public abstract List<L> getTdls();

  /**
   * <p>Setter for tdls.</p>
   * @param pTdls reference
   **/
  public abstract void setTdls(final List<L> pTdls);

  //Simple getters and setters:
  /**
   * <p>Getter for txCt.</p>
   * @return TxCt
   **/
  public final TxCt getTxCt() {
    return this.txCt;
  }

  /**
   * <p>Setter for txCt.</p>
   * @param pTxCt reference
   **/
  public final void setTxCt(final TxCt pTxCt) {
    this.txCt = pTxCt;
  }

  /**
   * <p>Getter for duom.</p>
   * @return Uom
   **/
  public final Uom getDuom() {
    return this.duom;
  }

  /**
   * <p>Setter for duom.</p>
   * @param pDuom reference
   **/
  public final void setDuom(final Uom pDuom) {
    this.duom = pDuom;
  }
}
