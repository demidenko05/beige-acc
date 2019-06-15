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

package org.beigesoft.acc.prc;

import java.util.Map;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hld.UvdVar;
import org.beigesoft.prc.IPrcEnt;
import org.beigesoft.acc.mdl.AcUpf;
import org.beigesoft.acc.mdlb.IEntrSrc;

/**
 * <p>Service that creates document that makes entries.</p>
 *
 * @author Yury Demidenko
 */
public class EntrSrcCr implements IPrcEnt<IEntrSrc, Long> {

  /**
   * <p>Process that creates entity.</p>
   * @param pRvs request scoped vars, e.g. return this line's
   * owner(document) in "nextEntity" for farther processing
   * @param pRqDt Request Data
   * @param pEnt Entity to process
   * @return Entity processed for farther process or null
   * @throws Exception - an exception
   **/
  @Override
  public final IEntrSrc process(final Map<String, Object> pRvs,
    final IEntrSrc pEnt, final IReqDt pRqDt) throws Exception {
    pEnt.setIsNew(true);
    AcUpf aupf = (AcUpf) pRvs.get("aupf");
    Calendar nowc = Calendar.getInstance(new Locale("en", "US"));
    nowc.setTime(new Date());
    Calendar opDtc = Calendar.getInstance(new Locale("en", "US"));
    opDtc.setTime(aupf.getOpDt());
    opDtc.set(Calendar.MINUTE, nowc.get(Calendar.MINUTE));
    opDtc.set(Calendar.SECOND, nowc.get(Calendar.SECOND));
    opDtc.set(Calendar.MILLISECOND, nowc.get(Calendar.MILLISECOND));
    pEnt.setDat(opDtc.getTime());
    UvdVar uvs = (UvdVar) pRvs.get("uvs");
    uvs.setEnt(pEnt);
    return pEnt;
  }
}
