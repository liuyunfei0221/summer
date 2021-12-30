package com.blue.file.config.filter.global;

import com.blue.base.model.exps.BlueException;
import com.blue.file.remote.consumer.RpcSecureServiceConsumer;
import com.blue.secure.api.model.AssertAuth;
import com.blue.secure.api.model.AuthAsserted;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
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
import static com.blue.file.config.filter.BlueFilterOrder.BLUE_SECURE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * secure filter
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces"})
@Component
public final class BlueSecureFilter implements WebFilter, Ordered {

    private static final Logger LOGGER = getLogger(BlueSecureFilter.class);

    private final RpcSecureServiceConsumer rpcSecureServiceConsumer;

    public BlueSecureFilter(RpcSecureServiceConsumer rpcSecureServiceConsumer) {
        this.rpcSecureServiceConsumer = rpcSecureServiceConsumer;
    }

    private static final BiConsumer<ServerHttpRequest, String> AUTHENTICATION_REPACKAGER =
            (request, accessInfo) -> request.mutate().headers(hs -> hs.set(AUTHORIZATION.name, accessInfo));

    private static void authProcess(AuthAsserted authAsserted, ServerHttpRequest request, Map<String, Object> attributes) {
        if (authAsserted == null || attributes == null)
            throw new BlueException(UNAUTHORIZED);

        String accStr = accessToJson(ofNullable(authAsserted.getAccessInfo()).orElse(VISITOR.access));
        if (authAsserted.getCertificate())
            AUTHENTICATION_REPACKAGER.accept(request, accStr);

        attributes.put(ACCESS.key, accStr);
        attributes.put(SEC_KEY.key, authAsserted.getSecKey());
        attributes.put(REQUEST_UN_DECRYPTION.key, authAsserted.getRequestUnDecryption());
        attributes.put(RESPONSE_UN_ENCRYPTION.key, authAsserted.getResponseUnEncryption());
        attributes.put(EXISTENCE_REQUEST_BODY.key, authAsserted.getExistenceRequestBody());
        attributes.put(EXISTENCE_RESPONSE_BODY.key, authAsserted.getExistenceResponseBody());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Map<String, Object> attributes = exchange.getAttributes();

        return rpcSecureServiceConsumer.assertAuth(
                        new AssertAuth(HEADER_VALUE_GETTER.apply(request.getHeaders(), AUTHORIZATION.name),
                                ofNullable(attributes.get(METHOD.key)).map(String::valueOf).orElse(""),
                                ofNullable(attributes.get(URI.key)).map(String::valueOf).orElse("")))
                .flatMap(authAsserted -> {
                    LOGGER.info("authAsserted = {}", authAsserted);
                    authProcess(authAsserted, request, attributes);
                    return chain.filter(exchange);
                });
    }

    @Override
    public int getOrder() {
        return BLUE_SECURE.order;
    }

}
