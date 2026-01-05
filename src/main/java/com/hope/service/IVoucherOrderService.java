package com.hope.service;

import com.hope.domain.pojo.Result;
import com.hope.domain.pojo.SeckillVoucher;
import com.hope.domain.pojo.Voucher;

public interface IVoucherOrderService {

    Result seckillVoucher(Long voucherId);

    Result saveVoucherOrder(Long voucherId);

    Result createSecKillVoucherOrder(Long userId, Voucher voucher);
}
