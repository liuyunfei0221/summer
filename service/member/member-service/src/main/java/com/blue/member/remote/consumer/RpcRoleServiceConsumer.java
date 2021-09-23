package com.blue.member.remote.consumer;

import com.blue.secure.api.inter.RpcRoleService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;

import static reactor.util.Loggers.getLogger;


/**
 * 角色关联相关RPC接口
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam", "FieldCanBeLocal", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class RpcRoleServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcRoleServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-member"}, methods = {
            @Method(name = "insertDefaultMemberRoleRelation", async = false)
    })
    private RpcRoleService rpcRoleService;

    private final ExecutorService executorService;

    public RpcRoleServiceConsumer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * 为成员分配默认角色
     *
     * @param memberId
     */
    public void insertDefaultMemberRoleRelation(Long memberId) {
        LOGGER.info("void insertDefaultMemberRoleRelation(Long memberId), memberId = {}", memberId);
        rpcRoleService.insertDefaultMemberRoleRelation(memberId);
    }

}
