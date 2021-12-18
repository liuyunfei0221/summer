package com.blue.gateway.config.filter.global;

import com.blue.base.model.exps.BlueException;
import com.blue.gateway.remote.consumer.RpcSecureServiceConsumer;
import com.blue.secure.api.model.AssertAuth;
import com.blue.secure.api.model.AuthAsserted;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.Map;
import java.util.function.BiConsumer;

import static com.blue.base.common.auth.AuthProcessor.accessToJson;
import static com.blue.base.common.base.CommonFunctions.HEADER_VALUE_GETTER;
import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.base.ResponseElement.UNAUTHORIZED;
import static com.blue.base.constant.base.SpecialAccess.VISITOR;
import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_SECURE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * secure filter
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces"})
@Component
public final class BlueSecureFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = getLogger(BlueSecureFilter.class);

    private final RpcSecureServiceConsumer rpcSecureServiceConsumer;

    public BlueSecureFilter(RpcSecureServiceConsumer rpcSecureServiceConsumer) {
        this.rpcSecureServiceConsumer = rpcSecureServiceConsumer;
    }

    private static final BiConsumer<ServerHttpRequest, String> AUTHENTICATION_REPACKAGER =
            (request, accessInfo) -> request.mutate().headers(hs -> hs.set(AUTHORIZATION.name, accessInfo));

    private static void authProcess(AuthAsserted authAsserted, ServerHttpRequest request, Map<String, Object> exchangeAttributes) {
        if (authAsserted == null || exchangeAttributes == null)
            throw new BlueException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message);

        String accStr = accessToJson(ofNullable(authAsserted.getAccessInfo()).orElse(VISITOR.access));
        if (authAsserted.getCertificate())
            AUTHENTICATION_REPACKAGER.accept(request, accStr);

        exchangeAttributes.put(ACCESS.key, accStr);
        exchangeAttributes.put(SEC_KEY.key, authAsserted.getSecKey());
        exchangeAttributes.put(REQUEST_UN_DECRYPTION.key, authAsserted.getRequestUnDecryption());
        exchangeAttributes.put(RESPONSE_UN_ENCRYPTION.key, authAsserted.getResponseUnEncryption());
        exchangeAttributes.put(EXISTENCE_REQUEST_BODY.key, authAsserted.getExistenceRequestBody());
        exchangeAttributes.put(EXISTENCE_RESPONSE_BODY.key, authAsserted.getExistenceResponseBody());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        return rpcSecureServiceConsumer.assertAuth(
                        new AssertAuth(HEADER_VALUE_GETTER.apply(request.getHeaders(), AUTHORIZATION.name),
                                request.getMethodValue().intern(), request.getPath().value()))
                .flatMap(authAsserted -> {
                    LOGGER.info("authAsserted = {}", authAsserted);
                    authProcess(authAsserted, request, exchange.getAttributes());
                    return chain.filter(exchange);
                });
    }

    @Override
    public int getOrder() {
        return BLUE_SECURE.order;
    }

}
