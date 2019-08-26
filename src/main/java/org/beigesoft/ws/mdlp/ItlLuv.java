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

import org.beigesoft.mdlp.AIdLn;

/**
 * <p>Holds last updated versions of goods/service/s.e.goods/s.e.service
 * characteristics.</p>
 *
 * @author Yury Demidenko
 */
public class ItlLuv extends AIdLn {

  /**
   * <p>Last version of Goods Specific updated Itlist.</p>
   **/
  private Long gdSpv;

  /**
   * <p>Last version of Goods Price updated Itlist.</p>
   **/
  private Long gdPrv;

  /**
   * <p>Last version of Goods Available updated Itlist.</p>
   **/
  private Long gdPlv;

  /**
   * <p>Last version of Service Specifics updated Itlist.</p>
   **/
  private Long srSpv;

  /**
   * <p>Last version of Service Price updated Itlist.</p>
   **/
  private Long srPrv;

  /**
   * <p>Last version of Service Place updated Itlist.</p>
   **/
  private Long srPlv;

  /**
   * <p>Last version of SeGoods Specifics updated Itlist.</p>
   **/
  private Long sgdSpv;

  /**
   * <p>Last version of SeGoods Price updated Itlist.</p>
   **/
  private Long sgdPrv;

  /**
   * <p>Last version of SeGoods Place updated Itlist.</p>
   **/
  private Long sgdPlv;

  /**
   * <p>Last version of SeService Specifics updated Itlist.</p>
   **/
  private Long ssrSpv;

  /**
   * <p>Last version of SeService Price updated Itlist.</p>
   **/
  private Long ssrPrv;

  /**
   * <p>Last version of SeService Place updated Itlist.</p>
   **/
  private Long ssrPlv;

  //Simple getters and setters:
  /**
   * <p>Getter for gdSpv.</p>
   * @return Long
   **/
  public final Long getGdSpv() {
    return this.gdSpv;
  }

  /**
   * <p>Setter for gdSpv.</p>
   * @param pGdSpv reference
   **/
  public final void setGdSpv(final Long pGdSpv) {
    this.gdSpv = pGdSpv;
  }

  /**
   * <p>Getter for gdPrv.</p>
   * @return Long
   **/
  public final Long getGdPrv() {
    return this.gdPrv;
  }

  /**
   * <p>Setter for gdPrv.</p>
   * @param pGdPrv reference
   **/
  public final void setGdPrv(final Long pGdPrv) {
    this.gdPrv = pGdPrv;
  }

  /**
   * <p>Getter for gdPlv.</p>
   * @return Long
   **/
  public final Long getGdPlv() {
    return this.gdPlv;
  }

  /**
   * <p>Setter for gdPlv.</p>
   * @param pGdPlv reference
   **/
  public final void setGdPlv(final Long pGdPlv) {
    this.gdPlv = pGdPlv;
  }

  /**
   * <p>Getter for srSpv.</p>
   * @return Long
   **/
  public final Long getSrSpv() {
    return this.srSpv;
  }

  /**
   * <p>Setter for srSpv.</p>
   * @param pSrSpv reference
   **/
  public final void setSrSpv(final Long pSrSpv) {
    this.srSpv = pSrSpv;
  }

  /**
   * <p>Getter for srPrv.</p>
   * @return Long
   **/
  public final Long getSrPrv() {
    return this.srPrv;
  }

  /**
   * <p>Setter for srPrv.</p>
   * @param pSrPrv reference
   **/
  public final void setSrPrv(final Long pSrPrv) {
    this.srPrv = pSrPrv;
  }

  /**
   * <p>Getter for srPlv.</p>
   * @return Long
   **/
  public final Long getSrPlv() {
    return this.srPlv;
  }

  /**
   * <p>Setter for srPlv.</p>
   * @param pSrPlv reference
   **/
  public final void setSrPlv(final Long pSrPlv) {
    this.srPlv = pSrPlv;
  }

  /**
   * <p>Getter for sgdSpv.</p>
   * @return Long
   **/
  public final Long getSgdSpv() {
    return this.sgdSpv;
  }

  /**
   * <p>Setter for sgdSpv.</p>
   * @param pSgdSpv reference
   **/
  public final void setSgdSpv(final Long pSgdSpv) {
    this.sgdSpv = pSgdSpv;
  }

  /**
   * <p>Getter for sgdPrv.</p>
   * @return Long
   **/
  public final Long getSgdPrv() {
    return this.sgdPrv;
  }

  /**
   * <p>Setter for sgdPrv.</p>
   * @param pSgdPrv reference
   **/
  public final void setSgdPrv(final Long pSgdPrv) {
    this.sgdPrv = pSgdPrv;
  }

  /**
   * <p>Getter for sgdPlv.</p>
   * @return Long
   **/
  public final Long getSgdPlv() {
    return this.sgdPlv;
  }

  /**
   * <p>Setter for sgdPlv.</p>
   * @param pSgdPlv reference
   **/
  public final void setSgdPlv(final Long pSgdPlv) {
    this.sgdPlv = pSgdPlv;
  }

  /**
   * <p>Getter for ssrSpv.</p>
   * @return Long
   **/
  public final Long getSsrSpv() {
    return this.ssrSpv;
  }

  /**
   * <p>Setter for ssrSpv.</p>
   * @param pSsrSpv reference
   **/
  public final void setSsrSpv(final Long pSsrSpv) {
    this.ssrSpv = pSsrSpv;
  }

  /**
   * <p>Getter for ssrPrv.</p>
   * @return Long
   **/
  public final Long getSsrPrv() {
    return this.ssrPrv;
  }

  /**
   * <p>Setter for ssrPrv.</p>
   * @param pSsrPrv reference
   **/
  public final void setSsrPrv(final Long pSsrPrv) {
    this.ssrPrv = pSsrPrv;
  }

  /**
   * <p>Getter for ssrPlv.</p>
   * @return Long
   **/
  public final Long getSsrPlv() {
    return this.ssrPlv;
  }

  /**
   * <p>Setter for ssrPlv.</p>
   * @param pSsrPlv reference
   **/
  public final void setSsrPlv(final Long pSsrPlv) {
    this.ssrPlv = pSsrPlv;
  }
}
