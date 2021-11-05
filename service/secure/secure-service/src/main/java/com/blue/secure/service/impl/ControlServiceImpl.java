package com.blue.secure.service.impl;

import com.blue.base.model.base.Access;
import com.blue.base.model.base.IdentityParam;
import com.blue.base.model.exps.BlueException;
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
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static com.blue.base.common.base.Asserter.assertIdentityParamsAndReturnIdForOperate;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.DATA_NOT_EXIST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static com.blue.base.constant.base.SummerAttr.NON_VALUE_PARAM;
import static reactor.core.publisher.Mono.just;
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

    private final RedissonClient redissonClient;

    private final SystemAuthorityInfosRefreshProducer systemAuthorityInfosRefreshProducer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ControlServiceImpl(SecureService secureService, RoleService roleService, ResourceService resourceService, RoleResRelationService roleResRelationService,
                              MemberRoleRelationService memberRoleRelationService, RedissonClient redissonClient, SystemAuthorityInfosRefreshProducer systemAuthorityInfosRefreshProducer) {
        this.secureService = secureService;
        this.roleService = roleService;
        this.resourceService = resourceService;
        this.roleResRelationService = roleResRelationService;
        this.memberRoleRelationService = memberRoleRelationService;
        this.redissonClient = redissonClient;
        this.systemAuthorityInfosRefreshProducer = systemAuthorityInfosRefreshProducer;
    }

    private static final String ROLE_SYNC_KEY = "ROLE_SYNC";

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        Role role = roleService.getDefaultRole();
        if (role == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, DATA_NOT_EXIST.message);

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
    public Mono<RoleInfo> insertRole(RoleInsertParam roleInsertParam, Long operatorId) {
        LOGGER.info("RoleInfo insertRole(RoleInsertParam roleInsertParam, Long operatorId), roleInsertParam = {}, operatorId = {}", roleInsertParam, operatorId);

        return just(roleService.insertRole(roleInsertParam, operatorId))
                .doOnSuccess(ri -> {
                    LOGGER.info("roleInfo = {}", ri);
                    systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
                });
    }

    /**
     * update a exist role
     *
     * @param roleUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<RoleInfo> updateRole(RoleUpdateParam roleUpdateParam, Long operatorId) {
        LOGGER.info("RoleInfo updateRole(RoleUpdateParam roleUpdateParam, Long operatorId), roleUpdateParam = {}, operatorId = {}", roleUpdateParam, operatorId);

        return just(roleService.updateRole(roleUpdateParam, operatorId))
                .doOnSuccess(ri -> {
                    LOGGER.info("roleInfo = {}", ri);
                    systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
                });
    }

    /**
     * delete a exist role
     *
     * @param identityParam
     * @param operatorId
     * @return
     */
    @Override
    public RoleInfo deleteRole(IdentityParam identityParam, Long operatorId) {
        LOGGER.info("RoleInfo deleteRole(IdentityParam identityParam, Long operatorId), identityParam = {}, operatorId = {}", identityParam, operatorId);
        long roleId = assertIdentityParamsAndReturnIdForOperate(identityParam, operatorId);

        //TODO
        long relCount = roleResRelationService.countRelationByRoleId(roleId);


        return null;
    }

    /**
     * insert resource
     *
     * @param resourceInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<ResourceInfo> insertResource(ResourceInsertParam resourceInsertParam, Long operatorId) {
        LOGGER.info("ResourceInfo insertResource(ResourceInsertParam resourceInsertParam, Long operatorId), resourceInsertParam = {}, operatorId = {}", resourceInsertParam, operatorId);

        return just(resourceService.insertResource(resourceInsertParam, operatorId))
                .doOnSuccess(ri -> {
                    LOGGER.info("resourceInfo = {}", ri);
                    systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
                });
    }

    /**
     * update a exist resource
     *
     * @param resourceUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<ResourceInfo> updateResource(ResourceUpdateParam resourceUpdateParam, Long operatorId) {
        LOGGER.info("ResourceInfo updateResource(ResourceUpdateParam resourceUpdateParam, Long operatorId), resourceUpdateParam = {}, operatorId = {}", resourceUpdateParam, operatorId);

        return just(resourceService.updateResource(resourceUpdateParam, operatorId))
                .doOnSuccess(ri -> {
                    LOGGER.info("resourceInfo = {}", ri);
                    systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
                });
    }

}
