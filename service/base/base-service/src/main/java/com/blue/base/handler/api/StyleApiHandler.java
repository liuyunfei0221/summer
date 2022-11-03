package com.blue.base.handler.api;

import com.blue.base.service.inter.StyleService;
import com.blue.basic.model.common.BlueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.PathVariableGetter.getIntegerVariable;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.PathVariable.TYPE;
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
        return styleService.getActiveStyleInfoByTypeWithCache(getIntegerVariable(serverRequest, TYPE.key))
                .flatMap(sti -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(success(sti, serverRequest), BlueResponse.class)
                );
    }

}
