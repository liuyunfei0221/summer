package com.blue.secure.remote.consumer;

import com.blue.member.api.inter.RpcMemberService;
import com.blue.member.api.model.MemberBasicInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * rpc member reference
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcMemberServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcMemberServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-member"}, methods = {
            @Method(name = "getMemberBasicMonoByPrimaryKey", async = true),
            @Method(name = "selectMemberBasicMonoByIds", async = true),
            @Method(name = "getMemberBasicByPhone", async = true),
            @Method(name = "getMemberBasicByEmail", async = true)
    })
    private RpcMemberService rpcMemberService;

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    Mono<MemberBasicInfo> getMemberBasicMonoByPrimaryKey(Long id) {
        LOGGER.info("Mono<MemberBasicInfo> getMemberBasicMonoByPrimaryKey(Long id), id = {}", id);
        return fromFuture(rpcMemberService.getMemberBasicMonoByPrimaryKey(id));
    }

    /**
     * select member basic by ids
     *
     * @param ids
     * @return
     */
    Mono<List<MemberBasicInfo>> selectMemberBasicMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<MemberBasicInfo>> selectMemberBasicMonoByIds(List<Long> ids), ids = {}", ids);
        return fromFuture(rpcMemberService.selectMemberBasicMonoByIds(ids));
    }

    /**
     * get member basic info by member's phone
     *
     * @param phone
     * @return
     */
    public Mono<MemberBasicInfo> getMemberBasicByPhone(String phone) {
        LOGGER.info("Mono<MemberBasicInfo> getMemberBasicByPhone(String phone), phone = {}", phone);
        return fromFuture(rpcMemberService.getMemberBasicByPhone(phone));
    }


    /**
     * get member basic info by member's email
     *
     * @param email
     * @return
     */
    public Mono<MemberBasicInfo> getMemberBasicByEmail(String email) {
        LOGGER.info("Mono<MemberBasicInfo> getMemberBasicByEmail(String email), email = {}", email);
        return fromFuture(rpcMemberService.getMemberBasicByEmail(email));
    }

}
