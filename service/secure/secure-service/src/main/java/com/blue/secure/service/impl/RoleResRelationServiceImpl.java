package com.blue.secure.service.impl;

import com.blue.base.model.base.IdentityParam;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.api.model.AuthorityBaseOnRole;
import com.blue.secure.api.model.ResourceInfo;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.model.*;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.repository.entity.RoleResRelation;
import com.blue.secure.repository.mapper.RoleResRelationMapper;
import com.blue.secure.service.inter.ResourceService;
import com.blue.secure.service.inter.RoleResRelationService;
import com.blue.secure.service.inter.RoleService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Objects;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.Asserter.*;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.*;
import static com.blue.base.constant.base.SyncKey.RESOURCES_SYNC;
import static com.blue.secure.converter.SecureModelConverters.*;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;


/**
 * role resource relation service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class RoleResRelationServiceImpl implements RoleResRelationService {

    private static final Logger LOGGER = getLogger(RoleResRelationServiceImpl.class);

    private final RoleResRelationMapper roleResRelationMapper;

    private final RoleService roleService;

    private final ResourceService resourceService;

    private final RedissonClient redissonClient;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RoleResRelationServiceImpl(RoleResRelationMapper roleResRelationMapper, RoleService roleService, ResourceService resourceService, RedissonClient redissonClient) {
        this.roleResRelationMapper = roleResRelationMapper;
        this.roleService = roleService;
        this.resourceService = resourceService;
        this.redissonClient = redissonClient;
    }

    /**
     * get authority base on role by role id
     *
     * @param roleId
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnRole> selectAuthorityMonoByRoleId(Long roleId) {
        LOGGER.info("Mono<AuthorityBaseOnRole> getAuthorityMonoByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return roleService.getRoleMonoById(roleId)
                .flatMap(roleOpt ->
                        roleOpt.map(role ->
                                        just(ROLE_2_ROLE_INFO_CONVERTER.apply(role)))
                                .orElseGet(() -> {
                                    LOGGER.error("role info doesn't exist, roleId = {}", roleId);
                                    return error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, DATA_NOT_EXIST.message));
                                })
                ).flatMap(roleInfo ->
                        this.selectResIdsMonoByRoleId(roleId)
                                .flatMap(resourceService::selectResourceMonoByIds)
                                .flatMap(resources -> just(resources.stream().map(RESOURCE_2_RESOURCE_INFO_CONVERTER).collect(toList())))
                                .flatMap(resourceInfos -> just(new AuthorityBaseOnRole(roleInfo, resourceInfos)))
                );
    }

    /**
     * get authority base on resource by res id
     *
     * @param resId
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnResource> selectAuthorityMonoByResId(Long resId) {
        LOGGER.info("Mono<AuthorityBaseOnResource> getAuthorityMonoByResId(Long resId), resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return resourceService.getResourceMonoById(resId)
                .flatMap(resOpt ->
                        resOpt.map(res ->
                                        just(RESOURCE_2_RESOURCE_INFO_CONVERTER.apply(res)))
                                .orElseGet(() -> {
                                    LOGGER.error("res info doesn't exist, resId = {}", resId);
                                    return error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, DATA_NOT_EXIST.message));
                                })
                ).flatMap(resourceInfo ->
                        this.selectRoleIdsMonoByResId(resId)
                                .flatMap(roleService::selectRoleMonoByIds)
                                .flatMap(roles -> just(roles.stream().map(ROLE_2_ROLE_INFO_CONVERTER).collect(toList())))
                                .flatMap(roleInfos -> just(new AuthorityBaseOnResource(resourceInfo, roleInfos)))
                );
    }

    /**
     * select all role resource relation
     *
     * @return
     */
    @Override
    public Mono<List<RoleResRelation>> selectRelation() {
        LOGGER.info("Mono<List<RoleResRelation>> selectRelation()");
        return just(roleResRelationMapper.select());
    }

    /**
     * select resources ids by role id
     *
     * @return
     */
    @Override
    public Mono<List<Long>> selectResIdsMonoByRoleId(Long roleId) {
        LOGGER.info("Mono<List<Long>> selectResIdsMonoByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return just(roleResRelationMapper.selectResIdsByRoleId(roleId));
    }

    /**
     * select resources by role id
     *
     * @param roleId
     * @return
     */
    @Override
    public Mono<List<Resource>> selectResMonoByRoleId(Long roleId) {
        LOGGER.info("Mono<List<Resource>> selectResMonoByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return this.selectResIdsMonoByRoleId(roleId)
                .flatMap(ids -> {
                    LOGGER.info("Mono<List<Resource>> selectResMonoByRoleId(Long roleId), ids = {}", ids);
                    return isValidIdentities(ids) ? resourceService.selectResourceMonoByIds(ids) : just(emptyList());
                });
    }

    /**
     * select role ids by resource id
     *
     * @param resId
     * @return
     */
    @Override
    public Mono<List<Long>> selectRoleIdsMonoByResId(Long resId) {
        LOGGER.info("Mono<List<Long>> selectRoleIdsMonoByResId(Long resId), resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return just(roleResRelationMapper.selectRoleIdsByResId(resId));
    }

    /**
     * select role by resource id
     *
     * @param resId
     * @return
     */
    @Override
    public Mono<List<Role>> selectRoleMonoByResId(Long resId) {
        LOGGER.info("Mono<List<Role>> selectRoleMonoByResId(Long resId), resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return this.selectRoleIdsMonoByResId(resId)
                .flatMap(ids -> {
                    LOGGER.info("Mono<List<Role>> selectRoleMonoByResId(Long resId), resId = {}, ids = {}", ids);
                    return isEmpty(ids) ? just(emptyList()) : roleService.selectRoleMonoByIds(ids);
                });
    }

    /**
     * select relation by role id
     *
     * @param roleId
     * @return
     */
    @Override
    public Mono<List<RoleResRelation>> selectRelationByRoleId(Long roleId) {
        LOGGER.info("Mono<List<RoleResRelation>> selectRelationByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return just(roleResRelationMapper.selectByRoleId(roleId));
    }

    /**
     * select relation by limit and role id
     *
     * @param roleId
     * @param limit
     * @param rows
     * @return
     */
    @Override
    public List<RoleResRelation> selectRelationByRowsAndRoleId(Long roleId, Long limit, Long rows) {
        LOGGER.info("List<RoleResRelation> selectRelationByRowsAndRoleId(Long roleId, Long limit, Long rows), roleId = {}, limit = {}, rows = {}", roleId, limit, rows);
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid roleId");
        if (isInvalidLimit(limit))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid limit");
        if (isInvalidRows(rows))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid rows");

        return roleResRelationMapper.selectRelationByRowsAndRoleId(roleId, limit, rows);
    }

    /**
     * count relation by role id
     *
     * @param roleId
     * @return
     */
    @Override
    public long countRelationByRoleId(Long roleId) {
        LOGGER.info("long countRelationByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return roleResRelationMapper.countByRoleId(roleId);
    }

    /**
     * select relation by limit and resource id
     *
     * @param resId
     * @param limit
     * @param rows
     * @return
     */
    @Override
    public List<RoleResRelation> selectRelationByRowsAndResId(Long resId, Long limit, Long rows) {
        LOGGER.info("List<RoleResRelation> selectRelationByRowsAndResId(Long resId, Long limit, Long rows), resId = {}, limit = {}, rows = {}", resId, limit, rows);
        if (isInvalidIdentity(resId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid resId");
        if (isInvalidLimit(limit))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid limit");
        if (isInvalidRows(rows))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid rows");

        return roleResRelationMapper.selectRelationByRowsAndResId(resId, limit, rows);
    }

    /**
     * count relation by resource id
     *
     * @param resId
     * @return
     */
    @Override
    public long countRelationByResId(Long resId) {
        LOGGER.info("long countRelationByResId(Long resId), resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return roleResRelationMapper.countByResId(resId);
    }

    /**
     * select relation by resource id
     *
     * @param resId
     * @return
     */
    @Override
    public Mono<List<RoleResRelation>> selectRelationByResId(Long resId) {
        LOGGER.info("Mono<List<RoleResRelation>> selectRelationByResId(Long resId), resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return just(roleResRelationMapper.selectByResId(resId));
    }

    /**
     * select relation by ids
     *
     * @param roleIds
     * @return
     */
    @Override
    public Mono<List<RoleResRelation>> selectRelationByRoleIds(List<Long> roleIds) {
        LOGGER.info("Mono<List<RoleResRelation>> selectRelationByRoleIds(List<Long> roleIds), roleIds = {}", roleIds);

        return isValidIdentities(roleIds) ? just(allotByMax(roleIds, (int) DB_SELECT.value, false)
                .stream().map(roleResRelationMapper::selectByRoleIds)
                .flatMap(List::stream)
                .collect(toList()))
                :
                just(emptyList());
    }

    /**
     * select relation by resource ids
     *
     * @param resIds
     * @return
     */
    @Override
    public Mono<List<RoleResRelation>> selectRelationByResIds(List<Long> resIds) {
        LOGGER.info("Mono<List<RoleResRelation>> selectRelationByResIds(List<Long> resIds), resIds = {}", resIds);

        return isValidIdentities(resIds) ? just(allotByMax(resIds, (int) DB_SELECT.value, false)
                .stream().map(roleResRelationMapper::selectByResIds)
                .flatMap(List::stream)
                .collect(toList()))
                :
                just(emptyList());
    }

    /**
     * get default role
     *
     * @return
     */
    @Override
    public Role getDefaultRole() {
        LOGGER.info("Role getDefaultRole()");
        return roleService.getDefaultRole();
    }

    /**
     * update default role by role id
     *
     * @param id
     * @param operatorId
     */
    @Override
    public void updateDefaultRole(Long id, Long operatorId) {
        LOGGER.info("void updateDefaultRole(Long id, Long operatorId), id = {}, operatorId = {}", id, operatorId);

        RLock lock = redissonClient.getLock(RESOURCES_SYNC.key);
        lock.lock();

        try {
            roleService.updateDefaultRole(id, operatorId);
        } catch (Exception e) {
            LOGGER.error("void updateDefaultRole(Long id) failed, id = {}, e = {}", id, e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                LOGGER.error("lock.unlock() fail, e = {0}", e);
            }
        }
    }

    /**
     * insert a new role
     *
     * @param roleInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public RoleInfo insertRole(RoleInsertParam roleInsertParam, Long operatorId) {
        LOGGER.info("RoleInfo insertRole(RoleInsertParam roleInsertParam, Long operatorId), roleInsertParam = {}, operatorId = {}", roleInsertParam, operatorId);

        RLock lock = redissonClient.getLock(RESOURCES_SYNC.key);
        try {
            lock.lock();
            return roleService.insertRole(roleInsertParam, operatorId);
        } catch (Exception e) {
            LOGGER.error("lock on insertRole failed, e = {0}", e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                LOGGER.error("lock.unlock() failed, e = {}", e);
            }
        }
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

        RLock lock = redissonClient.getLock(RESOURCES_SYNC.key);
        try {
            lock.lock();
            return roleService.updateRole(roleUpdateParam, operatorId);
        } catch (Exception e) {
            LOGGER.error("lock on updateRole failed, e = {0}", e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                LOGGER.error("lock.unlock() failed, e = {}", e);
            }
        }
    }

    /**
     * delete a exist role
     *
     * @param identityParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public RoleInfo deleteRole(IdentityParam identityParam, Long operatorId) {
        LOGGER.info("RoleInfo deleteRole(IdentityParam identityParam, Long operatorId), identityParam = {}, operatorId = {}", identityParam, operatorId);
        long id = assertIdentityParamsAndReturnIdForOperate(identityParam, operatorId);

        RLock lock = redissonClient.getLock(RESOURCES_SYNC.key);
        try {
            lock.lock();

            if (this.countRelationByRoleId(id) > 0L)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "role-res-relations count > 0");

            this.deleteRelationByRoleId(id);
            return roleService.deleteRoleById(id);
        } catch (Exception e) {
            LOGGER.error("lock on deleteRole failed, e = {0}", e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                LOGGER.error("lock.unlock() failed, e = {}", e);
            }
        }
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

        RLock lock = redissonClient.getLock(RESOURCES_SYNC.key);
        try {
            lock.lock();
            return resourceService.insertResource(resourceInsertParam, operatorId);
        } catch (Exception e) {
            LOGGER.error("lock on insertResource failed, e = {0}", e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                LOGGER.error("lock.unlock() failed, e = {}", e);
            }
        }
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

        RLock lock = redissonClient.getLock(RESOURCES_SYNC.key);
        try {
            lock.lock();
            return resourceService.updateResource(resourceUpdateParam, operatorId);
        } catch (Exception e) {
            LOGGER.error("lock on updateResource failed, e = {}", e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                LOGGER.error("lock.unlock() failed, e = {}", e);
            }
        }
    }

    /**
     * delete a exist resource
     *
     * @param identityParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public ResourceInfo deleteResource(IdentityParam identityParam, Long operatorId) {
        LOGGER.info("ResourceInfo deleteResource(IdentityParam identityParam, Long operatorId), identityParam = {}, operatorId = {}", identityParam, operatorId);
        long id = assertIdentityParamsAndReturnIdForOperate(identityParam, operatorId);

        RLock lock = redissonClient.getLock(RESOURCES_SYNC.key);
        try {
            lock.lock();

            if (this.countRelationByResId(id) > 0L)
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "role-res-relations count > 0");

            this.deleteRelationByResId(id);
            ResourceInfo resourceInfo = resourceService.deleteResourceById(id);

            LOGGER.info("ResourceInfo deleteResource(IdentityParam identityParam, Long operatorId), resourceInfo = {}", resourceInfo);
            return resourceInfo;
        } catch (Exception e) {
            LOGGER.error("lock on deleteResource failed, e = {0}", e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                LOGGER.error("lock.unlock() failed, e = {}", e);
            }
        }
    }

    /**
     * insert relation
     *
     * @param roleResRelation
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public void insertRelation(RoleResRelation roleResRelation) {
        LOGGER.info("void insertRelation(RoleResRelation roleResRelation), roleResRelation = {}", roleResRelation);
        if (roleResRelation == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);

        roleResRelationMapper.insert(roleResRelation);
    }

    /**
     * insert relations
     *
     * @param roleResRelations
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public void insertRelationBatch(List<RoleResRelation> roleResRelations) {
        LOGGER.info("void insertRelationBatch(List<RoleResRelation> roleResRelations), roleResRelations = {}", roleResRelations);
        if (isEmpty(roleResRelations))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);

        roleResRelationMapper.insertBatch(roleResRelations.stream().filter(Objects::nonNull).collect(toList()));
    }

    /**
     * delete relation by role id
     *
     * @param roleId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public void deleteRelationByRoleId(Long roleId) {
        LOGGER.info("void deleteRelationByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        int count = roleResRelationMapper.deleteByRoleId(roleId);
        LOGGER.info("void deleteRelationByRoleId(Long roleId), count = {}", count);
    }

    /**
     * delete relation by resource id
     *
     * @param resId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public void deleteRelationByResId(Long resId) {
        LOGGER.info("void deleteRelationByResId(Long resId), resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        RLock lock = redissonClient.getLock(RESOURCES_SYNC.key);
        lock.lock();

        try {
            int count = roleResRelationMapper.deleteByResId(resId);
            LOGGER.info("void deleteRelationByResId(Long resId), count = {}", count);
        } catch (Exception e) {
            LOGGER.error("void deleteRelationByResId(Long resId) failed, resId = {}, e = {}", resId, e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                LOGGER.error("lock.unlock() fail, e = {0}", e);
            }
        }
    }

}
