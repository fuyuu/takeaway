package com.hope.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 代金券订单实体
 *
 * @author your name
 */
@Data
@TableName("tb_voucher_order")
public class VoucherOrder {

    /** 主键 */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /** 下单的用户id */
    private Long userId;

    /** 购买的代金券id */
    private Long voucherId;

    /** 支付方式：1 余额支付；2 支付宝；3 微信 */
    private Integer payType;

    /** 订单状态：1 未支付；2 已支付；3 已核销；4 已退款 */
    private Integer status;

    /** 下单时间 */
    private LocalDateTime createTime;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 核销时间 */
    private LocalDateTime useTime;

    /** 退款时间 */
    private LocalDateTime refundTime;

    /** 最后更新时间 */
    private LocalDateTime updateTime;
}