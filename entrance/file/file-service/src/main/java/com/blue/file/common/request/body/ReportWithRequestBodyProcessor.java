package com.blue.file.common.request.body;

import com.blue.base.model.event.data.DataEvent;
import com.blue.file.component.RequestEventReporter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 根据请求类型处理请求并封装上报的业务逻辑
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface ReportWithRequestBodyProcessor {

    /**
     * 处理的请求类型
     *
     * @return
     */
    String getContentType();

    /**
     * 处理
     *
     * @param request
     * @param exchange
     * @param chain
     * @param requestEventReporter
     * @param dataEvent
     * @return
     */
    Mono<Void> processor(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain, RequestEventReporter requestEventReporter, DataEvent dataEvent);

}
