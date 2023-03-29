package com.blue.risk.service.impl;

import com.blue.auth.api.model.ResourceInfo;
import com.blue.basic.common.access.AccessProcessor;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.SpecialAccess;
import com.blue.basic.model.common.Access;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.risk.api.model.RiskAsserted;
import com.blue.risk.api.model.RiskEvent;
import com.blue.risk.api.model.RiskHit;
import com.blue.risk.component.risk.RiskProcessor;
import com.blue.risk.config.deploy.NestingResponseDeploy;
import com.blue.risk.repository.entity.RiskHitRecord;
import com.blue.risk.service.inter.AuthService;
import com.blue.risk.service.inter.ResourceService;
import com.blue.risk.service.inter.RiskAnalyzeService;
import com.blue.risk.service.inter.RiskHitRecordService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.*;
import static com.blue.basic.common.base.ConstantProcessor.getBoolByBool;
import static com.blue.basic.constant.common.BlueBoolean.TRUE;
import static com.blue.basic.constant.common.BlueCommonThreshold.NON_RESOURCE_ID;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialAccess.VISITOR;
import static com.blue.basic.constant.common.SpecialIntegerElement.ZERO;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.SummerAttr.DATE_FORMATTER;
import static java.time.Instant.ofEpochSecond;
import static java.time.LocalDate.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.*;

