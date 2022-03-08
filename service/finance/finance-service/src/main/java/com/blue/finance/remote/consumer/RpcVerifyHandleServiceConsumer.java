package com.blue.finance.remote.consumer;

import com.blue.base.constant.verify.VerifyBusinessType;
import com.blue.base.constant.verify.VerifyType;
import com.blue.verify.api.inter.RpcVerifyHandleService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcVerifyHandleServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcVerifyHandleServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-verify"}, methods = {
            @Method(name = "generate", async = true),
            @Method(name = "validate", async = true)
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
     * @return
     */
    public Mono<String> generate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String destination) {
        LOGGER.info("Mono<String> generate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String destination), verifyType = {}, verifyBusinessType = {}, destination = {}",
                verifyType, verifyBusinessType, destination);

        return fromFuture(rpcVerifyHandleService.generate(verifyType, verifyBusinessType, destination)).publishOn(scheduler);
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
        LOGGER.info("Mono<Boolean> validate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String key, String verify, Boolean repeatable), " +
                "verifyType = {}, verifyBusinessType = {}, key = {}, verify = {}, repeatable = {}", verifyType, verifyBusinessType, key, verify, repeatable);

        return fromFuture(rpcVerifyHandleService.validate(verifyType, verifyBusinessType, key, verify, repeatable)).publishOn(scheduler);
    }

}
