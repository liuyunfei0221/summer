package com.blue.base.handler.api;

import com.blue.basic.model.common.BlueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.message.MessageProcessor.defaultLanguage;
import static com.blue.basic.common.message.MessageProcessor.supportLanguages;
import static com.blue.basic.common.reactive.ReactiveCommonFunctions.success;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

/**
 * language api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public final class LanguageApiHandler {

    /**
     * select language
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return just(supportLanguages())
                .flatMap(ls ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ls), BlueResponse.class)
                );
    }

    /**
     * get default language
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getDefault(ServerRequest serverRequest) {
        return just(defaultLanguage())
                .flatMap(ls ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ls), BlueResponse.class)
                );
    }

}
