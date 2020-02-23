package com.stever.gmall.service;

import com.stever.gmall.bean.PmsSearchParam;
import com.stever.gmall.bean.PmsSearchSkuInfo;

import java.io.IOException;
import java.util.List;

public interface SearchService {

    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
