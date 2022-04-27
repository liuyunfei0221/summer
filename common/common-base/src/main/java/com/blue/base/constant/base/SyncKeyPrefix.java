package com.blue.base.constant.base;

/**
 * sync key prefix
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum SyncKeyPrefix {

    /**
     * portal cache sync key prefix
     */
    PORTALS_REFRESH_PRE("SYNC_PORTALS_REFRESHing_PRE:"),

    /**
     * member role relation update sync key prefix
     */
    MEMBER_ROLE_REL_UPDATE_PRE("MEMBER_ROLE_REL_UPDATING_PRE:"),

    /**
     * invalid member auth key pre
     */
    AUTH_INVALID_BY_MEMBER_ID_PRE("AUTH_INVALID_BY_MEMBER_ID_PRE:"),

    /**
     * question insert key pre
     */
    QUESTION_INSERT_PRE("QUESTION_INSERT_PRE:");

    public final String prefix;

    SyncKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

}
