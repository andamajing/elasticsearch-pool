package com.majing.learning.elasticsearch.highapi.documentapi.getapi;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;

public class GetApiMain {
    public static void main(String[] args) throws IOException {
        get();
    }

    public static void get() throws IOException {

        try{
            RestHighLevelClient client = HighLevelClient.getInstance();
            GetRequest getRequest = new GetRequest("jingma2_test_log", "testlog", "1");

//            getRequest.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);

            String[] includes = new String[]{"name", "age", "interests"};
            String[] excludes = Strings.EMPTY_ARRAY;
            FetchSourceContext fetchSourceContext =
                    new FetchSourceContext(true, includes, excludes);
            getRequest.fetchSourceContext(fetchSourceContext);//用于设置查询返回的字段

            try {
                GetResponse getResponse = client.get(getRequest);
                System.out.println(getResponse.getSource());
            } catch (ElasticsearchException e) {
                if (e.status() == RestStatus.NOT_FOUND) {

                }else if (e.status() == RestStatus.CONFLICT){

                }
            }

        }catch(Exception e){

        }finally{
            HighLevelClient.close();
        }
    }
}
