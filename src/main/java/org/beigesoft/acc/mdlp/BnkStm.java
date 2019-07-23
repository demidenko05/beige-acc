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

import java.util.List;
import java.util.Date;

import org.beigesoft.mdlp.AOrId;

/**
 * <p>Model of bank statement imported from CSV file.</p>
 *
 * @author Yury Demidenko
 */
public class BnkStm extends AOrId {

  /**
   * <p>Date.</p>
   **/
  private Date dat;

  /**
   * <p>Bank CSV method, not null.</p>
   **/
  private BnkCsv mth;

  /**
   * <p>Bank account, not null.</p>
   **/
  private Bnka bnka;

  /**
   * <p>Source name, not null, CSV file name + BnkCsv name.</p>
   **/
  private String srcNm;

  /**
   * <p>Description.</p>
   **/
  private String dscr;

  /**
   * <p>Lines.</p>
   **/
  private List<BnStLn> lns;

  //Simple getters and setters:
  /**
   * <p>Getter for dat.</p>
   * @return Date
   **/
  public final Date getDat() {
    return this.dat;
  }

  /**
   * <p>Setter for dat.</p>
   * @param pDat reference
   **/
  public final void setDat(final Date pDat) {
    this.dat = pDat;
  }

  /**
   * <p>Getter for mth.</p>
   * @return BnkCsv
   **/
  public final BnkCsv getMth() {
    return this.mth;
  }

  /**
   * <p>Setter for mth.</p>
   * @param pMth reference
   **/
  public final void setMth(final BnkCsv pMth) {
    this.mth = pMth;
  }

  /**
   * <p>Getter for bnka.</p>
   * @return Bnka
   **/
  public final Bnka getBnka() {
    return this.bnka;
  }

  /**
   * <p>Setter for bnka.</p>
   * @param pBnka reference
   **/
  public final void setBnka(final Bnka pBnka) {
    this.bnka = pBnka;
  }

  /**
   * <p>Getter for srcNm.</p>
   * @return String
   **/
  public final String getSrcNm() {
    return this.srcNm;
  }

  /**
   * <p>Setter for srcNm.</p>
   * @param pSrcNm reference
   **/
  public final void setSrcNm(final String pSrcNm) {
    this.srcNm = pSrcNm;
  }

  /**
   * <p>Getter for dscr.</p>
   * @return String
   **/
  public final String getDscr() {
    return this.dscr;
  }

  /**
   * <p>Setter for dscr.</p>
   * @param pDscr reference
   **/
  public final void setDscr(final String pDscr) {
    this.dscr = pDscr;
  }

  /**
   * <p>Getter for lns.</p>
   * @return List<BnStLn>
   **/
  public final List<BnStLn> getLns() {
    return this.lns;
  }

  /**
   * <p>Setter for lns.</p>
   * @param pLns reference
   **/
  public final void setLns(final List<BnStLn> pLns) {
    this.lns = pLns;
  }
}
