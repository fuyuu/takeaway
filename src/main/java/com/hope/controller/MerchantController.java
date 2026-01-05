package com.hope.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hope.domain.dto.MerchantDTO;
import com.hope.domain.pojo.Merchant;
import com.hope.domain.pojo.Result;
import com.hope.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/merchant")
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    @PostMapping("/create")
    public Result createMerchant(@RequestBody Merchant merchant) {
        return merchantService.createMerchant(merchant);
    }

    @PutMapping("/{id}")
    public Result deleteMerchant(@PathVariable Long id) {
        return merchantService.deleteMerchant(id);
    }

    @GetMapping("/{id}")
    public Result getMerchant(@PathVariable Long id) {
        return merchantService.getMerchant(id);
    }

    @GetMapping("/list")
    public Result getMerchantList() {
        System.out.println("getMerchantList");
        return merchantService.getMerchantList();
//        return merchantService.getMerchantWithDishesList();
    }

    // 获取单个商家及其菜品SPU
    @GetMapping("/{id}/spu")
    public Result<MerchantDTO> getMerchantWithDishSpu(@PathVariable Long id) throws JsonProcessingException {
        return merchantService.getMerchantWithDishSpu(id);
    }
    // 获取所有商家及其菜品SPU
    @GetMapping("/list-with-dishes")
    public Result<List<MerchantDTO>> listMerchantWithDishes() {
        return merchantService.listMerchantWithDishSpu();
    }
    @GetMapping("/list-with-category/{id}")
    public Result<List<MerchantDTO>> listMerchantWithCategory(@PathVariable Long id) {
        return merchantService.listMerchantWithCategory(id);
    }
    @PutMapping
    public Result updateMerchant(@RequestBody Merchant merchant) {
        return merchantService.updateMerchant(merchant);
    }
}
