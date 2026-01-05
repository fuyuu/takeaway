package com.hope.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private Long id;          // 购物车id

    private Integer quantity; // 商品数量

    private Long userId;      // 用户id

    private Long skuId;   // 产品id

    private Long spuId;
    
    private int status;
    
    private int isDeleted;
}
