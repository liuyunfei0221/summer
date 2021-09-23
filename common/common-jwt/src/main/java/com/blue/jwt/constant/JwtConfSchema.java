package com.blue.jwt.constant;

/**
 * 数据id与分片配置
 *
 * @author DarkBlue
 */
public enum JwtConfSchema {

    /**
     * jwtId长度
     */
    RANDOM_JWT_ID(32),

    /**
     * 签名key最小长度
     */
    SEC_KEY_STR_MIN(32),

    /**
     * 签名key最大长度
     */
    SEC_KEY_STR_MAX(512),

    /**
     * 混淆key最小长度
     */
    GAMMA_KEY_STR_MIN(16),

    /**
     * 混淆key最大长度
     */
    GAMMA_KEY_STR_MAX(256),

    /**
     * 混淆key最小数量
     */
    GAMMA_SECRETS_MIN(16),

    /**
     * 混淆key最大数量
     */
    GAMMA_SECRETS_MAX(1024);

    public final int len;

    JwtConfSchema(int len) {
        this.len = len;
    }
}
