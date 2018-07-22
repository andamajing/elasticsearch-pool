package com.majing.learning.elasticsearch.exception;

/**
 * @author : jingma2
 * @date : 2018/7/21
 * @description
 */
public class ElasticSearchConnectException extends ElasticSearchException{

    public ElasticSearchConnectException(String message) {
        super(message);
    }

    public ElasticSearchConnectException(Throwable cause) {
        super(cause);
    }

    public ElasticSearchConnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
