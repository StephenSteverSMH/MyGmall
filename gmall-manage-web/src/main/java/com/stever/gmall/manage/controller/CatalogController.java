package com.stever.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stever.gmall.bean.PmsBaseCatalog1;
import com.stever.gmall.bean.PmsBaseCatalog2;
import com.stever.gmall.bean.PmsBaseCatalog3;
import com.stever.gmall.service.CatalogService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CatalogController {
    @Reference
    CatalogService catalogService;


    @RequestMapping("getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1(){
        return catalogService.getCatalog1();
    }

    @RequestMapping("getCatalog2")
    public List<PmsBaseCatalog2> getCatalog2(@RequestParam("catalog1Id") String catalog1Id){
        return catalogService.getCatalog12(catalog1Id);
    }

    @RequestMapping("getCatalog3")
    public List<PmsBaseCatalog3> getCatalog3(@RequestParam("catalog2Id") String catalog2Id){
        return catalogService.getCatalog3(catalog2Id);
    }
}
