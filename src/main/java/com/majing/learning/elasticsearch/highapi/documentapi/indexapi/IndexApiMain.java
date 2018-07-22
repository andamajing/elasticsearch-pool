package com.majing.learning.elasticsearch.highapi.documentapi.indexapi;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class IndexApiMain {

    private static String[] interests = {"唱歌","跳舞","打球","钓鱼"};

    private static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws IOException {
        index();
    }

    private static void indexAll() throws IOException {

        RestHighLevelClient client = HighLevelClient.getInstance();

        int count = 10000;
        StringBuilder content = new StringBuilder();
        for (int i=1; i<=count; i++){
            IndexRequest indexRequest = new IndexRequest("jingma2_test", "testlog", String.valueOf(i));
            int interestCount = random.nextInt(4)+1;
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("name","马靖"+i);
            data.put("age",30);
            boolean isFirst = true;
            for (int index = 0; index<interestCount; index++){
                if(isFirst){
                    content.append(interests[random.nextInt(interests.length)]);
                    isFirst = false;
                }else{
                    content.append(",").append(interests[random.nextInt(interests.length)]);
                }
            }
            data.put("interests",content.toString());
            content.setLength(0);
            indexRequest.source(buildIndexData());

            client.index(indexRequest);
        }

        HighLevelClient.close();
    }

    private static void index() throws IOException {
        IndexRequest indexRequest = new IndexRequest("jingma2_test", "testlog", "1");
        indexRequest.source(buildIndexData());

        RestHighLevelClient client = HighLevelClient.getInstance();
        client.index(indexRequest);
        HighLevelClient.close();
    }

    private static Map<String,Object> buildIndexData(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("name","李永虎");
        data.put("age",31);
        data.put("interests","当兵，跳舞");
        return data;
    }
}
