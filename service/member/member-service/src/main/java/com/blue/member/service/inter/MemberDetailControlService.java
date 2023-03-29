package com.blue.member.service.inter;

import com.blue.member.api.model.MemberDetailInfo;
import reactor.core.publisher.Mono;

/**
 * member detail control service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface MemberDetailControlService {

    /**
     * get member detail info mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<MemberDetailInfo> getMemberDetailInfoByMemberIdWithInit(Long memberId);

    /**
     * query member detail by member id with assert
     *
     * @param memberId
     * @return
     */
    Mono<MemberDetailInfo> getMemberDetailInfoByMemberIdWithAssert(Long memberId);

}
