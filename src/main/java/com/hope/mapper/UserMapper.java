package com.hope.mapper;

import com.hope.domain.dto.LoginFormDTO;
import com.hope.domain.dto.UserDTO;
import com.hope.domain.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("select * from user where id = #{id} ")
    User getUserAllInfo(Long id);

    @Select("select id, username, nickname, avatar, role, email, info from user where id = #{id} ")
    UserDTO getUserInfo(Long id);

    User login(LoginFormDTO user);

    @Insert("insert into user (username, password, email) values (#{username},#{password},#{email})")
    int register(LoginFormDTO user);

    @Update("update user set password = #{password} where username = #{username} and email = #{email}")
    int forget(LoginFormDTO user);

//    后面少了再加
    @Select("select id, username, password, email from user where username=#{name}")
    User findUserByName(String name);

    @Select("select id, username, nickname, email, avatar from user where username=#{name}")
    UserDTO findUserByUsername(String name);

    int edit(User user);

    @Update("update user set avatar = #{avatar} where id = #{id}")
    int updateAvatar(String avatar,Long id);

    @Select("select * from user where username=#{username}")
    User selectByUsername(String username);

    @Select("select * from user where email=#{toEmail}")
    User selectByEmail(String toEmail);

    @Select("select * from user where id=#{id}")
    User selectById(Long id);

    @Update("update user set password = #{password} where id = #{id}")
    void modifyPassword(long id, String password);
}
