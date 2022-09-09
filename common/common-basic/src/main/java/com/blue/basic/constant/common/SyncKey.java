package com.blue.basic.constant.common;

/**
 * sync keys
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum SyncKey {

    /**
     * resource cache sync key
     */
    RESOURCES_REFRESH_SYNC("RESOURCES_REFRESH_SYNC"),

    /**
     * roles cache sync key
     */
    ROLES_REFRESH_SYNC("ROLES_REFRESH_SYNC"),

    /**
     * default role cache sync key
     */
    DEFAULT_ROLE_REFRESH_SYNC("DEFAULT_ROLE_REFRESH_SYNC"),

    /**
     * relations cache sync key
     */
    ROLE_RESOURCE_RELATIONS_REFRESH_SYNC("ROLE_RESOURCE_RELATIONS_REFRESH_SYNC"),

    /**
     * AUTHORITY_UPDATE_SYNC
     */
    AUTHORITY_UPDATE_SYNC("AUTHORITY_UPDATE_SYNC"),

    /**
     * DEFAULT_ROLE_UPDATE_SYNC
     */
    DEFAULT_ROLE_UPDATE_SYNC("DEFAULT_ROLE_UPDATE_SYNC"),

    /**
     * QR_CODE_CONFIG_UPDATE_SYNC
     */
    QR_CODE_CONFIG_UPDATE_SYNC("QR_CODE_CONFIG_UPDATE_SYNC"),

    /**
     * VERIFY_TEMPLATE_UPDATE_SYNC
     */
    VERIFY_TEMPLATE_UPDATE_SYNC("VERIFY_TEMPLATE_UPDATE_SYNC"),

    /**
     * REGION_UPDATE_SYNC
     */
    REGION_UPDATE_SYNC("REGION_UPDATE_SYNC");


    public final String key;

    SyncKey(String key) {
        this.key = key;
    }

}
