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
 * <p>Payment methods.</p>
 *
 * @author Yury Demidenko
 */
public enum EPaymMth {

  /**
   * <p>0, buyer must pay right now with any online method
   * (e.g. credit card, PayPal).</p>
   **/
  ONLINE,

  /**
   * <p>1, any, it's means that buyer can pay 100% with any method
   * (e.g. with cash when goods has been delivered),
   * and if order can be payed partially with several methods,
   * e.g. 50% online, 50% bank transfer, cash or cheque.</p>
   **/
  ANY,

  /**
   * <p>2, cash.</p>
   **/
  PAY_CASH,

  /**
   * <p>3, bank transfer.</p>
   **/
  BANK_TRANSFER,

  /**
   * <p>4, bank cheque.</p>
   **/
  BANK_CHEQUE,

  /**
   * <p>5, cash or bank transfer.</p>
   **/
  CASH_BANK_TRANSFER,

  /**
   * <p>6, bank transfer or cheque.</p>
   **/
  BANK_TRANSFER_CHEQUE,

  /**
   * <p>7, cash, bank transfer or cheque.</p>
   **/
  CASH_BANK_TRANSFER_CHEQUE,

  /**
   * <p>8, in case when order must be payed partially online
   * e.g 50% and the rest with any methods - online, bank transfer,
   * cash or check.</p>
   **/
  PARTIAL_ONLINE,

  /**
   * <p>9, exactly PayPal.</p>
   **/
  PAYPAL,

  /**
   * <p>10, PayPal or credit card (or any other method) through PayPal.</p>
   **/
  PAYPAL_ANY;
}
