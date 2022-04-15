package com.blue.member.service.inter;

import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;

import java.util.List;

/**
 * member register service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface MemberAuthService {

    /**
     * member register
     *
     * @param memberRegistryParam
     * @return
     */
    MemberBasicInfo registerMemberBasic(MemberRegistryParam memberRegistryParam);

    /**
     * member register for auto registry or third party login
     *
     * @param memberRegistryParam
     * @return
     */
    MemberBasicInfo autoRegisterMemberBasic(MemberRegistryParam memberRegistryParam);

    /**
     * package credential attribute to member basic
     *
     * @param credentialTypes
     * @param credential
     * @param memberId
     * @return
     */
    MemberBasicInfo updateMemberCredentialAttr(List<String> credentialTypes, String credential, Long memberId);

}
