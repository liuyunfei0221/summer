package com.blue.base.constant.auth;

import com.blue.base.constant.verify.VerifyType;

import java.util.Set;

import static com.blue.base.constant.auth.CredentialType.*;
import static com.blue.base.constant.verify.VerifyType.MAIL;
import static com.blue.base.constant.verify.VerifyType.SMS;

/**
 * verify type with credential types relation
 *
 * @author liuyunfei
 */
public enum VerifyTypeAndCredentialTypesRelation {

    /**
     * sms with credential types
     */
    SMS_VT_AND_LTS(SMS, Set.of(PHONE_VERIFY_AUTO_REGISTER, PHONE_PWD)),

    /**
     * email with credential types
     */
    EMAIL_VT_AND_LTS(MAIL, Set.of(EMAIL_VERIFY_AUTO_REGISTER, EMAIL_PWD));

    public final VerifyType verifyType;

    public final Set<CredentialType> credentialTypes;

    VerifyTypeAndCredentialTypesRelation(VerifyType verifyType, Set<CredentialType> credentialTypes) {
        this.verifyType = verifyType;
        this.credentialTypes = credentialTypes;
    }

}
