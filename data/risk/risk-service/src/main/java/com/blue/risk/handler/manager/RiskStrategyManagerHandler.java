package com.blue.risk.handler.manager;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.risk.model.RiskStrategyInsertParam;
import com.blue.risk.model.RiskStrategyUpdateParam;
import com.blue.risk.service.inter.RiskStrategyService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.access.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.risk.constant.RiskTypeReference.PAGE_MODEL_FOR_RISK_STRATEGY_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * risk strategy manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class RiskStrategyManagerHandler {

    private final RiskStrategyService riskStrategyService;

    public RiskStrategyManagerHandler(RiskStrategyService riskStrategyService) {
        this.riskStrategyService = riskStrategyService;
    }

    /**
     * create a new risk strategy
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(RiskStrategyInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> riskStrategyService.insertRiskStrategy(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(rsi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(rsi, serverRequest), BlueResponse.class));
    }

    /**
     * update risk strategy
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(RiskStrategyUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> riskStrategyService.updateRiskStrategy(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(rsi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(rsi, serverRequest), BlueResponse.class));
    }

    /**
     * delete risk strategy
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(riskStrategyService::deleteRiskStrategy)
                .flatMap(rsi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(rsi, serverRequest), BlueResponse.class));
    }

    /**
     * select risk strategy by page and condition
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> page(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_RISK_STRATEGY_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(riskStrategyService::selectRiskStrategyManagerInfoPageByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr, serverRequest), BlueResponse.class));
    }

}