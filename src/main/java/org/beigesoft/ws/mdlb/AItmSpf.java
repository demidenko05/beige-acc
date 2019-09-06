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

package org.beigesoft.ws.mdlb;

import java.math.BigDecimal;

import org.beigesoft.mdl.AHasVr;
import org.beigesoft.mdl.IIdLnNm;
import org.beigesoft.ws.mdlp.ItmSp;

/**
 * <p>
 * Model of Specifics values for a Item (Goods/Service/SeGoods/SeService).
 * Beige-ORM does not support generic fields!
 * </p>
 *
 * @param <T> item type
 * @param <ID> ID type
 * @author Yury Demidenko
 */
public abstract class AItmSpf<T extends IIdLnNm, ID extends AItmSpfId<T>>
  extends AHasVr<ID> {

  /**
   * <p>Numeric Value1 if present.</p>
   **/
  private BigDecimal num1;

  /**
   * <p>Numeric Value2 if present.</p>
   **/
  private BigDecimal num2;

  /**
   * <p>Long Value1 if present.</p>
   **/
  private Long lng1;

  /**
   * <p>Long Value2 if present.</p>
   **/
  private Long lng2;

  /**
   * <p>String Value1 if present.</p>
   **/
  private String str1;

  /**
   * <p>String Value2 if present.</p>
   **/
  private String str2;

  /**
   * <p>String Value3 if present.</p>
   **/
  private String str3;

  /**
   * <p>String Value4 if present.</p>
   **/
  private String str4;

  /**
   * <p>Getter for specifics.</p>
   * @return ItmSp
   **/
  public abstract ItmSp getSpec();

  /**
   * <p>Setter for specifics.</p>
   * @param pSpecifics reference
   **/
  public abstract void setSpec(ItmSp pSpecifics);

  /**
   * <p>Getter for item.</p>
   * @return T
   **/
  public abstract T getItm();

  /**
   * <p>Setter for item.</p>
   * @param pItm reference
   **/
  public abstract void setItm(T pItm);

  //Simple getters and setters:
  /**
   * <p>Getter for num1.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getNum1() {
    return this.num1;
  }

  /**
   * <p>Setter for num1.</p>
   * @param pNum1 reference
   **/
  public final void setNum1(final BigDecimal pNum1) {
    this.num1 = pNum1;
  }

  /**
   * <p>Getter for num2.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getNum2() {
    return this.num2;
  }

  /**
   * <p>Setter for num2.</p>
   * @param pNum2 reference
   **/
  public final void setNum2(final BigDecimal pNum2) {
    this.num2 = pNum2;
  }

  /**
   * <p>Getter for lng1.</p>
   * @return Long
   **/
  public final Long getLng1() {
    return this.lng1;
  }

  /**
   * <p>Setter for lng1.</p>
   * @param pLng1 reference
   **/
  public final void setLng1(final Long pLng1) {
    this.lng1 = pLng1;
  }

  /**
   * <p>Getter for lng2.</p>
   * @return Long
   **/
  public final Long getLng2() {
    return this.lng2;
  }

  /**
   * <p>Setter for lng2.</p>
   * @param pLng2 reference
   **/
  public final void setLng2(final Long pLng2) {
    this.lng2 = pLng2;
  }

  /**
   * <p>Getter for str1.</p>
   * @return String
   **/
  public final String getStr1() {
    return this.str1;
  }

  /**
   * <p>Setter for str1.</p>
   * @param pStr1 reference
   **/
  public final void setStr1(final String pStr1) {
    this.str1 = pStr1;
  }

  /**
   * <p>Getter for str2.</p>
   * @return String
   **/
  public final String getStr2() {
    return this.str2;
  }

  /**
   * <p>Setter for str2.</p>
   * @param pStr2 reference
   **/
  public final void setStr2(final String pStr2) {
    this.str2 = pStr2;
  }

  /**
   * <p>Getter for str3.</p>
   * @return String
   **/
  public final String getStr3() {
    return this.str3;
  }

  /**
   * <p>Setter for str3.</p>
   * @param pStr3 reference
   **/
  public final void setStr3(final String pStr3) {
    this.str3 = pStr3;
  }

  /**
   * <p>Getter for str4.</p>
   * @return String
   **/
  public final String getStr4() {
    return this.str4;
  }

  /**
   * <p>Setter for str4.</p>
   * @param pStr4 reference
   **/
  public final void setStr4(final String pStr4) {
    this.str4 = pStr4;
  }
}
