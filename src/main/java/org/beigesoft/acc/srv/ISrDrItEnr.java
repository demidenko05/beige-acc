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

import org.beigesoft.acc.mdlb.ADrItEnr;
import org.beigesoft.acc.mdlb.IMkDriEnr;
import org.beigesoft.acc.mdlb.IItmSrc;
import org.beigesoft.acc.mdlb.IDcDri;

/**
 * <p>Service that makes, reverses, retrieves draw item entries
 * for given drawer/document.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrDrItEnr {


  /**
   * <p>Handles settings changed, i.e. clears cached data.</p>
   * @param pRvs Request scoped variables
   * @throws Exception - an exception
   **/
  void hndStgCng(Map<String, Object> pRvs) throws Exception;

  /**
   * <p>Makes drawing entries for given drawer.</p>
   * @param <T> draw entry type
   * @param pRvs Request scoped variables
   * @param pDrer drawer document or line
   * @throws Exception - an exception
   **/
  <T extends ADrItEnr> void draw(Map<String, Object> pRvs,
    IMkDriEnr<T> pDrer) throws Exception;

  /**
   * <p>Makes drawing entries for given drawer from given source.</p>
   * @param <T> draw entry type
   * @param pRvs Request scoped variables
   * @param pDrer drawer document or line
   * @param pSrc item source
   * @param pQuan quantity
   * @throws Exception - an exception
   **/
  <T extends ADrItEnr> void drawFr(Map<String, Object> pRvs, IMkDriEnr<T> pDrer,
    IItmSrc pSrc, BigDecimal pQuan) throws Exception;

  /**
   * <p>Reverses drawing entries for given drawer.</p>
   * @param <T> draw entry type
   * @param pRvs Request scoped variables
   * @param pDrer drawer document or line
   * @throws Exception - an exception
   **/
  <T extends ADrItEnr> void rvDraw(Map<String, Object> pRvs,
    IMkDriEnr<T> pDrer) throws Exception;

  /**
   * <p>Retrieves drawing entries for given document.</p>
   * @param <T> draw entry type
   * @param pRvs Request scoped variables
   * @param pDoc document
   * @param pEnrCls entries class
   * @return entries
   * @throws Exception - an exception
   **/
  <T extends ADrItEnr> List<T> retEntrs(Map<String, Object> pRvs,
    IDcDri pDoc, Class<T> pEnrCls) throws Exception;
}
