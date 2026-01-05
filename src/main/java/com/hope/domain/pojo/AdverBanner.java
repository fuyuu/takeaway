package com.hope.domain.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("adver_banner")
public class AdverBanner {

    /**
     * 广告横幅ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 图片URL
     */
    private String image;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述信息
     */
    private String description;
}