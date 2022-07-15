package com.blue.basic.constant.media;

/**
 * HTTP headers
 *
 * @author liuyunfei
 */
public enum MailHeader {

    /**
     * LIST_UNSUBSCRIBE
     */
    LIST_UNSUBSCRIBE("List-Unsubscribe");


    public final String name;

    MailHeader(String name) {
        this.name = name;
    }
}
