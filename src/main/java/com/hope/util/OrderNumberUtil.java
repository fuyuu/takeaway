package com.hope.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class OrderNumberUtil {

    /** 日期格式化：yyyyMMdd */
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private OrderNumberUtil() { }

    /**
     * 生成订单号：yyyyMMdd + 32位去掉中划线的UUID
     * 例：20251010b3b9c3e3f3e34e4f4g4h4i4j4k4l4m4n4o4p4q4r4s4t4u4v4w4x4y4z4a4b4c
     */
    public static String generate() {
        return LocalDate.now().format(DATE_FMT) +"-"+ UUID.randomUUID().toString().replace("-", "");
    }

    /* 测试 */
    public static void main(String[] args) {
        System.out.println(generate()); // 20251010b3b9c3e3f3e34e4f4g4h4i4j4k4l4m4n4o4p4q4r4s4t4u4v4w4x4y4z4a4b4c
    }
}