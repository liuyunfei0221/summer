package com.blue.base.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.common.ElememtKeysParam;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.message.ElementProcessor.selectAllElement;
import static com.blue.basic.common.message.ElementProcessor.selectElement;
import static com.blue.basic.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * element api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public final class ElementApiHandler {

    /**
     * select all element kv
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return just(serverRequest)
                .flatMap(request ->
                        just(selectAllElement(request)))
                .flatMap(element ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, element, serverRequest), BlueResponse.class)
                );
    }

    /**
     * select element kv by keys
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectByKeys(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ElememtKeysParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(ep -> {
                    ep.asserts();
                    return just(selectElement(serverRequest, ep.getKeys()));
                })
                .flatMap(element ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, element, serverRequest), BlueResponse.class)
                );
    }

}
