package com.blue.auth.component.auto.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.component.auto.inter.MemberParamByAutoLoginPackager;
import com.blue.base.constant.auth.LoginType;
import com.blue.member.api.model.MemberRegistryParam;

import static com.blue.base.common.base.BlueChecker.isNotBlank;
import static com.blue.base.common.base.EmailProcessor.parsePrefix;
import static com.blue.base.constant.auth.LoginType.EMAIL_VERIFY_AUTO_REGISTER;

/**
 * packager for login by email and verify
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class EmailVerifyMemberParamPackager implements MemberParamByAutoLoginPackager {

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

        memberRegistryParam.setEmail(credential);
        memberRegistryParam.setName(isNotBlank(name) ? name : parsePrefix(credential));
    }

    /**
     * target login type to package param
     *
     * @return
     */
    @Override
    public LoginType targetType() {
        return EMAIL_VERIFY_AUTO_REGISTER;
    }

}
