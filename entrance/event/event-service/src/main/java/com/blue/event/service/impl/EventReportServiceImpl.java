package com.blue.event.service.impl;

import com.blue.auth.api.model.MemberPayload;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.Access;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.event.component.event.RequestEventReporter;
import com.blue.event.service.inter.EventReportService;
import com.blue.jwt.common.JwtProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static com.blue.basic.common.access.AccessProcessor.accessToJson;
import static com.blue.basic.common.base.BlueChecker.isNotBlank;
import static com.blue.basic.common.base.CommonFunctions.EVENT_ATTR_PACKAGER;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.BlueCommonThreshold.UNKNOWN_LOGGED_IN_ROLE_ID;
import static com.blue.basic.constant.common.BlueDataAttrKey.ACCESS;
import static com.blue.basic.constant.common.BlueDataAttrKey.JWT;
import static com.blue.basic.constant.common.BlueHeader.AUTHORIZATION;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SpecialAccess.VISITOR;
import static com.blue.basic.constant.event.DataEventAttrKey.DATA_EVENT_OP_TYPE;
import static com.blue.basic.constant.event.DataEventAttrKey.DATA_EVENT_TYPE;
import static com.blue.event.constant.EventTypeReference.EVENT_MODEL_FOR_RESOURCE_CONDITION_TYPE;
import static java.lang.Long.parseLong;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * event report service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class EventReportServiceImpl implements EventReportService {

    private static final Logger LOGGER = getLogger(EventReportServiceImpl.class);

    private final JwtProcessor<MemberPayload> jwtProcessor;

    private final RequestEventReporter requestEventReporter;

    public EventReportServiceImpl(JwtProcessor<MemberPayload> jwtProcessor, RequestEventReporter requestEventReporter) {
        this.jwtProcessor = jwtProcessor;
        this.requestEventReporter = requestEventReporter;
    }

    /**
     * report event
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<Boolean> insert(ServerRequest serverRequest) {
        return just(serverRequest)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(request -> {
                    DataEvent dataEvent = new DataEvent();

                    dataEvent.setStamp(TIME_STAMP_GETTER.get());

                    String jwt = serverRequest.headers().firstHeader(AUTHORIZATION.name);
                    if (isNotBlank(jwt)) {
                        dataEvent.addData(JWT.key, jwt);
                        try {
                            MemberPayload memberPayload = jwtProcessor.parse(jwt);
                            dataEvent.addData(ACCESS.key, accessToJson(new Access(parseLong(memberPayload.getId()), singletonList(UNKNOWN_LOGGED_IN_ROLE_ID.value), memberPayload.getCredentialType().intern(),
                                    memberPayload.getDeviceType().intern(), parseLong(memberPayload.getLoginTime()))));
                        } catch (Exception e) {
                            LOGGER.error("parse jwt failed, jwt = {}", jwt);
                        }
                    } else {
                        dataEvent.addData(ACCESS.key, accessToJson(VISITOR.access));
                    }

                    return request.bodyToMono(EVENT_MODEL_FOR_RESOURCE_CONDITION_TYPE)
                            .flatMap(eventData -> {
                                LOGGER.info("eventData = {}", eventData);

                                ofNullable(eventData.get(DATA_EVENT_TYPE.key))
                                        .map(String::valueOf)
                                        .filter(BlueChecker::isNotBlank)
                                        .ifPresent(dataEvent::setDataEventType);
                                ofNullable(eventData.get(DATA_EVENT_OP_TYPE.key))
                                        .map(String::valueOf)
                                        .filter(BlueChecker::isNotBlank)
                                        .ifPresent(dataEvent::setDataEventOpType);

                                EVENT_ATTR_PACKAGER.accept(eventData, dataEvent);
                                return just(dataEvent);
                            }).flatMap(de -> {
                                requestEventReporter.report(de);
                                return just(true);
                            });
                });
    }

}
