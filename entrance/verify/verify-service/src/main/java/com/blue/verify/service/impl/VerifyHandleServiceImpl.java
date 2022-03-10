package com.blue.verify.service.impl;

import com.blue.base.constant.verify.BusinessType;
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
     * @param businessType
     * @return destination
     */
    @Override
    public Mono<String> generate(VerifyType verifyType, BusinessType businessType, String destination) {
        return verifyProcessor.handle(verifyType, businessType, destination);
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
     * @param businessType
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    @Override
    public Mono<Boolean> validate(VerifyType verifyType, BusinessType businessType, String key, String verify, Boolean repeatable) {
        return verifyProcessor.validate(verifyType, businessType, key, verify, repeatable);
    }

}
