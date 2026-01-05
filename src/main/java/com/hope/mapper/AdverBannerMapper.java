package com.hope.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hope.domain.pojo.AdverBanner;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdverBannerMapper extends BaseMapper<AdverBanner> {
     List<AdverBanner> getPhotos();
}
