package com.blue.verify.remote.provider;


import com.blue.base.constant.verify.VerifyType;
import com.blue.verify.api.inter.RpcVerifyService;
import com.blue.verify.api.model.VerifyPair;
import com.blue.verify.service.inter.VerifyService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static reactor.util.Loggers.getLogger;

/**
 * rpc secure provider
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcVerifyService.class, version = "1.0", methods = {
        @Method(name = "generate", async = true),
        @Method(name = "validate", async = true)
})
public class RpcVerifyServiceProvider implements RpcVerifyService {

    private static final Logger LOGGER = getLogger(RpcVerifyServiceProvider.class);

    private final VerifyService verifyService;

    public RpcVerifyServiceProvider(VerifyService verifyService) {
        this.verifyService = verifyService;
    }

    /**
     * generate
     *
     * @param type
     * @param key
     * @param length
     * @param toUpperCase
     * @param expire
     * @return
     */
    @Override
    public CompletableFuture<VerifyPair> generate(VerifyType type, String key, Integer length, Boolean toUpperCase, Duration expire) {
        LOGGER.info("CompletableFuture<VerifyPair> generate(VerifyType type, String key, Integer length, Boolean toUpperCase, Duration expire), type = {}, key = {}, length = {}, toUpperCase = {}, expire = {}",
                type, key, length, toUpperCase, expire);
        return verifyService.generate(type, key, length, toUpperCase, expire).toFuture();
    }

    /**
     * validate pair
     *
     * @param verifyPair
     * @param repeatable
     * @return
     */
    @Override
    public CompletableFuture<Boolean> validate(VerifyType type, VerifyPair verifyPair, boolean repeatable) {
        LOGGER.info("CompletableFuture<Boolean> validate(VerifyType type, VerifyPair verifyPair, boolean repeatable), type = {}, verifyPair = {}, repeatable = {}",
                type, verifyPair, repeatable);
        return verifyService.validate(type, verifyPair, repeatable).toFuture();
    }

}
