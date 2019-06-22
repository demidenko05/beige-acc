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

package org.beigesoft.acc.srv;

import org.beigesoft.fct.IFctRq;
import org.beigesoft.mdl.IIdLn;
import org.beigesoft.acc.mdlb.ALnTxLn;
import org.beigesoft.acc.mdlb.ATxDsLn;
import org.beigesoft.acc.mdlb.AInv;
import org.beigesoft.acc.mdlb.AInvLn;

/**
 * <p>Abstraction of tax method code/data for purchase/sales invoice line.
 * It contains code/data dedicated to concrete invoice line type.</p>
 *
 * @param <T> invoice type
 * @param <L> invoice line type
 * @param <LTL> invoice line's tax line type
 * @author Yury Demidenko
 */
public interface IInvLnTxMeth<T extends AInv, L extends AInvLn<T, ?>,
  LTL extends ALnTxLn<T, L>> {

  /**
   * <p>Getter for dstTxItLnCl.</p>
   * @return Class<?>
   **/
  Class<? extends ATxDsLn<?>> getDstTxItLnCl();

  /**
   * <p>Getter for item class.</p>
   * @return Class<?>
   **/
  Class<? extends IIdLn> getItmCl();

  /**
   * <p>Getter for isMutable, if line editable, e.g. any good doesn't.</p>
   * @return Boolean
   **/
  Boolean getIsMutable();

  /**
   * <p>Getter for invoice line class.</p>
   * @return Class<L>
   **/
  Class<L> getInvLnCl();

  /**
   * <p>Getter for ltlCl.</p>
   * @return Class<LTL>
   **/
  Class<LTL> getLtlCl();

  /**
   * <p>Getter for fctLineTxLn.</p>
   * @return IFctRq<LTL>
   **/
  IFctRq<LTL> getFctLineTxLn();

  /**
   * <p>Getter for need make line tax category (purchase return not).</p>
   * @return Boolean
   **/
  Boolean getNeedMkTxCat();
}
