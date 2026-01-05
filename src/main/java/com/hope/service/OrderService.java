package com.hope.service;


import com.hope.domain.dto.OrderListDTO;
import com.hope.domain.pojo.OrderList;
import com.hope.domain.pojo.Result;

public interface OrderService  {

    Result createOrder(OrderListDTO order);

    Result deleteOrder(Long id);

    Result getOrder(Long id);

    Result getOrderList();

    Result updateOrder(OrderList orderList);
}
