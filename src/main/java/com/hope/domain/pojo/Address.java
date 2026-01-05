package com.hope.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address  {
    private Long id;

    private String name;   // 收件人姓名

    private String email;  // 收件人电话（字段名按表来）

    private String location;

    private int top;       // 是否默认地址（Y/N 或 1/0，按业务定）

    private Long userId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer status;         // 0健康 1删除 2异常
}
