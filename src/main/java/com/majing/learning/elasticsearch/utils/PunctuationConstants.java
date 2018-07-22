package com.majing.learning.elasticsearch.utils;

/**
 * @author : jingma2
 * @date : 2018/7/21
 * @description
 */
public enum PunctuationConstants {
    /** 逗号 */
    COMMA(","),

    /** 分号 */
    SEMI_COLON(";"),

    /** 问号 */
    QUESTION_MARK("?"),

    /** 句号 */
    PERIOD("。"),

    /** 点 */
    POINT("."),

    /** 星 */
    STAR("*"),

    /** 冒号 */
    COLON(":");

    /** value */
    private String value;

    /**
     * constructor
     *
     * @param value
     *            value
     */
    private PunctuationConstants(String value) {
        this.value = value;
    }

    /**
     * getter method
     *
     * @see PunctuationConstants#value
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
