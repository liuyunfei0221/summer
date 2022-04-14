package com.blue.auth.service.impl;

import com.blue.auth.converter.AuthModelConverters;
import com.blue.auth.repository.entity.Resource;
import com.blue.auth.repository.entity.Role;
import com.blue.auth.repository.entity.RoleResRelation;
import com.blue.auth.repository.mapper.RoleResRelationMapper;
import com.blue.auth.service.inter.ResourceService;
import com.blue.auth.service.inter.RoleResRelationService;
import com.blue.auth.service.inter.RoleService;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.auth.api.model.AuthorityBaseOnResource;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.api.model.ResourceInfo;
import com.blue.auth.api.model.RoleInfo;
import com.blue.auth.model.ResourceInsertParam;
import com.blue.auth.model.ResourceUpdateParam;
import com.blue.auth.model.RoleInsertParam;
import com.blue.auth.model.RoleUpdateParam;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.SyncKey.AUTHORITY_UPDATE_SYNC;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;


/**
 * role resource relation service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class RoleResRelationServiceImpl implements RoleResRelationService {

    private static final Logger LOGGER = getLogger(RoleResRelationServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private RoleResRelationMapper roleResRelationMapper;

    private final RoleService roleService;

    private final ResourceService resourceService;

    private final RedissonClient redissonClient;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RoleResRelationServiceImpl(BlueIdentityProcessor blueIdentityProcessor, RoleResRelationMapper roleResRelationMapper, RoleService roleService, ResourceService resourceService, RedissonClient redissonClient) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.roleResRelationMapper = roleResRelationMapper;
        this.roleService = roleService;
        this.resourceService = resourceService;
        this.redissonClient = redissonClient;
    }

    /**
     * is a relation exist?
     */
    private final Consumer<RoleResRelation> INSERT_RELATION_VALIDATOR = relation -> {
        if (relation == null)
            throw new BlueException(EMPTY_PARAM);

        Long id = relation.getId();
        if (isInvalidIdentity(id))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "id is invalid");

        Long roleId = relation.getRoleId();
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "roleId is invalid");

        Long resId = relation.getResId();
        if (isInvalidIdentity(resId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "resId is invalid");

        Long createTime = relation.getCreateTime();
        Long updateTime = relation.getUpdateTime();
        if (createTime == null || updateTime == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "createTime or updateTime is invalid");

        Long creator = relation.getCreator();
        Long updater = relation.getUpdater();
        if (isInvalidIdentity(creator) || isInvalidIdentity(updater))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "creator or updater is invalid");

        if (isNotNull(roleResRelationMapper.selectExistByResIdAndRoleId(resId, roleId)))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "The data base res and role already exists");
    };

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
            throw new BlueException(INVALID_IDENTITY);

        return roleService.getRoleMonoById(roleId)
                .flatMap(roleOpt ->
                        roleOpt.map(role ->
                                        just(AuthModelConverters.ROLE_2_ROLE_INFO_CONVERTER.apply(role)))
                                .orElseGet(() -> {
                                    LOGGER.error("role info doesn't exist, roleId = {}", roleId);
                                    return error(() -> new BlueException(DATA_NOT_EXIST));
                                })
                ).flatMap(roleInfo ->
                        this.selectResIdsMonoByRoleId(roleId)
                                .flatMap(resourceService::selectResourceMonoByIds)
                                .flatMap(resources -> just(resources.stream().map(AuthModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER).collect(toList())))
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
            throw new BlueException(INVALID_IDENTITY);

        return resourceService.getResourceMonoById(resId)
                .flatMap(resOpt ->
                        resOpt.map(res ->
                                        just(AuthModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER.apply(res)))
                                .orElseGet(() -> {
                                    LOGGER.error("res info doesn't exist, resId = {}", resId);
                                    return error(() -> new BlueException(DATA_NOT_EXIST));
                                })
                ).flatMap(resourceInfo ->
                        this.selectRoleIdsMonoByResId(resId)
                                .flatMap(roleService::selectRoleMonoByIds)
                                .flatMap(roles -> just(roles.stream().map(AuthModelConverters.ROLE_2_ROLE_INFO_CONVERTER).collect(toList())))
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
            throw new BlueException(INVALID_IDENTITY);

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
            throw new BlueException(INVALID_IDENTITY);

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
            throw new BlueException(INVALID_IDENTITY);

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
            throw new BlueException(INVALID_IDENTITY);

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
            throw new BlueException(INVALID_IDENTITY);

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
            throw new BlueException(INVALID_IDENTITY);

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
            throw new BlueException(INVALID_IDENTITY);

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
            throw new BlueException(INVALID_IDENTITY);

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

        RLock lock = redissonClient.getLock(AUTHORITY_UPDATE_SYNC.key);
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

        RLock lock = redissonClient.getLock(AUTHORITY_UPDATE_SYNC.key);
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

        RLock lock = redissonClient.getLock(AUTHORITY_UPDATE_SYNC.key);
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
     * @param id
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public RoleInfo deleteRole(Long id, Long operatorId) {
        LOGGER.info("RoleInfo deleteRole(Long id, Long operatorId), id = {}, operatorId = {}", id, operatorId);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        RLock lock = redissonClient.getLock(AUTHORITY_UPDATE_SYNC.key);
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

        RLock lock = redissonClient.getLock(AUTHORITY_UPDATE_SYNC.key);
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
                LOGGER.warn("insertResource, lock.unlock() failed, e = {}", e);
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

        RLock lock = redissonClient.getLock(AUTHORITY_UPDATE_SYNC.key);
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
     * @param id
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public ResourceInfo deleteResource(Long id, Long operatorId) {
        LOGGER.info("ResourceInfo deleteResource(Long id, Long operatorId), id = {}, operatorId = {}", id, operatorId);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        RLock lock = redissonClient.getLock(AUTHORITY_UPDATE_SYNC.key);
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
    public int insertRelation(RoleResRelation roleResRelation) {
        LOGGER.info("void insertRelation(RoleResRelation roleResRelation), roleResRelation = {}", roleResRelation);
        if (roleResRelation == null)
            throw new BlueException(EMPTY_PARAM);

        INSERT_RELATION_VALIDATOR.accept(roleResRelation);
        return roleResRelationMapper.insert(roleResRelation);
    }

    /**
     * insert relations
     *
     * @param roleResRelations
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public int insertRelationBatch(List<RoleResRelation> roleResRelations) {
        LOGGER.info("void insertRelationBatch(List<RoleResRelation> roleResRelations), roleResRelations = {}", roleResRelations);
        if (isEmpty(roleResRelations))
            throw new BlueException(EMPTY_PARAM);

        return roleResRelationMapper.insertBatch(roleResRelations.stream().filter(Objects::nonNull)
                .peek(INSERT_RELATION_VALIDATOR).collect(toList()));
    }

    /**
     * delete relation by role id
     *
     * @param roleId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public int deleteRelationByRoleId(Long roleId) {
        LOGGER.info("void deleteRelationByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(INVALID_IDENTITY);

        int count = roleResRelationMapper.deleteByRoleId(roleId);
        LOGGER.info("void deleteRelationByRoleId(Long roleId), count = {}", count);
        return count;
    }

    /**
     * delete relation by resource id
     *
     * @param resId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public int deleteRelationByResId(Long resId) {
        LOGGER.info("void deleteRelationByResId(Long resId), resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(INVALID_IDENTITY);

        RLock lock = redissonClient.getLock(AUTHORITY_UPDATE_SYNC.key);
        lock.lock();

        try {
            int count = roleResRelationMapper.deleteByResId(resId);
            LOGGER.info("void deleteRelationByResId(Long resId), count = {}", count);
            return count;
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

    /**
     * generate role-res-relations
     *
     * @param roleId
     * @param resIds
     * @param operatorId
     * @return
     */
    private List<RoleResRelation> generateRoleResRelations(Long roleId, List<Long> resIds, Long operatorId) {
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid roleId");
        if (isEmpty(resIds))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid resIds");
        if (isInvalidIdentity(operatorId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid operatorId");

        long epochSecond = TIME_STAMP_GETTER.get();

        return resIds.stream()
                .map(resId -> {
                    RoleResRelation relation = new RoleResRelation();

                    relation.setId(blueIdentityProcessor.generate(RoleResRelation.class));
                    relation.setRoleId(roleId);
                    relation.setResId(resId);
                    relation.setCreateTime(epochSecond);
                    relation.setUpdateTime(epochSecond);
                    relation.setCreator(operatorId);
                    relation.setUpdater(operatorId);

                    return relation;
                }).collect(toList());
    }

    /**
     * update authority base on role / generate role-resource-relations
     *
     * @param roleId
     * @param resIds
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public AuthorityBaseOnRole updateAuthorityByRole(Long roleId, List<Long> resIds, Long operatorId) {
        LOGGER.info("Mono<AuthorityBaseOnRole> updateAuthorityBaseOnRole(Long roleId, List<Long> resIds, Long operatorId), roleId = {}, resIds = {}, operatorId = {}", roleId, resIds, operatorId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid roleId");
        if (isEmpty(resIds))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid resIds");
        if (isInvalidIdentity(operatorId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid operatorId");

        RLock lock = redissonClient.getLock(AUTHORITY_UPDATE_SYNC.key);
        lock.lock();

        try {
            Optional<Role> roleOpt = roleService.getRoleById(roleId);
            if (roleOpt.isEmpty())
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "role is not exist");

            List<Long> resourceIds = resIds.stream().filter(Objects::nonNull).distinct()
                    .collect(toList());

            if (isEmpty(resourceIds))
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid resIds");
            List<Resource> resources = resourceService.selectResourceByIds(resourceIds);
            if (isEmpty(resources))
                throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid resIds");

            if (resources.size() < resourceIds.size()) {
                Set<Long> resIdSet = resources.stream().map(Resource::getId).collect(toSet());
                for (long rid : resourceIds)
                    if (!resIdSet.contains(rid))
                        throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid resId in resIds, resId = " + rid);
            }

            List<RoleResRelation> roleResRelations = generateRoleResRelations(roleId, resourceIds, operatorId);

            this.deleteRelationByRoleId(roleId);
            this.insertRelationBatch(roleResRelations);

            return new AuthorityBaseOnRole(AuthModelConverters.ROLE_2_ROLE_INFO_CONVERTER.apply(roleOpt.get()), resources.stream().map(AuthModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER).collect(toList()));
        } catch (Exception e) {
            LOGGER.error("Mono<AuthorityBaseOnRole> updateAuthorityBaseOnRole(Long roleId, List<Long> resIds, Long operatorId) failed, roleId = {}, resIds = {}, operatorId = {}, e = {}", roleId, resIds, operatorId, e);
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
