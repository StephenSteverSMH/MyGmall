package com.stever.gmail.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stever.gmall.bean.*;
import com.stever.gmall.service.AttrService;
import com.stever.gmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.util.HashCodeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class SearchController {
    @Reference
    SearchService searchService;

    @Reference
    AttrService attrService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap){// 三级分类id、关键字
        // 调用搜索服务，返回搜索结果
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList", pmsSearchSkuInfoList);
        Set<String> valueIdSet = new HashSet<>();
        for(PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList){
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for(PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList){
                String valueId = pmsSkuAttrValue.getValueId();
                valueIdSet.add(valueId);
            }
        }
        // 根据valueId查询属性列表
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.getAttrValueListByValueId(valueIdSet);
        modelMap.put("attrList", pmsBaseAttrInfos);

        Map<String, String> attrTempMap = new HashMap<>();

        String[] delValueIds = pmsSearchParam.getValueId();
        if(delValueIds!=null){
            for(Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();iterator.hasNext();){
                PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                for(PmsBaseAttrValue pmsBaseAttrValue : attrValueList){
                    String valueId = pmsBaseAttrValue.getId();
                    for(String delValueId: delValueIds){
                        if(delValueId.equals(valueId)){
                            attrTempMap.put(valueId, pmsBaseAttrValue.getValueName());
                            iterator.remove();
                        }
                    }
                }
            }
        }
        String urlParam = getUrlParam(pmsSearchParam);
        modelMap.put("urlParam", urlParam);


        // 面包屑
        List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
        if(delValueIds!=null){
            // 当前请求中包含属性的参数，每一个属性参数，都会生成一个面包屑
            for(String delValueId :delValueIds){
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                pmsSearchCrumb.setValueId(delValueId);
                pmsSearchCrumb.setValueName(attrTempMap.get(delValueId));
                pmsSearchCrumb.setUrlParam(getUrlParamForCrumb(pmsSearchParam, delValueId));
                pmsSearchCrumbs.add(pmsSearchCrumb);
            }
        }
//        pmsSearchParam.getValueId();
        modelMap.put("attrValueSelectedList", pmsSearchCrumbs);

        return "list";
    }
    private String getUrlParam(PmsSearchParam pmsSearchParam){
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        List<PmsSkuAttrValue> skuAttrValueList = pmsSearchParam.getSkuAttrValueList();
        String urlParam = "";
        if(StringUtils.isNotBlank(keyword)){
            urlParam += urlParam + "&keyword=" + keyword;
        }
        if(StringUtils.isNotBlank(catalog3Id)){
            urlParam += urlParam + "&catalog3Id=" + keyword;
        }
//        if(skuAttrValueList!=null){
//            for(PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList){
//                String valueId = pmsSkuAttrValue.getValueId();
//                urlParam = urlParam + "&valueId=" + valueId;
//            }
//        }
        if(pmsSearchParam.getValueId()!=null){
            for(String valueId :pmsSearchParam.getValueId()){
                urlParam = urlParam + "&valueId=" + valueId;
            }
        }
        if(urlParam.indexOf("&")==0){
            urlParam = urlParam.substring(1, urlParam.length());
        }
        return urlParam;
    }
    private String getUrlParamForCrumb(PmsSearchParam pmsSearchParam, String delValueId){
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        List<PmsSkuAttrValue> skuAttrValueList = pmsSearchParam.getSkuAttrValueList();
        String urlParam = "";
        if(StringUtils.isNotBlank(keyword)){
            urlParam += urlParam + "&keyword=" + keyword;
        }
        if(StringUtils.isNotBlank(catalog3Id)){
            urlParam += urlParam + "&catalog3Id=" + keyword;
        }
//        if(skuAttrValueList!=null){
//            for(PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList){
//                String valueId = pmsSkuAttrValue.getValueId();
//                urlParam = urlParam + "&valueId=" + valueId;
//            }
//        }
        if(pmsSearchParam.getValueId()!=null){
            for(String valueId :pmsSearchParam.getValueId()){
                if(!valueId.equals(delValueId)){
                    urlParam = urlParam + "&valueId=" + valueId;
                }
            }
        }
        if(urlParam.indexOf("&")==0){
            urlParam = urlParam.substring(1, urlParam.length());
        }
        return urlParam;
    }
}
