package com.blue.lake.service.impl;

import com.blue.basic.common.access.AccessProcessor;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.Access;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.lake.config.deploy.NestingResponseDeploy;
import com.blue.lake.remote.consumer.RpcAuthServiceConsumer;
import com.blue.lake.repository.entity.OptEvent;
import com.blue.lake.repository.mapper.OptEventMapper;
import com.blue.lake.service.inter.OptEventService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.getBoolByBool;
import static com.blue.basic.constant.common.BlueBoolean.TRUE;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.OK;
import static com.blue.basic.constant.common.SpecialAccess.VISITOR;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.SummerAttr.DATE_FORMATTER;
import static com.blue.database.common.SearchAfterProcessor.parseSearchAfter;
import static java.time.Instant.ofEpochSecond;
import static java.time.LocalDate.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * option event service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class OptEventServiceImpl implements OptEventService {

    private static final Logger LOGGER = getLogger(OptEventServiceImpl.class);

    private RpcAuthServiceConsumer rpcAuthServiceConsumer;

    private BlueIdentityProcessor blueIdentityProcessor;

    private OptEventMapper optEventMapper;

    public OptEventServiceImpl(RpcAuthServiceConsumer rpcAuthServiceConsumer, BlueIdentityProcessor blueIdentityProcessor,
                               OptEventMapper optEventMapper, NestingResponseDeploy nestingResponseDeploy) {
        this.rpcAuthServiceConsumer = rpcAuthServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.optEventMapper = optEventMapper;

        this.nestingRealUris = ofNullable(nestingResponseDeploy.getUris()).filter(uris -> !isEmpty(uris))
                .map(HashSet::new)
                .orElseGet(() -> new HashSet<>(0));

        this.nestingStatuses = ofNullable(nestingResponseDeploy.getStatuses()).filter(statuses -> !isEmpty(statuses))
                .map(HashSet::new)
                .orElseGet(() -> new HashSet<>(0));

        this.nestingRes = ofNullable(nestingResponseDeploy.getResponse()).filter(StringUtils::isNotEmpty)
                .orElse(EMPTY_VALUE.value);
    }

    private static final Function<Long, String> DATE_STR_FUNC = stamp ->
            ofInstant(ofEpochSecond(stamp), systemDefault()).format(DATE_FORMATTER);

    private Set<String> nestingRealUris;

    private Set<Integer> nestingStatuses;

    private String nestingRes;

    private final BiPredicate<String, Integer> NESTING_PREDICATE = (realUri, status) ->
            (isBlank(realUri) || isNull(status)) || nestingRealUris.contains(realUri) && nestingStatuses.contains(status);

    private final Access UNKNOWN_ACCESS = VISITOR.access;

    private final Long[] EMPTY_ROLES = UNKNOWN_ACCESS.getRoleIds().toArray(Long[]::new);

    /**
     * data event -> option event
     */
    public final Function<DataEvent, OptEvent> DATA_EVENT_2_OPT_EVENT_CONVERTOR = event -> {
        if (isNull(event))
            throw new BlueException(EMPTY_PARAM);

        OptEvent optEvent = new OptEvent();

        optEvent.setDataEventType(ofNullable(event.getDataEventType())
                .filter(BlueChecker::isNotBlank).orElse(EMPTY_VALUE.value));
        optEvent.setDataEventOpType(ofNullable(event.getDataEventOpType())
                .filter(BlueChecker::isNotBlank).orElse(EMPTY_VALUE.value));

        long stamp = ofNullable(event.getStamp()).orElse(TIME_STAMP_GETTER.get());
        optEvent.setStamp(stamp);
        optEvent.setCreateDate(DATE_STR_FUNC.apply(stamp));

        Map<String, String> entries = event.getEntries();

        optEvent.setMethod(ofNullable(entries.get(METHOD.key)).orElse(EMPTY_VALUE.value));
        optEvent.setUri(ofNullable(entries.get(URI.key)).orElse(EMPTY_VALUE.value));

        String realUri = ofNullable(entries.get(REAL_URI.key)).orElse(EMPTY_VALUE.value);
        optEvent.setRealUri(realUri);
        optEvent.setRequestBody(ofNullable(entries.get(REQUEST_BODY.key)).orElse(EMPTY_VALUE.value));
        optEvent.setRequestExtra(ofNullable(entries.get(REQUEST_EXTRA.key)).orElse(EMPTY_VALUE.value));

        int responseStatus = ofNullable(entries.get(RESPONSE_STATUS.key)).map(Integer::parseInt).orElse(OK.status);
        optEvent.setResponseStatus(responseStatus);

        optEvent.setResponseBody(ofNullable(entries.get(RESPONSE_BODY.key)).map(body -> NESTING_PREDICATE.test(realUri, responseStatus) ? nestingRes : body).orElse(EMPTY_VALUE.value));
        optEvent.setResponseExtra(ofNullable(entries.get(RESPONSE_EXTRA.key)).orElse(EMPTY_VALUE.value));
        optEvent.setRequestId(ofNullable(entries.get(REQUEST_ID.key)).orElse(EMPTY_VALUE.value));
        optEvent.setMetadata(ofNullable(entries.get(METADATA.key)).orElse(EMPTY_VALUE.value));

        String jwt = entries.get(JWT.key);
        optEvent.setJwt(ofNullable(jwt).orElse(EMPTY_VALUE.value));

        try {
            Access access = ofNullable(entries.get(ACCESS.key))
                    .filter(BlueChecker::isNotBlank)
                    .map(AccessProcessor::jsonToAccess)
                    .orElseGet(() ->
                            ofNullable(jwt)
                                    .filter(BlueChecker::isNotBlank)
                                    .map(a ->
                                            rpcAuthServiceConsumer.parseAccess(a).toFuture().join()
                                    ).orElse(UNKNOWN_ACCESS)
                    );

            optEvent.setMemberId(access.getId());
            optEvent.setRoleIds(ofNullable(access.getRoleIds())
                    .filter(BlueChecker::isNotEmpty).map(l -> l.toArray(Long[]::new)).orElse(EMPTY_ROLES));
            optEvent.setCredentialType(access.getCredentialType());
            optEvent.setDeviceType(access.getDeviceType());
            optEvent.setLoginTime(access.getLoginTime());
        } catch (Exception e) {
            LOGGER.error("event = {}, e = {}", event, e);
        }

        optEvent.setClientIp(ofNullable(entries.get(CLIENT_IP.key)).orElse(EMPTY_VALUE.value));
        optEvent.setUserAgent(ofNullable(entries.get(USER_AGENT.key)).orElse(EMPTY_VALUE.value));
        optEvent.setSecKey(ofNullable(entries.get(SEC_KEY.key)).orElse(EMPTY_VALUE.value));
        optEvent.setRequestUnDecryption(getBoolByBool(ofNullable(entries.get(REQUEST_UN_DECRYPTION.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        optEvent.setResponseUnEncryption(getBoolByBool(ofNullable(entries.get(RESPONSE_UN_ENCRYPTION.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        optEvent.setExistenceRequestBody(getBoolByBool(ofNullable(entries.get(EXISTENCE_REQUEST_BODY.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        optEvent.setExistenceResponseBody(getBoolByBool(ofNullable(entries.get(EXISTENCE_RESPONSE_BODY.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        optEvent.setDurationSeconds(ofNullable(entries.get(DURATION_SECONDS.key)).map(Integer::parseInt).orElse(0));

        optEvent.setId(blueIdentityProcessor.generate(OptEvent.class));

        return optEvent;
    };

    private final Function<List<DataEvent>, Mono<Boolean>> EVENTS_INSERTER = dataEvents ->
            justOrEmpty(dataEvents)
                    .filter(BlueChecker::isNotEmpty)
                    .map(des -> dataEvents.stream().map(DATA_EVENT_2_OPT_EVENT_CONVERTOR).collect(toList()))
                    .map(oes -> {
                        try {
                            optEventMapper.insertBatch(oes);
                            return true;
                        } catch (Exception e) {
                            LOGGER.info("optEventMapper.insertBatch() failed, dataEvents = {}, e = {}", dataEvents, e);
                            return false;
                        }
                    }).switchIfEmpty(defer(() -> just(false)));

    /**
     * insert events
     *
     * @param dataEvents
     * @return
     */
    @Override
    public Mono<Boolean> insertOptEvents(List<DataEvent> dataEvents) {
        LOGGER.info("dataEvents = {}", dataEvents);
        if (isEmpty(dataEvents))
            throw new BlueException(EMPTY_PARAM);

        return EVENTS_INSERTER.apply(dataEvents);
    }

    /**
     * select by search after
     *
     * @param scrollModelRequest
     * @return
     */
    @Override
    public Mono<ScrollModelResponse<OptEvent, Long>> selectOptEventScrollByScrollAndCursor(ScrollModelRequest<Void, Long> scrollModelRequest) {
        LOGGER.info("scrollModelRequest = {}", scrollModelRequest);
        if (isNull(scrollModelRequest))
            throw new BlueException(EMPTY_PARAM);

        List<OptEvent> optEvents = optEventMapper.selectByRowsAndSearchAfter(scrollModelRequest.getRows(), scrollModelRequest.getCursor());

        return isNotEmpty(optEvents) ?
                just(new ScrollModelResponse<>(optEvents, parseSearchAfter(optEvents, OptEvent::getId)))
                :
                just(new ScrollModelResponse<>(emptyList(), null));
    }

}
