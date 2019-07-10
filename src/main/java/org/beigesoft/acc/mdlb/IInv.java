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

import java.util.Date;
import java.math.BigDecimal;

/**
 * <p>Base model of invoice.</p>
 *
 * @author Yury Demidenko
 */
public interface IInv extends IInvb {

  /**
   * <p>Getter for prep.</p>
   * @return PrepTo
   **/
  APrep getPrep();

  /**
   * <p>Getter for payb.</p>
   * @return Date
   **/
  Date getPayb();

  /**
   * <p>Setter for payb.</p>
   * @param pPayb reference
   **/
  void setPayb(Date pPayb);

  /**
   * <p>Getter for toPa.</p>
   * @return BigDecimal
   **/
  BigDecimal getToPa();

  /**
   * <p>Setter for toPa.</p>
   * @param pToPa reference
   **/
  void setToPa(BigDecimal pToPa);

  /**
   * <p>Getter for paFc.</p>
   * @return BigDecimal
   **/
  BigDecimal getPaFc();

  /**
   * <p>Setter for paFc.</p>
   * @param pPaFc reference
   **/
  void setPaFc(BigDecimal pPaFc);

  /**
   * <p>Getter for pdsc.</p>
   * @return String
   **/
  String getPdsc();

  /**
   * <p>Setter for pdsc.</p>
   * @param pPdsc reference
   **/
  void setPdsc(String pPdsc);
}
