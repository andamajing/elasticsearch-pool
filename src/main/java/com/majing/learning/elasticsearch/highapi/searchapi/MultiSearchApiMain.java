package com.majing.learning.elasticsearch.highapi.searchapi;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

public class MultiSearchApiMain {

    public static void main(String[] args) throws IOException {
        multiSearchApi();
    }

    public static void multiSearchApi() throws IOException {


        try{
            RestHighLevelClient client = HighLevelClient.getInstance();

            MultiSearchRequest request = new MultiSearchRequest();

            SearchRequest firstSearchRequest = new SearchRequest("jingma2_test");
            firstSearchRequest.types("testlog");//限定type
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery("name", "李永虎"));
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(5);
            firstSearchRequest.source(searchSourceBuilder);
            request.add(firstSearchRequest);

            SearchRequest secondSearchRequest = new SearchRequest("jingma2_test");
            secondSearchRequest.types("testlog");//限定type
            searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery("interests", "跳舞"));
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(5);
            secondSearchRequest.source(searchSourceBuilder);
            request.add(secondSearchRequest);

            MultiSearchResponse response = client.multiSearch(request);

            MultiSearchResponse.Item firstResponse = response.getResponses()[0];
            SearchResponse searchResponse = firstResponse.getResponse();
            System.out.println(searchResponse);
            MultiSearchResponse.Item secondResponse = response.getResponses()[1];
            searchResponse = secondResponse.getResponse();
            System.out.println(searchResponse);

        }finally{
            HighLevelClient.close();
        }
    }
}
