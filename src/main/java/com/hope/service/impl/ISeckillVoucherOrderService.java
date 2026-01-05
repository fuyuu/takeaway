package com.hope.service.impl;

import com.hope.domain.pojo.Result;
import com.hope.domain.pojo.Voucher;
import com.hope.domain.pojo.VoucherOrder;
import com.hope.mapper.VoucherMapper;
import com.hope.mapper.VoucherOrderMapper;
import com.hope.service.IVoucherOrderService;
import com.hope.util.RedisIdWorker;
import com.hope.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
@Service
@Slf4j
public class ISeckillVoucherOrderService implements IVoucherOrderService {

    @Autowired
    private VoucherMapper voucherMapper;
    @Autowired
    private VoucherOrderMapper voucherOrderMapper;
    @Autowired
    private RedisIdWorker redisIdWorker;

    @Override
    @Transactional
    public Result seckillVoucher(Long voucherId) {
        // 查询优惠卷
        Voucher voucher = voucherMapper.getVoucherById(voucherId);
        if (voucher == null) {
            return Result.fail("秒杀卷没有查询到"); //
        }
        // 秒杀是否开始
        if(voucher.getEndTime().isBefore(LocalDateTime.now())|| voucher.getBeginTime().isAfter(LocalDateTime.now())){
            return Result.fail("秒杀卷没有开始");
        }
        if(voucher.getStock()<1){
            return Result.fail("秒杀卷没有库存了");
        }
        // 扣减库存
        int affected = voucherMapper.decreaseSecKill(voucher);
        if (affected == 0) {
            // 扣减失败（库存不足）
            return Result.fail("秒杀券没有库存了");
        }
        // 创建秒杀卷订单
        save(voucher);
        // 返回
        return Result.ok();
    }

    @Override
    public Result saveVoucherOrder(Long voucherId) {
        // 查询优惠卷
        Voucher voucherById = voucherMapper.getVoucherById(voucherId);
        if (voucherById == null) {
            return Result.fail("卷没有查询到");
        }
        System.out.println("voucher.getType()"+voucherById.getType());
        System.out.println(voucherById);
        if(voucherById.getType()==0){
            //普通卷添加
            if(save(voucherById)==1){
                return Result.ok("success,抢到卷了");
            }else{
                return Result.fail("普通卷添加失败");
            }
        }
        Voucher voucher = voucherMapper.getVoucherSeckillById(voucherId);
        System.out.println("voucher:"+voucher);
        // 秒杀是否开始 抢卷
        if(voucher.getEndTime().isBefore(LocalDateTime.now())|| voucher.getBeginTime().isAfter(LocalDateTime.now())){
            return Result.fail("秒杀卷没有开始");
        }
        if(voucher.getStock()<1){
            return Result.fail("秒杀卷没有库存了");
        }
        Long userId = Long.valueOf(ThreadLocalUtil.get());
        synchronized (userId.toString().intern()) {
            // 扣减库存
            IVoucherOrderService currentProxy = (IVoucherOrderService) AopContext.currentProxy();
            return currentProxy.createSecKillVoucherOrder(userId, voucher);
        }
    }

    @Transactional
    public Result createSecKillVoucherOrder(Long userId, Voucher voucher) {
        int i = voucherMapper.queryUserIdVoucherId(userId, voucher.getId());
        System.out.println("wwwwwwww"+i);
        if(i > 0){
            return Result.fail("该用户已经买过了");
        }
        int affected = voucherMapper.decreaseSecKill(voucher);
        if (affected == 0) {
            // 扣减失败（库存不足）
            return Result.fail("秒杀券没有库存了");
        }
        // 创建秒杀卷订单
        save(voucher);
        return Result.ok("success,抢到卷了");
    }

    private int save(Voucher voucher) {
        VoucherOrder voucherOrder = new VoucherOrder();
        voucherOrder.setId(redisIdWorker.nextId("order"));  // 订单id
        voucherOrder.setUserId(Long.valueOf(ThreadLocalUtil.get()));   // 用户id
        voucherOrder.setVoucherId(voucher.getId());   // 代金券id
        int rows= voucherOrderMapper.saveVoucherOrder(voucherOrder);
        return rows;
    }
}
