package com.stever.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stever.gmall.bean.PmsBaseSaleAttr;
import com.stever.gmall.bean.PmsProductInfo;
import com.stever.gmall.bean.PmsProductSaleAttr;
import com.stever.gmall.bean.PmsProductSaleAttrValue;
import com.stever.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.stever.gmall.manage.mapper.PmsProductInfoMapper;
import com.stever.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.stever.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.stever.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        return  pmsProductInfoMapper.select(pmsProductInfo);
    }

    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {
        pmsProductInfoMapper.insertSelective(pmsProductInfo);
        for(PmsProductSaleAttr pmsProductSaleAttr :pmsProductInfo.getSpuSaleAttrList()){
            pmsProductSaleAttr.setProductId(pmsProductInfo.getId());
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
            for(PmsProductSaleAttrValue pmsProductSaleAttrValue:pmsProductSaleAttr.getSpuSaleAttrValueList()){
                pmsProductSaleAttrValue.setProductId(pmsProductInfo.getId());
                pmsProductSaleAttrValue.setSaleAttrId(pmsProductSaleAttr.getSaleAttrId());
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }
        }
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrList = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        for(PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrList){
            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
            pmsProductSaleAttrValue.setProductId(spuId);
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }

        return pmsProductSaleAttrList;
    }

    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId, String skuId) {
//        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
//        pmsProductSaleAttr.setProductId(productId);
//        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
//        for(PmsProductSaleAttr productSaleAttr: pmsProductSaleAttrs){
//            String saleAttrId = productSaleAttr.getSaleAttrId();
//            PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
//            pmsProductSaleAttrValue.setSaleAttrId(saleAttrId);
//            pmsProductSaleAttrValue.setProductId(productId);
//            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
//            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
//        }
        List<PmsProductSaleAttr> pmsProductSaleAttrs =  pmsProductSaleAttrMapper.selectSpuSaleAttrListCheckBySku(productId, skuId);
        return pmsProductSaleAttrs;
    }
}
