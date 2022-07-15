package com.blue.shine.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.shine.service.inter.ShineService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.Map;

import static com.blue.basic.common.reactive.MetadataGetterForReactive.getMetadata;
import static com.blue.basic.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.basic.common.reactive.RequestIpGetterForReactive.getRequestIp;
import static com.blue.basic.constant.common.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
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
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        String ip = getRequestIp(serverRequest);

        LOGGER.warn("client ip = {}", ip);
        Map<String, String> metadata = getMetadata(serverRequest);
        LOGGER.warn("metadata = {}", metadata);

        return shineService.selectShineInfo(ip)
                .flatMap(shineInfo ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, shineInfo, serverRequest), BlueResponse.class));
    }


}
