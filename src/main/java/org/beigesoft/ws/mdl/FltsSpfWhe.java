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

package org.beigesoft.ws.mdl;

/**
 * <p>
 * Bundle of specifics filter where clause and count of conditions.
 * </p>
 *
 * @author Yury Demidenko
 */
public class FltsSpfWhe  {

  /**
   * <p>Where clouse e.g.:
   * "(SPECIFICS=3 and LONGVALUE1 in (3, 14))
   * or (SPECIFICS=4 and NUMERICVALUE1&lt;2.33)".</p>
   **/
  private String where;

  /**
   * <p>Count of where conditions e.g. 2 for:
   * (SPECIFICS=3 and LONGVALUE1 in (3, 14))
   * or (SPECIFICS=4 and NUMERICVALUE1&lt;2.33).</p>
   **/
  private Integer whereCount = 0;

  //Simple getters and setters:
  /**
   * <p>Getter for where.</p>
   * @return String
   **/
  public final String getWhere() {
    return this.where;
  }

  /**
   * <p>Setter for where.</p>
   * @param pWhere reference
   **/
  public final void setWhere(final String pWhere) {
    this.where = pWhere;
  }

  /**
   * <p>Getter for whereCount.</p>
   * @return Integer
   **/
  public final Integer getWhereCount() {
    return this.whereCount;
  }

  /**
   * <p>Setter for whereCount.</p>
   * @param pWhereCount reference
   **/
  public final void setWhereCount(final Integer pWhereCount) {
    this.whereCount = pWhereCount;
  }
}
