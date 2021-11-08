package com.blue.secure.service.impl;

import com.blue.base.common.base.Asserter;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.config.deploy.BlockingDeploy;
import com.blue.secure.constant.RoleSortAttribute;
import com.blue.secure.model.RoleCondition;
import com.blue.secure.model.RoleInsertParam;
import com.blue.secure.model.RoleUpdateParam;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.repository.mapper.RoleMapper;
import com.blue.secure.service.inter.RoleService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.Asserter.*;
import static com.blue.base.common.base.CommonFunctions.GSON;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConstantProcessor.assertSortType;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.Default.DEFAULT;
import static com.blue.base.constant.base.Default.NOT_DEFAULT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.ResponseMessage.*;
import static com.blue.secure.converter.SecureModelConverters.ROLE_2_ROLE_INFO_CONVERTER;
import static com.blue.secure.converter.SecureModelConverters.ROLE_INSERT_PARAM_2_ROLE_CONVERTER;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;
import static reactor.util.Loggers.getLogger;

/**
 * role service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "CatchMayIgnoreException"})
@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = getLogger(RoleServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private RoleMapper roleMapper;

    private StringRedisTemplate stringRedisTemplate;

    private final RedissonClient redissonClient;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RoleServiceImpl(BlueIdentityProcessor blueIdentityProcessor, RoleMapper roleMapper, StringRedisTemplate stringRedisTemplate,
                           RedissonClient redissonClient, BlockingDeploy blockingDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.roleMapper = roleMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;

        MAX_WAITING_FOR_REFRESH = blockingDeploy.getBlockingMillis();
    }

    private static long MAX_WAITING_FOR_REFRESH;

    private static final String DEFAULT_ROLE_KEY = "SUMMER_DEFAULT_ROLE",
            DEFAULT_ROLE_REFRESH_KEY = "SUMMER_DEFAULT_ROLE_REFRESHING";

    private volatile Role defaultRole;

    private final Consumer<Role> REDIS_DEFAULT_ROLE_CACHE = role ->
            ofNullable(role)
                    .ifPresent(r -> {
                        stringRedisTemplate.opsForValue().set(DEFAULT_ROLE_KEY, GSON.toJson(r));
                        LOGGER.info("REDIS_CACHE_DEFAULT_ROLE_CACHE, r = {}", r);
                    });

    private final Supplier<Optional<Role>> REDIS_DEFAULT_ROLE_GETTER = () ->
            ofNullable(stringRedisTemplate.opsForValue().get(DEFAULT_ROLE_KEY))
                    .filter(StringUtils::hasText)
                    .map(v -> {
                        LOGGER.info("REDIS_DEFAULT_ROLE_GETTER, v = {}", v);
                        return GSON.fromJson(v, Role.class);
                    });

    private void deleteDefaultRoleFromCache() {
        stringRedisTemplate.delete(DEFAULT_ROLE_KEY);
    }

    /**
     * select default role from database
     *
     * @return
     */
    private Role getDefaultRoleFromDb() {
        List<Role> defaultRoles = roleMapper.selectDefault();
        if (isEmpty(defaultRoles))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);
        if (defaultRoles.size() > 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);

        Role defaultRole = defaultRoles.get(0);
        this.defaultRole = defaultRole;

        LOGGER.info("Role getDefaultRoleFromDb(), defaultRole = {]", defaultRole);
        return defaultRole;
    }

    /**
     * select default role or set to redis
     *
     * @return
     */
    private Role getDefaultRoleFromCache() {
        return REDIS_DEFAULT_ROLE_GETTER.get()
                .orElseGet(() -> {
                    RLock lock = redissonClient.getLock(DEFAULT_ROLE_REFRESH_KEY);
                    boolean tryLock = true;
                    try {
                        tryLock = lock.tryLock();
                        if (tryLock) {
                            Role defaultRole = getDefaultRoleFromDb();
                            REDIS_DEFAULT_ROLE_CACHE.accept(defaultRole);
                            return defaultRole;
                        }

                        long start = currentTimeMillis();
                        while (!(tryLock = lock.tryLock()) && currentTimeMillis() - start <= MAX_WAITING_FOR_REFRESH)
                            onSpinWait();

                        return tryLock ? REDIS_DEFAULT_ROLE_GETTER.get().orElse(defaultRole) : defaultRole;
                    } catch (Exception e) {
                        return getDefaultRoleFromDb();
                    } finally {
                        if (tryLock)
                            try {
                                lock.unlock();
                            } catch (Exception e) {
                            }
                    }
                });
    }

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = of(RoleSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Consumer<RoleCondition> CONDITION_REPACKAGER = condition -> {
        if (condition != null) {
            ofNullable(condition.getSortAttribute())
                    .filter(StringUtils::hasText)
                    .map(SORT_ATTRIBUTE_MAPPING::get)
                    .filter(StringUtils::hasText)
                    .ifPresent(condition::setSortAttribute);

            assertSortType(condition.getSortType(), true);

            ofNullable(condition.getName())
                    .filter(n -> !isBlank(n)).map(String::toLowerCase).ifPresent(n -> condition.setName("%" + n + "%"));
        }
    };

    /**
     * is a role exist?
     */
    private final Consumer<RoleInsertParam> INSERT_ROLE_VALIDATOR = rip -> {
        if (rip == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);

        String name = rip.getName();
        if (isBlank(name))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "name can't be blank");

        String description = rip.getDescription();
        if (isBlank(description))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "description can't be blank");

        if (isNotNull(roleMapper.selectByName(rip.getName())))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "The name already exists");
    };

    /**
     * is a role exist?
     */
    private final Function<RoleUpdateParam, Role> UPDATE_ROLE_VALIDATOR_AND_ORIGIN_RETURNER = rup -> {
        Long id = rup.getId();
        if (isInvalidIdentity(id))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        ofNullable(rup.getName())
                .filter(Asserter::isNotBlank)
                .map(roleMapper::selectByName)
                .map(Role::getId)
                .ifPresent(eid -> {
                    if (!id.equals(eid))
                        throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "The name already exist");
                });

        Role role = roleMapper.selectByPrimaryKey(id);
        if (role == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, DATA_NOT_EXIST.message);

        return role;
    };

    /**
     * for role
     */
    public static final BiFunction<RoleUpdateParam, Role, Boolean> ROLE_UPDATE_PARAM_AND_ROLE_COMPARER = (p, t) -> {
        if (!p.getId().equals(t.getId()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message);

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

        return alteration;
    };

    @PostConstruct
    public void init() {
        refreshDefaultRole();
    }

    /**
     * insert a new role
     *
     * @param roleInsertParam
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public RoleInfo insertRole(RoleInsertParam roleInsertParam, Long operatorId) {
        LOGGER.info("RoleInfo insertRole(RoleInsertParam roleInsertParam), roleInsertParam = {}, operatorId = {}", roleInsertParam, operatorId);
        if (isNull(roleInsertParam))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message);

        INSERT_ROLE_VALIDATOR.accept(roleInsertParam);
        Role role = ROLE_INSERT_PARAM_2_ROLE_CONVERTER.apply(roleInsertParam);

        long id = blueIdentityProcessor.generate(Role.class);
        role.setId(id);
        role.setCreator(operatorId);
        role.setUpdater(operatorId);

        roleMapper.insert(role);

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
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public RoleInfo updateRole(RoleUpdateParam roleUpdateParam, Long operatorId) {
        LOGGER.info("RoleInfo updateRole(RoleInsertParam roleInsertParam), roleUpdateParam = {}, operatorId = {}", roleUpdateParam, operatorId);
        if (isNull(roleUpdateParam))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message);

        Role role = UPDATE_ROLE_VALIDATOR_AND_ORIGIN_RETURNER.apply(roleUpdateParam);
        if (ROLE_UPDATE_PARAM_AND_ROLE_COMPARER.apply(roleUpdateParam, role)) {
            roleMapper.updateByPrimaryKeySelective(role);
            return ROLE_2_ROLE_INFO_CONVERTER.apply(role);
        }

        throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "data has no change");
    }

    /**
     * delete role
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public RoleInfo deleteRoleById(Long id) {
        LOGGER.info("RoleInfo deleteRoleById(Long id, Long operatorId), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        Role role = roleMapper.selectByPrimaryKey(id);
        if (role != null) {
            roleMapper.deleteByPrimaryKey(id);
            return ROLE_2_ROLE_INFO_CONVERTER.apply(role);
        }

        throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, DATA_NOT_EXIST.message);
    }


    /**
     * refresh default role
     *
     * @return
     */
    @Override
    public void refreshDefaultRole() {
        REDIS_DEFAULT_ROLE_CACHE.accept(getDefaultRoleFromDb());
        LOGGER.info("void refreshDefaultRole() -> SUCCESS");
    }

    /**
     * get default role
     *
     * @return
     */
    @Override
    public Role getDefaultRole() {
        LOGGER.info("Role getDefaultRole()");

        Role defaultRole = getDefaultRoleFromCache();

        LOGGER.info("defaultRole = {}", defaultRole);
        if (isNull(defaultRole)) {
            LOGGER.error("Role getDefaultRole(), default role not exist");
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);
        }

        return defaultRole;
    }

    /**
     * update default role by role id
     *
     * @param id
     * @param operatorId
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public void updateDefaultRole(Long id, Long operatorId) {
        LOGGER.info("void updateDefaultRole(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        Role role = roleMapper.selectByPrimaryKey(id);
        Role defaultRole = getDefaultRoleFromDb();

        if (role.getId().equals(defaultRole.getId()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "role is already default");

        Long stamp = TIME_STAMP_GETTER.get();

        role.setIsDefault(DEFAULT.status);
        role.setUpdater(operatorId);
        role.setUpdateTime(stamp);

        defaultRole.setIsDefault(NOT_DEFAULT.status);
        defaultRole.setUpdater(operatorId);
        defaultRole.setUpdateTime(stamp);

        deleteDefaultRoleFromCache();
        roleMapper.updateByPrimaryKey(role);
        roleMapper.updateByPrimaryKey(defaultRole);
        deleteDefaultRoleFromCache();
    }

    /**
     * get role by role id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Role> getRoleById(Long id) {
        LOGGER.info("Optional<Role> getRoleById(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return ofNullable(roleMapper.selectByPrimaryKey(id));
    }

    /**
     * get role mono by role id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Optional<Role>> getRoleMonoById(Long id) {
        LOGGER.info("Mono<Optional<Role>> getRoleMonoById(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return just(ofNullable(roleMapper.selectByPrimaryKey(id)));
    }

    /**
     * select roles by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<Role>> selectRoleMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<Role>> selectRoleMonoByIds(List<Long> ids), ids = {}", ids);
        return isValidIdentities(ids) ? just(allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(roleMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList()))
                :
                just(emptyList());
    }

    /**
     * select all roles
     *
     * @return
     */
    @Override
    public Mono<List<Role>> selectRole() {
        LOGGER.info("Mono<List<Role>> selectRole()");
        return just(roleMapper.select());
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
    public Mono<PageModelResponse<RoleInfo>> selectRoleInfoPageMonoByPageAndCondition(PageModelRequest<RoleCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<RoleInfo>> selectRoleInfoPageMonoByPageAndCondition(PageModelRequest<RoleCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);

        RoleCondition roleCondition = pageModelRequest.getParam();
        CONDITION_REPACKAGER.accept(roleCondition);

        return zip(selectRoleMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), roleCondition), countRoleMonoByCondition(roleCondition))
                .flatMap(tuple2 -> {
                    List<Role> roles = tuple2.getT1();
                    Mono<List<RoleInfo>> roleInfosMono = roles.size() > 0 ?
                            just(roles.stream().map(ROLE_2_ROLE_INFO_CONVERTER).collect(toList()))
                            :
                            just(emptyList());

                    return roleInfosMono
                            .flatMap(roleInfos ->
                                    just(new PageModelResponse<>(roleInfos, tuple2.getT2())));
                });
    }

}
