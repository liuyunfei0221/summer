package com.blue.finance.remote.consumer;

import com.blue.basic.constant.verify.BusinessType;
import com.blue.basic.constant.verify.VerifyType;
import com.blue.verify.api.inter.RpcVerifyHandleService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

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
     * @param businessType
     * @param destination
     * @return
     */
    public Mono<String> generate(VerifyType verifyType, BusinessType businessType, String destination) {
        return fromFuture(rpcVerifyHandleService.generate(verifyType, businessType, destination)).publishOn(scheduler);
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
    public Mono<Boolean> validate(VerifyType verifyType, BusinessType businessType, String key, String verify, Boolean repeatable) {
        return fromFuture(rpcVerifyHandleService.validate(verifyType, businessType, key, verify, repeatable)).publishOn(scheduler);
    }

    /**
     * validate by turing test
     *
     * @param key
     * @param verify
     * @return
     */
    public Mono<Boolean> turingValidate(String key, String verify) {
        return fromFuture(rpcVerifyHandleService.turingValidate(key, verify)).publishOn(scheduler);
    }

}
