package com.hope.util;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.hope.util.RedisConstant.*;

@Slf4j
@Component
public class CacheClient {

    private final StringRedisTemplate stringRedisTemplate;

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void set(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), expireTime, timeUnit);
    }

    // 设置逻辑过期时间
    public void setWithLogicalExpire(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        // 设置逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(expireTime)));
        // 写入 redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    // 设置 缓存穿透  JSONUtil.toBean     JSONUtil.toJsonStr
    public <R, ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback,
                              Long expireTime, TimeUnit timeUnit) throws JsonProcessingException {
        // 1 redis 的缓存key
        String key = keyPrefix + id;
        // 2 从 redis 查询商铺 信息
        String json = stringRedisTemplate.opsForValue().get(key);
//        if (StringUtils.isNotBlank(json)) {
        if (StrUtil.isNotBlank(json)) {
            // 3 redis 里面存在就直接返回商铺
            return JSONUtil.toBean(json, type);
        }
        // 判断的不是空值
        if (json != null) {
            return null; // 缓存的空值
        }
        // 4 不存在  则查询数据库
        R r = dbFallback.apply(id);
        // 5 数据库不存在 则报错误 数据库没有
        if (r == null) {
            // 将空值写入到redis里面    返回错误信息
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        // 6 写入redis 将数据库里面的插入 redis objectMapper
        //stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(merchantWithDishSpu));
        // 7 返回
        return r;
    }

    // 缓存击穿 ( 互斥锁 解决 redis 过期时间缓存复杂的业务 短时间的查询发现没有-将复杂更新缓存的东西-锁起来 其他的查询语句返回之前的不一致的数据 )
    public <R, ID> R queryWithMutex(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback,
                                                   Long expireTime, TimeUnit timeUnit) throws JsonProcessingException {
        // 1 redis 的缓存key
        String key = keyPrefix + id;
        // 2 从 redis 查询商铺 信息
        String json = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(json)) {
            // 3 redis 里面存在就直接返回商铺
            return JSONUtil.toBean(json, type);
        }
        // 判断的不是空值
        if ("".equals(json)) {
            return null; // 缓存的空值
        }
        // 4 实现缓存重建
        // 4.1 获取互斥锁
        String lockKey = "lock:merchant:" + id;
        R r = null;
        try {
            boolean tryLock = tryLock(lockKey);
            // 4.2 判断获取锁是否成功
            if (!tryLock) {
                // 4.3 失败 休眠
                Thread.sleep(50);
                return queryWithMutex(keyPrefix, id, type, dbFallback, expireTime, timeUnit);
            }
            // 4.4 获取锁成功  则查询数据库
            r = dbFallback.apply(id);
            // 模拟重建的延时
            Thread.sleep(200);
            // 5 数据库不存在 则报错误 数据库没有
            if (r == null) {
                // 将空值写入到redis里面
                stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            // stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(r),CACHE_MERCHANT_TTL,TimeUnit.MINUTES);
            this.set(key, r, expireTime, timeUnit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 7 释放互斥锁
            unLock(lockKey);
        }
        // 8 返回
        return r;
    }

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    // 缓存击穿 ( 逻辑过期解决 先判断缓存存不存在 在判断缓存有没有过期 过期异步处理查询数据库更新缓存 )
    public <R, ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type, Function<ID, R> dbFallback,
                                          Long expireTime, TimeUnit timeUnit) throws JsonProcessingException {
        // 1 redis 的缓存key
        String key = keyPrefix + id;
        // 2 从 redis 查询商铺 信息
        String json = stringRedisTemplate.opsForValue().get(key);
        System.out.println(json);
        if (StrUtil.isBlank(json)) {
            // 3 未命中缓存 则返回空值
            return null;
        }
        // 4 命中缓存 将json字符串转化为 MerchantDTO 对象
        RedisData bean = JSONUtil.toBean(json, RedisData.class);
        R r=JSONUtil.toBean((JSONObject) bean.getData(), type);
        LocalDateTime deadLine = bean.getExpireTime();
        System.out.println("lock");
        // 5 判断缓存是否过期
        if (deadLine.isAfter(LocalDateTime.now())) {
            // 5.2 未过期,直接返回店铺数据
            return r;
        }
        // 6 已过期 缓存重建 已过期
        // 6.1 获取互斥锁
        System.out.println("lock");
        String lockKey = LOCAL_MERCHANT_KEY + id;
        boolean lock = tryLock(lockKey);
        // 6.2 判断是否获取锁成功
        // 6.3 成功 则开启独立线程,实现缓存重建
        if (lock) {
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                // 重建缓存
                try {
                    R apply = dbFallback.apply(id);
                    System.out.println(apply);
                    this.setWithLogicalExpire(key, apply, expireTime, timeUnit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }finally {
                    unLock(lockKey);
                }
            });
        }
        // 6.4 失败 则返回过期的商铺信息
        return r;
    }


    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unLock(String key) {
        stringRedisTemplate.delete(key);
    }
}
