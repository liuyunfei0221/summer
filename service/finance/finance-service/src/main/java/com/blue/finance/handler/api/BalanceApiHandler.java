package com.blue.finance.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.finance.service.inter.FinanceAccountService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.common.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * balance api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class BalanceApiHandler {

    private final FinanceAccountService financeAccountService;

    public BalanceApiHandler(FinanceAccountService financeAccountService) {
        this.financeAccountService = financeAccountService;
    }

    /**
     * query member's balance
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        financeAccountService.getBalanceByMemberId(acc.getId())
                                .flatMap(fv ->
                                        ok()
                                                .contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, fv, serverRequest), BlueResponse.class))
                );
    }


}
