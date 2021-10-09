package com.blue.base.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.service.inter.DictService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * dict api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public final class DictApiHandler {

    private final DictService dictService;

    public DictApiHandler(DictService dictService) {
        this.dictService = dictService;
    }

    /**
     * select dict type
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectDictType(ServerRequest serverRequest) {
        return dictService.selectDictType()
                .flatMap(dts ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, dts, OK.message), BlueResponse.class)
                );
    }


}
