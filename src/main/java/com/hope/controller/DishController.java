package com.hope.controller;

import com.hope.domain.pojo.DishSku;
import com.hope.domain.pojo.DishSpu;
import com.hope.domain.pojo.Result;
import com.hope.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping("/create")
    public Result createDishSpu(@RequestBody DishSpu dishSpu) {
        return dishService.createDishSpu(dishSpu);
    }

    @PutMapping("/{id}")
    public Result deleteDishSpu(@PathVariable Long id) {
        return dishService.deleteDishSpu(id);
    }

    @GetMapping("/{id}")
    public Result getDishSpu(@PathVariable Long id) {
        return dishService.getDishSpu(id);
    }

    @GetMapping("/list")
    public Result getDishSpuList() {
        return dishService.getDishSpuList();
    }

    @GetMapping("/list-spu-sku")
    public Result getDishSpuListWithSku() {
        return dishService.getDishSpuListWithSku();
    }

    @PutMapping("/update")
    public Result updateDishSpu(@RequestBody DishSpu DishSpu) {
        return dishService.updateDishSpu(DishSpu);
    }

    //获取单个spu的所有sku列表
    @GetMapping("/spu/{spuId}/sku")
    public Result getSkuBySpuId(@PathVariable Long spuId) {
        return dishService.getSkuBySpuId(spuId);
    }

}
