package com.hope.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.hutool.json.JSONObject;
import com.hope.anno.MyLog;
import com.hope.domain.dto.MerchantDTO;
import com.hope.domain.pojo.Merchant;
import com.hope.domain.pojo.Result;
import com.hope.mapper.MerchantMapper;
import com.hope.service.MerchantService;
import com.hope.util.CacheClient;
import com.hope.util.RedisData;
import com.hope.util.ThreadLocalUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.hope.util.RedisConstant.*;

@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantMapper merchantMapper;

    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Resource
    private CacheClient cacheClient;


    @Override
    public Result createMerchant(Merchant merchant) {
        int byUserId = merchantMapper.findByUserId(merchant.getUserId());
        if (byUserId != 0) {
            return Result.fail("该用户已经申请过了在我们平台开店了");
        }
        int i = merchantMapper.createMerchant(merchant);
        return i>0 ? Result.ok("创建商家信息成功"):Result.fail("创建商家信息失败");
    }

    @Override
    public Result deleteMerchant(Long id) {
        merchantMapper.existsById(id);
        String uidStr = ThreadLocalUtil.get();
        if (uidStr == null) {
            return Result.fail("未登录");
        }
        Long userId = Long.parseLong(uidStr);
//        Long userId=Long.parseLong(ThreadLocalUtil.get());
        int i = merchantMapper.deleteMerchant(id,userId);
        return i>0 ? Result.ok("删除商家信息成功"):Result.fail("删除商家信息失败");
    }

    @Override
    public Result getMerchant(Long id) {
        Merchant merchant = merchantMapper.getMerchant(id);
        return null;
    }

    @Override
    @Transactional
    public Result updateMerchant(Merchant merchant) {
        try{
            // 1 先更新数据库
            int existsById = merchantMapper.existsById(merchant.getUserId());
            if (existsById == 0) {
                log.error("没有Userid为{}这个的商家", merchant.getUserId());
                return Result.fail("没有Userid为{" + merchant.getUserId() + "}这个的商家");
            }
            int i = merchantMapper.updateMerchant(merchant);
            if (i == 0) {
                log.error("删除商家更新失败,商家信息merchant:{}", merchant.getUserId());
                return Result.fail("删除商家更新失败");
            }
            // 2 删除缓存
            stringRedisTemplate.delete(CACHE_MERCHANT_KEY + merchant.getId());
            return Result.ok(merchant);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail("系统错误"+e.getMessage());
        }
    }

    @MyLog
    @Override
    public Result SelectClosed(Long id) {
        merchantMapper.existsById(id);
        String uidStr = ThreadLocalUtil.get();
        if (uidStr == null) {
            return Result.fail("未登录");
        }
        Long userId = Long.parseLong(uidStr);
//        Long userId=Long.parseLong(ThreadLocalUtil.get());
        int i = merchantMapper.deleteMerchant(id,userId);
        return i>0 ? Result.ok("删除商家信息成功"):Result.fail("删除商家信息失败");
    }

    @MyLog
    @Override
    public Result getMerchantList() {
//        String userId=stringRedisTemplate
        try{
            List<Merchant> merchantList = merchantMapper.getMerchantList();
            if (merchantList != null && !merchantList.isEmpty()) {
                return Result.ok(merchantList); // 假设Result有带数据的ok方法
            } else {
                return Result.fail("未查询到餐馆信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("系统错误getMerchantList"+e.getMessage());
        }
    }

    @Override
    public Result getMerchantWithDishesList() {
        System.out.println("getMerchantWithDishesList");
        return null;
    }

    @MyLog
    @Override
    // 根据商家id查询它的spu以及商家信息
    public Result<MerchantDTO> getMerchantWithDishSpu(Long merchantId) throws JsonProcessingException {
        // 缓存穿透
        // MerchantDTO merchant = queryWithPassThrough(merchantId);

        // 互斥锁解决缓存击穿问题
        // MerchantDTO merchantDTO = queryMerchantWithDishSpuMutex(merchantId);

        // 逻辑过期 解决缓存击穿
        // MerchantDTO merchantDTO = queryMerchantWithLogicalExpire(merchantId);

        // CacheClient 缓存穿透
        // MerchantDTO merchantDTO = cacheClient.
        //         queryWithPassThrough(CACHE_MERCHANT_KEY,merchantId,MerchantDTO.class,
        //                 id -> merchantMapper.getMerchantWithDishSpu(id),CACHE_MERCHANT_TTL,TimeUnit.MINUTES);

         //CacheClient 互斥锁解决缓存击穿问题
         MerchantDTO merchantDTO = cacheClient.
                 queryWithMutex(CACHE_MERCHANT_KEY,merchantId,MerchantDTO.class,
                         id -> merchantMapper.getMerchantWithDishSpu(id),CACHE_MERCHANT_TTL,TimeUnit.MINUTES);

        // CacheClient 逻辑过期 解决缓存击穿
//        MerchantDTO merchantDTO = cacheClient.
//                queryWithLogicalExpire(CACHE_MERCHANT_KEY,merchantId,MerchantDTO.class,
//                        id -> merchantMapper.getMerchantWithDishSpu(id),10L,TimeUnit.SECONDS);

        if (merchantDTO == null) {
            return Result.fail("店铺不存在 redis getMerchantWithDishSpu queryMerchantWithDishSpuMutex");
        }
        System.out.println("getMerchantWithDishSpu"+merchantDTO);
        return Result.ok(merchantDTO);
    }
    /*public Result<MerchantDTO> getMerchantWithDishSpu(Long merchantId) {
        MyLog.info("getMerchantWithDishSpu");
        try{
            // 1 redis 的缓存key
            String key = CACHE_MERCHANT_KEY+merchantId;
            // 2 从 redis 查询商铺 信息
            String merchantJson = stringRedisTemplate.opsForValue().get(key);
            if (StringUtils.isNotBlank(merchantJson)) {
//            if (StrUtil.isNotBlank(merchantJson)) {
                // 3 redis 里面存在就直接返回商铺
                MerchantDTO merchantDTO = objectMapper.readValue(merchantJson, MerchantDTO.class);
                System.out.println("redis merchantDTO:"+merchantDTO);
                return Result.ok(merchantDTO);
            }
            // 判断的不是空值
            if(merchantJson==null){
                // 返回一个错误信息
                return Result.fail("店铺信息不存在");
            }
            // 4 不存在  则查询数据库
            MerchantDTO merchantWithDishSpu = merchantMapper.getMerchantWithDishSpu(merchantId);
            // 5 数据库不存在 则报错误 数据库没有
            if (merchantWithDishSpu == null) {
                // 将空值写入到redis里面
                stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL, TimeUnit.MINUTES);
                return Result.fail("未查询到商家信息");
            }
            // 6 写入redis 将数据库里面的插入 redis objectMapper
//            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(merchantWithDishSpu));
            stringRedisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(merchantWithDishSpu),CACHE_MERCHANT_TTL,TimeUnit.MINUTES);
            // 7 返回
            return Result.ok(merchantWithDishSpu);
        } catch (Exception e) {
            MyLog.error("查询商家菜品失败", e);
            return Result.fail("系统异常，查询失败");
        }
    }*/

    // 缓存穿透 ( 解决的是缓存和数据库不存在的值,基本上是空值查询 一下都是这样子,上面是原版,下面是使用util CacheClient)
    public MerchantDTO queryMerchantWithDishSpuPassThrough1(Long merchantId) throws JsonProcessingException {
        // 1 redis 的缓存key
        String key = CACHE_MERCHANT_KEY + merchantId;
        // 2 从 redis 查询商铺 信息
        String merchantJson = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(merchantJson)) {
//            if (StrUtil.isNotBlank(merchantJson)) {
            // 3 redis 里面存在就直接返回商铺
            MerchantDTO merchantDTO = objectMapper.readValue(merchantJson, MerchantDTO.class);
            System.out.println("redis merchantDTO:"+merchantDTO);
            return merchantDTO;
        }
        // 判断的不是空值
        if ("".equals(merchantJson)) {
            return null; // 缓存的空值
        }

        // 4 不存在  则查询数据库
        MerchantDTO merchantWithDishSpu = merchantMapper.getMerchantWithDishSpu(merchantId);
        // 5 数据库不存在 则报错误 数据库没有
        if (merchantWithDishSpu == null) {
//                MyLog.error("getMerchantWithDishSpu error 未查询到商家信息");
            // 将空值写入到redis里面
            stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL, TimeUnit.MINUTES);
            return null;
        }
        // 6 写入redis 将数据库里面的插入 redis objectMapper
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(merchantWithDishSpu),CACHE_MERCHANT_TTL,TimeUnit.MINUTES);
        // 7 返回
        return merchantWithDishSpu;
    }

    // 缓存击穿 ( 互斥锁 解决 redis 过期时间缓存复杂的业务 短时间的查询发现没有-将复杂更新缓存的东西-锁起来 其他的查询语句返回之前的不一致的数据 )
    public MerchantDTO queryMerchantWithDishSpuMutex(Long merchantId) throws JsonProcessingException {
        /* 1 redis 的缓存key */
        String key = CACHE_MERCHANT_KEY + merchantId;
        /* 2 从 redis 查询商铺 信息 */
        String merchantJson = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(merchantJson)) {
//            if (StrUtil.isNotBlank(merchantJson)) {
            // 3 redis 里面存在就直接返回商铺
            MerchantDTO merchantDTO = objectMapper.readValue(merchantJson, MerchantDTO.class);
            System.out.println("redis merchantDTO:"+merchantDTO);
            return merchantDTO;
        }
        // 判断的不是空值
        if ("".equals(merchantJson)) {
            return null; // 缓存的空值
        }
        // 4 实现缓存重建
        // 4.1 获取互斥锁
        String lockKey = "lock:merchant:" + merchantId;
        MerchantDTO merchantWithDishSpu = null;
        try {
            boolean tryLock = tryLock(lockKey);
            // 4.2 判断获取锁是否成功
            if (!tryLock) {
                // 4.3 失败 休眠
                Thread.sleep(50);
                return queryMerchantWithDishSpuMutex(merchantId);
            }
            // 4.4 获取锁成功  则查询数据库
            merchantWithDishSpu = merchantMapper.getMerchantWithDishSpu(merchantId);
            // 模拟重建的延时
            Thread.sleep(200);
            // 5 数据库不存在 则报错误 数据库没有
            if (merchantWithDishSpu == null) {
    //                MyLog.error("getMerchantWithDishSpu error 未查询到商家信息");
                // 将空值写入到redis里面
                stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL, TimeUnit.MINUTES);
                return null;
            }
            // 6 写入redis 将数据库里面的插入 redis objectMapper
