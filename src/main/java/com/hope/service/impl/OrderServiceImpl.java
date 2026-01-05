package com.hope.service.impl;

import com.hope.domain.dto.OrderListDTO;
import com.hope.domain.pojo.*;
import com.hope.mapper.*;
import com.hope.service.OrderService;
import com.hope.util.OrderNumberUtil;
import com.hope.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private MerchantMapper merchantMapper; // 用于查询商家信息（配送费等）
    @Autowired
    private DishMapper dishMapper; // 用于查询商品SPU/SKU信息
    @Autowired
    private CartMapper cartMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createOrder(OrderListDTO orderListDTO) {
        // 1. 校验参数
        if (orderListDTO.getMerchantId() == null || orderListDTO.getTotalPrice() == null
                || orderListDTO.getCarts() == null || orderListDTO.getCarts().isEmpty()) {
            return Result.fail("订单参数不完整");
        }

        // 2. 获取当前登录用户ID（从ThreadLocal，参考CartServiceImpl的实现）
        Long userId = Long.parseLong(ThreadLocalUtil.get());
        if (userId == null) {
            return Result.fail("用户还没有登录，就想起飞");
        }
        orderListDTO.setUserId(userId);

        // 3. 查询商家信息（获取配送费等）
        Merchant merchant = merchantMapper.getMerchant(orderListDTO.getMerchantId());
        if (merchant == null) {
            return Result.fail("商家不存在");
        }
        
        // 4. 生成订单号并设置初始状态
        String orderNumber = OrderNumberUtil.generate();
        orderListDTO.setOrderNumber(orderNumber);
        orderListDTO.setStatus(0);
        orderListDTO.setDiscountPrice(BigDecimal.ZERO);
        /*   todo   得修改  */
        orderListDTO.setAddressId(3L);
        
        log.info("开始处理订单创建请求: {}", orderListDTO);
        // 5. 插入主订单（获取自增 ID）
        int orderRows = orderMapper.createOrder(orderListDTO);
        if (orderRows <= 0) {
            return Result.fail("主订单插入失败");
        }
        Long orderId = orderListDTO.getId();
        // 7. 遍历订单项，补全信息并插入
        List<OrderListDTO.Item> items = orderListDTO.getCarts();
        for (OrderListDTO.Item item : items) {
            // 7.1 校验订单项参数
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new RuntimeException("订单项参数错误（SKU ID或数量无效）");
            }

            // 直接根据前端传来的具体规格ID查询，而不是查出列表后再盲目取第一个
            DishSku targetSku = dishMapper.getSkuById(item.getSkuId());

            if (targetSku == null || targetSku.getSkuStatus() != 0) {
                throw new RuntimeException("您选择的规格已下架或不存在，skuId: " + item.getSkuId());
            }
            
            // 7.3 查询SPU信息（获取商品名称、图片等）
            DishSpu spu = dishMapper.getDishSpuSPU(item.getSpuId());
            System.out.println("spu: " + spu);
            if (spu == null) {
                throw new RuntimeException("未找到SPU信息，spuId: " + item.getSpuId());
            }

            // 7.4 构造订单项对象
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId); // 关联主订单ID
            orderItem.setSpuId(item.getSpuId());
            orderItem.setSkuId(item.getSkuId());   // 前端传过来的值
            orderItem.setName(spu.getName()); // 商品名称（取自SPU）
            orderItem.setImage(spu.getImage()); // 商品图片（取自SPU）
            orderItem.setPrice(targetSku.getPrice()); // 商品单价（取自SKU）
            orderItem.setQuantity(item.getQuantity()); // 数量
            // 7.5 插入订单项
            int itemRows = orderItemMapper.insert(orderItem);
            if (itemRows <= 0) {
                throw new RuntimeException("订单项插入失败，skuId: " + item.getSkuId());
            }
        }
        int deletedRows = cartMapper.cleanCartByUserIdMerchant(userId,orderListDTO.getMerchantId());
        if (deletedRows <= 0) {
            return Result.fail("消除购物车失败");
        }
        log.info("用户 {} 的购物车已清空，删除行数: {}", userId, deletedRows);
        log.info("订单创建成功，订单号: {}", orderNumber);
        return Result.ok(orderNumber);
    }


    @Override
    public Result deleteOrder(Long id) {
        return null;
    }

    @Override
    public Result getOrder(Long id) {
        return null;
    }

    @Override
    public Result getOrderList() {
        return null;
    }

    @Override
    public Result updateOrder(OrderList orderList) {
        return null;
    }

}
