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
import com.blue.secure.component.auth.AuthInfoCache;
import com.blue.secure.config.deploy.BlockingDeploy;
import com.blue.secure.config.deploy.SessionKeyDeploy;
import com.blue.secure.event.producer.InvalidLocalAuthProducer;
import com.blue.secure.model.*;
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

import static com.blue.base.constant.base.BlueNumericalValue.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.ResponseMessage.*;
import static com.blue.base.constant.base.SpecialSecKey.NOT_LOGGED_IN_SEC_KEY;
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
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * secure service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "CatchMayIgnoreException", "AliControlFlowStatementWithoutBraces", "GrazieInspection"})
@Service
public class SecureServiceImpl implements SecureService {

    private static final Logger LOGGER = getLogger(SecureServiceImpl.class);

    private JwtProcessor<MemberPayload> jwtProcessor;

    private final AuthInfoCache authInfoCache;

    private final StringRedisTemplate stringRedisTemplate;

    private final RoleService roleService;

    private final ResourceService resourceService;

    private final MemberService memberService;

    private final RoleResRelationService roleResRelationService;

    private final MemberRoleRelationService memberRoleRelationService;

    private final InvalidLocalAuthProducer invalidLocalAuthProducer;

    private final ExecutorService executorService;

    private final RedissonClient redissonClient;

    private final SessionKeyDeploy sessionKeyDeploy;

    private final BlockingDeploy blockingDeploy;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public SecureServiceImpl(JwtProcessor<MemberPayload> jwtProcessor, AuthInfoCache authInfoCache, StringRedisTemplate stringRedisTemplate,
                             RoleService roleService, ResourceService resourceService, MemberService memberService,
                             RoleResRelationService roleResRelationService, MemberRoleRelationService memberRoleRelationService, InvalidLocalAuthProducer invalidLocalAuthProducer,
                             ExecutorService executorService, RedissonClient redissonClient, SessionKeyDeploy sessionKeyDeploy, BlockingDeploy blockingDeploy) {
        this.jwtProcessor = jwtProcessor;
        this.authInfoCache = authInfoCache;
        this.stringRedisTemplate = stringRedisTemplate;
        this.roleService = roleService;
        this.resourceService = resourceService;
        this.memberService = memberService;
        this.roleResRelationService = roleResRelationService;
        this.memberRoleRelationService = memberRoleRelationService;
        this.invalidLocalAuthProducer = invalidLocalAuthProducer;
        this.executorService = executorService;
        this.redissonClient = redissonClient;
        this.sessionKeyDeploy = sessionKeyDeploy;
        this.blockingDeploy = blockingDeploy;
    }

    private static final Gson GSON = CommonFunctions.GSON;

    private static final Supplier<Long> TIME_STAMP_GETTER = CommonFunctions.TIME_STAMP_GETTER;

    private static int RANDOM_ID_LENGTH;

    private static long MAX_WAITING_FOR_REFRESH;

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
     * generate resource key on init(method, uri)
     */
    private static final BiFunction<String, String, String> INIT_RES_KEY_GENERATOR = CommonFunctions.INIT_RES_KEY_GENERATOR;

    /**
     * get uri
     */
    private static final Function<Resource, String> REAL_URI_GETTER = resource ->
            PATH_SEPARATOR + resource.getModule().intern() + resource.getUri().intern();

    /**
     * generate resource key for request(method, uri)
     */
    private static final BiFunction<String, String, String> REQ_RES_KEY_GENERATOR = CommonFunctions.REQ_RES_KEY_GENERATOR;

