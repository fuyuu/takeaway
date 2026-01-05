package com.hope.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.hope.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayUtil {
    @Autowired
    private OrderService orderService;
    //appid
    private final String APP_ID = "9021000156657096";
    //应用私钥
    private final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCmLshhw7oj3anIPBtPLWIKulhE3q7i8H4KwGy/1QEECSrwUYjdIpLoay4eXaQsS6Cv7yArDjnuR99AvMLo+veFJHtkxf2TH/5WiGlFYCSEjfjeqsVXLbHnTkjYKQzt+x8faBhFvuoV2VC0xbz+XbcAYDojkJS37r89pity+SwX6b64jBERMAw0w2tiqWuU70cStPU1J16ZzbNV0FRfJ9D0vbVCX7PCzZHdwKv6BmbW9s1zTaXvhFAL+FvSQxbRtBjpXuQe25RQoTtwHhoNTwnG1viYPBVbMeMcPwPkMOaisncRHBATBd8jfIK14oz8JHMktcUc48EwVtNNcqSMwWfBAgMBAAECggEBAI95Em09c0YlyMBkDFvWXa/PZAfJjYnGgbs8mGciOedHFvg2HIh2ogjhCAcVZ3kKridWtxoVBTTQflHA5jIJAovDjHjbauqLk6UXznMj50M942AyZX+3psGBV+ivwEbyKNOPpm0Iv8avQTthAKNHO4iqi2dLGuqVyCwse5SexU+6io773czUWRPQ0Ldhp4ypdki2VruwzpaRnbMwjYgwS0nZeUebry5h4febiqRbartw1Nbt1L/gfT8KGNatmLVq2xU9GopsRXTpqLmMQwJ1HTnMAj88E3xBJaqLtyZAbwLWBrNQiggq76U8X26ymJYGJA2GUBMquvnYab4xaRSTRwECgYEA6afJVIOyrx+YAVEvMi3cIzE52De31JZ2Zsy9T4YR28Q+TRdt8JzSqcmrvTwhLhU9uu3I8zf8h2VkhxHH9EMIxS7ktl0u3rvtl26AYdfpvXgziv7v90ZctRKq9cj01FJfOYwpUQMauF2fNJUtQ4qm96Qi6BNXys+EiY4xt2r22JECgYEAthMrmf78XKJPut/tmCz2Uu4db63rf+9w/zCyQ5hBU8zjPTdnrQhJHYGCOr/ErToKQiuF9RaDCqR41qmH5G1OwiFIJLgAjHBZ+duGRWogxrHour/QFaDNzia++KNJsY5v6Ni2p7HM4iv+4gYc813eYzY3c9c+jo1c/hvGL018tDECgYBeaUanmKNtsZVpCtXIL9GggrZ90F2+T6G9/gKMjH7QzyXhXpM0wxk4M93qE87QYRLVYlltNXUbfHO3RA8IesGjc0RFD7ftlf/1zVegsW6n+VbSVhq+TrcND2L292E+HZ6OkTzsmBWG44V96L0manL242KbZZ12vI0wS715AIaHkQKBgGrLQ7/Ht+39phZOUeX4OWLbF5jxwjgAEZ8cyPKs02R7wK2fWcg8G+aal/AtmwagDHRTYvcmhSYw4k/GkFaDZranHtGqu2ekXqIXCOKeJStmHsOqzjphX1WTlF/yJr9mdG1272vc9feTJqUcQM7eOBqpKk1f6uliMTE6yTvlj0eBAoGAbqS3TDm0PoH9TpyVTi718zow4NC2XTwnglffwmMhtVPsOKSyupfFNvaDq3Ox+XjE/u3NApUD7XmLna/68BoXpW9kBgPI7ynr1hJSRTpUR7/4T/iB2nNtLU9h+krmTjJWV8wZTjgw1GvMGp/DvgOT/yegTczpMFrk8I/QVmGhAEQ=";

    private final String CHARSET = "UTF-8";
    // 支付宝公钥
    private final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmme9UKSMq2tyHPKxaXlWWJ6yQmefetBCg2EoL6JX/qw5lzdTLMtpcPZPsgN2+a5LrRnZimE14q0RL2sPGVWKdozffi70K7X5ttZQ8BoiRGSNVKmQLZ1sWxhZoNBWze4kDrcTqKtI02OPwQc+mDhjMQf0w/D5A+0M6jWTwM3KNBlSVABZYE1xmmiEZFiM5nugAwJ2cSyIE/AoIoHYZhkCfXwI9G+by/W2sS2T7RH/tnKHtjtSx+eGeAxFHJyJvznJf34tJRFP9/EgmGLKNnTi1akIG+Wszf3fCox0ZCbkqT0cBd7VOrrmGoChyOtcJqBrbQ76d6JO7lljHl9/4gQWDQIDAQAB";
    //这是沙箱接口路径,正式路径为https://openapi.alipay.com/gateway.do
    private final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private final String FORMAT = "JSON";
    //签名方式
    private final String SIGN_TYPE = "RSA2";
    //支付宝异步通知路径,付款完毕后会异步调用本项目的方法,必须为公网地址
    private final String NOTIFY_URL = "http://pf835785.natappfree.cc/api/alipay/toSuccess";
    //支付宝同步通知路径,也就是当付款完毕后跳转本项目的页面,可以不是公网地址
    private final String RETURN_URL = "http://localhost:8080/api/alipay/toSuccess";
    private AlipayClient alipayClient = null;
    //支付宝官方提供的接口
    public String sendRequestToAlipay(String outTradeNo, Float totalAmount, String subject) throws AlipayApiException {
        //获得初始化的AlipayClient
        alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(RETURN_URL);
        alipayRequest.setNotifyUrl(NOTIFY_URL);

        //商品描述（可空）
        String body = "";
        alipayRequest.setBizContent("{\"out_trade_nos\":\"" + outTradeNo + "\","
                + "\"total_amount\":\"" + totalAmount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        System.out.println("返回的结果是："+result );
        return result;
    }

    //    通过订单编号查询
    public String query(String id){
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", id);
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = null;
        String body=null;
        try {
            response = alipayClient.execute(request);
            body = response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        return body;
    }
}
