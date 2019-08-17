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

package org.beigesoft.ws.mdlp;

import org.beigesoft.mdl.AHasVr;
import org.beigesoft.mdlp.UsTmc;
import org.beigesoft.acc.mdlp.DbCr;

/**
 * <p>Model of S.E. Seller that sells its goods on this web-store.</p>
 *
 * @author Yury Demidenko
 */
public class SeSel extends AHasVr<DbCr> {

  /**
   * <p>Seller, PK.</p>
   **/
  private DbCr dbcr;

  /**
   * <p>User from JEE JDBC based authentication, not null.</p>
   **/
  private UsTmc usr;

  /**
   * <p>Usually it's simple getter that return model ID.</p>
   * @return ID model ID
   **/
  @Override
  public final DbCr getIid() {
    return this.dbcr;
  }

  /**
   * <p>Usually it's simple setter for model ID.</p>
   * @param pIid model ID
   **/
  @Override
  public final void setIid(final DbCr pIid) {
    this.dbcr = pIid;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for dbcr.</p>
   * @return DbCr
   **/
  public final DbCr getDbcr() {
    return this.dbcr;
  }

  /**
   * <p>Setter for dbcr.</p>
   * @param pDbcr reference
   **/
  public final void setDbcr(final DbCr pDbcr) {
    this.dbcr = pDbcr;
  }

  /**
   * <p>Getter for usr.</p>
   * @return UsTmc
   **/
  public final UsTmc getUsr() {
    return this.usr;
  }

  /**
   * <p>Setter for usr.</p>
   * @param pUsTmc reference
   **/
  public final void setUsr(final UsTmc pUsTmc) {
    this.usr = pUsTmc;
  }
}
