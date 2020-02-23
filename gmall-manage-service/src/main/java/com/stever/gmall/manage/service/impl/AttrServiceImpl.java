package com.stever.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stever.gmall.bean.PmsBaseAttrInfo;
import com.stever.gmall.bean.PmsBaseAttrValue;
import com.stever.gmall.bean.PmsBaseSaleAttr;
import com.stever.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.stever.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.stever.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.stever.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

@Service
public class AttrServiceImpl implements AttrService {
    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        for(PmsBaseAttrInfo baseAttrInfo:pmsBaseAttrInfos){
            PmsBaseAttrValue temp = new PmsBaseAttrValue();
            temp.setAttrId(baseAttrInfo.getId());
            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(temp);
            baseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }
        return pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
    }

    @Override
    @Transactional
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        try{
            String id = pmsBaseAttrInfo.getId();
            if(StringUtils.isBlank(id)) {
                //新插入
                pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
                List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                    pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                    pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
                }
            }else{
                //修改
                Example e = new Example(PmsBaseAttrInfo.class);
                e.createCriteria().andEqualTo("id", pmsBaseAttrInfo.getId());
                pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo, e);
                List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                PmsBaseAttrValue temp = new PmsBaseAttrValue();
                temp.setAttrId(pmsBaseAttrInfo.getId());
                pmsBaseAttrValueMapper.delete(temp);
                for(PmsBaseAttrValue pmsBaseAttrValue:attrValueList){
                    pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
                }
            }
        }catch (Exception e){
            throw new RuntimeException();
        }
        return "success";
    }

    @Override
    public List<PmsBaseAttrValue> attrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
        return pmsBaseAttrValues;
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return  pmsBaseSaleAttrMapper.selectAll();
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<String> valueIdSet) {
        String valueIdStr = StringUtils.join(valueIdSet, ",");
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.selectAttrValueListByValueId(valueIdStr);
        return pmsBaseAttrInfos;
    }


}
