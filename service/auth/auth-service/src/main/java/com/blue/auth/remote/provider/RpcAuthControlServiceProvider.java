package com.blue.auth.remote.provider;

import com.blue.auth.api.inter.RpcAuthControlService;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.api.model.MemberCredentialInfo;
import com.blue.auth.api.model.MemberRoleRelationParam;
import com.blue.auth.service.inter.AuthControlService;
import com.blue.base.model.common.Access;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;


/**
 * rpc auth control provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcAuthControlService.class,
        version = "1.0",
        methods = {
                @Method(name = "initMemberAuthInfo", async = false, timeout = 60000, retries = 0),
                @Method(name = "updateAuthorityByMemberSync", async = false, timeout = 60000, retries = 0),
                @Method(name = "refreshMemberRoleById", async = false),
                @Method(name = "getAuthorityByAccess", async = true),
                @Method(name = "getAuthorityByMemberId", async = true)
        })
public class RpcAuthControlServiceProvider implements RpcAuthControlService {

    private static final Logger LOGGER = getLogger(RpcAuthControlServiceProvider.class);

    private final AuthControlService authControlService;

    private final Scheduler scheduler;

    public RpcAuthControlServiceProvider(AuthControlService authControlService, Scheduler scheduler) {
        this.authControlService = authControlService;
        this.scheduler = scheduler;
    }

    /**
     * init auth info for a new member
     *
     * @param memberCredentialInfo
     */
    @Override
    public void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo) {
        LOGGER.info("void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo), memberCredentialInfo = {}", memberCredentialInfo);
        authControlService.initMemberAuthInfo(memberCredentialInfo);
    }

    /**
     * update authority base on member / update member-role-relations sync with trans / not support for manager
     *
     * @param memberRoleRelationParam
     * @return
     */
    @Override
    public AuthorityBaseOnRole updateAuthorityByMemberSync(MemberRoleRelationParam memberRoleRelationParam) {
        LOGGER.info("AuthorityBaseOnRole updateAuthorityByMemberSync(MemberRoleRelationParam memberRoleRelationParam), memberRoleRelationParam = {}", memberRoleRelationParam);
        return authControlService.updateAuthorityByMemberSync(memberRoleRelationParam);
    }

    /**
     * update member's auth by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    @Override
    public CompletableFuture<Boolean> refreshMemberRoleById(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("CompletableFuture<Boolean> refreshMemberRoleById(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}, operatorId = {}", memberId, roleId, operatorId);
        return authControlService.refreshMemberRoleById(memberId, roleId, operatorId).subscribeOn(scheduler).toFuture();
    }

    /**
     * query authority by access
     *
     * @param access
     * @return
     */
    @Override
    public CompletableFuture<AuthorityBaseOnRole> getAuthorityByAccess(Access access) {
        LOGGER.info("CompletableFuture<Authority> getAuthorityByAccess(Access access), access = {}", access);
        return just(access).subscribeOn(scheduler).flatMap(authControlService::getAuthorityMonoByAccess).toFuture();
    }

    /**
     * query authority by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public CompletableFuture<AuthorityBaseOnRole> getAuthorityByMemberId(Long memberId) {
        LOGGER.info("CompletableFuture<Authority> getAuthorityByMemberId(Long memberId), memberId = {}", memberId);
        return just(memberId).subscribeOn(scheduler).flatMap(authControlService::getAuthorityMonoByMemberId).toFuture();
    }

}
