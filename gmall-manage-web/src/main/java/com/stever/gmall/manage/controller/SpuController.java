package com.stever.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stever.gmall.bean.PmsProductImage;
import com.stever.gmall.bean.PmsProductInfo;

import com.stever.gmall.bean.PmsProductSaleAttr;
import com.stever.gmall.service.SpuService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
public class SpuController {

    @Reference
    SpuService spuService;

    @RequestMapping("spuList")
    public List<PmsProductInfo> spuList(String catalog3Id){
        List<PmsProductInfo> pmsProductInfos = spuService.spuList(catalog3Id);
        return  pmsProductInfos;
    }

    @RequestMapping("saveSpuInfo")
    public String saveSpu(@RequestBody PmsProductInfo pmsProductInfo){
        spuService.saveSpuInfo(pmsProductInfo);
        return  "success";
    }


    @RequestMapping(value = "fileUpload", method = RequestMethod.POST)
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile){
        // 将图片上传到分布式文件存储系统

        // 将图片的存储路径返回给页面
        String imgUrl = "";
        return imgUrl;
    }

    @RequestMapping("spuSaleAttrList")
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }

    @RequestMapping("spuImageList")
    public List<PmsProductImage> spuImageList(String spuId){
        return null;
    }
}
