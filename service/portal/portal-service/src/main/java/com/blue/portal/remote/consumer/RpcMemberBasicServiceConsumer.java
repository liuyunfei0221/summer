package com.blue.portal.remote.consumer;

import com.blue.member.api.inter.RpcMemberBasicService;
import com.blue.member.api.model.MemberBasicInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
                    @Method(name = "getMemberBasicInfo", async = true),
                    @Method(name = "selectMemberBasicInfoByIds", async = true),
                    @Method(name = "getMemberBasicInfoByPhone", async = true),
                    @Method(name = "getMemberBasicInfoByEmail", async = true)
            })
    private RpcMemberBasicService rpcMemberBasicService;

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    public Mono<MemberBasicInfo> getMemberBasicInfo(Long id) {
        return fromFuture(rpcMemberBasicService.getMemberBasicInfo(id));
    }

    /**
     * select member basic by ids
     *
     * @param ids
     * @return
     */
    public Mono<List<MemberBasicInfo>> selectMemberBasicInfoByIds(List<Long> ids) {
        return fromFuture(rpcMemberBasicService.selectMemberBasicInfoByIds(ids));
    }

    /**
     * get member basic info by member's phone
     *
     * @param phone
     * @return
     */
    public Mono<MemberBasicInfo> getMemberBasicInfoByPhone(String phone) {
        return fromFuture(rpcMemberBasicService.getMemberBasicInfoByPhone(phone));
    }

    /**
     * get member basic info by member's email
     *
     * @param email
     * @return
     */
    public Mono<MemberBasicInfo> getMemberBasicInfoByEmail(String email) {
        return fromFuture(rpcMemberBasicService.getMemberBasicInfoByEmail(email));
    }

}
