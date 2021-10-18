package com.blue.secure.remote.provider;

import com.blue.base.model.base.Access;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.api.inter.RpcRoleService;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.service.inter.MemberRoleRelationService;
import com.blue.secure.service.inter.RoleService;
import com.blue.secure.service.inter.SecureService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.base.common.reactive.BlueAsserter.notNullObjOpt;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.secure.converter.SecureModelConverters.ROLE_2_ROLE_INFO_CONVERTER;
import static reactor.util.Loggers.getLogger;


/**
 * rpc role provider
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam", "OptionalGetWithoutIsPresent"})
@DubboService(interfaceClass = RpcRoleService.class, version = "1.0", methods = {
        @Method(name = "insertDefaultMemberRoleRelation", async = false),
        @Method(name = "updateMemberRoleByAccess", async = false),
        @Method(name = "updateMemberRoleById", async = false)
})
public class RpcRoleServiceProvider implements RpcRoleService {

    private static final Logger LOGGER = getLogger(RpcRoleServiceProvider.class);

    private final SecureService secureService;

    private final RoleService roleService;

    private final MemberRoleRelationService memberRoleRelationService;

    public RpcRoleServiceProvider(SecureService secureService, RoleService roleService, MemberRoleRelationService memberRoleRelationService) {
        this.secureService = secureService;
        this.roleService = roleService;
        this.memberRoleRelationService = memberRoleRelationService;
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

    /**
     * get member's role info by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public RoleInfo getRoleInfoByMemberId(Long memberId) {
        LOGGER.info("RoleInfo getRoleInfoByMemberId(Long memberId), memberId = {}", memberId);

        Optional<Long> roleIdOpt = memberRoleRelationService.getRoleIdByMemberId(memberId);
        notNullObjOpt(roleIdOpt, new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "roleId can't be null"));

        Optional<Role> roleOpt = roleService.getRoleById(roleIdOpt.get());
        notNullObjOpt(roleOpt, new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "role can't be null"));

        return ROLE_2_ROLE_INFO_CONVERTER.apply(roleOpt.get());
    }

    /**
     * get member's roles info by member ids
     *
     * @param memberIds
     * @return
     */
    @Override
    public List<RoleInfo> selectRoleInfoByMemberIds(List<Long> memberIds) {
        return null;
    }
}
