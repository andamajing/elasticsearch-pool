package com.majing.learning.elasticsearch.highapi.documentapi.updateapi;

import com.majing.learning.elasticsearch.highapi.HighLevelClient;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:admin
 * @date:2018/7/16
 * @description
 */
public class UpdateApiMain {
    public static void main(String[] args) throws IOException {
        try{
            RestHighLevelClient client = HighLevelClient.getInstance();
            UpdateRequest updateRequest = new UpdateRequest("jingma2_20180716", "testlog", "1");
            updateRequest.doc(buildIndexData());//传递Map结构数据
            //updateRequest.doc(jsonString, XContentType.JSON);//传递json字符串

//            updateRequest.version(1);//设置待更新文档的版本，防止高并发下误更新
//            updateRequest.upsert(buildIndexData());//没有文档时插入该文档
//            updateRequest.upsert(jsonString, XContentType.JSON);

            UpdateResponse updateResponse = client.update(updateRequest);
                    System.out.println(updateResponse);
        }finally{
            HighLevelClient.close();
        }
    }

    private static Map<String,Object> buildIndexData(){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("name","马靖");
        data.put("age",30);
        data.put("interests","working");
        return data;
    }
}
