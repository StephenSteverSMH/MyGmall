package com.stever.gmall.service;

import com.stever.gmall.bean.OmsCartItem;

import java.util.List;

public interface CartService {
    OmsCartItem ifCartExistedByUser(String memberId, String skuId);

    void addCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem cartItem);

    void flushCartCache(String memberId);

    List<OmsCartItem> cartList(String userId);
}
