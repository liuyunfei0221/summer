package com.blue.marketing.remote.consumer;

import com.blue.member.api.inter.RpcMemberBasicService;
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
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcMemberBasicServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcMemberBasicServiceConsumer.class);

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
    public Mono<MemberBasicInfo> getMemberBasicInfoMonoByPrimaryKey(Long id) {
        LOGGER.info("Mono<MemberBasicInfo> getMemberBasicInfoMonoByPrimaryKey(Long id), id = {}", id);
        return fromFuture(rpcMemberBasicService.getMemberBasicInfoMonoByPrimaryKey(id)).subscribeOn(scheduler);
    }

    /**
     * select member basic by ids
     *
     * @param ids
     * @return
     */
    public Mono<List<MemberBasicInfo>> selectMemberBasicInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<MemberBasicInfo>> selectMemberBasicInfoMonoByIds(List<Long> ids), ids = {}", ids);
        return fromFuture(rpcMemberBasicService.selectMemberBasicInfoMonoByIds(ids)).subscribeOn(scheduler);
    }

    /**
     * get member basic info by member's phone
     *
     * @param phone
     * @return
     */
    public Mono<MemberBasicInfo> getMemberBasicInfoByPhone(String phone) {
        LOGGER.info("Mono<MemberBasicInfo> getMemberBasicInfoByPhone(String phone), phone = {}", phone);
        return fromFuture(rpcMemberBasicService.getMemberBasicInfoByPhone(phone)).subscribeOn(scheduler);
    }

    /**
     * get member basic info by member's email
     *
     * @param email
     * @return
     */
    public Mono<MemberBasicInfo> getMemberBasicInfoByEmail(String email) {
        LOGGER.info("Mono<MemberBasicInfo> selectMemberBasicInfoByEmail(String email), email = {}", email);
        return fromFuture(rpcMemberBasicService.getMemberBasicInfoByEmail(email)).subscribeOn(scheduler);
    }

}
