package com.blue.auth.component.auto.inter;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.base.constant.auth.LoginType;
import com.blue.member.api.model.MemberRegistryParam;

/**
 * member register param by auto login packager interface
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface MemberParamByAutoLoginPackager {

    /**
     * package credential to member register param
     *
     * @param credentialInfo
     * @param memberRegistryParam
     */
    void packageCredentialInfoToRegistryParam(CredentialInfo credentialInfo, MemberRegistryParam memberRegistryParam);

    /**
     * target login type to package param
     *
     * @return
     */
    LoginType targetType();

}
