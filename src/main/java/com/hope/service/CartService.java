package com.hope.service;

import com.hope.domain.pojo.Cart;
import com.hope.domain.pojo.Result;

public interface CartService {
    Result createCart(Cart cart);

    Result deleteCart(Long id);

    Result getCart(Long id);

    Result getCartList(Long merchantId);

    Result updateCart(Cart cart);

    Result deleteALLCart(Long cartId);
}
