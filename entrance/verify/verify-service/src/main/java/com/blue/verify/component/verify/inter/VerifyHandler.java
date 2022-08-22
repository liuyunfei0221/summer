package com.blue.verify.component.verify.inter;

import com.blue.basic.constant.verify.VerifyBusinessType;
import com.blue.basic.constant.verify.VerifyType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * verify handler interface
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface VerifyHandler {

    /**
     * handle for api
     *
     * @param verifyBusinessType
     * @param destination
     * @return key
     */
    Mono<String> handle(VerifyBusinessType verifyBusinessType, String destination);

    /**
     * handle for endpoint
     *
     * @param verifyBusinessType
     * @param destination
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> handle(VerifyBusinessType verifyBusinessType, String destination, ServerRequest serverRequest);

    /**
     * validate verify
     *
     * @param verifyBusinessType
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    Mono<Boolean> validate(VerifyBusinessType verifyBusinessType, String key, String verify, Boolean repeatable);

    /**
     * target verify type to process
     *
     * @return
     */
    VerifyType targetType();

}
