package com.blue.member.service.inter;

import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;

/**
 * member register service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface MemberRegistryService {

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

}
