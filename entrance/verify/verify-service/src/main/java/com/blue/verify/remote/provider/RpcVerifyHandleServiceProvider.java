package com.blue.verify.remote.provider;

import com.blue.base.constant.verify.BusinessType;
import com.blue.base.constant.verify.VerifyType;
import com.blue.verify.api.inter.RpcVerifyHandleService;
import com.blue.verify.service.inter.VerifyHandleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * rpc verify provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcVerifyHandleService.class,
        version = "1.0",
        methods = {
                @Method(name = "generate", async = true),
                @Method(name = "validate", async = true)
        })
public class RpcVerifyHandleServiceProvider implements RpcVerifyHandleService {

    private static final Logger LOGGER = getLogger(RpcVerifyHandleServiceProvider.class);

    private final VerifyHandleService verifyHandleService;

    private final Scheduler scheduler;

    public RpcVerifyHandleServiceProvider(VerifyHandleService verifyHandleService, Scheduler scheduler) {
        this.verifyHandleService = verifyHandleService;
        this.scheduler = scheduler;
    }

    /**
     * generate verify for api
     *
     * @param verifyType
     * @param businessType
     * @param destination
     * @return
     */
    @Override
    public CompletableFuture<String> generate(VerifyType verifyType, BusinessType businessType, String destination) {
        LOGGER.info("CompletableFuture<String> generate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String destination), verifyType = {}, verifyBusinessType = {}, destination = {}",
                verifyType, businessType, destination);

        return just(true).subscribeOn(scheduler).flatMap(v -> verifyHandleService.generate(verifyType, businessType, destination)).toFuture();
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
    public CompletableFuture<Boolean> validate(VerifyType verifyType, BusinessType businessType, String key, String verify, Boolean repeatable) {
        LOGGER.info("CompletableFuture<Boolean> validate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String key, String verify, Boolean repeatable), " +
                "verifyType = {}, verifyBusinessType = {}, key = {}, verify = {}, repeatable = {}", verifyType, businessType, key, verify, repeatable);

        return just(true).subscribeOn(scheduler).flatMap(v -> verifyHandleService.validate(verifyType, businessType, key, verify, repeatable)).toFuture();
    }

}
