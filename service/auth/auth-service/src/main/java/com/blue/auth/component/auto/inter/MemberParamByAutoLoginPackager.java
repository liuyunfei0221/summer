package com.blue.auth.component.auto.inter;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.basic.constant.auth.CredentialType;
import com.blue.member.api.model.MemberInitParam;

/**
 * member register param by auto session packager interface
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface MemberParamByAutoLoginPackager {

    /**
     * package credential to member register param
     *
     * @param credentialInfo
     * @param memberInitParam
     */
    void packageCredentialInfoToRegistryParam(CredentialInfo credentialInfo, MemberInitParam memberInitParam);

    /**
     * target credential type to package param
     *
     * @return
     */
    CredentialType targetType();

}
