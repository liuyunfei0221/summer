package com.blue.finance.component.dynamic.impl;

import com.blue.finance.component.dynamic.inter.DynamicEndPointHandler;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

/**
 * 动态处理器实现
 *
 * @author liuyunfei
 * @date 2021/9/14
 * @apiNote
 */
public class BlueGetDynamicEndPointHandlerImpl implements DynamicEndPointHandler {

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return ok().body(just("BlueGetDynamicEndPointHandlerImpl ok"), String.class);
    }

}
