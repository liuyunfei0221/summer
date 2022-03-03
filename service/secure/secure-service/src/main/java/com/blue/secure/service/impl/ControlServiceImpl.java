package com.blue.secure.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.api.model.*;
import com.blue.secure.common.AccessEncoder;
import com.blue.secure.event.producer.SystemAuthorityInfosRefreshProducer;
import com.blue.secure.model.*;
import com.blue.secure.repository.entity.Credential;
import com.blue.secure.repository.entity.MemberRoleRelation;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.service.inter.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.common.base.ConstantProcessor.assertLoginType;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
import static com.blue.base.constant.base.SummerAttr.NON_VALUE_PARAM;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * config role,resource,relation
 *
 * @author liuyunfei
 * @date 2021/11/1
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class ControlServiceImpl implements ControlService {

    private static final Logger LOGGER = getLogger(ControlServiceImpl.class);

    private final SecureService secureService;

    private final RoleService roleService;

    private final CredentialService credentialService;

    private RoleResRelationService roleResRelationService;

    private final MemberRoleRelationService memberRoleRelationService;

    private final SystemAuthorityInfosRefreshProducer systemAuthorityInfosRefreshProducer;

    private final ExecutorService executorService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ControlServiceImpl(SecureService secureService, RoleService roleService, CredentialService credentialService, RoleResRelationService roleResRelationService,
                              MemberRoleRelationService memberRoleRelationService, SystemAuthorityInfosRefreshProducer systemAuthorityInfosRefreshProducer, ExecutorService executorService) {
        this.secureService = secureService;
        this.roleService = roleService;
        this.credentialService = credentialService;
        this.roleResRelationService = roleResRelationService;
        this.memberRoleRelationService = memberRoleRelationService;
        this.systemAuthorityInfosRefreshProducer = systemAuthorityInfosRefreshProducer;
        this.executorService = executorService;
    }

    private final Function<Long, MemberRoleRelation> MEMBER_ROLE_RELATION_GEN = memberId -> {
        if (isInvalidIdentity(memberId))
            throw new BlueException(MEMBER_ALREADY_HAS_A_ROLE);

        Role role = roleResRelationService.getDefaultRole();
        if (role == null)
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
        if (credentialInfo == null || isInvalidIdentity(memberId))
            throw new BlueException(EMPTY_PARAM);

        String type = credentialInfo.getType();
        assertLoginType(type, false);

        Credential credential = new Credential();

        credential.setCredential(credentialInfo.getCredential());
        credential.setType(type);

        credential.setAccess(ofNullable(credentialInfo.getAccess())
                .filter(BlueChecker::isNotBlank)
                .map(AccessEncoder::encryptAccess)
                .orElse(""));

        credential.setMemberId(memberId);
        credential.setExtra(credentialInfo.getExtra());
        credential.setStatus(VALID.status);

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

    /**
     * get authority base on role by role id
     *
     * @param roleId
     * @return
     */
    @Override
    public Mono<AuthorityBaseOnRole> selectAuthorityMonoByRoleId(Long roleId) {
        LOGGER.info("Mono<AuthorityBaseOnRole> getAuthorityMonoByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(INVALID_IDENTITY);

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
        LOGGER.info("Mono<AuthorityBaseOnResource> getAuthorityMonoByResId(Long resId), resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(INVALID_IDENTITY);

        return roleResRelationService.selectAuthorityMonoByResId(resId);
    }

    /**
     * refresh resource key/info or role-resource-relation
     *
     * @return
     */
    @Override
    public Mono<Void> refreshSystemAuthorityInfos() {
        LOGGER.info("Mono<Void> refreshSystemAuthorityInfos()");
        secureService.refreshSystemAuthorityInfos();

        return fromRunnable(secureService::refreshSystemAuthorityInfos).then();
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
    public int updateMemberRoleById(Long memberId, Long roleId, Long operatorId) {
        LOGGER.info("void updateMemberRoleById(Long memberId, Long roleId, Long operatorId), memberId = {}, roleId = {}", memberId, roleId);
        assertRoleLevelForOperate(getRoleByRoleId(roleId).getLevel(), getRoleByMemberId(operatorId).getLevel());
        assertRoleLevelForOperate(getRoleByMemberId(memberId).getLevel(), getRoleByMemberId(operatorId).getLevel());

        int i = memberRoleRelationService.updateMemberRoleRelation(memberId, roleId, operatorId);
        if (i > 0)
            secureService.refreshMemberRoleById(memberId, roleId, operatorId);

        return i;
    }

    /**
     * init secure infos for a new member
     *
     * @param memberCredentialInfo
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public void initMemberSecureInfo(MemberCredentialInfo memberCredentialInfo) {
        if (isNull(memberCredentialInfo))
            throw new BlueException(EMPTY_PARAM);

        Long memberId = memberCredentialInfo.getMemberId();
        List<CredentialInfo> credentials = memberCredentialInfo.getCredentials();
        if (isInvalidIdentity(memberId) || isEmpty(credentials))
            throw new BlueException(INVALID_PARAM);

        memberRoleRelationService.insertMemberRoleRelation(MEMBER_ROLE_RELATION_GEN.apply(memberId));

        credentialService.insertCredentials(credentials.stream()
                .map(c -> CREDENTIAL_INFO_AND_MEMBER_ID_2_CREDENTIAL_CONVERTER.apply(c, memberId))
                .collect(toList()));
    }

    /**
     * update default role by role id
     *
     * @param id
     * @param operatorId
     * @return
     */
    @Override
    public Mono<Void> updateDefaultRole(Long id, Long operatorId) {
        LOGGER.info("void updateDefaultRole(Long id, Long operatorId), id = {}, operatorId = {}", id, operatorId);
        assertRoleLevelForOperate(getRoleByRoleId(id).getLevel(), getRoleByMemberId(operatorId).getLevel());

        return fromRunnable(() -> roleResRelationService.updateDefaultRole(id, operatorId)).then();
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
        if (roleResRelationParam == null)
            throw new BlueException(EMPTY_PARAM);

        Long roleId = roleResRelationParam.getRoleId();
        List<Long> resIds = roleResRelationParam.getResIds();
        if (isInvalidIdentity(roleId) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);
        if (isEmpty(resIds))
            throw new BlueException(INVALID_IDENTITY.status, INVALID_IDENTITY.code, "invalid resIds");
        assertRoleLevelForOperate(getRoleByRoleId(roleId).getLevel(), getRoleByMemberId(operatorId).getLevel());

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
        if (memberRoleRelationParam == null)
            throw new BlueException(EMPTY_PARAM);

        Long memberId = memberRoleRelationParam.getMemberId();
        Long roleId = memberRoleRelationParam.getRoleId();
        if (isInvalidIdentity(memberId) || isInvalidIdentity(roleId) || isInvalidIdentity(operatorId))
            throw new BlueException(INVALID_IDENTITY);

        Integer operatorRoleLevel = getRoleByMemberId(operatorId).getLevel();
        assertRoleLevelForOperate(getRoleByMemberId(memberId).getLevel(), operatorRoleLevel);
        assertRoleLevelForOperate(getRoleByRoleId(roleId).getLevel(), operatorRoleLevel);

        return fromFuture(supplyAsync(() ->
                memberRoleRelationService.updateMemberRoleRelation(memberId, roleId, operatorId), executorService))
                .doOnSuccess(i -> {
                    if (i > 0)
                        secureService.refreshMemberRoleById(memberId, roleId, operatorId);
                })
                .flatMap(i -> this.selectAuthorityMonoByRoleId(roleId));
    }

}
