package com.blue.analyze.handler.api;

import com.blue.base.model.base.BlueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * analyze api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AnalyzeApiHandler {

    /**
     * test analyze
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getData(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(ai ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, "test", serverRequest), BlueResponse.class)
                );
    }


}
