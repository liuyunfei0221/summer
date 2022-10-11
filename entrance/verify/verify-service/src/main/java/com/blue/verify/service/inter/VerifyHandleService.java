package com.blue.verify.service.inter;

import com.blue.basic.constant.verify.VerifyBusinessType;
import com.blue.basic.constant.verify.VerifyType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * verify handle service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface VerifyHandleService {

    /**
     * generate verify for api
     *
     * @param verifyType
     * @param verifyBusinessType
     * @param destination
     * @return
     */
    Mono<String> generate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String destination);

    /**
     * generate verify for endpoint
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> generate(ServerRequest serverRequest);

    /**
     * validate verify
     *
     * @param verifyType
     * @param verifyBusinessType
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    Mono<Boolean> validate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String key, String verify, Boolean repeatable);

    /**
     * validate by turing test
     *
     * @param identity
     * @param allow
     * @param expiresMillis
     * @param key
     * @param verify
     * @return
     */
    Mono<Boolean> turingValidate(String identity, Integer allow, Long expiresMillis, String key, String verify);

}
