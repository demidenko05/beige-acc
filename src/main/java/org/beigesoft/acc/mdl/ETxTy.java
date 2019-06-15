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

package org.beigesoft.acc.mdl;

/**
 * <p>Tax Type INCOME/SALES/EMPLOYEE/EMPLOYER/OTHER.</p>
 *
 * @author Yury Demidenko
 */
 public enum ETxTy {

  /**
   * <p>0 Income taxes due to business owner.</p>
   **/
  INCOME,

  /**
   * <p>1 Sales taxes, e.g. any USA state sales tax or VAT.</p>
   **/
  SALES,

  /**
   * <p>2 Employment taxes from employee,
   * e.g. USA Federal Income Tax, Medicare.</p>
   **/
  EMPLOYEE,

  /**
   * <p>3 Employment taxes from employer,
   * e.g. USA FUTA.</p>
   **/
  EMPLOYER,

  /**
   * <p>4 Other taxes.</p>
   **/
  OTHER;
}
