package com.hope.service.impl;

import com.hope.domain.pojo.AdverBanner;
import com.hope.domain.pojo.Result;
import com.hope.mapper.AdverBannerMapper;
import com.hope.service.AdverBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdverBannerServiceImpl implements AdverBannerService {
    @Autowired
    public AdverBannerMapper adverBannerMapper;
    @Override
    public Result getPhotos2() {
        List<AdverBanner> adverBanners = adverBannerMapper.selectList(null);
        System.out.println(adverBanners);
        return adverBanners == null
                ? Result.fail("暂无数据")
                : Result.ok(adverBanners);
    }

    @Override
    public Result getPhotos() {
        List<AdverBanner> adverBanners = adverBannerMapper.selectList(null);
        System.out.println(adverBanners);
        return adverBanners == null
                ? Result.fail("暂无数据")
                : Result.ok(adverBanners);
    }
}
