package com.hope.controller;

import com.hope.domain.pojo.Cart;
import com.hope.domain.pojo.Result;
import com.hope.service.CartService;
import com.hope.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    public CartService cartService;

    @PostMapping("/add")
    public Result createCart(@RequestBody Cart cart) {
        return cartService.createCart(cart);
    }

    @DeleteMapping("/{id}")
    public Result deleteCart(@PathVariable String id) {
        Long cartId = Long.valueOf(id);
        return cartService.deleteCart(cartId);
    }

    @DeleteMapping()
    public Result deleteALLCart() {
        Long cartId = Long.valueOf(ThreadLocalUtil.get());
        return cartService.deleteALLCart(cartId);
    }
    
    @GetMapping("/{id}")
    public Result getCart(@PathVariable Long id) {
        return cartService.getCart(id);
    }

    @GetMapping("/list/{id}")
    public Result getCartList(@PathVariable("id") Long merchantId) {
        return cartService.getCartList(merchantId);
    }

    @PutMapping("/{id}")
    public Result updateCart(@RequestBody Cart cart) {
        return cartService.updateCart(cart);
    }
}
