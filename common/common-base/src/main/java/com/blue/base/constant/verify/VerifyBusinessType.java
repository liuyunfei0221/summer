package com.blue.base.constant.verify;

/**
 * verify business type
 *
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
public enum VerifyBusinessType {

    /**
     * register
     */
    REGISTER("REGISTER"),

    /**
     * login
     */
    LOGIN("LOGIN"),

    /**
     * reset password
     */
    RESET_PWD("RESET_PWD");

    public final String identity;

    VerifyBusinessType(String identity) {
        this.identity = identity;
    }
}
