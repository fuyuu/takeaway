package com.hope.controller;

import com.hope.domain.pojo.Category;
import com.hope.domain.pojo.Result;
import com.hope.service.CategoryService;
import com.hope.util.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    public final CategoryService categoryService;

    /**
     * 新增
     */
    @PostMapping
    public Result save(@RequestBody Category category) {
        long userId = Long.parseLong(ThreadLocalUtil.get());
        category.setId(userId);
        return categoryService.add(category);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        return categoryService.remove(id);
    }

    /**
     * 修改
     */
    @PutMapping
    public Result update(@RequestBody Category category) {
        return categoryService.update(category);
    }

    /**
     * 单查
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    /**
     * 列表（支持 merchantId 过滤）
     */
    /*@GetMapping
    public Result list(@RequestParam Long merchantId) {
        return categoryService.list(merchantId);
    }*/
    @GetMapping("/list-merchant")
    public Result listMerchantCategory() {
        return categoryService.listMerchantCategory();
    }

    @GetMapping("/list-food/{id}")
    public Result listFoodCategory(@PathVariable Long id) {
        return categoryService.listFoodCategory(id);
    }
    @GetMapping("/tag")
    public Result selectByTag(@RequestParam String tag) {
        return categoryService.listShopByTag(tag);
    }
}