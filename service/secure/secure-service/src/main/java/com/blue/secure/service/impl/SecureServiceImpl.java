package com.blue.secure.service.impl;

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
import com.blue.secure.model.AuthGenElement;
import com.blue.secure.model.AuthInfo;
import com.blue.secure.model.AuthInfoRefreshElement;
import com.blue.secure.model.MemberAuth;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.repository.entity.RoleResRelation;
import com.blue.secure.service.inter.*;
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

import static com.blue.base.common.base.Asserter.*;
import static com.blue.base.common.base.CommonFunctions.*;
import static com.blue.base.common.base.RsaProcessor.initKeyPair;
import static com.blue.base.constant.base.BlueNumericalValue.*;
import static com.blue.base.constant.base.ResponseElement.*;
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
import static java.util.concurrent.CompletableFuture.supplyAsync;
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
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "GrazieInspection"})
@Service
public class SecureServiceImpl implements SecureService {

    private static final Logger LOGGER = getLogger(SecureServiceImpl.class);

    private JwtProcessor<MemberPayload> jwtProcessor;

    private AuthInfoCache authInfoCache;

    private final StringRedisTemplate stringRedisTemplate;

    private final RoleService roleService;

    private final ResourceService resourceService;

    private final MemberService memberService;

    private final RoleResRelationService roleResRelationService;

    private final MemberRoleRelationService memberRoleRelationService;

    private final InvalidLocalAuthProducer invalidLocalAuthProducer;

    private final ExecutorService executorService;

    private final RedissonClient redissonClient;

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

