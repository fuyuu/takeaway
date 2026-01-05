package com.hope.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderListDTO{

    private Long id;
    private Long userId;
    private Long addressId; 
    private Long merchantId;
    private String orderNumber;
    
    private BigDecimal totalPrice;
    private BigDecimal deliveryFee;
    private BigDecimal discountPrice;

    /**
     * 订单状态统一化：
     * 0-待支付, 1-待发货, 2-待收货, 3-已完成, 4-已取消, 5-已退款
     */
    private Integer status;

    /**
     * 逻辑删除标识：0-可见, 1-隐藏
     */
    private Integer isDeleted;

    private List<Item> carts;

    @Data
    public static class Item {
        private Long spuId;
        private Long skuId;  // 必须，用于定位具体规格（如：大杯、少冰）
        private Integer quantity;
    }
}