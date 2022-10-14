package com.blue.media.remote.consumer;

import com.blue.basic.constant.verify.VerifyBusinessType;
import com.blue.basic.constant.verify.VerifyType;
import com.blue.verify.api.inter.RpcVerifyHandleService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcVerifyHandleServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-verify"},
            methods = {
                    @Method(name = "generate", async = true),
                    @Method(name = "validate", async = true),
                    @Method(name = "turingValidate", async = true)
            })
    private RpcVerifyHandleService rpcVerifyHandleService;

    private final Scheduler scheduler;

    public RpcVerifyHandleServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
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
    public Mono<String> generate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String destination, List<String> languages) {
        return fromFuture(rpcVerifyHandleService.generate(verifyType, verifyBusinessType, destination, languages)).publishOn(scheduler);
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
    public Mono<Boolean> validate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String key, String verify, Boolean repeatable) {
        return fromFuture(rpcVerifyHandleService.validate(verifyType, verifyBusinessType, key, verify, repeatable)).publishOn(scheduler);
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
    public Mono<Boolean> turingValidate(String identity, Integer allow, Long expiresMillis, String key, String verify) {
        return fromFuture(rpcVerifyHandleService.turingValidate(identity, allow, expiresMillis, key, verify)).publishOn(scheduler);
    }

}
