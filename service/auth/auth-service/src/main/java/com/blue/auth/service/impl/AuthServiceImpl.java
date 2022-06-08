package com.blue.auth.service.impl;

import com.blue.auth.api.model.*;
import com.blue.auth.component.access.AccessInfoCache;
import com.blue.auth.config.deploy.BlockingDeploy;
import com.blue.auth.config.deploy.SessionKeyDeploy;
import com.blue.auth.event.producer.InvalidLocalAccessProducer;
import com.blue.auth.model.AccessInfo;
import com.blue.auth.model.AuthInfoRefreshElement;
import com.blue.auth.model.MemberAccess;
import com.blue.auth.model.MemberAuth;
import com.blue.auth.repository.entity.RefreshInfo;
import com.blue.auth.repository.entity.Resource;
import com.blue.auth.repository.entity.Role;
import com.blue.auth.repository.entity.RoleResRelation;
import com.blue.auth.service.inter.*;
import com.blue.base.common.base.BlueChecker;
import com.blue.base.constant.auth.AccessInfoRefreshElementType;
import com.blue.base.constant.auth.CredentialType;
import com.blue.base.constant.auth.DeviceType;
import com.blue.base.constant.common.CacheKeyPrefix;
import com.blue.base.constant.common.Symbol;
import com.blue.base.model.common.Access;
import com.blue.base.model.common.InvalidLocalAccessEvent;
import com.blue.base.model.common.KeyPair;
import com.blue.base.model.exps.BlueException;
import com.blue.jwt.common.JwtProcessor;
import com.blue.redisson.component.SynchronizedProcessor;
import com.google.gson.JsonSyntaxException;
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

