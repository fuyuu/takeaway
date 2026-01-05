package com.hope.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hope.domain.pojo.DishSpu;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("dish_sku")
public class DishSkuDTO {

    /**
     * SKU ID（主键）
     */
    private Long id;

    /**
     * 关联的SPU ID（外键，关联dish_spu表的id）
     */
    private Long spuId;

    /**
     * 分量标识（1=小份，2=大份）
     */
    private Integer heft;

    /**
     * SKU状态（0=在售，1=售罄）
     */
    private Integer skuStatus;

    /**
     * 规格1（如“小份/大份”）
     */
    private String spec1;

    /**
     * 规格2（如“微辣/中辣/特辣”）
     */
    private String spec2;

    /**
     * 单价（精确到分）
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Integer stockNum;

    // 非列字段
    @TableField(exist = false)
    private DishSpu spu;
}
