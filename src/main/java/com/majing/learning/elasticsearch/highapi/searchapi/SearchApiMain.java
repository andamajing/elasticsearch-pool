package com.majing.learning.elasticsearch.highapi.searchapi;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SearchApiMain {

    public static void main(String[] args) throws IOException {
        searchApi();
    }

    public static void searchApi() throws IOException {

        RestHighLevelClient client = HighLevelClient.getInstance();

        try {
            SearchRequest searchRequest = new SearchRequest("jingma2_test");//限定index
            searchRequest.types("testlog");//限定type

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            /*查询所有记录*/

//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());


            /*根据匹配查询*/
            QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "风雷");
            /*设置中文分词器*/
//            ((MatchQueryBuilder) matchQueryBuilder).analyzer("ik");
//            ((MatchQueryBuilder) matchQueryBuilder).analyzer("ik_max_word");
//            ((MatchQueryBuilder) matchQueryBuilder).analyzer("ik_smart");
//            ((MatchQueryBuilder) matchQueryBuilder).analyzer("standard");
            searchSourceBuilder.query(matchQueryBuilder);

            /*限定查询条件和查询条数*/
//            searchSourceBuilder.query(QueryBuilders.termQuery("name", "风雷"));
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(5);
//            searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

            /*限定查询结果排序*/
//            searchSourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
//            searchSourceBuilder.sort(new FieldSortBuilder("age").order(SortOrder.ASC));

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest);
            System.out.println(searchResponse);

            SearchHits hits = searchResponse.getHits();
            long totalHits = hits.getTotalHits();
            float maxScore = hits.getMaxScore();

            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                String index = hit.getIndex();
                String type = hit.getType();
                String id = hit.getId();
                float score = hit.getScore();
                String sourceAsString = hit.getSourceAsString();
                System.out.println(sourceAsString);
//                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            }

        }finally {
            HighLevelClient.close();
        }

    }
}
