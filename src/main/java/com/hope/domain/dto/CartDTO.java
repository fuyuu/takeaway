package com.hope.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO{
    private Long id;          // 购物车id

    private Integer quantity; // 商品数量

    private Long userId;      // 用户id

    private Long skuId;   // 产品id

    private Long spuId;

    private int status;

    private int isDeleted;
    
    private BigDecimal price;
    
    private String name;
    
    private String image;
}
