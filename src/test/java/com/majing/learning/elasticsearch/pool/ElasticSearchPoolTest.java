package com.majing.learning.elasticsearch.pool;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.HashSet;
import java.util.Set;

/**
 * @author:admin
 * @date:2018/7/20
 * @description
 */
public class ElasticSearchPoolTest {

    public static void main(String[] args) throws Exception {
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("172.31.4.14:9200","172.31.4.14",9200,"http"));
        ElasticSearchPoolConfig config = new ElasticSearchPoolConfig();
        config.setConnectTimeMillis(8000);
        config.setMaxTotal(100);
        config.setClusterName("elasticsearch");
        config.setNodes(nodes);
        ElasticSearchPool pool = new ElasticSearchPool(config);

        long start = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
//            RestHighLevelClient client = pool.getResource();
            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("172.31.4.14", 9200, "http")));
            boolean response = client.ping();
//            System.out.println("ping response: " + response);
//            pool.returnResource(client);
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时(ms)："+(end-start));

    }

}
