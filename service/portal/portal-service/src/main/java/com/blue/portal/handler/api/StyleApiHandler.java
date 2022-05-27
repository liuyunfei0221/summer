package com.blue.portal.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.portal.service.inter.StyleService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.PathVariableGetter.getIntegerVariable;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.PathVariable.TYPE;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * style api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public final class StyleApiHandler {

    private final StyleService styleService;

    public StyleApiHandler(StyleService styleService) {
        this.styleService = styleService;
    }

    /**
     * get active style
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        return styleService.getActiveStyleInfoMonoByTypeWithCache(getIntegerVariable(serverRequest, TYPE.key))
                .flatMap(sti -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(generate(OK.code, sti, serverRequest), BlueResponse.class)
                );
    }

}