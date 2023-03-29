package com.blue.auth.service.impl;

import com.blue.auth.api.model.*;
import com.blue.auth.component.access.AccessInfoCache;
import com.blue.auth.config.deploy.AccessDeploy;
import com.blue.auth.event.producer.InvalidLocalAccessProducer;
import com.blue.auth.model.AccessInfo;
import com.blue.auth.model.EncryptedDataParam;
import com.blue.auth.model.MemberAccess;
import com.blue.auth.model.MemberAuth;
import com.blue.auth.repository.entity.RefreshInfo;
import com.blue.auth.repository.entity.Resource;
import com.blue.auth.repository.entity.Role;
import com.blue.auth.repository.entity.RoleResRelation;
import com.blue.auth.service.inter.*;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.auth.CredentialType;
import com.blue.basic.constant.auth.DeviceType;
import com.blue.basic.constant.common.CacheKeyPrefix;
import com.blue.basic.constant.common.Symbol;
import com.blue.basic.model.common.Access;
import com.blue.basic.model.common.KeyPair;
import com.blue.basic.model.common.Session;
import com.blue.basic.model.exps.BlueException;
import com.blue.jwt.component.JwtProcessor;
import com.blue.redisson.api.inter.HandleTask;
import com.blue.redisson.component.SynchronizedProcessor;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.*;

import static com.blue.auth.converter.AuthModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER;
import static com.blue.auth.converter.AuthModelConverters.ROLE_2_ROLE_INFO_CONVERTER;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.*;
import static com.blue.basic.common.base.ConstantProcessor.*;
import static com.blue.basic.common.base.RsaProcessor.initKeyPair;
import static com.blue.basic.constant.auth.CredentialType.NOT_LOGGED_IN;
import static com.blue.basic.constant.auth.DeviceType.UNKNOWN;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialSecKey.NOT_LOGGED_IN_SEC_KEY;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.basic.constant.common.SpecialStringElement.PRIVACY_VALUE;
import static com.blue.basic.constant.common.SyncKeyPrefix.ACCESS_UPDATE_PRE;
import static java.lang.Long.parseLong;
import static java.lang.String.valueOf;
import static java.util.Collections.*;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.*;

