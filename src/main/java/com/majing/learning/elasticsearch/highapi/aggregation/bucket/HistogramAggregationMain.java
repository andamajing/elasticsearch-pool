package com.majing.learning.elasticsearch.highapi.aggregation.bucket;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;

/**
 * @author:admin
 * @date:2018/7/13
 * @description
 */
public class HistogramAggregationMain {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = HighLevelClient.getInstance();
        try{
            HistogramAggregationBuilder histogramAggregationBuilder = AggregationBuilders.histogram("utm_histogram");
            histogramAggregationBuilder.field("utm");//设置直方图针对的字段
            histogramAggregationBuilder.interval(1000);//直方图每个分组对应的范围
            histogramAggregationBuilder.order(BucketOrder.aggregation("_key",true));//分组key的排序
//            histogramAggregationBuilder.minDocCount(0);//对于每个分组最少具有多少条数据，少于这个设置，则该分组不显示
//            histogramAggregationBuilder.extendedBounds(0,8000);//设置分组区间的下线和上线，只有当min_doc_count为0时有效

            TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("top_cmd");
            aggregationBuilder.field("cmd");
            aggregationBuilder.size(3);
            aggregationBuilder.subAggregation(histogramAggregationBuilder);

            SearchRequest searchRequest = new SearchRequest("serverlog_20180710");//限定index
            searchRequest.types("log");//限定type

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggregationBuilder);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest);
            System.out.println(searchResponse);

        }finally{
            HighLevelClient.close();
        }
    }

}
