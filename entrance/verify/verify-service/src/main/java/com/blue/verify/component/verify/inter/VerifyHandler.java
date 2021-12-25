package com.blue.verify.component.verify.inter;

import com.blue.base.constant.verify.VerifyType;
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
     * handle verify
     *
     * @param destination
     */
    Mono<ServerResponse> handle(String destination);

    /**
     * target verify type to process
     *
     * @return
     */
    VerifyType targetType();

}
