package com.blue.secure.remote.consumer;

import com.blue.base.constant.base.RandomType;
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
     * @param length
     * @param expire
     * @return
     */
    Mono<VerifyPair> generate(RandomType type, int length, Duration expire) {
        LOGGER.info("Mono<VerifyPair> generate(RandomType type, int length, Duration expire), type = {}, length = {}, expire ={}", type, length, expire);
        return fromFuture(rpcVerifyService.generate(type, length, expire));
    }

    /**
     * validate pair
     *
     * @param verifyPair
     * @param repeatable
     * @return
     */
    Mono<Boolean> validate(VerifyPair verifyPair, boolean repeatable) {
        LOGGER.info("Mono<Boolean> validate(VerifyPair verifyPair, boolean repeatable), verifyPair = {}, repeatable = {}", verifyPair, repeatable);
        return fromFuture(rpcVerifyService.validate(verifyPair, repeatable));
    }

}
