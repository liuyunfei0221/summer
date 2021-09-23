package com.blue.file.config.common.request.body;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 根据请求类型封装上报数据的业务逻辑
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface RequestBodyGetter {

    /**
     * 处理的请求类型
     *
     * @return
     */
    String getContentType();

    /**
     * 处理
     *
     * @param exchange
     * @return
     */
    Mono<String> processor(ServerWebExchange exchange);

}
