package com.hope.mapper;

import com.hope.domain.dto.CategoryDTO;
import com.hope.domain.dto.MerchantDTO;
import com.hope.domain.pojo.AdverBanner;
import com.hope.domain.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    int insert(Category category);

    int deleteById(Long id, long userId);

    int updateById(Category category);

    Category selectById(@Param("id") Long id);

    List<Category> selectList(@Param("merchantId") Long merchantId);

    List<CategoryDTO> listMerchantCategory();

    List<CategoryDTO> listFoodCategory(Long id);

    List<MerchantDTO> selectByTag(String tag);
}
