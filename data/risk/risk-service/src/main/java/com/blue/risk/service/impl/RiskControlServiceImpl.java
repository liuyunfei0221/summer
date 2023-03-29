package com.blue.risk.service.impl;

import com.blue.basic.constant.common.BlueCommonThreshold;
import com.blue.basic.model.event.IllegalMarkEvent;
import com.blue.basic.model.event.InvalidAuthEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.risk.event.producer.IllegalMarkProducer;
import com.blue.risk.event.producer.InvalidAuthProducer;
import com.blue.risk.model.IllegalMarkParam;
import com.blue.risk.remote.consumer.RpcAuthServiceConsumer;
import com.blue.risk.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.risk.service.inter.RiskControlService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConstantProcessor.assertStatus;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.risk.converter.RiskModelConverters.ILLEGAL_MARK_PARAM_2_ILLEGAL_MARK_EVENT_CONVERTER;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Flux.concat;
import static reactor.core.publisher.Mono.fromRunnable;
import static reactor.core.publisher.Mono.just;


/**
 * risk control service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class RiskControlServiceImpl implements RiskControlService {

    private static final Logger LOGGER = getLogger(RiskControlServiceImpl.class);

    private final IllegalMarkProducer illegalMarkProducer;

    private final InvalidAuthProducer invalidAuthProducer;

    private final RpcAuthServiceConsumer rpcAuthServiceConsumer;

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    public RiskControlServiceImpl(IllegalMarkProducer illegalMarkProducer, InvalidAuthProducer invalidAuthProducer, RpcAuthServiceConsumer rpcAuthServiceConsumer, RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer) {
        this.illegalMarkProducer = illegalMarkProducer;
        this.invalidAuthProducer = invalidAuthProducer;
        this.rpcAuthServiceConsumer = rpcAuthServiceConsumer;
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
    }

    /**
     * illegal mark
     *
     * @param illegalMarkEvent
     * @return
     */
    @Override
    public Mono<Boolean> illegalMarkByEvent(IllegalMarkEvent illegalMarkEvent) {
        LOGGER.info("illegalMarkEvent = {}", illegalMarkEvent);
        if (isNull(illegalMarkEvent))
            throw new BlueException(EMPTY_PARAM);

        try {
            illegalMarkProducer.send(illegalMarkEvent);
            return just(true);
        } catch (Exception e) {
            LOGGER.info("event send failed, e = {}", e);
            return just(true);
        }
    }

    /**
     * illegal mark
     *
     * @param illegalMarkParam
     * @return
     */
    @Override
    public Mono<Boolean> illegalMarkByParam(IllegalMarkParam illegalMarkParam) {
        LOGGER.info("illegalMarkParam = {}", illegalMarkParam);
        if (isNull(illegalMarkParam))
            throw new BlueException(EMPTY_PARAM);

        return this.illegalMarkByEvent(ILLEGAL_MARK_PARAM_2_ILLEGAL_MARK_EVENT_CONVERTER.apply(illegalMarkParam));
    }

    /**
     * invalid auth
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Boolean> invalidateAuth(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return rpcAuthServiceConsumer.invalidateAuthByMemberId(memberId);
    }

    /**
     * invalid auth async
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Boolean> invalidateAuthAsync(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return fromRunnable(() -> invalidAuthProducer.send(new InvalidAuthEvent(memberId)))
                .then(just(true))
                .onErrorResume(t -> {
                    LOGGER.error("invalidAuthProducer send failed, t = {}", t);
                    return just(false);
                });
    }

    /**
     * invalid auth batch
     *
     * @param memberIds
     * @return
     */
    @Override
    public Mono<Boolean> invalidateAuthBatch(List<Long> memberIds) {
        LOGGER.info("memberIds = {}", memberIds);
        if (isInvalidIdentities(memberIds))
            throw new BlueException(INVALID_IDENTITY);
        if (memberIds.size() > BlueCommonThreshold.DB_WRITE.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return concat(memberIds.stream().map(rpcAuthServiceConsumer::invalidateAuthByMemberId).collect(toList()))
                .reduce((a, b) -> a && b);
    }

    /**
     * update member status
     *
     * @param memberId
     * @param status
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> updateMemberBasicStatus(Long memberId, Integer status) {
        LOGGER.info("memberId = {}, status = {}", memberId, status);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);
        assertStatus(status, false);

        return rpcMemberBasicServiceConsumer.updateMemberBasicStatus(memberId, status);
    }

    /**
     * update member status batch
     *
     * @param memberIds
     * @param status
     * @return
     */
    @Override
    public Mono<List<MemberBasicInfo>> updateMemberBasicStatusBatch(List<Long> memberIds, Integer status) {
        LOGGER.info("memberIds = {}, status = {}", memberIds, status);
        if (isInvalidIdentities(memberIds))
            throw new BlueException(INVALID_IDENTITY);
        if (memberIds.size() > BlueCommonThreshold.DB_WRITE.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        assertStatus(status, false);

        return rpcMemberBasicServiceConsumer.updateMemberBasicStatusBatch(memberIds, status);
    }

}