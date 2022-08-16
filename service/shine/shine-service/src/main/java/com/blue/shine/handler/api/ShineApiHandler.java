package com.blue.shine.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.shine.service.inter.ShineService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.Map;

import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.base.MetadataGetter.getMetadata;
import static com.blue.basic.common.base.RequestIpGetter.getRequestIp;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.shine.constant.ShineTypeReference.PAGE_MODEL_FOR_SHINE_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;
import static reactor.util.Loggers.getLogger;

/**
 * shine api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Component
public final class ShineApiHandler {

    private static final Logger LOGGER = getLogger(ShineApiHandler.class);

    private final ShineService shineService;

    public ShineApiHandler(ShineService shineService) {
        this.shineService = shineService;
    }

    /**
     * select shine info
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> page(ServerRequest serverRequest) {
        String ip = getRequestIp(serverRequest);

        LOGGER.warn("client ip = {}", ip);
        Map<String, String> metadata = getMetadata(serverRequest);
        LOGGER.warn("metadata = {}", metadata);

        return serverRequest.bodyToMono(PAGE_MODEL_FOR_SHINE_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(shineService::selectShineInfoPageMonoByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr, serverRequest), BlueResponse.class));
    }

}
