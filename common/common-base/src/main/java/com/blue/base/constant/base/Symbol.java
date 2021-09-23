package com.blue.base.constant.base;

/**
 * 符号
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public enum Symbol {

    /**
     * 请求特征key前缀
     */
    RATE_LIMIT_KEY_PRE("RI:", "请求特征key前缀"),

    /**
     * 未知标识
     */
    UNKNOWN("UNKNOWN", "未知信息标识"),

    /**
     * 主键
     */
    IDENTITY("id", "主键"),

    /**
     * 参数拼接符
     */
    PAR_CONCATENATION("_", "参数拼接符"),

    /**
     * dbUrl参数配置符
     */
    PAR_CONCATENATION_DATABASE_CONF("?", "dbUrl参数配置符"),

    /**
     * dbUrl参数拼接符
     */
    PAR_CONCATENATION_DATABASE_URL("-", "dbUrl参数拼接符"),

    /**
     * 特殊参数拼接符
     */
    PAR_CONCATENATION_SPEC("&&BLUE&&", "特殊参数拼接符"),

    /**
     * 通配符
     */
    WILDCARD("*", "通配符"),

    /**
     * list元素分隔符
     */
    LIST_ELEMENT_PATH_SEPARATOR(",", "list元素分隔符"),

    /**
     * 路径分隔符
     */
    PATH_SEPARATOR("/", "路径分隔符"),

    /**
     * 格式分隔符
     */
    SCHEME_SEPARATOR(".", "格式分隔符"),

    /**
     * 键值分隔符
     */
    KEY_VALUE_SEPARATOR(":", "键值分隔符"),

    /**
     * 键值对分隔符
     */
    PAIR_SEPARATOR(";", "键值对分隔符");


    /**
     * 符号
     */
    public final String identity;

    /**
     * 描述
     */
    public final String disc;

    Symbol(String identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
