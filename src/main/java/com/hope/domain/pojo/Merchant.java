package com.hope.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {
//    这里缺少了营业时间time
    private Long id;

    private String name;            // 商家名称

    private String avatar;          // Logo

    private String description;     // 商家描述

    private BigDecimal deliveryFee; // 配送费

    private BigDecimal minOrderAmount; // 起送价

    private Double score;           // 评分

    private String address;         // 商家地址

    private String email;           // 商家电话（表注释写用邮箱）

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer status;         // 0健康 1删除 2异常

    private Long userId;
}
