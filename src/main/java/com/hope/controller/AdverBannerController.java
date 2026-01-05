package com.hope.controller;

import com.hope.domain.pojo.Result;
import com.hope.service.AdverBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banner")
/**
 * 这是首页广告横幅的实现
 */
public class AdverBannerController {

    @Autowired
    public AdverBannerService adverBannerService;

    @GetMapping("/photos")
    public Result getPhotos() {
        return adverBannerService.getPhotos();
    }
}
