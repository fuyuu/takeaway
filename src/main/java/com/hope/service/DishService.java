package com.hope.service;

import com.hope.domain.pojo.DishSku;
import com.hope.domain.pojo.DishSpu;
import com.hope.domain.pojo.Result;

import java.util.List;

public interface DishService {


    Result createDishSpu(DishSpu DishSpu);

    Result deleteDishSpu(Long id);

    Result getDishSpu(Long id);

    Result getDishSpuList();

    Result updateDishSpu(DishSpu DishSpu);

    Result getDishSpuListWithSku();

    Result<List<DishSku>> getSkuBySpuId(Long spuId);
}
