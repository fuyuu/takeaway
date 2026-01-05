package com.hope.mapper;

import com.hope.domain.dto.DishSpuDTO;
import com.hope.domain.pojo.Dish;
import com.hope.domain.pojo.DishSku;
import com.hope.domain.pojo.DishSpu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishMapper {
    /**
     * 插入菜品数据
     * @param dish
     */
    int insert(Dish dish);

    int createDishSpu(DishSpu dishSpu);

    int deleteDishSpu(Long id);

    DishSpu getDishSpu(Long id);

    DishSpu getDishSpuSPU(Long id);

    List<DishSpu> getDishSpuList();

    int updateDishSpu(DishSpu dishSpu);

    List<DishSpu> getDishSpuListSkuByMerchantId(Long merchantId);

    List<DishSpu> getDishSpuWithSkuByMerchantId(Long merchantId);

    List<DishSpuDTO> getDishSpuListWithSku();

    List<DishSku> getSkuBySpuId(Long spuId);

    DishSku getSkuById(Long skuId);
}
