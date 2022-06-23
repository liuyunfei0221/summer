package com.blue.auth.service.impl;

import com.blue.auth.api.model.AuthorityBaseOnResource;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.api.model.ResourceInfo;
import com.blue.auth.api.model.RoleInfo;
import com.blue.auth.converter.AuthModelConverters;
import com.blue.auth.repository.entity.Resource;
import com.blue.auth.repository.entity.Role;
import com.blue.auth.repository.entity.RoleResRelation;
import com.blue.auth.repository.mapper.RoleResRelationMapper;
import com.blue.auth.service.inter.ResourceService;
import com.blue.auth.service.inter.RoleResRelationService;
import com.blue.auth.service.inter.RoleService;
import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.redisson.component.SynchronizedProcessor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.blue.auth.converter.AuthModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER;
import static com.blue.auth.converter.AuthModelConverters.ROLE_2_ROLE_INFO_CONVERTER;
import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.common.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.common.CacheKey.ROLE_RES_RELS;
import static com.blue.base.constant.common.ResponseElement.*;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.*;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;
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

    private StringRedisTemplate stringRedisTemplate;

    private SynchronizedProcessor synchronizedProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RoleResRelationServiceImpl(BlueIdentityProcessor blueIdentityProcessor, RoleResRelationMapper roleResRelationMapper, RoleService roleService, ResourceService resourceService,
                                      StringRedisTemplate stringRedisTemplate, SynchronizedProcessor synchronizedProcessor) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.roleResRelationMapper = roleResRelationMapper;
        this.roleService = roleService;
        this.resourceService = resourceService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.synchronizedProcessor = synchronizedProcessor;
    }

    /**
     * is a relation exist?
     */
    private final Consumer<RoleResRelation> INSERT_RELATION_VALIDATOR = relation -> {
        if (isNull(relation))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(relation.getId()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "id is invalid");

        Long roleId = relation.getRoleId();
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "roleId is invalid");

        Long resId = relation.getResId();
        if (isInvalidIdentity(resId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "resId is invalid");

        if (isNull(relation.getCreateTime()) || isNull(relation.getUpdateTime()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "createTime or updateTime is invalid");

        if (isInvalidIdentity(relation.getCreator()) || isInvalidIdentity(relation.getUpdater()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "creator or updater is invalid");

        if (isNotNull(roleResRelationMapper.selectExistByRoleIdAndResId(roleId, resId)))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "The data base res and role already exists");
    };

    private final Consumer<String> CACHE_DELETER = key ->
            stringRedisTemplate.delete(key);

    private final Supplier<List<RoleResRelation>> RELATIONS_DB_SUP = () ->
            roleResRelationMapper.select();

    private final Supplier<List<RoleResRelation>> RELATIONS_REDIS_SUP = () ->
            ofNullable(stringRedisTemplate.opsForList().range(ROLE_RES_RELS.key, 0, -1))
                    .orElseGet(Collections::emptyList).stream().map(s -> GSON.fromJson(s, RoleResRelation.class)).collect(toList());

    private final Consumer<List<RoleResRelation>> RELATIONS_REDIS_SETTER = roles -> {
        CACHE_DELETER.accept(ROLE_RES_RELS.key);
        stringRedisTemplate.opsForList().rightPushAll(ROLE_RES_RELS.key,
                roles.stream().map(GSON::toJson).collect(toList()));
    };

    private final Supplier<List<RoleResRelation>> RELATIONS_WITH_CACHE_SUP = () ->
            synchronizedProcessor.handleSupByOrderedWithSetter(RELATIONS_REDIS_SUP, BlueChecker::isNotEmpty, RELATIONS_DB_SUP, RELATIONS_REDIS_SETTER);

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
     * insert relation
     *
     * @param roleResRelation
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public int insertRelation(RoleResRelation roleResRelation) {
        LOGGER.info("void insertRelation(RoleResRelation roleResRelation), roleResRelation = {}", roleResRelation);
        if (isNull(roleResRelation))
            throw new BlueException(EMPTY_PARAM);

        INSERT_RELATION_VALIDATOR.accept(roleResRelation);

        CACHE_DELETER.accept(ROLE_RES_RELS.key);
        int inserted = roleResRelationMapper.insert(roleResRelation);
        CACHE_DELETER.accept(ROLE_RES_RELS.key);

        return inserted;
    }

    /**
     * insert relations
     *
     * @param roleResRelations
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public int insertRelationBatch(List<RoleResRelation> roleResRelations) {
        LOGGER.info("insertRelationBatch(List<RoleResRelation> roleResRelations), roleResRelations = {}", roleResRelations);
        if (isEmpty(roleResRelations))
            throw new BlueException(EMPTY_PARAM);

        CACHE_DELETER.accept(ROLE_RES_RELS.key);
        int inserted = roleResRelationMapper.insertBatch(roleResRelations.stream().filter(Objects::nonNull)
                .peek(INSERT_RELATION_VALIDATOR).collect(toList()));
        CACHE_DELETER.accept(ROLE_RES_RELS.key);

        return inserted;
    }

    /**
     * delete relation by role id
     *
     * @param roleId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public int deleteRelationByRoleId(Long roleId) {
        LOGGER.info("int deleteRelationByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(INVALID_IDENTITY);

        CACHE_DELETER.accept(ROLE_RES_RELS.key);
        int count = roleResRelationMapper.deleteByRoleId(roleId);
        CACHE_DELETER.accept(ROLE_RES_RELS.key);

        LOGGER.info("int deleteRelationByRoleId(Long roleId), count = {}", count);
        return count;
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
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public AuthorityBaseOnRole updateAuthorityByRole(Long roleId, List<Long> resIds, Long operatorId) {
        LOGGER.info("Mono<AuthorityBaseOnRole> updateAuthorityBaseOnRole(Long roleId, List<Long> resIds, Long operatorId), roleId = {}, resIds = {}, operatorId = {}", roleId, resIds, operatorId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid roleId");
        if (isEmpty(resIds))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid resIds");
        if (isInvalidIdentity(operatorId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid operatorId");

        Optional<Role> roleOpt = roleService.getRole(roleId);
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

        CACHE_DELETER.accept(ROLE_RES_RELS.key);
        this.deleteRelationByRoleId(roleId);
        this.insertRelationBatch(roleResRelations);
        CACHE_DELETER.accept(ROLE_RES_RELS.key);

        return new AuthorityBaseOnRole(ROLE_2_ROLE_INFO_CONVERTER.apply(roleOpt.get()),
                resources.stream().map(RESOURCE_2_RESOURCE_INFO_CONVERTER).collect(toList()));
    }

    /**
     * get authority base on role by role id
     *
     * @param roleId
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnRole> getAuthorityMonoByRoleId(Long roleId) {
        LOGGER.info("Mono<AuthorityBaseOnRole> getAuthorityMonoByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(INVALID_IDENTITY);

        return roleService.getRoleMono(roleId)
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
     * get authority base on role by role id
     *
     * @param roleIds
     * @return
     */
    @Override
    public Mono<List<AuthorityBaseOnRole>> selectAuthoritiesMonoByRoleIds(List<Long> roleIds) {
        LOGGER.info("Mono<List<AuthorityBaseOnRole>> selectAuthorityMonoByRoleId(List<Long> roleIds), roleIds = {}", roleIds);
        if (isEmpty(roleIds))
            throw new BlueException(INVALID_IDENTITY);

        return zip(roleService.selectRoleMonoByIds(roleIds).flatMap(roles -> just(roles.stream().map(ROLE_2_ROLE_INFO_CONVERTER).collect(toMap(RoleInfo::getId, r -> r, (a, b) -> a)))),
                this.selectRelationByRoleIds(roleIds))
                .flatMap(tuple2 -> {
                    List<RoleResRelation> resRelations = tuple2.getT2();
                    return zip(just(tuple2.getT1()),
                            just(resourceService.selectResourceByIds(resRelations.stream().map(RoleResRelation::getResId).collect(toList())))
                                    .flatMap(resources -> just(resources.stream().map(RESOURCE_2_RESOURCE_INFO_CONVERTER).collect(toMap(ResourceInfo::getId, r -> r, (a, b) -> a)))),
                            just(resRelations.stream().collect(groupingBy(RoleResRelation::getRoleId))));
                }).flatMap(tuple3 -> {
                    Map<Long, RoleInfo> roleInfoMapping = tuple3.getT1();
                    Map<Long, ResourceInfo> resourceInfoMapping = tuple3.getT2();
                    return just(tuple3.getT3().entrySet().stream()
                            .map(entry -> new AuthorityBaseOnRole(roleInfoMapping.get(entry.getKey()),
                                    entry.getValue().stream().map(rel -> resourceInfoMapping.get(rel.getResId())).collect(toList()))).collect(toList()));
                });
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

        return resourceService.getResourceMono(resId)
                .flatMap(resOpt ->
                        resOpt.map(res ->
                                        just(RESOURCE_2_RESOURCE_INFO_CONVERTER.apply(res)))
                                .orElseGet(() -> {
                                    LOGGER.error("res info doesn't exist, resId = {}", resId);
                                    return error(() -> new BlueException(DATA_NOT_EXIST));
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

        return just(RELATIONS_WITH_CACHE_SUP.get());
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
    public List<RoleResRelation> selectRelationByResId(Long resId) {
        LOGGER.info("List<RoleResRelation> selectRelationByResId(Long resId), resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(INVALID_IDENTITY);

        return roleResRelationMapper.selectByResId(resId);
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

}
