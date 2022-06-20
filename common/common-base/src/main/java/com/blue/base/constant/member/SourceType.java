package com.blue.base.constant.member;


/**
 * member source type
 *
 * @author liuyunfei
 */
public enum SourceType {

    /**
     * from app
     */
    APP("APP", "from app"),

    /**
     * from WeChat
     */
    WE("WE", "from wechat");

    public final String identity;

    public final String disc;

    SourceType(String identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
