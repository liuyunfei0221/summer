package com.blue.analyze.remote.consumer;

import com.blue.auth.api.inter.RpcAuthControlService;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.base.model.common.Access;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

import static reactor.core.publisher.Mono.fromFuture;


/**
 * rpc auth control consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "FieldCanBeLocal"})
@Component
public class RpcAuthControlServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-auth"},
            methods = {
                    @Method(name = "selectAuthorityByAccess", async = true),
                    @Method(name = "selectAuthorityByMemberId", async = true)
            })
    private RpcAuthControlService rpcAuthControlService;

    private final Scheduler scheduler;

    public RpcAuthControlServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * query authority by access
     *
     * @param access
     * @return
     */
    public Mono<List<AuthorityBaseOnRole>> selectAuthorityByAccess(Access access) {
        return fromFuture(rpcAuthControlService.selectAuthorityByAccess(access)).publishOn(scheduler);
    }

    /**
     * query authority by member id
     *
     * @param memberId
     * @return
     */
    public Mono<List<AuthorityBaseOnRole>> selectAuthorityByMemberId(Long memberId) {
        return fromFuture(rpcAuthControlService.selectAuthorityByMemberId(memberId)).publishOn(scheduler);
    }

}