/**
 * risk analyse service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Service
public class RiskAnalyzeServiceImpl implements RiskAnalyzeService {

    private static final Logger LOGGER = getLogger(RiskAnalyzeServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private AuthService authService;

    private ResourceService resourceService;

    private final RiskProcessor riskProcessor;

    private RiskHitRecordService riskHitRecordService;

    public RiskAnalyzeServiceImpl(BlueIdentityProcessor blueIdentityProcessor, AuthService authService, ResourceService resourceService,
                                  RiskProcessor riskProcessor, RiskHitRecordService riskHitRecordService, NestingResponseDeploy nestingResponseDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.authService = authService;
        this.resourceService = resourceService;
        this.riskProcessor = riskProcessor;
        this.riskHitRecordService = riskHitRecordService;

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

    private final Access VISITOR_ACCESS = VISITOR.access;

    private final Long[] EMPTY_ROLES = VISITOR_ACCESS.getRoleIds().toArray(Long[]::new);

    private final ResourceInfo NON_RESOURCE = new ResourceInfo(NON_RESOURCE_ID.value, EMPTY_VALUE.value, EMPTY_VALUE.value, EMPTY_VALUE.value, EMPTY_VALUE.value, EMPTY_VALUE.value,
            false, true, true, false, false, ZERO.value, EMPTY_VALUE.value, EMPTY_VALUE.value);

    private static final Function<Long, String> DATE_STR_FUNC = stamp ->
            ofInstant(ofEpochSecond(stamp), systemDefault()).format(DATE_FORMATTER);

    /**
     * data event -> risk event
     */
    private final Function<DataEvent, Mono<RiskEvent>> DATA_EVENT_2_RISK_EVENT = dataEvent -> {
        if (isNull(dataEvent))
            throw new BlueException(INVALID_PARAM);

        RiskEvent riskEvent = new RiskEvent();

        riskEvent.setDataEventType(ofNullable(dataEvent.getDataEventType())
                .filter(BlueChecker::isNotBlank).orElse(EMPTY_VALUE.value).intern());
        riskEvent.setDataEventOpType(ofNullable(dataEvent.getDataEventOpType())
                .filter(BlueChecker::isNotBlank).orElse(EMPTY_VALUE.value).intern());

        long stamp = ofNullable(dataEvent.getStamp()).orElse(TIME_STAMP_GETTER.get());

        riskEvent.setStamp(stamp);
        riskEvent.setCreateDate(DATE_STR_FUNC.apply(stamp));

        Map<String, String> entries = dataEvent.getEntries();

        riskEvent.setMethod(ofNullable(entries.get(METHOD.key)).orElse(EMPTY_VALUE.value).intern());

        String uri = ofNullable(entries.get(URI.key)).orElse(EMPTY_VALUE.value);
        riskEvent.setUri(uri);
        riskEvent.setRealUri(ofNullable(entries.get(REAL_URI.key)).orElse(EMPTY_VALUE.value));
        riskEvent.setRequestBody(ofNullable(entries.get(REQUEST_BODY.key)).orElse(EMPTY_VALUE.value));
        riskEvent.setRequestExtra(ofNullable(entries.get(REQUEST_EXTRA.key)).orElse(EMPTY_VALUE.value));

        int responseStatus = ofNullable(entries.get(RESPONSE_STATUS.key)).map(Integer::parseInt).orElse(OK.status);

        riskEvent.setResponseStatus(responseStatus);
        riskEvent.setResponseBody(ofNullable(entries.get(RESPONSE_BODY.key)).map(body -> NESTING_PREDICATE.test(uri, responseStatus) ? nestingRes : body).orElse(EMPTY_VALUE.value));

        riskEvent.setResponseExtra(ofNullable(entries.get(RESPONSE_EXTRA.key)).orElse(EMPTY_VALUE.value));
        riskEvent.setRequestId(ofNullable(entries.get(REQUEST_ID.key)).orElse(EMPTY_VALUE.value));
        riskEvent.setMetadata(ofNullable(entries.get(METADATA.key)).orElse(EMPTY_VALUE.value));

        riskEvent.setClientIp(ofNullable(entries.get(CLIENT_IP.key)).orElse(EMPTY_VALUE.value));
        riskEvent.setUserAgent(ofNullable(entries.get(USER_AGENT.key)).orElse(EMPTY_VALUE.value));
        riskEvent.setSecKey(ofNullable(entries.get(SEC_KEY.key)).orElse(EMPTY_VALUE.value));
        riskEvent.setRequestUnDecryption(getBoolByBool(ofNullable(entries.get(REQUEST_UN_DECRYPTION.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        riskEvent.setResponseUnEncryption(getBoolByBool(ofNullable(entries.get(RESPONSE_UN_ENCRYPTION.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        riskEvent.setExistenceRequestBody(getBoolByBool(ofNullable(entries.get(EXISTENCE_REQUEST_BODY.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        riskEvent.setExistenceResponseBody(getBoolByBool(ofNullable(entries.get(EXISTENCE_RESPONSE_BODY.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        riskEvent.setDurationSeconds(ofNullable(entries.get(DURATION_SECONDS.key)).map(Integer::parseInt).orElse(ZERO.value));

        String jwt = ofNullable(entries.get(JWT.key)).orElse(EMPTY_VALUE.value);
        riskEvent.setJwt(jwt);

        return justOrEmpty(entries.get(ACCESS.key))
                .filter(BlueChecker::isNotBlank)
                .map(AccessProcessor::jsonToAccess)
                .switchIfEmpty(defer(() ->
                        justOrEmpty(jwt)
                                .filter(BlueChecker::isNotBlank)
                                .flatMap(authService::parseAccess)
                                .switchIfEmpty(defer(() -> just(VISITOR_ACCESS)))
                )).onErrorResume(t -> {
                    LOGGER.error("ACCESS_INFO_REMOTE_GETTER failed, t = {}", t);
                    return just(SpecialAccess.VISITOR.access);
                }).flatMap(access -> {
                    riskEvent.setMemberId(access.getId());
                    riskEvent.setRoleIds(ofNullable(access.getRoleIds())
                            .filter(BlueChecker::isNotEmpty).map(l -> l.toArray(Long[]::new)).orElse(EMPTY_ROLES));
                    riskEvent.setCredentialType(access.getCredentialType());
                    riskEvent.setDeviceType(access.getDeviceType());
                    riskEvent.setLoginTime(access.getLoginTime());

                    return just(riskEvent);
                });
    };

    /**
     * risk event -> records
     */
    private final BiFunction<RiskEvent, List<RiskHit>, Mono<List<RiskHitRecord>>> DATA_EVENT_2_RECORD_EVENTS = (event, hits) -> {
        if (isNull(event) || isEmpty(hits))
            return just(emptyList());

        String dataEventType = ofNullable(event.getDataEventType())
                .filter(BlueChecker::isNotBlank).orElse(EMPTY_VALUE.value).intern();
        String dataEventOpType = ofNullable(event.getDataEventOpType())
                .filter(BlueChecker::isNotBlank).orElse(EMPTY_VALUE.value).intern();

        long stamp = ofNullable(event.getStamp()).orElse(TIME_STAMP_GETTER.get());
        String createDate = DATE_STR_FUNC.apply(stamp);

        String method = ofNullable(event.getMethod()).orElse(EMPTY_VALUE.value).intern();
        String uri = ofNullable(event.getUri()).orElse(EMPTY_VALUE.value);
        String realUri = ofNullable(event.getRealUri()).orElse(EMPTY_VALUE.value);
        String requestBody = ofNullable(event.getRequestBody()).orElse(EMPTY_VALUE.value);
        String requestExtra = ofNullable(event.getRequestExtra()).orElse(EMPTY_VALUE.value);

        int responseStatus = ofNullable(event.getResponseStatus()).orElse(OK.status);

        String responseBody = ofNullable(event.getResponseBody()).map(body -> NESTING_PREDICATE.test(uri, responseStatus) ? nestingRes : body).orElse(EMPTY_VALUE.value);
        String responseExtra = ofNullable(event.getResponseExtra()).orElse(EMPTY_VALUE.value);
        String requestId = ofNullable(event.getRequestId()).orElse(EMPTY_VALUE.value);
        String metadata = ofNullable(event.getMetadata()).orElse(EMPTY_VALUE.value);

        String jwt = ofNullable(event.getJwt()).orElse(EMPTY_VALUE.value);
        Long memberId = event.getMemberId();
        Long[] roleIds = event.getRoleIds();
        String credentialType = event.getCredentialType();
        String deviceType = event.getDeviceType();
        Long loginTime = event.getLoginTime();

        String clientIp = ofNullable(event.getClientIp()).orElse(EMPTY_VALUE.value);
        String userAgent = ofNullable(event.getUserAgent()).orElse(EMPTY_VALUE.value);
        String secKey = ofNullable(event.getSecKey()).orElse(EMPTY_VALUE.value);
        Integer requestUnDecryption = ofNullable(event.getRequestUnDecryption()).orElseGet(() -> getBoolByBool(TRUE.bool).status);
        Integer responseUnEncryption = ofNullable(event.getResponseUnEncryption()).orElseGet(() -> getBoolByBool(TRUE.bool).status);
        Integer existenceRequestBody = ofNullable(event.getExistenceRequestBody()).orElseGet(() -> getBoolByBool(TRUE.bool).status);
        Integer existenceResponseBody = ofNullable(event.getExistenceResponseBody()).orElseGet(() -> getBoolByBool(TRUE.bool).status);
        Integer durationSeconds = ofNullable(event.getDurationSeconds()).orElse(0);

        return zip(resourceService.getResourceInfoByResourceKey(REQ_RES_KEY_GENERATOR.apply(method, uri))
                        .switchIfEmpty(defer(() -> just(NON_RESOURCE))),
                fromIterable(hits)
                        .map(hit -> {
                            RiskHitRecord riskHitRecord = new RiskHitRecord();

                            riskHitRecord.setDataEventType(dataEventType);
                            riskHitRecord.setDataEventOpType(dataEventOpType);

                            riskHitRecord.setStamp(stamp);
                            riskHitRecord.setCreateDate(createDate);

                            riskHitRecord.setMethod(method);
                            riskHitRecord.setUri(uri);

                            riskHitRecord.setRealUri(realUri);
                            riskHitRecord.setRequestBody(requestBody);
                            riskHitRecord.setRequestExtra(requestExtra);

                            riskHitRecord.setResponseStatus(responseStatus);

                            riskHitRecord.setResponseBody(responseBody);
                            riskHitRecord.setResponseExtra(responseExtra);
                            riskHitRecord.setRequestId(requestId);
                            riskHitRecord.setMetadata(metadata);

                            riskHitRecord.setJwt(jwt);
                            riskHitRecord.setMemberId(memberId);
                            riskHitRecord.setRoleIds(roleIds);
                            riskHitRecord.setCredentialType(credentialType);
                            riskHitRecord.setDeviceType(deviceType);
                            riskHitRecord.setLoginTime(loginTime);

                            riskHitRecord.setClientIp(clientIp);
                            riskHitRecord.setUserAgent(userAgent);
                            riskHitRecord.setSecKey(secKey);
                            riskHitRecord.setRequestUnDecryption(requestUnDecryption);
                            riskHitRecord.setResponseUnEncryption(responseUnEncryption);
                            riskHitRecord.setExistenceRequestBody(existenceRequestBody);
                            riskHitRecord.setExistenceResponseBody(existenceResponseBody);
                            riskHitRecord.setDurationSeconds(durationSeconds);

                            riskHitRecord.setHitType(hit.getHitType());
                            riskHitRecord.setIllegalExpiresSecond(hit.getIllegalExpiresSecond());
                            riskHitRecord.setRemoveSession(getBoolByBool(hit.getRemoveSession()).status);
                            riskHitRecord.setInvalidStatus(getBoolByBool(hit.getInvalidStatus()).status);

                            long id = blueIdentityProcessor.generate(RiskHitRecord.class);
                            riskHitRecord.setId(id);
                            riskHitRecord.setCursor(id);

                            return riskHitRecord;
                        }).collectList()
        ).map(tuple2 -> {
            ResourceInfo resourceInfo = tuple2.getT1();
            List<RiskHitRecord> riskHitRecords = tuple2.getT2();

            Long resourceId = resourceInfo.getId();
            String module = resourceInfo.getModule().intern();
            String relativeUri = resourceInfo.getRelativeUri().intern();
            String absoluteUri = resourceInfo.getAbsoluteUri().intern();
            String relationView = resourceInfo.getRelationView().intern();
            int authenticate = getBoolByBool(resourceInfo.getAuthenticate()).status;
            Integer type = resourceInfo.getType();
            String name = resourceInfo.getName().intern();

            for (RiskHitRecord riskHitRecord : riskHitRecords) {
                riskHitRecord.setResourceId(resourceId);
                riskHitRecord.setModule(module);
                riskHitRecord.setRelativeUri(relativeUri);
                riskHitRecord.setAbsoluteUri(absoluteUri);
                riskHitRecord.setRelationView(relationView);
                riskHitRecord.setAuthenticate(authenticate);
                riskHitRecord.setType(type);
                riskHitRecord.setName(name);
            }

            return riskHitRecords;
        });
    };

    private final BiFunction<RiskEvent, RiskAsserted, Mono<Boolean>> EVENTS_INSERTER = (riskEvent, riskAsserted) -> {
        if (isNull(riskEvent) || isNull(riskAsserted))
            return just(false);

        List<RiskHit> hits = riskAsserted.getHits();

        if (!riskAsserted.getHit() || isEmpty(hits))
            return just(false);

        return DATA_EVENT_2_RECORD_EVENTS.apply(riskEvent, hits)
                .flatMap(riskHitRecordService::insertRiskHitRecords)
                .onErrorResume(t -> {
                    LOGGER.error("riskEvent = {}, hits = {}, t = {}", riskEvent, hits, t);
                    return just(false);
                });
    };

    @Override
    public Mono<RiskAsserted> handleRiskEvent(RiskEvent riskEvent) {
        LOGGER.info("riskEvent = {}", riskEvent);
        if (isNull(riskEvent))
            throw new BlueException(EMPTY_PARAM);

        return riskProcessor.handle(riskEvent)
                .flatMap(ra -> EVENTS_INSERTER.apply(riskEvent, ra)
                        .doOnSuccess(b -> LOGGER.warn("b = {}", b))
                        .doOnError(t -> LOGGER.error("t = {}", t))
                        .then(just(ra))
                );
    }

    @Override
    public Mono<RiskAsserted> handleDataEvent(DataEvent dataEvent) {
        LOGGER.info("dataEvent = {}", dataEvent);
        if (isNull(dataEvent))
            throw new BlueException(EMPTY_PARAM);

        return just(dataEvent).flatMap(DATA_EVENT_2_RISK_EVENT).flatMap(this::handleRiskEvent);
    }

    @Override
    public Mono<RiskAsserted> validateRiskEvent(RiskEvent riskEvent) {
        LOGGER.info("riskEvent = {}", riskEvent);
        if (isNull(riskEvent))
            throw new BlueException(EMPTY_PARAM);

        return riskProcessor.validate(riskEvent)
                .flatMap(ra -> EVENTS_INSERTER.apply(riskEvent, ra)
                        .doOnSuccess(b -> LOGGER.warn("b = {}", b))
                        .doOnError(t -> LOGGER.error("t = {}", t))
                        .then(just(ra)));
    }

    @Override
    public Mono<RiskAsserted> validateDataEvent(DataEvent dataEvent) {
        LOGGER.info("dataEvent = {}", dataEvent);
        if (isNull(dataEvent))
            throw new BlueException(EMPTY_PARAM);

        return just(dataEvent).flatMap(DATA_EVENT_2_RISK_EVENT).flatMap(this::validateRiskEvent);
    }

}