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
 * <p>Document type - ACC/WRH/WRHLN/WRHBTH/ITSR/ITSRLN/ITSRBTH/
 * DRAW/DRAWLN/DRAWBTH.</p>
 *
 * @author Yury Demidenko
 */
public enum EDocTy {

  /**
   * <p>0 only accounting entries.</p>
   **/
  ACC,

  /**
   * <p>1 makes warehouse entries.</p>
   **/
  WRH,

  /**
   * <p>2 lines make warehouse entries.</p>
   **/
  WRHLN,

  /**
   * <p>3 document and lines make warehouse entries.</p>
   **/
  WRHBTH,

  /**
   * <p>4 it is draw item source.</p>
   **/
  ITSR,

  /**
   * <p>5 lines are draw item sources.</p>
   **/
  ITSRLN,

  /**
   * <p>6 document and lines are draw item sources.</p>
   **/
  ITSRBTH,

  /**
   * <p>7 makes draw item entries.</p>
   **/
  DRAW,

  /**
   * <p>8 lines make draw item entries.</p>
   **/
  DRAWLN,

  /**
   * <p>9 document and lines make draw item entries.</p>
   **/
  DRAWBTH,

  /**
   * <p>10 document is source and lines make draw item entries.</p>
   **/
  ITSRDRAWLN,

  /**
   * <p>11 document is source and also make draw item.</p>
   **/
  ITSRDRAW;
}
