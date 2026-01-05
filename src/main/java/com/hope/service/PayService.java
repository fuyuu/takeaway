package com.hope.service;

import com.alipay.api.AlipayApiException;
import com.hope.domain.dto.OrderListDTO;
import com.hope.domain.pojo.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface PayService {

    Result pay(OrderListDTO order) throws AlipayApiException;

    void notifyPayment(HttpServletRequest request);

    void pagePay(OrderListDTO order, HttpServletResponse response) throws AlipayApiException, IOException;
}
