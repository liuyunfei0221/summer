package com.blue.base.handler.api;

import com.blue.base.service.inter.DictService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.common.StringDataParam;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

/**
 * dict api handler
 *
 * @author liuyunfei
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
        return dictService.selectDictTypeInfo()
                .flatMap(dts ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(success(dts, serverRequest), BlueResponse.class)
                );
    }

    /**
     * select dict by code
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectDict(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(StringDataParam.class)
                .map(StringDataParam::getData)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(dictService::selectDictInfoByTypeCode)
                .flatMap(ts ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(success(ts, serverRequest), BlueResponse.class)
                );
    }

}
