package com.blue.secure.remote.provider;

import com.blue.base.model.base.Access;
import com.blue.secure.api.inter.RpcRoleService;
import com.blue.secure.service.inter.SecureService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;


/**
 * rpc role provider
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcRoleService.class, version = "1.0", methods = {
        @Method(name = "insertDefaultMemberRoleRelation", async = false),
        @Method(name = "updateMemberRoleByAccess", async = false),
        @Method(name = "updateMemberRoleById", async = false)
})
public class RpcRoleServiceProvider implements RpcRoleService {

    private static final Logger LOGGER = getLogger(RpcRoleServiceProvider.class);

    private final SecureService secureService;

    public RpcRoleServiceProvider(SecureService secureService) {
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
        secureService.insertDefaultMemberRoleRelation(memberId);
    }

    /**
     * update member's auth by access
     *
     * @param access
     * @param roleId
     * @return
     */
    @Override
    public void updateMemberRoleByAccess(Access access, Long roleId) {
        LOGGER.info("void updateMemberRoleByAccess(Access access, Long roleId), access = {}, roleId = {}", access, roleId);
        secureService.updateMemberRoleByAccess(access, roleId);
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
        secureService.updateMemberRoleById(memberId, roleId, operatorId);
    }
}
