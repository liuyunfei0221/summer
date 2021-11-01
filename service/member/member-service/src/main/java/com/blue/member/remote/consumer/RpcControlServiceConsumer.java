package com.blue.member.remote.consumer;

import com.blue.secure.api.inter.RpcControlService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;


/**
 * rpc control consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam", "FieldCanBeLocal"})
@Component
public class RpcControlServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcControlServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-member"},
            methods = {
                    @Method(name = "insertDefaultMemberRoleRelation", async = false)
            })
    private RpcControlService rpcControlService;

    /**
     * assign default roles to member
     *
     * @param memberId
     */
    public void insertDefaultMemberRoleRelation(Long memberId) {
        LOGGER.info("void insertDefaultMemberRoleRelation(Long memberId), memberId = {}", memberId);
        rpcControlService.insertDefaultMemberRoleRelation(memberId);
    }

}
