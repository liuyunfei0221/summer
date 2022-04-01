package com.blue.auth.component.auto.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.component.auto.inter.MemberParamByAutoLoginPackager;
import com.blue.base.constant.auth.LoginType;
import com.blue.member.api.model.MemberRegistryParam;

import static com.blue.base.common.base.BlueChecker.isNotBlank;
import static com.blue.base.common.base.PhoneProcessor.parseLast4no;
import static com.blue.base.constant.auth.LoginType.LOCAL_PHONE_AUTO_REGISTER;

/**
 * packager for login by local phone
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class LocalPhoneMemberParamPackager implements MemberParamByAutoLoginPackager {

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
     * target login type to package param
     *
     * @return
     */
    @Override
    public LoginType targetType() {
        return LOCAL_PHONE_AUTO_REGISTER;
    }

}