import static com.blue.auth.converter.AuthModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER;
import static com.blue.auth.converter.AuthModelConverters.ROLE_2_ROLE_INFO_CONVERTER;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.*;
import static com.blue.base.common.base.ConstantProcessor.*;
import static com.blue.base.common.base.RsaProcessor.initKeyPair;
import static com.blue.base.constant.auth.AccessInfoRefreshElementType.PUB_KEY;
import static com.blue.base.constant.auth.AccessInfoRefreshElementType.ROLE;
import static com.blue.base.constant.auth.CredentialType.NOT_LOGGED_IN;
import static com.blue.base.constant.auth.DeviceType.UNKNOWN;
import static com.blue.base.constant.common.BlueNumericalValue.*;
import static com.blue.base.constant.common.ResponseElement.*;
import static com.blue.base.constant.common.SpecialSecKey.NOT_LOGGED_IN_SEC_KEY;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.base.constant.common.SyncKeyPrefix.AUTH_INVALID_BY_MEMBER_ID_PRE;
import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
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
 * auth service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = getLogger(AuthServiceImpl.class);

    private RefreshInfoService refreshInfoService;

    private JwtProcessor<MemberPayload> jwtProcessor;

    private AccessInfoCache accessInfoCache;

    private final StringRedisTemplate stringRedisTemplate;

    private final RoleService roleService;

    private final ResourceService resourceService;

    private final RoleResRelationService roleResRelationService;

    private final MemberRoleRelationService memberRoleRelationService;

    private final InvalidLocalAccessProducer invalidLocalAccessProducer;

    private final ExecutorService executorService;

    private SynchronizedProcessor synchronizedProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AuthServiceImpl(RefreshInfoService refreshInfoService, JwtProcessor<MemberPayload> jwtProcessor, AccessInfoCache accessInfoCache, StringRedisTemplate stringRedisTemplate,
                           RoleService roleService, ResourceService resourceService, RoleResRelationService roleResRelationService, MemberRoleRelationService memberRoleRelationService,
                           InvalidLocalAccessProducer invalidLocalAccessProducer, ExecutorService executorService, SynchronizedProcessor synchronizedProcessor,
                           SessionKeyDeploy sessionKeyDeploy, BlockingDeploy blockingDeploy) {
        this.refreshInfoService = refreshInfoService;
        this.jwtProcessor = jwtProcessor;
        this.accessInfoCache = accessInfoCache;
        this.stringRedisTemplate = stringRedisTemplate;
        this.roleService = roleService;
        this.resourceService = resourceService;
        this.roleResRelationService = roleResRelationService;
        this.memberRoleRelationService = memberRoleRelationService;
        this.invalidLocalAccessProducer = invalidLocalAccessProducer;
        this.executorService = executorService;
        this.synchronizedProcessor = synchronizedProcessor;

        this.randomIdLen = sessionKeyDeploy.getRanLen();
        this.maxWaitingMillisForRefresh = blockingDeploy.getBlockingMillis();
        this.refreshExpireMillis = this.jwtProcessor.getRefreshExpireMillis();
    }

    private int randomIdLen;

    private long maxWaitingMillisForRefresh;

    private long refreshExpireMillis;

    private final Supplier<Date> REFRESH_EXPIRE_AT_GETTER = () ->
            new Date(MILLIS_STAMP_SUP.get() + refreshExpireMillis);

    public static final String
            SESSION_KEY_PRE = CacheKeyPrefix.SESSION_PRE.prefix,
            ACCESS_REFRESH_KEY_PRE = CacheKeyPrefix.ACCESS_REFRESH_PRE.prefix,
            PAR_CONCATENATION = Symbol.PAR_CONCATENATION.identity,
            PATH_SEPARATOR = Symbol.PATH_SEPARATOR.identity;

    private static final List<CredentialType> VALID_CREDENTIAL_TYPES = of(CredentialType.values())
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

    /**
     * auth info refreshing mark
     */
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
     * accessInfo parser
     */
    private final Function<String, AccessInfo> ACCESS_INFO_PARSER = authInfoStr -> {
        try {
            return GSON.fromJson(authInfoStr, AccessInfo.class);
        } catch (JsonSyntaxException e) {
            throw new BlueException(UNAUTHORIZED);
        }
    };

    /**
     * blocker when resource or relation refreshing
     */
    private final Supplier<Boolean> RES_OR_REL_REFRESHING_BLOCKER = () -> {
        if (authorityInfosRefreshing) {
            long start = currentTimeMillis();
            while (authorityInfosRefreshing) {
                if (currentTimeMillis() - start > maxWaitingMillisForRefresh)
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
                if (currentTimeMillis() - start > maxWaitingMillisForRefresh)
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
        RES_OR_REL_REFRESHING_BLOCKER.get();
        return keyAndResourceMapping.get(resourceKey);
    };

    /**
     * authorization checker
     */
    private final BiFunction<Long, String, Boolean> AUTHORIZATION_RES_CHECKER = (roleId, resourceKey) -> {
        RES_OR_REL_REFRESHING_BLOCKER.get();
        return ofNullable(roleAndResourcesKeyMapping.get(roleId)).map(set -> set.contains(resourceKey)).orElse(false);
    };

    /**
     * credential type identity -> credential type nature
     */
    private static final UnaryOperator<String> CREDENTIAL_TYPE_2_NATURE_CONVERTER = identity ->
            getCredentialTypeByIdentity(identity).nature.intern();

    /**
     * UN_AUTH
     */
    private static final Access NO_AUTH_ACCESS = new Access(NOT_LOGGED_IN_MEMBER_ID.value,
            NOT_LOGGED_IN_ROLE_ID.value, NOT_LOGGED_IN.identity, UNKNOWN.identity, NOT_LOGGED_IN_TIME.value);

    /**
     * Resources do not require authentication access
     */
    private final Function<Resource, Mono<AccessAsserted>> NO_AUTH_REQUIRED_RES_GEN = resource ->
            just(new AccessAsserted(false, true, true,
                    resource.getExistenceRequestBody(), resource.getExistenceResponseBody(),
                    NOT_LOGGED_IN_SEC_KEY.value, NO_AUTH_ACCESS, NO_AUTH_REQUIRED_RESOURCE.message));

    /**
     * refresh element type -> authInfo packagers
     */
    private static final Map<AccessInfoRefreshElementType, BiConsumer<AccessInfo, String>> RE_PACKAGERS = new HashMap<>(4, 1.0f);

    /**
     * generate access refresh sync key
     */
    private static final Map<AccessInfoRefreshElementType, BinaryOperator<String>> ACCESS_REFRESH_KEY_GENS = new HashMap<>(4, 1.0f);

    static {
        RE_PACKAGERS.put(ROLE, (ai, ele) -> {
            if (isNull(ai) || isNull(ele) || EMPTY_DATA.value.equals(ele))
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
            if (isNull(ai) || isNull(ele) || EMPTY_DATA.value.equals(ele))
                throw new BlueException(BAD_REQUEST);
            ai.setPubKey(ele);
        });

        ACCESS_REFRESH_KEY_GENS.put(ROLE, (snKey, eleValue) ->
                ACCESS_REFRESH_KEY_PRE + snKey + PAR_CONCATENATION + ROLE.name() + PAR_CONCATENATION + eleValue);
        ACCESS_REFRESH_KEY_GENS.put(PUB_KEY, (snKey, eleValue) ->
                ACCESS_REFRESH_KEY_PRE + snKey + PAR_CONCATENATION + PUB_KEY.name() + PAR_CONCATENATION + eleValue.hashCode());

        LOGGER.info("static codes, RE_PACKAGERS = {}, ACCESS_REFRESH_KEY_GENS = {}", RE_PACKAGERS, ACCESS_REFRESH_KEY_GENS);
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
        RES_OR_REL_REFRESHING_BLOCKER.get();
        return roleAndResourceInfosMapping.get(roleId);
    };

    /**
     * generate redis sessionKey
     *
     * @param id
     * @param credentialTypeIdentity
     * @param deviceTypeIdentity
     * @return
     */
    private static String genSessionKey(Long id, String credentialTypeIdentity, String deviceTypeIdentity) {
        LOGGER.info("String genSessionKey(Long id, String credentialTypeIdentity, String deviceTypeIdentity), id = {}, credentialTypeIdentity = {}, deviceTypeIdentity = {}", id, credentialTypeIdentity, deviceTypeIdentity);
        if (isInvalidIdentity(id) || isBlank(deviceTypeIdentity))
            throw new BlueException(BAD_REQUEST);

        return SESSION_KEY_PRE + id + PAR_CONCATENATION + CREDENTIAL_TYPE_2_NATURE_CONVERTER.apply(credentialTypeIdentity).intern() + PAR_CONCATENATION + deviceTypeIdentity;
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
    private static String genAccessInfoRefreshLockKey(String snKey, AccessInfoRefreshElementType elementType, String elementValue) {
        LOGGER.info("String genAccessInfoRefreshLockKey(String snKey, AuthInfoRefreshElementType elementType, String elementValue), snKey = {}, elementType = {}, elementValue = {}", snKey, elementType, elementValue);
        if (isBlank(snKey) || isNull(elementType) || isBlank(elementValue))
            throw new BlueException(BAD_REQUEST);

        return ACCESS_REFRESH_KEY_GENS.get(elementType).apply(snKey, elementValue);
    }

    /**
     * delete all refresh token info by key id
     */
    private final Function<String, Mono<Boolean>> REFRESH_INFOS_BY_ID_DELETER = id ->
            refreshInfoService.deleteRefreshInfo(id)
                    .onErrorResume(throwable -> {
                        LOGGER.warn("BiFunction<MemberPayload, Long, Mono<MemberAuth>> AUTH_GENERATOR,  refreshInfoService.deleteRefreshInfoById(id) failed, id = {}, throwable = {}", id, throwable);
                        return empty();
                    })
                    .then(just(true));

    /**
     * delete all refresh token info by member id
     */
    private final Function<Long, Mono<Boolean>> REFRESH_INFOS_BY_MEMBER_ID_DELETER = memberId -> {
        RefreshInfo probe = new RefreshInfo();
        probe.setMemberId(ofNullable(memberId).filter(BlueChecker::isValidIdentity)
                .map(String::valueOf).orElseThrow(() -> new BlueException(BAD_REQUEST)));

        return refreshInfoService.selectRefreshInfoMonoByProbe(probe)
                .flatMap(refreshInfoService::deleteRefreshInfos)
                .onErrorResume(throwable -> {
                    LOGGER.warn("Function<Long, Mono<Boolean>> REFRESH_INFOS_BY_MEMBER_ID_DELETER failed, memberId = {}, throwable = {}", memberId, throwable);
                    return empty();
                })
                .then(just(true));
    };

    /**
     * generate member payload mono
     *
     * @param memberId
     * @param credentialType
     * @param deviceType
     * @return
     */
    private Mono<MemberPayload> genMemberPayloadMono(Long memberId, String credentialType, String deviceType) {
        LOGGER.info("Mono<MemberPayload> genMemberPayloadMono(Long memberId, String credentialType, String deviceType), memberId = {}, credentialType = {}, deviceType = {}", memberId, credentialType, deviceType);
        if (isInvalidIdentity(memberId))
            return error(() -> new BlueException(BAD_REQUEST));

        assertCredentialType(credentialType, false);
        assertDeviceType(deviceType, false);

        return just(new MemberPayload(
                randomAlphanumeric(randomIdLen),
                genSessionKey(memberId, credentialType, deviceType),
                valueOf(memberId),
                credentialType, deviceType,
                valueOf(TIME_STAMP_GETTER.get())));
    }

    /**
     * generate access
     */
    private final BiFunction<MemberPayload, Long, Mono<MemberAccess>> ACCESS_GENERATOR = (memberPayload, roleId) -> {
        LOGGER.info("BiFunction<MemberPayload, Long, Mono<MemberAccess>> ACCESS_GENERATOR, memberPayload = {}, roleId = {}", memberPayload, roleId);
        if (isNull(memberPayload) || isInvalidIdentity(roleId))
            return error(() -> new BlueException(BAD_REQUEST));

        String jwt = jwtProcessor.create(memberPayload);
        KeyPair keyPair = initKeyPair();
        String authInfoJson = GSON.toJson(new AccessInfo(jwt, roleId, keyPair.getPubKey()));

        return accessInfoCache.setAccessInfo(memberPayload.getKeyId(), authInfoJson)
                .flatMap(b -> {
                    LOGGER.info("authInfoJson = {}, keyPair = {}", authInfoJson, keyPair);
                    if (b)
                        return just(new MemberAccess(jwt, keyPair.getPriKey()));

                    LOGGER.error("authInfoCache.setAuthInfo(memberPayload.getKeyId(), authInfoJson), failed, memberPayload = {}, roleId = {}, keyPair = {}, authInfoJson = {}", memberPayload, roleId, keyPair, authInfoJson);
                    return error(() -> new BlueException(INTERNAL_SERVER_ERROR));
                });
    };

    /**
     * generate auth(access and refresh)
     */
    private final BiFunction<MemberPayload, Long, Mono<MemberAuth>> AUTH_GENERATOR = (memberPayload, roleId) -> {
        LOGGER.info("BiFunction<MemberPayload, Long, Mono<MemberAuth>> AUTH_GENERATOR, memberPayload = {}, roleId = {}", memberPayload, roleId);

        return ACCESS_GENERATOR.apply(memberPayload, roleId)
                .flatMap(access -> {
                    memberPayload.setGamma(randomAlphanumeric(randomIdLen));

                    return refreshInfoService.insertRefreshInfo(new RefreshInfo(memberPayload.getKeyId(), memberPayload.getGamma(), memberPayload.getId(),
                                    memberPayload.getCredentialType().intern(), memberPayload.getDeviceType().intern(), memberPayload.getLoginTime(), REFRESH_EXPIRE_AT_GETTER.get()))
                            .onErrorResume(throwable -> {
                                LOGGER.warn("BiFunction<MemberPayload, Long, Mono<MemberAuth>> AUTH_GENERATOR, refreshInfoService.insertRefreshInfo(refreshInfo) failed, memberPayload = {}, roleId = {}, throwable = {}",
                                        memberPayload, roleId, throwable);
                                return just(new RefreshInfo());
                            })
                            .flatMap(ig -> just(jwtProcessor.create(memberPayload)))
                            .flatMap(jwt -> just(new MemberAuth(access.getAuth(), access.getSecKey(), jwt)));
                });
    };

    /**
     * assert auth
     */
    private final BiConsumer<MemberPayload, RefreshInfo> AUTH_ASSERTER = (memberPayload, refreshInfo) -> {
        try {
            if (memberPayload == null || refreshInfo == null)
                throw new BlueException(UNAUTHORIZED);

            if (parseLong(refreshInfo.getLoginTime()) + refreshExpireMillis <= TIME_STAMP_GETTER.get()) {
                refreshInfoService.deleteRefreshInfo(refreshInfo.getId())
                        .doOnError(throwable -> LOGGER.error("AUTH_ASSERTER ->  refreshInfoService.deleteRefreshInfoById() failed, refreshInfo = {}, throwable = {}", refreshInfo, throwable))
                        .subscribe();
                throw new BlueException(UNAUTHORIZED);
            }

            if (memberPayload.getGamma().equals(refreshInfo.getGamma())
                    && memberPayload.getKeyId().equals(refreshInfo.getId())
                    && memberPayload.getId().equals(refreshInfo.getMemberId())
                    && memberPayload.getCredentialType().equals(refreshInfo.getCredentialType())
                    && memberPayload.getLoginTime().equals(refreshInfo.getLoginTime()))
                return;
        } catch (Exception e) {
            throw new BlueException(UNAUTHORIZED);
        }

        throw new BlueException(UNAUTHORIZED);
    };

    /**
     * refresh access info (role id/sec key) by key id
     *
     * @param keyId
     * @param elementType
     * @param elementValue
     */
    private void refreshAccessInfoElementByKeyId(String keyId, AccessInfoRefreshElementType elementType, String elementValue) {
        LOGGER.info("void refreshAccessInfoElementByKeyId(String keyId, AuthInfoRefreshElementType elementType, String elementValue), keyId = {}, elementType = {}, elementValue = {}", keyId, elementType, elementValue);
        String originalAuthInfoJson = ofNullable(stringRedisTemplate.opsForValue().get(keyId))
                .orElse(EMPTY_DATA.value);

        if (isBlank(originalAuthInfoJson)) {
            LOGGER.info("member's authInfo is blank, can't refresh");
            return;
        }

        AccessInfo accessInfo;
        try {
            accessInfo = GSON.fromJson(originalAuthInfoJson, AccessInfo.class);
        } catch (JsonSyntaxException e) {
            LOGGER.info("member's authInfo is invalid, can't refresh");
            return;
        }

        synchronizedProcessor.handleTaskWithTryLock(genAccessInfoRefreshLockKey(keyId, elementType, elementValue), () ->
                        synchronizedProcessor.handleTaskWithLock(GLOBAL_LOCK_KEY_GEN.apply(keyId), () -> {
                            try {
                                RE_PACKAGERS.get(elementType).accept(accessInfo, elementValue);
                                stringRedisTemplate.opsForValue()
                                        .set(keyId, GSON.toJson(accessInfo), Duration.of(ofNullable(stringRedisTemplate.getExpire(keyId, SECONDS)).orElse(0L), ChronoUnit.SECONDS));

                                Boolean localAddressInvalid = this.invalidateLocalAccessByKeyId(keyId).toFuture().join();
                                LOGGER.info("this.invalidateLocalAccessByKeyId(keyId).toFuture().join(), localAddressInvalid = {}", localAddressInvalid);
                            } catch (Exception e) {
                                LOGGER.error("invalidLocalAuthProducer send failed, keyId = {}, e = {}", keyId, e);
                            }
                        })
                , true);
    }

    /**
     * refresh auth elements
     *
     * @param authInfoRefreshElement
     */
    private void refreshAuthElementByMultiTypes(AuthInfoRefreshElement authInfoRefreshElement) {
        LOGGER.info("void refreshAuthElementByMultiTypes(AuthInfoRefreshParam authInfoRefreshParam), authInfoRefreshParam = {}", authInfoRefreshElement);
        Long memberId = authInfoRefreshElement.getMemberId();
        if (isInvalidIdentity(memberId))
            throw new BlueException(BAD_REQUEST);

        AccessInfoRefreshElementType elementType = authInfoRefreshElement.getElementType();
        if (isNull(elementType))
            throw new BlueException(BAD_REQUEST);

        String elementValue = authInfoRefreshElement.getElementValue();
        if (isBlank(elementValue))
            throw new BlueException(BAD_REQUEST);

        List<String> credentialTypes = ofNullable(authInfoRefreshElement.getCredentialTypes())
                .filter(lts -> lts.size() > 0)
                .map(lts ->
                        lts.stream()
                                .map(lt -> lt.identity)
                                .distinct()
                                .filter(lti -> !lti.equals(NOT_LOGGED_IN.identity))
                                .collect(toList())
                ).orElseGet(Collections::emptyList);
        if (isEmpty(credentialTypes))
            return;

        List<String> deviceTypes = ofNullable(authInfoRefreshElement.getDeviceTypes())
                .filter(dts -> dts.size() > 0)
                .map(dts ->
                        dts.stream()
                                .map(dt -> dt.identity)
                                .distinct()
                                .filter(dti -> !dti.equals(UNKNOWN.identity))
                                .collect(toList())
                ).orElseGet(Collections::emptyList);
        if (isEmpty(deviceTypes))
            return;

        for (String credentialType : credentialTypes)
            for (String deviceType : deviceTypes)
                refreshAccessInfoElementByKeyId(
                        genSessionKey(memberId, credentialType.intern(), deviceType.intern()), elementType, elementValue);
    }

    /**
     * generate auth invalid sync key
     */
    private static final Function<Long, String> AUTH_INVALID_BY_MID_SYNC_KEY_GEN = memberId -> AUTH_INVALID_BY_MEMBER_ID_PRE.prefix + memberId;

    /**
     * auth info invalid task
     */
    private final Function<Long, Boolean> INVALID_AUTH_BY_MEMBER_ID_TASK = memberId ->
            REFRESH_INFOS_BY_MEMBER_ID_DELETER.apply(memberId)
                    .flatMap(b -> {
                        if (!b)
                            LOGGER.error("REFRESH_INFOS_BY_MEMBER_ID_DELETER.apply(memberId), result is false");

                        String keyId;
                        for (CredentialType credentialType : VALID_CREDENTIAL_TYPES)
                            for (DeviceType deviceType : VALID_DEVICE_TYPES) {
                                keyId = genSessionKey(memberId, credentialType.identity, deviceType.identity);
                                accessInfoCache.invalidAccessInfo(keyId).toFuture().join();
                                this.invalidateLocalAccessByKeyId(keyId).toFuture().join();
                            }

                        return just(true);
                    }).onErrorResume(throwable -> {
                        LOGGER.error("REFRESH_INFOS_BY_MEMBER_ID_DELETER failed, throwable = {}", throwable);
                        return just(false);
                    }).toFuture().join();

    /**
     * auth delete task
     */
    private final Function<Long, Boolean> INVALID_AUTH_BY_MEMBER_ID_SYNC_TASK = memberId ->
            synchronizedProcessor.handleSupWithTryLock(AUTH_INVALID_BY_MID_SYNC_KEY_GEN.apply(memberId),
                    () -> INVALID_AUTH_BY_MEMBER_ID_TASK.apply(memberId), () -> true, () -> false);

    /**
     * get authority by role id
     */
    private final Function<Long, Mono<AuthorityBaseOnRole>> AUTHORITY_MONO_BY_ROLE_ID_GETTER = roleId -> {
        LOGGER.info("Mono<AuthorityBaseOnRole> getAuthorityByRoleOpt(Long roleId), roleId = {}", roleId);
        return just(ROLE_INFO_BY_ID_GETTER.apply(roleId)
                .map(role ->
                        new AuthorityBaseOnRole(role, RESOURCE_INFOS_BY_ROLE_ID_GETTER.apply(roleId)))
                .orElseThrow(() -> {
                    LOGGER.error("role info doesn't exist, roleId = {}", roleId);
                    return new BlueException(BAD_REQUEST);
                }));
    };

    /**
     * init
     */
    @PostConstruct
    public void init() {
        refreshSystemAuthorityInfos();
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
                                        RES_KEY_GENERATOR.apply(r.getRequestMethod().toUpperCase().intern(),
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
                .collect(toMap(r -> RES_KEY_GENERATOR.apply(r.getRequestMethod().toUpperCase().intern(),
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
     * assert auth
     *
     * @param accessAssert
     * @return
     */
    @Override
    public Mono<AccessAsserted> assertAccessMono(AccessAssert accessAssert) {
        LOGGER.info("Mono<AuthAsserted> assertAuth(AssertAuth assertAuth), assertAuth = {}", accessAssert);
        return just(accessAssert)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(UNAUTHORIZED))))
                .flatMap(aa -> {
                    String resourceKey = REQ_RES_KEY_GENERATOR.apply(
                            aa.getMethod().intern(), aa.getUri());

                    Resource resource = RESOURCE_GETTER.apply(resourceKey);
                    if (isNull(resource))
                        return error(() -> new BlueException(NOT_FOUND));

                    if (!resource.getAuthenticate())
                        return NO_AUTH_REQUIRED_RES_GEN.apply(resource);

                    String jwt = aa.getAuthentication();
                    MemberPayload memberPayload = jwtProcessor.parse(jwt);

                    return accessInfoCache.getAccessInfo(memberPayload.getKeyId())
                            .switchIfEmpty(defer(() -> error(() -> new BlueException(UNAUTHORIZED))))
                            .flatMap(v -> {
                                if (isBlank(v))
                                    return error(() -> new BlueException(UNAUTHORIZED));

                                AccessInfo accessInfo = ACCESS_INFO_PARSER.apply(v);
                                if (!jwt.equals(accessInfo.getJwt()))
                                    return error(() -> new BlueException(UNAUTHORIZED));
                                if (!AUTHORIZATION_RES_CHECKER.apply(accessInfo.getRoleId(), resourceKey))
                                    return error(() -> new BlueException(FORBIDDEN.status, FORBIDDEN.code, FORBIDDEN.message));

                                boolean reqUnDecryption = resource.getRequestUnDecryption();
                                boolean resUnEncryption = resource.getResponseUnEncryption();

                                return just(new AccessAsserted(true, reqUnDecryption, resUnEncryption, resource.getExistenceRequestBody(), resource.getExistenceResponseBody(),
                                        reqUnDecryption && resUnEncryption ? EMPTY_DATA.value : accessInfo.getPubKey(),
                                        new Access(parseLong(memberPayload.getId()), accessInfo.getRoleId(), memberPayload.getCredentialType().intern(),
                                                memberPayload.getDeviceType().intern(), parseLong(memberPayload.getLoginTime())), OK.message));
                            });
                });
    }

    /**
     * generate member auth
     *
     * @param memberId
     * @param credentialType
     * @param deviceType
     * @return
     */
    @Override
    public Mono<MemberAuth> generateAuthMono(Long memberId, String credentialType, String deviceType) {
        LOGGER.info("Mono<MemberAuth> generateAuthMono(Long memberId, String credentialType, String deviceType), memberId = {}, credentialType = {}, deviceType", memberId, credentialType, deviceType);
        return isValidIdentity(memberId) && isNotBlank(credentialType) && isNotBlank(deviceType) ?
                zip(genMemberPayloadMono(memberId, credentialType, deviceType),
                        memberRoleRelationService.getRoleIdMonoByMemberId(memberId)
                                .map(ridOpt -> ridOpt.orElseThrow(() -> new BlueException(MEMBER_NOT_HAS_A_ROLE)))
                ).flatMap(tuple2 ->
                        AUTH_GENERATOR.apply(tuple2.getT1(), tuple2.getT2()))
                :
                error(() -> new BlueException(BAD_REQUEST));
    }

    /**
     * generate member auth with auto register
     *
     * @param memberId
     * @param roleId
     * @param credentialType
     * @param deviceType
     * @return
     */
    @Override
    public Mono<MemberAuth> generateAuthMono(Long memberId, Long roleId, String credentialType, String deviceType) {
        LOGGER.info("Mono<MemberAuth> generateAuthMono(Long memberId, Long roleId, String credentialType, String deviceType) , memberId = {}, roleId = {}, credentialType = {}, deviceType", memberId, roleId, credentialType, deviceType);
        return isValidIdentity(memberId) && isValidIdentity(roleId) && isNotBlank(credentialType) && isNotBlank(deviceType) ?
                genMemberPayloadMono(memberId, credentialType, deviceType)
                        .flatMap(memberPayload ->
                                AUTH_GENERATOR.apply(memberPayload, roleId))
                :
                error(() -> new BlueException(BAD_REQUEST));
    }

    /**
     * generate member access
     *
     * @param memberId
     * @param credentialType
     * @param deviceType
     * @return
     */
    @Override
    public Mono<MemberAccess> generateAccessMono(Long memberId, String credentialType, String deviceType) {
        LOGGER.info("Mono<MemberAccess> generateAccessMono(Long memberId, String credentialType, String deviceType), memberId = {}, credentialType = {}, deviceType", memberId, credentialType, deviceType);
        return isValidIdentity(memberId) && isNotBlank(credentialType) && isNotBlank(deviceType) ?
                zip(genMemberPayloadMono(memberId, credentialType, deviceType),
                        memberRoleRelationService.getRoleIdMonoByMemberId(memberId)
                                .map(ridOpt -> ridOpt.orElseThrow(() -> new BlueException(DATA_NOT_EXIST)))
                ).flatMap(tuple2 ->
                        ACCESS_GENERATOR.apply(tuple2.getT1(), tuple2.getT2()))
                :
                error(() -> new BlueException(BAD_REQUEST));
    }

    /**
     * refresh jwt by member payload
     *
     * @param memberPayload
     * @return
     */
    @Override
    public Mono<MemberAccess> refreshAccessByMemberPayload(MemberPayload memberPayload) {
        LOGGER.info("Mono<MemberAccess> refreshAccessByMemberPayload(MemberPayload memberPayload), memberPayload = {}", memberPayload);
        if (isNull(memberPayload))
            return error(() -> new BlueException(UNAUTHORIZED));

        return refreshInfoService.getRefreshInfoMono(memberPayload.getKeyId())
                .onErrorResume(t -> {
                    LOGGER.warn("Mono<MemberAccess> refreshAccess(String refresh), refreshInfoService.getRefreshInfoById(memberPayload.getKeyId()) failed, memberPayload = {}, t = {}", memberPayload, t);
                    return error(() -> new BlueException(UNAUTHORIZED));
                })
                .switchIfEmpty(defer(() -> error(() -> new BlueException(UNAUTHORIZED))))
                .flatMap(refreshInfo -> {
                    AUTH_ASSERTER.accept(memberPayload, refreshInfo);
                    return this.generateAccessMono(parseLong(refreshInfo.getMemberId()), refreshInfo.getCredentialType(), refreshInfo.getDeviceType());
                });
    }

    /**
     * refresh jwt by refresh token
     *
     * @param refresh
     * @return
     */
    @Override
    public Mono<MemberAccess> refreshAccessByRefresh(String refresh) {
        LOGGER.info("Mono<MemberAccess> refreshAccessByRefresh(String refresh), refresh = {}", refresh);
        return just(jwtProcessor.parse(refresh)).flatMap(this::refreshAccessByMemberPayload);
    }

    /**
     * invalid auth by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<Boolean> invalidateAuthByAccess(Access access) {
        LOGGER.info("Mono<Boolean> invalidateAuthByAccess(Access access), access = {}", access);
        if (isNull(access))
            return error(() -> new BlueException(UNAUTHORIZED));

        String keyId = genSessionKey(access.getId(), access.getCredentialType().intern(), access.getDeviceType().intern());
        return zip(
                REFRESH_INFOS_BY_ID_DELETER.apply(keyId),
                accessInfoCache.invalidAccessInfo(keyId).flatMap(b -> this.invalidateLocalAccessByKeyId(keyId))
        ).flatMap(tuple2 -> just(tuple2.getT1() && tuple2.getT2()));
    }

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    @Override
    public Mono<Boolean> invalidateAuthByJwt(String jwt) {
        LOGGER.info("Mono<Boolean> invalidateAuthByJwt(String jwt), jwt = {}", jwt);
        try {
            MemberPayload memberPayload = jwtProcessor.parse(jwt);
            String keyId = memberPayload.getKeyId();
            return zip(
                    REFRESH_INFOS_BY_ID_DELETER.apply(memberPayload.getKeyId()),
                    accessInfoCache.invalidAccessInfo(keyId).flatMap(b -> this.invalidateLocalAccessByKeyId(keyId))
            ).flatMap(tuple2 -> just(tuple2.getT1() && tuple2.getT2()))
                    .onErrorResume(throwable -> {
                        LOGGER.warn("invalidateAuthByJwt(String jwt) failed, throwable = {}", throwable);
                        return just(false);
                    });
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
    public Mono<Boolean> invalidateAuthByMemberId(Long memberId) {
        LOGGER.info("Mono<Boolean> invalidAuthByMemberId(Long memberId), memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            return error(() -> new BlueException(INVALID_IDENTITY));

        return fromFuture(supplyAsync(() -> INVALID_AUTH_BY_MEMBER_ID_SYNC_TASK.apply(memberId), executorService));
    }

    /**
     * invalid local auth by key id
     *
     * @param keyId
     */
    @Override
    public Mono<Boolean> invalidateLocalAccessByKeyId(String keyId) {
        LOGGER.info("Mono<Boolean> invalidateLocalAccessByKeyId(String keyId), keyId = {}", keyId);
        invalidLocalAccessProducer.send(new InvalidLocalAccessEvent(keyId));
        return just(true);
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
    public Mono<Boolean> refreshMemberRoleById(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("void refreshMemberRoleById(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}", memberId, roleId);
        if (isInvalidIdentity(memberId) || isInvalidIdentity(roleId) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        AuthInfoRefreshElement authInfoRefreshElement = new AuthInfoRefreshElement(memberId,
                VALID_CREDENTIAL_TYPES, VALID_DEVICE_TYPES, ROLE, valueOf(roleId).intern());
        return fromRunnable(() -> executorService.execute(() ->
                refreshAuthElementByMultiTypes(authInfoRefreshElement))).then(just(true));
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
        return isNotNull(access) ?
                just(initKeyPair())
                        .flatMap(keyPair -> {
                            refreshAccessInfoElementByKeyId(
                                    genSessionKey(access.getId(), access.getCredentialType().intern(), access.getDeviceType().intern()),
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
        return isNotNull(access) ?
                AUTHORITY_MONO_BY_ROLE_ID_GETTER.apply(access.getRoleId())
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
        return isValidIdentity(memberId) ?
                memberRoleRelationService.getRoleIdMonoByMemberId(memberId)
                        .flatMap(roleIdOpt ->
                                roleIdOpt.map(AUTHORITY_MONO_BY_ROLE_ID_GETTER)
                                        .orElseGet(() -> {
                                            LOGGER.error("Mono<AuthorityBaseOnRole> getAuthorityMonoByMemberId(Long memberId), role id of the member id -> {} not found", memberId);
                                            return error(() -> new BlueException(INTERNAL_SERVER_ERROR));
                                        }))
                :
                error(() -> new BlueException(INVALID_IDENTITY));
    }

}