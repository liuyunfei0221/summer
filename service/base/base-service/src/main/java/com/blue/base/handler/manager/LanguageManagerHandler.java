package com.blue.base.handler.manager;

import com.blue.base.common.message.ElementProcessor;
import com.blue.base.common.message.MessageProcessor;
import com.blue.base.model.common.BlueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.common.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * language api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public final class LanguageManagerHandler {

    /**
     * refresh support languages and elements
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> refresh(ServerRequest serverRequest) {

        MessageProcessor.refresh();
        ElementProcessor.refresh();

        return ok().contentType(APPLICATION_JSON)
                .body(generate(OK.code, OK.message, serverRequest), BlueResponse.class);
    }

}
