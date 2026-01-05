package com.hope.service.impl;

import com.hope.domain.dto.CartDTO;
import com.hope.domain.pojo.Cart;
import com.hope.domain.pojo.Result;
import com.hope.mapper.CartMapper;
import com.hope.mapper.MerchantMapper;
import com.hope.service.CartService;
import com.hope.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    public CartMapper cartMapper;
    
    @Autowired
    public MerchantMapper merchantMapper;

    @Override
    public Result createCart(Cart cart) {
//        Long userId=Long.parseLong(ThreadLocalUtil.get());
        String uidStr = ThreadLocalUtil.get();
        if (uidStr == null) {
            return Result.fail("未登录");
        }
        Long userId = Long.parseLong(uidStr);
        cart.setUserId(userId);
        // 2. 逻辑增强：查询该用户购物车中是否已存在该 SKU
        // 你需要在 Mapper 中补充这个 findByUserAndSku 的查询方法
        CartDTO existingCart = cartMapper.findByUserAndSku(userId, cart.getSkuId());

        if (existingCart != null) {
            // 3. 如果已存在，更新数量：原有数量 + 新加数量
            existingCart.setQuantity(existingCart.getQuantity() + cart.getQuantity());
            int row = cartMapper.updateCartQuantity(existingCart); // 修改后的更新方法
            if (row == 0) {
                return Result.fail("更新购物车数量失败");
            }
            log.info("购物车] 加购请求 cart={}", existingCart);
            return Result.ok(existingCart);
        } else {
            // 4. 如果不存在，才执行新增
            int row = cartMapper.createCart(cart);
            if (row == 0) {
                return Result.fail("加入购物车失败");
            }
            log.info("购物车] 加购请求 cart={}", cart);
            return Result.ok(cart);
        }
    }

    @Override
    @Transactional
    public Result deleteCart(Long id) {
        String uidStr = ThreadLocalUtil.get();
        if (uidStr == null) {
            return Result.fail("未登录");
        }
        Long userId = Long.parseLong(uidStr);
        log.info("id={},userId={}",id,userId);
//        Long userId=Long.parseLong(ThreadLocalUtil.get());
        // 1. 前端删除通常只传 id，所以必须先查数据库，获取当前要删除商品的 skuId 和 quantity。
        Cart cart = cartMapper.getCart(id,userId);
        if (cart == null) {
            return Result.fail("找不到该购物车记录");
        }
        // 2. 逻辑增强：查询该用户购物车中是否已存在 删除DEL 该 SKU
        // 你需要在 Mapper 中补充这个 findByUserAndSkuDel 的查询方法
        CartDTO historyDeletedCart = cartMapper.findByUserAndSkuDel(userId, cart.getSkuId());
        if (historyDeletedCart != null) {
            // 计算总数 = 历史删除的数量 + 当前要删除的数量
            int totalQuantity = historyDeletedCart.getQuantity() + cart.getQuantity();
            // 3. 如果已存在，更新数量：原有数量 + 新加数量
            historyDeletedCart.setQuantity(totalQuantity);
            int updateRow = cartMapper.updateCartQuantity(historyDeletedCart); // 修改后的更新方法
            if (updateRow > 0) {
                // 【关键】合并成功后，必须把当前这条记录“物理删除”
                // 否则数据库里会有两条记录（一条合并后的，一条刚逻辑删除的），导致数据冗余
                cartMapper.deleteCartPhysically(cart.getId());

                log.info("[购物车] 删除累积成功: 历史ID={}, 合并后数量={}", historyDeletedCart.getId(), totalQuantity);
                return Result.ok("删除并合并成功");
            }
            return Result.fail("合并删除记录失败");
        } else {
            // 4. 如果不存在，才执行新增
            int row = cartMapper.deleteCart(cart);
            if (row > 0) {
                log.info("[购物车] 首次放入回收站: id={},cart={}", cart.getId(),cart);
                return Result.ok("删除成功");
            }
            return Result.fail("删除失败");
        }
    }

    @Override
    public Result getCart(Long id) {
        String uidStr = ThreadLocalUtil.get();
        if (uidStr == null) {
            return Result.fail("未登录");
        }
        Long userId = Long.parseLong(uidStr);
        
        List<Cart> carts = cartMapper.getCartByMerchant(id, userId);
        log.error("晚上的说法二额呜呜 carts={}", carts);
        return carts != null ? Result.ok(carts) : Result.fail("未找到购物车记录");
    }

    @Override
    public Result getCartList(Long merchantId) {
        String oid = ThreadLocalUtil.get();
        if(oid==null){
            return Result.ok(List.of());
        }
        Long userId=Long.parseLong(oid);
        List<CartDTO> cartList = cartMapper.getCartListByMerchant(userId,merchantId);

        log.info("cartList:{}", cartList);
        return Result.ok(cartList);
    }

    @Override
    public Result updateCart(Cart cart) {
        log.info("[购物车] 更新请求: {}", cart);
        String oid = ThreadLocalUtil.get();
        if(oid==null){
            return Result.ok(List.of());
        }
        Long userId=Long.parseLong(oid);
        cart.setUserId(userId);
        int row = cartMapper.updateCart(cart);

        return row > 0 ? Result.ok("更新成功") : Result.fail("更新失败");
    }

    @Override
    public Result deleteALLCart(Long id) {
        log.info("[购物车] 删除请求, user_id: {}", id);
        int row = cartMapper.deleteALLCart(id);
        return row > 0 ? Result.ok("删除成功") : Result.fail("删除失败");
    }
}
