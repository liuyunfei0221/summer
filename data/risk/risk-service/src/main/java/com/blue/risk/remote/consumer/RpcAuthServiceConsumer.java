package com.blue.risk.remote.consumer;

import com.blue.auth.api.inter.RpcAuthService;
import com.blue.auth.api.model.AccessAssert;
import com.blue.auth.api.model.AccessAsserted;
import com.blue.base.model.common.Access;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc auth reference
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class RpcAuthServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-auth"},
            methods = {
                    @Method(name = "assertAccess", async = true),
                    @Method(name = "invalidateAuthByAccess", async = true),
                    @Method(name = "invalidateAuthByJwt", async = true),
                    @Method(name = "invalidateAuthByMemberId", async = true)
            })
    private RpcAuthService rpcAuthService;

    private final Scheduler scheduler;

    public RpcAuthServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * authentication and authorization
     *
     * @param accessAssert
     * @return
     */
    public Mono<AccessAsserted> assertAccess(AccessAssert accessAssert) {
        return fromFuture(rpcAuthService.assertAccess(accessAssert)).subscribeOn(scheduler);
    }

    /**
     * invalid auth by access
     *
     * @param access
     * @return
     */
    public Mono<Boolean> invalidateAuthByAccess(Access access) {
        return fromFuture(rpcAuthService.invalidateAuthByAccess(access)).subscribeOn(scheduler);
    }

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    public Mono<Boolean> invalidateAuthByJwt(String jwt) {
        return fromFuture(rpcAuthService.invalidateAuthByJwt(jwt)).subscribeOn(scheduler);
    }

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    public Mono<Boolean> invalidateAuthByMemberId(Long memberId) {
        return fromFuture(rpcAuthService.invalidateAuthByMemberId(memberId)).subscribeOn(scheduler);
    }

}
