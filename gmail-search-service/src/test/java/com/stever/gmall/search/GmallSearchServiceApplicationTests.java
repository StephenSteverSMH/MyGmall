package com.stever.gmall.search;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.stever.gmall.bean.PmsSearchSkuInfo;
import com.stever.gmall.bean.PmsSkuInfo;
import com.stever.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchServiceApplicationTests {
    @Reference
    SkuService skuService;

//    @Autowired
//    JestClient jestClient;

    @Test
    public void contextLoads() throws IOException {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(
                new HttpClientConfig.Builder("http://192.168.137.60:9200")
                        .multiThreaded(false)
                        .defaultMaxTotalConnectionPerRoute(1)
                        .maxTotalConnection(1)
                        .build());
        JestClient jestClient;
//        List<PmsSkuInfo> pmsSkuInfoList = new ArrayList<>();
//        //转化为es的数据结构
//        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
//        pmsSkuInfoList = skuService.getAllSku("");
//        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
//            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
//            BeanUtils.copyProperties(pmsSkuInfo, pmsSearchSkuInfo);
//            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
//        }
//        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
//            jestClient = factory.getObject();
//            Index put = new Index.Builder(pmsSearchSkuInfo).index("gmailpms").type("pmsSkuInfo").id(pmsSearchSkuInfo.getId()).build();
//            jestClient.execute(put);
//            jestClient.close();
//        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(null);


        String search_str = "{\n" +
                "  \"query\":{\n" +
                "    \"bool\":{\n" +
                "      \"filter\":[\n" +
                "        {\n" +
                "          \"term\":{\n" +
                "            \"skuAttrValueList.valueId\": \"39\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"term\":{\n" +
                "            \"skuAttrValueList.valueId\": \"43\"\n" +
                "          }\n" +
                "        }\n" +
                "        ],\n" +
                "      \"must\":[{\n" +
                "        \"match\":{\n" +
                "          \"skuName\":\"小米\"\n" +
                "        }\n" +
                "      }]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        Search search = new Search.Builder(search_str).addIndex("gmailpms").addType("pmsSkuInfo").build();
        jestClient = factory.getObject();
        SearchResult execute = jestClient.execute(search);
        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);
        for(SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits){
            PmsSearchSkuInfo source = hit.source;
            System.out.println(JSON.toJSONString(source));
        }

    }

}
