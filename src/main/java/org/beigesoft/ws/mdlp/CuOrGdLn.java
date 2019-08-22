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

package org.beigesoft.ws.mdlp;

import java.util.List;

import org.beigesoft.acc.mdlp.Itm;
import org.beigesoft.ws.mdlb.ACuOrLn;

/**
 * <p>
 * Model of Customer Order Goods line.
 * </p>
 *
 * @author Yury Demidenko
 */
public class CuOrGdLn extends ACuOrLn {

  /**
   * <p>Good, not null.</p>
   **/
  private Itm good;

  /**
   * <p>Item taxes for item basis non-aggregate method.</p>
   **/
  private List<CuOrGdTxLn> itTxs;

  //Simple getters and setters:
  /**
   * <p>Getter for goods.</p>
   * @return Itm
   **/
  public final Itm getGood() {
    return this.good;
  }

  /**
   * <p>Setter for goods.</p>
   * @param pGood reference
   **/
  public final void setGood(final Itm pGood) {
    this.good = pGood;
  }

  /**
   * <p>Getter for itTxs.</p>
   * @return List<CuOrGdTxLn>
   **/
  public final List<CuOrGdTxLn> getItTxs() {
    return this.itTxs;
  }

  /**
   * <p>Setter for itTxs.</p>
   * @param pItTxs reference
   **/
  public final void setItTxs(final List<CuOrGdTxLn> pItTxs) {
    this.itTxs = pItTxs;
  }
}