        MAX_WAITING_FOR_REFRESH = blockingDeploy.getBlockingMillis();
        RANDOM_ID_LENGTH = sessionKeyDeploy.getRanLen();
    }

    private static int RANDOM_ID_LENGTH;

    private static long MAX_WAITING_FOR_REFRESH;

    public static final String
            SESSION_KEY_PRE = CacheKey.SESSION_KEY_PRE.key,
            AUTH_REFRESH_KEY_PRE = CacheKey.AUTH_REFRESH_KEY_PRE.key,
            PAR_CONCATENATION = Symbol.PAR_CONCATENATION.identity,
            PATH_SEPARATOR = Symbol.PATH_SEPARATOR.identity;

    private static final List<LoginType> VALID_LOGIN_TYPES = of(LoginType.values())
            .filter(lt -> !lt.identity.intern().equals(NOT_LOGGED_IN.identity))
            .collect(toList());

    private static final List<DeviceType> VALID_DEVICE_TYPES = of(DeviceType.values())
            .filter(dt -> !dt.identity.intern().equals(UNKNOWN.identity))
            .collect(toList());

    /**
     * get uri
     */
    private static final Function<Resource, String> REAL_URI_GETTER = resource ->
            PATH_SEPARATOR + resource.getModule().intern() + resource.getUri().intern();

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
            throw new BlueException(UNAUTHORIZED);
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
            throw new BlueException(UNAUTHORIZED);
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
                    throw new BlueException(INTERNAL_SERVER_ERROR);
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
                    throw new BlueException(INTERNAL_SERVER_ERROR);
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
     * login type identity -> login type
     */
    private static final Map<String, LoginType> IDENTITY_NATURE_MAPPING = of(LoginType.values())
            .collect(toMap(lt -> lt.identity, lt -> lt, (a, b) -> a));

    /**
     * login type identity -> login type nature
     */
    private static final UnaryOperator<String> LOGIN_TYPE_2_NATURE_CONVERTER = identity -> {
        if (isNotBlank(identity)) {
            LoginType loginType = IDENTITY_NATURE_MAPPING.get(identity);
            if (loginType != null)
                return loginType.nature.intern();
            throw new BlueException(NO_AUTH_REQUIRED_RESOURCE.status, NO_AUTH_REQUIRED_RESOURCE.code, NO_AUTH_REQUIRED_RESOURCE.message);
        }
        throw new BlueException(NO_AUTH_REQUIRED_RESOURCE.status, NO_AUTH_REQUIRED_RESOURCE.code, NO_AUTH_REQUIRED_RESOURCE.message);
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
        clientLoginHandlers.put(SMS_VERIFY.identity, memberService::selectMemberBasicInfoMonoByPhoneWithAssertVerify);
        clientLoginHandlers.put(PHONE_PWD.identity, memberService::selectMemberBasicInfoMonoByPhoneWithAssertPwd);
        clientLoginHandlers.put(EMAIL_PWD.identity, memberService::selectMemberBasicInfoMonoByEmailWithAssertPwd);

        LOGGER.info("generateLoginHandler(), clientLoginHandlers = {}", clientLoginHandlers);
    }

    /**
     * get login handler
     */
    private final Function<String, Function<ClientLoginParam, Mono<MemberBasicInfo>>> LOGIN_HANDLER_GETTER = loginType -> {
        LOGGER.info("LOGIN_HANDLER_GETTER, loginType = {}", loginType);
        if (loginType == null || "".equals(loginType))
            throw new BlueException(BAD_REQUEST);

        Function<ClientLoginParam, Mono<MemberBasicInfo>> loginFunc = clientLoginHandlers.get(loginType);
        if (loginFunc != null)
            return loginFunc;

        throw new BlueException(BAD_REQUEST);
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
                throw new BlueException(BAD_REQUEST);

            long roleId;
            try {
                roleId = parseLong(ele);
            } catch (NumberFormatException e) {
                throw new BlueException(BAD_REQUEST);
            }
            ai.setRoleId(roleId);
        });
        RE_PACKAGERS.put(PUB_KEY, (ai, ele) -> {
            if (ai == null || ele == null || "".equals(ele))
                throw new BlueException(BAD_REQUEST);
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
    private static String genSessionKey(Long id, String loginTypeIdentity, String deviceTypeIdentity) {
        LOGGER.info("String genSessionKey(Long id, String loginTypeIdentity, String deviceTypeIdentity), id = {}, loginTypeIdentity = {}, deviceTypeIdentity = {}", id, loginTypeIdentity, deviceTypeIdentity);
        if (id != null && id >= 0L && deviceTypeIdentity != null && !"".equals(deviceTypeIdentity))
            return SESSION_KEY_PRE + id + PAR_CONCATENATION + LOGIN_TYPE_2_NATURE_CONVERTER.apply(loginTypeIdentity).intern() + PAR_CONCATENATION + deviceTypeIdentity;

        throw new BlueException(BAD_REQUEST);
    }

    /**
     * generate global sync key
     */
    private static final UnaryOperator<String> GLOBAL_LOCK_KEY_GEN = keyId -> valueOf(keyId.hashCode());

    /**
     * generate auth refresh sync key
     *
     * @param snKey
     * @param elementType
     * @param elementValue
     * @return
     */
    private String genAuthRefreshLockKey(String snKey, AuthInfoRefreshElementType elementType, String elementValue) {
        LOGGER.info("String genAuthRefreshLockKey(String snKey, AuthInfoRefreshElementType elementType, String elementValue), snKey = {}, elementType = {}, elementValue = {}", snKey, elementType, elementValue);
        if (isNotBlank(snKey) && elementType != null && isNotBlank(elementValue))
            return AUTH_REFRESH_KEY_GENS.get(elementType).apply(snKey, elementValue);

        throw new BlueException(BAD_REQUEST);
    }

    /**
     * refresh auth info (role id/sec key) by key id
     *
     * @param keyId
     * @param elementType
     * @param elementValue
     */
    private void refreshAuthInfoElementByKeyId(String keyId, AuthInfoRefreshElementType elementType, String elementValue) {
        LOGGER.info("void refreshAuthInfoElementByKeyId(String keyId, AuthInfoRefreshElementType elementType, String elementValue), keyId = {}, elementType = {}, elementValue = {}", keyId, elementType, elementValue);
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

        RLock authRefreshLock = redissonClient.getLock(genAuthRefreshLockKey(keyId, elementType, elementValue));
        boolean tryBusinessLock = false;
        try {
            if (!(tryBusinessLock = authRefreshLock.tryLock()))
                return;

            RLock globalLock = redissonClient.getLock(GLOBAL_LOCK_KEY_GEN.apply(keyId));
            try {
                globalLock.lock();
                RE_PACKAGERS.get(elementType).accept(authInfo, elementValue);
                stringRedisTemplate.opsForValue()
                        .set(keyId, GSON.toJson(authInfo), Duration.of(ofNullable(stringRedisTemplate.getExpire(keyId, SECONDS)).orElse(0L), ChronoUnit.SECONDS));
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
                    LOGGER.error("invalidLocalAuthProducer send failed, keyId = {}, e = {}", keyId, e);
                }
            }
        }
    }

    /**
     * refresh auth elements
     *
     * @param authInfoRefreshElement
     */
    private void refreshAuthElementMultiTypes(AuthInfoRefreshElement authInfoRefreshElement) {
        LOGGER.info("void refreshAuthElementMultiTypes(AuthInfoRefreshParam authInfoRefreshParam), authInfoRefreshParam = {}", authInfoRefreshElement);
        Long memberId = authInfoRefreshElement.getMemberId();
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        AuthInfoRefreshElementType elementType = authInfoRefreshElement.getElementType();
        if (elementType == null)
            throw new BlueException(BAD_REQUEST);

        String elementValue = authInfoRefreshElement.getElementValue();
        if (isBlank(elementValue))
            throw new BlueException(BAD_REQUEST);

        List<String> loginTypes = ofNullable(authInfoRefreshElement.getLoginTypes())
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

        List<String> deviceTypes = ofNullable(authInfoRefreshElement.getDeviceTypes())
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
                        genSessionKey(memberId, loginType.intern(), deviceType.intern()), elementType, elementValue);
    }

    /**
     * get authority by role id
     *
     * @param roleId
     * @return
     */
    private Mono<AuthorityBaseOnRole> getAuthorityMonoByRoleId(Long roleId) {
        LOGGER.info("Mono<AuthorityBaseOnRole> getAuthorityByRoleOpt(Long roleId), roleId = {}", roleId);
        return just(ROLE_INFO_BY_ID_GETTER.apply(roleId)
                .map(role ->
                        new AuthorityBaseOnRole(role, RESOURCE_INFOS_BY_ROLE_ID_GETTER.apply(roleId)))
                .orElseThrow(() -> {
                    LOGGER.error("role info doesn't exist, roleId = {}", roleId);
                    return new BlueException(BAD_REQUEST);
                }));
    }

    private final Function<Long, Boolean> INVALID_AUTH_BY_MEMBER_ID_TASK = memberId -> {
        String keyId;
        try {
            for (LoginType loginType : VALID_LOGIN_TYPES)
                for (DeviceType deviceType : VALID_DEVICE_TYPES) {
                    keyId = genSessionKey(memberId, loginType.identity, deviceType.identity);
                    authInfoCache.invalidAuthInfo(keyId).subscribe();
                    authInfoCache.invalidLocalAuthInfo(keyId).subscribe();
                }
        } catch (Exception e) {
            return false;
        }

        return true;
    };

    /**
     * init
     */
    @PostConstruct
    public void init() {
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
        LOGGER.info("void refreshResourceKeyOrRelation()");

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
        LOGGER.info("Mono<MemberAuth> loginByClient(ClientLoginParam clientLoginParam), clientLoginParam = {}", clientLoginParam);
        if (clientLoginParam == null)
            throw new BlueException(EMPTY_PARAM);

        String loginType = clientLoginParam.getLoginType().intern();
        String deviceType = clientLoginParam.getDeviceType().intern();

        return LOGIN_HANDLER_GETTER.apply(loginType).apply(clientLoginParam)
                .flatMap(mbi -> {
                    LOGGER.info("mbi = {}", mbi);
                    Long mid = mbi.getId();

                    return memberRoleRelationService.getRoleIdMonoByMemberId(mid)
                            .flatMap(ridOpt ->
                                    ridOpt.map(rid ->
                                                    just(new AuthGenElement(mid, rid, loginType, deviceType)))
                                            .orElseGet(() -> {
                                                LOGGER.error("Mono<MemberAuth> loginByClient(ClientLoginParam clientLoginParam) failed, member has no role, memberId = {}", mid);
                                                return error(() -> new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, MEMBER_NOT_HAS_A_ROLE.message));
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
        LOGGER.info("Mono<MemberAuth> loginByMiniPro(MiniProLoginParam miniProLoginParam), miniProLoginParam = {}", miniProLoginParam);
        if (miniProLoginParam == null)
            throw new BlueException(EMPTY_PARAM);

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
        LOGGER.info("Mono<MemberAuth> loginByWechat(WechatProLoginParam wechatProLoginParam), wechatProLoginParam = {}", wechatProLoginParam);
        if (wechatProLoginParam == null)
            throw new BlueException(EMPTY_PARAM);

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
        LOGGER.info("Mono<AuthAsserted> assertAuth(AssertAuth assertAuth), assertAuth = {}", assertAuth);
        return just(assertAuth)
                .switchIfEmpty(error(() -> new BlueException(UNAUTHORIZED)))
                .flatMap(aa -> {
                    String resourceKey = REQ_RES_KEY_GENERATOR.apply(
                            aa.getMethod().intern(), aa.getUri());

                    Resource resource = RESOURCE_GETTER.apply(resourceKey);
                    if (resource == null)
                        return error(() -> new BlueException(NOT_FOUND));

                    if (!resource.getAuthenticate())
                        return NO_AUTH_REQUIRED_RES_GEN.apply(resource);

                    String jwt = aa.getAuthentication();
                    MemberPayload memberPayload = JWT_PARSER.apply(jwt);

                    return authInfoCache.getAuthInfo(memberPayload.getKeyId())
                            .flatMap(v -> {
                                if (v == null || "".equals(v))
                                    return error(() -> new BlueException(UNAUTHORIZED));

                                AuthInfo authInfo = AUTH_INFO_PARSER.apply(v);
                                if (!jwt.equals(authInfo.getJwt()))
                                    return error(() -> new BlueException(UNAUTHORIZED));
                                if (!AUTHORIZATION_RES_CHECKER.apply(authInfo.getRoleId(), resourceKey))
                                    return error(() -> new BlueException(FORBIDDEN.status, FORBIDDEN.code, FORBIDDEN.message));

                                boolean reqUnDecryption = resource.getRequestUnDecryption();
                                boolean resUnEncryption = resource.getResponseUnEncryption();

                                return just(new AuthAsserted(true, reqUnDecryption, resUnEncryption, resource.getExistenceRequestBody(), resource.getExistenceResponseBody(),
                                        reqUnDecryption && resUnEncryption ? "" : authInfo.getPubKey(),
                                        new Access(parseLong(memberPayload.getId()), authInfo.getRoleId(), memberPayload.getLoginType().intern(),
                                                memberPayload.getDeviceType().intern(), parseLong(memberPayload.getLoginTime())), OK.message));
                            });
                });
    }

    /**
     * generate member auth
     *
     * @param authGenElement
     * @return
     */
    @Override
    public Mono<MemberAuth> generateAuthMono(AuthGenElement authGenElement) {
        LOGGER.info("Mono<MemberAuth> generateAuthMono(AuthGenParam authGenParam), authGenParam = {}", authGenElement);
        return authGenElement != null ?
                just(authGenElement)
                        .flatMap(agp -> {
                            Long memberId = agp.getMemberId();
                            String loginType = agp.getLoginType().intern();
                            String deviceType = agp.getDeviceType().intern();
                            KeyPair keyPair = initKeyPair();

                            return just(new MemberPayload(
                                    randomAlphanumeric(RANDOM_ID_LENGTH),
                                    genSessionKey(memberId, loginType, deviceType),
                                    valueOf(memberId),
                                    loginType, deviceType,
                                    valueOf(TIME_STAMP_GETTER.get()))
                            ).flatMap(mp -> {
                                String jwt = jwtProcessor.create(mp);
                                String authInfoJson = GSON.toJson(new AuthInfo(jwt, authGenElement.getRoleId(), keyPair.getPubKey()));

                                return authInfoCache.setAuthInfo(mp.getKeyId(), authInfoJson)
                                        .flatMap(b -> {
                                            LOGGER.info("authInfoJson = {}, keyPairDTO = {}", authInfoJson, keyPair);
                                            if (b)
                                                return just(new MemberAuth(jwt, keyPair.getPriKey()));

                                            LOGGER.error("authInfoCache.setAuthInfo(mp.getKeyId(), authInfoJson), failed, mp = {}", mp);
                                            return error(() -> new BlueException(INTERNAL_SERVER_ERROR));
                                        });
                            });
                        })
                :
                error(() -> new BlueException(INTERNAL_SERVER_ERROR));
    }

    /**
     * invalid auth by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<Boolean> invalidAuthByAccess(Access access) {
        LOGGER.info("Mono<Boolean> invalidAuthByAccess(Access access), access = {}", access);
        return access != null ?
                authInfoCache.invalidAuthInfo(genSessionKey(access.getId(), access.getLoginType().intern(), access.getDeviceType().intern()))
                :
                error(() -> new BlueException(UNAUTHORIZED));
    }

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    @Override
    public Mono<Boolean> invalidAuthByJwt(String jwt) {
        LOGGER.info("Mono<Boolean> invalidAuthByJwt(String jwt), jwt = {}", jwt);
        try {
            return authInfoCache.invalidAuthInfo(JWT_PARSER.apply(jwt).getKeyId());
        } catch (Exception e) {
            LOGGER.error("Mono<Boolean> invalidAuthByJwt(String jwt) failed, jwt = {}, e = {}", jwt, e);
            return just(false);
        }
    }

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Boolean> invalidAuthByMemberId(Long memberId) {
        LOGGER.info("Mono<Boolean> invalidAuthByMemberId(Long memberId), memberId = {}", memberId);
        if (isValidIdentity(memberId))
            return fromFuture(supplyAsync(() -> INVALID_AUTH_BY_MEMBER_ID_TASK.apply(memberId), executorService));

        return error(() -> new BlueException(INVALID_IDENTITY));
    }

    /**
     * invalid local auth by key id
     *
     * @param keyId
     */
    @Override
    public Mono<Boolean> invalidLocalAuthByKeyId(String keyId) {
        LOGGER.info("Mono<Boolean> invalidLocalAuthByKeyId(String keyId), keyId = {}", keyId);
        return authInfoCache.invalidLocalAuthInfo(keyId);
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
    public void refreshMemberRoleById(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("void refreshMemberRoleById(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}", memberId, roleId);
        AuthInfoRefreshElement authInfoRefreshElement = new AuthInfoRefreshElement(memberId,
                VALID_LOGIN_TYPES, VALID_DEVICE_TYPES, ROLE, valueOf(roleId).intern());
        executorService.submit(() ->
                refreshAuthElementMultiTypes(authInfoRefreshElement));
    }

    /**
     * update member sec key by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<String> updateSecKeyByAccess(Access access) {
        LOGGER.info("Mono<String> updateSecKeyByAccess(Access access), access = {}", access);
        return access != null ?
                just(initKeyPair())
                        .flatMap(keyPair -> {
                            refreshAuthInfoElementByKeyId(
                                    genSessionKey(access.getId(), access.getLoginType().intern(), access.getDeviceType().intern()),
                                    PUB_KEY, keyPair.getPubKey());
                            return just(keyPair.getPriKey());
                        })
                :
                error(() -> new BlueException(UNAUTHORIZED));
    }

    /**
     * get member's authority by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnRole> getAuthorityMonoByAccess(Access access) {
        LOGGER.info("Mono<AuthorityBaseOnRole> getAuthorityMonoByAccess(Access access), access = {}", access);
        return access != null ?
                getAuthorityMonoByRoleId(access.getRoleId())
                :
                error(() -> new BlueException(UNAUTHORIZED));
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
        return memberId != null ?
                memberRoleRelationService.getRoleIdMonoByMemberId(memberId)
                        .flatMap(roleIdOpt ->
                                roleIdOpt.map(this::getAuthorityMonoByRoleId)
                                        .orElseGet(() -> {
                                            LOGGER.error("Mono<AuthorityBaseOnRole> getAuthorityMonoByMemberId(Long memberId), role id of the member id -> {} not found", memberId);
                                            return error(() -> new BlueException(INTERNAL_SERVER_ERROR));
                                        }))
                :
                error(() -> new BlueException(INVALID_IDENTITY));
    }

}
