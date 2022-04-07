package com.blue.base.constant.base;

/**
 * sync key prefix
 *
 * @author DarkBlue
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
     * IMAGE_VERIFY_RATE_LIMIT_KEY_PRE
     */
    IMAGE_VERIFY_RATE_LIMIT_KEY_PRE("image_v_"),

    /**
     * SMS_VERIFY_RATE_LIMIT_KEY_PRE
     */
    SMS_VERIFY_RATE_LIMIT_KEY_PRE("sms_v_"),

    /**
     * MAIL_VERIFY_RATE_LIMIT_KEY_PRE
     */
    MAIL_VERIFY_RATE_LIMIT_KEY_PRE("mail_v_");

    public final String prefix;

    SyncKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

}
