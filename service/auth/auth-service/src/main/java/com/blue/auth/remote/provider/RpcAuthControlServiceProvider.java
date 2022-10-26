package com.blue.auth.remote.provider;

import com.blue.auth.api.inter.RpcAuthControlService;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.api.model.MemberRoleRelationInsertOrDeleteParam;
import com.blue.auth.api.model.MemberRoleRelationUpdateParam;
import com.blue.auth.service.inter.AuthControlService;
import com.blue.basic.model.common.Access;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;


/**
 * rpc auth control provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcAuthControlService.class,
        version = "1.0",
        methods = {
                @Method(name = "insertAuthorityByMemberSync", async = false, timeout = 60000, retries = 0),
                @Method(name = "updateAuthoritiesByMemberSync", async = false, timeout = 60000, retries = 0),
                @Method(name = "deleteAuthorityByMemberSync", async = false, timeout = 60000, retries = 0),
                @Method(name = "refreshMemberRoleById", async = false),
                @Method(name = "selectAuthorityByAccess", async = true),
                @Method(name = "selectAuthorityByMemberId", async = true)
        })
public class RpcAuthControlServiceProvider implements RpcAuthControlService {

    private final AuthControlService authControlService;

    public RpcAuthControlServiceProvider(AuthControlService authControlService) {
        this.authControlService = authControlService;
    }

    /**
     * add authority base on member / insert member-role-relations sync
     *
     * @param memberRoleRelationInsertOrDeleteParam
     * @return
     */
    @Override
    public AuthorityBaseOnRole insertAuthorityByMemberSync(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam) {
        return authControlService.insertAuthorityByMemberSync(memberRoleRelationInsertOrDeleteParam);
    }

    /**
     * update authority base on member / update member-role-relations sync with trans / not support for manager
     *
     * @param memberRoleRelationUpdateParam
     * @return
     */
    @Override
    public List<AuthorityBaseOnRole> updateAuthoritiesByMemberSync(MemberRoleRelationUpdateParam memberRoleRelationUpdateParam) {
        return authControlService.updateAuthoritiesByMemberSync(memberRoleRelationUpdateParam);
    }

    /**
     * delete authority base on member / delete member-role-relations sync
     *
     * @param memberRoleRelationInsertOrDeleteParam
     * @return
     */
    @Override
    public AuthorityBaseOnRole deleteAuthorityByMemberSync(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam) {
        return authControlService.deleteAuthorityByMemberSync(memberRoleRelationInsertOrDeleteParam);
    }

    /**
     * update member's auth by member id
     *
     * @param memberId
     * @param roleIds
     * @return
     */
    @Override
    public CompletableFuture<Boolean> refreshMemberRoleById(Long memberId, List<Long> roleIds) {
        return authControlService.refreshMemberRoleByIds(memberId, roleIds).toFuture();
    }

    /**
     * query authority by access
     *
     * @param access
     * @return
     */
    @Override
    public CompletableFuture<List<AuthorityBaseOnRole>> selectAuthorityByAccess(Access access) {
        return just(access).flatMap(authControlService::selectAuthoritiesMonoByAccess).toFuture();
    }

    /**
     * query authority by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public CompletableFuture<List<AuthorityBaseOnRole>> selectAuthorityByMemberId(Long memberId) {
        return just(memberId).flatMap(authControlService::selectAuthoritiesMonoByMemberId).toFuture();
    }

}
