package com.hope.service.impl;

import com.hope.domain.dto.CategoryDTO;
import com.hope.domain.dto.MerchantDTO;
import com.hope.domain.pojo.AdverBanner;
import com.hope.domain.pojo.Category;
import com.hope.domain.pojo.Result;
import com.hope.mapper.CategoryMapper;
import com.hope.service.CategoryService;
import com.hope.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    public CategoryMapper categoryMapper;

    @Override
    public Result add(Category category) {
        int rows = categoryMapper.insert(category);
        if (rows <= 0) {
            log.warn("新增分类失败，无数据插入");
            return Result.fail("新增分类失败");
        }
        return Result.ok();
    }

    @Override
    public Result remove(Long id) {
        long userId = Long.parseLong(ThreadLocalUtil.get());
        int rows = categoryMapper.deleteById(id,userId);
        if (rows <= 0) {
            log.warn("删除分类失败，ID: {}",id);
            return Result.fail("删除失败，分类不存在");
        }
        return Result.ok("删除分类成功");
    }

    @Override
    public Result update(Category category) {
        int rows = categoryMapper.updateById(category);
        if (rows <= 0) {
            log.warn("更新分类失败，category: {}",category);
            return Result.fail("删除失败，分类不存在");
        }
        return Result.ok("更新分类成功");
    }

    @Override
    public Result findById(Long id) {
        Category category = categoryMapper.selectById(id);
        return category == null
                ? Result.fail("分类不存在")
                : Result.ok(category);
    }

    @Override
    public Result list(Long merchantId) {
        List<Category> categories = categoryMapper.selectList(merchantId);
        return categories == null
                ? Result.fail("暂无数据")
                : Result.ok(categories);
    }

    @Override
    public Result listMerchantCategory() {
        List<CategoryDTO> categories = categoryMapper.listMerchantCategory();
        System.out.println(categories);
        return categories == null
                ? Result.fail("暂无数据")
                : Result.ok(categories);
    }

    @Override
    public Result listFoodCategory(Long id) {
        List<CategoryDTO> categories = categoryMapper.listFoodCategory(id);
        System.out.println(categories);
        return categories == null
                ? Result.fail("暂无数据")
                : Result.ok(categories);
    }


    @Override
    public Result listShopByTag(String tag) {
        List<MerchantDTO> merchantDTOS = categoryMapper.selectByTag(tag);
        System.out.println(merchantDTOS);
        return merchantDTOS == null
                ? Result.fail("暂无数据")
                : Result.ok(merchantDTOS);
    }

}
