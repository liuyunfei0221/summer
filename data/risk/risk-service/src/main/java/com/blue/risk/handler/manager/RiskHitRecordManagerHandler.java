package com.blue.risk.handler.manager;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.risk.service.inter.RiskHitRecordService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.risk.constant.RiskTypeReference.SCROLL_MODEL_FOR_RISK_HIT_RECORD_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

/**
 * risk hit record manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Component
public class RiskHitRecordManagerHandler {

    private final RiskHitRecordService riskHitRecordService;

    public RiskHitRecordManagerHandler(RiskHitRecordService riskHitRecordService) {
        this.riskHitRecordService = riskHitRecordService;
    }

    /**
     * select records
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> scroll(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SCROLL_MODEL_FOR_RISK_HIT_RECORD_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(riskHitRecordService::selectRiskHitRecordScrollByScrollAndCursor)
                .flatMap(smr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(smr, serverRequest), BlueResponse.class));
    }

}