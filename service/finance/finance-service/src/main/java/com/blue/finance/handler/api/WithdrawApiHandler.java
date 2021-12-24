package com.blue.finance.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.finance.api.model.WithdrawInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * test encrypt endpoint
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public final class WithdrawApiHandler {

    /**
     * test withdraw
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> withdraw(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(WithdrawInfo.class)
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> {

                    System.err.println(tuple2.getT2());
                    System.err.println();
                    System.err.println(tuple2.getT1());

                    return just("OK");
                }).flatMap(
                        rs ->
                                ok().contentType(APPLICATION_JSON)
                                        .body(generate(OK.code, rs, OK.message), BlueResponse.class));
    }


}
