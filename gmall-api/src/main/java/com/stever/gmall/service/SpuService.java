package com.stever.gmall.service;

import com.stever.gmall.bean.PmsProductInfo;
import com.stever.gmall.bean.PmsProductSaleAttr;

import java.util.List;

public interface SpuService {
    public List<PmsProductInfo> spuList(String catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId, String skuId);
}
