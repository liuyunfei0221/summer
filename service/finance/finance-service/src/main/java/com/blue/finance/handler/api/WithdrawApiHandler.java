package com.blue.finance.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.finance.api.model.WithdrawInfo;
import com.blue.finance.service.inter.WithdrawService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.*;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.common.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * withdraw api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class WithdrawApiHandler {

    private final WithdrawService withdrawService;

    public WithdrawApiHandler(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    /**
     * withdraw
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> withdraw(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(WithdrawInfo.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        just(withdrawService.withdraw(tuple2.getT1(), tuple2.getT2())))
                .flatMap(
                        rs ->
                                ok().contentType(APPLICATION_JSON)
                                        .body(generate(OK.code, rs, serverRequest), BlueResponse.class));
    }

}
