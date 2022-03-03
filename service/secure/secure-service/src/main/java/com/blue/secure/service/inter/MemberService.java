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
    Mono<MemberBasicInfo> selectMemberBasicInfoMonoById(Long id);

    /**
     * get member by phone
     *
     * @param phone
     * @return
     */
    Mono<MemberBasicInfo> selectMemberBasicInfoMonoByPhone(String phone);

    /**
     * get member by email
     *
     * @param email
     * @return
     */
    Mono<MemberBasicInfo> selectMemberBasicInfoMonoByEmail(String email);

}
