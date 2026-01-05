package com.hope.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("Tag")
public class Tag {
    private Long id;

    private String name;

    private Long type;

    private Integer sort;

    private Integer status;
}
