package com.blue.member.remote.consumer;

import com.blue.auth.api.inter.RpcRoleService;
import com.blue.auth.api.model.MemberRoleInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc role consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class RpcRoleServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-auth"},
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
    public Mono<MemberRoleInfo> selectRoleInfoByMemberId(Long memberId) {
        return fromFuture(rpcRoleService.selectRoleInfoByMemberId(memberId)).subscribeOn(scheduler);
    }

    /**
     * get member's roles info by member ids
     *
     * @param memberIds
     * @return
     */
    public Mono<List<MemberRoleInfo>> selectRoleInfoByMemberIds(List<Long> memberIds) {
        return fromFuture(rpcRoleService.selectRoleInfoByMemberIds(memberIds)).subscribeOn(scheduler);
    }

}
