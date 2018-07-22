package com.majing.learning.elasticsearch.highapi.aggregation.bucket;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTimeZone;

import java.io.IOException;

/**
 * @author:admin
 * @date:2018/7/13
 * @description
 */
public class DateRangeAggregationMain {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = HighLevelClient.getInstance();
        try{
            DateRangeAggregationBuilder dateRangeAggregationBuilder = AggregationBuilders.dateRange("date_range");
            dateRangeAggregationBuilder.field("ctm");
            dateRangeAggregationBuilder.format("yyyy-MM-dd HH:mm:ss");
            dateRangeAggregationBuilder.addRange("2018-07-10 00:00:00","2018-07-11 00:00:00");
            dateRangeAggregationBuilder.timeZone(DateTimeZone.forOffsetHours(0));

            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("cmd","weather_info");

            SearchRequest searchRequest = new SearchRequest("serverlog_20180710");//限定index
            searchRequest.types("log");//限定type

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(termQueryBuilder);
            searchSourceBuilder.aggregation(dateRangeAggregationBuilder);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest);
            System.out.println(searchResponse);

        }finally{
            HighLevelClient.close();
        }
    }
}
