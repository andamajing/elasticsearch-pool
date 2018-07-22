package com.majing.learning.elasticsearch.highapi.documentapi.multigetapi;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author:admin
 * @date:2018/7/16
 * @description
 */
public class MultiGetApiMain {

    public static void main(String[] args) throws IOException {
        try{
            RestHighLevelClient client = HighLevelClient.getInstance();
            MultiGetRequest multiGetRequest = new MultiGetRequest();

            multiGetRequest.add(new MultiGetRequest.Item("jingma2_20180716","testlog","1"));
            multiGetRequest.add(new MultiGetRequest.Item("jingma2_20180716","testlog","2"));
            multiGetRequest.add(new MultiGetRequest.Item("jingma2_20180716","testlog","3"));

            MultiGetResponse multiGetResponse = client.multiGet(multiGetRequest);
            MultiGetItemResponse[] itemResponses = multiGetResponse.getResponses();
            for(int i=0;i<itemResponses.length;i++){
                System.out.println(itemResponses[i].getResponse());
            }
        }finally{
            HighLevelClient.close();
        }
    }
}
