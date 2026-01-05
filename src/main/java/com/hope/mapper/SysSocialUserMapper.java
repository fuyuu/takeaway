package com.hope.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hope.domain.pojo.SysSocialUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SysSocialUserMapper extends BaseMapper<SysSocialUser> {

    SysSocialUser findBySourceAndUuid(String source, String uuid);

    SysSocialUser findByUserIdAndSource(Long userId, String source);

    @Insert("INSERT INTO sys_social_user (user_id, source, uuid, access_token) " +
            "VALUES (#{userId}, #{source}, #{uuid}, #{accessToken})")
    int insert(SysSocialUser socialUser);

    int updateAccessToken(SysSocialUser socialUser);

    @Update("UPDATE sys_social_user SET access_token = #{accessToken} " +
            "WHERE source = #{source} AND uuid = #{uuid}")
    int update(SysSocialUser socialUser);
}
