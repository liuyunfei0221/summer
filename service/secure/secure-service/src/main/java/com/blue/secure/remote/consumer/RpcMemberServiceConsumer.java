package com.blue.secure.remote.consumer;

import com.blue.member.api.inter.RpcMemberService;
import com.blue.member.api.model.MemberBasicInfo;
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
 * rpc member reference
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class RpcMemberServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcMemberServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-member"}, methods = {
            @Method(name = "selectMemberBasicMonoByPrimaryKey", async = true),
            @Method(name = "selectMemberBasicMonoByIds", async = true),
            @Method(name = "selectMemberBasicByPhone", async = true),
            @Method(name = "selectMemberBasicByEmail", async = true)
    })
    private RpcMemberService rpcMemberService;

    private final Scheduler scheduler;

    public RpcMemberServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    Mono<MemberBasicInfo> selectMemberBasicMonoByPrimaryKey(Long id) {
        LOGGER.info("Mono<MemberBasicInfo> selectMemberBasicMonoByPrimaryKey(Long id), id = {}", id);
        return fromFuture(rpcMemberService.selectMemberBasicMonoByPrimaryKey(id)).publishOn(scheduler);
    }

    /**
     * select member basic by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberBasicInfo>> selectMemberBasicMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<MemberBasicInfo>> selectMemberBasicMonoByIds(List<Long> ids), ids = {}", ids);
        return fromFuture(rpcMemberService.selectMemberBasicMonoByIds(ids)).publishOn(scheduler);
    }

    /**
     * get member basic info by member's phone
     *
     * @param phone
     * @return
     */
    public Mono<MemberBasicInfo> selectMemberBasicByPhone(String phone) {
        LOGGER.info("Mono<MemberBasicInfo> selectMemberBasicByPhone(String phone), phone = {}", phone);
        return fromFuture(rpcMemberService.selectMemberBasicByPhone(phone)).publishOn(scheduler);
    }


    /**
     * get member basic info by member's email
     *
     * @param email
     * @return
     */
    public Mono<MemberBasicInfo> selectMemberBasicByEmail(String email) {
        LOGGER.info("Mono<MemberBasicInfo> selectMemberBasicByEmail(String email), email = {}", email);
        return fromFuture(rpcMemberService.selectMemberBasicByEmail(email)).publishOn(scheduler);
    }

}
