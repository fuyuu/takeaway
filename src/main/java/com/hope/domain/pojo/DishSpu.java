package com.hope.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("dish_spu")
public class DishSpu {
    /**
     * 菜品ID（主键）
     */
    @TableId(type = IdType.AUTO) // 自增主键
    private Long id;

    /**
     * 菜品名称
     */
    private String name;

    /**
     dishSpu
     */
    private Long categoryId;

    /**
     * 所属餐厅ID
     */
    private Long merchantId;

    /**
     * 图片路径（OSS地址）
     */
    private String image;

    /**
     * 菜品描述
     */
    private String description;

    /**
     * 售卖状态（0=在售，1=下架）
     */
    private Integer status;

    /**
     * 创建时间 更新时间    创建者 更新者
     */
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long createId;
    private Long updateId;

    @TableField(exist = false)   // 告诉 MP：别去数据库找这一列
    private List<DishSku> skuList; // 查询后手动/自动填充
}