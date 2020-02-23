package com.stever.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.stever.gmall.bean.PmsProductSaleAttr;
import com.stever.gmall.bean.PmsSkuInfo;
import com.stever.gmall.bean.PmsSkuSaleAttrValue;
import com.stever.gmall.service.SkuService;
import com.stever.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@CrossOrigin
public class ItemController {
    @Reference
    SkuService skuService;
    @Reference
    SpuService spuService;


    @RequestMapping("index")
    public ModelAndView index(){
        List<String> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            list.add("循环数据"+i);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("list", list);
        modelAndView.addObject("hellos", "hello thymeleaf");
        modelAndView.addObject("check", 1);
        return modelAndView;
    }

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable("skuId") String skuid, ModelMap map){
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuid);
        List<PmsProductSaleAttr> pmsProductSaleAttrList = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(), skuid);
        map.put("skuInfo", pmsSkuInfo);
        //销售属性列表
        map.put("spuSaleAttrListCheckBySku", pmsProductSaleAttrList);

        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());
        HashMap<String, String> skuSaleAttrHashMap = new HashMap<>();
        for(PmsSkuInfo skuInfo : pmsSkuInfos){
            String k = "";
            String v = skuInfo.getId();
            List<PmsSkuSaleAttrValue> skuSaleAttrValues = skuInfo.getSkuSaleAttrValueList();
            for(PmsSkuSaleAttrValue pmsSkuSaleAttrValue:skuSaleAttrValues){
                k += pmsSkuSaleAttrValue.getSaleAttrValueId() + "|";
            }
            skuSaleAttrHashMap.put(k, v);
        }
        // 将sku的销售属性hash表放到页面
        String jsonStr = JSON.toJSONString(skuSaleAttrHashMap);
        map.put("skuSaleAttrHashJsonStr", jsonStr);

        return "item";
    }

    @RequestMapping("cache/{skuId}")
    @ResponseBody
    public PmsSkuInfo testCache(@PathVariable("skuId") String skuid){
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuid);
        return pmsSkuInfo;
    }
}
