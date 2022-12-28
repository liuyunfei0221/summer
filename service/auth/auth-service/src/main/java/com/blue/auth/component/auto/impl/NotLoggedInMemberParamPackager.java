package com.blue.auth.component.auto.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.component.auto.inter.MemberParamByAutoLoginPackager;
import com.blue.basic.constant.auth.CredentialType;
import com.blue.basic.model.exps.BlueException;
import com.blue.member.api.model.MemberInitParam;

import static com.blue.basic.constant.auth.CredentialType.NOT_LOGGED_IN;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * packager for not session
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class NotLoggedInMemberParamPackager implements MemberParamByAutoLoginPackager {

    /**
     * package credential to member register param
     *
     * @param credentialInfo
     * @param memberInitParam
     */
    @Override
    public void packageCredentialInfoToRegistryParam(CredentialInfo credentialInfo, MemberInitParam memberInitParam) {
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
