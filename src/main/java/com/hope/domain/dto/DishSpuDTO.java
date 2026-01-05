package com.hope.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hope.domain.pojo.DishSku;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DishSpuDTO {

    @TableId(type = IdType.AUTO)
    private Long id;
    // 菜品名称
    private String name;
    // 菜品分类id
    private Long categoryId;
    // 商家id
    private Long merchantId;
    // 图片路径（OSS地址）
    private String image;
    // 菜品描述
    private String description;
    // 0=在售，1=下架
    private Integer status;
    //最低价格
    private BigDecimal price = BigDecimal.ZERO;
    // 月销售
    private int sales;

//    /**
//     * 创建时间 更新时间    创建者 更新者
//     */
//    private LocalDateTime createTime;
//    private LocalDateTime updateTime;
//    private Long createUser;
//    private Long updateUser;

    @TableField(exist = false)   // 告诉 MP：别去数据库找这一列
    private List<DishSku> skuList; // 查询后手动/自动填充

}
