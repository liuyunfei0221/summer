package com.blue.verify.service.impl;

import com.blue.verify.component.verify.VerifyProcessor;
import com.blue.verify.service.inter.VerifyHandleService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * verify service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class VerifyHandleServiceImpl implements VerifyHandleService {

    private final VerifyProcessor verifyProcessor;

    public VerifyHandleServiceImpl(VerifyProcessor verifyProcessor) {
        this.verifyProcessor = verifyProcessor;
    }

    /**
     * generate verify
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> generate(ServerRequest serverRequest) {
        return verifyProcessor.generate(serverRequest);
    }

}
