package com.hope.mapper;

import com.hope.domain.pojo.SeckillVoucher;
import com.hope.domain.pojo.Voucher;
import com.hope.domain.pojo.VoucherOrder;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface VoucherMapper {

    /* 查询优惠卷  */
    VoucherOrder getByIdVoucherOrder(Long voucherId);

    Voucher getVoucherById(Long voucherId);

    Voucher getVoucherSeckillById(Long voucherId);

    List<Voucher> listVouchers(Long merchantId);

    int queryUserIdVoucherId(Long userId,Long voucherId);
    /* update 优惠卷 */
    int decreaseSecKill(Voucher voucher);

    int updateVoucher(Voucher voucher);

    int updateVoucherSeckill(Voucher voucher);

    /* 插入 */
    int saveVoucher(Voucher voucher);

    int saveSecKillVoucher(SeckillVoucher seckillVoucher);

    int saveVoucherOrder(VoucherOrder voucher);



    /* 删除 */

}
