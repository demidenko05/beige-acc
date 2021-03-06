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

package org.beigesoft.ws.srv;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.beigesoft.mdl.CmnPrf;
import org.beigesoft.mdlp.UsPrf;
import org.beigesoft.srv.INumStr;
import org.beigesoft.ws.mdl.EItmSpTy;
import org.beigesoft.ws.mdlb.AItmSpf;
import org.beigesoft.ws.mdlp.CatGs;
import org.beigesoft.ws.mdlp.ItmSp;
import org.beigesoft.ws.mdlp.ChoSp;
import org.beigesoft.ws.mdlp.I18CatGs;
import org.beigesoft.ws.mdlp.I18Trd;
import org.beigesoft.ws.mdlp.I18ChoSp;
import org.beigesoft.ws.mdlp.I18ItmSp;
import org.beigesoft.ws.mdlp.TrdStg;
import org.beigesoft.ws.mdlp.AddStg;
import org.beigesoft.ws.mdlp.ItmSpGr;

/**
 * <p>Helper that is used in JSP. E.g. it makes catalog name by given CatGs,
 * List&lt;I18CatGs&gt; and language.</p>
 *
 * @author Yury Demidenko
 */
public class UtlTrJsp {

  /**
   * <p>Service print number.</p>
   **/
  private INumStr numStr;

  /**
   * <p>It makes item specifics string.</p>
   * @param pRvs additional param
   * @param pItemSpecLst AItmSpf<?> list
   * @return appearance
   **/
  public final String itmSpfStr(final Map<String, Object> pRvs,
    final List<AItmSpf<?, ?>> pItemSpecLst) {
    AddStg astg = (AddStg) pRvs.get("tastg");
    UsPrf upf = (UsPrf) pRvs.get("upf");
    CmnPrf cpf = (CmnPrf) pRvs.get("cpf");
    StringBuffer sb = new StringBuffer();
    boolean wasGrStart = false;
    ItmSpGr spGrWs = null;
    if (astg.getShtms() !=  null) {
      sb.append(astg.getShtms());
    }
    for (AItmSpf<?, ?> gs : pItemSpecLst) {
      if (gs.getSpec().getInLst()
        && !gs.getSpec().getTyp().equals(EItmSpTy.IMAGE)) {
        if (gs.getSpec().getGrp() == null || spGrWs == null
          || !gs.getSpec().getGrp().getIid().equals(spGrWs.getIid())) {
          if (wasGrStart) {
            if (astg.getSghtme() != null) {
              sb.append(astg.getSghtme());
            }
            if (astg.getSpGrSp() != null) {
              sb.append(astg.getSpGrSp());
            }
          }
          wasGrStart = true;
          if (astg.getSghtms() != null) {
            sb.append(astg.getSghtms());
          }
          if (gs.getSpec().getGrp() != null
            && gs.getSpec().getGrp().getTmpls() != null) {
            String grst = gs.getSpec().getGrp().getTmpls()
              .getVal().replace(":SPECGRNM", gs.getSpec().getGrp().getNme());
            sb.append(grst);
          }
        }
        String val1 = "";
        String val2 = "";
        if (gs.getSpec().getTyp().equals(EItmSpTy.TEXT)) {
          val1 = gs.getStr1();
        } else if (gs.getSpec().getTyp().equals(EItmSpTy.BIGDECIMAL)) {
          val1 = numStr.frmt(gs.getNum1().toString(), cpf.getDcSpv(),
            cpf.getDcGrSpv(), Integer.valueOf(gs.getLng1().intValue()),
              upf.getDgInGr());
          if (gs.getStr1() != null) {
            val2 = gs.getStr1();
          }
        } else if (gs.getSpec().getTyp().equals(EItmSpTy.INTEGER)) {
          val1 = gs.getLng1().toString();
          if (gs.getStr1() != null) {
            val2 = gs.getStr1();
          }
       } else if (gs.getSpec().getTyp().equals(EItmSpTy.CHOOSEABLE_SPECIFICS)) {
          val1 =  gs.getStr1();
        } else {
          continue;
        }
        String htmd;
        if (gs.getSpec().getHtmt() != null) {
          htmd = gs.getSpec().getHtmt().getVal();
        } else if (gs.getSpec().getGrp() != null
          && gs.getSpec().getGrp().getTmpld() != null) {
          htmd = gs.getSpec().getGrp().getTmpld().getVal();
        } else {
          htmd = " <b>:SPECNM:</b> :VAL1:VAL2";
        }
        String spdet = htmd.replace(":SPECNM", gs.getSpec().getNme());
        spdet = spdet.replace(":VAL1", val1);
        spdet = spdet.replace(":VAL2", val2);
        if (gs.getSpec().getGrp() != null && spGrWs != null
          && gs.getSpec().getGrp().getIid().equals(spGrWs.getIid())) {
          sb.append(astg.getSpeSp() + spdet);
        } else {
          sb.append(spdet);
        }
        spGrWs = gs.getSpec().getGrp();
      }
    }
    if (astg.getSghtme() != null) {
      sb.append(astg.getSghtme());
    }
    if (astg.getShtme() != null) {
      sb.append(astg.getShtme());
    }
    return sb.toString();
  }

