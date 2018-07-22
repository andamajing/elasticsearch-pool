package com.majing.learning.elasticsearch.highapi.aggregation.bucket;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.join.aggregations.ChildrenAggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;

/**
 * @author:admin
 * @date:2018/7/13
 * @description
 */
public class ChildrenAggregationMain {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = HighLevelClient.getInstance();
        try{

            ChildrenAggregationBuilder aggregationBuilder = new ChildrenAggregationBuilder("utm","histogram");
            aggregationBuilder.field("utm");

            SearchRequest searchRequest = new SearchRequest("serverlog_20180710");//限定index
            searchRequest.types("log");//限定type

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggregationBuilder);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest);
            System.out.println(searchResponse);
            Aggregations aggregations = searchResponse.getAggregations();
            List<Aggregation> aggregationList = aggregations.asList();
            for(Aggregation each: aggregationList){
                System.out.println(each);
            }
        }finally{
            HighLevelClient.close();
        }
    }
}
