package com.hope.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysSocialUser {

    private Long id;

    private Long userId;  // 关联 sys_user 表的主键ID

    private String source;  // 来源: GITHUB, GITEE, WECHAT, GOOGLE

    private String uuid;  // 第三方平台的唯一ID

    private String accessToken;  // OAuth拿到的Token

    private LocalDateTime createTime;
}
