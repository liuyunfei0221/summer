package com.blue.base.constant.media;

/**
 * HTTP headers
 *
 * @author DarkBlue
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
