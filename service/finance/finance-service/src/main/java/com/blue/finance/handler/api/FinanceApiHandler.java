package com.blue.finance.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.finance.service.inter.FinanceService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * 用户接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public final class FinanceApiHandler {

    private final FinanceService financeService;

    public FinanceApiHandler(FinanceService financeService) {
        this.financeService = financeService;
    }

    /**
     * 根据商户session信息获取商户余额
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getBalance(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(ai ->
                        financeService.getBalanceByMemberId(ai.getId())
                                .flatMap(fv ->
                                        ok()
                                                .contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, fv, OK.message), BlueResponse.class))
                );
    }


}
