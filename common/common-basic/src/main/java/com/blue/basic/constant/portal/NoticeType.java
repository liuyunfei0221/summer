package com.blue.basic.constant.portal;

/**
 * notice type
 *
 * @author liuyunfei
 */
public enum NoticeType {

    /**
     * privacy policy
     */
    PRIVACY_POLICY(1, "privacy policy"),

    /**
     * faq
     */
    FAQ(2, "faq"),

    /**
     * about us
     */
    ABOUT_US(3, "about us"),

    /**
     * terms and conditions
     */
    TERMS_AND_CONDITIONS(4, "terms and conditions");

    /**
     * identity
     */
    public final int identity;

    /**
     * disc
     */
    public final String disc;

    NoticeType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
