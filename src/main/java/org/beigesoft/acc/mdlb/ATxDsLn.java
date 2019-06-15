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

import org.beigesoft.mdl.IOwned;
import org.beigesoft.mdl.IIdLn;
import org.beigesoft.mdlp.AIdLn;
import org.beigesoft.acc.mdlp.TxDst;
import org.beigesoft.acc.mdlp.TxCt;

/**
 * <p>Abstract model of tax destination line.</p>
 *
 * @param <T> owner type
 * @author Yury Demidenko
 */
public abstract class ATxDsLn<T extends IIdLn> extends AIdLn
  implements IOwned<T, Long> {

  /**
   * <p>Tax destination, not null.</p>
   **/
  private TxDst txDs;

  /**
   * <p>Tax category, null means non-taxable.</p>
   **/
  private TxCt txCt;

  //Simple getters and setters:
  /**
   * <p>Getter for txDs.</p>
   * @return TxDs
   **/
  public final TxDst getTxDs() {
    return this.txDs;
  }

  /**
   * <p>Setter for txDs.</p>
   * @param pTxDs reference
   **/
  public final void setTxDs(final TxDst pTxDs) {
    this.txDs = pTxDs;
  }

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
}
