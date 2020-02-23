package com.stever.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.stever.gmall.bean.OmsCartItem;
import com.stever.gmall.bean.PmsSkuInfo;
import com.stever.gmall.service.CartService;
import com.stever.gmall.service.SkuService;
import com.stever.gmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {

    @Reference
    SkuService skuService;

    @Reference
    CartService cartService;

    @RequestMapping("cartList")
    public String cartList(String skuId, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap modelMap){
        List<OmsCartItem> omsCartItems = new ArrayList<>();
        String userId = "1";
        if(StringUtils.isNotBlank(userId)){
            //已经登录查询db
           omsCartItems = cartService.cartList(userId);
        }else {
            //没有登录查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isNotBlank(cartListCookie)){
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
            }
        }
        for (OmsCartItem omsCartItem : omsCartItems) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().intValue() * omsCartItem.getQuantity());
        }
        modelMap.put("cartList", omsCartItems);
        int totalAmount = 0;
        for (OmsCartItem omsCartItem : omsCartItems) {
            totalAmount+=omsCartItem.getQuantity()*omsCartItem.getPrice().intValue();
        }
        modelMap.put("totalAmount", totalAmount);
        return "cartList";
    }



    @RequestMapping(value = "addToCart", method = RequestMethod.POST)
    public String addToCart(String skuId, long quantity, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
        PmsSkuInfo skuInfo = skuService.getSkuById(skuId);
        //将商品信息封装成购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(skuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductBrand("");
        omsCartItem.setProductCategoryId(skuInfo.getCatalog3Id());
        omsCartItem.setProductId(skuInfo.getSkuName());
        omsCartItem.setProductPic(skuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuId("1111");
        omsCartItem.setQuantity((int)quantity);

//        omsCartItems.add(omsCartItem);

//        CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60*60*72, true);

        List<OmsCartItem> omsCartItems = new ArrayList<>();
        String memberId = "1";
        if(StringUtils.isBlank(memberId)){
            //用户没有登录
           String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
           omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
           if(omsCartItems==null){
               omsCartItems = new ArrayList<>();
           }
           if(StringUtils.isBlank(cartListCookie)){
               // cookie为空
               omsCartItems.add(omsCartItem);
           }else {
               //cookie不为空
               // 判断添加的购物车数据在cookie中是否存在
               boolean exist = ifCartExist(omsCartItems, omsCartItem);
               if(exist){
                   //之前添加过
                    for(OmsCartItem cartItem : omsCartItems){
                        if(cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())){
                            cartItem.setQuantity(cartItem.getQuantity()+omsCartItem.getQuantity());
                            cartItem.setPrice(cartItem.getPrice().add(omsCartItem.getPrice()));
                        }
                    }
               }else {
                   //之前没有添加过,新增入当前购物车
                   omsCartItems.add(omsCartItem);
               }
           }
        }else{
            //用户已经登录
            // 从db中查处购物车数据
            OmsCartItem cartItem = cartService.ifCartExistedByUser(memberId, skuId);
            if(cartItem==null){
                //该用户没有添加过当前商品
                omsCartItems.add(omsCartItem);
                cartService.addCart(omsCartItem);
            }else {
                //用户添加过当前商品
                cartItem.setQuantity(omsCartItem.getQuantity());
                cartService.updateCart(cartItem);
            }
            // 同步缓存
            cartService.flushCartCache(memberId);
        }
        CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60*60*72, true);
        modelMap.put("skuInfo", skuInfo);
        return "success";
    }

    private boolean ifCartExist(List<OmsCartItem> omsCartItems, OmsCartItem omsCartItem) {
        boolean b = false;
        for(OmsCartItem cartItem : omsCartItems){
            String productSkuId = cartItem.getProductSkuId();
            if(productSkuId.equals(omsCartItem.getProductSkuId())){
                b = true;
            }
        }
        return b;
    }


}
