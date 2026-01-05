package com.hope.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_list") // 1. 统一表名
public class OrderList {
    @TableId
    private Long id;
    private String orderNumber;
    private Long userId;
    private Long merchantId;
    private Long address_id;
    
    private BigDecimal totalPrice;
    private BigDecimal deliveryFee;
    private BigDecimal discountPrice;

    private Integer status; 
    private Integer isDeleted; 

    private LocalDateTime payTime;
    private LocalDateTime cancelTime;
    private String remark;
    
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}