/**
 * auth service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "AlibabaAvoidComplexCondition"})
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
                           InvalidLocalAccessProducer invalidLocalAccessProducer, ExecutorService executorService, SynchronizedProcessor synchronizedProcessor, AccessDeploy accessDeploy) {
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

        this.gammaLength = accessDeploy.getGammaLength();
        this.refreshExpiresMillis = this.jwtProcessor.getRefreshExpiresMillis();
    }

    private int gammaLength;

    private long refreshExpiresMillis;

    private final Supplier<Date> REFRESH_EXPIRE_AT_GETTER = () ->
            new Date(MILLIS_STAMP_SUP.get() + refreshExpiresMillis);

    public static final String
            SESSION_KEY_PRE = CacheKeyPrefix.SESSION_PRE.prefix,
            PAR_CONCATENATION = Symbol.PAR_CONCATENATION.identity,
            PATH_SEPARATOR = Symbol.SLASH.identity;

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
     * get resource by resKey
     */
    private final Function<String, Resource> RESOURCE_GETTER = resourceKey ->
            isNotBlank(resourceKey) ? keyAndResourceMapping.get(resourceKey) : null;

    /**
     * authorization checker
     */
    private final BiFunction<List<Long>, String, Boolean> AUTHORIZATION_RES_CHECKER = (roleIds, resourceKey) ->
            isNotEmpty(roleIds) && isNotBlank(resourceKey) &&
                    roleIds.stream().anyMatch(roleId -> ofNullable(roleAndResourcesKeyMapping.get(roleId)).map(set -> set.contains(resourceKey)).orElse(false));

    /**
     * credential type identity -> credential type nature
     */
    private static final UnaryOperator<String> CREDENTIAL_TYPE_2_NATURE_CONVERTER = identity ->
            getCredentialTypeByIdentity(identity).nature.intern();

    /**
     * UN_AUTH
     */
    private static final Access NO_AUTH_ACCESS = new Access(NOT_LOGGED_IN_MEMBER_ID.value,
            singletonList(NOT_LOGGED_IN_ROLE_ID.value), NOT_LOGGED_IN.identity, UNKNOWN.identity, NOT_LOGGED_IN_TIME.value);

    /**
     * Resources do not require authentication access
     */
    private final Function<Resource, Mono<AccessAsserted>> NO_AUTH_REQUIRED_RES_GEN = resource ->
            just(new AccessAsserted(false, true, true,
                    resource.getExistenceRequestBody(), resource.getExistenceResponseBody(),
                    NOT_LOGGED_IN_SEC_KEY.value, NO_AUTH_ACCESS, NO_AUTH_REQUIRED_RESOURCE.message));

    /**
     * get role info by role id
     */
    private final Function<List<Long>, List<RoleInfo>> ROLES_INFO_BY_IDS_GETTER = roleIds ->
            isNotEmpty(roleIds) ? roleIds.stream().map(idAndRoleInfoMapping::get).collect(toList()) : emptyList();

    /**
     * get resource info list by role id
     */
    private final Function<Long, List<ResourceInfo>> RESOURCES_INFO_BY_ROLE_IDS_GETTER = roleId ->
            roleAndResourceInfosMapping.get(roleId);

    /**
     * generate redis sessionKey
     *
     * @param id
     * @param credentialTypeIdentity
     * @param deviceTypeIdentity
     * @return
     */
    private static String genSessionKeyId(Long id, String credentialTypeIdentity, String deviceTypeIdentity) {
        LOGGER.info("id = {}, credentialTypeIdentity = {}, deviceTypeIdentity = {}", id, credentialTypeIdentity, deviceTypeIdentity);
        if (isInvalidIdentity(id) || isBlank(deviceTypeIdentity))
            throw new BlueException(BAD_REQUEST);

        return SESSION_KEY_PRE + id + PAR_CONCATENATION + CREDENTIAL_TYPE_2_NATURE_CONVERTER.apply(credentialTypeIdentity).intern() + PAR_CONCATENATION + deviceTypeIdentity;
    }

    private static final Function<Long, String> ACCESS_UPDATE_SYNC_KEY_GEN = memberId -> {
        if (isValidIdentity(memberId))
            return ACCESS_UPDATE_PRE.prefix + memberId;

        throw new BlueException(BAD_REQUEST);
    };

    /**
     * delete all refresh token info by key id
     */
    private final Function<String, Mono<Boolean>> REFRESH_INFOS_BY_ID_DELETER = id ->
            refreshInfoService.deleteRefreshInfo(id)
                    .onErrorResume(throwable -> {
                        LOGGER.warn("id = {}, throwable = {}", id, throwable);
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

        return refreshInfoService.selectRefreshInfoByProbe(probe)
                .flatMap(refreshInfoService::deleteRefreshInfos)
                .onErrorResume(throwable -> {
                    LOGGER.warn("memberId = {}, throwable = {}", memberId, throwable);
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
        LOGGER.info("memberId = {}, credentialType = {}, deviceType = {}", memberId, credentialType, deviceType);
        if (isInvalidIdentity(memberId))
            return error(() -> new BlueException(BAD_REQUEST));

        assertCredentialType(credentialType, false);
        assertDeviceType(deviceType, false);

        return just(new MemberPayload(
                randomAlphanumeric(gammaLength),
                genSessionKeyId(memberId, credentialType, deviceType),
                valueOf(memberId),
                credentialType, deviceType,
                valueOf(TIME_STAMP_GETTER.get())));
    }

    /**
     * convert access
     *
     * @param memberPayload
     * @param roleIds
     * @param keyPair
     * @return
     */
    private static AccessInfo genAccessInfo(MemberPayload memberPayload, List<Long> roleIds, KeyPair keyPair) {
        if (isNull(memberPayload) || isEmpty(roleIds) || isNull(keyPair))
            throw new BlueException(BAD_REQUEST);

        return new AccessInfo(memberPayload.getGamma(), parseLong(memberPayload.getId()), roleIds, keyPair.getPubKey(),
                SECOND_STAMP_2_MILLIS_STAMP.apply(parseLong(memberPayload.getLoginTime())));
    }

    /**
     * generate access
     */
    private final BiFunction<MemberPayload, List<Long>, Mono<MemberAccess>> ACCESS_GENERATOR = (memberPayload, roleIds) -> {
        LOGGER.info("memberPayload = {}, roleIds = {}", memberPayload, roleIds);
        if (isNull(memberPayload) || isInvalidIdentities(roleIds))
            return error(() -> new BlueException(BAD_REQUEST));

        String jwt = jwtProcessor.create(memberPayload);
        KeyPair keyPair = initKeyPair();
        AccessInfo accessInfo = genAccessInfo(memberPayload, roleIds, keyPair);

        return accessInfoCache.setAccessInfo(memberPayload.getKeyId(), accessInfo)
                .flatMap(b -> {
                    LOGGER.info("accessInfo = {}, keyPair = {}", accessInfo, keyPair);
                    if (!b)
                        LOGGER.error("memberPayload = {}, roleIds = {}, keyPair = {}, accessInfo = {}", memberPayload, roleIds, keyPair, accessInfo);

                    return just(new MemberAccess(jwt, keyPair.getPriKey()));
                });
    };

    /**
     * generate auth(access and refresh)
     */
    private final BiFunction<MemberPayload, List<Long>, Mono<MemberAuth>> AUTH_GENERATOR = (memberPayload, roleIds) -> {
        LOGGER.info("memberPayload = {}, roleIds = {}", memberPayload, roleIds);

        return ACCESS_GENERATOR.apply(memberPayload, roleIds)
                .flatMap(access -> {
                    memberPayload.setGamma(randomAlphanumeric(gammaLength));

                    return refreshInfoService.insertRefreshInfo(new RefreshInfo(memberPayload.getKeyId(), memberPayload.getGamma(), memberPayload.getId(),
                                    memberPayload.getCredentialType().intern(), memberPayload.getDeviceType().intern(), memberPayload.getLoginTime(), REFRESH_EXPIRE_AT_GETTER.get()))
                            .onErrorResume(throwable -> {
                                LOGGER.warn("memberPayload = {}, roleIds = {}, throwable = {}",
                                        memberPayload, roleIds, throwable);
                                return just(new RefreshInfo());
                            })
                            .flatMap(ig -> just(jwtProcessor.create(memberPayload)))
                            .flatMap(refresh -> just(new MemberAuth(access.getAccess(), access.getSecKey(), refresh)));
                });
    };

    /**
     * assert auth
     */
    private final BiConsumer<MemberPayload, RefreshInfo> AUTH_ASSERTER = (memberPayload, refreshInfo) -> {
        try {
            if (memberPayload == null || refreshInfo == null)
                throw new BlueException(UNAUTHORIZED);

            if (parseLong(refreshInfo.getLoginTime()) + refreshExpiresMillis <= TIME_STAMP_GETTER.get()) {
                refreshInfoService.deleteRefreshInfo(refreshInfo.getId())
                        .doOnError(throwable -> LOGGER.error("refreshInfo = {}, throwable = {}", refreshInfo, throwable))
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
     * refresh access info role ids or pub key by key id
     *
     * @param keyId
     * @param roleIds
     * @param pubKey
     */
    private void refreshAccessRoleIdsOrPubKeyByKeyId(String keyId, List<Long> roleIds, String pubKey) {
        LOGGER.info("keyId = {}, roleIds = {}, pubKey = {}", keyId, roleIds, pubKey);
        if (isBlank(keyId) || (isInvalidIdentities(roleIds) && isBlank(pubKey)))
            return;

        String originalAuthInfoJson = ofNullable(stringRedisTemplate.opsForValue().get(keyId))
                .orElse(EMPTY_VALUE.value);

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

        if (isValidIdentities(roleIds))
            accessInfo.setRoleIds(roleIds);
        if (isNotBlank(pubKey))
            accessInfo.setPubKey(pubKey);

        try {
            Boolean refreshed = accessInfoCache.setAccessInfo(keyId, accessInfo)
                    .flatMap(b ->
                            b ? this.invalidateLocalAccessByKeyId(keyId) : just(false)
                    ).toFuture().join();

            LOGGER.info("refreshed = {}", refreshed);
        } catch (Exception e) {
            LOGGER.error("keyId = {}, e = {}", keyId, e);
        }
    }

    /**
     * handle task sync by member
     */
    private final BiConsumer<Long, HandleTask> ACCESS_SYNC_UPDATER = (memberId, handleTask) -> {
        if (isInvalidIdentity(memberId) || isNull(handleTask))
            return;

        synchronizedProcessor.handleTaskWithSync(ACCESS_UPDATE_SYNC_KEY_GEN.apply(memberId), handleTask);
    };

    /**
     * update all access roles with sync by member
     */
    private final BiConsumer<Long, List<Long>> ALL_ACCESS_ROLES_REFRESHER = (memberId, roleIds) -> {
        LOGGER.info("memberId = {}, roleIds = {}", memberId, roleIds);
        if (isInvalidIdentity(memberId) || isEmpty(roleIds))
            return;

        ACCESS_SYNC_UPDATER.accept(memberId, () -> {
            for (CredentialType credentialType : VALID_CREDENTIAL_TYPES)
                for (DeviceType deviceType : VALID_DEVICE_TYPES)
                    refreshAccessRoleIdsOrPubKeyByKeyId(
                            genSessionKeyId(memberId, credentialType.identity.intern(), deviceType.identity.intern()), roleIds, null);
        });
    };

    /**
     * update target access pub key with sync by member
     */
    private final BiConsumer<Access, String> ACCESS_PUB_KEY_REFRESHER = (access, pubKey) -> {
        LOGGER.info("access = {}, pubKey = {}", access, pubKey);
        if (isNull(access) || isBlank(pubKey))
            throw new BlueException(BAD_REQUEST);

        long memberId = access.getId();
        ACCESS_SYNC_UPDATER.accept(memberId, () ->
                refreshAccessRoleIdsOrPubKeyByKeyId(genSessionKeyId(memberId, access.getCredentialType().intern(), access.getDeviceType().intern()),
                        null, pubKey));
    };

    /**
     * auth info invalid
     */
    private final Function<Long, Boolean> INVALID_AUTH_BY_MEMBER_ID_TASK = memberId -> {
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return synchronizedProcessor.handleSupWithSync(ACCESS_UPDATE_SYNC_KEY_GEN.apply(memberId), () ->
                REFRESH_INFOS_BY_MEMBER_ID_DELETER.apply(memberId)
                        .flatMap(b -> {
                            if (!b)
                                LOGGER.error("result is false");

                            String keyId;
                            for (CredentialType credentialType : VALID_CREDENTIAL_TYPES)
                                for (DeviceType deviceType : VALID_DEVICE_TYPES) {
                                    keyId = genSessionKeyId(memberId, credentialType.identity, deviceType.identity);
                                    accessInfoCache.invalidAccessInfo(keyId).toFuture().join();
                                    this.invalidateLocalAccessByKeyId(keyId).toFuture().join();
                                }

                            return just(true);
                        }).onErrorResume(throwable -> {
                            LOGGER.error("throwable = {}", throwable);
                            return just(false);
                        }).toFuture().join()
        );
    };

    /**
     * get authority by role id
     */
    private final Function<List<Long>, Mono<List<AuthorityBaseOnRole>>> AUTHORITIES_MONO_BY_ROLE_ID_GETTER = roleIds -> {
        LOGGER.info("roleIds = {}", roleIds);
        return just(ROLES_INFO_BY_IDS_GETTER.apply(roleIds).stream()
                .map(roleInfo -> new AuthorityBaseOnRole(roleInfo, RESOURCES_INFO_BY_ROLE_IDS_GETTER.apply(roleInfo.getId())))
                .collect(toList()));
    };

    /**
     * to simple converter
     */
    private static final Function<List<AuthorityBaseOnRole>, Mono<MemberAuthority>> AUTHS_2_AUTH_CONVERTER = authorities -> {
        if (isEmpty(authorities))
            return error(() -> new BlueException(UNAUTHORIZED));

        List<RoleInfo> roleInfos = new ArrayList<>(authorities.size());
        Set<ResourceInfo> resources = new HashSet<>();

        for (AuthorityBaseOnRole authority : authorities) {
            roleInfos.add(authority.getRole());
            resources.addAll(authority.getResources());
        }

        return just(new MemberAuthority(roleInfos, resources));
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
                .collect(toMap(Resource::getId, identity(), (a, b) -> a));

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
                        REAL_URI_GETTER.apply(r).intern()), identity(), (a, b) -> a));

        List<Role> roles = roleListCf.join();
        Map<Long, RoleInfo> tempIdAndRoleInfoMapping = roles
                .parallelStream()
                .map(ROLE_2_ROLE_INFO_CONVERTER)
                .collect(toMap(RoleInfo::getId, identity(), (a, b) -> a));

        keyAndResourceMapping = tempKeyAndResourceMapping;
        roleAndResourcesKeyMapping = tempRoleAndResourcesKeyMapping;
        roleAndResourceInfosMapping = tempRoleAndResourceInfosMapping;
        idAndRoleInfoMapping = tempIdAndRoleInfoMapping;
    }

    /**
     * assert auth
     *
     * @param accessAssert
     * @return
     */
    @Override
    public Mono<AccessAsserted> assertAccess(AccessAssert accessAssert) {
        LOGGER.info("assertAuth = {}", accessAssert);
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

                    MemberPayload memberPayload = jwtProcessor.parse(aa.getAuthentication());
                    return accessInfoCache.getAccessInfo(memberPayload.getKeyId())
                            .switchIfEmpty(defer(() -> error(() -> new BlueException(UNAUTHORIZED))))
                            .flatMap(accessInfo -> {
                                if (!memberPayload.getGamma().equals(accessInfo.getGamma()))
                                    return error(() -> new BlueException(UNAUTHORIZED));

                                if (!AUTHORIZATION_RES_CHECKER.apply(accessInfo.getRoleIds(), resourceKey))
                                    return error(() -> new BlueException(FORBIDDEN.status, FORBIDDEN.code, FORBIDDEN.message));

                                boolean reqUnDecryption = resource.getRequestUnDecryption();
                                boolean resUnEncryption = resource.getResponseUnEncryption();

                                return just(new AccessAsserted(true, reqUnDecryption, resUnEncryption, resource.getExistenceRequestBody(), resource.getExistenceResponseBody(),
                                        reqUnDecryption && resUnEncryption ? EMPTY_VALUE.value : accessInfo.getPubKey(),
                                        new Access(accessInfo.getId(), accessInfo.getRoleIds(), memberPayload.getCredentialType().intern(),
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
    public Mono<MemberAuth> generateAuth(Long memberId, String credentialType, String deviceType) {
        LOGGER.info("memberId = {}, credentialType = {}, deviceType", memberId, credentialType, deviceType);
        return isValidIdentity(memberId) && isNotBlank(credentialType) && isNotBlank(deviceType) ?
                zip(genMemberPayloadMono(memberId, credentialType, deviceType),
                        memberRoleRelationService.selectRoleIdsByMemberId(memberId)
                ).flatMap(tuple2 ->
                        AUTH_GENERATOR.apply(tuple2.getT1(), tuple2.getT2()))
                :
                error(() -> new BlueException(BAD_REQUEST));
    }

    /**
     * generate member auth with auto register
     *
     * @param memberId
     * @param roleIds
     * @param credentialType
     * @param deviceType
     * @return
     */
    @Override
    public Mono<MemberAuth> generateAuth(Long memberId, List<Long> roleIds, String credentialType, String deviceType) {
        LOGGER.info("memberId = {}, roleIds = {}, credentialType = {}, deviceType", memberId, roleIds, credentialType, deviceType);
        return isValidIdentity(memberId) && isValidIdentities(roleIds) && isNotBlank(credentialType) && isNotBlank(deviceType) ?
                genMemberPayloadMono(memberId, credentialType, deviceType)
                        .flatMap(memberPayload ->
                                AUTH_GENERATOR.apply(memberPayload, roleIds))
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
    public Mono<MemberAccess> generateAccess(Long memberId, String credentialType, String deviceType) {
        LOGGER.info("memberId = {}, credentialType = {}, deviceType", memberId, credentialType, deviceType);
        return isValidIdentity(memberId) && isNotBlank(credentialType) && isNotBlank(deviceType) ?
                zip(genMemberPayloadMono(memberId, credentialType, deviceType),
                        memberRoleRelationService.selectRoleIdsByMemberId(memberId)
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
        LOGGER.info("memberPayload = {}", memberPayload);
        if (isNull(memberPayload))
            return error(() -> new BlueException(UNAUTHORIZED));

        return refreshInfoService.getRefreshInfo(memberPayload.getKeyId())
                .onErrorResume(t -> {
                    LOGGER.warn("memberPayload = {}, t = {}", memberPayload, t);
                    return error(() -> new BlueException(UNAUTHORIZED));
                })
                .switchIfEmpty(defer(() -> error(() -> new BlueException(UNAUTHORIZED))))
                .flatMap(refreshInfo -> {
                    AUTH_ASSERTER.accept(memberPayload, refreshInfo);
                    return this.generateAccess(parseLong(refreshInfo.getMemberId()), refreshInfo.getCredentialType(), refreshInfo.getDeviceType());
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
        LOGGER.info("refresh = {}", refresh);
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
        LOGGER.info("access = {}", access);
        if (isNull(access))
            return error(() -> new BlueException(UNAUTHORIZED));

        String keyId = genSessionKeyId(access.getId(), access.getCredentialType().intern(), access.getDeviceType().intern());
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
        LOGGER.info("jwt = {}", jwt);
        try {
            MemberPayload memberPayload = jwtProcessor.parse(jwt);
            String keyId = memberPayload.getKeyId();
            return zip(
                    REFRESH_INFOS_BY_ID_DELETER.apply(memberPayload.getKeyId()),
                    accessInfoCache.invalidAccessInfo(keyId).flatMap(b -> this.invalidateLocalAccessByKeyId(keyId))
            ).flatMap(tuple2 -> just(tuple2.getT1() && tuple2.getT2()))
                    .onErrorResume(t -> {
                        LOGGER.warn("t = {}", t);
                        return just(false);
                    });
        } catch (Exception e) {
            LOGGER.error("jwt = {}, e = {}", jwt, e);
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
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            return error(() -> new BlueException(INVALID_IDENTITY));

        return fromFuture(supplyAsync(() -> INVALID_AUTH_BY_MEMBER_ID_TASK.apply(memberId), executorService));
    }

    /**
     * invalid local auth by key id
     *
     * @param keyId
     */
    @Override
    public Mono<Boolean> invalidateLocalAccessByKeyId(String keyId) {
        LOGGER.info("keyId = {}", keyId);
        invalidLocalAccessProducer.send(new InvalidLocalAccessEvent(keyId));
        return just(true);
    }

    /**
     * update member role info by member id
     *
     * @param memberId
     * @param roleIds
     * @return
     */
    @Override
    public Mono<Boolean> refreshMemberRoleById(Long memberId, List<Long> roleIds) {
        LOGGER.info("memberId = {}, roleIds = {}", memberId, roleIds);
        if (isInvalidIdentity(memberId) || isInvalidIdentities(roleIds))
            throw new BlueException(INVALID_IDENTITY);

        return fromRunnable(() -> executorService.execute(() ->
                ALL_ACCESS_ROLES_REFRESHER.accept(memberId, roleIds)))
                .then(just(true));
    }

    /**
     * update member role info by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Boolean> refreshMemberRoleById(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return memberRoleRelationService.selectRoleIdsByMemberId(memberId)
                .map(ids -> {
                    ALL_ACCESS_ROLES_REFRESHER.accept(memberId, ids);
                    return true;
                });
    }

    /**
     * update member sec key by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<String> updateSecKeyByAccess(Access access) {
        LOGGER.info("access = {}", access);

        return isNotNull(access) ?
                just(initKeyPair())
                        .flatMap(keyPair -> {
                            ACCESS_PUB_KEY_REFRESHER.accept(access, keyPair.getPubKey());
                            return just(keyPair.getPriKey());
                        })
                :
                error(() -> new BlueException(UNAUTHORIZED));
    }

    /**
     * jwt -> payload
     *
     * @param authentication
     * @return
     */
    @Override
    public Mono<MemberPayload> parsePayload(String authentication) {
        LOGGER.info("authentication = {}", authentication);

        return justOrEmpty(authentication)
                .filter(BlueChecker::isNotBlank)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .map(a -> {
                    MemberPayload memberPayload = jwtProcessor.parse(a);
                    memberPayload.setGamma(PRIVACY_VALUE.value);
                    return memberPayload;
                })
                .onErrorMap(throwable -> {
                    LOGGER.info("throwable = {}", throwable);
                    return new BlueException(INVALID_PARAM);
                });
    }

    /**
     * jwt -> access
     *
     * @param authentication
     * @return
     */
    @Override
    public Mono<Access> parseAccess(String authentication) {
        LOGGER.info("authentication = {}", authentication);

        return justOrEmpty(authentication)
                .filter(BlueChecker::isNotBlank)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(a -> {
                    MemberPayload memberPayload = jwtProcessor.parse(a);

                    return accessInfoCache.getAccessInfo(memberPayload.getKeyId())
                            .switchIfEmpty(defer(() -> error(() -> new BlueException(INVALID_PARAM))))
                            .flatMap(accessInfo -> {
                                if (!memberPayload.getGamma().equals(accessInfo.getGamma()))
                                    return error(() -> new BlueException(UNAUTHORIZED));

                                return just(new Access(accessInfo.getId(), accessInfo.getRoleIds(), memberPayload.getCredentialType().intern(),
                                        memberPayload.getDeviceType().intern(), parseLong(memberPayload.getLoginTime())));
                            });
                }).onErrorMap(t -> {
                    LOGGER.info("t = {}", t);
                    return new BlueException(INVALID_PARAM);
                });
    }

    /**
     * jwt -> session
     *
     * @param authentication
     * @return
     */
    @Override
    public Mono<Session> parseSession(String authentication) {
        LOGGER.info("authentication = {}", authentication);

        return justOrEmpty(authentication)
                .filter(BlueChecker::isNotBlank)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(a -> {
                    MemberPayload memberPayload = jwtProcessor.parse(a);

                    return accessInfoCache.getAccessInfo(memberPayload.getKeyId())
                            .switchIfEmpty(defer(() -> error(() -> new BlueException(INVALID_PARAM))))
                            .flatMap(accessInfo -> {
                                if (!memberPayload.getGamma().equals(accessInfo.getGamma()))
                                    return error(() -> new BlueException(UNAUTHORIZED));

                                return just(new Session(accessInfo.getId(), accessInfo.getPubKey(), accessInfo.getRoleIds(), memberPayload.getCredentialType().intern(),
                                        memberPayload.getDeviceType().intern(), parseLong(memberPayload.getLoginTime())));
                            });
                }).onErrorMap(t -> {
                    LOGGER.info("t = {}", t);
                    return new BlueException(INVALID_PARAM);
                });
    }

    /**
     * encrypted -> data
     *
     * @param encryptedDataParam
     * @return
     */
    @Override
    public Mono<String> parseEncrypted(EncryptedDataParam encryptedDataParam) {
        LOGGER.info("encryptedDataParam = {}", encryptedDataParam);

        return justOrEmpty(encryptedDataParam)
                .filter(BlueChecker::isNotNull)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .map(ep -> {
                    ep.asserts();

                    return getRsaDecryptModeByIdentity(ep.getRsaDecryptMode())
                            .decipher.apply(ep.getEncrypted(), ep.getSecKey());
                }).onErrorMap(t -> {
                    LOGGER.info("t = {}", t);
                    return new BlueException(INVALID_PARAM);
                });
    }

    /**
     * get member's authority by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<List<AuthorityBaseOnRole>> selectAuthoritiesByAccess(Access access) {
        LOGGER.info("access = {}", access);
        return isNotNull(access) ?
                AUTHORITIES_MONO_BY_ROLE_ID_GETTER.apply(access.getRoleIds())
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
    public Mono<List<AuthorityBaseOnRole>> selectAuthoritiesByMemberId(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        return isValidIdentity(memberId) ?
                memberRoleRelationService.selectRoleIdsByMemberId(memberId)
                        .flatMap(AUTHORITIES_MONO_BY_ROLE_ID_GETTER)
                :
                error(() -> new BlueException(INVALID_IDENTITY));
    }

    /**
     * get member's authority by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<MemberAuthority> getAuthorityByAccess(Access access) {
        LOGGER.info("access = {}", access);
        return this.selectAuthoritiesByAccess(access).flatMap(AUTHS_2_AUTH_CONVERTER);
    }

    /**
     * get member's authority by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<MemberAuthority> getAuthorityByMemberId(Long memberId) {
        LOGGER.info("memberId = {}", memberId);
        return this.selectAuthoritiesByMemberId(memberId).flatMap(AUTHS_2_AUTH_CONVERTER);
    }

}