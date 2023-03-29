package com.blue.member.service.impl;

import com.blue.basic.model.exps.BlueException;
import com.blue.member.api.model.MemberDetailInfo;
import com.blue.member.service.inter.MemberDetailControlService;
import com.blue.member.service.inter.MemberDetailService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.common.base.BlueChecker.isValidStatus;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.member.converter.MemberModelConverters.MEMBER_DETAIL_2_MEMBER_DETAIL_INFO;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.*;

/**
 * member detail control service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class MemberDetailControlServiceImpl implements MemberDetailControlService {

    private static final Logger LOGGER = getLogger(MemberDetailControlServiceImpl.class);

    private final MemberDetailService memberDetailService;

    public MemberDetailControlServiceImpl(MemberDetailService memberDetailService) {
        this.memberDetailService = memberDetailService;
    }

    /**
     * get member detail info mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MemberDetailInfo> getMemberDetailInfoByMemberIdWithInit(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return memberDetailService.getMemberDetailInfoByMemberId(memberId)
                .switchIfEmpty(defer(() -> justOrEmpty(memberDetailService.initMemberDetail(memberId)).map(MEMBER_DETAIL_2_MEMBER_DETAIL_INFO)))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))));
    }

    /**
     * query member detail by member id with assert
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MemberDetailInfo> getMemberDetailInfoByMemberIdWithAssert(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return getMemberDetailInfoByMemberIdWithInit(memberId)
                .flatMap(mdi ->
                        isValidStatus(mdi.getStatus()) ?
                                just(mdi)
                                :
                                error(() -> new BlueException(DATA_HAS_BEEN_FROZEN))
                );
    }

}
