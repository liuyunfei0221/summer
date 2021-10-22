package com.blue.secure.service.impl;

import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.entity.RoleResRelation;
import com.blue.secure.repository.mapper.RoleResRelationMapper;
import com.blue.secure.service.inter.ResourceService;
import com.blue.secure.service.inter.RoleResRelationService;
import com.blue.secure.service.inter.RoleService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;


/**
 * role resource relation service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
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
    public Mono<List<RoleResRelation>> selectRoleResRelation() {
        LOGGER.info("Mono<List<RoleResRelation>> selectRoleResRelation()");
        return just(roleResRelationMapper.select());
    }

    /**
     * select resources ids by role id
     *
     * @return
     */
    @Override
    public Mono<List<Long>> selectResourceIdsMonoByRoleId(Long roleId) {
        LOGGER.info("Mono<List<Long>> selectResourceIdsMonoByRoleId(Long roleId), roleId = {}", roleId);
        return just(roleResRelationMapper.selectResIdsByRoleId(roleId));
    }

    /**
     * select resources by role id
     *
     * @param roleId
     * @return
     */
    @Override
    public Mono<List<Resource>> selectResourceMonoByRoleId(Long roleId) {
        LOGGER.info("listResourceByRoleId(Long roleId), roleId = {}", roleId);
        return this.selectResourceIdsMonoByRoleId(roleId)
                .flatMap(ids -> {
                    LOGGER.info("Mono<List<Resource>> selectResourceMonoByRoleId(Long roleId), ids = {}", ids);
                    return isEmpty(ids) ? just(emptyList()) : resourceService.selectResourceMonoByIds(ids);
                });
    }

    /**
     * select relation by ids
     *
     * @param roleIds
     * @return
     */
    @Override
    public Mono<List<RoleResRelation>> selectRoleResRelationByRoleIds(List<Long> roleIds) {


        return null;
    }
}
