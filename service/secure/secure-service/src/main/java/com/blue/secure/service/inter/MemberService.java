package com.blue.secure.service.inter;

import com.blue.member.api.model.MemberBasicInfo;
import com.blue.secure.api.model.ClientLoginParam;
import reactor.core.publisher.Mono;


/**
 * member auth service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface MemberService {

    /**
     * get member by phone and check verify
     *
     * @param clientLoginParam
     * @return
     */
    Mono<MemberBasicInfo> getMemberByPhoneWithAssertVerify(ClientLoginParam clientLoginParam);

    /**
     * get member by phone and check password
     *
     * @param clientLoginParam
     * @return
     */
    Mono<MemberBasicInfo> getMemberByPhoneWithAssertPwd(ClientLoginParam clientLoginParam);

    /**
     * get member by email and check password
     *
     * @param clientLoginParam
     * @return
     */
    Mono<MemberBasicInfo> getMemberByEmailWithAssertPwd(ClientLoginParam clientLoginParam);

}
