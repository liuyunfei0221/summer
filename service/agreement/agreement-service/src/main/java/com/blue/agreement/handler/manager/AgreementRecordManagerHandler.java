package com.blue.agreement.handler.manager;

import com.blue.agreement.service.inter.AgreementRecordService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.agreement.constant.AgreementTypeReference.PAGE_MODEL_FOR_AGREEMENT_RECORD_CONDITION_TYPE;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

/**
 * agreement record manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AgreementRecordManagerHandler {

    private final AgreementRecordService agreementRecordService;

    public AgreementRecordManagerHandler(AgreementRecordService agreementRecordService) {
        this.agreementRecordService = agreementRecordService;
    }

    /**
     * select agreement record by page and condition
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> page(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_AGREEMENT_RECORD_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(agreementRecordService::selectAgreementRecordManagerInfoPageMonoByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr, serverRequest), BlueResponse.class));
    }

}
