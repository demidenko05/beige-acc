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

import java.util.Map;

import org.beigesoft.mdl.IReqDt;
import org.beigesoft.acc.mdlp.AcStg;
import org.beigesoft.acc.mdlp.TxDst;
import org.beigesoft.ws.mdl.EItmTy;
import org.beigesoft.ws.mdlb.AItmPri;
import org.beigesoft.ws.mdlp.TrdStg;
import org.beigesoft.ws.mdlp.Buyer;
import org.beigesoft.ws.mdlp.Cart;
import org.beigesoft.ws.mdlp.CartLn;

/**
 * <p>Service that retrieve/create buyer's shopping cart, make cart totals
 * after any line action, etc.
 * This is shared non-transactional service.</p>
 *
 * @author Yury Demidenko
 */
public interface ISrCart {

  /**
   * <p>Empties Cart.</p>
   * @param pRvs request scoped vars
   * @param pBuyr buyer
   * @throws Exception - an exception
   **/
  void emptyCart(Map<String, Object> pRvs, Buyer pBuyr) throws Exception;

  /**
   * <p>Get/Create Cart.</p>
   * @param pRvs request scoped vars
   * @param pRqDt Request Data
   * @param pIsNeedToCreate if need to create, e.g. "NO" for deleting item from
   *  cart, "YES" for adding one.
   * @param pIsBuAuth buyer must be authorized
   * @return shopping cart or null
   * @throws Exception - an exception
   **/
  Cart getCart(Map<String, Object> pRvs, IReqDt pRqDt, boolean pIsNeedToCreate,
    boolean pIsBuAuth) throws Exception;

  /**
   * <p>Refresh cart totals by seller cause line inserted/changed/deleted.</p>
   * @param pRvs request scoped vars
   * @param pTs TrdStg
   * @param pCartLn affected cart line
   * @param pAs Accounting Settings
   * @param pTxRules NULL if not taxable
   * @throws Exception - an exception.
   **/
  void mkCartTots(Map<String, Object> pRvs, TrdStg pTs, CartLn pCartLn,
    AcStg pAs, TxDst pTxRules) throws Exception;

  /**
   * <p>Reveal shared tax rules for cart. It also makes buyer-regCustomer.</p>
   * @param pRvs request scoped vars
   * @param pCart cart
   * @param pAs Accounting Settings
   * @return tax rules, NULL if not taxable
   * @throws Exception - an exception.
   **/
  TxDst revTxRules(Map<String, Object> pRvs, Cart pCart,
    AcStg pAs) throws Exception;

  /**
   * <p>Handle event cart currency changed.</p>
   * @param pRvs request scoped vars
   * @param pCart cart
   * @param pAs Accounting Settings
   * @param pTs TrdStg
   * @throws Exception - an exception.
   **/
  void hndCurrChg(Map<String, Object> pRvs,
    Cart pCart, AcStg pAs, TrdStg pTs) throws Exception;

  /**
   * <p>Handle event cart delivering or line changed.</p>
   * @param pRvs request scoped vars
   * @param pCart cart
   * @param pTxRules Tax Rules
   * @throws Exception - an exception.
   **/
  void hndCartChg(Map<String, Object> pRvs,
    Cart pCart, TxDst pTxRules) throws Exception;

  /**
   * <p>Deletes cart line.</p>
   * @param pRvs request scoped vars
   * @param pCartLn cart line
   * @param pTxRules Tax Rules
   * @throws Exception - an exception.
   **/
  void delLine(Map<String, Object> pRvs,
    CartLn pCartLn, TxDst pTxRules) throws Exception;

  /**
   * <p>Makes cart line. Tax category, price, seller are already done.</p>
   * @param pRvs request scoped vars
   * @param pCartLn cart line
   * @param pAs Accounting Settings
   * @param pTs TrdStg
   * @param pTxRules NULL if not taxable
   * @param pRedoPr redo price
   * @param pRedoTxc redo tax category
   * @throws Exception - an exception.
   **/
  void mkLine(Map<String, Object> pRvs, CartLn pCartLn, AcStg pAs, TrdStg pTs,
    TxDst pTxRules, boolean pRedoPr, boolean pRedoTxc) throws Exception;

  /**
   * <p>Reveals item's price descriptor.</p>
   * @param pRvs request scoped vars
   * @param pTs TrdStg
   * @param pBuyer Buyer
   * @param pItType Item Type
   * @param pItId Item ID
   * @return item's price descriptor or exception
   * @throws Exception - an exception
   **/
  AItmPri<?, ?> revItmPri(Map<String, Object> pRvs, TrdStg pTs,
    Buyer pBuyer, EItmTy pItType, Long pItId) throws Exception;
}
