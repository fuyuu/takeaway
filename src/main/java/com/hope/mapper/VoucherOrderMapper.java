package com.hope.mapper;

import com.hope.domain.pojo.VoucherOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VoucherOrderMapper {
    int saveVoucherOrder(VoucherOrder voucherOrder);
}
