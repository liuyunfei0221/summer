package com.blue.context.component;

import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.blue.context.constant.BlueContextKey.SERVER_WEB_EXCHANGE;

/**
 * context filter
 *
 * @author liuyunfei
 */
public final class ContextProcessFilter implements WebFilter {

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return chain.filter(exchange)
                .contextWrite(context -> context.putNonNull(SERVER_WEB_EXCHANGE, exchange));
    }

}
