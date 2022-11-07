package com.blue.risk.service.impl;

import com.blue.basic.common.access.AccessProcessor;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.Access;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.risk.api.model.RiskAsserted;
import com.blue.risk.api.model.RiskHit;
import com.blue.risk.component.risk.RiskProcessor;
import com.blue.risk.config.deploy.NestingResponseDeploy;
import com.blue.risk.remote.consumer.RpcAuthServiceConsumer;
import com.blue.risk.repository.entity.RiskHitRecord;
import com.blue.risk.service.inter.RiskHitRecordService;
import com.blue.risk.service.inter.RiskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.getBoolByBool;
import static com.blue.basic.constant.common.BlueBoolean.TRUE;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.basic.constant.common.ResponseElement.OK;
import static com.blue.basic.constant.common.SpecialAccess.VISITOR;
import static com.blue.basic.constant.common.SpecialIntegerElement.ZERO;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.SummerAttr.DATE_FORMATTER;
import static java.time.Instant.ofEpochSecond;
import static java.time.LocalDate.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * risk analyse service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Service
public class RiskServiceImpl implements RiskService {

    private static final Logger LOGGER = getLogger(RiskServiceImpl.class);

    private RpcAuthServiceConsumer rpcAuthServiceConsumer;

    private BlueIdentityProcessor blueIdentityProcessor;

    private final RiskProcessor riskProcessor;

    private RiskHitRecordService riskHitRecordService;

