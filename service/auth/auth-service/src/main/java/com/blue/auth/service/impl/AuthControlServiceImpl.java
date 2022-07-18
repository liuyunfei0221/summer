package com.blue.auth.service.impl;

import com.blue.auth.api.model.*;
import com.blue.auth.common.AccessEncoder;
import com.blue.auth.config.deploy.ControlDeploy;
import com.blue.auth.event.producer.SystemAuthorityInfosRefreshProducer;
import com.blue.auth.model.*;
import com.blue.auth.remote.consumer.RpcMemberAuthServiceConsumer;
import com.blue.auth.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.auth.remote.consumer.RpcVerifyHandleServiceConsumer;
import com.blue.auth.repository.entity.Credential;
import com.blue.auth.repository.entity.MemberRoleRelation;
import com.blue.auth.repository.entity.Role;
import com.blue.auth.repository.entity.RoleResRelation;
import com.blue.auth.service.inter.*;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.auth.CredentialType;
import com.blue.basic.constant.auth.VerifyTypeAndCredentialTypesRelation;
import com.blue.basic.constant.verify.VerifyType;
import com.blue.basic.model.common.Access;
import com.blue.basic.model.exps.BlueException;
import com.blue.jwt.common.JwtProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redis.component.BlueLeakyBucketRateLimiter;
import com.blue.redisson.component.SynchronizedProcessor;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.assertCredentialType;
import static com.blue.basic.common.base.ConstantProcessor.getVerifyTypeByIdentity;
import static com.blue.basic.common.base.CommonFunctions.getIpReact;
import static com.blue.basic.constant.common.BlueCommonThreshold.BLUE_ID;
import static com.blue.basic.constant.common.RateLimitKeyPrefix.ACCESS_UPDATE_RATE_LIMIT_KEY_PRE;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.basic.constant.common.Status.INVALID;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.basic.constant.common.SummerAttr.EMPTY_EVENT;
import static com.blue.basic.constant.common.SyncKey.AUTHORITY_UPDATE_SYNC;
import static com.blue.basic.constant.common.SyncKey.DEFAULT_ROLE_UPDATE_SYNC;
import static com.blue.basic.constant.common.SyncKeyPrefix.MEMBER_ROLE_REL_UPDATE_PRE;
import static com.blue.basic.constant.common.SyncKeyPrefix.QUESTION_INSERT_PRE;
import static com.blue.basic.constant.verify.BusinessType.*;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * config role,resource,relation
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class AuthControlServiceImpl implements AuthControlService {

    private static final Logger LOGGER = getLogger(AuthControlServiceImpl.class);

    private final RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer;

    private final RpcMemberAuthServiceConsumer rpcMemberAuthServiceConsumer;

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final JwtProcessor<MemberPayload> jwtProcessor;

    private final LoginService loginService;

    private final AuthService authService;

    private RoleService roleService;

    private final ResourceService resourceService;

    private final RoleResRelationService roleResRelationService;

    private final CredentialService credentialService;

    private final CredentialHistoryService credentialHistoryService;

    private final SecurityQuestionService securityQuestionService;

    private final MemberRoleRelationService memberRoleRelationService;

    private final SystemAuthorityInfosRefreshProducer systemAuthorityInfosRefreshProducer;

    private final ExecutorService executorService;

    private final BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter;

    private final SynchronizedProcessor synchronizedProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AuthControlServiceImpl(RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer, RpcMemberAuthServiceConsumer rpcMemberAuthServiceConsumer,
                                  RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, JwtProcessor<MemberPayload> jwtProcessor,
                                  LoginService loginService, AuthService authService, RoleService roleService, ResourceService resourceService,
                                  RoleResRelationService roleResRelationService, CredentialService credentialService, CredentialHistoryService credentialHistoryService,
                                  SecurityQuestionService securityQuestionService, MemberRoleRelationService memberRoleRelationService,
                                  SystemAuthorityInfosRefreshProducer systemAuthorityInfosRefreshProducer, ExecutorService executorService,
                                  SynchronizedProcessor synchronizedProcessor, BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter, ControlDeploy controlDeploy) {
        this.rpcVerifyHandleServiceConsumer = rpcVerifyHandleServiceConsumer;
        this.rpcMemberAuthServiceConsumer = rpcMemberAuthServiceConsumer;
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.jwtProcessor = jwtProcessor;
        this.loginService = loginService;
        this.authService = authService;
        this.roleService = roleService;
        this.resourceService = resourceService;
        this.roleResRelationService = roleResRelationService;
        this.credentialService = credentialService;
        this.credentialHistoryService = credentialHistoryService;
        this.securityQuestionService = securityQuestionService;
        this.memberRoleRelationService = memberRoleRelationService;
        this.systemAuthorityInfosRefreshProducer = systemAuthorityInfosRefreshProducer;
        this.executorService = executorService;
        this.synchronizedProcessor = synchronizedProcessor;
        this.blueLeakyBucketRateLimiter = blueLeakyBucketRateLimiter;

        ALLOW = controlDeploy.getAllow();
        SEND_INTERVAL_MILLIS = controlDeploy.getIntervalMillis();
    }

    private final int ALLOW;
    private final long SEND_INTERVAL_MILLIS;

    private final BiFunction<Long, Long, MemberRoleRelation> MEMBER_ROLE_RELATION_GEN = (memberId, roleId) -> {
        if (isInvalidIdentity(memberId) || isInvalidIdentity(roleId))
            throw new BlueException(MEMBER_ALREADY_HAS_A_ROLE);

        long epochSecond = TIME_STAMP_GETTER.get();

        MemberRoleRelation memberRoleRelation = new MemberRoleRelation();
        memberRoleRelation.setMemberId(memberId);
        memberRoleRelation.setRoleId(roleId);
        memberRoleRelation.setCreateTime(epochSecond);
        memberRoleRelation.setUpdateTime(epochSecond);
        memberRoleRelation.setCreator(memberId);
        memberRoleRelation.setUpdater(memberId);

        return memberRoleRelation;
    };

    private final Function<Long, MemberRoleRelation> MEMBER_DEFAULT_ROLE_RELATION_GEN = memberId -> {
        if (isInvalidIdentity(memberId))
            throw new BlueException(MEMBER_ALREADY_HAS_A_ROLE);

        Role role = roleService.getDefaultRole();
        if (isNull(role))
            throw new BlueException(DATA_NOT_EXIST);

        long epochSecond = TIME_STAMP_GETTER.get();

        MemberRoleRelation memberRoleRelation = new MemberRoleRelation();
        memberRoleRelation.setMemberId(memberId);
        memberRoleRelation.setRoleId(role.getId());
        memberRoleRelation.setCreateTime(epochSecond);
        memberRoleRelation.setUpdateTime(epochSecond);
        memberRoleRelation.setCreator(memberId);
        memberRoleRelation.setUpdater(memberId);

        return memberRoleRelation;
    };

    /**
     * credential info, member id -> credential
     */
    public static final BiFunction<CredentialInfo, Long, Credential> CREDENTIAL_INFO_AND_MEMBER_ID_2_CREDENTIAL_CONVERTER = (credentialInfo, memberId) -> {
        if (isNull(credentialInfo) || isInvalidIdentity(memberId))
            throw new BlueException(EMPTY_PARAM);

        String type = credentialInfo.getType();
        assertCredentialType(type, false);

        Credential credential = new Credential();

        credential.setCredential(credentialInfo.getCredential());
        credential.setType(type);

        credential.setAccess(ofNullable(credentialInfo.getAccess())
                .filter(BlueChecker::isNotBlank)
                .map(AccessEncoder::encryptAccess)
                .orElse(EMPTY_DATA.value));

        credential.setMemberId(memberId);
        credential.setExtra(credentialInfo.getExtra());
        credential.setStatus(credentialInfo.getStatus());

        Long stamp = TIME_STAMP_GETTER.get();
        credential.setCreateTime(stamp);
        credential.setUpdateTime(stamp);

        return credential;
    };

    private Role getRoleByRoleId(Long roleId) {
        return roleService.getRole(roleId).orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
    }

    private List<Role> selectRoleByRoleIds(List<Long> roleIds) {
        return roleService.selectRoleByIds(roleIds);
    }

    private Role getMaxLevelRoleByRoleIds(List<Long> roleIds) {
        return selectRoleByRoleIds(roleIds).stream().max((a, b) -> Integer.compare(b.getLevel(), a.getLevel())).orElseThrow(() -> new BlueException(MEMBER_NOT_HAS_A_ROLE));
    }

    private List<Role> selectRoleByMemberId(Long memberId) {
        return roleService.selectRoleByIds(memberRoleRelationService.selectRoleIdsByMemberId(memberId));
    }

    private Role getMaxLevelRoleByMemberId(Long memberId) {
        return selectRoleByMemberId(memberId).stream().max((a, b) -> Integer.compare(b.getLevel(), a.getLevel())).orElseThrow(() -> new BlueException(MEMBER_NOT_HAS_A_ROLE));
    }

    private static final BiFunction<Integer, Integer, Boolean> ROLE_LEVEL_VALIDATOR = (tarLevel, operatorLevel) -> {
        if (isNull(tarLevel) || isNull(operatorLevel))
            throw new BlueException(BAD_REQUEST);

        return tarLevel > operatorLevel;
    };

    private static final BiConsumer<Integer, Integer> ROLE_LEVEL_ASSERTER = (tarLevel, operatorLevel) -> {
        if (isNull(tarLevel) || isNull(operatorLevel))
            throw new BlueException(BAD_REQUEST);

        if (tarLevel <= operatorLevel)
            throw new BlueException(FORBIDDEN);
    };

    private final BiFunction<Long, Long, Boolean> MEMBER_ROLE_LEVEL_VALIDATOR = (targetMemberId, operatorMemberId) -> {
        LOGGER.info("BiFunction<Long, Long, Boolean> MEMBER_ROLE_LEVEL_VALIDATOR, targetMemberId = {}, operatorMemberId = {}",
                targetMemberId, operatorMemberId);

        return ROLE_LEVEL_VALIDATOR.apply(getMaxLevelRoleByMemberId(targetMemberId).getLevel(),
                getMaxLevelRoleByMemberId(operatorMemberId).getLevel());
    };

    private final BiFunction<Long, Long, Boolean> TAR_ROLE_LEVEL_VALIDATOR = (targetRoleId, operatorMemberId) -> {
        LOGGER.info("BiFunction<Long, Long, Boolean> TAR_ROLE_LEVEL_VALIDATOR, targetRoleId = {}, operatorMemberId = {}",
                targetRoleId, operatorMemberId);

        return ROLE_LEVEL_VALIDATOR.apply(getRoleByRoleId(targetRoleId).getLevel(),
                getMaxLevelRoleByMemberId(operatorMemberId).getLevel());
    };

    private final BiConsumer<Long, Long> MEMBER_ROLE_LEVEL_ASSERTER = (targetMemberId, operatorMemberId) -> {
        if (!MEMBER_ROLE_LEVEL_VALIDATOR.apply(targetMemberId, operatorMemberId))
            throw new BlueException(FORBIDDEN);
    };

    private final BiConsumer<Long, Long> TAR_ROLE_LEVEL_ASSERTER = (targetRoleId, operatorMemberId) -> {
        if (!TAR_ROLE_LEVEL_VALIDATOR.apply(targetRoleId, operatorMemberId))
            throw new BlueException(FORBIDDEN);
    };

    private static final Map<VerifyType, List<String>> VT_WITH_CTS_REL = Stream.of(VerifyTypeAndCredentialTypesRelation.values())
            .collect(toMap(e -> e.verifyType, e -> e.credentialTypes.stream().map(lt -> lt.identity).collect(toList()), (a, b) -> a));

    private static final Function<VerifyType, List<String>> CTS_BY_VT_GETTER = verifyType -> {
        if (isNull(verifyType))
            throw new BlueException(BAD_REQUEST);

        List<String> credentialTypes = VT_WITH_CTS_REL.get(verifyType);
        if (isEmpty(credentialTypes))
            throw new BlueException(BAD_REQUEST);

        return credentialTypes;
    };

    private static final List<String> ALLOW_ACCESS_LTS = Stream.of(CredentialType.values())
            .filter(lt -> lt.allowAccess).map(lt -> lt.identity).collect(toList());

    private static final Set<String> ALLOW_ACCESS_LT_SET = new HashSet<>(ALLOW_ACCESS_LTS);

    private static final UnaryOperator<String> LIMIT_KEY_WRAPPER = key -> {
        if (isBlank(key))
            throw new RuntimeException("key can't be blank");

        return ACCESS_UPDATE_RATE_LIMIT_KEY_PRE.prefix + key;
    };

    private void packageExistAccess(List<Credential> credentials, Long memberId) {
        credentialService.selectCredentialByMemberIdAndTypes(memberId, ALLOW_ACCESS_LTS).stream().findAny()
                .ifPresent(ac ->
                        credentials.stream().filter(c -> ALLOW_ACCESS_LT_SET.contains(c.getType()))
                                .forEach(c -> {
                                    c.setAccess(ac.getAccess());
                                    c.setStatus(VALID.status);
                                }));
    }

    private List<Credential> generateCredentialsByElements(List<String> types, String credential, Long memberId) {
        Long stamp = TIME_STAMP_GETTER.get();
        return types.stream()
                .map(type -> {
                    Credential cre = new Credential();

                    cre.setCredential(credential);
                    cre.setType(type);
                    cre.setAccess(EMPTY_DATA.value);
                    cre.setMemberId(memberId);
                    cre.setExtra("from add credential");
                    cre.setStatus(ALLOW_ACCESS_LT_SET.contains(type) ? INVALID.status : VALID.status);
                    cre.setCreateTime(stamp);
                    cre.setUpdateTime(stamp);

                    return cre;
                }).collect(toList());
    }

    private static final Function<Long, String>
            MEMBER_ROLE_REL_UPDATE_KEY_WRAPPER = memberId -> MEMBER_ROLE_REL_UPDATE_PRE.prefix + memberId,
            QUESTION_UPDATE_KEY_WRAPPER = memberId -> QUESTION_INSERT_PRE.prefix + memberId;

    /**
     * login
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> insertSession(ServerRequest serverRequest) {
        return getIpReact(serverRequest)
                .flatMap(ip -> {
                    LOGGER.info("Mono<ServerResponse> login(ServerRequest serverRequest), ip = {}", ip);
                    return blueLeakyBucketRateLimiter.isAllowed(LIMIT_KEY_WRAPPER.apply(ip), ALLOW, SEND_INTERVAL_MILLIS);
                })
                .flatMap(allowed ->
                        allowed ?
                                loginService.insertSession(serverRequest)
                                :
                                error(() -> new BlueException(TOO_MANY_REQUESTS)));
    }

    /**
     * logout
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> deleteSession(ServerRequest serverRequest) {
        return loginService.deleteSession(serverRequest);
    }

    /**
     * logout everywhere
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> deleteSessions(ServerRequest serverRequest) {
        return loginService.deleteSessions(serverRequest);
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
        return just(jwtProcessor.parse(refresh))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(UNAUTHORIZED))))
                .flatMap(memberPayload ->
                        blueLeakyBucketRateLimiter.isAllowed(LIMIT_KEY_WRAPPER.apply(memberPayload.getKeyId()), ALLOW, SEND_INTERVAL_MILLIS)
                                .flatMap(allowed ->
                                        allowed ?
                                                authService.refreshAccessByMemberPayload(memberPayload)
                                                :
                                                error(() -> new BlueException(TOO_MANY_REQUESTS))));
    }

    /**
     * operator's role level must higher than target member role level
     *
     * @param targetMemberId
     * @param operatorMemberId
     * @return
     */
    @Override
    public Boolean validateRoleLevelForOperate(Long targetMemberId, Long operatorMemberId) {
        return MEMBER_ROLE_LEVEL_VALIDATOR.apply(targetMemberId, operatorMemberId);
    }

    /**
     * invalid auth by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<Boolean> invalidateAuthByAccess(Access access) {
        return authService.invalidateAuthByAccess(access);
    }

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    @Override
    public Mono<Boolean> invalidateAuthByJwt(String jwt) {
        return authService.invalidateAuthByJwt(jwt);
    }

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<Boolean> invalidateAuthByMemberId(Long memberId) {
        return authService.invalidateAuthByMemberId(memberId);
    }

    /**
     * invalid member auth by member id
     *
     * @param memberId
     * @param operatorId
     * @return
     */
    @Override
    public Mono<Boolean> invalidateAuthByMemberId(Long memberId, Long operatorId) {
        LOGGER.info("Mono<Boolean> invalidateAuthByMember(Long memberId, Long operatorId), memberId = {}, operatorId = {}", memberId, operatorId);
        if (isInvalidIdentity(memberId) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        MEMBER_ROLE_LEVEL_ASSERTER.accept(memberId, operatorId);

        return authService.invalidateAuthByMemberId(memberId);
    }

    /**
     * invalid local auth by key id
     *
     * @param keyId
     * @return
     */
    @Override
    public Mono<Boolean> invalidateLocalAccessByKeyId(String keyId) {
        return authService.invalidateLocalAccessByKeyId(keyId);
    }

    /**
     * refresh resource key/info or role-resource-relation
     *
     * @return
     */
    @Override
    public Mono<Void> refreshSystemAuthorityInfos() {
        return fromRunnable(authService::refreshSystemAuthorityInfos).then();
    }

    /**
     * init auth info for a new member
     *
     * @param memberCredentialInfo
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo) {
        LOGGER.info("void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo), memberCredentialInfo = {}", memberCredentialInfo);
        if (isNull(memberCredentialInfo))
            throw new BlueException(EMPTY_PARAM);
        memberCredentialInfo.asserts();

        Long memberId = memberCredentialInfo.getMemberId();
        List<Credential> credentials = memberCredentialInfo.getCredentials().stream()
                .map(c -> CREDENTIAL_INFO_AND_MEMBER_ID_2_CREDENTIAL_CONVERTER.apply(c, memberId))
                .collect(toList());

        packageExistAccess(credentials, memberId);

        synchronizedProcessor.handleTaskWithLock(MEMBER_ROLE_REL_UPDATE_KEY_WRAPPER.apply(memberId), () ->
                memberRoleRelationService.insertMemberRoleRelation(MEMBER_DEFAULT_ROLE_RELATION_GEN.apply(memberId)));
        credentialService.insertCredentials(credentials);
    }

    /**
     * init auth info for a new member
     *
     * @param memberCredentialInfo
     * @param roleId
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo, Long roleId) {
        LOGGER.info("void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo, Long roleId), memberCredentialInfo = {}, roleId = {}", memberCredentialInfo, roleId);
        if (isNull(memberCredentialInfo) || isInvalidIdentity(roleId))
            throw new BlueException(EMPTY_PARAM);
        memberCredentialInfo.asserts();
        List<CredentialInfo> credentialInfos = memberCredentialInfo.getCredentials();

        Long memberId = memberCredentialInfo.getMemberId();
        List<Credential> credentials = credentialInfos.stream()
                .map(c -> CREDENTIAL_INFO_AND_MEMBER_ID_2_CREDENTIAL_CONVERTER.apply(c, memberId))
                .collect(toList());

        packageExistAccess(credentials, memberId);

        memberRoleRelationService.insertMemberRoleRelation(MEMBER_ROLE_RELATION_GEN.apply(memberId, roleId));
        credentialService.insertCredentials(credentials);

        credentialHistoryService.insertCredentialHistoriesByCredentialsAndMemberIdAsync(credentials, memberId);
    }

    /**
     * add new credential base on verify type for a member
     *
     * @param credentialSettingUpParam
     * @param access
     * @return
     */
    @Override
    @GlobalTransactional(propagation = io.seata.tm.api.transaction.Propagation.REQUIRED,
            rollbackFor = Exception.class, lockRetryInternal = 1, lockRetryTimes = 1, timeoutMills = 30000)
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 30)
    public MemberBasicInfo credentialSettingUp(CredentialSettingUpParam credentialSettingUpParam, Access access) {
        LOGGER.info("MemberBasicInfo credentialSettingUp(CredentialSettingUpParam credentialSettingUpParam, Access access), credentialSettingUpParam = {}, access = {}",
                credentialSettingUpParam, access);
        if (isNull(credentialSettingUpParam))
            throw new BlueException(EMPTY_PARAM);
        if (isNull(access))
            throw new BlueException(UNAUTHORIZED);
        credentialSettingUpParam.asserts();

        long memberId = access.getId();
        if (!blueLeakyBucketRateLimiter.isAllowed(LIMIT_KEY_WRAPPER.apply(String.valueOf(memberId)), ALLOW, SEND_INTERVAL_MILLIS).toFuture().join())
            throw new BlueException(TOO_MANY_REQUESTS);

        String credential = credentialSettingUpParam.getCredential();

        if (!rpcVerifyHandleServiceConsumer.validate(getVerifyTypeByIdentity(credentialSettingUpParam.getVerifyType()),
                CREDENTIAL_SETTING_UP, credential, credentialSettingUpParam.getVerificationCode(), true).toFuture().join())
            throw new BlueException(VERIFY_IS_INVALID);

        List<String> types = CTS_BY_VT_GETTER.apply(getVerifyTypeByIdentity(credentialSettingUpParam.getVerifyType()));

        List<Credential> sameTypeCredentials = credentialService.selectCredentialByMemberIdAndTypes(memberId, types);
        if (isNotEmpty(sameTypeCredentials))
            throw new BlueException(DATA_ALREADY_EXIST);

        credentialService.insertCredentials(generateCredentialsByElements(types, credential, memberId));
        MemberBasicInfo memberBasicInfo = rpcMemberAuthServiceConsumer.updateMemberCredentialAttr(types, credential, memberId);

        credentialHistoryService.insertCredentialHistoryByCredentialAndMemberIdAsync(credential, memberId);

        return memberBasicInfo;
    }

    /**
     * update exist credential
     *
     * @param credentialModifyParam
     * @param access
     * @return
     */
    @Override
    @GlobalTransactional(propagation = io.seata.tm.api.transaction.Propagation.REQUIRED,
            rollbackFor = Exception.class, lockRetryInternal = 1, lockRetryTimes = 1, timeoutMills = 30000)
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 30)
    public MemberBasicInfo credentialModify(CredentialModifyParam credentialModifyParam, Access access) {
        LOGGER.info("MemberBasicInfo credentialModify(CredentialModifyParam credentialModifyParam, Access access), credentialModifyParam = {}, access = {}",
                credentialModifyParam, access);
        if (isNull(credentialModifyParam))
            throw new BlueException(EMPTY_PARAM);
        if (isNull(access))
            throw new BlueException(UNAUTHORIZED);
        credentialModifyParam.asserts();

        long memberId = access.getId();
        if (!blueLeakyBucketRateLimiter.isAllowed(LIMIT_KEY_WRAPPER.apply(String.valueOf(memberId)), ALLOW, SEND_INTERVAL_MILLIS).toFuture().join())
            throw new BlueException(TOO_MANY_REQUESTS);

        VerifyType currentVerifyType = getVerifyTypeByIdentity(credentialModifyParam.getCurrentVerifyType());
        Optional<Credential> currentCredentialOpt = credentialService.selectCredentialByMemberIdAndTypes(memberId, CTS_BY_VT_GETTER.apply(currentVerifyType)).stream().findAny();
        if (currentCredentialOpt.isEmpty())
            throw new BlueException(BAD_REQUEST);

        if (!rpcVerifyHandleServiceConsumer.validate(currentVerifyType, CREDENTIAL_UPDATE, currentCredentialOpt.get().getCredential(),
                credentialModifyParam.getCurrentVerificationCode(), true).toFuture().join())
            throw new BlueException(VERIFY_IS_INVALID);


        VerifyType destinationVerifyType = getVerifyTypeByIdentity(credentialModifyParam.getDestinationVerifyType());
        String destinationCredential = credentialModifyParam.getDestinationCredential();
        if (!rpcVerifyHandleServiceConsumer.validate(destinationVerifyType, CREDENTIAL_UPDATE, destinationCredential,
                credentialModifyParam.getDestinationVerificationCode(), true).toFuture().join())
            throw new BlueException(VERIFY_IS_INVALID);

        List<String> destinationCredentialTypes = CTS_BY_VT_GETTER.apply(destinationVerifyType);
        List<Credential> destinationCredentials = credentialService.selectCredentialByMemberIdAndTypes(memberId, destinationCredentialTypes);
        if (isEmpty(destinationCredentials))
            throw new BlueException(DATA_NOT_EXIST);

        credentialService.updateCredentialByIds(destinationCredential, destinationCredentials.stream().map(Credential::getId).collect(toList()));
        authService.invalidateAuthByMemberId(memberId).doOnError(throwable -> LOGGER.info("authService.invalidateAuthByMemberId(memberId) failed, memberId = {}, throwable = {}", memberId, throwable)).subscribe();
        MemberBasicInfo memberBasicInfo = rpcMemberAuthServiceConsumer.updateMemberCredentialAttr(destinationCredentialTypes, destinationCredential, memberId);

        credentialHistoryService.insertCredentialHistoryByCredentialAndMemberIdAsync(destinationCredential, memberId);

        return memberBasicInfo;
    }

    /**
     * update member role info by member id
     *
     * @param memberId
     * @param roleIds
     * @return
     */
    @Override
    public Mono<Boolean> refreshMemberRoleByIds(Long memberId, List<Long> roleIds) {
        return authService.refreshMemberRoleById(memberId, roleIds);
    }

    /**
     * update default role by role id
     *
     * @param id
     * @param operatorId
     * @return
     */
    @Override
    public Mono<RoleManagerInfo> updateDefaultRole(Long id, Long operatorId) {
        LOGGER.info("Mono<RoleManagerInfo> updateDefaultRole(Long id, Long operatorId), id = {}, operatorId = {}", id, operatorId);
        if (isInvalidIdentity(id) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        Role targetRole = this.getRoleByRoleId(id);
        if (isNull(targetRole))
            throw new BlueException(DATA_NOT_EXIST);

        ROLE_LEVEL_ASSERTER.accept(targetRole.getLevel(), this.getMaxLevelRoleByMemberId(operatorId).getLevel());

        return just(synchronizedProcessor.handleSupWithLock(DEFAULT_ROLE_UPDATE_SYNC.key, () ->
                roleService.updateDefaultRole(id, operatorId)));
    }

    /**
     * update member access/password by access
     *
     * @param accessUpdateParam
     * @param access
     * @return
     */
    @Override
    public Mono<Boolean> updateAccessByAccess(AccessUpdateParam accessUpdateParam, Access access) {
        LOGGER.info("Mono<Boolean> updateAccessByAccess(AccessUpdateParam accessUpdateParam, Access access), accessUpdateParam = {}, access = {}", accessUpdateParam, access);
        long memberId = access.getId();
        if (isInvalidIdentity(memberId))
            return error(() -> new BlueException(UNAUTHORIZED));
        if (isNull(accessUpdateParam))
            return error(() -> new BlueException(EMPTY_PARAM));
        accessUpdateParam.asserts();

        VerifyType verifyType = getVerifyTypeByIdentity(accessUpdateParam.getVerifyType());
        return blueLeakyBucketRateLimiter.isAllowed(LIMIT_KEY_WRAPPER.apply(String.valueOf(memberId)), ALLOW, SEND_INTERVAL_MILLIS)
                .flatMap(allowed ->
                        allowed ?
                                credentialService.selectCredentialMonoByMemberIdAndTypes(memberId, CTS_BY_VT_GETTER.apply(verifyType))
                                        .flatMap(credentials ->
                                                just(credentials.stream().findAny().orElseThrow(() -> new BlueException(BAD_REQUEST))))
                                        .flatMap(credential ->
                                                rpcVerifyHandleServiceConsumer.validate(verifyType, UPDATE_ACCESS, credential.getCredential(), accessUpdateParam.getVerificationCode(), true)
                                                        .flatMap(validate ->
                                                                validate ?
                                                                        just(credentialService.updateAccess(memberId, ALLOW_ACCESS_LTS, accessUpdateParam.getAccess()))
                                                                                .flatMap(b -> b ? authService.invalidateAuthByMemberId(memberId) : just(false))
                                                                        :
                                                                        error(() -> new BlueException(VERIFY_IS_INVALID))))
                                :
                                error(() -> new BlueException(TOO_MANY_REQUESTS)));
    }

    /**
     * reset member access/password by access
     *
     * @param accessResetParam
     * @return
     */
    @Override
    public Mono<Boolean> resetAccessByAccess(AccessResetParam accessResetParam) {
        LOGGER.info("Mono<Boolean> resetAccessByAccess(AccessResetParam accessResetParam), accessResetParam = {}", accessResetParam);
        if (isNull(accessResetParam))
            return error(() -> new BlueException(EMPTY_PARAM));
        accessResetParam.asserts();

        String credential = accessResetParam.getCredential();
        VerifyType verifyType = getVerifyTypeByIdentity(accessResetParam.getVerifyType());
        return blueLeakyBucketRateLimiter.isAllowed(LIMIT_KEY_WRAPPER.apply(credential), ALLOW, SEND_INTERVAL_MILLIS)
                .flatMap(allowed ->
                        allowed ?
                                credentialService.selectCredentialMonoByCredentialAndTypes(credential, CTS_BY_VT_GETTER.apply(verifyType))
                                        .flatMap(credentials ->
                                                just(credentials.stream().findAny().orElseThrow(() -> new BlueException(BAD_REQUEST))))
                                        .flatMap(cre ->
                                                rpcVerifyHandleServiceConsumer.validate(verifyType, RESET_ACCESS, cre.getCredential(), accessResetParam.getVerificationCode(), true)
                                                        .flatMap(validate ->
                                                                validate ?
                                                                        just(credentialService.updateAccess(cre.getMemberId(), ALLOW_ACCESS_LTS, accessResetParam.getAccess()))
                                                                                .flatMap(b -> b ? authService.invalidateAuthByMemberId(cre.getMemberId()) : just(false))
                                                                        :
                                                                        error(() -> new BlueException(VERIFY_IS_INVALID))))
                                :
                                error(() -> new BlueException(TOO_MANY_REQUESTS)));
    }

    /**
     * refresh member sec key by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<String> refreshSecKeyByAccess(Access access) {
        LOGGER.info("Mono<String> refreshSecKeyByAccess(Access access), access = {}", access);
        return isNotNull(access) ?
                blueLeakyBucketRateLimiter.isAllowed(LIMIT_KEY_WRAPPER.apply(String.valueOf(access)), ALLOW, SEND_INTERVAL_MILLIS)
                        .flatMap(allowed ->
                                allowed ?
                                        authService.updateSecKeyByAccess(access)
                                        :
                                        error(() -> new BlueException(TOO_MANY_REQUESTS)))
                :
                error(() -> new BlueException(UNAUTHORIZED));
    }

    /**
     * insert security question
     *
     * @param securityQuestionInsertParam
     * @param memberId
     * @return
     */
    @Override
    public Mono<Boolean> insertSecurityQuestion(SecurityQuestionInsertParam securityQuestionInsertParam, Long memberId) {
        return just(synchronizedProcessor.handleSupWithLock(QUESTION_UPDATE_KEY_WRAPPER.apply(memberId), () ->
                securityQuestionService.insertSecurityQuestion(securityQuestionInsertParam, memberId)) > 0);
    }

    /**
     * insert security question batch
     *
     * @param securityQuestionInsertParams
     * @param memberId
     * @return
     */
    @Override
    public Mono<Boolean> insertSecurityQuestions(List<SecurityQuestionInsertParam> securityQuestionInsertParams, Long memberId) {
        return just(synchronizedProcessor.handleSupWithLock(QUESTION_UPDATE_KEY_WRAPPER.apply(memberId), () ->
                securityQuestionService.insertSecurityQuestions(securityQuestionInsertParams, memberId)) > 0);
    }

    /**
     * get member's authority by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<List<AuthorityBaseOnRole>> selectAuthoritiesMonoByAccess(Access access) {
        return authService.getAuthoritiesMonoByAccess(access);
    }

    /**
     * get member's authority by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<AuthorityBaseOnRole>> selectAuthoritiesMonoByMemberId(Long memberId) {
        return authService.getAuthoritiesMonoByMemberId(memberId);
    }

    /**
     * insert a new role
     *
     * @param roleInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<RoleInfo> insertRole(RoleInsertParam roleInsertParam, Long operatorId) {
        LOGGER.info("Mono<RoleInfo> insertRole(RoleInsertParam roleInsertParam, Long operatorId), roleInsertParam = {}, operatorId = {}", roleInsertParam, operatorId);
        roleInsertParam.asserts();

        return just(synchronizedProcessor.handleSupWithLock(AUTHORITY_UPDATE_SYNC.key, () -> {
            ROLE_LEVEL_ASSERTER.accept(roleInsertParam.getLevel(),
                    getMaxLevelRoleByMemberId(operatorId).getLevel());
            return roleService.insertRole(roleInsertParam, operatorId);
        })).doOnSuccess(ri -> {
            LOGGER.info("ri = {}", ri);
            systemAuthorityInfosRefreshProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * update a exist role
     *
     * @param roleUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<RoleInfo> updateRole(RoleUpdateParam roleUpdateParam, Long operatorId) {
        LOGGER.info("Mono<RoleInfo> updateRole(RoleUpdateParam roleUpdateParam, Long operatorId), roleUpdateParam = {}, operatorId = {}", roleUpdateParam, operatorId);
        roleUpdateParam.asserts();

        return just(synchronizedProcessor.handleSupWithLock(AUTHORITY_UPDATE_SYNC.key, () -> {
            ROLE_LEVEL_ASSERTER.accept(roleUpdateParam.getLevel(),
                    getMaxLevelRoleByMemberId(operatorId).getLevel());
            return roleService.updateRole(roleUpdateParam, operatorId);
        })).doOnSuccess(ri -> {
            LOGGER.info("ri = {}", ri);
            systemAuthorityInfosRefreshProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * delete a exist role
     *
     * @param id
     * @param operatorId
     * @return
     */
    @Override
    public Mono<RoleInfo> deleteRole(Long id, Long operatorId) {
        LOGGER.info("Mono<RoleInfo> deleteRole(Long id, Long operatorId), id = {}, operatorId = {}", id, operatorId);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        return just(synchronizedProcessor.handleSupWithLock(AUTHORITY_UPDATE_SYNC.key, () -> {
            TAR_ROLE_LEVEL_ASSERTER.accept(id, operatorId);
            roleResRelationService.deleteRelationByRoleId(id);
            return roleService.deleteRole(id);
        })).doOnSuccess(ri -> {
            LOGGER.info("ri = {}", ri);
            systemAuthorityInfosRefreshProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * insert resource
     *
     * @param resourceInsertParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<ResourceInfo> insertResource(ResourceInsertParam resourceInsertParam, Long operatorId) {
        LOGGER.info("Mono<ResourceInfo> insertResource(ResourceInsertParam resourceInsertParam, Long operatorId), resourceInsertParam = {}, operatorId = {}", resourceInsertParam, operatorId);

        return just(synchronizedProcessor.handleSupWithLock(AUTHORITY_UPDATE_SYNC.key,
                () -> resourceService.insertResource(resourceInsertParam, operatorId)))
                .doOnSuccess(ri -> {
                    LOGGER.info("ri = {}", ri);
                    systemAuthorityInfosRefreshProducer.send(EMPTY_EVENT);
                });
    }

    /**
     * update a exist resource
     *
     * @param resourceUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<ResourceInfo> updateResource(ResourceUpdateParam resourceUpdateParam, Long operatorId) {
        LOGGER.info("Mono<ResourceInfo> updateResource(ResourceUpdateParam resourceUpdateParam, Long operatorId), resourceUpdateParam = {}, operatorId = {}", resourceUpdateParam, operatorId);
        if (isNull(resourceUpdateParam))
            throw new BlueException(EMPTY_PARAM);

        Long resId = resourceUpdateParam.getId();
        if (isInvalidIdentity(resId))
            throw new BlueException(INVALID_IDENTITY);

        return just(synchronizedProcessor.handleSupWithLock(AUTHORITY_UPDATE_SYNC.key, () -> {
            ofNullable(roleResRelationService.selectRelationByResId(resId))
                    .filter(BlueChecker::isNotEmpty)
                    .map(rs -> rs.stream().map(RoleResRelation::getRoleId).collect(toList()))
                    .filter(BlueChecker::isNotEmpty)
                    .map(roleService::selectRoleByIds)
                    .filter(BlueChecker::isNotEmpty)
                    .map(relRoles -> {
                        Role operatorRole = getMaxLevelRoleByMemberId(operatorId);
                        Integer operatorLevel = operatorRole.getLevel();

                        return relRoles.stream().filter(role ->
                                !ROLE_LEVEL_VALIDATOR.apply(role.getLevel(), operatorLevel)
                                        && !operatorRole.getId().equals(role.getId())).collect(toList());
                    })
                    .filter(BlueChecker::isNotEmpty)
                    .ifPresent(higherLevelRoles -> {
                        if (isNotEmpty(higherLevelRoles))
                            throw new BlueException(RESOURCE_STILL_USED, new String[]{
                                    higherLevelRoles.stream().map(Role::getName).reduce((a, b) -> a + "," + b).orElse(EMPTY_DATA.value)});
                    });

            return resourceService.updateResource(resourceUpdateParam, operatorId);
        })).doOnSuccess(ri -> {
            LOGGER.info("ri = {}", ri);
            systemAuthorityInfosRefreshProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * delete a exist resource
     *
     * @param id
     * @param operatorId
     * @return
     */
    @Override
    public Mono<ResourceInfo> deleteResource(Long id, Long operatorId) {
        LOGGER.info("Mono<ResourceInfo> deleteResource(Long id, Long operatorId), id = {}, operatorId = {}", id, operatorId);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);
        if (isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        return just(synchronizedProcessor.handleSupWithLock(AUTHORITY_UPDATE_SYNC.key, () -> {
            List<RoleResRelation> relations = roleResRelationService.selectRelationByResId(id);
            if (isNotEmpty(relations))
                throw new BlueException(RESOURCE_STILL_USED, new String[]{
                        roleService.selectRoleByIds(relations.stream().map(RoleResRelation::getRoleId).collect(toList()))
                                .stream().map(Role::getName).reduce((a, b) -> a + "," + b).orElse(EMPTY_DATA.value)});

            return resourceService.deleteResource(id);
        })).doOnSuccess(ri -> {
            LOGGER.info("ri = {}", ri);
            systemAuthorityInfosRefreshProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * update authority base on role / generate role-resource-relations
     *
     * @param roleResRelationParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnRole> updateAuthorityByRole(RoleResRelationParam roleResRelationParam, Long operatorId) {
        LOGGER.info("Mono<AuthorityBaseOnRole> updateAuthorityBaseOnRole(RoleResRelationParam roleResRelationParam, Long operatorId), roleResRelationParam = {}, operatorId = {}", roleResRelationParam, operatorId);
        if (isNull(roleResRelationParam))
            throw new BlueException(EMPTY_PARAM);
        roleResRelationParam.asserts();

        return just(synchronizedProcessor.handleSupWithLock(AUTHORITY_UPDATE_SYNC.key, () -> {
            TAR_ROLE_LEVEL_ASSERTER.accept(roleResRelationParam.getRoleId(), operatorId);
            return roleResRelationService.updateAuthorityByRole(roleResRelationParam.getRoleId(), roleResRelationParam.getResIds(), operatorId);
        })).doOnSuccess(auth -> {
            LOGGER.info("auth = {}", auth);
            systemAuthorityInfosRefreshProducer.send(EMPTY_EVENT);
        });
    }

    /**
     * add authority base on member / insert member-role-relations
     *
     * @param memberRoleRelationInsertOrDeleteParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnRole> insertAuthorityByMember(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam, Long operatorId) {
        LOGGER.info("Mono<AuthorityBaseOnRole> insertAuthoritiesByMember(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam, Long operatorId), memberRoleRelationInsertOrDeleteParam = {}, operatorId = {}",
                memberRoleRelationInsertOrDeleteParam, operatorId);
        if (isNull(memberRoleRelationInsertOrDeleteParam) || isInvalidIdentity(operatorId))
            throw new BlueException(EMPTY_PARAM);
        memberRoleRelationInsertOrDeleteParam.asserts();

        Long memberId = memberRoleRelationInsertOrDeleteParam.getMemberId();
        Long roleId = memberRoleRelationInsertOrDeleteParam.getRoleId();

        return fromFuture(supplyAsync(() ->
                synchronizedProcessor.handleSupWithLock(MEMBER_ROLE_REL_UPDATE_KEY_WRAPPER.apply(memberId), () -> {
                    Integer operatorLevel = getMaxLevelRoleByMemberId(operatorId).getLevel();

                    ROLE_LEVEL_ASSERTER.accept(getMaxLevelRoleByMemberId(memberId).getLevel(), operatorLevel);
                    ROLE_LEVEL_ASSERTER.accept(getRoleByRoleId(roleId).getLevel(), operatorLevel);

                    return memberRoleRelationService.insertMemberRoleRelation(memberId, roleId, operatorId);
                }), executorService))
                .flatMap(ig -> authService.refreshMemberRoleById(memberId, memberRoleRelationService.selectRoleIdsByMemberId(memberId)))
                .flatMap(ig -> roleResRelationService.getAuthorityMonoByRoleId(roleId));
    }

    /**
     * update authority base on member / update member-role-relations
     *
     * @param memberRoleRelationUpdateParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<List<AuthorityBaseOnRole>> updateAuthoritiesByMember(MemberRoleRelationUpdateParam memberRoleRelationUpdateParam, Long operatorId) {
        LOGGER.info("Mono<List<AuthorityBaseOnRole>> updateAuthoritiesByMember(MemberRoleRelationUpdateParam memberRoleRelationUpdateParam, Long operatorId), memberRoleRelationUpdateParam = {}, operatorId = {}",
                memberRoleRelationUpdateParam, operatorId);
        if (isNull(memberRoleRelationUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        memberRoleRelationUpdateParam.asserts();

        Long memberId = memberRoleRelationUpdateParam.getMemberId();
        List<Long> roleIds = memberRoleRelationUpdateParam.getRoleIds();

        return fromFuture(supplyAsync(() ->
                synchronizedProcessor.handleSupWithLock(MEMBER_ROLE_REL_UPDATE_KEY_WRAPPER.apply(memberId), () -> {
                    Integer operatorLevel = getMaxLevelRoleByMemberId(operatorId).getLevel();

                    ROLE_LEVEL_ASSERTER.accept(getMaxLevelRoleByMemberId(memberId).getLevel(), operatorLevel);
                    ROLE_LEVEL_ASSERTER.accept(getMaxLevelRoleByRoleIds(roleIds).getLevel(), operatorLevel);

                    return memberRoleRelationService.updateMemberRoleRelations(memberId, roleIds, operatorId);
                }), executorService))
                .flatMap(ig -> authService.refreshMemberRoleById(memberId, roleIds))
                .flatMap(ig -> roleResRelationService.selectAuthoritiesMonoByRoleIds(roleIds));
    }

    /**
     * delete authority base on member / delete member-role-relations
     *
     * @param memberRoleRelationInsertOrDeleteParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnRole> deleteAuthorityByMember(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam, Long operatorId) {
        LOGGER.info("Mono<AuthorityBaseOnRole> deleteAuthoritiesByMember(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam, Long operatorId), memberRoleRelationInsertOrDeleteParam = {}, operatorId = {}",
                memberRoleRelationInsertOrDeleteParam, operatorId);
        if (isNull(memberRoleRelationInsertOrDeleteParam) || isInvalidIdentity(operatorId))
            throw new BlueException(EMPTY_PARAM);
        memberRoleRelationInsertOrDeleteParam.asserts();

        Long memberId = memberRoleRelationInsertOrDeleteParam.getMemberId();
        Long roleId = memberRoleRelationInsertOrDeleteParam.getRoleId();

        return fromFuture(supplyAsync(() ->
                synchronizedProcessor.handleSupWithLock(MEMBER_ROLE_REL_UPDATE_KEY_WRAPPER.apply(memberId), () -> {
                    Integer operatorLevel = getMaxLevelRoleByMemberId(operatorId).getLevel();

                    ROLE_LEVEL_ASSERTER.accept(getMaxLevelRoleByMemberId(memberId).getLevel(), operatorLevel);
                    ROLE_LEVEL_ASSERTER.accept(getRoleByRoleId(roleId).getLevel(), operatorLevel);

                    return memberRoleRelationService.deleteMemberRoleRelation(memberId, roleId, operatorId);
                }), executorService))
                .flatMap(ig -> authService.refreshMemberRoleById(memberId, memberRoleRelationService.selectRoleIdsByMemberId(memberId)))
                .flatMap(ig -> roleResRelationService.getAuthorityMonoByRoleId(roleId));
    }

    /**
     * update authority base on role / generate role-resource-relations sync with trans / not support for manager
     *
     * @param roleResRelationParam
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public AuthorityBaseOnRole updateAuthorityByRoleSync(RoleResRelationParam roleResRelationParam) {
        LOGGER.info("AuthorityBaseOnRole updateAuthorityByRoleSync(RoleResRelationParam roleResRelationParam), roleResRelationParam = {}", roleResRelationParam);
        if (isNull(roleResRelationParam))
            throw new BlueException(EMPTY_PARAM);
        roleResRelationParam.asserts();

        AuthorityBaseOnRole authorityBaseOnRole = synchronizedProcessor.handleSupWithLock(AUTHORITY_UPDATE_SYNC.key, () ->
                roleResRelationService.updateAuthorityByRole(roleResRelationParam.getRoleId(),
                        roleResRelationParam.getResIds(), BLUE_ID.value));

        systemAuthorityInfosRefreshProducer.send(EMPTY_EVENT);

        return authorityBaseOnRole;
    }

    /**
     * add authority base on member / insert member-role-relations sync
     *
     * @param memberRoleRelationInsertOrDeleteParam
     * @return
     */
    @Override
    public AuthorityBaseOnRole insertAuthorityByMemberSync(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam) {
        LOGGER.info("AuthorityBaseOnRole insertAuthorityByMemberSync(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam), memberRoleRelationInsertOrDeleteParam = {}",
                memberRoleRelationInsertOrDeleteParam);
        if (isNull(memberRoleRelationInsertOrDeleteParam))
            throw new BlueException(EMPTY_PARAM);
        memberRoleRelationInsertOrDeleteParam.asserts();

        Long memberId = memberRoleRelationInsertOrDeleteParam.getMemberId();
        Long roleId = memberRoleRelationInsertOrDeleteParam.getRoleId();

        if (synchronizedProcessor.handleSupWithLock(MEMBER_ROLE_REL_UPDATE_KEY_WRAPPER.apply(memberId), () ->
                memberRoleRelationService.insertMemberRoleRelation(memberId, roleId, BLUE_ID.value)) < 1)
            throw new BlueException(DATA_NOT_EXIST);

        authService.refreshMemberRoleById(memberId, memberRoleRelationService.selectRoleIdsByMemberId(memberId)).toFuture().join();

        return roleResRelationService.getAuthorityMonoByRoleId(roleId).toFuture().join();
    }

    /**
     * update authority base on member / update member-role-relations sync with trans / not support for manager
     *
     * @param memberRoleRelationUpdateParam
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public List<AuthorityBaseOnRole> updateAuthoritiesByMemberSync(MemberRoleRelationUpdateParam memberRoleRelationUpdateParam) {
        LOGGER.info("AuthorityBaseOnRole updateAuthorityByMemberSync(MemberRoleRelationParam memberRoleRelationParam), memberRoleRelationParam = {}", memberRoleRelationUpdateParam);
        if (isNull(memberRoleRelationUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        memberRoleRelationUpdateParam.asserts();

        Long memberId = memberRoleRelationUpdateParam.getMemberId();
        List<Long> roleIds = memberRoleRelationUpdateParam.getRoleIds();

        if (synchronizedProcessor.handleSupWithLock(MEMBER_ROLE_REL_UPDATE_KEY_WRAPPER.apply(memberId), () ->
                memberRoleRelationService.updateMemberRoleRelations(memberId, roleIds, BLUE_ID.value)) < 1)
            throw new BlueException(DATA_NOT_EXIST);

        authService.refreshMemberRoleById(memberId, roleIds).toFuture().join();

        return roleResRelationService.selectAuthoritiesMonoByRoleIds(roleIds).toFuture().join();
    }

    /**
     * delete authority base on member / delete member-role-relations sync
     *
     * @param memberRoleRelationInsertOrDeleteParam
     * @return
     */
    @Override
    public AuthorityBaseOnRole deleteAuthorityByMemberSync(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam) {
        LOGGER.info("AuthorityBaseOnRole deleteAuthorityByMemberSync(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam), memberRoleRelationInsertOrDeleteParam = {}",
                memberRoleRelationInsertOrDeleteParam);
        if (isNull(memberRoleRelationInsertOrDeleteParam))
            throw new BlueException(EMPTY_PARAM);
        memberRoleRelationInsertOrDeleteParam.asserts();

        Long memberId = memberRoleRelationInsertOrDeleteParam.getMemberId();
        Long roleId = memberRoleRelationInsertOrDeleteParam.getRoleId();

        if (synchronizedProcessor.handleSupWithLock(MEMBER_ROLE_REL_UPDATE_KEY_WRAPPER.apply(memberId), () ->
                memberRoleRelationService.deleteMemberRoleRelation(memberId, roleId, BLUE_ID.value)) < 1)
            throw new BlueException(DATA_NOT_EXIST);

        authService.refreshMemberRoleById(memberId, memberRoleRelationService.selectRoleIdsByMemberId(memberId)).toFuture().join();

        return roleResRelationService.getAuthorityMonoByRoleId(roleId).toFuture().join();
    }

    /**
     * select security info mono by member id
     *
     * @param memberId
     * @param operatorId
     * @return
     */
    @Override
    public Mono<MemberSecurityInfo> selectSecurityInfoMonoByMemberId(Long memberId, Long operatorId) {
        LOGGER.info("Mono<List<SecurityQuestionInfo>> selectSecurityQuestionInfoMonoByMemberId(Long memberId, Long operatorId), memberId = {}, operatorId = {}", memberId, operatorId);
        if (isInvalidIdentity(memberId) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        MEMBER_ROLE_LEVEL_ASSERTER.accept(memberId, operatorId);

        return zip(rpcMemberBasicServiceConsumer.getMemberBasicInfoByPrimaryKey(memberId),
                credentialHistoryService.selectCredentialHistoryInfoMonoByMemberIdWithLimit(memberId),
                securityQuestionService.selectSecurityQuestionInfoMonoByMemberId(memberId)
        ).flatMap(tuple3 -> just(new MemberSecurityInfo(memberId, tuple3.getT1(), tuple3.getT2(), tuple3.getT3())));
    }

}
