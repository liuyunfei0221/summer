package com.blue.member.remote.consumer;

import com.blue.auth.api.inter.RpcAuthControlService;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.api.model.MemberCredentialInfo;
import com.blue.base.model.common.Access;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;


/**
 * rpc auth control consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam", "FieldCanBeLocal", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class RpcControlServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcControlServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-auth"},
            methods = {
                    @Method(name = "initMemberAuthInfo", async = false, timeout = 60000, retries = 0),
                    @Method(name = "refreshMemberRoleById", async = true),
                    @Method(name = "getAuthorityByAccess", async = true),
                    @Method(name = "getAuthorityByMemberId", async = true)
            })
    private RpcAuthControlService rpcAuthControlService;

    private final Scheduler scheduler;

    public RpcControlServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * init auth info for a new member
     *
     * @param memberCredentialInfo
     */
    public void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo) {
        LOGGER.info("void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo), memberCredentialInfo = {}", memberCredentialInfo);
        rpcAuthControlService.initMemberAuthInfo(memberCredentialInfo);
    }

    /**
     * update member's auth by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    Mono<Boolean> refreshMemberRoleById(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("Mono<Boolean> refreshMemberRoleById(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}, operatorId = {}", memberId, roleId, operatorId);
        return fromFuture(rpcAuthControlService.refreshMemberRoleById(memberId, roleId, operatorId)).subscribeOn(scheduler).subscribeOn(scheduler);
    }

    /**
     * query authority by access
     *
     * @param access
     * @return
     */
    public Mono<AuthorityBaseOnRole> getAuthorityByAccess(Access access) {
        LOGGER.info("Mono<AuthorityBaseOnRole> getAuthorityByAccess(Access access), access = {}", access);
        return fromFuture(rpcAuthControlService.getAuthorityByAccess(access)).subscribeOn(scheduler).subscribeOn(scheduler);
    }

    /**
     * query authority by member id
     *
     * @param memberId
     * @return
     */
    public Mono<AuthorityBaseOnRole> getAuthorityByMemberId(Long memberId) {
        LOGGER.info("Mono<AuthorityBaseOnRole> getAuthorityByMemberId(Long memberId), memberId = {}", memberId);
        return fromFuture(rpcAuthControlService.getAuthorityByMemberId(memberId)).subscribeOn(scheduler).subscribeOn(scheduler);
    }

}
