package com.blue.finance.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.finance.service.inter.FinanceAccountService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * balance api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class FinanceAccountApiHandler {

    private final FinanceAccountService financeAccountService;

    public FinanceAccountApiHandler(FinanceAccountService financeAccountService) {
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
                        financeAccountService.getFinanceAccountInfoByMemberId(acc.getId())
                                .flatMap(fai ->
                                        ok()
                                                .contentType(APPLICATION_JSON)
                                                .body(success(fai, serverRequest), BlueResponse.class))
                );
    }


}
