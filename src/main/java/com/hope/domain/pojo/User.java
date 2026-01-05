package com.hope.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * id,username,nickname,email,password,avatar,age,createTime,updateTime,status
 */
public class User {

    private Long id;

    private String username;  // 账号

    private String nickname;  // 昵称

    private String email;     // 邮箱

    private String info;

    private String password;  // 密码（密文）

    private String avatar;    // 头像地址

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private int status;   // 0健康 1删除 2异常

    // 新增角色字段：0-普通用户，1-商家, 2-大管理员
    private int role = 0; // 默认普通用户
}
