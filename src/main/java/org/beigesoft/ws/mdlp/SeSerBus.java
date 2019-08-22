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

import java.util.Date;

import org.beigesoft.mdlp.AIdLna;
import org.beigesoft.ws.mdlb.IHsSeSel;

/**
 * <p>S.E.-service busy from till time.</p>
 *
 * @author Yury Demidenko
 */
public class SeSerBus extends AIdLna implements IHsSeSel<Long> {

  /**
   * <p>Service, not null.</p>
   **/
  private SeSrv srv;

  /**
   * <p>Not null, busy from time (include).</p>
   **/
  private Date frTm;

  /**
   * <p>Not null, busy till time (exclude).</p>
   **/
  private Date tiTm;

  /**
   * <p>Is free (disabled), otherwise used. It's because of inserting/deleting
   * is more expensive than updating.</p>
   **/
  private Boolean fre = Boolean.FALSE;

  /**
   * <p>Getter for seller.</p>
   * @return SeSel
   **/
  @Override
  public final SeSel getSelr() {
    return this.srv.getSelr();
  }

  /**
   * <p>Setter for seller.</p>
   * @param pSeller reference
   **/
  @Override
  public final void setSelr(final SeSel pSeller) {
    this.srv.setSelr(pSeller);
  }

  //Simple getters and setters:
  /**
   * <p>Getter for serv.</p>
   * @return SeSrv
   **/
  public final SeSrv getSrv() {
    return this.srv;
  }

  /**
   * <p>Setter for serv.</p>
   * @param pServ reference
   **/
  public final void setSrv(final SeSrv pServ) {
    this.srv = pServ;
  }

  /**
   * <p>Getter for frTm.</p>
   * @return Date
   **/
  public final Date getFrTm() {
    return this.frTm;
  }

  /**
   * <p>Setter for frTm.</p>
   * @param pFrTm reference
   **/
  public final void setFrTm(final Date pFrTm) {
    this.frTm = pFrTm;
  }

  /**
   * <p>Getter for tiTm.</p>
   * @return Date
   **/
  public final Date getTiTm() {
    return this.tiTm;
  }

  /**
   * <p>Setter for tiTm.</p>
   * @param pTiTm reference
   **/
  public final void setTiTm(final Date pTiTm) {
    this.tiTm = pTiTm;
  }

  /**
   * <p>Getter for fre.</p>
   * @return Boolean
   **/
  public final Boolean getFre() {
    return this.fre;
  }

  /**
   * <p>Setter for fre.</p>
   * @param pFre reference
   **/
  public final void setFre(final Boolean pFre) {
    this.fre = pFre;
  }
}
