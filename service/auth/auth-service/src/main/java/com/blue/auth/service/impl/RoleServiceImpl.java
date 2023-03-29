package com.blue.auth.service.impl;

import com.blue.auth.api.model.RoleInfo;
import com.blue.auth.constant.RoleSortAttribute;
import com.blue.auth.model.RoleCondition;
import com.blue.auth.model.RoleInsertParam;
import com.blue.auth.model.RoleManagerInfo;
import com.blue.auth.model.RoleUpdateParam;
import com.blue.auth.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.auth.repository.entity.Role;
import com.blue.auth.repository.mapper.RoleMapper;
import com.blue.auth.service.inter.RoleService;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redisson.component.SynchronizedProcessor;
import org.slf4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import static com.blue.auth.converter.AuthModelConverters.*;
import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.CacheKey.DEFAULT_ROLE;
import static com.blue.basic.constant.common.CacheKey.ROLES;
import static com.blue.basic.constant.common.Default.DEFAULT;
import static com.blue.basic.constant.common.Default.NOT_DEFAULT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Symbol.PERCENT;
import static com.blue.basic.constant.common.SyncKey.DEFAULT_ROLE_REFRESH_SYNC;
import static com.blue.basic.constant.common.SyncKey.ROLES_REFRESH_SYNC;
import static com.blue.database.common.ConditionSortProcessor.process;
import static java.util.Collections.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;

