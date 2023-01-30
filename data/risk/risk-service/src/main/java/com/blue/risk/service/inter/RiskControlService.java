package com.blue.risk.service.inter;

import com.blue.basic.model.event.IllegalMarkEvent;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.risk.model.IllegalMarkParam;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * risk control service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RiskControlService {

    /**
     * illegal mark
     *
     * @param illegalMarkEvent
     * @return
     */
    Mono<Boolean> illegalMarkByEvent(IllegalMarkEvent illegalMarkEvent);

    /**
     * illegal mark
     *
     * @param illegalMarkParam
     * @return
     */
    Mono<Boolean> illegalMarkByParam(IllegalMarkParam illegalMarkParam);

    /**
     * invalid auth
     *
     * @param memberId
     * @return
     */
    Mono<Boolean> invalidateAuth(Long memberId);

    /**
     * invalid auth async
     *
     * @param memberId
     * @return
     */
    Mono<Boolean> invalidateAuthAsync(Long memberId);

    /**
     * invalid auth batch
     *
     * @param memberIds
     * @return
     */
    Mono<Boolean> invalidateAuthBatch(List<Long> memberIds);

    /**
     * update member status
     *
     * @param memberId
     * @param status
     * @return
     */
    Mono<MemberBasicInfo> updateMemberBasicStatus(Long memberId, Integer status);

    /**
     * update member status batch
     *
     * @param memberIds
     * @param status
     * @return
     */
    Mono<List<MemberBasicInfo>> updateMemberBasicStatusBatch(List<Long> memberIds, Integer status);

}