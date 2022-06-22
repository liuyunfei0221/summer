package com.blue.base.constant.common;

/**
 * sync key prefix
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum SyncKeyPrefix {

    /**
     * access update sync prefix
     */
    ACCESS_UPDATE_PRE("ACCESS_UPDATE_PRE:"),

    /**
     * bulletins cache sync key prefix
     */
    BULLETINS_CACHE_PRE("BULLETINS_CACHE_PRE:"),

    /**
     * styles cache sync key prefix
     */
    STYLES_CACHE_PRE("STYLES_CACHE_PRE:"),

    /**
     * member role relation update sync key prefix
     */
    MEMBER_ROLE_REL_UPDATE_PRE("MEMBER_ROLE_REL_UPDATING_PRE:"),

    /**
     * operate address sync key pre
     */
    ADDRESS_UPDATE_PRE("ADDRESS_UPDATE:"),

    /**
     * operate card sync key pre
     */
    CARD_UPDATE_PRE("CARD_UPDATE:"),

    /**
     * question insert key pre
     */
    QUESTION_INSERT_PRE("QUESTION_INSERT_PRE:");

    public final String prefix;

    SyncKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

}
