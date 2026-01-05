package com.hope.controller;

import com.hope.domain.pojo.Result;
import com.hope.domain.pojo.Voucher;
import com.hope.service.IVoucherOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voucher-order")
@Slf4j
public class VoucherOrderController {
    @Autowired
    private IVoucherOrderService voucherOrderService;

    @PostMapping("/{id}")
    public Result addVoucher(@PathVariable("id") Long voucherId) {
        return voucherOrderService.saveVoucherOrder(voucherId);
    }

    @PostMapping("seckill/{id}")
    public Result seckillVoucher(@PathVariable("id") Long voucherId) {
        return voucherOrderService.seckillVoucher(voucherId);
    }
}