    /**
     * exps
     */
    private static final BlueException
            UNAUTHORIZED_EXP = new BlueException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message),
            NOT_FOUND_EXP = new BlueException(NOT_FOUND.status, NOT_FOUND.code, NOT_FOUND.message),
            INTERNAL_SERVER_ERROR_EXP = new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INTERNAL_SERVER_ERROR.message);

    private volatile boolean authorityInfosRefreshing = true;

    /**
     * role id -> resource keys
     */
    private volatile Map<Long, Set<String>> roleAndResourcesKeyMapping = emptyMap();

    /**
     * role id -> role info
     */
    private volatile Map<Long, RoleInfo> idAndRoleInfoMapping = emptyMap();

    /**
     * role id -> resource info list
     */
    private volatile Map<Long, List<ResourceInfo>> roleAndResourceInfosMapping = emptyMap();

    /**
     * resource key -> resource
     */
    private volatile Map<String, Resource> keyAndResourceMapping = emptyMap();

    /**
     * jwt parser
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
     * authInfo parser
     */
    private final Function<String, AuthInfo> AUTH_INFO_PARSER = authInfoStr -> {
        AuthInfo authInfo;
        try {
            authInfo = GSON.fromJson(authInfoStr, AuthInfo.class);
        } catch (JsonSyntaxException e) {
            throw UNAUTHORIZED_EXP;
        }
        return authInfo;
    };

    /**
     * blocker when resource or relation refreshing
     */
    private final Supplier<Boolean> RESOURCE_OR_REL_REFRESHING_BLOCKER = () -> {
        if (authorityInfosRefreshing) {
            long start = currentTimeMillis();
            while (authorityInfosRefreshing) {
                if (currentTimeMillis() - start > MAX_WAITING_FOR_REFRESH)
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "waiting resource refresh timeout");
                onSpinWait();
            }
        }
        return true;
    };

    /**
     * blocker when role info refreshing
     */
    private final Supplier<Boolean> ROLE_REFRESHING_BLOCKER = () -> {
        if (authorityInfosRefreshing) {
            long start = currentTimeMillis();
            while (authorityInfosRefreshing) {
                if (currentTimeMillis() - start > MAX_WAITING_FOR_REFRESH)
                    throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "waiting role refresh timeout");
                onSpinWait();
            }
        }
        return true;
    };

    /**
     * get resource by resKey
     */
    private final Function<String, Resource> RESOURCE_GETTER = resourceKey -> {
        RESOURCE_OR_REL_REFRESHING_BLOCKER.get();
        return keyAndResourceMapping.get(resourceKey);
    };

    /**
     * authorization checker
     */
    private final BiFunction<Long, String, Boolean> AUTHORIZATION_RES_CHECKER = (roleId, resourceKey) -> {
        RESOURCE_OR_REL_REFRESHING_BLOCKER.get();
        return ofNullable(roleAndResourcesKeyMapping.get(roleId)).map(set -> set.contains(resourceKey)).orElse(false);
    };

    /**
     * un authorization message getter
     */
    private final UnaryOperator<String> RES_NOT_ACCESS_MESSAGE_GETTER = resourceKey -> {
        RESOURCE_OR_REL_REFRESHING_BLOCKER.get();
        return "Can't access " + ofNullable(keyAndResourceMapping.get(resourceKey))
                .map(Resource::getName).orElse(" resource ");
    };

    /**
     * login type identity -> login type
     */
    private static final Map<String, LoginType> IDENTITY_NATURE_MAPPING = Stream.of(LoginType.values())
            .collect(toMap(lt -> lt.identity, lt -> lt, (a, b) -> a));

    /**
     * login type identity -> login type nature
     */
    private static final UnaryOperator<String> LOGIN_TYPE_2_NATURE_CONVERTER = identity -> {
        if (identity != null && !"".equals(identity)) {
            LoginType loginType = IDENTITY_NATURE_MAPPING.get(identity);
            if (loginType != null)
                return loginType.nature.intern();
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "loginType with identity " + identity + " is not exist");
        }
        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "identity can't be null or ''");
    };

    /**
     * UN_AUTH
     */
    private static final Access NO_AUTH_ACCESS = new Access(NOT_LOGGED_IN_MEMBER_ID.value,
            NOT_LOGGED_IN_ROLE_ID.value, NOT_LOGGED_IN.identity, UNKNOWN.identity, NOT_LOGGED_IN_TIME.value);

    /**
     * Resources do not require authentication access
     */
    private final Function<Resource, Mono<AuthAsserted>> NO_AUTH_REQUIRED_RES_GEN = resource ->
            just(new AuthAsserted(
                    false, true, true,
                    resource.getExistenceRequestBody(), resource.getExistenceResponseBody(),
                    NOT_LOGGED_IN_SEC_KEY.value, NO_AUTH_ACCESS, NO_AUTH_REQUIRED_RESOURCE.message));

    /**
     * login func
     */
    private Map<String, Function<ClientLoginParam, Mono<MemberBasicInfo>>> clientLoginHandlers;

    /**
     * init login handler
     */
    private void initLoginHandler() {
        clientLoginHandlers = new HashMap<>(16, 1.0f);
        clientLoginHandlers.put(SMS_VERIGY.identity, memberService::getMemberBasicInfoMonoByPhoneWithAssertVerify);
        clientLoginHandlers.put(PHONE_PWD.identity, memberService::getMemberBasicInfoMonoByPhoneWithAssertPwd);
        clientLoginHandlers.put(EMAIL_PWD.identity, memberService::getMemberBasicInfoMonoByEmailWithAssertPwd);

        LOGGER.info("generateLoginHandler(), clientLoginHandlers = {}", clientLoginHandlers);
    }

    /**
     * get login handler
     */
    private final Function<String, Function<ClientLoginParam, Mono<MemberBasicInfo>>> LOGIN_HANDLER_GETTER = loginType -> {
        LOGGER.info("LOGIN_HANDLER_GETTER, loginType = {}", loginType);
        if (loginType == null || "".equals(loginType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "loginType can't be blank");

        Function<ClientLoginParam, Mono<MemberBasicInfo>> loginFunc = clientLoginHandlers.get(loginType);
        if (loginFunc == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid login type " + loginType);

        return loginFunc;
    };

    /**
     * refresh element type -> authInfo packagers
     */
    private static final Map<AuthInfoRefreshElementType, BiConsumer<AuthInfo, String>> RE_PACKAGERS = new HashMap<>();

    /**
     * generate auth refresh sync key
     */
    private static final Map<AuthInfoRefreshElementType, BinaryOperator<String>> AUTH_REFRESH_KEY_GENS = new HashMap<>();

    static {
        RE_PACKAGERS.put(ROLE, (ai, ele) -> {
            if (ai == null || ele == null || "".equals(ele))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "ai or ele can't be null");

            long roleId;
            try {
                roleId = Long.parseLong(ele);
            } catch (NumberFormatException e) {
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "parse ele to roleId failed, e = " + e);
            }
            ai.setRoleId(roleId);
        });
        RE_PACKAGERS.put(PUB_KEY, (ai, ele) -> {
            if (ai == null || ele == null || "".equals(ele))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "ai or ele can't be null");
            ai.setPubKey(ele);
        });

        AUTH_REFRESH_KEY_GENS.put(ROLE, (snKey, eleValue) ->
                AUTH_REFRESH_KEY_PRE + snKey + PAR_CONCATENATION + ROLE.name() + PAR_CONCATENATION + eleValue);
        AUTH_REFRESH_KEY_GENS.put(PUB_KEY, (snKey, eleValue) ->
                AUTH_REFRESH_KEY_PRE + snKey + PAR_CONCATENATION + PUB_KEY.name() + PAR_CONCATENATION + eleValue.hashCode());

        LOGGER.info("static codes, RE_PACKAGERS = {}, AUTH_REFRESH_KEY_GENS = {}", RE_PACKAGERS, AUTH_REFRESH_KEY_GENS);
    }

    /**
     * get role info by role id
     */
    private final Function<Long, Optional<RoleInfo>> ROLE_INFO_BY_ID_GETTER = roleId -> {
        ROLE_REFRESHING_BLOCKER.get();
        return ofNullable(idAndRoleInfoMapping.get(roleId));
    };

    /**
     * get resource info list by role id
     */
    private final Function<Long, List<ResourceInfo>> RESOURCE_INFOS_BY_ROLE_ID_GETTER = roleId -> {
        RESOURCE_OR_REL_REFRESHING_BLOCKER.get();
        return roleAndResourceInfosMapping.get(roleId);
    };

    /**
     * generate redis sessionKey
     *
     * @param id
     * @param loginTypeIdentity
     * @param deviceTypeIdentity
     * @return
     */
    private String genSessionKey(Long id, String loginTypeIdentity, String deviceTypeIdentity) {
        LOGGER.info("genSessionKey(Long id, String loginTypeIdentity, String deviceTypeIdentity), id = {}, loginTypeIdentity = {}, deviceTypeIdentity = {}", id, loginTypeIdentity, deviceTypeIdentity);
        if (id == null || id < 1L || deviceTypeIdentity == null || "".equals(deviceTypeIdentity))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "id or loginType or deviceType can't be null");

        return SESSION_KEY_PRE + id + PAR_CONCATENATION + LOGIN_TYPE_2_NATURE_CONVERTER.apply(loginTypeIdentity).intern() + PAR_CONCATENATION + deviceTypeIdentity;
    }

    /**
     * generate global sync key
     */
    private static final UnaryOperator<String> GLOBAL_LOCK_KEY_GEN = keyId ->
            valueOf(keyId.hashCode());

    /**
     * generate auth refresh sync key
     *
     * @param snKey
     * @param elementType
     * @param elementValue
     * @return
     */
    private String genAuthRefreshLockKey(String snKey, AuthInfoRefreshElementType elementType, String elementValue) {
        LOGGER.info("genAuthRefreshLockKey(String snKey, AuthInfoRefreshElementType elementType, String elementValue), snKey = {}, elementType = {}, elementValue = {}", snKey, elementType, elementValue);
        if (snKey == null || "".equals(snKey) || elementType == null || elementValue == null || "".equals(elementValue))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "id or loginType or deviceType can't be null");

        return AUTH_REFRESH_KEY_GENS.get(elementType).apply(snKey, elementValue);
    }

    /**
     * refresh auth info (role id/sec key) by key id
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
            LOGGER.info("member's authInfo is empty, can't refresh");
            return;
        }

        AuthInfo authInfo;
        try {
            authInfo = GSON.fromJson(originalAuthInfoJson, AuthInfo.class);
        } catch (JsonSyntaxException e) {
            LOGGER.info("member's authInfo is invalid, can't refresh");
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
                LOGGER.error("lock on global failed, e = {}", exception);
            } finally {
                try {
                    globalLock.unlock();
                } catch (Exception e) {
                    LOGGER.error("globalLock unlock failed, e = {}", e);
                }
            }
        } catch (Exception exception) {
            LOGGER.error("lock on business failed, e = {}", exception);
        } finally {
            if (tryBusinessLock) {
                try {
                    authRefreshLock.unlock();
                } catch (Exception e) {
                    LOGGER.error("authRefreshLock unlock failed, e = {}", e);
                }
                try {
                    invalidLocalAuthProducer.send(new InvalidLocalAuthParam(keyId));
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * refresh auth elements
     *
     * @param authInfoRefreshParam
     */
    public void refreshAuthElementMultiTypes(AuthInfoRefreshParam authInfoRefreshParam) {
        LOGGER.info("refreshAuthElementMultiTypes(AuthInfoRefreshParam authInfoRefreshParam), authInfoRefreshParam = {}", authInfoRefreshParam);
        AuthInfoRefreshElementType elementType = authInfoRefreshParam.getElementType();
        if (elementType == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "elementType can't be null");

        String elementValue = authInfoRefreshParam.getElementValue();
        if (elementValue == null || "".equals(elementValue))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "elementValue can't be null");

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
     * get authority by role id
     *
     * @param roleId
     * @return
     */
    private Mono<AuthorityBaseOnRole> getAuthorityMonoByRoleId(Long roleId) {
        LOGGER.info("getAuthorityByRoleOpt(Long roleId), roleId = {}", roleId);
        return just(ROLE_INFO_BY_ID_GETTER.apply(roleId)
                .map(role ->
                        new AuthorityBaseOnRole(role,
                                RESOURCE_INFOS_BY_ROLE_ID_GETTER.apply(roleId)))
                .orElseThrow(() ->
                        new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "role info doesn't exist, roleId = " + roleId)));
    }

    /**
     * init
     */
    @PostConstruct
    public void init() {
        MAX_WAITING_FOR_REFRESH = blockingDeploy.getBlockingMillis();
        RANDOM_ID_LENGTH = sessionKeyDeploy.getRanLen();

        refreshSystemAuthorityInfos();
        initLoginHandler();
    }

    /**
     * refresh resource key/info or role-resource-relation
     *
     * @return
     */
    @Override
    public void refreshSystemAuthorityInfos() {
        LOGGER.info("refreshResourceKeyOrRelation()");

        CompletableFuture<List<Resource>> resourceListCf =
                resourceService.selectResource().toFuture();
        CompletableFuture<List<RoleResRelation>> roleResRelationListCf =
                roleResRelationService.selectRelation().toFuture();
        CompletableFuture<List<Role>> roleListCf =
                roleService.selectRole().toFuture();

        List<Resource> resources = resourceListCf.join();
        Map<Long, Resource> idAndResourceMapping = resources
                .parallelStream()
                .collect(toMap(Resource::getId, e -> e, (a, b) -> a));

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

        Map<String, Resource> tempKeyAndResourceMapping = resources
                .parallelStream()
                .collect(toMap(r -> INIT_RES_KEY_GENERATOR.apply(r.getRequestMethod().toUpperCase().intern(),
                        REAL_URI_GETTER.apply(r).intern()), r -> r, (a, b) -> a));

        List<Role> roles = roleListCf.join();
        Map<Long, RoleInfo> tempIdAndRoleInfoMapping = roles
                .parallelStream()
                .map(ROLE_2_ROLE_INFO_CONVERTER)
                .collect(toMap(RoleInfo::getId, r -> r, (a, b) -> a));

        authorityInfosRefreshing = true;

        keyAndResourceMapping = tempKeyAndResourceMapping;
        roleAndResourcesKeyMapping = tempRoleAndResourcesKeyMapping;
        roleAndResourceInfosMapping = tempRoleAndResourceInfosMapping;
        idAndRoleInfoMapping = tempIdAndRoleInfoMapping;

        authorityInfosRefreshing = false;
    }

    /**
     * login by client
     *
     * @param clientLoginParam
     * @return
     */
    @Override
    public Mono<MemberAuth> loginByClient(ClientLoginParam clientLoginParam) {
        LOGGER.info("loginByClient(ClientLoginParam clientLoginParam), clientLoginParam = {}", clientLoginParam);

        if (clientLoginParam == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);

        return zip(
                LOGIN_HANDLER_GETTER.apply(clientLoginParam.getLoginType()).apply(clientLoginParam),
                just(clientLoginParam.getLoginType().intern()),
                just(clientLoginParam.getDeviceType().intern()))
                .flatMap(t3 -> {
                    MemberBasicInfo memberBasicInfo = t3.getT1();
                    LOGGER.info("memberBasicInfo = {}", memberBasicInfo);
                    Long mid = memberBasicInfo.getId();

                    return memberRoleRelationService.getRoleIdMonoByMemberId(mid)
                            .flatMap(ridOpt ->
                                    ridOpt.map(rid ->
                                                    just(new AuthGenParam(mid, rid, t3.getT2(), t3.getT3())))
                                            .orElseGet(() -> {
                                                LOGGER.error("loginByClient(ClientLoginParam clientLoginParam) failed, member has no role, memberId = {}", mid);
                                                return error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, MEMBER_NOT_HAS_A_ROLE.message));
                                            }));
                }).flatMap(this::generateAuthMono);
    }

    /**
     * login by wechat mini program
     *
     * @param miniProLoginParam
     * @return
     */
    @Override
    public Mono<MemberAuth> loginByMiniPro(MiniProLoginParam miniProLoginParam) {
        LOGGER.info("loginByMiniPro(MiniProLoginParam miniProLoginParam), miniProLoginParam = {}", miniProLoginParam);

        if (miniProLoginParam == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);

        return null;
    }

    /**
     * login by wechat
     *
     * @param wechatProLoginParam
     * @return
     */
    @Override
    public Mono<MemberAuth> loginByWechat(WechatProLoginParam wechatProLoginParam) {
        LOGGER.info("loginByWechat(WechatProLoginParam wechatProLoginParam), wechatProLoginParam = {}", wechatProLoginParam);

        if (wechatProLoginParam == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message);

        return null;
    }

    /**
     * assert auth
     *
     * @param assertAuth
     * @return
     */
    @Override
    public Mono<AuthAsserted> assertAuthMono(AssertAuth assertAuth) {
        LOGGER.info("assertAuth(AssertAuth assertAuth), assertAuth = {}", assertAuth);
        return just(assertAuth)
                .switchIfEmpty(error(UNAUTHORIZED_EXP))
                .flatMap(aa -> {
                    String resourceKey = REQ_RES_KEY_GENERATOR.apply(
                            aa.getMethod().intern(), aa.getUri());

                    Resource resource = RESOURCE_GETTER.apply(resourceKey);
                    if (resource == null)
                        return error(NOT_FOUND_EXP);

                    if (!resource.getAuthenticate())
                        return NO_AUTH_REQUIRED_RES_GEN.apply(resource);

                    String jwt = aa.getAuthentication();
                    MemberPayload memberPayload = JWT_PARSER.apply(jwt);

                    return authInfoCache.getAuthInfo(memberPayload.getKeyId())
                            .flatMap(v -> {
                                if (v == null || "".equals(v))
                                    return error(UNAUTHORIZED_EXP);

                                AuthInfo authInfo = AUTH_INFO_PARSER.apply(v);
                                if (!jwt.equals(authInfo.getJwt()))
                                    return error(UNAUTHORIZED_EXP);
                                if (!AUTHORIZATION_RES_CHECKER.apply(authInfo.getRoleId(), resourceKey))
                                    return error(new BlueException(FORBIDDEN.status, FORBIDDEN.code, RES_NOT_ACCESS_MESSAGE_GETTER.apply(resourceKey)));

                                boolean reqUnDecryption = resource.getRequestUnDecryption();
                                boolean resUnEncryption = resource.getResponseUnEncryption();

                                return just(new AuthAsserted(true, reqUnDecryption, resUnEncryption, resource.getExistenceRequestBody(), resource.getExistenceResponseBody(),
                                        reqUnDecryption && resUnEncryption ? "" : authInfo.getPubKey(),
                                        new Access(parseLong(memberPayload.getId()), authInfo.getRoleId(), memberPayload.getLoginType().intern(),
                                                memberPayload.getDeviceType().intern(), parseLong(memberPayload.getLoginTime())), ACCESS.message));
                            });
                });
    }

    /**
     * generate member auth
     *
     * @param authGenParam
     * @return
     */
    @Override
    public Mono<MemberAuth> generateAuthMono(AuthGenParam authGenParam) {
        LOGGER.info("Mono<MemberAuth> generateAuthMono(AuthGenParam authGenParam), authGenParam = {}", authGenParam);
        if (authGenParam == null)
            return error(INTERNAL_SERVER_ERROR_EXP);

        return just(authGenParam)
                .flatMap(agp -> {
                    Long memberId = agp.getMemberId();
                    String loginType = agp.getLoginType().intern();
                    String deviceType = agp.getDeviceType().intern();
                    KeyPair keyPair = RsaProcessor.initKeyPair();

                    return just(new MemberPayload(
                            randomAlphanumeric(RANDOM_ID_LENGTH),
                            genSessionKey(memberId, loginType, deviceType),
                            valueOf(memberId),
                            loginType, deviceType,
                            valueOf(TIME_STAMP_GETTER.get()))
                    ).flatMap(mp -> {
                        String jwt = jwtProcessor.create(mp);
                        String authInfoJson = GSON.toJson(new AuthInfo(jwt, authGenParam.getRoleId(), keyPair.getPubKey()));

                        return authInfoCache.setAuthInfo(mp.getKeyId(), authInfoJson)
                                .flatMap(b -> {
                                    LOGGER.info("authInfoJson = {}, keyPairDTO = {}", authInfoJson, keyPair);
                                    if (b)
                                        return just(new MemberAuth(jwt, keyPair.getPriKey()));

                                    LOGGER.error("authInfoCache.setAuthInfo(mp.getKeyId(), authInfoJson), failed");
                                    return error(INTERNAL_SERVER_ERROR_EXP);
                                });
                    });
                });
    }

    /**
     * invalid auth by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<Boolean> invalidAuthByAccess(Access access) {
        LOGGER.info("invalidAuthByAccess(Access access), access = {}", access);
        if (access != null)
            return authInfoCache.invalidAuthInfo(genSessionKey(access.getId(), access.getLoginType().intern(), access.getDeviceType().intern()));

        return error(UNAUTHORIZED_EXP);
    }

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    @Override
    public Mono<Boolean> invalidAuthByJwt(String jwt) {
        LOGGER.info("invalidAuthByJwt(String jwt), jwt = {}", jwt);
        try {
            MemberPayload memberPayload = JWT_PARSER.apply(jwt);
            return authInfoCache.invalidAuthInfo(memberPayload.getKeyId());
        } catch (Exception e) {
            LOGGER.info("invalidAuthByJwt(String jwt) failed, jwt = {}, e = {}", jwt, e);
            return just(false);
        }
    }

    /**
     * invalid local auth by key id
     *
     * @param keyId
     */
    @Override
    public Mono<Boolean> invalidLocalAuthByKeyId(String keyId) {
        LOGGER.info("invalidLocalAuthByKeyId(String keyId), keyId = {}", keyId);
        return authInfoCache.invalidLocalAuthInfo(keyId);
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
        LOGGER.info("updateMemberRoleByAccess(Access access, Long roleId), access = {}, roleId = {}", access, roleId);
        if (access == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "access can't be null");

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
     * update member role info by member id
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
     * update member sec key by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<String> updateSecKeyByAccess(Access access) {
        LOGGER.info("updateSecKeyByAccess(Access access), access = {}", access);
        if (access == null)
            throw UNAUTHORIZED_EXP;

        return just(RsaProcessor.initKeyPair())
                .flatMap(keyPair -> {
                    refreshAuthInfoElementByKeyId(
                            genSessionKey(access.getId(), access.getLoginType().intern(), access.getDeviceType().intern()),
                            PUB_KEY, keyPair.getPubKey());
                    return just(keyPair.getPriKey());
                });
    }

    /**
     * set a default role to member
     *
     * @param memberId
     */
    @Override
    public void insertDefaultMemberRoleRelation(Long memberId) {
        LOGGER.info("insertDefaultMemberRoleRelation(Long memberId), memberId = {}", memberId);
        if (memberId == null || memberId < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INVALID_IDENTITY.message);

        Role role = roleService.getDefaultRole();
        if (role == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "default role not found");

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
     * get member's authority by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnRole> getAuthorityMonoByAccess(Access access) {
        LOGGER.info("Mono<Authority> getAuthorityMonoByAccess(Access access), access = {}", access);
        if (access == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "access can't be null");

        return getAuthorityMonoByRoleId(access.getRoleId());
    }

    /**
     * get member's authority by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnRole> getAuthorityMonoByMemberId(Long memberId) {
        LOGGER.info("Mono<Authority> getAuthorityMonoByMemberId(Long memberId), memberId = {}", memberId);
        if (memberId == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, INVALID_IDENTITY.message);

        return memberRoleRelationService.getRoleIdMonoByMemberId(memberId)
                .flatMap(roleIdOpt ->
                        roleIdOpt.map(this::getAuthorityMonoByRoleId)
                                .orElseThrow(() ->
                                        new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "role id of the member id -> " + memberId + " not found")));
    }

    /**
     * refresh auth for other login type and device type
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
