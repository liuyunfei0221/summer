package com.blue.base.handler.api;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * element api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public final class ElementApiHandler {


    /**
     * select language
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {

        return null;

//        return just(LANGUAGES)
//                .flatMap(ls ->
//                        ok().contentType(APPLICATION_JSON)
//                                .body(generate(OK.code, ls, serverRequest), BlueResponse.class)
//                );
    }

    /**
     * get default language
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getDefault(ServerRequest serverRequest) {

        return null;

//        return just(DEFAULT_LANGUAGE)
//                .flatMap(ls ->
//                        ok().contentType(APPLICATION_JSON)
//                                .body(generate(OK.code, ls, serverRequest), BlueResponse.class)
//                );
    }

}
