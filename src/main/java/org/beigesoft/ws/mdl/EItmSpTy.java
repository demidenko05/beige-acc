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
 * Specifics Type described how to treat (edit/print/filter) assigned specifics.
 * </p>
 *
 * @author Yury Demidenko
 */
public enum EItmSpTy {

  /**
   * <p>0, default, printed as text. Use FILE_EMBEDDED instead cause
   * its' more suitable, powerful and I18N ready.</p>
   **/
  TEXT,

  /**
   * <p>1, for specifics like "Weight", longValue2 may hold unit of
   * measure ID and stringValue1 UOM name (def lang) to improve
   * performance, longValue1 holds decimal places - 2 default.</p>
   **/
  BIGDECIMAL,

  /**
   * <p>2, for specifics like "MemorySize",
   * stringValue may hold unit of measure.</p>
   **/
  INTEGER,

  /**
   * <p>3, stringValue1 hold URL to image,
   * stringValue2 - uploaded file path if it was uploaded.</p>
   **/
  IMAGE,

  /**
   * <p>4, stringValue1 hold URL to image,
   * stringValue2 - uploaded file path if it was uploaded.
   * Image that belongs to set of images ordered and gathered
   * (they must have adjacent indexes) by itsIndex,
   * longValue1 may hold "showSizeTypeClass".</p>
   **/
  IMAGE_IN_SET,

  /**
   * <p>5, stringValue1 hold URL to file, e.g. "get brochure",
   * stringValue2 - uploaded file path if it was uploaded.</p>
   **/
  FILE,

  /**
   * <p>6, show it on page, stringValue1 hold URL to file e.g. a PDF/HTML,
   * stringValue2 - uploaded file path if it was uploaded,
   * longValue1 may hold "showSizeTypeClass", e.g. class=1 means
   * show 30% of page size. Main file is on base language,
   * stringValue3 may holds comma separated other languages (e.g. "ru,fr" means
   * that there are two files with these languages with the same name
   * plus "_ru.html".</p>
   **/
  FILE_EMBEDDED,

  /**
   * <p>7, stringValue1 hold URL.</p>
   **/
  LINK,

  /**
   * <p>8, show HTML page. stringValue1 hold URL HTML page,
   * longValue1 may hold "showSizeClass". Use FILE_EMBEDDED instead.</p>
   **/
  LINK_EMBEDDED,

  /**
   * <p>10, longValue1 hold ID of chosen from list of ChooseableSpecifics,
   * stringValue1 hold appearance to improve performance,
   * and so does longValue2 - ChooseableSpecificsType.
   * This is the mostly used method.</p>
   **/
  CHOOSEABLE_SPECIFICS;
}
