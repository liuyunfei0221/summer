package com.blue.basic.constant.auth;

import com.blue.basic.constant.verify.VerifyType;

import java.util.Set;

import static com.blue.basic.constant.auth.CredentialType.*;
import static com.blue.basic.constant.verify.VerifyType.*;

/**
 * verify type with credential types relation
 *
 * @author liuyunfei
 */
public enum VerifyTypeAndCredentialTypesRelation {

    /**
     * image with credential types
     */
    IMAGE_VT_AND_CTS(IMAGE, Set.of(ACCOUNT_PWD)),

    /**
     * sms with credential types
     */
    SMS_VT_AND_CTS(SMS, Set.of(PHONE_VERIFY_AUTO_REGISTER, PHONE_PWD, LOCAL_PHONE_AUTO_REGISTER, WECHAT_AUTO_REGISTER, MINI_PRO_AUTO_REGISTER)),

    /**
     * email with credential types
     */
    EMAIL_VT_AND_CTS(MAIL, Set.of(EMAIL_VERIFY_AUTO_REGISTER, EMAIL_PWD));

    public final VerifyType verifyType;

    public final Set<CredentialType> credentialTypes;

    VerifyTypeAndCredentialTypesRelation(VerifyType verifyType, Set<CredentialType> credentialTypes) {
        this.verifyType = verifyType;
        this.credentialTypes = credentialTypes;
    }

}
