package com.blue.verify.handler.api;

import com.blue.verify.service.inter.VerifyHandleService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


/**
 * verify api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "Duplicates"})
@Component
public final class VerifyApiHandler {

    private final VerifyHandleService verifyHandleService;

    public VerifyApiHandler(VerifyHandleService verifyHandleService) {
        this.verifyHandleService = verifyHandleService;
    }

    /**
     * download
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> generate(ServerRequest serverRequest) {
        return verifyHandleService.generate(serverRequest);
    }

}
