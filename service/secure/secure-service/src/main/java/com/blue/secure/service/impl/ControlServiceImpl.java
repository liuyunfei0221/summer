package com.blue.secure.service.impl;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.model.base.Access;
import com.blue.secure.api.model.ResourceInfo;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.event.producer.SystemAuthorityInfosRefreshProducer;
import com.blue.secure.model.ResourceInsertParam;
import com.blue.secure.model.ResourceUpdateParam;
import com.blue.secure.model.RoleInsertParam;
import com.blue.secure.model.RoleUpdateParam;
import com.blue.secure.repository.entity.MemberRoleRelation;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.service.inter.*;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.function.Supplier;

import static com.blue.base.constant.base.CommonException.DATA_NOT_EXIST_EXP;
import static com.blue.base.constant.base.CommonException.INVALID_IDENTITY_EXP;
import static com.blue.base.constant.base.SummerAttr.NON_VALUE_PARAM;
import static reactor.util.Loggers.getLogger;

/**
 * config role,resource,relation
 *
 * @author liuyunfei
 * @date 2021/11/1
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class ControlServiceImpl implements ControlService {

    private static final Logger LOGGER = getLogger(ControlServiceImpl.class);

    private final SecureService secureService;

    private final RoleService roleService;

    private final ResourceService resourceService;

    private final RoleResRelationService roleResRelationService;

    private final MemberRoleRelationService memberRoleRelationService;

    private final SystemAuthorityInfosRefreshProducer systemAuthorityInfosRefreshProducer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ControlServiceImpl(SecureService secureService, RoleService roleService, ResourceService resourceService, RoleResRelationService roleResRelationService,
                              MemberRoleRelationService memberRoleRelationService, SystemAuthorityInfosRefreshProducer systemAuthorityInfosRefreshProducer) {
        this.secureService = secureService;
        this.roleService = roleService;
        this.resourceService = resourceService;
        this.roleResRelationService = roleResRelationService;
        this.memberRoleRelationService = memberRoleRelationService;
        this.systemAuthorityInfosRefreshProducer = systemAuthorityInfosRefreshProducer;
    }

    private static final Supplier<Long> TIME_STAMP_GETTER = CommonFunctions.TIME_STAMP_GETTER;

    /**
     * refresh resource key/info or role-resource-relation
     *
     * @return
     */
    @Override
    public void refreshSystemAuthorityInfos() {
        LOGGER.info("void refreshResourceKeyOrRelation()");
        secureService.refreshSystemAuthorityInfos();
    }

    /**
     * update member role info by access
     *
     * @param access
     * @param roleId
     * @return
     */
    @Override
    public void updateMemberRoleByAccess(Access access, Long roleId) {
        LOGGER.info("void updateMemberRoleByAccess(Access access, Long roleId), access = {}, roleId = {}", access, roleId);
        long memberId = access.getId();
        memberRoleRelationService.updateMemberRoleRelation(memberId, roleId, memberId);
        secureService.refreshMemberRoleByAccess(access, roleId);
    }

    /**
     * update member role info by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    @Override
    public void updateMemberRoleById(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("void updateMemberRoleById(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}", memberId, roleId);
        memberRoleRelationService.updateMemberRoleRelation(memberId, roleId, operatorId);
        secureService.refreshMemberRoleById(memberId, roleId, operatorId);
    }

    /**
     * set a default role to member
     *
     * @param memberId
     */
    @Override
    public void insertDefaultMemberRoleRelation(Long memberId) {
        LOGGER.info("void insertDefaultMemberRoleRelation(Long memberId), memberId = {}", memberId);
        if (memberId == null || memberId < 1L)
            throw INVALID_IDENTITY_EXP.exp;

        Role role = roleService.getDefaultRole();
        if (role == null)
            throw DATA_NOT_EXIST_EXP.exp;

        MemberRoleRelation memberRoleRelation = new MemberRoleRelation();
        long epochSecond = TIME_STAMP_GETTER.get();
        memberRoleRelation.setMemberId(memberId);
        memberRoleRelation.setRoleId(role.getId());
        memberRoleRelation.setCreateTime(epochSecond);
        memberRoleRelation.setUpdateTime(epochSecond);
        memberRoleRelation.setCreator(memberId);
        memberRoleRelation.setUpdater(memberId);

        memberRoleRelationService.insertMemberRoleRelation(memberRoleRelation);
    }

    @Override
    public void updateDefaultRole(Long id, Long operatorId) {
        LOGGER.info("void updateDefaultRole(Long id, Long operatorId), id = {}, operatorId = {}", id, operatorId);
        roleService.updateDefaultRole(id, operatorId);
    }

    @Override
    public RoleInfo insertRole(RoleInsertParam roleInsertParam, Long operatorId) {
        LOGGER.info("RoleInfo insertRole(RoleInsertParam roleInsertParam, Long operatorId), roleInsertParam = {}, operatorId = {}", roleInsertParam, operatorId);
        RoleInfo roleInfo = roleService.insertRole(roleInsertParam, operatorId);
        LOGGER.info("roleInfo = {}", roleInfo);

        systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
        return roleInfo;
    }

    /**
     * update a exist role
     *
     * @param roleUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    public RoleInfo updateRole(RoleUpdateParam roleUpdateParam, Long operatorId) {
        LOGGER.info("RoleInfo updateRole(RoleUpdateParam roleUpdateParam, Long operatorId), roleUpdateParam = {}, operatorId = {}", roleUpdateParam, operatorId);
        RoleInfo roleInfo = roleService.updateRole(roleUpdateParam, operatorId);
        LOGGER.info("roleInfo = {}", roleInfo);

        systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
        return roleInfo;
    }

    /**
     * insert resource
     *
     * @param resourceInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public ResourceInfo insertResource(ResourceInsertParam resourceInsertParam, Long operatorId) {
        LOGGER.info("ResourceInfo insertResource(ResourceInsertParam resourceInsertParam, Long operatorId), resourceInsertParam = {}, operatorId = {}", resourceInsertParam, operatorId);
        ResourceInfo resourceInfo = resourceService.insertResource(resourceInsertParam, operatorId);
        LOGGER.info("resourceInfo = {}", resourceInfo);

        systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
        return resourceInfo;
    }

    /**
     * update a exist resource
     *
     * @param resourceUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    public ResourceInfo updateResource(ResourceUpdateParam resourceUpdateParam, Long operatorId) {
        LOGGER.info("ResourceInfo updateResource(ResourceUpdateParam resourceUpdateParam, Long operatorId), resourceUpdateParam = {}, operatorId = {}", resourceUpdateParam, operatorId);
        ResourceInfo resourceInfo = resourceService.updateResource(resourceUpdateParam, operatorId);
        LOGGER.info("resourceInfo = {}", resourceInfo);

        systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
        return resourceInfo;
    }

}
