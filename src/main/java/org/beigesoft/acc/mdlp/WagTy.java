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

import org.beigesoft.mdlp.AIdLnNm;
import org.beigesoft.acc.mdlb.ISacnt;

/**
 * <p>Model of wage type, e.g. "Cooking", "Sick compensation".</p>
 *
 * @author Yury Demidenko
 */
public class WagTy extends AIdLnNm implements ISacnt {

  /**
   * <p>Expense.</p>
   **/
  private Expn exp;

  /**
   * <p>OOP friendly Constant of code type 1011.</p>
   * @return 1011
   **/
  @Override
  public final Integer cnsTy() {
    return 1011;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for exp.</p>
   * @return Expn
   **/
  public final Expn getExp() {
    return this.exp;
  }

  /**
   * <p>Setter for exp.</p>
   * @param pExp reference
   **/
  public final void setExp(final Expn pExp) {
    this.exp = pExp;
  }
}
