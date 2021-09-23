package com.blue.finance.handler.api;

import com.blue.base.model.base.Access;
import com.blue.base.model.base.BlueResult;
import com.blue.base.model.exps.BlueException;
import com.blue.finance.api.model.WithdrawInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccess;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.base.constant.base.ResponseMessage.EMPTY_REQUEST_BODY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;

/**
 * 用户接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public final class WithdrawApiHandler {

    /**
     * 测试数据加密
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> withdraw(ServerRequest serverRequest) {
        Access access = getAccess(serverRequest);

        return serverRequest.bodyToMono(WithdrawInfo.class)
                .switchIfEmpty(
                        error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_REQUEST_BODY.message)))
                .flatMap(withdrawInfo -> {

                    System.err.println(access);
                    System.err.println();
                    System.err.println(withdrawInfo);

                    return Mono.just("OK");
                }).flatMap(
                        rs ->
                                ok().contentType(APPLICATION_JSON)
                                        .body(generate(OK.code, rs, OK.message), BlueResult.class));

    }


}
