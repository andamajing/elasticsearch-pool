package com.majing.learning.elasticsearch.highapi.aggregation.bucket;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTimeZone;

import java.io.IOException;

/**
 * @author:admin
 * @date:2018/7/13
 * @description
 */
public class DateHistogramAggregationMain {

    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = HighLevelClient.getInstance();
        try{
            DateHistogramAggregationBuilder dateHistogramAggregationBuilder = AggregationBuilders.dateHistogram("ctm_date_histogram");
            dateHistogramAggregationBuilder.field("ctm");//设置直方图针对的字段
            dateHistogramAggregationBuilder.dateHistogramInterval(DateHistogramInterval.hours(6));//直方图每个分组对应的范围
            dateHistogramAggregationBuilder.timeZone(DateTimeZone.forOffsetHours(8));//时区偏移
            dateHistogramAggregationBuilder.keyed(true);//是否需要key名
            dateHistogramAggregationBuilder.format("yyyy-MM-dd HH:mm");//key名格式
//            dateHistogramAggregationBuilder.order(BucketOrder.aggregation("_key",true));//分组key的排序
//            dateHistogramAggregationBuilder.minDocCount(0);//对于每个分组最少具有多少条数据，少于这个设置，则该分组不显示
//            dateHistogramAggregationBuilder.extendedBounds(0,8000);//设置分组区间的下线和上线，只有当min_doc_count为0时有效

            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("cmd","weather_info");

            SearchRequest searchRequest = new SearchRequest("serverlog_20180710");//限定index
            searchRequest.types("log");//限定type

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(termQueryBuilder);
            searchSourceBuilder.aggregation(dateHistogramAggregationBuilder);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest);
            System.out.println(searchResponse);

        }finally{
            HighLevelClient.close();
        }
    }
}