  /**
   * <p>It makes rate. If it's low with huge decimal places, e.g. 1/62.44,
   * then input value will be -62.44.</p>
   * @param pRateIn input rate e.g. -62.44
   * @return input unchanged one or dericed with 15 DP e.g. 0.01601537476
   **/
  public final BigDecimal mkRate(final BigDecimal pRateIn) {
    if (pRateIn.compareTo(BigDecimal.ZERO) == -1) {
      return BigDecimal.ONE.divide(pRateIn.negate(), 15, RoundingMode.HALF_UP);
    }
    return pRateIn;
  }

  /**
   * <p>It makes catalog name.</p>
   * @param pCatalog Catalog
   * @param pI18Catalogs I18 Catalogs
   * @param pLang language
   * @return catalog name
   **/
  public final String catlToStr(final CatGs pCatalog,
    final List<I18CatGs> pI18Catalogs, final String pLang) {
    if (pI18Catalogs != null) {
      for (I18CatGs icat : pI18Catalogs) {
        if (icat.getLng().getIid().equals(pLang) && pCatalog.getIid()
          .equals(icat.getHasNm().getIid())) {
          return icat.getNme();
        }
      }
    }
    return pCatalog.getNme();
  }

  /**
   * <p>It makes chooseable specifics name.</p>
   * @param pSpec Spec
   * @param pI18Specs I18 Specs
   * @param pLang language
   * @return specifics name
   **/
  public final String choSpToStr(final ChoSp pSpec,
    final List<I18ChoSp> pI18Specs, final String pLang) {
    if (pI18Specs != null) {
      for (I18ChoSp i18n : pI18Specs) {
        if (i18n.getLng().getIid().equals(pLang) && pSpec.getIid()
          .equals(i18n.getHasNm().getIid())) {
          return i18n.getNme();
        }
      }
    }
    return pSpec.getNme();
  }

  /**
   * <p>It makes specifics name.</p>
   * @param pSpec Spec
   * @param pI18Specs I18 Specs
   * @param pLang language
   * @return specifics name
   **/
  public final String specToStr(final ItmSp pSpec,
    final List<I18ItmSp> pI18Specs, final String pLang) {
    if (pI18Specs != null) {
      for (I18ItmSp i18n : pI18Specs) {
        if (i18n.getLng().getIid().equals(pLang) && pSpec.getIid()
          .equals(i18n.getHasNm().getIid())) {
          return i18n.getNme();
        }
      }
    }
    return pSpec.getNme();
  }

  /**
   * <p>It makes webstore name.</p>
   * @param pTrdStg Trading Settings
   * @param pI18TrdList I18 WebStore List
   * @param pLang language
   * @return catalog name
   **/
  public final String wsNme(final TrdStg pTrdStg,
    final List<I18Trd> pI18TrdList, final String pLang) {
    if (pI18TrdList != null) {
      for (I18Trd iws : pI18TrdList) {
        if (iws.getLng().getIid().equals(pLang)) {
          return iws.getNme();
        }
      }
    }
    return pTrdStg.getNme();
  }

  /**
   * <p>Getter for numStr.</p>
   * @return INumStr
   **/
  public final INumStr getNumStr() {
    return this.numStr;
  }

  /**
   * <p>Setter for numStr.</p>
   * @param pNumStr reference
   **/
  public final void setNumStr(final INumStr pNumStr) {
    this.numStr = pNumStr;
  }
}
