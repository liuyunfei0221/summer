package com.blue.verify.remote.provider;

import com.blue.basic.constant.verify.VerifyBusinessType;
import com.blue.basic.constant.verify.VerifyType;
import com.blue.verify.api.inter.RpcVerifyHandleService;
import com.blue.verify.service.inter.VerifyHandleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

import java.util.List;
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
                @Method(name = "validate", async = true),
                @Method(name = "turingValidate", async = true)
        })
public class RpcVerifyHandleServiceProvider implements RpcVerifyHandleService {

    private final VerifyHandleService verifyHandleService;

    public RpcVerifyHandleServiceProvider(VerifyHandleService verifyHandleService) {
        this.verifyHandleService = verifyHandleService;
    }

    /**
     * generate verify for api
     *
     * @param verifyType
     * @param verifyBusinessType
     * @param destination
     * @return
     */
    @Override
    public CompletableFuture<String> generate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String destination, List<String> languages) {
        return just(true).flatMap(v -> verifyHandleService.generate(verifyType, verifyBusinessType, destination, languages)).toFuture();
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
    public CompletableFuture<Boolean> validate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String key, String verify, Boolean repeatable) {
        return just(true).flatMap(v -> verifyHandleService.validate(verifyType, verifyBusinessType, key, verify, repeatable)).toFuture();
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
    public CompletableFuture<Boolean> turingValidate(String identity, Integer allow, Long expiresMillis, String key, String verify) {
        return just(true).flatMap(v ->
                verifyHandleService.turingValidate(identity, allow, expiresMillis, key, verify)).toFuture();
    }

}
