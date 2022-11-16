package com.blue.verify.config.filter.global;

import com.blue.basic.model.exps.BlueException;
import com.blue.verify.config.deploy.RequestAttributeDeploy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.CommonFunctions.HEADER_VALUE_GETTER;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.verify.config.filter.BlueFilterOrder.BLUE_REQUEST_ATTR;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static reactor.core.publisher.Mono.*;

/**
 * request attr filter
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
@Component
public final class BlueRequestAttrFilter implements WebFilter, Ordered {

    public BlueRequestAttrFilter(RequestAttributeDeploy requestAttributeDeploy) {
        VALID_CONTENT_TYPES = new HashSet<>(requestAttributeDeploy.getValidContentTypes());
        MAX_URI_LENGTH = requestAttributeDeploy.getMaxUriLength();
        MAX_HEADER_COUNT = requestAttributeDeploy.getMaxHeaderCount();
        MAX_HEADER_LENGTH = requestAttributeDeploy.getMaxHeaderLength();
        MAX_CONTENT_LENGTH = requestAttributeDeploy.getMaxContentLength();
    }

    private static Set<String> VALID_CONTENT_TYPES;

    private static int MAX_URI_LENGTH, MAX_HEADER_COUNT, MAX_HEADER_LENGTH, MAX_CONTENT_LENGTH;

    private static final Function<ServerHttpRequest, Mono<Boolean>> URI_ASSERTER = request ->
            request.getURI().getRawPath().length() <= MAX_URI_LENGTH
                    ?
                    just(true)
                    :
                    error(() -> new BlueException(PAYLOAD_TOO_LARGE));

    private static final Function<HttpHeaders, Mono<Boolean>> CONTENT_TYPE_ASSERTER = headers ->
            VALID_CONTENT_TYPES.contains(HEADER_VALUE_GETTER.apply(headers, CONTENT_TYPE))
                    ?
                    just(true)
                    :
                    error(() -> new BlueException(UNSUPPORTED_MEDIA_TYPE));

    private static final Function<HttpHeaders, Mono<Boolean>> HEADER_LENGTH_ASSERTER = headers -> {
        int headerCount = 0;
        int headerLength = 0;
        List<String> headerValues;

        for (Map.Entry<String, List<String>> headerEntry : headers.entrySet()) {
            if (++headerCount > MAX_HEADER_COUNT)
                throw new BlueException(PAYLOAD_TOO_LARGE);

            headerValues = headerEntry.getValue();
            for (String value : headerValues) {
                headerLength += value.getBytes().length;
                if (headerLength > MAX_HEADER_LENGTH)
                    throw new BlueException(PAYLOAD_TOO_LARGE);
            }
        }

        return just(true);
    };

    private static final Function<ServerHttpRequest, Mono<Boolean>> HEADER_ASSERTER = request -> {
        HttpHeaders headers = request.getHeaders();

        return zip(CONTENT_TYPE_ASSERTER.apply(headers), HEADER_LENGTH_ASSERTER.apply(headers))
                .flatMap(b -> just(true));
    };

    private static final Function<ServerHttpRequest, Mono<Boolean>> CONTENT_ASSERTER = request ->
            ofNullable(request.getHeaders().getFirst(CONTENT_LENGTH)).map(Integer::valueOf).orElse(1) <= MAX_CONTENT_LENGTH
                    ?
                    just(true)
                    :
                    error(() -> new BlueException(PAYLOAD_TOO_LARGE));

    private static final Function<ServerHttpRequest, Mono<Void>> REQUEST_ASSERTER = request ->
            isNotNull(request)
                    ?
                    zip(URI_ASSERTER.apply(request), HEADER_ASSERTER.apply(request), CONTENT_ASSERTER.apply(request)).then()
                    :
                    error(() -> new BlueException(BAD_REQUEST));

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return REQUEST_ASSERTER.apply(exchange.getRequest()).then(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return BLUE_REQUEST_ATTR.order;
    }

}
