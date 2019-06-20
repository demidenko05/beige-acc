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

package org.beigesoft.acc.mdlp;

import java.math.BigDecimal;

import org.beigesoft.mdl.IHasId;
import org.beigesoft.mdl.AHasVr;

/**
 * <p>Model of item in warehouse place.</p>
 *
 * @author Yury Demidenko
 */
public class WrhItm extends AHasVr<WrhItmId>
  implements IHasId<WrhItmId> {

  /**
   * <p>Composite ID.</p>
   **/
  private WrhItmId iid;

  /**
   * <p>Warehouse place, not null, PK.</p>
   **/
  private WrhPl wrhp;

  /**
   * <p>Item, not null, PK.</p>
   **/
  private Itm itm;

  /**
   * <p>Uom, not null, PK.</p>
   **/
  private Uom uom;

  /**
   * <p>Items left (the rest) to draw.</p>
   **/
  private BigDecimal itLf = BigDecimal.ZERO;

  /**
   * <p>Getter for iid.</p>
   * @return WrhItmId
   **/
  @Override
  public final WrhItmId getIid() {
    return this.iid;
  }

  /**
   * <p>Setter for iid.</p>
   * @param pIid reference
   **/
  @Override
  public final void setIid(final WrhItmId pIid) {
    this.iid = pIid;
    if (this.iid == null) {
      this.wrhp = null;
      this.itm = null;
      this.uom = null;
    } else {
      this.wrhp = this.iid.getWrhp();
      this.itm = this.iid.getItm();
      this.uom = this.iid.getUom();
    }
  }

  //Customized setters:
  /**
   * <p>Setter for wrhp.</p>
   * @param pWrhp reference
   **/
  public final void setWrhp(final WrhPl pWrhp) {
    this.wrhp = pWrhp;
    if (this.wrhp != null) {
      if (this.iid == null) {
        this.iid = new WrhItmId();
      }
      this.iid.setWrhp(this.wrhp);
    } else {
      this.iid = null;
    }
  }

  /**
   * <p>Setter for itm.</p>
   * @param pItm reference
   **/
  public final void setItm(final Itm pItm) {
    this.itm = pItm;
    if (this.itm != null) {
      if (this.iid == null) {
        this.iid = new WrhItmId();
      }
      this.iid.setItm(this.itm);
    } else {
      this.iid = null;
    }
  }

  /**
   * <p>Setter for uom.</p>
   * @param pUom reference
   **/
  public final void setUom(final Uom pUom) {
    this.uom = pUom;
    if (this.wrhp != null) {
      if (this.iid == null) {
        this.iid = new WrhItmId();
      }
      this.iid.setUom(this.uom);
    } else {
      this.iid = null;
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for wrhp.</p>
   * @return WrhPl
   **/
  public final WrhPl getWrhp() {
    return this.wrhp;
  }

  /**
   * <p>Getter for itm.</p>
   * @return Itm
   **/
  public final Itm getItm() {
    return this.itm;
  }

  /**
   * <p>Getter for uom.</p>
   * @return Uom
   **/
  public final Uom getUom() {
    return this.uom;
  }

  /**
   * <p>Getter for itLf.</p>
   * @return BigDecimal
   **/
  public final BigDecimal getItLf() {
    return this.itLf;
  }

  /**
   * <p>Setter for itLf.</p>
   * @param pItLf reference
   **/
  public final void setItLf(final BigDecimal pItLf) {
    this.itLf = pItLf;
  }
}
