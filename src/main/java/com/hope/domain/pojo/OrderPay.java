package com.hope.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_pay")
// 对应数据库表order_pay，已有的字段足够存储支付记录
public class OrderPay {
    private Long id;
    private Long orderId; // 关联订单ID
    private Integer payMethod; // 1-支付宝，2-微信（后续可扩展）
    private String tradeNo; // 支付宝交易号
    private BigDecimal price; // 支付金额
    
    private Integer status; // 0-未支付，1-已支付，2-支付失败
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}