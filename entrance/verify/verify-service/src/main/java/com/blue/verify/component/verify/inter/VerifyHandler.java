package com.blue.verify.component.verify.inter;

import com.blue.base.constant.verify.BusinessType;
import com.blue.base.constant.verify.VerifyType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * verify handler interface
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface VerifyHandler {

    /**
     * handle for api
     *
     * @param businessType
     * @param destination
     * @return key
     */
    Mono<String> handle(BusinessType businessType, String destination);

    /**
     * handle for endpoint
     *
     * @param businessType
     * @param destination
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> handle(BusinessType businessType, String destination, ServerRequest serverRequest);

    /**
     * validate verify
     *
     * @param businessType
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    Mono<Boolean> validate(BusinessType businessType, String key, String verify, Boolean repeatable);

    /**
     * target verify type to process
     *
     * @return
     */
    VerifyType targetType();

}
