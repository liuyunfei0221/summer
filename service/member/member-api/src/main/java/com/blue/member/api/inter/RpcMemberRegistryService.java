package com.blue.member.api.inter;

import com.blue.member.api.model.MemberInfo;
import com.blue.member.api.model.MemberRegistryParam;

/**
 * rpc member registry interface
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcMemberRegistryService {

    /**
     * member register for auto registry or third party login
     *
     * @param memberRegistryParam
     * @return
     */
    MemberInfo autoRegisterMemberBasic(MemberRegistryParam memberRegistryParam);

}
