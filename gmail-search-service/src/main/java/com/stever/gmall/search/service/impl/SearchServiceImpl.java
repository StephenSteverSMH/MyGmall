package com.stever.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.stever.gmall.bean.PmsSearchParam;
import com.stever.gmall.bean.PmsSearchSkuInfo;
import com.stever.gmall.bean.PmsSkuAttrValue;
import com.stever.gmall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    JestClientFactory jestClientFactory;

    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) {
        JestClient jestClient = jestClientFactory.getObject();
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        String dslStr = getSearchDsl(pmsSearchParam);
        SearchResult execute = null;
        Search search = new Search.Builder(dslStr).addIndex("gmailpms").addType("pmsSkuInfo").build();
        try{
            execute = jestClient.execute(search);
        }catch (IOException e){
            e.printStackTrace();
        }
        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);
        for(SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits){
            PmsSearchSkuInfo source = hit.source;
            String skuName = hit.highlight.get("skuName").get(0);
            if(StringUtils.isNotBlank(skuName))
            {
                source.setSkuName(skuName);
            }
            pmsSearchSkuInfos.add(source);
        }
        try {
            jestClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pmsSearchSkuInfos;
    }

    private String getSearchDsl(PmsSearchParam pmsSearchParam) {
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        List<PmsSkuAttrValue> skuAttrValueList = pmsSearchParam.getSkuAttrValueList();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //filter
        if(StringUtils.isNotBlank(catalog3Id)){
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id", catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }

        if(skuAttrValueList!=null){

//            for(PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList){
//                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId", pmsSkuAttrValue.getValueId());
//                boolQueryBuilder.filter(termQueryBuilder);
//            }
        }
        if(pmsSearchParam.getValueId()!=null){
            for(String valueId : pmsSearchParam.getValueId()){
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId", valueId);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }//must
        if(StringUtils.isNotBlank(keyword)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", keyword);
            boolQueryBuilder.must(matchQueryBuilder);
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(20);
        searchSourceBuilder.highlighter(null);

        searchSourceBuilder.sort("id", SortOrder.DESC);

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.field("skuName");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);


        //aggs
//        TermsAggregationBuilder groupby_attr = AggregationBuilders.terms("groupby_attr").field("skuAttrValueList.valueId");
//        searchSourceBuilder.aggregation(groupby_attr);


        return searchSourceBuilder.toString();
    }

}
