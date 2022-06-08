package com.blue.base.common.reactive;

import com.blue.base.common.base.BlueChecker;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.getIp;
import static com.blue.base.constant.common.BlueHeader.REQUEST_IP;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;

/**
 * req ip getter for reactive
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class RequestIpGetterForReactive {

    /**
     * get req ip from request
     *
     * @param serverRequest
     * @return
     */
    public static String getRequestIp(ServerRequest serverRequest) {
        return ofNullable(serverRequest.headers().firstHeader(REQUEST_IP.name))
                .filter(BlueChecker::isNotBlank).orElseGet(() -> getIp(serverRequest));
    }

    /**
     * get req ip mono from access
     *
     * @param serverRequest
     * @return
     */
    public static Mono<String> getRequestIpReact(ServerRequest serverRequest) {
        return just(getRequestIp(serverRequest));
    }

}
