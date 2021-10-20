package com.blue.member.remote.consumer;

import com.blue.base.model.exps.BlueException;
import com.blue.secure.api.inter.RpcRoleService;
import com.blue.secure.api.model.MemberRoleRelationInfo;
import com.blue.secure.api.model.RoleInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;


/**
 * rpc role consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam", "FieldCanBeLocal", "SpringJavaInjectionPointsAutowiringInspection", "AliControlFlowStatementWithoutBraces"})
@Component
public class RpcRoleServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcRoleServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-member"},
            methods = {
                    @Method(name = "insertDefaultMemberRoleRelation", async = false),
                    @Method(name = "getRoleInfoByMemberId", async = true),
                    @Method(name = "selectRoleInfoByMemberIds", async = true)
            })
    private RpcRoleService rpcRoleService;

    private final ExecutorService executorService;

    public RpcRoleServiceConsumer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * assign default roles to member
     *
     * @param memberId
     */
    public void insertDefaultMemberRoleRelation(Long memberId) {
        LOGGER.info("void insertDefaultMemberRoleRelation(Long memberId), memberId = {}", memberId);
        rpcRoleService.insertDefaultMemberRoleRelation(memberId);
    }

    /**
     * get member's role info by member id
     *
     * @param memberId
     * @return
     */
    public Mono<RoleInfo> getRoleInfoByMemberId(Long memberId) {
        LOGGER.info("RoleInfo getRoleInfoByMemberId(Long memberId), memberId = {}", memberId);
        if (memberId == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "memberId can't be null");
        return fromFuture(rpcRoleService.getRoleInfoByMemberId(memberId));
    }

    /**
     * get member's roles info by member ids
     *
     * @param memberIds
     * @return
     */
    public Mono<List<MemberRoleRelationInfo>> selectRoleInfoByMemberIds(List<Long> memberIds) {
        LOGGER.info("List<MemberRoleRelationInfo> selectRoleInfoByMemberIds(List<Long> memberIds), memberIds = {}", memberIds);
        if (isEmpty(memberIds))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "memberIds can't be empty");

        return fromFuture(rpcRoleService.selectRoleInfoByMemberIds(memberIds));
    }

}
