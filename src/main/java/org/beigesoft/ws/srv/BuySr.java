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
import java.util.HashMap;
import java.util.Arrays;
import java.util.Date;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.hnd.IHndSpam;
import org.beigesoft.rdb.IOrm;
import org.beigesoft.ws.mdlp.Buyer;

/**
 * <p>Buyer's service.</p>
 *
 * @author Yury Demidenko
 */
public class BuySr implements IBuySr {

  /**
   * <p>ORM service.</p>
   **/
  private IOrm orm;

  /**
   * <p>Spam handler.</p>
   **/
  private IHndSpam spamHnd;

  /**
   * <p>Get authorized buyer. It refresh authorized buyer last time
   * and set request variable  "buyr".</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @return authorized buyer or null
   * @throws Exception - an exception
   **/
  @Override
  public final Buyer getAuthBuyr(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    String buyerIdStr = pRqDt.getCookVl("cBuyerId");
    if (buyerIdStr == null) {
      this.spamHnd.handle(pRvs, pRqDt, 1, "Buyer. has no cBuyerId!");
      return null;
    }
    Map<String, Object> vs = new HashMap<String, Object>();
    Long buyerId = Long.valueOf(buyerIdStr);
    Buyer buyer = new Buyer();
    buyer.setIid(buyerId);
    getOrm().refrEnt(pRvs, vs, buyer);
    if (buyer.getIid() == null) {
      this.spamHnd.handle(pRvs, pRqDt, 1, "Buyer. DB has no cBuyerId: "
        + buyerIdStr);
      return null;
    }
    if (buyer.getEml() == null || buyer.getBuSeId() == null) {
      this.spamHnd.handle(pRvs, pRqDt, 1, "Buyer. Unauthorized cBuyerId: "
        + buyerIdStr);
      return null;
    }
    String buSeId = pRqDt.getCookVl("buSeId");
    if (!buyer.getBuSeId().equals(buSeId)) {
      this.spamHnd.handle(pRvs, pRqDt, 1000,
        "Buyer. Authorized invasion cBuyerId: " + buyerIdStr);
      return null;
    }
    long now = new Date().getTime();
    if (now - buyer.getLsTm() > 1800000L) {
      this.spamHnd.handle(pRvs, pRqDt, 0,
        "Buyer. Authorized exceed cBuyerId/ms: " + buyerIdStr + "/"
          + (now - buyer.getLsTm()));
      return null;
    }
    buyer.setLsTm(now);
    String[] fieldsNames = new String[] {"iid", "ver", "lsTm"};
    Arrays.sort(fieldsNames);
    vs.put("fdNms", fieldsNames);
    buyer.setLsTm(now);
    this.orm.update(pRvs, vs, buyer);
    pRvs.put("buyr", buyer);
    return buyer;
  }

  /**
   * <p>Get authorized or not buyer.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @return buyer or null
   * @throws Exception - an exception
   **/
  @Override
  public final Buyer getBuyr(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
    Long buyerId = null;
    String buyerIdStr = pRqDt.getCookVl("cBuyerId");
    if (buyerIdStr != null && buyerIdStr.length() > 0) {
       buyerId = Long.valueOf(buyerIdStr);
    }
    Buyer buyer = null;
    if (buyerId != null) {
      Map<String, Object> vs = new HashMap<String, Object>();
      buyer = new Buyer();
      buyer.setIid(buyerId);
      getOrm().refrEnt(pRvs, vs, buyer);
      if (buyer.getIid() == null) {
        buyer = null;
      }
    }
    if (buyer != null && buyer.getEml() != null && buyer.getBuSeId() != null) {
      String buSeId = pRqDt.getCookVl("buSeId");
      if (!buyer.getBuSeId().equals(buSeId)) {
        this.spamHnd.handle(pRvs, pRqDt, 100,
          "Buyer. Authorized invasion? cBuyerId: " + buyerIdStr);
        //buyer also might clears cookie, so it's need new authorization
        //new/free buyer will be used till authorization:
        buyer = null;
      }
    }
    return buyer;
  }

  /**
   * <p>Creates buyer without saving into DB.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @return created buyer will be unsaved into DB!
   * @throws Exception - an exception
   **/
  @Override
  public final Buyer createBuyr(final Map<String, Object> pRvs,
    final IReqDt pRqDt) throws Exception {
      Map<String, Object> vs = new HashMap<String, Object>();
    Buyer buyer = null;
    vs.put("DbCrdpLv", 1);
    vs.put("TxDstdpLv", 1);
    List<Buyer> brs = getOrm().retLstCnd(pRvs, vs, Buyer.class,
      "where FRE=1 and PWD is null");
    vs.clear();
    if (brs.size() > 0) {
      double rd = Math.random();
      if (rd > 0.5) {
        buyer = brs.get(brs.size() - 1);
      } else {
        buyer = brs.get(0);
      }
      buyer.setPwd(null);
      buyer.setEml(null);
      buyer.setFre(false);
    }
    if (buyer == null) {
      buyer = new Buyer();
      buyer.setIsNew(true);
      buyer.setNme("newbe" + new Date().getTime());
    }
    return buyer;
  }

  //Simple getters and setters:
  /**
   * <p>Geter for orm.</p>
   * @return IOrm
   **/
  public final IOrm getOrm() {
    return this.orm;
  }

  /**
   * <p>Setter for orm.</p>
   * @param pOrm reference
   **/
  public final void setOrm(final IOrm pOrm) {
    this.orm = pOrm;
  }

  /**
   * <p>Getter for spamHnd.</p>
   * @return IHndSpam
   **/
  public final IHndSpam getSpamHnd() {
    return this.spamHnd;
  }

  /**
   * <p>Setter for spamHnd.</p>
   * @param pSpamHnd reference
   **/
  public final void setSpamHnd(final IHndSpam pSpamHnd) {
    this.spamHnd = pSpamHnd;
  }
}
