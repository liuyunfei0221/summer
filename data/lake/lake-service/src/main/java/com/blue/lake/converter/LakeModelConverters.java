package com.blue.lake.converter;

import com.blue.basic.common.access.AccessProcessor;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.lake.config.deploy.NestingResponseDeploy;
import com.blue.lake.repository.entity.OptEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.getBoolByBool;
import static com.blue.basic.constant.common.BlueBoolean.TRUE;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.OK;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.basic.constant.common.SummerAttr.DATE_FORMATTER;
import static java.time.Instant.ofEpochSecond;
import static java.time.LocalDate.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.util.Optional.ofNullable;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * model converters in lake project
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
@Component
public final class LakeModelConverters implements ApplicationListener<ContextRefreshedEvent> {

    private static final Function<Long, String> DATE_STR_FUNC = stamp ->
            ofInstant(ofEpochSecond(stamp), systemDefault()).format(DATE_FORMATTER);

    private static Set<String> NESTING_REAL_URIS;

    private static String NESTING_RES;

    private static final BinaryOperator<String> NESTING_RESPONSE_BODY_HANDLER = (realUri, body) ->
            NESTING_REAL_URIS.contains(realUri) ? NESTING_RES : body;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        NestingResponseDeploy nestingResponseDeploy = applicationContext.getBean(NestingResponseDeploy.class);

        NESTING_REAL_URIS = ofNullable(nestingResponseDeploy.getUris()).filter(uris -> !isEmpty(uris))
                .map(HashSet::new)
                .orElse(new HashSet<>(0));

        NESTING_RES = ofNullable(nestingResponseDeploy.getResponse()).filter(StringUtils::isNotEmpty)
                .orElse(EMPTY_DATA.value);
    }

    /**
     * data event -> option event
     */
    public static final Function<DataEvent, OptEvent> DATA_EVENT_2_OPT_EVENT = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);

        OptEvent optEvent = new OptEvent();

        optEvent.setDataEventType(ofNullable(param.getDataEventType())
                .filter(BlueChecker::isNotBlank).orElse(EMPTY_DATA.value));
        optEvent.setDataEventOpType(ofNullable(param.getDataEventOpType())
                .filter(BlueChecker::isNotBlank).orElse(EMPTY_DATA.value));

        long stamp = ofNullable(param.getStamp()).orElse(TIME_STAMP_GETTER.get());
        optEvent.setStamp(stamp);
        optEvent.setCreateDate(DATE_STR_FUNC.apply(stamp));

        Map<String, String> entries = param.getEntries();

        optEvent.setMethod(ofNullable(entries.get(METHOD.key)).orElse(EMPTY_DATA.value));
        optEvent.setUri(ofNullable(entries.get(URI.key)).orElse(EMPTY_DATA.value));

        String realUri = ofNullable(entries.get(REAL_URI.key)).orElse(EMPTY_DATA.value);
        optEvent.setRealUri(realUri);
        optEvent.setRequestBody(ofNullable(entries.get(REQUEST_BODY.key)).orElse(EMPTY_DATA.value));
        optEvent.setRequestExtra(ofNullable(entries.get(REQUEST_EXTRA.key)).orElse(EMPTY_DATA.value));
        optEvent.setResponseStatus(ofNullable(entries.get(RESPONSE_STATUS.key)).map(Integer::parseInt).orElse(OK.status));
        optEvent.setResponseBody(ofNullable(entries.get(RESPONSE_BODY.key)).map(body -> NESTING_RESPONSE_BODY_HANDLER.apply(realUri, body)).orElse(EMPTY_DATA.value));
        optEvent.setResponseExtra(ofNullable(entries.get(RESPONSE_EXTRA.key)).orElse(EMPTY_DATA.value));
        optEvent.setRequestId(ofNullable(entries.get(REQUEST_ID.key)).orElse(EMPTY_DATA.value));
        optEvent.setMetadata(ofNullable(entries.get(METADATA.key)).orElse(EMPTY_DATA.value));
        optEvent.setJwt(ofNullable(entries.get(JWT.key)).orElse(EMPTY_DATA.value));

        ofNullable(entries.get(ACCESS.key)).filter(BlueChecker::isNotBlank)
                .map(AccessProcessor::jsonToAccess)
                .ifPresent(access -> {
                    optEvent.setMemberId(access.getId());
                    optEvent.setRoleIds(ofNullable(access.getRoleIds())
                            .filter(BlueChecker::isNotEmpty).map(l -> l.toArray(Long[]::new)).orElseGet(() -> new Long[0]));
                    optEvent.setCredentialType(access.getCredentialType());
                    optEvent.setDeviceType(access.getDeviceType());
                    optEvent.setLoginTime(access.getLoginTime());
                });

        optEvent.setClientIp(ofNullable(entries.get(CLIENT_IP.key)).orElse(EMPTY_DATA.value));
        optEvent.setUserAgent(ofNullable(entries.get(USER_AGENT.key)).orElse(EMPTY_DATA.value));
        optEvent.setSecKey(ofNullable(entries.get(SEC_KEY.key)).orElse(EMPTY_DATA.value));
        optEvent.setRequestUnDecryption(getBoolByBool(ofNullable(entries.get(REQUEST_UN_DECRYPTION.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        optEvent.setResponseUnEncryption(getBoolByBool(ofNullable(entries.get(RESPONSE_UN_ENCRYPTION.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        optEvent.setExistenceRequestBody(getBoolByBool(ofNullable(entries.get(EXISTENCE_REQUEST_BODY.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        optEvent.setExistenceResponseBody(getBoolByBool(ofNullable(entries.get(EXISTENCE_RESPONSE_BODY.key)).map(Boolean::parseBoolean).orElse(TRUE.bool)).status);
        optEvent.setDurationSeconds(ofNullable(entries.get(DURATION_SECONDS.key)).map(Integer::parseInt).orElse(0));

        return optEvent;
    };

}
