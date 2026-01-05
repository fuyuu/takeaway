package com.hope.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_voucher")
public class Voucher {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long merchantId;
    private String title;
    private String subTitle;
    private String rules;
    private Integer payValue; // 单位：分
    private Integer actualValue; // 单位：分
    private Integer type; // 0:普通券, 1:秒杀券
    private Integer status; // 1:上架, 2:下架, 3:过期
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @TableField(exist=false)
    private Integer stock;

    @TableField(exist=false)
    private LocalDateTime beginTime;

    @TableField(exist=false)
    private LocalDateTime endTime;
}
