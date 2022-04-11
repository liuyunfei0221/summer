package com.blue.auth.component.auto.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.component.auto.inter.MemberParamByAutoLoginPackager;
import com.blue.base.constant.auth.CredentialType;
import com.blue.member.api.model.MemberRegistryParam;

import static com.blue.base.common.base.BlueChecker.isNotBlank;
import static com.blue.base.common.base.PhoneProcessor.parseLast4no;
import static com.blue.base.constant.auth.CredentialType.PHONE_VERIFY_AUTO_REGISTER;

/**
 * packager for login by phone and verify
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class PhoneVerifyMemberParamPackager implements MemberParamByAutoLoginPackager {

    /**
     * package credential to member register param
     *
     * @param credentialInfo
     * @param memberRegistryParam
     */
    @Override
    public void packageCredentialInfoToRegistryParam(CredentialInfo credentialInfo, MemberRegistryParam memberRegistryParam) {
        String credential = credentialInfo.getCredential();
        String name = memberRegistryParam.getName();

        memberRegistryParam.setPhone(credential);
        memberRegistryParam.setName(isNotBlank(name) ? name : parseLast4no(credential));
    }

    /**
     * target credential type to package param
     *
     * @return
     */
    @Override
    public CredentialType targetType() {
        return PHONE_VERIFY_AUTO_REGISTER;
    }

}
