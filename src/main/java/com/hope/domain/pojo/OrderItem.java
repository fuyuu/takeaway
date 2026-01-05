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
@TableName("order_item")
public class OrderItem  {
    @TableId
    private Long id;
    private Long orderId;
    private Long spuId;
    private Long skuId;
    private String name;
    private String image;
    
    private BigDecimal price;
    private String spec;
    private Integer quantity;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
