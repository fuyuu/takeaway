package com.hope;

import com.hope.service.AdverBannerService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTest {
    @Resource
    public AdverBannerService adverBannerService;
    @Test
    public void test() {
        System.out.println(adverBannerService.getPhotos2());
    }
}
