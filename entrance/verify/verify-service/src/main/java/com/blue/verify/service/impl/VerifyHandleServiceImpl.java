package com.blue.verify.service.impl;

import com.blue.basic.constant.verify.VerifyBusinessType;
import com.blue.basic.constant.verify.VerifyType;
import com.blue.basic.model.exps.BlueException;
import com.blue.verify.component.verify.VerifyProcessor;
import com.blue.verify.service.inter.VerifyHandleService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.BlueChecker.isGreaterThanZero;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static reactor.util.Loggers.getLogger;

/**
 * verify service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class VerifyHandleServiceImpl implements VerifyHandleService {

    private static final Logger LOGGER = getLogger(VerifyHandleServiceImpl.class);

    private final VerifyProcessor verifyProcessor;

    public VerifyHandleServiceImpl(VerifyProcessor verifyProcessor) {
        this.verifyProcessor = verifyProcessor;
    }

    /**
     * generate verify for api
     *
     * @param verifyType
     * @param verifyBusinessType
     * @param destination
     * @param languages
     * @return
     */
    @Override
    public Mono<String> generate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String destination, List<String> languages) {
        LOGGER.info("Mono<String> generate(), verifyType = {}, verifyBusinessType = {}, destination = {}, languages = {}",
                verifyType, verifyBusinessType, destination, languages);

        return verifyProcessor.handle(verifyType, verifyBusinessType, destination, languages);
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
        LOGGER.info("Mono<Boolean> validate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String key, String verify, Boolean repeatable), verifyType = {}, verifyBusinessType = {}, key = {}, verify = {}, repeatable = {}",
                verifyType, verifyBusinessType, key, verify, repeatable);

        return verifyProcessor.validate(verifyType, verifyBusinessType, key, verify, repeatable);
    }

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
    @Override
    public Mono<Boolean> turingValidate(String identity, Integer allow, Long expiresMillis, String key, String verify) {
        LOGGER.info("Mono<Boolean> turingValidate(Integer allow, Long expiresMillis, String key, String verify), allow = {}, expiresMillis = {}, key = {}, verify = {}",
                allow, expiresMillis, key, verify);

        if (isBlank(identity) || !isGreaterThanZero(allow) || !isGreaterThanZero(expiresMillis))
            throw new BlueException(INVALID_PARAM);

        return verifyProcessor.turingValidate(identity, allow, expiresMillis, key, verify);
    }

}
