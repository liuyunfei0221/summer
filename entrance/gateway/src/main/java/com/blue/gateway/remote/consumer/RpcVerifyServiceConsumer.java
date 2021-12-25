package com.blue.gateway.remote.consumer;

import com.blue.verify.api.inter.RpcVerifyService;
import com.blue.verify.api.model.VerifyPair;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2021/12/26
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public class RpcVerifyServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcVerifyServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-verify"},
            methods = {
                    @Method(name = "validate", async = true, retries = 2)
            })
    private RpcVerifyService rpcVerifyService;

    /**
     * validate pair
     *
     * @param verifyPair
     * @param repeatable
     * @return
     */
    public Mono<Boolean> validate(VerifyPair verifyPair, boolean repeatable) {
        LOGGER.info("Mono<Boolean> validate(VerifyPair verifyPair, boolean repeatable), verifyPair = {}, repeatable = {}", verifyPair, repeatable);
        return fromFuture(rpcVerifyService.validate(verifyPair, repeatable));
    }

}