/**
 * role service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = getLogger(RoleServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private RoleMapper roleMapper;

    private StringRedisTemplate stringRedisTemplate;

    private SynchronizedProcessor synchronizedProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RoleServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BlueIdentityProcessor blueIdentityProcessor, RoleMapper roleMapper,
                           StringRedisTemplate stringRedisTemplate, SynchronizedProcessor synchronizedProcessor) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.roleMapper = roleMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.synchronizedProcessor = synchronizedProcessor;
    }

    private static final int
            REDIS_LIST_START = 0,
            REDIS_LIST_END = -1;

    private final Consumer<String> CACHE_DELETER = key ->
            stringRedisTemplate.delete(key);

    private final Supplier<List<Role>> ROLES_DB_SUP = () ->
            roleMapper.select();

    private final Supplier<List<Role>> ROLES_REDIS_SUP = () ->
            ofNullable(stringRedisTemplate.opsForList().range(ROLES.key, REDIS_LIST_START, REDIS_LIST_END))
                    .orElseGet(Collections::emptyList).stream().map(s -> GSON.fromJson(s, Role.class)).collect(toList());

    private final Consumer<List<Role>> ROLES_REDIS_SETTER = roles -> {
        CACHE_DELETER.accept(ROLES.key);
        stringRedisTemplate.opsForList().rightPushAll(ROLES.key,
                roles.stream().map(GSON::toJson).collect(toList()));
    };

    private final Supplier<List<Role>> ROLES_WITH_CACHE_SUP = () ->
            synchronizedProcessor.handleSupByOrderedWithSetter(ROLES_REFRESH_SYNC.key, ROLES_REDIS_SUP, ROLES_DB_SUP, ROLES_REDIS_SETTER, BlueChecker::isNotEmpty);

    private final Supplier<Role> DEFAULT_ROLE_DB_SUP = () -> {
        List<Role> defaultRoles = roleMapper.selectDefault();
        if (isEmpty(defaultRoles))
            throw new RuntimeException("defaultRoles is empty");
        if (defaultRoles.size() > 1)
            throw new RuntimeException("defaultRoles more than 1");

        Role defaultRole = defaultRoles.get(0);

        LOGGER.info("defaultRole = {]", defaultRole);
        return defaultRole;
    };

    private final Supplier<Role> NULLABLE_DEFAULT_ROLE_REDIS_SUP = () ->
            ofNullable(stringRedisTemplate.opsForValue().get(DEFAULT_ROLE.key))
                    .filter(StringUtils::hasText)
                    .map(v -> {
                        LOGGER.info("REDIS_DEFAULT_ROLE_GETTER, v = {}", v);
                        return GSON.fromJson(v, Role.class);
                    }).orElse(null);

    private final Consumer<Role> DEFAULT_ROLE_REDIS_SETTER = role ->
            ofNullable(role)
                    .ifPresent(r -> {
                        stringRedisTemplate.opsForValue().set(DEFAULT_ROLE.key, GSON.toJson(r));
                        LOGGER.info("REDIS_CACHE_DEFAULT_ROLE_CACHE, r = {}", r);
                    });

    private final Supplier<Role> DEFAULT_ROLE_WITH_CACHE_SUP = () ->
            synchronizedProcessor.handleSupByOrderedWithSetter(DEFAULT_ROLE_REFRESH_SYNC.key, NULLABLE_DEFAULT_ROLE_REDIS_SUP,
                    DEFAULT_ROLE_DB_SUP, DEFAULT_ROLE_REDIS_SETTER, BlueChecker::isNotNull);

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(RoleSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<RoleCondition> CONDITION_PROCESSOR = c -> {
        RoleCondition rc = isNotNull(c) ? c : new RoleCondition();

        process(rc, SORT_ATTRIBUTE_MAPPING, RoleSortAttribute.CREATE_TIME.column);

        ofNullable(rc.getNameLike())
                .filter(StringUtils::hasText).ifPresent(nameLike -> rc.setNameLike(PERCENT.identity + nameLike + PERCENT.identity));

        return rc;
    };

    private static final Function<List<Role>, List<Long>> OPERATORS_GETTER = rs -> {
        Set<Long> operatorIds = new HashSet<>(rs.size());

        for (Role r : rs) {
            operatorIds.add(r.getCreator());
            operatorIds.add(r.getUpdater());
        }

        return new ArrayList<>(operatorIds);
    };

    private final Consumer<RoleInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        if (isNotNull(roleMapper.selectByName(p.getName())))
            throw new BlueException(ROLE_NAME_ALREADY_EXIST);

        if (isNotNull(roleMapper.selectByLevel(p.getLevel())))
            throw new BlueException(ROLE_LEVEL_ALREADY_EXIST);
    };

    private final Function<RoleUpdateParam, Role> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        ofNullable(p.getName())
                .filter(BlueChecker::isNotBlank)
                .map(roleMapper::selectByName)
                .map(Role::getId)
                .ifPresent(eid -> {
                    if (!id.equals(eid))
                        throw new BlueException(ROLE_NAME_ALREADY_EXIST);
                });

        ofNullable(p.getLevel())
                .map(roleMapper::selectByLevel)
                .map(Role::getId)
                .ifPresent(eid -> {
                    if (!id.equals(eid))
                        throw new BlueException(ROLE_LEVEL_ALREADY_EXIST);
                });

        Role role = roleMapper.selectByPrimaryKey(id);
        if (isNull(role))
            throw new BlueException(DATA_NOT_EXIST);

        return role;
    };

    public static final BiConsumer<RoleUpdateParam, Role> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
        if (!p.getId().equals(t.getId()))
            throw new BlueException(BAD_REQUEST);

        boolean alteration = false;

        String name = p.getName();
        if (isNotBlank(name) && !name.equals(t.getName())) {
            t.setName(name);
            alteration = true;
        }

        String description = p.getDescription();
        if (isNotBlank(description) && !description.equals(t.getDescription())) {
            t.setDescription(description);
            alteration = true;
        }

        Integer level = p.getLevel();
        if (isNotNull(level) && !level.equals(t.getLevel())) {
            t.setLevel(level);
            alteration = true;
        }

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    /**
     * insert a new role
     *
     * @param roleInsertParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public RoleInfo insertRole(RoleInsertParam roleInsertParam, Long operatorId) {
        LOGGER.info("roleInsertParam = {}, operatorId = {}", roleInsertParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        INSERT_ITEM_VALIDATOR.accept(roleInsertParam);
        Role role = ROLE_INSERT_PARAM_2_ROLE_CONVERTER.apply(roleInsertParam);

        role.setId(blueIdentityProcessor.generate(Role.class));
        role.setCreator(operatorId);
        role.setUpdater(operatorId);

        CACHE_DELETER.accept(ROLES.key);
        roleMapper.insert(role);
        CACHE_DELETER.accept(ROLES.key);

        return ROLE_2_ROLE_INFO_CONVERTER.apply(role);
    }

    /**
     * update a exist role
     *
     * @param roleUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public RoleInfo updateRole(RoleUpdateParam roleUpdateParam, Long operatorId) {
        LOGGER.info("roleUpdateParam = {}, operatorId = {}", roleUpdateParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        Role role = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(roleUpdateParam);

        UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(roleUpdateParam, role);
        role.setUpdater(operatorId);

        CACHE_DELETER.accept(ROLES.key);
        roleMapper.updateByPrimaryKeySelective(role);
        CACHE_DELETER.accept(ROLES.key);

        return ROLE_2_ROLE_INFO_CONVERTER.apply(role);
    }

    /**
     * delete role
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public RoleInfo deleteRole(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        Role role = roleMapper.selectByPrimaryKey(id);
        if (isNull(role))
            throw new BlueException(DATA_NOT_EXIST);
        if (role.getIsDefault())
            throw new BlueException(UNSUPPORTED_OPERATE);

        CACHE_DELETER.accept(ROLES.key);
        roleMapper.deleteByPrimaryKey(id);
        CACHE_DELETER.accept(ROLES.key);

        return ROLE_2_ROLE_INFO_CONVERTER.apply(role);
    }

    /**
     * update default role by role id
     *
     * @param id
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public RoleManagerInfo updateDefaultRole(Long id, Long operatorId) {
        LOGGER.info("id = {}， operatorId = {}", id, operatorId);
        if (isInvalidIdentity(id) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        Role newDefaultRole = roleMapper.selectByPrimaryKey(id);
        if (isNull(newDefaultRole))
            throw new BlueException(DATA_NOT_EXIST);

        Role oldDefaultRole = DEFAULT_ROLE_DB_SUP.get();
        if (newDefaultRole.getId().equals(oldDefaultRole.getId()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "role is already default");

        Long stamp = TIME_STAMP_GETTER.get();

        newDefaultRole.setIsDefault(DEFAULT.status);
        newDefaultRole.setUpdater(operatorId);
        newDefaultRole.setUpdateTime(stamp);

        oldDefaultRole.setIsDefault(NOT_DEFAULT.status);
        oldDefaultRole.setUpdater(operatorId);
        oldDefaultRole.setUpdateTime(stamp);

        CACHE_DELETER.accept(DEFAULT_ROLE.key);
        roleMapper.updateByPrimaryKey(newDefaultRole);
        roleMapper.updateByPrimaryKey(oldDefaultRole);
        CACHE_DELETER.accept(DEFAULT_ROLE.key);

        Map<Long, String> idAndMemberNameMapping;
        try {
            idAndMemberNameMapping = rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(singletonList(newDefaultRole))).toFuture().join()
                    .parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
        } catch (Exception e) {
            LOGGER.error("generate idAndMemberNameMapping failed, id = {}， operatorId = {}, e = {}", id, operatorId, e);
            idAndMemberNameMapping = emptyMap();
        }

        return ROLE_2_ROLE_MANAGER_INFO_CONVERTER.apply(newDefaultRole, idAndMemberNameMapping);
    }

    /**
     * get default role
     *
     * @return
     */
    @Override
    public Role getDefaultRole() {
        Role defaultRole = DEFAULT_ROLE_WITH_CACHE_SUP.get();
        LOGGER.info("defaultRole = {}", defaultRole);

        return ofNullable(defaultRole).orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
    }

    /**
     * get role by role id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Role> getRoleOpt(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(roleMapper.selectByPrimaryKey(id));
    }

    /**
     * get role mono by role id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Role> getRole(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(roleMapper.selectByPrimaryKey(id));
    }

    /**
     * select roles by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<Role>> selectRoleByIds(List<Long> ids) {
        LOGGER.info("ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return just(allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(roleMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList()));
    }

    /**
     * select all roles
     *
     * @return
     */
    @Override
    public Mono<List<Role>> selectRole() {
        return just(ROLES_WITH_CACHE_SUP.get());
    }

    /**
     * select role by page and condition
     *
     * @param limit
     * @param rows
     * @param roleCondition
     * @return
     */
    @Override
    public Mono<List<Role>> selectRoleByLimitAndCondition(Long limit, Long rows, RoleCondition roleCondition) {
        LOGGER.info("limit = {}, rows = {}, roleCondition = {}", limit, rows, roleCondition);
        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        return just(roleMapper.selectByLimitAndCondition(limit, rows, roleCondition));
    }

    /**
     * count role by condition
     *
     * @param roleCondition
     * @return
     */
    @Override
    public Mono<Long> countRoleByCondition(RoleCondition roleCondition) {
        LOGGER.info("roleCondition = {}", roleCondition);
        return just(ofNullable(roleMapper.countByCondition(roleCondition)).orElse(0L));
    }

    /**
     * select role info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<RoleManagerInfo>> selectRoleManagerInfoPageByPageAndCondition(PageModelRequest<RoleCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);

        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        RoleCondition roleCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectRoleByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), roleCondition), countRoleByCondition(roleCondition))
                .flatMap(tuple2 -> {
                    List<Role> roles = tuple2.getT1();
                    Long count = tuple2.getT2();
                    return isNotEmpty(roles) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(OPERATORS_GETTER.apply(roles))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndMemberNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(roles.stream().map(r ->
                                                ROLE_2_ROLE_MANAGER_INFO_CONVERTER.apply(r, idAndMemberNameMapping)).collect(toList()));
                                    }).flatMap(resourceManagerInfos ->
                                            just(new PageModelResponse<>(resourceManagerInfos, count)))
                            :
                            just(new PageModelResponse<>(emptyList(), count));
                });
    }

}