    public RiskServiceImpl(RpcAuthServiceConsumer rpcAuthServiceConsumer, BlueIdentityProcessor blueIdentityProcessor, RiskProcessor riskProcessor,
                           RiskHitRecordService riskHitRecordService, NestingResponseDeploy nestingResponseDeploy) {
        this.rpcAuthServiceConsumer = rpcAuthServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.riskHitRecordService = riskHitRecordService;
        this.riskProcessor = riskProcessor;

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
     * data event -> records
     */
    private final BiFunction<DataEvent, List<RiskHit>, List<RiskHitRecord>> DATA_EVENT_2_RECORD_EVENTS = (event, hits) -> {
        if (isNull(event) || isEmpty(hits))
            throw new BlueException(INVALID_PARAM);

        String dataEventType = ofNullable(event.getDataEventType())
                .filter(BlueChecker::isNotBlank).orElse(EMPTY_VALUE.value).intern();
        String dataEventOpType = ofNullable(event.getDataEventOpType())
                .filter(BlueChecker::isNotBlank).orElse(EMPTY_VALUE.value).intern();

        long stamp = ofNullable(event.getStamp()).orElse(TIME_STAMP_GETTER.get());
        String createDate = DATE_STR_FUNC.apply(stamp);

        Map<String, String> entries = event.getEntries();

        String method = ofNullable(entries.get(METHOD.key)).orElse(EMPTY_VALUE.value).intern();
        String uri = ofNullable(entries.get(URI.key)).orElse(EMPTY_VALUE.value);
        String realUri = ofNullable(entries.get(REAL_URI.key)).orElse(EMPTY_VALUE.value);
        String requestBody = ofNullable(entries.get(REQUEST_BODY.key)).orElse(EMPTY_VALUE.value);
        String requestExtra = ofNullable(entries.get(REQUEST_EXTRA.key)).orElse(EMPTY_VALUE.value);

        int responseStatus = ofNullable(entries.get(RESPONSE_STATUS.key)).map(Integer::parseInt).orElse(OK.status);

        String responseBody = ofNullable(entries.get(RESPONSE_BODY.key)).map(body -> NESTING_PREDICATE.test(realUri, responseStatus) ? nestingRes : body).orElse(EMPTY_VALUE.value);
        String responseExtra = ofNullable(entries.get(RESPONSE_EXTRA.key)).orElse(EMPTY_VALUE.value);
        String requestId = ofNullable(entries.get(REQUEST_ID.key)).orElse(EMPTY_VALUE.value);
        String metadata = ofNullable(entries.get(METADATA.key)).orElse(EMPTY_VALUE.value);

        String jwt = ofNullable(entries.get(JWT.key)).orElse(EMPTY_VALUE.value);

        Long memberId = null;
        Long[] roleIds = null;
        String credentialType = null;
        String deviceType = null;
        Long loginTime = null;
        try {
            Access access = ofNullable(entries.get(ACCESS.key))
                    .filter(BlueChecker::isNotBlank)
                    .map(AccessProcessor::jsonToAccess)
                    .orElseGet(() ->
                            ofNullable(jwt)
                                    .filter(BlueChecker::isNotBlank)
                                    .map(a -> rpcAuthServiceConsumer.parseAccess(a).toFuture().join()).orElse(UNKNOWN_ACCESS));

            memberId = access.getId();
            roleIds = ofNullable(access.getRoleIds())
                    .filter(BlueChecker::isNotEmpty).map(l -> l.toArray(Long[]::new)).orElse(EMPTY_ROLES);
            credentialType = access.getCredentialType();
            deviceType = access.getDeviceType();
            loginTime = access.getLoginTime();
        } catch (Exception e) {
            LOGGER.error("entries = {}, e = {}", entries, e);
        }

        String clientIp = ofNullable(entries.get(CLIENT_IP.key)).orElse(EMPTY_VALUE.value);
        String userAgent = ofNullable(entries.get(USER_AGENT.key)).orElse(EMPTY_VALUE.value);
        String secKey = ofNullable(entries.get(SEC_KEY.key)).orElse(EMPTY_VALUE.value);
        Integer requestUnDecryption = getBoolByBool(ofNullable(entries.get(REQUEST_UN_DECRYPTION.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status;
        Integer responseUnEncryption = getBoolByBool(ofNullable(entries.get(RESPONSE_UN_ENCRYPTION.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status;
        Integer existenceRequestBody = getBoolByBool(ofNullable(entries.get(EXISTENCE_REQUEST_BODY.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status;
        Integer existenceResponseBody = getBoolByBool(ofNullable(entries.get(EXISTENCE_RESPONSE_BODY.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status;
        Integer durationSeconds = ofNullable(entries.get(DURATION_SECONDS.key)).map(Integer::parseInt).orElse(ZERO.value);


        List<RiskHitRecord> riskHitRecords = new ArrayList<>(hits.size());
        RiskHitRecord riskHitRecord;

        for (RiskHit hit : hits) {
            riskHitRecord = new RiskHitRecord();

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

            riskHitRecord.setClientIp(ofNullable(entries.get(CLIENT_IP.key)).orElse(EMPTY_VALUE.value));
            riskHitRecord.setUserAgent(ofNullable(entries.get(USER_AGENT.key)).orElse(EMPTY_VALUE.value));
            riskHitRecord.setSecKey(ofNullable(entries.get(SEC_KEY.key)).orElse(EMPTY_VALUE.value));
            riskHitRecord.setRequestUnDecryption(getBoolByBool(ofNullable(entries.get(REQUEST_UN_DECRYPTION.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
            riskHitRecord.setResponseUnEncryption(getBoolByBool(ofNullable(entries.get(RESPONSE_UN_ENCRYPTION.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
            riskHitRecord.setExistenceRequestBody(getBoolByBool(ofNullable(entries.get(EXISTENCE_REQUEST_BODY.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
            riskHitRecord.setExistenceResponseBody(getBoolByBool(ofNullable(entries.get(EXISTENCE_RESPONSE_BODY.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
            riskHitRecord.setDurationSeconds(ofNullable(entries.get(DURATION_SECONDS.key)).map(Integer::parseInt).orElse(ZERO.value));

            riskHitRecord.setResourceKey(hit.getResourceKey());
            riskHitRecord.setHitType(hit.getHitType());
            riskHitRecord.setIllegalExpiresSecond(hit.getIllegalExpiresSecond());

            riskHitRecord.setId(blueIdentityProcessor.generate(RiskHitRecord.class));

            riskHitRecords.add(riskHitRecord);
        }

        return riskHitRecords;
    };

    private final BiFunction<DataEvent, List<RiskHit>, Mono<Boolean>> EVENTS_INSERTER = (dataEvent, hits) -> {
        if (isNull(dataEvent) || isEmpty(hits))
            return just(false);

        try {
            List<RiskHitRecord> riskHitRecords = DATA_EVENT_2_RECORD_EVENTS.apply(dataEvent, hits);
            return riskHitRecordService.insertRiskHitRecords(riskHitRecords);
        } catch (Exception e) {
            LOGGER.error("dataEvent = {}, hits = {}, e = {}", dataEvent, hits, e);

            return just(false);
        }
    };

    /**
     * analyze event
     *
     * @param dataEvent
     * @return
     */
    @Override
    public Mono<RiskAsserted> analyzeEvent(DataEvent dataEvent) {
        LOGGER.info("dataEvent = {}", dataEvent);

        return riskProcessor.analyzeEvent(dataEvent)
                .flatMap(ra ->
                        EVENTS_INSERTER.apply(dataEvent, ra.getHits()).doOnEach(b -> LOGGER.info("b = {}", b))
                                .then(just(ra))
                );
    }

}
