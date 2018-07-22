package com.majing.learning.elasticsearch.highapi.queryapi.rangeapi;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

public class DateRangeApiMain {
    public static void main(String[] args) throws IOException {
        dateRange1();
    }


    public static void dateRange1() throws IOException {
        RestHighLevelClient client = HighLevelClient.getInstance();
        try{
            RangeQueryBuilder matchQueryBuilder = QueryBuilders.rangeQuery("ctm")
                    .from("2018-07-01 07:27:59.733",true)
                    .to("2018-07-01 07:30:00.000",false);
//            matchQueryBuilder.format("yyyy-MM-dd HH:mm:ss.SSS");//设置日期格式
//            matchQueryBuilder.timeZone("+08:00");//设置时区

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchQueryBuilder);
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(5);

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
