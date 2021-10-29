package com.blue.secure.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.api.model.AuthorityBaseOnRole;
import com.blue.secure.model.AuthorityBaseOnResource;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.entity.Role;
import com.blue.secure.repository.entity.RoleResRelation;
import com.blue.secure.repository.mapper.RoleResRelationMapper;
import com.blue.secure.service.inter.ResourceService;
import com.blue.secure.service.inter.RoleResRelationService;
import com.blue.secure.service.inter.RoleService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.Asserter.*;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static com.blue.secure.converter.SecureModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER;
import static com.blue.secure.converter.SecureModelConverters.ROLE_2_ROLE_INFO_CONVERTER;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;


/**
 * role resource relation service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class RoleResRelationServiceImpl implements RoleResRelationService {

    private static final Logger LOGGER = getLogger(RoleResRelationServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final RoleResRelationMapper roleResRelationMapper;

    private final RoleService roleService;

    private final ResourceService resourceService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RoleResRelationServiceImpl(BlueIdentityProcessor blueIdentityProcessor, RoleResRelationMapper roleResRelationMapper, RoleService roleService, ResourceService resourceService) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.roleResRelationMapper = roleResRelationMapper;
        this.roleService = roleService;
        this.resourceService = resourceService;
    }

    /**
     * select all role resource relation
     *
     * @return
     */
    @Override
    public Mono<List<RoleResRelation>> selectRelation() {
        LOGGER.info("Mono<List<RoleResRelation>> selectRelation()");
        return just(roleResRelationMapper.select());
    }

    /**
     * select resources ids by role id
     *
     * @return
     */
    @Override
    public Mono<List<Long>> selectResIdsMonoByRoleId(Long roleId) {
        LOGGER.info("Mono<List<Long>> selectResIdsMonoByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return just(roleResRelationMapper.selectResIdsByRoleId(roleId));
    }

    /**
     * select resources by role id
     *
     * @param roleId
     * @return
     */
    @Override
    public Mono<List<Resource>> selectResMonoByRoleId(Long roleId) {
        LOGGER.info("Mono<List<Resource>> selectResMonoByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return this.selectResIdsMonoByRoleId(roleId)
                .flatMap(ids -> {
                    LOGGER.info("Mono<List<Resource>> selectResMonoByRoleId(Long roleId), ids = {}", ids);
                    return isValidIdentities(ids) ? resourceService.selectResourceMonoByIds(ids) : just(emptyList());
                });
    }

    /**
     * select role ids by resource id
     *
     * @param resId
     * @return
     */
    @Override
    public Mono<List<Long>> selectRoleIdsMonoByResId(Long resId) {
        LOGGER.info("Mono<List<Long>> selectRoleIdsMonoByResId(Long resId), resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return just(roleResRelationMapper.selectRoleIdsByResId(resId));
    }

    /**
     * select role by resource id
     *
     * @param resId
     * @return
     */
    @Override
    public Mono<List<Role>> selectRoleMonoByResId(Long resId) {
        LOGGER.info("Mono<List<Role>> selectRoleMonoByResId(Long resId), resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return this.selectRoleIdsMonoByResId(resId)
                .flatMap(ids -> {
                    LOGGER.info("Mono<List<Role>> selectRoleMonoByResId(Long resId), resId = {}, ids = {}", ids);
                    return isEmpty(ids) ? just(emptyList()) : roleService.selectRoleMonoByIds(ids);
                });
    }

    /**
     * select relation by role id
     *
     * @param roleId
     * @return
     */
    @Override
    public Mono<List<RoleResRelation>> selectRelationByRoleId(Long roleId) {
        LOGGER.info("Mono<List<RoleResRelation>> selectRelationByRoleId(Long roleId), roleId = {}", roleId);
        if (isInvalidIdentity(roleId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return just(roleResRelationMapper.selectByRoleId(roleId));
    }

    /**
     * select relation by resource id
     *
     * @param resId
     * @return
     */
    @Override
    public Mono<List<RoleResRelation>> selectRelationByResId(Long resId) {
        LOGGER.info("Mono<List<RoleResRelation>> selectRelationByResId(Long resId), resId = {}", resId);
        if (isInvalidIdentity(resId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return just(roleResRelationMapper.selectByResId(resId));
    }

    /**
     * select relation by ids
     *
     * @param roleIds
     * @return
     */
    @Override
    public Mono<List<RoleResRelation>> selectRelationByRoleIds(List<Long> roleIds) {
        LOGGER.info("Mono<List<RoleResRelation>> selectRelationByRoleIds(List<Long> roleIds), roleIds = {}", roleIds);

        return isValidIdentities(roleIds) ? just(allotByMax(roleIds, (int) DB_SELECT.value, false)
                .stream().map(roleResRelationMapper::selectByRoleIds)
                .flatMap(List::stream)
                .collect(toList()))
                :
                just(emptyList());
    }

    /**
     * select relation by resource ids
     *
     * @param resIds
     * @return
     */
    @Override
    public Mono<List<RoleResRelation>> selectRelationByResIds(List<Long> resIds) {
        LOGGER.info("Mono<List<RoleResRelation>> selectRelationByResIds(List<Long> resIds), resIds = {}", resIds);

        return isValidIdentities(resIds) ? just(allotByMax(resIds, (int) DB_SELECT.value, false)
                .stream().map(roleResRelationMapper::selectByResIds)
                .flatMap(List::stream)
                .collect(toList()))
                :
                just(emptyList());
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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return roleService.getRoleMonoById(roleId)
                .flatMap(roleOpt ->
                        roleOpt.map(role ->
                                        just(ROLE_2_ROLE_INFO_CONVERTER.apply(role)))
                                .orElseGet(() ->
                                        error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "role info doesn't exist, roleId = " + roleId)))
                ).flatMap(roleInfo ->
                        this.selectResIdsMonoByRoleId(roleId)
                                .flatMap(resourceService::selectResourceMonoByIds)
                                .flatMap(resources -> just(resources.stream().map(RESOURCE_2_RESOURCE_INFO_CONVERTER).collect(toList())))
                                .flatMap(resourceInfos -> just(new AuthorityBaseOnRole(roleInfo, resourceInfos)))
                );
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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return resourceService.getResourceMonoById(resId)
                .flatMap(resOpt ->
                        resOpt.map(res ->
                                        just(RESOURCE_2_RESOURCE_INFO_CONVERTER.apply(res)))
                                .orElseGet(() ->
                                        error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "res info doesn't exist, resId = " + resId)))
                ).flatMap(resourceInfo ->
                        this.selectRoleIdsMonoByResId(resId)
                                .flatMap(roleService::selectRoleMonoByIds)
                                .flatMap(roles -> just(roles.stream().map(ROLE_2_ROLE_INFO_CONVERTER).collect(toList())))
                                .flatMap(roleInfos -> just(new AuthorityBaseOnResource(resourceInfo, roleInfos)))
                );
    }

}
