package com.blue.member.service.impl;

import com.blue.basic.model.exps.BlueException;
import com.blue.member.api.model.RealNameInfo;
import com.blue.member.service.inter.RealNameControlService;
import com.blue.member.service.inter.RealNameService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.common.base.BlueChecker.isValidStatus;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.member.converter.MemberModelConverters.REAL_NAME_2_REAL_NAME_INFO;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.*;
import static reactor.core.publisher.Mono.defer;

/**
 * member real name control service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class RealNameControlServiceImpl implements RealNameControlService {

    private static final Logger LOGGER = getLogger(RealNameControlServiceImpl.class);

    private final RealNameService realNameService;

    public RealNameControlServiceImpl(RealNameService realNameService) {
        this.realNameService = realNameService;
    }

    /**
     * get real name info mono by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<RealNameInfo> getRealNameInfoByMemberIdWithInit(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return realNameService.getRealNameInfoByMemberId(memberId)
                .switchIfEmpty(defer(() -> justOrEmpty(realNameService.initRealName(memberId)).map(REAL_NAME_2_REAL_NAME_INFO)))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))));
    }

    /**
     * query real name info by member id with assert
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<RealNameInfo> getRealNameInfoByMemberIdWithAssert(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return getRealNameInfoByMemberIdWithInit(memberId)
                .flatMap(mdi ->
                        isValidStatus(mdi.getStatus()) ?
                                just(mdi)
                                :
                                error(() -> new BlueException(DATA_HAS_BEEN_FROZEN))
                );
    }

}
