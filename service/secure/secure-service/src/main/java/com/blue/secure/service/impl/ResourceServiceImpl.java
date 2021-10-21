package com.blue.secure.service.impl;

import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.secure.repository.entity.Resource;
import com.blue.secure.repository.mapper.ResourceMapper;
import com.blue.secure.service.inter.ResourceService;
import com.blue.secure.service.inter.RoleResRelationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;


/**
 * resource service interface impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class ResourceServiceImpl implements ResourceService {

    private static final Logger LOGGER = getLogger(ResourceServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final ResourceMapper resourceMapper;

    private final RoleResRelationService roleResRelationService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public ResourceServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ResourceMapper resourceMapper, RoleResRelationService roleResRelationService) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.resourceMapper = resourceMapper;
        this.roleResRelationService = roleResRelationService;
    }

    /**
     * select all resources
     *
     * @return
     */
    @Override
    public List<Resource> selectResource() {
        LOGGER.info("List<Resource> selectResource()");
        return resourceMapper.selectResource();
    }

    /**
     * select resources by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<Resource>> selectResourceMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<Resource>> selectResourceMonoByIds(List<Long> ids), ids = {}", ids);
        return just(resourceMapper.selectResourceByIds(ids));
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

        return roleResRelationService.selectResourceIdsMonoByRoleId(roleId)
                .flatMap(ids -> {
                    LOGGER.info("Mono<List<Resource>> selectResourceMonoByRoleId(Long roleId), ids = {}", ids);
                    return isEmpty(ids) ? just(emptyList()) : just(resourceMapper.selectResourceByIds(ids));
                });
    }
}
