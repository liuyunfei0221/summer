package com.blue.gateway.config.filter.global;

import com.blue.base.model.exps.BlueException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static com.blue.base.constant.base.CommonException.NOT_FOUND_EXP;
import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_LOAD_BALANCER_CLIENT;
import static java.util.Optional.ofNullable;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * loadbalancer
 *
 * @author liuyunfei
 * @date 2021/9/28
 * @apiNote
 */
@SuppressWarnings({"rawtypes", "ConstantConditions", "AlibabaAvoidComplexCondition"})
@Component
public class BlueReactiveLoadBalancerClientFilter implements GlobalFilter, Ordered {

    private final LoadBalancerClientFactory clientFactory;

    public BlueReactiveLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    private static final Supplier<BlueException> NOT_FOUND_EXP_SUP = () -> NOT_FOUND_EXP.exp;

    private static final String LOAD_BALANCE_SCHEME = "lb";

    private static final String SECURE_PROTOCOL = "https", PROTOCOL = "http";

    private static final Supplier<Request> REQUEST_SUPPLIER = () -> ReactiveLoadBalancer.REQUEST;

    private static final BiFunction<ServiceInstance, URI, URI> URI_RE_CONSTRUCTOR = LoadBalancerUriTools::reconstructURI;

    @Override
    public final Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI uri = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);
        String urlSchema;
        if (uri != null && (LOAD_BALANCE_SCHEME.equals((urlSchema = uri.getScheme()).intern()) || LOAD_BALANCE_SCHEME.equals(schemePrefix))) {
            addOriginalRequestUrl(exchange, uri);
            return choose(exchange)
                    .doOnNext(response -> {
                        if (response.hasServer()) {
                            ServiceInstance retrievedInstance = response.getServer();
                            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR,
                                    URI_RE_CONSTRUCTOR.apply(new DelegatingServiceInstance(retrievedInstance, schemePrefix == null ? retrievedInstance.isSecure() ? SECURE_PROTOCOL : PROTOCOL : urlSchema), uri));
                        } else {
                            throw NOT_FOUND_EXP.exp;
                        }
                    }).then(chain.filter(exchange));
        }

        return chain.filter(exchange);
    }

    private Mono<Response<ServiceInstance>> choose(ServerWebExchange exchange) {
        return ofNullable(this.clientFactory.getInstance(((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost(), ReactorServiceInstanceLoadBalancer.class))
                .map(lb -> lb.choose(REQUEST_SUPPLIER.get()))
                .orElseThrow(NOT_FOUND_EXP_SUP);
    }

    @Override
    public final int getOrder() {
        return BLUE_LOAD_BALANCER_CLIENT.order;
    }

}
