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

import org.beigesoft.mdlp.IOrId;

/**
 * <p>Abstract model of a source of accounting entries. It's any document that
 * makes accounting entry by hand or automatically.
 * Method cnsTy returns unique code of entity type (OOP consumable constant),
 * range 1...999, e.g. 1 - input accounting entries by hand base document.
 * Any source has date and description.</p>
 *
 * @author Yury Demidenko
 */
public interface IEntrSrc extends IOrId, ITyp {

  /**
   * <p>Getter for dat.</p>
   * @return Date
   **/
  Date getDat();

  /**
   * <p>Setter for dat.</p>
   * @param pDat reference
   **/
  void setDat(Date pDat);

  /**
   * <p>Getter for dscr.</p>
   * @return String
   **/
  String getDscr();

  /**
   * <p>Setter for dscr.</p>
   * @param pDscr reference
   **/
  void setDscr(String pDscr);
}
