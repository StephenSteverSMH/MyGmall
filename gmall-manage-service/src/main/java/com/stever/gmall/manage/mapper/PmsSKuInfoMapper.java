package com.stever.gmall.manage.mapper;

import com.stever.gmall.bean.PmsSkuInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsSKuInfoMapper extends Mapper<PmsSkuInfo> {
    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId);
}
