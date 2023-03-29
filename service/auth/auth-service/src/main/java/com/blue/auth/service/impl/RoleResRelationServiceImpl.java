package com.blue.auth.service.impl;

import com.blue.auth.api.model.AuthorityBaseOnResource;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.api.model.ResourceInfo;
import com.blue.auth.api.model.RoleInfo;
import com.blue.auth.repository.entity.Resource;
import com.blue.auth.repository.entity.Role;
import com.blue.auth.repository.entity.RoleResRelation;
import com.blue.auth.repository.mapper.RoleResRelationMapper;
import com.blue.auth.service.inter.ResourceService;
import com.blue.auth.service.inter.RoleResRelationService;
import com.blue.auth.service.inter.RoleService;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.redisson.component.SynchronizedProcessor;
import org.slf4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static com.blue.auth.converter.AuthModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER;
import static com.blue.auth.converter.AuthModelConverters.ROLE_2_ROLE_INFO_CONVERTER;
import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.CacheKey.ROLE_RES_RELS;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SyncKey.ROLE_RESOURCE_RELATIONS_REFRESH_SYNC;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collectors.*;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;


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

    private static final int
            REDIS_LIST_START = 0,
            REDIS_LIST_END = -1;

    private final Consumer<RoleResRelation> INSERT_ITEM_VALIDATOR = r -> {
        if (isNull(r))
            throw new BlueException(EMPTY_PARAM);

        Long roleId = r.getRoleId();
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "roleId is invalid");

        Long resId = r.getResId();
        if (isInvalidIdentity(resId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "resId is invalid");

        if (isNull(r.getCreateTime()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "createTime is invalid");

        if (isInvalidIdentity(r.getCreator()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "creator is invalid");

        if (isNotNull(roleResRelationMapper.selectByRoleIdAndResId(roleId, resId)))
            throw new BlueException(DATA_ALREADY_EXIST.status, DATA_ALREADY_EXIST.code, "The data base res and role already exists");
    };

    private static final Collector<RoleResRelation, Set<Long>, Set<Long>> RES_ID_SET_COLLECTOR = Collector.of(
            HashSet::new,
            (set, rel) -> set.add(rel.getResId()),
            (a, b) -> {
                a.addAll(b);
                return a;
            }, CONCURRENT);

    private final Consumer<List<RoleResRelation>> INSERT_ITEMS_VALIDATOR = rs -> {
        if (isEmpty(rs))
            throw new BlueException(EMPTY_PARAM);

        Set<Long> roleIds = new HashSet<>();

        for (RoleResRelation relation : rs) {
            INSERT_ITEM_VALIDATOR.accept(relation);
            roleIds.add(relation.getRoleId());
        }

        Map<Long, Set<Long>> roleIdAndResIdSetMapping = roleResRelationMapper.selectByRoleIds(new ArrayList<>(roleIds))
                .stream().collect(groupingBy(RoleResRelation::getRoleId, RES_ID_SET_COLLECTOR));

        for (RoleResRelation relation : rs)
            if (ofNullable(roleIdAndResIdSetMapping.get(relation.getRoleId())).map(set -> set.contains(relation.getResId())).isPresent())
                throw new BlueException(DATA_ALREADY_EXIST.status, DATA_ALREADY_EXIST.code, "The data base res and role already exists");
    };

    private final Consumer<String> CACHE_DELETER = key ->
            stringRedisTemplate.delete(key);

    private final Supplier<List<RoleResRelation>> RELATIONS_DB_SUP = () ->
            roleResRelationMapper.select();

    private final Supplier<List<RoleResRelation>> RELATIONS_REDIS_SUP = () ->
            ofNullable(stringRedisTemplate.opsForList().range(ROLE_RES_RELS.key, REDIS_LIST_START, REDIS_LIST_END))
                    .orElseGet(Collections::emptyList).stream().map(s -> GSON.fromJson(s, RoleResRelation.class)).collect(toList());

    private final Consumer<List<RoleResRelation>> RELATIONS_REDIS_SETTER = roles -> {
        CACHE_DELETER.accept(ROLE_RES_RELS.key);
        stringRedisTemplate.opsForList().rightPushAll(ROLE_RES_RELS.key,
                roles.stream().map(GSON::toJson).collect(toList()));
    };

    private final Supplier<List<RoleResRelation>> RELATIONS_WITH_CACHE_SUP = () ->
            synchronizedProcessor.handleSupByOrderedWithSetter(ROLE_RESOURCE_RELATIONS_REFRESH_SYNC.key, RELATIONS_REDIS_SUP, RELATIONS_DB_SUP, RELATIONS_REDIS_SETTER, BlueChecker::isNotEmpty);

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

        long timeStamp = TIME_STAMP_GETTER.get();

        return resIds.stream()
                .map(resId -> {
                    RoleResRelation relation = new RoleResRelation();

                    relation.setId(blueIdentityProcessor.generate(RoleResRelation.class));
                    relation.setRoleId(roleId);
                    relation.setResId(resId);
                    relation.setCreateTime(timeStamp);
                    relation.setCreator(operatorId);

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
        LOGGER.info("roleResRelation = {}", roleResRelation);
        if (isNull(roleResRelation))
            throw new BlueException(EMPTY_PARAM);

        INSERT_ITEM_VALIDATOR.accept(roleResRelation);
        if (isInvalidIdentity(roleResRelation.getId()))
            roleResRelation.setId(blueIdentityProcessor.generate(RoleResRelation.class));

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
        LOGGER.info("roleResRelations = {}", roleResRelations);

        INSERT_ITEMS_VALIDATOR.accept(roleResRelations);
        for (RoleResRelation roleResRelation : roleResRelations)
            if (isInvalidIdentity(roleResRelation.getId()))
                roleResRelation.setId(blueIdentityProcessor.generate(RoleResRelation.class));

        CACHE_DELETER.accept(ROLE_RES_RELS.key);
        int inserted = roleResRelationMapper.insertBatch(roleResRelations);
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
        LOGGER.info("roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(INVALID_IDENTITY);

        CACHE_DELETER.accept(ROLE_RES_RELS.key);
        int count = roleResRelationMapper.deleteByRoleId(roleId);
        CACHE_DELETER.accept(ROLE_RES_RELS.key);

        LOGGER.info("count = {}", count);
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
        LOGGER.info("roleId = {}, resIds = {}, operatorId = {}", roleId, resIds, operatorId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid roleId");
        if (isEmpty(resIds))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid resIds");
        if (isInvalidIdentity(operatorId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid operatorId");

        Optional<Role> roleOpt = roleService.getRoleOpt(roleId);
        if (roleOpt.isEmpty())
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "role is not exist");

        List<Long> resourceIds = resIds.stream().filter(Objects::nonNull).distinct()
                .collect(toList());

        if (isEmpty(resourceIds))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid resIds");
        List<Resource> resources = resourceService.selectResourceByIds(resourceIds).toFuture().join();
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
        LOGGER.info("roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(INVALID_IDENTITY);

        return roleService.getRole(roleId)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .map((ROLE_2_ROLE_INFO_CONVERTER)
                ).flatMap(roleInfo ->
                        this.selectResIdsByRoleId(roleId)
                                .flatMap(resourceService::selectResourceByIds)
                                .flatMap(resources -> just(resources.stream().map(RESOURCE_2_RESOURCE_INFO_CONVERTER).collect(toList())))
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
    public Mono<List<AuthorityBaseOnRole>> selectAuthoritiesByRoleIds(List<Long> roleIds) {
        LOGGER.info("roleIds = {}", roleIds);
        if (isEmpty(roleIds))
            throw new BlueException(INVALID_IDENTITY);

        return zip(roleService.selectRoleByIds(roleIds).flatMap(roles -> just(roles.stream().map(ROLE_2_ROLE_INFO_CONVERTER).collect(toMap(RoleInfo::getId, identity(), (a, b) -> a)))),
                this.selectRelationByRoleIds(roleIds))
                .flatMap(tuple2 -> {
                    List<RoleResRelation> resRelations = tuple2.getT2();
                    return zip(just(tuple2.getT1()),
                            just(resourceService.selectResourceByIds(resRelations.stream().map(RoleResRelation::getResId).collect(toList()))
                                    .toFuture().join())
                                    .flatMap(resources -> just(resources.stream().map(RESOURCE_2_RESOURCE_INFO_CONVERTER).collect(toMap(ResourceInfo::getId, identity(), (a, b) -> a)))),
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
    public Mono<AuthorityBaseOnResource> selectAuthorityByResId(Long resId) {
        LOGGER.info("resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(INVALID_IDENTITY);

        return resourceService.getResource(resId)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .map(RESOURCE_2_RESOURCE_INFO_CONVERTER)
                .flatMap(resourceInfo ->
                        this.selectRoleIdsByResId(resId)
                                .flatMap(roleService::selectRoleByIds)
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
        return just(RELATIONS_WITH_CACHE_SUP.get());
    }

    /**
     * select resources ids by role id
     *
     * @return
     */
    @Override
    public Mono<List<Long>> selectResIdsByRoleId(Long roleId) {
        LOGGER.info("roleId = {}", roleId);
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
    public Mono<List<Resource>> selectResByRoleId(Long roleId) {
        LOGGER.info("roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(INVALID_IDENTITY);

        return this.selectResIdsByRoleId(roleId)
                .flatMap(ids -> {
                    LOGGER.info("ids = {}", ids);
                    return isValidIdentities(ids) ? resourceService.selectResourceByIds(ids) : just(emptyList());
                });
    }

    /**
     * select role ids by resource id
     *
     * @param resId
     * @return
     */
    @Override
    public Mono<List<Long>> selectRoleIdsByResId(Long resId) {
        LOGGER.info("resId = {}", resId);
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
    public Mono<List<Role>> selectRoleByResId(Long resId) {
        LOGGER.info("resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(INVALID_IDENTITY);

        return this.selectRoleIdsByResId(resId)
                .flatMap(ids -> {
                    LOGGER.info("resId = {}, ids = {}", ids);
                    return isEmpty(ids) ? just(emptyList()) : roleService.selectRoleByIds(ids);
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
        LOGGER.info("roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(INVALID_IDENTITY);

        return just(roleResRelationMapper.selectByRoleId(roleId));
    }

    /**
     * select relation by resource id
     *
     * @param resId
     * @return
     */
    @Override
    public Mono<List<RoleResRelation>> selectRelationByResId(Long resId) {
        LOGGER.info("resId = {}", resId);
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
        LOGGER.info("roleIds = {}", roleIds);

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
        LOGGER.info("resIds = {}", resIds);

        return isValidIdentities(resIds) ? just(allotByMax(resIds, (int) DB_SELECT.value, false)
                .stream().map(roleResRelationMapper::selectByResIds)
                .flatMap(List::stream)
                .collect(toList()))
                :
                just(emptyList());
    }

}
