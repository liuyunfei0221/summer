package com.blue.file.remote.consumer;

import com.blue.base.constant.verify.VerifyType;
import com.blue.verify.api.inter.RpcVerifyService;
import com.blue.verify.api.model.VerifyPair;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcVerifyServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcVerifyServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-verify"}, methods = {
            @Method(name = "generate", async = true),
            @Method(name = "validate", async = true)
    })
    private RpcVerifyService rpcVerifyService;

    /**
     * generate pair
     *
     * @param type
     * @param key
     * @param length
     * @param toUpperCase
     * @param expire
     * @return
     */
    Mono<VerifyPair> generate(VerifyType type, String key, int length, Boolean toUpperCase, Duration expire) {
        LOGGER.info("Mono<VerifyPair> generate(VerifyType type, String key, int length, Duration expire), type = {}, key = {}, length = {}, expire ={}",
                key, type, length, expire);
        return fromFuture(rpcVerifyService.generate(type, key, length, toUpperCase, expire));
    }

    /**
     * validate pair
     *
     * @param type
     * @param verifyPair
     * @param repeatable
     * @return
     */
    Mono<Boolean> validate(VerifyType type, VerifyPair verifyPair, boolean repeatable) {
        LOGGER.info("Mono<Boolean> validate(VerifyType type,VerifyPair verifyPair, boolean repeatable), type = {} verifyPair = {}, repeatable = {}",
                type, verifyPair, repeatable);
        return fromFuture(rpcVerifyService.validate(type, verifyPair, repeatable));
    }

}
