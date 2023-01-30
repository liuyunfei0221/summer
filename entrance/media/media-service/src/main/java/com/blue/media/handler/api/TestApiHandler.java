package com.blue.media.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.media.api.model.WithdrawInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * test api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Component
public final class TestApiHandler {

    /**
     * test encrypt in media project
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> withdraw(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(WithdrawInfo.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> {
                    System.err.println(tuple2.getT2());
                    System.err.println();
                    System.err.println(tuple2.getT1());

                    return just("OK");
                }).flatMap(
                        rs ->
                                ok().contentType(APPLICATION_JSON)
                                        .body(success(rs, serverRequest), BlueResponse.class));
    }

    /**
     * test redirect in media project
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> trd(ServerRequest serverRequest) {
        return ServerResponse
                .temporaryRedirect(URI.create("https://www.baidu.com/"))
                .build();
    }

    /**
     * test redirect in media project
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> rd(ServerRequest serverRequest) {
        return ServerResponse
                .permanentRedirect(URI.create("https://www.baidu.com/"))
                .build();
    }

    /**
     * test forward in media project
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> fd(ServerRequest serverRequest) {
        return ServerResponse
                .seeOther(URI.create("localhost:11000/blue-member/basic"))
                .contentType(APPLICATION_JSON)
                .build();
    }

}