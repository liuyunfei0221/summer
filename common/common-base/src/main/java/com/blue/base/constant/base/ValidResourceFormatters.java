package com.blue.base.constant.base;

/**
 * 合法网络资源/只有在这里配置资源类型后,secure中配置的此类资源才会有效
 *
 * @author liuyunfei
 * @date 2021/8/22
 * @apiNote
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
