package com.stever.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stever.gmall.bean.PmsBaseAttrInfo;
import com.stever.gmall.bean.PmsBaseAttrValue;
import com.stever.gmall.bean.PmsBaseSaleAttr;
import com.stever.gmall.service.AttrService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class AttrController {
    @Reference
    AttrService attrService;


    @RequestMapping("attrInfoList")
    public List<PmsBaseAttrInfo> attrInfoList(@RequestParam("catalog3Id")String catalog3Id){
        List<PmsBaseAttrInfo> list = attrService.attrInfoList(catalog3Id);
        return list;
    }

    @RequestMapping("getAttrValueList")
    public List<PmsBaseAttrValue> getAttrValueList(@RequestParam("attrId") String attrId){
        List<PmsBaseAttrValue> pmsBaseAttrValues = attrService.attrValueList(attrId);
        return pmsBaseAttrValues;
    }

    @RequestMapping("saveAttrInfo")
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        String success = attrService.saveAttrInfo(pmsBaseAttrInfo);
        return "success";
    }

    @RequestMapping("baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList(){
        List<PmsBaseSaleAttr>  pmsBaseSaleAttrs = attrService.baseSaleAttrList();
        return pmsBaseSaleAttrs;
    }
}
