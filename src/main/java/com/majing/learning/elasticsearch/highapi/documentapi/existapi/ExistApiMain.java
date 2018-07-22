package com.majing.learning.elasticsearch.highapi.documentapi.existapi;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;

public class ExistApiMain {

    public static void main(String[] args) throws IOException {
        exists();
    }

    public static void exists() throws IOException {
        try{
            RestHighLevelClient client = HighLevelClient.getInstance();
            GetRequest getRequest = new GetRequest("jingma2_test_log", "testlog", "1");
            getRequest.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);
            getRequest.storedFields("_none_");
            boolean exists = client.exists(getRequest);
            System.out.println(exists);
        }finally{
            HighLevelClient.close();
        }
    }
}
