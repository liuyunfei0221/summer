package com.blue.basic.constant.common;

/**
 * sync key prefix
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum SyncKeyPrefix {

    /**
     * request sync key prefix
     */
    REQUEST_SYNC_PRE("REQ_SYNC:"),

    /**
     * credential update sync prefix
     */
    CREDENTIAL_UPDATE_PRE("CREDENTIAL_UPDATE_PRE:"),

    /**
     * finance account insert sync prefix
     */
    FINANCE_ACCOUNT_INSERT_SYNC_PRE("FINANCE_ACCOUNT_INSERT_SYNC_PRE:"),

    /**
     * member detail insert sync prefix
     */
    MEMBER_DETAIL_INSERT_SYNC_PRE("MEMBER_DETAIL_INSERT_SYNC_PRE:"),

    /**
     * access update sync prefix
     */
    ACCESS_UPDATE_PRE("ACCESS_UPDATE:"),

    /**
     * verify template sync key prefix
     */
    VERIFY_TEMPLATES_UPDATE_SYNC_PRE("VERIFY_TEMPLATES_UPDATE_SYNC_PRE:"),

    /**
     * bulletins cache sync key prefix
     */
    BULLETINS_CACHE_PRE("BULLETINS_CACHE:"),

    /**
     * bulletins sync key prefix
     */
    BULLETINS_UPDATE_SYNC_PRE("BULLETINS_UPDATE_SYNC:"),

    /**
     * notice cache sync key prefix
     */
    NOTICE_CACHE_PRE("NOTICE_CACHE:"),

    /**
     * notice sync key prefix
     */
    NOTICE_UPDATE_SYNC_PRE("NOTICE_UPDATE_SYNC:"),

    /**
     * agreement cache sync key prefix
     */
    AGREEMENT_CACHE_PRE("AGREEMENT_CACHE:"),

    /**
     * agreement sync key prefix
     */
    AGREEMENT_UPDATE_SYNC_PRE("AGREEMENT_UPDATE_SYNC:"),

    /**
     * styles cache key prefix
     */
    STYLES_CACHE_PRE("STYLES_CACHE:"),

    /**
     * styles sync key prefix
     */
    STYLES_UPDATE_SYNC_PRE("STYLES_UPDATE_SYNC:"),

    /**
     * member role relation update sync key prefix
     */
    MEMBER_ROLE_REL_UPDATE_PRE("MEMBER_ROLE_REL_UPDATE:"),

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
    QUESTION_INSERT_PRE("QUESTION_INSERT:"),

    /**
     * reward date relation update sync key prefix
     */
    REWARD_DATE_REL_UPDATE_PRE("REWARD_DATE_REL_UPDATE:"),

    /**
     * risk strategy update sync prefix
     */
    RISK_STRATEGY_UPDATE_PRE("RISK_STRATEGY_UPDATE_PRE:");

    public final String prefix;

    SyncKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

}
