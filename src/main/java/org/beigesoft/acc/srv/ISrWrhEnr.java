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

package org.beigesoft.acc.srv;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import org.beigesoft.acc.mdlb.IDocb;
import org.beigesoft.acc.mdlb.IMkWsEnr;
import org.beigesoft.acc.mdlp.WrhEnr;
import org.beigesoft.acc.mdlp.WrhPl;

/**
 * <p>Service that makes warehouse entries, items left in warehouse
 *  for given document/line.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrWrhEnr {

  /**
   * <p>Loads warehouse place with item from given source.</p>
   * @param pRvs Request scoped variables, not null
   * @param pEnt loader source, not null
   * @param pWrp warehouse place, not null
   * @throws Exception - an exception
   **/
  void load(Map<String, Object> pRvs, IMkWsEnr pEnt,
    WrhPl pWrp) throws Exception;

  /**
   * <p>Draws item from warehouse place for given source.</p>
   * @param pRvs Request scoped variables, not null
   * @param pEnt drawer source, not null
   * @param pWrp warehouse place, optional
   * @param pQuan quantity
   * @throws Exception - an exception
   **/
  void draw(Map<String, Object> pRvs, IMkWsEnr pEnt,
    WrhPl pWrp, BigDecimal pQuan) throws Exception;

  /**
   * <p>Moves item from one warehouse place to another for given source.</p>
   * @param pRvs Request scoped variables, not null
   * @param pEnt move source, not null
   * @param pWrpFr warehouse place from, not null
   * @param pWrpTo warehouse place to, not null
   * @throws Exception - an exception
   **/
  void move(Map<String, Object> pRvs, IMkWsEnr pEnt, WrhPl pWrpFr,
    WrhPl pWrpTo) throws Exception;

  /**
   * <p>Reverse for given loading.</p>
   * @param pRvs Request scoped variables, not null
   * @param pEnt loader source, not null
   * @throws Exception - an exception
   **/
  void revLoad(Map<String, Object> pRvs, IMkWsEnr pEnt) throws Exception;

  /**
   * <p>Reverse for given drawing.</p>
   * @param pRvs Request scoped variables, not null
   * @param pEnt drawer source, not null
   * @throws Exception - an exception
   **/
  void revDraw(Map<String, Object> pRvs, IMkWsEnr pEnt) throws Exception;

  /**
   * <p>Reverse for given moving.</p>
   * @param pRvs Request scoped variables, not null
   * @param pEnt move source, not null
   * @throws Exception - an exception
   **/
  void revMove(Map<String, Object> pRvs, IMkWsEnr pEnt) throws Exception;

  /**
   * <p>Retrieves entries for given document.</p>
   * @param pRvs Request scoped variables, not null
   * @param pDoc source document, not null
   * @return entries
   * @throws Exception - an exception
   **/
  List<WrhEnr> retEntrs(Map<String, Object> pRvs, IDocb pDoc) throws Exception;
}
