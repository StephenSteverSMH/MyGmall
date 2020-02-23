package com.stever.gmall.payment.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.stever.gmall.bean.PaymentInfo;
import com.stever.gmall.payment.config.AlipayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaymentController {
    @Autowired
    AlipayClient alipayClient;

    @Autowired
    PaymentService paymentService;

    @RequestMapping("mx/submit")
    public String mx(String outTradeNo, BigDecimal totalAmount, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){

        return null;
    }
    @RequestMapping(value = "alipay/submit")
    @ResponseBody
    public String alipay(String outTradeNo, BigDecimal totalAmount, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        // 获得支付宝请求客户端
        String form = null;
        AlipayTradePagePayRequest alipayTradePagePayRequest = new AlipayTradePagePayRequest();
        alipayTradePagePayRequest.setReturnUrl(AlipayConfig.return_payment_url);
        alipayTradePagePayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);
        outTradeNo = "123456";
        if(totalAmount==null){
            totalAmount = new BigDecimal(1200);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no", outTradeNo);
        map.put("product_code", "FAST_INSTANT_TRADE_PAY");
        map.put("total_amount", totalAmount);
        map.put("subject", "这是个标题字段");
        String param = JSON.toJSONString(map);
        alipayTradePagePayRequest.setBizContent(param);
        try {
            form = alipayClient.pageExecute(alipayTradePagePayRequest).getBody();//生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        // 在db中存储订单
        return form;
    }

    @RequestMapping("alipay/callback/return")
    public String aliPayCallBackReturn(HttpServletRequest request, ModelMap modelMap){
        String sign = request.getParameter("sign");
        String trade_no = request.getParameter("trade_no");
        String out_trade_no = request.getParameter("out_trade_no");
        String trade_status = request.getParameter("trade_status");
        String total_amount = request.getParameter("total_amount");
        String subject = request.getParameter("subject");
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentService.updatePayment(paymentInfo);
        return null;
    }


    @RequestMapping("index")
    public String index(String outTradeNo, BigDecimal totalAmount){
        return "index";
    }
}
