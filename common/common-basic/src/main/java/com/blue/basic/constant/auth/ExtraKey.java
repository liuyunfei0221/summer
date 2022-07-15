package com.blue.basic.constant.auth;

/**
 * header extra key
 *
 * @author liuyunfei
 */
public enum ExtraKey {

    /**
     * is a new member?
     */
    NEW_MEMBER("NEW_MEMBER");

    public final String key;

    ExtraKey(String key) {
        this.key = key;
    }

}
