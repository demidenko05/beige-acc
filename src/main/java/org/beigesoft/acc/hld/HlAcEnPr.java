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

package org.beigesoft.acc.hld;

import org.beigesoft.hld.IHlNmClSt;
import org.beigesoft.prc.PrcEntRt;
import org.beigesoft.prc.PrcEntCr;
import org.beigesoft.acc.mdl.ISacnt;
import org.beigesoft.acc.mdlp.Entr;
import org.beigesoft.acc.mdlp.InEntr;
import org.beigesoft.acc.prc.EntrCr;
import org.beigesoft.acc.prc.EntrSrcCr;
import org.beigesoft.acc.prc.IsacntSv;
import org.beigesoft.acc.prc.IsacntDl;
import org.beigesoft.acc.prc.EntrSv;
import org.beigesoft.acc.prc.InEntrSv;
import org.beigesoft.acc.prc.InEntrDl;
import org.beigesoft.acc.prc.InEntrRt;

/**
 * <p>Additional holder of names of ACC entities processors.</p>
 *
 * @author Yury Demidenko
 */
public class HlAcEnPr implements IHlNmClSt {

  /**
   * <p>Get processor name for given class and action name.</p>
   * @param pCls a Class
   * @param pAct action name
   * @return processor name
   **/
  @Override
  public final String get(final Class<?> pCls, final String pAct) {
    if (ISacnt.class.isAssignableFrom(pCls) || Entr.class == pCls
      || InEntr.class == pCls) {
      if ("entEd".equals(pAct) || "entPr".equals(pAct)
        || "entCd".equals(pAct)) {
        if (InEntr.class == pCls) {
          return InEntrRt.class.getSimpleName();
        } else if (ISacnt.class.isAssignableFrom(pCls)) {
          return PrcEntRt.class.getSimpleName();
        }
      } else if ("entCr".equals(pAct)) {
        if (Entr.class == pCls) {
          return EntrCr.class.getSimpleName();
        } else if (InEntr.class == pCls) {
          return EntrSrcCr.class.getSimpleName();
        } else if (ISacnt.class.isAssignableFrom(pCls)) {
          return PrcEntCr.class.getSimpleName();
        }
      } else if ("entDl".equals(pAct)) {
        if (ISacnt.class.isAssignableFrom(pCls)) {
          return IsacntDl.class.getSimpleName();
        } else if (InEntr.class == pCls) {
          return InEntrDl.class.getSimpleName();
        }
       } else if ("entSv".equals(pAct)) {
        if (Entr.class == pCls) {
          return EntrSv.class.getSimpleName();
        } else if (InEntr.class == pCls) {
          return InEntrSv.class.getSimpleName();
        } else if (ISacnt.class.isAssignableFrom(pCls)) {
          return IsacntSv.class.getSimpleName();
        }
      }
      return NULL;
    }
    return null;
  }
}
