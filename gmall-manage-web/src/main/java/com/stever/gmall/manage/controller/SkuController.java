package com.stever.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stever.gmall.bean.PmsSkuInfo;
import com.stever.gmall.service.SkuService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class SkuController {

    @Reference
    SkuService skuService;

    @RequestMapping(value = "saveSkuInfo", method = RequestMethod.POST)
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo){
        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());
        skuService.saveSkuInfo(pmsSkuInfo);
        return "succcess";
    }
}
