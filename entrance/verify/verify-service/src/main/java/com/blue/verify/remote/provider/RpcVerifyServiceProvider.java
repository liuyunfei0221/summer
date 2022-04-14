package com.blue.verify.remote.provider;


import com.blue.base.constant.verify.VerifyType;
import com.blue.verify.api.inter.RpcVerifyService;
import com.blue.verify.service.inter.VerifyService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * rpc verify provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcVerifyService.class, version = "1.0", methods = {
        @Method(name = "generate", async = true),
        @Method(name = "validate", async = true)
})
public class RpcVerifyServiceProvider implements RpcVerifyService {

    private static final Logger LOGGER = getLogger(RpcVerifyServiceProvider.class);

    private final VerifyService verifyService;

    private final Scheduler scheduler;

    public RpcVerifyServiceProvider(VerifyService verifyService, Scheduler scheduler) {
        this.verifyService = verifyService;
        this.scheduler = scheduler;
    }

    /**
     * generate
     *
     * @param type
     * @param key
     * @param length
     * @param expire
     * @return
     */
    @Override
    public CompletableFuture<String> generate(VerifyType type, String key, Integer length, Duration expire) {
        LOGGER.info("CompletableFuture<String> generate(VerifyType type, String key, Integer length, Duration expire), type = {}, key = {}, length = {}, toUpperCase = {}, expire = {}",
                type, key, length, expire);
        return just(true).publishOn(scheduler).flatMap(v -> verifyService.generate(type, key, length, expire)).toFuture();
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
    @Override
    public CompletableFuture<Boolean> validate(VerifyType type, String key, String verify, Boolean repeatable) {
        LOGGER.info("CompletableFuture<Boolean> validate(VerifyType type, String key, String verify, Boolean repeatable), type = {}, key = {}, verify = {}, repeatable = {}",
                type, key, verify, repeatable);
        return just(true).publishOn(scheduler).flatMap(v -> verifyService.validate(type, key, verify, repeatable)).toFuture();
    }

}
