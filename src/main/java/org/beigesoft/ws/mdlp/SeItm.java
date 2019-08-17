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

import org.beigesoft.acc.mdlb.AItm;
import org.beigesoft.ws.mdlb.IHsSeSel;

/**
 * <p>Model of S.E. Seller's good.</p>
 *
 * @author Yury Demidenko
 */
public class SeItm extends AItm<SeItm, SitTxDl> implements IHsSeSel<Long> {

  /**
   * <p>S.E.Seller, not null.</p>
   **/
  private SeSel selr;

  /**
   * <p>Tax destination lines.</p>
   **/
  private List<SitTxDl> tdls;

  /**
   * <p>Getter for tdls.</p>
   * @return List<SitTxDl>
   **/
  @Override
  public final List<SitTxDl> getTdls() {
    return this.tdls;
  }

  /**
   * <p>Setter for tdls.</p>
   * @param pTdls reference
   **/
  @Override
  public final void setTdls(final List<SitTxDl> pTdls) {
    this.tdls = pTdls;
  }

  /**
   * <p>Getter for selr.</p>
   * @return SeSel
   **/
  @Override
  public final SeSel getSelr() {
    return this.selr;
  }

  /**
   * <p>Setter for selr.</p>
   * @param pSelr reference
   **/
  @Override
  public final void setSelr(final SeSel pSelr) {
    this.selr = pSelr;
  }
}
