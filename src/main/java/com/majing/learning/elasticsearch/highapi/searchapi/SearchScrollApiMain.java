package com.majing.learning.elasticsearch.highapi.searchapi;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

/**
 * @author:admin
 * @date:2018/7/16
 * @description
 */
public class SearchScrollApiMain {
    public static void main(String[] args) throws IOException {
        searchApi();
    }

    public static void searchApi() throws IOException {

        RestHighLevelClient client = HighLevelClient.getInstance();

        try {


        }finally {
            HighLevelClient.close();
        }

    }
}
