package com.blue.secure.remote.provider;

import com.blue.secure.api.inter.RpcControlService;
import com.blue.secure.service.inter.ControlService;
import com.blue.secure.service.inter.SecureService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;


/**
 * rpc control provider
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcControlService.class, version = "1.0", methods = {
        @Method(name = "insertDefaultMemberRoleRelation", async = false),
        @Method(name = "updateMemberRoleByAccess", async = false),
        @Method(name = "updateMemberRoleById", async = false)
})
public class RpcControlServiceProvider implements RpcControlService {

    private static final Logger LOGGER = getLogger(RpcControlServiceProvider.class);

    private final ControlService controlService;

    private final SecureService secureService;

    public RpcControlServiceProvider(ControlService controlService, SecureService secureService) {
        this.controlService = controlService;
        this.secureService = secureService;
    }

    /**
     * assign default roles to member
     *
     * @param memberId
     */
    @Override
    public void insertDefaultMemberRoleRelation(Long memberId) {
        LOGGER.info("void insertDefaultMemberRoleRelation(Long memberId), memberId = {}", memberId);
        controlService.insertDefaultMemberRoleRelation(memberId);
    }

    /**
     * update member's auth by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    @Override
    public void updateMemberRoleById(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("void updateMemberRoleById(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}, operatorId = {}", memberId, roleId, operatorId);
        secureService.refreshMemberRoleById(memberId, roleId, operatorId);
    }

}
