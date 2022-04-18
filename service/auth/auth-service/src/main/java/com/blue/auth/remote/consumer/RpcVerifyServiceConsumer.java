package com.blue.auth.remote.consumer;

import com.blue.base.constant.verify.VerifyType;
import com.blue.verify.api.inter.RpcVerifyService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.time.Duration;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
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

    private final Scheduler scheduler;

    public RpcVerifyServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * generate pair
     *
     * @param type
     * @param key
     * @param length
     * @param expire
     * @return
     */
    Mono<String> generate(VerifyType type, String key, Integer length, Duration expire) {
        LOGGER.info("Mono<String> generate(VerifyType type, String key, Integer length, Duration expire), type = {}, key = {}, length = {}, expire ={}",
                key, type, length, expire);
        return fromFuture(rpcVerifyService.generate(type, key, length, expire)).subscribeOn(scheduler);
    }

    /**
     * validate pair
     *
     * @param type
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    Mono<Boolean> validate(VerifyType type, String key, String verify, Boolean repeatable) {
        LOGGER.info("Mono<Boolean> validate(VerifyType type, String key, String verify, Boolean repeatable), type = {}, key = {}, verify = {}, repeatable = {}",
                type, key, verify, repeatable);
        return fromFuture(rpcVerifyService.validate(type, key, verify, repeatable)).subscribeOn(scheduler);
    }

}
