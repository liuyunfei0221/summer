package com.blue.basic.constant.verify;

import java.util.Set;

import static com.blue.basic.constant.verify.VerifyType.*;

/**
 * verify business type
 *
 * @author liuyunfei
 */
public enum VerifyBusinessType {

    /**
     * turing test
     */
    TURING_TEST("TT", Set.of(IMAGE), true),

    /**
     * login by credential and access
     */
    CREDENTIAL_ACCESS_LOGIN("CAL", Set.of(IMAGE), true),

    /**
     * sms verify login with auto register
     */
    PHONE_VERIFY_LOGIN_WITH_AUTO_REGISTER("PVL_WAR", Set.of(SMS), true),

    /**
     * email verify login with auto register
     */
    EMAIL_VERIFY_LOGIN_WITH_AUTO_REGISTER("EVL_WAR", Set.of(MAIL), true),

    /**
     * setting up credential
     */
    CREDENTIAL_SETTING_UP("CSU", Set.of(SMS, MAIL), false),

    /**
     * update credential
     */
    CREDENTIAL_UPDATE("CUP", Set.of(SMS, MAIL), false),

    /**
     * update access/password
     */
    UPDATE_ACCESS("UPD_ACC", Set.of(SMS, MAIL), false),

    /**
     * reset access/password
     */
    RESET_ACCESS("RST_ACC", Set.of(SMS, MAIL), true);

    public final String identity;

    public final Set<VerifyType> allowedVerifyTypes;

    public final boolean withoutSession;

    VerifyBusinessType(String identity, Set<VerifyType> allowedVerifyTypes, boolean withoutSession) {
        this.identity = identity;
        this.allowedVerifyTypes = allowedVerifyTypes;
        this.withoutSession = withoutSession;
    }

}
