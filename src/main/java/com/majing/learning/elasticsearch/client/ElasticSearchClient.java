package com.majing.learning.elasticsearch.client;

import com.majing.learning.elasticsearch.exception.ElasticSearchException;
import com.majing.learning.elasticsearch.pool.ElasticSearchConnectionManager;
import com.majing.learning.elasticsearch.pool.ElasticSearchPool;
import org.apache.http.Header;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author : jingma2
 * @date :2018/7/21
 * @description
 */
public class ElasticSearchClient {

    private ElasticSearchPool pool;

    private ElasticSearchClient(String clusterName){
        this.pool = ElasticSearchConnectionManager.sharePool(clusterName);
    }

    public boolean ping(){
        RestHighLevelClient client = null;
        try{
            client = getResource();
            boolean result = client.ping();
            returnResource(client);
            return result;
        }catch(Exception e){
            returnBrokenResource(client);
            return false;
        }
    }

    public MainResponse info(){
        RestHighLevelClient client = null;
        try{
            client = getResource();
            MainResponse result = client.info();
            returnResource(client);
            return result;
        }catch(Exception e){
            returnBrokenResource(client);
            throw new ElasticSearchException(e);
        }
    }

    public boolean exists(GetRequest getRequest, Header... headers){
        RestHighLevelClient client = null;
        try{
            client = getResource();
            boolean result = client.exists(getRequest,headers);
            returnResource(client);
            return result;
        }catch(Exception e){
            returnBrokenResource(client);
            throw new ElasticSearchException(e);
        }
    }

    public GetResponse get(GetRequest getRequest, Header... headers){
        RestHighLevelClient client = null;
        try{
            client = getResource();
            GetResponse result = client.get(getRequest,headers);
            returnResource(client);
            return result;
        }catch(Exception e){
            returnBrokenResource(client);
            throw new ElasticSearchException(e);
        }
    }

    public RestHighLevelClient getResource(){
        RestHighLevelClient client = null;
        try{
            client = pool.getResource();
            return client;
        }catch(RuntimeException e){
            if(client!=null) {
                returnBrokenResource(client);
            }
            throw new ElasticSearchException(e);
        }
    }

    public void returnResource(RestHighLevelClient client){
        try{
            if(client!=null){
                this.pool.returnResource(client);
            }
        }catch(Exception e){
            this.pool.returnBrokenResource(client);
        }
    }

    public void returnBrokenResource(RestHighLevelClient client){
        pool.returnBrokenResource(client);
    }
}
