package com.blue.caffeine.constant;

/**
 * cache过期策略
 *
 * @author DarkBlue
 */
public enum ExpireStrategy {

    /**
     * 写后
     */
    AFTER_WRITE,

    /**
     * 访问后
     */
    AFTER_ACCESS

}
