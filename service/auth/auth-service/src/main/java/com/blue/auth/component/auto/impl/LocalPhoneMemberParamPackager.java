package com.blue.auth.component.auto.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.component.auto.inter.MemberParamByAutoLoginPackager;
import com.blue.basic.constant.auth.CredentialType;
import com.blue.member.api.model.MemberInitParam;

import static com.blue.basic.common.base.BlueChecker.isNotBlank;
import static com.blue.basic.common.base.PhoneProcessor.parseLast4no;
import static com.blue.basic.constant.auth.CredentialType.LOCAL_PHONE_AUTO_REGISTER;
import static com.blue.basic.constant.common.BlueCommonThreshold.ID_LEN_MIN;
import static com.blue.basic.constant.common.Symbol.PAR_CONCATENATION;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * packager for session by local phone
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class LocalPhoneMemberParamPackager implements MemberParamByAutoLoginPackager {

    /**
     * package credential to member register param
     *
     * @param credentialInfo
     * @param memberInitParam
     */
    @Override
    public void packageCredentialInfoToRegistryParam(CredentialInfo credentialInfo, MemberInitParam memberInitParam) {
        String credential = credentialInfo.getCredential();
        String name = memberInitParam.getName();

        memberInitParam.setPhone(credential);
        memberInitParam.setName(isNotBlank(name) ? name : randomAlphabetic((int) ID_LEN_MIN.value) + PAR_CONCATENATION.identity + parseLast4no(credential));
    }

    /**
     * target credential type to package param
     *
     * @return
     */
    @Override
    public CredentialType targetType() {
        return LOCAL_PHONE_AUTO_REGISTER;
    }

}
