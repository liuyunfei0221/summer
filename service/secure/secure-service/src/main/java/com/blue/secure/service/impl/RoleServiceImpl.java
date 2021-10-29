package com.blue.secure.service.impl;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.config.deploy.BlockingDeploy;
import com.blue.secure.constant.RoleSortAttribute;
import com.blue.secure.model.RoleCondition;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.repository.mapper.RoleMapper;
import com.blue.secure.service.inter.RoleService;
import com.google.gson.Gson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.Asserter.*;
import static com.blue.base.common.base.ConstantProcessor.assertSortType;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static com.blue.secure.converter.SecureModelConverters.ROLE_2_ROLE_INFO_CONVERTER;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.isBlank;
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

    private final RoleMapper roleMapper;

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

    private static final Gson GSON = CommonFunctions.GSON;

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

    /**
     * select default role from database
     *
     * @return
     */
    private Role getDefaultRoleFromDb() {
        List<Role> defaultRoles = roleMapper.selectDefault();
        if (isEmpty(defaultRoles))
            throw new RuntimeException("defaultRoles can't be empty");
        if (defaultRoles.size() > 1)
            throw new RuntimeException("defaultRoles can't be more than 1");

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

    @PostConstruct
    public void init() {
        refreshDefaultRole();
    }

    /**
     * refresh default role
     *
     * @return
     */
    @Override
    public void refreshDefaultRole() {
        REDIS_DEFAULT_ROLE_CACHE.accept(getDefaultRoleFromCache());
        LOGGER.info("void refreshDefaultRole() -> SUCCESS");
    }

    /**
     * get default role
     *
     * @return
     */
    @Override
    public Role getDefaultRole() {
        LOGGER.info("getDefaultRole()");

        Role defaultRole = getDefaultRoleFromCache();

        LOGGER.info("defaultRole = {}", defaultRole);
        if (isNull(defaultRole)) {
            LOGGER.error("Role getDefaultRole(), default role not exist");
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);
        }

        return defaultRole;
    }

    /**
     * get role by role id
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
        LOGGER.info("List<Role> selectRoles()");
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

        return this.countRoleMonoByCondition(roleCondition)
                .flatMap(roleCount -> {
                    Mono<List<Role>> listMono = roleCount > 0L ? this.selectRoleMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), roleCondition) : just(emptyList());
                    return zip(listMono, just(roleCount));
                })
                .flatMap(tuple2 -> {
                    List<Role> roles = tuple2.getT1();
                    Mono<List<RoleInfo>> roleInfosMono = roles.size() > 0 ?
                            just(roles.stream()
                                    .map(ROLE_2_ROLE_INFO_CONVERTER).collect(toList()))
                            :
                            just(emptyList());

                    return roleInfosMono
                            .flatMap(roleInfos ->
                                    just(new PageModelResponse<>(roleInfos, tuple2.getT2())));
                });
    }

}
