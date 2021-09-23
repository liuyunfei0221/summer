package com.blue.secure.service.impl;

import com.blue.base.common.base.CommonFunctions;
import com.blue.base.common.base.RsaProcessor;
import com.blue.base.constant.base.CacheKey;
import com.blue.base.constant.base.Symbol;
import com.blue.base.constant.secure.AuthInfoRefreshElementType;
import com.blue.base.constant.secure.DeviceType;
import com.blue.base.constant.secure.LoginType;
import com.blue.base.model.base.Access;
import com.blue.base.model.base.KeyPair;
import com.blue.base.model.exps.BlueException;
import com.blue.jwt.common.JwtProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.secure.api.model.*;
import com.blue.secure.component.auth.AuthInfoCacher;
import com.blue.secure.config.deploy.BlockingDeploy;
import com.blue.secure.config.deploy.SessionKeyDeploy;
import com.blue.secure.config.mq.producer.InvalidLocalAuthProducer;
import com.blue.secure.repository.entity.MemberRoleRelation;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.repository.entity.RoleResRelation;
import com.blue.secure.service.inter.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.*;
import java.util.stream.Stream;

import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.SecKey.NOT_LOGGED_IN_SEC_KEY;
import static com.blue.base.constant.base.ThresholdNumericalValue.*;
import static com.blue.base.constant.secure.AuthInfoRefreshElementType.PUB_KEY;
import static com.blue.base.constant.secure.AuthInfoRefreshElementType.ROLE;
import static com.blue.base.constant.secure.DeviceType.UNKNOWN;
import static com.blue.base.constant.secure.LoginType.*;
import static com.blue.secure.converter.SecureModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER;
import static com.blue.secure.converter.SecureModelConverters.ROLE_2_ROLE_INFO_CONVERTER;
import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * 用户认证授权业务实现
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "CatchMayIgnoreException", "AliControlFlowStatementWithoutBraces"})
@Service
public class SecureServiceImpl implements SecureService {

    private static final Logger LOGGER = getLogger(SecureServiceImpl.class);

    private JwtProcessor<MemberPayload> jwtProcessor;

    private final AuthInfoCacher authInfoCacher;

    private final StringRedisTemplate stringRedisTemplate;

    private final MemberAuthService memberAuthService;

    private final MemberRoleRelationService memberRoleRelationService;

    private final RoleService roleService;

    private final RoleResRelationService roleResRelationService;

    private final ResourceService resourceService;

    private final InvalidLocalAuthProducer invalidLocalAuthProducer;

    private final ExecutorService executorService;

    private final RedissonClient redissonClient;

    private final SessionKeyDeploy sessionKeyDeploy;

    private final BlockingDeploy blockingDeploy;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SecureServiceImpl(JwtProcessor<MemberPayload> jwtProcessor, AuthInfoCacher authInfoCacher, StringRedisTemplate stringRedisTemplate,
                             MemberAuthService memberAuthService, MemberRoleRelationService memberRoleRelationService, RoleService roleService,
                             RoleResRelationService roleResRelationService, ResourceService resourceService, InvalidLocalAuthProducer invalidLocalAuthProducer,
                             ExecutorService executorService, RedissonClient redissonClient, SessionKeyDeploy sessionKeyDeploy, BlockingDeploy blockingDeploy) {
        this.jwtProcessor = jwtProcessor;
        this.authInfoCacher = authInfoCacher;
        this.stringRedisTemplate = stringRedisTemplate;
        this.memberAuthService = memberAuthService;
        this.memberRoleRelationService = memberRoleRelationService;
        this.roleService = roleService;
        this.roleResRelationService = roleResRelationService;
        this.resourceService = resourceService;
        this.invalidLocalAuthProducer = invalidLocalAuthProducer;
        this.executorService = executorService;
        this.redissonClient = redissonClient;
        this.sessionKeyDeploy = sessionKeyDeploy;
        this.blockingDeploy = blockingDeploy;
    }

    private static final Gson GSON = CommonFunctions.GSON;

    private static final Supplier<Long> TIME_STAMP_GETTER = CommonFunctions.TIME_STAMP_GETTER;

    /**
     * 随机信息长度
     */
    private static int RANDOM_ID_LENGTH;

    /**
     * 刷新资源时最大等待时间
     */
    private static long MAX_WAITING_FOR_REFRESH;

    /**
     * 符号
     */
    public static final String
            SESSION_KEY_PRE = CacheKey.SESSION_KEY_PRE.key,
            AUTH_REFRESH_KEY_PRE = CacheKey.AUTH_REFRESH_KEY_PRE.key,
            PAR_CONCATENATION = Symbol.PAR_CONCATENATION.identity,
            PATH_SEPARATOR = Symbol.PATH_SEPARATOR.identity;

