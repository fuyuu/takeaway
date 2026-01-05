package com.hope.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hope.domain.dto.MerchantDTO;
import com.hope.domain.pojo.Merchant;
import com.hope.domain.pojo.Result;

import java.util.List;

public interface MerchantService {
    Result createMerchant(Merchant merchant);

    Result deleteMerchant(Long id);

    Result getMerchant(Long id);

    Result<List<Merchant>> getMerchantList();

    Result updateMerchant(Merchant merchant);

    Result SelectClosed(Long id);

    Result getMerchantWithDishesList();

    // 获取单个商家及其菜品SPU
    Result<MerchantDTO> getMerchantWithDishSpu(Long merchantId) throws JsonProcessingException;

    // 获取所有商家及其菜品SPU
    Result<List<MerchantDTO>> listMerchantWithDishSpu();

    void saveRedisData(Long id, Long expireTime) throws InterruptedException;

    Result<List<MerchantDTO>> listMerchantWithCategory(Long id);
}
