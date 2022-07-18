package com.blue.basic.common.base;

import com.blue.basic.common.base.BlueChecker;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.CommonFunctions.getIp;
import static com.blue.basic.constant.common.BlueHeader.REQUEST_IP;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;

/**
 * req ip getter for reactive
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class RequestIpGetter {

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
