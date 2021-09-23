package com.blue.finance.component.dynamic.inter;


import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 动态处理器接口
 *
 * @author liuyunfei
 * @date 2021/9/14
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface DynamicEndPointHandler {

    /**
     * 处理
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> handle(ServerRequest serverRequest);

}
