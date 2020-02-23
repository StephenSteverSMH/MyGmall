package com.stever.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stever.gmall.bean.PmsBaseCatalog1;
import com.stever.gmall.bean.PmsBaseCatalog2;
import com.stever.gmall.bean.PmsBaseCatalog3;
import com.stever.gmall.manage.mapper.PmsBaseCatalog1Mapper;
import com.stever.gmall.manage.mapper.PmsBaseCatalog2Mapper;
import com.stever.gmall.manage.mapper.PmsBaseCatalog3Mapper;
import com.stever.gmall.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalogMapper1;

    @Autowired
    PmsBaseCatalog2Mapper pmsBaseCatalogMapper2;

    @Autowired
    PmsBaseCatalog3Mapper pmsBaseCatalogMapper3;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {
        return pmsBaseCatalogMapper1.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog12(String catalog1Id) {
        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(catalog1Id);
        return pmsBaseCatalogMapper2.select(pmsBaseCatalog2);
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalog2Id);
        return pmsBaseCatalogMapper3.select(pmsBaseCatalog3);
    }
}
