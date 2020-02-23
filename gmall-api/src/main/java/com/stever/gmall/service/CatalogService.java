package com.stever.gmall.service;

import com.stever.gmall.bean.PmsBaseCatalog1;
import com.stever.gmall.bean.PmsBaseCatalog2;
import com.stever.gmall.bean.PmsBaseCatalog3;

import java.util.List;

public interface CatalogService {
    public List<PmsBaseCatalog1> getCatalog1();

    List<PmsBaseCatalog2> getCatalog12(String catalog1Id);

    List<PmsBaseCatalog3> getCatalog3(String catalog2Id);
}
