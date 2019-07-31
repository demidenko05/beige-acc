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

package org.beigesoft.acc.rpl;

import java.util.List;

import org.beigesoft.rpl.ARplMth;

/**
 * <p>Model of replication of accounting data method.</p>
 *
 * @author Yury Demidenko
 */
public class RplAcc extends ARplMth {

  /**
   * <p>Exclude debits.</p>
   **/
  private List<RpExDbl> exDbls;

  /**
   * <p>Exclude credits.</p>
   **/
  private List<RpExCrl> exCrds;

  //Simple getters and setters:
  /**
   * <p>Getter for exDbls.</p>
   * @return List<RpExDbl>
   **/
  public final List<RpExDbl> getExDbls() {
    return this.exDbls;
  }

  /**
   * <p>Setter for exDbls.</p>
   * @param pExDbls reference
   **/
  public final void setExDbls(final List<RpExDbl> pExDbls) {
    this.exDbls = pExDbls;
  }

  /**
   * <p>Getter for exCrds.</p>
   * @return List<RpExCrl>
   **/
  public final List<RpExCrl> getExCrds() {
    return this.exCrds;
  }

  /**
   * <p>Setter for exCrds.</p>
   * @param pExCrds reference
   **/
  public final void setExCrds(final List<RpExCrl> pExCrds) {
    this.exCrds = pExCrds;
  }
}
