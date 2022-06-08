package com.blue.auth.component.auto.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.component.auto.inter.MemberParamByAutoLoginPackager;
import com.blue.base.constant.auth.CredentialType;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberRegistryParam;

import static com.blue.base.constant.auth.CredentialType.NOT_LOGGED_IN;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;

/**
 * packager for not login
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class NotLoggedInMemberParamPackager implements MemberParamByAutoLoginPackager {

    /**
     * package credential to member register param
     *
     * @param credentialInfo
     * @param memberRegistryParam
     */
    @Override
    public void packageCredentialInfoToRegistryParam(CredentialInfo credentialInfo, MemberRegistryParam memberRegistryParam) {
        throw new BlueException(BAD_REQUEST);
    }

    /**
     * target credential type to package param
     *
     * @return
     */
    @Override
    public CredentialType targetType() {
        return NOT_LOGGED_IN;
    }

}
