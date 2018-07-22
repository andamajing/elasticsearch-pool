package com.majing.learning.elasticsearch.pool;

import com.majing.learning.elasticsearch.exception.ElasticSearchException;
import com.majing.learning.elasticsearch.utils.ResourceUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * @author : jingma2
 * @date : 2018/7/21
 * @description
 */
public class ElasticSearchConnectionManager {

    //默认配置文件路径
    protected static final String DEFAULT_CONF_BASE_PATH = "classpath:config/elasticsearch";

    private static ElasticSearchConnectionManager connectionManager;

    /** use for lock */
    private static final byte[] INIT_LOCK = new byte[0];

    private static Map<String,ElasticSearchPool> poolMap = new HashMap<String,ElasticSearchPool>();

    private ElasticSearchConnectionManager(){}

    public static ElasticSearchPool sharePool(String clusterName) {
        if (connectionManager == null) {
            throw new ElasticSearchException("Need to call the load method first!");
        }
        if (poolMap.containsKey(clusterName)) {
            return poolMap.get(clusterName);
        } else {
            throw new ElasticSearchException(clusterName + " connection pool is not exists!");
        }
    }

    public static void load() throws IOException, URISyntaxException {
        load0(DEFAULT_CONF_BASE_PATH);
    }

    public static void load0(String configPath) throws IOException, URISyntaxException {
        initFromConfig(configPath);
    }

    private static void initFromConfig(String configPath) throws IOException, URISyntaxException {
        if(connectionManager==null){
            synchronized (INIT_LOCK){
                if(connectionManager==null){
                    connectionManager = new ElasticSearchConnectionManager();
                    connectionManager.init(configPath);
                }
            }
        }
    }

    private static void init(String configPath) throws IOException, URISyntaxException {
        clean();
        String searchPath = null;
        if(StringUtils.endsWith(configPath,"/")){
            searchPath = configPath + "es-config-*.properties";
        }else{
            searchPath = configPath + "/es-config-*.properties";
        }
        URL[] urlArray = ResourceUtils.loadResources(searchPath);
        if (ArrayUtils.isEmpty(urlArray)) {
            throw new IOException("Any file could not be found in " + searchPath);
        }
        for (URL url : urlArray) {
            try {
                initOfSingleConfigFile(url);
            }catch(Exception e){
                throw new ElasticSearchException("Load resource from url error in:" + url, e);
            }
        }
    }

    private static void initOfSingleConfigFile(URL url) throws Exception{
        String esClusterName = StringUtils.substringBetween(url.getFile(), "es-config-", ".properties");
        if (StringUtils.isNotBlank(esClusterName)) {
            return;
        }

        Properties properties = new Properties();
        InputStream is = new BufferedInputStream(new FileInputStream(url.getFile()));
        properties.load(is);

        String clusterName = "elasticsearch";
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        long connectTimeout = 8000;
        int poolMaxTotal = 8;
        int poolMaxIdle = 8;
        int poolMinIdle = 0;
        long maxWaitMillis=-1;
        boolean testOnBorrow = false;
        boolean testWhileIdle = false;
        boolean testOnCreate = false;
        boolean testOnReturn = false;

        Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            String name = String.valueOf(entry.getKey());
            String value = properties.getProperty(name);
            if (StringUtils.startsWithIgnoreCase(name, "cluster.node")) {
                if (StringUtils.isNotBlank(value)) {
                    String[] hostAndPort = StringUtils.split(value,":",2);
                    if(hostAndPort.length==2&&Integer.parseInt(hostAndPort[1])>0) {
                        nodes.add(new HostAndPort(name, hostAndPort[0], Integer.parseInt(hostAndPort[1]), "http"));
                    }
                }
            }else if(StringUtils.equalsIgnoreCase(name, "cluster.name")){
                clusterName = properties.getProperty(name);
            }else if(StringUtils.equalsIgnoreCase(name, "timeout")){
                connectTimeout = Integer.parseInt(value);
            }else if(StringUtils.equalsIgnoreCase(name, "maxTotal")){
                poolMaxTotal = Integer.parseInt(value);
            }else if(StringUtils.equalsIgnoreCase(name, "maxIdle")){
                poolMaxIdle = Integer.parseInt(value);
            }else if(StringUtils.equalsIgnoreCase(name, "minIdle")){
                poolMinIdle = Integer.parseInt(value);
            }else if(StringUtils.equalsIgnoreCase(name, "testOnBorrow")){
                testOnBorrow = Boolean.parseBoolean(value);
            }else if(StringUtils.equalsIgnoreCase(name, "testWhileIdle")){
                testWhileIdle = Boolean.parseBoolean(value);
            }else if(StringUtils.equalsIgnoreCase(name, "testOnCreate")){
                testOnCreate = Boolean.parseBoolean(value);
            }else if(StringUtils.equalsIgnoreCase(name, "testOnReturn")){
                testOnReturn = Boolean.parseBoolean(value);
            }else if(StringUtils.equalsIgnoreCase(name, "maxWaitMillis")){
                maxWaitMillis = Long.parseLong(value);
            }
        }

        if(poolMap.get(clusterName)!=null){
            return;
        }

        if(nodes.isEmpty()){
            return;
        }

        ElasticSearchPoolConfig clusterConfig = new ElasticSearchPoolConfig();
        clusterConfig.setConnectTimeMillis(connectTimeout);
        clusterConfig.setMaxTotal(poolMaxTotal);
        clusterConfig.setMaxIdle(poolMaxIdle);
        clusterConfig.setMinIdle(poolMinIdle);
        clusterConfig.setMaxWaitMillis(maxWaitMillis);
        clusterConfig.setTestOnBorrow(testOnBorrow);
        clusterConfig.setTestWhileIdle(testWhileIdle);
        clusterConfig.setTestOnCreate(testOnCreate);
        clusterConfig.setTestOnReturn(testOnReturn);
        clusterConfig.setClusterName(clusterName);
        clusterConfig.setNodes(nodes);

        ElasticSearchPool pool = new ElasticSearchPool(clusterConfig);
        poolMap.put(clusterName,pool);
    }

    public void close() {
        if (connectionManager == null) {
            return;
        }
        clean();
        connectionManager = null;
    }

    private static void clean() {
        if(MapUtils.isEmpty(poolMap)){
            return;
        }
        Set<Map.Entry<String,ElasticSearchPool>> poolMapEntrySets = poolMap.entrySet();
        Iterator<Map.Entry<String,ElasticSearchPool>> iterator = poolMapEntrySets.iterator();
        while(iterator.hasNext()){
            ElasticSearchPool pool = iterator.next().getValue();
            if(pool!=null){
                pool.destroy();
            }
        }
        poolMap.clear();
    }

}
