package com.blue.finance.component.dynamic.inter;


import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * dynamic endpoint handler interface
 *
 * @author liuyunfei
 * @date 2021/9/14
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface DynamicEndPointHandler {

    /**
     * handle
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> handle(ServerRequest serverRequest);

}
