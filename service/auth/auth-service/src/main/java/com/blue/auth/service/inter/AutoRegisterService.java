package com.blue.auth.service.inter;

import com.blue.member.api.model.MemberBasicInfo;
import com.blue.auth.api.model.CredentialInfo;

import java.util.List;

/**
 * auto register member service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface AutoRegisterService {

    /**
     * auto register for a new member
     *
     * @param credentials
     * @param roleId
     * @param source
     * @return
     */
    MemberBasicInfo autoRegisterMemberInfo(List<CredentialInfo> credentials, Long roleId, String source);

}
