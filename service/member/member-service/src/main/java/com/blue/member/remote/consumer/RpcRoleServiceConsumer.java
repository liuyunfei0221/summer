package com.blue.member.remote.consumer;

import com.blue.auth.api.inter.RpcRoleService;
import com.blue.auth.api.model.MemberRoleRelationInfo;
import com.blue.auth.api.model.RoleInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * rpc role consumer
 *
 * @author liuyunfei
 * @date 2021/11/1
 * @apiNote
 */

@SuppressWarnings({"JavaDoc", "unused"})
@Component
public class RpcRoleServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcRoleServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-member"},
            methods = {
                    @Method(name = "selectRoleInfoByMemberId", async = true),
                    @Method(name = "selectRoleInfoByMemberIds", async = true)
            })
    private RpcRoleService rpcRoleService;

    private final Scheduler scheduler;

    public RpcRoleServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * get member's role info by member id
     *
     * @param memberId
     * @return
     */
    public Mono<RoleInfo> selectRoleInfoByMemberId(Long memberId) {
        LOGGER.info("RoleInfo getRoleInfoByMemberId(Long memberId), memberId = {}", memberId);
        return fromFuture(rpcRoleService.selectRoleInfoByMemberId(memberId)).publishOn(scheduler);
    }

    /**
     * get member's roles info by member ids
     *
     * @param memberIds
     * @return
     */
    public Mono<List<MemberRoleRelationInfo>> selectRoleInfoByMemberIds(List<Long> memberIds) {
        LOGGER.info("List<MemberRoleRelationInfo> selectRoleInfoByMemberIds(List<Long> memberIds), memberIds = {}", memberIds);
        return fromFuture(rpcRoleService.selectRoleInfoByMemberIds(memberIds)).publishOn(scheduler);
    }

}
