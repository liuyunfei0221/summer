package com.blue.base.constant.base;

/**
 * 同步KEY
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public enum SyncKey {

    /**
     * 首页公告缓存KEY
     */
    PORTALS_REFRESH_PRE("SYNC_PORTALS_REFRESH_PRE:"),

    MEMBER_ROLE_REL_UPDATE_PRE("MEMBER_ROLE_REL_UPDATE_PRE:");

    public final String key;

    SyncKey(String key) {
        this.key = key;
    }

}
