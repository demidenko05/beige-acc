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

import org.beigesoft.mdlp.IOrId;
import org.beigesoft.acc.mdlp.Uom;
import org.beigesoft.acc.mdlp.Itm;

/**
 * <p>Model of entity that makes warehouse entries, i.e.
 * it loads(puts) or withdrawal an item into/from warehouse.
 * E.g. purchase invoice line, sales invoice line, beginning inventory line,
 * manufacturing.Constant of making WS entries - range 2000...2999.</p>
 *
 * @author Yury Demidenko
 */
public interface IMkWsEnr extends IOrId {

  /**
   * <p>Constant of making WS entries, range 2000...2999.</p>
   * @return entity type code
   **/
  Integer cnsTy();

  /**
   * <p>Getter for rvId.</p>
   * @return Long
   **/
  Long getRvId();

  /**
   * <p>Setter for rvId.</p>
   * @param pRvId reference
   **/
  void setRvId(Long pRvId);

  /**
   * <p>Getter for itm.</p>
   * @return Itm
   **/
  Itm getItm();

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  void setItm(Itm pItm);

  /**
   * <p>Getter for uom.</p>
   * @return Uom
   **/
  Uom getUom();

  /**
   * <p>Setter for uom.</p>
   * @param pUom reference
   **/
  void setUom(Uom pUom);

  /**
   * <p>Getter for quan.</p>
   * @return BigDecimal
   **/
  BigDecimal getQuan();

  /**
   * <p>Setter for quan.</p>
   * @param pQuan reference
   **/
  void setQuan(BigDecimal pQuan);

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

  /**
   * <p>Getter for document date (own or owner's).</p>
   * @return Date
   **/
  Date getDocDt();

  //Owner if exist:
  /**
   * <p>Getter for owner ID if exist.</p>
   * @return ID
   **/
  Long getOwnrId();

  /**
   * <p>Getter for owner type code if exist.</p>
   * @return type code
   **/
  Integer getOwnrTy();
}
