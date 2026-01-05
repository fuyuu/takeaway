package com.hope.exception;

import com.hope.domain.pojo.Result;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionaHandler {
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.fail("服务器异常"+e.getMessage());
    }
    /**
     * 处理所有运行时异常（兜底）
     */
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
//        MyLog.error("全局异常捕获", e);
        return Result.fail("服务器异常"+e.getMessage());
    }
    /**
     * 处理所有运行时异常（异常）
     */
//    @ExceptionHandler(Exception.class)
//    public Result handleException(Exception e) {
////        MyLog.error("全局异常捕获", e);
//        e.printStackTrace();
//        return Result.fail(StringUtils.isEmpty(e.getMessage())?"操作失败":e.getMessage());
//    }
}
