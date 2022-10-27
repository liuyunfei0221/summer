package com.blue.agreement.handler.api;

import com.blue.agreement.service.inter.AgreementService;
import com.blue.basic.model.common.BlueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.base.PathVariableGetter.getIntegerVariable;
import static com.blue.basic.constant.common.PathVariable.TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * agreement api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public class AgreementApiHandler {

    private final AgreementService agreementService;

    public AgreementApiHandler(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    /**
     * get agreement
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        return agreementService.getNewestAgreementInfoMonoByTypeWithCache(getIntegerVariable(serverRequest, TYPE.key))
                .flatMap(ai -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(success(ai, serverRequest), BlueResponse.class)
                );
    }

}
