package com.hope.mapper;

import com.hope.domain.pojo.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper {
    // 插入订单项
    int insert(OrderItem orderItem);
}
