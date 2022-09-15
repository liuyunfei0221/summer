package com.blue.gateway.config.filter.global;

import com.blue.auth.api.model.AccessAssert;
import com.blue.auth.api.model.AccessAsserted;
import com.blue.basic.model.exps.BlueException;
import com.blue.gateway.remote.consumer.RpcAuthServiceConsumer;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.BiConsumer;

import static com.blue.basic.common.access.AccessProcessor.accessToJson;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.HEADER_VALUE_GETTER;
import static com.blue.basic.constant.common.BlueDataAttrKey.*;
import static com.blue.basic.constant.common.BlueHeader.AUTHORIZATION;
import static com.blue.basic.constant.common.ResponseElement.UNAUTHORIZED;
import static com.blue.basic.constant.common.SpecialAccess.VISITOR;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_AUTH;
import static java.util.Optional.ofNullable;

/**
 * auth filter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces"})
@Component
public final class BlueAuthFilter implements GlobalFilter, Ordered {

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

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Map<String, Object> attributes = exchange.getAttributes();

        return rpcAuthServiceConsumer.assertAccess(
                        new AccessAssert(HEADER_VALUE_GETTER.apply(request.getHeaders(), AUTHORIZATION.name),
                                ofNullable(attributes.get(METHOD.key)).map(String::valueOf).orElse(EMPTY_DATA.value),
                                ofNullable(attributes.get(URI.key)).map(String::valueOf).orElse(EMPTY_DATA.value)))
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
