package com.blue.verify.remote.provider;

import com.blue.basic.constant.verify.BusinessType;
import com.blue.basic.constant.verify.VerifyType;
import com.blue.verify.api.inter.RpcVerifyHandleService;
import com.blue.verify.service.inter.VerifyHandleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;

import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;

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
        return just(true).publishOn(scheduler).flatMap(v -> verifyHandleService.generate(verifyType, businessType, destination)).toFuture();
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
        return just(true).publishOn(scheduler).flatMap(v -> verifyHandleService.validate(verifyType, businessType, key, verify, repeatable)).toFuture();
    }

    /**
     * validate by turing test
     *
     * @param key
     * @param verify
     * @return
     */
    @Override
    public CompletableFuture<Boolean> turingValidate(String key, String verify) {
        return just(true).publishOn(scheduler).flatMap(v -> verifyHandleService.turingValidate(key, verify)).toFuture();
    }

}
