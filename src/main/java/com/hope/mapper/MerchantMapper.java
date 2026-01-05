package com.hope.mapper;

import com.hope.domain.dto.MerchantDTO;
import com.hope.domain.pojo.Merchant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MerchantMapper {

    @Insert("insert into merchant" +
            "(id,name,avatar,description,delivery_fee,min_order_amount,score,address,email,user_id) " +
            "values (#{id},#{name},#{avatar},#{description},#{deliveryFee},#{minOrderAmount}," +
            "#{score},#{address},#{email},#{userId})")
    int createMerchant(Merchant merchant);

//    删除采用假删除设置状态值为 1   0--商家健康 1--商家删除 2--商家异常
    @Update("update merchant set status = 1, update_time=now() where id=#{id} and user_id=#{userId}")
    int deleteMerchant(Long id, Long userId);

    @Select("select id, name, avatar, description, delivery_fee, " +
            "min_order_amount, score, address, email, create_time, update_time, status, user_id" +
            " from merchant where id=#{id} and status = 0")
    Merchant getMerchant(Long id);

    @Update("update merchant set name=#{name}, avatar=#{avatar}, description=#{description}, " +
            "delivery_fee=#{deliveryFee}, min_order_amount=#{minOrderAmount}, score=#{score}, " +
            "address=#{address}, email=#{email}, user_id=#{userId}, update_time=now()" +
            "where id=#{id} and user_id=#{userId}")
    int updateMerchant(Merchant merchant);

    @Select("SELECT * FROM merchant WHERE status =0 ORDER BY create_time DESC")
    List<Merchant> getMerchantList();

    @Select("select count(1) from merchant where id =#{id}")
    int findByUserId(Long userId);

//    MerchantDTO findById(Long id);

    @Select("select count(1) from merchant where user_id =#{userId}")
    int existsById(Long userId);

    MerchantDTO getMerchantWithDishSpu(Long merchantId);

    List<MerchantDTO> listMerchantWithDishSpu();

    List<MerchantDTO> listMerchantWithCategory(Long id);
}
