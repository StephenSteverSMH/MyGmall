package com.stever.gmall.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PmsSearchParam implements Serializable {
    private String catalog3Id;
    private String keyword;
    private String[] valueId;
    private List<PmsSkuAttrValue> skuAttrValueList;
}
