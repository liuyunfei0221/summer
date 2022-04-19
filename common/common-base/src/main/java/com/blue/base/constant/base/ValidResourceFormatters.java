package com.blue.base.constant.base;

/**
 * valid resources
 *
 * @author liuyunfei
 */
public enum ValidResourceFormatters {

    /**
     * html freemarker
     */
    HTML(".html"),

    /**
     * js freemarker
     */
    JS(".js");

    public final String identity;

    ValidResourceFormatters(String identity) {
        this.identity = identity;
    }

}
