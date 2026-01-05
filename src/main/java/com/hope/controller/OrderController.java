package com.hope.controller;

import com.hope.domain.dto.OrderListDTO;
import com.hope.domain.pojo.OrderList;
import com.hope.domain.pojo.Result;
import com.hope.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * @author 马mq
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    public OrderService orderService;

    @PostMapping("/add")
    public Result createOrder(@RequestBody OrderListDTO request) {
        return orderService.createOrder(request);
    }

    @DeleteMapping("/{id}")
    public Result deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }

    @GetMapping("/{id}")
    public Result getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/list")
    public Result getOrderList() {
        return orderService.getOrderList();
    }

    @PutMapping("/update")
    public Result updateOrder(@RequestBody OrderList orderList) {
        return orderService.updateOrder(orderList);
    }
}
