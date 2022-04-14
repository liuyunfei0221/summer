package com.blue.member.api.inter;

import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;

/**
 * rpc member registry interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcMemberRegistryService {

    /**
     * member register for auto registry or third party login
     *
     * @param memberRegistryParam
     * @return
     */
    MemberBasicInfo autoRegisterMemberBasic(MemberRegistryParam memberRegistryParam);

}
