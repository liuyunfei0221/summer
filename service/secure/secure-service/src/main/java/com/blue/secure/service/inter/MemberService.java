package com.blue.secure.service.inter;

import com.blue.member.api.model.MemberBasicInfo;
import reactor.core.publisher.Mono;


/**
 * member auth service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface MemberService {

    /**
     * get member by id
     *
     * @param id
     * @return
     */
    Mono<MemberBasicInfo> selectMemberBasicById(Long id);

    /**
     * get member by phone
     *
     * @param phone
     * @return
     */
    Mono<MemberBasicInfo> selectMemberBasicInfoMonoByPhoneWithAssertVerify(String phone);

    /**
     * get member by phone and check password
     *
     * @param phone
     * @param password
     * @return
     */
    Mono<MemberBasicInfo> selectMemberBasicInfoMonoByPhoneWithAssertPwd(String phone, String password);

    /**
     * get member by email and check password
     *
     * @param email
     * @param password
     * @return
     */
    Mono<MemberBasicInfo> selectMemberBasicInfoMonoByEmailWithAssertPwd(String email, String password);

}
