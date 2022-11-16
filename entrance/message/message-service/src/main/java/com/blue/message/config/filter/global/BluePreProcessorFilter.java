package com.blue.message.config.filter.global;

import com.blue.basic.constant.common.BlueHeader;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.blue.basic.common.base.CommonFunctions.*;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
import static com.blue.basic.constant.common.BlueHeader.AUTHORIZATION;
import static com.blue.basic.constant.common.BlueHeader.REQUEST_IP;
import static com.blue.message.config.filter.BlueFilterOrder.BLUE_PRE_PROCESSOR;
import static java.util.Optional.ofNullable;

/**
 * pre processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces"})
@Component
public final class BluePreProcessorFilter implements WebFilter, Ordered {

    public BluePreProcessorFilter() {
    }

    private static final BiConsumer<ServerHttpRequest, String> REQUEST_IP_REPACKAGER =
            (request, ip) -> request.mutate().headers(hs -> hs.set(REQUEST_IP.name, ip));

    private final Consumer<ServerWebExchange> ATTR_PACKAGER = exchange -> {

        ServerHttpRequest request = exchange.getRequest();

        Map<String, Object> attributes = exchange.getAttributes();

        String method = request.getMethodValue().intern();
        METHOD_VALUE_ASSERTER.accept(method);
        SCHEMA_ASSERTER.accept(request.getURI().getScheme());

        String realUri = request.getPath().value();
        String ip = getIp(request);

        attributes.put(REQUEST_ID.key, request.getId());
        attributes.put(CLIENT_IP.key, ip);
        attributes.put(METHOD.key, method);
        attributes.put(REAL_URI.key, realUri);
        attributes.put(URI.key, REQUEST_REST_URI_PROCESSOR.apply(realUri).intern());

        ofNullable(request.getHeaders().getFirst(AUTHORIZATION.name))
                .ifPresent(jwt -> attributes.put(JWT.key, jwt));
        ofNullable(request.getHeaders().getFirst(BlueHeader.USER_AGENT.name))
                .ifPresent(userAgent -> attributes.put(USER_AGENT.key, userAgent));
        ofNullable(request.getHeaders().getFirst(BlueHeader.METADATA.name))
                .ifPresent(metadata -> attributes.put(METADATA.key, metadata));
        ofNullable(request.getHeaders().getFirst(BlueHeader.SOURCE.name))
                .ifPresent(source -> attributes.put(SOURCE.key, source));
        ofNullable(request.getHeaders().getFirst(BlueHeader.HOST.name))
                .ifPresent(host -> attributes.put(HOST.key, host));
        ofNullable(request.getHeaders().getFirst(BlueHeader.REQUEST_EXTRA.name))
                .ifPresent(extra -> attributes.put(REQUEST_EXTRA.key, extra));

        REQUEST_IP_REPACKAGER.accept(request, ip);
    };

    @SuppressWarnings({"NullableProblems", "DuplicatedCode"})
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ATTR_PACKAGER.accept(exchange);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return BLUE_PRE_PROCESSOR.order;
    }

}
