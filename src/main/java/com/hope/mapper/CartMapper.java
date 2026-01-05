package com.hope.mapper;

import com.hope.domain.dto.CartDTO;
import com.hope.domain.pojo.Cart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {
    int createCart(Cart cart);
    
    List<CartDTO> getCartListByMerchant(Long userId, Long merchantId);

    List<CartDTO> getCartList(Long userId,Long merchantId);

    int deleteCart(Cart cart);        // 删除

    Cart getCart(Long id, Long userId);          // 单条查询
    
    List<Cart> getCartByMerchant(Long merchantId, Long userId);
    
    int updateCart(Cart cart);      // 更新

    int deleteALLCart(Long id);

    CartDTO findByUserAndSku(Long userId, Long skuId);

    int updateCartQuantity(CartDTO existingCart);

    CartDTO findByUserAndSkuDel(Long userId, Long skuId);

    int deleteCartPhysically(Long id);

    int cleanCartByUserIdMerchant(Long userId,Long merchantId);
}
