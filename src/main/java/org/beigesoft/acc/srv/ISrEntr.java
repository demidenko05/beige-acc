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

import org.beigesoft.acc.mdlb.IEntrSrc;
import org.beigesoft.acc.mdlb.IDoci;
import org.beigesoft.acc.mdlp.Entr;

/**
 * <p>Service that makes, reverses, retrieves entries for given document.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrEntr {

  /**
   * <p>Handles settings changed, i.e. clears cached data.</p>
   **/
  String DOCFDSUPD = "docFdsUpd";

  /**
   * <p>Handles settings changed, i.e. clears cached data.</p>
   * @param pRvs Request scoped variables
   * @throws Exception - an exception
   **/
  void hndStgCng(Map<String, Object> pRvs) throws Exception;

  /**
   * <p>Makes entries for given document. Document must be inserted.</p>
   * @param pRvs Request scoped variables
   * @param pDoc source document
   * @throws Exception - an exception
   **/
  void mkEntrs(Map<String, Object> pRvs, IDoci pDoc) throws Exception;

  /**
   * <p>Reverses entries for given document.</p>
   * @param pRvs Request scoped variables
   * @param pRvng reversing document
   * @param pRved reversed document
   * @throws Exception - an exception
   **/
  void revEntrs(Map<String, Object> pRvs, IDoci pRvng,
    IDoci pRved) throws Exception;

  /**
   * <p>Retrieves entries for given document.</p>
   * @param pRvs Request scoped variables
   * @param pSrc source document
   * @return entries
   * @throws Exception - an exception
   **/
  List<Entr> retEntrs(Map<String, Object> pRvs, IEntrSrc pSrc) throws Exception;
}
