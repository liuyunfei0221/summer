package com.blue.auth.remote.provider;

import com.blue.auth.api.inter.RpcControlService;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.api.model.MemberCredentialInfo;
import com.blue.auth.api.model.MemberRoleRelationParam;
import com.blue.auth.service.inter.ControlService;
import com.blue.base.model.base.Access;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;


/**
 * rpc control provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcControlService.class,
        version = "1.0",
        methods = {
                @Method(name = "initMemberAuthInfo", async = false, timeout = 60000, retries = 0),
                @Method(name = "updateAuthorityByMemberSync", async = false, timeout = 60000, retries = 1),
                @Method(name = "refreshMemberRoleById", async = false),
                @Method(name = "getAuthorityByAccess", async = true),
                @Method(name = "getAuthorityByMemberId", async = true)
        })
public class RpcControlServiceProvider implements RpcControlService {

    private static final Logger LOGGER = getLogger(RpcControlServiceProvider.class);

    private final ControlService controlService;

    private final Scheduler scheduler;

    public RpcControlServiceProvider(ControlService controlService, Scheduler scheduler) {
        this.controlService = controlService;
        this.scheduler = scheduler;
    }

    /**
     * init auth infos for a new member
     *
     * @param memberCredentialInfo
     */
    @Override
    public void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo) {
        LOGGER.info("void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo), memberCredentialInfo = {}", memberCredentialInfo);
        controlService.initMemberAuthInfo(memberCredentialInfo);
    }

    /**
     * update authority base on member / update member-role-relations sync with trans / not support for manager
     *
     * @param memberRoleRelationParam
     */
    @Override
    public void updateAuthorityByMemberSync(MemberRoleRelationParam memberRoleRelationParam) {
        LOGGER.info("void updateAuthorityByMemberSync(MemberRoleRelationParam memberRoleRelationParam), memberRoleRelationParam = {}", memberRoleRelationParam);
        controlService.updateAuthorityByMemberSync(memberRoleRelationParam);
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
        return controlService.refreshMemberRoleById(memberId, roleId, operatorId).subscribeOn(scheduler).toFuture();
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
        return just(access).subscribeOn(scheduler).flatMap(controlService::getAuthorityMonoByAccess).toFuture();
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
        return just(memberId).subscribeOn(scheduler).flatMap(controlService::getAuthorityMonoByMemberId).toFuture();
    }

}
