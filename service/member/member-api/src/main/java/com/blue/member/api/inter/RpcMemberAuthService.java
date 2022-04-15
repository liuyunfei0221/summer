package com.blue.member.api.inter;

import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;

import java.util.List;

/**
 * rpc member registry interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcMemberAuthService {

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
