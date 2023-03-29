package com.blue.member.service.inter;

import com.blue.member.api.model.RealNameInfo;
import reactor.core.publisher.Mono;

/**
 * member real name control service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface RealNameControlService {

    /**
     * get real name info mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<RealNameInfo> getRealNameInfoByMemberIdWithInit(Long memberId);

    /**
     * query real name info by member id with assert
     *
     * @param memberId
     * @return
     */
    Mono<RealNameInfo> getRealNameInfoByMemberIdWithAssert(Long memberId);

}
