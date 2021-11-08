package com.blue.base.constant.base;

/**
 * sync keys
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public enum SyncKey {

    //prefix
    /**
     * portal cache sync key prefix
     */
    PORTALS_REFRESH_PRE("SYNC_PORTALS_REFRESHing_PRE:"),

    /**
     * member role relation update sync key prefix
     */
    MEMBER_ROLE_REL_UPDATE_PRE("MEMBER_ROLE_REL_UPDATING_PRE:"),

    //key
    RESOURCES_SYNC("RESOURCES_SYNC");

    public final String key;

    SyncKey(String key) {
        this.key = key;
    }

}
