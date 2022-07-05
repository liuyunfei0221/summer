package com.blue.risk.remote.consumer;

import com.blue.member.api.inter.RpcMemberBasicService;
import com.blue.member.api.model.MemberBasicInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc member reference
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcMemberBasicServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-member"},
            methods = {
                    @Method(name = "getMemberBasicInfoMonoByPrimaryKey", async = true),
                    @Method(name = "selectMemberBasicInfoMonoByIds", async = true),
                    @Method(name = "getMemberBasicInfoByPhone", async = true),
                    @Method(name = "getMemberBasicInfoByEmail", async = true)
            })
    private RpcMemberBasicService rpcMemberBasicService;

    private final Scheduler scheduler;

    public RpcMemberBasicServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    Mono<MemberBasicInfo> getMemberBasicInfoMonoByPrimaryKey(Long id) {
        return fromFuture(rpcMemberBasicService.getMemberBasicInfoMonoByPrimaryKey(id)).publishOn(scheduler);
    }

    /**
     * select member basic by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberBasicInfo>> selectMemberBasicInfoMonoByIds(List<Long> ids) {
        return fromFuture(rpcMemberBasicService.selectMemberBasicInfoMonoByIds(ids)).publishOn(scheduler);
    }

    /**
     * get member basic info by member's phone
     *
     * @param phone
     * @return
     */
    public Mono<MemberBasicInfo> getMemberBasicInfoByPhone(String phone) {
        return fromFuture(rpcMemberBasicService.getMemberBasicInfoByPhone(phone)).publishOn(scheduler);
    }

    /**
     * get member basic info by member's email
     *
     * @param email
     * @return
     */
    public Mono<MemberBasicInfo> getMemberBasicInfoByEmail(String email) {
        return fromFuture(rpcMemberBasicService.getMemberBasicInfoByEmail(email)).publishOn(scheduler);
    }

}
