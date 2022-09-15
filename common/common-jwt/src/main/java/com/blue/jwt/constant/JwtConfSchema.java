package com.blue.jwt.constant;

/**
 * jwt conf schema
 *
 * @author liuyunfei
 */
public enum JwtConfSchema {

    /**
     * jwtId len
     */
    RANDOM_JWT_ID(32),

    /**
     * sign key min len
     */
    SEC_KEY_STR_MIN(32),

    /**
     * sign key max len
     */
    SEC_KEY_STR_MAX(512),

    /**
     * gamma key min len
     */
    GAMMA_KEY_STR_MIN(16),

    /**
     * gamma key max len
     */
    GAMMA_KEY_STR_MAX(256),

    /**
     * gamma keys min size
     */
    GAMMA_SECRETS_MIN(16),

    /**
     * gamma keys max size
     */
    GAMMA_SECRETS_MAX(1024),

    /**
     * issuer key max len
     */
    ISSUER_STR_MAX(32),

    /**
     * subject key max len
     */
    SUBJECT_STR_MAX(32),

    /**
     * audience key max len
     */
    AUDIENCE_STR_MAX(32);

    public final int len;

    JwtConfSchema(int len) {
        this.len = len;
    }
}
