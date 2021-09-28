package com.blue.shine.handler.api;

import com.blue.base.common.reactive.ReactiveCommonFunctions;
import com.blue.base.model.base.BlueResponse;
import com.blue.shine.service.inter.ShineService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.util.Loggers.getLogger;

/**
 * 公益功能控制器
 *
 * @author DarkBlue
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
     * 获取公益信息
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getShineInfo(ServerRequest serverRequest) {
        String ip = ReactiveCommonFunctions.getIp(serverRequest);
        LOGGER.warn("client ip = {}", ip);
        return shineService.getShineInfo(ip)
                .flatMap(shineInfo ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, shineInfo, OK.message), BlueResponse.class));
    }


}
