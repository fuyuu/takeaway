package com.hope.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hope.domain.dto.OrderListDTO;
import com.hope.domain.pojo.OrderList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderListDTO> {
    // 插入主订单（返回自增ID）

    int createOrder(OrderListDTO order);

    OrderListDTO selectByOrderNumber(String orderNumber);

    void updateOrderAndPayStatus(String outTradeNo, int i, int i1);
}
