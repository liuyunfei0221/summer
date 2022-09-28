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
    TURING_TEST("TT", Set.of(IMAGE)),

    /**
     * login by credential and access
     */
    CREDENTIAL_ACCESS_LOGIN("CAL", Set.of(IMAGE)),

    /**
     * sms verify login with auto register
     */
    PHONE_VERIFY_LOGIN_WITH_AUTO_REGISTER("PVL_WAR", Set.of(SMS, MAIL)),

    /**
     * email verify login with auto register
     */
    EMAIL_VERIFY_LOGIN_WITH_AUTO_REGISTER("EVL_WAR", Set.of(SMS, MAIL)),

    /**
     * setting up credential
     */
    CREDENTIAL_SETTING_UP("CSU", Set.of(IMAGE, SMS, MAIL)),

    /**
     * update credential
     */
    CREDENTIAL_UPDATE("CUP", Set.of(SMS, MAIL)),

    /**
     * update access/password
     */
    UPDATE_ACCESS("UPD_ACC", Set.of(SMS, MAIL)),

    /**
     * reset access/password
     */
    RESET_ACCESS("RST_ACC", Set.of(SMS, MAIL));

    public final String identity;

    public final Set<VerifyType> allowedVerifyTypes;

    VerifyBusinessType(String identity, Set<VerifyType> allowedVerifyTypes) {
        this.identity = identity;
        this.allowedVerifyTypes = allowedVerifyTypes;
    }

}
