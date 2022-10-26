package com.blue.member.remote.consumer;

import com.blue.auth.api.inter.RpcAuthControlService;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.basic.model.common.Access;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
                    @Method(name = "refreshMemberRoleById", async = true),
                    @Method(name = "selectAuthorityByAccess", async = true),
                    @Method(name = "selectAuthorityByMemberId", async = true)
            })
    private RpcAuthControlService rpcAuthControlService;

    /**
     * update member's auth by member id
     *
     * @param memberId
     * @param roleIds
     * @return
     */
    Mono<Boolean> refreshMemberRoleById(Long memberId, List<Long> roleIds) {
        return fromFuture(rpcAuthControlService.refreshMemberRoleById(memberId, roleIds));
    }

    /**
     * query authority by access
     *
     * @param access
     * @return
     */
    public Mono<List<AuthorityBaseOnRole>> selectAuthorityByAccess(Access access) {
        return fromFuture(rpcAuthControlService.selectAuthorityByAccess(access));
    }

    /**
     * query authority by member id
     *
     * @param memberId
     * @return
     */
    public Mono<List<AuthorityBaseOnRole>> selectAuthorityByMemberId(Long memberId) {
        return fromFuture(rpcAuthControlService.selectAuthorityByMemberId(memberId));
    }

}
