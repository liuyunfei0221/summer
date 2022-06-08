package com.blue.auth.service.impl;

import com.blue.auth.api.model.RoleInfo;
import com.blue.auth.api.model.RoleManagerInfo;
import com.blue.auth.constant.RoleSortAttribute;
import com.blue.auth.model.RoleCondition;
import com.blue.auth.model.RoleInsertParam;
import com.blue.auth.model.RoleUpdateParam;
import com.blue.auth.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.auth.repository.entity.Role;
import com.blue.auth.repository.mapper.RoleMapper;
import com.blue.auth.service.inter.RoleService;
import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redisson.component.SynchronizedProcessor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import static com.blue.auth.converter.AuthModelConverters.*;
import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConditionSortProcessor.process;
import static com.blue.base.constant.common.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.common.CacheKey.DEFAULT_ROLE;
import static com.blue.base.constant.common.CacheKey.ROLES;
import static com.blue.base.constant.common.Default.DEFAULT;
import static com.blue.base.constant.common.Default.NOT_DEFAULT;
import static com.blue.base.constant.common.ResponseElement.*;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static java.util.Collections.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;
import static reactor.util.Loggers.getLogger;

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

    private final Consumer<String> CACHE_DELETER = key ->
            stringRedisTemplate.delete(key);

    private final Supplier<List<Role>> ROLES_DB_SUP = () ->
            roleMapper.select();

    private final Supplier<List<Role>> ROLES_REDIS_SUP = () ->
            ofNullable(stringRedisTemplate.opsForList().range(ROLES.key, 0, -1))
                    .orElseGet(Collections::emptyList).stream().map(s -> GSON.fromJson(s, Role.class)).collect(toList());

    private final Consumer<List<Role>> ROLES_REDIS_SETTER = roles -> {
        CACHE_DELETER.accept(ROLES.key);
        stringRedisTemplate.opsForList().rightPushAll(ROLES.key,
                roles.stream().map(GSON::toJson).collect(toList()));
    };

    private final Supplier<List<Role>> ROLES_WITH_CACHE_SUP = () ->
            synchronizedProcessor.handleSupByOrderedWithSetter(ROLES_REDIS_SUP, BlueChecker::isNotEmpty, ROLES_DB_SUP, ROLES_REDIS_SETTER);


    private final Supplier<Role> DEFAULT_ROLE_DB_SUP = () -> {
        List<Role> defaultRoles = roleMapper.selectDefault();
        if (isEmpty(defaultRoles))
            throw new BlueException(INTERNAL_SERVER_ERROR);
        if (defaultRoles.size() > 1)
            throw new BlueException(INTERNAL_SERVER_ERROR);

        Role defaultRole = defaultRoles.get(0);

        LOGGER.info("Role getDefaultRoleFromDb(), defaultRole = {]", defaultRole);
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
            synchronizedProcessor.handleSupByOrderedWithSetter(
                    NULLABLE_DEFAULT_ROLE_REDIS_SUP, BlueChecker::isNotNull, DEFAULT_ROLE_DB_SUP, DEFAULT_ROLE_REDIS_SETTER);

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(RoleSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<RoleCondition> CONDITION_PROCESSOR = condition -> {
        if (isNull(condition))
            return new RoleCondition();

        process(condition, SORT_ATTRIBUTE_MAPPING, RoleSortAttribute.ID.column);

        ofNullable(condition.getNameLike())
                .filter(StringUtils::hasText).ifPresent(nameLike -> condition.setNameLike("%" + nameLike + "%"));

        return condition;
    };

    private static final Function<List<Role>, List<Long>> OPERATORS_GETTER = roles -> {
        Set<Long> operatorIds = new HashSet<>(roles.size());

        for (Role r : roles) {
            operatorIds.add(r.getCreator());
            operatorIds.add(r.getUpdater());
        }

        return new ArrayList<>(operatorIds);
    };

    /**
     * is a role exist?
     */
    private final Consumer<RoleInsertParam> INSERT_ROLE_VALIDATOR = rip -> {
        if (isNull(rip))
            throw new BlueException(EMPTY_PARAM);
        rip.asserts();

        if (isNotNull(roleMapper.selectByName(rip.getName())))
            throw new BlueException(ROLE_NAME_ALREADY_EXIST);

        if (isNotNull(roleMapper.selectByLevel(rip.getLevel())))
            throw new BlueException(ROLE_LEVEL_ALREADY_EXIST);
    };

    /**
     * is a role exist?
     */
    private final Function<RoleUpdateParam, Role> UPDATE_ROLE_VALIDATOR_AND_ORIGIN_RETURNER = rup -> {
        if (isNull(rup))
            throw new BlueException(EMPTY_PARAM);
        rup.asserts();

        Long id = rup.getId();

        ofNullable(rup.getName())
                .filter(BlueChecker::isNotBlank)
                .map(roleMapper::selectByName)
                .map(Role::getId)
                .ifPresent(eid -> {
                    if (!id.equals(eid))
                        throw new BlueException(ROLE_NAME_ALREADY_EXIST);
                });

        ofNullable(rup.getLevel())
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

    /**
     * for role
     */
    public static final BiFunction<RoleUpdateParam, Role, Boolean> UPDATE_ROLE_VALIDATOR = (p, t) -> {
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

        return alteration;
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
        LOGGER.info("RoleInfo insertRole(RoleInsertParam roleInsertParam), roleInsertParam = {}, operatorId = {}", roleInsertParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        INSERT_ROLE_VALIDATOR.accept(roleInsertParam);
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
        LOGGER.info("RoleInfo updateRole(RoleInsertParam roleInsertParam), roleUpdateParam = {}, operatorId = {}", roleUpdateParam, operatorId);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED);

        Role role = UPDATE_ROLE_VALIDATOR_AND_ORIGIN_RETURNER.apply(roleUpdateParam);
        if (!UPDATE_ROLE_VALIDATOR.apply(roleUpdateParam, role))
            throw new BlueException(DATA_HAS_NOT_CHANGED);

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
        LOGGER.info("RoleInfo deleteRoleById(Long id, Long operatorId), id = {}", id);
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
        LOGGER.info("RoleManagerInfo updateDefaultRole(Long id, Long operatorId), id = {}， operatorId = {}", id, operatorId);
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

        Map<Long, String> idAndNameMapping;
        try {
            idAndNameMapping = rpcMemberBasicServiceConsumer.selectMemberBasicInfoMonoByIds(OPERATORS_GETTER.apply(singletonList(newDefaultRole))).toFuture().join()
                    .parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
        } catch (Exception e) {
            LOGGER.error("RoleManagerInfo updateDefaultRole(Long id, Long operatorId), generate idAndNameMapping failed, e = {}", e);
            idAndNameMapping = emptyMap();
        }

        return roleToRoleManagerInfo(newDefaultRole, ofNullable(idAndNameMapping.get(newDefaultRole.getCreator())).orElse(EMPTY_DATA.value),
                ofNullable(idAndNameMapping.get(newDefaultRole.getUpdater())).orElse(EMPTY_DATA.value));
    }

    /**
     * get role by role id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Role> getRole(Long id) {
        LOGGER.info("Optional<Role> getRoleById(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(roleMapper.selectByPrimaryKey(id));
    }

    /**
     * get default role
     *
     * @return
     */
    @Override
    public Role getDefaultRole() {
        LOGGER.info("Role getDefaultRole()");

        Role defaultRole = DEFAULT_ROLE_WITH_CACHE_SUP.get();

        LOGGER.info("defaultRole = {}", defaultRole);
        if (isNull(defaultRole)) {
            LOGGER.error("Role getDefaultRole(), default role not exist");
            throw new BlueException(INTERNAL_SERVER_ERROR);
        }

        return defaultRole;
    }

    /**
     * get role mono by role id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<Role>> getRoleMono(Long id) {
        LOGGER.info("Mono<Optional<Role>> getRoleMonoById(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return just(getRole(id));
    }

    /**
     * select roles by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<Role> selectRoleByIds(List<Long> ids) {
        LOGGER.info("Mono<List<Role>> selectRoleMonoByIds(List<Long> ids), ids = {}", ids);
        return isValidIdentities(ids) ? allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(roleMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList())
                :
                emptyList();
    }

    /**
     * select roles mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<Role>> selectRoleMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<Role>> selectRoleMonoByIds(List<Long> ids), ids = {}", ids);
        return just(this.selectRoleByIds(ids));
    }

    /**
     * select all roles
     *
     * @return
     */
    @Override
    public Mono<List<Role>> selectRole() {
        LOGGER.info("Mono<List<Role>> selectRole()");
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
    public Mono<List<Role>> selectRoleMonoByLimitAndCondition(Long limit, Long rows, RoleCondition roleCondition) {
        LOGGER.info("Mono<List<Role>> selectRoleMonoByLimitAndCondition(Long limit, Long rows, RoleCondition roleCondition), " +
                "limit = {}, rows = {}, roleCondition = {}", limit, rows, roleCondition);
        return just(roleMapper.selectByLimitAndCondition(limit, rows, roleCondition));
    }

    /**
     * count role by condition
     *
     * @param roleCondition
     * @return
     */
    @Override
    public Mono<Long> countRoleMonoByCondition(RoleCondition roleCondition) {
        LOGGER.info("Mono<Long> countRoleMonoByCondition(RoleCondition roleCondition), roleCondition = {}", roleCondition);
        return just(ofNullable(roleMapper.countByCondition(roleCondition)).orElse(0L));
    }

    /**
     * select role info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<RoleManagerInfo>> selectRoleManagerInfoPageMonoByPageAndCondition(PageModelRequest<RoleCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<RoleInfo>> selectRoleInfoPageMonoByPageAndCondition(PageModelRequest<RoleCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);

        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        RoleCondition roleCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectRoleMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), roleCondition), countRoleMonoByCondition(roleCondition))
                .flatMap(tuple2 -> {
                    List<Role> roles = tuple2.getT1();

                    return isNotEmpty(roles) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoMonoByIds(OPERATORS_GETTER.apply(roles))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(roles.stream().map(r ->
                                                roleToRoleManagerInfo(r, ofNullable(idAndNameMapping.get(r.getCreator())).orElse(EMPTY_DATA.value),
                                                        ofNullable(idAndNameMapping.get(r.getUpdater())).orElse(EMPTY_DATA.value))).collect(toList()));
                                    }).flatMap(resourceManagerInfos ->
                                            just(new PageModelResponse<>(resourceManagerInfos, tuple2.getT2())))
                            :
                            just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
                });
    }

}
