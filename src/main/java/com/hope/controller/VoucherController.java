package com.hope.controller;

import com.hope.domain.pojo.Result;
import com.hope.domain.pojo.Voucher;
import com.hope.service.VoucherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voucher")
@Slf4j
/*  商家  */
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    /**
     * 新增普通券
     * @param voucher 优惠券信息
     * @return 优惠券id
     */
    @PostMapping
    public Result addVoucher(@RequestBody Voucher voucher) {
        try {
            voucherService.saveVoucher(voucher);
            // 检查是否生成了id（如果是自增主键）
            if (voucher.getId() == null) {
                return Result.fail("优惠券创建失败，未生成ID");
            }
            return Result.ok(voucher.getId());
        } catch (Exception e) {
            log.error("新增普通券失败", e);
            return Result.fail("新增优惠券失败：" + e.getMessage());
        }
    }

    /**
     * 新增秒杀券
     * @param voucher 优惠券信息，包含秒杀信息
     * @return 优惠券id
     */
    @PostMapping("/seckill")
    public Result addSecKillVoucher(@RequestBody Voucher voucher) {
        try {
            voucherService.addSecKillVoucher(voucher);
            if (voucher.getId() == null) {
                return Result.fail("秒杀券创建失败，未生成ID");
            }
            return Result.ok(voucher.getId());
        } catch (Exception e) {
            log.error("新增秒杀券失败", e);
            return Result.fail("新增秒杀券失败：" + e.getMessage());
        }
    }

    /**
     * 查询单个普通优惠券
     * @param id  商家 id
     * @return 优惠券详情
     */
    @GetMapping("/{id}")
    public Result<Voucher> getVoucher(@PathVariable Long id) {
        try{
            Voucher voucher = voucherService.getVoucherById(id);
            return voucher != null ? Result.ok(voucher) : Result.fail("优惠券不存在");
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 查询所有优惠券
     * @return 优惠券列表
     */
    @GetMapping("/all/{id}")
    public Result<List<Voucher>> listVouchers(@PathVariable("id") Long id) {
        try{
            System.out.println("listVouchers"+id);
            List<Voucher> vouchers = voucherService.listVouchers(id);
            if(vouchers==null||vouchers.isEmpty()){
                return Result.fail("店铺"+id+"没有优惠卷");
            }
            return Result.ok(vouchers);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新 普通 优惠券
     * @param voucher 优惠券信息
     * @return 更新结果
     */
    @PutMapping
    public Result updateVoucher(@RequestBody Voucher voucher) {
        boolean success = voucherService.updateVoucher(voucher);
        return success ? Result.ok() : Result.fail("更新优惠券失败");
    }
    /**
     * 更新 秒杀 优惠券
     * @param voucher 优惠券信息
     * @return 更新结果
     */
    @PutMapping("/seckill")
    public Result updateVoucherSeckill(@RequestBody Voucher voucher) {
        boolean success = voucherService.updateVoucherSeckill(voucher);
        return success ? Result.ok() : Result.fail("更新优惠券失败");
    }

    /**
     * 删除 普通 优惠券
     * @param id 优惠券id
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result deleteVoucher(@PathVariable Long id) {
        boolean success = voucherService.deleteVoucher(id);
        return success ? Result.ok() : Result.fail("删除优惠券失败");
    }
    /**
     * 删除 秒杀 优惠券
     * @param id 优惠券id
     * @return 删除结果
     */
    @DeleteMapping("/seckill/{id}")
    public Result deleteVoucherSeckill(@PathVariable Long id) {
        boolean success = voucherService.deleteVoucherSeckill(id);
        return success ? Result.ok() : Result.fail("删除优惠券失败");
    }
}
