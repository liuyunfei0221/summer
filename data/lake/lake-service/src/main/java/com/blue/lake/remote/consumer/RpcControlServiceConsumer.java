package com.blue.lake.remote.consumer;

import com.blue.base.model.common.Access;
import com.blue.auth.api.inter.RpcAuthControlService;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;


/**
 * rpc auth control consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "FieldCanBeLocal"})
@Component
public class RpcControlServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcControlServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-auth"},
            methods = {
                    @Method(name = "getAuthorityByAccess", async = true),
                    @Method(name = "getAuthorityByMemberId", async = true)
            })
    private RpcAuthControlService rpcAuthControlService;

    /**
     * query authority by access
     *
     * @param access
     * @return
     */
    public Mono<AuthorityBaseOnRole> getAuthorityByAccess(Access access) {
        LOGGER.info("Mono<AuthorityBaseOnRole> getAuthorityByAccess(Access access), access = {}", access);
        return fromFuture(rpcAuthControlService.getAuthorityByAccess(access));
    }

    /**
     * query authority by member id
     *
     * @param memberId
     * @return
     */
    public Mono<AuthorityBaseOnRole> getAuthorityByMemberId(Long memberId) {
        LOGGER.info("Mono<AuthorityBaseOnRole> getAuthorityByMemberId(Long memberId), memberId = {}", memberId);
        return fromFuture(rpcAuthControlService.getAuthorityByMemberId(memberId));
    }

}
