package com.hope.anno;

import java.lang.annotation.*;

/**
 * 标记需要记录日志的方法
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyLog {
    /** 业务描述 */
    String value() default "meiyouxie";

    /** 是否记录参数 */
    boolean logArgs() default true;

    /** 是否记录执行时间 */
    boolean logTime() default true;
}
