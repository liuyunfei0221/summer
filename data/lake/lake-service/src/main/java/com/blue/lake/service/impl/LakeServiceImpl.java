package com.blue.lake.service.impl;

import com.blue.auth.api.model.ResourceInfo;
import com.blue.basic.common.access.AccessProcessor;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.Access;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.lake.config.deploy.NestingResponseDeploy;
import com.blue.lake.remote.consumer.RpcAuthServiceConsumer;
import com.blue.lake.repository.entity.OptEvent;
import com.blue.lake.service.inter.LakeService;
import com.blue.lake.service.inter.OptEventService;
import com.blue.lake.service.inter.ResourceService;
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
import static com.blue.basic.common.base.CommonFunctions.*;
import static com.blue.basic.common.base.ConstantProcessor.getBoolByBool;
import static com.blue.basic.constant.common.BlueBoolean.TRUE;
import static com.blue.basic.constant.common.BlueCommonThreshold.NON_RESOURCE_ID;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.OK;
import static com.blue.basic.constant.common.SpecialAccess.VISITOR;
import static com.blue.basic.constant.common.SpecialIntegerElement.ZERO;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.SummerAttr.DATE_FORMATTER;
import static java.time.Instant.ofEpochSecond;
import static java.time.LocalDate.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * lake service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class LakeServiceImpl implements LakeService {

    private static final Logger LOGGER = getLogger(LakeServiceImpl.class);

    private RpcAuthServiceConsumer rpcAuthServiceConsumer;

    private BlueIdentityProcessor blueIdentityProcessor;

    private ResourceService resourceService;

    private OptEventService optEventService;

    public LakeServiceImpl(RpcAuthServiceConsumer rpcAuthServiceConsumer, BlueIdentityProcessor blueIdentityProcessor, ResourceService resourceService, OptEventService optEventService,
                           NestingResponseDeploy nestingResponseDeploy) {
        this.rpcAuthServiceConsumer = rpcAuthServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.resourceService = resourceService;
        this.optEventService = optEventService;

        this.nestingRealUris = ofNullable(nestingResponseDeploy.getUris()).filter(uris -> !isEmpty(uris))
                .map(HashSet::new)
                .orElseGet(() -> new HashSet<>(0));

        this.nestingStatuses = ofNullable(nestingResponseDeploy.getStatuses()).filter(statuses -> !isEmpty(statuses))
                .map(HashSet::new)
                .orElseGet(() -> new HashSet<>(0));

        this.nestingRes = GSON.toJson(new BlueResponse<>(OK.code, ofNullable(nestingResponseDeploy.getResponse()).filter(StringUtils::isNotEmpty)
                .orElse(EMPTY_VALUE.value), OK.message.intern()));
    }

    private Set<String> nestingRealUris;

    private Set<Integer> nestingStatuses;

    private String nestingRes;

    private final BiPredicate<String, Integer> NESTING_PREDICATE = (uri, status) ->
            isNotBlank(uri) && isNotNull(status) && nestingRealUris.contains(uri) && nestingStatuses.contains(status);

    private final Access UNKNOWN_ACCESS = VISITOR.access;

    private final Long[] EMPTY_ROLES = UNKNOWN_ACCESS.getRoleIds().toArray(Long[]::new);

    private final ResourceInfo NON_RESOURCE = new ResourceInfo(NON_RESOURCE_ID.value, EMPTY_VALUE.value, EMPTY_VALUE.value, EMPTY_VALUE.value, EMPTY_VALUE.value, EMPTY_VALUE.value,
            false, true, true, false, false, ZERO.value, EMPTY_VALUE.value, EMPTY_VALUE.value);

    private static final Function<Long, String> DATE_STR_FUNC = stamp ->
            ofInstant(ofEpochSecond(stamp), systemDefault()).format(DATE_FORMATTER);

    /**
     * data event -> option event
     */
    public final Function<DataEvent, Mono<OptEvent>> DATA_EVENT_2_OPT_EVENT_CONVERTOR = (dataEvent) -> {
        if (isNull(dataEvent))
            return error(() -> new BlueException(EMPTY_PARAM));

        OptEvent optEvent = new OptEvent();

        optEvent.setDataEventType(ofNullable(dataEvent.getDataEventType())
                .filter(BlueChecker::isNotBlank).orElse(EMPTY_VALUE.value));
        optEvent.setDataEventOpType(ofNullable(dataEvent.getDataEventOpType())
                .filter(BlueChecker::isNotBlank).orElse(EMPTY_VALUE.value));

        long stamp = ofNullable(dataEvent.getStamp()).orElse(TIME_STAMP_GETTER.get());
        optEvent.setStamp(stamp);
        optEvent.setCreateDate(DATE_STR_FUNC.apply(stamp));

        Map<String, String> entries = dataEvent.getEntries();

        optEvent.setMethod(ofNullable(entries.get(METHOD.key)).orElse(EMPTY_VALUE.value));

        String uri = ofNullable(entries.get(URI.key)).orElse(EMPTY_VALUE.value);
        optEvent.setUri(uri);
        optEvent.setRealUri(ofNullable(entries.get(REAL_URI.key)).orElse(EMPTY_VALUE.value));

        optEvent.setRequestBody(ofNullable(entries.get(REQUEST_BODY.key)).orElse(EMPTY_VALUE.value));
        optEvent.setRequestExtra(ofNullable(entries.get(REQUEST_EXTRA.key)).orElse(EMPTY_VALUE.value));

        int responseStatus = ofNullable(entries.get(RESPONSE_STATUS.key)).map(Integer::parseInt).orElse(OK.status);
        optEvent.setResponseStatus(responseStatus);

        optEvent.setResponseBody(ofNullable(entries.get(RESPONSE_BODY.key)).map(body -> NESTING_PREDICATE.test(uri, responseStatus) ? nestingRes : body).orElse(EMPTY_VALUE.value));
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
            LOGGER.error("dataEvent = {}, e = {}", dataEvent, e);
        }

        optEvent.setClientIp(ofNullable(entries.get(CLIENT_IP.key)).orElse(EMPTY_VALUE.value));
        optEvent.setUserAgent(ofNullable(entries.get(USER_AGENT.key)).orElse(EMPTY_VALUE.value));
        optEvent.setSecKey(ofNullable(entries.get(SEC_KEY.key)).orElse(EMPTY_VALUE.value));
        optEvent.setRequestUnDecryption(getBoolByBool(ofNullable(entries.get(REQUEST_UN_DECRYPTION.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        optEvent.setResponseUnEncryption(getBoolByBool(ofNullable(entries.get(RESPONSE_UN_ENCRYPTION.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        optEvent.setExistenceRequestBody(getBoolByBool(ofNullable(entries.get(EXISTENCE_REQUEST_BODY.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        optEvent.setExistenceResponseBody(getBoolByBool(ofNullable(entries.get(EXISTENCE_RESPONSE_BODY.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        optEvent.setDurationSeconds(ofNullable(entries.get(DURATION_SECONDS.key)).map(Integer::parseInt).orElse(0));

        long id = blueIdentityProcessor.generate(OptEvent.class);
        optEvent.setId(id);
        optEvent.setCursor(id);

        return resourceService.getResourceInfoByResourceKey(REQ_RES_KEY_GENERATOR.apply(optEvent.getMethod(), optEvent.getUri()))
                .switchIfEmpty(defer(() -> just(NON_RESOURCE)))
                .map(ri -> {
                    optEvent.setResourceId(ri.getId());
                    optEvent.setModule(ri.getModule().intern());
                    optEvent.setRelativeUri(ri.getRelativeUri().intern());
                    optEvent.setAbsoluteUri(ri.getAbsoluteUri().intern());
                    optEvent.setRelationView(ri.getRelationView().intern());
                    optEvent.setAuthenticate(getBoolByBool(ri.getAuthenticate()).status);
                    optEvent.setType(ri.getType());
                    optEvent.setName(ri.getName().intern());

                    return optEvent;
                }).switchIfEmpty(defer(() -> just(optEvent)));
    };

    private final Function<List<DataEvent>, Mono<Boolean>> EVENTS_INSERTER = dataEvents ->
            justOrEmpty(dataEvents)
                    .filter(BlueChecker::isNotEmpty)
                    .flatMap(des ->
                            fromIterable(dataEvents)
                                    .flatMap(DATA_EVENT_2_OPT_EVENT_CONVERTOR)
                                    .collectList()
                                    .flatMap(optEventService::insertOptEvents)
                                    .onErrorResume(t -> {
                                        LOGGER.error("EVENTS_INSERTER failed, t = {}", t);
                                        return just(false);
                                    }))
                    .switchIfEmpty(defer(() -> just(false)));

    /**
     * handle events
     *
     * @param dataEvents
     */
    @Override
    public Mono<Boolean> handleEvents(List<DataEvent> dataEvents) {
        LOGGER.info("dataEvents = {}", dataEvents);
        if (isEmpty(dataEvents))
            return error(() -> new BlueException(EMPTY_PARAM));

        return EVENTS_INSERTER.apply(dataEvents);
    }

}