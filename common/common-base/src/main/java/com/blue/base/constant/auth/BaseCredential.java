package com.blue.base.constant.auth;

import java.util.Set;

import static com.blue.base.constant.auth.CredentialType.*;

/**
 * base credential
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public enum BaseCredential {

    /**
     * phone credential
     */
    PHONE("PHONE", Set.of(PHONE_VERIFY_AUTO_REGISTER, PHONE_PWD, LOCAL_PHONE_AUTO_REGISTER, WECHAT_AUTO_REGISTER, MINI_PRO_AUTO_REGISTER)),

    /**
     * email credential
     */
    EMAIL("EMAIL", Set.of(EMAIL_VERIFY_AUTO_REGISTER, EMAIL_PWD));

    /**
     * identity
     */
    public final String identity;

    public final Set<CredentialType> credentialTypes;

    BaseCredential(String identity, Set<CredentialType> credentialTypes) {
        this.identity = identity;
        this.credentialTypes = credentialTypes;
    }
}
