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
import com.blue.auth.service.inter.*;
import com.blue.base.common.base.BlueChecker;
import com.blue.base.common.base.ConstantProcessor;
import com.blue.base.constant.auth.CredentialType;
import com.blue.base.constant.auth.VerifyTypeAndCredentialTypesRelation;
import com.blue.base.constant.verify.VerifyType;
import com.blue.base.model.base.Access;
import com.blue.base.model.exps.BlueException;
import com.blue.jwt.common.JwtProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.redis.common.BlueLeakyBucketRateLimiter;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConstantProcessor.assertCredentialType;
import static com.blue.base.common.base.ConstantProcessor.getVerifyTypeByIdentity;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.getIpReact;
import static com.blue.base.constant.base.BlueNumericalValue.BLUE_ID;
import static com.blue.base.constant.base.RateLimitKeyPrefix.ACCESS_UPDATE_RATE_LIMIT_KEY_PRE;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.INVALID;
import static com.blue.base.constant.base.Status.VALID;
import static com.blue.base.constant.base.SummerAttr.NON_VALUE_PARAM;
import static com.blue.base.constant.verify.BusinessType.*;
import static com.blue.redis.api.generator.BlueRateLimiterGenerator.generateLeakyBucketRateLimiter;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.runAsync;
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
public class ControlServiceImpl implements ControlService {

    private static final Logger LOGGER = getLogger(ControlServiceImpl.class);

    private final RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer;

    private final RpcMemberAuthServiceConsumer rpcMemberAuthServiceConsumer;

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final JwtProcessor<MemberPayload> jwtProcessor;

    private final LoginService loginService;

    private final AuthService authService;

    private final RoleService roleService;

    private final CredentialService credentialService;

    private final CredentialHistoryService credentialHistoryService;

    private final SecurityQuestionService securityQuestionService;

    private RoleResRelationService roleResRelationService;

    private final MemberRoleRelationService memberRoleRelationService;

    private final SystemAuthorityInfosRefreshProducer systemAuthorityInfosRefreshProducer;

    private final ExecutorService executorService;

    private final BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ControlServiceImpl(RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer, RpcMemberAuthServiceConsumer rpcMemberAuthServiceConsumer, RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, JwtProcessor<MemberPayload> jwtProcessor,
                              LoginService loginService, AuthService authService, RoleService roleService, CredentialService credentialService, CredentialHistoryService credentialHistoryService, SecurityQuestionService securityQuestionService, RoleResRelationService roleResRelationService, MemberRoleRelationService memberRoleRelationService,
                              SystemAuthorityInfosRefreshProducer systemAuthorityInfosRefreshProducer, ExecutorService executorService, ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler, ControlDeploy controlDeploy) {
        this.rpcVerifyHandleServiceConsumer = rpcVerifyHandleServiceConsumer;
        this.rpcMemberAuthServiceConsumer = rpcMemberAuthServiceConsumer;
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.jwtProcessor = jwtProcessor;
        this.loginService = loginService;
        this.authService = authService;
        this.roleService = roleService;
        this.credentialService = credentialService;
        this.credentialHistoryService = credentialHistoryService;
        this.securityQuestionService = securityQuestionService;
        this.roleResRelationService = roleResRelationService;
        this.memberRoleRelationService = memberRoleRelationService;
        this.systemAuthorityInfosRefreshProducer = systemAuthorityInfosRefreshProducer;
        this.executorService = executorService;
        this.blueLeakyBucketRateLimiter = generateLeakyBucketRateLimiter(reactiveStringRedisTemplate, scheduler);

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

        Role role = roleResRelationService.getDefaultRole();
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
                .orElse(""));

        credential.setMemberId(memberId);
        credential.setExtra(credentialInfo.getExtra());
        credential.setStatus(credentialInfo.getStatus());

        Long stamp = TIME_STAMP_GETTER.get();
        credential.setCreateTime(stamp);
        credential.setUpdateTime(stamp);

