package com.hope.service.impl;

import com.hope.domain.pojo.SeckillVoucher;
import com.hope.domain.pojo.Voucher;
import com.hope.mapper.VoucherMapper;
import com.hope.service.VoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class VoucherServiceImpl implements VoucherService {

    @Autowired
    private VoucherMapper voucherMapper;

    @Override
    public void saveVoucher(Voucher voucher) {
        int rows = voucherMapper.saveVoucher(voucher);
        if (rows == 0) {
            log.error("保存普通优惠券失败: {}", voucher);
            throw new RuntimeException("保存普通优惠券失败");
        }
    }

    @Override
    public void saveSecKillVoucher(SeckillVoucher seckillVoucher) {
        int rows = voucherMapper.saveSecKillVoucher(seckillVoucher);
        if (rows == 0) {
            log.error("保存秒杀优惠券失败: {}", seckillVoucher);
            throw new RuntimeException("保存秒杀优惠券失败");
        }
    }

    @Override
    @Transactional
    public void addSecKillVoucher(Voucher voucher) {
        log.info("优惠卷具体信息为 voucher:{}",voucher);
        // 保存优惠券
        saveVoucher(voucher);
        // 保存秒杀信息
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());
        saveSecKillVoucher(seckillVoucher);
    }

    @Override
    public Voucher getVoucherById(Long voucherId) {
        Voucher voucherSeckillById = voucherMapper.getVoucherSeckillById(voucherId);
        if (voucherSeckillById != null) {
            return voucherSeckillById;
        }
        Voucher voucherById = voucherMapper.getVoucherById(voucherId);
        return voucherById;
    }



    @Override
    public List<Voucher> listVouchers(Long merchantId) {
        System.out.println("listVouchers");
        return voucherMapper.listVouchers(merchantId);
    }

    @Override
    public boolean updateVoucher(Voucher voucher) {
        int row1 = voucherMapper.updateVoucher(voucher);
        return row1 > 0 ;
    }

    @Override
    public boolean updateVoucherSeckill(Voucher voucher) {
        int row1 = voucherMapper.updateVoucher(voucher);
        int row2 = voucherMapper.updateVoucherSeckill(voucher);
        return row1 > 0 && row2 > 0;
    }

    @Override
    public boolean deleteVoucher(Long id) {
//        int row1= voucherMapper.deleteVoucherId;
        return false;
    }

    @Override
    public boolean deleteVoucherSeckill(Long id) {
//        return voucherMapper.deleteVoucherSeckill;
        return false;
    }
}
