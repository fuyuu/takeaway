package com.hope.service;

import com.hope.domain.pojo.SeckillVoucher;
import com.hope.domain.pojo.Voucher;

import java.util.List;

public interface VoucherService {

    void saveVoucher(Voucher voucher);

    void saveSecKillVoucher(SeckillVoucher seckillVoucher);

    void addSecKillVoucher(Voucher voucher);

    // 根据id查询 普通 or seckill 优惠券
    Voucher getVoucherById(Long id);

    // 查询 所有 优惠券
    List<Voucher> listVouchers(Long merchantId);

    // 更新 普通 优惠券
    boolean updateVoucher(Voucher voucher);

    // 更新 秒杀 优惠券
    boolean updateVoucherSeckill(Voucher voucher);

    // 删除 普通 优惠券
    boolean deleteVoucher(Long id);

    // 删除 秒杀 优惠券
    boolean deleteVoucherSeckill(Long id);
}
