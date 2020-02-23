package com.stever.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stever.gmall.bean.OmsCartItem;
import com.stever.gmall.bean.OmsOrderItem;
import com.stever.gmall.bean.UmsMemberReceiveAddress;
import com.stever.gmall.service.CartService;
import com.stever.gmall.service.OrderService;
import com.stever.gmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Reference
    CartService cartService;

    @Reference
    OrderService orderService;

    @RequestMapping("submitOrder")
    public String submitOrder(HttpServletRequest request, HttpServletResponse respons){
        String memberId = (String)request.getAttribute("memberId");
        memberId="1";
        String tradeCode = (String)request.getAttribute("tradeCode");
        tradeCode="";
        //检查交易码
        String success = orderService.checkTradeCode(memberId, tradeCode);
        if(success.equals("success")){

        }else {

        }
        //查询购物车

        //将订单写入数据库

        //删除购物车中的商品

        //重定向到支付系统

        //生成交易码
        String code = orderService.generateTradeCode(memberId);

        return null;
    }

    @RequestMapping("toTrade")
    public String toTrade(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap){
        String memberId = (String)request.getAttribute("memberId");
        String nickName = (String)request.getAttribute("nickname");
        memberId="1";
//        List<UmsMemberReceiveAddress> receiveAddressListByMemberId = cartService.cartList(memberId);

        List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
        List<OmsOrderItem> omsOrderItems = new ArrayList<>();
        for(OmsCartItem omsCartItem : omsCartItems){
            OmsOrderItem omsOrderItem = new OmsOrderItem();
            omsOrderItem.setProductName(omsCartItem.getProductName());
            omsOrderItems.add(omsOrderItem);
        }
        modelMap.put("omsOrderItems", omsOrderItems);
        //生成交易码
        return "trade";
    }

}
