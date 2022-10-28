package com.blue.agreement.handler.api;

import com.blue.agreement.model.AgreementRecordInsertParam;
import com.blue.agreement.service.inter.AgreementRecordService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * agreement record api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public class AgreementRecordApiHandler {

    private final AgreementRecordService agreementRecordService;

    public AgreementRecordApiHandler(AgreementRecordService agreementRecordService) {
        this.agreementRecordService = agreementRecordService;
    }

    /**
     * select unsigned agreements
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectUnsigned(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        agreementRecordService.selectNewestAgreementInfosUnsigned(acc.getId())
                                .flatMap(ais ->
                                        ok().contentType(APPLICATION_JSON)
                                                .body(success(ais, serverRequest), BlueResponse.class))
                );
    }

    /**
     * sign agreement
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> sign(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(AgreementRecordInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        agreementRecordService.insertAgreementRecord(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ai, serverRequest), BlueResponse.class));
    }

}
