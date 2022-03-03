package com.blue.member.service.inter;

import com.blue.member.api.model.MemberInfo;
import com.blue.member.api.model.MemberRegistryParam;

/**
 * member register service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface MemberRegistryService {

    /**
     * member register
     *
     * @param memberRegistryParam
     * @return
     */
    MemberInfo registerMemberBasic(MemberRegistryParam memberRegistryParam);

    /**
     * member register for auto registry or third party login
     *
     * @param memberRegistryParam
     * @return
     */
    MemberInfo autoRegisterMemberBasic(MemberRegistryParam memberRegistryParam);

}
