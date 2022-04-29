package com.blue.media.config.filter.global;

import com.blue.auth.api.model.AccessAssert;
import com.blue.auth.api.model.AccessAsserted;
import com.blue.base.model.exps.BlueException;
import com.blue.media.remote.consumer.RpcAuthServiceConsumer;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.BiConsumer;

import static com.blue.base.common.access.AccessProcessor.accessToJson;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.common.base.CommonFunctions.HEADER_VALUE_GETTER;
import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.base.ResponseElement.UNAUTHORIZED;
import static com.blue.base.constant.base.SpecialAccess.VISITOR;
import static com.blue.media.config.filter.BlueFilterOrder.BLUE_AUTH;
import static java.util.Optional.ofNullable;

/**
 * auth filter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces"})
@Component
public final class BlueAuthFilter implements WebFilter, Ordered {

    private final RpcAuthServiceConsumer rpcAuthServiceConsumer;

    public BlueAuthFilter(RpcAuthServiceConsumer rpcAuthServiceConsumer) {
        this.rpcAuthServiceConsumer = rpcAuthServiceConsumer;
    }

    private static final BiConsumer<ServerHttpRequest, String> AUTHENTICATION_REPACKAGER =
            (request, accessInfo) -> request.mutate().headers(hs -> hs.set(AUTHORIZATION.name, accessInfo));

    private static void authProcess(AccessAsserted accessAsserted, ServerHttpRequest request, Map<String, Object> attributes) {
        if (isNull(accessAsserted) || isNull(attributes))
            throw new BlueException(UNAUTHORIZED);

        String accStr = accessToJson(ofNullable(accessAsserted.getAccess()).orElse(VISITOR.access));
        if (accessAsserted.getCertificate())
            AUTHENTICATION_REPACKAGER.accept(request, accStr);

        attributes.put(ACCESS.key, accStr);
        attributes.put(SEC_KEY.key, accessAsserted.getSecKey());
        attributes.put(REQUEST_UN_DECRYPTION.key, accessAsserted.getRequestUnDecryption());
        attributes.put(RESPONSE_UN_ENCRYPTION.key, accessAsserted.getResponseUnEncryption());
        attributes.put(EXISTENCE_REQUEST_BODY.key, accessAsserted.getExistenceRequestBody());
        attributes.put(EXISTENCE_RESPONSE_BODY.key, accessAsserted.getExistenceResponseBody());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Map<String, Object> attributes = exchange.getAttributes();

        return rpcAuthServiceConsumer.assertAccess(
                        new AccessAssert(HEADER_VALUE_GETTER.apply(request.getHeaders(), AUTHORIZATION.name),
                                ofNullable(attributes.get(METHOD.key)).map(String::valueOf).orElse(""),
                                ofNullable(attributes.get(URI.key)).map(String::valueOf).orElse("")))
                .flatMap(authAsserted -> {
                    authProcess(authAsserted, request, attributes);
                    return chain.filter(exchange);
                });
    }

    @Override
    public int getOrder() {
        return BLUE_AUTH.order;
    }

}
