package com.hope.domain.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("category")
public class Category {
    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 唯一名字 */
    private String name;

    /** 1-菜品分类 2-套餐分类 3-商家分类 */
    private Integer type;

    /** 排序字段 */
    @TableField(fill = FieldFill.INSERT)
    private Integer sort;

    /** 1-启用 0-禁用 */
    private Integer status;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 创建人id */
    private Long forId;

    @TableField(exist = false)
    private List<Tag> tags;
}