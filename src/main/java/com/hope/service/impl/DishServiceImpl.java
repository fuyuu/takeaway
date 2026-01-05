package com.hope.service.impl;

import com.hope.domain.dto.DishSpuDTO;
import com.hope.domain.pojo.DishSku;
import com.hope.domain.pojo.DishSpu;
import com.hope.domain.pojo.Result;
import com.hope.mapper.DishMapper;
import com.hope.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Override
    public Result createDishSpu(DishSpu dishSpu) {
        log.info("createDishSpu,DishSpu:{}", dishSpu);
        try{
            int rows = dishMapper.createDishSpu(dishSpu);
            if(rows == 0){
                log.warn("createDishSpu函数没有插入成功,DishSpu:{}",dishSpu);
                return Result.fail("createDishSpu   没有插入成功");
            }
            log.info("createDishSpu函数插入成功,DishSpu:{}",dishSpu);
            return Result.ok("createDishSpu 函数插入成功");
        } catch (Exception e) {
            log.error("创建菜品失败,dishSpu:{}", dishSpu, e);
            throw new RuntimeException("系统异常 createDishSpu",e);
        }
    }

    @Override
    public Result deleteDishSpu(Long id) {
        log.info("deleteDishSpu,id:{}",id);
        try{
            int rows = dishMapper.deleteDishSpu(id);
            if(rows == 0){
                log.warn("deleteDishSpu 函数没有删除成功,用户id:{}",id);
                return Result.fail("deleteDishSpu   没有删除成功");
            }
            log.info("deleteDishSpu 函数插入成功,用户id:{}",id);
            return Result.ok("deleteDishSpu 删除菜品成功");
        } catch (Exception e) {
            log.error("删除菜品失败,id:{}", id, e);
            throw new RuntimeException("系统异常 deleteDishSpu",e);
        }
    }

    @Override
    public Result getDishSpu(Long id) {
        log.info("getDishSpu,id:{}",id);
        try{
            DishSpu dishSpu = dishMapper.getDishSpu(id);
            if(dishSpu == null){
                log.warn("getDishSpu 函数没有查询成功,用户id:{}",id);
                return Result.fail("getDishSpu   没有查询成功");
            }
            log.info("getDishSpu 函数插入成功,dishSpu:{},用户id:{}",dishSpu,id);
            return Result.ok(dishSpu);
        } catch (Exception e) {
            log.error("查询菜品失败,id:{}", id, e);
            throw new RuntimeException("系统异常 getDishSpu",e);
        }
    }


    @Override
    public Result getDishSpuList() {
        log.info("getDishSpuList");
        try{
            List<DishSpu> dishSpuList = dishMapper.getDishSpuList();
            if(dishSpuList == null){
                log.warn("getDishSpuList 函数没有查询成功,dishSpuList:{}",dishSpuList);
                return Result.fail("getDishSpuList   没有查询成功");
            }
            log.info("getDishSpuList 函数插入成功,,dishSpuList:{}",dishSpuList);
            return Result.ok(dishSpuList);
        } catch (Exception e) {
            log.error("查询菜品列表失败", e);
            throw new RuntimeException("系统异常 getDishSpuList",e);
        }
    }


    @Override
    public Result getDishSpuListWithSku() {
        log.info("getDishSpuListWithSku");
        try {
            List<DishSpuDTO> dishSpuListWithSku = dishMapper.getDishSpuListWithSku();
            if(dishSpuListWithSku == null){
                log.warn("getDishSpuListWithSku 函数没有查询成功");
                return Result.fail("getDishSpuListWithSku   没有查询成功");
            }
            log.info("getDishSpuListWithSku 函数查询成功,dishSpuListWithSku:{}", dishSpuListWithSku);
            return Result.ok(dishSpuListWithSku);
        } catch (Exception e) {
            log.error("查询菜品列表失败", e);
            throw new RuntimeException("系统异常 getDishSpuListWithSku",e);
        }
    }

    @Override
    public Result<List<DishSku>> getSkuBySpuId(Long spuId) {
        log.info("查询SPU对应的SKU列表，spuId:{}", spuId);
        List<DishSku> skuList = dishMapper.getSkuBySpuId(spuId);
        if (skuList == null || skuList.isEmpty()) {
            return Result.fail("未查询到该菜品的规格信息");
        }
        log.info("查询SPU对应的SKU列表，skuList:{},spuId:{}", skuList,spuId);
        return Result.ok(skuList);
    }


    @Override
    public Result updateDishSpu(DishSpu dishSpu) {
        log.info("updateDishSpu,DishSpu:{}", dishSpu);
        try{
            int result = dishMapper.updateDishSpu(dishSpu);
            if(result == 0){
                log.warn("updateDishSpu 函数没有更新成功,DishSpu:{}",dishSpu);
                return Result.fail("updateDishSpu   没有更新成功");
            }
            log.info("updateDishSpu 更新菜品成功,DishSpu:{}",dishSpu);
            return Result.ok("updateDishSpu 更新菜品成功");
        } catch (Exception e) {
            log.error("更新菜品列表失败,DishSpu:{}", dishSpu, e);
            throw new RuntimeException("系统异常 updateDishSpu",e);
        }
    }
}
