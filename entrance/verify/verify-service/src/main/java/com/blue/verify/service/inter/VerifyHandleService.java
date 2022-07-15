package com.blue.verify.service.inter;

import com.blue.basic.constant.verify.BusinessType;
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
     * @param businessType
     * @param destination
     * @return
     */
    Mono<String> generate(VerifyType verifyType, BusinessType businessType, String destination);

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
     * @param businessType
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    Mono<Boolean> validate(VerifyType verifyType, BusinessType businessType, String key, String verify, Boolean repeatable);

    /**
     * validate by turing test
     *
     * @param key
     * @param verify
     * @return
     */
    Mono<Boolean> turingValidate(String key, String verify);

}
