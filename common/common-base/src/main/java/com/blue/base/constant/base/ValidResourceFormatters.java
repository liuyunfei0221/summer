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
     * html模板
     */
    HTML(".html", "html模板"),

    /**
     * js模板
     */
    JS(".js", "js模板");

    public final String identity;

    public final String disc;

    ValidResourceFormatters(String identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