        return credential;
    };

    private Role getRoleByRoleId(Long roleId) {
        return roleService.getRoleById(roleId).orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
    }

    private Role getRoleByMemberId(Long memberId) {
        return memberRoleRelationService.getRoleIdByMemberId(memberId)
                .map(roleService::getRoleById).filter(Optional::isPresent).map(Optional::get)
                .orElseThrow(() -> new BlueException(MEMBER_NOT_HAS_A_ROLE));
    }

    private void assertRoleLevelForOperate(int tarLevel, int operatorLevel) {
        if (tarLevel <= operatorLevel)
            throw new BlueException(FORBIDDEN);
    }

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
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "key can't be blank");

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
                    cre.setAccess("");
                    cre.setMemberId(memberId);
                    cre.setExtra("from add credential");
                    cre.setStatus(ALLOW_ACCESS_LT_SET.contains(type) ? INVALID.status : VALID.status);
                    cre.setCreateTime(stamp);
                    cre.setUpdateTime(stamp);

                    return cre;
                }).collect(toList());
    }

    /**
     * login
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return getIpReact(serverRequest)
                .flatMap(ip -> {
                    LOGGER.info("Mono<ServerResponse> login(ServerRequest serverRequest), ip = {}", ip);
                    return blueLeakyBucketRateLimiter.isAllowed(LIMIT_KEY_WRAPPER.apply(ip), ALLOW, SEND_INTERVAL_MILLIS);
                })
                .flatMap(allowed ->
                        allowed ?
                                loginService.login(serverRequest)
                                :
                                error(() -> new BlueException(TOO_MANY_REQUESTS)));
    }

    /**
     * refresh jwt by refresh token
     *
     * @param refresh
     * @return
     */
    @Override
    public Mono<MemberAccess> refreshAccess(String refresh) {
        LOGGER.info("Mono<MemberAccess> refreshAccess(String refresh), refresh = {}", refresh);
        return just(jwtProcessor.parse(refresh))
                .switchIfEmpty(error(() -> new BlueException(UNAUTHORIZED)))
                .flatMap(memberPayload ->
                        blueLeakyBucketRateLimiter.isAllowed(LIMIT_KEY_WRAPPER.apply(memberPayload.getKeyId()), ALLOW, SEND_INTERVAL_MILLIS)
                                .flatMap(allowed ->
                                        allowed ?
                                                authService.refreshAccessByMemberPayload(memberPayload)
                                                :
                                                error(() -> new BlueException(TOO_MANY_REQUESTS))));
    }

    /**
     * logout
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> logout(ServerRequest serverRequest) {
        return loginService.logout(serverRequest);
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
     * get authority base on role by role id
     *
     * @param roleId
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnRole> selectAuthorityMonoByRoleId(Long roleId) {
        return roleResRelationService.selectAuthorityMonoByRoleId(roleId);
    }

    /**
     * get authority base on resource by res id
     *
     * @param resId
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnResource> selectAuthorityMonoByResId(Long resId) {
        return roleResRelationService.selectAuthorityMonoByResId(resId);
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
     * update member role info by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public boolean updateMemberRoleById(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("void updateMemberRoleById(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}", memberId, roleId);
        assertRoleLevelForOperate(getRoleByRoleId(roleId).getLevel(), getRoleByMemberId(operatorId).getLevel());
        assertRoleLevelForOperate(getRoleByMemberId(memberId).getLevel(), getRoleByMemberId(operatorId).getLevel());

        boolean updated = memberRoleRelationService.updateMemberRoleRelation(memberId, roleId, operatorId) > 0;
        if (updated)
            authService.refreshMemberRoleById(memberId, roleId, operatorId).toFuture().join();

        return updated;
    }

    /**
     * init auth infos for a new member
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

        memberRoleRelationService.insertMemberRoleRelation(MEMBER_DEFAULT_ROLE_RELATION_GEN.apply(memberId));
        credentialService.insertCredentials(credentials);
    }

    /**
     * init auth infos for a new member
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

        List<String> types = CTS_BY_VT_GETTER.apply(ConstantProcessor.getVerifyTypeByIdentity(credentialSettingUpParam.getVerifyType()));

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
     * @param roleId
     * @param operatorId
     * @return
     */
    @Override
    public Mono<Boolean> refreshMemberRoleById(Long memberId, Long roleId, Long operatorId) {
        return authService.refreshMemberRoleById(memberId, roleId, operatorId);
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

        assertRoleLevelForOperate(getRoleByRoleId(id).getLevel(), getRoleByMemberId(operatorId).getLevel());
        return just(roleResRelationService.updateDefaultRole(id, operatorId));
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
     * update member sec key by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<String> updateSecKeyByAccess(Access access) {
        LOGGER.info("Mono<String> updateSecKeyByAccess(Access access), access = {}", access);
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
        return fromFuture(runAsync(() -> securityQuestionService.insertSecurityQuestion(securityQuestionInsertParam, memberId), executorService))
                .then(just(true));
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
        return fromFuture(runAsync(() -> securityQuestionService.insertSecurityQuestions(securityQuestionInsertParams, memberId), executorService))
                .then(just(true));
    }

    /**
     * get member's authority by access
     *
     * @param access
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnRole> getAuthorityMonoByAccess(Access access) {
        return authService.getAuthorityMonoByAccess(access);
    }

    /**
     * get member's authority by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnRole> getAuthorityMonoByMemberId(Long memberId) {
        return authService.getAuthorityMonoByMemberId(memberId);
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
        assertRoleLevelForOperate(ofNullable(roleInsertParam).map(RoleInsertParam::getLevel)
                .filter(l -> l > 0)
                .orElseThrow(() -> new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "level can't be null or less than 1")), getRoleByMemberId(operatorId).getLevel());

        return just(roleResRelationService.insertRole(roleInsertParam, operatorId))
                .doOnSuccess(ri -> {
                    LOGGER.info("ri = {}", ri);
                    systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
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
        assertRoleLevelForOperate(getRoleByRoleId(roleUpdateParam.getId()).getLevel(), getRoleByMemberId(operatorId).getLevel());

        return just(roleResRelationService.updateRole(roleUpdateParam, operatorId))
                .doOnSuccess(ri -> {
                    LOGGER.info("ri = {}", ri);
                    systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
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
        assertRoleLevelForOperate(getRoleByRoleId(id).getLevel(), getRoleByMemberId(operatorId).getLevel());

        return just(roleResRelationService.deleteRole(id, operatorId))
                .doOnSuccess(ri -> {
                    LOGGER.info("ri = {}", ri);
                    systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
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

        return just(roleResRelationService.insertResource(resourceInsertParam, operatorId))
                .doOnSuccess(ri -> {
                    LOGGER.info("ri = {}", ri);
                    systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
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

        return just(roleResRelationService.updateResource(resourceUpdateParam, operatorId))
                .doOnSuccess(ri -> {
                    LOGGER.info("ri = {}", ri);
                    systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
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

        return just(roleResRelationService.deleteResource(id, operatorId))
                .doOnSuccess(ri -> {
                    LOGGER.info("ri = {}", ri);
                    systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
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
        assertRoleLevelForOperate(getRoleByRoleId(roleResRelationParam.getRoleId()).getLevel(), getRoleByMemberId(operatorId).getLevel());

        return just(roleResRelationService.updateAuthorityByRole(roleResRelationParam.getRoleId(), roleResRelationParam.getResIds(), operatorId))
                .doOnSuccess(auth -> {
                    LOGGER.info("auth = {}", auth);
                    systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);
                });
    }

    /**
     * update authority base on member / update member-role-relations
     *
     * @param memberRoleRelationParam
     * @param operatorId
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnRole> updateAuthorityByMember(MemberRoleRelationParam memberRoleRelationParam, Long operatorId) {
        LOGGER.info("Mono<AuthorityBaseOnRole> updateAuthorityByMember(MemberRoleRelationParam memberRoleRelationParam, Long operatorId), memberRoleRelationParam = {}, operatorId = {}", memberRoleRelationParam, operatorId);
        if (isNull(memberRoleRelationParam))
            throw new BlueException(EMPTY_PARAM);
        memberRoleRelationParam.asserts();

        Long memberId = memberRoleRelationParam.getMemberId();
        Long roleId = memberRoleRelationParam.getRoleId();

        Integer operatorRoleLevel = getRoleByMemberId(operatorId).getLevel();
        assertRoleLevelForOperate(getRoleByMemberId(memberId).getLevel(), operatorRoleLevel);
        assertRoleLevelForOperate(getRoleByRoleId(roleId).getLevel(), operatorRoleLevel);

        return fromFuture(supplyAsync(() ->
                memberRoleRelationService.updateMemberRoleRelation(memberId, roleId, operatorId), executorService))
                .flatMap(ig -> authService.refreshMemberRoleById(memberId, roleId, operatorId))
                .flatMap(ig -> this.selectAuthorityMonoByRoleId(roleId));
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

        AuthorityBaseOnRole authorityBaseOnRole = roleResRelationService.updateAuthorityByRole(roleResRelationParam.getRoleId(), roleResRelationParam.getResIds(), BLUE_ID.value);
        systemAuthorityInfosRefreshProducer.send(NON_VALUE_PARAM);

        return authorityBaseOnRole;
    }

    /**
     * update authority base on member / update member-role-relations sync with trans / not support for manager
     *
     * @param memberRoleRelationParam
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public void updateAuthorityByMemberSync(MemberRoleRelationParam memberRoleRelationParam) {
        LOGGER.info("AuthorityBaseOnRole updateAuthorityByMemberSync(MemberRoleRelationParam memberRoleRelationParam), memberRoleRelationParam = {}", memberRoleRelationParam);
        if (isNull(memberRoleRelationParam))
            throw new BlueException(EMPTY_PARAM);
        memberRoleRelationParam.asserts();

        Long memberId = memberRoleRelationParam.getMemberId();
        Long roleId = memberRoleRelationParam.getRoleId();

        if (memberRoleRelationService.updateMemberRoleRelation(memberId, roleId, BLUE_ID.value) < 1)
            throw new BlueException(DATA_NOT_EXIST);

        authService.refreshMemberRoleById(memberId, roleId, BLUE_ID.value).toFuture().join();
    }

    /**
     * invalid member auth by member id
     *
     * @param memberId
     * @param operatorId
     * @return
     */
    @Override
    public Mono<Boolean> invalidateAuthByMember(Long memberId, Long operatorId) {
        LOGGER.info("Mono<Boolean> invalidateAuthByMember(Long memberId, Long operatorId), memberId = {}, operatorId = {}", memberId, operatorId);
        if (isInvalidIdentity(memberId) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);
        assertRoleLevelForOperate(getRoleByMemberId(memberId).getLevel(), getRoleByMemberId(operatorId).getLevel());

        return authService.invalidateAuthByMemberId(memberId);
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
        assertRoleLevelForOperate(getRoleByMemberId(memberId).getLevel(), getRoleByMemberId(operatorId).getLevel());

        return zip(rpcMemberBasicServiceConsumer.selectMemberBasicInfoMonoByPrimaryKey(memberId),
                credentialHistoryService.selectCredentialHistoryInfoMonoByMemberIdWithLimit(memberId),
                securityQuestionService.selectSecurityQuestionInfoMonoByMemberId(memberId)
        ).flatMap(tuple3 -> just(new MemberSecurityInfo(memberId, tuple3.getT1().getName(), tuple3.getT2(), tuple3.getT3())));
    }

}
