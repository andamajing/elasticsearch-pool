package com.majing.learning.elasticsearch.highapi.queryapi.compound;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

/**
 * @author:admin
 * @date:2018/7/12
 * @description
 */
public class BoolApiMain {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = HighLevelClient.getInstance();
        try{
            BoolQueryBuilder matchQueryBuilder = QueryBuilders.boolQuery();
            matchQueryBuilder.must(QueryBuilders.termQuery("cpname","llbxinterface"));
            matchQueryBuilder.mustNot(QueryBuilders.termQuery("status","fail"));
            matchQueryBuilder.should(QueryBuilders.rangeQuery("utm").from(0,true).to(100,true));
            matchQueryBuilder.minimumShouldMatch(1);
//            matchQueryBuilder.filter();

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchQueryBuilder);
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(3);

            SearchRequest searchRequest = new SearchRequest("serverlog_20180701");//限定index
            searchRequest.types("log");//限定type
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest);
            System.out.println(searchResponse);


        }finally{
            HighLevelClient.close();
        }
    }
}
