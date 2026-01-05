package com.hope.mapper;

import com.hope.domain.pojo.Address;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressMapper {
    @Insert("insert into address " +
            "(name, email, location, top, user_id) " +
            "Values " +
            "(#{name},#{email},#{location},#{top},#{userId})")
    int create(Address address);

    @Update("update address set status = 1 where id = #{id} and user_id = #{userId}")
    int delete(Long id, Long userId);

    @Select("select * from address where id = #{id} and user_id = #{userId} and status = 0")
    Address getAddressById(Long id, Long userId);

    @Update("update address set name= #{name}, email = #{email}," +
            "location = #{location}," +
            "top = #{top}, update_time = now() where id = #{id} and user_id = #{userId}")
    int update(Address address);

    @Select("select * from address where user_id = #{userId} and status = 0 " +
            "order by update_time desc")
    List<Address> listByUserId(Long userId);

    // 将用户的所有地址设为非默认（用于新增默认地址时互斥）
    @Update("update address set top = 0, update_time = now() " +
            "where user_id = #{userId} and status = 0 and top = 1")
    int updateNoTop(Long userId);

    // 将指定地址设为默认（需验证用户权限）
    @Update("update address set top = 1, update_time = now() " +
            "where id = #{id} and user_id = #{userId} and status = 0")
    int setTop(Long id, Long userId);
}
