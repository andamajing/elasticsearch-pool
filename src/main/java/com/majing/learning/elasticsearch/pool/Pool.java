package com.majing.learning.elasticsearch.pool;

import com.majing.learning.elasticsearch.exception.ElasticSearchConnectException;
import com.majing.learning.elasticsearch.exception.ElasticSearchException;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.NoSuchElementException;

/**
 * @author : jingma2
 * @date : 2018/7/20
 * @description
 */
public class Pool<T> implements Cloneable {

    protected GenericObjectPool<T> internalPool ;

    public Pool(){
        super();
    }

    public Pool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory){
        initPool(poolConfig, factory);
    }

    public void initPool(final GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory) {

        if (this.internalPool != null) {
            try {
                closeInternalPool();
            } catch (Exception e) {
            }
        }

        this.internalPool = new GenericObjectPool<T>(factory, poolConfig);
    }

    protected void closeInternalPool(){
        try {
            internalPool.close();
        } catch (Exception e) {
            throw new ElasticSearchException("Could not destroy the pool", e);
        }
    }

    public T getResource(){
        try {
            return internalPool.borrowObject();
        } catch (Exception e) {
            throw new ElasticSearchConnectException("Could not get a resource from the pool", e);
        }
    }


    public void returnResource(final T resource){
        if (resource != null) {
            returnResourceObject(resource);
        }
    }

    private void returnResourceObject(final T resource){
        if (resource == null) {
            return;
        }
        try {
            internalPool.returnObject(resource);
        } catch (Exception e) {
            throw new ElasticSearchException("Could not return the resource to the pool", e);
        }
    }

    public void returnBrokenResource(final T resource){
        if (resource != null) {
            returnBrokenResourceObject(resource);
        }
    }

    private void returnBrokenResourceObject(T resource) {
        try {
            internalPool.invalidateObject(resource);
        } catch (Exception e) {
            throw new ElasticSearchException("Could not return the resource to the pool", e);
        }
    }

    public void destroy(){
        closeInternalPool();
    }


    public int getNumActive() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getNumActive();
    }

    public int getNumIdle() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getNumIdle();
    }

    public int getNumWaiters() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getNumWaiters();
    }

    public long getMeanBorrowWaitTimeMillis() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getMeanBorrowWaitTimeMillis();
    }

    public long getMaxBorrowWaitTimeMillis() {
        if (poolInactive()) {
            return -1;
        }

        return this.internalPool.getMaxBorrowWaitTimeMillis();
    }

    private boolean poolInactive() {
        return this.internalPool == null || this.internalPool.isClosed();
    }

    public void addObjects(int count) throws Exception {
        try {
            for (int i = 0; i < count; i++) {
                this.internalPool.addObject();
            }
        } catch (Exception e) {
            throw new Exception("Error trying to add idle objects", e);
        }
    }
}
