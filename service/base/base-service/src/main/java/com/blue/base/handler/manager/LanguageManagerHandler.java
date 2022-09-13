package com.blue.base.handler.manager;

import com.blue.basic.model.common.BlueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.message.InternationalProcessor.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

/**
 * language manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public final class LanguageManagerHandler {

    /**
     * select language
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectLanguage(ServerRequest serverRequest) {
        return just(supportLanguages())
                .flatMap(ls ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ls, serverRequest), BlueResponse.class)
                );
    }

    /**
     * get default language
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getDefaultLanguage(ServerRequest serverRequest) {
        return just(defaultLanguage())
                .flatMap(ls ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ls, serverRequest), BlueResponse.class)
                );
    }

    /**
     * select messages
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectMessage(ServerRequest serverRequest) {
        return just(listMessage(serverRequest))
                .flatMap(ls ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ls, serverRequest), BlueResponse.class)
                );
    }

    /**
     * select elements
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectElement(ServerRequest serverRequest) {
        return just(selectAllElement(serverRequest))
                .flatMap(ls ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ls, serverRequest), BlueResponse.class)
                );
    }

}
