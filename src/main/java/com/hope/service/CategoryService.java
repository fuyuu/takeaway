package com.hope.service;

import com.hope.domain.pojo.Category;
import com.hope.domain.pojo.Result;

import java.util.List;

public interface CategoryService {

    Result add(Category category);

    Result remove(Long id);

    Result update(Category category);

    Result<Category> findById(Long id);

    Result<List<Category>> list(Long merchantId);

    Result listMerchantCategory();

    Result listFoodCategory(Long id);

    Result listShopByTag(String tag);

}
