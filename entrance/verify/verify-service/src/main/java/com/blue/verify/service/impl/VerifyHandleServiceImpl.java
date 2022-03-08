package com.blue.verify.service.impl;

import com.blue.base.constant.verify.VerifyBusinessType;
import com.blue.base.constant.verify.VerifyType;
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
     * generate verify for api
     *
     * @param verifyType
     * @param verifyBusinessType
     * @return destination
     */
    @Override
    public Mono<String> generate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String destination) {
        return verifyProcessor.handle(verifyType, verifyBusinessType, destination);
    }

    /**
     * generate verify
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> generate(ServerRequest serverRequest) {
        return verifyProcessor.handle(serverRequest);
    }

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
    @Override
    public Mono<Boolean> validate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String key, String verify, Boolean repeatable) {
        return verifyProcessor.validate(verifyType, verifyBusinessType, key, verify, repeatable);
    }

}
