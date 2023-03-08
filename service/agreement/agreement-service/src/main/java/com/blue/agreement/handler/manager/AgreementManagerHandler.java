package com.blue.agreement.handler.manager;

import com.blue.agreement.model.AgreementInsertParam;
import com.blue.agreement.service.inter.AgreementService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.agreement.constant.AgreementTypeReference.PAGE_MODEL_FOR_AGREEMENT_CONDITION_TYPE;
import static com.blue.basic.common.access.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * agreement manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AgreementManagerHandler {

    private final AgreementService agreementService;

    public AgreementManagerHandler(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    /**
     * create a new agreement
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(AgreementInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> just(agreementService.insertAgreement(tuple2.getT1(), tuple2.getT2().getId())))
                .flatMap(ai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ai, serverRequest), BlueResponse.class));
    }

    /**
     * select agreement by page and condition
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> page(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_AGREEMENT_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(agreementService::selectAgreementManagerInfoPageByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr, serverRequest), BlueResponse.class));
    }

}
