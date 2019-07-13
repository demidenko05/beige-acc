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


import java.math.BigDecimal;

/**
 * <p>Abstract model of document that makes accounting entries,
 * e.g. Purchase Invoice.</p>
 *
 * @author Yury Demidenko
 */
public interface IDoc extends IEntrSrc, IRvId, IDocb {

  /**
   * <p>Getter of has made entries.</p>
   * @return Boolean
   **/
  Boolean getMdEnr();

  /**
   * <p>Setter for has made entries.</p>
   * @param pMdEnr reference
   **/
  void setMdEnr(Boolean pMdEnr);

  /**
   * <p>Getter for rvDbOr.</p> //TODO useless?
   * @return Integer
   **/
  Integer getRvDbOr();

  /**
   * <p>Setter for rvDbOr.</p>
   * @param pRvDbOr reference
   **/
  void setRvDbOr(Integer pRvDbOr);

  /**
   * <p>Getter for tot.</p>
   * @return BigDecimal
   **/
  BigDecimal getTot();

  /**
   * <p>Setter for tot.</p>
   * @param pTot reference
   **/
  void setTot(BigDecimal pTot);

  /**
   * <p>Getter for toFc.</p>
   * @return BigDecimal
   **/
  BigDecimal getToFc();

  /**
   * <p>Setter for toFc.</p>
   * @param pToFc reference
   **/
  void setToFc(BigDecimal pToFc);
}