    private static final List<LoginType> LOGIN_TYPES = of(LoginType.values())
            .collect(toList());

    private static final List<DeviceType> DEVICE_TYPES = of(DeviceType.values())
            .collect(toList());

    /**
     * 初始化资源key构建规则(method, uri)
     */
    private static final BiFunction<String, String, String> INIT_RES_KEY_GENERATOR = CommonFunctions.INIT_RES_KEY_GENERATOR;

    /**
     * 带服务前缀的uri
     */
    private static final Function<Resource, String> REAL_URI_GETTER = resource ->
            PATH_SEPARATOR + resource.getModule().intern() + resource.getUri().intern();

    /**
     * 请求资源key构建规则(method, uri)
     */
    private static final BiFunction<String, String, String> REQ_RES_KEY_GENERATOR = CommonFunctions.REQ_RES_KEY_GENERATOR;

    /**
     * 常用异常
     */
    private static final BlueException
            UNAUTHORIZED_EXP = new BlueException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message),
            NOT_FOUND_EXP = new BlueException(NOT_FOUND.status, NOT_FOUND.code, NOT_FOUND.message),
            INTERNAL_SERVER_ERROR_EXP = new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);

    /**
     * 资源信息或关联关系更新标记位
     */
    private volatile boolean resourceOrRelationRefreshing = true;

    /**
     * 角色与资源特征映射集
     */
    private volatile Map<Long, Set<String>> roleAndResourcesKeyMapping = emptyMap();

    /**
     * 角色id与角色资源映射
     */
    private volatile Map<Long, RoleInfo> idAndRoleInfoMapping = emptyMap();

    /**
     * 角色与资源信息映射集
     */
    private volatile Map<Long, List<ResourceInfo>> roleAndResourceInfosMapping = emptyMap();

    /**
     * 资源特征与资源信息映射集
     */
    private volatile Map<String, Resource> keyAndResourceMapping = emptyMap();

    /**
     * 角色信息更新标记位
     */
    private volatile boolean roleRefreshing = false;

    /**
     * jwt解析器
     */
    private final Function<String, MemberPayload> JWT_PARSER = jwt -> {
        MemberPayload memberPayload;
        try {
            memberPayload = jwtProcessor.parse(jwt);
        } catch (Exception e) {
            throw UNAUTHORIZED_EXP;
        }
        return memberPayload;
    };

    /**
     * 资源信息动态刷新时的阻塞器
     */
    private final Supplier<Boolean> RESOURCE_OR_REL_REFRESHING_BLOCKER = () -> {
        if (resourceOrRelationRefreshing) {
            long start = currentTimeMillis();
            while (resourceOrRelationRefreshing) {
                if (currentTimeMillis() - start > MAX_WAITING_FOR_REFRESH)
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "资源信息刷新超时");
                onSpinWait();
            }
        }
        return true;
    };

    /**
     * 资源信息动态刷新时的阻塞器
     */
    private final Supplier<Boolean> ROLE_REFRESHING_BLOCKER = () -> {
        if (roleRefreshing) {
            long start = currentTimeMillis();
            while (roleRefreshing) {
                if (currentTimeMillis() - start > MAX_WAITING_FOR_REFRESH)
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "角色信息刷新超时");
                onSpinWait();
            }
        }
        return true;
    };

    /**
     * 根据资源标识获取资源
     */
    private final Function<String, Resource> RESOURCE_GETTER = resourceKey -> {
        RESOURCE_OR_REL_REFRESHING_BLOCKER.get();
        return keyAndResourceMapping.get(resourceKey);
    };

    /**
     * 访问权限检查器
     */
    private final BiFunction<Long, String, Boolean> AUTHORIZATION_RES_CHECKER = (roleId, resourceKey) -> {
        RESOURCE_OR_REL_REFRESHING_BLOCKER.get();
        return ofNullable(roleAndResourcesKeyMapping.get(roleId)).map(set -> set.contains(resourceKey)).orElse(false);
    };

    /**
     * 资源名称获取器
     */
    private final UnaryOperator<String> RES_NOT_ACCESS_MESSAGE_GETTER = resourceKey -> {
        RESOURCE_OR_REL_REFRESHING_BLOCKER.get();
        return "无权访问" + ofNullable(keyAndResourceMapping.get(resourceKey))
                .map(Resource::getName).orElse("该资源");
    };

    /**
     * identity与loginType映射
     */
    private static final Map<String, LoginType> IDENTITY_NATURE_MAPPING = Stream.of(LoginType.values())
            .collect(toMap(lt -> lt.identity, lt -> lt, (a, b) -> a));

    /**
     * identity转nature
     */
    private static final UnaryOperator<String> LOGIN_TYPE_2_NATURE_CONVERTER = identity -> {
        if (identity != null && !"".equals(identity)) {
            LoginType loginType = IDENTITY_NATURE_MAPPING.get(identity);
            if (loginType != null)
                return loginType.nature.intern();
            throw new RuntimeException("loginType with identity " + identity + " is not exist");
        }
        throw new RuntimeException("identity can't be null or ''");
    };

    /**
     * 无需认证响应数据
     */
    private final Function<Resource, Mono<AuthAsserted>> NO_AUTH_REQUIRED_RES_GEN = resource ->
            just(new AuthAsserted(
                    false, true, true,
                    resource.getExistenceRequestBody(),
                    resource.getExistenceResponseBody(),
                    NOT_LOGGED_IN_SEC_KEY.value,
                    new Access(NOT_LOGGED_IN_MEMBER_ID.value,
                            NOT_LOGGED_IN_ROLE_ID.value, NOT_LOGGED_IN.identity, UNKNOWN.identity,
                            NOT_LOGGED_IN_TIME.value), "资源无需认证访问"));

    /**
     * 登录类型模板方法
     */
    private Map<String, Function<ClientLoginParam, Mono<MemberBasicInfo>>> clientLoginHandlers;

    /**
     * 初始化免认证资源信息与角色资源映射等
     */
    private void generateLoginHandler() {
        clientLoginHandlers = new HashMap<>(16, 1.0f);
        clientLoginHandlers.put(SMS_VERIGY.identity, memberAuthService::getMemberByPhoneWithAssertVerify);
        clientLoginHandlers.put(PHONE_PWD.identity, memberAuthService::getMemberByPhoneWithAssertPwd);
        clientLoginHandlers.put(EMAIL_PWD.identity, memberAuthService::getMemberByEmailWithAssertPwd);

        LOGGER.info("generateLoginHandler(), clientLoginHandlers = {}", clientLoginHandlers);
    }

    /**
     * 登录处理器获取器
     */
    private final Function<String, Function<ClientLoginParam, Mono<MemberBasicInfo>>> LOGIN_HANDLER_GETTER = loginType -> {
        LOGGER.info("LOGIN_HANDLER_GETTER, loginType = {}", loginType);
        if (loginType == null || "".equals(loginType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "loginType不能为空或''");

        Function<ClientLoginParam, Mono<MemberBasicInfo>> loginFunc = clientLoginHandlers.get(loginType);
        if (loginFunc == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "客户端登录不支持该登录类型 " + loginType);

        return loginFunc;
    };

    /**
     * authInfo属性重新封装器
     */
    private static final Map<AuthInfoRefreshElementType, BiConsumer<AuthInfo, String>> RE_PACKAGERS = new HashMap<>();

    /**
     * business同步key构建器
     */
    private static final Map<AuthInfoRefreshElementType, BinaryOperator<String>> AUTH_REFRESH_KEY_GENS = new HashMap<>();

    static {
        RE_PACKAGERS.put(ROLE, (ai, ele) -> {
            if (ai == null || ele == null || "".equals(ele))
                throw new RuntimeException("ai,ele均不能为空");

            long roleId;
            try {
                roleId = Long.parseLong(ele);
            } catch (NumberFormatException e) {
                throw new RuntimeException("角色id类型元素类型转换失败");
            }
            ai.setRoleId(roleId);
        });
        RE_PACKAGERS.put(PUB_KEY, (ai, ele) -> {
            if (ai == null || ele == null || "".equals(ele))
                throw new RuntimeException("ai,ele均不能为空");
            ai.setPubKey(ele);
        });

        AUTH_REFRESH_KEY_GENS.put(ROLE, (snKey, eleValue) ->
                AUTH_REFRESH_KEY_PRE + snKey + PAR_CONCATENATION + ROLE.name() + PAR_CONCATENATION + eleValue);
        AUTH_REFRESH_KEY_GENS.put(PUB_KEY, (snKey, eleValue) ->
                AUTH_REFRESH_KEY_PRE + snKey + PAR_CONCATENATION + PUB_KEY.name() + PAR_CONCATENATION + eleValue.hashCode());

        LOGGER.info("static codes, RE_PACKAGERS = {}, AUTH_REFRESH_KEY_GENS = {}", RE_PACKAGERS, AUTH_REFRESH_KEY_GENS);
    }

    /**
     * 根据角色id查询资源集的获取器
     */
    private final Function<Long, Optional<RoleInfo>> ROLE_INFO_BY_ID_GETTER = roleId -> {
        ROLE_REFRESHING_BLOCKER.get();
        return ofNullable(idAndRoleInfoMapping.get(roleId));
    };

    /**
     * 根据角色id查询资源集的获取器
     */
    private final Function<Long, List<ResourceInfo>> RESOURCE_INFOS_BY_ROLE_ID_GETTER = roleId -> {
        RESOURCE_OR_REL_REFRESHING_BLOCKER.get();
        return roleAndResourceInfosMapping.get(roleId);
    };

    /**
     * 构建redis sessionKey
     *
     * @param id
     * @param loginTypeIdentity
     * @param deviceTypeIdentity
     * @return
     */
    private String genSessionKey(Long id, String loginTypeIdentity, String deviceTypeIdentity) {
        LOGGER.info("genSessionKey(Long id, String loginTypeIdentity, String deviceTypeIdentity), id = {}, loginTypeIdentity = {}, deviceTypeIdentity = {}", id, loginTypeIdentity, deviceTypeIdentity);
        if (id == null || id < 1L || deviceTypeIdentity == null || "".equals(deviceTypeIdentity))
            throw new RuntimeException("id或loginType或deviceType不能为空");

        return SESSION_KEY_PRE + id + PAR_CONCATENATION + LOGIN_TYPE_2_NATURE_CONVERTER.apply(loginTypeIdentity).intern() + PAR_CONCATENATION + deviceTypeIdentity;
    }

    /**
     * 构建全局同步key
     */
    private static final UnaryOperator<String> GLOBAL_LOCK_KEY_GEN = snKey ->
            valueOf(snKey.hashCode());

    /**
     * 权限业务同步key
     *
     * @param snKey
     * @param elementType
     * @param elementValue
     * @return
     */
    private String genAuthRefreshLockKey(String snKey, AuthInfoRefreshElementType elementType, String elementValue) {
        LOGGER.info("genAuthRefreshLockKey(String snKey, AuthInfoRefreshElementType elementType, String elementValue), snKey = {}, elementType = {}, elementValue = {}", snKey, elementType, elementValue);
        if (snKey == null || "".equals(snKey) || elementType == null || elementValue == null || "".equals(elementValue))
            throw new RuntimeException("id或loginType或deviceType不能为空");

        return AUTH_REFRESH_KEY_GENS.get(elementType).apply(snKey, elementValue);
    }

    /**
     * 刷新authInfo中的元素
     *
     * @param keyId
     * @param elementType
     * @param elementValue
     */
    private void refreshAuthInfoElementByKeyId(String keyId, AuthInfoRefreshElementType elementType, String elementValue) {
        LOGGER.info("refreshAuthInfoElementByKeyId(String keyId, AuthInfoRefreshElementType elementType, String elementValue), keyId = {}, elementType = {}, elementValue = {}", keyId, elementType, elementValue);
        String originalAuthInfoJson = ofNullable(stringRedisTemplate.opsForValue().get(keyId))
                .orElse("");

        if ("".equals(originalAuthInfoJson)) {
            LOGGER.info("用户的authInfo为空,未完成角色动态刷新");
            return;
        }

        AuthInfo authInfo;
        try {
            authInfo = GSON.fromJson(originalAuthInfoJson, AuthInfo.class);
        } catch (JsonSyntaxException e) {
            LOGGER.info("用户的authInfo非法,未完成角色动态刷新");
            return;
        }

        String authRefreshKeyId = genAuthRefreshLockKey(keyId, elementType, elementValue);

        RLock authRefreshLock = redissonClient.getLock(authRefreshKeyId);
        boolean tryBusinessLock = false;
        try {
            if (!(tryBusinessLock = authRefreshLock.tryLock()))
                return;

            String globalLockKey = GLOBAL_LOCK_KEY_GEN.apply(keyId);
            RLock globalLock = redissonClient.getLock(globalLockKey);
            try {
                globalLock.lock(MAX_WAITING_FOR_REFRESH, SECONDS);
                RE_PACKAGERS.get(elementType).accept(authInfo, elementValue);
                stringRedisTemplate.opsForValue().set(keyId, GSON.toJson(authInfo), Duration.of(ofNullable(stringRedisTemplate.getExpire(keyId, SECONDS)).orElse(0L), ChronoUnit.SECONDS));
            } catch (Exception exception) {
                LOGGER.error("exception = {}", exception);
            } finally {
                try {
                    globalLock.unlock();
                } catch (Exception e) {
                    LOGGER.error("globalLock已超时释放");
                }
            }
        } catch (Exception exception) {
            LOGGER.error("exception = {}", exception);
        } finally {
            if (tryBusinessLock) {
                try {
                    authRefreshLock.unlock();
                } catch (Exception e) {
                }
                try {
                    invalidLocalAuthProducer.send(new InvalidLocalAuthParam(keyId));
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 动态刷新认证的信息的元素
     *
     * @param authInfoRefreshParam
     */
    public void refreshAuthElementMultiTypes(AuthInfoRefreshParam authInfoRefreshParam) {
        LOGGER.info("refreshAuthElementMultiTypes(AuthInfoRefreshParam authInfoRefreshParam), authInfoRefreshParam = {}", authInfoRefreshParam);
        AuthInfoRefreshElementType elementType = authInfoRefreshParam.getElementType();
        if (elementType == null)
            throw new RuntimeException("elementType can't be null");

        String elementValue = authInfoRefreshParam.getElementValue();
        if (elementValue == null || "".equals(elementValue))
            throw new RuntimeException("elementValue can't be null");

        List<String> loginTypes = ofNullable(authInfoRefreshParam.getLoginTypes())
                .filter(lts -> lts.size() > 0)
                .map(lts ->
                        lts.stream()
                                .map(lt -> lt.identity)
                                .distinct()
                                .filter(lti -> !lti.equals(NOT_LOGGED_IN.identity))
                                .collect(toList())
                ).orElse(emptyList());
        if (isEmpty(loginTypes))
            return;

        List<String> deviceTypes = ofNullable(authInfoRefreshParam.getDeviceTypes())
                .filter(dts -> dts.size() > 0)
                .map(dts ->
                        dts.stream()
                                .map(dt -> dt.identity)
                                .distinct()
                                .filter(dti -> !dti.equals(UNKNOWN.identity))
                                .collect(toList())
                ).orElse(emptyList());
        if (isEmpty(deviceTypes))
            return;

        for (String loginType : loginTypes)
            for (String deviceType : deviceTypes)
                refreshAuthInfoElementByKeyId(
                        genSessionKey(authInfoRefreshParam.getMemberId(), loginType.intern(), deviceType.intern()), elementType, elementValue);
    }

    /**
     * 通用同步构建权限信息
     *
     * @param roleId
     * @return
     */
    private Authority getAuthorityByRoleId(Long roleId) {
        LOGGER.info("getAuthorityByRoleOpt(Long roleId), roleId = {}", roleId);
        return ROLE_INFO_BY_ID_GETTER.apply(roleId)
                .map(role ->
                        new Authority(role,
                                RESOURCE_INFOS_BY_ROLE_ID_GETTER.apply(roleId)))
                .orElseThrow(() ->
                        new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access的角色为空"));
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        MAX_WAITING_FOR_REFRESH = blockingDeploy.getBlockingMillis();
        RANDOM_ID_LENGTH = sessionKeyDeploy.getRanLen();

        refreshResourceKeyOrRelation();
        refreshRoleInfo();
        generateLoginHandler();
    }

    /**
     * 更新资源或关联信息
     *
     * @return
     */
    @Override
    public boolean refreshResourceKeyOrRelation() {
        LOGGER.info("refreshResourceKeyOrRelation()");

        //查询所有资源信息与所有角色资源关联信息
        CompletableFuture<List<Resource>> resourceListCf =
                supplyAsync(resourceService::listResource, executorService);
        CompletableFuture<List<RoleResRelation>> roleResRelationListCf =
                supplyAsync(roleResRelationService::listRoleResRelation, executorService);

        //构建资源id与资源的临时映射
        List<Resource> resources = resourceListCf.join();
        Map<Long, Resource> idAndResourceMapping = resources
                .parallelStream()
                .collect(toMap(Resource::getId, e -> e, (a, b) -> a));

        //角色id与资源key集的映射
        List<RoleResRelation> roleResRelations = roleResRelationListCf.join();
        Map<Long, Set<String>> tempRoleAndResourcesKeyMapping = roleResRelations
                .parallelStream()
                .collect(groupingBy(RoleResRelation::getRoleId))
                .entrySet()
                .stream().collect(toMap(Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .map(RoleResRelation::getResId)
                                .map(idAndResourceMapping::get)
                                .filter(Objects::nonNull)
                                .map(r ->
                                        INIT_RES_KEY_GENERATOR.apply(r.getRequestMethod().toUpperCase().intern(),
                                                REAL_URI_GETTER.apply(r).intern()).intern())
                                .collect(toSet()), (a, b) -> a));

        //角色id与资源集的映射
        Map<Long, List<ResourceInfo>> tempRoleAndResourceInfosMapping = roleResRelations
                .parallelStream()
                .collect(groupingBy(RoleResRelation::getRoleId))
                .entrySet()
                .stream().collect(toMap(Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .map(RoleResRelation::getResId)
                                .map(idAndResourceMapping::get)
                                .filter(Objects::nonNull)
                                .distinct()
                                .map(RESOURCE_2_RESOURCE_INFO_CONVERTER)
                                .collect(toList())
                        , (a, b) -> a));

        //初始化资源特征与资源的映射集
        Map<String, Resource> tempKeyAndResourceMapping = resources
                .parallelStream()
                .collect(toMap(r -> INIT_RES_KEY_GENERATOR.apply(r.getRequestMethod().toUpperCase().intern(),
                        REAL_URI_GETTER.apply(r).intern()), r -> r, (a, b) -> a));

        resourceOrRelationRefreshing = true;

        keyAndResourceMapping = tempKeyAndResourceMapping;
        roleAndResourcesKeyMapping = tempRoleAndResourcesKeyMapping;
        roleAndResourceInfosMapping = tempRoleAndResourceInfosMapping;

        resourceOrRelationRefreshing = false;

        return true;
    }

    /**
     * 更新角色信息
     *
     * @return
     */
    @Override
    public boolean refreshRoleInfo() {
        LOGGER.info("refreshRoleInfo()");

        //所有角色
        List<Role> roles = roleService.listRoles();
        Map<Long, RoleInfo> tempIdAndRoleInfoMapping = roles
                .parallelStream()
                .map(ROLE_2_ROLE_INFO_CONVERTER)
                .collect(toMap(RoleInfo::getId, r -> r, (a, b) -> a));

        roleRefreshing = true;
        idAndRoleInfoMapping = tempIdAndRoleInfoMapping;
        roleRefreshing = false;

        return true;
    }

    /**
     * 客户端登录
     *
     * @param clientLoginParam
     * @return
     */
    @Override
    public Mono<MemberAuth> loginByClient(ClientLoginParam clientLoginParam) {
        LOGGER.info("loginByClient(ClientLoginParam clientLoginParam), clientLoginParam = {}", clientLoginParam);

        return zip(
                LOGIN_HANDLER_GETTER.apply(clientLoginParam.getLoginType())
                        .apply(clientLoginParam),
                just(clientLoginParam.getLoginType().intern()),
                just(clientLoginParam.getDeviceType().intern()))
                .flatMap(t3 -> {
                    MemberBasicInfo memberBasicInfo = t3.getT1();
                    LOGGER.info("memberBasicInfo = {}", memberBasicInfo);
                    Long mid = memberBasicInfo.getId();

                    return just(roleService.getRoleIdByMemberId(mid))
                            .flatMap(ridOpt ->
                                    ridOpt.map(rid ->
                                                    just(new AuthGenParam(mid, rid, t3.getT2(), t3.getT3())))
                                            .orElseGet(() -> {
                                                LOGGER.info("系统错误,成员的角色不存在, mid = {}", mid);
                                                return error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "成员角色不存在"));
                                            }));
                }).flatMap(this::generateAuth);
    }

    /**
     * 小程序登录
     *
     * @param miniProLoginParam
     * @return
     */
    @Override
    public Mono<MemberAuth> loginByMiniPro(MiniProLoginParam miniProLoginParam) {
        LOGGER.info("loginByMiniPro(MiniProLoginParam miniProLoginParam), miniProLoginParam = {}", miniProLoginParam);
        return null;
    }

    /**
     * 微信登录
     *
     * @param wechatProLoginParam
     * @return
     */
    @Override
    public Mono<MemberAuth> loginByWechat(WechatProLoginParam wechatProLoginParam) {
        LOGGER.info("loginByWechat(WechatProLoginParam wechatProLoginParam), wechatProLoginParam = {}", wechatProLoginParam);
        return null;
    }

    /**
     * 认证并鉴权,除断言外,任何时候都返回AuthAssertResult
     *
     * @param assertAuth
     * @return
     */
    @Override
    public Mono<AuthAsserted> assertAuth(AssertAuth assertAuth) {
        LOGGER.info("assertAuth(AssertAuth assertAuth), assertAuth = {}", assertAuth);
        return just(assertAuth)
                .switchIfEmpty(error(UNAUTHORIZED_EXP))
                .flatMap(aa -> {
                    String resourceKey = REQ_RES_KEY_GENERATOR.apply(
                            aa.getMethod().intern(), aa.getUri());
                    LOGGER.warn("resourceKey = {}", resourceKey);

                    Resource resource = RESOURCE_GETTER.apply(resourceKey);
                    if (resource == null)
                        return error(NOT_FOUND_EXP);

                    if (!resource.getAuthenticate())
                        return NO_AUTH_REQUIRED_RES_GEN.apply(resource);

                    String jwt = aa.getAuthentication();
                    MemberPayload memberPayload = JWT_PARSER.apply(jwt);

                    return authInfoCacher.getAuthInfo(memberPayload.getKeyId())
                            .flatMap(v -> {
                                if (v == null || "".equals(v))
                                    return error(UNAUTHORIZED_EXP);
                                AuthInfo authInfo;
                                try {
                                    authInfo = GSON.fromJson(v, AuthInfo.class);
                                } catch (JsonSyntaxException e) {
                                    return error(UNAUTHORIZED_EXP);
                                }
                                if (!jwt.equals(authInfo.getJwt()))
                                    return error(UNAUTHORIZED_EXP);
                                if (!AUTHORIZATION_RES_CHECKER.apply(authInfo.getRoleId(), resourceKey))
                                    return error(new BlueException(FORBIDDEN.status, FORBIDDEN.code, RES_NOT_ACCESS_MESSAGE_GETTER.apply(resourceKey)));

                                boolean preUnDecryption = resource.getPreUnDecryption();
                                boolean postUnEncryption = resource.getPostUnEncryption();

                                //认证并鉴权通过响应数据
                                AuthAsserted assertResult = new AuthAsserted(true, preUnDecryption, postUnEncryption, resource.getExistenceRequestBody(), resource.getExistenceResponseBody(),
                                        preUnDecryption && postUnEncryption ? "" : authInfo.getPubKey(),
                                        new Access(parseLong(memberPayload.getId()), authInfo.getRoleId(), memberPayload.getLoginType().intern(),
                                                memberPayload.getDeviceType().intern(), parseLong(memberPayload.getLoginTime())), "认证与鉴权通过");

                                LOGGER.info("assertResult = {}", assertResult);

                                return just(assertResult);
                            });
                });
    }

    /**
     * 生成用户jwt信息
     *
     * @param authGenParam
     * @return
     */
    @Override
    public Mono<MemberAuth> generateAuth(AuthGenParam authGenParam) {
        LOGGER.info("generateAuth(AuthGenParam authGenParam), authGenParam = {}", authGenParam);
        if (authGenParam == null)
            return error(INTERNAL_SERVER_ERROR_EXP);

        return just(authGenParam)
                .flatMap(agp -> {
                    Long memberId = agp.getMemberId();
                    String loginType = agp.getLoginType().intern();
                    String deviceType = agp.getDeviceType().intern();
                    KeyPair keyPair = RsaProcessor.initKeyPair();
                    //构建UserPayload
                    return just(new MemberPayload(
                            randomAlphanumeric(RANDOM_ID_LENGTH),
                            genSessionKey(memberId, loginType, deviceType),
                            valueOf(memberId),
                            loginType, deviceType,
                            valueOf(TIME_STAMP_GETTER.get()))
                    ).flatMap(mp -> {
                        //生成authInfo并落入REDIS
                        String jwt = jwtProcessor.create(mp);
                        String authInfoJson = GSON.toJson(new AuthInfo(jwt, authGenParam.getRoleId(), keyPair.getPubKey()));

                        return authInfoCacher.setAuthInfo(mp.getKeyId(), authInfoJson)
                                .flatMap(b -> {
                                    LOGGER.info("authInfoJson = {}, keyPairDTO = {}", authInfoJson, keyPair);
                                    if (b)
                                        return just(new MemberAuth(jwt, keyPair.getPriKey()));

                                    LOGGER.error("authInfoCacher.setAuthInfo(mp.getKeyId(), authInfoJson), failed");
                                    return error(INTERNAL_SERVER_ERROR_EXP);
                                });
                    });
                });
    }

    /**
     * 根据accessInfo清除认证信息
     *
     * @param access
     * @return
     */
    @Override
    public Mono<Boolean> invalidAuthByAccess(Access access) {
        LOGGER.info("invalidAuthByAccess(Access access), access = {}", access);
        if (access != null)
            return authInfoCacher.invalidAuthInfo(genSessionKey(access.getId(), access.getLoginType().intern(), access.getDeviceType().intern()));

        throw UNAUTHORIZED_EXP;
    }

    /**
     * 根据jwt清除认证信息
     *
     * @param jwt
     * @return
     */
    @Override
    public Mono<Boolean> invalidAuthByJwt(String jwt) {
        LOGGER.info("invalidAuthByJwt(String jwt), jwt = {}", jwt);
        try {
            MemberPayload memberPayload = JWT_PARSER.apply(jwt);
            return authInfoCacher.invalidAuthInfo(memberPayload.getKeyId());
        } catch (Exception e) {
            LOGGER.info("invalidAuthByJwt(String jwt) failed, jwt = {}, e = {}", jwt, e);
            return just(false);
        }
    }

    /**
     * 根据keyId失效本地缓存
     *
     * @param keyId
     */
    @Override
    public Mono<Boolean> invalidLocalAuthByKeyId(String keyId) {
        LOGGER.info("invalidLocalAuthByKeyId(String keyId), keyId = {}", keyId);
        return authInfoCacher.invalidLocalAuthInfo(keyId);
    }

    /**
     * 根据access更新成员auth的角色信息,半同步
     *
     * @param access
     * @param roleId
     * @return
     */
    @Override
    public void updateMemberRoleByAccess(Access access, Long roleId) {
        LOGGER.info("updateMemberRoleByAccess(Access access, Long roleId), access = {}, roleId = {}", access, roleId);
        if (roleId == null || roleId < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "角色id不能为空或小于1");

        long memberId = access.getId();
        memberRoleRelationService.updateMemberRoleRelation(memberId, roleId, memberId);
        AuthInfoRefreshElementType elementType = ROLE;
        String elementValue = valueOf(roleId);

        String loginType = access.getLoginType().intern();
        String deviceType = access.getDeviceType().intern();

        String keyId = genSessionKey(memberId, loginType, deviceType);
        refreshAuthInfoElementByKeyId(keyId, elementType, elementValue);

        executorService.submit(() ->
                new OtherTypesAuthRefreshTask(memberId, loginType, deviceType, elementType, elementValue));
    }

    /**
     * 根据memberId更新成员auth的角色信息
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    @Override
    public void updateMemberRoleById(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("updateMemberRoleById(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}", memberId, roleId);
        memberRoleRelationService.updateMemberRoleRelation(memberId, roleId, operatorId);
        AuthInfoRefreshParam authInfoRefreshParam = new AuthInfoRefreshParam(memberId,
                LOGIN_TYPES.stream().filter(lt -> !lt.identity.intern().equals(NOT_LOGGED_IN.identity)).collect(toList()),
                DEVICE_TYPES.stream().filter(dt -> !dt.identity.intern().equals(UNKNOWN.identity)).collect(toList()),
                ROLE, valueOf(roleId).intern());
        refreshAuthElementMultiTypes(authInfoRefreshParam);
    }

    /**
     * 根据access更新成员auth的密钥信息
     *
     * @param access
     * @return
     */
    @Override
    public Mono<String> updateSecKeyByAccess(Access access) {
        LOGGER.info("updateSecKeyByAccess(Access access), access = {}", access);
        return just(RsaProcessor.initKeyPair())
                .flatMap(keyPair -> {
                    refreshAuthInfoElementByKeyId(
                            genSessionKey(access.getId(), access.getLoginType().intern(), access.getDeviceType().intern()),
                            PUB_KEY, keyPair.getPubKey());
                    return just(keyPair.getPriKey());
                });
    }

    /**
     * 为成员分配默认角色
     *
     * @param memberId
     */
    @Override
    public void insertDefaultMemberRoleRelation(Long memberId) {
        LOGGER.info("insertDefaultMemberRoleRelation(Long memberId), memberId = {}", memberId);
        if (memberId == null || memberId < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "memberId不能为空或小于1");

        Role role = roleService.getDefaultRole();
        if (role == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "默认角色不能为空,请查看项目配置");

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

    /**
     * 根据access查询成员的角色及权限信息
     *
     * @param access
     * @return
     */
    @Override
    public Mono<Authority> getAuthorityByAccess(Access access) {
        LOGGER.info("getAuthorityByAccess(Access access), access = {}", access);
        return just(getAuthorityByRoleId(access.getRoleId()));
    }

    /**
     * 根据memberId查询成员的角色及权限信息
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Authority> getAuthorityByMemberId(Long memberId) {
        LOGGER.info("getAuthorityByMemberId(Long memberId), memberId = {}", memberId);
        return just(getAuthorityByRoleId(
                memberRoleRelationService.getRoleIdByMemberId(memberId)
                        .orElseThrow(() ->
                                new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access的角色为空"))));
    }

    /**
     * 刷新其他登录或设备类型auth任务
     */
    private final class OtherTypesAuthRefreshTask implements Runnable {

        private final long memberId;
        private final String exclusiveLoginType;
        private final String exclusiveDeviceType;
        private final AuthInfoRefreshElementType elementType;
        private final String elementValue;

        public OtherTypesAuthRefreshTask(long memberId, String exclusiveLoginType, String exclusiveDeviceType, AuthInfoRefreshElementType elementType, String elementValue) {
            this.memberId = memberId;
            this.exclusiveLoginType = exclusiveLoginType.intern();
            this.exclusiveDeviceType = exclusiveDeviceType.intern();
            this.elementType = elementType;
            this.elementValue = elementValue;
        }

        @Override
        public void run() {
            List<LoginType> loginTypes = LOGIN_TYPES.stream()
                    .filter(lt -> !lt.identity.intern().equals(NOT_LOGGED_IN.identity) && !lt.identity.intern().equals(exclusiveLoginType)).collect(toList());
            List<DeviceType> deviceTypes = DEVICE_TYPES.stream()
                    .filter(dt -> !dt.identity.intern().equals(UNKNOWN.identity) && !dt.identity.intern().equals(exclusiveDeviceType)).collect(toList());

            AuthInfoRefreshParam authInfoRefreshParam = new AuthInfoRefreshParam(memberId, loginTypes, deviceTypes, elementType, elementValue);
            refreshAuthElementMultiTypes(authInfoRefreshParam);
        }
    }
}
