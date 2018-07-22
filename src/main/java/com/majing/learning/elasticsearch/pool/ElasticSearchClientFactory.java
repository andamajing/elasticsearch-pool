package com.majing.learning.elasticsearch.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : jingma2
 * @date : 2018/7/20
 * @description
 */
public class ElasticSearchClientFactory implements PooledObjectFactory<RestHighLevelClient> {

    private AtomicReference<Set<HostAndPort>> nodesReference = new AtomicReference<Set<HostAndPort>>();

    private String clusterName;

    public ElasticSearchClientFactory(String clusterName, Set<HostAndPort> clusterNodes){
        this.clusterName = clusterName;
        this.nodesReference.set(clusterNodes);
    }

    public PooledObject<RestHighLevelClient> makeObject() throws Exception {
        HttpHost[] nodes = new HttpHost[nodesReference.get().size()];
        List<HttpHost> nodeList = new ArrayList<HttpHost>();
        for(HostAndPort each: nodesReference.get()){
            nodeList.add(new HttpHost(each.getHost(),each.getPort(),each.getSchema()));
        }
        nodes = nodeList.toArray(nodes);
        RestClientBuilder clientBuilder = RestClient.builder(nodes);
        RestHighLevelClient client = new RestHighLevelClient(clientBuilder);
        return new DefaultPooledObject(client);
    }

    public void destroyObject(PooledObject<RestHighLevelClient> pooledObject) throws Exception {
        RestHighLevelClient client = pooledObject.getObject();
        if(client!=null&&client.ping()){
            try {
                client.close();
            }catch (Exception e){
                //ignore
            }
        }
    }

    public boolean validateObject(PooledObject<RestHighLevelClient> pooledObject) {
        RestHighLevelClient client = pooledObject.getObject();
        try {
            return client.ping();
        }catch(Exception e){
            return false;
        }
    }

    public void activateObject(PooledObject<RestHighLevelClient> pooledObject) throws Exception {
        RestHighLevelClient client = pooledObject.getObject();
        boolean response = client.ping();
    }

    public void passivateObject(PooledObject<RestHighLevelClient> pooledObject) throws Exception {
        //nothing
    }


}
