package com.blue.auth.component.auto.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.component.auto.inter.MemberParamByAutoLoginPackager;
import com.blue.base.constant.auth.CredentialType;
import com.blue.member.api.model.MemberRegistryParam;

import static com.blue.base.common.base.BlueChecker.isNotBlank;
import static com.blue.base.common.base.PhoneProcessor.parseLast4no;
import static com.blue.base.constant.auth.CredentialType.PHONE_PWD;
import static com.blue.base.constant.common.BlueCommonThreshold.ID_LEN_MIN;
import static com.blue.base.constant.common.Symbol.PAR_CONCATENATION;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * packager for login by phone and pwd
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class PhonePwdMemberParamPackager implements MemberParamByAutoLoginPackager {

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
        memberRegistryParam.setName(isNotBlank(name) ? name : randomAlphabetic((int) ID_LEN_MIN.value) + PAR_CONCATENATION.identity + parseLast4no(credential));
    }

    /**
     * target credential type to package param
     *
     * @return
     */
    @Override
    public CredentialType targetType() {
        return PHONE_PWD;
    }

}
