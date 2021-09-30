package com.blue.secure.remote.consumer;

import com.blue.member.api.inter.RpcMemberService;
import com.blue.member.api.model.MemberBasicInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

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
            @Method(name = "getMemberBasicByPhone", async = true),
            @Method(name = "getMemberBasicByEmail", async = true)
    })
    private RpcMemberService rpcMemberService;

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
