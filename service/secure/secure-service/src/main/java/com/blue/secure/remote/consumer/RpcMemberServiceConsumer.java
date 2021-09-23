package com.blue.secure.remote.consumer;

import com.blue.member.api.inter.RpcMemberService;
import com.blue.member.api.model.MemberBasicInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.converterFutureToMono;
import static reactor.util.Loggers.getLogger;

/**
 * 成员相关RPC接口
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class RpcMemberServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcMemberServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-member"}, methods = {
            @Method(name = "getMemberBasicByPhone", async = true),
            @Method(name = "getMemberBasicByEmail", async = true)
    })
    private RpcMemberService rpcMemberService;

    private final ExecutorService executorService;

    public RpcMemberServiceConsumer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * 根据手机号获取成员关键信息
     *
     * @param phone
     * @return
     */
    public Mono<MemberBasicInfo> getMemberBasicByPhone(String phone) {
        LOGGER.info("Mono<MemberBasicInfo> getMemberBasicByPhone(String phone), phone = {}", phone);
        return converterFutureToMono(rpcMemberService.getMemberBasicByPhone(phone), executorService);
    }


    /**
     * 根据邮箱地址获取成员关键信息
     *
     * @param email
     * @return
     */
    public Mono<MemberBasicInfo> getMemberBasicByEmail(String email) {
        LOGGER.info("Mono<MemberBasicInfo> getMemberBasicByEmail(String email), email = {}", email);
        return converterFutureToMono(rpcMemberService.getMemberBasicByEmail(email), executorService);
    }

}