//            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(merchantWithDishSpu));
            stringRedisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(merchantWithDishSpu),CACHE_MERCHANT_TTL,TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 7 释放互斥锁
            unLock(lockKey);
        }
        // 8 返回
        return merchantWithDishSpu;
    }
    // 缓存击穿 ( 逻辑过期解决 先判断缓存存不存在 在判断缓存有没有过期 过期异步处理查询数据库更新缓存 )
    public MerchantDTO queryMerchantWithLogicalExpire(Long merchantId) throws JsonProcessingException {
        // 1 redis 的缓存key
        String key = CACHE_MERCHANT_KEY + merchantId;
        System.out.println("redis merchantDTO key:"+key);
        // 2 从 redis 查询商铺 信息
        String merchantJson = stringRedisTemplate.opsForValue().get(key);
        System.out.println("redis merchantDTO:"+merchantJson);
        if (StrUtil.isBlank(merchantJson)) {
            // 3 未命中缓存 则返回空值
            System.out.println("return null;");
            return null;
        }

        // 4 命中缓存 将json字符串转化为 MerchantDTO 对象
        RedisData bean = JSONUtil.toBean(merchantJson, RedisData.class);
        System.out.println("redis merchantDTO:"+bean);
        MerchantDTO merchant=JSONUtil.toBean((JSONObject) bean.getData(), MerchantDTO.class);
        System.out.println(merchant);
        LocalDateTime expireTime = bean.getExpireTime();
        // 5 判断缓存是否过期
        if (expireTime.isAfter(LocalDateTime.now())) {
            // 5.2 未过期,直接返回店铺数据
            return merchant;
        }
        // 6 已过期 缓存重建 已过期
        // 6.1 获取互斥锁
        String lockKey = LOCAL_MERCHANT_KEY + merchantId;
        boolean lock = tryLock(lockKey);
        // 6.2 判断是否获取锁成功
        // 6.3 成功 则开启独立线程,实现缓存重建
        if (lock) {
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                // 重建缓存
                try {
                    this.saveRedisData(merchantId,20L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    unLock(lockKey);
                }
            });
        }
        // 6.4 失败 则返回过期的商铺信息
        return merchant;
    }

    // 逻辑过期 设置数据
    @Override
    public void saveRedisData(Long id, Long expireTime) throws InterruptedException {
        // 1 查询数据库
        MerchantDTO merchant = merchantMapper.getMerchantWithDishSpu(id);
        Thread.sleep(200);
        // 2 封装逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(merchant);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireTime));
        System.out.println("过期时间: " + redisData.getExpireTime());
        System.out.println("当前时间: " + LocalDateTime.now());
        // 3 写入redis
        stringRedisTemplate.opsForValue().set(CACHE_MERCHANT_KEY+id, JSONUtil.toJsonStr(redisData));
    }

    @MyLog
    @Override
    public Result<List<MerchantDTO>> listMerchantWithCategory(Long id) {
        log.info("listMerchantWithCategory");
        try{
            List<MerchantDTO> merchantDTOList = merchantMapper.listMerchantWithCategory(id);
            if(merchantDTOList == null || merchantDTOList.isEmpty()) {
                log.error("listMerchantWithCategory error 为查询到数据");
                return Result.fail("为查询到数据");
            }
            return Result.ok(merchantDTOList);
        } catch (Exception e) {
            log.error("为查询到商家数据+"+e.getMessage());
            return Result.fail("系统异常，查询失败");
        }
    }

    @Override
    @MyLog
    public Result<List<MerchantDTO>> listMerchantWithDishSpu() {
        log.info("listMerchantWithDishSpu");
        try{
            List<MerchantDTO> merchantDTOList = merchantMapper.listMerchantWithDishSpu();
            if(merchantDTOList == null || merchantDTOList.isEmpty()) {
                log.error("listMerchantWithDishSpu error 为查询到数据");
                return Result.fail("为查询到数据");
            }
            return Result.ok(merchantDTOList);
        } catch (Exception e) {
            log.error("为查询到商家数据+"+e.getMessage());
            return Result.fail("系统异常，查询失败");
        }
    }

    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unLock(String key) {
        stringRedisTemplate.delete(key);
    }

}
