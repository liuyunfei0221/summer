package com.blue.captcha.config.filter.global;

import com.blue.base.constant.base.BlueDataAttrKey;
import com.blue.base.model.exps.BlueException;
import com.blue.captcha.config.deploy.RequestAttributeDeploy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static com.blue.base.common.base.CommonFunctions.HEADER_VALUE_GETTER;
import static com.blue.base.constant.base.ResponseElement.PAYLOAD_TOO_LARGE;
import static com.blue.base.constant.base.ResponseElement.UNSUPPORTED_MEDIA_TYPE;
import static com.blue.captcha.config.filter.BlueFilterOrder.BLUE_REQUEST_ATTR;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static reactor.util.Loggers.getLogger;

/**
 * request attr filter
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
@Component
public final class BlueRequestAttrFilter implements WebFilter, Ordered {

    private static final Logger LOGGER = getLogger(BlueRequestAttrFilter.class);

    private final RequestAttributeDeploy requestAttributeDeploy;

    public BlueRequestAttrFilter(RequestAttributeDeploy requestAttributeDeploy) {
        this.requestAttributeDeploy = requestAttributeDeploy;
    }

    private static Set<String> VALID_CONTENT_TYPES;

    private static int MAX_URI_LENGTH, MAX_HEADER_COUNT, MAX_HEADER_LENGTH;
    private static long MAX_CONTENT_LENGTH;

    private static final Consumer<ServerHttpRequest> URI_ASSERTER = request -> {
        if (request.getURI().getRawPath().length() > MAX_URI_LENGTH)
            throw new BlueException(PAYLOAD_TOO_LARGE.status, PAYLOAD_TOO_LARGE.code, PAYLOAD_TOO_LARGE.message);
    };

    private static final Consumer<HttpHeaders> CONTENT_TYPE_ASSERTER = headers -> {
        if (VALID_CONTENT_TYPES.contains(HEADER_VALUE_GETTER.apply(headers, CONTENT_TYPE)))
            return;

        throw new BlueException(UNSUPPORTED_MEDIA_TYPE.status, UNSUPPORTED_MEDIA_TYPE.code, UNSUPPORTED_MEDIA_TYPE.message);
    };

    private static final Consumer<ServerHttpRequest> HEADER_ASSERTER = request -> {
        HttpHeaders headers = request.getHeaders();

        CONTENT_TYPE_ASSERTER.accept(headers);

        int headerCount = 0;
        int headerLength = 0;
        List<String> headerValues;

        for (Map.Entry<String, List<String>> headerEntry : headers.entrySet()) {
            if (++headerCount > MAX_HEADER_COUNT)
                throw new BlueException(PAYLOAD_TOO_LARGE.status, PAYLOAD_TOO_LARGE.code, PAYLOAD_TOO_LARGE.message);

            headerValues = headerEntry.getValue();
            for (String value : headerValues) {
                headerLength += value.getBytes().length;
                if (headerLength > MAX_HEADER_LENGTH)
                    throw new BlueException(PAYLOAD_TOO_LARGE.status, PAYLOAD_TOO_LARGE.code, PAYLOAD_TOO_LARGE.message);
            }
        }
    };

    private static final Consumer<ServerHttpRequest> CONTENT_ASSERTER = request -> {
        if (ofNullable(request.getHeaders().getFirst(CONTENT_LENGTH))
                .map(Integer::valueOf).orElse(1) > MAX_CONTENT_LENGTH)
            throw new BlueException(PAYLOAD_TOO_LARGE.status, PAYLOAD_TOO_LARGE.code, PAYLOAD_TOO_LARGE.message);
    };

    @PostConstruct
    private void init() {
        VALID_CONTENT_TYPES = new HashSet<>(requestAttributeDeploy.getValidContentTypes());
        MAX_URI_LENGTH = requestAttributeDeploy.getMaxUriLength();
        MAX_HEADER_COUNT = requestAttributeDeploy.getMaxHeaderCount();
        MAX_HEADER_LENGTH = requestAttributeDeploy.getMaxHeaderLength();
        MAX_CONTENT_LENGTH = requestAttributeDeploy.getMaxContentLength();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestId = ofNullable(exchange.getAttribute(BlueDataAttrKey.REQUEST_ID.key)).map(String::valueOf).orElse("");
        LOGGER.info("blueRequestAttrFilter -> requestId = {}", requestId);

        ServerHttpRequest request = exchange.getRequest();

        URI_ASSERTER.accept(request);
        HEADER_ASSERTER.accept(request);
        CONTENT_ASSERTER.accept(request);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return BLUE_REQUEST_ATTR.order;
    }

}
