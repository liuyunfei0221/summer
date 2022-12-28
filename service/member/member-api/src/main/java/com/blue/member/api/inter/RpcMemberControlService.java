package com.blue.member.api.inter;

import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberInitParam;

import java.util.List;

/**
 * rpc member control interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcMemberControlService {

    /**
     * member register for auto registry or third party session
     *
     * @param memberInitParam
     * @return
     */
    MemberBasicInfo initMemberBasic(MemberInitParam memberInitParam);

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
