package com.hope.aspect;

import com.hope.anno.MyLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.apache.ibatis.type.SimpleTypeRegistry.isSimpleType;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();
//    @Around("execution(* com.hope.service.impl..*(..))")
    @Around("@annotation(com.hope.anno.MyLog)")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取方法前面还有注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        MyLog myLog = method.getAnnotation(MyLog.class);

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = signature.getName();
        String description = myLog.value();
        String params = getParamsString(joinPoint, signature);
        // 3. 记录方法开始日志
        log.info("【方法开始】{} | 类：{} | 方法：{} | 参数：{}",
                description, className, methodName, params);
        //记录方法执行开始时间
        long begin = System.currentTimeMillis();
        //执行原始方法
        Object result = null;
        if (myLog.logArgs()) {
            result = joinPoint.proceed();
        }
        //记录方法执行结束时间
        long end = System.currentTimeMillis();
        //计算方法执行耗时
        log.debug("{}执行耗时: {}毫秒", joinPoint.getSignature(), end - begin);
        return result;
    }
    // ===== 方式2：所有 service.impl 方法（无描述，仅用于调试）=====
    @Around("execution(* com.hope.service.impl..*(..)) && !@annotation(com.hope.anno.MyLog)")
    public Object recordWithoutDescription(ProceedingJoinPoint joinPoint) throws Throwable {
        // 避免和上面的方法重复拦截（排除已加 @MyLog 的方法）
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("【通用监控】类：{} | 方法：{} | 参数：{}",
                className, methodName, Arrays.toString(joinPoint.getArgs()));

        long begin = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();

        log.debug("{} 执行耗时: {}毫秒", joinPoint.getSignature(), end - begin);
        return result;
    }

    private String getParamsString(ProceedingJoinPoint joinPoint, MethodSignature signature) {
        try {
            Object[] args = joinPoint.getArgs();
            String[] paramNames = signature.getParameterNames();

            if (args == null || args.length == 0) {
                return "无参数";
            }

            Map<String, Object> paramMap = new HashMap<>();
            for (int i = 0; i < args.length; i++) {
                String paramName = (paramNames != null && i < paramNames.length)
                        ? paramNames[i] : "arg" + i;

                // 对于复杂对象，使用JSON序列化；对于简单类型，直接toString
                if (args[i] == null) {
                    paramMap.put(paramName, "null");
                } else if (isSimpleType(args[i].getClass())) {
                    paramMap.put(paramName, args[i]);
                } else {
                    paramMap.put(paramName, objectMapper.writeValueAsString(args[i]));
                }
            }

            return objectMapper.writeValueAsString(paramMap);

        } catch (Exception e) {
            return "参数解析失败: " + e.getMessage();
        }
    }
}
